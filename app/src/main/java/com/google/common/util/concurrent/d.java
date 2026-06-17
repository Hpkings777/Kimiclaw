package com.google.common.util.concurrent;

import com.google.common.util.concurrent.AbstractIdleService;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class d implements Runnable {
    public final /* synthetic */ int a;
    public final /* synthetic */ AbstractIdleService.DelegateService b;

    public /* synthetic */ d(AbstractIdleService.DelegateService delegateService, int i) {
        this.a = i;
        this.b = delegateService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.a) {
            case 0:
                this.b.lambda$doStop$0();
                break;
            default:
                this.b.lambda$doStart$0();
                break;
        }
    }
}
