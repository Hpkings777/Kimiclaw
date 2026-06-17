package com.bytedance.android.monitor.webview;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Keep;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.bytedance.android.monitor.logger.MonitorLog;
import defpackage.WM0;
import defpackage.XM0;
import defpackage.YI0;
import defpackage.YM0;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
@Keep
public class TTLiveWebViewMonitorJsBridge {
    private WeakReference<WebView> mWebViewRef;
    private Handler mainHanlder = new Handler(Looper.getMainLooper());

    public TTLiveWebViewMonitorJsBridge(WebView webView) {
        this.mWebViewRef = new WeakReference<>(webView);
    }

    @JavascriptInterface
    public void cover(String str, String str2) {
        if (WebViewMonitorHelper.b.isNeedMonitor(this.mWebViewRef.get())) {
            MonitorLog.d("TTLiveWebViewMonitorJsBridge", "cover: service:" + str2 + " json : " + str);
            this.mainHanlder.post(new WM0(this, str, str2, 0));
        }
    }

    @JavascriptInterface
    public void customReport(String str, String str2, String str3, String str4) {
        if (WebViewMonitorHelper.b.isNeedMonitor(this.mWebViewRef.get())) {
            StringBuilder sbI = YI0.i("customReport: merticJson:", str, " categoryJson : ", str2, " extraJson:");
            sbI.append(str3);
            sbI.append(" type:");
            sbI.append(str4);
            MonitorLog.d("TTLiveWebViewMonitorJsBridge", sbI.toString());
            this.mainHanlder.post(new XM0(this, str2, str, str3, str4));
        }
    }

    @JavascriptInterface
    public void reportDirectly(String str, String str2) {
        if (WebViewMonitorHelper.b.isNeedMonitor(this.mWebViewRef.get())) {
            MonitorLog.d("TTLiveWebViewMonitorJsBridge", "reportDirectly: service:" + str2 + " json : " + str);
            this.mainHanlder.post(new WM0(this, str2, str, 1));
        }
    }

    @JavascriptInterface
    public void reportPageLatestData(String str) {
        if (WebViewMonitorHelper.b.isNeedMonitor(this.mWebViewRef.get())) {
            MonitorLog.d("TTLiveWebViewMonitorJsBridge", "reportPageLatestData: json:" + str);
            this.mainHanlder.post(new YM0(this, str, 1));
        }
    }

    @JavascriptInterface
    public void sendInitTimeInfo(String str) {
        if (WebViewMonitorHelper.b.isNeedMonitor(this.mWebViewRef.get())) {
            MonitorLog.d("TTLiveWebViewMonitorJsBridge", "sendInitTimeInfo: json:" + str);
            this.mainHanlder.post(new YM0(this, str, 0));
        }
    }
}
