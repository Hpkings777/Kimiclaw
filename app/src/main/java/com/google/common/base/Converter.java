package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import defpackage.C4693xT0;
import java.io.Serializable;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public abstract class Converter<A, B> implements Function<A, B> {
    private final boolean handleNullAutomatically;
    private transient Converter<B, A> reverse;

    public static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;
        final Converter<A, B> first;
        final Converter<B, C> second;

        public ConverterComposition(Converter<A, B> converter, Converter<B, C> converter2) {
            this.first = converter;
            this.second = converter2;
        }

        @Override // com.google.common.base.Converter
        public A correctedDoBackward(C c) {
            return (A) this.first.correctedDoBackward(this.second.correctedDoBackward(c));
        }

        @Override // com.google.common.base.Converter
        public C correctedDoForward(A a) {
            return (C) this.second.correctedDoForward(this.first.correctedDoForward(a));
        }

        @Override // com.google.common.base.Converter
        public A doBackward(C c) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        public C doForward(A a) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(Object obj) {
            if (obj instanceof ConverterComposition) {
                ConverterComposition converterComposition = (ConverterComposition) obj;
                if (this.first.equals(converterComposition.first) && this.second.equals(converterComposition.second)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return this.second.hashCode() + (this.first.hashCode() * 31);
        }

        public String toString() {
            return this.first + ".andThen(" + this.second + ")";
        }
    }

    public static final class FunctionBasedConverter<A, B> extends Converter<A, B> implements Serializable {
        private final Function<? super B, ? extends A> backwardFunction;
        private final Function<? super A, ? extends B> forwardFunction;

        @Override // com.google.common.base.Converter
        public A doBackward(B b) {
            return this.backwardFunction.apply(b);
        }

        @Override // com.google.common.base.Converter
        public B doForward(A a) {
            return this.forwardFunction.apply(a);
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(Object obj) {
            if (obj instanceof FunctionBasedConverter) {
                FunctionBasedConverter functionBasedConverter = (FunctionBasedConverter) obj;
                if (this.forwardFunction.equals(functionBasedConverter.forwardFunction) && this.backwardFunction.equals(functionBasedConverter.backwardFunction)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return this.backwardFunction.hashCode() + (this.forwardFunction.hashCode() * 31);
        }

        public String toString() {
            return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")";
        }

        private FunctionBasedConverter(Function<? super A, ? extends B> function, Function<? super B, ? extends A> function2) {
            this.forwardFunction = (Function) Preconditions.checkNotNull(function);
            this.backwardFunction = (Function) Preconditions.checkNotNull(function2);
        }
    }

    public static final class IdentityConverter<T> extends Converter<T, T> implements Serializable {
        static final Converter<?, ?> INSTANCE = new IdentityConverter();

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;

        private IdentityConverter() {
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.base.Converter
        public <S> Converter<T, S> doAndThen(Converter<T, S> converter) {
            return (Converter) Preconditions.checkNotNull(converter, "otherConverter");
        }

        @Override // com.google.common.base.Converter
        public T doBackward(T t) {
            return t;
        }

        @Override // com.google.common.base.Converter
        public T doForward(T t) {
            return t;
        }

        @Override // com.google.common.base.Converter
        public IdentityConverter<T> reverse() {
            return this;
        }

        public String toString() {
            return "Converter.identity()";
        }
    }

    public static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;
        final Converter<A, B> original;

        public ReverseConverter(Converter<A, B> converter) {
            this.original = converter;
        }

        @Override // com.google.common.base.Converter
        public B correctedDoBackward(A a) {
            return this.original.correctedDoForward(a);
        }

        @Override // com.google.common.base.Converter
        public A correctedDoForward(B b) {
            return this.original.correctedDoBackward(b);
        }

        @Override // com.google.common.base.Converter
        public B doBackward(A a) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        public A doForward(B b) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(Object obj) {
            if (obj instanceof ReverseConverter) {
                return this.original.equals(((ReverseConverter) obj).original);
            }
            return false;
        }

        public int hashCode() {
            return ~this.original.hashCode();
        }

        @Override // com.google.common.base.Converter
        public Converter<A, B> reverse() {
            return this.original;
        }

        public String toString() {
            return this.original + ".reverse()";
        }
    }

    public Converter() {
        this(true);
    }

    public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> function, Function<? super B, ? extends A> function2) {
        return new FunctionBasedConverter(function, function2);
    }

    public static <T> Converter<T, T> identity() {
        return (IdentityConverter) IdentityConverter.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Iterator lambda$convertAll$0(Iterable iterable) {
        return new Iterator<B>(this, iterable) { // from class: com.google.common.base.Converter.1
            private final Iterator<? extends A> fromIterator;
            final /* synthetic */ Converter this$0;
            final /* synthetic */ Iterable val$fromIterable;

            {
                this.val$fromIterable = iterable;
                this.this$0 = this;
                this.fromIterator = iterable.iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.fromIterator.hasNext();
            }

            @Override // java.util.Iterator
            public B next() {
                return (B) this.this$0.convert(this.fromIterator.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                this.fromIterator.remove();
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    private A unsafeDoBackward(B b) {
        return (A) doBackward(NullnessCasts.uncheckedCastNullableTToT(b));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private B unsafeDoForward(A a) {
        return (B) doForward(NullnessCasts.uncheckedCastNullableTToT(a));
    }

    public final <C> Converter<A, C> andThen(Converter<B, C> converter) {
        return doAndThen(converter);
    }

    @Override // com.google.common.base.Function
    @Deprecated
    public final B apply(A a) {
        return convert(a);
    }

    public final B convert(A a) {
        return correctedDoForward(a);
    }

    public Iterable<B> convertAll(Iterable<? extends A> iterable) {
        Preconditions.checkNotNull(iterable, "fromIterable");
        return new C4693xT0(iterable, 3, this);
    }

    public A correctedDoBackward(B b) {
        if (!this.handleNullAutomatically) {
            return unsafeDoBackward(b);
        }
        if (b == null) {
            return null;
        }
        return (A) Preconditions.checkNotNull(doBackward(b));
    }

    public B correctedDoForward(A a) {
        if (!this.handleNullAutomatically) {
            return unsafeDoForward(a);
        }
        if (a == null) {
            return null;
        }
        return (B) Preconditions.checkNotNull(doForward(a));
    }

    public <C> Converter<A, C> doAndThen(Converter<B, C> converter) {
        return new ConverterComposition(this, (Converter) Preconditions.checkNotNull(converter));
    }

    public abstract A doBackward(B b);

    public abstract B doForward(A a);

    @Override // com.google.common.base.Function
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Converter<B, A> reverse() {
        Converter<B, A> converter = this.reverse;
        if (converter != null) {
            return converter;
        }
        ReverseConverter reverseConverter = new ReverseConverter(this);
        this.reverse = reverseConverter;
        return reverseConverter;
    }

    public Converter(boolean z) {
        this.handleNullAutomatically = z;
    }
}
