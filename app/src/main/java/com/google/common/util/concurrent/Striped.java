package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.math.IntMath;
import defpackage.C1684bz;
import defpackage.C4852yc;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.jvm.internal.IntCompanionObject;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public abstract class Striped<L> {
    private static final int ALL_SET = -1;
    private static final int LARGE_LAZY_CUTOFF = 1024;

    public static final class CompactStriped<L> extends PowerOfTwoStriped<L> {
        private final Object[] array;

        @Override // com.google.common.util.concurrent.Striped
        public L getAt(int i) {
            return (L) this.array[i];
        }

        @Override // com.google.common.util.concurrent.Striped
        public int size() {
            return this.array.length;
        }

        private CompactStriped(int i, Supplier<L> supplier) {
            super(i);
            int i2 = 0;
            Preconditions.checkArgument(i <= 1073741824, "Stripes must be <= 2^30)");
            this.array = new Object[this.mask + 1];
            while (true) {
                Object[] objArr = this.array;
                if (i2 >= objArr.length) {
                    return;
                }
                objArr[i2] = supplier.get();
                i2++;
            }
        }
    }

    @VisibleForTesting
    public static final class LargeLazyStriped<L> extends PowerOfTwoStriped<L> {
        final ConcurrentMap<Integer, L> locks;
        final int size;
        final Supplier<L> supplier;

        public LargeLazyStriped(int i, Supplier<L> supplier) {
            super(i);
            int i2 = this.mask;
            this.size = i2 == -1 ? IntCompanionObject.MAX_VALUE : i2 + 1;
            this.supplier = supplier;
            this.locks = new MapMaker().weakValues().makeMap();
        }

        @Override // com.google.common.util.concurrent.Striped
        public L getAt(int i) {
            if (this.size != Integer.MAX_VALUE) {
                Preconditions.checkElementIndex(i, size());
            }
            L l = this.locks.get(Integer.valueOf(i));
            if (l != null) {
                return l;
            }
            L l2 = this.supplier.get();
            return (L) MoreObjects.firstNonNull(this.locks.putIfAbsent(Integer.valueOf(i), l2), l2);
        }

        @Override // com.google.common.util.concurrent.Striped
        public int size() {
            return this.size;
        }
    }

    public static final class PaddedLock extends ReentrantLock {
        long unused1;
        long unused2;
        long unused3;

        public PaddedLock() {
            super(false);
        }
    }

    public static final class PaddedSemaphore extends Semaphore {
        long unused1;
        long unused2;
        long unused3;

        public PaddedSemaphore(int i) {
            super(i, false);
        }
    }

    public static abstract class PowerOfTwoStriped<L> extends Striped<L> {
        final int mask;

        public PowerOfTwoStriped(int i) {
            super();
            Preconditions.checkArgument(i > 0, "Stripes must be positive");
            this.mask = i > 1073741824 ? -1 : Striped.ceilToPowerOfTwo(i) - 1;
        }

        @Override // com.google.common.util.concurrent.Striped
        public final L get(Object obj) {
            return getAt(indexFor(obj));
        }

        @Override // com.google.common.util.concurrent.Striped
        public final int indexFor(Object obj) {
            return Striped.smear(obj.hashCode()) & this.mask;
        }
    }

    @VisibleForTesting
    public static final class SmallLazyStriped<L> extends PowerOfTwoStriped<L> {
        final AtomicReferenceArray<ArrayReference<? extends L>> locks;
        final ReferenceQueue<L> queue;
        final int size;
        final Supplier<L> supplier;

        public static final class ArrayReference<L> extends WeakReference<L> {
            final int index;

            public ArrayReference(L l, int i, ReferenceQueue<L> referenceQueue) {
                super(l, referenceQueue);
                this.index = i;
            }
        }

        public SmallLazyStriped(int i, Supplier<L> supplier) {
            super(i);
            this.queue = new ReferenceQueue<>();
            int i2 = this.mask;
            int i3 = i2 == -1 ? IntCompanionObject.MAX_VALUE : i2 + 1;
            this.size = i3;
            this.locks = new AtomicReferenceArray<>(i3);
            this.supplier = supplier;
        }

        private void drainQueue() {
            while (true) {
                Reference<? extends L> referencePoll = this.queue.poll();
                if (referencePoll == null) {
                    return;
                }
                ArrayReference<? extends L> arrayReference = (ArrayReference) referencePoll;
                AtomicReferenceArray<ArrayReference<? extends L>> atomicReferenceArray = this.locks;
                int i = arrayReference.index;
                while (!atomicReferenceArray.compareAndSet(i, arrayReference, null) && atomicReferenceArray.get(i) == arrayReference) {
                }
            }
        }

        @Override // com.google.common.util.concurrent.Striped
        public L getAt(int i) {
            L l;
            if (this.size != Integer.MAX_VALUE) {
                Preconditions.checkElementIndex(i, size());
            }
            ArrayReference<? extends L> arrayReference = this.locks.get(i);
            L l2 = arrayReference == null ? null : arrayReference.get();
            if (l2 != null) {
                return l2;
            }
            L l3 = this.supplier.get();
            ArrayReference<? extends L> arrayReference2 = new ArrayReference<>(l3, i, this.queue);
            do {
                AtomicReferenceArray<ArrayReference<? extends L>> atomicReferenceArray = this.locks;
                while (!atomicReferenceArray.compareAndSet(i, arrayReference, arrayReference2)) {
                    if (atomicReferenceArray.get(i) != arrayReference) {
                        arrayReference = this.locks.get(i);
                        l = arrayReference == null ? null : arrayReference.get();
                    }
                }
                drainQueue();
                return l3;
            } while (l == null);
            return l;
        }

        @Override // com.google.common.util.concurrent.Striped
        public int size() {
            return this.size;
        }
    }

    public static final class WeakSafeCondition extends ForwardingCondition {
        private final Condition delegate;
        private final WeakSafeReadWriteLock strongReference;

        public WeakSafeCondition(Condition condition, WeakSafeReadWriteLock weakSafeReadWriteLock) {
            this.delegate = condition;
            this.strongReference = weakSafeReadWriteLock;
        }

        @Override // com.google.common.util.concurrent.ForwardingCondition
        public Condition delegate() {
            return this.delegate;
        }
    }

    public static final class WeakSafeLock extends ForwardingLock {
        private final Lock delegate;
        private final WeakSafeReadWriteLock strongReference;

        public WeakSafeLock(Lock lock, WeakSafeReadWriteLock weakSafeReadWriteLock) {
            this.delegate = lock;
            this.strongReference = weakSafeReadWriteLock;
        }

        @Override // com.google.common.util.concurrent.ForwardingLock
        public Lock delegate() {
            return this.delegate;
        }

        @Override // com.google.common.util.concurrent.ForwardingLock, java.util.concurrent.locks.Lock
        public Condition newCondition() {
            return new WeakSafeCondition(this.delegate.newCondition(), this.strongReference);
        }
    }

    public static final class WeakSafeReadWriteLock implements ReadWriteLock {
        private final ReadWriteLock delegate = new ReentrantReadWriteLock();

        @Override // java.util.concurrent.locks.ReadWriteLock
        public Lock readLock() {
            return new WeakSafeLock(this.delegate.readLock(), this);
        }

        @Override // java.util.concurrent.locks.ReadWriteLock
        public Lock writeLock() {
            return new WeakSafeLock(this.delegate.writeLock(), this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int ceilToPowerOfTwo(int i) {
        return 1 << IntMath.log2(i, RoundingMode.CEILING);
    }

    public static <L> Striped<L> custom(int i, Supplier<L> supplier) {
        return new CompactStriped(i, supplier);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Lock lambda$lazyWeakLock$0() {
        return new ReentrantLock(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Semaphore lambda$lazyWeakSemaphore$0(int i) {
        return new Semaphore(i, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Semaphore lambda$semaphore$0(int i) {
        return new PaddedSemaphore(i);
    }

    public static <L> Striped<L> lazyWeakCustom(int i, Supplier<L> supplier) {
        return i < LARGE_LAZY_CUTOFF ? new SmallLazyStriped(i, supplier) : new LargeLazyStriped(i, supplier);
    }

    public static Striped<Lock> lazyWeakLock(int i) {
        return lazyWeakCustom(i, new C1684bz(3));
    }

    public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int i) {
        return lazyWeakCustom(i, new k(0));
    }

    public static Striped<Semaphore> lazyWeakSemaphore(int i, int i2) {
        return lazyWeakCustom(i, new C4852yc(i2, 3));
    }

    public static Striped<Lock> lock(int i) {
        return custom(i, new k(1));
    }

    public static Striped<ReadWriteLock> readWriteLock(int i) {
        return custom(i, new C1684bz(4));
    }

    public static Striped<Semaphore> semaphore(int i, int i2) {
        return custom(i, new C4852yc(i2, 2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int smear(int i) {
        int i2 = i ^ ((i >>> 20) ^ (i >>> 12));
        return (i2 >>> 4) ^ ((i2 >>> 7) ^ i2);
    }

    public Iterable<L> bulkGet(Iterable<? extends Object> iterable) {
        ArrayList arrayListNewArrayList = Lists.newArrayList(iterable);
        if (arrayListNewArrayList.isEmpty()) {
            return ImmutableList.of();
        }
        int[] iArr = new int[arrayListNewArrayList.size()];
        for (int i = 0; i < arrayListNewArrayList.size(); i++) {
            iArr[i] = indexFor(arrayListNewArrayList.get(i));
        }
        Arrays.sort(iArr);
        int i2 = iArr[0];
        arrayListNewArrayList.set(0, getAt(i2));
        for (int i3 = 1; i3 < arrayListNewArrayList.size(); i3++) {
            int i4 = iArr[i3];
            if (i4 == i2) {
                arrayListNewArrayList.set(i3, arrayListNewArrayList.get(i3 - 1));
            } else {
                arrayListNewArrayList.set(i3, getAt(i4));
                i2 = i4;
            }
        }
        return Collections.unmodifiableList(arrayListNewArrayList);
    }

    public abstract L get(Object obj);

    public abstract L getAt(int i);

    public abstract int indexFor(Object obj);

    public abstract int size();

    private Striped() {
    }
}
