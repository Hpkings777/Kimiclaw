package com.bytedance.android.monitor.webview;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import defpackage.C2459hV;
import defpackage.F31;
import defpackage.G31;
import defpackage.P51;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public interface ITTLiveWebViewMonitorHelper {
    void addConfig(C2459hV c2459hV);

    C2459hV buildConfig();

    String createWebViewKey(WebView webView);

    void customParams(WebView webView, String str);

    void customParseKey(WebView webView, Set<String> set);

    void customReport(WebView webView, String str, String str2, String str3, String str4);

    void customReport(WebView webView, String str, String str2, String str3, String str4, String str5, String str6);

    void destroy(WebView webView);

    void dispatchTouchEvent(WebView webView, MotionEvent motionEvent);

    void goBack(WebView webView);

    void handleFetchError(WebView webView, G31 g31);

    void handleFetchSuccess(WebView webView);

    void handleJSBError(WebView webView, P51 p51);

    void handleRequestError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError);

    @Deprecated
    void initConfig(C2459hV c2459hV);

    void onClientOffline(WebView webView, String str, boolean z);

    void onLoadUrl(WebView webView, String str);

    void onOffline(WebView webView, String str, boolean z);

    void onOfflineInfoExtra(WebView webView, String str, String str2, String str3, String str4, String str5);

    void onPageFinished(WebView webView, String str);

    void onPageStarted(WebView webView, String str);

    @Deprecated
    void onPageStarted(WebView webView, String str, Bitmap bitmap);

    void onProgressChanged(WebView webView, int i);

    void reload(WebView webView);

    void removeWebViewKey(String str);

    void report(WebView webView);

    void reportTruly(WebView webView);

    void setDefaultConfig(C2459hV c2459hV);

    void setGeckoClient(F31 f31);
}
