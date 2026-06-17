package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.time.Duration;
import kotlin.jvm.internal.LongCompanionObject;

/* JADX INFO: loaded from: classes.dex */
@IgnoreJRERequirement
@J2ktIncompatible
@GwtIncompatible
final class Internal {
    private Internal() {
    }

    public static long toNanosSaturated(Duration duration) {
        try {
            return duration.toNanos();
        } catch (ArithmeticException unused) {
            if (duration.isNegative()) {
                return Long.MIN_VALUE;
            }
            return LongCompanionObject.MAX_VALUE;
        }
    }
}
