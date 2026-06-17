package com.google.common.collect;

import com.google.common.collect.Streams;
import java.util.function.BiFunction;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class v implements BiFunction {
    @Override // java.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        return new Streams.TemporaryPair(obj, obj2);
    }
}
