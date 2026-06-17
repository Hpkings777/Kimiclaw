package com.bytedance.services.apm.api;

import com.bytedance.apm.impl.MonitorLogManagerImpl;
import com.bytedance.news.common.service.manager.IServiceProxy;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class IMonitorLogManager__ServiceProxy implements IServiceProxy<IMonitorLogManager> {
    @Override // com.bytedance.news.common.service.manager.IServiceProxy
    public void collectService(Map<String, String> map) {
        map.put("com.bytedance.services.apm.api.IMonitorLogManager", "com.bytedance.apm.impl.MonitorLogManagerImpl");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bytedance.news.common.service.manager.IServiceProxy
    public IMonitorLogManager newInstance() {
        return new MonitorLogManagerImpl();
    }
}
