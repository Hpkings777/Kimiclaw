package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public interface Function<F, T> {
    @ParametricNullness
    T apply(@ParametricNullness F f);

    boolean equals(Object obj);
}
