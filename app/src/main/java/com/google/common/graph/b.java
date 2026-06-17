package com.google.common.graph;

import com.google.common.base.Supplier;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class b implements Supplier {
    public final /* synthetic */ int a = 1;
    public final /* synthetic */ Object b;
    public final /* synthetic */ Object c;

    public /* synthetic */ b(AbstractBaseGraph abstractBaseGraph, Object obj) {
        this.c = abstractBaseGraph;
        this.b = obj;
    }

    @Override // com.google.common.base.Supplier
    public final Object get() {
        switch (this.a) {
            case 0:
                return AbstractBaseGraph.lambda$nodePairInvalidatableSet$1(this.b, this.c);
            default:
                return ((AbstractBaseGraph) this.c).lambda$nodeInvalidatableSet$0(this.b);
        }
    }

    public /* synthetic */ b(Object obj, Object obj2) {
        this.b = obj;
        this.c = obj2;
    }
}
