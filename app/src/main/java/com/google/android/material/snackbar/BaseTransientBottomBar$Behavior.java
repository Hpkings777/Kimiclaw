package com.google.android.material.snackbar;

import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.behavior.SwipeDismissBehavior;
import defpackage.AbstractC2762jf;
import defpackage.C2911kj0;
import defpackage.U61;

/* JADX INFO: loaded from: classes.dex */
public class BaseTransientBottomBar$Behavior extends SwipeDismissBehavior<View> {
    public final U61 h;

    public BaseTransientBottomBar$Behavior() {
        U61 u61 = new U61(13);
        this.e = Math.min(Math.max(0.0f, 0.1f), 1.0f);
        this.f = Math.min(Math.max(0.0f, 0.6f), 1.0f);
        this.d = 0;
        this.h = u61;
    }

    @Override // com.google.android.material.behavior.SwipeDismissBehavior, defpackage.AbstractC1313Yt
    public final boolean f(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
        this.h.getClass();
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1 || actionMasked == 3) {
                if (C2911kj0.b == null) {
                    C2911kj0.b = new C2911kj0();
                }
                synchronized (C2911kj0.b.a) {
                }
            }
        } else if (coordinatorLayout.o(view, (int) motionEvent.getX(), (int) motionEvent.getY())) {
            if (C2911kj0.b == null) {
                C2911kj0.b = new C2911kj0();
            }
            synchronized (C2911kj0.b.a) {
            }
        }
        return super.f(coordinatorLayout, view, motionEvent);
    }

    @Override // com.google.android.material.behavior.SwipeDismissBehavior
    public final boolean r(View view) {
        this.h.getClass();
        return view instanceof AbstractC2762jf;
    }
}
