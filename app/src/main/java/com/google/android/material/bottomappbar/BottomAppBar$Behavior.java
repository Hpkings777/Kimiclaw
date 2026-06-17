package com.google.android.material.bottomappbar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import defpackage.ViewOnLayoutChangeListenerC0612Lg;

/* JADX INFO: loaded from: classes.dex */
public class BottomAppBar$Behavior extends HideBottomViewOnScrollBehavior<Object> {
    public BottomAppBar$Behavior() {
        new ViewOnLayoutChangeListenerC0612Lg(this);
        new Rect();
    }

    @Override // com.google.android.material.behavior.HideBottomViewOnScrollBehavior, defpackage.AbstractC1313Yt
    public final boolean g(CoordinatorLayout coordinatorLayout, View view, int i) {
        throw new ClassCastException();
    }

    @Override // com.google.android.material.behavior.HideBottomViewOnScrollBehavior, defpackage.AbstractC1313Yt
    public final boolean o(View view, int i, int i2) {
        throw new ClassCastException();
    }

    public BottomAppBar$Behavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        new ViewOnLayoutChangeListenerC0612Lg(this);
        new Rect();
    }
}
