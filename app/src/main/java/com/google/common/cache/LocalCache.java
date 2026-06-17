package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.google.common.cache.AbstractCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.jvm.internal.LongCompanionObject;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class LocalCache<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final int DRAIN_MAX = 16;
    static final int DRAIN_THRESHOLD = 63;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    final int concurrencyLevel;
    final CacheLoader<? super K, V> defaultLoader;
    final EntryFactory entryFactory;
    Set<Map.Entry<K, V>> entrySet;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final AbstractCache.StatsCounter globalStatsCounter;
    final Equivalence<Object> keyEquivalence;
    Set<K> keySet;
    final Strength keyStrength;
    final long maxWeight;
    final long refreshNanos;
    final RemovalListener<K, V> removalListener;
    final Queue<RemovalNotification<K, V>> removalNotificationQueue;
    final int segmentMask;
    final int segmentShift;
    final Segment<K, V>[] segments;
    final Ticker ticker;
    final Equivalence<Object> valueEquivalence;
    final Strength valueStrength;
    Collection<V> values;
    final Weigher<K, V> weigher;
    static final Logger logger = Logger.getLogger(LocalCache.class.getName());
    static final ValueReference<Object, Object> UNSET = new ValueReference<Object, Object>() { // from class: com.google.common.cache.LocalCache.1
        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<Object, Object> copyFor(ReferenceQueue<Object> referenceQueue, Object obj, ReferenceEntry<Object, Object> referenceEntry) {
            return this;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public Object get() {
            return null;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<Object, Object> getEntry() {
            return null;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 0;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(Object obj) {
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public Object waitForValue() {
            return null;
        }
    };
    static final Queue<?> DISCARDING_QUEUE = new AbstractQueue<Object>() { // from class: com.google.common.cache.LocalCache.2
        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<Object> iterator() {
            return ImmutableSet.of().iterator();
        }

        @Override // java.util.Queue
        public boolean offer(Object obj) {
            return true;
        }

        @Override // java.util.Queue
        public Object peek() {
            return null;
        }

        @Override // java.util.Queue
        public Object poll() {
            return null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return 0;
        }
    };

    public abstract class AbstractCacheSet<T> extends AbstractSet<T> {
        public AbstractCacheSet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() throws Throwable {
            LocalCache.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return LocalCache.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return LocalCache.this.size();
        }
    }

    public static abstract class AbstractReferenceEntry<K, V> implements ReferenceEntry<K, V> {
        @Override // com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public int getHash() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public K getKey() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setAccessTime(long j) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setWriteTime(long j) {
            throw new UnsupportedOperationException();
        }
    }

    public static final class AccessQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new AbstractReferenceEntry<K, V>() { // from class: com.google.common.cache.LocalCache.AccessQueue.1
            ReferenceEntry<K, V> nextAccess = this;
            ReferenceEntry<K, V> previousAccess = this;

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public long getAccessTime() {
                return LongCompanionObject.MAX_VALUE;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public ReferenceEntry<K, V> getNextInAccessQueue() {
                return this.nextAccess;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                return this.previousAccess;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setAccessTime(long j) {
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
                this.nextAccess = referenceEntry;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
                this.previousAccess = referenceEntry;
            }
        };

        @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            ReferenceEntry<K, V> nextInAccessQueue = this.head.getNextInAccessQueue();
            while (true) {
                ReferenceEntry<K, V> referenceEntry = this.head;
                if (nextInAccessQueue == referenceEntry) {
                    referenceEntry.setNextInAccessQueue(referenceEntry);
                    ReferenceEntry<K, V> referenceEntry2 = this.head;
                    referenceEntry2.setPreviousInAccessQueue(referenceEntry2);
                    return;
                } else {
                    ReferenceEntry<K, V> nextInAccessQueue2 = nextInAccessQueue.getNextInAccessQueue();
                    LocalCache.nullifyAccessOrder(nextInAccessQueue);
                    nextInAccessQueue = nextInAccessQueue2;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return ((ReferenceEntry) obj).getNextInAccessQueue() != NullEntry.INSTANCE;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.head.getNextInAccessQueue() == this.head;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) { // from class: com.google.common.cache.LocalCache.AccessQueue.2
                @Override // com.google.common.collect.AbstractSequentialIterator
                public ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> referenceEntry) {
                    ReferenceEntry<K, V> nextInAccessQueue = referenceEntry.getNextInAccessQueue();
                    if (nextInAccessQueue == AccessQueue.this.head) {
                        return null;
                    }
                    return nextInAccessQueue;
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object obj) {
            ReferenceEntry referenceEntry = (ReferenceEntry) obj;
            ReferenceEntry<K, V> previousInAccessQueue = referenceEntry.getPreviousInAccessQueue();
            ReferenceEntry<K, V> nextInAccessQueue = referenceEntry.getNextInAccessQueue();
            LocalCache.connectAccessOrder(previousInAccessQueue, nextInAccessQueue);
            LocalCache.nullifyAccessOrder(referenceEntry);
            return nextInAccessQueue != NullEntry.INSTANCE;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            int i = 0;
            for (ReferenceEntry<K, V> nextInAccessQueue = this.head.getNextInAccessQueue(); nextInAccessQueue != this.head; nextInAccessQueue = nextInAccessQueue.getNextInAccessQueue()) {
                i++;
            }
            return i;
        }

        @Override // java.util.Queue
        public boolean offer(ReferenceEntry<K, V> referenceEntry) {
            LocalCache.connectAccessOrder(referenceEntry.getPreviousInAccessQueue(), referenceEntry.getNextInAccessQueue());
            LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), referenceEntry);
            LocalCache.connectAccessOrder(referenceEntry, this.head);
            return true;
        }

        @Override // java.util.Queue
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> nextInAccessQueue = this.head.getNextInAccessQueue();
            if (nextInAccessQueue == this.head) {
                return null;
            }
            return nextInAccessQueue;
        }

        @Override // java.util.Queue
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> nextInAccessQueue = this.head.getNextInAccessQueue();
            if (nextInAccessQueue == this.head) {
                return null;
            }
            remove(nextInAccessQueue);
            return nextInAccessQueue;
        }
    }

    /* JADX WARN: Enum visitor error
    jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v0 com.google.common.cache.LocalCache$EntryFactory, still in use, count: 1, list:
      (r0v0 com.google.common.cache.LocalCache$EntryFactory) from 0x005a: FILLED_NEW_ARRAY 
      (r0v0 com.google.common.cache.LocalCache$EntryFactory)
      (r1v1 com.google.common.cache.LocalCache$EntryFactory)
      (r3v1 com.google.common.cache.LocalCache$EntryFactory)
      (r5v1 com.google.common.cache.LocalCache$EntryFactory)
      (r7v1 com.google.common.cache.LocalCache$EntryFactory)
      (r9v1 com.google.common.cache.LocalCache$EntryFactory)
      (r11v1 com.google.common.cache.LocalCache$EntryFactory)
      (r13v1 com.google.common.cache.LocalCache$EntryFactory)
     A[WRAPPED] elemType: com.google.common.cache.LocalCache$EntryFactory
    	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
    	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
    	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:99)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:98)
    	at jadx.core.utils.InsnRemover.removeAllAndUnbind(InsnRemover.java:252)
    	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:180)
    	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
     */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    public static abstract class EntryFactory {
        STRONG { // from class: com.google.common.cache.LocalCache.EntryFactory.1
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new StrongEntry(k, i, referenceEntry);
            }
        },
        STRONG_ACCESS { // from class: com.google.common.cache.LocalCache.EntryFactory.2
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyAccessEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new StrongAccessEntry(k, i, referenceEntry);
            }
        },
        STRONG_WRITE { // from class: com.google.common.cache.LocalCache.EntryFactory.3
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyWriteEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new StrongWriteEntry(k, i, referenceEntry);
            }
        },
        STRONG_ACCESS_WRITE { // from class: com.google.common.cache.LocalCache.EntryFactory.4
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyAccessEntry(referenceEntry, referenceEntryCopyEntry);
                copyWriteEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new StrongAccessWriteEntry(k, i, referenceEntry);
            }
        },
        WEAK { // from class: com.google.common.cache.LocalCache.EntryFactory.5
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new WeakEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }
        },
        WEAK_ACCESS { // from class: com.google.common.cache.LocalCache.EntryFactory.6
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyAccessEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new WeakAccessEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }
        },
        WEAK_WRITE { // from class: com.google.common.cache.LocalCache.EntryFactory.7
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyWriteEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new WeakWriteEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }
        },
        WEAK_ACCESS_WRITE { // from class: com.google.common.cache.LocalCache.EntryFactory.8
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyAccessEntry(referenceEntry, referenceEntryCopyEntry);
                copyWriteEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new WeakAccessWriteEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }
        };

        static final int ACCESS_MASK = 1;
        static final int WEAK_MASK = 4;
        static final int WRITE_MASK = 2;
        static final EntryFactory[] factories = {new EntryFactory() { // from class: com.google.common.cache.LocalCache.EntryFactory.1
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new StrongEntry(k, i, referenceEntry);
            }
        }, new EntryFactory() { // from class: com.google.common.cache.LocalCache.EntryFactory.2
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyAccessEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new StrongAccessEntry(k, i, referenceEntry);
            }
        }, new EntryFactory() { // from class: com.google.common.cache.LocalCache.EntryFactory.3
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyWriteEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new StrongWriteEntry(k, i, referenceEntry);
            }
        }, new EntryFactory() { // from class: com.google.common.cache.LocalCache.EntryFactory.4
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyAccessEntry(referenceEntry, referenceEntryCopyEntry);
                copyWriteEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new StrongAccessWriteEntry(k, i, referenceEntry);
            }
        }, new EntryFactory() { // from class: com.google.common.cache.LocalCache.EntryFactory.5
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new WeakEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }
        }, new EntryFactory() { // from class: com.google.common.cache.LocalCache.EntryFactory.6
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyAccessEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new WeakAccessEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }
        }, new EntryFactory() { // from class: com.google.common.cache.LocalCache.EntryFactory.7
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyWriteEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new WeakWriteEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }
        }, new EntryFactory() { // from class: com.google.common.cache.LocalCache.EntryFactory.8
            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = super.copyEntry(segment, referenceEntry, referenceEntry2, k);
                copyAccessEntry(referenceEntry, referenceEntryCopyEntry);
                copyWriteEntry(referenceEntry, referenceEntryCopyEntry);
                return referenceEntryCopyEntry;
            }

            @Override // com.google.common.cache.LocalCache.EntryFactory
            public <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return new WeakAccessWriteEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }
        }};

        static {
        }

        private EntryFactory() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static EntryFactory getFactory(Strength strength, boolean z, boolean z2) {
            return factories[(strength == Strength.WEAK ? (char) 4 : (char) 0) | (z ? 1 : 0) | (z2 ? 2 : 0)];
        }

        public static EntryFactory valueOf(String str) {
            return (EntryFactory) Enum.valueOf(EntryFactory.class, str);
        }

        public static EntryFactory[] values() {
            return (EntryFactory[]) $VALUES.clone();
        }

        public <K, V> void copyAccessEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setAccessTime(referenceEntry.getAccessTime());
            LocalCache.connectAccessOrder(referenceEntry.getPreviousInAccessQueue(), referenceEntry2);
            LocalCache.connectAccessOrder(referenceEntry2, referenceEntry.getNextInAccessQueue());
            LocalCache.nullifyAccessOrder(referenceEntry);
        }

        public <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k) {
            return newEntry(segment, k, referenceEntry.getHash(), referenceEntry2);
        }

        public <K, V> void copyWriteEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setWriteTime(referenceEntry.getWriteTime());
            LocalCache.connectWriteOrder(referenceEntry.getPreviousInWriteQueue(), referenceEntry2);
            LocalCache.connectWriteOrder(referenceEntry2, referenceEntry.getNextInWriteQueue());
            LocalCache.nullifyWriteOrder(referenceEntry);
        }

        public abstract <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, ReferenceEntry<K, V> referenceEntry);
    }

    public final class EntryIterator extends LocalCache<K, V>.HashIterator<Map.Entry<K, V>> {
        public EntryIterator() {
            super();
        }

        @Override // com.google.common.cache.LocalCache.HashIterator, java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    public final class EntrySet extends LocalCache<K, V>.AbstractCacheSet<Map.Entry<K, V>> {
        public EntrySet() {
            super();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            Map.Entry entry;
            Object key;
            Object obj2;
            return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && (obj2 = LocalCache.this.get(key)) != null && LocalCache.this.valueEquivalence.equivalent(entry.getValue(), obj2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Map.Entry entry;
            Object key;
            return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && LocalCache.this.remove(key, entry.getValue());
        }
    }

    public abstract class HashIterator<T> implements Iterator<T> {
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        LocalCache<K, V>.WriteThroughEntry lastReturned;
        ReferenceEntry<K, V> nextEntry;
        LocalCache<K, V>.WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex = -1;

        public HashIterator() {
            this.nextSegmentIndex = LocalCache.this.segments.length - 1;
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
                Segment<K, V>[] segmentArr = LocalCache.this.segments;
                this.nextSegmentIndex = i - 1;
                Segment<K, V> segment = segmentArr[i];
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

        public boolean advanceTo(ReferenceEntry<K, V> referenceEntry) {
            try {
                long j = LocalCache.this.ticker.read();
                K key = referenceEntry.getKey();
                Object liveValue = LocalCache.this.getLiveValue(referenceEntry, j);
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

        public LocalCache<K, V>.WriteThroughEntry nextEntry() {
            LocalCache<K, V>.WriteThroughEntry writeThroughEntry = this.nextExternal;
            if (writeThroughEntry == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = writeThroughEntry;
            advance();
            return this.lastReturned;
        }

        public boolean nextInChain() {
            ReferenceEntry<K, V> referenceEntry = this.nextEntry;
            if (referenceEntry == null) {
                return false;
            }
            while (true) {
                this.nextEntry = referenceEntry.getNext();
                ReferenceEntry<K, V> referenceEntry2 = this.nextEntry;
                if (referenceEntry2 == null) {
                    return false;
                }
                if (advanceTo(referenceEntry2)) {
                    return true;
                }
                referenceEntry = this.nextEntry;
            }
        }

        public boolean nextInTable() {
            while (true) {
                int i = this.nextTableIndex;
                if (i < 0) {
                    return false;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.currentTable;
                this.nextTableIndex = i - 1;
                ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get(i);
                this.nextEntry = referenceEntry;
                if (referenceEntry != null && (advanceTo(referenceEntry) || nextInChain())) {
                    return true;
                }
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            Preconditions.checkState(this.lastReturned != null);
            LocalCache.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }

    public final class KeyIterator extends LocalCache<K, V>.HashIterator<K> {
        public KeyIterator() {
            super();
        }

        @Override // com.google.common.cache.LocalCache.HashIterator, java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }
    }

    public final class KeySet extends LocalCache<K, V>.AbstractCacheSet<K> {
        public KeySet() {
            super();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return LocalCache.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return LocalCache.this.remove(obj) != null;
        }
    }

    public static final class LoadingSerializationProxy<K, V> extends ManualSerializationProxy<K, V> implements LoadingCache<K, V> {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 1;
        transient LoadingCache<K, V> autoDelegate;

        public LoadingSerializationProxy(LocalCache<K, V> localCache) {
            super(localCache);
        }

        private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();
            this.autoDelegate = (LoadingCache<K, V>) recreateCacheBuilder().build(this.loader);
        }

        private Object readResolve() {
            return this.autoDelegate;
        }

        @Override // com.google.common.cache.LoadingCache, com.google.common.base.Function
        public V apply(K k) {
            return this.autoDelegate.apply(k);
        }

        @Override // com.google.common.cache.LoadingCache
        public V get(K k) throws ExecutionException {
            return this.autoDelegate.get(k);
        }

        @Override // com.google.common.cache.LoadingCache
        public ImmutableMap<K, V> getAll(Iterable<? extends K> iterable) throws ExecutionException {
            return this.autoDelegate.getAll(iterable);
        }

        @Override // com.google.common.cache.LoadingCache
        public V getUnchecked(K k) {
            return this.autoDelegate.getUnchecked(k);
        }

        @Override // com.google.common.cache.LoadingCache
        public void refresh(K k) {
            this.autoDelegate.refresh(k);
        }
    }

    public static class LoadingValueReference<K, V> implements ValueReference<K, V> {
        final SettableFuture<V> futureValue;
        volatile ValueReference<K, V> oldValue;
        final Stopwatch stopwatch;

        public LoadingValueReference() {
            this(LocalCache.unset());
        }

        private ListenableFuture<V> fullyFailedFuture(Throwable th) {
            return Futures.immediateFailedFuture(th);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ Object lambda$loadFuture$0(Object obj) {
            set(obj);
            return obj;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        public long elapsedNanos() {
            return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V get() {
            return this.oldValue.get();
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        public ValueReference<K, V> getOldValue() {
            return this.oldValue;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.oldValue.getWeight();
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return this.oldValue.isActive();
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return true;
        }

        public ListenableFuture<V> loadFuture(K k, CacheLoader<? super K, V> cacheLoader) {
            try {
                this.stopwatch.start();
                V v = this.oldValue.get();
                if (v == null) {
                    V vLoad = cacheLoader.load(k);
                    return set(vLoad) ? this.futureValue : Futures.immediateFuture(vLoad);
                }
                ListenableFuture<V> listenableFutureReload = cacheLoader.reload(k, v);
                return listenableFutureReload == null ? Futures.immediateFuture(null) : Futures.transform(listenableFutureReload, new Function() { // from class: com.google.common.cache.b
                    @Override // com.google.common.base.Function
                    public final Object apply(Object obj) {
                        return this.a.lambda$loadFuture$0(obj);
                    }
                }, MoreExecutors.directExecutor());
            } catch (Throwable th) {
                ListenableFuture<V> listenableFutureFullyFailedFuture = setException(th) ? this.futureValue : fullyFailedFuture(th);
                if (th instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                return listenableFutureFullyFailedFuture;
            }
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(V v) {
            if (v != null) {
                set(v);
            } else {
                this.oldValue = LocalCache.unset();
            }
        }

        public boolean set(V v) {
            return this.futureValue.set(v);
        }

        public boolean setException(Throwable th) {
            return this.futureValue.setException(th);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() throws ExecutionException {
            return (V) Uninterruptibles.getUninterruptibly(this.futureValue);
        }

        public LoadingValueReference(ValueReference<K, V> valueReference) {
            this.futureValue = SettableFuture.create();
            this.stopwatch = Stopwatch.createUnstarted();
            this.oldValue = valueReference;
        }
    }

    public static class LocalLoadingCache<K, V> extends LocalManualCache<K, V> implements LoadingCache<K, V> {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 1;

        public LocalLoadingCache(CacheBuilder<? super K, ? super V> cacheBuilder, CacheLoader<? super K, V> cacheLoader) {
            super();
        }

        private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
            throw new InvalidObjectException("Use LoadingSerializationProxy");
        }

        @Override // com.google.common.cache.LoadingCache, com.google.common.base.Function
        public final V apply(K k) {
            return getUnchecked(k);
        }

        @Override // com.google.common.cache.LoadingCache
        public V get(K k) throws ExecutionException {
            return this.localCache.getOrLoad(k);
        }

        @Override // com.google.common.cache.LoadingCache
        public ImmutableMap<K, V> getAll(Iterable<? extends K> iterable) throws ExecutionException {
            return this.localCache.getAll(iterable);
        }

        @Override // com.google.common.cache.LoadingCache
        public V getUnchecked(K k) {
            try {
                return get(k);
            } catch (ExecutionException e) {
                throw new UncheckedExecutionException(e.getCause());
            }
        }

        @Override // com.google.common.cache.LoadingCache
        public void refresh(K k) {
            this.localCache.refresh(k);
        }

        @Override // com.google.common.cache.LocalCache.LocalManualCache
        public Object writeReplace() {
            return new LoadingSerializationProxy(this.localCache);
        }
    }

    public static class LocalManualCache<K, V> implements Cache<K, V>, Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 1;
        final LocalCache<K, V> localCache;

        private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
            throw new InvalidObjectException("Use ManualSerializationProxy");
        }

        @Override // com.google.common.cache.Cache
        public ConcurrentMap<K, V> asMap() {
            return this.localCache;
        }

        @Override // com.google.common.cache.Cache
        public void cleanUp() {
            this.localCache.cleanUp();
        }

        @Override // com.google.common.cache.Cache
        public V get(K k, final Callable<? extends V> callable) throws ExecutionException {
            Preconditions.checkNotNull(callable);
            return this.localCache.get(k, new CacheLoader<Object, V>(this) { // from class: com.google.common.cache.LocalCache.LocalManualCache.1
                final /* synthetic */ LocalManualCache this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.common.cache.CacheLoader
                public V load(Object obj) throws Exception {
                    return (V) callable.call();
                }
            });
        }

        @Override // com.google.common.cache.Cache
        public ImmutableMap<K, V> getAllPresent(Iterable<?> iterable) {
            return this.localCache.getAllPresent(iterable);
        }

        @Override // com.google.common.cache.Cache
        public V getIfPresent(Object obj) {
            return this.localCache.getIfPresent(obj);
        }

        @Override // com.google.common.cache.Cache
        public void invalidate(Object obj) {
            Preconditions.checkNotNull(obj);
            this.localCache.remove(obj);
        }

        @Override // com.google.common.cache.Cache
        public void invalidateAll(Iterable<?> iterable) {
            this.localCache.invalidateAll(iterable);
        }

        @Override // com.google.common.cache.Cache
        public void put(K k, V v) {
            this.localCache.put(k, v);
        }

        @Override // com.google.common.cache.Cache
        public void putAll(Map<? extends K, ? extends V> map) {
            this.localCache.putAll(map);
        }

        @Override // com.google.common.cache.Cache
        public long size() {
            return this.localCache.longSize();
        }

        @Override // com.google.common.cache.Cache
        public CacheStats stats() {
            AbstractCache.SimpleStatsCounter simpleStatsCounter = new AbstractCache.SimpleStatsCounter();
            simpleStatsCounter.incrementBy(this.localCache.globalStatsCounter);
            for (Segment<K, V> segment : this.localCache.segments) {
                simpleStatsCounter.incrementBy(segment.statsCounter);
            }
            return simpleStatsCounter.snapshot();
        }

        public Object writeReplace() {
            return new ManualSerializationProxy(this.localCache);
        }

        public LocalManualCache(CacheBuilder<? super K, ? super V> cacheBuilder) {
            this(new LocalCache(cacheBuilder, null));
        }

        @Override // com.google.common.cache.Cache
        public void invalidateAll() throws Throwable {
            this.localCache.clear();
        }

        private LocalManualCache(LocalCache<K, V> localCache) {
            this.localCache = localCache;
        }
    }

    public static class ManualSerializationProxy<K, V> extends ForwardingCache<K, V> implements Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 1;
        final int concurrencyLevel;
        transient Cache<K, V> delegate;
        final long expireAfterAccessNanos;
        final long expireAfterWriteNanos;
        final Equivalence<Object> keyEquivalence;
        final Strength keyStrength;
        final CacheLoader<? super K, V> loader;
        final long maxWeight;
        final RemovalListener<? super K, ? super V> removalListener;
        final Ticker ticker;
        final Equivalence<Object> valueEquivalence;
        final Strength valueStrength;
        final Weigher<K, V> weigher;

        public ManualSerializationProxy(LocalCache<K, V> localCache) {
            this(localCache.keyStrength, localCache.valueStrength, localCache.keyEquivalence, localCache.valueEquivalence, localCache.expireAfterWriteNanos, localCache.expireAfterAccessNanos, localCache.maxWeight, localCache.weigher, localCache.concurrencyLevel, localCache.removalListener, localCache.ticker, localCache.defaultLoader);
        }

        private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();
            this.delegate = (Cache<K, V>) recreateCacheBuilder().build();
        }

        private Object readResolve() {
            return this.delegate;
        }

        public CacheBuilder<K, V> recreateCacheBuilder() {
            CacheBuilder<K, V> cacheBuilder = (CacheBuilder<K, V>) CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).removalListener(this.removalListener);
            cacheBuilder.strictParsing = false;
            long j = this.expireAfterWriteNanos;
            if (j > 0) {
                cacheBuilder.expireAfterWrite(j, TimeUnit.NANOSECONDS);
            }
            long j2 = this.expireAfterAccessNanos;
            if (j2 > 0) {
                cacheBuilder.expireAfterAccess(j2, TimeUnit.NANOSECONDS);
            }
            Weigher weigher = this.weigher;
            if (weigher != CacheBuilder.OneWeigher.INSTANCE) {
                cacheBuilder.weigher(weigher);
                long j3 = this.maxWeight;
                if (j3 != -1) {
                    cacheBuilder.maximumWeight(j3);
                }
            } else {
                long j4 = this.maxWeight;
                if (j4 != -1) {
                    cacheBuilder.maximumSize(j4);
                }
            }
            Ticker ticker = this.ticker;
            if (ticker != null) {
                cacheBuilder.ticker(ticker);
            }
            return cacheBuilder;
        }

        private ManualSerializationProxy(Strength strength, Strength strength2, Equivalence<Object> equivalence, Equivalence<Object> equivalence2, long j, long j2, long j3, Weigher<K, V> weigher, int i, RemovalListener<? super K, ? super V> removalListener, Ticker ticker, CacheLoader<? super K, V> cacheLoader) {
            this.keyStrength = strength;
            this.valueStrength = strength2;
            this.keyEquivalence = equivalence;
            this.valueEquivalence = equivalence2;
            this.expireAfterWriteNanos = j;
            this.expireAfterAccessNanos = j2;
            this.maxWeight = j3;
            this.weigher = weigher;
            this.concurrencyLevel = i;
            this.removalListener = removalListener;
            this.ticker = (ticker == Ticker.systemTicker() || ticker == CacheBuilder.NULL_TICKER) ? null : ticker;
            this.loader = cacheLoader;
        }

        @Override // com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
        public Cache<K, V> delegate() {
            return this.delegate;
        }
    }

    public enum NullEntry implements ReferenceEntry<Object, Object> {
        INSTANCE;

        @Override // com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return 0L;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public int getHash() {
            return 0;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public Object getKey() {
            return null;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getNext() {
            return null;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getNextInAccessQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getNextInWriteQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
            return this;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ValueReference<Object, Object> getValueReference() {
            return null;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return 0L;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setAccessTime(long j) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<Object, Object> valueReference) {
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setWriteTime(long j) {
        }
    }

    public static class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        public SoftValueReference(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            super(v, referenceQueue);
            this.entry = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return new SoftValueReference(referenceQueue, v, referenceEntry);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 1;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return true;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(V v) {
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    public enum Strength {
        STRONG { // from class: com.google.common.cache.LocalCache.Strength.1
            @Override // com.google.common.cache.LocalCache.Strength
            public Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }

            @Override // com.google.common.cache.LocalCache.Strength
            public <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v, int i) {
                return i == 1 ? new StrongValueReference(v) : new WeightedStrongValueReference(v, i);
            }
        },
        SOFT { // from class: com.google.common.cache.LocalCache.Strength.2
            @Override // com.google.common.cache.LocalCache.Strength
            public Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }

            @Override // com.google.common.cache.LocalCache.Strength
            public <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v, int i) {
                return i == 1 ? new SoftValueReference(segment.valueReferenceQueue, v, referenceEntry) : new WeightedSoftValueReference(segment.valueReferenceQueue, v, referenceEntry, i);
            }
        },
        WEAK { // from class: com.google.common.cache.LocalCache.Strength.3
            @Override // com.google.common.cache.LocalCache.Strength
            public Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }

            @Override // com.google.common.cache.LocalCache.Strength
            public <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v, int i) {
                return i == 1 ? new WeakValueReference(segment.valueReferenceQueue, v, referenceEntry) : new WeightedWeakValueReference(segment.valueReferenceQueue, v, referenceEntry, i);
            }
        };

        public abstract Equivalence<Object> defaultEquivalence();

        public abstract <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v, int i);
    }

    public static final class StrongAccessEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> previousAccess;

        public StrongAccessEntry(K k, int i, ReferenceEntry<K, V> referenceEntry) {
            super(k, i, referenceEntry);
            this.accessTime = LongCompanionObject.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long j) {
            this.accessTime = j;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }
    }

    public static final class StrongAccessWriteEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousAccess;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;

        public StrongAccessWriteEntry(K k, int i, ReferenceEntry<K, V> referenceEntry) {
            super(k, i, referenceEntry);
            this.accessTime = LongCompanionObject.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
            this.writeTime = LongCompanionObject.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long j) {
            this.accessTime = j;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long j) {
            this.writeTime = j;
        }
    }

    public static class StrongEntry<K, V> extends AbstractReferenceEntry<K, V> {
        final int hash;
        final K key;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = LocalCache.unset();

        public StrongEntry(K k, int i, ReferenceEntry<K, V> referenceEntry) {
            this.key = k;
            this.hash = i;
            this.next = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public int getHash() {
            return this.hash;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }
    }

    public static class StrongValueReference<K, V> implements ValueReference<K, V> {
        final V referent;

        public StrongValueReference(V v) {
            this.referent = v;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V get() {
            return this.referent;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 1;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return true;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(V v) {
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    public static final class StrongWriteEntry<K, V> extends StrongEntry<K, V> {
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;

        public StrongWriteEntry(K k, int i, ReferenceEntry<K, V> referenceEntry) {
            super(k, i, referenceEntry);
            this.writeTime = LongCompanionObject.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long j) {
            this.writeTime = j;
        }
    }

    public final class ValueIterator extends LocalCache<K, V>.HashIterator<V> {
        public ValueIterator() {
            super();
        }

        @Override // com.google.common.cache.LocalCache.HashIterator, java.util.Iterator
        public V next() {
            return nextEntry().getValue();
        }
    }

    public interface ValueReference<K, V> {
        ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry);

        V get();

        ReferenceEntry<K, V> getEntry();

        int getWeight();

        boolean isActive();

        boolean isLoading();

        void notifyNewValue(V v);

        V waitForValue() throws ExecutionException;
    }

    public final class Values extends AbstractCollection<V> {
        public Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() throws Throwable {
            LocalCache.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return LocalCache.this.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return LocalCache.this.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return LocalCache.this.size();
        }
    }

    public static final class WeakAccessEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> previousAccess;

        public WeakAccessEntry(ReferenceQueue<K> referenceQueue, K k, int i, ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
            this.accessTime = LongCompanionObject.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long j) {
            this.accessTime = j;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }
    }

    public static final class WeakAccessWriteEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousAccess;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;

        public WeakAccessWriteEntry(ReferenceQueue<K> referenceQueue, K k, int i, ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
            this.accessTime = LongCompanionObject.MAX_VALUE;
            this.nextAccess = LocalCache.nullEntry();
            this.previousAccess = LocalCache.nullEntry();
            this.writeTime = LongCompanionObject.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getAccessTime() {
            return this.accessTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setAccessTime(long j) {
            this.accessTime = j;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long j) {
            this.writeTime = j;
        }
    }

    public static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;

        public WeakEntry(ReferenceQueue<K> referenceQueue, K k, int i, ReferenceEntry<K, V> referenceEntry) {
            super(k, referenceQueue);
            this.valueReference = LocalCache.unset();
            this.hash = i;
            this.next = referenceEntry;
        }

        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public int getHash() {
            return this.hash;
        }

        @Override // com.google.common.cache.ReferenceEntry
        public K getKey() {
            return get();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        public void setAccessTime(long j) {
            throw new UnsupportedOperationException();
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.cache.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        public void setWriteTime(long j) {
            throw new UnsupportedOperationException();
        }
    }

    public static class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        public WeakValueReference(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            super(v, referenceQueue);
            this.entry = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return new WeakValueReference(referenceQueue, v, referenceEntry);
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return 1;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isActive() {
            return true;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public boolean isLoading() {
            return false;
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public void notifyNewValue(V v) {
        }

        @Override // com.google.common.cache.LocalCache.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    public static final class WeakWriteEntry<K, V> extends WeakEntry<K, V> {
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousWrite;
        volatile long writeTime;

        public WeakWriteEntry(ReferenceQueue<K> referenceQueue, K k, int i, ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
            this.writeTime = LongCompanionObject.MAX_VALUE;
            this.nextWrite = LocalCache.nullEntry();
            this.previousWrite = LocalCache.nullEntry();
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public long getWriteTime() {
            return this.writeTime;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }

        @Override // com.google.common.cache.LocalCache.WeakEntry, com.google.common.cache.ReferenceEntry
        public void setWriteTime(long j) {
            this.writeTime = j;
        }
    }

    public static final class WeightedSoftValueReference<K, V> extends SoftValueReference<K, V> {
        final int weight;

        public WeightedSoftValueReference(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry, int i) {
            super(referenceQueue, v, referenceEntry);
            this.weight = i;
        }

        @Override // com.google.common.cache.LocalCache.SoftValueReference, com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return new WeightedSoftValueReference(referenceQueue, v, referenceEntry, this.weight);
        }

        @Override // com.google.common.cache.LocalCache.SoftValueReference, com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.weight;
        }
    }

    public static final class WeightedStrongValueReference<K, V> extends StrongValueReference<K, V> {
        final int weight;

        public WeightedStrongValueReference(V v, int i) {
            super(v);
            this.weight = i;
        }

        @Override // com.google.common.cache.LocalCache.StrongValueReference, com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.weight;
        }
    }

    public static final class WeightedWeakValueReference<K, V> extends WeakValueReference<K, V> {
        final int weight;

        public WeightedWeakValueReference(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry, int i) {
            super(referenceQueue, v, referenceEntry);
            this.weight = i;
        }

        @Override // com.google.common.cache.LocalCache.WeakValueReference, com.google.common.cache.LocalCache.ValueReference
        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return new WeightedWeakValueReference(referenceQueue, v, referenceEntry, this.weight);
        }

        @Override // com.google.common.cache.LocalCache.WeakValueReference, com.google.common.cache.LocalCache.ValueReference
        public int getWeight() {
            return this.weight;
        }
    }

    public static final class WriteQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new AbstractReferenceEntry<K, V>() { // from class: com.google.common.cache.LocalCache.WriteQueue.1
            ReferenceEntry<K, V> nextWrite = this;
            ReferenceEntry<K, V> previousWrite = this;

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public ReferenceEntry<K, V> getNextInWriteQueue() {
                return this.nextWrite;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                return this.previousWrite;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public long getWriteTime() {
                return LongCompanionObject.MAX_VALUE;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
                this.nextWrite = referenceEntry;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
                this.previousWrite = referenceEntry;
            }

            @Override // com.google.common.cache.LocalCache.AbstractReferenceEntry, com.google.common.cache.ReferenceEntry
            public void setWriteTime(long j) {
            }
        };

        @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            ReferenceEntry<K, V> nextInWriteQueue = this.head.getNextInWriteQueue();
            while (true) {
                ReferenceEntry<K, V> referenceEntry = this.head;
                if (nextInWriteQueue == referenceEntry) {
                    referenceEntry.setNextInWriteQueue(referenceEntry);
                    ReferenceEntry<K, V> referenceEntry2 = this.head;
                    referenceEntry2.setPreviousInWriteQueue(referenceEntry2);
                    return;
                } else {
                    ReferenceEntry<K, V> nextInWriteQueue2 = nextInWriteQueue.getNextInWriteQueue();
                    LocalCache.nullifyWriteOrder(nextInWriteQueue);
                    nextInWriteQueue = nextInWriteQueue2;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return ((ReferenceEntry) obj).getNextInWriteQueue() != NullEntry.INSTANCE;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.head.getNextInWriteQueue() == this.head;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) { // from class: com.google.common.cache.LocalCache.WriteQueue.2
                @Override // com.google.common.collect.AbstractSequentialIterator
                public ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> referenceEntry) {
                    ReferenceEntry<K, V> nextInWriteQueue = referenceEntry.getNextInWriteQueue();
                    if (nextInWriteQueue == WriteQueue.this.head) {
                        return null;
                    }
                    return nextInWriteQueue;
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object obj) {
            ReferenceEntry referenceEntry = (ReferenceEntry) obj;
            ReferenceEntry<K, V> previousInWriteQueue = referenceEntry.getPreviousInWriteQueue();
            ReferenceEntry<K, V> nextInWriteQueue = referenceEntry.getNextInWriteQueue();
            LocalCache.connectWriteOrder(previousInWriteQueue, nextInWriteQueue);
            LocalCache.nullifyWriteOrder(referenceEntry);
            return nextInWriteQueue != NullEntry.INSTANCE;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            int i = 0;
            for (ReferenceEntry<K, V> nextInWriteQueue = this.head.getNextInWriteQueue(); nextInWriteQueue != this.head; nextInWriteQueue = nextInWriteQueue.getNextInWriteQueue()) {
                i++;
            }
            return i;
        }

        @Override // java.util.Queue
        public boolean offer(ReferenceEntry<K, V> referenceEntry) {
            LocalCache.connectWriteOrder(referenceEntry.getPreviousInWriteQueue(), referenceEntry.getNextInWriteQueue());
            LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), referenceEntry);
            LocalCache.connectWriteOrder(referenceEntry, this.head);
            return true;
        }

        @Override // java.util.Queue
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> nextInWriteQueue = this.head.getNextInWriteQueue();
            if (nextInWriteQueue == this.head) {
                return null;
            }
            return nextInWriteQueue;
        }

        @Override // java.util.Queue
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> nextInWriteQueue = this.head.getNextInWriteQueue();
            if (nextInWriteQueue == this.head) {
                return null;
            }
            remove(nextInWriteQueue);
            return nextInWriteQueue;
        }
    }

    public final class WriteThroughEntry implements Map.Entry<K, V> {
        final K key;
        V value;

        public WriteThroughEntry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                if (this.key.equals(entry.getKey()) && this.value.equals(entry.getValue())) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            V v2 = (V) LocalCache.this.put(this.key, v);
            this.value = v;
            return v2;
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    public LocalCache(CacheBuilder<? super K, ? super V> cacheBuilder, CacheLoader<? super K, V> cacheLoader) {
        this.concurrencyLevel = Math.min(cacheBuilder.getConcurrencyLevel(), MAX_SEGMENTS);
        Strength keyStrength = cacheBuilder.getKeyStrength();
        this.keyStrength = keyStrength;
        this.valueStrength = cacheBuilder.getValueStrength();
        this.keyEquivalence = cacheBuilder.getKeyEquivalence();
        this.valueEquivalence = cacheBuilder.getValueEquivalence();
        long maximumWeight = cacheBuilder.getMaximumWeight();
        this.maxWeight = maximumWeight;
        this.weigher = (Weigher<K, V>) cacheBuilder.getWeigher();
        this.expireAfterAccessNanos = cacheBuilder.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = cacheBuilder.getExpireAfterWriteNanos();
        this.refreshNanos = cacheBuilder.getRefreshNanos();
        CacheBuilder.NullListener nullListener = (RemovalListener<K, V>) cacheBuilder.getRemovalListener();
        this.removalListener = nullListener;
        this.removalNotificationQueue = nullListener == CacheBuilder.NullListener.INSTANCE ? discardingQueue() : new ConcurrentLinkedQueue<>();
        this.ticker = cacheBuilder.getTicker(recordsTime());
        this.entryFactory = EntryFactory.getFactory(keyStrength, usesAccessEntries(), usesWriteEntries());
        this.globalStatsCounter = cacheBuilder.getStatsCounterSupplier().get();
        this.defaultLoader = cacheLoader;
        int iMin = Math.min(cacheBuilder.getInitialCapacity(), 1073741824);
        if (evictsBySize() && !customWeigher()) {
            iMin = (int) Math.min(iMin, maximumWeight);
        }
        int i = 0;
        int i2 = 1;
        int i3 = 0;
        int i4 = 1;
        while (i4 < this.concurrencyLevel && (!evictsBySize() || ((long) i4) * 20 <= this.maxWeight)) {
            i3++;
            i4 <<= 1;
        }
        this.segmentShift = 32 - i3;
        this.segmentMask = i4 - 1;
        this.segments = newSegmentArray(i4);
        int i5 = iMin / i4;
        while (i2 < (i5 * i4 < iMin ? i5 + 1 : i5)) {
            i2 <<= 1;
        }
        if (evictsBySize()) {
            long j = this.maxWeight;
            long j2 = i4;
            long j3 = (j / j2) + 1;
            long j4 = j % j2;
            while (true) {
                Segment<K, V>[] segmentArr = this.segments;
                if (i >= segmentArr.length) {
                    return;
                }
                if (i == j4) {
                    j3--;
                }
                segmentArr[i] = createSegment(i2, j3, cacheBuilder.getStatsCounterSupplier().get());
                i++;
            }
        } else {
            while (true) {
                Segment<K, V>[] segmentArr2 = this.segments;
                if (i >= segmentArr2.length) {
                    return;
                }
                segmentArr2[i] = createSegment(i2, -1L, cacheBuilder.getStatsCounterSupplier().get());
                i++;
            }
        }
    }

    public static <K, V> void connectAccessOrder(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        referenceEntry.setNextInAccessQueue(referenceEntry2);
        referenceEntry2.setPreviousInAccessQueue(referenceEntry);
    }

    public static <K, V> void connectWriteOrder(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        referenceEntry.setNextInWriteQueue(referenceEntry2);
        referenceEntry2.setPreviousInWriteQueue(referenceEntry);
    }

    public static <E> Queue<E> discardingQueue() {
        return (Queue<E>) DISCARDING_QUEUE;
    }

    public static <K, V> ReferenceEntry<K, V> nullEntry() {
        return NullEntry.INSTANCE;
    }

    public static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> referenceEntry) {
        ReferenceEntry<K, V> referenceEntryNullEntry = nullEntry();
        referenceEntry.setNextInAccessQueue(referenceEntryNullEntry);
        referenceEntry.setPreviousInAccessQueue(referenceEntryNullEntry);
    }

    public static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> referenceEntry) {
        ReferenceEntry<K, V> referenceEntryNullEntry = nullEntry();
        referenceEntry.setNextInWriteQueue(referenceEntryNullEntry);
        referenceEntry.setPreviousInWriteQueue(referenceEntryNullEntry);
    }

    public static int rehash(int i) {
        int i2 = i + ((i << 15) ^ (-12931));
        int i3 = i2 ^ (i2 >>> 10);
        int i4 = i3 + (i3 << 3);
        int i5 = i4 ^ (i4 >>> 6);
        int i6 = (i5 << 2) + (i5 << 14) + i5;
        return (i6 >>> 16) ^ i6;
    }

    public static <K, V> ValueReference<K, V> unset() {
        return (ValueReference<K, V>) UNSET;
    }

    public void cleanUp() {
        for (Segment<K, V> segment : this.segments) {
            segment.cleanUp();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() throws Throwable {
        for (Segment<K, V> segment : this.segments) {
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
    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        int i = 0;
        if (obj == null) {
            return false;
        }
        long j = this.ticker.read();
        Segment<K, V>[] segmentArr = this.segments;
        long j2 = -1;
        int i2 = 0;
        while (i2 < 3) {
            int length = segmentArr.length;
            long j3 = 0;
            int i3 = i == true ? 1 : 0;
            while (i3 < length) {
                Segment<K, V> segment = segmentArr[i3];
                int i4 = segment.count;
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = segment.table;
                int i5 = i;
                while (i5 < atomicReferenceArray.length()) {
                    ReferenceEntry<K, V> next = atomicReferenceArray.get(i5);
                    while (next != null) {
                        Segment<K, V>[] segmentArr2 = segmentArr;
                        V liveValue = segment.getLiveValue(next, j);
                        ReferenceEntry<K, V> referenceEntry = next;
                        if (liveValue != null && this.valueEquivalence.equivalent(obj, liveValue)) {
                            return true;
                        }
                        next = referenceEntry.getNext();
                        segmentArr = segmentArr2;
                    }
                    i5++;
                }
                j3 += (long) segment.modCount;
                i3++;
                i = i5;
            }
            boolean z = i;
            Segment<K, V>[] segmentArr3 = segmentArr;
            if (j3 == j2) {
                return z;
            }
            i2++;
            j2 = j3;
            i = z ? 1 : 0;
            segmentArr = segmentArr3;
        }
        return i == true ? 1 : 0;
    }

    @VisibleForTesting
    public ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        return segmentFor(referenceEntry.getHash()).copyEntry(referenceEntry, referenceEntry2);
    }

    public Segment<K, V> createSegment(int i, long j, AbstractCache.StatsCounter statsCounter) {
        return new Segment<>(this, i, j, statsCounter);
    }

    public boolean customWeigher() {
        return this.weigher != CacheBuilder.OneWeigher.INSTANCE;
    }

    @Override // java.util.AbstractMap, java.util.Map
    @GwtIncompatible
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        EntrySet entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    public boolean evictsBySize() {
        return this.maxWeight >= 0;
    }

    public boolean expires() {
        return expiresAfterWrite() || expiresAfterAccess();
    }

    public boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos > 0;
    }

    public boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos > 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        if (obj == null) {
            return null;
        }
        int iHash = hash(obj);
        return segmentFor(iHash).get(obj, iHash);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ImmutableMap<K, V> getAll(Iterable<? extends K> iterable) throws ExecutionException {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        int i = 0;
        int i2 = 0;
        for (K k : iterable) {
            Object obj = get(k);
            if (!linkedHashMap.containsKey(k)) {
                linkedHashMap.put(k, obj);
                if (obj == null) {
                    i2++;
                    linkedHashSet.add(k);
                } else {
                    i++;
                }
            }
        }
        try {
            if (!linkedHashSet.isEmpty()) {
                try {
                    Map mapLoadAll = loadAll(Collections.unmodifiableSet(linkedHashSet), this.defaultLoader);
                    for (Object obj2 : linkedHashSet) {
                        Object obj3 = mapLoadAll.get(obj2);
                        if (obj3 == null) {
                            throw new CacheLoader.InvalidCacheLoadException("loadAll failed to return a value for " + obj2);
                        }
                        linkedHashMap.put(obj2, obj3);
                    }
                } catch (CacheLoader.UnsupportedLoadingOperationException unused) {
                    for (Object obj4 : linkedHashSet) {
                        i2--;
                        linkedHashMap.put(obj4, get(obj4, this.defaultLoader));
                    }
                }
            }
            ImmutableMap<K, V> immutableMapCopyOf = ImmutableMap.copyOf((Map) linkedHashMap);
            this.globalStatsCounter.recordHits(i);
            this.globalStatsCounter.recordMisses(i2);
            return immutableMapCopyOf;
        } catch (Throwable th) {
            this.globalStatsCounter.recordHits(i);
            this.globalStatsCounter.recordMisses(i2);
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ImmutableMap<K, V> getAllPresent(Iterable<?> iterable) {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        int i = 0;
        int i2 = 0;
        for (Object obj : iterable) {
            V v = get(obj);
            if (v == null) {
                i2++;
            } else {
                builder.put(obj, v);
                i++;
            }
        }
        this.globalStatsCounter.recordHits(i);
        this.globalStatsCounter.recordMisses(i2);
        return builder.buildKeepingLast();
    }

    public ReferenceEntry<K, V> getEntry(Object obj) {
        if (obj == null) {
            return null;
        }
        int iHash = hash(obj);
        return segmentFor(iHash).getEntry(obj, iHash);
    }

    public V getIfPresent(Object obj) {
        int iHash = hash(Preconditions.checkNotNull(obj));
        V v = segmentFor(iHash).get(obj, iHash);
        if (v == null) {
            this.globalStatsCounter.recordMisses(1);
            return v;
        }
        this.globalStatsCounter.recordHits(1);
        return v;
    }

    public V getLiveValue(ReferenceEntry<K, V> referenceEntry, long j) {
        V v;
        if (referenceEntry.getKey() == null || (v = referenceEntry.getValueReference().get()) == null || isExpired(referenceEntry, j)) {
            return null;
        }
        return v;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V getOrDefault(Object obj, V v) {
        V v2 = get(obj);
        return v2 != null ? v2 : v;
    }

    public V getOrLoad(K k) throws ExecutionException {
        return get(k, this.defaultLoader);
    }

    public int hash(Object obj) {
        return rehash(this.keyEquivalence.hash(obj));
    }

    public void invalidateAll(Iterable<?> iterable) {
        Iterator<?> it = iterable.iterator();
        while (it.hasNext()) {
            remove(it.next());
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        Segment<K, V>[] segmentArr = this.segments;
        long j = 0;
        for (Segment<K, V> segment : segmentArr) {
            if (segment.count != 0) {
                return false;
            }
            j += (long) segment.modCount;
        }
        if (j == 0) {
            return true;
        }
        for (Segment<K, V> segment2 : segmentArr) {
            if (segment2.count != 0) {
                return false;
            }
            j -= (long) segment2.modCount;
        }
        return j == 0;
    }

    public boolean isExpired(ReferenceEntry<K, V> referenceEntry, long j) {
        Preconditions.checkNotNull(referenceEntry);
        if (!expiresAfterAccess() || j - referenceEntry.getAccessTime() < this.expireAfterAccessNanos) {
            return expiresAfterWrite() && j - referenceEntry.getWriteTime() >= this.expireAfterWriteNanos;
        }
        return true;
    }

    @VisibleForTesting
    public boolean isLive(ReferenceEntry<K, V> referenceEntry, long j) {
        return segmentFor(referenceEntry.getHash()).getLiveValue(referenceEntry, j) != null;
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

    /* JADX WARN: Removed duplicated region for block: B:39:0x00b9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Map<K, V> loadAll(Set<? extends K> set, CacheLoader<? super K, V> cacheLoader) throws Throwable {
        Preconditions.checkNotNull(cacheLoader);
        Preconditions.checkNotNull(set);
        Stopwatch stopwatchCreateStarted = Stopwatch.createStarted();
        boolean z = true;
        boolean z2 = false;
        try {
            try {
                try {
                    Map<? super K, V> mapLoadAll = cacheLoader.loadAll(set);
                    if (mapLoadAll == null) {
                        this.globalStatsCounter.recordLoadException(stopwatchCreateStarted.elapsed(TimeUnit.NANOSECONDS));
                        throw new CacheLoader.InvalidCacheLoadException(cacheLoader + " returned null map from loadAll");
                    }
                    stopwatchCreateStarted.stop();
                    for (Map.Entry<K, V> entry : mapLoadAll.entrySet()) {
                        K key = entry.getKey();
                        V value = entry.getValue();
                        if (key == null || value == null) {
                            z2 = true;
                        } else {
                            put(key, value);
                        }
                    }
                    if (!z2) {
                        this.globalStatsCounter.recordLoadSuccess(stopwatchCreateStarted.elapsed(TimeUnit.NANOSECONDS));
                        return mapLoadAll;
                    }
                    this.globalStatsCounter.recordLoadException(stopwatchCreateStarted.elapsed(TimeUnit.NANOSECONDS));
                    throw new CacheLoader.InvalidCacheLoadException(cacheLoader + " returned null keys or values from loadAll");
                } catch (CacheLoader.UnsupportedLoadingOperationException e) {
                    try {
                        throw e;
                    } catch (Throwable th) {
                        th = th;
                        if (!z) {
                            this.globalStatsCounter.recordLoadException(stopwatchCreateStarted.elapsed(TimeUnit.NANOSECONDS));
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    throw new ExecutionException(e2);
                }
            } catch (Error e3) {
                throw new ExecutionError(e3);
            } catch (InterruptedException e4) {
                Thread.currentThread().interrupt();
                throw new ExecutionException(e4);
            } catch (RuntimeException e5) {
                throw new UncheckedExecutionException(e5);
            }
        } catch (Throwable th2) {
            th = th2;
            z = false;
            if (!z) {
            }
            throw th;
        }
    }

    public long longSize() {
        long jMax = 0;
        for (Segment<K, V> segment : this.segments) {
            jMax += (long) Math.max(0, segment.count);
        }
        return jMax;
    }

    @VisibleForTesting
    public ReferenceEntry<K, V> newEntry(K k, int i, ReferenceEntry<K, V> referenceEntry) {
        Segment<K, V> segmentSegmentFor = segmentFor(i);
        segmentSegmentFor.lock();
        try {
            return segmentSegmentFor.newEntry(k, i, referenceEntry);
        } finally {
            segmentSegmentFor.unlock();
        }
    }

    public final Segment<K, V>[] newSegmentArray(int i) {
        return new Segment[i];
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @VisibleForTesting
    public ValueReference<K, V> newValueReference(ReferenceEntry<K, V> referenceEntry, V v, int i) {
        return this.valueStrength.referenceValue(segmentFor(referenceEntry.getHash()), referenceEntry, Preconditions.checkNotNull(v), i);
    }

    public void processPendingNotifications() {
        while (true) {
            RemovalNotification<K, V> removalNotificationPoll = this.removalNotificationQueue.poll();
            if (removalNotificationPoll == null) {
                return;
            }
            try {
                this.removalListener.onRemoval(removalNotificationPoll);
            } catch (Throwable th) {
                logger.log(Level.WARNING, "Exception thrown by removal listener", th);
            }
        }
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

    public void reclaimKey(ReferenceEntry<K, V> referenceEntry) throws Throwable {
        int hash = referenceEntry.getHash();
        segmentFor(hash).reclaimKey(referenceEntry, hash);
    }

    public void reclaimValue(ValueReference<K, V> valueReference) throws Throwable {
        ReferenceEntry<K, V> entry = valueReference.getEntry();
        int hash = entry.getHash();
        segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
    }

    public boolean recordsAccess() {
        return expiresAfterAccess();
    }

    public boolean recordsTime() {
        return recordsWrite() || recordsAccess();
    }

    public boolean recordsWrite() {
        return expiresAfterWrite() || refreshes();
    }

    public void refresh(K k) {
        int iHash = hash(Preconditions.checkNotNull(k));
        segmentFor(iHash).refresh(k, iHash, this.defaultLoader, false);
    }

    public boolean refreshes() {
        return this.refreshNanos > 0;
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

    public Segment<K, V> segmentFor(int i) {
        return this.segments[(i >>> this.segmentShift) & this.segmentMask];
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return Ints.saturatedCast(longSize());
    }

    public boolean usesAccessEntries() {
        return usesAccessQueue() || recordsAccess();
    }

    public boolean usesAccessQueue() {
        return expiresAfterAccess() || evictsBySize();
    }

    public boolean usesKeyReferences() {
        return this.keyStrength != Strength.STRONG;
    }

    public boolean usesValueReferences() {
        return this.valueStrength != Strength.STRONG;
    }

    public boolean usesWriteEntries() {
        return usesWriteQueue() || recordsWrite();
    }

    public boolean usesWriteQueue() {
        return expiresAfterWrite();
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

    public V get(K k, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
        int iHash = hash(Preconditions.checkNotNull(k));
        return segmentFor(iHash).get(k, iHash, cacheLoader);
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

    public static final class Segment<K, V> extends ReentrantLock {
        final Queue<ReferenceEntry<K, V>> accessQueue;
        volatile int count;
        final ReferenceQueue<K> keyReferenceQueue;
        final LocalCache<K, V> map;
        final long maxSegmentWeight;
        int modCount;
        final AtomicInteger readCount = new AtomicInteger();
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final AbstractCache.StatsCounter statsCounter;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        int threshold;
        long totalWeight;
        final ReferenceQueue<V> valueReferenceQueue;
        final Queue<ReferenceEntry<K, V>> writeQueue;

        public Segment(LocalCache<K, V> localCache, int i, long j, AbstractCache.StatsCounter statsCounter) {
            this.map = localCache;
            this.maxSegmentWeight = j;
            this.statsCounter = (AbstractCache.StatsCounter) Preconditions.checkNotNull(statsCounter);
            initTable(newEntryArray(i));
            this.keyReferenceQueue = localCache.usesKeyReferences() ? new ReferenceQueue<>() : null;
            this.valueReferenceQueue = localCache.usesValueReferences() ? new ReferenceQueue<>() : null;
            this.recencyQueue = localCache.usesAccessQueue() ? new ConcurrentLinkedQueue<>() : LocalCache.discardingQueue();
            this.writeQueue = localCache.usesWriteQueue() ? new WriteQueue<>() : LocalCache.discardingQueue();
            this.accessQueue = localCache.usesAccessQueue() ? new AccessQueue<>() : LocalCache.discardingQueue();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$loadAsync$0(Object obj, int i, LoadingValueReference loadingValueReference, ListenableFuture listenableFuture) {
            try {
                getAndRecordStats(obj, i, loadingValueReference, listenableFuture);
            } catch (Throwable th) {
                LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", th);
                loadingValueReference.setException(th);
            }
        }

        public void cleanUp() {
            runLockedCleanup(this.map.ticker.read());
            runUnlockedCleanup();
        }

        public void clear() throws Throwable {
            if (this.count == 0) {
                return;
            }
            lock();
            try {
                preWriteCleanup(this.map.ticker.read());
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                for (int i = 0; i < atomicReferenceArray.length(); i++) {
                    for (ReferenceEntry<K, V> next = atomicReferenceArray.get(i); next != null; next = next.getNext()) {
                        if (next.getValueReference().isActive()) {
                            K key = next.getKey();
                            V v = next.getValueReference().get();
                            try {
                                enqueueNotification(key, next.getHash(), v, next.getValueReference().getWeight(), (key == null || v == null) ? RemovalCause.COLLECTED : RemovalCause.EXPLICIT);
                            } catch (Throwable th) {
                                th = th;
                                unlock();
                                postWriteCleanup();
                                throw th;
                            }
                        }
                    }
                }
                for (int i2 = 0; i2 < atomicReferenceArray.length(); i2++) {
                    atomicReferenceArray.set(i2, null);
                }
                clearReferenceQueues();
                this.writeQueue.clear();
                this.accessQueue.clear();
                this.readCount.set(0);
                this.modCount++;
                this.count = 0;
                unlock();
                postWriteCleanup();
            } catch (Throwable th2) {
                th = th2;
            }
        }

        public void clearKeyReferenceQueue() {
            while (this.keyReferenceQueue.poll() != null) {
            }
        }

        public void clearReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                clearKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                clearValueReferenceQueue();
            }
        }

        public void clearValueReferenceQueue() {
            while (this.valueReferenceQueue.poll() != null) {
            }
        }

        public boolean containsKey(Object obj, int i) {
            try {
                if (this.count == 0) {
                    return false;
                }
                ReferenceEntry<K, V> liveEntry = getLiveEntry(obj, i, this.map.ticker.read());
                if (liveEntry == null) {
                    return false;
                }
                return liveEntry.getValueReference().get() != null;
            } finally {
                postReadCleanup();
            }
        }

        @VisibleForTesting
        public boolean containsValue(Object obj) {
            try {
                if (this.count != 0) {
                    long j = this.map.ticker.read();
                    AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                    int length = atomicReferenceArray.length();
                    for (int i = 0; i < length; i++) {
                        for (ReferenceEntry<K, V> next = atomicReferenceArray.get(i); next != null; next = next.getNext()) {
                            V liveValue = getLiveValue(next, j);
                            if (liveValue != null && this.map.valueEquivalence.equivalent(obj, liveValue)) {
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

        public ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            K key = referenceEntry.getKey();
            if (key == null) {
                return null;
            }
            ValueReference<K, V> valueReference = referenceEntry.getValueReference();
            V v = valueReference.get();
            if (v == null && valueReference.isActive()) {
                return null;
            }
            ReferenceEntry<K, V> referenceEntryCopyEntry = this.map.entryFactory.copyEntry(this, referenceEntry, referenceEntry2, key);
            referenceEntryCopyEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, v, referenceEntryCopyEntry));
            return referenceEntryCopyEntry;
        }

        public void drainKeyReferenceQueue() throws Throwable {
            int i = 0;
            do {
                Reference<? extends K> referencePoll = this.keyReferenceQueue.poll();
                if (referencePoll == null) {
                    return;
                }
                this.map.reclaimKey((ReferenceEntry) referencePoll);
                i++;
            } while (i != 16);
        }

        public void drainRecencyQueue() {
            while (true) {
                ReferenceEntry<K, V> referenceEntryPoll = this.recencyQueue.poll();
                if (referenceEntryPoll == null) {
                    return;
                }
                if (this.accessQueue.contains(referenceEntryPoll)) {
                    this.accessQueue.add(referenceEntryPoll);
                }
            }
        }

        public void drainReferenceQueues() throws Throwable {
            if (this.map.usesKeyReferences()) {
                drainKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                drainValueReferenceQueue();
            }
        }

        public void drainValueReferenceQueue() throws Throwable {
            int i = 0;
            do {
                Reference<? extends V> referencePoll = this.valueReferenceQueue.poll();
                if (referencePoll == null) {
                    return;
                }
                this.map.reclaimValue((ValueReference) referencePoll);
                i++;
            } while (i != 16);
        }

        public void enqueueNotification(K k, int i, V v, int i2, RemovalCause removalCause) {
            this.totalWeight -= (long) i2;
            if (removalCause.wasEvicted()) {
                this.statsCounter.recordEviction();
            }
            if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
                this.map.removalNotificationQueue.offer(RemovalNotification.create(k, v, removalCause));
            }
        }

        public void evictEntries(ReferenceEntry<K, V> referenceEntry) {
            if (this.map.evictsBySize()) {
                drainRecencyQueue();
                if (referenceEntry.getValueReference().getWeight() > this.maxSegmentWeight && !removeEntry(referenceEntry, referenceEntry.getHash(), RemovalCause.SIZE)) {
                    throw new AssertionError();
                }
                while (this.totalWeight > this.maxSegmentWeight) {
                    ReferenceEntry<K, V> nextEvictable = getNextEvictable();
                    if (!removeEntry(nextEvictable, nextEvictable.getHash(), RemovalCause.SIZE)) {
                        throw new AssertionError();
                    }
                }
            }
        }

        public void expand() {
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
            int length = atomicReferenceArray.length();
            if (length >= 1073741824) {
                return;
            }
            int i = this.count;
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArrayNewEntryArray = newEntryArray(length << 1);
            this.threshold = (atomicReferenceArrayNewEntryArray.length() * 3) / 4;
            int length2 = atomicReferenceArrayNewEntryArray.length() - 1;
            for (int i2 = 0; i2 < length; i2++) {
                ReferenceEntry<K, V> next = atomicReferenceArray.get(i2);
                if (next != null) {
                    ReferenceEntry<K, V> next2 = next.getNext();
                    int hash = next.getHash() & length2;
                    if (next2 == null) {
                        atomicReferenceArrayNewEntryArray.set(hash, next);
                    } else {
                        ReferenceEntry<K, V> referenceEntry = next;
                        while (next2 != null) {
                            int hash2 = next2.getHash() & length2;
                            if (hash2 != hash) {
                                referenceEntry = next2;
                                hash = hash2;
                            }
                            next2 = next2.getNext();
                        }
                        atomicReferenceArrayNewEntryArray.set(hash, referenceEntry);
                        while (next != referenceEntry) {
                            int hash3 = next.getHash() & length2;
                            ReferenceEntry<K, V> referenceEntryCopyEntry = copyEntry(next, atomicReferenceArrayNewEntryArray.get(hash3));
                            if (referenceEntryCopyEntry != null) {
                                atomicReferenceArrayNewEntryArray.set(hash3, referenceEntryCopyEntry);
                            } else {
                                removeCollectedEntry(next);
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

        public void expireEntries(long j) {
            ReferenceEntry<K, V> referenceEntryPeek;
            ReferenceEntry<K, V> referenceEntryPeek2;
            drainRecencyQueue();
            do {
                referenceEntryPeek = this.writeQueue.peek();
                if (referenceEntryPeek == null || !this.map.isExpired(referenceEntryPeek, j)) {
                    do {
                        referenceEntryPeek2 = this.accessQueue.peek();
                        if (referenceEntryPeek2 == null || !this.map.isExpired(referenceEntryPeek2, j)) {
                            return;
                        }
                    } while (removeEntry(referenceEntryPeek2, referenceEntryPeek2.getHash(), RemovalCause.EXPIRED));
                    throw new AssertionError();
                }
            } while (removeEntry(referenceEntryPeek, referenceEntryPeek.getHash(), RemovalCause.EXPIRED));
            throw new AssertionError();
        }

        public V get(K k, int i, CacheLoader<? super K, V> cacheLoader) throws Throwable {
            K k2;
            int i2;
            CacheLoader<? super K, V> cacheLoader2;
            ReferenceEntry<K, V> entry;
            Preconditions.checkNotNull(k);
            Preconditions.checkNotNull(cacheLoader);
            try {
                try {
                    try {
                    } catch (ExecutionException e) {
                        e = e;
                    }
                } catch (Throwable th) {
                    th = th;
                    Throwable th2 = th;
                    postReadCleanup();
                    throw th2;
                }
            } catch (ExecutionException e2) {
                e = e2;
            } catch (Throwable th3) {
                th = th3;
                Throwable th22 = th;
                postReadCleanup();
                throw th22;
            }
            if (this.count != 0 && (entry = getEntry(k, i)) != null) {
                long j = this.map.ticker.read();
                V liveValue = getLiveValue(entry, j);
                if (liveValue != null) {
                    recordRead(entry, j);
                    this.statsCounter.recordHits(1);
                    V vScheduleRefresh = scheduleRefresh(entry, k, i, liveValue, j, cacheLoader);
                    postReadCleanup();
                    return vScheduleRefresh;
                }
                k2 = k;
                i2 = i;
                cacheLoader2 = cacheLoader;
                ValueReference<K, V> valueReference = entry.getValueReference();
                if (valueReference.isLoading()) {
                    V vWaitForLoadingValue = waitForLoadingValue(entry, k2, valueReference);
                    postReadCleanup();
                    return vWaitForLoadingValue;
                }
                ExecutionException executionException = e;
                Throwable cause = executionException.getCause();
                if (cause instanceof Error) {
                    throw new ExecutionError((Error) cause);
                }
                if (cause instanceof RuntimeException) {
                    throw new UncheckedExecutionException(cause);
                }
                throw executionException;
            }
            k2 = k;
            i2 = i;
            cacheLoader2 = cacheLoader;
            V vLockedGetOrLoad = lockedGetOrLoad(k2, i2, cacheLoader2);
            postReadCleanup();
            return vLockedGetOrLoad;
        }

        public V getAndRecordStats(K k, int i, LoadingValueReference<K, V> loadingValueReference, ListenableFuture<V> listenableFuture) throws Throwable {
            V v;
            try {
                v = (V) Uninterruptibles.getUninterruptibly(listenableFuture);
                try {
                    if (v != null) {
                        this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
                        storeLoadedValue(k, i, loadingValueReference, v);
                        return v;
                    }
                    throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + k + ".");
                } catch (Throwable th) {
                    th = th;
                    if (v == null) {
                        this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                        removeLoadingValue(k, i, loadingValueReference);
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                v = null;
            }
        }

        public ReferenceEntry<K, V> getEntry(Object obj, int i) {
            for (ReferenceEntry<K, V> first = getFirst(i); first != null; first = first.getNext()) {
                if (first.getHash() == i) {
                    K key = first.getKey();
                    if (key == null) {
                        tryDrainReferenceQueues();
                    } else if (this.map.keyEquivalence.equivalent(obj, key)) {
                        return first;
                    }
                }
            }
            return null;
        }

        public ReferenceEntry<K, V> getFirst(int i) {
            return this.table.get(i & (r0.length() - 1));
        }

        public ReferenceEntry<K, V> getLiveEntry(Object obj, int i, long j) {
            ReferenceEntry<K, V> entry = getEntry(obj, i);
            if (entry == null) {
                return null;
            }
            if (!this.map.isExpired(entry, j)) {
                return entry;
            }
            tryExpireEntries(j);
            return null;
        }

        public V getLiveValue(ReferenceEntry<K, V> referenceEntry, long j) {
            if (referenceEntry.getKey() == null) {
                tryDrainReferenceQueues();
                return null;
            }
            V v = referenceEntry.getValueReference().get();
            if (v == null) {
                tryDrainReferenceQueues();
                return null;
            }
            if (!this.map.isExpired(referenceEntry, j)) {
                return v;
            }
            tryExpireEntries(j);
            return null;
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            for (ReferenceEntry<K, V> referenceEntry : this.accessQueue) {
                if (referenceEntry.getValueReference().getWeight() > 0) {
                    return referenceEntry;
                }
            }
            throw new AssertionError();
        }

        public void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray) {
            this.threshold = (atomicReferenceArray.length() * 3) / 4;
            if (!this.map.customWeigher()) {
                int i = this.threshold;
                if (i == this.maxSegmentWeight) {
                    this.threshold = i + 1;
                }
            }
            this.table = atomicReferenceArray;
        }

        /* JADX WARN: Finally extract failed */
        public LoadingValueReference<K, V> insertLoadingValueReference(K k, int i, boolean z) {
            lock();
            try {
                long j = this.map.ticker.read();
                preWriteCleanup(j);
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry<K, V> referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry next = referenceEntry; next != null; next = next.getNext()) {
                    Object key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        ValueReference<K, V> valueReference = next.getValueReference();
                        if (!valueReference.isLoading() && (!z || j - next.getWriteTime() >= this.map.refreshNanos)) {
                            this.modCount++;
                            LoadingValueReference<K, V> loadingValueReference = new LoadingValueReference<>(valueReference);
                            next.setValueReference(loadingValueReference);
                            unlock();
                            postWriteCleanup();
                            return loadingValueReference;
                        }
                        unlock();
                        postWriteCleanup();
                        return null;
                    }
                }
                this.modCount++;
                LoadingValueReference<K, V> loadingValueReference2 = new LoadingValueReference<>();
                ReferenceEntry<K, V> referenceEntryNewEntry = newEntry(k, i, referenceEntry);
                referenceEntryNewEntry.setValueReference(loadingValueReference2);
                atomicReferenceArray.set(length, referenceEntryNewEntry);
                unlock();
                postWriteCleanup();
                return loadingValueReference2;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
                throw th;
            }
        }

        public ListenableFuture<V> loadAsync(final K k, final int i, final LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> cacheLoader) {
            final ListenableFuture<V> listenableFutureLoadFuture = loadingValueReference.loadFuture(k, cacheLoader);
            listenableFutureLoadFuture.addListener(new Runnable() { // from class: com.google.common.cache.c
                @Override // java.lang.Runnable
                public final void run() {
                    this.a.lambda$loadAsync$0(k, i, loadingValueReference, listenableFutureLoadFuture);
                }
            }, MoreExecutors.directExecutor());
            return listenableFutureLoadFuture;
        }

        public V loadSync(K k, int i, LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
            return getAndRecordStats(k, i, loadingValueReference, loadingValueReference.loadFuture(k, cacheLoader));
        }

        /* JADX WARN: Code restructure failed: missing block: B:29:0x00a0, code lost:
        
            if (r2 == false) goto L34;
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x00a2, code lost:
        
            r13 = new com.google.common.cache.LocalCache.LoadingValueReference<>();
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x00a7, code lost:
        
            if (r12 != null) goto L33;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x00a9, code lost:
        
            r12 = newEntry(r16, r3, r11);
            r12.setValueReference(r13);
            r9.set(r10, r12);
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x00b4, code lost:
        
            r12.setValueReference(r13);
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x00b7, code lost:
        
            unlock();
            postWriteCleanup();
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x00bd, code lost:
        
            if (r2 == false) goto L48;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x00bf, code lost:
        
            monitor-enter(r12);
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x00c2, code lost:
        
            r0 = loadSync(r16, r3, r13, r18);
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x00c6, code lost:
        
            monitor-exit(r12);
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x00cc, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x00d0, code lost:
        
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x00d1, code lost:
        
            r15.statsCounter.recordMisses(1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x00d6, code lost:
        
            throw r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x00db, code lost:
        
            return waitForLoadingValue(r12, r16, r14);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public V lockedGetOrLoad(K k, int i, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
            boolean z;
            ValueReference<K, V> valueReference;
            int i2 = i;
            lock();
            try {
                long j = this.map.ticker.read();
                preWriteCleanup(j);
                int i3 = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int length = i2 & (atomicReferenceArray.length() - 1);
                ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get(length);
                ReferenceEntry<K, V> referenceEntryNewEntry = referenceEntry;
                while (true) {
                    LoadingValueReference<K, V> loadingValueReference = null;
                    if (referenceEntryNewEntry == null) {
                        z = true;
                        valueReference = null;
                        break;
                    }
                    long j2 = j;
                    K key = referenceEntryNewEntry.getKey();
                    if (referenceEntryNewEntry.getHash() == i2 && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        valueReference = referenceEntryNewEntry.getValueReference();
                        if (valueReference.isLoading()) {
                            z = false;
                        } else {
                            V v = valueReference.get();
                            if (v == null) {
                                enqueueNotification(key, i2, v, valueReference.getWeight(), RemovalCause.COLLECTED);
                                i2 = i;
                            } else {
                                if (!this.map.isExpired(referenceEntryNewEntry, j2)) {
                                    recordLockedRead(referenceEntryNewEntry, j2);
                                    this.statsCounter.recordHits(1);
                                    unlock();
                                    postWriteCleanup();
                                    return v;
                                }
                                i2 = i;
                                enqueueNotification(key, i2, v, valueReference.getWeight(), RemovalCause.EXPIRED);
                            }
                            this.writeQueue.remove(referenceEntryNewEntry);
                            this.accessQueue.remove(referenceEntryNewEntry);
                            this.count = i3;
                            z = true;
                        }
                    } else {
                        referenceEntryNewEntry = referenceEntryNewEntry.getNext();
                        j = j2;
                    }
                }
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
                throw th;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        public ReferenceEntry<K, V> newEntry(K k, int i, ReferenceEntry<K, V> referenceEntry) {
            return this.map.entryFactory.newEntry(this, Preconditions.checkNotNull(k), i, referenceEntry);
        }

        public AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int i) {
            return new AtomicReferenceArray<>(i);
        }

        public void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & LocalCache.DRAIN_THRESHOLD) == 0) {
                cleanUp();
            }
        }

        public void postWriteCleanup() {
            runUnlockedCleanup();
        }

        public void preWriteCleanup(long j) {
            runLockedCleanup(j);
        }

        /* JADX WARN: Code restructure failed: missing block: B:23:0x0088, code lost:
        
            unlock();
            postWriteCleanup();
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x008e, code lost:
        
            return null;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public V put(K k, int i, V v, boolean z) {
            ReferenceEntry<K, V> referenceEntry;
            int i2;
            lock();
            try {
                long j = this.map.ticker.read();
                preWriteCleanup(j);
                if (this.count + 1 > this.threshold) {
                    expand();
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int length = i & (atomicReferenceArray.length() - 1);
                ReferenceEntry<K, V> referenceEntry2 = atomicReferenceArray.get(length);
                ReferenceEntry<K, V> next = referenceEntry2;
                while (true) {
                    if (next == null) {
                        this.modCount++;
                        ReferenceEntry<K, V> referenceEntryNewEntry = newEntry(k, i, referenceEntry2);
                        setValue(referenceEntryNewEntry, k, v, j);
                        atomicReferenceArray.set(length, referenceEntryNewEntry);
                        this.count++;
                        evictEntries(referenceEntryNewEntry);
                        break;
                    }
                    K key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        ValueReference<K, V> valueReference = next.getValueReference();
                        V v2 = valueReference.get();
                        if (v2 != null) {
                            long j2 = j;
                            ReferenceEntry<K, V> referenceEntry3 = next;
                            if (z) {
                                recordLockedRead(referenceEntry3, j2);
                                unlock();
                                postWriteCleanup();
                                return v2;
                            }
                            this.modCount++;
                            enqueueNotification(k, i, v2, valueReference.getWeight(), RemovalCause.REPLACED);
                            setValue(referenceEntry3, k, v, j2);
                            evictEntries(referenceEntry3);
                            unlock();
                            postWriteCleanup();
                            return v2;
                        }
                        this.modCount++;
                        if (valueReference.isActive()) {
                            enqueueNotification(k, i, v2, valueReference.getWeight(), RemovalCause.COLLECTED);
                            ReferenceEntry<K, V> referenceEntry4 = next;
                            setValue(referenceEntry4, k, v, j);
                            i2 = this.count;
                            referenceEntry = referenceEntry4;
                        } else {
                            ReferenceEntry<K, V> referenceEntry5 = next;
                            setValue(referenceEntry5, k, v, j);
                            referenceEntry = referenceEntry5;
                            i2 = this.count + 1;
                        }
                        this.count = i2;
                        evictEntries(referenceEntry);
                    } else {
                        long j3 = j;
                        next = next.getNext();
                        j = j3;
                    }
                }
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
                throw th;
            }
        }

        public boolean reclaimKey(ReferenceEntry<K, V> referenceEntry, int i) throws Throwable {
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray;
            int length;
            ReferenceEntry<K, V> referenceEntry2;
            ReferenceEntry<K, V> next;
            lock();
            try {
                atomicReferenceArray = this.table;
                length = (atomicReferenceArray.length() - 1) & i;
                referenceEntry2 = atomicReferenceArray.get(length);
                next = referenceEntry2;
            } catch (Throwable th) {
                th = th;
            }
            while (next != null) {
                if (next == referenceEntry) {
                    this.modCount++;
                    ReferenceEntry<K, V> referenceEntryRemoveValueFromChain = removeValueFromChain(referenceEntry2, next, next.getKey(), i, next.getValueReference().get(), next.getValueReference(), RemovalCause.COLLECTED);
                    int i2 = this.count - 1;
                    atomicReferenceArray.set(length, referenceEntryRemoveValueFromChain);
                    this.count = i2;
                    unlock();
                    postWriteCleanup();
                    return true;
                }
                int i3 = i;
                try {
                    next = next.getNext();
                    i = i3;
                } catch (Throwable th2) {
                    th = th2;
                }
                th = th2;
                Throwable th3 = th;
                unlock();
                postWriteCleanup();
                throw th3;
            }
            unlock();
            postWriteCleanup();
            return false;
        }

        /* JADX WARN: Removed duplicated region for block: B:37:0x008f  */
        /* JADX WARN: Removed duplicated region for block: B:45:? A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean reclaimValue(K k, int i, ValueReference<K, V> valueReference) throws Throwable {
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray;
            int length;
            ReferenceEntry<K, V> referenceEntry;
            ReferenceEntry<K, V> next;
            lock();
            try {
                atomicReferenceArray = this.table;
                length = (atomicReferenceArray.length() - 1) & i;
                referenceEntry = atomicReferenceArray.get(length);
                next = referenceEntry;
            } catch (Throwable th) {
                th = th;
            }
            while (next != null) {
                K key = next.getKey();
                if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                    if (next.getValueReference() != valueReference) {
                        unlock();
                        if (!isHeldByCurrentThread()) {
                            postWriteCleanup();
                        }
                        return false;
                    }
                    this.modCount++;
                    ReferenceEntry<K, V> referenceEntryRemoveValueFromChain = removeValueFromChain(referenceEntry, next, key, i, valueReference.get(), valueReference, RemovalCause.COLLECTED);
                    int i2 = this.count - 1;
                    atomicReferenceArray.set(length, referenceEntryRemoveValueFromChain);
                    this.count = i2;
                    unlock();
                    if (!isHeldByCurrentThread()) {
                        postWriteCleanup();
                    }
                    return true;
                    Throwable th2 = th;
                    unlock();
                    if (!isHeldByCurrentThread()) {
                        throw th2;
                    }
                    postWriteCleanup();
                    throw th2;
                }
                int i3 = i;
                ValueReference<K, V> valueReference2 = valueReference;
                try {
                    next = next.getNext();
                    i = i3;
                    valueReference = valueReference2;
                } catch (Throwable th3) {
                    th = th3;
                }
                th = th3;
                Throwable th22 = th;
                unlock();
                if (!isHeldByCurrentThread()) {
                }
            }
            unlock();
            if (!isHeldByCurrentThread()) {
                postWriteCleanup();
            }
            return false;
        }

        public void recordLockedRead(ReferenceEntry<K, V> referenceEntry, long j) {
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(j);
            }
            this.accessQueue.add(referenceEntry);
        }

        public void recordRead(ReferenceEntry<K, V> referenceEntry, long j) {
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(j);
            }
            this.recencyQueue.add(referenceEntry);
        }

        public void recordWrite(ReferenceEntry<K, V> referenceEntry, int i, long j) {
            drainRecencyQueue();
            this.totalWeight += (long) i;
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(j);
            }
            if (this.map.recordsWrite()) {
                referenceEntry.setWriteTime(j);
            }
            this.accessQueue.add(referenceEntry);
            this.writeQueue.add(referenceEntry);
        }

        public V refresh(K k, int i, CacheLoader<? super K, V> cacheLoader, boolean z) {
            LoadingValueReference<K, V> loadingValueReferenceInsertLoadingValueReference = insertLoadingValueReference(k, i, z);
            if (loadingValueReferenceInsertLoadingValueReference == null) {
                return null;
            }
            ListenableFuture<V> listenableFutureLoadAsync = loadAsync(k, i, loadingValueReferenceInsertLoadingValueReference, cacheLoader);
            if (listenableFutureLoadAsync.isDone()) {
                try {
                    return (V) Uninterruptibles.getUninterruptibly(listenableFutureLoadAsync);
                } catch (Throwable unused) {
                }
            }
            return null;
        }

        /* JADX WARN: Code restructure failed: missing block: B:11:0x0038, code lost:
        
            r9 = r5.getValueReference();
            r8 = r9.get();
         */
        /* JADX WARN: Code restructure failed: missing block: B:12:0x0040, code lost:
        
            if (r8 == null) goto L17;
         */
        /* JADX WARN: Code restructure failed: missing block: B:13:0x0042, code lost:
        
            r12 = com.google.common.cache.RemovalCause.EXPLICIT;
         */
        /* JADX WARN: Code restructure failed: missing block: B:14:0x0044, code lost:
        
            r10 = r12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:15:0x0046, code lost:
        
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x0047, code lost:
        
            r12 = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x004e, code lost:
        
            if (r9.isActive() == false) goto L29;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x0050, code lost:
        
            r12 = com.google.common.cache.RemovalCause.COLLECTED;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0053, code lost:
        
            r11.modCount++;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x005b, code lost:
        
            r12 = removeValueFromChain(r4, r5, r6, r13, r8, r9, r10);
            r13 = r11.count - 1;
            r0.set(r1, r12);
            r11.count = r13;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0068, code lost:
        
            unlock();
            postWriteCleanup();
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x006e, code lost:
        
            return r8;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public V remove(Object obj, int i) throws Throwable {
            ReferenceEntry<K, V> next;
            lock();
            try {
                preWriteCleanup(this.map.ticker.read());
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get(length);
                next = referenceEntry;
            } catch (Throwable th) {
                th = th;
            }
            while (true) {
                if (next == null) {
                    break;
                }
                K key = next.getKey();
                if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(obj, key)) {
                    break;
                }
                int i2 = i;
                try {
                    next = next.getNext();
                    i = i2;
                } catch (Throwable th2) {
                    th = th2;
                }
                th = th2;
                Throwable th3 = th;
                unlock();
                postWriteCleanup();
                throw th3;
            }
            unlock();
            postWriteCleanup();
            return null;
        }

        public void removeCollectedEntry(ReferenceEntry<K, V> referenceEntry) {
            enqueueNotification(referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry.getValueReference().get(), referenceEntry.getValueReference().getWeight(), RemovalCause.COLLECTED);
            this.writeQueue.remove(referenceEntry);
            this.accessQueue.remove(referenceEntry);
        }

        @VisibleForTesting
        public boolean removeEntry(ReferenceEntry<K, V> referenceEntry, int i, RemovalCause removalCause) {
            AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
            int length = (atomicReferenceArray.length() - 1) & i;
            ReferenceEntry<K, V> referenceEntry2 = atomicReferenceArray.get(length);
            for (ReferenceEntry<K, V> next = referenceEntry2; next != null; next = next.getNext()) {
                if (next == referenceEntry) {
                    this.modCount++;
                    ReferenceEntry<K, V> referenceEntryRemoveValueFromChain = removeValueFromChain(referenceEntry2, next, next.getKey(), i, next.getValueReference().get(), next.getValueReference(), removalCause);
                    int i2 = this.count - 1;
                    atomicReferenceArray.set(length, referenceEntryRemoveValueFromChain);
                    this.count = i2;
                    return true;
                }
            }
            return false;
        }

        public ReferenceEntry<K, V> removeEntryFromChain(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            int i = this.count;
            ReferenceEntry<K, V> next = referenceEntry2.getNext();
            while (referenceEntry != referenceEntry2) {
                ReferenceEntry<K, V> referenceEntryCopyEntry = copyEntry(referenceEntry, next);
                if (referenceEntryCopyEntry != null) {
                    next = referenceEntryCopyEntry;
                } else {
                    removeCollectedEntry(referenceEntry);
                    i--;
                }
                referenceEntry = referenceEntry.getNext();
            }
            this.count = i;
            return next;
        }

        public boolean removeLoadingValue(K k, int i, LoadingValueReference<K, V> loadingValueReference) {
            lock();
            try {
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get(length);
                ReferenceEntry<K, V> next = referenceEntry;
                while (true) {
                    if (next == null) {
                        break;
                    }
                    K key = next.getKey();
                    if (next.getHash() != i || key == null || !this.map.keyEquivalence.equivalent(k, key)) {
                        next = next.getNext();
                    } else if (next.getValueReference() == loadingValueReference) {
                        if (loadingValueReference.isActive()) {
                            next.setValueReference(loadingValueReference.getOldValue());
                        } else {
                            atomicReferenceArray.set(length, removeEntryFromChain(referenceEntry, next));
                        }
                        unlock();
                        postWriteCleanup();
                        return true;
                    }
                }
                unlock();
                postWriteCleanup();
                return false;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
                throw th;
            }
        }

        public ReferenceEntry<K, V> removeValueFromChain(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, K k, int i, V v, ValueReference<K, V> valueReference, RemovalCause removalCause) {
            enqueueNotification(k, i, v, valueReference.getWeight(), removalCause);
            this.writeQueue.remove(referenceEntry2);
            this.accessQueue.remove(referenceEntry2);
            if (!valueReference.isLoading()) {
                return removeEntryFromChain(referenceEntry, referenceEntry2);
            }
            valueReference.notifyNewValue(null);
            return referenceEntry;
        }

        /* JADX WARN: Code restructure failed: missing block: B:20:0x006f, code lost:
        
            return false;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean replace(K k, int i, V v, V v2) {
            int i2 = i;
            lock();
            try {
                long j = this.map.ticker.read();
                preWriteCleanup(j);
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int length = i2 & (atomicReferenceArray.length() - 1);
                ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get(length);
                ReferenceEntry<K, V> next = referenceEntry;
                while (true) {
                    if (next == null) {
                        break;
                    }
                    ReferenceEntry<K, V> referenceEntry2 = next;
                    K key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i2 && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        long j2 = j;
                        ValueReference<K, V> valueReference = referenceEntry2.getValueReference();
                        V v3 = valueReference.get();
                        if (v3 == null) {
                            if (valueReference.isActive()) {
                                this.modCount++;
                                ReferenceEntry<K, V> referenceEntryRemoveValueFromChain = removeValueFromChain(referenceEntry, referenceEntry2, key, i2, v3, valueReference, RemovalCause.COLLECTED);
                                int i3 = this.count - 1;
                                atomicReferenceArray.set(length, referenceEntryRemoveValueFromChain);
                                this.count = i3;
                            }
                        } else {
                            if (this.map.valueEquivalence.equivalent(v, v3)) {
                                this.modCount++;
                                enqueueNotification(k, i, v3, valueReference.getWeight(), RemovalCause.REPLACED);
                                setValue(referenceEntry2, k, v2, j2);
                                evictEntries(referenceEntry2);
                                return true;
                            }
                            recordLockedRead(referenceEntry2, j2);
                        }
                    } else {
                        ReferenceEntry<K, V> referenceEntry3 = referenceEntry;
                        long j3 = j;
                        next = referenceEntry2.getNext();
                        referenceEntry = referenceEntry3;
                        i2 = i;
                        j = j3;
                    }
                }
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        public void runLockedCleanup(long j) {
            if (tryLock()) {
                try {
                    drainReferenceQueues();
                    expireEntries(j);
                    this.readCount.set(0);
                } finally {
                    unlock();
                }
            }
        }

        public void runUnlockedCleanup() {
            if (isHeldByCurrentThread()) {
                return;
            }
            this.map.processPendingNotifications();
        }

        public V scheduleRefresh(ReferenceEntry<K, V> referenceEntry, K k, int i, V v, long j, CacheLoader<? super K, V> cacheLoader) {
            V vRefresh;
            return (!this.map.refreshes() || j - referenceEntry.getWriteTime() <= this.map.refreshNanos || referenceEntry.getValueReference().isLoading() || (vRefresh = refresh(k, i, cacheLoader, true)) == null) ? v : vRefresh;
        }

        public void setValue(ReferenceEntry<K, V> referenceEntry, K k, V v, long j) {
            ValueReference<K, V> valueReference = referenceEntry.getValueReference();
            int iWeigh = this.map.weigher.weigh(k, v);
            Preconditions.checkState(iWeigh >= 0, "Weights must be non-negative");
            referenceEntry.setValueReference(this.map.valueStrength.referenceValue(this, referenceEntry, v, iWeigh));
            recordWrite(referenceEntry, iWeigh, j);
            valueReference.notifyNewValue(v);
        }

        public boolean storeLoadedValue(K k, int i, LoadingValueReference<K, V> loadingValueReference, V v) {
            lock();
            try {
                long j = this.map.ticker.read();
                preWriteCleanup(j);
                int i2 = this.count + 1;
                if (i2 > this.threshold) {
                    expand();
                    i2 = this.count + 1;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int length = i & (atomicReferenceArray.length() - 1);
                ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get(length);
                ReferenceEntry<K, V> next = referenceEntry;
                while (true) {
                    if (next == null) {
                        this.modCount++;
                        ReferenceEntry<K, V> referenceEntryNewEntry = newEntry(k, i, referenceEntry);
                        setValue(referenceEntryNewEntry, k, v, j);
                        atomicReferenceArray.set(length, referenceEntryNewEntry);
                        this.count = i2;
                        evictEntries(referenceEntryNewEntry);
                        break;
                    }
                    K key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        ValueReference<K, V> valueReference = next.getValueReference();
                        V v2 = valueReference.get();
                        if (loadingValueReference != valueReference && (v2 != null || valueReference == LocalCache.UNSET)) {
                            enqueueNotification(k, i, v, 0, RemovalCause.REPLACED);
                            unlock();
                            postWriteCleanup();
                            return false;
                        }
                        this.modCount++;
                        if (loadingValueReference.isActive()) {
                            enqueueNotification(k, i, v2, loadingValueReference.getWeight(), v2 == null ? RemovalCause.COLLECTED : RemovalCause.REPLACED);
                            i2--;
                        }
                        ReferenceEntry<K, V> referenceEntry2 = next;
                        setValue(referenceEntry2, k, v, j);
                        this.count = i2;
                        evictEntries(referenceEntry2);
                    } else {
                        next = next.getNext();
                    }
                }
                unlock();
                postWriteCleanup();
                return true;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
                throw th;
            }
        }

        public void tryDrainReferenceQueues() {
            if (tryLock()) {
                try {
                    drainReferenceQueues();
                } finally {
                    unlock();
                }
            }
        }

        public void tryExpireEntries(long j) {
            if (tryLock()) {
                try {
                    expireEntries(j);
                } finally {
                    unlock();
                }
            }
        }

        public V waitForLoadingValue(ReferenceEntry<K, V> referenceEntry, K k, ValueReference<K, V> valueReference) throws ExecutionException {
            if (!valueReference.isLoading()) {
                throw new AssertionError();
            }
            Preconditions.checkState(!Thread.holdsLock(referenceEntry), "Recursive load of: %s", k);
            try {
                V vWaitForValue = valueReference.waitForValue();
                if (vWaitForValue != null) {
                    recordRead(referenceEntry, this.map.ticker.read());
                    return vWaitForValue;
                }
                throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + k + ".");
            } finally {
                this.statsCounter.recordMisses(1);
            }
        }

        public V get(Object obj, int i) {
            try {
                if (this.count != 0) {
                    long j = this.map.ticker.read();
                    ReferenceEntry<K, V> liveEntry = getLiveEntry(obj, i, j);
                    if (liveEntry == null) {
                        postReadCleanup();
                        return null;
                    }
                    V v = liveEntry.getValueReference().get();
                    if (v != null) {
                        recordRead(liveEntry, j);
                        V vScheduleRefresh = scheduleRefresh(liveEntry, liveEntry.getKey(), i, v, j, this.map.defaultLoader);
                        postReadCleanup();
                        return vScheduleRefresh;
                    }
                    tryDrainReferenceQueues();
                }
                postReadCleanup();
                return null;
            } catch (Throwable th) {
                postReadCleanup();
                throw th;
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:11:0x0038, code lost:
        
            r10 = r6.getValueReference();
            r9 = r10.get();
         */
        /* JADX WARN: Code restructure failed: missing block: B:12:0x0048, code lost:
        
            if (r12.map.valueEquivalence.equivalent(r15, r9) == false) goto L17;
         */
        /* JADX WARN: Code restructure failed: missing block: B:13:0x004a, code lost:
        
            r13 = com.google.common.cache.RemovalCause.EXPLICIT;
         */
        /* JADX WARN: Code restructure failed: missing block: B:14:0x004c, code lost:
        
            r11 = r13;
         */
        /* JADX WARN: Code restructure failed: missing block: B:15:0x004e, code lost:
        
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x004f, code lost:
        
            r13 = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x0052, code lost:
        
            if (r9 != null) goto L38;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x0058, code lost:
        
            if (r10.isActive() == false) goto L38;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x005a, code lost:
        
            r13 = com.google.common.cache.RemovalCause.COLLECTED;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x005d, code lost:
        
            r12.modCount++;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0064, code lost:
        
            r13 = removeValueFromChain(r5, r6, r7, r14, r9, r10, r11);
            r14 = r12.count - 1;
            r0.set(r1, r13);
            r12.count = r14;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0072, code lost:
        
            if (r11 != com.google.common.cache.RemovalCause.EXPLICIT) goto L26;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x0075, code lost:
        
            r2 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x0076, code lost:
        
            unlock();
            postWriteCleanup();
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x007c, code lost:
        
            return r2;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean remove(Object obj, int i, Object obj2) throws Throwable {
            ReferenceEntry<K, V> next;
            lock();
            try {
                preWriteCleanup(this.map.ticker.read());
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                boolean z = true;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get(length);
                next = referenceEntry;
            } catch (Throwable th) {
                th = th;
            }
            while (true) {
                if (next == null) {
                    break;
                }
                K key = next.getKey();
                if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(obj, key)) {
                    break;
                }
                int i2 = i;
                try {
                    next = next.getNext();
                    i = i2;
                } catch (Throwable th2) {
                    th = th2;
                }
                th = th2;
                Throwable th3 = th;
                unlock();
                postWriteCleanup();
                throw th3;
            }
            unlock();
            postWriteCleanup();
            return false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:20:0x006c, code lost:
        
            return null;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public V replace(K k, int i, V v) {
            lock();
            try {
                long j = this.map.ticker.read();
                preWriteCleanup(j);
                AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray = this.table;
                int length = i & (atomicReferenceArray.length() - 1);
                ReferenceEntry<K, V> referenceEntry = atomicReferenceArray.get(length);
                ReferenceEntry<K, V> next = referenceEntry;
                while (true) {
                    if (next == null) {
                        break;
                    }
                    K key = next.getKey();
                    if (next.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        long j2 = j;
                        ValueReference<K, V> valueReference = next.getValueReference();
                        V v2 = valueReference.get();
                        if (v2 == null) {
                            if (valueReference.isActive()) {
                                this.modCount++;
                                ReferenceEntry<K, V> referenceEntryRemoveValueFromChain = removeValueFromChain(referenceEntry, next, key, i, v2, valueReference, RemovalCause.COLLECTED);
                                int i2 = this.count - 1;
                                atomicReferenceArray.set(length, referenceEntryRemoveValueFromChain);
                                this.count = i2;
                            }
                        } else {
                            ReferenceEntry<K, V> referenceEntry2 = next;
                            this.modCount++;
                            enqueueNotification(k, i, v2, valueReference.getWeight(), RemovalCause.REPLACED);
                            setValue(referenceEntry2, k, v, j2);
                            evictEntries(referenceEntry2);
                            unlock();
                            postWriteCleanup();
                            return v2;
                        }
                    } else {
                        ReferenceEntry<K, V> referenceEntry3 = referenceEntry;
                        long j3 = j;
                        next = next.getNext();
                        referenceEntry = referenceEntry3;
                        j = j3;
                    }
                }
            } finally {
                unlock();
                postWriteCleanup();
            }
        }
    }
}
