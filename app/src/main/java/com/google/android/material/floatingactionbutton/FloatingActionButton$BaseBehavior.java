package com.google.android.material.floatingactionbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC1313Yt;
import defpackage.C1675bu;

/* JADX INFO: loaded from: classes.dex */
public class FloatingActionButton$BaseBehavior<T> extends AbstractC1313Yt {
    public FloatingActionButton$BaseBehavior() {
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean a(View view) {
        throw new ClassCastException();
    }

    @Override // defpackage.AbstractC1313Yt
    public final void c(C1675bu c1675bu) {
        if (c1675bu.h == 0) {
            c1675bu.h = 80;
        }
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean d(CoordinatorLayout coordinatorLayout, View view, View view2) {
        throw new ClassCastException();
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean g(CoordinatorLayout coordinatorLayout, View view, int i) {
        throw new ClassCastException();
    }

    public FloatingActionButton$BaseBehavior(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, AbstractC1264Xu0.g);
        typedArrayObtainStyledAttributes.getBoolean(0, true);
        typedArrayObtainStyledAttributes.recycle();
    }
}
