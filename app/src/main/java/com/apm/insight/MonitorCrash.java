package com.apm.insight;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.apm.insight.runtime.ConfigManager;
import defpackage.AbstractC1208Ws0;
import defpackage.AbstractC2063ee1;
import defpackage.AbstractC4598wn0;
import defpackage.C1913db;
import defpackage.C2603iX;
import defpackage.C4075t31;
import defpackage.H51;
import defpackage.InterfaceC2179fV;
import defpackage.Jb1;
import defpackage.Ld1;
import defpackage.Ud1;
import defpackage.XM0;
import defpackage.Y41;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class MonitorCrash {
    private static final String TAG = "MonitorCrash";
    private static volatile boolean sAppMonitorCrashInit = false;
    static String sDefaultApplogUrl;
    private volatile boolean isAppLogInit;
    volatile C2603iX mApmApplogConfig;
    Config mConfig;
    private Context mContext;
    AttachUserData mCustomData;
    AttachUserData mCustomLongData;
    private boolean mIsApp;
    HeaderParams mParams;
    private AtomicBoolean mStarted;
    Map<String, String> mTagMap;

    public static class Config {
        public static boolean v = false;
        public String a;
        public String b;
        public String c;
        public String e;
        public String[] f;
        public String[] g;
        public AttachUserData h;
        public String i;
        public String j;
        public String k;
        public C2603iX l;
        public String n;
        public IDynamicParams o;
        public long d = -1;
        public boolean m = false;
        public Map p = null;
        public boolean q = true;
        public boolean r = false;
        public boolean s = true;
        public boolean t = false;
        public boolean u = true;

        public static class Builder {
            public Config a;

            public Builder autoStart(boolean z) {
                this.a.u = z;
                return this;
            }

            public Config build() {
                return this.a;
            }

            public Builder channel(String str) {
                this.a.c = str;
                return this;
            }

            public Builder crashCountOneStart(int i) {
                if (i > 0) {
                    C4075t31.o = i;
                    return this;
                }
                C4075t31 c4075t31 = C4075t31.l;
                return this;
            }

            public Builder crashProtect(boolean z) {
                Jb1.r = z;
                return this;
            }

            public Builder crashWaitTime(long j) {
                C4075t31.p = j;
                return this;
            }

            public Builder customData(AttachUserData attachUserData) {
                this.a.h = attachUserData;
                return this;
            }

            public Builder customFile(CrashInfoCallback crashInfoCallback) {
                CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) Ud1.f.g;
                if (!copyOnWriteArrayList.contains(crashInfoCallback)) {
                    copyOnWriteArrayList.add(crashInfoCallback);
                }
                return this;
            }

            public Builder customPageView(boolean z) {
                this.a.r = z;
                return this;
            }

            public Builder debugMode(boolean z) {
                Npth.getConfigManager().setDebugMode(z);
                return this;
            }

            public Builder disableSigQuit() {
                Jb1.y = false;
                return this;
            }

            public Builder dynamicParams(IDynamicParams iDynamicParams) {
                this.a.o = iDynamicParams;
                return this;
            }

            public Builder enableAnrInfo(boolean z) {
                Jb1.u = z;
                return this;
            }

            public Builder enableApmPlusLog(boolean z) {
                Npth.getConfigManager().setApmPLusLogEnable(z);
                return this;
            }

            public Builder enableOptimizer(boolean z) {
                Jb1.x = z;
                return this;
            }

            public Builder exitType(ExitType exitType) {
                Jb1.q = exitType.type;
                return this;
            }

            public Builder fixPageView(boolean z) {
                this.a.t = z;
                return this;
            }

            public Builder keyThread(String str) {
                Jb1.A = str;
                return this;
            }

            public Builder looperMonitor(boolean z) {
                Jb1.t = z;
                return this;
            }

            public Builder networkClient(InterfaceC2179fV interfaceC2179fV) {
                if (interfaceC2179fV != null) {
                    Jb1.p = interfaceC2179fV;
                    C1913db.j = interfaceC2179fV;
                }
                this.a.q = false;
                return this;
            }

            public Builder pageViewTags(Map<String, String> map) {
                this.a.p = map;
                return this;
            }

            public Builder token(String str) {
                this.a.b = str;
                return this;
            }

            public Builder traceDump(boolean z) {
                Jb1.s = z && Jb1.s;
                return this;
            }

            public Builder url(String str) {
                Config config = this.a;
                config.n = str;
                config.q = false;
                return this;
            }

            public Builder versionCode(long j) {
                this.a.d = j;
                return this;
            }

            public Builder versionName(String str) {
                this.a.e = str;
                return this;
            }
        }

        public interface IDynamicParams {
            String getDid();

            String getUserId();
        }

        public static class SdkBuilder {
            public Config a;

            public SdkBuilder acceptWithActivity(boolean z) {
                this.a.m = z;
                return this;
            }

            public SdkBuilder autoStart(boolean z) {
                this.a.u = z;
                return this;
            }

            public Config build() {
                return this.a;
            }

            public SdkBuilder channel(String str) {
                this.a.c = str;
                return this;
            }

            public SdkBuilder customData(AttachUserData attachUserData) {
                this.a.h = attachUserData;
                return this;
            }

            public SdkBuilder customPageView(boolean z) {
                this.a.r = z;
                return this;
            }

            public SdkBuilder debugMode(boolean z) {
                Npth.getConfigManager().setDebugMode(z);
                return this;
            }

            public SdkBuilder disablePageView() {
                this.a.s = false;
                return this;
            }

            public SdkBuilder disableSelfMonitor() {
                this.a.q = false;
                return this;
            }

            public SdkBuilder disableSigQuit() {
                Jb1.y = false;
                return this;
            }

            public SdkBuilder dynamicParams(IDynamicParams iDynamicParams) {
                this.a.o = iDynamicParams;
                return this;
            }

            public SdkBuilder enableAnrInfo(boolean z) {
                Jb1.u = z;
                return this;
            }

            public SdkBuilder enableAnrMonitor(boolean z) {
                Npth.getConfigManager().setAnrEnable(z);
                return this;
            }

            public SdkBuilder enableJavaCrash(boolean z) {
                Npth.getConfigManager().setJavaCrashEnable(z);
                return this;
            }

            public SdkBuilder enableNativeCrash(boolean z) {
                Npth.getConfigManager().setNativeCrashEnable(z);
                return this;
            }

            public SdkBuilder enableRegisterJavaCrash(boolean z) {
                Npth.getConfigManager().setRegisterJavaCrashEnable(z);
                return this;
            }

            public SdkBuilder fixPageView(boolean z) {
                this.a.t = z;
                return this;
            }

            public SdkBuilder keyWords(String... strArr) {
                this.a.f = strArr;
                return this;
            }

            public SdkBuilder looperMonitor(boolean z) {
                Jb1.t = z;
                return this;
            }

            public SdkBuilder pageViewTags(Map<String, String> map) {
                this.a.p = map;
                return this;
            }

            public SdkBuilder soList(String... strArr) {
                this.a.g = strArr;
                return this;
            }

            public SdkBuilder token(String str) {
                this.a.b = str;
                return this;
            }

            public SdkBuilder traceDump(boolean z) {
                Jb1.s = z && Jb1.s;
                return this;
            }

            public SdkBuilder url(String str) {
                this.a.n = str;
                return disableSelfMonitor();
            }

            public SdkBuilder versionCode(long j) {
                this.a.d = j;
                return this;
            }

            public SdkBuilder versionName(String str) {
                this.a.e = str;
                return this;
            }
        }

        public static Builder app(String str) {
            Builder builder = new Builder();
            Config config = new Config();
            builder.a = config;
            config.a = str;
            return builder;
        }

        public static void disableConfigUrl() {
            v = true;
            ConfigManager.disableConfigUrl = true;
        }

        public static SdkBuilder sdk(String str) {
            SdkBuilder sdkBuilder = new SdkBuilder();
            Config config = new Config();
            sdkBuilder.a = config;
            config.a = str;
            return sdkBuilder;
        }

        public String getDeviceId() {
            try {
                IDynamicParams iDynamicParams = this.o;
                String did = iDynamicParams == null ? "" : iDynamicParams.getDid();
                return TextUtils.isEmpty(did) ? this.i : did;
            } catch (Throwable unused) {
                return this.i;
            }
        }

        public String getSSID() {
            return this.k;
        }

        public String getUID() {
            try {
                IDynamicParams iDynamicParams = this.o;
                return iDynamicParams == null ? this.j : iDynamicParams.getUserId();
            } catch (Throwable unused) {
                return this.j;
            }
        }

        public Config setChannel(String str) {
            this.c = str;
            C2603iX c2603iX = this.l;
            if (c2603iX != null) {
                c2603iX.c = str;
            }
            H51.a();
            return this;
        }

        @Deprecated
        public Config setDeviceId(String str) {
            return setDeviceId(str, true);
        }

        public Config setPackageName(String str) {
            return setPackageName(str);
        }

        @Deprecated
        public Config setSSID(String str) {
            this.k = str;
            H51.a();
            return this;
        }

        public Config setSoList(String[] strArr) {
            this.g = strArr;
            H51.a();
            return this;
        }

        @Deprecated
        public Config setUID(String str) {
            this.j = str;
            H51.a();
            return this;
        }

        @Deprecated
        public Config setDeviceId(String str, boolean z) {
            this.i = str;
            C2603iX c2603iX = this.l;
            if (c2603iX != null) {
                c2603iX.l = str;
            }
            if (z) {
                H51.a();
            }
            return this;
        }

        public Config setPackageName(String... strArr) {
            this.f = strArr;
            H51.a();
            return this;
        }
    }

    @Deprecated
    public interface HeaderParams {
        Map<String, Object> getCommonParams();
    }

    private MonitorCrash(Context context, Config config) {
        this.mTagMap = new ConcurrentHashMap();
        this.isAppLogInit = false;
        this.mStarted = new AtomicBoolean(false);
        this.mContext = context;
        this.mConfig = config;
        this.mCustomData = config.h;
    }

    private static void checkInit(String str) {
        if (c.b == null || c.b.mConfig == null || TextUtils.equals(c.b.mConfig.a, str)) {
            return;
        }
        if (Jb1.g.isDebugMode()) {
            throw new RuntimeException("Duplicate init App MonitorCrash with different aids.");
        }
        Log.e(TAG, "Duplicate init App MonitorCrash with different aids.");
    }

    @Deprecated
    public static MonitorCrash init(Context context, String str, long j, String str2) {
        if (!sAppMonitorCrashInit) {
            synchronized (MonitorCrash.class) {
                try {
                    if (!sAppMonitorCrashInit) {
                        Ud1.b(context, Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isNativeCrashEnable(), Npth.getConfigManager().isAnrEnable());
                        MonitorCrash monitorCrash = new MonitorCrash((Config) null, context, str, j, str2);
                        sAppMonitorCrashInit = true;
                        return monitorCrash;
                    }
                } finally {
                }
            }
        }
        checkInit(str);
        return c.b;
    }

    private void initAppLog(Context context, boolean z) {
        synchronized (this) {
            try {
                if (this.mApmApplogConfig == null) {
                    Config config = this.mConfig;
                    this.mApmApplogConfig = new C2603iX(config.a, config.b, "empty");
                    this.mConfig.l = this.mApmApplogConfig;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        initAppLogAsync(context, z);
    }

    private void initAppLogAsync(Context context, boolean z) {
        AbstractC2063ee1.A().b(new a(this, z, context), 5L);
    }

    public static synchronized MonitorCrash initSDK(Context context, Config config) {
        try {
            if (TextUtils.isEmpty(config.b)) {
                Log.e(TAG, config.a + " MonitorCrash init without token.");
            }
            MonitorCrash monitorCrash = (MonitorCrash) c.c.get(config.a);
            if (monitorCrash != null) {
                Log.e(TAG, "Duplicate init MonitorCrash with same aid.");
                return monitorCrash;
            }
            MonitorCrash monitorCrash2 = new MonitorCrash(context, config);
            monitorCrash2.mIsApp = false;
            if (sDefaultApplogUrl == null && !TextUtils.isEmpty(config.n)) {
                String str = config.n;
                sDefaultApplogUrl = str;
                monitorCrash2.setReportUrlInner(str);
            }
            if (!config.q) {
                Ld1.b = false;
            }
            Map<? extends String, ? extends String> map = config.p;
            if (map != null) {
                monitorCrash2.mTagMap.putAll(map);
            }
            if (config.u) {
                monitorCrash2.start();
            }
            return monitorCrash2;
        } catch (Throwable th) {
            throw th;
        }
    }

    @Deprecated
    public static MonitorCrash initSDKWithConfig(Context context, Config config, String str, long j, String str2, String str3, String[] strArr) {
        Ud1.b(context, Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isNativeCrashEnable(), Npth.getConfigManager().isAnrEnable());
        MonitorCrash monitorCrash = new MonitorCrash(config, str, j, str2, str3);
        monitorCrash.config().setPackageName(str3).setSoList(strArr);
        return monitorCrash;
    }

    @Deprecated
    public static MonitorCrash initWithConfig(Context context, Config config, String str, long j, String str2) {
        if (!sAppMonitorCrashInit) {
            synchronized (MonitorCrash.class) {
                try {
                    if (!sAppMonitorCrashInit) {
                        Ud1.b(context, Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isNativeCrashEnable(), Npth.getConfigManager().isAnrEnable());
                        MonitorCrash monitorCrash = new MonitorCrash(config, context, str, j, str2);
                        sAppMonitorCrashInit = true;
                        return monitorCrash;
                    }
                } finally {
                }
            }
        }
        checkInit(str);
        return c.b;
    }

    public static void reInitAppLog(String str) {
        MonitorCrash monitorCrash;
        boolean z;
        try {
            if (!TextUtils.isEmpty(str) && C1913db.c(str) == null) {
                if (c.b == null || !TextUtils.equals(str, c.b.mConfig.a)) {
                    monitorCrash = (MonitorCrash) c.c.get(str);
                    z = false;
                } else {
                    monitorCrash = c.b;
                    z = true;
                }
                if (monitorCrash != null && !monitorCrash.isAppLogInit) {
                    Application application = Jb1.b;
                    if (monitorCrash.mApmApplogConfig == null) {
                        monitorCrash.initAppLog(application, z);
                    } else {
                        monitorCrash.initAppLogAsync(application, z);
                    }
                }
            }
        } catch (Throwable unused) {
        }
    }

    @Deprecated
    public static void setDefaultReportUrlPrefix(String str) {
        if (Config.v) {
            return;
        }
        ConfigManager configManager = Jb1.g;
        configManager.setLaunchCrashUrl(str + ConfigManager.EXCEPTION_URL_SUFFIX);
        configManager.setLaunchCrashUrl2(str + ConfigManager.LAUNCH_URL_SUFFIX);
        configManager.setJavaCrashUploadUrl(str + ConfigManager.JAVA_URL_SUFFIX);
        configManager.setNativeCrashUrl(str + ConfigManager.NATIVE_URL_SUFFIX);
        configManager.setConfigUrl(str + ConfigManager.CONFIG_URL_SUFFIX);
        configManager.setAlogUploadUrl(str + ConfigManager.ALOG_URL_SUFFIX);
        configManager.setFileUploadUrl(str + ConfigManager.FILE_UPLOAD_URL_SUFFIX);
        configManager.setCrashPortraitUrl(str + ConfigManager.PORTRAIT_UPLOAD_URL_SUFFIX);
        sDefaultApplogUrl = str;
    }

    private MonitorCrash setReportUrlInner(String str) {
        if (!Config.v && !TextUtils.isEmpty(str)) {
            if (str.indexOf("://") < 0) {
                str = "https://".concat(str);
            }
            AbstractC1208Ws0.c("set url " + str);
            ConfigManager configManager = Jb1.g;
            configManager.setLaunchCrashUrl(str + ConfigManager.EXCEPTION_URL_SUFFIX);
            configManager.setLaunchCrashUrl2(str + ConfigManager.LAUNCH_URL_SUFFIX);
            configManager.setJavaCrashUploadUrl(str + ConfigManager.JAVA_URL_SUFFIX);
            configManager.setNativeCrashUrl(str + ConfigManager.NATIVE_URL_SUFFIX);
            configManager.setConfigUrl(str + ConfigManager.CONFIG_URL_SUFFIX);
            configManager.setAlogUploadUrl(str + ConfigManager.ALOG_URL_SUFFIX);
            configManager.setFileUploadUrl(str + ConfigManager.FILE_UPLOAD_URL_SUFFIX);
            configManager.setFileUploadUrl(str + ConfigManager.PORTRAIT_UPLOAD_URL_SUFFIX);
            sDefaultApplogUrl = str;
        }
        return this;
    }

    public MonitorCrash addTags(String str, String str2) {
        this.mTagMap.put(str, str2);
        return this;
    }

    public Config config() {
        return this.mConfig;
    }

    public Map<String, String> getPvTags() {
        Config config = this.mConfig;
        if (config == null) {
            return null;
        }
        return config.p;
    }

    public Map<String, String> getTags() {
        return this.mTagMap;
    }

    public void registerCrashCallback(ICrashCallback iCrashCallback, CrashType crashType) {
        Ud1.c(iCrashCallback, crashType);
    }

    public void registerOOMCallback(IOOMCallback iOOMCallback) {
        ((CopyOnWriteArrayList) Ud1.f.f).add(iOOMCallback);
    }

    public void reportCustomErr(String str, String str2, Throwable th) {
        reportCustomErr(str, str2, th, null);
    }

    @Deprecated
    public MonitorCrash setCustomDataCallback(AttachUserData attachUserData) {
        this.mCustomData = attachUserData;
        return this;
    }

    @Deprecated
    public MonitorCrash setReportUrl(String str) {
        return this;
    }

    public void start() {
        Context context;
        if (!this.mStarted.compareAndSet(false, true) || this.mConfig == null || (context = this.mContext) == null) {
            return;
        }
        Ud1.b(context, Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isNativeCrashEnable(), Npth.getConfigManager().isAnrEnable());
        if (this.mConfig.s) {
            initAppLog(this.mContext, this.mIsApp);
        }
        if (this.mIsApp) {
            c.e(this.mContext, this);
            return;
        }
        new c(this);
        if (this.mConfig != null) {
            c.c.put(this.mConfig.a, this);
        }
    }

    public void unregisterCrashCallback(ICrashCallback iCrashCallback, CrashType crashType) {
        Ud1.d(iCrashCallback, crashType);
    }

    public void unregisterOOMCallback(IOOMCallback iOOMCallback, CrashType crashType) {
        ((CopyOnWriteArrayList) Ud1.f.f).remove(iOOMCallback);
    }

    @Deprecated
    public MonitorCrash withOtherHeaders(HeaderParams headerParams) {
        this.mParams = headerParams;
        return this;
    }

    private MonitorCrash(Config config, Context context, String str, long j, String str2) {
        this.mTagMap = new ConcurrentHashMap();
        this.isAppLogInit = false;
        this.mStarted = new AtomicBoolean(false);
        config = config == null ? new Config() : config;
        this.mConfig = config;
        config.a = str;
        config.d = j;
        config.e = str2;
        c.e(context, this);
        initAppLog(context, true);
    }

    public static synchronized MonitorCrash init(Context context, Config config) {
        try {
            if (!sAppMonitorCrashInit) {
                if (TextUtils.isEmpty(config.b)) {
                    Log.e(TAG, "MonitorCrash init without token.");
                }
                MonitorCrash monitorCrash = new MonitorCrash(context, config);
                monitorCrash.mIsApp = true;
                if (TextUtils.isEmpty(config.e)) {
                    try {
                        config.e = AbstractC4598wn0.c(context);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                if (config.d == -1) {
                    try {
                        ConcurrentHashMap concurrentHashMap = AbstractC4598wn0.a;
                        config.d = AbstractC4598wn0.b(0, context, context.getPackageName()) != null ? r7.versionCode : 0;
                    } catch (Throwable th2) {
                        th2.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(config.n)) {
                    String str = config.n;
                    sDefaultApplogUrl = str;
                    monitorCrash.setReportUrlInner(str);
                }
                if (!config.q) {
                    Ld1.b = false;
                }
                Map<? extends String, ? extends String> map = config.p;
                if (map != null) {
                    monitorCrash.mTagMap.putAll(map);
                }
                if (config.u) {
                    monitorCrash.start();
                } else {
                    c.b = monitorCrash;
                }
                sAppMonitorCrashInit = true;
            }
            checkInit(config.a);
        } catch (Throwable th3) {
            throw th3;
        }
        return c.b;
    }

    @Deprecated
    public static MonitorCrash initSDKWithConfig(Context context, Config config, String str, long j, String str2, String... strArr) {
        Ud1.b(context, Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isNativeCrashEnable(), Npth.getConfigManager().isAnrEnable());
        MonitorCrash monitorCrash = new MonitorCrash(config, str, j, str2, strArr);
        monitorCrash.config().setPackageName(strArr);
        return monitorCrash;
    }

    public void reportCustomErr(String str, String str2, Throwable th, Map<String, String> map) {
        if (TextUtils.isEmpty(str2)) {
            str2 = "EnsureNotReachHere";
        }
        try {
            AbstractC2063ee1.A().a(new XM0(this, th, str, map, str2));
        } catch (Throwable unused) {
        }
    }

    private MonitorCrash(Config config, String str, long j, String str2, String... strArr) {
        this.mTagMap = new ConcurrentHashMap();
        this.isAppLogInit = false;
        this.mStarted = new AtomicBoolean(false);
        config = config == null ? new Config() : config;
        this.mConfig = config;
        config.a = str;
        config.d = j;
        config.e = str2;
        config.f = strArr;
        new c(this);
        if (this.mConfig != null) {
            c.c.put(this.mConfig.a, this);
        }
        initAppLog(Jb1.a, false);
    }

    @Deprecated
    public static MonitorCrash initSDKWithConfig(Context context, Config config, String str, long j, String str2, String[] strArr, String[] strArr2) {
        Ud1.b(context, Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isNativeCrashEnable(), Npth.getConfigManager().isAnrEnable());
        MonitorCrash monitorCrash = new MonitorCrash(config, str, j, str2, strArr);
        monitorCrash.config().setPackageName(strArr).setSoList(strArr2);
        return monitorCrash;
    }

    public void reportCustomErr(StackTraceElement[] stackTraceElementArr, int i, String str, String str2, Map<String, String> map) {
        if (TextUtils.isEmpty(str2)) {
            str2 = "EnsureNotReachHere";
        }
        try {
            AbstractC2063ee1.A().a(new Y41(stackTraceElementArr, i, str, str2, map));
        } catch (Throwable unused) {
        }
    }

    private MonitorCrash(String str, long j, String str2, String... strArr) {
        this((Config) null, str, j, str2, strArr);
    }

    @Deprecated
    public static MonitorCrash initSDK(Context context, String str, long j, String str2, String str3) {
        Ud1.b(context, Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isNativeCrashEnable(), Npth.getConfigManager().isAnrEnable());
        MonitorCrash monitorCrash = new MonitorCrash(str, j, str2, str3);
        monitorCrash.config().setPackageName(str3);
        return monitorCrash;
    }

    @Deprecated
    public static MonitorCrash initSDK(Context context, String str, long j, String str2, String str3, String[] strArr) {
        Ud1.b(context, Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isNativeCrashEnable(), Npth.getConfigManager().isAnrEnable());
        MonitorCrash monitorCrash = new MonitorCrash(str, j, str2, str3);
        monitorCrash.config().setPackageName(str3).setSoList(strArr);
        return monitorCrash;
    }

    @Deprecated
    public static MonitorCrash initSDK(Context context, String str, long j, String str2, String... strArr) {
        Ud1.b(context, Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isNativeCrashEnable(), Npth.getConfigManager().isAnrEnable());
        MonitorCrash monitorCrash = new MonitorCrash(str, j, str2, strArr);
        monitorCrash.config().setPackageName(strArr);
        return monitorCrash;
    }

    @Deprecated
    public static MonitorCrash initSDK(Context context, String str, long j, String str2, String[] strArr, String[] strArr2) {
        Ud1.b(context, Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isJavaCrashEnable(), Npth.getConfigManager().isNativeCrashEnable(), Npth.getConfigManager().isAnrEnable());
        MonitorCrash monitorCrash = new MonitorCrash(str, j, str2, strArr);
        monitorCrash.config().setPackageName(strArr).setSoList(strArr2);
        return monitorCrash;
    }
}
