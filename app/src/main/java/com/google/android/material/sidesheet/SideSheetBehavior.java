package com.google.android.material.sidesheet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.AbsSavedState;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.moonshot.kimiclaw.R;
import defpackage.AbstractC0173Cu0;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC1313Yt;
import defpackage.AbstractC2039eV;
import defpackage.AbstractC2917km0;
import defpackage.AbstractC3302nX0;
import defpackage.AbstractC4123tO0;
import defpackage.AbstractC4421vX0;
import defpackage.BX0;
import defpackage.C0767Og;
import defpackage.C0871Qg;
import defpackage.C1675bu;
import defpackage.C2664j;
import defpackage.C2986lF0;
import defpackage.C3126mF0;
import defpackage.C3168ma0;
import defpackage.C4390vI;
import defpackage.P40;
import defpackage.S0;
import defpackage.WF0;
import defpackage.YI0;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public class SideSheetBehavior<V extends View> extends AbstractC1313Yt {
    public AbstractC0173Cu0 a;
    public final C3168ma0 b;
    public final ColorStateList c;
    public final C3126mF0 d;
    public final C0871Qg e;
    public final float f;
    public final boolean g;
    public int h;
    public BX0 i;
    public boolean j;
    public final float k;
    public int l;
    public int m;
    public int n;
    public int o;
    public WeakReference p;
    public WeakReference q;
    public final int r;
    public VelocityTracker s;
    public int t;
    public final LinkedHashSet u;
    public final C0767Og v;

    public SideSheetBehavior() {
        this.e = new C0871Qg(this);
        this.g = true;
        this.h = 5;
        this.k = 0.1f;
        this.r = -1;
        this.u = new LinkedHashSet();
        this.v = new C0767Og(this, 1);
    }

    @Override // defpackage.AbstractC1313Yt
    public final void c(C1675bu c1675bu) {
        this.p = null;
        this.i = null;
    }

    @Override // defpackage.AbstractC1313Yt
    public final void e() {
        this.p = null;
        this.i = null;
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean f(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
        BX0 bx0;
        VelocityTracker velocityTracker;
        if ((!view.isShown() && AbstractC4421vX0.f(view) == null) || !this.g) {
            this.j = true;
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0 && (velocityTracker = this.s) != null) {
            velocityTracker.recycle();
            this.s = null;
        }
        if (this.s == null) {
            this.s = VelocityTracker.obtain();
        }
        this.s.addMovement(motionEvent);
        if (actionMasked == 0) {
            this.t = (int) motionEvent.getX();
        } else if ((actionMasked == 1 || actionMasked == 3) && this.j) {
            this.j = false;
            return false;
        }
        return (this.j || (bx0 = this.i) == null || !bx0.r(motionEvent)) ? false : true;
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean g(CoordinatorLayout coordinatorLayout, View view, int i) {
        View view2;
        View view3;
        int i2;
        View viewFindViewById;
        int iU = 0;
        C3168ma0 c3168ma0 = this.b;
        int i3 = 1;
        if (coordinatorLayout.getFitsSystemWindows() && !view.getFitsSystemWindows()) {
            view.setFitsSystemWindows(true);
        }
        if (this.p == null) {
            this.p = new WeakReference(view);
            new PathInterpolator(0.1f, 0.1f, 0.0f, 1.0f);
            Context context = view.getContext();
            AbstractC2917km0.Y(context, R.attr.motionDurationMedium2, 300);
            AbstractC2917km0.Y(context, R.attr.motionDurationShort3, 150);
            AbstractC2917km0.Y(context, R.attr.motionDurationShort2, 100);
            Resources resources = view.getResources();
            resources.getDimension(R.dimen.m3_back_progress_side_container_max_scale_x_distance_shrink);
            resources.getDimension(R.dimen.m3_back_progress_side_container_max_scale_x_distance_grow);
            resources.getDimension(R.dimen.m3_back_progress_side_container_max_scale_y_distance);
            if (c3168ma0 != null) {
                view.setBackground(c3168ma0);
                float elevation = this.f;
                if (elevation == -1.0f) {
                    elevation = view.getElevation();
                }
                c3168ma0.m(elevation);
            } else {
                ColorStateList colorStateList = this.c;
                if (colorStateList != null) {
                    WeakHashMap weakHashMap = AbstractC4421vX0.a;
                    AbstractC3302nX0.h(view, colorStateList);
                }
            }
            int i4 = this.h == 5 ? 4 : 0;
            if (view.getVisibility() != i4) {
                view.setVisibility(i4);
            }
            u();
            if (view.getImportantForAccessibility() == 0) {
                view.setImportantForAccessibility(1);
            }
            if (AbstractC4421vX0.f(view) == null) {
                AbstractC4421vX0.r(view, view.getResources().getString(R.string.side_sheet_accessibility_pane_title));
            }
        }
        int i5 = Gravity.getAbsoluteGravity(((C1675bu) view.getLayoutParams()).c, i) == 3 ? 1 : 0;
        AbstractC0173Cu0 abstractC0173Cu0 = this.a;
        if (abstractC0173Cu0 == null || abstractC0173Cu0.w() != i5) {
            C3126mF0 c3126mF0 = this.d;
            C1675bu c1675bu = null;
            if (i5 == 0) {
                this.a = new P40(this, i3);
                if (c3126mF0 != null) {
                    WeakReference weakReference = this.p;
                    if (weakReference != null && (view3 = (View) weakReference.get()) != null && (view3.getLayoutParams() instanceof C1675bu)) {
                        c1675bu = (C1675bu) view3.getLayoutParams();
                    }
                    if (c1675bu == null || ((ViewGroup.MarginLayoutParams) c1675bu).rightMargin <= 0) {
                        C2986lF0 c2986lF0F = c3126mF0.f();
                        c2986lF0F.f = new C2664j(0.0f);
                        c2986lF0F.g = new C2664j(0.0f);
                        C3126mF0 c3126mF0A = c2986lF0F.a();
                        if (c3168ma0 != null) {
                            c3168ma0.setShapeAppearanceModel(c3126mF0A);
                        }
                    }
                }
            } else {
                if (i5 != 1) {
                    throw new IllegalArgumentException(AbstractC2039eV.i(i5, "Invalid sheet edge position value: ", ". Must be 0 or 1."));
                }
                this.a = new P40(this, iU);
                if (c3126mF0 != null) {
                    WeakReference weakReference2 = this.p;
                    if (weakReference2 != null && (view2 = (View) weakReference2.get()) != null && (view2.getLayoutParams() instanceof C1675bu)) {
                        c1675bu = (C1675bu) view2.getLayoutParams();
                    }
                    if (c1675bu == null || ((ViewGroup.MarginLayoutParams) c1675bu).leftMargin <= 0) {
                        C2986lF0 c2986lF0F2 = c3126mF0.f();
                        c2986lF0F2.e = new C2664j(0.0f);
                        c2986lF0F2.h = new C2664j(0.0f);
                        C3126mF0 c3126mF0A2 = c2986lF0F2.a();
                        if (c3168ma0 != null) {
                            c3168ma0.setShapeAppearanceModel(c3126mF0A2);
                        }
                    }
                }
            }
        }
        if (this.i == null) {
            this.i = new BX0(coordinatorLayout.getContext(), coordinatorLayout, this.v);
        }
        int iU2 = this.a.u(view);
        coordinatorLayout.q(i, view);
        this.m = coordinatorLayout.getWidth();
        this.n = this.a.v(coordinatorLayout);
        this.l = view.getWidth();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        this.o = marginLayoutParams != null ? this.a.m(marginLayoutParams) : 0;
        int i6 = this.h;
        if (i6 == 1 || i6 == 2) {
            iU = iU2 - this.a.u(view);
        } else if (i6 != 3) {
            if (i6 != 5) {
                throw new IllegalStateException("Unexpected value: " + this.h);
            }
            iU = this.a.q();
        }
        WeakHashMap weakHashMap2 = AbstractC4421vX0.a;
        view.offsetLeftAndRight(iU);
        if (this.q == null && (i2 = this.r) != -1 && (viewFindViewById = coordinatorLayout.findViewById(i2)) != null) {
            this.q = new WeakReference(viewFindViewById);
        }
        Iterator it = this.u.iterator();
        while (it.hasNext()) {
            if (it.next() != null) {
                throw new ClassCastException();
            }
        }
        return true;
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean h(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(ViewGroup.getChildMeasureSpec(i, coordinatorLayout.getPaddingRight() + coordinatorLayout.getPaddingLeft() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i2, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i3, coordinatorLayout.getPaddingBottom() + coordinatorLayout.getPaddingTop() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height));
        return true;
    }

    @Override // defpackage.AbstractC1313Yt
    public final void m(View view, Parcelable parcelable) {
        int i = ((WF0) parcelable).c;
        if (i == 1 || i == 2) {
            i = 5;
        }
        this.h = i;
    }

    @Override // defpackage.AbstractC1313Yt
    public final Parcelable n(View view) {
        AbsSavedState absSavedState = View.BaseSavedState.EMPTY_STATE;
        return new WF0(this);
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean q(View view, MotionEvent motionEvent) {
        VelocityTracker velocityTracker;
        if (!view.isShown()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (this.h == 1 && actionMasked == 0) {
            return true;
        }
        if (s()) {
            this.i.k(motionEvent);
        }
        if (actionMasked == 0 && (velocityTracker = this.s) != null) {
            velocityTracker.recycle();
            this.s = null;
        }
        if (this.s == null) {
            this.s = VelocityTracker.obtain();
        }
        this.s.addMovement(motionEvent);
        if (s() && actionMasked == 2 && !this.j && s()) {
            float fAbs = Math.abs(this.t - motionEvent.getX());
            BX0 bx0 = this.i;
            if (fAbs > bx0.b) {
                bx0.b(motionEvent.getPointerId(motionEvent.getActionIndex()), view);
            }
        }
        return !this.j;
    }

    public final void r(int i) {
        View view;
        if (this.h == i) {
            return;
        }
        this.h = i;
        WeakReference weakReference = this.p;
        if (weakReference == null || (view = (View) weakReference.get()) == null) {
            return;
        }
        int i2 = this.h == 5 ? 4 : 0;
        if (view.getVisibility() != i2) {
            view.setVisibility(i2);
        }
        Iterator it = this.u.iterator();
        if (it.hasNext()) {
            throw YI0.f(it);
        }
        u();
    }

    public final boolean s() {
        if (this.i != null) {
            return this.g || this.h == 1;
        }
        return false;
    }

    public final void t(View view, int i, boolean z) {
        int iP;
        if (i == 3) {
            iP = this.a.p();
        } else {
            if (i != 5) {
                throw new IllegalArgumentException(YI0.h(i, "Invalid state to get outer edge offset: "));
            }
            iP = this.a.q();
        }
        BX0 bx0 = this.i;
        if (bx0 == null || (!z ? bx0.s(view, iP, view.getTop()) : bx0.q(iP, view.getTop()))) {
            r(i);
        } else {
            r(2);
            this.e.a(i);
        }
    }

    public final void u() {
        View view;
        WeakReference weakReference = this.p;
        if (weakReference == null || (view = (View) weakReference.get()) == null) {
            return;
        }
        AbstractC4421vX0.n(262144, view);
        AbstractC4421vX0.j(0, view);
        AbstractC4421vX0.n(1048576, view);
        AbstractC4421vX0.j(0, view);
        int i = 5;
        if (this.h != 5) {
            AbstractC4421vX0.o(view, S0.n, new C4390vI(i, 2, this));
        }
        int i2 = 3;
        if (this.h != 3) {
            AbstractC4421vX0.o(view, S0.l, new C4390vI(i2, 2, this));
        }
    }

    public SideSheetBehavior(Context context, AttributeSet attributeSet) {
        this.e = new C0871Qg(this);
        this.g = true;
        this.h = 5;
        this.k = 0.1f;
        this.r = -1;
        this.u = new LinkedHashSet();
        this.v = new C0767Og(this, 1);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, AbstractC1264Xu0.z);
        if (typedArrayObtainStyledAttributes.hasValue(3)) {
            this.c = AbstractC4123tO0.J(context, typedArrayObtainStyledAttributes, 3);
        }
        if (typedArrayObtainStyledAttributes.hasValue(6)) {
            this.d = C3126mF0.b(context, attributeSet, 0, R.style.Widget_Material3_SideSheet).a();
        }
        if (typedArrayObtainStyledAttributes.hasValue(5)) {
            int resourceId = typedArrayObtainStyledAttributes.getResourceId(5, -1);
            this.r = resourceId;
            WeakReference weakReference = this.q;
            if (weakReference != null) {
                weakReference.clear();
            }
            this.q = null;
            WeakReference weakReference2 = this.p;
            if (weakReference2 != null) {
                View view = (View) weakReference2.get();
                if (resourceId != -1 && view.isLaidOut()) {
                    view.requestLayout();
                }
            }
        }
        C3126mF0 c3126mF0 = this.d;
        if (c3126mF0 != null) {
            C3168ma0 c3168ma0 = new C3168ma0(c3126mF0);
            this.b = c3168ma0;
            c3168ma0.k(context);
            ColorStateList colorStateList = this.c;
            if (colorStateList != null) {
                this.b.n(colorStateList);
            } else {
                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.colorBackground, typedValue, true);
                this.b.setTint(typedValue.data);
            }
        }
        this.f = typedArrayObtainStyledAttributes.getDimension(2, -1.0f);
        this.g = typedArrayObtainStyledAttributes.getBoolean(4, true);
        typedArrayObtainStyledAttributes.recycle();
        ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }
}
