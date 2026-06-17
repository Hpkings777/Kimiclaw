package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import defpackage.L3;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class Runnables {
    private static final Runnable EMPTY_RUNNABLE = new L3(2);

    private Runnables() {
    }

    public static Runnable doNothing() {
        return EMPTY_RUNNABLE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$0() {
    }
}
