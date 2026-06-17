package com.google.common.graph;

import com.google.common.base.Function;
import com.google.common.graph.AbstractNetwork;
import com.google.common.graph.Graphs;
import java.util.AbstractSet;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class e implements Function {
    public final /* synthetic */ int a;
    public final /* synthetic */ AbstractSet b;

    public /* synthetic */ e(AbstractSet abstractSet, int i) {
        this.a = i;
        this.b = abstractSet;
    }

    @Override // com.google.common.base.Function
    public final Object apply(Object obj) {
        switch (this.a) {
            case 0:
                return ((AbstractNetwork.AnonymousClass1.C00241) this.b).lambda$iterator$0(obj);
            default:
                return ((Graphs.TransposedGraph.AnonymousClass1) this.b).lambda$iterator$0((EndpointPair) obj);
        }
    }
}
