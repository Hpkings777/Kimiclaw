package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import defpackage.AbstractC2039eV;
import defpackage.AbstractC4671xI0;
import defpackage.O;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
abstract class AbstractFutureState<V> extends InternalFutureFailureAccess implements ListenableFuture<V> {
    private static final AtomicHelper ATOMIC_HELPER;
    static final boolean GENERATE_CANCELLATION_CAUSES;
    private static final long SPIN_THRESHOLD_NANOS = 1000;
    volatile AbstractFuture.Listener listenersField;
    volatile Object valueField;
    volatile Waiter waitersField;
    static final Object NULL = new Object();
    static final LazyLogger log = new LazyLogger(AbstractFuture.class);

    public static abstract class AtomicHelper {
        private AtomicHelper() {
        }

        public abstract String atomicHelperTypeForTest();

        public abstract boolean casListeners(AbstractFutureState<?> abstractFutureState, AbstractFuture.Listener listener, AbstractFuture.Listener listener2);

        public abstract boolean casValue(AbstractFutureState<?> abstractFutureState, Object obj, Object obj2);

        public abstract boolean casWaiters(AbstractFutureState<?> abstractFutureState, Waiter waiter, Waiter waiter2);

        public abstract AbstractFuture.Listener gasListeners(AbstractFutureState<?> abstractFutureState, AbstractFuture.Listener listener);

        public abstract Waiter gasWaiters(AbstractFutureState<?> abstractFutureState, Waiter waiter);

        public abstract void putNext(Waiter waiter, Waiter waiter2);

        public abstract void putThread(Waiter waiter, Thread thread);
    }

    public static final class AtomicReferenceFieldUpdaterAtomicHelper extends AtomicHelper {
        private static final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater = AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread");
        private static final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater = AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next");
        private static final AtomicReferenceFieldUpdater<? super AbstractFutureState<?>, Waiter> waitersUpdater = AtomicReferenceFieldUpdater.newUpdater(AbstractFutureState.class, Waiter.class, "waitersField");
        private static final AtomicReferenceFieldUpdater<? super AbstractFutureState<?>, AbstractFuture.Listener> listenersUpdater = AtomicReferenceFieldUpdater.newUpdater(AbstractFutureState.class, AbstractFuture.Listener.class, "listenersField");
        private static final AtomicReferenceFieldUpdater<? super AbstractFutureState<?>, Object> valueUpdater = AtomicReferenceFieldUpdater.newUpdater(AbstractFutureState.class, Object.class, "valueField");

        private AtomicReferenceFieldUpdaterAtomicHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public String atomicHelperTypeForTest() {
            return "AtomicReferenceFieldUpdaterAtomicHelper";
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public boolean casListeners(AbstractFutureState<?> abstractFutureState, AbstractFuture.Listener listener, AbstractFuture.Listener listener2) {
            AtomicReferenceFieldUpdater<? super AbstractFutureState<?>, AbstractFuture.Listener> atomicReferenceFieldUpdater = listenersUpdater;
            while (!atomicReferenceFieldUpdater.compareAndSet(abstractFutureState, listener, listener2)) {
                if (atomicReferenceFieldUpdater.get(abstractFutureState) != listener) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public boolean casValue(AbstractFutureState<?> abstractFutureState, Object obj, Object obj2) {
            AtomicReferenceFieldUpdater<? super AbstractFutureState<?>, Object> atomicReferenceFieldUpdater = valueUpdater;
            while (!atomicReferenceFieldUpdater.compareAndSet(abstractFutureState, obj, obj2)) {
                if (atomicReferenceFieldUpdater.get(abstractFutureState) != obj) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public boolean casWaiters(AbstractFutureState<?> abstractFutureState, Waiter waiter, Waiter waiter2) {
            AtomicReferenceFieldUpdater<? super AbstractFutureState<?>, Waiter> atomicReferenceFieldUpdater = waitersUpdater;
            while (!atomicReferenceFieldUpdater.compareAndSet(abstractFutureState, waiter, waiter2)) {
                if (atomicReferenceFieldUpdater.get(abstractFutureState) != waiter) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public AbstractFuture.Listener gasListeners(AbstractFutureState<?> abstractFutureState, AbstractFuture.Listener listener) {
            return listenersUpdater.getAndSet(abstractFutureState, listener);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public Waiter gasWaiters(AbstractFutureState<?> abstractFutureState, Waiter waiter) {
            return waitersUpdater.getAndSet(abstractFutureState, waiter);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public void putNext(Waiter waiter, Waiter waiter2) {
            waiterNextUpdater.lazySet(waiter, waiter2);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public void putThread(Waiter waiter, Thread thread) {
            waiterThreadUpdater.lazySet(waiter, thread);
        }
    }

    public static final class SynchronizedHelper extends AtomicHelper {
        private SynchronizedHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public String atomicHelperTypeForTest() {
            return "SynchronizedHelper";
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public boolean casListeners(AbstractFutureState<?> abstractFutureState, AbstractFuture.Listener listener, AbstractFuture.Listener listener2) {
            synchronized (abstractFutureState) {
                try {
                    if (abstractFutureState.listenersField != listener) {
                        return false;
                    }
                    abstractFutureState.listenersField = listener2;
                    return true;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public boolean casValue(AbstractFutureState<?> abstractFutureState, Object obj, Object obj2) {
            synchronized (abstractFutureState) {
                try {
                    if (abstractFutureState.valueField != obj) {
                        return false;
                    }
                    abstractFutureState.valueField = obj2;
                    return true;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public boolean casWaiters(AbstractFutureState<?> abstractFutureState, Waiter waiter, Waiter waiter2) {
            synchronized (abstractFutureState) {
                try {
                    if (abstractFutureState.waitersField != waiter) {
                        return false;
                    }
                    abstractFutureState.waitersField = waiter2;
                    return true;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public AbstractFuture.Listener gasListeners(AbstractFutureState<?> abstractFutureState, AbstractFuture.Listener listener) {
            AbstractFuture.Listener listener2;
            synchronized (abstractFutureState) {
                try {
                    listener2 = abstractFutureState.listenersField;
                    if (listener2 != listener) {
                        abstractFutureState.listenersField = listener;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return listener2;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public Waiter gasWaiters(AbstractFutureState<?> abstractFutureState, Waiter waiter) {
            Waiter waiter2;
            synchronized (abstractFutureState) {
                try {
                    waiter2 = abstractFutureState.waitersField;
                    if (waiter2 != waiter) {
                        abstractFutureState.waitersField = waiter;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return waiter2;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public void putNext(Waiter waiter, Waiter waiter2) {
            waiter.next = waiter2;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public void putThread(Waiter waiter, Thread thread) {
            waiter.thread = thread;
        }
    }

    public static final class UnsafeAtomicHelper extends AtomicHelper {
        static final long LISTENERS_OFFSET;
        static final Unsafe UNSAFE;
        static final long VALUE_OFFSET;
        static final long WAITERS_OFFSET;
        static final long WAITER_NEXT_OFFSET;
        static final long WAITER_THREAD_OFFSET;

        static {
            Unsafe unsafe;
            try {
                try {
                    unsafe = Unsafe.getUnsafe();
                } catch (PrivilegedActionException e) {
                    throw new RuntimeException("Could not initialize intrinsics", e.getCause());
                }
            } catch (SecurityException unused) {
                unsafe = (Unsafe) AccessController.doPrivileged(new c());
            }
            try {
                WAITERS_OFFSET = unsafe.objectFieldOffset(AbstractFutureState.class.getDeclaredField("waitersField"));
                LISTENERS_OFFSET = unsafe.objectFieldOffset(AbstractFutureState.class.getDeclaredField("listenersField"));
                VALUE_OFFSET = unsafe.objectFieldOffset(AbstractFutureState.class.getDeclaredField("valueField"));
                WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("thread"));
                WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("next"));
                UNSAFE = unsafe;
            } catch (NoSuchFieldException e2) {
                throw new RuntimeException(e2);
            }
        }

        private UnsafeAtomicHelper() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Unsafe lambda$static$0() throws Exception {
            for (Field field : Unsafe.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object obj = field.get(null);
                if (Unsafe.class.isInstance(obj)) {
                    return (Unsafe) Unsafe.class.cast(obj);
                }
            }
            throw new NoSuchFieldError("the Unsafe");
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public String atomicHelperTypeForTest() {
            return "UnsafeAtomicHelper";
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public boolean casListeners(AbstractFutureState<?> abstractFutureState, AbstractFuture.Listener listener, AbstractFuture.Listener listener2) {
            return O.a(UNSAFE, abstractFutureState, LISTENERS_OFFSET, listener, listener2);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public boolean casValue(AbstractFutureState<?> abstractFutureState, Object obj, Object obj2) {
            return O.a(UNSAFE, abstractFutureState, VALUE_OFFSET, obj, obj2);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public boolean casWaiters(AbstractFutureState<?> abstractFutureState, Waiter waiter, Waiter waiter2) {
            return O.a(UNSAFE, abstractFutureState, WAITERS_OFFSET, waiter, waiter2);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public AbstractFuture.Listener gasListeners(AbstractFutureState<?> abstractFutureState, AbstractFuture.Listener listener) {
            AbstractFuture.Listener listener2;
            do {
                listener2 = abstractFutureState.listenersField;
                if (listener == listener2) {
                    break;
                }
            } while (!casListeners(abstractFutureState, listener2, listener));
            return listener2;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public Waiter gasWaiters(AbstractFutureState<?> abstractFutureState, Waiter waiter) {
            Waiter waiter2;
            do {
                waiter2 = abstractFutureState.waitersField;
                if (waiter == waiter2) {
                    break;
                }
            } while (!casWaiters(abstractFutureState, waiter2, waiter));
            return waiter2;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public void putNext(Waiter waiter, Waiter waiter2) {
            UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, waiter2);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        public void putThread(Waiter waiter, Thread thread) {
            UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, thread);
        }
    }

    public static final class Waiter {
        static final Waiter TOMBSTONE = new Waiter(false);
        volatile Waiter next;
        volatile Thread thread;

        public Waiter(boolean z) {
        }

        public void setNext(Waiter waiter) {
            AbstractFutureState.putNext(this, waiter);
        }

        public void unpark() {
            Thread thread = this.thread;
            if (thread != null) {
                this.thread = null;
                LockSupport.unpark(thread);
            }
        }

        public Waiter() {
            AbstractFutureState.putThread(this, Thread.currentThread());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.util.logging.Logger] */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.common.util.concurrent.AbstractFutureState$1] */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v6 */
    static {
        boolean z;
        AtomicHelper atomicReferenceFieldUpdaterAtomicHelper;
        Throwable th;
        AtomicHelper unsafeAtomicHelper;
        try {
            z = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
        } catch (SecurityException unused) {
            z = false;
        }
        GENERATE_CANCELLATION_CAUSES = z;
        ?? r1 = 0;
        ?? r12 = 0;
        if (mightBeAndroid()) {
            try {
                unsafeAtomicHelper = new UnsafeAtomicHelper();
            } catch (Error | Exception e) {
                try {
                    atomicReferenceFieldUpdaterAtomicHelper = new AtomicReferenceFieldUpdaterAtomicHelper();
                } catch (Error | Exception e2) {
                    SynchronizedHelper synchronizedHelper = new SynchronizedHelper();
                    r1 = e2;
                    atomicReferenceFieldUpdaterAtomicHelper = synchronizedHelper;
                }
                AtomicHelper atomicHelper = atomicReferenceFieldUpdaterAtomicHelper;
                th = e;
                unsafeAtomicHelper = atomicHelper;
                r12 = r1;
            }
        } else {
            try {
                unsafeAtomicHelper = new AtomicReferenceFieldUpdaterAtomicHelper();
            } catch (NoClassDefFoundError unused2) {
                unsafeAtomicHelper = new SynchronizedHelper();
            }
        }
        th = null;
        ATOMIC_HELPER = unsafeAtomicHelper;
        if (r12 != 0) {
            LazyLogger lazyLogger = log;
            Logger logger = lazyLogger.get();
            Level level = Level.SEVERE;
            logger.log(level, "UnsafeAtomicHelper is broken!", th);
            lazyLogger.get().log(level, "AtomicReferenceFieldUpdaterAtomicHelper is broken!", r12);
        }
    }

    @VisibleForTesting
    public static String atomicHelperTypeForTest() {
        return ATOMIC_HELPER.atomicHelperTypeForTest();
    }

    public static boolean casValue(AbstractFutureState<?> abstractFutureState, Object obj, Object obj2) {
        return ATOMIC_HELPER.casValue(abstractFutureState, obj, obj2);
    }

    private boolean casWaiters(Waiter waiter, Waiter waiter2) {
        return ATOMIC_HELPER.casWaiters(this, waiter, waiter2);
    }

    private final Waiter gasWaiters(Waiter waiter) {
        return ATOMIC_HELPER.gasWaiters(this, waiter);
    }

    private static boolean mightBeAndroid() {
        String property = System.getProperty("java.runtime.name", "");
        return property == null || property.contains("Android");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void putNext(Waiter waiter, Waiter waiter2) {
        ATOMIC_HELPER.putNext(waiter, waiter2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void putThread(Waiter waiter, Thread thread) {
        ATOMIC_HELPER.putThread(waiter, thread);
    }

    private void removeWaiter(Waiter waiter) {
        waiter.thread = null;
        while (true) {
            Waiter waiter2 = this.waitersField;
            if (waiter2 == Waiter.TOMBSTONE) {
                return;
            }
            Waiter waiter3 = null;
            while (waiter2 != null) {
                Waiter waiter4 = waiter2.next;
                if (waiter2.thread != null) {
                    waiter3 = waiter2;
                } else if (waiter3 != null) {
                    waiter3.next = waiter4;
                    if (waiter3.thread == null) {
                        break;
                    }
                } else if (!casWaiters(waiter2, waiter4)) {
                    break;
                }
                waiter2 = waiter4;
            }
            return;
        }
    }

    @ParametricNullness
    public final V blockingGet(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        long nanos = timeUnit.toNanos(j);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object obj = this.valueField;
        if ((obj != null) && AbstractFuture.notInstanceOfDelegatingToFuture(obj)) {
            return (V) AbstractFuture.getDoneValue(obj);
        }
        long jNanoTime = nanos > 0 ? System.nanoTime() + nanos : 0L;
        if (nanos >= 1000) {
            Waiter waiter = this.waitersField;
            if (waiter != Waiter.TOMBSTONE) {
                Waiter waiter2 = new Waiter();
                do {
                    waiter2.setNext(waiter);
                    if (casWaiters(waiter, waiter2)) {
                        do {
                            OverflowAvoidingLockSupport.parkNanos(this, nanos);
                            if (Thread.interrupted()) {
                                removeWaiter(waiter2);
                                throw new InterruptedException();
                            }
                            Object obj2 = this.valueField;
                            if ((obj2 != null) && AbstractFuture.notInstanceOfDelegatingToFuture(obj2)) {
                                return (V) AbstractFuture.getDoneValue(obj2);
                            }
                            nanos = jNanoTime - System.nanoTime();
                        } while (nanos >= 1000);
                        removeWaiter(waiter2);
                    } else {
                        waiter = this.waitersField;
                    }
                } while (waiter != Waiter.TOMBSTONE);
            }
            Object obj3 = this.valueField;
            Objects.requireNonNull(obj3);
            return (V) AbstractFuture.getDoneValue(obj3);
        }
        while (nanos > 0) {
            Object obj4 = this.valueField;
            if ((obj4 != null) && AbstractFuture.notInstanceOfDelegatingToFuture(obj4)) {
                return (V) AbstractFuture.getDoneValue(obj4);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            nanos = jNanoTime - System.nanoTime();
        }
        String string = toString();
        String string2 = timeUnit.toString();
        Locale locale = Locale.ROOT;
        String lowerCase = string2.toLowerCase(locale);
        StringBuilder sbS = AbstractC2039eV.s(j, "Waited ", " ");
        sbS.append(timeUnit.toString().toLowerCase(locale));
        String string3 = sbS.toString();
        if (nanos + 1000 < 0) {
            String strI = AbstractC4671xI0.i(string3, " (plus ");
            long j2 = -nanos;
            long jConvert = timeUnit.convert(j2, TimeUnit.NANOSECONDS);
            long nanos2 = j2 - timeUnit.toNanos(jConvert);
            boolean z = jConvert == 0 || nanos2 > 1000;
            if (jConvert > 0) {
                String strI2 = strI + jConvert + " " + lowerCase;
                if (z) {
                    strI2 = AbstractC4671xI0.i(strI2, ",");
                }
                strI = AbstractC4671xI0.i(strI2, " ");
            }
            if (z) {
                strI = strI + nanos2 + " nanoseconds ";
            }
            string3 = AbstractC4671xI0.i(strI, "delay)");
        }
        if (isDone()) {
            throw new TimeoutException(AbstractC4671xI0.i(string3, " but future completed as timeout expired"));
        }
        throw new TimeoutException(AbstractC4671xI0.j(string3, " for ", string));
    }

    public final boolean casListeners(AbstractFuture.Listener listener, AbstractFuture.Listener listener2) {
        return ATOMIC_HELPER.casListeners(this, listener, listener2);
    }

    public final AbstractFuture.Listener gasListeners(AbstractFuture.Listener listener) {
        return ATOMIC_HELPER.gasListeners(this, listener);
    }

    public final AbstractFuture.Listener listeners() {
        return this.listenersField;
    }

    public final void releaseWaiters() {
        for (Waiter waiterGasWaiters = gasWaiters(Waiter.TOMBSTONE); waiterGasWaiters != null; waiterGasWaiters = waiterGasWaiters.next) {
            waiterGasWaiters.unpark();
        }
    }

    public final Object value() {
        return this.valueField;
    }

    @ParametricNullness
    public final V blockingGet() throws ExecutionException, InterruptedException {
        Object obj;
        if (!Thread.interrupted()) {
            Object obj2 = this.valueField;
            if ((obj2 != null) & AbstractFuture.notInstanceOfDelegatingToFuture(obj2)) {
                return (V) AbstractFuture.getDoneValue(obj2);
            }
            Waiter waiter = this.waitersField;
            if (waiter != Waiter.TOMBSTONE) {
                Waiter waiter2 = new Waiter();
                do {
                    waiter2.setNext(waiter);
                    if (casWaiters(waiter, waiter2)) {
                        do {
                            LockSupport.park(this);
                            if (!Thread.interrupted()) {
                                obj = this.valueField;
                            } else {
                                removeWaiter(waiter2);
                                throw new InterruptedException();
                            }
                        } while (!((obj != null) & AbstractFuture.notInstanceOfDelegatingToFuture(obj)));
                        return (V) AbstractFuture.getDoneValue(obj);
                    }
                    waiter = this.waitersField;
                } while (waiter != Waiter.TOMBSTONE);
            }
            Object obj3 = this.valueField;
            Objects.requireNonNull(obj3);
            return (V) AbstractFuture.getDoneValue(obj3);
        }
        throw new InterruptedException();
    }
}
