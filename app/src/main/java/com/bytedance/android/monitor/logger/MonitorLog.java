package com.bytedance.android.monitor.logger;

import android.text.TextUtils;
import android.util.Log;
import defpackage.E31;

/* JADX INFO: loaded from: classes.dex */
public class MonitorLog {
    public static E31 a = null;
    public static boolean b = false;

    public static String a(String str) {
        return TextUtils.isEmpty(str) ? "hybrid_monitor" : !str.startsWith("hybrid_monitor_") ? "hybrid_monitor_".concat(str) : str;
    }

    public static void d(String str, String str2) {
        if (isLogEnable()) {
            Log.d(a(str), str2);
        }
    }

    public static void e(String str, String str2) {
        Log.e(a(str), str2);
    }

    public static void i(String str, String str2) {
        if (isLogEnable()) {
            Log.i(a(str), str2);
        }
    }

    public static boolean isLogEnable() {
        return b;
    }

    public static void setLogEnable(boolean z) {
        b = z;
    }

    public static void setLogger(E31 e31) {
    }

    public static void v(String str, String str2) {
        if (isLogEnable()) {
            Log.v(a(str), str2);
        }
    }

    public static void w(String str, String str2) {
        if (isLogEnable()) {
            Log.w(a(str), str2);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        if (isLogEnable()) {
            Log.e(a(str), str2, th);
        }
    }
}
