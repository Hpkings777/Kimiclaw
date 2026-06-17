package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public interface AsyncFunction<I, O> {
    ListenableFuture<O> apply(@ParametricNullness I i) throws Exception;
}
