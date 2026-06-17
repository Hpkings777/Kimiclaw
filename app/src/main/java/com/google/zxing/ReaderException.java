package com.google.zxing;

/* JADX INFO: loaded from: classes.dex */
public abstract class ReaderException extends Exception {
    protected static final StackTraceElement[] NO_TRACE;
    protected static boolean isStackTrace;

    static {
        isStackTrace = System.getProperty("surefire.test.class.path") != null;
        NO_TRACE = new StackTraceElement[0];
    }

    public ReaderException() {
    }

    public static void setStackTrace(boolean z) {
        isStackTrace = z;
    }

    @Override // java.lang.Throwable
    public final synchronized Throwable fillInStackTrace() {
        return null;
    }

    public ReaderException(Throwable th) {
        super(th);
    }
}
