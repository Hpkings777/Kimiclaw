package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class MoreObjects {

    public static final class ToStringHelper {
        private final String className;
        private final ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitEmptyValues;
        private boolean omitNullValues;

        public static final class UnconditionalValueHolder extends ValueHolder {
            private UnconditionalValueHolder() {
            }
        }

        public static class ValueHolder {
            String name;
            ValueHolder next;
            Object value;
        }

        private ValueHolder addHolder() {
            ValueHolder valueHolder = new ValueHolder();
            this.holderTail.next = valueHolder;
            this.holderTail = valueHolder;
            return valueHolder;
        }

        private UnconditionalValueHolder addUnconditionalHolder() {
            UnconditionalValueHolder unconditionalValueHolder = new UnconditionalValueHolder();
            this.holderTail.next = unconditionalValueHolder;
            this.holderTail = unconditionalValueHolder;
            return unconditionalValueHolder;
        }

        private static boolean isEmpty(Object obj) {
            return obj instanceof CharSequence ? ((CharSequence) obj).length() == 0 : obj instanceof Collection ? ((Collection) obj).isEmpty() : obj instanceof Map ? ((Map) obj).isEmpty() : obj instanceof Optional ? !((Optional) obj).isPresent() : obj.getClass().isArray() && Array.getLength(obj) == 0;
        }

        public ToStringHelper add(String str, Object obj) {
            return addHolder(str, obj);
        }

        public ToStringHelper addValue(Object obj) {
            return addHolder(obj);
        }

        public ToStringHelper omitEmptyValues() {
            this.omitEmptyValues = true;
            return this;
        }

        public ToStringHelper omitNullValues() {
            this.omitNullValues = true;
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0030  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public String toString() {
            boolean z = this.omitNullValues;
            boolean z2 = this.omitEmptyValues;
            StringBuilder sb = new StringBuilder(32);
            sb.append(this.className);
            sb.append('{');
            String str = "";
            for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; valueHolder = valueHolder.next) {
                Object obj = valueHolder.value;
                if (valueHolder instanceof UnconditionalValueHolder) {
                    sb.append(str);
                    String str2 = valueHolder.name;
                    if (str2 != null) {
                        sb.append(str2);
                        sb.append('=');
                    }
                    if (obj == null || !obj.getClass().isArray()) {
                        sb.append(obj);
                    } else {
                        String strDeepToString = Arrays.deepToString(new Object[]{obj});
                        sb.append((CharSequence) strDeepToString, 1, strDeepToString.length() - 1);
                    }
                    str = ", ";
                } else if (obj == null) {
                    if (!z) {
                    }
                } else if (!z2 || !isEmpty(obj)) {
                }
            }
            sb.append('}');
            return sb.toString();
        }

        private ToStringHelper(String str) {
            ValueHolder valueHolder = new ValueHolder();
            this.holderHead = valueHolder;
            this.holderTail = valueHolder;
            this.omitNullValues = false;
            this.omitEmptyValues = false;
            this.className = (String) Preconditions.checkNotNull(str);
        }

        public ToStringHelper add(String str, boolean z) {
            return addUnconditionalHolder(str, String.valueOf(z));
        }

        public ToStringHelper addValue(boolean z) {
            return addUnconditionalHolder(String.valueOf(z));
        }

        private ToStringHelper addHolder(Object obj) {
            addHolder().value = obj;
            return this;
        }

        private ToStringHelper addUnconditionalHolder(Object obj) {
            addUnconditionalHolder().value = obj;
            return this;
        }

        public ToStringHelper add(String str, char c) {
            return addUnconditionalHolder(str, String.valueOf(c));
        }

        public ToStringHelper addValue(char c) {
            return addUnconditionalHolder(String.valueOf(c));
        }

        public ToStringHelper add(String str, double d) {
            return addUnconditionalHolder(str, String.valueOf(d));
        }

        public ToStringHelper addValue(double d) {
            return addUnconditionalHolder(String.valueOf(d));
        }

        private ToStringHelper addHolder(String str, Object obj) {
            ValueHolder valueHolderAddHolder = addHolder();
            valueHolderAddHolder.value = obj;
            valueHolderAddHolder.name = (String) Preconditions.checkNotNull(str);
            return this;
        }

        private ToStringHelper addUnconditionalHolder(String str, Object obj) {
            UnconditionalValueHolder unconditionalValueHolderAddUnconditionalHolder = addUnconditionalHolder();
            unconditionalValueHolderAddUnconditionalHolder.value = obj;
            unconditionalValueHolderAddUnconditionalHolder.name = (String) Preconditions.checkNotNull(str);
            return this;
        }

        public ToStringHelper add(String str, float f) {
            return addUnconditionalHolder(str, String.valueOf(f));
        }

        public ToStringHelper addValue(float f) {
            return addUnconditionalHolder(String.valueOf(f));
        }

        public ToStringHelper add(String str, int i) {
            return addUnconditionalHolder(str, String.valueOf(i));
        }

        public ToStringHelper addValue(int i) {
            return addUnconditionalHolder(String.valueOf(i));
        }

        public ToStringHelper add(String str, long j) {
            return addUnconditionalHolder(str, String.valueOf(j));
        }

        public ToStringHelper addValue(long j) {
            return addUnconditionalHolder(String.valueOf(j));
        }
    }

    private MoreObjects() {
    }

    public static <T> T firstNonNull(T t, T t2) {
        if (t != null) {
            return t;
        }
        if (t2 != null) {
            return t2;
        }
        throw new NullPointerException("Both parameters are null");
    }

    public static ToStringHelper toStringHelper(Object obj) {
        return new ToStringHelper(obj.getClass().getSimpleName());
    }

    public static ToStringHelper toStringHelper(Class<?> cls) {
        return new ToStringHelper(cls.getSimpleName());
    }

    public static ToStringHelper toStringHelper(String str) {
        return new ToStringHelper(str);
    }
}
