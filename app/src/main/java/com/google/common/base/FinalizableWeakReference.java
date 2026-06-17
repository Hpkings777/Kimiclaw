package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public abstract class FinalizableWeakReference<T> extends WeakReference<T> implements FinalizableReference {
    public FinalizableWeakReference(T t, FinalizableReferenceQueue finalizableReferenceQueue) {
        super(t, finalizableReferenceQueue.queue);
        finalizableReferenceQueue.cleanUp();
    }
}
