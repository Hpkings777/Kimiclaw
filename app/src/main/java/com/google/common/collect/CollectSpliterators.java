package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import defpackage.A20;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import kotlin.jvm.internal.LongCompanionObject;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
@IgnoreJRERequirement
final class CollectSpliterators {

    /* JADX INFO: Add missing generic type declarations: [OutElementT] */
    /* JADX INFO: renamed from: com.google.common.collect.CollectSpliterators$1, reason: invalid class name */
    public class AnonymousClass1<OutElementT> implements Spliterator<OutElementT> {
        final /* synthetic */ Spliterator val$fromSpliterator;
        final /* synthetic */ Function val$function;

        public AnonymousClass1(Spliterator spliterator, Function function) {
            this.val$fromSpliterator = spliterator;
            this.val$function = function;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$forEachRemaining$0(Consumer consumer, Function function, Object obj) {
            consumer.accept(function.apply(obj));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$tryAdvance$0(Consumer consumer, Function function, Object obj) {
            consumer.accept(function.apply(obj));
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.val$fromSpliterator.characteristics() & (-262);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.val$fromSpliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super OutElementT> consumer) {
            this.val$fromSpliterator.forEachRemaining(new j(consumer, this.val$function, 0));
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super OutElementT> consumer) {
            return this.val$fromSpliterator.tryAdvance(new j(consumer, this.val$function, 1));
        }

        @Override // java.util.Spliterator
        public Spliterator<OutElementT> trySplit() {
            Spliterator spliteratorTrySplit = this.val$fromSpliterator.trySplit();
            if (spliteratorTrySplit != null) {
                return CollectSpliterators.map(spliteratorTrySplit, this.val$function);
            }
            return null;
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: com.google.common.collect.CollectSpliterators$1Splitr, reason: invalid class name */
    @IgnoreJRERequirement
    public final class C1Splitr<T> implements Spliterator<T>, Consumer<T> {
        T holder = null;
        final /* synthetic */ Spliterator val$fromSpliterator;
        final /* synthetic */ Predicate val$predicate;

        public C1Splitr(Spliterator spliterator, Predicate predicate) {
            this.val$fromSpliterator = spliterator;
            this.val$predicate = predicate;
        }

        @Override // java.util.function.Consumer
        public void accept(@ParametricNullness T t) {
            this.holder = t;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.val$fromSpliterator.characteristics() & 277;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.val$fromSpliterator.estimateSize() / 2;
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            return this.val$fromSpliterator.getComparator();
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            while (this.val$fromSpliterator.tryAdvance(this)) {
                try {
                    A20 a20 = (Object) NullnessCasts.uncheckedCastNullableTToT(this.holder);
                    if (this.val$predicate.test(a20)) {
                        consumer.accept(a20);
                        this.holder = null;
                        return true;
                    }
                } finally {
                    this.holder = null;
                }
            }
            return false;
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            Spliterator<T> spliteratorTrySplit = this.val$fromSpliterator.trySplit();
            if (spliteratorTrySplit == null) {
                return null;
            }
            return CollectSpliterators.filter(spliteratorTrySplit, this.val$predicate);
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: com.google.common.collect.CollectSpliterators$1WithCharacteristics, reason: invalid class name */
    @IgnoreJRERequirement
    public final class C1WithCharacteristics<T> implements Spliterator<T> {
        private final Spliterator.OfInt delegate;
        final /* synthetic */ Comparator val$comparator;
        final /* synthetic */ int val$extraCharacteristics;
        final /* synthetic */ IntFunction val$function;

        public C1WithCharacteristics(Spliterator.OfInt ofInt, IntFunction intFunction, int i, Comparator comparator) {
            this.val$function = intFunction;
            this.val$extraCharacteristics = i;
            this.val$comparator = comparator;
            this.delegate = ofInt;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ void lambda$forEachRemaining$0(Consumer consumer, IntFunction intFunction, int i) {
            consumer.accept(intFunction.apply(i));
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ void lambda$tryAdvance$0(Consumer consumer, IntFunction intFunction, int i) {
            consumer.accept(intFunction.apply(i));
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.val$extraCharacteristics | 16464;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.delegate.estimateSize();
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super T> consumer) {
            this.delegate.forEachRemaining((IntConsumer) new k(consumer, this.val$function, 0));
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            if (hasCharacteristics(4)) {
                return this.val$comparator;
            }
            throw new IllegalStateException();
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            return this.delegate.tryAdvance((IntConsumer) new k(consumer, this.val$function, 1));
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            Spliterator.OfInt ofIntTrySplit = this.delegate.trySplit();
            if (ofIntTrySplit == null) {
                return null;
            }
            return new C1WithCharacteristics(ofIntTrySplit, this.val$function, this.val$extraCharacteristics, this.val$comparator);
        }
    }

    @IgnoreJRERequirement
    public static abstract class FlatMapSpliterator<InElementT, OutElementT, OutSpliteratorT extends Spliterator<OutElementT>> implements Spliterator<OutElementT> {
        int characteristics;
        long estimatedSize;
        final Factory<InElementT, OutSpliteratorT> factory;
        final Spliterator<InElementT> from;
        final Function<? super InElementT, OutSpliteratorT> function;
        OutSpliteratorT prefix;

        @IgnoreJRERequirement
        public interface Factory<InElementT, OutSpliteratorT extends Spliterator<?>> {
            OutSpliteratorT newFlatMapSpliterator(OutSpliteratorT outspliteratort, Spliterator<InElementT> spliterator, Function<? super InElementT, OutSpliteratorT> function, int i, long j);
        }

        public FlatMapSpliterator(OutSpliteratorT outspliteratort, Spliterator<InElementT> spliterator, Function<? super InElementT, OutSpliteratorT> function, Factory<InElementT, OutSpliteratorT> factory, int i, long j) {
            this.prefix = outspliteratort;
            this.from = spliterator;
            this.function = function;
            this.factory = factory;
            this.characteristics = i;
            this.estimatedSize = j;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$forEachRemaining$0(Consumer consumer, Object obj) {
            OutSpliteratorT outspliteratortApply = this.function.apply(obj);
            if (outspliteratortApply != null) {
                outspliteratortApply.forEachRemaining(consumer);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$tryAdvance$0(Object obj) {
            this.prefix = this.function.apply(obj);
        }

        @Override // java.util.Spliterator
        public final int characteristics() {
            return this.characteristics;
        }

        @Override // java.util.Spliterator
        public final long estimateSize() {
            OutSpliteratorT outspliteratort = this.prefix;
            if (outspliteratort != null) {
                this.estimatedSize = Math.max(this.estimatedSize, outspliteratort.estimateSize());
            }
            return Math.max(this.estimatedSize, 0L);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super OutElementT> consumer) {
            OutSpliteratorT outspliteratort = this.prefix;
            if (outspliteratort != null) {
                outspliteratort.forEachRemaining(consumer);
                this.prefix = null;
            }
            this.from.forEachRemaining(new l(this, consumer, 0));
            this.estimatedSize = 0L;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super OutElementT> consumer) {
            do {
                OutSpliteratorT outspliteratort = this.prefix;
                if (outspliteratort != null && outspliteratort.tryAdvance(consumer)) {
                    long j = this.estimatedSize;
                    if (j == LongCompanionObject.MAX_VALUE) {
                        return true;
                    }
                    this.estimatedSize = j - 1;
                    return true;
                }
                this.prefix = null;
            } while (this.from.tryAdvance(new m(this, 0)));
            return false;
        }

        @Override // java.util.Spliterator
        public final OutSpliteratorT trySplit() {
            Spliterator<InElementT> spliteratorTrySplit = this.from.trySplit();
            if (spliteratorTrySplit == null) {
                OutSpliteratorT outspliteratort = this.prefix;
                if (outspliteratort == null) {
                    return null;
                }
                this.prefix = null;
                return outspliteratort;
            }
            int i = this.characteristics & (-65);
            long jEstimateSize = estimateSize();
            if (jEstimateSize < LongCompanionObject.MAX_VALUE) {
                jEstimateSize /= 2;
                this.estimatedSize -= jEstimateSize;
                this.characteristics = i;
            }
            OutSpliteratorT outspliteratort2 = (OutSpliteratorT) this.factory.newFlatMapSpliterator(this.prefix, spliteratorTrySplit, this.function, i, jEstimateSize);
            this.prefix = null;
            return outspliteratort2;
        }
    }

    @IgnoreJRERequirement
    public static final class FlatMapSpliteratorOfDouble<InElementT> extends FlatMapSpliteratorOfPrimitive<InElementT, Double, DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble {
        public FlatMapSpliteratorOfDouble(Spliterator.OfDouble ofDouble, Spliterator<InElementT> spliterator, Function<? super InElementT, Spliterator.OfDouble> function, int i, long j) {
            super(ofDouble, spliterator, function, new n(0), i, j);
        }

        @Override // java.util.Spliterator.OfDouble
        public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
            forEachRemaining(doubleConsumer);
        }

        @Override // java.util.Spliterator.OfDouble
        public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
            return tryAdvance(doubleConsumer);
        }

        @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliteratorOfPrimitive, com.google.common.collect.CollectSpliterators.FlatMapSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
            return (Spliterator.OfDouble) trySplit();
        }
    }

    @IgnoreJRERequirement
    public static final class FlatMapSpliteratorOfInt<InElementT> extends FlatMapSpliteratorOfPrimitive<InElementT, Integer, IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt {
        public FlatMapSpliteratorOfInt(Spliterator.OfInt ofInt, Spliterator<InElementT> spliterator, Function<? super InElementT, Spliterator.OfInt> function, int i, long j) {
            super(ofInt, spliterator, function, new n(1), i, j);
        }

        @Override // java.util.Spliterator.OfInt
        public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
            forEachRemaining(intConsumer);
        }

        @Override // java.util.Spliterator.OfInt
        public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
            return tryAdvance(intConsumer);
        }

        @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliteratorOfPrimitive, com.google.common.collect.CollectSpliterators.FlatMapSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
            return (Spliterator.OfInt) trySplit();
        }
    }

    @IgnoreJRERequirement
    public static final class FlatMapSpliteratorOfLong<InElementT> extends FlatMapSpliteratorOfPrimitive<InElementT, Long, LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong {
        public FlatMapSpliteratorOfLong(Spliterator.OfLong ofLong, Spliterator<InElementT> spliterator, Function<? super InElementT, Spliterator.OfLong> function, int i, long j) {
            super(ofLong, spliterator, function, new n(2), i, j);
        }

        @Override // java.util.Spliterator.OfLong
        public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
            forEachRemaining(longConsumer);
        }

        @Override // java.util.Spliterator.OfLong
        public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
            return tryAdvance(longConsumer);
        }

        @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliteratorOfPrimitive, com.google.common.collect.CollectSpliterators.FlatMapSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
            return (Spliterator.OfLong) trySplit();
        }
    }

    @IgnoreJRERequirement
    public static final class FlatMapSpliteratorOfObject<InElementT, OutElementT> extends FlatMapSpliterator<InElementT, OutElementT, Spliterator<OutElementT>> {
        public FlatMapSpliteratorOfObject(Spliterator<OutElementT> spliterator, Spliterator<InElementT> spliterator2, Function<? super InElementT, Spliterator<OutElementT>> function, int i, long j) {
            super(spliterator, spliterator2, function, new n(3), i, j);
        }
    }

    @IgnoreJRERequirement
    public static abstract class FlatMapSpliteratorOfPrimitive<InElementT, OutElementT, OutConsumerT, OutSpliteratorT extends Spliterator.OfPrimitive<OutElementT, OutConsumerT, OutSpliteratorT>> extends FlatMapSpliterator<InElementT, OutElementT, OutSpliteratorT> implements Spliterator.OfPrimitive<OutElementT, OutConsumerT, OutSpliteratorT> {
        public FlatMapSpliteratorOfPrimitive(OutSpliteratorT outspliteratort, Spliterator<InElementT> spliterator, Function<? super InElementT, OutSpliteratorT> function, FlatMapSpliterator.Factory<InElementT, OutSpliteratorT> factory, int i, long j) {
            super(outspliteratort, spliterator, function, factory, i, j);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Type inference incomplete: some casts might be missing */
        public /* synthetic */ void lambda$forEachRemaining$0(Object obj, Object obj2) {
            Spliterator.OfPrimitive ofPrimitive = (Spliterator.OfPrimitive) this.function.apply((Object) obj2);
            if (ofPrimitive != null) {
                ofPrimitive.forEachRemaining(obj);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Type inference incomplete: some casts might be missing */
        public /* synthetic */ void lambda$tryAdvance$0(Object obj) {
            this.prefix = (OutSpliteratorT) this.function.apply((Object) obj);
        }

        @Override // java.util.Spliterator.OfPrimitive
        public final void forEachRemaining(OutConsumerT outconsumert) {
            OutSpliteratorT outspliteratort = this.prefix;
            if (outspliteratort != 0) {
                ((Spliterator.OfPrimitive) outspliteratort).forEachRemaining(outconsumert);
                this.prefix = null;
            }
            this.from.forEachRemaining(new l(this, outconsumert, 1));
            this.estimatedSize = 0L;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public final boolean tryAdvance(OutConsumerT outconsumert) {
            do {
                OutSpliteratorT outspliteratort = this.prefix;
                if (outspliteratort != 0 && ((Spliterator.OfPrimitive) outspliteratort).tryAdvance(outconsumert)) {
                    long j = this.estimatedSize;
                    if (j == LongCompanionObject.MAX_VALUE) {
                        return true;
                    }
                    this.estimatedSize = j - 1;
                    return true;
                }
                this.prefix = null;
            } while (this.from.tryAdvance(new m(this, 1)));
            return false;
        }

        @Override // com.google.common.collect.CollectSpliterators.FlatMapSpliterator, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
            return trySplit();
        }
    }

    private CollectSpliterators() {
    }

    public static <T> Spliterator<T> filter(Spliterator<T> spliterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(spliterator);
        Preconditions.checkNotNull(predicate);
        return new C1Splitr(spliterator, predicate);
    }

    public static <InElementT, OutElementT> Spliterator<OutElementT> flatMap(Spliterator<InElementT> spliterator, Function<? super InElementT, Spliterator<OutElementT>> function, int i, long j) {
        Preconditions.checkArgument((i & 16384) == 0, "flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((i & 4) == 0, "flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(spliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfObject(null, spliterator, function, i, j);
    }

    public static <InElementT> Spliterator.OfDouble flatMapToDouble(Spliterator<InElementT> spliterator, Function<? super InElementT, Spliterator.OfDouble> function, int i, long j) {
        Preconditions.checkArgument((i & 16384) == 0, "flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((i & 4) == 0, "flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(spliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfDouble(null, spliterator, function, i, j);
    }

    public static <InElementT> Spliterator.OfInt flatMapToInt(Spliterator<InElementT> spliterator, Function<? super InElementT, Spliterator.OfInt> function, int i, long j) {
        Preconditions.checkArgument((i & 16384) == 0, "flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((i & 4) == 0, "flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(spliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfInt(null, spliterator, function, i, j);
    }

    public static <InElementT> Spliterator.OfLong flatMapToLong(Spliterator<InElementT> spliterator, Function<? super InElementT, Spliterator.OfLong> function, int i, long j) {
        Preconditions.checkArgument((i & 16384) == 0, "flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((i & 4) == 0, "flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(spliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfLong(null, spliterator, function, i, j);
    }

    public static <T> Spliterator<T> indexed(int i, int i2, IntFunction<T> intFunction) {
        return indexed(i, i2, intFunction, null);
    }

    public static <InElementT, OutElementT> Spliterator<OutElementT> map(Spliterator<InElementT> spliterator, Function<? super InElementT, ? extends OutElementT> function) {
        Preconditions.checkNotNull(spliterator);
        Preconditions.checkNotNull(function);
        return new AnonymousClass1(spliterator, function);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [java.util.Spliterator$OfInt] */
    public static <T> Spliterator<T> indexed(int i, int i2, IntFunction<T> intFunction, Comparator<? super T> comparator) {
        if (comparator != null) {
            Preconditions.checkArgument((i2 & 4) != 0);
        }
        return new C1WithCharacteristics(IntStream.range(0, i).spliterator(), intFunction, i2, comparator);
    }
}
