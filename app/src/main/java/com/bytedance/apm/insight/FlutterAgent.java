package com.bytedance.apm.insight;

import com.bytedance.apm.config.SlardarConfigManagerImpl;
import com.bytedance.services.slardar.config.IConfigManager;
import defpackage.AbstractC1587bE0;
import defpackage.B9;
import defpackage.C4784y61;
import defpackage.C9;
import defpackage.RunnableC1568b61;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class FlutterAgent {

    public interface IFlutterConfigListener {
        void onReady();

        void onRefresh(JSONObject jSONObject, boolean z);
    }

    public static JSONObject getFlutterConfig() {
        SlardarConfigManagerImpl slardarConfigManagerImpl;
        C9 c9 = B9.a;
        if (!c9.e || (slardarConfigManagerImpl = c9.d) == null) {
            return null;
        }
        return slardarConfigManagerImpl.getConfigJSON("dart_module");
    }

    public static void monitorFlutter(JSONObject jSONObject) {
        C4784y61.i.b(new RunnableC1568b61(jSONObject, 3));
    }

    public static void registerConfigListener(IFlutterConfigListener iFlutterConfigListener) {
        B9.a.b();
        ((IConfigManager) AbstractC1587bE0.a(IConfigManager.class)).registerConfigListener(new a(iFlutterConfigListener));
    }
}
