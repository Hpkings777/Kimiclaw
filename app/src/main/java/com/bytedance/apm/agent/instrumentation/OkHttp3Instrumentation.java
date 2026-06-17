package com.bytedance.apm.agent.instrumentation;

import defpackage.C0099Bj0;
import defpackage.C0151Cj0;
import defpackage.GY;
import defpackage.HH;
import defpackage.R31;
import defpackage.T31;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: loaded from: classes.dex */
public class OkHttp3Instrumentation {
    /* JADX WARN: Removed duplicated region for block: B:12:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static C0151Cj0 build(C0099Bj0 c0099Bj0) {
        c0099Bj0.getClass();
        ArrayList arrayList = c0099Bj0.c;
        C0151Cj0 c0151Cj0 = new C0151Cj0(c0099Bj0);
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    if (((GY) it.next()) instanceof R31) {
                        break;
                    }
                }
                R31 interceptor = new R31();
                Intrinsics.checkNotNullParameter(interceptor, "interceptor");
                arrayList.add(interceptor);
            } else {
                R31 interceptor2 = new R31();
                Intrinsics.checkNotNullParameter(interceptor2, "interceptor");
                arrayList.add(interceptor2);
            }
        }
        HH hh = c0151Cj0.e;
        if (hh instanceof T31) {
            return new C0151Cj0(c0099Bj0);
        }
        T31 eventListenerFactory = new T31(hh);
        Intrinsics.checkNotNullParameter(eventListenerFactory, "eventListenerFactory");
        c0099Bj0.e = eventListenerFactory;
        return new C0151Cj0(c0099Bj0);
    }

    public static C0151Cj0 init() {
        C0099Bj0 c0099Bj0 = new C0099Bj0();
        R31 interceptor = new R31();
        Intrinsics.checkNotNullParameter(interceptor, "interceptor");
        c0099Bj0.c.add(interceptor);
        T31 eventListenerFactory = new T31(null);
        Intrinsics.checkNotNullParameter(eventListenerFactory, "eventListenerFactory");
        c0099Bj0.e = eventListenerFactory;
        return new C0151Cj0(c0099Bj0);
    }
}
