package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ObjectArrays;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public final class SimpleTimeLimiter implements TimeLimiter {
    private final ExecutorService executor;

    private SimpleTimeLimiter(ExecutorService executorService) {
        this.executor = (ExecutorService) Preconditions.checkNotNull(executorService);
    }

    @ParametricNullness
    private <T> T callWithTimeout(Callable<T> callable, long j, TimeUnit timeUnit, boolean z) throws Exception {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(timeUnit);
        checkPositiveTimeout(j);
        Future<T> futureSubmit = this.executor.submit(callable);
        try {
            return z ? futureSubmit.get(j, timeUnit) : (T) Uninterruptibles.getUninterruptibly(futureSubmit, j, timeUnit);
        } catch (InterruptedException e) {
            futureSubmit.cancel(true);
            throw e;
        } catch (ExecutionException e2) {
            throw throwCause(e2, true);
        } catch (TimeoutException e3) {
            futureSubmit.cancel(true);
            throw new UncheckedTimeoutException(e3);
        }
    }

    private static void checkPositiveTimeout(long j) {
        Preconditions.checkArgument(j > 0, "timeout must be positive: %s", j);
    }

    public static SimpleTimeLimiter create(ExecutorService executorService) {
        return new SimpleTimeLimiter(executorService);
    }

    private static boolean declaresInterruptedEx(Method method) {
        for (Class<?> cls : method.getExceptionTypes()) {
            if (cls == InterruptedException.class) {
                return true;
            }
        }
        return false;
    }

    private static Set<Method> findInterruptibleMethods(Class<?> cls) {
        HashSet hashSet = new HashSet();
        for (Method method : cls.getMethods()) {
            if (declaresInterruptedEx(method)) {
                hashSet.add(method);
            }
        }
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object lambda$newProxy$0(final Object obj, long j, TimeUnit timeUnit, Set set, Object obj2, final Method method, final Object[] objArr) throws Throwable {
        return callWithTimeout(new Callable() { // from class: fG0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return SimpleTimeLimiter.lambda$newProxy$1(method, obj, objArr);
            }
        }, j, timeUnit, set.contains(method));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object lambda$newProxy$1(Method method, Object obj, Object[] objArr) throws Exception {
        try {
            return method.invoke(obj, objArr);
        } catch (InvocationTargetException e) {
            throw throwCause(e, false);
        }
    }

    private static Exception throwCause(Exception exc, boolean z) throws Exception {
        Throwable cause = exc.getCause();
        if (cause == null) {
            throw exc;
        }
        if (z) {
            cause.setStackTrace((StackTraceElement[]) ObjectArrays.concat(cause.getStackTrace(), exc.getStackTrace(), StackTraceElement.class));
        }
        if (cause instanceof Exception) {
            throw ((Exception) cause);
        }
        if (cause instanceof Error) {
            throw ((Error) cause);
        }
        throw exc;
    }

    private static void wrapAndThrowExecutionExceptionOrError(Throwable th) throws ExecutionException {
        if (th instanceof Error) {
            throw new ExecutionError((Error) th);
        }
        if (!(th instanceof RuntimeException)) {
            throw new ExecutionException(th);
        }
        throw new UncheckedExecutionException(th);
    }

    private static void wrapAndThrowRuntimeExecutionExceptionOrError(Throwable th) {
        if (!(th instanceof Error)) {
            throw new UncheckedExecutionException(th);
        }
        throw new ExecutionError((Error) th);
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    @ParametricNullness
    public <T> T callUninterruptiblyWithTimeout(Callable<T> callable, long j, TimeUnit timeUnit) throws ExecutionException, TimeoutException {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(timeUnit);
        checkPositiveTimeout(j);
        Future<T> futureSubmit = this.executor.submit(callable);
        try {
            return (T) Uninterruptibles.getUninterruptibly(futureSubmit, j, timeUnit);
        } catch (ExecutionException e) {
            wrapAndThrowExecutionExceptionOrError(e.getCause());
            throw new AssertionError();
        } catch (TimeoutException e2) {
            futureSubmit.cancel(true);
            throw e2;
        }
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public <T> T newProxy(final T t, Class<T> cls, final long j, final TimeUnit timeUnit) {
        Preconditions.checkNotNull(t);
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(timeUnit);
        checkPositiveTimeout(j);
        Preconditions.checkArgument(cls.isInterface(), "interfaceType must be an interface type");
        final Set<Method> setFindInterruptibleMethods = findInterruptibleMethods(cls);
        return (T) newProxy(cls, new InvocationHandler() { // from class: gG0
            @Override // java.lang.reflect.InvocationHandler
            public final Object invoke(Object obj, Method method, Object[] objArr) {
                return this.a.lambda$newProxy$0(t, j, timeUnit, setFindInterruptibleMethods, obj, method, objArr);
            }
        });
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public void runUninterruptiblyWithTimeout(Runnable runnable, long j, TimeUnit timeUnit) throws TimeoutException {
        Preconditions.checkNotNull(runnable);
        Preconditions.checkNotNull(timeUnit);
        checkPositiveTimeout(j);
        Future<?> futureSubmit = this.executor.submit(runnable);
        try {
            Uninterruptibles.getUninterruptibly(futureSubmit, j, timeUnit);
        } catch (ExecutionException e) {
            wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
            throw new AssertionError();
        } catch (TimeoutException e2) {
            futureSubmit.cancel(true);
            throw e2;
        }
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    public void runWithTimeout(Runnable runnable, long j, TimeUnit timeUnit) throws Throwable {
        Preconditions.checkNotNull(runnable);
        Preconditions.checkNotNull(timeUnit);
        checkPositiveTimeout(j);
        Future<?> futureSubmit = this.executor.submit(runnable);
        try {
            futureSubmit.get(j, timeUnit);
        } catch (InterruptedException e) {
            e = e;
            futureSubmit.cancel(true);
            throw e;
        } catch (ExecutionException e2) {
            wrapAndThrowRuntimeExecutionExceptionOrError(e2.getCause());
            throw new AssertionError();
        } catch (TimeoutException e3) {
            e = e3;
            futureSubmit.cancel(true);
            throw e;
        }
    }

    private static <T> T newProxy(Class<T> cls, InvocationHandler invocationHandler) {
        return cls.cast(Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, invocationHandler));
    }

    @Override // com.google.common.util.concurrent.TimeLimiter
    @ParametricNullness
    public <T> T callWithTimeout(Callable<T> callable, long j, TimeUnit timeUnit) throws Throwable {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(timeUnit);
        checkPositiveTimeout(j);
        Future<T> futureSubmit = this.executor.submit(callable);
        try {
            return futureSubmit.get(j, timeUnit);
        } catch (InterruptedException e) {
            e = e;
            futureSubmit.cancel(true);
            throw e;
        } catch (ExecutionException e2) {
            wrapAndThrowExecutionExceptionOrError(e2.getCause());
            throw new AssertionError();
        } catch (TimeoutException e3) {
            e = e3;
            futureSubmit.cancel(true);
            throw e;
        }
    }
}
