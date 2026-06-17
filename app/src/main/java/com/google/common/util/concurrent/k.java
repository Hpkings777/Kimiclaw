package com.google.common.util.concurrent;

import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Striped;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class k implements Supplier {
    public final /* synthetic */ int a;

    @Override // com.google.common.base.Supplier
    public final Object get() {
        switch (this.a) {
            case 0:
                return new Striped.WeakSafeReadWriteLock();
            default:
                return new Striped.PaddedLock();
        }
    }
}
