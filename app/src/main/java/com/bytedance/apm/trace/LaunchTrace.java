package com.bytedance.apm.trace;

import defpackage.AbstractC2653iu0;
import defpackage.AbstractC4017sd1;
import defpackage.C3309na1;
import defpackage.I31;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public final class LaunchTrace {
    public static void customLaunchEnd(boolean z) {
        AbstractC4017sd1.z = z;
    }

    public static void endSpan(String str) {
        ConcurrentHashMap concurrentHashMap = I31.a;
        C3309na1 c3309na1 = (C3309na1) concurrentHashMap.get("null#" + str);
        if (c3309na1 == null) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        String name = Thread.currentThread().getName();
        c3309na1.b = jCurrentTimeMillis;
        c3309na1.c = name;
        concurrentHashMap.put("null#" + str, c3309na1);
    }

    public static void reportLaunchEnd() {
        AbstractC2653iu0.v = System.currentTimeMillis();
        AbstractC2653iu0.e(AbstractC4017sd1.i, true);
    }

    public static void startSpan(String str) {
        ConcurrentHashMap concurrentHashMap = I31.a;
        if (((C3309na1) concurrentHashMap.get("null#" + str)) == null) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            C3309na1 c3309na1 = new C3309na1();
            c3309na1.a = jCurrentTimeMillis;
            concurrentHashMap.put("null#" + str, c3309na1);
        }
    }
}
