package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
@Beta
public final class ElementOrder<T> {
    private final Comparator<T> comparator;
    private final Type type;

    public enum Type {
        UNORDERED,
        STABLE,
        INSERTION,
        SORTED
    }

    private ElementOrder(Type type, Comparator<T> comparator) {
        this.type = (Type) Preconditions.checkNotNull(type);
        this.comparator = comparator;
        Preconditions.checkState((type == Type.SORTED) == (comparator != null));
    }

    public static <S> ElementOrder<S> insertion() {
        return new ElementOrder<>(Type.INSERTION, null);
    }

    public static <S extends Comparable<? super S>> ElementOrder<S> natural() {
        return new ElementOrder<>(Type.SORTED, Ordering.natural());
    }

    public static <S> ElementOrder<S> sorted(Comparator<S> comparator) {
        return new ElementOrder<>(Type.SORTED, (Comparator) Preconditions.checkNotNull(comparator));
    }

    public static <S> ElementOrder<S> stable() {
        return new ElementOrder<>(Type.STABLE, null);
    }

    public static <S> ElementOrder<S> unordered() {
        return new ElementOrder<>(Type.UNORDERED, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T1 extends T> ElementOrder<T1> cast() {
        return this;
    }

    public Comparator<T> comparator() {
        Comparator<T> comparator = this.comparator;
        if (comparator != null) {
            return comparator;
        }
        throw new UnsupportedOperationException("This ordering does not define a comparator.");
    }

    public <K extends T, V> Map<K, V> createMap(int i) {
        int iOrdinal = this.type.ordinal();
        if (iOrdinal == 0) {
            return Maps.newHashMapWithExpectedSize(i);
        }
        if (iOrdinal == 1 || iOrdinal == 2) {
            return Maps.newLinkedHashMapWithExpectedSize(i);
        }
        if (iOrdinal == 3) {
            return Maps.newTreeMap(comparator());
        }
        throw new AssertionError();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ElementOrder)) {
            return false;
        }
        ElementOrder elementOrder = (ElementOrder) obj;
        return this.type == elementOrder.type && Objects.equals(this.comparator, elementOrder.comparator);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.comparator);
    }

    public String toString() {
        MoreObjects.ToStringHelper toStringHelperAdd = MoreObjects.toStringHelper(this).add("type", this.type);
        Comparator<T> comparator = this.comparator;
        if (comparator != null) {
            toStringHelperAdd.add("comparator", comparator);
        }
        return toStringHelperAdd.toString();
    }

    public Type type() {
        return this.type;
    }
}
