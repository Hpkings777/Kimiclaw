package com.apm.insight;

import android.content.Context;
import android.text.TextUtils;
import defpackage.C1913db;
import defpackage.Jb1;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class b implements ICommonParams {
    public final /* synthetic */ c a;
    public final /* synthetic */ MonitorCrash b;

    public b(c cVar, MonitorCrash monitorCrash) {
        this.a = cVar;
        this.b = monitorCrash;
    }

    @Override // com.apm.insight.ICommonParams
    public final Map getCommonParams() {
        JSONObject jSONObjectD = this.a.d(false);
        HashMap map = new HashMap();
        Iterator<String> itKeys = jSONObjectD.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            map.put(next, jSONObjectD.opt(next));
        }
        return map;
    }

    @Override // com.apm.insight.ICommonParams
    public final String getDeviceId() {
        String deviceId = this.b.mConfig.getDeviceId();
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }
        Context context = Jb1.a;
        try {
            C1913db c1913dbC = C1913db.c(Jb1.a().i());
            return c1913dbC != null ? c1913dbC.b() : "";
        } catch (Throwable unused) {
            return "";
        }
    }

    @Override // com.apm.insight.ICommonParams
    public final List getPatchInfo() {
        return null;
    }

    @Override // com.apm.insight.ICommonParams
    public final Map getPluginInfo() {
        return null;
    }

    @Override // com.apm.insight.ICommonParams
    public final String getSessionId() {
        return null;
    }

    @Override // com.apm.insight.ICommonParams
    public final long getUserId() {
        return 0L;
    }
}
