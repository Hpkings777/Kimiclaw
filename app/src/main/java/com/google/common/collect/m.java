package com.google.common.collect;

import com.google.common.collect.CollectSpliterators;
import com.google.common.collect.Streams;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class m implements Consumer {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ m(Object obj, int i) {
        this.a = i;
        this.b = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.a) {
            case 0:
                ((CollectSpliterators.FlatMapSpliterator) this.b).lambda$tryAdvance$0(obj);
                break;
            case 1:
                ((CollectSpliterators.FlatMapSpliteratorOfPrimitive) this.b).lambda$tryAdvance$0(obj);
                break;
            case 2:
                Streams.lambda$forEachPair$0((BiConsumer) this.b, (Streams.TemporaryPair) obj);
                break;
            default:
                ((Streams.C1OptionalState) this.b).set(obj);
                break;
        }
    }
}
