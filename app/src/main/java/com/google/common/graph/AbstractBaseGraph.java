package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.graph.AbstractBaseGraph;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
abstract class AbstractBaseGraph<N> implements BaseGraph<N> {

    /* JADX INFO: renamed from: com.google.common.graph.AbstractBaseGraph$2, reason: invalid class name */
    public class AnonymousClass2 extends IncidentEdgeSet<N> {
        public AnonymousClass2(BaseGraph baseGraph, Object obj) {
            super(baseGraph, obj);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ EndpointPair lambda$iterator$0(Object obj) {
            return EndpointPair.ordered(obj, this.node);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ EndpointPair lambda$iterator$1(Object obj) {
            return EndpointPair.ordered(this.node, obj);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ EndpointPair lambda$iterator$2(Object obj) {
            return EndpointPair.unordered(this.node, obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public UnmodifiableIterator<EndpointPair<N>> iterator() {
            if (!this.graph.isDirected()) {
                final int i = 2;
                return Iterators.unmodifiableIterator(Iterators.transform(this.graph.adjacentNodes(this.node).iterator(), new Function(this) { // from class: com.google.common.graph.d
                    public final /* synthetic */ AbstractBaseGraph.AnonymousClass2 b;

                    {
                        this.b = this;
                    }

                    @Override // com.google.common.base.Function
                    public final Object apply(Object obj) {
                        switch (i) {
                            case 0:
                                return this.b.lambda$iterator$0(obj);
                            case 1:
                                return this.b.lambda$iterator$1(obj);
                            default:
                                return this.b.lambda$iterator$2(obj);
                        }
                    }
                }));
            }
            final int i2 = 0;
            final int i3 = 1;
            return Iterators.unmodifiableIterator(Iterators.concat(Iterators.transform(this.graph.predecessors((Object) this.node).iterator(), new Function(this) { // from class: com.google.common.graph.d
                public final /* synthetic */ AbstractBaseGraph.AnonymousClass2 b;

                {
                    this.b = this;
                }

                @Override // com.google.common.base.Function
                public final Object apply(Object obj) {
                    switch (i2) {
                        case 0:
                            return this.b.lambda$iterator$0(obj);
                        case 1:
                            return this.b.lambda$iterator$1(obj);
                        default:
                            return this.b.lambda$iterator$2(obj);
                    }
                }
            }), Iterators.transform(Sets.difference(this.graph.successors((Object) this.node), ImmutableSet.of(this.node)).iterator(), new Function(this) { // from class: com.google.common.graph.d
                public final /* synthetic */ AbstractBaseGraph.AnonymousClass2 b;

                {
                    this.b = this;
                }

                @Override // com.google.common.base.Function
                public final Object apply(Object obj) {
                    switch (i3) {
                        case 0:
                            return this.b.lambda$iterator$0(obj);
                        case 1:
                            return this.b.lambda$iterator$1(obj);
                        default:
                            return this.b.lambda$iterator$2(obj);
                    }
                }
            })));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$nodeInvalidatableSet$0(Object obj) {
        return Boolean.valueOf(nodes().contains(obj));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$nodeInvalidatableSet$1(Object obj) {
        return String.format("Node %s that was used to generate this set is no longer in the graph.", obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$nodePairInvalidatableSet$0(Object obj, Object obj2) {
        return Boolean.valueOf(nodes().contains(obj) && nodes().contains(obj2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$nodePairInvalidatableSet$1(Object obj, Object obj2) {
        return String.format("Node %s or node %s that were used to generate this set are no longer in the graph.", obj, obj2);
    }

    @Override // com.google.common.graph.BaseGraph
    public int degree(N n) {
        if (isDirected()) {
            return IntMath.saturatedAdd(predecessors((Object) n).size(), successors((Object) n).size());
        }
        Set<N> setAdjacentNodes = adjacentNodes(n);
        return IntMath.saturatedAdd(setAdjacentNodes.size(), (allowsSelfLoops() && setAdjacentNodes.contains(n)) ? 1 : 0);
    }

    public long edgeCount() {
        Iterator<N> it = nodes().iterator();
        long jDegree = 0;
        while (it.hasNext()) {
            jDegree += (long) degree(it.next());
        }
        Preconditions.checkState((1 & jDegree) == 0);
        return jDegree >>> 1;
    }

    @Override // com.google.common.graph.BaseGraph
    public Set<EndpointPair<N>> edges() {
        return new AbstractSet<EndpointPair<N>>() { // from class: com.google.common.graph.AbstractBaseGraph.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                if (!(obj instanceof EndpointPair)) {
                    return false;
                }
                EndpointPair<?> endpointPair = (EndpointPair) obj;
                return AbstractBaseGraph.this.isOrderingCompatible(endpointPair) && AbstractBaseGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractBaseGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object obj) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                return EndpointPairIterator.of(AbstractBaseGraph.this);
            }
        };
    }

    @Override // com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(N n, N n2) {
        Preconditions.checkNotNull(n);
        Preconditions.checkNotNull(n2);
        return nodes().contains(n) && successors((Object) n).contains(n2);
    }

    @Override // com.google.common.graph.BaseGraph
    public int inDegree(N n) {
        return isDirected() ? predecessors((Object) n).size() : degree(n);
    }

    @Override // com.google.common.graph.BaseGraph
    public ElementOrder<N> incidentEdgeOrder() {
        return ElementOrder.unordered();
    }

    @Override // com.google.common.graph.BaseGraph
    public Set<EndpointPair<N>> incidentEdges(N n) {
        Preconditions.checkNotNull(n);
        Preconditions.checkArgument(nodes().contains(n), "Node %s is not an element of this graph.", n);
        return (Set<EndpointPair<N>>) nodeInvalidatableSet(new AnonymousClass2(this, n), n);
    }

    public final boolean isOrderingCompatible(EndpointPair<?> endpointPair) {
        return endpointPair.isOrdered() == isDirected();
    }

    public final <T> Set<T> nodeInvalidatableSet(Set<T> set, final N n) {
        return InvalidatableSet.of((Set) set, (Supplier<Boolean>) new b((AbstractBaseGraph) this, (Object) n), (Supplier<String>) new Supplier() { // from class: com.google.common.graph.c
            @Override // com.google.common.base.Supplier
            public final Object get() {
                return AbstractBaseGraph.lambda$nodeInvalidatableSet$1(n);
            }
        });
    }

    public final <T> Set<T> nodePairInvalidatableSet(Set<T> set, final N n, final N n2) {
        return InvalidatableSet.of((Set) set, (Supplier<Boolean>) new Supplier() { // from class: com.google.common.graph.a
            @Override // com.google.common.base.Supplier
            public final Object get() {
                return this.a.lambda$nodePairInvalidatableSet$0(n, n2);
            }
        }, (Supplier<String>) new b(n, n2));
    }

    @Override // com.google.common.graph.BaseGraph
    public int outDegree(N n) {
        return isDirected() ? successors((Object) n).size() : degree(n);
    }

    public final void validateEndpoints(EndpointPair<?> endpointPair) {
        Preconditions.checkNotNull(endpointPair);
        Preconditions.checkArgument(isOrderingCompatible(endpointPair), "Mismatch: endpoints' ordering is not compatible with directionality of the graph");
    }

    @Override // com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(EndpointPair<N> endpointPair) {
        Preconditions.checkNotNull(endpointPair);
        if (!isOrderingCompatible(endpointPair)) {
            return false;
        }
        N nNodeU = endpointPair.nodeU();
        return nodes().contains(nNodeU) && successors((Object) nNodeU).contains(endpointPair.nodeV());
    }
}
