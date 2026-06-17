package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.TopKSelector;
import defpackage.C1145Vn;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collector;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class Comparators {
    private Comparators() {
    }

    @IgnoreJRERequirement
    public static <T> Comparator<Optional<T>> emptiesFirst(Comparator<? super T> comparator) {
        Preconditions.checkNotNull(comparator);
        return Comparator.comparing(new C1145Vn(14), Comparator.nullsFirst(comparator));
    }

    @IgnoreJRERequirement
    public static <T> Comparator<Optional<T>> emptiesLast(Comparator<? super T> comparator) {
        Preconditions.checkNotNull(comparator);
        return Comparator.comparing(new C1145Vn(15), Comparator.nullsLast(comparator));
    }

    @IgnoreJRERequirement
    public static <T> Collector<T, ?, List<T>> greatest(int i, Comparator<? super T> comparator) {
        return least(i, comparator.reversed());
    }

    public static <T> boolean isInOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
        Preconditions.checkNotNull(comparator);
        Iterator<? extends T> it = iterable.iterator();
        if (!it.hasNext()) {
            return true;
        }
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (comparator.compare(next, next2) > 0) {
                return false;
            }
            next = next2;
        }
        return true;
    }

    public static <T> boolean isInStrictOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
        Preconditions.checkNotNull(comparator);
        Iterator<? extends T> it = iterable.iterator();
        if (!it.hasNext()) {
            return true;
        }
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (comparator.compare(next, next2) >= 0) {
                return false;
            }
            next = next2;
        }
        return true;
    }

    @IgnoreJRERequirement
    public static <T> Collector<T, ?, List<T>> least(final int i, final Comparator<? super T> comparator) {
        CollectPreconditions.checkNonnegative(i, "k");
        Preconditions.checkNotNull(comparator);
        return Collector.of(new Supplier() { // from class: fp
            @Override // java.util.function.Supplier
            public final Object get() {
                return TopKSelector.least(i, comparator);
            }
        }, new c(1), new d(7), new e(3), Collector.Characteristics.UNORDERED);
    }

    public static <T, S extends T> Comparator<Iterable<S>> lexicographical(Comparator<T> comparator) {
        return new LexicographicalOrdering((Comparator) Preconditions.checkNotNull(comparator));
    }

    public static <T extends Comparable<? super T>> T max(T t, T t2) {
        return t.compareTo(t2) >= 0 ? t : t2;
    }

    public static <T extends Comparable<? super T>> T min(T t, T t2) {
        return t.compareTo(t2) <= 0 ? t : t2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @IgnoreJRERequirement
    public static <T> T orElseNull(Optional<T> optional) {
        return optional.orElse(null);
    }

    @ParametricNullness
    public static <T> T max(@ParametricNullness T t, @ParametricNullness T t2, Comparator<? super T> comparator) {
        return comparator.compare(t, t2) >= 0 ? t : t2;
    }

    @ParametricNullness
    public static <T> T min(@ParametricNullness T t, @ParametricNullness T t2, Comparator<? super T> comparator) {
        return comparator.compare(t, t2) <= 0 ? t : t2;
    }
}
