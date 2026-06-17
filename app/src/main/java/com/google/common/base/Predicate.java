package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public interface Predicate<T> {
    boolean apply(@ParametricNullness T t);

    boolean equals(Object obj);
}
