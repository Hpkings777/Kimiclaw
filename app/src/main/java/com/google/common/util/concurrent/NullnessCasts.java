package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class NullnessCasts {
    private NullnessCasts() {
    }

    @ParametricNullness
    public static <T> T uncheckedCastNullableTToT(T t) {
        return t;
    }

    @ParametricNullness
    public static <T> T uncheckedNull() {
        return null;
    }
}
