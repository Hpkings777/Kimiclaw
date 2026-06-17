package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.lang.Throwable;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class SneakyThrows<T extends Throwable> {
    private SneakyThrows() {
    }

    public static Error sneakyThrow(Throwable th) {
        throw new SneakyThrows().throwIt(th);
    }

    private Error throwIt(Throwable th) throws Throwable {
        throw th;
    }
}
