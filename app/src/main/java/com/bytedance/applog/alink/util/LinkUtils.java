package com.bytedance.applog.alink.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import defpackage.C1451aU0;
import defpackage.C2498hm;
import defpackage.T00;
import defpackage.YK0;
import java.nio.charset.Charset;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@T00
public final class LinkUtils {
    public static final LinkUtils INSTANCE = new LinkUtils();

    public final JSONObject getParamFromClipboard(Context context) {
        Object systemService;
        if (context != null) {
            try {
                systemService = context.getSystemService("clipboard");
            } catch (Throwable unused) {
            }
        } else {
            systemService = null;
        }
        if (systemService == null) {
            throw new C1451aU0("null cannot be cast to non-null type android.content.ClipboardManager");
        }
        ClipboardManager clipboardManager = (ClipboardManager) systemService;
        ClipData primaryClip = clipboardManager.getPrimaryClip();
        if (primaryClip != null) {
            ClipData.Item itemAt = primaryClip.getItemAt(0);
            Intrinsics.checkExpressionValueIsNotNull(itemAt, "clipData.getItemAt(0)");
            String string = itemAt.getText().toString();
            if (YK0.b0(string, "datatracer:", false)) {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("", ""));
                String strSubstring = string.substring(11);
                Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.String).substring(startIndex)");
                Charset charset = C2498hm.b;
                if (strSubstring == null) {
                    throw new C1451aU0("null cannot be cast to non-null type java.lang.String");
                }
                byte[] bytes = strSubstring.getBytes(charset);
                Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
                byte[] data = Base64.decode(bytes, 2);
                Intrinsics.checkExpressionValueIsNotNull(data, "data");
                return getParamFromLink(Uri.parse("?".concat(new String(data, charset))));
            }
        }
        return null;
    }

    public final JSONObject getParamFromLink(Uri uri) {
        try {
            JSONObject jSONObject = new JSONObject();
            if (uri != null) {
                String scheme = uri.getScheme();
                if (Intrinsics.areEqual(scheme, "http") || Intrinsics.areEqual(scheme, "https")) {
                    jSONObject.put("tr_token", uri.getLastPathSegment());
                }
                for (String str : uri.getQueryParameterNames()) {
                    jSONObject.put(str, uri.getQueryParameter(str));
                }
            }
            return jSONObject;
        } catch (Throwable unused) {
            return null;
        }
    }
}
