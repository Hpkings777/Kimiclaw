package com.apmplus.hybrid.webview;

import android.text.TextUtils;
import android.webkit.WebView;
import defpackage.AbstractC0119Bt0;
import defpackage.AbstractC4671xI0;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class HybridMonitorManager {
    public static final HybridMonitorManager INSTANCE = new HybridMonitorManager();
    public static String a = "apm_plus_web_view_last_url_tag";
    public static String b = "apm_plus_web_view_tag";
    public static Map<String, String> c = new HashMap();
    public boolean d;

    public static HybridMonitorManager getInstance() {
        return INSTANCE;
    }

    public final void a(WebView webView) {
        String str = b;
        if (str.equals(a(webView, str))) {
            return;
        }
        HybridMonitorJsBridge hybridMonitorJsBridge = new HybridMonitorJsBridge(webView);
        if (!webView.getSettings().getJavaScriptEnabled()) {
            webView.getSettings().setJavaScriptEnabled(true);
        }
        webView.addJavascriptInterface(hybridMonitorJsBridge, "APMPlusJsBridge");
        String str2 = b;
        c.put(AbstractC4671xI0.i(str2, b(webView)), str2);
    }

    public final String b(WebView webView) {
        if (webView == null) {
            return "";
        }
        return webView.hashCode() + "";
    }

    public void init(boolean z) {
        this.d = z;
    }

    public void onLoadUrl(WebView webView, String str) {
        try {
            if (this.d) {
                c.remove(a + b(webView));
                a(webView);
            }
        } catch (Throwable unused) {
        }
    }

    public void onProgressChanged(WebView webView, int i) {
        try {
            a(webView, i);
        } catch (Throwable unused) {
        }
    }

    public final void a(WebView webView, int i) {
        if (this.d && i >= 15 && webView != null) {
            if (!webView.getSettings().getJavaScriptEnabled()) {
                webView.getSettings().setJavaScriptEnabled(true);
            }
            try {
                String url = webView.getUrl();
                if (url == null || !url.equals("about:blank")) {
                    String strA = a(webView, a);
                    if (TextUtils.isEmpty(url) || url.equals(strA)) {
                        return;
                    }
                    webView.evaluateJavascript(AbstractC0119Bt0.d(webView.getContext()), null);
                    c.put(a + b(webView), url);
                }
            } catch (Throwable unused) {
            }
        }
    }

    public final String a(WebView webView, String str) {
        String strB = b(webView);
        String str2 = c.get(AbstractC4671xI0.i(str, strB));
        if (str2 == null) {
            return null;
        }
        return str2.replaceAll(strB, "");
    }
}
