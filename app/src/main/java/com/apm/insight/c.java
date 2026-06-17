package com.apm.insight;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.apm.insight.MonitorCrash;
import com.sun.mail.imap.IMAPStore;
import defpackage.AO0;
import defpackage.AbstractC1370Zv0;
import defpackage.AbstractC2063ee1;
import defpackage.AbstractC2550i71;
import defpackage.AbstractC4598wn0;
import defpackage.C1913db;
import defpackage.C3222mz;
import defpackage.Ce1;
import defpackage.H51;
import defpackage.Jb1;
import defpackage.K51;
import defpackage.Pd1;
import defpackage.RunnableC4791y9;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    public static volatile MonitorCrash b;
    public static volatile ConcurrentHashMap c = new ConcurrentHashMap();
    public final MonitorCrash a;

    public c(MonitorCrash monitorCrash) {
        this.a = monitorCrash;
        AbstractC2550i71.a.add(this);
        H51.a();
        File file = Pd1.a;
        AbstractC2063ee1.A().a(new RunnableC4791y9(20));
    }

    public static void e(Context context, MonitorCrash monitorCrash) {
        b = monitorCrash;
        b bVar = new b(new c(monitorCrash), monitorCrash);
        Context context2 = Jb1.a;
        synchronized (Jb1.class) {
            Application application = Jb1.b;
            if (application == null) {
                if (context instanceof Application) {
                    application = (Application) context;
                    if (application.getBaseContext() == null) {
                        throw new IllegalArgumentException("初始化时传入的Application还未attach, 请在init时传入attachBaseContext的参数, 并在init之前手动调用Npth.setApplication(Application).");
                    }
                } else {
                    application = (Application) context.getApplicationContext();
                    if (application == null) {
                        throw new IllegalArgumentException("初始化时传入了baseContext, 导致无法获取Application实例, 请在init之前手动调用Npth.setApplication(Application).");
                    }
                    if (application.getBaseContext() != null) {
                        context = application.getBaseContext();
                    }
                }
            }
            Jb1.d(application, context);
            Jb1.f = new AO0(Jb1.a, bVar, Jb1.a());
        }
    }

    public static String h(String str) {
        MonitorCrash monitorCrash;
        if (b != null && TextUtils.equals(str, b.mConfig.a)) {
            monitorCrash = b;
        } else if (c == null || (monitorCrash = (MonitorCrash) c.get(str)) == null) {
            return null;
        }
        return monitorCrash.mConfig.b;
    }

    public static String i(String str) {
        MonitorCrash monitorCrash;
        if (b != null && TextUtils.equals(str, b.mConfig.a)) {
            monitorCrash = b;
        } else if (c == null || (monitorCrash = (MonitorCrash) c.get(str)) == null) {
            return null;
        }
        return monitorCrash.mConfig.getDeviceId();
    }

    public static String j() {
        if (b == null) {
            return null;
        }
        return b.mConfig.a;
    }

    public static long k() {
        long j = b == null ? 0L : b.mConfig.d;
        if (j <= 0) {
            try {
                Context context = Jb1.a;
                ConcurrentHashMap concurrentHashMap = AbstractC4598wn0.a;
                return AbstractC4598wn0.b(0, context, context.getPackageName()) != null ? r0.versionCode : 0;
            } catch (Throwable unused) {
            }
        }
        return j;
    }

    public final JSONArray a(StackTraceElement[] stackTraceElementArr, Throwable th) {
        String[] strArr = this.a.mConfig.f;
        if (strArr == null) {
            JSONArray jSONArray = new JSONArray();
            int length = stackTraceElementArr.length;
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("start", 0);
                jSONObject.put("end", length);
            } catch (Throwable unused) {
            }
            return jSONArray.put(jSONObject);
        }
        if (th == null || stackTraceElementArr == null) {
            return null;
        }
        StackTraceElement stackTraceElement = Ce1.a;
        C3222mz c3222mz = new C3222mz();
        JSONArray jSONArray2 = new JSONArray();
        for (int i = 0; i < stackTraceElementArr.length; i++) {
            if (c3222mz.a == -1) {
                if (Ce1.m(stackTraceElementArr[i].getClassName(), strArr)) {
                    c3222mz.a = i;
                    c3222mz.b = i;
                }
            } else if (!Ce1.m(stackTraceElementArr[i].getClassName(), strArr)) {
                c3222mz.b = i;
                jSONArray2.put(c3222mz.c());
                c3222mz = new C3222mz();
            }
        }
        if (c3222mz.a != -1) {
            c3222mz.b = stackTraceElementArr.length;
            jSONArray2.put(c3222mz.c());
        }
        return jSONArray2;
    }

    public final JSONArray b(String[] strArr) {
        MonitorCrash monitorCrash = this.a;
        if (monitorCrash.config().f == null) {
            JSONArray jSONArray = new JSONArray();
            int length = strArr.length;
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("start", 0);
                jSONObject.put("end", length);
            } catch (Throwable unused) {
            }
            return jSONArray.put(jSONObject);
        }
        String[] strArr2 = monitorCrash.mConfig.f;
        StackTraceElement stackTraceElement = Ce1.a;
        C3222mz c3222mz = new C3222mz();
        JSONArray jSONArray2 = new JSONArray();
        for (int i = 0; i < strArr.length; i++) {
            if (c3222mz.a == -1) {
                if (Ce1.m(strArr[i], strArr2)) {
                    c3222mz.a = i;
                    c3222mz.b = i;
                }
            } else if (!Ce1.m(strArr[i], strArr2)) {
                c3222mz.b = i;
                jSONArray2.put(c3222mz.c());
                c3222mz = new C3222mz();
            }
        }
        if (c3222mz.a != -1) {
            c3222mz.b = strArr.length;
            jSONArray2.put(c3222mz.c());
        }
        try {
            if (AbstractC1370Zv0.e(jSONArray2) && monitorCrash.mConfig.m) {
                String strValueOf = String.valueOf(K51.c().j);
                if (!TextUtils.isEmpty(strValueOf)) {
                    for (String str : monitorCrash.mConfig.f) {
                        if (strValueOf.contains(str)) {
                            JSONArray jSONArray3 = new JSONArray();
                            int length2 = strArr.length;
                            JSONObject jSONObject2 = new JSONObject();
                            try {
                                jSONObject2.put("start", 0);
                                jSONObject2.put("end", length2);
                            } catch (Throwable unused2) {
                            }
                            return jSONArray3.put(jSONObject2);
                        }
                    }
                }
            }
        } catch (Throwable unused3) {
        }
        return jSONArray2;
    }

    public final JSONObject c(CrashType crashType, JSONArray jSONArray, boolean z) {
        Map<? extends String, ? extends String> userData;
        MonitorCrash monitorCrash = this.a;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("header", d(z));
            if (crashType != null) {
                AttachUserData attachUserData = monitorCrash.mCustomData;
                JSONObject jSONObject2 = null;
                if (attachUserData != null && (userData = attachUserData.getUserData(crashType)) != null) {
                    jSONObject2 = new JSONObject(userData);
                }
                jSONObject.put("custom", jSONObject2);
                jSONObject.put("filters", new JSONObject(monitorCrash.mTagMap));
            }
            jSONObject.put("line_num", jSONArray);
        } catch (Throwable unused) {
        }
        return jSONObject;
    }

    public final JSONObject d(boolean z) {
        C1913db c1913dbC;
        JSONArray jSONArray;
        MonitorCrash monitorCrash = this.a;
        JSONObject jSONObject = new JSONObject();
        try {
            MonitorCrash.Config config = monitorCrash.mConfig;
            if (config.f == null) {
                Context context = Jb1.a;
                if (config.d == -1) {
                    ConcurrentHashMap concurrentHashMap = AbstractC4598wn0.a;
                    config.d = AbstractC4598wn0.b(0, context, context.getPackageName()) != null ? r5.versionCode : 0;
                }
                MonitorCrash.Config config2 = monitorCrash.mConfig;
                if (config2.e == null) {
                    config2.e = AbstractC4598wn0.c(context);
                }
            }
        } catch (Throwable unused) {
        }
        if ((TextUtils.isEmpty(monitorCrash.mConfig.getDeviceId()) || "0".equals(monitorCrash.mConfig.getDeviceId())) && (c1913dbC = C1913db.c(monitorCrash.mConfig.a)) != null) {
            monitorCrash.mConfig.setDeviceId(c1913dbC.b(), false);
        }
        try {
            jSONObject.put("aid", String.valueOf(monitorCrash.mConfig.a));
            if (z && !TextUtils.isEmpty(monitorCrash.mConfig.b)) {
                jSONObject.put("x-auth-token", monitorCrash.mConfig.b);
            }
            jSONObject.put("update_version_code", monitorCrash.mConfig.d);
            jSONObject.put("version_code", monitorCrash.mConfig.d);
            jSONObject.put("app_version", monitorCrash.mConfig.e);
            jSONObject.put("channel", monitorCrash.mConfig.c);
            String[] strArr = monitorCrash.mConfig.f;
            JSONArray jSONArray2 = null;
            if (strArr == null) {
                jSONArray = null;
            } else {
                jSONArray = new JSONArray();
                for (String str : strArr) {
                    jSONArray.put(str);
                }
            }
            jSONObject.put("package", jSONArray);
            jSONObject.put("device_id", monitorCrash.mConfig.getDeviceId());
            jSONObject.put("user_id", monitorCrash.mConfig.getUID());
            jSONObject.put("ssid", monitorCrash.mConfig.getSSID());
            jSONObject.put(IMAPStore.ID_OS, "Android");
            String[] strArr2 = monitorCrash.mConfig.g;
            if (strArr2 != null) {
                jSONArray2 = new JSONArray();
                for (String str2 : strArr2) {
                    jSONArray2.put(str2);
                }
            }
            jSONObject.put("so_list", jSONArray2);
            jSONObject.put("single_upload", 0);
            Context context2 = Jb1.a;
            if (C1913db.q) {
                String strConcat = Build.VERSION.RELEASE;
                if (!strConcat.contains(".")) {
                    strConcat = strConcat.concat(".0");
                }
                jSONObject.put("os_version", strConcat);
            }
        } catch (JSONException unused2) {
        }
        return jSONObject;
    }

    public final JSONArray f(String str) {
        if (this.a == b) {
            return new JSONArray();
        }
        String[] strArr = this.a.mConfig.g;
        if (strArr == null || strArr.length <= 0) {
            return null;
        }
        for (String str2 : strArr) {
            if (str.contains(str2)) {
                return new JSONArray();
            }
        }
        return null;
    }

    public final String g() {
        return this.a.mConfig.a;
    }
}
