package com.google.common.collect;

import com.google.common.collect.CollectCollectors;
import com.google.common.collect.MoreCollectors;
import java.util.function.BiConsumer;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class c implements BiConsumer {
    public final /* synthetic */ int a;

    public /* synthetic */ c(int i) {
        this.a = i;
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                ((CollectCollectors.EnumSetAccumulator) obj).add((Enum) obj2);
                break;
            case 1:
                ((TopKSelector) obj).offer(obj2);
                break;
            case 2:
                ((MoreCollectors.ToOptionalState) obj).add(obj2);
                break;
            default:
                MoreCollectors.lambda$static$0((MoreCollectors.ToOptionalState) obj, obj2);
                break;
        }
    }
}
