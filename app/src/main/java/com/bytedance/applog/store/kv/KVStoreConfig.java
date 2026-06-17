package com.bytedance.applog.store.kv;

import defpackage.T00;

/* JADX INFO: loaded from: classes.dex */
@T00
public class KVStoreConfig {
    public static final KVStoreConfig DEFAULT_CONFIG = new KVStoreConfig();
    public static final KVStoreConfig DEFAULT_SECURITY_CONFIG = new KVStoreConfig(true);
    public String aesKey;
    public boolean isSecurityMode;

    public KVStoreConfig() {
        this.isSecurityMode = false;
        this.aesKey = null;
    }

    public static KVStoreConfig createCustomASEKeySecurityConfig(String str) {
        return new KVStoreConfig(true, str);
    }

    public String getAesKey() {
        return this.aesKey;
    }

    public boolean isSecurityMode() {
        return this.isSecurityMode;
    }

    public KVStoreConfig(boolean z) {
        this.aesKey = null;
        this.isSecurityMode = z;
    }

    public KVStoreConfig(boolean z, String str) {
        this.isSecurityMode = z;
        this.aesKey = str;
    }
}
