package com.google.common.cache;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.cache.RemovalListeners;
import com.google.common.cache.RemovalNotification;
import defpackage.RunnableC0790Or0;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
@GwtIncompatible
public final class RemovalListeners {
    private RemovalListeners() {
    }

    public static <K, V> RemovalListener<K, V> asynchronous(final RemovalListener<K, V> removalListener, final Executor executor) {
        Preconditions.checkNotNull(removalListener);
        Preconditions.checkNotNull(executor);
        return new RemovalListener() { // from class: dx0
            @Override // com.google.common.cache.RemovalListener
            public final void onRemoval(RemovalNotification removalNotification) {
                RemovalListeners.lambda$asynchronous$0(executor, removalListener, removalNotification);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$asynchronous$0(Executor executor, RemovalListener removalListener, RemovalNotification removalNotification) {
        executor.execute(new RunnableC0790Or0(2, removalListener, removalNotification));
    }
}
