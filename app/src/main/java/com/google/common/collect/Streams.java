package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;
import defpackage.C1145Vn;
import defpackage.NN;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
@IgnoreJRERequirement
public final class Streams {

    /* JADX INFO: renamed from: com.google.common.collect.Streams$1OptionalState, reason: invalid class name */
    public final class C1OptionalState {
        boolean set = false;
        T value = null;

        public T get() {
            T t = this.value;
            Objects.requireNonNull(t);
            return t;
        }

        public void set(T t) {
            this.set = true;
            this.value = t;
        }
    }

    /* JADX INFO: Add missing generic type declarations: [R, T] */
    /* JADX INFO: renamed from: com.google.common.collect.Streams$1Splitr, reason: invalid class name */
    public final class C1Splitr<R, T> extends MapWithIndexSpliterator<Spliterator<T>, R, C1Splitr> implements Consumer<T> {
        T holder;
        final /* synthetic */ FunctionWithIndex val$function;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 4, insn: 0x0000: IPUT (r4 I:com.google.common.collect.Streams$FunctionWithIndex), (r0 I:com.google.common.collect.Streams$1Splitr) com.google.common.collect.Streams.1Splitr.val$function com.google.common.collect.Streams$FunctionWithIndex, block:B:2:0x0000 */
        public C1Splitr(Spliterator spliterator, Spliterator<T> spliterator2, long j) {
            FunctionWithIndex functionWithIndex;
            super(spliterator, spliterator2);
            this.val$function = functionWithIndex;
        }

        @Override // java.util.function.Consumer
        public void accept(@ParametricNullness T t) {
            this.holder = t;
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
        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super R> consumer) {
            if (!this.fromSpliterator.tryAdvance(this)) {
                return false;
            }
            try {
                FunctionWithIndex functionWithIndex = this.val$function;
                Object objUncheckedCastNullableTToT = NullnessCasts.uncheckedCastNullableTToT(this.holder);
                long j = this.index;
                this.index = 1 + j;
                consumer.accept((Object) functionWithIndex.apply(objUncheckedCastNullableTToT, j));
                this.holder = null;
                return true;
            } catch (Throwable th) {
                this.holder = null;
                throw th;
            }
        }

        @Override // com.google.common.collect.Streams.MapWithIndexSpliterator
        public C1Splitr createSplit(Spliterator<T> spliterator, long j) {
            return new C1Splitr(spliterator, j, this.val$function);
        }
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* JADX INFO: renamed from: com.google.common.collect.Streams$2Splitr, reason: invalid class name */
    public final class C2Splitr<R> extends MapWithIndexSpliterator<Spliterator.OfInt, R, C2Splitr> implements IntConsumer {
        int holder;
        final /* synthetic */ IntFunctionWithIndex val$function;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 4, insn: 0x0000: IPUT (r4 I:com.google.common.collect.Streams$IntFunctionWithIndex), (r0 I:com.google.common.collect.Streams$2Splitr) com.google.common.collect.Streams.2Splitr.val$function com.google.common.collect.Streams$IntFunctionWithIndex, block:B:2:0x0000 */
        public C2Splitr(Spliterator.OfInt ofInt, Spliterator.OfInt ofInt2, long j) {
            IntFunctionWithIndex intFunctionWithIndex;
            super(ofInt, ofInt2);
            this.val$function = intFunctionWithIndex;
        }

        @Override // java.util.function.IntConsumer
        public void accept(int i) {
            this.holder = i;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super R> consumer) {
            if (!((Spliterator.OfInt) this.fromSpliterator).tryAdvance((IntConsumer) this)) {
                return false;
            }
            IntFunctionWithIndex intFunctionWithIndex = this.val$function;
            int i = this.holder;
            long j = this.index;
            this.index = 1 + j;
            consumer.accept((Object) intFunctionWithIndex.apply(i, j));
            return true;
        }

        @Override // com.google.common.collect.Streams.MapWithIndexSpliterator
        public C2Splitr createSplit(Spliterator.OfInt ofInt, long j) {
            return new C2Splitr(ofInt, j, this.val$function);
        }
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* JADX INFO: renamed from: com.google.common.collect.Streams$3Splitr, reason: invalid class name */
    public final class C3Splitr<R> extends MapWithIndexSpliterator<Spliterator.OfLong, R, C3Splitr> implements LongConsumer {
        long holder;
        final /* synthetic */ LongFunctionWithIndex val$function;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 4, insn: 0x0000: IPUT (r4 I:com.google.common.collect.Streams$LongFunctionWithIndex), (r0 I:com.google.common.collect.Streams$3Splitr) com.google.common.collect.Streams.3Splitr.val$function com.google.common.collect.Streams$LongFunctionWithIndex, block:B:2:0x0000 */
        public C3Splitr(Spliterator.OfLong ofLong, Spliterator.OfLong ofLong2, long j) {
            LongFunctionWithIndex longFunctionWithIndex;
            super(ofLong, ofLong2);
            this.val$function = longFunctionWithIndex;
        }

        @Override // java.util.function.LongConsumer
        public void accept(long j) {
            this.holder = j;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super R> consumer) {
            if (!((Spliterator.OfLong) this.fromSpliterator).tryAdvance((LongConsumer) this)) {
                return false;
            }
            LongFunctionWithIndex longFunctionWithIndex = this.val$function;
            long j = this.holder;
            long j2 = this.index;
            this.index = 1 + j2;
            consumer.accept((Object) longFunctionWithIndex.apply(j, j2));
            return true;
        }

        @Override // com.google.common.collect.Streams.MapWithIndexSpliterator
        public C3Splitr createSplit(Spliterator.OfLong ofLong, long j) {
            return new C3Splitr(ofLong, j, this.val$function);
        }
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* JADX INFO: renamed from: com.google.common.collect.Streams$4Splitr, reason: invalid class name */
    public final class C4Splitr<R> extends MapWithIndexSpliterator<Spliterator.OfDouble, R, C4Splitr> implements DoubleConsumer {
        double holder;
        final /* synthetic */ DoubleFunctionWithIndex val$function;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 4, insn: 0x0000: IPUT (r4 I:com.google.common.collect.Streams$DoubleFunctionWithIndex), (r0 I:com.google.common.collect.Streams$4Splitr) com.google.common.collect.Streams.4Splitr.val$function com.google.common.collect.Streams$DoubleFunctionWithIndex, block:B:2:0x0000 */
        public C4Splitr(Spliterator.OfDouble ofDouble, Spliterator.OfDouble ofDouble2, long j) {
            DoubleFunctionWithIndex doubleFunctionWithIndex;
            super(ofDouble, ofDouble2);
            this.val$function = doubleFunctionWithIndex;
        }

        @Override // java.util.function.DoubleConsumer
        public void accept(double d) {
            this.holder = d;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super R> consumer) {
            if (!((Spliterator.OfDouble) this.fromSpliterator).tryAdvance((DoubleConsumer) this)) {
                return false;
            }
            DoubleFunctionWithIndex doubleFunctionWithIndex = this.val$function;
            double d = this.holder;
            long j = this.index;
            this.index = 1 + j;
            consumer.accept((Object) doubleFunctionWithIndex.apply(d, j));
            return true;
        }

        @Override // com.google.common.collect.Streams.MapWithIndexSpliterator
        public C4Splitr createSplit(Spliterator.OfDouble ofDouble, long j) {
            return new C4Splitr(ofDouble, j, this.val$function);
        }
    }

    public interface DoubleFunctionWithIndex<R> {
        @ParametricNullness
        R apply(double d, long j);
    }

    public interface FunctionWithIndex<T, R> {
        @ParametricNullness
        R apply(@ParametricNullness T t, long j);
    }

    public interface IntFunctionWithIndex<R> {
        @ParametricNullness
        R apply(int i, long j);
    }

    public interface LongFunctionWithIndex<R> {
        @ParametricNullness
        R apply(long j, long j2);
    }

    @IgnoreJRERequirement
    public static abstract class MapWithIndexSpliterator<F extends Spliterator<?>, R, S extends MapWithIndexSpliterator<F, R, S>> implements Spliterator<R> {
        final F fromSpliterator;
        long index;

        public MapWithIndexSpliterator(F f, long j) {
            this.fromSpliterator = f;
            this.index = j;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.fromSpliterator.characteristics() & 16464;
        }

        public abstract S createSplit(F f, long j);

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.fromSpliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public S trySplit() {
            Spliterator spliteratorTrySplit = this.fromSpliterator.trySplit();
            if (spliteratorTrySplit == null) {
                return null;
            }
            S s = (S) createSplit(spliteratorTrySplit, this.index);
            this.index = spliteratorTrySplit.getExactSizeIfKnown() + this.index;
            return s;
        }
    }

    public static final class TemporaryPair<A, B> {

        @ParametricNullness
        final A a;

        @ParametricNullness
        final B b;

        public TemporaryPair(@ParametricNullness A a, @ParametricNullness B b) {
            this.a = a;
            this.b = b;
        }
    }

    private Streams() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void closeAll(BaseStream<?, ?>[] baseStreamArr) {
        Exception exc = null;
        for (BaseStream<?, ?> baseStream : baseStreamArr) {
            try {
                baseStream.close();
            } catch (Exception e) {
                if (exc == null) {
                    exc = e;
                } else {
                    exc.addSuppressed(e);
                }
            }
        }
        if (exc != null) {
            SneakyThrows.sneakyThrow(exc);
        }
    }

    @SafeVarargs
    public static <T> Stream<T> concat(Stream<? extends T>... streamArr) {
        ImmutableList.Builder builder = new ImmutableList.Builder(streamArr.length);
        long jSaturatedAdd = 0;
        int iCharacteristics = 336;
        boolean zIsParallel = false;
        for (Stream<? extends T> stream : streamArr) {
            zIsParallel |= stream.isParallel();
            Spliterator<? extends T> spliterator = stream.spliterator();
            builder.add(spliterator);
            iCharacteristics &= spliterator.characteristics();
            jSaturatedAdd = LongMath.saturatedAdd(jSaturatedAdd, spliterator.estimateSize());
        }
        return (Stream) StreamSupport.stream(CollectSpliterators.flatMap(builder.build().spliterator(), new C1145Vn(25), iCharacteristics, jSaturatedAdd), zIsParallel).onClose(new NN(streamArr, 20));
    }

    public static <T> Optional<T> findLast(Stream<T> stream) {
        C1OptionalState c1OptionalState = new C1OptionalState();
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.addLast(stream.spliterator());
        while (!arrayDeque.isEmpty()) {
            Spliterator<T> spliterator = (Spliterator) arrayDeque.removeLast();
            if (spliterator.getExactSizeIfKnown() != 0) {
                if (spliterator.hasCharacteristics(16384)) {
                    while (true) {
                        Spliterator<T> spliteratorTrySplit = spliterator.trySplit();
                        if (spliteratorTrySplit == null || spliteratorTrySplit.getExactSizeIfKnown() == 0) {
                            break;
                        }
                        if (spliterator.getExactSizeIfKnown() == 0) {
                            spliterator = spliteratorTrySplit;
                            break;
                        }
                    }
                    spliterator.forEachRemaining(new m(c1OptionalState, 3));
                    return Optional.of(c1OptionalState.get());
                }
                Spliterator<T> spliteratorTrySplit2 = spliterator.trySplit();
                if (spliteratorTrySplit2 == null || spliteratorTrySplit2.getExactSizeIfKnown() == 0) {
                    spliterator.forEachRemaining(new m(c1OptionalState, 3));
                    if (c1OptionalState.set) {
                        return Optional.of(c1OptionalState.get());
                    }
                } else {
                    arrayDeque.addLast(spliteratorTrySplit2);
                    arrayDeque.addLast(spliterator);
                }
            }
        }
        return Optional.empty();
    }

    @Beta
    public static <A, B> void forEachPair(Stream<A> stream, Stream<B> stream2, BiConsumer<? super A, ? super B> biConsumer) {
        Preconditions.checkNotNull(biConsumer);
        if (stream.isParallel() || stream2.isParallel()) {
            zip(stream, stream2, new v()).forEach(new m(biConsumer, 2));
            return;
        }
        Iterator<A> it = stream.iterator();
        Iterator<B> it2 = stream2.iterator();
        while (it.hasNext() && it2.hasNext()) {
            biConsumer.accept(it.next(), it2.next());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Spliterator lambda$concat$0(Spliterator spliterator) {
        return spliterator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Spliterator.OfInt lambda$concat$2(Spliterator.OfInt ofInt) {
        return ofInt;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Spliterator.OfLong lambda$concat$4(Spliterator.OfLong ofLong) {
        return ofLong;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Spliterator.OfDouble lambda$concat$6(Spliterator.OfDouble ofDouble) {
        return ofDouble;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$forEachPair$0(BiConsumer biConsumer, TemporaryPair temporaryPair) {
        biConsumer.accept(temporaryPair.a, temporaryPair.b);
    }

    public static <T, R> Stream<R> mapWithIndex(Stream<T> stream, final FunctionWithIndex<? super T, ? extends R> functionWithIndex) {
        Preconditions.checkNotNull(stream);
        Preconditions.checkNotNull(functionWithIndex);
        boolean zIsParallel = stream.isParallel();
        Spliterator<T> spliterator = stream.spliterator();
        if (spliterator.hasCharacteristics(16384)) {
            return (Stream) StreamSupport.stream(new C1Splitr(spliterator, 0L, functionWithIndex), zIsParallel).onClose(new NN(stream, 19));
        }
        final Iterator it = Spliterators.iterator(spliterator);
        return (Stream) StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(spliterator.estimateSize(), spliterator.characteristics() & 80) { // from class: com.google.common.collect.Streams.2
            long index = 0;

            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super R> consumer) {
                if (!it.hasNext()) {
                    return false;
                }
                FunctionWithIndex functionWithIndex2 = functionWithIndex;
                Object next = it.next();
                long j = this.index;
                this.index = 1 + j;
                consumer.accept((Object) functionWithIndex2.apply(next, j));
                return true;
            }
        }, zIsParallel).onClose(new NN(stream, 19));
    }

    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return iterable instanceof Collection ? ((Collection) iterable).stream() : StreamSupport.stream(iterable.spliterator(), false);
    }

    @Beta
    public static <A, B, R> Stream<R> zip(Stream<A> stream, Stream<B> stream2, final BiFunction<? super A, ? super B, R> biFunction) {
        Preconditions.checkNotNull(stream);
        Preconditions.checkNotNull(stream2);
        Preconditions.checkNotNull(biFunction);
        boolean z = stream.isParallel() || stream2.isParallel();
        Spliterator<A> spliterator = stream.spliterator();
        Spliterator<B> spliterator2 = stream2.spliterator();
        int iCharacteristics = spliterator.characteristics() & spliterator2.characteristics() & 80;
        final Iterator it = Spliterators.iterator(spliterator);
        final Iterator it2 = Spliterators.iterator(spliterator2);
        return (Stream) ((Stream) StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(Math.min(spliterator.estimateSize(), spliterator2.estimateSize()), iCharacteristics) { // from class: com.google.common.collect.Streams.1
            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super R> consumer) {
                if (!it.hasNext() || !it2.hasNext()) {
                    return false;
                }
                consumer.accept((Object) biFunction.apply(it.next(), it2.next()));
                return true;
            }
        }, z).onClose(new NN(stream, 19))).onClose(new NN(stream2, 19));
    }

    @Deprecated
    public static <T> Stream<T> stream(Collection<T> collection) {
        return collection.stream();
    }

    public static <T> Stream<T> stream(Iterator<T> it) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, 0), false);
    }

    public static <T> Stream<T> stream(com.google.common.base.Optional<T> optional) {
        return optional.isPresent() ? Stream.of(optional.get()) : Stream.empty();
    }

    @Beta
    public static <T> Stream<T> stream(Optional<T> optional) {
        return optional.isPresent() ? Stream.of(optional.get()) : Stream.empty();
    }

    @Beta
    public static IntStream stream(OptionalInt optionalInt) {
        return optionalInt.isPresent() ? IntStream.of(optionalInt.getAsInt()) : IntStream.empty();
    }

    @Beta
    public static LongStream stream(OptionalLong optionalLong) {
        return optionalLong.isPresent() ? LongStream.of(optionalLong.getAsLong()) : LongStream.empty();
    }

    @Beta
    public static DoubleStream stream(OptionalDouble optionalDouble) {
        return optionalDouble.isPresent() ? DoubleStream.of(optionalDouble.getAsDouble()) : DoubleStream.empty();
    }

    public static IntStream concat(IntStream... intStreamArr) {
        ImmutableList.Builder builder = new ImmutableList.Builder(intStreamArr.length);
        long jSaturatedAdd = 0;
        int iCharacteristics = 336;
        boolean zIsParallel = false;
        for (IntStream intStream : intStreamArr) {
            zIsParallel |= intStream.isParallel();
            Spliterator<Integer> spliterator = intStream.spliterator();
            builder.add(spliterator);
            iCharacteristics &= spliterator.characteristics();
            jSaturatedAdd = LongMath.saturatedAdd(jSaturatedAdd, spliterator.estimateSize());
        }
        return (IntStream) StreamSupport.intStream(CollectSpliterators.flatMapToInt(builder.build().spliterator(), new C1145Vn(27), iCharacteristics, jSaturatedAdd), zIsParallel).onClose(new NN(intStreamArr, 13));
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [java.util.Spliterator, java.util.Spliterator$OfInt] */
    public static <R> Stream<R> mapWithIndex(IntStream intStream, final IntFunctionWithIndex<R> intFunctionWithIndex) {
        Preconditions.checkNotNull(intStream);
        Preconditions.checkNotNull(intFunctionWithIndex);
        boolean zIsParallel = intStream.isParallel();
        ?? Spliterator = intStream.spliterator();
        if (!Spliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfInt it = Spliterators.iterator((Spliterator.OfInt) Spliterator);
            return (Stream) StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(Spliterator.estimateSize(), Spliterator.characteristics() & 80) { // from class: com.google.common.collect.Streams.3
                long index = 0;

                @Override // java.util.Spliterator
                public boolean tryAdvance(Consumer<? super R> consumer) {
                    if (!it.hasNext()) {
                        return false;
                    }
                    IntFunctionWithIndex intFunctionWithIndex2 = intFunctionWithIndex;
                    int iNextInt = it.nextInt();
                    long j = this.index;
                    this.index = 1 + j;
                    consumer.accept((Object) intFunctionWithIndex2.apply(iNextInt, j));
                    return true;
                }
            }, zIsParallel).onClose(new NN(intStream, 14));
        }
        return (Stream) StreamSupport.stream(new C2Splitr(Spliterator, 0L, intFunctionWithIndex), zIsParallel).onClose(new NN(intStream, 14));
    }

    public static OptionalInt findLast(IntStream intStream) {
        return (OptionalInt) findLast(intStream.boxed()).map(new C1145Vn(21)).orElse(OptionalInt.empty());
    }

    public static LongStream concat(LongStream... longStreamArr) {
        ImmutableList.Builder builder = new ImmutableList.Builder(longStreamArr.length);
        long jSaturatedAdd = 0;
        int iCharacteristics = 336;
        boolean zIsParallel = false;
        for (LongStream longStream : longStreamArr) {
            zIsParallel |= longStream.isParallel();
            Spliterator<Long> spliterator = longStream.spliterator();
            builder.add(spliterator);
            iCharacteristics &= spliterator.characteristics();
            jSaturatedAdd = LongMath.saturatedAdd(jSaturatedAdd, spliterator.estimateSize());
        }
        return (LongStream) StreamSupport.longStream(CollectSpliterators.flatMapToLong(builder.build().spliterator(), new C1145Vn(24), iCharacteristics, jSaturatedAdd), zIsParallel).onClose(new NN(longStreamArr, 18));
    }

    public static OptionalLong findLast(LongStream longStream) {
        return (OptionalLong) findLast(longStream.boxed()).map(new C1145Vn(26)).orElse(OptionalLong.empty());
    }

    public static OptionalDouble findLast(DoubleStream doubleStream) {
        return (OptionalDouble) findLast(doubleStream.boxed()).map(new C1145Vn(22)).orElse(OptionalDouble.empty());
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [java.util.Spliterator, java.util.Spliterator$OfLong] */
    public static <R> Stream<R> mapWithIndex(LongStream longStream, final LongFunctionWithIndex<R> longFunctionWithIndex) {
        Preconditions.checkNotNull(longStream);
        Preconditions.checkNotNull(longFunctionWithIndex);
        boolean zIsParallel = longStream.isParallel();
        ?? Spliterator = longStream.spliterator();
        if (!Spliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfLong it = Spliterators.iterator((Spliterator.OfLong) Spliterator);
            return (Stream) StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(Spliterator.estimateSize(), Spliterator.characteristics() & 80) { // from class: com.google.common.collect.Streams.4
                long index = 0;

                @Override // java.util.Spliterator
                public boolean tryAdvance(Consumer<? super R> consumer) {
                    if (!it.hasNext()) {
                        return false;
                    }
                    LongFunctionWithIndex longFunctionWithIndex2 = longFunctionWithIndex;
                    long jNextLong = it.nextLong();
                    long j = this.index;
                    this.index = 1 + j;
                    consumer.accept((Object) longFunctionWithIndex2.apply(jNextLong, j));
                    return true;
                }
            }, zIsParallel).onClose(new NN(longStream, 16));
        }
        return (Stream) StreamSupport.stream(new C3Splitr(Spliterator, 0L, longFunctionWithIndex), zIsParallel).onClose(new NN(longStream, 16));
    }

    public static DoubleStream concat(DoubleStream... doubleStreamArr) {
        ImmutableList.Builder builder = new ImmutableList.Builder(doubleStreamArr.length);
        long jSaturatedAdd = 0;
        int iCharacteristics = 336;
        boolean zIsParallel = false;
        for (DoubleStream doubleStream : doubleStreamArr) {
            zIsParallel |= doubleStream.isParallel();
            Spliterator<Double> spliterator = doubleStream.spliterator();
            builder.add(spliterator);
            iCharacteristics &= spliterator.characteristics();
            jSaturatedAdd = LongMath.saturatedAdd(jSaturatedAdd, spliterator.estimateSize());
        }
        return (DoubleStream) StreamSupport.doubleStream(CollectSpliterators.flatMapToDouble(builder.build().spliterator(), new C1145Vn(23), iCharacteristics, jSaturatedAdd), zIsParallel).onClose(new NN(doubleStreamArr, 17));
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [java.util.Spliterator, java.util.Spliterator$OfDouble] */
    public static <R> Stream<R> mapWithIndex(DoubleStream doubleStream, final DoubleFunctionWithIndex<R> doubleFunctionWithIndex) {
        Preconditions.checkNotNull(doubleStream);
        Preconditions.checkNotNull(doubleFunctionWithIndex);
        boolean zIsParallel = doubleStream.isParallel();
        ?? Spliterator = doubleStream.spliterator();
        if (!Spliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfDouble it = Spliterators.iterator((Spliterator.OfDouble) Spliterator);
            return (Stream) StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(Spliterator.estimateSize(), Spliterator.characteristics() & 80) { // from class: com.google.common.collect.Streams.5
                long index = 0;

                @Override // java.util.Spliterator
                public boolean tryAdvance(Consumer<? super R> consumer) {
                    if (!it.hasNext()) {
                        return false;
                    }
                    DoubleFunctionWithIndex doubleFunctionWithIndex2 = doubleFunctionWithIndex;
                    double dNextDouble = it.nextDouble();
                    long j = this.index;
                    this.index = 1 + j;
                    consumer.accept((Object) doubleFunctionWithIndex2.apply(dNextDouble, j));
                    return true;
                }
            }, zIsParallel).onClose(new NN(doubleStream, 15));
        }
        return (Stream) StreamSupport.stream(new C4Splitr(Spliterator, 0L, doubleFunctionWithIndex), zIsParallel).onClose(new NN(doubleStream, 15));
    }
}
