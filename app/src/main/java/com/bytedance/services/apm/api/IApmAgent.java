package com.bytedance.services.apm.api;

import android.content.Context;
import com.bytedance.news.common.service.manager.IService;
import defpackage.C1391a31;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public interface IApmAgent extends IService {
    void monitorCommonLog(String str, JSONObject jSONObject);

    @Deprecated
    void monitorDuration(String str, JSONObject jSONObject, JSONObject jSONObject2);

    void monitorEvent(C1391a31 c1391a31);

    void monitorEvent(String str, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3);

    void monitorExceptionLog(String str, JSONObject jSONObject);

    @Deprecated
    void monitorLog(String str, JSONObject jSONObject);

    @Deprecated
    void monitorStatusAndDuration(String str, int i, JSONObject jSONObject, JSONObject jSONObject2);

    @Deprecated
    void monitorStatusRate(String str, int i, JSONObject jSONObject);

    void reportLegacyMonitorLog(Context context, long j, long j2, boolean z);
}
