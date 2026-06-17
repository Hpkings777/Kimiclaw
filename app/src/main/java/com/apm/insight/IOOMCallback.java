package com.apm.insight;

/* JADX INFO: loaded from: classes.dex */
public interface IOOMCallback {
    void onCrash(CrashType crashType, Throwable th, Thread thread, long j);
}
