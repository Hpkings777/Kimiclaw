package com.bytedance.apm.insight;

import android.app.Activity;

/* JADX INFO: loaded from: classes.dex */
public interface IActivityLeakListener {
    void onActivityLeaked(Activity activity);
}
