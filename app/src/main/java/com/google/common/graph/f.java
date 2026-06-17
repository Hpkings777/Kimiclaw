package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.graph.DirectedGraphConnections;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class f implements Function {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ f(Object obj, int i) {
        this.a = i;
        this.b = obj;
    }

    @Override // com.google.common.base.Function
    public final Object apply(Object obj) {
        switch (this.a) {
            case 0:
                return DirectedGraphConnections.lambda$incidentEdgeIterator$0(this.b, obj);
            case 1:
                return EndpointPair.ordered(this.b, obj);
            case 2:
                return DirectedGraphConnections.lambda$incidentEdgeIterator$2(this.b, (DirectedGraphConnections.NodeConnection) obj);
            default:
                return EndpointPair.unordered(this.b, obj);
        }
    }
}
