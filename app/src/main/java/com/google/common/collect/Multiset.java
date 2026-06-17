package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public interface Multiset<E> extends Collection<E> {

    public interface Entry<E> {
        boolean equals(Object obj);

        int getCount();

        @ParametricNullness
        E getElement();

        int hashCode();

        String toString();
    }

    int add(@ParametricNullness E e, int i);

    boolean add(@ParametricNullness E e);

    boolean contains(Object obj);

    @Override // java.util.Collection
    boolean containsAll(Collection<?> collection);

    int count(Object obj);

    Set<E> elementSet();

    Set<Entry<E>> entrySet();

    boolean equals(Object obj);

    int hashCode();

    Iterator<E> iterator();

    int remove(Object obj, int i);

    boolean remove(Object obj);

    boolean removeAll(Collection<?> collection);

    boolean retainAll(Collection<?> collection);

    int setCount(@ParametricNullness E e, int i);

    boolean setCount(@ParametricNullness E e, int i, int i2);

    int size();

    String toString();
}
