package com.bytedance.apm.agent.instrumentation;

import defpackage.AbstractC4647x71;
import defpackage.C0715Ng;
import defpackage.P31;
import defpackage.P71;
import defpackage.X51;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes.dex */
public final class HttpInstrumentation {
    public static URLConnection openConnection(URLConnection uRLConnection) {
        if (C0715Ng.d().b()) {
            int i = P71.p;
            P71 p71 = AbstractC4647x71.a;
            if (p71.o && (!p71.j || !p71.k)) {
                return uRLConnection;
            }
            if (uRLConnection instanceof HttpsURLConnection) {
                return new X51((HttpsURLConnection) uRLConnection);
            }
            if (uRLConnection instanceof HttpURLConnection) {
                return new P31((HttpURLConnection) uRLConnection);
            }
        }
        return uRLConnection;
    }

    public static URLConnection openConnectionWithProxy(URLConnection uRLConnection) {
        if (C0715Ng.d().b()) {
            int i = P71.p;
            P71 p71 = AbstractC4647x71.a;
            if (p71.o && (!p71.j || !p71.k)) {
                return uRLConnection;
            }
            if (uRLConnection instanceof HttpsURLConnection) {
                return new X51((HttpsURLConnection) uRLConnection);
            }
            if (uRLConnection instanceof HttpURLConnection) {
                return new P31((HttpURLConnection) uRLConnection);
            }
        }
        return uRLConnection;
    }
}
