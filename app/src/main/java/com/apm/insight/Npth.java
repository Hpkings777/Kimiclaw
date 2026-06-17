package com.apm.insight;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Keep;
import android.text.TextUtils;
import com.apm.insight.nativecrash.NativeImpl;
import com.apm.insight.runtime.ConfigManager;
import defpackage.AO0;
import defpackage.AbstractC0744Nu0;
import defpackage.AbstractC1208Ws0;
import defpackage.AbstractC2063ee1;
import defpackage.AbstractC4752xu0;
import defpackage.B1;
import defpackage.C2270g71;
import defpackage.C2289gG;
import defpackage.C3988sR;
import defpackage.C4075t31;
import defpackage.C4992zc;
import defpackage.Da1;
import defpackage.Ha1;
import defpackage.I81;
import defpackage.InterfaceC2130f71;
import defpackage.InterfaceC2480hd1;
import defpackage.InterfaceC3178md1;
import defpackage.Jb1;
import defpackage.Kb1;
import defpackage.N91;
import defpackage.RunnableC0530Jr;
import defpackage.RunnableC2953l21;
import defpackage.Ud1;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class Npth {
    private static boolean sInit;

    public static void addAttachLongUserData(AttachUserData attachUserData, CrashType crashType) {
        if (attachUserData != null) {
            Da1 da1 = Jb1.h;
            da1.getClass();
            if (crashType != CrashType.ALL) {
                da1.G(attachUserData, crashType);
                return;
            }
            da1.G(attachUserData, CrashType.LAUNCH);
            da1.G(attachUserData, CrashType.JAVA);
            da1.G(attachUserData, CrashType.CUSTOM_JAVA);
            da1.G(attachUserData, CrashType.NATIVE);
            da1.G(attachUserData, CrashType.ANR);
            da1.G(attachUserData, CrashType.DART);
        }
    }

    public static void addAttachUserData(AttachUserData attachUserData, CrashType crashType) {
        if (attachUserData != null) {
            Jb1.h.m(attachUserData, crashType);
        }
    }

    public static void addTags(Map<? extends String, ? extends String> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        ((HashMap) Jb1.h.d).putAll(map);
    }

    public static void checkInnerNpth(boolean z) {
        boolean z2 = Ud1.a;
        Jb1.v = z;
    }

    public static void disableLogcat() {
        boolean z = Ud1.a;
        Jb1.z = false;
    }

    public static void disableSigQuit() {
    }

    public static void dumpHprof(String str) {
        boolean z = Ud1.a;
        NativeImpl.l(str);
    }

    public static void enableALogCollector(String str, InterfaceC2130f71 interfaceC2130f71, I81 i81) {
        boolean z = Ud1.a;
    }

    public static void enableAnrInfo(boolean z) {
        boolean z2 = Ud1.a;
        Jb1.u = z;
    }

    public static void enableLoopMonitor(boolean z) {
        boolean z2 = Ud1.a;
        Jb1.t = z;
    }

    public static void enableNativeDump(boolean z) {
        boolean z2 = Ud1.a;
        Jb1.w = z;
    }

    public static void enableThreadsBoost() {
        Jb1.o = 1;
    }

    public static ConfigManager getConfigManager() {
        return Jb1.g;
    }

    public static boolean hasCrash() {
        boolean z = Ud1.a;
        return C4075t31.m || NativeImpl.r();
    }

    public static boolean hasCrashWhenJavaCrash() {
        boolean z = Ud1.a;
        Boolean bool = (Boolean) C4075t31.n.get();
        return (bool != null && bool.booleanValue()) || NativeImpl.r();
    }

    public static boolean hasCrashWhenNativeCrash() {
        boolean z = Ud1.a;
        return C4075t31.m;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0058 A[Catch: all -> 0x00a5, TryCatch #1 {, blocks: (B:4:0x0003, B:9:0x000a, B:20:0x004f, B:31:0x0072, B:33:0x0087, B:23:0x0058, B:25:0x005c, B:26:0x0063, B:12:0x0036, B:14:0x003a, B:15:0x0041), top: B:45:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0087 A[Catch: all -> 0x00a5, TRY_LEAVE, TryCatch #1 {, blocks: (B:4:0x0003, B:9:0x000a, B:20:0x004f, B:31:0x0072, B:33:0x0087, B:23:0x0058, B:25:0x005c, B:26:0x0063, B:12:0x0036, B:14:0x003a, B:15:0x0041), top: B:45:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static synchronized void init(Application application, Context context, ICommonParams iCommonParams, boolean z, boolean z2, boolean z3, boolean z4, long j) {
        int iIntValue;
        Object obj;
        int iIntValue2;
        MonitorCrash monitorCrashInit;
        if (sInit) {
            return;
        }
        sInit = true;
        Ud1.a(application, context, z, z2, z3, z4);
        Jb1.d(application, context);
        Jb1.f = new AO0(Jb1.a, iCommonParams, Jb1.a());
        Map mapD = Jb1.a().d();
        Object obj2 = mapD.get("update_version_code");
        if (obj2 != null) {
            if (!(obj2 instanceof Integer)) {
                if (obj2 instanceof String) {
                    try {
                        iIntValue = Integer.parseInt(String.valueOf(obj2));
                    } catch (Throwable unused) {
                        iIntValue = 0;
                    }
                }
            }
            iIntValue = ((Integer) obj2).intValue();
            obj = mapD.get("aid");
            if (obj != null) {
                if (!(obj instanceof Integer)) {
                    if (obj instanceof String) {
                        try {
                            iIntValue2 = Integer.parseInt(String.valueOf(obj));
                        } catch (Throwable unused2) {
                            iIntValue2 = 4444;
                        }
                    }
                }
                iIntValue2 = ((Integer) obj).intValue();
                monitorCrashInit = MonitorCrash.init(context, String.valueOf(iIntValue2), iIntValue, String.valueOf(mapD.get("app_version")));
                if (monitorCrashInit != null) {
                    monitorCrashInit.config().setDeviceId(Jb1.a().g()).setChannel(String.valueOf(mapD.get("channel")));
                }
            }
            iIntValue2 = 4444;
            monitorCrashInit = MonitorCrash.init(context, String.valueOf(iIntValue2), iIntValue, String.valueOf(mapD.get("app_version")));
            if (monitorCrashInit != null) {
            }
        }
        iIntValue = 0;
        obj = mapD.get("aid");
        if (obj != null) {
        }
        iIntValue2 = 4444;
        monitorCrashInit = MonitorCrash.init(context, String.valueOf(iIntValue2), iIntValue, String.valueOf(mapD.get("app_version")));
        if (monitorCrashInit != null) {
        }
    }

    public static synchronized void initMiniApp(Context context, ICommonParams iCommonParams) {
        Jb1.e = true;
        init(context, iCommonParams, true, false, true, true);
    }

    public static boolean isANREnable() {
        return Ud1.c;
    }

    public static boolean isInit() {
        return sInit;
    }

    public static boolean isJavaCrashEnable() {
        return Ud1.b;
    }

    public static boolean isNativeCrashEnable() {
        return Ud1.d;
    }

    public static boolean isRunning() {
        boolean z = Ud1.a;
        return SystemClock.uptimeMillis() - C4992zc.d <= 15000;
    }

    public static boolean isStopUpload() {
        return Ud1.h;
    }

    public static void openANRMonitor() {
        if (Ud1.a) {
            C2270g71 c2270g71 = (C2270g71) Kb1.d(Jb1.a).b;
            if (!c2270g71.c) {
                c2270g71.a = new C4992zc(c2270g71);
                c2270g71.d = Jb1.c;
                c2270g71.c = true;
            }
            Ud1.c = true;
        }
    }

    public static void openJavaCrashMonitor() {
        if (!Ud1.a || Ud1.b) {
            return;
        }
        Context context = Jb1.a;
        C4075t31 c4075t31A = C4075t31.a();
        c4075t31A.b = new C3988sR(context, 2);
        c4075t31A.c = new C2289gG(context, 1);
    }

    public static boolean openNativeCrashMonitor() {
        if (Ud1.a && !Ud1.d) {
            boolean zH = NativeImpl.h(Jb1.a);
            Ud1.d = zH;
            if (!zH) {
                Ud1.e = true;
            }
        }
        return Ud1.d;
    }

    public static void registerCrashCallback(ICrashCallback iCrashCallback, CrashType crashType) {
        Ud1.c(iCrashCallback, crashType);
    }

    public static void registerOOMCallback(IOOMCallback iOOMCallback) {
        ((CopyOnWriteArrayList) Ud1.f.f).add(iOOMCallback);
    }

    public static void registerSdk(int i, String str) {
        if (Jb1.i == null) {
            synchronized (Jb1.class) {
                try {
                    if (Jb1.i == null) {
                        Jb1.i = new ConcurrentHashMap();
                    }
                } finally {
                }
            }
        }
        Jb1.i.put(Integer.valueOf(i), str);
    }

    public static void removeAttachLongUserData(AttachUserData attachUserData, CrashType crashType) {
        if (attachUserData != null) {
            Da1 da1 = Jb1.h;
            da1.getClass();
            if (crashType != CrashType.ALL) {
                da1.K(attachUserData, crashType);
                return;
            }
            da1.K(attachUserData, CrashType.LAUNCH);
            da1.K(attachUserData, CrashType.JAVA);
            da1.K(attachUserData, CrashType.CUSTOM_JAVA);
            da1.K(attachUserData, CrashType.NATIVE);
            da1.K(attachUserData, CrashType.ANR);
            da1.K(attachUserData, CrashType.DART);
        }
    }

    public static void removeAttachUserData(AttachUserData attachUserData, CrashType crashType) {
        if (attachUserData != null) {
            Da1 da1 = Jb1.h;
            da1.getClass();
            if (crashType != CrashType.ALL) {
                da1.J(attachUserData, crashType);
                return;
            }
            da1.J(attachUserData, CrashType.LAUNCH);
            da1.J(attachUserData, CrashType.JAVA);
            da1.J(attachUserData, CrashType.CUSTOM_JAVA);
            da1.J(attachUserData, CrashType.NATIVE);
            da1.J(attachUserData, CrashType.ANR);
            da1.J(attachUserData, CrashType.DART);
        }
    }

    @Keep
    public static void reportDartError(String str) {
        AbstractC1208Ws0.c("reportDartError " + str);
        boolean z = Ud1.a;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        AbstractC4752xu0.c(str, null, null, null, null);
    }

    @Deprecated
    public static void reportError(String str) {
        boolean z = Ud1.a;
        if (Jb1.g.isReportErrorEnable()) {
            C4075t31 c4075t31 = C4075t31.l;
            if (str == null) {
                return;
            }
            try {
                AbstractC2063ee1.A().a(new RunnableC0530Jr(str, 1));
            } catch (Throwable unused) {
            }
        }
    }

    public static void setAlogFlushAddr(long j) {
        boolean z = Ud1.a;
    }

    public static void setAlogFlushV2Addr(long j) {
        boolean z = Ud1.a;
        NativeImpl.k(j);
    }

    public static void setAlogLogDirAddr(long j) {
        boolean z = Ud1.a;
        NativeImpl.p(j);
    }

    public static void setAlogWriteAddr(long j) {
    }

    public static void setAnrInfoFileObserver(String str, Ha1 ha1) {
        boolean z = Ud1.a;
        AbstractC2063ee1.A().a(new B1(str, 26, ha1, false));
    }

    public static void setApplication(Application application) {
        if (application != null) {
            Jb1.b = application;
        } else {
            Context context = Jb1.a;
        }
    }

    @Deprecated
    public static void setAttachUserData(AttachUserData attachUserData, CrashType crashType) {
        if (attachUserData != null) {
            Jb1.h.m(attachUserData, crashType);
        }
    }

    public static void setBusiness(String str) {
        if (str != null) {
            Jb1.d = str;
        }
    }

    public static void setCrashFilter(ICrashFilter iCrashFilter) {
        Jb1.h.e = iCrashFilter;
    }

    public static void setCrashWaitTime(long j) {
    }

    public static void setCurProcessName(String str) {
        AbstractC0744Nu0.b = str;
    }

    public static void setEncryptImpl(N91 n91) {
        boolean z = Ud1.a;
        Jb1.g.setEncryptImpl(n91);
    }

    public static void setLogcatImpl(InterfaceC2480hd1 interfaceC2480hd1) {
        boolean z = Ud1.a;
    }

    public static void setOriginSignalResend(boolean z) {
    }

    public static void setRequestIntercept(InterfaceC3178md1 interfaceC3178md1) {
        boolean z = Ud1.a;
    }

    public static void stopAnr() {
        if (Ud1.a) {
            C2270g71 c2270g71 = (C2270g71) Kb1.d(Jb1.a).b;
            if (c2270g71.c) {
                c2270g71.c = false;
                C4992zc c4992zc = c2270g71.a;
                if (c4992zc != null) {
                    c4992zc.a = true;
                }
                c2270g71.a = null;
            }
            Ud1.c = false;
        }
    }

    public static void stopUpload() {
        Ud1.h = true;
    }

    public static void unregisterCrashCallback(ICrashCallback iCrashCallback, CrashType crashType) {
        Ud1.d(iCrashCallback, crashType);
    }

    public static void unregisterOOMCallback(IOOMCallback iOOMCallback, CrashType crashType) {
        ((CopyOnWriteArrayList) Ud1.f.f).remove(iOOMCallback);
    }

    public static synchronized void initMiniApp(Context context, ICommonParams iCommonParams, int i, String str) {
        Jb1.e = true;
        Jb1.m = i;
        Jb1.n = str;
        init(context, iCommonParams, true, true, true, true);
    }

    @Keep
    public static void reportDartError(String str, Map<? extends String, ? extends String> map, Map<String, String> map2, IUploadCallback iUploadCallback) {
        AbstractC1208Ws0.c("reportDartError " + str);
        boolean z = Ud1.a;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        AbstractC4752xu0.c(str, map, map2, null, iUploadCallback);
    }

    @Deprecated
    public static void reportError(Throwable th) {
        boolean z = Ud1.a;
        if (Jb1.g.isReportErrorEnable()) {
            C4075t31 c4075t31 = C4075t31.l;
            if (th == null) {
                return;
            }
            try {
                AbstractC2063ee1.A().a(new RunnableC2953l21(th, 0));
            } catch (Throwable unused) {
            }
        }
    }

    @Keep
    public static void reportDartError(String str, Map<? extends String, ? extends String> map, Map<String, String> map2, Map<String, String> map3, IUploadCallback iUploadCallback) {
        AbstractC1208Ws0.c("reportDartError " + str);
        boolean z = Ud1.a;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        AbstractC4752xu0.c(str, map, map2, map3, iUploadCallback);
    }

    public static synchronized void init(Context context, ICommonParams iCommonParams) {
        init(context, iCommonParams, true, false, false);
    }

    public static synchronized void init(Context context, ICommonParams iCommonParams, boolean z, boolean z2, boolean z3) {
        init(context, iCommonParams, z, z, z2, z3);
    }

    public static synchronized void init(Context context, ICommonParams iCommonParams, boolean z, boolean z2, boolean z3, boolean z4) {
        init(context, iCommonParams, z, z2, z3, z4, 0L);
    }

    public static synchronized void init(Context context, ICommonParams iCommonParams, boolean z, boolean z2, boolean z3, boolean z4, long j) {
        Application application = Jb1.b;
        if (application == null) {
            if (context instanceof Application) {
                application = (Application) context;
                if (application.getBaseContext() == null) {
                    throw new IllegalArgumentException("初始化时传入的Application还未attach, 请在init时传入attachBaseContext的参数, 并在init之前手动调用Npth.setApplication(Application).");
                }
            } else {
                application = (Application) context.getApplicationContext();
                if (application == null) {
                    throw new IllegalArgumentException("初始化时传入了baseContext, 导致无法获取Application实例, 请在init之前手动调用Npth.setApplication(Application).");
                }
                if (application.getBaseContext() != null) {
                    context = application.getBaseContext();
                }
            }
        }
        init(application, context, iCommonParams, z, z2, z3, z4, j);
    }
}
