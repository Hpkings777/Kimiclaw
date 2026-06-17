package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

/* JADX INFO: loaded from: classes.dex */
@GwtIncompatible
public abstract class ForwardingNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E> {

    public class StandardDescendingSet extends Sets.DescendingSet<E> {
        public StandardDescendingSet() {
            super(ForwardingNavigableSet.this);
        }
    }

    @Override // java.util.NavigableSet
    public E ceiling(@ParametricNullness E e) {
        return delegate().ceiling(e);
    }

    @Override // com.google.common.collect.ForwardingSortedSet, com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    public abstract NavigableSet<E> delegate();

    @Override // java.util.NavigableSet
    public Iterator<E> descendingIterator() {
        return delegate().descendingIterator();
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> descendingSet() {
        return delegate().descendingSet();
    }

    @Override // java.util.NavigableSet
    public E floor(@ParametricNullness E e) {
        return delegate().floor(e);
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> headSet(@ParametricNullness E e, boolean z) {
        return delegate().headSet(e, z);
    }

    @Override // java.util.NavigableSet
    public E higher(@ParametricNullness E e) {
        return delegate().higher(e);
    }

    @Override // java.util.NavigableSet
    public E lower(@ParametricNullness E e) {
        return delegate().lower(e);
    }

    @Override // java.util.NavigableSet
    public E pollFirst() {
        return delegate().pollFirst();
    }

    @Override // java.util.NavigableSet
    public E pollLast() {
        return delegate().pollLast();
    }

    public E standardCeiling(@ParametricNullness E e) {
        return (E) Iterators.getNext(tailSet(e, true).iterator(), null);
    }

    @ParametricNullness
    public E standardFirst() {
        return iterator().next();
    }

    public E standardFloor(@ParametricNullness E e) {
        return (E) Iterators.getNext(headSet(e, true).descendingIterator(), null);
    }

    public SortedSet<E> standardHeadSet(@ParametricNullness E e) {
        return headSet(e, false);
    }

    public E standardHigher(@ParametricNullness E e) {
        return (E) Iterators.getNext(tailSet(e, false).iterator(), null);
    }

    @ParametricNullness
    public E standardLast() {
        return descendingIterator().next();
    }

    public E standardLower(@ParametricNullness E e) {
        return (E) Iterators.getNext(headSet(e, false).descendingIterator(), null);
    }

    public E standardPollFirst() {
        return (E) Iterators.pollNext(iterator());
    }

    public E standardPollLast() {
        return (E) Iterators.pollNext(descendingIterator());
    }

    public NavigableSet<E> standardSubSet(@ParametricNullness E e, boolean z, @ParametricNullness E e2, boolean z2) {
        return tailSet(e, z).headSet(e2, z2);
    }

    public SortedSet<E> standardTailSet(@ParametricNullness E e) {
        return tailSet(e, true);
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> subSet(@ParametricNullness E e, boolean z, @ParametricNullness E e2, boolean z2) {
        return delegate().subSet(e, z, e2, z2);
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> tailSet(@ParametricNullness E e, boolean z) {
        return delegate().tailSet(e, z);
    }

    @Override // com.google.common.collect.ForwardingSortedSet
    public SortedSet<E> standardSubSet(@ParametricNullness E e, @ParametricNullness E e2) {
        return subSet(e, true, e2, false);
    }
}
