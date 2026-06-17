package com.google.common.graph;

import java.util.AbstractSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
abstract class IncidentEdgeSet<N> extends AbstractSet<EndpointPair<N>> {
    final BaseGraph<N> graph;
    final N node;

    public IncidentEdgeSet(BaseGraph<N> baseGraph, N n) {
        this.graph = baseGraph;
        this.node = n;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        if (!(obj instanceof EndpointPair)) {
            return false;
        }
        EndpointPair endpointPair = (EndpointPair) obj;
        if (this.graph.isDirected()) {
            if (!endpointPair.isOrdered()) {
                return false;
            }
            Object objSource = endpointPair.source();
            Object objTarget = endpointPair.target();
            return (this.node.equals(objSource) && this.graph.successors((Object) this.node).contains(objTarget)) || (this.node.equals(objTarget) && this.graph.predecessors((Object) this.node).contains(objSource));
        }
        if (endpointPair.isOrdered()) {
            return false;
        }
        Set<N> setAdjacentNodes = this.graph.adjacentNodes(this.node);
        Object objNodeU = endpointPair.nodeU();
        Object objNodeV = endpointPair.nodeV();
        return (this.node.equals(objNodeV) && setAdjacentNodes.contains(objNodeU)) || (this.node.equals(objNodeU) && setAdjacentNodes.contains(objNodeV));
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.graph.isDirected() ? (this.graph.inDegree(this.node) + this.graph.outDegree(this.node)) - (this.graph.successors((Object) this.node).contains(this.node) ? 1 : 0) : this.graph.adjacentNodes(this.node).size();
    }
}
