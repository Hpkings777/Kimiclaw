package com.google.common.util.concurrent;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.JdkFutureAdapters;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class b implements Runnable {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ b(Object obj, int i) {
        this.a = i;
        this.b = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.a) {
            case 0:
                ((AbstractExecutionThreadService.AnonymousClass1) this.b).lambda$doStart$1();
                break;
            case 1:
                ((JdkFutureAdapters.ListenableFutureAdapter) this.b).lambda$addListener$0();
                break;
            default:
                WrappingExecutorService.lambda$wrapTask$0((Callable) this.b);
                break;
        }
    }
}
