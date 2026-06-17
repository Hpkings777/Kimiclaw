package com.bytedance.apm.insight;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.apm.insight.log.VLog;
import com.apm.insight.log.VLogConfig;
import com.bytedance.apm.core.ActivityLifeObserver;
import com.bytedance.mira.plugin.hook.flipped.compat.FlippedV2Impl;
import com.bytedance.services.apm.api.IHttpService;
import defpackage.AbstractC0539Jv0;
import defpackage.AbstractC1542ax0;
import defpackage.AbstractC1802co;
import defpackage.AbstractC1820cw0;
import defpackage.AbstractC2653iu0;
import defpackage.AbstractC2834k91;
import defpackage.AbstractC3394o91;
import defpackage.AbstractC4017sd1;
import defpackage.AbstractC4790y81;
import defpackage.B9;
import defpackage.C0708Nc0;
import defpackage.C0715Ng;
import defpackage.C1049Tr;
import defpackage.C1446aS;
import defpackage.C1476ae1;
import defpackage.C1913db;
import defpackage.C1921dd1;
import defpackage.C2623ie1;
import defpackage.C2825k61;
import defpackage.C3090m11;
import defpackage.C3169ma1;
import defpackage.C3379o41;
import defpackage.C3519p41;
import defpackage.C3880re1;
import defpackage.C41;
import defpackage.C4148ta1;
import defpackage.C4650x81;
import defpackage.C4778y41;
import defpackage.C4784y61;
import defpackage.C4854yc1;
import defpackage.C4924z61;
import defpackage.C4933z91;
import defpackage.C4985zZ0;
import defpackage.C4992zc;
import defpackage.C81;
import defpackage.C9;
import defpackage.D9;
import defpackage.E91;
import defpackage.GY0;
import defpackage.I41;
import defpackage.J91;
import defpackage.K21;
import defpackage.Ke1;
import defpackage.L31;
import defpackage.M41;
import defpackage.N41;
import defpackage.O61;
import defpackage.P41;
import defpackage.Q41;
import defpackage.Te1;
import defpackage.U81;
import defpackage.Z71;
import defpackage.Za1;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class ApmInsight {
    public static final ApmInsight a = new ApmInsight();
    public static boolean b = false;
    public static boolean c = false;
    public static String sPackage = "com.bytedance";
    public boolean d = false;
    public Context e;

    public static ApmInsight getInstance() {
        return a;
    }

    public void closeFlipped(boolean z) {
        c = z;
    }

    public void init(Application application) {
        if (application == null) {
            throw new NullPointerException("application can not be null!");
        }
        this.e = application;
        ActivityLifeObserver.init(application);
        try {
            if (c) {
                return;
            }
            int i = Build.VERSION.SDK_INT;
            ((i >= 30 || (i == 29 && Build.VERSION.PREVIEW_SDK_INT > 0)) ? new FlippedV2Impl() : (i >= 28 || (i == 27 && Build.VERSION.PREVIEW_SDK_INT > 0)) ? new C4985zZ0(4) : new GY0()).invokeHiddenApiRestrictions();
        } catch (Throwable unused) {
        }
    }

    public void start(ApmInsightInitConfig apmInsightInitConfig) {
        init(this.e, apmInsightInitConfig);
    }

    public void init(Context context, ApmInsightInitConfig apmInsightInitConfig) {
        int i = 5;
        int i2 = 4;
        int i3 = 3;
        int i4 = 2;
        int i5 = 1;
        if (context == null) {
            throw new NullPointerException("Please call the init method first!");
        }
        if (apmInsightInitConfig != null) {
            if (TextUtils.isEmpty(apmInsightInitConfig.getToken())) {
                Log.e("ApmInsight", "Token can not be null!!");
            }
            C0715Ng c0715NgD = C0715Ng.d();
            c0715NgD.b = apmInsightInitConfig;
            c0715NgD.a = true;
            boolean zEnableAPMPlusLocalLog = apmInsightInitConfig.enableAPMPlusLocalLog();
            C3379o41 c3379o41 = C3379o41.c;
            if (zEnableAPMPlusLocalLog) {
                VLog.createInstance(new VLogConfig.Builder(context).setLogDirPath(context.getFilesDir() + "/Vlog/APMPlus").setMaxDirSize(10485760).setSubProcessMaxDirSizeRatio(0.1f).setLogFileExpDays(14).build(), "APMPlus");
            }
            C3379o41.c.a = zEnableAPMPlusLocalLog;
            C1049Tr c1049Tr = new C1049Tr();
            c1049Tr.a = apmInsightInitConfig.isWithFpsMonitor();
            long maxLaunchTime = apmInsightInitConfig.getMaxLaunchTime();
            J91 j91 = new J91();
            j91.a = maxLaunchTime;
            c1049Tr.d = j91;
            c1049Tr.b = apmInsightInitConfig.isDebug();
            if (apmInsightInitConfig.getActivityLeakListener() != null) {
                I41 i41 = new I41(apmInsightInitConfig, i4);
                Za1 za1 = new Za1();
                za1.a = i41;
                c1049Tr.c = za1;
            }
            C4992zc c4992zc = new C4992zc(c1049Tr);
            C9 c9 = B9.a;
            if (!c9.f) {
                c9.f = true;
                int i6 = C0708Nc0.b;
                AbstractC4017sd1.f();
                AbstractC4017sd1.j = true;
                c9.a = c4992zc;
                AtomicInteger atomicInteger = L31.d;
                Application application = context instanceof Application ? (Application) context : (Application) context.getApplicationContext();
                if (application != null) {
                    AbstractC4017sd1.a = application;
                }
                AbstractC4017sd1.p = "1.5.7";
                ActivityLifeObserver.init(application);
                c9.b();
                AbstractC4017sd1.n = null;
                boolean zH = AbstractC4017sd1.h();
                c9.h = zH;
                if (zH) {
                    Za1 za12 = (Za1) c9.a.b;
                    C4148ta1 c4148ta1 = C4148ta1.g;
                    if (application != null && za12 != null && !C4148ta1.i) {
                        C4148ta1.i = true;
                        C4148ta1 c4148ta12 = C4148ta1.g;
                        c4148ta12.d = za12;
                        c4148ta12.e = 60000L;
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        c4148ta12.a = new Handler(Looper.getMainLooper());
                        c4148ta12.b = new ReferenceQueue();
                        c4148ta12.c = new CopyOnWriteArraySet();
                        application.registerActivityLifecycleCallbacks(new C3519p41());
                        if (AbstractC4017sd1.b) {
                            Log.i("ApmInsight:ActivityLeakTask", AbstractC1820cw0.b(new String[]{"initActivityLeakCheck done, cost: " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms."}));
                        }
                    }
                    AbstractC2834k91.c = 20000L;
                    AbstractC4017sd1.l = System.currentTimeMillis();
                    boolean z = c4992zc.a;
                    C3169ma1 c3169ma1 = C3169ma1.p;
                    if (!c3169ma1.o) {
                        c3169ma1.d = z;
                        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                            ActivityLifeObserver.getInstance().register(c3169ma1);
                            AbstractC3394o91.a();
                            AbstractC3394o91.d = new Z71(c3169ma1);
                            c3169ma1.o = true;
                        } else {
                            throw new AssertionError("must be init in main thread!");
                        }
                    }
                    C2825k61 c2825k61 = new C2825k61();
                    c2825k61.b = new ArrayList();
                    c2825k61.c = new HashMap();
                    c3169ma1.b(c2825k61);
                    synchronized (AbstractC1542ax0.c) {
                    }
                    AbstractC2653iu0.z = ((J91) c4992zc.c).a;
                }
                if (AbstractC4017sd1.b) {
                    if (c9.h) {
                        K21.a.q("APM_INIT", null);
                    } else {
                        K21.a.q("APM_INIT_OTHER_PROCESS", null);
                    }
                }
                C41.a = "ApmSender";
                C1446aS.s = true;
                C1446aS.t = C1913db.n;
                C1446aS.u = C1913db.q;
                C1446aS.v = C1913db.p;
                C3090m11 c3090m11 = new C3090m11(7);
                synchronized (AbstractC1802co.class) {
                    try {
                        if (!AbstractC1802co.a) {
                            AbstractC1802co.a = true;
                            C1446aS.d = c3090m11;
                            C1446aS.c = context instanceof Application ? (Application) context : (Application) context.getApplicationContext();
                            C1446aS.m = System.currentTimeMillis();
                            C1446aS.n = System.currentTimeMillis();
                            AbstractC0539Jv0.e = new C4650x81();
                            C4778y41 c4778y41 = new C4778y41(c3090m11, i4);
                            ConcurrentHashMap concurrentHashMap = AbstractC4790y81.b;
                            concurrentHashMap.put(IHttpService.class, c4778y41);
                            concurrentHashMap.put(Te1.class, new C4924z61(c3090m11, i5));
                            concurrentHashMap.put(C4854yc1.class, new C4924z61(i4));
                            concurrentHashMap.put(C1921dd1.class, new C4924z61(i3));
                            concurrentHashMap.put(Ke1.class, new C4924z61(c3090m11, i2));
                            concurrentHashMap.put(P41.class, new C4924z61(c3090m11, i));
                            concurrentHashMap.put(N41.class, new C4924z61(6));
                            concurrentHashMap.put(U81.class, new C4778y41(c3090m11, i3));
                            concurrentHashMap.put(C1476ae1.class, new C4778y41(c3090m11, i2));
                            new N41();
                            int i7 = 0;
                            concurrentHashMap.put(C3880re1.class, new C4778y41(c3090m11, i7));
                            concurrentHashMap.put(M41.class, new C4924z61(i7));
                            concurrentHashMap.put(C2623ie1.class, new C4778y41(c3090m11, i5));
                            Q41.a().c();
                            O61.a(C81.b).c(new C4933z91(0L));
                            E91 e91 = E91.f;
                            C3090m11 c3090m112 = new C3090m11(i);
                            synchronized (e91) {
                                e91.b = c3090m112;
                            }
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            sPackage = "com.bytedance";
            IDynamicParams dynamicParams = apmInsightInitConfig.getDynamicParams();
            AbstractC4017sd1.s = apmInsightInitConfig.getExternalTraceId();
            AbstractC4017sd1.u = apmInsightInitConfig.enableTrace();
            AbstractC4017sd1.w = apmInsightInitConfig.getToken();
            AbstractC4017sd1.v = apmInsightInitConfig.enableOperateMonitor();
            C4784y61.i.c(new D9(this, dynamicParams, apmInsightInitConfig, context), 2000L);
            return;
        }
        throw new NullPointerException("ApmInsightInitConfig can not be null!");
    }
}
