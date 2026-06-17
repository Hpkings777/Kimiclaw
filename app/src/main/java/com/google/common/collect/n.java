package com.google.common.collect;

import com.google.common.collect.CollectSpliterators;
import java.util.Spliterator;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class n implements CollectSpliterators.FlatMapSpliterator.Factory {
    public final /* synthetic */ int a;

    public /* synthetic */ n(int i) {
        this.a = i;
    }

    @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliterator.Factory
    public final Spliterator newFlatMapSpliterator(Spliterator spliterator, Spliterator spliterator2, Function function, int i, long j) {
        switch (this.a) {
            case 0:
                return new CollectSpliterators.FlatMapSpliteratorOfDouble((Spliterator.OfDouble) spliterator, spliterator2, function, i, j);
            case 1:
                return new CollectSpliterators.FlatMapSpliteratorOfInt((Spliterator.OfInt) spliterator, spliterator2, function, i, j);
            case 2:
                return new CollectSpliterators.FlatMapSpliteratorOfLong((Spliterator.OfLong) spliterator, spliterator2, function, i, j);
            default:
                return new CollectSpliterators.FlatMapSpliteratorOfObject(spliterator, spliterator2, function, i, j);
        }
    }
}
