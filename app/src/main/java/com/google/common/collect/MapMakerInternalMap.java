package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import com.google.common.collect.MapMakerInternalMap.InternalEntry;
import com.google.common.collect.MapMakerInternalMap.Segment;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
final class MapMakerInternalMap<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final int DRAIN_MAX = 16;
    static final int DRAIN_THRESHOLD = 63;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE = new WeakValueReference<Object, Object, DummyInternalEntry>() { // from class: com.google.common.collect.MapMakerInternalMap.1
        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public void clear() {
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public WeakValueReference<Object, Object, DummyInternalEntry> copyFor(ReferenceQueue<Object> referenceQueue, DummyInternalEntry dummyInternalEntry) {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public Object get() {
            return null;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public DummyInternalEntry getEntry() {
            return null;
        }
    };
    private static final long serialVersionUID = 5;
    final int concurrencyLevel;
    final transient InternalEntryHelper<K, V, E, S> entryHelper;
    transient Set<Map.Entry<K, V>> entrySet;
    final Equivalence<Object> keyEquivalence;
    transient Set<K> keySet;
    final transient int segmentMask;
    final transient int segmentShift;
    final transient Segment<K, V, E, S>[] segments;
    transient Collection<V> values;

    public static abstract class AbstractStrongKeyEntry<K, V, E extends InternalEntry<K, V, E>> implements InternalEntry<K, V, E> {
        final int hash;
        final K key;

        public AbstractStrongKeyEntry(K k, int i) {
            this.key = k;
            this.hash = i;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final int getHash() {
            return this.hash;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public E getNext() {
            return null;
        }
    }

    public static abstract class AbstractWeakKeyEntry<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<K> implements InternalEntry<K, V, E> {
        final int hash;

        public AbstractWeakKeyEntry(ReferenceQueue<K> referenceQueue, K k, int i) {
            super(k, referenceQueue);
            this.hash = i;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final int getHash() {
            return this.hash;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final K getKey() {
            return get();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public E getNext() {
            return null;
        }
    }

    public static final class CleanupMapTask implements Runnable {
        final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;

        public CleanupMapTask(MapMakerInternalMap<?, ?, ?, ?> mapMakerInternalMap) {
            this.mapReference = new WeakReference<>(mapMakerInternalMap);
        }

        @Override // java.lang.Runnable
        public void run() {
            MapMakerInternalMap<?, ?, ?, ?> mapMakerInternalMap = this.mapReference.get();
            if (mapMakerInternalMap == null) {
                throw new CancellationException();
            }
            for (Segment<?, ?, E, S> segment : mapMakerInternalMap.segments) {
                segment.runCleanup();
            }
        }
    }

    public static final class DummyInternalEntry implements InternalEntry<Object, Object, DummyInternalEntry> {
        private DummyInternalEntry() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public int getHash() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public Object getKey() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public Object getValue() {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public DummyInternalEntry getNext() {
            throw new AssertionError();
        }
    }

    public final class EntryIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<Map.Entry<K, V>> {
        public EntryIterator() {
            super();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.HashIterator, java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    public final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        public EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            MapMakerInternalMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            Map.Entry entry;
            Object key;
            Object obj2;
            return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && (obj2 = MapMakerInternalMap.this.get(key)) != null && MapMakerInternalMap.this.valueEquivalence().equivalent(entry.getValue(), obj2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Map.Entry entry;
            Object key;
            return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && MapMakerInternalMap.this.remove(key, entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return MapMakerInternalMap.this.size();
        }
    }

    public abstract class HashIterator<T> implements Iterator<T> {
        Segment<K, V, E, S> currentSegment;
        AtomicReferenceArray<E> currentTable;
        MapMakerInternalMap<K, V, E, S>.WriteThroughEntry lastReturned;
        E nextEntry;
        MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex = -1;

        public HashIterator() {
            this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
            advance();
        }

        public final void advance() {
            this.nextExternal = null;
            if (nextInChain() || nextInTable()) {
                return;
            }
            while (true) {
                int i = this.nextSegmentIndex;
                if (i < 0) {
                    return;
                }
                Segment<K, V, E, S>[] segmentArr = MapMakerInternalMap.this.segments;
                this.nextSegmentIndex = i - 1;
                Segment<K, V, E, S> segment = segmentArr[i];
                this.currentSegment = segment;
                if (segment.count != 0) {
                    this.currentTable = this.currentSegment.table;
                    this.nextTableIndex = r0.length() - 1;
                    if (nextInTable()) {
                        return;
                    }
                }
            }
        }

        public boolean advanceTo(E e) {
            try {
                Object key = e.getKey();
                Object liveValue = MapMakerInternalMap.this.getLiveValue(e);
                if (liveValue == null) {
                    this.currentSegment.postReadCleanup();
                    return false;
                }
                this.nextExternal = new WriteThroughEntry(key, liveValue);
                this.currentSegment.postReadCleanup();
                return true;
            } catch (Throwable th) {
                this.currentSegment.postReadCleanup();
                throw th;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextExternal != null;
        }

        @Override // java.util.Iterator
        public abstract T next();

        public MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry() {
            MapMakerInternalMap<K, V, E, S>.WriteThroughEntry writeThroughEntry = this.nextExternal;
            if (writeThroughEntry == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = writeThroughEntry;
            advance();
            return this.lastReturned;
        }

        public boolean nextInChain() {
            E e = this.nextEntry;
            if (e == null) {
                return false;
            }
            while (true) {
                this.nextEntry = (E) e.getNext();
                E e2 = this.nextEntry;
                if (e2 == null) {
                    return false;
                }
                if (advanceTo(e2)) {
                    return true;
                }
                e = this.nextEntry;
            }
        }

        public boolean nextInTable() {
            while (true) {
                int i = this.nextTableIndex;
                if (i < 0) {
                    return false;
                }
                AtomicReferenceArray<E> atomicReferenceArray = this.currentTable;
                this.nextTableIndex = i - 1;
                E e = atomicReferenceArray.get(i);
                this.nextEntry = e;
                if (e != null && (advanceTo(e) || nextInChain())) {
                    return true;
                }
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            CollectPreconditions.checkRemove(this.lastReturned != null);
            MapMakerInternalMap.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }

    public interface InternalEntry<K, V, E extends InternalEntry<K, V, E>> {
        int getHash();

        K getKey();

        E getNext();

        V getValue();
    }

    public interface InternalEntryHelper<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> {
        E copy(S s, E e, E e2);

        Strength keyStrength();

        E newEntry(S s, K k, int i, E e);

        S newSegment(MapMakerInternalMap<K, V, E, S> mapMakerInternalMap, int i);

        void setValue(S s, E e, V v);

        Strength valueStrength();
    }

    public final class KeyIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<K> {
        public KeyIterator() {
            super();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.HashIterator, java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }
    }

    public final class KeySet extends AbstractSet<K> {
        public KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            MapMakerInternalMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return MapMakerInternalMap.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return MapMakerInternalMap.this.remove(obj) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return MapMakerInternalMap.this.size();
        }
    }

    public static final class SerializationProxy<K, V> extends AbstractSerializationProxy<K, V> {
        private static final long serialVersionUID = 3;

        public SerializationProxy(Strength strength, Strength strength2, Equivalence<Object> equivalence, Equivalence<Object> equivalence2, int i, ConcurrentMap<K, V> concurrentMap) {
            super(strength, strength2, equivalence, equivalence2, i, concurrentMap);
        }

        @J2ktIncompatible
        private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();
            this.delegate = readMapMaker(objectInputStream).makeMap();
            readEntries(objectInputStream);
        }

        private Object readResolve() {
            return this.delegate;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            writeMapTo(objectOutputStream);
        }
    }

    public enum Strength {
        STRONG { // from class: com.google.common.collect.MapMakerInternalMap.Strength.1
            @Override // com.google.common.collect.MapMakerInternalMap.Strength
            public Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        },
        WEAK { // from class: com.google.common.collect.MapMakerInternalMap.Strength.2
            @Override // com.google.common.collect.MapMakerInternalMap.Strength
            public Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };

        public abstract Equivalence<Object> defaultEquivalence();
    }

    public static class StrongKeyDummyValueEntry<K> extends AbstractStrongKeyEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> {

        public static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> {
            private static final Helper<?> INSTANCE = new Helper<>();

            public static <K> Helper<K> instance() {
                return (Helper<K>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public void setValue(StrongKeyDummyValueSegment<K> strongKeyDummyValueSegment, StrongKeyDummyValueEntry<K> strongKeyDummyValueEntry, MapMaker.Dummy dummy) {
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyDummyValueEntry<K> copy(StrongKeyDummyValueSegment<K> strongKeyDummyValueSegment, StrongKeyDummyValueEntry<K> strongKeyDummyValueEntry, StrongKeyDummyValueEntry<K> strongKeyDummyValueEntry2) {
                return newEntry((StrongKeyDummyValueSegment) strongKeyDummyValueSegment, (Object) strongKeyDummyValueEntry.key, strongKeyDummyValueEntry.hash, (StrongKeyDummyValueEntry) strongKeyDummyValueEntry2);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyDummyValueEntry<K> newEntry(StrongKeyDummyValueSegment<K> strongKeyDummyValueSegment, K k, int i, StrongKeyDummyValueEntry<K> strongKeyDummyValueEntry) {
                return strongKeyDummyValueEntry == null ? new StrongKeyDummyValueEntry<>(k, i) : new LinkedStrongKeyDummyValueEntry(k, i, strongKeyDummyValueEntry);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> mapMakerInternalMap, int i) {
                return new StrongKeyDummyValueSegment<>(mapMakerInternalMap, i);
            }
        }

        public static final class LinkedStrongKeyDummyValueEntry<K> extends StrongKeyDummyValueEntry<K> {
            private final StrongKeyDummyValueEntry<K> next;

            public LinkedStrongKeyDummyValueEntry(K k, int i, StrongKeyDummyValueEntry<K> strongKeyDummyValueEntry) {
                super(k, i);
                this.next = strongKeyDummyValueEntry;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.StrongKeyDummyValueEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public /* bridge */ /* synthetic */ Object getValue() {
                return getValue();
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractStrongKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public StrongKeyDummyValueEntry<K> getNext() {
                return this.next;
            }
        }

        private StrongKeyDummyValueEntry(K k, int i) {
            super(k, i);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final MapMaker.Dummy getValue() {
            return MapMaker.Dummy.VALUE;
        }
    }

    public static final class StrongKeyDummyValueSegment<K> extends Segment<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> {
        public StrongKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> mapMakerInternalMap, int i) {
            super(mapMakerInternalMap, i);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyDummyValueSegment<K> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyDummyValueEntry<K> castForTesting(InternalEntry<K, MapMaker.Dummy, ?> internalEntry) {
            return (StrongKeyDummyValueEntry) internalEntry;
        }
    }

    public static class StrongKeyStrongValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>> {
        private volatile V value;

        public static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper<>();

            public static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueSegment<K, V> strongKeyStrongValueSegment, StrongKeyStrongValueEntry<K, V> strongKeyStrongValueEntry, StrongKeyStrongValueEntry<K, V> strongKeyStrongValueEntry2) {
                StrongKeyStrongValueEntry<K, V> strongKeyStrongValueEntryNewEntry = newEntry((StrongKeyStrongValueSegment) strongKeyStrongValueSegment, (Object) strongKeyStrongValueEntry.key, strongKeyStrongValueEntry.hash, (StrongKeyStrongValueEntry) strongKeyStrongValueEntry2);
                ((StrongKeyStrongValueEntry) strongKeyStrongValueEntryNewEntry).value = ((StrongKeyStrongValueEntry) strongKeyStrongValueEntry).value;
                return strongKeyStrongValueEntryNewEntry;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyStrongValueEntry<K, V> newEntry(StrongKeyStrongValueSegment<K, V> strongKeyStrongValueSegment, K k, int i, StrongKeyStrongValueEntry<K, V> strongKeyStrongValueEntry) {
                return strongKeyStrongValueEntry == null ? new StrongKeyStrongValueEntry<>(k, i) : new LinkedStrongKeyStrongValueEntry(k, i, strongKeyStrongValueEntry);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> mapMakerInternalMap, int i) {
                return new StrongKeyStrongValueSegment<>(mapMakerInternalMap, i);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public void setValue(StrongKeyStrongValueSegment<K, V> strongKeyStrongValueSegment, StrongKeyStrongValueEntry<K, V> strongKeyStrongValueEntry, V v) {
                ((StrongKeyStrongValueEntry) strongKeyStrongValueEntry).value = v;
            }
        }

        public static final class LinkedStrongKeyStrongValueEntry<K, V> extends StrongKeyStrongValueEntry<K, V> {
            private final StrongKeyStrongValueEntry<K, V> next;

            public LinkedStrongKeyStrongValueEntry(K k, int i, StrongKeyStrongValueEntry<K, V> strongKeyStrongValueEntry) {
                super(k, i);
                this.next = strongKeyStrongValueEntry;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractStrongKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public StrongKeyStrongValueEntry<K, V> getNext() {
                return this.next;
            }
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final V getValue() {
            return this.value;
        }

        private StrongKeyStrongValueEntry(K k, int i) {
            super(k, i);
            this.value = null;
        }
    }

    public static final class StrongKeyStrongValueSegment<K, V> extends Segment<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> {
        public StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> mapMakerInternalMap, int i) {
            super(mapMakerInternalMap, i);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyStrongValueSegment<K, V> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyStrongValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> internalEntry) {
            return (StrongKeyStrongValueEntry) internalEntry;
        }
    }

    public static class StrongKeyWeakValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>> {
        private volatile WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference;

        public static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper<>();

            public static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyWeakValueEntry<K, V> copy(StrongKeyWeakValueSegment<K, V> strongKeyWeakValueSegment, StrongKeyWeakValueEntry<K, V> strongKeyWeakValueEntry, StrongKeyWeakValueEntry<K, V> strongKeyWeakValueEntry2) {
                if (Segment.isCollected(strongKeyWeakValueEntry)) {
                    return null;
                }
                StrongKeyWeakValueEntry<K, V> strongKeyWeakValueEntryNewEntry = newEntry((StrongKeyWeakValueSegment) strongKeyWeakValueSegment, (Object) strongKeyWeakValueEntry.key, strongKeyWeakValueEntry.hash, (StrongKeyWeakValueEntry) strongKeyWeakValueEntry2);
                ((StrongKeyWeakValueEntry) strongKeyWeakValueEntryNewEntry).valueReference = ((StrongKeyWeakValueEntry) strongKeyWeakValueEntry).valueReference.copyFor(((StrongKeyWeakValueSegment) strongKeyWeakValueSegment).queueForValues, strongKeyWeakValueEntryNewEntry);
                return strongKeyWeakValueEntryNewEntry;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyWeakValueEntry<K, V> newEntry(StrongKeyWeakValueSegment<K, V> strongKeyWeakValueSegment, K k, int i, StrongKeyWeakValueEntry<K, V> strongKeyWeakValueEntry) {
                return strongKeyWeakValueEntry == null ? new StrongKeyWeakValueEntry<>(k, i) : new LinkedStrongKeyWeakValueEntry(k, i, strongKeyWeakValueEntry);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> mapMakerInternalMap, int i) {
                return new StrongKeyWeakValueSegment<>(mapMakerInternalMap, i);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public void setValue(StrongKeyWeakValueSegment<K, V> strongKeyWeakValueSegment, StrongKeyWeakValueEntry<K, V> strongKeyWeakValueEntry, V v) {
                WeakValueReference weakValueReference = ((StrongKeyWeakValueEntry) strongKeyWeakValueEntry).valueReference;
                ((StrongKeyWeakValueEntry) strongKeyWeakValueEntry).valueReference = new WeakValueReferenceImpl(((StrongKeyWeakValueSegment) strongKeyWeakValueSegment).queueForValues, v, strongKeyWeakValueEntry);
                weakValueReference.clear();
            }
        }

        public static final class LinkedStrongKeyWeakValueEntry<K, V> extends StrongKeyWeakValueEntry<K, V> {
            private final StrongKeyWeakValueEntry<K, V> next;

            public LinkedStrongKeyWeakValueEntry(K k, int i, StrongKeyWeakValueEntry<K, V> strongKeyWeakValueEntry) {
                super(k, i);
                this.next = strongKeyWeakValueEntry;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractStrongKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public StrongKeyWeakValueEntry<K, V> getNext() {
                return this.next;
            }
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final V getValue() {
            return this.valueReference.get();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueEntry
        public final WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }

        private StrongKeyWeakValueEntry(K k, int i) {
            super(k, i);
            this.valueReference = MapMakerInternalMap.unsetWeakValueReference();
        }
    }

    public static final class StrongKeyWeakValueSegment<K, V> extends Segment<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> {
        private final ReferenceQueue<V> queueForValues;

        public StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> mapMakerInternalMap, int i) {
            super(mapMakerInternalMap, i);
            this.queueForValues = new ReferenceQueue<>();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public ReferenceQueue<V> getValueReferenceQueueForTesting() {
            return this.queueForValues;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry) {
            return castForTesting((InternalEntry) internalEntry).getValueReference();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForValues);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void maybeDrainReferenceQueues() {
            drainValueReferenceQueue(this.queueForValues);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry, V v) {
            return new WeakValueReferenceImpl(this.queueForValues, v, castForTesting((InternalEntry) internalEntry));
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyWeakValueSegment<K, V> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> weakValueReference) {
            StrongKeyWeakValueEntry<K, V> strongKeyWeakValueEntryCastForTesting = castForTesting((InternalEntry) internalEntry);
            WeakValueReference weakValueReference2 = ((StrongKeyWeakValueEntry) strongKeyWeakValueEntryCastForTesting).valueReference;
            ((StrongKeyWeakValueEntry) strongKeyWeakValueEntryCastForTesting).valueReference = weakValueReference;
            weakValueReference2.clear();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public StrongKeyWeakValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> internalEntry) {
            return (StrongKeyWeakValueEntry) internalEntry;
        }
    }

    public interface StrongValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
    }

    public final class ValueIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<V> {
        public ValueIterator() {
            super();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.HashIterator, java.util.Iterator
        public V next() {
            return nextEntry().getValue();
        }
    }

    public final class Values extends AbstractCollection<V> {
        public Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            MapMakerInternalMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return MapMakerInternalMap.this.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return MapMakerInternalMap.this.size();
        }
    }

    public static class WeakKeyDummyValueEntry<K> extends AbstractWeakKeyEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>> {

        public static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> {
            private static final Helper<?> INSTANCE = new Helper<>();

            public static <K> Helper<K> instance() {
                return (Helper<K>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public void setValue(WeakKeyDummyValueSegment<K> weakKeyDummyValueSegment, WeakKeyDummyValueEntry<K> weakKeyDummyValueEntry, MapMaker.Dummy dummy) {
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyDummyValueEntry<K> copy(WeakKeyDummyValueSegment<K> weakKeyDummyValueSegment, WeakKeyDummyValueEntry<K> weakKeyDummyValueEntry, WeakKeyDummyValueEntry<K> weakKeyDummyValueEntry2) {
                K key = weakKeyDummyValueEntry.getKey();
                if (key == null) {
                    return null;
                }
                return newEntry((WeakKeyDummyValueSegment) weakKeyDummyValueSegment, (Object) key, weakKeyDummyValueEntry.hash, (WeakKeyDummyValueEntry) weakKeyDummyValueEntry2);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyDummyValueEntry<K> newEntry(WeakKeyDummyValueSegment<K> weakKeyDummyValueSegment, K k, int i, WeakKeyDummyValueEntry<K> weakKeyDummyValueEntry) {
                return weakKeyDummyValueEntry == null ? new WeakKeyDummyValueEntry<>(((WeakKeyDummyValueSegment) weakKeyDummyValueSegment).queueForKeys, k, i) : new LinkedWeakKeyDummyValueEntry(((WeakKeyDummyValueSegment) weakKeyDummyValueSegment).queueForKeys, k, i, weakKeyDummyValueEntry);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> mapMakerInternalMap, int i) {
                return new WeakKeyDummyValueSegment<>(mapMakerInternalMap, i);
            }
        }

        public static final class LinkedWeakKeyDummyValueEntry<K> extends WeakKeyDummyValueEntry<K> {
            private final WeakKeyDummyValueEntry<K> next;

            @Override // com.google.common.collect.MapMakerInternalMap.WeakKeyDummyValueEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public /* bridge */ /* synthetic */ Object getValue() {
                return getValue();
            }

            private LinkedWeakKeyDummyValueEntry(ReferenceQueue<K> referenceQueue, K k, int i, WeakKeyDummyValueEntry<K> weakKeyDummyValueEntry) {
                super(referenceQueue, k, i);
                this.next = weakKeyDummyValueEntry;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractWeakKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public WeakKeyDummyValueEntry<K> getNext() {
                return this.next;
            }
        }

        private WeakKeyDummyValueEntry(ReferenceQueue<K> referenceQueue, K k, int i) {
            super(referenceQueue, k, i);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final MapMaker.Dummy getValue() {
            return MapMaker.Dummy.VALUE;
        }
    }

    public static final class WeakKeyDummyValueSegment<K> extends Segment<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> {
        private final ReferenceQueue<K> queueForKeys;

        public WeakKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> mapMakerInternalMap, int i) {
            super(mapMakerInternalMap, i);
            this.queueForKeys = new ReferenceQueue<>();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyDummyValueSegment<K> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyDummyValueEntry<K> castForTesting(InternalEntry<K, MapMaker.Dummy, ?> internalEntry) {
            return (WeakKeyDummyValueEntry) internalEntry;
        }
    }

    public static class WeakKeyStrongValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>> {
        private volatile V value;

        public static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper<>();

            public static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.STRONG;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyStrongValueEntry<K, V> copy(WeakKeyStrongValueSegment<K, V> weakKeyStrongValueSegment, WeakKeyStrongValueEntry<K, V> weakKeyStrongValueEntry, WeakKeyStrongValueEntry<K, V> weakKeyStrongValueEntry2) {
                K key = weakKeyStrongValueEntry.getKey();
                if (key == null) {
                    return null;
                }
                WeakKeyStrongValueEntry<K, V> weakKeyStrongValueEntryNewEntry = newEntry((WeakKeyStrongValueSegment) weakKeyStrongValueSegment, (Object) key, weakKeyStrongValueEntry.hash, (WeakKeyStrongValueEntry) weakKeyStrongValueEntry2);
                ((WeakKeyStrongValueEntry) weakKeyStrongValueEntryNewEntry).value = ((WeakKeyStrongValueEntry) weakKeyStrongValueEntry).value;
                return weakKeyStrongValueEntryNewEntry;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyStrongValueEntry<K, V> newEntry(WeakKeyStrongValueSegment<K, V> weakKeyStrongValueSegment, K k, int i, WeakKeyStrongValueEntry<K, V> weakKeyStrongValueEntry) {
                return weakKeyStrongValueEntry == null ? new WeakKeyStrongValueEntry<>(((WeakKeyStrongValueSegment) weakKeyStrongValueSegment).queueForKeys, k, i) : new LinkedWeakKeyStrongValueEntry(((WeakKeyStrongValueSegment) weakKeyStrongValueSegment).queueForKeys, k, i, weakKeyStrongValueEntry);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> mapMakerInternalMap, int i) {
                return new WeakKeyStrongValueSegment<>(mapMakerInternalMap, i);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public void setValue(WeakKeyStrongValueSegment<K, V> weakKeyStrongValueSegment, WeakKeyStrongValueEntry<K, V> weakKeyStrongValueEntry, V v) {
                ((WeakKeyStrongValueEntry) weakKeyStrongValueEntry).value = v;
            }
        }

        public static final class LinkedWeakKeyStrongValueEntry<K, V> extends WeakKeyStrongValueEntry<K, V> {
            private final WeakKeyStrongValueEntry<K, V> next;

            private LinkedWeakKeyStrongValueEntry(ReferenceQueue<K> referenceQueue, K k, int i, WeakKeyStrongValueEntry<K, V> weakKeyStrongValueEntry) {
                super(referenceQueue, k, i);
                this.next = weakKeyStrongValueEntry;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractWeakKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public WeakKeyStrongValueEntry<K, V> getNext() {
                return this.next;
            }
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final V getValue() {
            return this.value;
        }

        private WeakKeyStrongValueEntry(ReferenceQueue<K> referenceQueue, K k, int i) {
            super(referenceQueue, k, i);
            this.value = null;
        }
    }

    public static final class WeakKeyStrongValueSegment<K, V> extends Segment<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> {
        private final ReferenceQueue<K> queueForKeys;

        public WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> mapMakerInternalMap, int i) {
            super(mapMakerInternalMap, i);
            this.queueForKeys = new ReferenceQueue<>();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyStrongValueSegment<K, V> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyStrongValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> internalEntry) {
            return (WeakKeyStrongValueEntry) internalEntry;
        }
    }

    public static class WeakKeyWeakValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>> {
        private volatile WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference;

        public static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> {
            private static final Helper<?, ?> INSTANCE = new Helper<>();

            public static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>) INSTANCE;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength keyStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public Strength valueStrength() {
                return Strength.WEAK;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyWeakValueEntry<K, V> copy(WeakKeyWeakValueSegment<K, V> weakKeyWeakValueSegment, WeakKeyWeakValueEntry<K, V> weakKeyWeakValueEntry, WeakKeyWeakValueEntry<K, V> weakKeyWeakValueEntry2) {
                K key = weakKeyWeakValueEntry.getKey();
                if (key == null || Segment.isCollected(weakKeyWeakValueEntry)) {
                    return null;
                }
                WeakKeyWeakValueEntry<K, V> weakKeyWeakValueEntryNewEntry = newEntry((WeakKeyWeakValueSegment) weakKeyWeakValueSegment, (Object) key, weakKeyWeakValueEntry.hash, (WeakKeyWeakValueEntry) weakKeyWeakValueEntry2);
                ((WeakKeyWeakValueEntry) weakKeyWeakValueEntryNewEntry).valueReference = ((WeakKeyWeakValueEntry) weakKeyWeakValueEntry).valueReference.copyFor(((WeakKeyWeakValueSegment) weakKeyWeakValueSegment).queueForValues, weakKeyWeakValueEntryNewEntry);
                return weakKeyWeakValueEntryNewEntry;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyWeakValueEntry<K, V> newEntry(WeakKeyWeakValueSegment<K, V> weakKeyWeakValueSegment, K k, int i, WeakKeyWeakValueEntry<K, V> weakKeyWeakValueEntry) {
                return weakKeyWeakValueEntry == null ? new WeakKeyWeakValueEntry<>(((WeakKeyWeakValueSegment) weakKeyWeakValueSegment).queueForKeys, k, i) : new LinkedWeakKeyWeakValueEntry(((WeakKeyWeakValueSegment) weakKeyWeakValueSegment).queueForKeys, k, i, weakKeyWeakValueEntry);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> mapMakerInternalMap, int i) {
                return new WeakKeyWeakValueSegment<>(mapMakerInternalMap, i);
            }

            @Override // com.google.common.collect.MapMakerInternalMap.InternalEntryHelper
            public void setValue(WeakKeyWeakValueSegment<K, V> weakKeyWeakValueSegment, WeakKeyWeakValueEntry<K, V> weakKeyWeakValueEntry, V v) {
                WeakValueReference weakValueReference = ((WeakKeyWeakValueEntry) weakKeyWeakValueEntry).valueReference;
                ((WeakKeyWeakValueEntry) weakKeyWeakValueEntry).valueReference = new WeakValueReferenceImpl(((WeakKeyWeakValueSegment) weakKeyWeakValueSegment).queueForValues, v, weakKeyWeakValueEntry);
                weakValueReference.clear();
            }
        }

        public static final class LinkedWeakKeyWeakValueEntry<K, V> extends WeakKeyWeakValueEntry<K, V> {
            private final WeakKeyWeakValueEntry<K, V> next;

            public LinkedWeakKeyWeakValueEntry(ReferenceQueue<K> referenceQueue, K k, int i, WeakKeyWeakValueEntry<K, V> weakKeyWeakValueEntry) {
                super(referenceQueue, k, i);
                this.next = weakKeyWeakValueEntry;
            }

            @Override // com.google.common.collect.MapMakerInternalMap.AbstractWeakKeyEntry, com.google.common.collect.MapMakerInternalMap.InternalEntry
            public WeakKeyWeakValueEntry<K, V> getNext() {
                return this.next;
            }
        }

        public WeakKeyWeakValueEntry(ReferenceQueue<K> referenceQueue, K k, int i) {
            super(referenceQueue, k, i);
            this.valueReference = MapMakerInternalMap.unsetWeakValueReference();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.InternalEntry
        public final V getValue() {
            return this.valueReference.get();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueEntry
        public final WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }
    }

    public static final class WeakKeyWeakValueSegment<K, V> extends Segment<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> {
        private final ReferenceQueue<K> queueForKeys;
        private final ReferenceQueue<V> queueForValues;

        public WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> mapMakerInternalMap, int i) {
            super(mapMakerInternalMap, i);
            this.queueForKeys = new ReferenceQueue<>();
            this.queueForValues = new ReferenceQueue<>();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public ReferenceQueue<V> getValueReferenceQueueForTesting() {
            return this.queueForValues;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry) {
            return castForTesting((InternalEntry) internalEntry).getValueReference();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void maybeClearReferenceQueues() {
            clearReferenceQueue(this.queueForKeys);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void maybeDrainReferenceQueues() {
            drainKeyReferenceQueue(this.queueForKeys);
            drainValueReferenceQueue(this.queueForValues);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry, V v) {
            return new WeakValueReferenceImpl(this.queueForValues, v, castForTesting((InternalEntry) internalEntry));
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyWeakValueSegment<K, V> self() {
            return this;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> weakValueReference) {
            WeakKeyWeakValueEntry<K, V> weakKeyWeakValueEntryCastForTesting = castForTesting((InternalEntry) internalEntry);
            WeakValueReference weakValueReference2 = ((WeakKeyWeakValueEntry) weakKeyWeakValueEntryCastForTesting).valueReference;
            ((WeakKeyWeakValueEntry) weakKeyWeakValueEntryCastForTesting).valueReference = weakValueReference;
            weakValueReference2.clear();
        }

        @Override // com.google.common.collect.MapMakerInternalMap.Segment
        public WeakKeyWeakValueEntry<K, V> castForTesting(InternalEntry<K, V, ?> internalEntry) {
            return (WeakKeyWeakValueEntry) internalEntry;
        }
    }

    public interface WeakValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
        WeakValueReference<K, V, E> getValueReference();
    }

    public interface WeakValueReference<K, V, E extends InternalEntry<K, V, E>> {
        void clear();

        WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> referenceQueue, E e);

        V get();

        E getEntry();
    }

    public static final class WeakValueReferenceImpl<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<V> implements WeakValueReference<K, V, E> {
        final E entry;

        public WeakValueReferenceImpl(ReferenceQueue<V> referenceQueue, V v, E e) {
            super(v, referenceQueue);
            this.entry = e;
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> referenceQueue, E e) {
            return new WeakValueReferenceImpl(referenceQueue, get(), e);
        }

        @Override // com.google.common.collect.MapMakerInternalMap.WeakValueReference
        public E getEntry() {
            return this.entry;
        }
    }

    public final class WriteThroughEntry extends AbstractMap.SimpleEntry<K, V> {
        public WriteThroughEntry(K k, V v) {
            super(k, v);
        }

        @Override // java.util.AbstractMap.SimpleEntry, java.util.Map.Entry
        public V setValue(V v) {
            MapMakerInternalMap.this.put(getKey(), v);
            return (V) super.setValue(v);
        }
    }

    private MapMakerInternalMap(MapMaker mapMaker, InternalEntryHelper<K, V, E, S> internalEntryHelper) {
        this.concurrencyLevel = Math.min(mapMaker.getConcurrencyLevel(), MAX_SEGMENTS);
        this.keyEquivalence = mapMaker.getKeyEquivalence();
        this.entryHelper = internalEntryHelper;
        int iMin = Math.min(mapMaker.getInitialCapacity(), 1073741824);
        int i = 0;
        int i2 = 1;
        int i3 = 0;
        int i4 = 1;
        while (i4 < this.concurrencyLevel) {
            i3++;
            i4 <<= 1;
        }
        this.segmentShift = 32 - i3;
        this.segmentMask = i4 - 1;
        this.segments = newSegmentArray(i4);
        int i5 = iMin / i4;
        while (i2 < (i4 * i5 < iMin ? i5 + 1 : i5)) {
            i2 <<= 1;
        }
        while (true) {
            Segment<K, V, E, S>[] segmentArr = this.segments;
            if (i >= segmentArr.length) {
                return;
            }
            segmentArr[i] = createSegment(i2);
            i++;
        }
    }

    public static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(MapMaker mapMaker) {
        Strength keyStrength = mapMaker.getKeyStrength();
        Strength strength = Strength.STRONG;
        if (keyStrength == strength && mapMaker.getValueStrength() == strength) {
            return new MapMakerInternalMap<>(mapMaker, StrongKeyStrongValueEntry.Helper.instance());
        }
        if (mapMaker.getKeyStrength() == strength && mapMaker.getValueStrength() == Strength.WEAK) {
            return new MapMakerInternalMap<>(mapMaker, StrongKeyWeakValueEntry.Helper.instance());
        }
        Strength keyStrength2 = mapMaker.getKeyStrength();
        Strength strength2 = Strength.WEAK;
        if (keyStrength2 == strength2 && mapMaker.getValueStrength() == strength) {
            return new MapMakerInternalMap<>(mapMaker, WeakKeyStrongValueEntry.Helper.instance());
        }
        if (mapMaker.getKeyStrength() == strength2 && mapMaker.getValueStrength() == strength2) {
            return new MapMakerInternalMap<>(mapMaker, WeakKeyWeakValueEntry.Helper.instance());
        }
        throw new AssertionError();
    }

    public static <K> MapMakerInternalMap<K, MapMaker.Dummy, ? extends InternalEntry<K, MapMaker.Dummy, ?>, ?> createWithDummyValues(MapMaker mapMaker) {
        Strength keyStrength = mapMaker.getKeyStrength();
        Strength strength = Strength.STRONG;
        if (keyStrength == strength && mapMaker.getValueStrength() == strength) {
            return new MapMakerInternalMap<>(mapMaker, StrongKeyDummyValueEntry.Helper.instance());
        }
        Strength keyStrength2 = mapMaker.getKeyStrength();
        Strength strength2 = Strength.WEAK;
        if (keyStrength2 == strength2 && mapMaker.getValueStrength() == strength) {
            return new MapMakerInternalMap<>(mapMaker, WeakKeyDummyValueEntry.Helper.instance());
        }
        if (mapMaker.getValueStrength() == strength2) {
            throw new IllegalArgumentException("Map cannot have both weak and dummy values");
        }
        throw new AssertionError();
    }

    @J2ktIncompatible
    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializationProxy");
    }

    public static int rehash(int i) {
        int i2 = i + ((i << 15) ^ (-12931));
        int i3 = i2 ^ (i2 >>> 10);
        int i4 = i3 + (i3 << 3);
        int i5 = i4 ^ (i4 >>> 6);
        int i6 = (i5 << 2) + (i5 << 14) + i5;
        return (i6 >>> 16) ^ i6;
    }

    public static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() {
        return (WeakValueReference<K, V, E>) UNSET_WEAK_VALUE_REFERENCE;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        for (Segment<K, V, E, S> segment : this.segments) {
            segment.clear();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        if (obj == null) {
            return false;
        }
        int iHash = hash(obj);
        return segmentFor(iHash).containsKey(obj, iHash);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0 */
    /* JADX WARN: Type inference failed for: r10v1, types: [int] */
    /* JADX WARN: Type inference failed for: r10v3 */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [int] */
    /* JADX WARN: Type inference failed for: r13v3 */
    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        Segment<K, V, E, S>[] segmentArr = this.segments;
        long j = -1;
        int i = 0;
        while (i < 3) {
            int length = segmentArr.length;
            long j2 = 0;
            for (?? r10 = z; r10 < length; r10++) {
                Segment<K, V, E, S> segment = segmentArr[r10];
                int i2 = segment.count;
                AtomicReferenceArray<E> atomicReferenceArray = segment.table;
                for (?? r13 = z; r13 < atomicReferenceArray.length(); r13++) {
                    E next = atomicReferenceArray.get(r13);
                    while (next != null) {
                        V liveValue = segment.getLiveValue(next);
                        boolean z2 = z;
                        if (liveValue != null && valueEquivalence().equivalent(obj, liveValue)) {
                            return true;
                        }
                        next = next.getNext();
                        z = z2;
                    }
                }
                j2 += (long) segment.modCount;
                z = z;
            }
            boolean z3 = z;
            if (j2 == j) {
                return z3;
            }
            i++;
            j = j2;
            z = z3;
        }
        return z;
    }

    @VisibleForTesting
    public E copyEntry(E e, E e2) {
        return (E) segmentFor(e.getHash()).copyEntry(e, e2);
    }

    public Segment<K, V, E, S> createSegment(int i) {
        return this.entryHelper.newSegment(this, i);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        EntrySet entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        if (obj == null) {
            return null;
        }
        int iHash = hash(obj);
        return segmentFor(iHash).get(obj, iHash);
    }

    public E getEntry(Object obj) {
        if (obj == null) {
            return null;
        }
        int iHash = hash(obj);
        return (E) segmentFor(iHash).getEntry(obj, iHash);
    }

    public V getLiveValue(E e) {
        if (e.getKey() == null) {
            return null;
        }
        return (V) e.getValue();
    }

    public int hash(Object obj) {
        return rehash(this.keyEquivalence.hash(obj));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        Segment<K, V, E, S>[] segmentArr = this.segments;
        long j = 0;
        for (int i = 0; i < segmentArr.length; i++) {
            if (segmentArr[i].count != 0) {
                return false;
            }
            j += (long) segmentArr[i].modCount;
        }
        if (j == 0) {
            return true;
        }
        for (int i2 = 0; i2 < segmentArr.length; i2++) {
            if (segmentArr[i2].count != 0) {
                return false;
            }
            j -= (long) segmentArr[i2].modCount;
        }
        return j == 0;
    }

    @VisibleForTesting
    public boolean isLiveForTesting(InternalEntry<K, V, ?> internalEntry) {
        return segmentFor(internalEntry.getHash()).getLiveValueForTesting(internalEntry) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        KeySet keySet = new KeySet();
        this.keySet = keySet;
        return keySet;
    }

    @VisibleForTesting
    public Strength keyStrength() {
        return this.entryHelper.keyStrength();
    }

    public final Segment<K, V, E, S>[] newSegmentArray(int i) {
        return new Segment[i];
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k, V v) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v);
        int iHash = hash(k);
        return segmentFor(iHash).put(k, iHash, v, false);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V putIfAbsent(K k, V v) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v);
        int iHash = hash(k);
        return segmentFor(iHash).put(k, iHash, v, true);
    }

    public void reclaimKey(E e) {
        int hash = e.getHash();
        segmentFor(hash).reclaimKey(e, hash);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void reclaimValue(WeakValueReference<K, V, E> weakValueReference) {
        InternalEntry entry = weakValueReference.getEntry();
        int hash = entry.getHash();
        segmentFor(hash).reclaimValue(entry.getKey(), hash, weakValueReference);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        if (obj == null) {
            return null;
        }
        int iHash = hash(obj);
        return segmentFor(iHash).remove(obj, iHash);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(K k, V v, V v2) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v2);
        if (v == null) {
            return false;
        }
        int iHash = hash(k);
        return segmentFor(iHash).replace(k, iHash, v, v2);
    }

    public Segment<K, V, E, S> segmentFor(int i) {
        return this.segments[(i >>> this.segmentShift) & this.segmentMask];
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        long j = 0;
        for (Segment<K, V, E, S> segment : this.segments) {
            j += (long) segment.count;
        }
        return Ints.saturatedCast(j);
    }

    @VisibleForTesting
    public Equivalence<Object> valueEquivalence() {
        return this.entryHelper.valueStrength().defaultEquivalence();
    }

    @VisibleForTesting
    public Strength valueStrength() {
        return this.entryHelper.valueStrength();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        Values values = new Values();
        this.values = values;
        return values;
    }

    public Object writeReplace() {
        return new SerializationProxy(this.entryHelper.keyStrength(), this.entryHelper.valueStrength(), this.keyEquivalence, this.entryHelper.valueStrength().defaultEquivalence(), this.concurrencyLevel, this);
    }

    public static abstract class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V> implements Serializable {
        private static final long serialVersionUID = 3;
        final int concurrencyLevel;
        transient ConcurrentMap<K, V> delegate;
        final Equivalence<Object> keyEquivalence;
        final Strength keyStrength;
        final Equivalence<Object> valueEquivalence;
        final Strength valueStrength;

        public AbstractSerializationProxy(Strength strength, Strength strength2, Equivalence<Object> equivalence, Equivalence<Object> equivalence2, int i, ConcurrentMap<K, V> concurrentMap) {
            this.keyStrength = strength;
            this.valueStrength = strength2;
            this.keyEquivalence = equivalence;
            this.valueEquivalence = equivalence2;
            this.concurrencyLevel = i;
            this.delegate = concurrentMap;
        }

        @J2ktIncompatible
        public void readEntries(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
            while (true) {
                Object object = objectInputStream.readObject();
                if (object == null) {
                    return;
                }
                this.delegate.put((K) object, (V) objectInputStream.readObject());
            }
        }

        @J2ktIncompatible
        public MapMaker readMapMaker(ObjectInputStream objectInputStream) throws IOException {
            return new MapMaker().initialCapacity(objectInputStream.readInt()).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
        }

        public void writeMapTo(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.writeInt(this.delegate.size());
            for (Map.Entry<K, V> entry : this.delegate.entrySet()) {
                objectOutputStream.writeObject(entry.getKey());
                objectOutputStream.writeObject(entry.getValue());
            }
            objectOutputStream.writeObject(null);
        }

        @Override // com.google.common.collect.ForwardingConcurrentMap, com.google.common.collect.ForwardingMap, com.google.common.collect.ForwardingObject
        public ConcurrentMap<K, V> delegate() {
            return this.delegate;
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(Object obj, Object obj2) {
        if (obj == null || obj2 == null) {
            return false;
        }
        int iHash = hash(obj);
        return segmentFor(iHash).remove(obj, iHash, obj2);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V replace(K k, V v) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v);
        int iHash = hash(k);
        return segmentFor(iHash).replace(k, iHash, v);
    }

    public static abstract class Segment<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends ReentrantLock {
        volatile int count;
        final MapMakerInternalMap<K, V, E, S> map;
        int modCount;
        final AtomicInteger readCount = new AtomicInteger();
        volatile AtomicReferenceArray<E> table;
        int threshold;

        public Segment(MapMakerInternalMap<K, V, E, S> mapMakerInternalMap, int i) {
            this.map = mapMakerInternalMap;
            initTable(newEntryArray(i));
        }

        public static <K, V, E extends InternalEntry<K, V, E>> boolean isCollected(E e) {
            return e.getValue() == null;
        }

        public abstract E castForTesting(InternalEntry<K, V, ?> internalEntry);

        public void clear() {
            if (this.count != 0) {
                lock();
                try {
                    AtomicReferenceArray<E> atomicReferenceArray = this.table;
                    for (int i = 0; i < atomicReferenceArray.length(); i++) {
                        atomicReferenceArray.set(i, null);
                    }
                    maybeClearReferenceQueues();
                    this.readCount.set(0);
                    this.modCount++;
                    this.count = 0;
                    unlock();
                } catch (Throwable th) {
                    unlock();
                    throw th;
                }
            }
        }

        public <T> void clearReferenceQueue(ReferenceQueue<T> referenceQueue) {
            while (referenceQueue.poll() != null) {
            }
        }

        public boolean clearValueForTesting(K k, int i, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> weakValueReference) {
            lock();
            try {
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                E e = atomicReferenceArray.get(length);
                for (InternalEntry next = e; next != null; next = next.getNext()) {
                    Object key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        if (((WeakValueEntry) next).getValueReference() != weakValueReference) {
                            return false;
                        }
                        atomicReferenceArray.set(length, (E) removeFromChain(e, next));
                        return true;
                    }
                }
                return false;
            } finally {
                unlock();
            }
        }

        public boolean containsKey(Object obj, int i) {
            try {
                boolean z = false;
                if (this.count == 0) {
                    return false;
                }
                InternalEntry liveEntry = getLiveEntry(obj, i);
                if (liveEntry != null) {
                    if (liveEntry.getValue() != null) {
                        z = true;
                    }
                }
                return z;
            } finally {
                postReadCleanup();
            }
        }

        @VisibleForTesting
        public boolean containsValue(Object obj) {
            try {
                if (this.count != 0) {
                    AtomicReferenceArray<E> atomicReferenceArray = this.table;
                    int length = atomicReferenceArray.length();
                    for (int i = 0; i < length; i++) {
                        for (E next = atomicReferenceArray.get(i); next != null; next = next.getNext()) {
                            V liveValue = getLiveValue(next);
                            if (liveValue != null && this.map.valueEquivalence().equivalent(obj, liveValue)) {
                                postReadCleanup();
                                return true;
                            }
                        }
                    }
                }
                return false;
            } finally {
                postReadCleanup();
            }
        }

        public E copyEntry(E e, E e2) {
            return (E) this.map.entryHelper.copy(self(), e, e2);
        }

        public E copyForTesting(InternalEntry<K, V, ?> internalEntry, InternalEntry<K, V, ?> internalEntry2) {
            return (E) this.map.entryHelper.copy(self(), castForTesting(internalEntry), castForTesting(internalEntry2));
        }

        public void drainKeyReferenceQueue(ReferenceQueue<K> referenceQueue) {
            int i = 0;
            do {
                Reference<? extends K> referencePoll = referenceQueue.poll();
                if (referencePoll == null) {
                    return;
                }
                this.map.reclaimKey((InternalEntry) referencePoll);
                i++;
            } while (i != 16);
        }

        public void drainValueReferenceQueue(ReferenceQueue<V> referenceQueue) {
            int i = 0;
            do {
                Reference<? extends V> referencePoll = referenceQueue.poll();
                if (referencePoll == null) {
                    return;
                }
                this.map.reclaimValue((WeakValueReference) referencePoll);
                i++;
            } while (i != 16);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void expand() {
            AtomicReferenceArray<E> atomicReferenceArray = this.table;
            int length = atomicReferenceArray.length();
            if (length >= 1073741824) {
                return;
            }
            int i = this.count;
            AtomicReferenceArray<E> atomicReferenceArrayNewEntryArray = newEntryArray(length << 1);
            this.threshold = (atomicReferenceArrayNewEntryArray.length() * 3) / 4;
            int length2 = atomicReferenceArrayNewEntryArray.length() - 1;
            for (int i2 = 0; i2 < length; i2++) {
                E next = atomicReferenceArray.get(i2);
                if (next != null) {
                    InternalEntry next2 = next.getNext();
                    int hash = next.getHash() & length2;
                    if (next2 == null) {
                        atomicReferenceArrayNewEntryArray.set(hash, next);
                    } else {
                        InternalEntry internalEntry = next;
                        while (next2 != null) {
                            int hash2 = next2.getHash() & length2;
                            if (hash2 != hash) {
                                internalEntry = next2;
                                hash = hash2;
                            }
                            next2 = next2.getNext();
                        }
                        atomicReferenceArrayNewEntryArray.set(hash, internalEntry);
                        while (next != internalEntry) {
                            int hash3 = next.getHash() & length2;
                            InternalEntry internalEntryCopyEntry = copyEntry(next, (InternalEntry) atomicReferenceArrayNewEntryArray.get(hash3));
                            if (internalEntryCopyEntry != null) {
                                atomicReferenceArrayNewEntryArray.set(hash3, internalEntryCopyEntry);
                            } else {
                                i--;
                            }
                            next = next.getNext();
                        }
                    }
                }
            }
            this.table = atomicReferenceArrayNewEntryArray;
            this.count = i;
        }

        public V get(Object obj, int i) {
            try {
                InternalEntry liveEntry = getLiveEntry(obj, i);
                if (liveEntry == null) {
                    postReadCleanup();
                    return null;
                }
                V v = (V) liveEntry.getValue();
                if (v == null) {
                    tryDrainReferenceQueues();
                }
                return v;
            } finally {
                postReadCleanup();
            }
        }

        public E getEntry(Object obj, int i) {
            if (this.count == 0) {
                return null;
            }
            for (E e = (E) getFirst(i); e != null; e = (E) e.getNext()) {
                if (e.getHash() == i) {
                    Object key = e.getKey();
                    if (key == null) {
                        tryDrainReferenceQueues();
                    } else if (this.map.keyEquivalence.equivalent(obj, key)) {
                        return e;
                    }
                }
            }
            return null;
        }

        public E getFirst(int i) {
            return this.table.get(i & (r0.length() - 1));
        }

        public ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            throw new AssertionError();
        }

        public E getLiveEntry(Object obj, int i) {
            return (E) getEntry(obj, i);
        }

        public V getLiveValue(E e) {
            if (e.getKey() == null) {
                tryDrainReferenceQueues();
                return null;
            }
            V v = (V) e.getValue();
            if (v != null) {
                return v;
            }
            tryDrainReferenceQueues();
            return null;
        }

        public V getLiveValueForTesting(InternalEntry<K, V, ?> internalEntry) {
            return getLiveValue(castForTesting(internalEntry));
        }

        public ReferenceQueue<V> getValueReferenceQueueForTesting() {
            throw new AssertionError();
        }

        public WeakValueReference<K, V, E> getWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry) {
            throw new AssertionError();
        }

        public void initTable(AtomicReferenceArray<E> atomicReferenceArray) {
            this.threshold = (atomicReferenceArray.length() * 3) / 4;
            this.table = atomicReferenceArray;
        }

        public void maybeClearReferenceQueues() {
        }

        public void maybeDrainReferenceQueues() {
        }

        public AtomicReferenceArray<E> newEntryArray(int i) {
            return new AtomicReferenceArray<>(i);
        }

        public E newEntryForTesting(K k, int i, InternalEntry<K, V, ?> internalEntry) {
            return (E) this.map.entryHelper.newEntry(self(), k, i, castForTesting(internalEntry));
        }

        public WeakValueReference<K, V, E> newWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry, V v) {
            throw new AssertionError();
        }

        public void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & MapMakerInternalMap.DRAIN_THRESHOLD) == 0) {
                runCleanup();
            }
        }

        public void preWriteCleanup() {
            runLockedCleanup();
        }

        public V put(K k, int i, V v, boolean z) {
            lock();
            try {
                preWriteCleanup();
                int i2 = this.count + 1;
                if (i2 > this.threshold) {
                    expand();
                    i2 = this.count + 1;
                }
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                E e = atomicReferenceArray.get(length);
                for (InternalEntry next = e; next != null; next = next.getNext()) {
                    Object key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        V v2 = (V) next.getValue();
                        if (v2 == null) {
                            this.modCount++;
                            setValue(next, v);
                            this.count = this.count;
                            unlock();
                            return null;
                        }
                        if (z) {
                            unlock();
                            return v2;
                        }
                        this.modCount++;
                        setValue(next, v);
                        unlock();
                        return v2;
                    }
                }
                this.modCount++;
                InternalEntry internalEntryNewEntry = this.map.entryHelper.newEntry(self(), k, i, e);
                setValue(internalEntryNewEntry, v);
                atomicReferenceArray.set(length, (E) internalEntryNewEntry);
                this.count = i2;
                unlock();
                return null;
            } catch (Throwable th) {
                unlock();
                throw th;
            }
        }

        public boolean reclaimKey(E e, int i) {
            lock();
            try {
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = i & (atomicReferenceArray.length() - 1);
                E e2 = atomicReferenceArray.get(length);
                for (InternalEntry next = e2; next != null; next = next.getNext()) {
                    if (next == e) {
                        this.modCount++;
                        InternalEntry internalEntryRemoveFromChain = removeFromChain(e2, next);
                        int i2 = this.count - 1;
                        atomicReferenceArray.set(length, (E) internalEntryRemoveFromChain);
                        this.count = i2;
                        return true;
                    }
                }
                unlock();
                return false;
            } finally {
                unlock();
            }
        }

        public boolean reclaimValue(K k, int i, WeakValueReference<K, V, E> weakValueReference) {
            lock();
            try {
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                E e = atomicReferenceArray.get(length);
                for (InternalEntry next = e; next != null; next = next.getNext()) {
                    Object key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        if (((WeakValueEntry) next).getValueReference() != weakValueReference) {
                            return false;
                        }
                        this.modCount++;
                        InternalEntry internalEntryRemoveFromChain = removeFromChain(e, next);
                        int i2 = this.count - 1;
                        atomicReferenceArray.set(length, (E) internalEntryRemoveFromChain);
                        this.count = i2;
                        return true;
                    }
                }
                return false;
            } finally {
                unlock();
            }
        }

        public V remove(Object obj, int i) {
            lock();
            try {
                preWriteCleanup();
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                E e = atomicReferenceArray.get(length);
                for (InternalEntry next = e; next != null; next = next.getNext()) {
                    Object key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(obj, key)) {
                        V v = (V) next.getValue();
                        if (v == null && !isCollected(next)) {
                            return null;
                        }
                        this.modCount++;
                        InternalEntry internalEntryRemoveFromChain = removeFromChain(e, next);
                        int i2 = this.count - 1;
                        atomicReferenceArray.set(length, (E) internalEntryRemoveFromChain);
                        this.count = i2;
                        return v;
                    }
                }
                return null;
            } finally {
                unlock();
            }
        }

        public boolean removeEntryForTesting(E e) {
            int hash = e.getHash();
            AtomicReferenceArray<E> atomicReferenceArray = this.table;
            int length = hash & (atomicReferenceArray.length() - 1);
            E e2 = atomicReferenceArray.get(length);
            for (InternalEntry next = e2; next != null; next = next.getNext()) {
                if (next == e) {
                    this.modCount++;
                    InternalEntry internalEntryRemoveFromChain = removeFromChain(e2, next);
                    int i = this.count - 1;
                    atomicReferenceArray.set(length, (E) internalEntryRemoveFromChain);
                    this.count = i;
                    return true;
                }
            }
            return false;
        }

        public E removeFromChain(E e, E e2) {
            int i = this.count;
            E e3 = (E) e2.getNext();
            while (e != e2) {
                InternalEntry internalEntryCopyEntry = copyEntry(e, e3);
                if (internalEntryCopyEntry != null) {
                    e3 = (E) internalEntryCopyEntry;
                } else {
                    i--;
                }
                e = (E) e.getNext();
            }
            this.count = i;
            return e3;
        }

        public E removeFromChainForTesting(InternalEntry<K, V, ?> internalEntry, InternalEntry<K, V, ?> internalEntry2) {
            return (E) removeFromChain(castForTesting(internalEntry), castForTesting(internalEntry2));
        }

        public boolean removeTableEntryForTesting(InternalEntry<K, V, ?> internalEntry) {
            return removeEntryForTesting(castForTesting(internalEntry));
        }

        public boolean replace(K k, int i, V v, V v2) {
            lock();
            try {
                preWriteCleanup();
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                E e = atomicReferenceArray.get(length);
                for (InternalEntry next = e; next != null; next = next.getNext()) {
                    Object key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        Object value = next.getValue();
                        if (value != null) {
                            if (!this.map.valueEquivalence().equivalent(v, value)) {
                                return false;
                            }
                            this.modCount++;
                            setValue(next, v2);
                            return true;
                        }
                        if (isCollected(next)) {
                            this.modCount++;
                            InternalEntry internalEntryRemoveFromChain = removeFromChain(e, next);
                            int i2 = this.count - 1;
                            atomicReferenceArray.set(length, (E) internalEntryRemoveFromChain);
                            this.count = i2;
                        }
                        return false;
                    }
                }
                return false;
            } finally {
                unlock();
            }
        }

        public void runCleanup() {
            runLockedCleanup();
        }

        public void runLockedCleanup() {
            if (tryLock()) {
                try {
                    maybeDrainReferenceQueues();
                    this.readCount.set(0);
                } finally {
                    unlock();
                }
            }
        }

        public abstract S self();

        public void setTableEntryForTesting(int i, InternalEntry<K, V, ?> internalEntry) {
            this.table.set(i, (E) castForTesting(internalEntry));
        }

        public void setValue(E e, V v) {
            this.map.entryHelper.setValue(self(), e, v);
        }

        public void setValueForTesting(InternalEntry<K, V, ?> internalEntry, V v) {
            this.map.entryHelper.setValue(self(), castForTesting(internalEntry), v);
        }

        public void setWeakValueReferenceForTesting(InternalEntry<K, V, ?> internalEntry, WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> weakValueReference) {
            throw new AssertionError();
        }

        public void tryDrainReferenceQueues() {
            if (tryLock()) {
                try {
                    maybeDrainReferenceQueues();
                } finally {
                    unlock();
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x003d, code lost:
        
            if (r8.map.valueEquivalence().equivalent(r11, r4.getValue()) == false) goto L14;
         */
        /* JADX WARN: Code restructure failed: missing block: B:13:0x003f, code lost:
        
            r5 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:15:0x0045, code lost:
        
            if (isCollected(r4) == false) goto L21;
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x0047, code lost:
        
            r8.modCount++;
            r9 = removeFromChain(r3, r4);
            r10 = r8.count - 1;
            r0.set(r1, (E) r9);
            r8.count = r10;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x005b, code lost:
        
            return r5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x0061, code lost:
        
            return false;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean remove(Object obj, int i, Object obj2) {
            lock();
            try {
                preWriteCleanup();
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                E e = atomicReferenceArray.get(length);
                InternalEntry next = e;
                while (true) {
                    boolean z = false;
                    if (next == null) {
                        return false;
                    }
                    Object key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(obj, key)) {
                        break;
                    }
                    next = next.getNext();
                }
            } finally {
                unlock();
            }
        }

        public V replace(K k, int i, V v) {
            lock();
            try {
                preWriteCleanup();
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                E e = atomicReferenceArray.get(length);
                for (InternalEntry next = e; next != null; next = next.getNext()) {
                    Object key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        V v2 = (V) next.getValue();
                        if (v2 == null) {
                            if (isCollected(next)) {
                                this.modCount++;
                                InternalEntry internalEntryRemoveFromChain = removeFromChain(e, next);
                                int i2 = this.count - 1;
                                atomicReferenceArray.set(length, (E) internalEntryRemoveFromChain);
                                this.count = i2;
                            }
                            return null;
                        }
                        this.modCount++;
                        setValue(next, v);
                        return v2;
                    }
                }
                return null;
            } finally {
                unlock();
            }
        }
    }
}
