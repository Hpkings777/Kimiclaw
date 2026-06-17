package com.google.common.collect;

import com.apm.insight.log.LogLevel;
import com.google.common.collect.CollectCollectors;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/* JADX INFO: renamed from: com.google.common.collect.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class C1804a implements BiConsumer {
    public final /* synthetic */ int a;
    public final /* synthetic */ Function b;
    public final /* synthetic */ Function c;

    public /* synthetic */ C1804a(Function function, Function function2, int i) {
        this.a = i;
        this.b = function;
        this.c = function2;
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                CollectCollectors.lambda$toImmutableBiMap$0(this.b, this.c, (ImmutableBiMap.Builder) obj, obj2);
                break;
            case 1:
                CollectCollectors.lambda$toImmutableRangeMap$0(this.b, this.c, (ImmutableRangeMap.Builder) obj, obj2);
                break;
            case 2:
                CollectCollectors.lambda$flatteningToMultimap$0(this.b, this.c, (Multimap) obj, obj2);
                break;
            case 3:
                CollectCollectors.lambda$toImmutableEnumMap$2(this.b, this.c, (CollectCollectors.EnumMapAccumulator) obj, obj2);
                break;
            case 4:
                CollectCollectors.lambda$toImmutableMap$0(this.b, this.c, (ImmutableMap.Builder) obj, obj2);
                break;
            case 5:
                CollectCollectors.lambda$toImmutableListMultimap$0(this.b, this.c, (ImmutableListMultimap.Builder) obj, obj2);
                break;
            case 6:
                CollectCollectors.lambda$toImmutableSortedMap$1(this.b, this.c, (ImmutableSortedMap.Builder) obj, obj2);
                break;
            case LogLevel.NONE /* 7 */:
                CollectCollectors.lambda$toImmutableSetMultimap$0(this.b, this.c, (ImmutableSetMultimap.Builder) obj, obj2);
                break;
            case 8:
                CollectCollectors.lambda$toImmutableEnumMap$4(this.b, this.c, (CollectCollectors.EnumMapAccumulator) obj, obj2);
                break;
            default:
                CollectCollectors.lambda$toMultimap$0(this.b, this.c, (Multimap) obj, obj2);
                break;
        }
    }
}
