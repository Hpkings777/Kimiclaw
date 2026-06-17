package com.apm.insight.nativecrash;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Keep;
import android.text.TextUtils;
import defpackage.AbstractC0541Jw0;
import defpackage.AbstractC2063ee1;
import defpackage.De1;
import defpackage.Jb1;
import defpackage.Kb1;
import defpackage.RunnableC2535i21;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class NativeImpl {
    public static volatile boolean a = false;
    public static volatile boolean b = false;
    public static boolean c = true;

    public static void A() {
        if (a) {
            doInitThreadDump();
        }
    }

    public static void B() {
        if (a) {
            try {
                doStartAnrMonitor(Build.VERSION.SDK_INT);
            } catch (Throwable unused) {
            }
        }
    }

    public static String a(String str) {
        if (a) {
            return doGetCrashHeader(str);
        }
        return null;
    }

    public static void b(int i) {
        if (!a || i < 0) {
            return;
        }
        try {
            doLock("", i);
        } catch (Throwable unused) {
        }
    }

    public static void c(int i, String str) {
        if (!a || TextUtils.isEmpty(str)) {
            return;
        }
        try {
            doWriteFile(i, str, str.length());
        } catch (Throwable unused) {
        }
    }

    public static void d(File file) {
        if (a) {
            doRebuildTombstone(new File(file, "header.bin").getAbsolutePath(), new File(file, "tombstone.txt").getAbsolutePath(), new File(AbstractC0541Jw0.h(Jb1.a, file.getName()), "maps.txt").getAbsolutePath());
        }
    }

    @Keep
    private static native boolean doCheckNativeCrash();

    @Keep
    private static native void doCloseFile(int i);

    @Keep
    private static native int doCreateCallbackThread();

    @Keep
    private static native void doDump(String str);

    @Keep
    private static native void doDumpFds(String str);

    @Keep
    private static native void doDumpHprof(String str);

    @Keep
    private static native void doDumpLogcat(String str, String str2, String str3);

    @Keep
    private static native void doDumpMaps(String str);

    @Keep
    private static native void doDumpMemInfo(String str);

    @Keep
    private static native void doDumpThreads(String str);

    @Keep
    private static native long doGetAppCpuTime();

    @Keep
    private static native long doGetChildCpuTime();

    @Keep
    private static native String doGetCrashHeader(String str);

    @Keep
    private static native long doGetDeviceCpuTime();

    @Keep
    private static native int doGetFDCount();

    @Keep
    private static native String[] doGetFdDump(int i, int i2, int[] iArr, String[] strArr);

    @Keep
    private static native long doGetFreeMemory();

    @Keep
    private static native long doGetThreadCpuTime(int i);

    @Keep
    private static native int doGetThreadsCount();

    @Keep
    private static native long doGetTotalMemory();

    @Keep
    private static native long doGetVMSize();

    @Keep
    private static native void doInitThreadDump();

    @Keep
    private static native int doLock(String str, int i);

    @Keep
    private static native int doOpenFile(String str);

    @Keep
    private static native void doRebuildTombstone(String str, String str2, String str3);

    @Keep
    private static native void doSetAlogConfigPath(String str);

    @Keep
    private static native void doSetAlogFlushAddr(long j);

    @Keep
    private static native void doSetAlogLogDirAddr(long j);

    @Keep
    private static native void doSetResendSigQuit(int i);

    @Keep
    private static native void doSetUploadEnd();

    @Keep
    private static native void doSignalMainThread();

    @Keep
    private static native int doStart(int i, String str, String str2, String str3, int i2);

    @Keep
    private static native void doStartAnrMonitor(int i);

    @Keep
    private static native void doWriteFile(int i, String str, int i2);

    public static void e(String str, String str2, String str3) {
        if (a && Jb1.z) {
            try {
                doDumpLogcat(str, str2, str3);
            } catch (Throwable unused) {
            }
        }
    }

    public static void f(boolean z) {
        c = z;
        if (a) {
            doSetResendSigQuit(z ? 1 : 0);
        }
    }

    public static boolean g() {
        if (b) {
            return a;
        }
        boolean z = true;
        b = true;
        if (!a) {
            try {
                System.loadLibrary("apminsighta");
            } catch (Throwable unused) {
                z = false;
            }
            a = z;
        }
        return a;
    }

    public static boolean h(Context context) {
        String str;
        boolean zG = g();
        if (zG) {
            String str2 = AbstractC0541Jw0.v(context) + "/apminsight";
            new File(str2).mkdirs();
            if (new File(context.getApplicationInfo().nativeLibraryDir, "libapminsightb.so").exists()) {
                str = context.getApplicationInfo().nativeLibraryDir;
            } else {
                str = Jb1.a.getFilesDir() + "/apminsight/selflib/";
                De1 de1A = AbstractC2063ee1.A();
                RunnableC2535i21 runnableC2535i21 = new RunnableC2535i21();
                runnableC2535i21.b = false;
                de1A.a(runnableC2535i21);
            }
            doStart(Build.VERSION.SDK_INT, str, str2, Jb1.g(), Jb1.o);
        }
        return zG;
    }

    @Keep
    private static void handleNativeCrash(String str) {
        NativeCrashCollector.onNativeCrash(str);
    }

    public static int i() {
        if (a) {
            return doCreateCallbackThread();
        }
        return -1;
    }

    @Keep
    private static native boolean is64Bit();

    public static void j(int i) {
        if (a) {
            try {
                doCloseFile(i);
            } catch (Throwable unused) {
            }
        }
    }

    public static void k(long j) {
        if (a) {
            try {
                doSetAlogFlushAddr(j);
            } catch (Throwable unused) {
            }
        }
    }

    public static void l(String str) {
        if (a) {
            doDumpHprof(str);
        }
    }

    public static int m(String str) {
        if (a && !TextUtils.isEmpty(str)) {
            try {
                return doLock(str, -1);
            } catch (Throwable unused) {
            }
        }
        return -1;
    }

    public static long n(int i) {
        if (a) {
            return doGetThreadCpuTime(i);
        }
        return 0L;
    }

    public static void o() {
        if (a) {
            try {
                String strG = Jb1.g();
                File file = new File(AbstractC0541Jw0.v(Jb1.a), "apminsight/alogCrash");
                file.mkdirs();
                doSetAlogConfigPath(file.getPath() + "/native_" + strG + ".atmp");
            } catch (Throwable unused) {
            }
        }
    }

    public static void p(long j) {
        if (a) {
            try {
                doSetAlogLogDirAddr(j);
            } catch (Throwable unused) {
            }
        }
    }

    public static void q(String str) {
        if (a) {
            try {
                doDumpMemInfo(str);
            } catch (Throwable unused) {
            }
        }
    }

    public static boolean r() {
        if (!a) {
            return false;
        }
        try {
            return doCheckNativeCrash();
        } catch (Throwable unused) {
            return false;
        }
    }

    @Keep
    private static void reportEventForAnrMonitor() {
        try {
            System.currentTimeMillis();
            Context context = Jb1.a;
            Kb1.y();
        } catch (Throwable unused) {
        }
    }

    public static void s(String str) {
        if (a) {
            try {
                doDumpFds(str);
            } catch (Throwable unused) {
            }
        }
    }

    public static boolean t() {
        if (!a) {
            return false;
        }
        try {
            return is64Bit();
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void u(String str) {
        if (a) {
            try {
                doDumpMaps(str);
            } catch (Throwable unused) {
            }
        }
    }

    public static void v(String str) {
        if (a) {
            try {
                doDumpThreads(str);
            } catch (Throwable unused) {
            }
        }
    }

    public static int w(String str) {
        if (!a) {
            return -1;
        }
        try {
            return doOpenFile(str);
        } catch (Throwable unused) {
            return -1;
        }
    }

    public static void x() {
        if (a) {
            doSignalMainThread();
        }
    }

    public static void y() {
        if (a) {
            doSetUploadEnd();
        }
    }

    public static void z(String str) {
        if (a) {
            if (!Jb1.y) {
                f(false);
            }
            doDump(str);
        }
    }
}
