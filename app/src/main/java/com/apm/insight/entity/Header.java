package com.apm.insight.entity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.support.annotation.Keep;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.apm.insight.ICommonParams;
import com.sun.mail.imap.IMAPStore;
import defpackage.AO0;
import defpackage.AbstractC0589Ku0;
import defpackage.AbstractC1208Ws0;
import defpackage.AbstractC1542ax0;
import defpackage.AbstractC4598wn0;
import defpackage.AbstractC4635x31;
import defpackage.C1913db;
import defpackage.Da1;
import defpackage.Jb1;
import defpackage.Q91;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class Header {
    public static final String[] b = {"version_code", "manifest_version_code", "aid", "update_version_code"};
    public static String c = null;
    public static int d = -1;
    public static int e = -1;
    public final JSONObject a = new JSONObject();

    public static Header a() {
        Header header = new Header();
        JSONObject jSONObject = header.a;
        try {
            jSONObject.put("sdk_version", 1052090);
            jSONObject.put("sdk_version_name", "1.5.20");
        } catch (Exception unused) {
        }
        return header;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:4|(2:105|5)|(13:(1:7)(3:9|(1:15)(1:14)|(1:17)(26:18|(1:20)|21|(1:23)|24|(1:26)|27|103|28|(1:30)(2:31|(1:33)(2:34|(1:36)(2:37|(1:39)(2:40|(1:42)(1:43)))))|44|(1:46)|47|(10:101|50|(1:52)|53|(1:55)|56|(1:58)|59|(1:61)|62)|107|63|(3:65|(1:67)(1:68)|69)|70|(3:72|(1:74)(1:(1:78))|79)|80|(4:82|83|(1:85)(1:87)|86)|88|89|(1:91)(1:92)|93|110))|107|63|(0)|70|(0)|80|(0)|88|89|(0)(0)|93|110)|8|24|(0)|27|103|28|(0)(0)|44|(0)|47|(0)) */
    /* JADX WARN: Removed duplicated region for block: B:101:0x00ce A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0059 A[Catch: all -> 0x006b, TryCatch #2 {all -> 0x006b, blocks: (B:5:0x000a, B:8:0x0012, B:24:0x004e, B:26:0x0059, B:27:0x0062, B:9:0x0016, B:11:0x0020, B:18:0x0033, B:20:0x003d, B:21:0x0042, B:23:0x0048), top: B:105:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00aa A[Catch: Exception -> 0x00c7, TRY_LEAVE, TryCatch #1 {Exception -> 0x00c7, blocks: (B:28:0x006b, B:44:0x009c, B:46:0x00aa), top: B:103:0x006b }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x012f A[Catch: all -> 0x01cd, TryCatch #3 {all -> 0x01cd, blocks: (B:63:0x0117, B:65:0x012f, B:69:0x0142, B:68:0x013c, B:70:0x014c, B:72:0x0150, B:79:0x0176, B:76:0x015c, B:78:0x0162, B:80:0x0179, B:82:0x01a3, B:85:0x01a9, B:86:0x01ad, B:87:0x01b1, B:88:0x01bc, B:93:0x01c9), top: B:107:0x0117 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0150 A[Catch: all -> 0x01cd, TryCatch #3 {all -> 0x01cd, blocks: (B:63:0x0117, B:65:0x012f, B:69:0x0142, B:68:0x013c, B:70:0x014c, B:72:0x0150, B:79:0x0176, B:76:0x015c, B:78:0x0162, B:80:0x0179, B:82:0x01a3, B:85:0x01a9, B:86:0x01ad, B:87:0x01b1, B:88:0x01bc, B:93:0x01c9), top: B:107:0x0117 }] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01a3 A[Catch: all -> 0x01cd, TRY_LEAVE, TryCatch #3 {all -> 0x01cd, blocks: (B:63:0x0117, B:65:0x012f, B:69:0x0142, B:68:0x013c, B:70:0x014c, B:72:0x0150, B:79:0x0176, B:76:0x015c, B:78:0x0162, B:80:0x0179, B:82:0x01a3, B:85:0x01a9, B:86:0x01ad, B:87:0x01b1, B:88:0x01bc, B:93:0x01c9), top: B:107:0x0117 }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01c7  */
    @Keep
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void addOtherHeader(JSONObject jSONObject) {
        ApplicationInfo applicationInfo;
        String str;
        if (jSONObject == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        try {
            if (Q91.d()) {
                str = "MIUI-";
            } else {
                if (!(Build.DISPLAY.contains("Flyme") || Build.USER.equals("flyme"))) {
                    String strA = Q91.a();
                    if (Q91.b(strA)) {
                        sb.append("EMUI-");
                    }
                    if (!TextUtils.isEmpty(strA)) {
                        sb.append(strA);
                        str = "-";
                    }
                    sb.append(Build.VERSION.INCREMENTAL);
                    if (sb.length() > 0) {
                        jSONObject.put("rom", sb.toString());
                    }
                    jSONObject.put("rom_version", AbstractC0589Ku0.g());
                    DisplayMetrics displayMetrics = Jb1.a.getResources().getDisplayMetrics();
                    int i = displayMetrics.densityDpi;
                    String str2 = i >= 160 ? "ldpi" : i < 240 ? "mdpi" : i < 320 ? "hdpi" : i < 480 ? "xhdpi" : i < 640 ? "xxhdpi" : "xxxhdpi";
                    jSONObject.put("density_dpi", i);
                    jSONObject.put("display_density", str2);
                    if (C1913db.o) {
                        jSONObject.put("resolution", displayMetrics.heightPixels + "x" + displayMetrics.widthPixels);
                    }
                    Context context = Jb1.a;
                    if (C1913db.r) {
                        try {
                            String language = Jb1.a.getResources().getConfiguration().locale.getLanguage();
                            if (!TextUtils.isEmpty(language)) {
                                jSONObject.put("language", language);
                            }
                            String country = Locale.getDefault().getCountry();
                            if (!TextUtils.isEmpty(country)) {
                                jSONObject.put("region", country);
                            }
                            int rawOffset = TimeZone.getDefault().getRawOffset() / 3600000;
                            if (rawOffset < -12) {
                                rawOffset = -12;
                            }
                            if (rawOffset > 12) {
                                rawOffset = 12;
                            }
                            jSONObject.put("timezone", rawOffset);
                        } catch (Exception unused) {
                        }
                    }
                    jSONObject.put(IMAPStore.ID_OS, "Android");
                    jSONObject.put("device_id", Jb1.e().d());
                    if (C1913db.q) {
                        String strConcat = Build.VERSION.RELEASE;
                        if (!strConcat.contains(".")) {
                            strConcat = strConcat.concat(".0");
                        }
                        jSONObject.put("os_version", strConcat);
                        jSONObject.put("os_api", Build.VERSION.SDK_INT);
                    }
                    if (C1913db.n) {
                        String str3 = Build.MODEL;
                        String str4 = Build.BRAND;
                        if (str3 == null) {
                            str3 = str4;
                        } else if (str4 != null && !str3.contains(str4)) {
                            str3 = str4 + ' ' + str3;
                        }
                        jSONObject.put("device_model", str3);
                    }
                    jSONObject.put("device_brand", Build.BRAND);
                    jSONObject.put("device_manufacturer", Build.MANUFACTURER);
                    jSONObject.put("cpu_abi", l());
                    Context context2 = Jb1.a;
                    String packageName = context2.getPackageName();
                    jSONObject.put("package", packageName);
                    PackageInfo packageInfoB = AbstractC4598wn0.b(0, context2, packageName);
                    applicationInfo = packageInfoB.applicationInfo;
                    if (applicationInfo != null) {
                        int i2 = applicationInfo.labelRes;
                        jSONObject.put("display_name", i2 > 0 ? context2.getString(i2) : context2.getPackageManager().getApplicationLabel(packageInfoB.applicationInfo));
                    }
                    jSONObject.put("is_harmony_os", !AbstractC1542ax0.n() ? "1" : "0");
                    return;
                }
                str = "FLYME-";
            }
            jSONObject.put(IMAPStore.ID_OS, "Android");
            jSONObject.put("device_id", Jb1.e().d());
            if (C1913db.q) {
            }
            if (C1913db.n) {
            }
            jSONObject.put("device_brand", Build.BRAND);
            jSONObject.put("device_manufacturer", Build.MANUFACTURER);
            jSONObject.put("cpu_abi", l());
            Context context22 = Jb1.a;
            String packageName2 = context22.getPackageName();
            jSONObject.put("package", packageName2);
            PackageInfo packageInfoB2 = AbstractC4598wn0.b(0, context22, packageName2);
            applicationInfo = packageInfoB2.applicationInfo;
            if (applicationInfo != null) {
            }
            jSONObject.put("is_harmony_os", !AbstractC1542ax0.n() ? "1" : "0");
            return;
        } catch (Throwable th) {
            th.printStackTrace();
            return;
        }
        sb.append(str);
        sb.append(Build.VERSION.INCREMENTAL);
        if (sb.length() > 0) {
        }
        jSONObject.put("rom_version", AbstractC0589Ku0.g());
        DisplayMetrics displayMetrics2 = Jb1.a.getResources().getDisplayMetrics();
        int i3 = displayMetrics2.densityDpi;
        if (i3 >= 160) {
        }
        jSONObject.put("density_dpi", i3);
        jSONObject.put("display_density", str2);
        if (C1913db.o) {
        }
        Context context3 = Jb1.a;
        if (C1913db.r) {
        }
    }

    public static void addRuntimeHeader(JSONObject jSONObject) {
        try {
            jSONObject.put("access", AbstractC4635x31.M(AbstractC4635x31.N(Jb1.a)));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) Jb1.a.getSystemService("phone");
            if (telephonyManager != null) {
                String networkOperatorName = telephonyManager.getNetworkOperatorName();
                if (!TextUtils.isEmpty(networkOperatorName)) {
                    jSONObject.put("carrier", networkOperatorName);
                }
                String networkOperator = telephonyManager.getNetworkOperator();
                if (TextUtils.isEmpty(networkOperator)) {
                    return;
                }
                jSONObject.put("mcc_mnc", networkOperator);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static Header b(long j) {
        Header headerA;
        Da1 da1Q = Da1.q();
        if (j == 0) {
            j = System.currentTimeMillis();
        }
        JSONObject jSONObjectK = da1Q.k(j);
        if (jSONObjectK == null || jSONObjectK.length() == 0) {
            Context context = Jb1.a;
            headerA = a();
            headerA.i();
            try {
                headerA.a.put("errHeader", 1);
            } catch (Throwable unused) {
            }
        } else {
            try {
                if (!jSONObjectK.has("aid")) {
                    jSONObjectK.put("aid", 4444);
                }
            } catch (Exception unused2) {
            }
            Context context2 = Jb1.a;
            headerA = new Header();
        }
        f(headerA);
        headerA.d(jSONObjectK);
        return headerA;
    }

    public static boolean e() {
        if (d == -1) {
            d = l().contains("64") ? 1 : 0;
        }
        return d == 1;
    }

    public static void f(Header header) {
        if (header == null) {
            return;
        }
        addOtherHeader(header.a);
    }

    public static boolean g() {
        if (e == -1) {
            e = l().contains("86") ? 1 : 0;
        }
        return e == 1;
    }

    public static boolean h(JSONObject jSONObject) {
        if (jSONObject == null || jSONObject.length() == 0) {
            return true;
        }
        return (jSONObject.opt("app_version") == null && jSONObject.opt("version_name") == null) || jSONObject.opt("version_code") == null || jSONObject.opt("update_version_code") == null;
    }

    public static String l() {
        if (c == null) {
            try {
                StringBuilder sb = new StringBuilder();
                if (Build.SUPPORTED_ABIS.length > 0) {
                    int i = 0;
                    while (true) {
                        String[] strArr = Build.SUPPORTED_ABIS;
                        if (i >= strArr.length) {
                            break;
                        }
                        sb.append(strArr[i]);
                        if (i != strArr.length - 1) {
                            sb.append(", ");
                        }
                        i++;
                    }
                } else {
                    sb = new StringBuilder(Build.CPU_ABI);
                }
                if (TextUtils.isEmpty(sb.toString())) {
                    c = "unknown";
                }
                c = sb.toString();
            } catch (Exception e2) {
                AbstractC1208Ws0.j(e2);
                c = "unknown";
            }
        }
        return c;
    }

    public final JSONObject c(Map map) {
        JSONObject jSONObject = this.a;
        if (map == null) {
            return jSONObject;
        }
        try {
            for (Map.Entry entry : map.entrySet()) {
                if (!jSONObject.has((String) entry.getKey())) {
                    jSONObject.put((String) entry.getKey(), entry.getValue());
                }
            }
            String[] strArr = b;
            for (int i = 0; i < 4; i++) {
                String str = strArr[i];
                if (map.containsKey(str)) {
                    try {
                        jSONObject.put(str, Integer.parseInt(String.valueOf(map.get(str))));
                    } catch (Throwable unused) {
                        jSONObject.put(str, map.get(str));
                    }
                }
            }
            if (map.containsKey("version_code") && !map.containsKey("manifest_version_code")) {
                try {
                    jSONObject.put("manifest_version_code", Integer.parseInt(String.valueOf(map.get("version_code"))));
                } catch (Throwable unused2) {
                }
            }
            if (map.containsKey("iid")) {
                jSONObject.put("udid", map.get("iid"));
                jSONObject.remove("iid");
            }
            if (map.containsKey("version_name")) {
                jSONObject.put("app_version", map.get("version_name"));
                jSONObject.remove("version_name");
            }
        } catch (Throwable unused3) {
        }
        return jSONObject;
    }

    public final void d(JSONObject jSONObject) {
        JSONObject jSONObject2 = this.a;
        if (jSONObject == null) {
            return;
        }
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            try {
                jSONObject2.put(next, jSONObject.opt(next));
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    public final void i() {
        c(Jb1.a().d());
    }

    public final void j() {
        try {
            this.a.put("device_id", Jb1.e().d());
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public final void k() {
        long userId;
        JSONObject jSONObject = this.a;
        try {
            AO0 ao0A = Jb1.a();
            ao0A.getClass();
            try {
                userId = ((ICommonParams) ao0A.c).getUserId();
            } catch (Throwable unused) {
                userId = 0;
            }
            if (userId > 0) {
                jSONObject.put("user_id", userId);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }
}
