package com.google.common.collect;

import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class f implements Function {
    public final /* synthetic */ int a;
    public final /* synthetic */ Function b;

    public /* synthetic */ f(Function function, int i) {
        this.a = i;
        this.b = function;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.a) {
            case 0:
                return CollectCollectors.lambda$flatteningToImmutableListMultimap$0(this.b, obj);
            case 1:
                return CollectCollectors.lambda$flatteningToImmutableListMultimap$1(this.b, obj);
            case 2:
                return CollectCollectors.lambda$flatteningToImmutableSetMultimap$0(this.b, obj);
            default:
                return CollectCollectors.lambda$flatteningToImmutableSetMultimap$1(this.b, obj);
        }
    }
}
