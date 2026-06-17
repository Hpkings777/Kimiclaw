package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Preconditions;
import java.util.AbstractMap;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class RemovalNotification<K, V> extends AbstractMap.SimpleImmutableEntry<K, V> {

    @J2ktIncompatible
    @GwtIncompatible
    private static final long serialVersionUID = 0;
    private final RemovalCause cause;

    private RemovalNotification(K k, V v, RemovalCause removalCause) {
        super(k, v);
        this.cause = (RemovalCause) Preconditions.checkNotNull(removalCause);
    }

    public static <K, V> RemovalNotification<K, V> create(K k, V v, RemovalCause removalCause) {
        return new RemovalNotification<>(k, v, removalCause);
    }

    public RemovalCause getCause() {
        return this.cause;
    }

    public boolean wasEvicted() {
        return this.cause.wasEvicted();
    }
}
