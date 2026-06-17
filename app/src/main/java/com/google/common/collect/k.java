package com.google.common.collect;

import com.google.common.collect.CollectSpliterators;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class k implements IntConsumer {
    public final /* synthetic */ int a;
    public final /* synthetic */ Consumer b;
    public final /* synthetic */ IntFunction c;

    public /* synthetic */ k(Consumer consumer, IntFunction intFunction, int i) {
        this.a = i;
        this.b = consumer;
        this.c = intFunction;
    }

    @Override // java.util.function.IntConsumer
    public final void accept(int i) {
        switch (this.a) {
            case 0:
                CollectSpliterators.C1WithCharacteristics.lambda$forEachRemaining$0(this.b, this.c, i);
                break;
            default:
                CollectSpliterators.C1WithCharacteristics.lambda$tryAdvance$0(this.b, this.c, i);
                break;
        }
    }
}
