package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractFuture;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
abstract class AggregateFutureState<OutputT> extends AbstractFuture.TrustedFuture<OutputT> {
    private static final AtomicHelper ATOMIC_HELPER;
    private static final LazyLogger log = new LazyLogger(AggregateFutureState.class);
    volatile int remainingField;
    volatile Set<Throwable> seenExceptionsField = null;

    public static abstract class AtomicHelper {
        private AtomicHelper() {
        }

        public abstract String atomicHelperTypeForTest();

        public abstract void compareAndSetSeenExceptions(AggregateFutureState<?> aggregateFutureState, Set<Throwable> set, Set<Throwable> set2);

        public abstract int decrementAndGetRemainingCount(AggregateFutureState<?> aggregateFutureState);
    }

    public static final class SafeAtomicHelper extends AtomicHelper {
        private static final AtomicReferenceFieldUpdater<? super AggregateFutureState<?>, ? super Set<Throwable>> seenExceptionsUpdater = AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptionsField");
        private static final AtomicIntegerFieldUpdater<? super AggregateFutureState<?>> remainingCountUpdater = AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remainingField");

        private SafeAtomicHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        public String atomicHelperTypeForTest() {
            return "SafeAtomicHelper";
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        public void compareAndSetSeenExceptions(AggregateFutureState<?> aggregateFutureState, Set<Throwable> set, Set<Throwable> set2) {
            AtomicReferenceFieldUpdater<? super AggregateFutureState<?>, ? super Set<Throwable>> atomicReferenceFieldUpdater = seenExceptionsUpdater;
            while (!atomicReferenceFieldUpdater.compareAndSet(aggregateFutureState, set, set2) && atomicReferenceFieldUpdater.get(aggregateFutureState) == set) {
            }
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        public int decrementAndGetRemainingCount(AggregateFutureState<?> aggregateFutureState) {
            return remainingCountUpdater.decrementAndGet(aggregateFutureState);
        }
    }

    public static final class SynchronizedAtomicHelper extends AtomicHelper {
        private SynchronizedAtomicHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        public String atomicHelperTypeForTest() {
            return "SynchronizedAtomicHelper";
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        public void compareAndSetSeenExceptions(AggregateFutureState<?> aggregateFutureState, Set<Throwable> set, Set<Throwable> set2) {
            synchronized (aggregateFutureState) {
                try {
                    if (aggregateFutureState.seenExceptionsField == set) {
                        aggregateFutureState.seenExceptionsField = set2;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // com.google.common.util.concurrent.AggregateFutureState.AtomicHelper
        public int decrementAndGetRemainingCount(AggregateFutureState<?> aggregateFutureState) {
            int i;
            synchronized (aggregateFutureState) {
                i = aggregateFutureState.remainingField - 1;
                aggregateFutureState.remainingField = i;
            }
            return i;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static {
        AtomicHelper safeAtomicHelper;
        Throwable th = null;
        Object[] objArr = 0;
        try {
            safeAtomicHelper = new SafeAtomicHelper();
        } catch (Throwable th2) {
            SynchronizedAtomicHelper synchronizedAtomicHelper = new SynchronizedAtomicHelper();
            th = th2;
            safeAtomicHelper = synchronizedAtomicHelper;
        }
        ATOMIC_HELPER = safeAtomicHelper;
        if (th != null) {
            log.get().log(Level.SEVERE, "SafeAtomicHelper is broken!", th);
        }
    }

    public AggregateFutureState(int i) {
        this.remainingField = i;
    }

    @VisibleForTesting
    public static String atomicHelperTypeForTest() {
        return ATOMIC_HELPER.atomicHelperTypeForTest();
    }

    public abstract void addInitialException(Set<Throwable> set);

    public final void clearSeenExceptions() {
        this.seenExceptionsField = null;
    }

    public final int decrementRemainingAndGet() {
        return ATOMIC_HELPER.decrementAndGetRemainingCount(this);
    }

    public final Set<Throwable> getOrInitSeenExceptions() {
        Set<Throwable> set = this.seenExceptionsField;
        if (set != null) {
            return set;
        }
        Set<Throwable> setNewConcurrentHashSet = Sets.newConcurrentHashSet();
        addInitialException(setNewConcurrentHashSet);
        ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, setNewConcurrentHashSet);
        Set<Throwable> set2 = this.seenExceptionsField;
        Objects.requireNonNull(set2);
        return set2;
    }
}
