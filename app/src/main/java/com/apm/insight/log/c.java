package com.apm.insight.log;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import com.apm.insight.log.a.f;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.UnknownHostException;

/* JADX INFO: loaded from: classes.dex */
public class c implements f {
    public static boolean a() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /* JADX WARN: Can't wrap try/catch for region: R(5:9|(3:42|10|(2:12|(3:37|14|15)))|33|18|28) */
    /* JADX WARN: Removed duplicated region for block: B:33:0x004e A[EXC_TOP_SPLITTER, PHI: r1
      0x004e: PHI (r1v6 java.io.BufferedReader) = (r1v5 java.io.BufferedReader), (r1v7 java.io.BufferedReader), (r1v7 java.io.BufferedReader) binds: [B:26:0x005d, B:11:0x003c, B:13:0x0046] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0058 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String b() throws Throwable {
        BufferedReader bufferedReader;
        Throwable th;
        if (Build.VERSION.SDK_INT >= 28) {
            return Application.getProcessName();
        }
        try {
            try {
                Method method = Class.forName("android.app.ActivityThread").getMethod("currentProcessName", new Class[0]);
                method.setAccessible(true);
                return (String) method.invoke(null, new Object[0]);
            } catch (Exception unused) {
                bufferedReader = new BufferedReader(new FileReader("/proc/self/cmdline"));
                try {
                    String line = bufferedReader.readLine();
                    if (!TextUtils.isEmpty(line)) {
                        String strTrim = line.trim();
                        if (!TextUtils.isEmpty(strTrim)) {
                            try {
                                bufferedReader.close();
                            } catch (Exception unused2) {
                            }
                            return strTrim;
                        }
                    }
                } catch (Exception unused3) {
                    if (bufferedReader != null) {
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                    }
                    throw th;
                }
                bufferedReader.close();
                return null;
            }
        } catch (Exception unused4) {
            bufferedReader = null;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return null;
        } catch (Throwable th3) {
            bufferedReader = null;
            th = th3;
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception unused5) {
                }
            }
            throw th;
        }
    }

    public static String d(Context context) {
        File file = new File(context.getFilesDir(), "volc_log");
        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(file, "cache");
        if (!file2.exists()) {
            file2.mkdir();
        }
        return file2.getAbsolutePath();
    }

    @Override // com.apm.insight.log.a.f
    public void c() {
        System.loadLibrary("volc_log");
    }

    public static boolean a(Context context) throws Throwable {
        String strB = b();
        if (strB == null || strB.contains(":")) {
            return false;
        }
        return strB.equals(context.getPackageName()) || strB.equals(context.getApplicationInfo().processName);
    }

    public static File c(Context context) {
        File file = new File(context.getFilesDir(), "volc_log");
        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(file, "logs");
        if (!file2.exists()) {
            file2.mkdir();
        }
        return file2;
    }

    public static String a(Throwable th) {
        if (th == null) {
            return "";
        }
        for (Throwable cause = th; cause != null; cause = cause.getCause()) {
            if (cause instanceof UnknownHostException) {
                return "ALOG:UnknownHostException";
            }
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        try {
            th.printStackTrace(printWriter);
        } catch (Throwable unused) {
        }
        printWriter.flush();
        return stringWriter.toString();
    }

    public static String b(Context context) {
        String str;
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception unused) {
            str = null;
        }
        return TextUtils.isEmpty(str) ? "unknown" : str;
    }
}
