package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Map.Entry[0];

    @J2ktIncompatible
    @GwtIncompatible
    private static final long serialVersionUID = 912559;
    private transient ImmutableSet<Map.Entry<K, V>> entrySet;
    private transient ImmutableSet<K> keySet;
    private transient ImmutableSetMultimap<K, V> multimapView;
    private transient ImmutableCollection<V> values;

    public static class Builder<K, V> {
        Object[] alternatingKeysAndValues;
        DuplicateKey duplicateKey;
        boolean entriesUsed;
        int size;
        Comparator<? super V> valueComparator;

        public static final class DuplicateKey {
            private final Object key;
            private final Object value1;
            private final Object value2;

            public DuplicateKey(Object obj, Object obj2, Object obj3) {
                this.key = obj;
                this.value1 = obj2;
                this.value2 = obj3;
            }

            public IllegalArgumentException exception() {
                return new IllegalArgumentException("Multiple entries with same key: " + this.key + "=" + this.value1 + " and " + this.key + "=" + this.value2);
            }
        }

        public Builder() {
            this(4);
        }

        private ImmutableMap<K, V> build(boolean z) {
            Object[] objArrLastEntryForEachKey;
            DuplicateKey duplicateKey;
            DuplicateKey duplicateKey2;
            if (z && (duplicateKey2 = this.duplicateKey) != null) {
                throw duplicateKey2.exception();
            }
            int length = this.size;
            if (this.valueComparator == null) {
                objArrLastEntryForEachKey = this.alternatingKeysAndValues;
            } else {
                if (this.entriesUsed) {
                    this.alternatingKeysAndValues = Arrays.copyOf(this.alternatingKeysAndValues, length * 2);
                }
                objArrLastEntryForEachKey = this.alternatingKeysAndValues;
                if (!z) {
                    objArrLastEntryForEachKey = lastEntryForEachKey(objArrLastEntryForEachKey, this.size);
                    if (objArrLastEntryForEachKey.length < this.alternatingKeysAndValues.length) {
                        length = objArrLastEntryForEachKey.length >>> 1;
                    }
                }
                sortEntries(objArrLastEntryForEachKey, length, this.valueComparator);
            }
            this.entriesUsed = true;
            RegularImmutableMap regularImmutableMapCreate = RegularImmutableMap.create(length, objArrLastEntryForEachKey, this);
            if (!z || (duplicateKey = this.duplicateKey) == null) {
                return regularImmutableMapCreate;
            }
            throw duplicateKey.exception();
        }

        private void ensureCapacity(int i) {
            int i2 = i * 2;
            Object[] objArr = this.alternatingKeysAndValues;
            if (i2 > objArr.length) {
                this.alternatingKeysAndValues = Arrays.copyOf(objArr, ImmutableCollection.Builder.expandedCapacity(objArr.length, i2));
                this.entriesUsed = false;
            }
        }

        private Object[] lastEntryForEachKey(Object[] objArr, int i) {
            HashSet hashSet = new HashSet();
            BitSet bitSet = new BitSet();
            for (int i2 = i - 1; i2 >= 0; i2--) {
                Object obj = objArr[i2 * 2];
                Objects.requireNonNull(obj);
                if (!hashSet.add(obj)) {
                    bitSet.set(i2);
                }
            }
            if (bitSet.isEmpty()) {
                return objArr;
            }
            Object[] objArr2 = new Object[(i - bitSet.cardinality()) * 2];
            int i3 = 0;
            int i4 = 0;
            while (i3 < i * 2) {
                if (bitSet.get(i3 >>> 1)) {
                    i3 += 2;
                } else {
                    int i5 = i4 + 1;
                    int i6 = i3 + 1;
                    Object obj2 = objArr[i3];
                    Objects.requireNonNull(obj2);
                    objArr2[i4] = obj2;
                    i4 += 2;
                    i3 += 2;
                    Object obj3 = objArr[i6];
                    Objects.requireNonNull(obj3);
                    objArr2[i5] = obj3;
                }
            }
            return objArr2;
        }

        public static <V> void sortEntries(Object[] objArr, int i, Comparator<? super V> comparator) {
            Map.Entry[] entryArr = new Map.Entry[i];
            for (int i2 = 0; i2 < i; i2++) {
                int i3 = i2 * 2;
                Object obj = objArr[i3];
                Objects.requireNonNull(obj);
                Object obj2 = objArr[i3 + 1];
                Objects.requireNonNull(obj2);
                entryArr[i2] = new AbstractMap.SimpleImmutableEntry(obj, obj2);
            }
            Arrays.sort(entryArr, 0, i, Ordering.from(comparator).onResultOf(Maps.valueFunction()));
            for (int i4 = 0; i4 < i; i4++) {
                int i5 = i4 * 2;
                objArr[i5] = entryArr[i4].getKey();
                objArr[i5 + 1] = entryArr[i4].getValue();
            }
        }

        public ImmutableMap<K, V> buildKeepingLast() {
            return build(false);
        }

        public ImmutableMap<K, V> buildOrThrow() {
            return build(true);
        }

        public Builder<K, V> combine(Builder<K, V> builder) {
            Preconditions.checkNotNull(builder);
            ensureCapacity(this.size + builder.size);
            System.arraycopy(builder.alternatingKeysAndValues, 0, this.alternatingKeysAndValues, this.size * 2, builder.size * 2);
            this.size += builder.size;
            return this;
        }

        public Builder<K, V> orderEntriesByValue(Comparator<? super V> comparator) {
            Preconditions.checkState(this.valueComparator == null, "valueComparator was already set");
            this.valueComparator = (Comparator) Preconditions.checkNotNull(comparator, "valueComparator");
            return this;
        }

        public Builder<K, V> put(K k, V v) {
            ensureCapacity(this.size + 1);
            CollectPreconditions.checkEntryNotNull(k, v);
            Object[] objArr = this.alternatingKeysAndValues;
            int i = this.size;
            objArr[i * 2] = k;
            objArr[(i * 2) + 1] = v;
            this.size = i + 1;
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            return putAll(map.entrySet());
        }

        public Builder(int i) {
            this.alternatingKeysAndValues = new Object[i * 2];
            this.size = 0;
            this.entriesUsed = false;
        }

        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> iterable) {
            if (iterable instanceof Collection) {
                ensureCapacity(((Collection) iterable).size() + this.size);
            }
            Iterator<? extends Map.Entry<? extends K, ? extends V>> it = iterable.iterator();
            while (it.hasNext()) {
                put(it.next());
            }
            return this;
        }

        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
            return put(entry.getKey(), entry.getValue());
        }

        public ImmutableMap<K, V> build() {
            return buildOrThrow();
        }
    }

    public static abstract class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> {
        @Override // com.google.common.collect.ImmutableMap
        public ImmutableSet<Map.Entry<K, V>> createEntrySet() {
            return new ImmutableMapEntrySet<K, V>() { // from class: com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap.1EntrySetImpl
                @Override // com.google.common.collect.ImmutableMapEntrySet
                public ImmutableMap<K, V> map() {
                    return IteratorBasedImmutableMap.this;
                }

                @Override // com.google.common.collect.ImmutableMapEntrySet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
                @J2ktIncompatible
                @GwtIncompatible
                public Object writeReplace() {
                    return super.writeReplace();
                }

                @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set, java.util.NavigableSet, com.google.common.collect.SortedIterable
                public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                    return IteratorBasedImmutableMap.this.entryIterator();
                }
            };
        }

        @Override // com.google.common.collect.ImmutableMap
        public ImmutableSet<K> createKeySet() {
            return new ImmutableMapKeySet(this);
        }

        @Override // com.google.common.collect.ImmutableMap
        public ImmutableCollection<V> createValues() {
            return new ImmutableMapValues(this);
        }

        public abstract UnmodifiableIterator<Map.Entry<K, V>> entryIterator();

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Set entrySet() {
            return super.entrySet();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Set keySet() {
            return super.keySet();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map, com.google.common.collect.BiMap
        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }

        @Override // com.google.common.collect.ImmutableMap
        @J2ktIncompatible
        @GwtIncompatible
        public Object writeReplace() {
            return super.writeReplace();
        }
    }

    public final class MapViewOfValuesAsSingletonSets extends IteratorBasedImmutableMap<K, ImmutableSet<V>> {
        private MapViewOfValuesAsSingletonSets() {
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public boolean containsKey(Object obj) {
            return ImmutableMap.this.containsKey(obj);
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        public ImmutableSet<K> createKeySet() {
            return ImmutableMap.this.keySet();
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap
        public UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> entryIterator() {
            final UnmodifiableIterator<Map.Entry<K, V>> it = ImmutableMap.this.entrySet().iterator();
            return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>(this) { // from class: com.google.common.collect.ImmutableMap.MapViewOfValuesAsSingletonSets.1
                final /* synthetic */ MapViewOfValuesAsSingletonSets this$1;

                {
                    this.this$1 = this;
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override // java.util.Iterator
                public Map.Entry<K, ImmutableSet<V>> next() {
                    final Map.Entry entry = (Map.Entry) it.next();
                    return new AbstractMapEntry<K, ImmutableSet<V>>(this) { // from class: com.google.common.collect.ImmutableMap.MapViewOfValuesAsSingletonSets.1.1
                        final /* synthetic */ AnonymousClass1 this$2;

                        {
                            this.this$2 = this;
                        }

                        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                        public K getKey() {
                            return (K) entry.getKey();
                        }

                        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                        public ImmutableSet<V> getValue() {
                            return ImmutableSet.of(entry.getValue());
                        }
                    };
                }
            };
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public int hashCode() {
            return ImmutableMap.this.hashCode();
        }

        @Override // com.google.common.collect.ImmutableMap
        public boolean isHashCodeFast() {
            return ImmutableMap.this.isHashCodeFast();
        }

        @Override // com.google.common.collect.ImmutableMap
        public boolean isPartialView() {
            return ImmutableMap.this.isPartialView();
        }

        @Override // java.util.Map
        public int size() {
            return ImmutableMap.this.size();
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        @J2ktIncompatible
        @GwtIncompatible
        public Object writeReplace() {
            return super.writeReplace();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public ImmutableSet<V> get(Object obj) {
            Object obj2 = ImmutableMap.this.get(obj);
            if (obj2 == null) {
                return null;
            }
            return ImmutableSet.of(obj2);
        }
    }

    @J2ktIncompatible
    public static class SerializedForm<K, V> implements Serializable {
        private static final boolean USE_LEGACY_SERIALIZATION = true;

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;
        private final Object keys;
        private final Object values;

        public SerializedForm(ImmutableMap<K, V> immutableMap) {
            Object[] objArr = new Object[immutableMap.size()];
            Object[] objArr2 = new Object[immutableMap.size()];
            UnmodifiableIterator<Map.Entry<K, V>> it = immutableMap.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Map.Entry<K, V> next = it.next();
                objArr[i] = next.getKey();
                objArr2[i] = next.getValue();
                i++;
            }
            this.keys = objArr;
            this.values = objArr2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final Object legacyReadResolve() {
            Object[] objArr = (Object[]) this.keys;
            Object[] objArr2 = (Object[]) this.values;
            Builder<K, V> builderMakeBuilder = makeBuilder(objArr.length);
            for (int i = 0; i < objArr.length; i++) {
                builderMakeBuilder.put(objArr[i], objArr2[i]);
            }
            return builderMakeBuilder.buildOrThrow();
        }

        public Builder<K, V> makeBuilder(int i) {
            return new Builder<>(i);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final Object readResolve() {
            Object obj = this.keys;
            if (!(obj instanceof ImmutableSet)) {
                return legacyReadResolve();
            }
            ImmutableSet immutableSet = (ImmutableSet) obj;
            ImmutableCollection immutableCollection = (ImmutableCollection) this.values;
            Builder<K, V> builderMakeBuilder = makeBuilder(immutableSet.size());
            UnmodifiableIterator it = immutableSet.iterator();
            UnmodifiableIterator it2 = immutableCollection.iterator();
            while (it.hasNext()) {
                builderMakeBuilder.put(it.next(), it2.next());
            }
            return builderMakeBuilder.buildOrThrow();
        }
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static <K, V> Builder<K, V> builderWithExpectedSize(int i) {
        CollectPreconditions.checkNonnegative(i, "expectedSize");
        return new Builder<>(i);
    }

    public static void checkNoConflict(boolean z, String str, Object obj, Object obj2) {
        if (!z) {
            throw conflictException(str, obj, obj2);
        }
    }

    public static IllegalArgumentException conflictException(String str, Object obj, Object obj2) {
        return new IllegalArgumentException("Multiple entries with same " + str + ": " + obj + " and " + obj2);
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if ((map instanceof ImmutableMap) && !(map instanceof SortedMap)) {
            ImmutableMap<K, V> immutableMap = (ImmutableMap) map;
            if (!immutableMap.isPartialView()) {
                return immutableMap;
            }
        }
        return copyOf(map.entrySet());
    }

    public static <K, V> Map.Entry<K, V> entryOf(K k, V v) {
        CollectPreconditions.checkEntryNotNull(k, v);
        return new AbstractMap.SimpleImmutableEntry(k, v);
    }

    public static <K, V> ImmutableMap<K, V> of() {
        return (ImmutableMap<K, V>) RegularImmutableMap.EMPTY;
    }

    @SafeVarargs
    public static <K, V> ImmutableMap<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... entryArr) {
        return copyOf(Arrays.asList(entryArr));
    }

    @J2ktIncompatible
    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @IgnoreJRERequirement
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        return CollectCollectors.toImmutableMap(function, function2);
    }

    public ImmutableSetMultimap<K, V> asMultimap() {
        if (isEmpty()) {
            return ImmutableSetMultimap.of();
        }
        ImmutableSetMultimap<K, V> immutableSetMultimap = this.multimapView;
        if (immutableSetMultimap != null) {
            return immutableSetMultimap;
        }
        ImmutableSetMultimap<K, V> immutableSetMultimap2 = new ImmutableSetMultimap<>(new MapViewOfValuesAsSingletonSets(), size(), null);
        this.multimapView = immutableSetMultimap2;
        return immutableSetMultimap2;
    }

    @Override // java.util.Map
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return values().contains(obj);
    }

    public abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();

    public abstract ImmutableSet<K> createKeySet();

    public abstract ImmutableCollection<V> createValues();

    @Override // java.util.Map
    public boolean equals(Object obj) {
        return Maps.equalsImpl(this, obj);
    }

    @Override // java.util.Map
    public abstract V get(Object obj);

    @Override // java.util.Map
    public final V getOrDefault(Object obj, V v) {
        V v2 = get(obj);
        return v2 != null ? v2 : v;
    }

    @Override // java.util.Map
    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean isHashCodeFast() {
        return false;
    }

    public abstract boolean isPartialView();

    public UnmodifiableIterator<K> keyIterator() {
        final UnmodifiableIterator<Map.Entry<K, V>> it = entrySet().iterator();
        return new UnmodifiableIterator<K>(this) { // from class: com.google.common.collect.ImmutableMap.1
            final /* synthetic */ ImmutableMap this$0;

            {
                this.this$0 = this;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override // java.util.Iterator
            public K next() {
                return (K) ((Map.Entry) it.next()).getKey();
            }
        };
    }

    @Override // java.util.Map
    @Deprecated
    public final V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    public final V remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return Maps.toStringImpl(this);
    }

    @J2ktIncompatible
    public Object writeReplace() {
        return new SerializedForm(this);
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v) {
        CollectPreconditions.checkEntryNotNull(k, v);
        return RegularImmutableMap.create(1, new Object[]{k, v});
    }

    @IgnoreJRERequirement
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, BinaryOperator<V> binaryOperator) {
        return CollectCollectors.toImmutableMap(function, function2, binaryOperator);
    }

    @Override // java.util.Map
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        ImmutableSet<Map.Entry<K, V>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        ImmutableSet<Map.Entry<K, V>> immutableSetCreateEntrySet = createEntrySet();
        this.entrySet = immutableSetCreateEntrySet;
        return immutableSetCreateEntrySet;
    }

    @Override // java.util.Map
    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.keySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        ImmutableSet<K> immutableSetCreateKeySet = createKeySet();
        this.keySet = immutableSetCreateKeySet;
        return immutableSetCreateKeySet;
    }

    @Override // java.util.Map, com.google.common.collect.BiMap
    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        ImmutableCollection<V> immutableCollectionCreateValues = createValues();
        this.values = immutableCollectionCreateValues;
        return immutableCollectionCreateValues;
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2) {
        CollectPreconditions.checkEntryNotNull(k, v);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        return RegularImmutableMap.create(2, new Object[]{k, v, k2, v2});
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> iterable) {
        Builder builder = new Builder(iterable instanceof Collection ? ((Collection) iterable).size() : 4);
        builder.putAll(iterable);
        return builder.build();
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        CollectPreconditions.checkEntryNotNull(k, v);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        return RegularImmutableMap.create(3, new Object[]{k, v, k2, v2, k3, v3});
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        CollectPreconditions.checkEntryNotNull(k, v);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        return RegularImmutableMap.create(4, new Object[]{k, v, k2, v2, k3, v3, k4, v4});
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        CollectPreconditions.checkEntryNotNull(k, v);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        return RegularImmutableMap.create(5, new Object[]{k, v, k2, v2, k3, v3, k4, v4, k5, v5});
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        CollectPreconditions.checkEntryNotNull(k, v);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        CollectPreconditions.checkEntryNotNull(k6, v6);
        return RegularImmutableMap.create(6, new Object[]{k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6});
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        CollectPreconditions.checkEntryNotNull(k, v);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        CollectPreconditions.checkEntryNotNull(k6, v6);
        CollectPreconditions.checkEntryNotNull(k7, v7);
        return RegularImmutableMap.create(7, new Object[]{k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7});
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        CollectPreconditions.checkEntryNotNull(k, v);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        CollectPreconditions.checkEntryNotNull(k6, v6);
        CollectPreconditions.checkEntryNotNull(k7, v7);
        CollectPreconditions.checkEntryNotNull(k8, v8);
        return RegularImmutableMap.create(8, new Object[]{k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8});
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        CollectPreconditions.checkEntryNotNull(k, v);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        CollectPreconditions.checkEntryNotNull(k6, v6);
        CollectPreconditions.checkEntryNotNull(k7, v7);
        CollectPreconditions.checkEntryNotNull(k8, v8);
        CollectPreconditions.checkEntryNotNull(k9, v9);
        return RegularImmutableMap.create(9, new Object[]{k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9});
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        CollectPreconditions.checkEntryNotNull(k, v);
        CollectPreconditions.checkEntryNotNull(k2, v2);
        CollectPreconditions.checkEntryNotNull(k3, v3);
        CollectPreconditions.checkEntryNotNull(k4, v4);
        CollectPreconditions.checkEntryNotNull(k5, v5);
        CollectPreconditions.checkEntryNotNull(k6, v6);
        CollectPreconditions.checkEntryNotNull(k7, v7);
        CollectPreconditions.checkEntryNotNull(k8, v8);
        CollectPreconditions.checkEntryNotNull(k9, v9);
        CollectPreconditions.checkEntryNotNull(k10, v10);
        return RegularImmutableMap.create(10, new Object[]{k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10});
    }
}
