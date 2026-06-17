package com.bytedance.apm.agent.v2.instrumentation;

import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.Log;
import com.bytedance.apm.agent.v2.InstructOperationSwitch;
import com.bytedance.apm.insight.ApmInsightInitConfig;
import com.sun.mail.imap.IMAPStore;
import defpackage.AbstractC0539Jv0;
import defpackage.AbstractC1820cw0;
import defpackage.AbstractC2653iu0;
import defpackage.AbstractC2834k91;
import defpackage.AbstractC4017sd1;
import defpackage.C0715Ng;
import defpackage.C4784y61;
import defpackage.K31;
import defpackage.Md1;
import defpackage.RunnableC0530Jr;
import defpackage.RunnableC1568b61;
import defpackage.Xb1;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@Keep
public class ActivityAgent {
    private static final String TAG = "ActivityInstrumentation";

    @Keep
    public static void onTrace(String str, String str2, boolean z) {
        Xb1 xb1;
        String str3 = "onWindowFocusChanged";
        long j = 0;
        String str4 = AppAgent.ON_CREATE;
        String str5 = "onResume";
        if (z && InstructOperationSwitch.sUiSwitch && TextUtils.equals("onResume", str2)) {
            AbstractC0539Jv0.K(str, true);
        }
        if (InstructOperationSwitch.sPageLoadSwitch) {
            HashSet hashSet = AbstractC2834k91.a;
            if (TextUtils.equals(AppAgent.ON_CREATE, str2)) {
                if (!z) {
                    if (AbstractC2653iu0.s == 0) {
                        AbstractC2653iu0.s = System.currentTimeMillis();
                    }
                    AbstractC2653iu0.i = System.currentTimeMillis();
                    Xb1 xb12 = (Xb1) AbstractC2834k91.b.peekLast();
                    if (xb12 == null || TextUtils.isEmpty(xb12.a)) {
                        return;
                    }
                    xb12.c = System.currentTimeMillis();
                    return;
                }
                if (AbstractC2653iu0.r == 0) {
                    AbstractC2653iu0.r = System.currentTimeMillis();
                }
                AbstractC2653iu0.h = System.currentTimeMillis();
                if (AbstractC2653iu0.r - AbstractC2653iu0.g < 800) {
                    AbstractC2653iu0.p = true;
                }
                ConcurrentLinkedDeque concurrentLinkedDeque = AbstractC2834k91.b;
                if (concurrentLinkedDeque.size() > 10) {
                    concurrentLinkedDeque.clear();
                }
                long jCurrentTimeMillis = System.currentTimeMillis();
                Xb1 xb13 = new Xb1();
                xb13.a = str;
                xb13.b = jCurrentTimeMillis;
                concurrentLinkedDeque.add(xb13);
                return;
            }
            if (TextUtils.equals("onResume", str2)) {
                if (!z) {
                    C4784y61.i.b(new RunnableC0530Jr(str, 3));
                    Xb1 xb14 = (Xb1) AbstractC2834k91.b.peekLast();
                    if (xb14 == null || TextUtils.isEmpty(xb14.a)) {
                        return;
                    }
                    xb14.e = System.currentTimeMillis();
                    return;
                }
                if (AbstractC2653iu0.t == 0) {
                    AbstractC2653iu0.t = System.currentTimeMillis();
                }
                AbstractC2653iu0.j = System.currentTimeMillis();
                Xb1 xb15 = (Xb1) AbstractC2834k91.b.peekLast();
                if (xb15 == null || TextUtils.isEmpty(xb15.a)) {
                    return;
                }
                xb15.d = System.currentTimeMillis();
                return;
            }
            if (!TextUtils.equals("onWindowFocusChanged", str2)) {
                if (TextUtils.equals("onRestart", str2)) {
                    if (z) {
                        AbstractC2653iu0.l = System.currentTimeMillis();
                        return;
                    } else {
                        AbstractC2653iu0.m = System.currentTimeMillis();
                        return;
                    }
                }
                if (TextUtils.equals("onStart", str2)) {
                    if (z) {
                        if (AbstractC2653iu0.w == 0) {
                            AbstractC2653iu0.w = System.currentTimeMillis();
                        }
                        AbstractC2653iu0.n = System.currentTimeMillis();
                        return;
                    } else {
                        if (AbstractC2653iu0.x == 0) {
                            AbstractC2653iu0.x = System.currentTimeMillis();
                        }
                        AbstractC2653iu0.o = System.currentTimeMillis();
                        return;
                    }
                }
                return;
            }
            if (!z || (xb1 = (Xb1) AbstractC2834k91.b.peekLast()) == null || xb1.f != 0 || TextUtils.isEmpty(xb1.a)) {
                return;
            }
            xb1.f = System.currentTimeMillis();
            if (((Integer) K31.a.get(str)) == null) {
                ConcurrentLinkedDeque concurrentLinkedDeque2 = AbstractC2834k91.b;
                try {
                    int size = concurrentLinkedDeque2.size();
                    int i = 0;
                    while (i < size) {
                        Xb1 xb16 = (Xb1) concurrentLinkedDeque2.peekLast();
                        if (xb16 != null && xb16.f != j) {
                            Xb1 xb17 = (Xb1) concurrentLinkedDeque2.pollLast();
                            long j2 = xb17.b;
                            long j3 = j;
                            String str6 = xb17.a;
                            if (j2 > j3) {
                                String str7 = str3;
                                long j4 = xb17.c;
                                if (j4 > j3) {
                                    String str8 = str5;
                                    long j5 = xb17.d;
                                    if (j5 > j3) {
                                        String str9 = str4;
                                        long j6 = xb17.e;
                                        if (j6 > j3) {
                                            if (j2 > j4 || j4 > j5 || j5 > j6 || j6 > xb17.f) {
                                                if (AbstractC4017sd1.b) {
                                                    Log.d("ApmInsight", AbstractC1820cw0.b(new String[]{"page data is not valid:" + xb17.toString()}));
                                                    return;
                                                }
                                                return;
                                            }
                                            JSONObject jSONObject = new JSONObject();
                                            jSONObject.put(IMAPStore.ID_NAME, str9);
                                            jSONObject.put("start", j2);
                                            jSONObject.put("end", xb17.c);
                                            JSONObject jSONObject2 = new JSONObject();
                                            jSONObject2.put(IMAPStore.ID_NAME, str8);
                                            jSONObject2.put("start", xb17.d);
                                            jSONObject2.put("end", xb17.e);
                                            JSONObject jSONObject3 = new JSONObject();
                                            jSONObject3.put(IMAPStore.ID_NAME, str7);
                                            ConcurrentLinkedDeque concurrentLinkedDeque3 = concurrentLinkedDeque2;
                                            int i2 = size;
                                            jSONObject3.put("start", xb17.f);
                                            JSONArray jSONArray = new JSONArray();
                                            jSONArray.put(jSONObject);
                                            jSONArray.put(jSONObject2);
                                            jSONArray.put(jSONObject3);
                                            JSONObject jSONObject4 = new JSONObject();
                                            jSONObject4.put(IMAPStore.ID_NAME, "page_load_stats");
                                            jSONObject4.put("page_type", "activity");
                                            jSONObject4.put("start", j2);
                                            jSONObject4.put("end", xb17.f);
                                            long j7 = xb17.f - j2;
                                            if (j7 >= j3 && j7 <= AbstractC2834k91.c) {
                                                jSONObject4.put("spans", jSONArray);
                                                HashSet hashSet2 = AbstractC2834k91.a;
                                                int i3 = hashSet2.contains(str6) ? 2 : 1;
                                                hashSet2.add(str6);
                                                jSONObject4.put("launch_mode", i3);
                                                jSONObject4.put("collect_from", 1);
                                                jSONObject4.put("page_name", str6);
                                                JSONObject jSONObject5 = new JSONObject();
                                                jSONObject5.put("trace", jSONObject4);
                                                if (AbstractC4017sd1.b) {
                                                    Log.d("ApmInsight", AbstractC1820cw0.b(new String[]{"Receive:PageData"}));
                                                }
                                                ApmInsightInitConfig apmInsightInitConfig = (ApmInsightInitConfig) C0715Ng.d().b;
                                                if (!(apmInsightInitConfig != null ? apmInsightInitConfig.enablePageMonitor() : true)) {
                                                    return;
                                                } else {
                                                    C4784y61.i.b(new RunnableC1568b61(jSONObject5, 0));
                                                }
                                            }
                                            i++;
                                            concurrentLinkedDeque2 = concurrentLinkedDeque3;
                                            size = i2;
                                            str4 = str9;
                                            str3 = str7;
                                            j = j3;
                                            str5 = str8;
                                        }
                                    }
                                }
                            }
                            if (AbstractC4017sd1.b) {
                                Md1.a.i("apm_autopage");
                                return;
                            }
                            return;
                        }
                        return;
                    }
                } catch (Exception e) {
                    if (AbstractC4017sd1.b) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
