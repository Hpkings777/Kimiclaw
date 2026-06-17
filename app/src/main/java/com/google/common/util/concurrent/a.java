package com.google.common.util.concurrent;

import com.google.common.base.Supplier;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.AbstractScheduledService;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class a implements Supplier {
    public final /* synthetic */ int a;
    public final /* synthetic */ AbstractService b;

    public /* synthetic */ a(AbstractService abstractService, int i) {
        this.a = i;
        this.b = abstractService;
    }

    @Override // com.google.common.base.Supplier
    public final Object get() {
        switch (this.a) {
            case 0:
                return ((AbstractExecutionThreadService.AnonymousClass1) this.b).lambda$doStart$0();
            default:
                return ((AbstractScheduledService.ServiceDelegate) this.b).lambda$doStart$0();
        }
    }
}
