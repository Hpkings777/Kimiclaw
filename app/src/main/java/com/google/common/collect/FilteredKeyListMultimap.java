package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Predicate;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class FilteredKeyListMultimap<K, V> extends FilteredKeyMultimap<K, V> implements ListMultimap<K, V> {
    public FilteredKeyListMultimap(ListMultimap<K, V> listMultimap, Predicate<? super K> predicate) {
        super(listMultimap, predicate);
    }

    @Override // com.google.common.collect.FilteredKeyMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public List<V> get(@ParametricNullness K k) {
        return (List) super.get((Object) k);
    }

    @Override // com.google.common.collect.FilteredKeyMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public List<V> removeAll(Object obj) {
        return (List) super.removeAll(obj);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public List<V> replaceValues(@ParametricNullness K k, Iterable<? extends V> iterable) {
        return (List) super.replaceValues((Object) k, (Iterable) iterable);
    }

    @Override // com.google.common.collect.FilteredKeyMultimap, com.google.common.collect.FilteredMultimap
    public ListMultimap<K, V> unfiltered() {
        return (ListMultimap) super.unfiltered();
    }
}
