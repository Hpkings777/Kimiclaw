package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.MultimapBuilder;
import defpackage.C1093Un;
import defpackage.C1145Vn;
import defpackage.C1197Wn;
import defpackage.C1249Xn;
import defpackage.C4443vg;
import defpackage.C4583wg;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
@IgnoreJRERequirement
final class CollectCollectors {
    private static final Collector<Object, ?, ImmutableList<Object>> TO_IMMUTABLE_LIST = Collector.of(new C1093Un(1), new C4443vg(1), new C4583wg(3), new C1145Vn(2), new Collector.Characteristics[0]);
    private static final Collector<Object, ?, ImmutableSet<Object>> TO_IMMUTABLE_SET = Collector.of(new C1093Un(2), new C4443vg(2), new C4583wg(4), new C1145Vn(3), new Collector.Characteristics[0]);

    @GwtIncompatible
    private static final Collector<Range<Comparable<?>>, ?, ImmutableRangeSet<Comparable<?>>> TO_IMMUTABLE_RANGE_SET = Collector.of(new C1093Un(3), new C4443vg(3), new C4583wg(2), new C1145Vn(1), new Collector.Characteristics[0]);

    @IgnoreJRERequirement
    public static final class EnumMapAccumulator<K extends Enum<K>, V> {
        private EnumMap<K, V> map = null;
        private final BinaryOperator<V> mergeFunction;

        public EnumMapAccumulator(BinaryOperator<V> binaryOperator) {
            this.mergeFunction = binaryOperator;
        }

        public EnumMapAccumulator<K, V> combine(EnumMapAccumulator<K, V> enumMapAccumulator) {
            if (this.map == null) {
                return enumMapAccumulator;
            }
            EnumMap<K, V> enumMap = enumMapAccumulator.map;
            if (enumMap == null) {
                return this;
            }
            enumMap.forEach(new BiConsumer() { // from class: com.google.common.collect.i
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    this.a.put((Enum) obj, obj2);
                }
            });
            return this;
        }

        public void put(K k, V v) {
            EnumMap<K, V> enumMap = this.map;
            if (enumMap == null) {
                this.map = new EnumMap<>(Collections.singletonMap(k, v));
            } else {
                enumMap.merge(k, v, this.mergeFunction);
            }
        }

        public ImmutableMap<K, V> toImmutableMap() {
            EnumMap<K, V> enumMap = this.map;
            return enumMap == null ? ImmutableMap.of() : ImmutableEnumMap.asImmutable(enumMap);
        }
    }

    @IgnoreJRERequirement
    public static final class EnumSetAccumulator<E extends Enum<E>> {
        static final Collector<Enum<?>, ?, ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET = CollectCollectors.toImmutableEnumSetGeneric();
        private EnumSet<E> set;

        private EnumSetAccumulator() {
        }

        public void add(E e) {
            EnumSet<E> enumSet = this.set;
            if (enumSet == null) {
                this.set = EnumSet.of((Enum) e);
            } else {
                enumSet.add(e);
            }
        }

        public EnumSetAccumulator<E> combine(EnumSetAccumulator<E> enumSetAccumulator) {
            EnumSet<E> enumSet = this.set;
            if (enumSet == null) {
                return enumSetAccumulator;
            }
            EnumSet<E> enumSet2 = enumSetAccumulator.set;
            if (enumSet2 == null) {
                return this;
            }
            enumSet.addAll(enumSet2);
            return this;
        }

        public ImmutableSet<E> toImmutableSet() {
            EnumSet<E> enumSet = this.set;
            if (enumSet == null) {
                return ImmutableSet.of();
            }
            ImmutableSet<E> immutableSetAsImmutable = ImmutableEnumSet.asImmutable(enumSet);
            this.set = null;
            return immutableSetAsImmutable;
        }
    }

    private CollectCollectors() {
    }

    public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends Stream<? extends V>> function2) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        f fVar = new f(function, 0);
        f fVar2 = new f(function2, 1);
        MultimapBuilder.ListMultimapBuilder<Object, Object> listMultimapBuilderArrayListValues = MultimapBuilder.linkedHashKeys().arrayListValues();
        Objects.requireNonNull(listMultimapBuilderArrayListValues);
        return Collectors.collectingAndThen(flatteningToMultimap(fVar, fVar2, new C1197Wn(listMultimapBuilderArrayListValues, 0)), new C1145Vn(4));
    }

    public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends Stream<? extends V>> function2) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        f fVar = new f(function, 2);
        f fVar2 = new f(function2, 3);
        MultimapBuilder.SetMultimapBuilder<Object, Object> setMultimapBuilderLinkedHashSetValues = MultimapBuilder.linkedHashKeys().linkedHashSetValues();
        Objects.requireNonNull(setMultimapBuilderLinkedHashSetValues);
        return Collectors.collectingAndThen(flatteningToMultimap(fVar, fVar2, new C1197Wn(setMultimapBuilderLinkedHashSetValues, 1)), new C1145Vn(6));
    }

    public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends Stream<? extends V>> function2, Supplier<M> supplier) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        Preconditions.checkNotNull(supplier);
        return Collector.of(supplier, new C1804a(function, function2, 2), new d(1), new Collector.Characteristics[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object lambda$flatteningToImmutableListMultimap$0(Function function, Object obj) {
        return Preconditions.checkNotNull(function.apply(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Stream lambda$flatteningToImmutableListMultimap$1(Function function, Object obj) {
        return ((Stream) function.apply(obj)).peek(new C1249Xn());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object lambda$flatteningToImmutableSetMultimap$0(Function function, Object obj) {
        return Preconditions.checkNotNull(function.apply(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Stream lambda$flatteningToImmutableSetMultimap$1(Function function, Object obj) {
        return ((Stream) function.apply(obj)).peek(new C1249Xn());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$flatteningToMultimap$0(Function function, Function function2, Multimap multimap, Object obj) {
        final Collection collection = multimap.get(function.apply(obj));
        Stream stream = (Stream) function2.apply(obj);
        Objects.requireNonNull(collection);
        stream.forEachOrdered(new Consumer() { // from class: Yn
            @Override // java.util.function.Consumer
            public final void accept(Object obj2) {
                collection.add(obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Multimap lambda$flatteningToMultimap$1(Multimap multimap, Multimap multimap2) {
        multimap.putAll(multimap2);
        return multimap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toImmutableBiMap$0(Function function, Function function2, ImmutableBiMap.Builder builder, Object obj) {
        builder.put(function.apply(obj), function2.apply(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ EnumMapAccumulator lambda$toImmutableEnumMap$0() {
        return new EnumMapAccumulator(new d(4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object lambda$toImmutableEnumMap$1(Object obj, Object obj2) {
        throw new IllegalArgumentException("Multiple values for key: " + obj + ", " + obj2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toImmutableEnumMap$2(Function function, Function function2, EnumMapAccumulator enumMapAccumulator, Object obj) {
        enumMapAccumulator.put((Enum) Preconditions.checkNotNull((Enum) function.apply(obj), "Null key for input %s", obj), Preconditions.checkNotNull(function2.apply(obj), "Null value for input %s", obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ EnumMapAccumulator lambda$toImmutableEnumMap$3(BinaryOperator binaryOperator) {
        return new EnumMapAccumulator(binaryOperator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toImmutableEnumMap$4(Function function, Function function2, EnumMapAccumulator enumMapAccumulator, Object obj) {
        enumMapAccumulator.put((Enum) Preconditions.checkNotNull((Enum) function.apply(obj), "Null key for input %s", obj), Preconditions.checkNotNull(function2.apply(obj), "Null value for input %s", obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ EnumSetAccumulator lambda$toImmutableEnumSetGeneric$0() {
        return new EnumSetAccumulator();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toImmutableListMultimap$0(Function function, Function function2, ImmutableListMultimap.Builder builder, Object obj) {
        builder.put(function.apply(obj), function2.apply(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toImmutableMap$0(Function function, Function function2, ImmutableMap.Builder builder, Object obj) {
        builder.put(function.apply(obj), function2.apply(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toImmutableMultiset$0(Function function, ToIntFunction toIntFunction, Multiset multiset, Object obj) {
        multiset.add(Preconditions.checkNotNull(function.apply(obj)), toIntFunction.applyAsInt(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Multiset lambda$toImmutableMultiset$1(Multiset multiset, Multiset multiset2) {
        multiset.addAll(multiset2);
        return multiset;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ImmutableMultiset lambda$toImmutableMultiset$2(Multiset multiset) {
        return ImmutableMultiset.copyFromEntries(multiset.entrySet());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toImmutableRangeMap$0(Function function, Function function2, ImmutableRangeMap.Builder builder, Object obj) {
        builder.put((Range) function.apply(obj), function2.apply(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toImmutableSetMultimap$0(Function function, Function function2, ImmutableSetMultimap.Builder builder, Object obj) {
        builder.put(function.apply(obj), function2.apply(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ImmutableSortedMap.Builder lambda$toImmutableSortedMap$0(Comparator comparator) {
        return new ImmutableSortedMap.Builder(comparator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toImmutableSortedMap$1(Function function, Function function2, ImmutableSortedMap.Builder builder, Object obj) {
        builder.put(function.apply(obj), function2.apply(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ TreeMap lambda$toImmutableSortedMap$2(Comparator comparator) {
        return new TreeMap(comparator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ImmutableSortedSet.Builder lambda$toImmutableSortedSet$0(Comparator comparator) {
        return new ImmutableSortedSet.Builder(comparator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toMultimap$0(Function function, Function function2, Multimap multimap, Object obj) {
        multimap.put(function.apply(obj), function2.apply(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Multimap lambda$toMultimap$1(Multimap multimap, Multimap multimap2) {
        multimap.putAll(multimap2);
        return multimap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toMultiset$0(Function function, ToIntFunction toIntFunction, Multiset multiset, Object obj) {
        multiset.add(function.apply(obj), toIntFunction.applyAsInt(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Multiset lambda$toMultiset$1(Multiset multiset, Multiset multiset2) {
        multiset.addAll(multiset2);
        return multiset;
    }

    public static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        return Collector.of(new C1093Un(9), new C1804a(function, function2, 0), new C4583wg(1), new C1145Vn(0), new Collector.Characteristics[0]);
    }

    public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        return Collector.of(new b(1), new C1804a(function, function2, 3), new d(3), new e(2), Collector.Characteristics.UNORDERED);
    }

    public static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
        return (Collector<E, ?, ImmutableSet<E>>) EnumSetAccumulator.TO_IMMUTABLE_ENUM_SET;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E extends Enum<E>> Collector<E, EnumSetAccumulator<E>, ImmutableSet<E>> toImmutableEnumSetGeneric() {
        return Collector.of(new b(0), new c(0), new d(0), new e(0), Collector.Characteristics.UNORDERED);
    }

    public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return (Collector<E, ?, ImmutableList<E>>) TO_IMMUTABLE_LIST;
    }

    public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        Preconditions.checkNotNull(function, "keyFunction");
        Preconditions.checkNotNull(function2, "valueFunction");
        return Collector.of(new C1093Un(6), new C1804a(function, function2, 5), new C4583wg(8), new C1145Vn(10), new Collector.Characteristics[0]);
    }

    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        return Collector.of(new C1093Un(5), new C1804a(function, function2, 4), new C4583wg(6), new C1145Vn(8), new Collector.Characteristics[0]);
    }

    public static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> function, ToIntFunction<? super T> toIntFunction) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(toIntFunction);
        return Collector.of(new C1093Un(4), new g(function, toIntFunction, 0), new d(2), new e(1), new Collector.Characteristics[0]);
    }

    @GwtIncompatible
    public static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> function, Function<? super T, ? extends V> function2) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        return Collector.of(new C1093Un(0), new C1804a(function, function2, 1), new C4583wg(5), new C1145Vn(5), new Collector.Characteristics[0]);
    }

    @GwtIncompatible
    public static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
        return (Collector<Range<E>, ?, ImmutableRangeSet<E>>) TO_IMMUTABLE_RANGE_SET;
    }

    public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        return (Collector<E, ?, ImmutableSet<E>>) TO_IMMUTABLE_SET;
    }

    public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        Preconditions.checkNotNull(function, "keyFunction");
        Preconditions.checkNotNull(function2, "valueFunction");
        return Collector.of(new C1093Un(7), new C1804a(function, function2, 7), new C4583wg(9), new C1145Vn(11), new Collector.Characteristics[0]);
    }

    public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        return Collector.of(new h(comparator, 1), new C1804a(function, function2, 6), new C4583wg(10), new C1145Vn(13), Collector.Characteristics.UNORDERED);
    }

    public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
        Preconditions.checkNotNull(comparator);
        return Collector.of(new h(comparator, 2), new C4443vg(4), new C4583wg(7), new C1145Vn(9), new Collector.Characteristics[0]);
    }

    public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, Supplier<M> supplier) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        Preconditions.checkNotNull(supplier);
        return Collector.of(supplier, new C1804a(function, function2, 9), new d(6), new Collector.Characteristics[0]);
    }

    public static <T, E, M extends Multiset<E>> Collector<T, ?, M> toMultiset(Function<? super T, E> function, ToIntFunction<? super T> toIntFunction, Supplier<M> supplier) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(toIntFunction);
        Preconditions.checkNotNull(supplier);
        return Collector.of(supplier, new g(function, toIntFunction, 1), new d(5), new Collector.Characteristics[0]);
    }

    public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, BinaryOperator<V> binaryOperator) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        Preconditions.checkNotNull(binaryOperator);
        return Collector.of(new h(binaryOperator, 3), new C1804a(function, function2, 8), new d(3), new e(2), new Collector.Characteristics[0]);
    }

    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, BinaryOperator<V> binaryOperator) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        Preconditions.checkNotNull(binaryOperator);
        return Collectors.collectingAndThen(Collectors.toMap(function, function2, binaryOperator, new C1093Un(8)), new C1145Vn(12));
    }

    public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, BinaryOperator<V> binaryOperator) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function2);
        Preconditions.checkNotNull(binaryOperator);
        return Collectors.collectingAndThen(Collectors.toMap(function, function2, binaryOperator, new h(comparator, 0)), new C1145Vn(7));
    }
}
