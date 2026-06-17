package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AggregateFuture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
abstract class CollectionFuture<V, C> extends AggregateFuture<V, C> {
    private List<Present<V>> values;

    public static final class ListFuture<V> extends CollectionFuture<V, List<V>> {
        public ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> immutableCollection, boolean z) {
            super(immutableCollection, z);
            init();
        }

        @Override // com.google.common.util.concurrent.CollectionFuture
        public List<V> combine(List<Present<V>> list) {
            ArrayList arrayListNewArrayListWithCapacity = Lists.newArrayListWithCapacity(list.size());
            Iterator<Present<V>> it = list.iterator();
            while (it.hasNext()) {
                Present<V> next = it.next();
                arrayListNewArrayListWithCapacity.add(next != null ? next.value : null);
            }
            return Collections.unmodifiableList(arrayListNewArrayListWithCapacity);
        }
    }

    public static final class Present<V> {

        @ParametricNullness
        final V value;

        public Present(@ParametricNullness V v) {
            this.value = v;
        }
    }

    public CollectionFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> immutableCollection, boolean z) {
        super(immutableCollection, z, true);
        List<Present<V>> listNewArrayListWithCapacity = immutableCollection.isEmpty() ? Collections.EMPTY_LIST : Lists.newArrayListWithCapacity(immutableCollection.size());
        for (int i = 0; i < immutableCollection.size(); i++) {
            listNewArrayListWithCapacity.add(null);
        }
        this.values = listNewArrayListWithCapacity;
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    public final void collectOneValue(int i, @ParametricNullness V v) {
        List<Present<V>> list = this.values;
        if (list != null) {
            list.set(i, new Present<>(v));
        }
    }

    public abstract C combine(List<Present<V>> list);

    @Override // com.google.common.util.concurrent.AggregateFuture
    public final void handleAllCompleted() {
        List<Present<V>> list = this.values;
        if (list != null) {
            set(combine(list));
        }
    }

    @Override // com.google.common.util.concurrent.AggregateFuture
    public void releaseResources(AggregateFuture.ReleaseResourcesReason releaseResourcesReason) {
        super.releaseResources(releaseResourcesReason);
        this.values = null;
    }
}
