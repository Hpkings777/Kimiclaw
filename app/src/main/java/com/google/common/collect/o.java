package com.google.common.collect;

import com.google.common.base.Predicate;
import com.google.common.collect.FilteredEntryMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class o implements Predicate {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ o(Object obj, int i) {
        this.a = i;
        this.b = obj;
    }

    @Override // com.google.common.base.Predicate
    public final boolean apply(Object obj) {
        switch (this.a) {
            case 0:
                return FilteredEntryMultimap.Keys.AnonymousClass1.lambda$removeEntriesIf$0((Predicate) this.b, (Map.Entry) obj);
            case 1:
                return Maps.FilteredEntryBiMap.lambda$inversePredicate$0((Predicate) this.b, (Map.Entry) obj);
            default:
                return ((Multisets.FilteredMultiset) this.b).lambda$createEntrySet$0((Multiset.Entry) obj);
        }
    }
}
