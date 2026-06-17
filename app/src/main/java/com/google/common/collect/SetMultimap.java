package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public interface SetMultimap<K, V> extends Multimap<K, V> {
    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    Map<K, Collection<V>> asMap();

    @Override // com.google.common.collect.Multimap
    Set<Map.Entry<K, V>> entries();

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    boolean equals(Object obj);

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    Set<V> get(@ParametricNullness K k);

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    Set<V> removeAll(Object obj);

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    Set<V> replaceValues(@ParametricNullness K k, Iterable<? extends V> iterable);
}
