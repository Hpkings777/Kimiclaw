package com.google.android.material.behavior;

import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import defpackage.AbstractC1313Yt;
import defpackage.AbstractC4421vX0;
import defpackage.BX0;
import defpackage.CM0;
import defpackage.P21;
import defpackage.S0;

/* JADX INFO: loaded from: classes.dex */
public class SwipeDismissBehavior<V extends View> extends AbstractC1313Yt {
    public BX0 a;
    public boolean b;
    public boolean c;
    public int d = 2;
    public float e = 0.0f;
    public float f = 0.5f;
    public final CM0 g = new CM0(this);

    @Override // defpackage.AbstractC1313Yt
    public boolean f(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
        boolean zO = this.b;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            zO = coordinatorLayout.o(view, (int) motionEvent.getX(), (int) motionEvent.getY());
            this.b = zO;
        } else if (actionMasked == 1 || actionMasked == 3) {
            this.b = false;
        }
        if (zO) {
            if (this.a == null) {
                this.a = new BX0(coordinatorLayout.getContext(), coordinatorLayout, this.g);
            }
            if (!this.c && this.a.r(motionEvent)) {
                return true;
            }
        }
        return false;
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean g(CoordinatorLayout coordinatorLayout, View view, int i) {
        if (view.getImportantForAccessibility() == 0) {
            view.setImportantForAccessibility(1);
            AbstractC4421vX0.n(1048576, view);
            AbstractC4421vX0.j(0, view);
            if (r(view)) {
                AbstractC4421vX0.o(view, S0.n, new P21(this, 17));
            }
        }
        return false;
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean q(View view, MotionEvent motionEvent) {
        if (this.a == null) {
            return false;
        }
        if (this.c && motionEvent.getActionMasked() == 3) {
            return true;
        }
        this.a.k(motionEvent);
        return true;
    }

    public boolean r(View view) {
        return true;
    }
}
