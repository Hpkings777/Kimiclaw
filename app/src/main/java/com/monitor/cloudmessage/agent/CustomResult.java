package com.monitor.cloudmessage.agent;

import java.io.File;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class CustomResult {
    public HashMap<String, String> a;
    public File b;

    public CustomResult(HashMap<String, String> map, File file) {
        this.a = map;
        this.b = file;
    }

    public HashMap<String, String> getCustomInfo() {
        return this.a;
    }

    public File getFile() {
        return this.b;
    }
}
