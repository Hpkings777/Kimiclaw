package com.bytedance.applog.simulate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.widget.TextView;
import com.moonshot.kimiclaw.R;
import defpackage.AbstractC0690Mt0;
import defpackage.AbstractC2804k;
import defpackage.AbstractC2901ke1;
import defpackage.B70;
import defpackage.InterfaceC0211Dn0;
import defpackage.InterfaceC1452aV;
import defpackage.InterfaceC2319gV;
import defpackage.Od1;
import defpackage.T91;
import java.lang.reflect.Field;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0211Dn0(path = "/simulateLaunch", title = "圈选/埋点验证")
public class SimulateLaunchActivity extends Activity implements InterfaceC2319gV {
    public static String b = "";
    public static int c = 0;
    public static String d = "";
    public static String e = "";
    public static String f = "";
    public TextView a;

    /* JADX WARN: Removed duplicated region for block: B:35:0x00e7  */
    @Override // android.app.Activity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void onCreate(Bundle bundle) {
        boolean z;
        ArrayMap arrayMap;
        super.onCreate(bundle);
        setContentView(R.layout.applog_activity_simulate);
        this.a = (TextView) findViewById(R.id.text_tip);
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (intent.hasExtra("url_prefix_no_qr") && intent.hasExtra("aid_no_qr")) {
            c = 1;
            d = intent.getStringExtra("url_prefix_no_qr");
            b = intent.getStringExtra("aid_no_qr");
        } else if (data != null) {
            c = 0;
            b = data.getQueryParameter("aid");
            e = data.getQueryParameter("qr_param");
            d = data.getQueryParameter("url_prefix");
            String queryParameter = data.getQueryParameter("type");
            f = queryParameter;
            "debug_log".equals(queryParameter);
            if (AbstractC2901ke1.o(d)) {
                this.a.setText("启动失败：缺少url_prefix参数");
                return;
            }
        }
        String str = b;
        T91 t91B = AbstractC2901ke1.o(str) ? null : AbstractC0690Mt0.b(str);
        if (t91B != null && t91B.o) {
            InterfaceC1452aV interfaceC1452aVH = (InterfaceC1452aV) AbstractC2804k.c.get(b);
            if (interfaceC1452aVH == null) {
                interfaceC1452aVH = B70.h();
            }
            ((B70) interfaceC1452aVH).c(0, Collections.singletonList("SimulateLaunchActivity"), "AppLog has started with appId:{}", b);
            new Od1(t91B).execute(new Void[0]);
        }
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Object objInvoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(null, new Object[0]);
            Field declaredField = cls.getDeclaredField("mActivities");
            declaredField.setAccessible(true);
            arrayMap = (ArrayMap) declaredField.get(objInvoke);
        } catch (Throwable th) {
            InterfaceC1452aV interfaceC1452aVH2 = (InterfaceC1452aV) AbstractC2804k.c.get(b);
            if (interfaceC1452aVH2 == null) {
                interfaceC1452aVH2 = B70.h();
            }
            ((B70) interfaceC1452aVH2).c(0, Collections.singletonList("SimulateLaunchActivity"), "Check has activity failed", th);
        }
        if (arrayMap != null) {
            z = arrayMap.size() > 0;
        }
        InterfaceC1452aV interfaceC1452aVH3 = (InterfaceC1452aV) AbstractC2804k.c.get(b);
        if (interfaceC1452aVH3 == null) {
            interfaceC1452aVH3 = B70.h();
        }
        ((B70) interfaceC1452aVH3).c(0, Collections.singletonList("SimulateLaunchActivity"), "Simulator onCreate appId: {}, urlPrefix: {}, mode: {}, params: {}, activity exists: {}", b, d, Integer.valueOf(c), e, Boolean.valueOf(z));
        if (!z) {
            Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(getApplicationInfo().packageName);
            if (launchIntentForPackage != null) {
                launchIntentForPackage.setPackage(null);
                startActivity(launchIntentForPackage);
            }
        }
        finish();
    }

    @Override // android.app.Activity
    public final void onDestroy() {
        super.onDestroy();
    }
}
