package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
final class FuturesGetChecked {
    private static final Ordering<List<Class<?>>> ORDERING_BY_CONSTRUCTOR_PARAMETER_LIST;
    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_THEN_WITH_THROWABLE_PARAM;

    @VisibleForTesting
    public interface GetCheckedTypeValidator {
        void validateClass(Class<? extends Exception> cls);
    }

    public static final class GetCheckedTypeValidatorHolder {
        static final GetCheckedTypeValidator BEST_VALIDATOR = getBestValidator();

        public enum WeakSetValidator implements GetCheckedTypeValidator {
            INSTANCE;

            private static final Set<WeakReference<Class<? extends Exception>>> validClasses = new CopyOnWriteArraySet();

            @Override // com.google.common.util.concurrent.FuturesGetChecked.GetCheckedTypeValidator
            public void validateClass(Class<? extends Exception> cls) {
                Iterator<WeakReference<Class<? extends Exception>>> it = validClasses.iterator();
                while (it.hasNext()) {
                    if (cls.equals(it.next().get())) {
                        return;
                    }
                }
                FuturesGetChecked.checkExceptionClassValidity(cls);
                Set<WeakReference<Class<? extends Exception>>> set = validClasses;
                if (set.size() > 1000) {
                    set.clear();
                }
                set.add(new WeakReference<>(cls));
            }
        }

        private GetCheckedTypeValidatorHolder() {
        }

        public static GetCheckedTypeValidator getBestValidator() {
            return FuturesGetChecked.weakSetValidator();
        }
    }

    static {
        final int i = 0;
        final int i2 = 1;
        Ordering<List<Class<?>>> orderingReverse = Ordering.natural().onResultOf(new Function() { // from class: com.google.common.util.concurrent.i
            @Override // com.google.common.base.Function
            public final Object apply(Object obj) {
                switch (i) {
                    case 0:
                        return FuturesGetChecked.lambda$static$0((List) obj);
                    case 1:
                        return FuturesGetChecked.lambda$static$1((List) obj);
                    default:
                        return FuturesGetChecked.lambda$static$2((Constructor) obj);
                }
            }
        }).compound(Ordering.natural().onResultOf(new Function() { // from class: com.google.common.util.concurrent.i
            @Override // com.google.common.base.Function
            public final Object apply(Object obj) {
                switch (i2) {
                    case 0:
                        return FuturesGetChecked.lambda$static$0((List) obj);
                    case 1:
                        return FuturesGetChecked.lambda$static$1((List) obj);
                    default:
                        return FuturesGetChecked.lambda$static$2((Constructor) obj);
                }
            }
        })).reverse();
        ORDERING_BY_CONSTRUCTOR_PARAMETER_LIST = orderingReverse;
        final int i3 = 2;
        WITH_STRING_PARAM_THEN_WITH_THROWABLE_PARAM = orderingReverse.onResultOf(new Function() { // from class: com.google.common.util.concurrent.i
            @Override // com.google.common.base.Function
            public final Object apply(Object obj) {
                switch (i3) {
                    case 0:
                        return FuturesGetChecked.lambda$static$0((List) obj);
                    case 1:
                        return FuturesGetChecked.lambda$static$1((List) obj);
                    default:
                        return FuturesGetChecked.lambda$static$2((Constructor) obj);
                }
            }
        });
    }

    private FuturesGetChecked() {
    }

    private static GetCheckedTypeValidator bestGetCheckedTypeValidator() {
        return GetCheckedTypeValidatorHolder.BEST_VALIDATOR;
    }

    @VisibleForTesting
    public static void checkExceptionClassValidity(Class<? extends Exception> cls) {
        Preconditions.checkArgument(isCheckedException(cls), "Futures.getChecked exception type (%s) must not be a RuntimeException", cls);
        Preconditions.checkArgument(hasConstructorUsableByGetChecked(cls), "Futures.getChecked exception type (%s) must be an accessible class with an accessible constructor whose parameters (if any) must be of type String and/or Throwable", cls);
    }

    @ParametricNullness
    public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> cls) throws Exception {
        return (V) getChecked(bestGetCheckedTypeValidator(), future, cls);
    }

    private static boolean hasConstructorUsableByGetChecked(Class<? extends Exception> cls) {
        try {
            newWithCause(cls, new Exception());
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    @VisibleForTesting
    public static boolean isCheckedException(Class<? extends Exception> cls) {
        return !RuntimeException.class.isAssignableFrom(cls);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Comparable lambda$static$0(List list) {
        return Boolean.valueOf(list.contains(String.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Comparable lambda$static$1(List list) {
        return Boolean.valueOf(list.contains(Throwable.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ List lambda$static$2(Constructor constructor) {
        return Arrays.asList(constructor.getParameterTypes());
    }

    private static <X> X newFromConstructor(Constructor<X> constructor, Throwable th) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] objArr = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> cls = parameterTypes[i];
            if (cls.equals(String.class)) {
                objArr[i] = th.toString();
            } else {
                if (!cls.equals(Throwable.class)) {
                    return null;
                }
                objArr[i] = th;
            }
        }
        try {
            return constructor.newInstance(objArr);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException unused) {
            return null;
        }
    }

    private static <X extends Exception> X newWithCause(Class<X> cls, Throwable th) {
        Iterator it = preferringStringsThenThrowables(Arrays.asList(cls.getConstructors())).iterator();
        while (it.hasNext()) {
            X x = (X) newFromConstructor((Constructor) it.next(), th);
            if (x != null) {
                if (x.getCause() == null) {
                    x.initCause(th);
                }
                return x;
            }
        }
        throw new IllegalArgumentException("No appropriate constructor for exception of type " + cls + " in response to chained exception", th);
    }

    private static <X extends Exception> List<Constructor<X>> preferringStringsThenThrowables(List<Constructor<X>> list) {
        return (List<Constructor<X>>) WITH_STRING_PARAM_THEN_WITH_THROWABLE_PARAM.sortedCopy(list);
    }

    @VisibleForTesting
    public static GetCheckedTypeValidator weakSetValidator() {
        return GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
    }

    private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable th, Class<X> cls) throws Exception {
        if (th instanceof Error) {
            throw new ExecutionError((Error) th);
        }
        if (!(th instanceof RuntimeException)) {
            throw newWithCause(cls, th);
        }
        throw new UncheckedExecutionException(th);
    }

    @VisibleForTesting
    @ParametricNullness
    public static <V, X extends Exception> V getChecked(GetCheckedTypeValidator getCheckedTypeValidator, Future<V> future, Class<X> cls) throws Exception {
        getCheckedTypeValidator.validateClass(cls);
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(cls, e);
        } catch (ExecutionException e2) {
            wrapAndThrowExceptionOrError(e2.getCause(), cls);
            throw new AssertionError();
        }
    }

    @ParametricNullness
    public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> cls, long j, TimeUnit timeUnit) throws Exception {
        bestGetCheckedTypeValidator().validateClass(cls);
        try {
            return future.get(j, timeUnit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(cls, e);
        } catch (ExecutionException e2) {
            wrapAndThrowExceptionOrError(e2.getCause(), cls);
            throw new AssertionError();
        } catch (TimeoutException e3) {
            throw newWithCause(cls, e3);
        }
    }
}
