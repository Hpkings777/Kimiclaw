package com.google.common.collect;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class g implements BiConsumer {
    public final /* synthetic */ int a;
    public final /* synthetic */ Function b;
    public final /* synthetic */ ToIntFunction c;

    public /* synthetic */ g(Function function, ToIntFunction toIntFunction, int i) {
        this.a = i;
        this.b = function;
        this.c = toIntFunction;
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        Multiset multiset = (Multiset) obj;
        switch (this.a) {
            case 0:
                CollectCollectors.lambda$toImmutableMultiset$0(this.b, this.c, multiset, obj2);
                break;
            default:
                CollectCollectors.lambda$toMultiset$0(this.b, this.c, multiset, obj2);
                break;
        }
    }
}
