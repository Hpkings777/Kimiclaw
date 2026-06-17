package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public interface Table<R, C, V> {

    public interface Cell<R, C, V> {
        boolean equals(Object obj);

        @ParametricNullness
        C getColumnKey();

        @ParametricNullness
        R getRowKey();

        @ParametricNullness
        V getValue();

        int hashCode();
    }

    Set<Cell<R, C, V>> cellSet();

    void clear();

    Map<R, V> column(@ParametricNullness C c);

    Set<C> columnKeySet();

    Map<C, Map<R, V>> columnMap();

    boolean contains(Object obj, Object obj2);

    boolean containsColumn(Object obj);

    boolean containsRow(Object obj);

    boolean containsValue(Object obj);

    boolean equals(Object obj);

    V get(Object obj, Object obj2);

    int hashCode();

    boolean isEmpty();

    V put(@ParametricNullness R r, @ParametricNullness C c, @ParametricNullness V v);

    void putAll(Table<? extends R, ? extends C, ? extends V> table);

    V remove(Object obj, Object obj2);

    Map<C, V> row(@ParametricNullness R r);

    Set<R> rowKeySet();

    Map<R, Map<C, V>> rowMap();

    int size();

    Collection<V> values();
}
