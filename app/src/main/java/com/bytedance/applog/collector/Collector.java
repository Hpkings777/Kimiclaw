package com.bytedance.applog.collector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import defpackage.AbstractC0690Mt0;
import defpackage.B70;
import defpackage.C2471ha1;
import defpackage.T91;
import defpackage.Ub1;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class Collector extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("K_DATA")) {
            String[] stringArrayExtra = intent.getStringArrayExtra("K_DATA");
            if (stringArrayExtra == null || stringArrayExtra.length <= 0) {
                ((B70) B70.h()).g(Collections.singletonList("Collector"), "Event is null", null, new Object[0]);
                return;
            }
            for (T91 t91 : T91.y) {
                String[] strArr = (String[]) stringArrayExtra.clone();
                t91.getClass();
                if (strArr.length != 0) {
                    for (String str : strArr) {
                    }
                    if (t91.m == null) {
                        Ub1 ub1 = t91.d;
                        synchronized (((LinkedList) ub1.c)) {
                            try {
                                if (((LinkedList) ub1.c).size() > 300) {
                                    ((LinkedList) ub1.c).poll();
                                }
                                ((LinkedList) ub1.c).addAll(Arrays.asList(strArr));
                            } finally {
                            }
                        }
                    } else {
                        C2471ha1 c2471ha1 = t91.m;
                        c2471ha1.n.removeMessages(4);
                        c2471ha1.n.obtainMessage(4, strArr).sendToTarget();
                    }
                }
            }
            return;
        }
        if (intent.hasExtra("K_ADD_CUSTOM_HEADER")) {
            String stringExtra = intent.getStringExtra("K_CUSTOM_HEADER_KEY");
            String stringExtra2 = intent.getStringExtra("K_CUSTOM_HEADER_VALUE");
            String stringExtra3 = intent.getStringExtra("K_APP_ID");
            T91 t91B = AbstractC0690Mt0.b(stringExtra3);
            if (t91B != null) {
                t91B.q(stringExtra, stringExtra2);
                return;
            }
            ((B70) B70.h()).g(Collections.singletonList("Collector"), "Add custom failed, because find appLogInstance is null, appId: {}, customKey: {}, customValue: {}.", null, stringExtra3, stringExtra, stringExtra2);
            return;
        }
        if (intent.hasExtra("K_REMOVE_CUSTOM_HEADER")) {
            String stringExtra4 = intent.getStringExtra("K_CUSTOM_HEADER_KEY");
            String stringExtra5 = intent.getStringExtra("K_APP_ID");
            T91 t91B2 = AbstractC0690Mt0.b(stringExtra5);
            if (t91B2 == null) {
                ((B70) B70.h()).g(Collections.singletonList("Collector"), "Remove custom failed, because find appLogInstance is null, appId: {}, customKey: {}.", null, stringExtra5, stringExtra4);
            } else {
                if (t91B2.b("removeHeaderInfo")) {
                    return;
                }
                t91B2.q.d("call removeHeaderInfo isMainProcess: {}, key: {}", Boolean.valueOf(t91B2.k.d()), stringExtra4);
                if (t91B2.k.d()) {
                    t91B2.l.k(stringExtra4);
                    return;
                }
                try {
                    t91B2.e(stringExtra4);
                } catch (Throwable th) {
                    t91B2.q.d("call removeHeaderInfo Post Main Process failed.", th);
                }
            }
        }
    }
}
