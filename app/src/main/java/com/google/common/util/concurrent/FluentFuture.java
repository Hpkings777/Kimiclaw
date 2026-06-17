package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.Partially;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public abstract class FluentFuture<V> extends GwtFluentFutureCatchingSpecialization<V> {

    public static abstract class TrustedFuture<V> extends FluentFuture<V> implements AbstractFuture.Trusted<V> {
        @Override // com.google.common.util.concurrent.AbstractFuture, com.google.common.util.concurrent.ListenableFuture
        public final void addListener(Runnable runnable, Executor executor) {
            super.addListener(runnable, executor);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean cancel(boolean z) {
            return super.cancel(z);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @ParametricNullness
        public final V get() throws ExecutionException, InterruptedException {
            return (V) super.get();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isCancelled() {
            return super.isCancelled();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isDone() {
            return super.isDone();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @ParametricNullness
        public final V get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
            return (V) super.get(j, timeUnit);
        }
    }

    public static <V> FluentFuture<V> from(ListenableFuture<V> listenableFuture) {
        return listenableFuture instanceof FluentFuture ? (FluentFuture) listenableFuture : new ForwardingFluentFuture(listenableFuture);
    }

    public final void addCallback(FutureCallback<? super V> futureCallback, Executor executor) {
        Futures.addCallback(this, futureCallback, executor);
    }

    @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    @J2ktIncompatible
    public final <X extends Throwable> FluentFuture<V> catching(Class<X> cls, Function<? super X, ? extends V> function, Executor executor) {
        return (FluentFuture) Futures.catching(this, cls, function, executor);
    }

    @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    @J2ktIncompatible
    public final <X extends Throwable> FluentFuture<V> catchingAsync(Class<X> cls, AsyncFunction<? super X, ? extends V> asyncFunction, Executor executor) {
        return (FluentFuture) Futures.catchingAsync(this, cls, asyncFunction, executor);
    }

    public final <T> FluentFuture<T> transform(Function<? super V, T> function, Executor executor) {
        return (FluentFuture) Futures.transform(this, function, executor);
    }

    public final <T> FluentFuture<T> transformAsync(AsyncFunction<? super V, T> asyncFunction, Executor executor) {
        return (FluentFuture) Futures.transformAsync(this, asyncFunction, executor);
    }

    @IgnoreJRERequirement
    @J2ktIncompatible
    @GwtIncompatible
    public final FluentFuture<V> withTimeout(Duration duration, ScheduledExecutorService scheduledExecutorService) {
        return withTimeout(Internal.toNanosSaturated(duration), TimeUnit.NANOSECONDS, scheduledExecutorService);
    }

    @J2ktIncompatible
    @GwtIncompatible
    public final FluentFuture<V> withTimeout(long j, TimeUnit timeUnit, ScheduledExecutorService scheduledExecutorService) {
        return (FluentFuture) Futures.withTimeout(this, j, timeUnit, scheduledExecutorService);
    }

    @Deprecated
    public static <V> FluentFuture<V> from(FluentFuture<V> fluentFuture) {
        return (FluentFuture) Preconditions.checkNotNull(fluentFuture);
    }
}
