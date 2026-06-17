package com.google.common.collect;

import com.google.common.collect.CollectCollectors;
import com.google.common.collect.MoreCollectors;
import com.google.common.collect.TableCollectors;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class e implements Function {
    public final /* synthetic */ int a;

    public /* synthetic */ e(int i) {
        this.a = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.a) {
            case 0:
                return ((CollectCollectors.EnumSetAccumulator) obj).toImmutableSet();
            case 1:
                return CollectCollectors.lambda$toImmutableMultiset$2((Multiset) obj);
            case 2:
                return ((CollectCollectors.EnumMapAccumulator) obj).toImmutableMap();
            case 3:
                return ((TopKSelector) obj).topK();
            case 4:
                return ((MoreCollectors.ToOptionalState) obj).getOptional();
            case 5:
                return MoreCollectors.lambda$static$1((MoreCollectors.ToOptionalState) obj);
            default:
                return ((TableCollectors.ImmutableTableCollectorState) obj).toTable();
        }
    }
}
