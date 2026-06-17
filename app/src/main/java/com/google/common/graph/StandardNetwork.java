package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes.dex */
class StandardNetwork<N, E> extends AbstractNetwork<N, E> {
    private final boolean allowsParallelEdges;
    private final boolean allowsSelfLoops;
    private final ElementOrder<E> edgeOrder;
    final MapIteratorCache<E, N> edgeToReferenceNode;
    private final boolean isDirected;
    final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    public StandardNetwork(NetworkBuilder<? super N, ? super E> networkBuilder) {
        this(networkBuilder, networkBuilder.nodeOrder.createMap(networkBuilder.expectedNodeCount.or(10).intValue()), networkBuilder.edgeOrder.createMap(networkBuilder.expectedEdgeCount.or(20).intValue()));
    }

    @Override // com.google.common.graph.Network
    public Set<N> adjacentNodes(N n) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(n).adjacentNodes(), n);
    }

    @Override // com.google.common.graph.Network
    public boolean allowsParallelEdges() {
        return this.allowsParallelEdges;
    }

    @Override // com.google.common.graph.Network
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    public final NetworkConnections<N, E> checkedConnections(N n) {
        NetworkConnections<N, E> networkConnections = this.nodeConnections.get(n);
        if (networkConnections != null) {
            return networkConnections;
        }
        Preconditions.checkNotNull(n);
        throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", n));
    }

    public final N checkedReferenceNode(E e) {
        N n = this.edgeToReferenceNode.get(e);
        if (n != null) {
            return n;
        }
        Preconditions.checkNotNull(e);
        throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", e));
    }

    public final boolean containsEdge(E e) {
        return this.edgeToReferenceNode.containsKey(e);
    }

    public final boolean containsNode(N n) {
        return this.nodeConnections.containsKey(n);
    }

    @Override // com.google.common.graph.Network
    public ElementOrder<E> edgeOrder() {
        return this.edgeOrder;
    }

    @Override // com.google.common.graph.Network
    public Set<E> edges() {
        return this.edgeToReferenceNode.unmodifiableKeySet();
    }

    @Override // com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
    public Set<E> edgesConnecting(N n, N n2) {
        NetworkConnections<N, E> networkConnectionsCheckedConnections = checkedConnections(n);
        if (!this.allowsSelfLoops && n == n2) {
            return ImmutableSet.of();
        }
        Preconditions.checkArgument(containsNode(n2), "Node %s is not an element of this graph.", n2);
        return (Set<E>) nodePairInvalidatableSet(networkConnectionsCheckedConnections.edgesConnecting(n2), n, n2);
    }

    @Override // com.google.common.graph.Network
    public Set<E> inEdges(N n) {
        return (Set<E>) nodeInvalidatableSet(checkedConnections(n).inEdges(), n);
    }

    @Override // com.google.common.graph.Network
    public Set<E> incidentEdges(N n) {
        return (Set<E>) nodeInvalidatableSet(checkedConnections(n).incidentEdges(), n);
    }

    @Override // com.google.common.graph.Network
    public EndpointPair<N> incidentNodes(E e) {
        N nCheckedReferenceNode = checkedReferenceNode(e);
        NetworkConnections<N, E> networkConnections = this.nodeConnections.get(nCheckedReferenceNode);
        Objects.requireNonNull(networkConnections);
        return EndpointPair.of(this, nCheckedReferenceNode, networkConnections.adjacentNode(e));
    }

    @Override // com.google.common.graph.Network
    public boolean isDirected() {
        return this.isDirected;
    }

    @Override // com.google.common.graph.Network
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    @Override // com.google.common.graph.Network
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    @Override // com.google.common.graph.Network
    public Set<E> outEdges(N n) {
        return (Set<E>) nodeInvalidatableSet(checkedConnections(n).outEdges(), n);
    }

    @Override // com.google.common.graph.Network, com.google.common.graph.PredecessorsFunction, com.google.common.graph.Graph
    public Set<N> predecessors(N n) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(n).predecessors(), n);
    }

    @Override // com.google.common.graph.Network, com.google.common.graph.SuccessorsFunction
    public Set<N> successors(N n) {
        return (Set<N>) nodeInvalidatableSet(checkedConnections(n).successors(), n);
    }

    public StandardNetwork(NetworkBuilder<? super N, ? super E> networkBuilder, Map<N, NetworkConnections<N, E>> map, Map<E, N> map2) {
        MapIteratorCache<N, NetworkConnections<N, E>> mapIteratorCache;
        this.isDirected = networkBuilder.directed;
        this.allowsParallelEdges = networkBuilder.allowsParallelEdges;
        this.allowsSelfLoops = networkBuilder.allowsSelfLoops;
        this.nodeOrder = (ElementOrder<N>) networkBuilder.nodeOrder.cast();
        this.edgeOrder = (ElementOrder<E>) networkBuilder.edgeOrder.cast();
        if (map instanceof TreeMap) {
            mapIteratorCache = new MapRetrievalCache<>(map);
        } else {
            mapIteratorCache = new MapIteratorCache<>(map);
        }
        this.nodeConnections = mapIteratorCache;
        this.edgeToReferenceNode = new MapIteratorCache<>(map2);
    }
}
