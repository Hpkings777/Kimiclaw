package com.bytedance.services.apm.api;

import com.bytedance.news.common.service.manager.IService;
import defpackage.X61;

/* JADX INFO: loaded from: classes.dex */
public interface IActivityLifeManager extends IService {
    boolean isForeground();

    void register(X61 x61);

    void unregister(X61 x61);
}
