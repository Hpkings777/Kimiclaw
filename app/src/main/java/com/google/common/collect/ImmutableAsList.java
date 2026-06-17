package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
abstract class ImmutableAsList<E> extends ImmutableList<E> {

    @J2ktIncompatible
    @GwtIncompatible
    public static final class SerializedForm implements Serializable {

        @J2ktIncompatible
        @GwtIncompatible
        private static final long serialVersionUID = 0;
        final ImmutableCollection<?> collection;

        public SerializedForm(ImmutableCollection<?> immutableCollection) {
            this.collection = immutableCollection;
        }

        public Object readResolve() {
            return this.collection.asList();
        }
    }

    @J2ktIncompatible
    @GwtIncompatible
    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return delegateCollection().contains(obj);
    }

    public abstract ImmutableCollection<E> delegateCollection();

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        return delegateCollection().isEmpty();
    }

    @Override // com.google.common.collect.ImmutableCollection
    public boolean isPartialView() {
        return delegateCollection().isPartialView();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return delegateCollection().size();
    }

    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
    @J2ktIncompatible
    @GwtIncompatible
    public Object writeReplace() {
        return new SerializedForm(delegateCollection());
    }
}
