package com.bytedance.android.monitor.webview;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/* JADX INFO: loaded from: classes.dex */
public class WebViewMonitorWebViewClient extends WebViewClient {
    @Override // android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        super.onPageStarted(webView, str, bitmap);
        WebViewMonitorHelper.getInstance().onPageStarted(webView, str, bitmap);
    }
}
