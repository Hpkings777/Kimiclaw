package com.google.android.material.appbar;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.sun.mail.imap.IMAPStore;
import defpackage.AbstractC2186fY0;

/* JADX INFO: loaded from: classes.dex */
public class AppBarLayout$BaseBehavior<T> extends AbstractC2186fY0 {
    public boolean b;
    public int d;
    public VelocityTracker f;
    public int c = -1;
    public int e = -1;

    public AppBarLayout$BaseBehavior() {
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004e  */
    @Override // defpackage.AbstractC1313Yt
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean f(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
        int iFindPointerIndex;
        if (this.e < 0) {
            this.e = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        if (motionEvent.getActionMasked() == 2 && this.b) {
            int i = this.c;
            if (i != -1 && (iFindPointerIndex = motionEvent.findPointerIndex(i)) != -1) {
                int y = (int) motionEvent.getY(iFindPointerIndex);
                if (Math.abs(y - this.d) > this.e) {
                    this.d = y;
                    return true;
                }
                if (motionEvent.getActionMasked() != 0) {
                }
            }
        } else {
            if (motionEvent.getActionMasked() != 0) {
                this.c = -1;
                motionEvent.getX();
                motionEvent.getY();
                throw new ClassCastException();
            }
            VelocityTracker velocityTracker = this.f;
            if (velocityTracker != null) {
                velocityTracker.addMovement(motionEvent);
            }
        }
        return false;
    }

    @Override // defpackage.AbstractC2186fY0, defpackage.AbstractC1313Yt
    public final boolean g(CoordinatorLayout coordinatorLayout, View view, int i) {
        throw new ClassCastException();
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean h(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3) {
        throw new ClassCastException();
    }

    @Override // defpackage.AbstractC1313Yt
    public final /* synthetic */ void j(CoordinatorLayout coordinatorLayout, View view, View view2, int i, int i2, int[] iArr, int i3) {
        throw new ClassCastException();
    }

    @Override // defpackage.AbstractC1313Yt
    public final void k(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3, int[] iArr) {
        throw new ClassCastException();
    }

    @Override // defpackage.AbstractC1313Yt
    public final void m(View view, Parcelable parcelable) {
        throw new ClassCastException();
    }

    @Override // defpackage.AbstractC1313Yt
    public final Parcelable n(View view) {
        throw new ClassCastException();
    }

    @Override // defpackage.AbstractC1313Yt
    public final boolean o(View view, int i, int i2) {
        throw new ClassCastException();
    }

    @Override // defpackage.AbstractC1313Yt
    public final void p(View view, View view2, int i) {
        throw new ClassCastException();
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0063 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0064 A[RETURN] */
    @Override // defpackage.AbstractC1313Yt
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean q(View view, MotionEvent motionEvent) {
        VelocityTracker velocityTracker;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                int iFindPointerIndex = motionEvent.findPointerIndex(this.c);
                if (iFindPointerIndex != -1) {
                    this.d = (int) motionEvent.getY(iFindPointerIndex);
                    view.getClass();
                    throw new ClassCastException();
                }
            }
            if (actionMasked != 3) {
                if (actionMasked == 6) {
                    int i = motionEvent.getActionIndex() == 0 ? 1 : 0;
                    this.c = motionEvent.getPointerId(i);
                    this.d = (int) (motionEvent.getY(i) + 0.5f);
                }
            }
            velocityTracker = this.f;
            if (velocityTracker != null) {
                velocityTracker.addMovement(motionEvent);
            }
            return !this.b;
        }
        VelocityTracker velocityTracker2 = this.f;
        if (velocityTracker2 != null) {
            velocityTracker2.addMovement(motionEvent);
            this.f.computeCurrentVelocity(IMAPStore.RESPONSE);
            this.f.getYVelocity(this.c);
            view.getClass();
            throw new ClassCastException();
        }
        this.b = false;
        this.c = -1;
        VelocityTracker velocityTracker3 = this.f;
        if (velocityTracker3 != null) {
            velocityTracker3.recycle();
            this.f = null;
        }
        velocityTracker = this.f;
        if (velocityTracker != null) {
        }
        if (!this.b) {
        }
    }

    public AppBarLayout$BaseBehavior(Context context, AttributeSet attributeSet) {
    }
}
