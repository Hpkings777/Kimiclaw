package com.bytedance.apm.agent.v2.instrumentation;

import android.app.Application;
import android.support.annotation.Keep;
import android.text.TextUtils;
import defpackage.AbstractC2653iu0;
import defpackage.AbstractC4017sd1;

/* JADX INFO: loaded from: classes.dex */
@Keep
public class AppAgent {
    public static final String ATTACH_BASE_CONTEXT = "attachBaseContext";
    public static final String CONSTRUCT = "<init>";
    public static final String ON_CREATE = "onCreate";
    private static long attachBaseContextEndTime;
    private static long attachBaseContextStartTime;
    private static long constructorEndTime;
    private static long constructorStartTime;
    private static long onCreateEndTime;
    private static long onCreateStartTime;

    @Keep
    public static void onTrace(String str, boolean z) {
        if (TextUtils.equals(str, CONSTRUCT)) {
            if (z) {
                constructorStartTime = System.currentTimeMillis();
                return;
            } else {
                constructorEndTime = System.currentTimeMillis();
                return;
            }
        }
        if (TextUtils.equals(str, ATTACH_BASE_CONTEXT)) {
            if (z) {
                attachBaseContextStartTime = System.currentTimeMillis();
                return;
            } else {
                attachBaseContextEndTime = System.currentTimeMillis();
                return;
            }
        }
        if (TextUtils.equals(str, ON_CREATE)) {
            if (z) {
                onCreateStartTime = System.currentTimeMillis();
                return;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            onCreateEndTime = jCurrentTimeMillis;
            long j = constructorStartTime;
            long j2 = constructorEndTime;
            long j3 = attachBaseContextStartTime;
            long j4 = attachBaseContextEndTime;
            long j5 = onCreateStartTime;
            AbstractC2653iu0.b = j;
            AbstractC2653iu0.c = j2;
            AbstractC2653iu0.d = j3;
            AbstractC2653iu0.e = j4;
            AbstractC2653iu0.f = j5;
            AbstractC2653iu0.g = jCurrentTimeMillis;
            Application application = AbstractC4017sd1.a;
            if (j <= 0) {
                return;
            }
            long j6 = AbstractC4017sd1.k;
            if (j6 == 0 || j < j6) {
                AbstractC4017sd1.k = j;
            }
        }
    }
}
