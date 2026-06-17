package com.google.common.graph;

import com.google.common.annotations.Beta;

/* JADX INFO: loaded from: classes.dex */
@Beta
public interface MutableValueGraph<N, V> extends ValueGraph<N, V> {
    boolean addNode(N n);

    V putEdgeValue(EndpointPair<N> endpointPair, V v);

    V putEdgeValue(N n, N n2, V v);

    V removeEdge(EndpointPair<N> endpointPair);

    V removeEdge(N n, N n2);

    boolean removeNode(N n);
}
