package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public interface BiMap<K, V> extends Map<K, V> {
    V forcePut(@ParametricNullness K k, @ParametricNullness V v);

    BiMap<V, K> inverse();

    V put(@ParametricNullness K k, @ParametricNullness V v);

    void putAll(Map<? extends K, ? extends V> map);

    @Override // com.google.common.collect.BiMap
    Set<V> values();
}
