package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public abstract class ForwardingIterator<T> extends ForwardingObject implements Iterator<T> {
    @Override // com.google.common.collect.ForwardingObject
    public abstract Iterator<T> delegate();

    @Override // java.util.Iterator
    public boolean hasNext() {
        return delegate().hasNext();
    }

    @ParametricNullness
    public T next() {
        return delegate().next();
    }

    public void remove() {
        delegate().remove();
    }
}
