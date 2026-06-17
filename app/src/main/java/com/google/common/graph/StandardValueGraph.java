package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes.dex */
class StandardValueGraph<N, V> extends AbstractValueGraph<N, V> {
    private final boolean allowsSelfLoops;
    long edgeCount;
    private final boolean isDirected;
    final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    public StandardValueGraph(AbstractGraphBuilder<? super N> abstractGraphBuilder) {
        this(abstractGraphBuilder, abstractGraphBuilder.nodeOrder.createMap(abstractGraphBuilder.expectedNodeCount.or(10).intValue()), 0L);
    }

    private final GraphConnections<N, V> checkedConnections(N n) {
        GraphConnections<N, V> graphConnections = this.nodeConnections.get(n);
        if (graphConnections != null) {
            return graphConnections;
        }
        Preconditions.checkNotNull(n);
        throw new IllegalArgumentException("Node " + n + " is not an element of this graph.");
    }

    private final V edgeValueOrDefaultInternal(N n, N n2, V v) {
        GraphConnections<N, V> graphConnections = this.nodeConnections.get(n);
        V vValue = graphConnections == null ? null : graphConnections.value(n2);
        return vValue == null ? v : vValue;
    }

    private final boolean hasEdgeConnectingInternal(N n, N n2) {
        GraphConnections<N, V> graphConnections = this.nodeConnections.get(n);
        return graphConnections != null && graphConnections.successors().contains(n2);
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public Set<N> adjacentNodes(N n) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(n).adjacentNodes(), n);
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    public final boolean containsNode(N n) {
        return this.nodeConnections.containsKey(n);
    }

    @Override // com.google.common.graph.AbstractBaseGraph
    public long edgeCount() {
        return this.edgeCount;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public V edgeValueOrDefault(N n, N n2, V v) {
        return (V) edgeValueOrDefaultInternal(Preconditions.checkNotNull(n), Preconditions.checkNotNull(n2), v);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(N n, N n2) {
        return hasEdgeConnectingInternal(Preconditions.checkNotNull(n), Preconditions.checkNotNull(n2));
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public Set<EndpointPair<N>> incidentEdges(N n) {
        final GraphConnections<N, V> graphConnectionsCheckedConnections = checkedConnections(n);
        return (Set<EndpointPair<N>>) nodeInvalidatableSet(new IncidentEdgeSet<N>(this, this, n) { // from class: com.google.common.graph.StandardValueGraph.1
            final /* synthetic */ StandardValueGraph this$0;

            {
                this.this$0 = this;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<EndpointPair<N>> iterator() {
                return graphConnectionsCheckedConnections.incidentEdgeIterator(this.node);
            }
        }, n);
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public boolean isDirected() {
        return this.isDirected;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.Graph
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    public V edgeValueOrDefault(EndpointPair<N> endpointPair, V v) {
        validateEndpoints(endpointPair);
        return edgeValueOrDefaultInternal(endpointPair.nodeU(), endpointPair.nodeV(), v);
    }

    @Override // com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
    public boolean hasEdgeConnecting(EndpointPair<N> endpointPair) {
        Preconditions.checkNotNull(endpointPair);
        return isOrderingCompatible(endpointPair) && hasEdgeConnectingInternal(endpointPair.nodeU(), endpointPair.nodeV());
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.PredecessorsFunction, com.google.common.graph.Graph
    public Set<N> predecessors(N n) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(n).predecessors(), n);
    }

    @Override // com.google.common.graph.BaseGraph, com.google.common.graph.SuccessorsFunction
    public Set<N> successors(N n) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(n).successors(), n);
    }

    public StandardValueGraph(AbstractGraphBuilder<? super N> abstractGraphBuilder, Map<N, GraphConnections<N, V>> map, long j) {
        MapIteratorCache<N, GraphConnections<N, V>> mapIteratorCache;
        this.isDirected = abstractGraphBuilder.directed;
        this.allowsSelfLoops = abstractGraphBuilder.allowsSelfLoops;
        this.nodeOrder = (ElementOrder<N>) abstractGraphBuilder.nodeOrder.cast();
        if (map instanceof TreeMap) {
            mapIteratorCache = new MapRetrievalCache<>(map);
        } else {
            mapIteratorCache = new MapIteratorCache<>(map);
        }
        this.nodeConnections = mapIteratorCache;
        this.edgeCount = Graphs.checkNonNegative(j);
    }
}
