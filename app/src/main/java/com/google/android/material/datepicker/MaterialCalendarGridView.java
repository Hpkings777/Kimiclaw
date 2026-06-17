package com.google.android.material.datepicker;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import defpackage.AbstractC4421vX0;
import defpackage.C2610ia0;
import defpackage.C2897kd0;
import defpackage.IV0;
import defpackage.XD;
import kotlin.jvm.internal.IntCompanionObject;

/* JADX INFO: loaded from: classes.dex */
final class MaterialCalendarGridView extends GridView {
    public final boolean a;

    public MaterialCalendarGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        IV0.c(null);
        if (C2610ia0.K(getContext(), R.attr.windowFullscreen)) {
            setNextFocusLeftId(com.moonshot.kimiclaw.R.id.cancel_button);
            setNextFocusRightId(com.moonshot.kimiclaw.R.id.confirm_button);
        }
        this.a = C2610ia0.K(getContext(), com.moonshot.kimiclaw.R.attr.nestedScrollable);
        AbstractC4421vX0.q(this, new XD(3));
    }

    public final C2897kd0 a() {
        return (C2897kd0) super.getAdapter();
    }

    @Override // android.widget.GridView, android.widget.AdapterView
    public final Adapter getAdapter() {
        return (C2897kd0) super.getAdapter();
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((C2897kd0) super.getAdapter()).notifyDataSetChanged();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        C2897kd0 c2897kd0 = (C2897kd0) super.getAdapter();
        c2897kd0.getClass();
        int iMax = Math.max(c2897kd0.a(), getFirstVisiblePosition());
        int iMin = Math.min(c2897kd0.c(), getLastVisiblePosition());
        c2897kd0.getItem(iMax);
        c2897kd0.getItem(iMin);
        throw null;
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    public final void onFocusChanged(boolean z, int i, Rect rect) {
        if (!z) {
            super.onFocusChanged(false, i, rect);
            return;
        }
        if (i == 33) {
            setSelection(((C2897kd0) super.getAdapter()).c());
        } else if (i == 130) {
            setSelection(((C2897kd0) super.getAdapter()).a());
        } else {
            super.onFocusChanged(true, i, rect);
        }
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (!super.onKeyDown(i, keyEvent)) {
            return false;
        }
        int selectedItemPosition = getSelectedItemPosition();
        if (selectedItemPosition == -1 || (selectedItemPosition >= ((C2897kd0) super.getAdapter()).a() && selectedItemPosition <= ((C2897kd0) super.getAdapter()).c())) {
            return true;
        }
        if (19 != i) {
            return false;
        }
        setSelection(((C2897kd0) super.getAdapter()).a());
        return true;
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    public final void onMeasure(int i, int i2) {
        if (!this.a) {
            super.onMeasure(i, i2);
            return;
        }
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(16777215, IntCompanionObject.MIN_VALUE));
        getLayoutParams().height = getMeasuredHeight();
    }

    @Override // android.widget.GridView, android.widget.AdapterView
    public final void setSelection(int i) {
        if (i < ((C2897kd0) super.getAdapter()).a()) {
            super.setSelection(((C2897kd0) super.getAdapter()).a());
        } else {
            super.setSelection(i);
        }
    }

    @Override // android.widget.GridView, android.widget.AdapterView
    public final ListAdapter getAdapter() {
        return (C2897kd0) super.getAdapter();
    }

    @Override // android.widget.AdapterView
    public final void setAdapter(ListAdapter listAdapter) {
        if (!(listAdapter instanceof C2897kd0)) {
            throw new IllegalArgumentException(String.format("%1$s must have its Adapter set to a %2$s", MaterialCalendarGridView.class.getCanonicalName(), C2897kd0.class.getCanonicalName()));
        }
        super.setAdapter(listAdapter);
    }
}
