package com.bytedance.apm.doctor;

import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public interface DoctorManager$ApmListener {
    void onDataEvent(int i, String str, JSONObject jSONObject);

    void onEvent(String str, String str2);
}
