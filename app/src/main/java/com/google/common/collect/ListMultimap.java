package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public interface ListMultimap<K, V> extends Multimap<K, V> {
    Map<K, Collection<V>> asMap();

    boolean equals(Object obj);

    @Override // 
    List<V> get(@ParametricNullness K k);

    @Override // 
    List<V> removeAll(Object obj);

    @Override // 
    List<V> replaceValues(@ParametricNullness K k, Iterable<? extends V> iterable);
}
