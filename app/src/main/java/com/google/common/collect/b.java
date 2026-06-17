package com.google.common.collect;

import com.google.common.collect.MoreCollectors;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class b implements Supplier {
    public final /* synthetic */ int a;

    public /* synthetic */ b(int i) {
        this.a = i;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        switch (this.a) {
            case 0:
                return CollectCollectors.lambda$toImmutableEnumSetGeneric$0();
            case 1:
                return CollectCollectors.lambda$toImmutableEnumMap$0();
            case 2:
                return new MoreCollectors.ToOptionalState();
            default:
                return TableCollectors.lambda$toImmutableTable$1();
        }
    }
}
