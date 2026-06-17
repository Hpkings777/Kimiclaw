package com.bytedance.apm.impl;

import android.content.Context;
import com.bytedance.services.apm.api.IApmAgent;
import defpackage.AbstractC0539Jv0;
import defpackage.AbstractC4017sd1;
import defpackage.B1;
import defpackage.C1391a31;
import defpackage.C4784y61;
import defpackage.C4992zc;
import defpackage.O81;
import defpackage.Q51;
import defpackage.RunnableC0152Ck;
import defpackage.RunnableC3452ob1;
import defpackage.RunnableC4201tz;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ApmAgentServiceImpl implements IApmAgent {
    @Override // com.bytedance.services.apm.api.IApmAgent
    public void monitorCommonLog(String str, JSONObject jSONObject) {
        C4784y61.i.b(new O81(1, str, AbstractC0539Jv0.z0(jSONObject)));
    }

    @Override // com.bytedance.services.apm.api.IApmAgent
    public void monitorDuration(String str, JSONObject jSONObject, JSONObject jSONObject2) {
        C4784y61.i.b(new RunnableC4201tz(str, jSONObject, AbstractC0539Jv0.z0(jSONObject2), 3));
    }

    @Override // com.bytedance.services.apm.api.IApmAgent
    public void monitorEvent(C1391a31 c1391a31) {
        String str = c1391a31.a;
        C4992zc c4992zc = new C4992zc();
        c4992zc.b = str;
        c4992zc.c = c1391a31.b;
        c4992zc.a = c1391a31.c;
        JSONObject jSONObject = new JSONObject();
        if (jSONObject.isNull("timestamp")) {
            try {
                jSONObject.put("timestamp", System.currentTimeMillis());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        C4784y61.i.b(new B1(c4992zc, 25, jSONObject, false));
    }

    @Override // com.bytedance.services.apm.api.IApmAgent
    public void monitorExceptionLog(String str, JSONObject jSONObject) {
        C4784y61.i.b(new O81(0, str, AbstractC0539Jv0.z0(jSONObject)));
    }

    @Override // com.bytedance.services.apm.api.IApmAgent
    public void monitorLog(String str, JSONObject jSONObject) {
        C4784y61.i.b(new O81(1, str, AbstractC0539Jv0.z0(jSONObject)));
    }

    @Override // com.bytedance.services.apm.api.IApmAgent
    public void monitorStatusAndDuration(String str, int i, JSONObject jSONObject, JSONObject jSONObject2) {
        C4784y61.i.b(new Q51(str, i, jSONObject, AbstractC0539Jv0.z0(jSONObject2)));
    }

    @Override // com.bytedance.services.apm.api.IApmAgent
    public void monitorStatusRate(String str, int i, JSONObject jSONObject) {
    }

    @Override // com.bytedance.services.apm.api.IApmAgent
    public void reportLegacyMonitorLog(Context context, long j, long j2, boolean z) {
        C4784y61.i.d(new RunnableC3452ob1(AbstractC4017sd1.a, j, j2, z));
    }

    @Override // com.bytedance.services.apm.api.IApmAgent
    public void monitorEvent(String str, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3) {
        C4784y61.i.b(new RunnableC0152Ck(2, str, jSONObject, jSONObject2, AbstractC0539Jv0.z0(jSONObject3)));
    }
}
