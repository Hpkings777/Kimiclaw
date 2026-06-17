package com.google.android.material.behavior;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityManager;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.moonshot.kimiclaw.R;
import defpackage.AbstractC1313Yt;
import defpackage.AbstractC2917km0;
import defpackage.C3787r1;
import defpackage.ViewOnAttachStateChangeListenerC4499w5;
import defpackage.W8;
import defpackage.YI0;
import defpackage.ZR;
import java.util.Iterator;
import java.util.LinkedHashSet;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class HideBottomViewOnScrollBehavior<V extends View> extends AbstractC1313Yt {
    public int b;
    public int c;
    public TimeInterpolator d;
    public TimeInterpolator e;
    public AccessibilityManager g;
    public ZR h;
    public ViewPropertyAnimator k;
    public final LinkedHashSet a = new LinkedHashSet();
    public int f = 0;
    public final boolean i = true;
    public int j = 2;

    public HideBottomViewOnScrollBehavior() {
    }

    @Override // defpackage.AbstractC1313Yt
    public boolean g(CoordinatorLayout coordinatorLayout, View view, int i) {
        this.f = view.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).bottomMargin;
        this.b = AbstractC2917km0.Y(view.getContext(), R.attr.motionDurationLong2, 225);
        this.c = AbstractC2917km0.Y(view.getContext(), R.attr.motionDurationMedium4, 175);
        this.d = AbstractC2917km0.Z(view.getContext(), R.attr.motionEasingEmphasizedInterpolator, W8.d);
        this.e = AbstractC2917km0.Z(view.getContext(), R.attr.motionEasingEmphasizedInterpolator, W8.c);
        if (this.g == null) {
            this.g = (AccessibilityManager) view.getContext().getSystemService(AccessibilityManager.class);
        }
        AccessibilityManager accessibilityManager = this.g;
        if (accessibilityManager == null || this.h != null) {
            return false;
        }
        ZR zr = new ZR(this, view, 0);
        this.h = zr;
        accessibilityManager.addTouchExplorationStateChangeListener(zr);
        view.addOnAttachStateChangeListener(new ViewOnAttachStateChangeListenerC4499w5(this, 4));
        return false;
    }

    @Override // defpackage.AbstractC1313Yt
    public final void k(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3, int[] iArr) {
        AccessibilityManager accessibilityManager;
        if (i <= 0) {
            if (i < 0) {
                r(view);
                return;
            }
            return;
        }
        if (this.j == 1) {
            return;
        }
        if (this.i && (accessibilityManager = this.g) != null && accessibilityManager.isTouchExplorationEnabled()) {
            return;
        }
        ViewPropertyAnimator viewPropertyAnimator = this.k;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
            view.clearAnimation();
        }
        this.j = 1;
        Iterator it = this.a.iterator();
        if (it.hasNext()) {
            throw YI0.f(it);
        }
        this.k = view.animate().translationY(this.f).setInterpolator(this.e).setDuration(this.c).setListener(new C3787r1(this, 3));
    }

    @Override // defpackage.AbstractC1313Yt
    public boolean o(View view, int i, int i2) {
        return i == 2;
    }

    public final void r(View view) {
        if (this.j == 2) {
            return;
        }
        ViewPropertyAnimator viewPropertyAnimator = this.k;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
            view.clearAnimation();
        }
        this.j = 2;
        Iterator it = this.a.iterator();
        if (it.hasNext()) {
            throw YI0.f(it);
        }
        this.k = view.animate().translationY(0).setInterpolator(this.d).setDuration(this.b).setListener(new C3787r1(this, 3));
    }

    public HideBottomViewOnScrollBehavior(Context context, AttributeSet attributeSet) {
    }
}
