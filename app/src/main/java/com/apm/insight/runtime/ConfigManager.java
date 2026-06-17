package com.apm.insight.runtime;

import android.content.Context;
import android.text.TextUtils;
import com.apm.insight.ICommonParams;
import defpackage.AO0;
import defpackage.AbstractC0744Nu0;
import defpackage.AbstractC2063ee1;
import defpackage.AbstractC3318nd1;
import defpackage.AbstractC4635x31;
import defpackage.C0582Kr;
import defpackage.C3880re1;
import defpackage.Jb1;
import defpackage.N91;
import defpackage.Qd1;
import defpackage.RunnableC0530Jr;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/* JADX INFO: loaded from: classes.dex */
public class ConfigManager {
    private static final String ALOG_UPLOAD_URL = "https://apmplus.volces.com/monitor/collect/c/cloudcontrol/file";
    public static final String ALOG_URL_SUFFIX = "/monitor/collect/c/cloudcontrol/file";
    private static final String ASAN_REPORT_URL = "https://apmplus.volces.com/monitor/collect/c/native_bin_crash";
    public static final long BLOCK_MONITOR_INTERVAL = 1000;
    private static final long BLOCK_MONITOR_MIN_INTERVAL = 10;
    private static final String CONFIG_URL = "https://apmplus.volces.com/settings/get";
    public static final String CONFIG_URL_SUFFIX = "/settings/get";
    private static final String CORE_DUMP_URL = "https://apmplus.volces.com/monitor/collect/c/core_dump_collect";
    private static final String CRASH_PORTRAIT_URL = "https://apmplus.volces.com/monitor/collect/c/crash_portrait";
    private static final String EXCEPTION_URL = "https://apmplus.volces.com/monitor/collect/c/exception";
    public static final String EXCEPTION_URL_SUFFIX = "/monitor/collect/c/exception";
    private static final String FILE_UPLOAD_URL = "https://apmplus.volces.com/monitor/collect/c/logcollect";
    public static final String FILE_UPLOAD_URL_SUFFIX = "/monitor/collect/c/logcollect";
    private static final String JAVA_CRASH_URL = "https://apmplus.volces.com/monitor/collect/c/crash";
    public static final String JAVA_URL_SUFFIX = "/monitor/collect/c/crash";
    private static final long LAUNCH_CRASH_INTERVAL = 8000;
    private static final String LAUNCH_CRASH_URL = "https://apmplus.volces.com/monitor/collect/c/exception/dump_collection";
    public static final String LAUNCH_URL_SUFFIX = "/monitor/collect/c/exception/dump_collection";
    public static final String LOG_TYPE_ALL_STACK = "npth_enable_all_thread_stack";
    private static final String NATIVE_CRASH_URL = "https://apmplus.volces.com/monitor/collect/c/native_bin_crash";
    private static final String NATIVE_MEM_URL = "https://apmplus.volces.com/monitor/collect/c/rapheal_file_collect";
    public static final String NATIVE_URL_SUFFIX = "/monitor/collect/c/native_bin_crash";
    public static final String PORTRAIT_UPLOAD_URL_SUFFIX = "/monitor/collect/c/crash_portrait";
    public static boolean disableConfigUrl = false;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private boolean reportErrorEnable = true;
    private String mNativeMemUrl = NATIVE_MEM_URL;
    private String mCoreDumpUrl = CORE_DUMP_URL;
    private String mJavaCrashUploadUrl = JAVA_CRASH_URL;
    private String mLaunchCrashUploadUrl = LAUNCH_CRASH_URL;
    private String mExceptionUploadUrl = EXCEPTION_URL;
    private String mConfigUrl = CONFIG_URL;
    private String mNativeCrashUploadUrl = "https://apmplus.volces.com/monitor/collect/c/native_bin_crash";
    private String mAlogUploadUrl = ALOG_UPLOAD_URL;
    private String mAsanReportUploadUrl = "https://apmplus.volces.com/monitor/collect/c/native_bin_crash";
    private String mFileUploadUrl = FILE_UPLOAD_URL;
    private String mCrashPortraitUploadUrl = CRASH_PORTRAIT_URL;
    private long mLaunchCrashInterval = LAUNCH_CRASH_INTERVAL;
    private N91 mEncryptImpl = new C3880re1(14);
    private int mLogcatDumpCount = 512;
    private int mLogcatLevel = 1;
    private boolean mNativeCrashMiniDump = true;
    private boolean mEnsureEnable = true;
    private boolean mEnsureWithLogcat = false;
    private long mBlockMonitorInterval = 1000;
    private boolean mBlockMonitorEnable = false;
    private boolean mIsDebugMode = false;
    private boolean mEnableApmPlusLog = false;
    private boolean mRegisterJavaCrash = false;
    private boolean mJavaCrashEnable = true;
    private boolean mNativeCrashEnable = true;
    private boolean mANREnable = true;

    public static void setDefaultCommonParams(ICommonParams iCommonParams, Context context) {
        Jb1.f = new AO0(context, iCommonParams, (AO0) null);
    }

    public static void updateDid(String str) {
        AbstractC2063ee1.A().a(new RunnableC0530Jr(str, 0));
    }

    public String getAlogUploadUrl() {
        return this.mAlogUploadUrl;
    }

    public String getAsanReportUploadUrl() {
        return this.mAsanReportUploadUrl;
    }

    public long getBlockInterval() {
        return this.mBlockMonitorInterval;
    }

    public String getConfigUrl() {
        return this.mConfigUrl;
    }

    public String getCoreDumpUrl() {
        return this.mCoreDumpUrl;
    }

    public String getCrashPortraitUploadUrl() {
        return this.mCrashPortraitUploadUrl;
    }

    public N91 getEncryptImpl() {
        return this.mEncryptImpl;
    }

    public String getExceptionUploadUrl() {
        return this.mExceptionUploadUrl;
    }

    public String getFileUploadUrl() {
        return this.mFileUploadUrl;
    }

    public Set<String> getFilterThreadSet() {
        return AbstractC3318nd1.a;
    }

    public String getJavaCrashUploadUrl() {
        return this.mJavaCrashUploadUrl;
    }

    public long getLaunchCrashInterval() {
        return this.mLaunchCrashInterval;
    }

    public String getLaunchCrashUploadUrl() {
        return this.mLaunchCrashUploadUrl;
    }

    public int getLogcatDumpCount() {
        return this.mLogcatDumpCount;
    }

    public int getLogcatLevel() {
        return this.mLogcatLevel;
    }

    public String getNativeCrashUploadUrl() {
        return this.mNativeCrashUploadUrl;
    }

    public String getNativeMemUrl() {
        return this.mNativeMemUrl;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.mThreadPoolExecutor;
    }

    public boolean isAnrEnable() {
        return this.mANREnable;
    }

    public boolean isApmExists() {
        return false;
    }

    public boolean isApmPLusLogEnable() {
        return this.mEnableApmPlusLog;
    }

    public boolean isBlockMonitorEnable() {
        return this.mBlockMonitorEnable;
    }

    public boolean isCrashIgnored(String str) {
        try {
            C0582Kr c0582Kr = new C0582Kr(str);
            if (Qd1.a(c0582Kr)) {
                return true;
            }
            if (AbstractC4635x31.V(Jb1.a)) {
                return Qd1.a(c0582Kr);
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }

    public boolean isDebugMode() {
        return this.mIsDebugMode;
    }

    public boolean isEnsureEnable() {
        return this.mEnsureEnable;
    }

    public boolean isEnsureWithLogcat() {
        return this.mEnsureWithLogcat;
    }

    public boolean isJavaCrashEnable() {
        return this.mJavaCrashEnable;
    }

    public boolean isNativeCrashEnable() {
        return this.mNativeCrashEnable;
    }

    public boolean isNativeCrashMiniDump() {
        return this.mNativeCrashMiniDump;
    }

    public boolean isRegisterJavaCrashEnable() {
        return this.mRegisterJavaCrash;
    }

    public boolean isReportErrorEnable() {
        return this.reportErrorEnable;
    }

    public void setAlogUploadUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mAlogUploadUrl = str;
    }

    public void setAnrEnable(boolean z) {
        this.mANREnable = z;
    }

    public void setApmPLusLogEnable(boolean z) {
        this.mEnableApmPlusLog = z;
    }

    public void setBlockMonitorEnable(boolean z) {
        this.mBlockMonitorEnable = z;
    }

    public void setBlockMonitorInterval(long j) {
        this.mBlockMonitorInterval = j;
    }

    public void setConfigUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mConfigUrl = str;
    }

    public void setCrashPortraitUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mCrashPortraitUploadUrl = str;
    }

    public void setCurrentProcessName(String str) {
        AbstractC0744Nu0.b = str;
    }

    public void setDebugMode(boolean z) {
        this.mIsDebugMode = z;
    }

    public void setEncryptImpl(N91 n91) {
        if (n91 != null) {
            this.mEncryptImpl = n91;
        }
    }

    public void setEnsureEnable(boolean z) {
        this.mEnsureEnable = z;
    }

    public void setEnsureWithLogcat(boolean z) {
        this.mEnsureWithLogcat = z;
    }

    public void setFileUploadUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mCrashPortraitUploadUrl = str;
    }

    public void setJavaCrashEnable(boolean z) {
        this.mJavaCrashEnable = z;
    }

    public void setJavaCrashUploadUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mJavaCrashUploadUrl = str;
    }

    public void setLaunchCrashInterval(long j) {
        if (j > 0) {
            this.mLaunchCrashInterval = j;
        }
    }

    public void setLaunchCrashUrl(String str) {
        String str2;
        if (disableConfigUrl || TextUtils.isEmpty(str)) {
            return;
        }
        this.mExceptionUploadUrl = str;
        int iIndexOf = str.indexOf("//");
        if (iIndexOf == -1) {
            str2 = str.substring(0, str.indexOf("/") + 1) + "monitor/collect/c/exception/dump_collection";
        } else {
            str2 = str.substring(0, str.indexOf("/", iIndexOf + 2) + 1) + "monitor/collect/c/exception/dump_collection";
        }
        this.mLaunchCrashUploadUrl = str2;
    }

    public void setLaunchCrashUrl2(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mLaunchCrashUploadUrl = str;
    }

    public void setLogcatDumpCount(int i) {
        if (i > 0) {
            this.mLogcatDumpCount = i;
        }
    }

    public void setLogcatLevel(int i) {
        if (i < 0 || i > 4) {
            return;
        }
        this.mLogcatLevel = i;
    }

    public void setNativeCrashEnable(boolean z) {
        this.mNativeCrashEnable = z;
    }

    public void setNativeCrashUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mNativeCrashUploadUrl = str;
    }

    public void setRegisterJavaCrashEnable(boolean z) {
        this.mRegisterJavaCrash = z;
    }

    public void setReportErrorEnable(boolean z) {
        this.reportErrorEnable = z;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.mThreadPoolExecutor = threadPoolExecutor;
    }
}
