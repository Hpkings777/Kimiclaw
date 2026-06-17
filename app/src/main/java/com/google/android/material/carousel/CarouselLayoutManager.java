package com.google.android.material.carousel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.e;
import androidx.recyclerview.widget.f;
import com.moonshot.kimiclaw.R;
import defpackage.AbstractC1081Uh;
import defpackage.AbstractC1264Xu0;
import defpackage.C0048Ak;
import defpackage.C0073Aw0;
import defpackage.C0100Bk;
import defpackage.C3777qw0;
import defpackage.C5010zk;
import defpackage.InterfaceC5035zw0;
import defpackage.UE;
import defpackage.ViewOnLayoutChangeListenerC4871yk;
import defpackage.YI0;

/* JADX INFO: loaded from: classes.dex */
public class CarouselLayoutManager extends e implements InterfaceC5035zw0 {
    public final UE p;
    public AbstractC1081Uh q;
    public final View.OnLayoutChangeListener r;

    public CarouselLayoutManager() {
        UE ue = new UE();
        new C0048Ak();
        this.r = new ViewOnLayoutChangeListenerC4871yk(this, 0);
        this.p = ue;
        n0();
        F0(0);
    }

    public final float C0(float f, float f2) {
        return E0() ? f - f2 : f + f2;
    }

    public final boolean D0() {
        return this.q.b == 0;
    }

    public final boolean E0() {
        return D0() && C() == 1;
    }

    public final void F0(int i) {
        C0100Bk c0100Bk;
        if (i != 0 && i != 1) {
            throw new IllegalArgumentException(YI0.h(i, "invalid orientation:"));
        }
        c(null);
        AbstractC1081Uh abstractC1081Uh = this.q;
        if (abstractC1081Uh == null || i != abstractC1081Uh.b) {
            if (i == 0) {
                c0100Bk = new C0100Bk(this, 1);
            } else {
                if (i != 1) {
                    throw new IllegalArgumentException("invalid orientation");
                }
                c0100Bk = new C0100Bk(this, 0);
            }
            this.q = c0100Bk;
            n0();
        }
    }

    @Override // androidx.recyclerview.widget.e
    public final boolean L() {
        return true;
    }

    @Override // androidx.recyclerview.widget.e
    public final void R(RecyclerView recyclerView) {
        UE ue = this.p;
        Context context = recyclerView.getContext();
        float dimension = ue.a;
        if (dimension <= 0.0f) {
            dimension = context.getResources().getDimension(R.dimen.m3_carousel_small_item_size_min);
        }
        ue.a = dimension;
        float dimension2 = ue.b;
        if (dimension2 <= 0.0f) {
            dimension2 = context.getResources().getDimension(R.dimen.m3_carousel_small_item_size_max);
        }
        ue.b = dimension2;
        n0();
        recyclerView.addOnLayoutChangeListener(this.r);
    }

    @Override // androidx.recyclerview.widget.e
    public final void S(RecyclerView recyclerView) {
        recyclerView.removeOnLayoutChangeListener(this.r);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0046  */
    @Override // androidx.recyclerview.widget.e
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final View T(View view, int i, f fVar, C0073Aw0 c0073Aw0) {
        byte b;
        if (v() == 0) {
            return null;
        }
        int i2 = this.q.b;
        if (i == 1) {
            b = -1;
        } else if (i == 2) {
            b = 1;
        } else if (i == 17) {
            if (i2 == 0) {
                if (E0()) {
                }
            }
            b = -2147483648;
        } else if (i != 33) {
            if (i != 66) {
                if (i != 130) {
                    Log.d("CarouselLayoutManager", "Unknown focus request:" + i);
                } else if (i2 == 1) {
                }
                b = -2147483648;
            } else {
                if (i2 == 0) {
                    if (E0()) {
                    }
                }
                b = -2147483648;
            }
        } else if (i2 != 1) {
            b = -2147483648;
        }
        if (b == -2147483648) {
            return null;
        }
        if (b == -1) {
            if (e.H(view) == 0) {
                return null;
            }
            int iH = e.H(u(0)) - 1;
            if (iH < 0 || iH >= B()) {
                return u(E0() ? v() - 1 : 0);
            }
            this.q.k();
            throw null;
        }
        if (e.H(view) == B() - 1) {
            return null;
        }
        int iH2 = e.H(u(v() - 1)) + 1;
        if (iH2 < 0 || iH2 >= B()) {
            return u(E0() ? 0 : v() - 1);
        }
        this.q.k();
        throw null;
    }

    @Override // androidx.recyclerview.widget.e
    public final void U(AccessibilityEvent accessibilityEvent) {
        super.U(accessibilityEvent);
        if (v() > 0) {
            accessibilityEvent.setFromIndex(e.H(u(0)));
            accessibilityEvent.setToIndex(e.H(u(v() - 1)));
        }
    }

    @Override // androidx.recyclerview.widget.e
    public final void Y(int i, int i2) {
        B();
    }

    @Override // androidx.recyclerview.widget.e
    public final void Z() {
        B();
    }

    @Override // defpackage.InterfaceC5035zw0
    public final PointF a(int i) {
        return null;
    }

    @Override // androidx.recyclerview.widget.e
    public final void b0(int i, int i2) {
        B();
    }

    @Override // androidx.recyclerview.widget.e
    public final boolean d() {
        return D0();
    }

    @Override // androidx.recyclerview.widget.e
    public final void d0(f fVar, C0073Aw0 c0073Aw0) {
        if (c0073Aw0.b() > 0) {
            if ((D0() ? this.n : this.o) > 0.0f) {
                E0();
                fVar.d(0);
                throw new IllegalStateException("All children of a RecyclerView using CarouselLayoutManager must use MaskableFrameLayout as their root ViewGroup.");
            }
        }
        i0(fVar);
    }

    @Override // androidx.recyclerview.widget.e
    public final boolean e() {
        return !D0();
    }

    @Override // androidx.recyclerview.widget.e
    public final void e0(C0073Aw0 c0073Aw0) {
        if (v() == 0) {
            return;
        }
        e.H(u(0));
    }

    @Override // androidx.recyclerview.widget.e
    public final int j(C0073Aw0 c0073Aw0) {
        v();
        return 0;
    }

    @Override // androidx.recyclerview.widget.e
    public final int k(C0073Aw0 c0073Aw0) {
        return 0;
    }

    @Override // androidx.recyclerview.widget.e
    public final int l(C0073Aw0 c0073Aw0) {
        return 0;
    }

    @Override // androidx.recyclerview.widget.e
    public final int m(C0073Aw0 c0073Aw0) {
        v();
        return 0;
    }

    @Override // androidx.recyclerview.widget.e
    public final boolean m0(RecyclerView recyclerView, View view, Rect rect, boolean z, boolean z2) {
        return false;
    }

    @Override // androidx.recyclerview.widget.e
    public final int n(C0073Aw0 c0073Aw0) {
        return 0;
    }

    @Override // androidx.recyclerview.widget.e
    public final int o(C0073Aw0 c0073Aw0) {
        return 0;
    }

    @Override // androidx.recyclerview.widget.e
    public final int o0(int i, C0073Aw0 c0073Aw0, f fVar) {
        if (!D0() || v() == 0 || i == 0) {
            return 0;
        }
        fVar.d(0);
        throw new IllegalStateException("All children of a RecyclerView using CarouselLayoutManager must use MaskableFrameLayout as their root ViewGroup.");
    }

    @Override // androidx.recyclerview.widget.e
    public final void p0(int i) {
    }

    @Override // androidx.recyclerview.widget.e
    public final int q0(int i, C0073Aw0 c0073Aw0, f fVar) {
        if (!e() || v() == 0 || i == 0) {
            return 0;
        }
        fVar.d(0);
        throw new IllegalStateException("All children of a RecyclerView using CarouselLayoutManager must use MaskableFrameLayout as their root ViewGroup.");
    }

    @Override // androidx.recyclerview.widget.e
    public final C3777qw0 r() {
        return new C3777qw0(-2, -2);
    }

    @Override // androidx.recyclerview.widget.e
    public final void y(Rect rect, View view) {
        super.y(rect, view);
        rect.centerY();
        if (D0()) {
            rect.centerX();
        }
        throw null;
    }

    @Override // androidx.recyclerview.widget.e
    public final void z0(RecyclerView recyclerView, int i) {
        C5010zk c5010zk = new C5010zk(this, recyclerView.getContext(), 0);
        c5010zk.a = i;
        A0(c5010zk);
    }

    @SuppressLint({"UnknownNullness"})
    public CarouselLayoutManager(Context context, AttributeSet attributeSet, int i, int i2) {
        new C0048Ak();
        this.r = new ViewOnLayoutChangeListenerC4871yk(this, 0);
        this.p = new UE();
        n0();
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, AbstractC1264Xu0.b);
            typedArrayObtainStyledAttributes.getInt(0, 0);
            n0();
            F0(typedArrayObtainStyledAttributes.getInt(0, 0));
            typedArrayObtainStyledAttributes.recycle();
        }
    }
}
