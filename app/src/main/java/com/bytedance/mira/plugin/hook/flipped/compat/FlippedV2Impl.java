package com.bytedance.mira.plugin.hook.flipped.compat;

import android.util.Log;
import defpackage.B81;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class FlippedV2Impl implements B81 {
    private static final String TAG = "FlippedV2Impl";

    static {
        System.loadLibrary("flipped");
    }

    private native Method getDeclaredMethod(Object obj, String str, Class<?>[] clsArr);

    @Override // defpackage.B81
    public void invokeHiddenApiRestrictions() {
        try {
            Class<?> cls = Class.forName("dalvik.system.VMRuntime");
            Method declaredMethod = getDeclaredMethod(cls, "getRuntime", new Class[0]);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(null, new Object[0]);
            Method declaredMethod2 = getDeclaredMethod(cls, "setHiddenApiExemptions", new Class[]{String[].class});
            declaredMethod2.setAccessible(true);
            declaredMethod2.invoke(objInvoke, new String[]{"L"});
            Log.e(TAG, "V2 invokeHiddenApiRestrictions success.");
        } catch (Exception e) {
            Log.e(TAG, "V2 invokeHiddenApiRestrictions fail: " + Log.getStackTraceString(e));
        }
    }
}
