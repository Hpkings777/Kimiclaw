package com.apm.insight.log;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import com.apm.insight.log.a.a;
import com.apm.insight.log.a.g;
import defpackage.AbstractC2039eV;
import defpackage.AbstractC4671xI0;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.commons.io.IOUtils;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static int a = 3;
    private static VLogConfig b = null;
    private static volatile boolean c = false;
    private static volatile Set<String> d = null;
    private static volatile boolean e = false;
    private static com.apm.insight.log.a.a f;
    private static HandlerThread h;
    private static Handler i;
    private static HashMap<String, com.apm.insight.log.a.a> g = new HashMap<>();
    private static long j = -1;
    private static boolean k = false;
    private static Object l = new Object();

    /* JADX INFO: renamed from: com.apm.insight.log.a$a, reason: collision with other inner class name */
    public static class C0012a {
        private static final Object i = new Object();
        private static C0012a j;
        private static int k;
        public int a;
        public String b;
        public String c;
        public Throwable d;
        public int e = 0;
        public Object f;
        public long g;
        public long h;
        private C0012a l;

        private C0012a() {
        }

        public static C0012a a() {
            synchronized (i) {
                try {
                    C0012a c0012a = j;
                    if (c0012a == null) {
                        return new C0012a();
                    }
                    j = c0012a.l;
                    c0012a.l = null;
                    k--;
                    return c0012a;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public final void b() {
            this.b = null;
            this.c = null;
            this.d = null;
            this.e = 0;
            this.f = null;
            this.g = -1L;
            this.h = 0L;
            this.l = null;
            synchronized (i) {
                try {
                    int i2 = k;
                    if (i2 < 50) {
                        this.l = j;
                        j = this;
                        k = i2 + 1;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static class b implements ILog {
        private final com.apm.insight.log.a.a a;

        public b(com.apm.insight.log.a.a aVar) {
            this.a = aVar;
        }

        @Override // com.apm.insight.log.ILog
        public final void asyncFlush() {
            this.a.b();
        }

        @Override // com.apm.insight.log.ILog
        public final void d(String str, String str2) {
            if (a.b(3, str)) {
                this.a.b(str, str2);
            }
        }

        @Override // com.apm.insight.log.ILog
        public final void e(String str, String str2) {
            if (a.b(6, str)) {
                this.a.e(str, str2);
            }
        }

        @Override // com.apm.insight.log.ILog
        public final List<String> getFiles(long j, long j2) throws Throwable {
            ArrayList arrayList = new ArrayList();
            try {
                File[] fileArrA = this.a.a(j * 1000, j2 * 1000);
                for (File file : fileArrA) {
                    arrayList.add(file.getAbsolutePath());
                }
            } catch (Exception unused) {
            }
            return arrayList;
        }

        @Override // com.apm.insight.log.ILog
        public final List<String> getFilesOfAllProcesses(long j, long j2) {
            ArrayList arrayList = new ArrayList();
            try {
                File[] fileArrA = this.a.a((String) null, j * 1000, j2 * 1000);
                for (File file : fileArrA) {
                    arrayList.add(file.getAbsolutePath());
                }
            } catch (Exception unused) {
            }
            return arrayList;
        }

        @Override // com.apm.insight.log.ILog
        public final long getNativeRef() {
            return this.a.j();
        }

        @Override // com.apm.insight.log.ILog
        public final void i(String str, String str2) {
            if (a.b(4, str)) {
                this.a.c(str, str2);
            }
        }

        @Override // com.apm.insight.log.ILog
        public final void syncFlush() {
            this.a.c();
        }

        @Override // com.apm.insight.log.ILog
        public final void timedSyncFlush(int i) {
            this.a.a(i);
        }

        @Override // com.apm.insight.log.ILog
        public final void v(String str, String str2) {
            if (a.b(2, str)) {
                this.a.a(str, str2);
            }
        }

        @Override // com.apm.insight.log.ILog
        public final void w(String str, String str2) {
            if (a.b(5, str)) {
                this.a.d(str, str2);
            }
        }
    }

    public static void a(ScheduledExecutorService scheduledExecutorService) {
    }

    public static Set<String> b() {
        return d;
    }

    public static void c(String str, String str2) {
        if (b(4, str)) {
            boolean zA = c.a();
            if (zA && i != null) {
                a(4, str, str2);
                return;
            }
            com.apm.insight.log.a.a aVar = f;
            if (aVar == null || !zA) {
                g.c(str, str2);
            } else {
                aVar.c(str, str2);
            }
        }
    }

    public static void d(String str, String str2) {
        if (b(5, str)) {
            boolean zA = c.a();
            if (zA && i != null) {
                a(5, str, str2);
                return;
            }
            com.apm.insight.log.a.a aVar = f;
            if (aVar == null || !zA) {
                g.d(str, str2);
            } else {
                aVar.d(str, str2);
            }
        }
    }

    public static void e(String str, String str2) {
        if (b(6, str)) {
            boolean zA = c.a();
            if (zA && i != null) {
                a(6, str, str2);
                return;
            }
            com.apm.insight.log.a.a aVar = f;
            if (aVar == null || !zA) {
                g.e(str, str2);
            } else {
                aVar.e(str, str2);
            }
        }
    }

    public static void f() {
        g.a();
        com.apm.insight.log.a.a aVar = f;
        if (aVar != null) {
            aVar.a();
        }
        if (i != null) {
            h.quit();
            h = null;
            i = null;
        }
    }

    public static void g() {
        g.a();
        com.apm.insight.log.a.a aVar = f;
        if (aVar != null) {
            aVar.a();
        }
        if (i != null) {
            h.quit();
            h = null;
            i = null;
        }
    }

    public static long h() {
        return g.e();
    }

    public static long i() {
        return g.f();
    }

    public static long j() {
        return g.g();
    }

    public static long k() {
        return g.h();
    }

    private static void l() {
        if (j == -1) {
            j = Process.myTid();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean b(int i2, String str) {
        if (i2 < a) {
            return false;
        }
        return d == null || TextUtils.isEmpty(str) || !d.contains(str);
    }

    public static boolean a(VLogConfig vLogConfig) throws Throwable {
        boolean z = false;
        if (vLogConfig == null) {
            return false;
        }
        b = vLogConfig;
        try {
            com.apm.insight.log.a.a.a(new c());
            synchronized (l) {
                try {
                    if (k) {
                        return false;
                    }
                    k = true;
                    a = vLogConfig.getLevel();
                    boolean zA = c.a(vLogConfig.getContext());
                    boolean zIsOffloadMainThreadWrite = vLogConfig.isOffloadMainThreadWrite();
                    if (!zIsOffloadMainThreadWrite && vLogConfig.isMainThreadSpeedUp() && zA) {
                        z = true;
                    }
                    if (!zA) {
                        vLogConfig.setMaxDirSize((int) (vLogConfig.getSubProcessMaxDirSizeRatio() * vLogConfig.getMaxDirSize()));
                    }
                    a.b bVarF = new a.b(vLogConfig.getContext()).a("default").a(vLogConfig.getLevel() - 2).a(c).b(vLogConfig.getLogDirPath()).b(vLogConfig.getPerSize()).c(z ? (vLogConfig.getMaxDirSize() / 3) << 1 : vLogConfig.getMaxDirSize()).d(vLogConfig.getLogFileExpDays()).c(vLogConfig.getBufferDirPath()).e(zA ? 65536 : 32768).f(zA ? 196608 : 65536);
                    a.d dVar = a.d.a;
                    a.b bVarA = bVarF.a(dVar);
                    a.g gVar = a.g.a;
                    a.b bVarA2 = bVarA.a(gVar);
                    a.e eVar = a.e.b;
                    g.a(bVarA2.a(eVar).a(vLogConfig.isCompress() ? a.c.b : a.c.a).a(vLogConfig.isEncrypt() ? a.f.b : a.f.a).a(vLogConfig.isEncrypt() ? a.EnumC0013a.b : a.EnumC0013a.a).d(vLogConfig.getPubKey()).a());
                    if (zIsOffloadMainThreadWrite && zA) {
                        HandlerThread handlerThread = new HandlerThread("volc_log_delegate");
                        h = handlerThread;
                        handlerThread.start();
                        i = new com.apm.insight.log.b(h.getLooper());
                    }
                    if (z) {
                        f = new a.b(vLogConfig.getContext()).a("main").a(vLogConfig.getLevel() - 2).a(c).b(vLogConfig.getLogDirPath()).b(vLogConfig.getPerSize() / 2).c(vLogConfig.getMaxDirSize() / 3).d(vLogConfig.getLogFileExpDays()).c(vLogConfig.getBufferDirPath()).e(32768).f(98304).a(dVar).a(gVar).a(eVar).a(vLogConfig.isCompress() ? a.c.b : a.c.a).a(vLogConfig.isEncrypt() ? a.f.b : a.f.a).a(vLogConfig.isEncrypt() ? a.EnumC0013a.b : a.EnumC0013a.a).d(vLogConfig.getPubKey()).a();
                    }
                    vLogConfig.getBufferDirPath();
                    vLogConfig.getLogDirPath();
                    e = true;
                    return true;
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void b(String str, String str2) {
        if (b(3, str)) {
            boolean zA = c.a();
            if (zA && i != null) {
                a(3, str, str2);
                return;
            }
            com.apm.insight.log.a.a aVar = f;
            if (aVar != null && zA) {
                aVar.b(str, str2);
            } else {
                g.b(str, str2);
            }
        }
    }

    public static void c() {
        Handler handler = i;
        if (handler != null) {
            handler.sendEmptyMessage(2);
        }
        g.b();
        com.apm.insight.log.a.a aVar = f;
        if (aVar != null) {
            aVar.b();
        }
        Iterator<com.apm.insight.log.a.a> it = g.values().iterator();
        while (it.hasNext()) {
            it.next().b();
        }
    }

    public static HashMap<String, String> d() {
        return g.c();
    }

    public static String e() {
        try {
            return g.d();
        } catch (Exception unused) {
            return "getStatus exception";
        }
    }

    public static void a(boolean z) {
        c = z;
        g.a(z);
        com.apm.insight.log.a.a aVar = f;
        if (aVar != null) {
            aVar.a(c);
        }
    }

    public static boolean a() {
        return e;
    }

    public static void a(Set<String> set) {
        d = Collections.unmodifiableSet(set);
    }

    public static void a(String str, String str2) {
        if (b(2, str)) {
            boolean zA = c.a();
            if (zA && i != null) {
                a(2, str, str2);
                return;
            }
            com.apm.insight.log.a.a aVar = f;
            if (aVar != null && zA) {
                aVar.a(str, str2);
            } else {
                g.a(str, str2);
            }
        }
    }

    public static void a(int i2) {
        a = i2;
        int i3 = i2 - 2;
        g.a(i3);
        com.apm.insight.log.a.a aVar = f;
        if (aVar != null) {
            aVar.b(i3);
        }
    }

    private static void a(int i2, String str, String str2) {
        a(i2, str, str2, null, 0, null);
    }

    private static void a(int i2, String str, String str2, Throwable th, int i3, Object obj) {
        l();
        C0012a c0012aA = C0012a.a();
        c0012aA.a = i2;
        c0012aA.b = str;
        c0012aA.c = str2;
        c0012aA.d = null;
        c0012aA.e = 0;
        c0012aA.f = null;
        c0012aA.g = j;
        c0012aA.h = System.currentTimeMillis();
        Message messageObtain = Message.obtain();
        messageObtain.what = 1;
        messageObtain.obj = c0012aA;
        i.sendMessage(messageObtain);
    }

    public static b a(String str) {
        com.apm.insight.log.a.a aVar = g.get(str);
        if (aVar == null) {
            return null;
        }
        return new b(aVar);
    }

    public static b a(String str, VLogConfig vLogConfig) {
        if (vLogConfig == null) {
            return null;
        }
        if (!e) {
            try {
                com.apm.insight.log.a.a.a(new c());
            } catch (Throwable unused) {
                return null;
            }
        }
        if (!c.a(vLogConfig.getContext())) {
            vLogConfig.setMaxDirSize((int) (vLogConfig.getSubProcessMaxDirSizeRatio() * vLogConfig.getMaxDirSize()));
        }
        com.apm.insight.log.a.a aVarA = new a.b(vLogConfig.getContext()).a(str).a(vLogConfig.getLevel() - 2).a(c).b(vLogConfig.getLogDirPath()).b(vLogConfig.getPerSize()).c(vLogConfig.getMaxDirSize()).d(vLogConfig.getLogFileExpDays()).c(vLogConfig.getBufferDirPath()).e(65536).f(196608).a(a.d.a).a(a.g.a).a(a.e.b).a(vLogConfig.isCompress() ? a.c.b : a.c.a).a(vLogConfig.isEncrypt() ? a.f.b : a.f.a).a(vLogConfig.isEncrypt() ? a.EnumC0013a.b : a.EnumC0013a.a).d(vLogConfig.getPubKey()).a();
        if (aVarA == null) {
            return null;
        }
        g.put(str, aVarA);
        return new b(aVarA);
    }

    public static void a(String str, Context context, boolean z) throws Throwable {
        String strD;
        String logDirPath;
        String strB = c.b();
        if (strB == null || strB.contains(":")) {
            return;
        }
        if (!z) {
            strB = strB.concat("-");
        }
        VLogConfig vLogConfig = b;
        if (vLogConfig != null) {
            logDirPath = vLogConfig.getLogDirPath();
            strD = b.getBufferDirPath();
        } else {
            String absolutePath = c.c(context).getAbsolutePath();
            strD = c.d(context);
            logDirPath = absolutePath;
        }
        File file = new File(logDirPath);
        if (file.exists() && file.isDirectory()) {
            String strZ = AbstractC4671xI0.z("__", str, ".alog.hot");
            for (File file2 : file.listFiles()) {
                String name = file2.getName();
                if (name != null && name.endsWith(strZ) && name.contains(strB)) {
                    file2.delete();
                }
            }
            File file3 = new File(strD);
            if (file3.exists() && file3.isDirectory()) {
                String strY = AbstractC4671xI0.y("__", str);
                for (File file4 : file3.listFiles()) {
                    String name2 = file4.getName();
                    if (name2 != null && name2.contains(strY) && name2.contains(strB)) {
                        file4.delete();
                    }
                }
            }
        }
    }

    public static /* synthetic */ void a(C0012a c0012a) {
        String string;
        int i2 = c0012a.a - 2;
        string = "";
        if (c0012a.e == 0) {
            if (c0012a.d == null) {
                string = c0012a.c;
            } else {
                StringBuilder sbT = AbstractC2039eV.t(c0012a.c != null ? AbstractC4671xI0.p(new StringBuilder(), c0012a.c, IOUtils.LINE_SEPARATOR_UNIX) : "");
                sbT.append(c.a(c0012a.d));
                string = sbT.toString();
            }
        }
        g.a(i2, c0012a.b, string, c0012a.g, c0012a.h);
        c0012a.b();
    }
}
