package com.bytedance.services.slardar.config;

import com.bytedance.news.common.service.manager.IService;
import defpackage.InterfaceC1559b31;
import defpackage.Y61;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public interface IConfigManager extends IService {
    void fetchConfig();

    int getConfigInt(String str, int i);

    JSONObject getConfigJSON(String str);

    boolean getLogTypeSwitch(String str);

    boolean getMetricTypeSwitch(String str);

    boolean getServiceSwitch(String str);

    boolean getSwitch(String str);

    boolean isConfigReady();

    String queryConfig();

    void registerConfigListener(InterfaceC1559b31 interfaceC1559b31);

    void registerResponseConfigListener(Y61 y61);

    void unregisterConfigListener(InterfaceC1559b31 interfaceC1559b31);

    void unregisterResponseConfigListener(Y61 y61);
}
