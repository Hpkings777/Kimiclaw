package com.bytedance.services.apm.api;

import com.bytedance.news.common.service.manager.IService;
import defpackage.K91;
import java.util.List;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public interface IMonitorLogManager extends IService {
    void deleteLegacyLogByIds(String str, String str2);

    void getLegacyLog(long j, long j2, String str, K91 k91);

    List<JSONObject> getRecentUiActionRecords();
}
