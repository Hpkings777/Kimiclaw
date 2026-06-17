package com.google.common.collect;

import com.apm.insight.log.LogLevel;
import com.google.common.collect.CollectCollectors;
import com.google.common.collect.MoreCollectors;
import java.util.function.BinaryOperator;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class d implements BinaryOperator {
    public final /* synthetic */ int a;

    public /* synthetic */ d(int i) {
        this.a = i;
    }

    @Override // java.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                return ((CollectCollectors.EnumSetAccumulator) obj).combine((CollectCollectors.EnumSetAccumulator) obj2);
            case 1:
                return CollectCollectors.lambda$flatteningToMultimap$1((Multimap) obj, (Multimap) obj2);
            case 2:
                return CollectCollectors.lambda$toImmutableMultiset$1((Multiset) obj, (Multiset) obj2);
            case 3:
                return ((CollectCollectors.EnumMapAccumulator) obj).combine((CollectCollectors.EnumMapAccumulator) obj2);
            case 4:
                return CollectCollectors.lambda$toImmutableEnumMap$1(obj, obj2);
            case 5:
                return CollectCollectors.lambda$toMultiset$1((Multiset) obj, (Multiset) obj2);
            case 6:
                return CollectCollectors.lambda$toMultimap$1((Multimap) obj, (Multimap) obj2);
            case LogLevel.NONE /* 7 */:
                return ((TopKSelector) obj).combine((TopKSelector) obj2);
            case 8:
                return ((MoreCollectors.ToOptionalState) obj).combine((MoreCollectors.ToOptionalState) obj2);
            default:
                return TableCollectors.lambda$toTable$0(obj, obj2);
        }
    }
}
