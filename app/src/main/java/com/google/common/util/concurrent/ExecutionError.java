package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public class ExecutionError extends Error {

    @J2ktIncompatible
    @GwtIncompatible
    private static final long serialVersionUID = 0;

    @Deprecated
    public ExecutionError() {
    }

    @Deprecated
    public ExecutionError(String str) {
        super(str);
    }

    public ExecutionError(String str, Error error) {
        super(str, error);
    }

    public ExecutionError(Error error) {
        super(error);
    }
}
