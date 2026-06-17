package com.apmplus.hybrid.webview;

import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import defpackage.AbstractC1820cw0;
import defpackage.AbstractC4017sd1;
import defpackage.AbstractC4671xI0;
import defpackage.M31;
import defpackage.V51;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@Keep
public class HybridMonitorJsBridge {
    public HybridMonitorJsBridge(WebView webView) {
    }

    @JavascriptInterface
    public int getAid() {
        try {
            return Integer.parseInt(AbstractC4017sd1.a());
        } catch (Exception unused) {
            return 0;
        }
    }

    @JavascriptInterface
    public void request(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONArray jSONArrayOptJSONArray = new JSONObject(str).optJSONArray("list");
                if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() > 0) {
                    for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                        JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
                        if (jSONObjectOptJSONObject != null) {
                            M31.g().c(new V51(0, "hybrid_v2", jSONObjectOptJSONObject));
                        }
                    }
                }
            } catch (Throwable unused) {
            }
        }
        if (AbstractC4017sd1.b) {
            Log.d("ApmInsight", AbstractC1820cw0.b(new String[]{"Receive:HybridData_V2"}));
            if (TextUtils.isEmpty(str)) {
                return;
            }
            Log.d("ApmInsight", AbstractC1820cw0.b(new String[]{AbstractC4671xI0.y("Receive:", str)}));
        }
    }
}
