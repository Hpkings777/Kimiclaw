package com.google.android.material.divider;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import com.moonshot.kimiclaw.R;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC4123tO0;
import defpackage.AbstractC4635x31;
import defpackage.C3168ma0;

/* JADX INFO: loaded from: classes.dex */
public class MaterialDivider extends View {
    public final C3168ma0 a;
    public int b;
    public int c;
    public int d;
    public int e;

    public MaterialDivider(Context context, AttributeSet attributeSet) {
        super(AbstractC4123tO0.f0(context, attributeSet, R.attr.materialDividerStyle, R.style.Widget_MaterialComponents_MaterialDivider), attributeSet, R.attr.materialDividerStyle);
        Context context2 = getContext();
        this.a = new C3168ma0();
        TypedArray typedArrayB0 = AbstractC4635x31.b0(context2, attributeSet, AbstractC1264Xu0.p, R.attr.materialDividerStyle, R.style.Widget_MaterialComponents_MaterialDivider, new int[0]);
        this.b = typedArrayB0.getDimensionPixelSize(3, getResources().getDimensionPixelSize(R.dimen.material_divider_thickness));
        this.d = typedArrayB0.getDimensionPixelOffset(2, 0);
        this.e = typedArrayB0.getDimensionPixelOffset(1, 0);
        setDividerColor(AbstractC4123tO0.J(context2, typedArrayB0, 0).getDefaultColor());
        typedArrayB0.recycle();
    }

    public int getDividerColor() {
        return this.c;
    }

    public int getDividerInsetEnd() {
        return this.e;
    }

    public int getDividerInsetStart() {
        return this.d;
    }

    public int getDividerThickness() {
        return this.b;
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        int width;
        int i;
        super.onDraw(canvas);
        boolean z = getLayoutDirection() == 1;
        int i2 = z ? this.e : this.d;
        if (z) {
            width = getWidth();
            i = this.d;
        } else {
            width = getWidth();
            i = this.e;
        }
        int i3 = width - i;
        C3168ma0 c3168ma0 = this.a;
        c3168ma0.setBounds(i2, 0, i3, getBottom() - getTop());
        c3168ma0.draw(canvas);
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int mode = View.MeasureSpec.getMode(i2);
        int measuredHeight = getMeasuredHeight();
        if (mode == Integer.MIN_VALUE || mode == 0) {
            int i3 = this.b;
            if (i3 > 0 && measuredHeight != i3) {
                measuredHeight = i3;
            }
            setMeasuredDimension(getMeasuredWidth(), measuredHeight);
        }
    }

    public void setDividerColor(int i) {
        if (this.c != i) {
            this.c = i;
            this.a.n(ColorStateList.valueOf(i));
            invalidate();
        }
    }

    public void setDividerColorResource(int i) {
        setDividerColor(getContext().getColor(i));
    }

    public void setDividerInsetEnd(int i) {
        this.e = i;
    }

    public void setDividerInsetEndResource(int i) {
        setDividerInsetEnd(getContext().getResources().getDimensionPixelOffset(i));
    }

    public void setDividerInsetStart(int i) {
        this.d = i;
    }

    public void setDividerInsetStartResource(int i) {
        setDividerInsetStart(getContext().getResources().getDimensionPixelOffset(i));
    }

    public void setDividerThickness(int i) {
        if (this.b != i) {
            this.b = i;
            requestLayout();
        }
    }

    public void setDividerThicknessResource(int i) {
        setDividerThickness(getContext().getResources().getDimensionPixelSize(i));
    }
}
