package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class ImmutableMapKeySet<K, V> extends IndexedImmutableSet<K> {
    private final ImmutableMap<K, V> map;

    @GwtIncompatible
    @J2ktIncompatible
    public static final class KeySetSerializedForm<K> implements Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;
        final ImmutableMap<K, ?> map;

        public KeySetSerializedForm(ImmutableMap<K, ?> immutableMap) {
            this.map = immutableMap;
        }

        public Object readResolve() {
            return this.map.keySet();
        }
    }

    public ImmutableMapKeySet(ImmutableMap<K, V> immutableMap) {
        this.map = immutableMap;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.map.containsKey(obj);
    }

    @Override // com.google.common.collect.IndexedImmutableSet
    public K get(int i) {
        return this.map.entrySet().asList().get(i).getKey();
    }

    @Override // com.google.common.collect.ImmutableCollection
    public boolean isPartialView() {
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.map.size();
    }

    @Override // com.google.common.collect.IndexedImmutableSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    @J2ktIncompatible
    @GwtIncompatible
    public Object writeReplace() {
        return new KeySetSerializedForm(this.map);
    }

    @Override // com.google.common.collect.IndexedImmutableSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
    public UnmodifiableIterator<K> iterator() {
        return this.map.keyIterator();
    }
}
