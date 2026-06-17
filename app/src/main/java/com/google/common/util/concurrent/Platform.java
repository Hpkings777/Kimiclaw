package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class Platform {
    private Platform() {
    }

    public static <V> V get(AbstractFuture<V> abstractFuture) throws ExecutionException, InterruptedException {
        return abstractFuture.blockingGet();
    }

    public static void interruptCurrentThread() {
        Thread.currentThread().interrupt();
    }

    public static boolean isInstanceOfThrowableClass(Throwable th, Class<? extends Throwable> cls) {
        return cls.isInstance(th);
    }

    public static void restoreInterruptIfIsInterruptedException(Throwable th) {
        Preconditions.checkNotNull(th);
        if (th instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    public static void rethrowIfErrorOtherThanStackOverflow(Throwable th) {
        Preconditions.checkNotNull(th);
        if ((th instanceof Error) && !(th instanceof StackOverflowError)) {
            throw ((Error) th);
        }
    }

    public static <V> V get(AbstractFuture<V> abstractFuture, long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return abstractFuture.blockingGet(j, timeUnit);
    }
}
