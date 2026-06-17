package com.bytedance.apm.agent.v2.instrumentation;

import android.annotation.TargetApi;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import com.sun.mail.imap.IMAPStore;
import defpackage.AbstractC4017sd1;
import defpackage.AbstractC4638x41;
import defpackage.B9;
import defpackage.C1049Tr;
import defpackage.C4784y61;
import defpackage.C4992zc;
import defpackage.K31;
import defpackage.RunnableC1568b61;
import defpackage.RunnableC4791y9;
import defpackage.XO;
import defpackage.YO;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@Keep
public class FragmentTimeAgent {
    private static final String TAG = "FragmentTimeAgent";
    private static long sCheckVisibilityStartTime = 0;
    private static String sFragmentName = null;
    private static long sMaxWaitTime = 60000;
    private static HashSet<String> sMethodSet = new HashSet<>(32);
    private static long sOnActivityCreatedEnd;
    private static long sOnActivityCreatedStart;
    private static long sOnCreateEnd;
    private static long sOnCreateStart;
    private static ViewTreeObserver.OnGlobalLayoutListener sOnGlobalLayoutListener;
    private static long sOnResumeEnd;
    private static long sOnResumeStart;
    private static long sOnViewCreatedEnd;
    private static long sOnViewCreatedStart;
    private static boolean sReported;
    private static WeakReference<View> sRootViewRef;
    private static Runnable sWaitViewTimeoutRunnable;

    private static boolean isValid(String str) {
        return !sReported && TextUtils.equals(str, sFragmentName);
    }

    @Keep
    public static void onHiddenChanged(Fragment fragment, boolean z) {
        if (z) {
            return;
        }
        try {
            String canonicalName = fragment.getClass().getCanonicalName();
            Integer num = (Integer) K31.a.get(canonicalName);
            if (num != null && num.intValue() > 0) {
                registerOnGlobalLayoutListener(canonicalName, fragment.getView(), num, System.currentTimeMillis(), "fragmentOnHiddenChangedToViewShow");
            }
        } catch (Exception unused) {
        }
    }

    public static void onResumeShow(Fragment fragment, String str) {
        if (!fragment.getUserVisibleHint() || fragment.isHidden()) {
            return;
        }
        try {
            Integer num = (Integer) K31.a.get(str);
            if (num != null && num.intValue() > 0) {
                registerOnGlobalLayoutListener(str, fragment.getView(), num, sOnCreateStart, "fragmentOnCreateToViewShow");
            }
        } catch (Exception unused) {
        }
    }

    @Keep
    public static void onTrace(Fragment fragment, String str, String str2, boolean z) {
        if (TextUtils.equals(AppAgent.ON_CREATE, str2)) {
            if (!z) {
                if (isValid(str)) {
                    sOnCreateEnd = System.currentTimeMillis();
                    return;
                }
                return;
            }
            sFragmentName = str;
            sOnCreateStart = System.currentTimeMillis();
            if (sMaxWaitTime == 0) {
                if (B9.a.a == null) {
                    new C4992zc(new C1049Tr());
                }
                sMaxWaitTime = 20000L;
            }
            sReported = false;
            return;
        }
        if (TextUtils.equals("onViewCreated", str2) && isValid(str)) {
            if (z) {
                if (sOnCreateStart > 0) {
                    sOnViewCreatedStart = System.currentTimeMillis();
                    return;
                }
                return;
            } else {
                if (sOnCreateStart > 0) {
                    sOnViewCreatedEnd = System.currentTimeMillis();
                    return;
                }
                return;
            }
        }
        if (TextUtils.equals("onActivityCreated", str2) && isValid(str)) {
            if (z) {
                if (sOnCreateStart > 0) {
                    sOnActivityCreatedStart = System.currentTimeMillis();
                    return;
                }
                return;
            } else {
                if (sOnCreateStart > 0) {
                    sOnActivityCreatedEnd = System.currentTimeMillis();
                    return;
                }
                return;
            }
        }
        if (TextUtils.equals("onResume", str2) && isValid(str)) {
            if (z) {
                if (sOnCreateStart > 0) {
                    sOnResumeStart = System.currentTimeMillis();
                    onResumeShow(fragment, str);
                    return;
                }
                return;
            }
            if (sOnCreateStart > 0) {
                sOnResumeEnd = System.currentTimeMillis();
                try {
                    Integer num = (Integer) K31.a.get(str);
                    if (num == null || num.intValue() <= 0) {
                        C4784y61.i.b(new RunnableC4791y9(2));
                    }
                } catch (Exception unused) {
                }
                sReported = true;
                sFragmentName = null;
            }
        }
    }

    @TargetApi(16)
    private static void registerOnGlobalLayoutListener(String str, View view, Integer num, long j, String str2) {
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
        if (num == null || num.intValue() <= 0) {
            return;
        }
        sFragmentName = str;
        WeakReference<View> weakReference = sRootViewRef;
        if (weakReference != null && weakReference.get() != view) {
            ViewTreeObserver viewTreeObserver = sRootViewRef.get().getViewTreeObserver();
            if (viewTreeObserver.isAlive() && (onGlobalLayoutListener = sOnGlobalLayoutListener) != null) {
                viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
            }
            if (sWaitViewTimeoutRunnable != null) {
                AbstractC4638x41.a.removeCallbacks(sWaitViewTimeoutRunnable);
            }
        }
        sRootViewRef = new WeakReference<>(view);
        sCheckVisibilityStartTime = 0L;
        sOnGlobalLayoutListener = new YO(num, j, str2);
        view.getViewTreeObserver().addOnGlobalLayoutListener(sOnGlobalLayoutListener);
        sWaitViewTimeoutRunnable = new RunnableC4791y9(3);
        AbstractC4638x41.a.postDelayed(sWaitViewTimeoutRunnable, sMaxWaitTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void reportStats(boolean z, String str, long j, long j2) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(IMAPStore.ID_NAME, AppAgent.ON_CREATE);
            jSONObject.put("start", sOnCreateStart);
            jSONObject.put("end", sOnCreateEnd);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(IMAPStore.ID_NAME, "onViewCreated");
            jSONObject2.put("start", sOnViewCreatedStart);
            jSONObject2.put("end", sOnViewCreatedEnd);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put(IMAPStore.ID_NAME, "onActivityCreated");
            jSONObject3.put("start", sOnActivityCreatedStart);
            jSONObject3.put("end", sOnActivityCreatedEnd);
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put(IMAPStore.ID_NAME, "onResume");
            jSONObject4.put("start", sOnResumeStart);
            jSONObject4.put("end", sOnResumeEnd);
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(jSONObject);
            jSONArray.put(jSONObject2);
            jSONArray.put(jSONObject3);
            jSONArray.put(jSONObject4);
            if (str != null) {
                JSONObject jSONObject5 = new JSONObject();
                jSONObject5.put(IMAPStore.ID_NAME, str);
                jSONObject5.put("start", j);
                jSONObject5.put("end", j2);
                jSONArray.put(jSONObject5);
            }
            JSONObject jSONObject6 = new JSONObject();
            jSONObject6.put(IMAPStore.ID_NAME, "page_load_stats");
            jSONObject6.put("page_type", "fragment");
            jSONObject6.put("start", sOnCreateStart);
            jSONObject6.put("spans", jSONArray);
            jSONObject6.put("launch_mode", z ? 2 : 1);
            jSONObject6.put("collect_from", 1);
            jSONObject6.put("page_name", sFragmentName);
            JSONObject jSONObject7 = new JSONObject();
            jSONObject7.put("trace", jSONObject6);
            C4784y61.i.b(new RunnableC1568b61(jSONObject7, 0));
        } catch (Exception e) {
            if (AbstractC4017sd1.b) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void reportTraceTime(String str, String str2, long j, long j2) {
        C4784y61.i.b(new XO(str2, j, j2));
    }

    @Keep
    public static void setUserVisibleHint(Fragment fragment, boolean z) {
        if (z && fragment.isResumed() && !fragment.isHidden()) {
            try {
                String canonicalName = fragment.getClass().getCanonicalName();
                Integer num = (Integer) K31.a.get(canonicalName);
                if (num != null && num.intValue() > 0) {
                    registerOnGlobalLayoutListener(canonicalName, fragment.getView(), num, System.currentTimeMillis(), "fragmentUserVisibleToViewShow");
                }
            } catch (Exception unused) {
            }
        }
    }
}
