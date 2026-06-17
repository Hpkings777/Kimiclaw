package com.apm.insight.log;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.apm.insight.log.a;
import com.apm.insight.log.a.g;

/* JADX INFO: loaded from: classes.dex */
final class b extends Handler {
    public b(Looper looper) {
        super(looper);
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        int i = message.what;
        if (i != 1) {
            if (i != 2) {
                return;
            }
            g.b();
        } else {
            Object obj = message.obj;
            if (obj == null || !(obj instanceof a.C0012a)) {
                return;
            }
            a.a((a.C0012a) obj);
        }
    }
}
