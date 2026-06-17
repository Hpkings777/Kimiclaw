package com.bytedance.apm.agent.v2.instrumentation;

import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import defpackage.AbstractC0539Jv0;

/* JADX INFO: loaded from: classes.dex */
@Keep
public class FragmentShowAgent {
    private static final String TAG = "FragmentShowAgent";

    @Keep
    public static void onHiddenChanged(Fragment fragment, boolean z) {
        AbstractC0539Jv0.K(fragment, !z);
    }

    @Keep
    public static void onPause(Fragment fragment) {
        if (!fragment.getUserVisibleHint() || fragment.isHidden()) {
            return;
        }
        AbstractC0539Jv0.K(fragment, false);
    }

    @Keep
    public static void onResume(Fragment fragment) {
        if (!fragment.getUserVisibleHint() || fragment.isHidden()) {
            return;
        }
        AbstractC0539Jv0.K(fragment, true);
    }

    @Keep
    public static void setUserVisibleHint(Fragment fragment, boolean z) {
        if (!fragment.isResumed() || fragment.isHidden()) {
            return;
        }
        AbstractC0539Jv0.K(fragment, z);
    }
}
