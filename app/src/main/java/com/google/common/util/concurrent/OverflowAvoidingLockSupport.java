package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.util.concurrent.locks.LockSupport;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
final class OverflowAvoidingLockSupport {
    static final long MAX_NANOSECONDS_THRESHOLD = 2147483647999999999L;

    private OverflowAvoidingLockSupport() {
    }

    public static void parkNanos(Object obj, long j) {
        LockSupport.parkNanos(obj, Math.min(j, MAX_NANOSECONDS_THRESHOLD));
    }
}
