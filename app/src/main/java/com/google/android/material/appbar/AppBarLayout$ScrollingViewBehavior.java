package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC1313Yt;
import defpackage.AbstractC2186fY0;
import defpackage.AbstractC4421vX0;
import defpackage.C1675bu;
import defpackage.T51;
import java.util.ArrayList;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public class AppBarLayout$ScrollingViewBehavior extends AbstractC2186fY0 {
    public final int b;

    public AppBarLayout$ScrollingViewBehavior() {
        new Rect();
        new Rect();
    }

    public static void s(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
        }
    }

    @Override // defpackage.AbstractC1313Yt
    public final void b(View view) {
    }

    @Override // defpackage.AbstractC1313Yt
    public boolean d(CoordinatorLayout coordinatorLayout, View view, View view2) {
        AbstractC1313Yt abstractC1313Yt = ((C1675bu) view2.getLayoutParams()).a;
        if (abstractC1313Yt instanceof AppBarLayout$BaseBehavior) {
            int bottom = view2.getBottom() - view.getTop();
            ((AppBarLayout$BaseBehavior) abstractC1313Yt).getClass();
            int i = this.b;
            int iT = bottom - (i == 0 ? 0 : T51.T((int) (0.0f * i), 0, i));
            WeakHashMap weakHashMap = AbstractC4421vX0.a;
            view.offsetTopAndBottom(iT);
        }
        return false;
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean h(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3) {
        int i4 = view.getLayoutParams().height;
        if (i4 != -1 && i4 != -2) {
            return false;
        }
        s(coordinatorLayout.j(view));
        return false;
    }

    @Override // defpackage.AbstractC1313Yt
    public final void l(CoordinatorLayout coordinatorLayout, View view) {
        s(coordinatorLayout.j(view));
    }

    @Override // defpackage.AbstractC2186fY0
    public final void r(CoordinatorLayout coordinatorLayout, View view, int i) {
        s(coordinatorLayout.j(view));
        coordinatorLayout.q(i, view);
    }

    public AppBarLayout$ScrollingViewBehavior(Context context, AttributeSet attributeSet) {
        new Rect();
        new Rect();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, AbstractC1264Xu0.x);
        this.b = typedArrayObtainStyledAttributes.getDimensionPixelSize(0, 0);
        typedArrayObtainStyledAttributes.recycle();
    }
}
