package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class LinkedHashMultimap<K, V> extends AbstractSetMultimap<K, V> {
    private static final int DEFAULT_KEY_CAPACITY = 16;
    private static final int DEFAULT_VALUE_SET_CAPACITY = 2;

    @VisibleForTesting
    static final double VALUE_SET_LOAD_FACTOR = 1.0d;

    @J2ktIncompatible
    @GwtIncompatible
    private static final long serialVersionUID = 1;
    private transient MultimapIterationChain<K, V> multimapIterationChain;

    @VisibleForTesting
    transient int valueSetCapacity;

    public static final class MultimapIterationChain<K, V> {
        ValueEntry<K, V> firstEntry;
        ValueEntry<K, V> lastEntry;

        private MultimapIterationChain() {
        }

        public void append(ValueEntry<K, V> valueEntry) {
            succeeds(this.lastEntry, valueEntry);
            this.lastEntry = valueEntry;
        }

        public void delete(ValueEntry<K, V> valueEntry) {
            succeeds(((ValueEntry) valueEntry).predecessorInMultimap, ((ValueEntry) valueEntry).successorInMultimap);
        }

        public void succeeds(ValueEntry<K, V> valueEntry, ValueEntry<K, V> valueEntry2) {
            if (valueEntry == null) {
                this.firstEntry = valueEntry2;
            } else {
                ((ValueEntry) valueEntry).successorInMultimap = valueEntry2;
            }
            if (valueEntry2 == null) {
                this.lastEntry = valueEntry;
            } else {
                ((ValueEntry) valueEntry2).predecessorInMultimap = valueEntry;
            }
        }
    }

    @VisibleForTesting
    public static final class ValueEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V> {
        ValueEntry<K, V> nextInValueBucket;
        private ValueEntry<K, V> predecessorInMultimap;
        private ValueEntry<K, V> predecessorInValueSet;
        final int smearedValueHash;
        private ValueEntry<K, V> successorInMultimap;
        private ValueEntry<K, V> successorInValueSet;

        public ValueEntry(@ParametricNullness K k, @ParametricNullness V v, int i, ValueEntry<K, V> valueEntry) {
            super(k, v);
            this.smearedValueHash = i;
            this.nextInValueBucket = valueEntry;
        }

        public boolean matchesValue(Object obj, int i) {
            return this.smearedValueHash == i && Objects.equals(getValue(), obj);
        }
    }

    @VisibleForTesting
    public final class ValueSet extends Sets.ImprovedAbstractSet<V> {
        private ValueEntry<K, V> firstEntry;

        @VisibleForTesting
        ValueEntry<K, V>[] hashTable;

        @ParametricNullness
        private final K key;
        private ValueEntry<K, V> lastEntry;
        private int size = 0;
        private int modCount = 0;

        public ValueSet(@ParametricNullness K k, int i) {
            this.key = k;
            this.hashTable = new ValueEntry[Hashing.closedTableSize(i, LinkedHashMultimap.VALUE_SET_LOAD_FACTOR)];
        }

        private void appendToValueSet(ValueEntry<K, V> valueEntry) {
            succeedsInValueSet(this.lastEntry, valueEntry);
            this.lastEntry = valueEntry;
        }

        private void deleteFromValueSet(ValueEntry<K, V> valueEntry) {
            succeedsInValueSet(((ValueEntry) valueEntry).predecessorInValueSet, ((ValueEntry) valueEntry).successorInValueSet);
        }

        private int mask() {
            return this.hashTable.length - 1;
        }

        private void rehashIfNecessary() {
            if (Hashing.needsResizing(this.size, this.hashTable.length, LinkedHashMultimap.VALUE_SET_LOAD_FACTOR)) {
                int length = this.hashTable.length * 2;
                ValueEntry<K, V>[] valueEntryArr = new ValueEntry[length];
                this.hashTable = valueEntryArr;
                int i = length - 1;
                for (ValueEntry<K, V> valueEntry = this.firstEntry; valueEntry != null; valueEntry = ((ValueEntry) valueEntry).successorInValueSet) {
                    int i2 = valueEntry.smearedValueHash & i;
                    valueEntry.nextInValueBucket = valueEntryArr[i2];
                    valueEntryArr[i2] = valueEntry;
                }
            }
        }

        private void succeedsInValueSet(ValueEntry<K, V> valueEntry, ValueEntry<K, V> valueEntry2) {
            if (valueEntry == null) {
                this.firstEntry = valueEntry2;
            } else {
                ((ValueEntry) valueEntry).successorInValueSet = valueEntry2;
            }
            if (valueEntry2 == null) {
                this.lastEntry = valueEntry;
            } else {
                ((ValueEntry) valueEntry2).predecessorInValueSet = valueEntry;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(@ParametricNullness V v) {
            int iSmearedHash = Hashing.smearedHash(v);
            int iMask = mask() & iSmearedHash;
            ValueEntry<K, V> valueEntry = this.hashTable[iMask];
            for (ValueEntry<K, V> valueEntry2 = valueEntry; valueEntry2 != null; valueEntry2 = valueEntry2.nextInValueBucket) {
                if (valueEntry2.matchesValue(v, iSmearedHash)) {
                    return false;
                }
            }
            ValueEntry<K, V> valueEntry3 = new ValueEntry<>(this.key, v, iSmearedHash, valueEntry);
            appendToValueSet(valueEntry3);
            LinkedHashMultimap.this.multimapIterationChain.append(valueEntry3);
            this.hashTable[iMask] = valueEntry3;
            this.size++;
            this.modCount++;
            rehashIfNecessary();
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Arrays.fill(this.hashTable, (Object) null);
            this.size = 0;
            for (ValueEntry<K, V> valueEntry = this.firstEntry; valueEntry != null; valueEntry = ((ValueEntry) valueEntry).successorInValueSet) {
                LinkedHashMultimap.this.multimapIterationChain.delete(valueEntry);
            }
            this.firstEntry = null;
            this.lastEntry = null;
            this.modCount++;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            int iSmearedHash = Hashing.smearedHash(obj);
            for (ValueEntry<K, V> valueEntry = this.hashTable[mask() & iSmearedHash]; valueEntry != null; valueEntry = valueEntry.nextInValueBucket) {
                if (valueEntry.matchesValue(obj, iSmearedHash)) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return new Iterator<V>() { // from class: com.google.common.collect.LinkedHashMultimap.ValueSet.1
                int expectedModCount;
                ValueEntry<K, V> nextEntry;
                ValueEntry<K, V> toRemove;

                {
                    this.nextEntry = ValueSet.this.firstEntry;
                    this.expectedModCount = ValueSet.this.modCount;
                }

                private void checkForComodification() {
                    if (ValueSet.this.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    checkForComodification();
                    return this.nextEntry != null;
                }

                @Override // java.util.Iterator
                @ParametricNullness
                public V next() {
                    checkForComodification();
                    ValueEntry<K, V> valueEntry = this.nextEntry;
                    if (valueEntry == null) {
                        throw new NoSuchElementException();
                    }
                    V value = valueEntry.getValue();
                    this.toRemove = valueEntry;
                    this.nextEntry = ((ValueEntry) valueEntry).successorInValueSet;
                    return value;
                }

                @Override // java.util.Iterator
                public void remove() {
                    checkForComodification();
                    Preconditions.checkState(this.toRemove != null, "no calls to next() since the last call to remove()");
                    ValueSet.this.remove(this.toRemove.getValue());
                    this.expectedModCount = ValueSet.this.modCount;
                    this.toRemove = null;
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int iSmearedHash = Hashing.smearedHash(obj);
            int iMask = mask() & iSmearedHash;
            ValueEntry<K, V> valueEntry = null;
            for (ValueEntry<K, V> valueEntry2 = this.hashTable[iMask]; valueEntry2 != null; valueEntry2 = valueEntry2.nextInValueBucket) {
                if (valueEntry2.matchesValue(obj, iSmearedHash)) {
                    if (valueEntry == null) {
                        this.hashTable[iMask] = valueEntry2.nextInValueBucket;
                    } else {
                        valueEntry.nextInValueBucket = valueEntry2.nextInValueBucket;
                    }
                    deleteFromValueSet(valueEntry2);
                    LinkedHashMultimap.this.multimapIterationChain.delete(valueEntry2);
                    this.size--;
                    this.modCount++;
                    return true;
                }
                valueEntry = valueEntry2;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.size;
        }
    }

    private LinkedHashMultimap(int i, int i2) {
        super(Platform.newLinkedHashMapWithExpectedSize(i));
        this.multimapIterationChain = new MultimapIterationChain<>();
        CollectPreconditions.checkNonnegative(i2, "expectedValuesPerKey");
        this.valueSetCapacity = i2;
    }

    public static <K, V> LinkedHashMultimap<K, V> create() {
        return new LinkedHashMultimap<>(16, 2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @J2ktIncompatible
    @GwtIncompatible
    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.multimapIterationChain = new MultimapIterationChain<>();
        this.valueSetCapacity = 2;
        int i = objectInputStream.readInt();
        Map mapNewLinkedHashMapWithExpectedSize = Platform.newLinkedHashMapWithExpectedSize(12);
        for (int i2 = 0; i2 < i; i2++) {
            Object object = objectInputStream.readObject();
            mapNewLinkedHashMapWithExpectedSize.put(object, createCollection(object));
        }
        int i3 = objectInputStream.readInt();
        for (int i4 = 0; i4 < i3; i4++) {
            Object object2 = objectInputStream.readObject();
            Object object3 = objectInputStream.readObject();
            Collection collection = (Collection) mapNewLinkedHashMapWithExpectedSize.get(object2);
            Objects.requireNonNull(collection);
            collection.add(object3);
        }
        setMap(mapNewLinkedHashMapWithExpectedSize);
    }

    @J2ktIncompatible
    @GwtIncompatible
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(keySet().size());
        Iterator<K> it = keySet().iterator();
        while (it.hasNext()) {
            objectOutputStream.writeObject(it.next());
        }
        objectOutputStream.writeInt(size());
        for (Map.Entry<K, V> entry : entries()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ Map asMap() {
        return super.asMap();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsEntry(Object obj, Object obj2) {
        return super.containsEntry(obj, obj2);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsKey(Object obj) {
        return super.containsKey(obj);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsValue(Object obj) {
        return super.containsValue(obj);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
    public Iterator<Map.Entry<K, V>> entryIterator() {
        return new Iterator<Map.Entry<K, V>>() { // from class: com.google.common.collect.LinkedHashMultimap.1
            ValueEntry<K, V> nextEntry;
            ValueEntry<K, V> toRemove;

            {
                this.nextEntry = LinkedHashMultimap.this.multimapIterationChain.firstEntry;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.nextEntry != null;
            }

            @Override // java.util.Iterator
            public void remove() {
                Preconditions.checkState(this.toRemove != null, "no calls to next() since the last call to remove()");
                LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
                this.toRemove = null;
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                ValueEntry<K, V> valueEntry = this.nextEntry;
                if (valueEntry == null) {
                    throw new NoSuchElementException();
                }
                this.toRemove = valueEntry;
                this.nextEntry = ((ValueEntry) valueEntry).successorInMultimap;
                return valueEntry;
            }
        };
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ Set get(@ParametricNullness Object obj) {
        return super.get(obj);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Set<K> keySet() {
        return super.keySet();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Multiset keys() {
        return super.keys();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean put(@ParametricNullness Object obj, @ParametricNullness Object obj2) {
        return super.put(obj, obj2);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean putAll(Multimap multimap) {
        return super.putAll(multimap);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean remove(Object obj, Object obj2) {
        return super.remove(obj, obj2);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ Set removeAll(Object obj) {
        return super.removeAll(obj);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    @Override // com.google.common.collect.AbstractMultimap
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
    public Iterator<V> valueIterator() {
        return Maps.valueIterator(entryIterator());
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Collection<V> values() {
        return super.values();
    }

    public static <K, V> LinkedHashMultimap<K, V> create(int i, int i2) {
        return new LinkedHashMultimap<>(Maps.capacity(i), Maps.capacity(i2));
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
    public Set<V> createCollection() {
        return Platform.newLinkedHashSetWithExpectedSize(this.valueSetCapacity);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Set<Map.Entry<K, V>> entries() {
        return super.entries();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean putAll(@ParametricNullness Object obj, Iterable iterable) {
        return super.putAll(obj, iterable);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public Set<V> replaceValues(@ParametricNullness K k, Iterable<? extends V> iterable) {
        return super.replaceValues((Object) k, (Iterable) iterable);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    public Collection<V> createCollection(@ParametricNullness K k) {
        return new ValueSet(k, this.valueSetCapacity);
    }

    public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        LinkedHashMultimap<K, V> linkedHashMultimapCreate = create(multimap.keySet().size(), 2);
        linkedHashMultimapCreate.putAll(multimap);
        return linkedHashMultimapCreate;
    }
}
