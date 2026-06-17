package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import com.google.common.primitives.Ints;
import com.moonshot.kimiclaw.R;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC1368Zu0;
import defpackage.AbstractC4123tO0;
import defpackage.AbstractC4421vX0;
import defpackage.AbstractC4635x31;
import defpackage.C3488os;
import defpackage.C3628ps;
import defpackage.C4187ts;
import defpackage.InterfaceC0314Fn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class ClockFaceView extends AbstractC1368Zu0 implements InterfaceC0314Fn {
    public final ClockHandView D;
    public final Rect E;
    public final RectF F;
    public final Rect G;
    public final SparseArray H;
    public final c I;
    public final int[] J;
    public final float[] K;
    public final int L;
    public final int M;
    public final int N;
    public final int O;
    public final String[] P;
    public float Q;
    public final ColorStateList R;

    public ClockFaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.E = new Rect();
        this.F = new RectF();
        this.G = new Rect();
        SparseArray sparseArray = new SparseArray();
        this.H = sparseArray;
        this.K = new float[]{0.0f, 0.9f, 1.0f};
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, AbstractC1264Xu0.d, R.attr.materialClockStyle, R.style.Widget_MaterialComponents_TimePicker_Clock);
        Resources resources = getResources();
        ColorStateList colorStateListJ = AbstractC4123tO0.J(context, typedArrayObtainStyledAttributes, 1);
        this.R = colorStateListJ;
        LayoutInflater.from(context).inflate(R.layout.material_clockface_view, (ViewGroup) this, true);
        ClockHandView clockHandView = (ClockHandView) findViewById(R.id.material_clock_hand);
        this.D = clockHandView;
        this.L = resources.getDimensionPixelSize(R.dimen.material_clock_hand_padding);
        int colorForState = colorStateListJ.getColorForState(new int[]{android.R.attr.state_selected}, colorStateListJ.getDefaultColor());
        this.J = new int[]{colorForState, colorForState, colorStateListJ.getDefaultColor()};
        clockHandView.c.add(this);
        int defaultColor = AbstractC4635x31.L(context, R.color.material_timepicker_clockface).getDefaultColor();
        ColorStateList colorStateListJ2 = AbstractC4123tO0.J(context, typedArrayObtainStyledAttributes, 0);
        setBackgroundColor(colorStateListJ2 != null ? colorStateListJ2.getDefaultColor() : defaultColor);
        getViewTreeObserver().addOnPreDrawListener(new b(this));
        setFocusable(false);
        typedArrayObtainStyledAttributes.recycle();
        this.I = new c(this);
        String[] strArr = new String[12];
        Arrays.fill(strArr, "");
        this.P = strArr;
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(getContext());
        int size = sparseArray.size();
        boolean z = false;
        for (int i = 0; i < Math.max(this.P.length, size); i++) {
            TextView textView = (TextView) sparseArray.get(i);
            if (i >= this.P.length) {
                removeView(textView);
                sparseArray.remove(i);
            } else {
                if (textView == null) {
                    textView = (TextView) layoutInflaterFrom.inflate(R.layout.material_clockface_textview, (ViewGroup) this, false);
                    sparseArray.put(i, textView);
                    addView(textView);
                }
                textView.setText(this.P[i]);
                textView.setTag(R.id.material_value_index, Integer.valueOf(i));
                int i2 = (i / 12) + 1;
                textView.setTag(R.id.material_clock_level, Integer.valueOf(i2));
                z = i2 > 1 ? true : z;
                AbstractC4421vX0.q(textView, this.I);
                textView.setTextColor(this.R);
            }
        }
        ClockHandView clockHandView2 = this.D;
        if (clockHandView2.b && !z) {
            clockHandView2.m = 1;
        }
        clockHandView2.b = z;
        clockHandView2.invalidate();
        this.M = resources.getDimensionPixelSize(R.dimen.material_time_picker_minimum_screen_height);
        this.N = resources.getDimensionPixelSize(R.dimen.material_time_picker_minimum_screen_width);
        this.O = resources.getDimensionPixelSize(R.dimen.material_clock_size);
    }

    @Override // defpackage.AbstractC1368Zu0
    public final void m() {
        C4187ts c4187ts = new C4187ts();
        c4187ts.b(this);
        HashMap map = new HashMap();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getId() != R.id.circle_center && !"skip".equals(childAt.getTag())) {
                int i2 = (Integer) childAt.getTag(R.id.material_clock_level);
                if (i2 == null) {
                    i2 = 1;
                }
                if (!map.containsKey(i2)) {
                    map.put(i2, new ArrayList());
                }
                ((List) map.get(i2)).add(childAt);
            }
        }
        for (Map.Entry entry : map.entrySet()) {
            List list = (List) entry.getValue();
            int iRound = ((Integer) entry.getKey()).intValue() == 2 ? Math.round(this.B * 0.66f) : this.B;
            Iterator it = list.iterator();
            float size = 0.0f;
            while (it.hasNext()) {
                int id = ((View) it.next()).getId();
                HashMap map2 = c4187ts.c;
                if (!map2.containsKey(Integer.valueOf(id))) {
                    map2.put(Integer.valueOf(id), new C3488os());
                }
                C3628ps c3628ps = ((C3488os) map2.get(Integer.valueOf(id))).d;
                c3628ps.y = R.id.circle_center;
                c3628ps.z = iRound;
                c3628ps.A = size;
                size += 360.0f / list.size();
            }
        }
        c4187ts.a(this);
        setConstraintSet(null);
        requestLayout();
        int i3 = 0;
        while (true) {
            SparseArray sparseArray = this.H;
            if (i3 >= sparseArray.size()) {
                return;
            }
            ((TextView) sparseArray.get(i3)).setVisibility(0);
            i3++;
        }
    }

    public final void n() {
        SparseArray sparseArray;
        RectF rectF;
        Rect rect;
        RectF rectF2 = this.D.g;
        float f = Float.MAX_VALUE;
        TextView textView = null;
        int i = 0;
        while (true) {
            sparseArray = this.H;
            int size = sparseArray.size();
            rectF = this.F;
            rect = this.E;
            if (i >= size) {
                break;
            }
            TextView textView2 = (TextView) sparseArray.get(i);
            if (textView2 != null) {
                textView2.getHitRect(rect);
                rectF.set(rect);
                rectF.union(rectF2);
                float fHeight = rectF.height() * rectF.width();
                if (fHeight < f) {
                    textView = textView2;
                    f = fHeight;
                }
            }
            i++;
        }
        for (int i2 = 0; i2 < sparseArray.size(); i2++) {
            TextView textView3 = (TextView) sparseArray.get(i2);
            if (textView3 != null) {
                textView3.setSelected(textView3 == textView);
                textView3.getHitRect(rect);
                rectF.set(rect);
                textView3.getLineBounds(0, this.G);
                rectF.inset(r8.left, r8.top);
                textView3.getPaint().setShader(!RectF.intersects(rectF2, rectF) ? null : new RadialGradient(rectF2.centerX() - rectF.left, rectF2.centerY() - rectF.top, 0.5f * rectF2.width(), this.J, this.K, Shader.TileMode.CLAMP));
                textView3.invalidate();
            }
        }
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setCollectionInfo(AccessibilityNodeInfo.CollectionInfo.obtain(1, this.P.length, false, 1));
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        n();
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int iMax = (int) (this.O / Math.max(Math.max(this.M / displayMetrics.heightPixels, this.N / displayMetrics.widthPixels), 1.0f));
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(iMax, Ints.MAX_POWER_OF_TWO);
        setMeasuredDimension(iMax, iMax);
        super.onMeasure(iMakeMeasureSpec, iMakeMeasureSpec);
    }
}
