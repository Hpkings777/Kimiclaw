package com.google.common.util.concurrent;

import com.google.common.util.concurrent.AbstractScheduledService;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class e implements Runnable {
    public final /* synthetic */ int a;
    public final /* synthetic */ AbstractScheduledService.ServiceDelegate b;

    public /* synthetic */ e(AbstractScheduledService.ServiceDelegate serviceDelegate, int i) {
        this.a = i;
        this.b = serviceDelegate;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.a) {
            case 0:
                this.b.lambda$doStart$1();
                break;
            default:
                this.b.lambda$doStop$0();
                break;
        }
    }
}
