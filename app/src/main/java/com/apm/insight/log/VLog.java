package com.apm.insight.log;

import android.content.Context;
import com.apm.insight.log.VLogConfig;
import com.apm.insight.log.a.g;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

/* JADX INFO: loaded from: classes.dex */
public class VLog {
    private static boolean a = false;
    private static Object b = new Object();

    public static void changeLevel(int i) {
        a.a(i);
    }

    public static ILog createInstance(VLogConfig vLogConfig, String str) {
        ILog vLog = getInstance(str);
        return vLog != null ? vLog : a.a(str, vLogConfig);
    }

    public static void d(String str, String str2) {
        a.b(str, str2);
    }

    public static void destroy() {
        a.f();
    }

    public static void e(String str, String str2) {
        a.e(str, str2);
    }

    public static void flush() {
        a.c();
    }

    public static long getALogSimpleWriteFuncAddr() {
        return a.k();
    }

    public static long getALogWriteFuncAddr() {
        return a.h();
    }

    public static long getAlogNativeFlushV2FuncAddr() {
        return a.i();
    }

    public static long getAlogNativeLogStoreDirFuncAddr() {
        return a.j();
    }

    public static Set<String> getBlockTagSet() {
        return a.b();
    }

    public static ILog getInstance(String str) {
        return a.a(str);
    }

    public static HashMap<String, String> getLastFetchErrorInfo() {
        return a.d();
    }

    public static List<String> getLogFiles(long j, long j2) {
        ArrayList arrayList = new ArrayList();
        try {
            File[] fileArrA = g.a((String) null, (String) null, j * 1000, j2 * 1000);
            for (File file : fileArrA) {
                arrayList.add(file.getAbsolutePath());
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public static String getStatus() {
        return a.e();
    }

    public static void i(String str, String str2) {
        a.c(str, str2);
    }

    public static void init(Context context, int i) throws Throwable {
        synchronized (b) {
            try {
                if (a) {
                    return;
                }
                a = true;
                a.a(new VLogConfig.Builder(context).setMaxDirSize(i << 20).setOffloadMainThreadWrite(true).build());
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static boolean isInitSuccess() {
        return a.a();
    }

    public static void release() {
        a.g();
    }

    public static void removeObsoleteInstance(String str, Context context, boolean z) throws Throwable {
        a.a(str, context, z);
    }

    public static void setBlockTagSet(Set<String> set) {
        a.a(set);
    }

    public static void setDebug(boolean z) {
        a.a(z);
    }

    public static void setOuterExecutorService(ScheduledExecutorService scheduledExecutorService) {
        a.a(scheduledExecutorService);
    }

    public static void v(String str, String str2) {
        a.a(str, str2);
    }

    public static void w(String str, String str2) {
        a.d(str, str2);
    }

    public static List<String> getLogFiles(boolean z, long j, long j2, int i) {
        ArrayList arrayList = new ArrayList();
        try {
            for (File file : g.a(z, j * 1000, j2 * 1000, i)) {
                arrayList.add(file.getAbsolutePath());
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }
}
