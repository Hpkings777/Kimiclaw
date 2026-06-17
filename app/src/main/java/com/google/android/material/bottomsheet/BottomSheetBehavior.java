package com.google.android.material.bottomsheet;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.AbsSavedState;
import android.view.MotionEvent;
import android.view.RoundedCorner;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.PathInterpolator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.common.primitives.Ints;
import com.moonshot.kimiclaw.R;
import com.sun.mail.imap.IMAPStore;
import defpackage.A0;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC1313Yt;
import defpackage.AbstractC2917km0;
import defpackage.AbstractC3302nX0;
import defpackage.AbstractC4123tO0;
import defpackage.AbstractC4421vX0;
import defpackage.AbstractC4671xI0;
import defpackage.BX0;
import defpackage.C0663Mg;
import defpackage.C0715Ng;
import defpackage.C0767Og;
import defpackage.C0819Pg;
import defpackage.C0871Qg;
import defpackage.C0967Sc;
import defpackage.C1675bu;
import defpackage.C2888ka0;
import defpackage.C3126mF0;
import defpackage.C3168ma0;
import defpackage.D0;
import defpackage.E0;
import defpackage.FX;
import defpackage.OX;
import defpackage.RunnableC0340Ga;
import defpackage.S0;
import defpackage.ViewOnAttachStateChangeListenerC3863rY0;
import defpackage.YI0;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;
import kotlin.jvm.internal.IntCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public class BottomSheetBehavior<V extends View> extends AbstractC1313Yt {
    public final C0871Qg A;
    public final ValueAnimator B;
    public final int C;
    public int D;
    public int E;
    public final float F;
    public int G;
    public final float H;
    public boolean I;
    public boolean J;
    public final boolean K;
    public final boolean L;
    public boolean M;
    public int N;
    public BX0 O;
    public boolean P;
    public int Q;
    public boolean R;
    public final float S;
    public int T;
    public int U;
    public int V;
    public WeakReference W;
    public WeakReference X;
    public final ArrayList Y;
    public VelocityTracker Z;
    public final int a;
    public int a0;
    public boolean b;
    public int b0;
    public final float c;
    public boolean c0;
    public final int d;
    public HashMap d0;
    public int e;
    public final SparseIntArray e0;
    public boolean f;
    public final C0767Og f0;
    public int g;
    public final int h;
    public final C3168ma0 i;
    public final ColorStateList j;
    public final int k;
    public final int l;
    public int m;
    public final boolean n;
    public final boolean o;
    public final boolean p;
    public final boolean q;
    public final boolean r;
    public final boolean s;
    public final boolean t;
    public final boolean u;
    public int v;
    public int w;
    public final boolean x;
    public final C3126mF0 y;
    public boolean z;

    public BottomSheetBehavior() {
        this.a = 0;
        this.b = true;
        this.k = -1;
        this.l = -1;
        this.A = new C0871Qg(this);
        this.F = 0.5f;
        this.H = -1.0f;
        this.K = true;
        this.L = true;
        this.N = 4;
        this.S = 0.1f;
        this.Y = new ArrayList();
        this.b0 = -1;
        this.e0 = new SparseIntArray();
        this.f0 = new C0767Og(this, 0);
    }

    public static View v(View view) {
        if (view.getVisibility() != 0) {
            return null;
        }
        if (view.isNestedScrollingEnabled()) {
            return view;
        }
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View viewV = v(viewGroup.getChildAt(i));
            if (viewV != null) {
                return viewV;
            }
        }
        return null;
    }

    public static int w(int i, int i2, int i3, int i4) {
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i, i2, i4);
        if (i3 == -1) {
            return childMeasureSpec;
        }
        int mode = View.MeasureSpec.getMode(childMeasureSpec);
        int size = View.MeasureSpec.getSize(childMeasureSpec);
        if (mode == 1073741824) {
            return View.MeasureSpec.makeMeasureSpec(Math.min(size, i3), Ints.MAX_POWER_OF_TWO);
        }
        if (size != 0) {
            i3 = Math.min(size, i3);
        }
        return View.MeasureSpec.makeMeasureSpec(i3, IntCompanionObject.MIN_VALUE);
    }

    public final void A(int i) {
        if (i == -1) {
            if (this.f) {
                return;
            } else {
                this.f = true;
            }
        } else {
            if (!this.f && this.e == i) {
                return;
            }
            this.f = false;
            this.e = Math.max(0, i);
        }
        I();
    }

    public final void B(int i) {
        if (i == 1 || i == 2) {
            throw new IllegalArgumentException(AbstractC4671xI0.p(new StringBuilder("STATE_"), i == 1 ? "DRAGGING" : "SETTLING", " should not be set externally."));
        }
        if (!this.I && i == 5) {
            Log.w("BottomSheetBehavior", "Cannot set state: " + i);
            return;
        }
        int i2 = (i == 6 && this.b && y(i) <= this.D) ? 3 : i;
        WeakReference weakReference = this.W;
        if (weakReference == null || weakReference.get() == null) {
            C(i);
            return;
        }
        View view = (View) this.W.get();
        RunnableC0340Ga runnableC0340Ga = new RunnableC0340Ga(this, view, i2);
        ViewParent parent = view.getParent();
        if (parent != null && parent.isLayoutRequested() && view.isAttachedToWindow()) {
            view.post(runnableC0340Ga);
        } else {
            runnableC0340Ga.run();
        }
    }

    public final void C(int i) {
        if (this.N == i) {
            return;
        }
        this.N = i;
        if (i != 4 && i != 3 && i != 6) {
            boolean z = this.I;
        }
        WeakReference weakReference = this.W;
        if (weakReference == null || ((View) weakReference.get()) == null) {
            return;
        }
        if (i == 3) {
            H(true);
        } else if (i == 6 || i == 5 || i == 4) {
            H(false);
        }
        G(i, true);
        ArrayList arrayList = this.Y;
        if (arrayList.size() > 0) {
            throw AbstractC4671xI0.d(0, arrayList);
        }
        F();
    }

    public final boolean D(View view, float f) {
        if (this.J) {
            return true;
        }
        if (view.getTop() < this.G) {
            return false;
        }
        return Math.abs(((f * this.S) + ((float) view.getTop())) - ((float) this.G)) / ((float) t()) > 0.5f;
    }

    public final void E(View view, int i, boolean z) {
        int iY = y(i);
        BX0 bx0 = this.O;
        if (bx0 == null || (!z ? bx0.s(view, view.getLeft(), iY) : bx0.q(view.getLeft(), iY))) {
            C(i);
            return;
        }
        C(2);
        G(i, true);
        this.A.a(i);
    }

    public final void F() {
        View view;
        int iA;
        WeakReference weakReference = this.W;
        if (weakReference == null || (view = (View) weakReference.get()) == null) {
            return;
        }
        AbstractC4421vX0.n(524288, view);
        AbstractC4421vX0.j(0, view);
        AbstractC4421vX0.n(262144, view);
        AbstractC4421vX0.j(0, view);
        AbstractC4421vX0.n(1048576, view);
        AbstractC4421vX0.j(0, view);
        SparseIntArray sparseIntArray = this.e0;
        int i = sparseIntArray.get(0, -1);
        if (i != -1) {
            AbstractC4421vX0.n(i, view);
            AbstractC4421vX0.j(0, view);
            sparseIntArray.delete(0);
        }
        if (!this.b && this.N != 6) {
            String string = view.getResources().getString(R.string.bottomsheet_action_expand_halfway);
            C0967Sc c0967Sc = new C0967Sc(i, 2, this);
            ArrayList arrayListG = AbstractC4421vX0.g(view);
            int i2 = 0;
            while (true) {
                if (i2 >= arrayListG.size()) {
                    int i3 = -1;
                    for (int i4 = 0; i4 < 32 && i3 == -1; i4++) {
                        int i5 = AbstractC4421vX0.d[i4];
                        boolean z = true;
                        for (int i6 = 0; i6 < arrayListG.size(); i6++) {
                            z &= ((S0) arrayListG.get(i6)).a() != i5;
                        }
                        if (z) {
                            i3 = i5;
                        }
                    }
                    iA = i3;
                } else {
                    if (TextUtils.equals(string, ((AccessibilityNodeInfo.AccessibilityAction) ((S0) arrayListG.get(i2)).a).getLabel())) {
                        iA = ((S0) arrayListG.get(i2)).a();
                        break;
                    }
                    i2++;
                }
            }
            if (iA != -1) {
                S0 s0 = new S0(null, iA, string, c0967Sc, null);
                View.AccessibilityDelegate accessibilityDelegateE = AbstractC4421vX0.e(view);
                E0 e0 = accessibilityDelegateE == null ? null : accessibilityDelegateE instanceof D0 ? ((D0) accessibilityDelegateE).a : new E0(accessibilityDelegateE);
                if (e0 == null) {
                    e0 = new E0();
                }
                AbstractC4421vX0.q(view, e0);
                AbstractC4421vX0.n(s0.a(), view);
                AbstractC4421vX0.g(view).add(s0);
                AbstractC4421vX0.j(0, view);
            }
            sparseIntArray.put(0, iA);
        }
        if (this.I) {
            int i7 = 5;
            if (this.N != 5) {
                AbstractC4421vX0.o(view, S0.n, new C0967Sc(i7, 2, this));
            }
        }
        int i8 = this.N;
        int i9 = 4;
        int i10 = 3;
        if (i8 == 3) {
            AbstractC4421vX0.o(view, S0.m, new C0967Sc(this.b ? 4 : 6, 2, this));
            return;
        }
        if (i8 == 4) {
            AbstractC4421vX0.o(view, S0.l, new C0967Sc(this.b ? 3 : 6, 2, this));
        } else {
            if (i8 != 6) {
                return;
            }
            AbstractC4421vX0.o(view, S0.m, new C0967Sc(i9, 2, this));
            AbstractC4421vX0.o(view, S0.l, new C0967Sc(i10, 2, this));
        }
    }

    public final void G(int i, boolean z) {
        C3168ma0 c3168ma0 = this.i;
        ValueAnimator valueAnimator = this.B;
        if (i == 2) {
            return;
        }
        boolean z2 = this.N == 3 && (this.x || z());
        if (this.z == z2 || c3168ma0 == null) {
            return;
        }
        this.z = z2;
        if (z && valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                valueAnimator.reverse();
                return;
            } else {
                valueAnimator.setFloatValues(c3168ma0.b.j, z2 ? s() : 1.0f);
                valueAnimator.start();
                return;
            }
        }
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        float fS = this.z ? s() : 1.0f;
        C2888ka0 c2888ka0 = c3168ma0.b;
        if (c2888ka0.j != fS) {
            c2888ka0.j = fS;
            c3168ma0.f = true;
            c3168ma0.g = true;
            c3168ma0.invalidateSelf();
        }
    }

    public final void H(boolean z) {
        WeakReference weakReference = this.W;
        if (weakReference == null) {
            return;
        }
        ViewParent parent = ((View) weakReference.get()).getParent();
        if (parent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
            int childCount = coordinatorLayout.getChildCount();
            if (z) {
                if (this.d0 != null) {
                    return;
                } else {
                    this.d0 = new HashMap(childCount);
                }
            }
            for (int i = 0; i < childCount; i++) {
                View childAt = coordinatorLayout.getChildAt(i);
                if (childAt != this.W.get() && z) {
                    this.d0.put(childAt, Integer.valueOf(childAt.getImportantForAccessibility()));
                }
            }
            if (z) {
                return;
            }
            this.d0 = null;
        }
    }

    public final void I() {
        View view;
        if (this.W != null) {
            r();
            if (this.N != 4 || (view = (View) this.W.get()) == null) {
                return;
            }
            view.requestLayout();
        }
    }

    @Override // defpackage.AbstractC1313Yt
    public final void c(C1675bu c1675bu) {
        this.W = null;
        this.O = null;
    }

    @Override // defpackage.AbstractC1313Yt
    public final void e() {
        this.W = null;
        this.O = null;
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean f(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
        int i;
        BX0 bx0;
        if (!view.isShown() || !this.K) {
            this.P = true;
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.a0 = -1;
            this.b0 = -1;
            VelocityTracker velocityTracker = this.Z;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.Z = null;
            }
        }
        if (this.Z == null) {
            this.Z = VelocityTracker.obtain();
        }
        this.Z.addMovement(motionEvent);
        if (actionMasked == 0) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            this.b0 = y;
            if (this.N != 2) {
                WeakReference weakReference = this.X;
                View view2 = weakReference != null ? (View) weakReference.get() : null;
                if (view2 != null && coordinatorLayout.o(view2, x, y)) {
                    this.a0 = motionEvent.getPointerId(motionEvent.getActionIndex());
                    this.c0 = true;
                }
            }
            this.P = this.a0 == -1 && !coordinatorLayout.o(view, x, this.b0);
        } else if (actionMasked == 1 || actionMasked == 3) {
            this.c0 = false;
            this.a0 = -1;
            if (this.P) {
                this.P = false;
                return false;
            }
        }
        if (this.P || (bx0 = this.O) == null || !bx0.r(motionEvent)) {
            WeakReference weakReference2 = this.X;
            View view3 = weakReference2 != null ? (View) weakReference2.get() : null;
            if (actionMasked != 2 || view3 == null || this.P || this.N == 1 || coordinatorLayout.o(view3, (int) motionEvent.getX(), (int) motionEvent.getY()) || this.O == null || (i = this.b0) == -1 || Math.abs(i - motionEvent.getY()) <= this.O.b) {
                return false;
            }
        }
        return true;
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean g(CoordinatorLayout coordinatorLayout, View view, int i) {
        int i2 = this.l;
        C3168ma0 c3168ma0 = this.i;
        if (coordinatorLayout.getFitsSystemWindows() && !view.getFitsSystemWindows()) {
            view.setFitsSystemWindows(true);
        }
        if (this.W == null) {
            this.g = coordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            boolean z = (Build.VERSION.SDK_INT < 29 || this.n || this.f) ? false : true;
            if (this.o || this.p || this.q || this.s || this.t || this.u || z) {
                C0715Ng c0715Ng = new C0715Ng();
                c0715Ng.b = this;
                c0715Ng.a = z;
                int paddingStart = view.getPaddingStart();
                view.getPaddingTop();
                int paddingEnd = view.getPaddingEnd();
                int paddingBottom = view.getPaddingBottom();
                A0 a0 = new A0();
                a0.a = paddingStart;
                a0.b = paddingEnd;
                a0.c = paddingBottom;
                FX fx = new FX(20, c0715Ng, a0);
                WeakHashMap weakHashMap = AbstractC4421vX0.a;
                AbstractC3302nX0.k(view, fx);
                if (view.isAttachedToWindow()) {
                    view.requestApplyInsets();
                } else {
                    view.addOnAttachStateChangeListener(new ViewOnAttachStateChangeListenerC3863rY0());
                }
            }
            AbstractC4421vX0.s(view, new OX(view));
            this.W = new WeakReference(view);
            new PathInterpolator(0.1f, 0.1f, 0.0f, 1.0f);
            Context context = view.getContext();
            AbstractC2917km0.Y(context, R.attr.motionDurationMedium2, 300);
            AbstractC2917km0.Y(context, R.attr.motionDurationShort3, 150);
            AbstractC2917km0.Y(context, R.attr.motionDurationShort2, 100);
            Resources resources = view.getResources();
            resources.getDimension(R.dimen.m3_back_progress_bottom_container_max_scale_x_distance);
            resources.getDimension(R.dimen.m3_back_progress_bottom_container_max_scale_y_distance);
            if (c3168ma0 != null) {
                view.setBackground(c3168ma0);
                float elevation = this.H;
                if (elevation == -1.0f) {
                    elevation = view.getElevation();
                }
                c3168ma0.m(elevation);
            } else {
                ColorStateList colorStateList = this.j;
                if (colorStateList != null) {
                    AbstractC3302nX0.h(view, colorStateList);
                }
            }
            F();
            if (view.getImportantForAccessibility() == 0) {
                view.setImportantForAccessibility(1);
            }
        }
        if (this.O == null) {
            this.O = new BX0(coordinatorLayout.getContext(), coordinatorLayout, this.f0);
        }
        int top = view.getTop();
        coordinatorLayout.q(i, view);
        this.U = coordinatorLayout.getWidth();
        this.V = coordinatorLayout.getHeight();
        int height = view.getHeight();
        this.T = height;
        int iMin = this.V;
        int i3 = iMin - height;
        int i4 = this.w;
        if (i3 < i4) {
            if (this.r) {
                if (i2 != -1) {
                    iMin = Math.min(iMin, i2);
                }
                this.T = iMin;
            } else {
                int iMin2 = iMin - i4;
                if (i2 != -1) {
                    iMin2 = Math.min(iMin2, i2);
                }
                this.T = iMin2;
            }
        }
        this.D = Math.max(0, this.V - this.T);
        this.E = (int) ((1.0f - this.F) * this.V);
        r();
        int i5 = this.N;
        if (i5 == 3) {
            int iX = x();
            WeakHashMap weakHashMap2 = AbstractC4421vX0.a;
            view.offsetTopAndBottom(iX);
        } else if (i5 == 6) {
            int i6 = this.E;
            WeakHashMap weakHashMap3 = AbstractC4421vX0.a;
            view.offsetTopAndBottom(i6);
        } else if (this.I && i5 == 5) {
            int i7 = this.V;
            WeakHashMap weakHashMap4 = AbstractC4421vX0.a;
            view.offsetTopAndBottom(i7);
        } else if (i5 == 4) {
            int i8 = this.G;
            WeakHashMap weakHashMap5 = AbstractC4421vX0.a;
            view.offsetTopAndBottom(i8);
        } else if (i5 == 1 || i5 == 2) {
            int top2 = top - view.getTop();
            WeakHashMap weakHashMap6 = AbstractC4421vX0.a;
            view.offsetTopAndBottom(top2);
        }
        G(this.N, false);
        this.X = new WeakReference(v(view));
        ArrayList arrayList = this.Y;
        if (arrayList.size() <= 0) {
            return true;
        }
        throw AbstractC4671xI0.d(0, arrayList);
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean h(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(w(i, coordinatorLayout.getPaddingRight() + coordinatorLayout.getPaddingLeft() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i2, this.k, marginLayoutParams.width), w(i3, coordinatorLayout.getPaddingBottom() + coordinatorLayout.getPaddingTop() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, this.l, marginLayoutParams.height));
        return true;
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean i(View view) {
        WeakReference weakReference = this.X;
        return (weakReference == null || view != weakReference.get() || this.N == 3 || this.M) ? false : true;
    }

    @Override // defpackage.AbstractC1313Yt
    public final void j(CoordinatorLayout coordinatorLayout, View view, View view2, int i, int i2, int[] iArr, int i3) {
        boolean z = this.K;
        boolean z2 = this.L;
        if (i3 == 1) {
            return;
        }
        WeakReference weakReference = this.X;
        View view3 = weakReference != null ? (View) weakReference.get() : null;
        if (view2 != view3) {
            return;
        }
        int top = view.getTop();
        int i4 = top - i2;
        if (i2 > 0) {
            if (!this.R && !z2 && view2 == view3 && view2.canScrollVertically(1)) {
                this.M = true;
                return;
            }
            if (i4 < x()) {
                int iX = top - x();
                iArr[1] = iX;
                WeakHashMap weakHashMap = AbstractC4421vX0.a;
                view.offsetTopAndBottom(-iX);
                C(3);
            } else {
                if (!z) {
                    return;
                }
                iArr[1] = i2;
                WeakHashMap weakHashMap2 = AbstractC4421vX0.a;
                view.offsetTopAndBottom(-i2);
                C(1);
            }
        } else if (i2 < 0) {
            boolean zCanScrollVertically = view2.canScrollVertically(-1);
            if (!this.R && !z2 && view2 == view3 && zCanScrollVertically) {
                this.M = true;
                return;
            }
            if (!zCanScrollVertically) {
                int i5 = this.G;
                if (i4 > i5 && !this.I) {
                    int i6 = top - i5;
                    iArr[1] = i6;
                    WeakHashMap weakHashMap3 = AbstractC4421vX0.a;
                    view.offsetTopAndBottom(-i6);
                    C(4);
                } else {
                    if (!z) {
                        return;
                    }
                    iArr[1] = i2;
                    WeakHashMap weakHashMap4 = AbstractC4421vX0.a;
                    view.offsetTopAndBottom(-i2);
                    C(1);
                }
            }
        }
        u(view.getTop());
        this.Q = i2;
        this.R = true;
        this.M = false;
    }

    @Override // defpackage.AbstractC1313Yt
    public final void k(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3, int[] iArr) {
    }

    @Override // defpackage.AbstractC1313Yt
    public final void m(View view, Parcelable parcelable) {
        C0819Pg c0819Pg = (C0819Pg) parcelable;
        int i = this.a;
        if (i != 0) {
            if (i == -1 || (i & 1) == 1) {
                this.e = c0819Pg.d;
            }
            if (i == -1 || (i & 2) == 2) {
                this.b = c0819Pg.e;
            }
            if (i == -1 || (i & 4) == 4) {
                this.I = c0819Pg.f;
            }
            if (i == -1 || (i & 8) == 8) {
                this.J = c0819Pg.g;
            }
        }
        int i2 = c0819Pg.c;
        if (i2 == 1 || i2 == 2) {
            this.N = 4;
        } else {
            this.N = i2;
        }
    }

    @Override // defpackage.AbstractC1313Yt
    public final Parcelable n(View view) {
        AbsSavedState absSavedState = View.BaseSavedState.EMPTY_STATE;
        return new C0819Pg(this);
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean o(View view, int i, int i2) {
        this.Q = 0;
        this.R = false;
        return (i & 2) != 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ae  */
    @Override // defpackage.AbstractC1313Yt
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void p(View view, View view2, int i) {
        float yVelocity;
        int i2 = 3;
        if (view.getTop() == x()) {
            C(3);
            return;
        }
        WeakReference weakReference = this.X;
        if (weakReference != null && view2 == weakReference.get() && this.R) {
            if (this.Q > 0) {
                if (!this.b && view.getTop() > this.E) {
                    i2 = 6;
                }
            } else if (this.I) {
                VelocityTracker velocityTracker = this.Z;
                if (velocityTracker == null) {
                    yVelocity = 0.0f;
                } else {
                    velocityTracker.computeCurrentVelocity(IMAPStore.RESPONSE, this.c);
                    yVelocity = this.Z.getYVelocity(this.a0);
                }
                if (D(view, yVelocity)) {
                    i2 = 5;
                }
            } else if (this.Q == 0) {
                int top = view.getTop();
                if (!this.b) {
                    int i3 = this.E;
                    if (top < i3) {
                        if (top >= Math.abs(top - this.G)) {
                        }
                    } else if (Math.abs(top - i3) < Math.abs(top - this.G)) {
                    }
                    i2 = 6;
                } else if (Math.abs(top - this.D) >= Math.abs(top - this.G)) {
                    i2 = 4;
                }
            } else {
                if (!this.b) {
                    int top2 = view.getTop();
                    if (Math.abs(top2 - this.E) < Math.abs(top2 - this.G)) {
                    }
                }
                i2 = 4;
            }
            E(view, i2, false);
            this.R = false;
        }
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean q(View view, MotionEvent motionEvent) {
        if (!view.isShown()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        int i = this.N;
        if (i == 1 && actionMasked == 0) {
            return true;
        }
        BX0 bx0 = this.O;
        if (bx0 != null && (this.K || i == 1)) {
            bx0.k(motionEvent);
        }
        if (actionMasked == 0) {
            this.a0 = -1;
            this.b0 = -1;
            VelocityTracker velocityTracker = this.Z;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.Z = null;
            }
        }
        if (this.Z == null) {
            this.Z = VelocityTracker.obtain();
        }
        this.Z.addMovement(motionEvent);
        if (this.O != null && ((this.K || this.N == 1) && actionMasked == 2 && !this.P)) {
            float fAbs = Math.abs(this.b0 - motionEvent.getY());
            BX0 bx02 = this.O;
            if (fAbs > bx02.b) {
                bx02.b(motionEvent.getPointerId(motionEvent.getActionIndex()), view);
            }
        }
        return !this.P;
    }

    public final void r() {
        int iT = t();
        if (this.b) {
            this.G = Math.max(this.V - iT, this.D);
        } else {
            this.G = this.V - iT;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0056  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final float s() {
        WeakReference weakReference;
        WindowInsets rootWindowInsets;
        float f;
        float f2 = 0.0f;
        if (this.i != null && (weakReference = this.W) != null && weakReference.get() != null && Build.VERSION.SDK_INT >= 31) {
            View view = (View) this.W.get();
            if (z() && (rootWindowInsets = view.getRootWindowInsets()) != null) {
                C3168ma0 c3168ma0 = this.i;
                float[] fArr = c3168ma0.J;
                float fA = fArr != null ? fArr[3] : c3168ma0.b.a.e.a(c3168ma0.g());
                RoundedCorner roundedCorner = rootWindowInsets.getRoundedCorner(0);
                if (roundedCorner != null) {
                    float radius = roundedCorner.getRadius();
                    f = (radius <= 0.0f || fA <= 0.0f) ? 0.0f : radius / fA;
                }
                C3168ma0 c3168ma02 = this.i;
                float[] fArr2 = c3168ma02.J;
                float fA2 = fArr2 != null ? fArr2[0] : c3168ma02.b.a.f.a(c3168ma02.g());
                RoundedCorner roundedCorner2 = rootWindowInsets.getRoundedCorner(1);
                if (roundedCorner2 != null) {
                    float radius2 = roundedCorner2.getRadius();
                    if (radius2 > 0.0f && fA2 > 0.0f) {
                        f2 = radius2 / fA2;
                    }
                }
                return Math.max(f, f2);
            }
        }
        return 0.0f;
    }

    public final int t() {
        int i;
        return this.f ? Math.min(Math.max(this.g, this.V - ((this.U * 9) / 16)), this.T) + this.v : (this.n || this.o || (i = this.m) <= 0) ? this.e + this.v : Math.max(this.e, i + this.h);
    }

    public final void u(int i) {
        if (((View) this.W.get()) != null) {
            ArrayList arrayList = this.Y;
            if (arrayList.isEmpty()) {
                return;
            }
            int i2 = this.G;
            if (i <= i2 && i2 != x()) {
                x();
            }
            if (arrayList.size() > 0) {
                throw AbstractC4671xI0.d(0, arrayList);
            }
        }
    }

    public final int x() {
        if (this.b) {
            return this.D;
        }
        return Math.max(this.C, this.r ? 0 : this.w);
    }

    public final int y(int i) {
        if (i == 3) {
            return x();
        }
        if (i == 4) {
            return this.G;
        }
        if (i == 5) {
            return this.V;
        }
        if (i == 6) {
            return this.E;
        }
        throw new IllegalArgumentException(YI0.h(i, "Invalid state to get top offset: "));
    }

    public final boolean z() {
        WeakReference weakReference = this.W;
        if (weakReference != null && weakReference.get() != null) {
            int[] iArr = new int[2];
            ((View) this.W.get()).getLocationOnScreen(iArr);
            if (iArr[1] == 0) {
                return true;
            }
        }
        return false;
    }

    public BottomSheetBehavior(Context context, AttributeSet attributeSet) {
        int i;
        int i2 = 0;
        this.a = 0;
        this.b = true;
        this.k = -1;
        this.l = -1;
        this.A = new C0871Qg(this);
        this.F = 0.5f;
        this.H = -1.0f;
        this.K = true;
        this.L = true;
        this.N = 4;
        this.S = 0.1f;
        this.Y = new ArrayList();
        this.b0 = -1;
        this.e0 = new SparseIntArray();
        this.f0 = new C0767Og(this, i2);
        this.h = context.getResources().getDimensionPixelSize(R.dimen.mtrl_min_touch_target_size);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, AbstractC1264Xu0.a);
        if (typedArrayObtainStyledAttributes.hasValue(3)) {
            this.j = AbstractC4123tO0.J(context, typedArrayObtainStyledAttributes, 3);
        }
        if (typedArrayObtainStyledAttributes.hasValue(22)) {
            this.y = C3126mF0.b(context, attributeSet, R.attr.bottomSheetStyle, R.style.Widget_Design_BottomSheet_Modal).a();
        }
        C3126mF0 c3126mF0 = this.y;
        if (c3126mF0 != null) {
            C3168ma0 c3168ma0 = new C3168ma0(c3126mF0);
            this.i = c3168ma0;
            c3168ma0.k(context);
            ColorStateList colorStateList = this.j;
            if (colorStateList != null) {
                this.i.n(colorStateList);
            } else {
                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.colorBackground, typedValue, true);
                this.i.setTint(typedValue.data);
            }
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(s(), 1.0f);
        this.B = valueAnimatorOfFloat;
        valueAnimatorOfFloat.setDuration(500L);
        this.B.addUpdateListener(new C0663Mg(this, i2));
        this.H = typedArrayObtainStyledAttributes.getDimension(2, -1.0f);
        if (typedArrayObtainStyledAttributes.hasValue(0)) {
            this.k = typedArrayObtainStyledAttributes.getDimensionPixelSize(0, -1);
        }
        if (typedArrayObtainStyledAttributes.hasValue(1)) {
            this.l = typedArrayObtainStyledAttributes.getDimensionPixelSize(1, -1);
        }
        TypedValue typedValuePeekValue = typedArrayObtainStyledAttributes.peekValue(10);
        if (typedValuePeekValue != null && (i = typedValuePeekValue.data) == -1) {
            A(i);
        } else {
            A(typedArrayObtainStyledAttributes.getDimensionPixelSize(10, -1));
        }
        boolean z = typedArrayObtainStyledAttributes.getBoolean(9, false);
        if (this.I != z) {
            this.I = z;
            if (!z && this.N == 5) {
                B(4);
            }
            F();
        }
        this.n = typedArrayObtainStyledAttributes.getBoolean(14, false);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(7, true);
        if (this.b != z2) {
            this.b = z2;
            if (this.W != null) {
                r();
            }
            C((this.b && this.N == 6) ? 3 : this.N);
            G(this.N, true);
            F();
        }
        this.J = typedArrayObtainStyledAttributes.getBoolean(13, false);
        this.K = typedArrayObtainStyledAttributes.getBoolean(4, true);
        this.L = typedArrayObtainStyledAttributes.getBoolean(5, true);
        this.a = typedArrayObtainStyledAttributes.getInt(11, 0);
        float f = typedArrayObtainStyledAttributes.getFloat(8, 0.5f);
        if (f > 0.0f && f < 1.0f) {
            this.F = f;
            if (this.W != null) {
                this.E = (int) ((1.0f - f) * this.V);
            }
            TypedValue typedValuePeekValue2 = typedArrayObtainStyledAttributes.peekValue(6);
            if (typedValuePeekValue2 != null && typedValuePeekValue2.type == 16) {
                int i3 = typedValuePeekValue2.data;
                if (i3 >= 0) {
                    this.C = i3;
                    G(this.N, true);
                } else {
                    throw new IllegalArgumentException("offset must be greater than or equal to 0");
                }
            } else {
                int dimensionPixelOffset = typedArrayObtainStyledAttributes.getDimensionPixelOffset(6, 0);
                if (dimensionPixelOffset >= 0) {
                    this.C = dimensionPixelOffset;
                    G(this.N, true);
                } else {
                    throw new IllegalArgumentException("offset must be greater than or equal to 0");
                }
            }
            this.d = typedArrayObtainStyledAttributes.getInt(12, 500);
            this.o = typedArrayObtainStyledAttributes.getBoolean(18, false);
            this.p = typedArrayObtainStyledAttributes.getBoolean(19, false);
            this.q = typedArrayObtainStyledAttributes.getBoolean(20, false);
            this.r = typedArrayObtainStyledAttributes.getBoolean(21, true);
            this.s = typedArrayObtainStyledAttributes.getBoolean(15, false);
            this.t = typedArrayObtainStyledAttributes.getBoolean(16, false);
            this.u = typedArrayObtainStyledAttributes.getBoolean(17, false);
            this.x = typedArrayObtainStyledAttributes.getBoolean(24, true);
            typedArrayObtainStyledAttributes.recycle();
            this.c = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
            return;
        }
        throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
    }
}
