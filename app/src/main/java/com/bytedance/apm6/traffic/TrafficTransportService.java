package com.bytedance.apm6.traffic;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import defpackage.GS0;

/* JADX INFO: loaded from: classes.dex */
public class TrafficTransportService extends Service {
    public static final /* synthetic */ int b = 0;
    public final GS0 a;

    public TrafficTransportService() {
        GS0 gs0 = new GS0();
        gs0.attachInterface(gs0, "com.bytedance.apm6.traffic.ITrafficTransportInterface");
        this.a = gs0;
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        GS0 gs0 = this.a;
        gs0.getClass();
        return gs0;
    }
}
