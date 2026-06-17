package com.bytedance.android.monitor.webview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import com.bytedance.android.monitor.logger.MonitorLog;
import defpackage.AbstractC0173Cu0;
import defpackage.AbstractC0539Jv0;
import defpackage.AbstractC4671xI0;
import defpackage.B1;
import defpackage.C2459hV;
import defpackage.C4918z41;
import defpackage.F2;
import defpackage.F31;
import defpackage.G31;
import defpackage.InterfaceC3312nb1;
import defpackage.L81;
import defpackage.Lc1;
import defpackage.Na1;
import defpackage.O51;
import defpackage.P51;
import defpackage.RunnableC2746jZ0;
import defpackage.S91;
import defpackage.ViewOnAttachStateChangeListenerC2886kZ0;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class WebViewMonitorHelper implements ITTLiveWebViewMonitorHelper, Na1 {
    public static ITTLiveWebViewMonitorHelper a;
    public static Na1 b;
    public static Map<String, String> c = new HashMap();
    public static C2459hV d;
    public Map<String, C2459hV> e = new HashMap();
    public Map<String, C2459hV> f = new HashMap();
    public Set<String> g = new HashSet();
    public ViewOnAttachStateChangeListenerC2886kZ0 h = new ViewOnAttachStateChangeListenerC2886kZ0();
    public Handler i = new Handler(Looper.getMainLooper());
    public RunnableC2746jZ0 j;

    static {
        WebViewMonitorHelper webViewMonitorHelper = new WebViewMonitorHelper();
        a = webViewMonitorHelper;
        b = webViewMonitorHelper;
    }

    public static ITTLiveWebViewMonitorHelper getInstance() {
        return a;
    }

    public final boolean a(WebView webView) {
        S91 s91;
        C2459hV c2459hVB = b(webView);
        if (c2459hVB == null || (s91 = c2459hVB.a) == null) {
            return false;
        }
        return s91.e(webView);
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void addConfig(C2459hV c2459hV) {
        try {
            C2459hV c2459hVA = a(c2459hV);
            c2459hVA.getClass();
            String[] strArr = c2459hVA.b;
            if (strArr == null || strArr.length == 0) {
                return;
            }
            for (String str : strArr) {
                this.e.put(str, c2459hVA);
            }
        } catch (Exception unused) {
        }
    }

    public final C2459hV b(WebView webView) {
        Class<?> cls;
        C2459hV c2459hV;
        if (webView == null) {
            return d;
        }
        C2459hV c2459hV2 = this.f.get(createWebViewKey(webView));
        if (c2459hV2 != null) {
            return c2459hV2;
        }
        String name = webView.getClass().getName();
        C2459hV c2459hV3 = this.e.get(name);
        if (c2459hV3 != null) {
            return c2459hV3;
        }
        if (this.g.contains(name)) {
            return d;
        }
        for (String str : new HashSet(this.e.keySet())) {
            Class<?> cls2 = null;
            try {
                cls = Class.forName(name);
            } catch (Throwable unused) {
                cls = null;
            }
            try {
                cls2 = Class.forName(str);
            } catch (Throwable unused2) {
            }
            if (((cls == null || cls2 == null) ? false : cls2.isAssignableFrom(cls)) && (c2459hV = this.e.get(str)) != null) {
                this.e.put(name, c2459hV);
                return c2459hV;
            }
        }
        this.g.add(name);
        return d;
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public C2459hV buildConfig() {
        return new C2459hV();
    }

    @Override // defpackage.Na1
    public void cover(WebView webView, String str, String str2, String str3) {
        S91 s91;
        try {
            C2459hV c2459hVB = b(webView);
            if (c2459hVB != null && (s91 = c2459hVB.a) != null) {
                s91.cover(webView, str, str2, str3);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public String createWebViewKey(WebView webView) {
        if (webView == null) {
            return "";
        }
        return webView.hashCode() + "";
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void customParams(WebView webView, String str) {
        S91 s91;
        try {
            C2459hV c2459hVB = b(webView);
            if (c2459hVB != null && (s91 = c2459hVB.a) != null) {
                s91.B(webView, str);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void customParseKey(WebView webView, Set<String> set) {
        S91 s91;
        try {
            C2459hV c2459hVB = b(webView);
            if (c2459hVB != null && (s91 = c2459hVB.a) != null) {
                s91.a(webView, set);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void customReport(WebView webView, String str, String str2, String str3, String str4) {
        try {
            customReport(webView, null, null, str, str2, str3, str4);
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void destroy(WebView webView) {
        try {
            if (isNeedMonitor(webView)) {
                a(webView, false);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void dispatchTouchEvent(WebView webView, MotionEvent motionEvent) {
        if (webView == null || motionEvent == null) {
            return;
        }
        try {
            if (motionEvent.getAction() == 1) {
                updateClickStartTime(webView);
            }
        } catch (Exception unused) {
        }
    }

    @Override // defpackage.Na1
    public L81 getCustomCallback(WebView webView) {
        try {
            b(webView);
        } catch (Exception unused) {
        }
        return null;
    }

    @Override // defpackage.Na1
    public O51 getMonitor(WebView webView) {
        try {
            C2459hV c2459hVB = b(webView);
            if (c2459hVB == null) {
                return null;
            }
            return c2459hVB.d;
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // defpackage.Na1
    public InterfaceC3312nb1 getTTWebviewDetect(WebView webView) {
        b(webView).getClass();
        return null;
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void goBack(WebView webView) {
        try {
            if (isNeedMonitor(webView)) {
                a(webView, false);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void handleFetchError(WebView webView, G31 g31) {
        C2459hV c2459hVB;
        S91 s91;
        if (webView == null) {
            return;
        }
        try {
            if (isNeedMonitor(webView) && a(webView) && (c2459hVB = b(webView)) != null && (s91 = c2459hVB.a) != null) {
                s91.j();
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void handleFetchSuccess(WebView webView) {
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void handleJSBError(WebView webView, P51 p51) {
        C2459hV c2459hVB;
        S91 s91;
        if (webView == null) {
            return;
        }
        try {
            if (isNeedMonitor(webView) && a(webView) && (c2459hVB = b(webView)) != null && (s91 = c2459hVB.a) != null) {
                s91.v();
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void handleRequestError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        C2459hV c2459hVB;
        S91 s91;
        if (webView == null || webResourceRequest == null || webResourceError == null) {
            return;
        }
        try {
            if (isNeedMonitor(webView) && a(webView) && webResourceRequest.isForMainFrame() && (c2459hVB = b(webView)) != null && (s91 = c2459hVB.a) != null) {
                s91.q(webView, webResourceRequest.getUrl().toString(), webResourceError.getErrorCode(), webResourceError.getDescription().toString());
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void initConfig(C2459hV c2459hV) {
        try {
            addConfig(c2459hV);
        } catch (Exception unused) {
        }
    }

    @Override // defpackage.Na1
    public void initTime(WebView webView, String str) {
        S91 s91;
        try {
            C2459hV c2459hVB = b(webView);
            if (c2459hVB != null && (s91 = c2459hVB.a) != null) {
                s91.f(webView, str);
            }
        } catch (Exception unused) {
        }
    }

    @Override // defpackage.Na1
    public boolean isNeedAutoReport(WebView webView) {
        try {
            C2459hV c2459hVB = b(webView);
            if (c2459hVB != null) {
                return c2459hVB.g;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    @Override // defpackage.Na1
    public boolean isNeedMonitor(WebView webView) {
        try {
            C2459hV c2459hVB = b(webView);
            if (c2459hVB == null) {
                return false;
            }
            return c2459hVB.f;
        } catch (Exception unused) {
            return false;
        }
    }

    @Override // defpackage.Na1
    public String mapService(WebView webView, String str) {
        if (webView != null) {
            try {
                C2459hV c2459hVB = b(webView);
                if (c2459hVB != null && !TextUtils.isEmpty(str)) {
                    if ("custom".equals(str)) {
                        return "tt" + c2459hVB.j + "_webview_timing_monitor_custom_service";
                    }
                    return "bd_hybrid_monitor_service_" + str + "_web_" + c2459hVB.j;
                }
            } catch (Exception unused) {
            }
        }
        return str;
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void onClientOffline(WebView webView, String str, boolean z) {
        S91 s91;
        try {
            String strA = a(str);
            C2459hV c2459hVB = b(webView);
            if (c2459hVB != null && (s91 = c2459hVB.a) != null) {
                s91.C(webView, strA, z);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void onLoadUrl(WebView webView, String str) {
        try {
            if (isNeedMonitor(webView)) {
                b(webView, "ttlive_web_view_last_url_tag");
                MonitorLog.d("TTLiveWebViewMonitorHelper", "onLoadUrl : " + str);
                updateClickStartTime(webView);
                if (!isNeedMonitor(webView) || "ttlive_web_view_tag".equals(a(webView, "ttlive_web_view_tag"))) {
                    return;
                }
                TTLiveWebViewMonitorJsBridge tTLiveWebViewMonitorJsBridge = new TTLiveWebViewMonitorJsBridge(webView);
                if (!webView.getSettings().getJavaScriptEnabled()) {
                    webView.getSettings().setJavaScriptEnabled(true);
                }
                webView.addJavascriptInterface(tTLiveWebViewMonitorJsBridge, "JsBridgeTransferMonitor");
                a(webView, "ttlive_web_view_tag", "ttlive_web_view_tag");
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void onOffline(WebView webView, String str, boolean z) {
        S91 s91;
        try {
            String strA = a(str);
            C2459hV c2459hVB = b(webView);
            if (c2459hVB != null && (s91 = c2459hVB.a) != null) {
                s91.x(webView, strA, z);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void onOfflineInfoExtra(WebView webView, String str, String str2, String str3, String str4, String str5) {
        S91 s91;
        try {
            C2459hV c2459hVB = b(webView);
            if (c2459hVB != null && (s91 = c2459hVB.a) != null) {
                s91.m(webView, str, str2, str3, str4, str5);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void onPageFinished(WebView webView, String str) {
        C2459hV c2459hVB;
        S91 s91;
        try {
            if (isNeedMonitor(webView) && (c2459hVB = b(webView)) != null && (s91 = c2459hVB.a) != null) {
                s91.A(webView);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        try {
            onPageStarted(webView, str);
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void onProgressChanged(WebView webView, int i) {
        C2459hV c2459hVB;
        S91 s91;
        try {
            a(webView, i);
            if (webView != null && isNeedMonitor(webView) && a(webView) && (c2459hVB = b(webView)) != null && (s91 = c2459hVB.a) != null) {
                s91.o(webView, i);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void reload(WebView webView) {
        try {
            if (isNeedMonitor(webView)) {
                b(webView, "ttlive_web_view_last_url_tag");
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void removeWebViewKey(String str) {
        try {
            Map<String, C2459hV> map = this.f;
            if (map != null) {
                map.remove(str);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void report(WebView webView) {
        try {
            this.i.post(new B1(12, this, webView));
            RunnableC2746jZ0 runnableC2746jZ0 = new RunnableC2746jZ0(this, webView);
            this.j = runnableC2746jZ0;
            this.i.postDelayed(runnableC2746jZ0, 200L);
        } catch (Exception unused) {
        }
    }

    @Override // defpackage.Na1
    public void reportDirectly(WebView webView, String str, String str2) {
        S91 s91;
        try {
            C2459hV c2459hVB = b(webView);
            if (c2459hVB != null && (s91 = c2459hVB.a) != null) {
                s91.reportDirectly(webView, str, str2);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void reportTruly(WebView webView) {
        S91 s91;
        S91 s912;
        try {
            RunnableC2746jZ0 runnableC2746jZ0 = this.j;
            if (runnableC2746jZ0 != null) {
                this.i.removeCallbacks(runnableC2746jZ0);
                this.j = null;
            }
            try {
                C2459hV c2459hVB = b(webView);
                if (c2459hVB != null && (s912 = c2459hVB.a) != null) {
                    s912.l(webView);
                }
            } catch (Exception unused) {
            }
            C2459hV c2459hVB2 = b(webView);
            if (c2459hVB2 != null && (s91 = c2459hVB2.a) != null) {
                s91.report(webView);
            }
            b(webView, "ttlive_web_view_last_url_tag");
            b(webView, "ttlive_web_view_auto_report_tag");
            b(webView, "ttlive_web_view_tag");
            ViewOnAttachStateChangeListenerC2886kZ0 viewOnAttachStateChangeListenerC2886kZ0 = this.h;
            if (viewOnAttachStateChangeListenerC2886kZ0 == null || webView == null) {
                return;
            }
            webView.removeOnAttachStateChangeListener(viewOnAttachStateChangeListenerC2886kZ0);
        } catch (Exception unused2) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void setDefaultConfig(C2459hV c2459hV) {
        try {
            d = a(c2459hV);
        } catch (Exception unused) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void setGeckoClient(F31 f31) {
        this.i.postDelayed(new F2(this, 20), 20000L);
    }

    public void updateClickStartTime(WebView webView) {
        C2459hV c2459hVB;
        S91 s91;
        if (webView == null || !isNeedMonitor(webView) || (c2459hVB = b(webView)) == null || (s91 = c2459hVB.a) == null) {
            return;
        }
        s91.u(webView);
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void customReport(WebView webView, String str, String str2, String str3, String str4, String str5, String str6) {
        C2459hV c2459hVB;
        S91 s91;
        S91 s912;
        try {
            if (!TextUtils.isEmpty(str2)) {
                JSONObject jSONObjectD0 = AbstractC0539Jv0.d0(str5);
                try {
                    jSONObjectD0.put("event_name", str2);
                } catch (JSONException unused) {
                }
                str5 = jSONObjectD0.toString();
            }
            String str7 = str5;
            if ("0".equals(str6)) {
                C2459hV c2459hVB2 = b(webView);
                if (c2459hVB2 != null && (s912 = c2459hVB2.a) != null) {
                    s912.y(webView, str, str3, str4, str7);
                    return;
                }
                return;
            }
            if ("1".equals(str6) && (c2459hVB = b(webView)) != null && (s91 = c2459hVB.a) != null) {
                s91.s(webView, str, str3, str4, str7);
            }
        } catch (Exception unused2) {
        }
    }

    @Override // com.bytedance.android.monitor.webview.ITTLiveWebViewMonitorHelper
    public void onPageStarted(WebView webView, String str) {
        S91 s91;
        try {
            if (isNeedMonitor(webView)) {
                if (isNeedAutoReport(webView) && !"ttlive_web_view_auto_report_tag".equals(a(webView, "ttlive_web_view_auto_report_tag"))) {
                    if (this.h != null && isNeedAutoReport(webView)) {
                        ViewOnAttachStateChangeListenerC2886kZ0 viewOnAttachStateChangeListenerC2886kZ0 = this.h;
                        viewOnAttachStateChangeListenerC2886kZ0.getClass();
                        if (webView != null) {
                            webView.removeOnAttachStateChangeListener(viewOnAttachStateChangeListenerC2886kZ0);
                            webView.addOnAttachStateChangeListener(viewOnAttachStateChangeListenerC2886kZ0);
                        }
                    }
                    a(webView, "ttlive_web_view_auto_report_tag", "ttlive_web_view_auto_report_tag");
                }
                C2459hV c2459hVB = b(webView);
                if (c2459hVB != null && (s91 = c2459hVB.a) != null) {
                    s91.p(webView, str);
                }
            }
        } catch (Exception unused) {
        }
    }

    public final void a(WebView webView, boolean z) {
        if (b(webView) == null) {
            return;
        }
        String strZ = AbstractC4671xI0.z(" javascript: (function () {\n    var target = {}\n    if (typeof SlardarHybrid !== 'undefined' && typeof jsIESLiveTimingMonitor !== 'undefined'){\n    var performacess = SlardarHybrid('getLatestPerformance');\n    var resourcess = SlardarHybrid('getLatestResource');\n    target.performance = performacess;\n    target.resource = resourcess;\n    target.needReport = ", z ? "true" : "false", ";\n    jsIESLiveTimingMonitor.reportPageLatestData(target);}\n })()");
        if (webView != null) {
            webView.evaluateJavascript(strZ, null);
        }
    }

    public final C2459hV a(C2459hV c2459hV) {
        String[] strArr;
        JSONArray jSONArrayOptJSONArray;
        boolean zOptBoolean;
        String strZ;
        C2459hV c2459hV2 = new C2459hV();
        c2459hV.j = c2459hV.j;
        if (c2459hV.e) {
            c2459hV.j = "live";
        }
        S91 s91 = c2459hV.a;
        if (s91 == null) {
            if (Lc1.b == null) {
                synchronized (Lc1.class) {
                    try {
                        if (Lc1.b == null) {
                            Lc1.b = new Lc1(0);
                        }
                    } finally {
                    }
                }
            }
            s91 = Lc1.b;
        }
        c2459hV2.a = s91;
        String str = c2459hV.c;
        if (str == null) {
            str = "WebViewMonitor";
        }
        c2459hV2.c = str;
        O51 o51 = c2459hV.d;
        C4918z41 c4918z41 = new C4918z41(25, false);
        c4918z41.b = o51;
        c2459hV2.d = c4918z41;
        c2459hV2.e = c2459hV.e;
        c2459hV2.g = c2459hV.g;
        c2459hV2.i = null;
        c2459hV2.f = c2459hV.f;
        c2459hV2.b = c2459hV.b;
        c2459hV2.j = c2459hV.j;
        c2459hV2.h = TextUtils.isEmpty(c2459hV.h) ? "" : c2459hV.h;
        if (AbstractC0539Jv0.d0("").opt("webview_classes") == null) {
            strArr = c2459hV2.b;
        } else {
            String[] strArr2 = new String[0];
            if (!TextUtils.isEmpty("") && (jSONArrayOptJSONArray = AbstractC0539Jv0.d0("").optJSONArray("webview_classes")) != null) {
                strArr2 = new String[jSONArrayOptJSONArray.length()];
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    try {
                        strArr2[i] = jSONArrayOptJSONArray.getString(i);
                    } catch (JSONException unused) {
                    }
                }
            }
            strArr = strArr2;
        }
        c2459hV2.b = strArr;
        if (AbstractC0539Jv0.d0("").opt("webview_is_need_monitor") == null) {
            zOptBoolean = c2459hV2.f;
        } else {
            zOptBoolean = AbstractC0539Jv0.d0("").optBoolean("webview_is_need_monitor", false);
        }
        c2459hV2.f = zOptBoolean;
        if (TextUtils.isEmpty("")) {
            strZ = c2459hV2.h;
        } else {
            JSONObject jSONObjectD0 = AbstractC0539Jv0.d0("");
            JSONObject jSONObjectOptJSONObject = jSONObjectD0.optJSONObject("apmReportConfig");
            JSONObject jSONObjectOptJSONObject2 = jSONObjectD0.optJSONObject("performanceReportConfig");
            JSONObject jSONObjectOptJSONObject3 = jSONObjectD0.optJSONObject("errorMsgReportConfig");
            JSONObject jSONObjectOptJSONObject4 = jSONObjectD0.optJSONObject("resourceTimingReportConfig");
            JSONObject jSONObjectOptJSONObject5 = jSONObjectD0.optJSONObject("commonReportConfig");
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject.put("monitors", jSONObject2);
            } catch (JSONException unused2) {
            }
            try {
                jSONObject.put("sendCommonParams", jSONObjectOptJSONObject5);
            } catch (JSONException unused3) {
            }
            if (jSONObjectOptJSONObject != null) {
                Iterator<String> itKeys = jSONObjectOptJSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    try {
                        jSONObject2.put(next, jSONObjectOptJSONObject.opt(next));
                    } catch (JSONException unused4) {
                    }
                }
            }
            if (jSONObjectOptJSONObject2 != null) {
                Iterator<String> itKeys2 = jSONObjectOptJSONObject2.keys();
                while (itKeys2.hasNext()) {
                    String next2 = itKeys2.next();
                    try {
                        jSONObject2.put(next2, jSONObjectOptJSONObject2.opt(next2));
                    } catch (JSONException unused5) {
                    }
                }
            }
            if (jSONObjectOptJSONObject3 != null) {
                Iterator<String> itKeys3 = jSONObjectOptJSONObject3.keys();
                while (itKeys3.hasNext()) {
                    String next3 = itKeys3.next();
                    try {
                        jSONObject2.put(next3, jSONObjectOptJSONObject3.opt(next3));
                    } catch (JSONException unused6) {
                    }
                }
            }
            if (jSONObjectOptJSONObject4 != null) {
                Iterator<String> itKeys4 = jSONObjectOptJSONObject4.keys();
                while (itKeys4.hasNext()) {
                    String next4 = itKeys4.next();
                    try {
                        jSONObject2.put(next4, jSONObjectOptJSONObject4.opt(next4));
                    } catch (JSONException unused7) {
                    }
                }
            }
            strZ = AbstractC4671xI0.z("RangersSiteHybridSDK('config', ", jSONObject.toString(), ")");
        }
        c2459hV2.h = strZ;
        return c2459hV2;
    }

    public final void b(WebView webView, String str) {
        c.remove(AbstractC4671xI0.i(str, createWebViewKey(webView)));
    }

    public final String a(String str) {
        int iIndexOf;
        return (TextUtils.isEmpty(str) || (iIndexOf = str.indexOf("?")) == -1) ? str : str.substring(0, iIndexOf);
    }

    public final void a(WebView webView, int i) {
        if (isNeedMonitor(webView) && i >= 15 && webView != null) {
            if (!webView.getSettings().getJavaScriptEnabled()) {
                webView.getSettings().setJavaScriptEnabled(true);
            }
            try {
                String url = webView.getUrl();
                if (url == null || !url.equals("about:blank")) {
                    String strA = a(webView, "ttlive_web_view_last_url_tag");
                    if (TextUtils.isEmpty(url) || url.equals(strA)) {
                        return;
                    }
                    C2459hV c2459hVB = b(webView);
                    String str = "";
                    String str2 = c2459hVB == null ? "" : c2459hVB.h;
                    if (c2459hVB != null) {
                        str = c2459hVB.i;
                    }
                    webView.evaluateJavascript(AbstractC0173Cu0.e(webView.getContext(), str, str2, true), null);
                    a(webView, "ttlive_web_view_last_url_tag", url);
                    MonitorLog.d("WebViewMonitorHelper", "injectJsScript : ".concat(url));
                }
            } catch (Exception unused) {
            }
        }
    }

    public final void a(WebView webView, String str, String str2) {
        c.put(AbstractC4671xI0.i(str, createWebViewKey(webView)), str2);
    }

    public final String a(WebView webView, String str) {
        String strCreateWebViewKey = createWebViewKey(webView);
        String str2 = c.get(AbstractC4671xI0.i(str, strCreateWebViewKey));
        if (str2 == null) {
            return null;
        }
        return str2.replaceAll(strCreateWebViewKey, "");
    }
}
