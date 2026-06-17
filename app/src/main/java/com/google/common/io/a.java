package com.google.common.io;

import com.google.common.io.Closer;
import java.io.Closeable;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class a implements Closer.Suppressor {
    @Override // com.google.common.io.Closer.Suppressor
    public final void suppress(Closeable closeable, Throwable th, Throwable th2) {
        Closer.lambda$static$0(closeable, th, th2);
    }
}
