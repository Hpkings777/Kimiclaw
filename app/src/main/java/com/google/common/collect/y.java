package com.google.common.collect;

import com.google.common.collect.TableCollectors;
import java.util.function.BinaryOperator;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class y implements BinaryOperator {
    public final /* synthetic */ int a;
    public final /* synthetic */ BinaryOperator b;

    public /* synthetic */ y(BinaryOperator binaryOperator, int i) {
        this.a = i;
        this.b = binaryOperator;
    }

    @Override // java.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                return TableCollectors.lambda$toTable$2(this.b, (Table) obj, (Table) obj2);
            default:
                return TableCollectors.lambda$toImmutableTable$3(this.b, (TableCollectors.ImmutableTableCollectorState) obj, (TableCollectors.ImmutableTableCollectorState) obj2);
        }
    }
}
