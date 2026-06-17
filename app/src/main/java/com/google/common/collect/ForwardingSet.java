package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public abstract class ForwardingSet<E> extends ForwardingCollection<E> implements Set<E> {
    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    public abstract Set<E> delegate();

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        return obj == this || delegate().equals(obj);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        return delegate().hashCode();
    }

    public boolean standardEquals(Object obj) {
        return Sets.equalsImpl(this, obj);
    }

    public int standardHashCode() {
        return Sets.hashCodeImpl(this);
    }

    @Override // com.google.common.collect.ForwardingCollection
    public boolean standardRemoveAll(Collection<?> collection) {
        return Sets.removeAllImpl(this, (Collection<?>) Preconditions.checkNotNull(collection));
    }
}
