package com.google.android.material.timepicker;

import android.view.ViewTreeObserver;

/* JADX INFO: loaded from: classes.dex */
public final class b implements ViewTreeObserver.OnPreDrawListener {
    public final /* synthetic */ ClockFaceView a;

    public b(ClockFaceView clockFaceView) {
        this.a = clockFaceView;
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public final boolean onPreDraw() {
        ClockFaceView clockFaceView = this.a;
        if (!clockFaceView.isShown()) {
            return true;
        }
        clockFaceView.getViewTreeObserver().removeOnPreDrawListener(this);
        int height = ((clockFaceView.getHeight() / 2) - clockFaceView.D.d) - clockFaceView.L;
        if (height != clockFaceView.B) {
            clockFaceView.B = height;
            clockFaceView.m();
            int i = clockFaceView.B;
            ClockHandView clockHandView = clockFaceView.D;
            clockHandView.l = i;
            clockHandView.invalidate();
        }
        return true;
    }
}
