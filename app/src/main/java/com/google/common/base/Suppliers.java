package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.annotations.VisibleForTesting;
import defpackage.AbstractC2039eV;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class Suppliers {

    @VisibleForTesting
    public static final class ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;
        final Supplier<T> delegate;
        final long durationNanos;
        volatile transient long expirationNanos;
        private transient Object lock = new Object();
        volatile transient T value;

        public ExpiringMemoizingSupplier(Supplier<T> supplier, long j) {
            this.delegate = supplier;
            this.durationNanos = j;
        }

        @J2ktIncompatible
        @GwtIncompatible
        private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();
            this.lock = new Object();
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            long j = this.expirationNanos;
            long jNanoTime = System.nanoTime();
            if (j == 0 || jNanoTime - j >= 0) {
                synchronized (this.lock) {
                    try {
                        if (j == this.expirationNanos) {
                            T t = this.delegate.get();
                            this.value = t;
                            long j2 = jNanoTime + this.durationNanos;
                            if (j2 == 0) {
                                j2 = 1;
                            }
                            this.expirationNanos = j2;
                            return t;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return (T) NullnessCasts.uncheckedCastNullableTToT(this.value);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Suppliers.memoizeWithExpiration(");
            sb.append(this.delegate);
            sb.append(", ");
            return AbstractC2039eV.l(this.durationNanos, ", NANOS)", sb);
        }
    }

    @VisibleForTesting
    public static final class MemoizingSupplier<T> implements Supplier<T>, Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;
        final Supplier<T> delegate;
        volatile transient boolean initialized;
        private transient Object lock = new Object();
        transient T value;

        public MemoizingSupplier(Supplier<T> supplier) {
            this.delegate = (Supplier) Preconditions.checkNotNull(supplier);
        }

        @J2ktIncompatible
        @GwtIncompatible
        private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();
            this.lock = new Object();
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            if (!this.initialized) {
                synchronized (this.lock) {
                    try {
                        if (!this.initialized) {
                            T t = this.delegate.get();
                            this.value = t;
                            this.initialized = true;
                            return t;
                        }
                    } finally {
                    }
                }
            }
            return (T) NullnessCasts.uncheckedCastNullableTToT(this.value);
        }

        public String toString() {
            Object obj;
            StringBuilder sb = new StringBuilder("Suppliers.memoize(");
            if (this.initialized) {
                obj = "<supplier that returned " + this.value + ">";
            } else {
                obj = this.delegate;
            }
            sb.append(obj);
            sb.append(")");
            return sb.toString();
        }
    }

    public static final class NonSerializableMemoizingSupplier<T> implements Supplier<T> {
        private static final Supplier<Void> SUCCESSFULLY_COMPUTED = new c();
        private volatile Supplier<T> delegate;
        private final Object lock = new Object();
        private T value;

        public NonSerializableMemoizingSupplier(Supplier<T> supplier) {
            this.delegate = (Supplier) Preconditions.checkNotNull(supplier);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Void lambda$static$0() {
            throw new IllegalStateException();
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            Supplier<T> supplier = this.delegate;
            Supplier<T> supplier2 = (Supplier<T>) SUCCESSFULLY_COMPUTED;
            if (supplier != supplier2) {
                synchronized (this.lock) {
                    try {
                        if (this.delegate != supplier2) {
                            T t = this.delegate.get();
                            this.value = t;
                            this.delegate = supplier2;
                            return t;
                        }
                    } finally {
                    }
                }
            }
            return (T) NullnessCasts.uncheckedCastNullableTToT(this.value);
        }

        public String toString() {
            Object obj = this.delegate;
            StringBuilder sb = new StringBuilder("Suppliers.memoize(");
            if (obj == SUCCESSFULLY_COMPUTED) {
                obj = "<supplier that returned " + this.value + ">";
            }
            sb.append(obj);
            sb.append(")");
            return sb.toString();
        }
    }

    public static final class SupplierComposition<F, T> implements Supplier<T>, Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;
        final Function<? super F, T> function;
        final Supplier<F> supplier;

        public SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
            this.function = (Function) Preconditions.checkNotNull(function);
            this.supplier = (Supplier) Preconditions.checkNotNull(supplier);
        }

        @Override // com.google.common.base.Supplier
        public boolean equals(Object obj) {
            if (obj instanceof SupplierComposition) {
                SupplierComposition supplierComposition = (SupplierComposition) obj;
                if (this.function.equals(supplierComposition.function) && this.supplier.equals(supplierComposition.supplier)) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            return this.function.apply(this.supplier.get());
        }

        public int hashCode() {
            return java.util.Objects.hash(this.function, this.supplier);
        }

        public String toString() {
            return "Suppliers.compose(" + this.function + ", " + this.supplier + ")";
        }
    }

    public interface SupplierFunction<T> extends Function<Supplier<T>, T> {
    }

    public enum SupplierFunctionImpl implements SupplierFunction<Object> {
        INSTANCE;

        @Override // java.lang.Enum
        public String toString() {
            return "Suppliers.supplierFunction()";
        }

        @Override // com.google.common.base.Function
        public Object apply(Supplier<Object> supplier) {
            return supplier.get();
        }
    }

    public static final class SupplierOfInstance<T> implements Supplier<T>, Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;

        @ParametricNullness
        final T instance;

        public SupplierOfInstance(@ParametricNullness T t) {
            this.instance = t;
        }

        @Override // com.google.common.base.Supplier
        public boolean equals(Object obj) {
            if (obj instanceof SupplierOfInstance) {
                return java.util.Objects.equals(this.instance, ((SupplierOfInstance) obj).instance);
            }
            return false;
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            return this.instance;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.instance);
        }

        public String toString() {
            return "Suppliers.ofInstance(" + this.instance + ")";
        }
    }

    @J2ktIncompatible
    public static final class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;
        final Supplier<T> delegate;

        public ThreadSafeSupplier(Supplier<T> supplier) {
            this.delegate = (Supplier) Preconditions.checkNotNull(supplier);
        }

        @Override // com.google.common.base.Supplier
        @ParametricNullness
        public T get() {
            T t;
            synchronized (this.delegate) {
                t = this.delegate.get();
            }
            return t;
        }

        public String toString() {
            return "Suppliers.synchronizedSupplier(" + this.delegate + ")";
        }
    }

    private Suppliers() {
    }

    public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
        return new SupplierComposition(function, supplier);
    }

    public static <T> Supplier<T> memoize(Supplier<T> supplier) {
        return ((supplier instanceof NonSerializableMemoizingSupplier) || (supplier instanceof MemoizingSupplier)) ? supplier : supplier instanceof Serializable ? new MemoizingSupplier(supplier) : new NonSerializableMemoizingSupplier(supplier);
    }

    public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> supplier, long j, TimeUnit timeUnit) {
        Preconditions.checkNotNull(supplier);
        Preconditions.checkArgument(j > 0, "duration (%s %s) must be > 0", j, timeUnit);
        return new ExpiringMemoizingSupplier(supplier, timeUnit.toNanos(j));
    }

    public static <T> Supplier<T> ofInstance(@ParametricNullness T t) {
        return new SupplierOfInstance(t);
    }

    public static <T> Function<Supplier<T>, T> supplierFunction() {
        return SupplierFunctionImpl.INSTANCE;
    }

    @J2ktIncompatible
    public static <T> Supplier<T> synchronizedSupplier(Supplier<T> supplier) {
        return new ThreadSafeSupplier(supplier);
    }

    @J2ktIncompatible
    @GwtIncompatible
    @IgnoreJRERequirement
    public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> supplier, Duration duration) {
        Preconditions.checkNotNull(supplier);
        Preconditions.checkArgument((duration.isNegative() || duration.isZero()) ? false : true, "duration (%s) must be > 0", duration);
        return new ExpiringMemoizingSupplier(supplier, Internal.toNanosSaturated(duration));
    }
}
