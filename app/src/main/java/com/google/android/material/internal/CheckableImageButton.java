package com.google.android.material.internal;

import android.R;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import defpackage.AbstractC4421vX0;
import defpackage.C2637im;
import defpackage.C2776jm;
import defpackage.C2887ka;

/* JADX INFO: loaded from: classes.dex */
public class CheckableImageButton extends C2887ka implements Checkable {
    public static final int[] g = {R.attr.state_checked};
    public boolean d;
    public boolean e;
    public boolean f;

    public CheckableImageButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, com.moonshot.kimiclaw.R.attr.imageButtonStyle);
        this.e = true;
        this.f = true;
        AbstractC4421vX0.q(this, new C2637im(this, 0));
    }

    @Override // android.widget.Checkable
    public final boolean isChecked() {
        return this.d;
    }

    @Override // android.widget.ImageView, android.view.View
    public final int[] onCreateDrawableState(int i) {
        return this.d ? View.mergeDrawableStates(super.onCreateDrawableState(i + 1), g) : super.onCreateDrawableState(i);
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof C2776jm)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        C2776jm c2776jm = (C2776jm) parcelable;
        super.onRestoreInstanceState(c2776jm.a);
        setChecked(c2776jm.c);
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        C2776jm c2776jm = new C2776jm(super.onSaveInstanceState());
        c2776jm.c = this.d;
        return c2776jm;
    }

    public void setCheckable(boolean z) {
        if (this.e != z) {
            this.e = z;
            sendAccessibilityEvent(0);
        }
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        if (!this.e || this.d == z) {
            return;
        }
        this.d = z;
        refreshDrawableState();
        sendAccessibilityEvent(2048);
    }

    public void setPressable(boolean z) {
        this.f = z;
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        if (this.f) {
            super.setPressed(z);
        }
    }

    @Override // android.widget.Checkable
    public final void toggle() {
        setChecked(!this.d);
    }
}
