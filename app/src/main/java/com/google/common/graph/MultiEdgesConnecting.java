package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
abstract class MultiEdgesConnecting<E> extends AbstractSet<E> {
    private final Map<E, ?> outEdgeToNode;
    private final Object targetNode;

    public MultiEdgesConnecting(Map<E, ?> map, Object obj) {
        this.outEdgeToNode = (Map) Preconditions.checkNotNull(map);
        this.targetNode = Preconditions.checkNotNull(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.targetNode.equals(this.outEdgeToNode.get(obj));
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public UnmodifiableIterator<E> iterator() {
        final Iterator<Map.Entry<E, ?>> it = this.outEdgeToNode.entrySet().iterator();
        return new AbstractIterator<E>(this) { // from class: com.google.common.graph.MultiEdgesConnecting.1
            final /* synthetic */ MultiEdgesConnecting this$0;

            {
                this.this$0 = this;
            }

            @Override // com.google.common.collect.AbstractIterator
            public E computeNext() {
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (this.this$0.targetNode.equals(entry.getValue())) {
                        return (E) entry.getKey();
                    }
                }
                return endOfData();
            }
        };
    }
}
