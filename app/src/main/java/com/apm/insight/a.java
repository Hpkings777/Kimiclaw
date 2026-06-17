package com.apm.insight;

import android.content.Context;
import android.text.TextUtils;
import com.apm.insight.MonitorCrash;
import defpackage.AbstractC2550i71;
import defpackage.AbstractC4671xI0;
import defpackage.C1913db;
import defpackage.C2603iX;
import defpackage.C3198mn;
import defpackage.Ia1;
import defpackage.Ke1;
import defpackage.Pd1;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class a implements Runnable {
    public final /* synthetic */ boolean a;
    public final /* synthetic */ Context b;
    public final /* synthetic */ MonitorCrash c;

    public a(MonitorCrash monitorCrash, boolean z, Context context) {
        this.c = monitorCrash;
        this.a = z;
        this.b = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.c.isAppLogInit) {
            return;
        }
        if (!Pd1.c) {
            Pd1.b();
        }
        Ia1 ia1 = (Ia1) Ia1.c.get(this.c.mConfig.a);
        if (ia1 == null || ia1.f()) {
            this.c.isAppLogInit = true;
            if (MonitorCrash.sDefaultApplogUrl != null) {
                Ke1 ke1 = new Ke1(18, false);
                ke1.b = AbstractC4671xI0.p(new StringBuilder(), MonitorCrash.sDefaultApplogUrl, "/apm/device_register");
                ke1.c = new String[]{AbstractC4671xI0.p(new StringBuilder(), MonitorCrash.sDefaultApplogUrl, "/monitor/collect/c/session")};
                this.c.mApmApplogConfig.f = new C3198mn(ke1);
            }
            if (this.a) {
                C2603iX c2603iX = this.c.mApmApplogConfig;
                MonitorCrash monitorCrash = this.c;
                c2603iX.i = (int) monitorCrash.mConfig.d;
                C2603iX c2603iX2 = monitorCrash.mApmApplogConfig;
                MonitorCrash monitorCrash2 = this.c;
                c2603iX2.h = (int) monitorCrash2.mConfig.d;
                C2603iX c2603iX3 = monitorCrash2.mApmApplogConfig;
                MonitorCrash monitorCrash3 = this.c;
                c2603iX3.j = monitorCrash3.mConfig.e;
                C2603iX c2603iX4 = monitorCrash3.mApmApplogConfig;
                String str = this.c.mConfig.e;
                c2603iX4.getClass();
                this.c.mApmApplogConfig.g = this.c.mConfig.e;
            } else {
                String strF = AbstractC2550i71.f(c.b);
                HashMap map = new HashMap();
                map.put("host_app_id", strF);
                map.put("sdk_version", this.c.mConfig.e);
                this.c.mApmApplogConfig.k = map;
            }
            if (!TextUtils.isEmpty(this.c.mConfig.getDeviceId())) {
                this.c.mApmApplogConfig.l = this.c.mConfig.getDeviceId();
            }
            if (!TextUtils.isEmpty(this.c.mConfig.c)) {
                this.c.mApmApplogConfig.c = this.c.mConfig.c;
            }
            C2603iX c2603iX5 = this.c.mApmApplogConfig;
            MonitorCrash monitorCrash4 = this.c;
            c2603iX5.m = monitorCrash4.mConfig.r;
            C2603iX c2603iX6 = monitorCrash4.mApmApplogConfig;
            MonitorCrash monitorCrash5 = this.c;
            MonitorCrash.Config config = monitorCrash5.mConfig;
            c2603iX6.n = config.t;
            if (config.p == null) {
                C1913db.d(this.b, monitorCrash5.mApmApplogConfig, null);
            } else {
                C1913db.d(this.b, monitorCrash5.mApmApplogConfig, this.c.mConfig.p);
            }
        }
    }
}
