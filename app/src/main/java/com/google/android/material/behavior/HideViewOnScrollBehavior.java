package com.google.android.material.behavior;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityManager;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.moonshot.kimiclaw.R;
import defpackage.AbstractC1313Yt;
import defpackage.AbstractC2039eV;
import defpackage.AbstractC2917km0;
import defpackage.C1446aS;
import defpackage.C1675bu;
import defpackage.C3787r1;
import defpackage.ViewOnAttachStateChangeListenerC4499w5;
import defpackage.W8;
import defpackage.YI0;
import defpackage.ZR;
import java.util.Iterator;
import java.util.LinkedHashSet;

/* JADX INFO: loaded from: classes.dex */
public class HideViewOnScrollBehavior<V extends View> extends AbstractC1313Yt {
    public C1446aS a;
    public AccessibilityManager b;
    public ZR c;
    public int e;
    public int f;
    public TimeInterpolator g;
    public TimeInterpolator h;
    public ViewPropertyAnimator k;
    public final LinkedHashSet d = new LinkedHashSet();
    public int i = 0;
    public int j = 2;

    public HideViewOnScrollBehavior() {
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean g(CoordinatorLayout coordinatorLayout, View view, int i) {
        int measuredHeight;
        int i2;
        if (this.b == null) {
            this.b = (AccessibilityManager) view.getContext().getSystemService(AccessibilityManager.class);
        }
        AccessibilityManager accessibilityManager = this.b;
        if (accessibilityManager != null && this.c == null) {
            ZR zr = new ZR(this, view, 1);
            this.c = zr;
            accessibilityManager.addTouchExplorationStateChangeListener(zr);
            view.addOnAttachStateChangeListener(new ViewOnAttachStateChangeListenerC4499w5(this, 5));
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int i3 = ((C1675bu) view.getLayoutParams()).c;
        if (i3 == 80 || i3 == 81) {
            r(1);
        } else {
            int absoluteGravity = Gravity.getAbsoluteGravity(i3, i);
            r((absoluteGravity == 3 || absoluteGravity == 19) ? 2 : 0);
        }
        switch (this.a.a) {
            case 0:
                measuredHeight = view.getMeasuredHeight();
                i2 = marginLayoutParams.bottomMargin;
                break;
            case 1:
                measuredHeight = view.getMeasuredWidth();
                i2 = marginLayoutParams.leftMargin;
                break;
            default:
                measuredHeight = view.getMeasuredWidth();
                i2 = marginLayoutParams.rightMargin;
                break;
        }
        this.i = measuredHeight + i2;
        this.e = AbstractC2917km0.Y(view.getContext(), R.attr.motionDurationLong2, 225);
        this.f = AbstractC2917km0.Y(view.getContext(), R.attr.motionDurationMedium4, 175);
        this.g = AbstractC2917km0.Z(view.getContext(), R.attr.motionEasingEmphasizedInterpolator, W8.d);
        this.h = AbstractC2917km0.Z(view.getContext(), R.attr.motionEasingEmphasizedInterpolator, W8.c);
        return false;
    }

    @Override // defpackage.AbstractC1313Yt
    public final void k(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3, int[] iArr) {
        if (i <= 0) {
            if (i < 0) {
                s(view);
                return;
            }
            return;
        }
        if (this.j == 1) {
            return;
        }
        AccessibilityManager accessibilityManager = this.b;
        if (accessibilityManager == null || !accessibilityManager.isTouchExplorationEnabled()) {
            ViewPropertyAnimator viewPropertyAnimator = this.k;
            if (viewPropertyAnimator != null) {
                viewPropertyAnimator.cancel();
                view.clearAnimation();
            }
            this.j = 1;
            Iterator it = this.d.iterator();
            if (it.hasNext()) {
                throw YI0.f(it);
            }
            this.k = this.a.x(this.i, view).setInterpolator(this.h).setDuration(this.f).setListener(new C3787r1(this, 4));
        }
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean o(View view, int i, int i2) {
        return i == 2;
    }

    public final void r(int i) {
        int i2;
        C1446aS c1446aS = this.a;
        if (c1446aS != null) {
            switch (c1446aS.a) {
                case 0:
                    i2 = 1;
                    break;
                case 1:
                    i2 = 2;
                    break;
                default:
                    i2 = 0;
                    break;
            }
            if (i2 == i) {
                return;
            }
        }
        if (i == 0) {
            this.a = new C1446aS(2);
        } else if (i == 1) {
            this.a = new C1446aS(0);
        } else {
            if (i != 2) {
                throw new IllegalArgumentException(AbstractC2039eV.i(i, "Invalid view edge position value: ", ". Must be 0, 1 or 2."));
            }
            this.a = new C1446aS(1);
        }
    }

    public final void s(View view) {
        if (this.j == 2) {
            return;
        }
        ViewPropertyAnimator viewPropertyAnimator = this.k;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
            view.clearAnimation();
        }
        this.j = 2;
        Iterator it = this.d.iterator();
        if (it.hasNext()) {
            throw YI0.f(it);
        }
        this.a.getClass();
        this.k = this.a.x(0, view).setInterpolator(this.g).setDuration(this.e).setListener(new C3787r1(this, 4));
    }

    public HideViewOnScrollBehavior(Context context, AttributeSet attributeSet) {
    }
}
