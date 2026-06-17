package com.bytedance.services.apm.api;

import com.bytedance.apm.impl.ApmAgentServiceImpl;
import com.bytedance.news.common.service.manager.IServiceProxy;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class IApmAgent__ServiceProxy implements IServiceProxy<IApmAgent> {
    @Override // com.bytedance.news.common.service.manager.IServiceProxy
    public void collectService(Map<String, String> map) {
        map.put("com.bytedance.services.apm.api.IApmAgent", "com.bytedance.apm.impl.ApmAgentServiceImpl");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bytedance.news.common.service.manager.IServiceProxy
    public IApmAgent newInstance() {
        return new ApmAgentServiceImpl();
    }
}
