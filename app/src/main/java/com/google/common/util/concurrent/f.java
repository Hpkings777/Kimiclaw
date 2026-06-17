package com.google.common.util.concurrent;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class f implements Runnable {
    public final /* synthetic */ int a = 0;
    public final /* synthetic */ int b;
    public final /* synthetic */ Object c;
    public final /* synthetic */ Object d;

    public /* synthetic */ f(AggregateFuture aggregateFuture, int i, ListenableFuture listenableFuture) {
        this.c = aggregateFuture;
        this.b = i;
        this.d = listenableFuture;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.a) {
            case 0:
                ((AggregateFuture) this.c).lambda$init$0(this.b, (ListenableFuture) this.d);
                break;
            default:
                Futures.InCompletionOrderState.access$300((Futures.InCompletionOrderState) this.c, (ImmutableList) this.d, this.b);
                break;
        }
    }

    public /* synthetic */ f(Futures.InCompletionOrderState inCompletionOrderState, ImmutableList immutableList, int i) {
        this.c = inCompletionOrderState;
        this.d = immutableList;
        this.b = i;
    }
}
