package com.tencent.mmkv;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.termux.app.TermuxApplication;
import dalvik.annotation.optimization.FastNative;
import defpackage.AbstractC1183Wg;
import defpackage.AbstractC4671xI0;
import defpackage.C0317Fo0;
import defpackage.C3342np;
import defpackage.EnumC3809r80;
import defpackage.EnumC3949s80;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class MMKV implements SharedPreferences, SharedPreferences.Editor {
    public static final EnumMap a;
    public static final EnumMap b;
    public static final EnumC3809r80[] c;
    public static final HashSet d;
    public static String e;
    public static boolean f;
    private final long nativeHandle;

    static {
        EnumMap enumMap = new EnumMap(EnumC3949s80.class);
        a = enumMap;
        enumMap.put(EnumC3949s80.a, 0);
        enumMap.put(EnumC3949s80.b, 1);
        EnumMap enumMap2 = new EnumMap(EnumC3809r80.class);
        b = enumMap2;
        EnumC3809r80 enumC3809r80 = EnumC3809r80.a;
        enumMap2.put(enumC3809r80, 0);
        EnumC3809r80 enumC3809r802 = EnumC3809r80.b;
        enumMap2.put(enumC3809r802, 1);
        EnumC3809r80 enumC3809r803 = EnumC3809r80.c;
        enumMap2.put(enumC3809r803, 2);
        EnumC3809r80 enumC3809r804 = EnumC3809r80.d;
        enumMap2.put(enumC3809r804, 3);
        EnumC3809r80 enumC3809r805 = EnumC3809r80.e;
        enumMap2.put(enumC3809r805, 4);
        c = new EnumC3809r80[]{enumC3809r80, enumC3809r802, enumC3809r803, enumC3809r804, enumC3809r805};
        d = new HashSet();
        e = null;
        f = true;
        new HashMap();
    }

    public MMKV(long j) {
        this.nativeHandle = j;
    }

    public static MMKV a(long j, String str) {
        if (j == 0) {
            throw new RuntimeException(AbstractC4671xI0.z("Fail to create an MMKV instance [", str, "] in JNI"));
        }
        if (!f) {
            return new MMKV(j);
        }
        HashSet hashSet = d;
        synchronized (hashSet) {
            try {
                if (!hashSet.contains(Long.valueOf(j))) {
                    if (!checkProcessMode(j)) {
                        throw new IllegalArgumentException("Opening a multi-process MMKV instance [" + str + "] with SINGLE_PROCESS_MODE!");
                    }
                    hashSet.add(Long.valueOf(j));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return new MMKV(j);
    }

    private native long actualSize(long j);

    private native String[] allKeys(long j, boolean z);

    public static native long backupAllToDirectory(String str);

    public static native boolean backupOneToDirectory(String str, String str2, String str3);

    private static native boolean checkProcessMode(long j);

    private native boolean containsKey(long j, String str);

    private native long count(long j, boolean z);

    private static native long createNB(int i);

    private native boolean decodeBool(long j, String str, boolean z);

    private native byte[] decodeBytes(long j, String str);

    private native double decodeDouble(long j, String str, double d2);

    private native float decodeFloat(long j, String str, float f2);

    private native int decodeInt(long j, String str, int i);

    private native long decodeLong(long j, String str, long j2);

    private native String decodeString(long j, String str, String str2);

    private native String[] decodeStringSet(long j, String str);

    private static native void destroyNB(long j, int i);

    private native boolean encodeBool(long j, String str, boolean z);

    private native boolean encodeBool_2(long j, String str, boolean z, int i);

    private native boolean encodeBytes(long j, String str, byte[] bArr);

    private native boolean encodeBytes_2(long j, String str, byte[] bArr, int i);

    private native boolean encodeDouble(long j, String str, double d2);

    private native boolean encodeDouble_2(long j, String str, double d2, int i);

    private native boolean encodeFloat(long j, String str, float f2);

    private native boolean encodeFloat_2(long j, String str, float f2, int i);

    private native boolean encodeInt(long j, String str, int i);

    private native boolean encodeInt_2(long j, String str, int i, int i2);

    private native boolean encodeLong(long j, String str, long j2);

    private native boolean encodeLong_2(long j, String str, long j2, int i);

    private native boolean encodeSet(long j, String str, String[] strArr);

    private native boolean encodeSet_2(long j, String str, String[] strArr, int i);

    private native boolean encodeString(long j, String str, String str2);

    private native boolean encodeString_2(long j, String str, String str2, int i);

    public static MMKV f() {
        if (e != null) {
            return a(getDefaultMMKV(1, null), "DefaultMMKV");
        }
        throw new IllegalStateException("You should Call MMKV.initialize() first.");
    }

    private static native long getDefaultMMKV(int i, String str);

    private static native long getMMKVWithAshmemFD(String str, int i, int i2, String str2);

    private static native long getMMKVWithID(String str, int i, String str2, String str3, long j);

    private static native long getMMKVWithIDAndSize(String str, int i, int i2, String str2);

    private native boolean isCompareBeforeSetEnabled();

    @FastNative
    private native boolean isEncryptionEnabled();

    @FastNative
    private native boolean isExpirationEnabled();

    public static native boolean isFileValid(String str, String str2);

    private static native void jniInitialize(String str, String str2, int i, boolean z);

    public static String k(TermuxApplication termuxApplication) {
        String str = termuxApplication.getFilesDir().getAbsolutePath() + "/mmkv";
        if (!Process.is64Bit()) {
            throw new C3342np("MMKV 2.0+ requires 64-bit App, use 1.3.x instead.");
        }
        if ((termuxApplication.getApplicationInfo().flags & 2) == 0) {
            synchronized (d) {
                f = false;
            }
            Log.i("MMKV", "Disable checkProcessMode()");
        } else {
            synchronized (d) {
                f = true;
            }
            Log.i("MMKV", "Enable checkProcessMode()");
        }
        String absolutePath = termuxApplication.getCacheDir().getAbsolutePath();
        System.loadLibrary("mmkv");
        jniInitialize(str, absolutePath, 1, false);
        e = str;
        return str;
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x00d4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static MMKV l(Context context, String str, int i, int i2, String str2) {
        int i3;
        ComponentName componentName;
        PackageManager packageManager;
        ProviderInfo providerInfo;
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        Object objInvoke;
        if (e == null) {
            throw new IllegalStateException("You should Call MMKV.initialize() first.");
        }
        int iMyPid = Process.myPid();
        Uri uri = MMKVContentProvider.a;
        MMKV mmkv = null;
        String str3 = "";
        if (iMyPid != Process.myPid()) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            if (activityManager != null) {
                Iterator<ActivityManager.RunningAppProcessInfo> it = activityManager.getRunningAppProcesses().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ActivityManager.RunningAppProcessInfo next = it.next();
                    if (next.pid == iMyPid) {
                        str3 = next.processName;
                        break;
                    }
                }
            }
        } else if (TextUtils.isEmpty(AbstractC1183Wg.q)) {
            String processName = Build.VERSION.SDK_INT >= 28 ? Application.getProcessName() : "";
            AbstractC1183Wg.q = processName;
            if (TextUtils.isEmpty(processName)) {
                try {
                    Method declaredMethod = Class.forName("android.app.ActivityThread").getDeclaredMethod("currentProcessName", new Class[0]);
                    declaredMethod.setAccessible(true);
                    objInvoke = declaredMethod.invoke(null, new Object[0]);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                String str4 = objInvoke instanceof String ? (String) objInvoke : "";
                AbstractC1183Wg.q = str4;
                if (TextUtils.isEmpty(str4)) {
                    int iMyPid2 = Process.myPid();
                    ActivityManager activityManager2 = (ActivityManager) context.getSystemService("activity");
                    if (activityManager2 != null && (runningAppProcesses = activityManager2.getRunningAppProcesses()) != null) {
                        Iterator<ActivityManager.RunningAppProcessInfo> it2 = runningAppProcesses.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            ActivityManager.RunningAppProcessInfo next2 = it2.next();
                            if (next2.pid == iMyPid2) {
                                str3 = next2.processName;
                                break;
                            }
                        }
                    }
                    AbstractC1183Wg.q = str3;
                } else {
                    str3 = AbstractC1183Wg.q;
                }
            } else {
                str3 = AbstractC1183Wg.q;
            }
        } else {
            str3 = AbstractC1183Wg.q;
        }
        EnumC3809r80 enumC3809r80 = EnumC3809r80.d;
        if (str3 == null || str3.length() == 0) {
            o(enumC3809r80, "process name detect fail, try again later");
            throw new IllegalStateException("process name detect fail, try again later");
        }
        boolean zContains = str3.contains(":");
        EnumC3809r80 enumC3809r802 = EnumC3809r80.b;
        if (zContains) {
            Uri uri2 = MMKVContentProvider.a;
            if (uri2 == null) {
                if (context == null) {
                    uri2 = null;
                } else {
                    try {
                        componentName = new ComponentName(context, MMKVContentProvider.class.getName());
                        packageManager = context.getPackageManager();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    String str5 = (packageManager == null || (providerInfo = packageManager.getProviderInfo(componentName, 0)) == null) ? null : providerInfo.authority;
                    if (str5 != null) {
                        uri2 = Uri.parse("content://".concat(str5));
                        MMKVContentProvider.a = uri2;
                    }
                }
            }
            if (uri2 == null) {
                o(enumC3809r80, "MMKVContentProvider has invalid authority");
                throw new IllegalStateException("MMKVContentProvider has invalid authority");
            }
            o(enumC3809r802, "getting parcelable mmkv in process, Uri = " + uri2);
            Bundle bundle = new Bundle();
            bundle.putInt("KEY_SIZE", i);
            bundle.putInt("KEY_MODE", i2);
            if (str2 != null) {
                bundle.putString("KEY_CRYPT", str2);
            }
            Bundle bundleCall = context.getContentResolver().call(uri2, "mmkvFromAshmemID", str, bundle);
            if (bundleCall != null) {
                bundleCall.setClassLoader(C0317Fo0.class.getClassLoader());
                C0317Fo0 c0317Fo0 = (C0317Fo0) bundleCall.getParcelable("KEY");
                if (c0317Fo0 != null) {
                    int i4 = c0317Fo0.b;
                    if (i4 >= 0 && (i3 = c0317Fo0.c) >= 0) {
                        String str6 = c0317Fo0.d;
                        String str7 = c0317Fo0.a;
                        long mMKVWithAshmemFD = getMMKVWithAshmemFD(str7, i4, i3, str6);
                        if (mMKVWithAshmemFD == 0) {
                            throw new RuntimeException(AbstractC4671xI0.z("Fail to create an ashmem MMKV instance [", str7, "] in JNI"));
                        }
                        mmkv = new MMKV(mMKVWithAshmemFD);
                    }
                    if (mmkv != null) {
                        o(enumC3809r802, mmkv.mmapID() + " fd = " + mmkv.ashmemFD() + ", meta fd = " + mmkv.ashmemMetaFD());
                        return mmkv;
                    }
                }
            }
        }
        o(enumC3809r802, "getting mmkv in main process");
        long mMKVWithIDAndSize = getMMKVWithIDAndSize(str, i, i2 | 8, str2);
        if (mMKVWithIDAndSize != 0) {
            return new MMKV(mMKVWithIDAndSize);
        }
        throw new IllegalStateException(AbstractC4671xI0.z("Fail to create an Ashmem MMKV instance [", str, "]"));
    }

    public static MMKV m() {
        if (e != null) {
            return a(getMMKVWithID("email_accounts", 1, null, null, 0L), "email_accounts");
        }
        throw new IllegalStateException("You should Call MMKV.initialize() first.");
    }

    private static void mmkvLogImp(int i, String str, int i2, String str2, String str3) {
        int iOrdinal = c[i].ordinal();
        if (iOrdinal == 0) {
            Log.d("MMKV", str3);
            return;
        }
        if (iOrdinal == 1) {
            Log.i("MMKV", str3);
        } else if (iOrdinal == 2) {
            Log.w("MMKV", str3);
        } else {
            if (iOrdinal != 3) {
                return;
            }
            Log.e("MMKV", str3);
        }
    }

    @FastNative
    private native void nativeEnableCompareBeforeSet();

    public static void o(EnumC3809r80 enumC3809r80, String str) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[r0.length - 1];
        Integer num = (Integer) b.get(enumC3809r80);
        mmkvLogImp(num == null ? 0 : num.intValue(), stackTraceElement.getFileName(), stackTraceElement.getLineNumber(), stackTraceElement.getMethodName(), str);
    }

    private static void onContentChangedByOuterProcess(String str) {
    }

    public static native void onExit();

    private static int onMMKVCRCCheckFail(String str) {
        EnumC3949s80 enumC3949s80 = EnumC3949s80.a;
        o(EnumC3809r80.b, "Recover strategic for " + str + " is " + enumC3949s80);
        Integer num = (Integer) a.get(enumC3949s80);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    private static int onMMKVFileLengthError(String str) {
        EnumC3949s80 enumC3949s80 = EnumC3949s80.a;
        o(EnumC3809r80.b, "Recover strategic for " + str + " is " + enumC3949s80);
        Integer num = (Integer) a.get(enumC3949s80);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public static native int pageSize();

    public static native boolean removeStorage(String str, String str2);

    private native void removeValueForKey(long j, String str);

    public static native long restoreAllFromDirectory(String str);

    public static native boolean restoreOneMMKVFromDirectory(String str, String str2, String str3);

    private static native void setCallbackHandler(boolean z, boolean z2);

    private static native void setLogLevel(int i);

    private static native void setWantsContentChangeNotify(boolean z);

    private native void sync(boolean z);

    private native long totalSize(long j);

    private native int valueSize(long j, String str, boolean z);

    public static native String version();

    private native int writeValueToNB(long j, String str, long j2, int i);

    @Override // android.content.SharedPreferences.Editor
    public final void apply() {
        sync(false);
    }

    public native int ashmemFD();

    public native int ashmemMetaFD();

    public final boolean b(String str) {
        return decodeBool(this.nativeHandle, str, false);
    }

    public final int c() {
        return decodeInt(this.nativeHandle, "oversea_override", -1);
    }

    public native void checkContentChangedByOuterProcess();

    public native void checkReSetCryptKey(String str);

    @Override // android.content.SharedPreferences.Editor
    public final SharedPreferences.Editor clear() {
        clearAll();
        return this;
    }

    public native void clearAll();

    public native void clearAllWithKeepingSpace();

    public native void clearMemoryCache();

    public native void close();

    @Override // android.content.SharedPreferences.Editor
    public final boolean commit() {
        sync(true);
        return true;
    }

    @Override // android.content.SharedPreferences
    public final boolean contains(String str) {
        return containsKey(this.nativeHandle, str);
    }

    public native String cryptKey();

    public final long d(String str) {
        return decodeLong(this.nativeHandle, str, 0L);
    }

    public native boolean disableAutoKeyExpire();

    public native void disableCompareBeforeSet();

    public final String e(String str, String str2) {
        return decodeString(this.nativeHandle, str, str2);
    }

    @Override // android.content.SharedPreferences
    public final SharedPreferences.Editor edit() {
        return this;
    }

    public native boolean enableAutoKeyExpire(int i);

    public final void g(int i) {
        encodeInt(this.nativeHandle, "oversea_override", i);
    }

    @Override // android.content.SharedPreferences
    public final Map getAll() {
        throw new UnsupportedOperationException("Intentionally Not Supported. Use allKeys() instead, getAll() not implement because type-erasure inside mmkv");
    }

    @Override // android.content.SharedPreferences
    public final boolean getBoolean(String str, boolean z) {
        return decodeBool(this.nativeHandle, str, z);
    }

    @Override // android.content.SharedPreferences
    public final float getFloat(String str, float f2) {
        return decodeFloat(this.nativeHandle, str, f2);
    }

    @Override // android.content.SharedPreferences
    public final int getInt(String str, int i) {
        return decodeInt(this.nativeHandle, str, i);
    }

    @Override // android.content.SharedPreferences
    public final long getLong(String str, long j) {
        return decodeLong(this.nativeHandle, str, j);
    }

    @Override // android.content.SharedPreferences
    public final String getString(String str, String str2) {
        return decodeString(this.nativeHandle, str, str2);
    }

    @Override // android.content.SharedPreferences
    public final Set getStringSet(String str, Set set) {
        String[] strArrDecodeStringSet = decodeStringSet(this.nativeHandle, str);
        if (strArrDecodeStringSet != null) {
            try {
                Set set2 = (Set) HashSet.class.newInstance();
                set2.addAll(Arrays.asList(strArrDecodeStringSet));
                return set2;
            } catch (IllegalAccessException | InstantiationException unused) {
            }
        }
        return set;
    }

    public final void h(long j, String str) {
        encodeLong(this.nativeHandle, str, j);
    }

    public final void i(String str, String str2) {
        encodeString(this.nativeHandle, str, str2);
    }

    public native boolean isMultiProcess();

    public native boolean isReadOnly();

    public final void j(String str, boolean z) {
        encodeBool(this.nativeHandle, str, z);
    }

    public native void lock();

    public native String mmapID();

    public final void n(String str) {
        removeValueForKey(this.nativeHandle, str);
    }

    @Override // android.content.SharedPreferences.Editor
    public final SharedPreferences.Editor putBoolean(String str, boolean z) {
        encodeBool(this.nativeHandle, str, z);
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public final SharedPreferences.Editor putFloat(String str, float f2) {
        encodeFloat(this.nativeHandle, str, f2);
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public final SharedPreferences.Editor putInt(String str, int i) {
        encodeInt(this.nativeHandle, str, i);
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public final SharedPreferences.Editor putLong(String str, long j) {
        encodeLong(this.nativeHandle, str, j);
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public final SharedPreferences.Editor putString(String str, String str2) {
        encodeString(this.nativeHandle, str, str2);
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public final SharedPreferences.Editor putStringSet(String str, Set set) {
        encodeSet(this.nativeHandle, str, set == null ? null : (String[]) set.toArray(new String[0]));
        return this;
    }

    public native boolean reKey(String str);

    @Override // android.content.SharedPreferences
    public final void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        throw new UnsupportedOperationException("Intentionally Not implement in MMKV");
    }

    @Override // android.content.SharedPreferences.Editor
    public final SharedPreferences.Editor remove(String str) {
        n(str);
        return this;
    }

    public native void removeValuesForKeys(String[] strArr);

    public native void trim();

    public native boolean tryLock();

    public native void unlock();

    @Override // android.content.SharedPreferences
    public final void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        throw new UnsupportedOperationException("Intentionally Not implement in MMKV");
    }
}
