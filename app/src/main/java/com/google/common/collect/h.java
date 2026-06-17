package com.google.common.collect;

import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class h implements Supplier {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ h(Object obj, int i) {
        this.a = i;
        this.b = obj;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        switch (this.a) {
            case 0:
                return CollectCollectors.lambda$toImmutableSortedMap$2((Comparator) this.b);
            case 1:
                return CollectCollectors.lambda$toImmutableSortedMap$0((Comparator) this.b);
            case 2:
                return CollectCollectors.lambda$toImmutableSortedSet$0((Comparator) this.b);
            default:
                return CollectCollectors.lambda$toImmutableEnumMap$3((BinaryOperator) this.b);
        }
    }
}
