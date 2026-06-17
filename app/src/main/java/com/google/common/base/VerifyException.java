package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public class VerifyException extends RuntimeException {
    public VerifyException() {
    }

    public VerifyException(String str) {
        super(str);
    }

    public VerifyException(Throwable th) {
        super(th);
    }

    public VerifyException(String str, Throwable th) {
        super(str, th);
    }
}
