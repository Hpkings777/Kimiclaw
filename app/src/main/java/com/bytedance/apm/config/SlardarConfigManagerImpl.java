package com.bytedance.apm.config;

import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import com.bytedance.services.slardar.config.IConfigManager;
import defpackage.AbstractC0539Jv0;
import defpackage.AbstractC1406a81;
import defpackage.AbstractC4017sd1;
import defpackage.C1462aa;
import defpackage.C4574wc1;
import defpackage.C4784y61;
import defpackage.InterfaceC1559b31;
import defpackage.O71;
import defpackage.W91;
import defpackage.Y61;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SlardarConfigManagerImpl implements IConfigManager {
    private C4574wc1 mSlardarConfigFetcher;

    public SlardarConfigManagerImpl() {
        C4574wc1 c4574wc1 = new C4574wc1();
        c4574wc1.b = false;
        c4574wc1.f = AbstractC1406a81.b;
        c4574wc1.g = 1200L;
        c4574wc1.h = 1;
        c4574wc1.m = -1L;
        c4574wc1.n = 15000L;
        c4574wc1.o = -1L;
        c4574wc1.p = false;
        this.mSlardarConfigFetcher = c4574wc1;
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public void fetchConfig() {
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        boolean zF = c4574wc1.f();
        if (AbstractC4017sd1.h()) {
            if (c4574wc1.m > System.currentTimeMillis()) {
                zF = true;
            }
            c4574wc1.c(zF);
        }
    }

    public void forceUpdateFromRemote(O71 o71, List<String> list) {
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        if (c4574wc1.i == null) {
            c4574wc1.i = W91.a(AbstractC4017sd1.a, "monitor_config");
        }
        if (o71 != null) {
            c4574wc1.j = o71;
        }
        if (!AbstractC0539Jv0.W(list)) {
            c4574wc1.f = new ArrayList(list);
        }
        c4574wc1.c(true);
    }

    public JSONObject getConfig() {
        return this.mSlardarConfigFetcher.k;
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public int getConfigInt(String str, int i) {
        JSONObject jSONObject;
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        c4574wc1.getClass();
        return (TextUtils.isEmpty(str) || (jSONObject = c4574wc1.k) == null) ? i : jSONObject.optInt(str, i);
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public JSONObject getConfigJSON(String str) {
        JSONObject jSONObject;
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        c4574wc1.getClass();
        return (TextUtils.isEmpty(str) || (jSONObject = c4574wc1.k) == null) ? new JSONObject() : jSONObject.optJSONObject(str);
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public boolean getLogTypeSwitch(String str) {
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        c4574wc1.getClass();
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (TextUtils.equals(str, "block_monitor")) {
            str = "caton_monitor";
        }
        return TextUtils.equals(str, "core_exception_monitor") ? c4574wc1.b : c4574wc1.c != null && c4574wc1.c.optInt(str) == 1;
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public boolean getMetricTypeSwitch(String str) {
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        return (c4574wc1.d == null || TextUtils.isEmpty(str) || c4574wc1.d.optInt(str) != 1) ? false : true;
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public boolean getServiceSwitch(String str) {
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        return (c4574wc1.e == null || TextUtils.isEmpty(str) || c4574wc1.e.optInt(str) != 1) ? false : true;
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public boolean getSwitch(String str) {
        JSONObject jSONObject;
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        c4574wc1.getClass();
        if (TextUtils.isEmpty(str) || (jSONObject = c4574wc1.k) == null) {
            return false;
        }
        return jSONObject.optBoolean(str);
    }

    public void initParams(boolean z, O71 o71, List<String> list) {
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        c4574wc1.q = z;
        c4574wc1.z = AbstractC4017sd1.h();
        if (c4574wc1.i == null) {
            c4574wc1.i = W91.a(AbstractC4017sd1.a, "monitor_config");
        }
        c4574wc1.j = o71;
        if (!AbstractC0539Jv0.W(list)) {
            c4574wc1.f = list;
        }
        if (c4574wc1.p) {
            return;
        }
        c4574wc1.p = true;
        if (c4574wc1.z || c4574wc1.q) {
            C4784y61.i.a(c4574wc1);
        }
        IntentFilter intentFilter = new IntentFilter();
        String str = "apmplus.";
        if (AbstractC4017sd1.a != null) {
            str = "apmplus." + AbstractC4017sd1.a.getApplicationContext().getPackageName();
        }
        intentFilter.addAction(str);
        C1462aa c1462aa = new C1462aa(c4574wc1, 6);
        try {
            if (AbstractC4017sd1.a != null) {
                if (Build.VERSION.SDK_INT >= 33) {
                    AbstractC4017sd1.a.registerReceiver(c1462aa, intentFilter, 4);
                } else {
                    AbstractC4017sd1.a.registerReceiver(c1462aa, intentFilter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public boolean isConfigReady() {
        return this.mSlardarConfigFetcher.a;
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public String queryConfig() {
        return this.mSlardarConfigFetcher.i.getString("monitor_net_config", "");
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public void registerConfigListener(InterfaceC1559b31 interfaceC1559b31) {
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        c4574wc1.getClass();
        if (interfaceC1559b31 == null) {
            return;
        }
        if (c4574wc1.A == null) {
            c4574wc1.A = new CopyOnWriteArrayList();
        }
        if (!c4574wc1.A.contains(interfaceC1559b31)) {
            c4574wc1.A.add(interfaceC1559b31);
        }
        if (c4574wc1.a) {
            interfaceC1559b31.onRefresh(c4574wc1.k, c4574wc1.l);
            interfaceC1559b31.onReady();
        }
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public void registerResponseConfigListener(Y61 y61) {
        if (y61 == null) {
            return;
        }
        if (AbstractC0539Jv0.b == null) {
            AbstractC0539Jv0.b = new CopyOnWriteArrayList();
        }
        if (AbstractC0539Jv0.b.contains(y61)) {
            return;
        }
        AbstractC0539Jv0.b.add(y61);
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public void unregisterConfigListener(InterfaceC1559b31 interfaceC1559b31) {
        CopyOnWriteArrayList copyOnWriteArrayList;
        C4574wc1 c4574wc1 = this.mSlardarConfigFetcher;
        c4574wc1.getClass();
        if (interfaceC1559b31 == null || (copyOnWriteArrayList = c4574wc1.A) == null) {
            return;
        }
        copyOnWriteArrayList.remove(interfaceC1559b31);
    }

    @Override // com.bytedance.services.slardar.config.IConfigManager
    public void unregisterResponseConfigListener(Y61 y61) {
        CopyOnWriteArrayList copyOnWriteArrayList;
        if (y61 == null || (copyOnWriteArrayList = AbstractC0539Jv0.b) == null) {
            return;
        }
        copyOnWriteArrayList.remove(y61);
    }
}
