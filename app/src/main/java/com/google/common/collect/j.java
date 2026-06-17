package com.google.common.collect;

import com.google.common.collect.CollectSpliterators;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class j implements Consumer {
    public final /* synthetic */ int a;
    public final /* synthetic */ Consumer b;
    public final /* synthetic */ Function c;

    public /* synthetic */ j(Consumer consumer, Function function, int i) {
        this.a = i;
        this.b = consumer;
        this.c = function;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.a) {
            case 0:
                CollectSpliterators.AnonymousClass1.lambda$forEachRemaining$0(this.b, this.c, obj);
                break;
            default:
                CollectSpliterators.AnonymousClass1.lambda$tryAdvance$0(this.b, this.c, obj);
                break;
        }
    }
}
