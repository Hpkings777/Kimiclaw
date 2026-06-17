package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public interface Multimap<K, V> {
    Map<K, Collection<V>> asMap();

    void clear();

    boolean containsEntry(Object obj, Object obj2);

    boolean containsKey(Object obj);

    boolean containsValue(Object obj);

    Collection<Map.Entry<K, V>> entries();

    boolean equals(Object obj);

    Collection<V> get(@ParametricNullness K k);

    int hashCode();

    boolean isEmpty();

    Set<K> keySet();

    Multiset<K> keys();

    boolean put(@ParametricNullness K k, @ParametricNullness V v);

    boolean putAll(Multimap<? extends K, ? extends V> multimap);

    boolean putAll(@ParametricNullness K k, Iterable<? extends V> iterable);

    boolean remove(Object obj, Object obj2);

    Collection<V> removeAll(Object obj);

    Collection<V> replaceValues(@ParametricNullness K k, Iterable<? extends V> iterable);

    int size();

    Collection<V> values();
}
