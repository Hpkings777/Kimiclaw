package com.bytedance.apm.insight;

import com.bytedance.apm.insight.FlutterAgent;
import defpackage.InterfaceC1559b31;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a implements InterfaceC1559b31 {
    public final /* synthetic */ FlutterAgent.IFlutterConfigListener a;

    public a(FlutterAgent.IFlutterConfigListener iFlutterConfigListener) {
        this.a = iFlutterConfigListener;
    }

    @Override // defpackage.InterfaceC1559b31
    public final void onReady() {
        FlutterAgent.IFlutterConfigListener iFlutterConfigListener = this.a;
        if (iFlutterConfigListener != null) {
            iFlutterConfigListener.onReady();
        }
    }

    @Override // defpackage.InterfaceC1559b31
    public final void onRefresh(JSONObject jSONObject, boolean z) {
        JSONObject jSONObject2;
        FlutterAgent.IFlutterConfigListener iFlutterConfigListener = this.a;
        if (iFlutterConfigListener != null) {
            if (jSONObject != null) {
                try {
                    jSONObject2 = jSONObject.getJSONObject("dart_module");
                } catch (JSONException e) {
                    e.printStackTrace();
                    jSONObject2 = null;
                }
            } else {
                jSONObject2 = null;
            }
            iFlutterConfigListener.onRefresh(jSONObject2, z);
        }
    }
}
