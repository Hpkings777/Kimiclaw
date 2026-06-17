package com.google.common.collect;

import com.google.common.collect.CollectSpliterators;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class l implements Consumer {
    public final /* synthetic */ int a;
    public final /* synthetic */ CollectSpliterators.FlatMapSpliterator b;
    public final /* synthetic */ Object c;

    public /* synthetic */ l(CollectSpliterators.FlatMapSpliterator flatMapSpliterator, Object obj, int i) {
        this.a = i;
        this.b = flatMapSpliterator;
        this.c = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.a) {
            case 0:
                this.b.lambda$forEachRemaining$0((Consumer) this.c, obj);
                break;
            default:
                ((CollectSpliterators.FlatMapSpliteratorOfPrimitive) this.b).lambda$forEachRemaining$0(this.c, obj);
                break;
        }
    }
}
