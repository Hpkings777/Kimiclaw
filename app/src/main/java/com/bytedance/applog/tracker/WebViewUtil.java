package com.bytedance.applog.tracker;

import android.annotation.SuppressLint;
import android.util.LruCache;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import defpackage.AbstractC1641bd1;
import defpackage.AbstractC2608iZ0;
import defpackage.B70;
import defpackage.C2188fZ0;
import defpackage.T91;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class WebViewUtil {
    public static final LruCache<String, Long> a = new LruCache<>(100);
    public static final LruCache<String, Long> b = new LruCache<>(100);

    @SuppressLint({"WebViewApiAvailability"})
    public static WebChromeClient a(WebView webView) {
        return webView.getWebChromeClient();
    }

    public static void b(LruCache<String, Long> lruCache, View view) {
        if (view == null) {
            return;
        }
        lruCache.put(view.hashCode() + "$$" + view.getId(), Long.valueOf(System.currentTimeMillis()));
    }

    public static void injectWebViewBridges(View view, String str) {
        if (a(b, view)) {
            for (T91 t91 : T91.y) {
                if (t91.g() != null) {
                    t91.g().getClass();
                }
            }
        }
        if (a(a, view)) {
            Iterator it = T91.y.iterator();
            while (it.hasNext()) {
                ((T91) it.next()).n();
            }
        }
    }

    public static void injectWebViewJsCode(View view, String str) {
        List list = AbstractC1641bd1.a;
        ((B70) B70.h()).c(0, AbstractC1641bd1.a, "Inject applog bridge compat js to: {}", view);
        AbstractC2608iZ0.a(view, "if(typeof AppLogBridge !== 'undefined' && !AppLogBridge.hasOwnProperty('hasStarted')) { AppLogBridge.hasStarted = function(callback) {if(callback) callback(AppLogBridge.hasStartedForJsSdkUnderV5_deprecated());  return AppLogBridge.hasStartedForJsSdkUnderV5_deprecated();};}", new C2188fZ0());
        Iterator it = T91.y.iterator();
        while (it.hasNext()) {
            ((T91) it.next()).n();
        }
    }

    public static boolean a(LruCache<String, Long> lruCache, View view) {
        if (view == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(view.hashCode());
        sb.append("$$");
        sb.append(view.getId());
        return lruCache.get(sb.toString()) == null;
    }
}
