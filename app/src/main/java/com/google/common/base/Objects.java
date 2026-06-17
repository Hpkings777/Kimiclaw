package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class Objects extends ExtraObjectsMethodsForWeb {
    private Objects() {
    }

    public static boolean equal(Object obj, Object obj2) {
        return java.util.Objects.equals(obj, obj2);
    }

    public static int hashCode(Object... objArr) {
        return java.util.Objects.hash(objArr);
    }
}
