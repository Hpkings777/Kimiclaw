package com.apm.insight.nativecrash;

import android.support.annotation.Keep;
import com.apm.insight.CrashType;
import com.apm.insight.ICrashCallback;
import defpackage.AbstractC0541Jw0;
import defpackage.AbstractC0690Mt0;
import defpackage.AbstractC1208Ws0;
import defpackage.AbstractC1674bt0;
import defpackage.AbstractC1990e71;
import defpackage.AbstractC2550i71;
import defpackage.C3198mn;
import defpackage.C3656q31;
import defpackage.C3935s31;
import defpackage.Da1;
import defpackage.Jb1;
import defpackage.K81;
import defpackage.P91;
import defpackage.Ud1;
import java.io.File;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class NativeCrashCollector {
    public static void a(String str) {
        Iterator it = ((CopyOnWriteArrayList) Ud1.f.d).iterator();
        while (it.hasNext()) {
            try {
                ((ICrashCallback) it.next()).onCrash(CrashType.NATIVE, str, null);
            } catch (Throwable th) {
                int i = AbstractC1990e71.a;
                AbstractC1674bt0.g("NPTH_CATCH", th);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0193 A[DONT_GENERATE, FINALLY_INSNS] */
    /* JADX WARN: Removed duplicated region for block: B:81:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:83:? A[DONT_GENERATE, FINALLY_INSNS, SYNTHETIC] */
    @Keep
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void onNativeCrash(String str) {
        P91 p91;
        String strB;
        P91 p912;
        long jCurrentTimeMillis = System.currentTimeMillis();
        AbstractC1208Ws0.c("[onNativeCrash] enter");
        P91 p913 = null;
        try {
            try {
                K81.b().g();
                File file = new File(AbstractC0541Jw0.f(), Jb1.g());
                File file2 = new File(file, "callback.json");
                C3935s31 c3935s31B = Da1.h().b(CrashType.NATIVE, new C3198mn(file, str, file2, 10));
                JSONObject jSONObject = c3935s31B.a;
                if (jSONObject != null && jSONObject.length() != 0) {
                    long jCurrentTimeMillis2 = System.currentTimeMillis();
                    long j = jCurrentTimeMillis2 - jCurrentTimeMillis;
                    try {
                        jSONObject.put("java_end", jCurrentTimeMillis2);
                        c3935s31B.n("crash_cost", String.valueOf(j));
                        c3935s31B.f("crash_cost", String.valueOf(j / 1000));
                    } catch (Throwable unused) {
                    }
                    File file3 = new File(file2.getAbsolutePath() + ".tmp");
                    AbstractC0690Mt0.r(file3, jSONObject);
                    file3.renameTo(file2);
                }
                try {
                    p912 = new P91(new File(AbstractC0541Jw0.f(), Jb1.g()));
                    try {
                        JSONArray jSONArrayH = AbstractC2550i71.h(p912.e);
                        String strB2 = p912.b();
                        C3656q31 c3656q31A = C3656q31.a();
                        CrashType crashType = CrashType.NATIVE;
                        String str2 = p912.b;
                        c3656q31A.getClass();
                        C3656q31.e(crashType, strB2, str2);
                        C3656q31.a().d(crashType, jCurrentTimeMillis, Jb1.g(), jSONArrayH);
                    } catch (Throwable unused2) {
                        p913 = p912;
                        p912 = p913;
                    }
                } catch (Throwable unused3) {
                }
            } catch (Throwable th) {
                try {
                    int i = AbstractC1990e71.a;
                    AbstractC1674bt0.g("NPTH_CATCH", th);
                    try {
                        p91 = new P91(new File(AbstractC0541Jw0.f(), Jb1.g()));
                        try {
                            JSONArray jSONArrayH2 = AbstractC2550i71.h(p91.e);
                            String strB3 = p91.b();
                            C3656q31 c3656q31A2 = C3656q31.a();
                            CrashType crashType2 = CrashType.NATIVE;
                            String str3 = p91.b;
                            c3656q31A2.getClass();
                            C3656q31.e(crashType2, strB3, str3);
                            C3656q31.a().d(crashType2, jCurrentTimeMillis, Jb1.g(), jSONArrayH2);
                        } catch (Throwable unused4) {
                            p913 = p91;
                            p91 = p913;
                            if (!((CopyOnWriteArrayList) Ud1.f.d).isEmpty()) {
                            }
                        }
                    } catch (Throwable unused5) {
                    }
                    if (!((CopyOnWriteArrayList) Ud1.f.d).isEmpty()) {
                        return;
                    }
                    if (p91 == null) {
                        p91 = new P91(new File(AbstractC0541Jw0.f(), Jb1.g()));
                    }
                    strB = p91.b();
                } finally {
                }
            }
            if (((CopyOnWriteArrayList) Ud1.f.d).isEmpty()) {
                return;
            }
            if (p912 == null) {
                p912 = new P91(new File(AbstractC0541Jw0.f(), Jb1.g()));
            }
            strB = p912.b();
            a(strB);
        } catch (Throwable unused6) {
            a("");
        }
    }
}
