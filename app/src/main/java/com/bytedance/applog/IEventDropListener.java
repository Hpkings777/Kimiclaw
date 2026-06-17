package com.bytedance.applog;

import defpackage.T00;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@T00
public interface IEventDropListener {
    void onDrop(String str, JSONObject jSONObject);

    void onPackFailed();
}
