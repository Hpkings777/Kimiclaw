package com.google.android.material.button;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Parcelable;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import defpackage.AbstractC0545Jy0;
import defpackage.AbstractC0690Mt0;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC1975e21;
import defpackage.AbstractC2917km0;
import defpackage.AbstractC4123tO0;
import defpackage.AbstractC4635x31;
import defpackage.C1738cK0;
import defpackage.C1879dK0;
import defpackage.C2018eK0;
import defpackage.C2113f2;
import defpackage.C2156fJ0;
import defpackage.C2296gJ0;
import defpackage.C2664j;
import defpackage.C2986lF0;
import defpackage.C3099m41;
import defpackage.C3126mF0;
import defpackage.C3168ma0;
import defpackage.I9;
import defpackage.InterfaceC4945zF0;
import defpackage.NN;
import defpackage.T51;
import defpackage.U90;
import defpackage.V90;
import defpackage.W90;
import defpackage.X90;
import defpackage.Y90;
import defpackage.YI0;
import java.util.Iterator;
import java.util.LinkedHashSet;

/* JADX INFO: loaded from: classes.dex */
public class MaterialButton extends I9 implements Checkable, InterfaceC4945zF0 {
    public static final int[] N = {R.attr.state_checkable};
    public static final int[] O = {R.attr.state_checked};
    public static final U90 P = new U90();
    public float A;
    public int B;
    public int C;
    public LinearLayout.LayoutParams D;
    public boolean E;
    public int F;
    public boolean G;
    public int H;
    public C2018eK0 I;
    public int J;
    public float K;
    public float L;
    public C2156fJ0 M;
    public final Y90 d;
    public final LinkedHashSet e;
    public V90 f;
    public PorterDuff.Mode g;
    public ColorStateList h;
    public Drawable i;
    public String j;
    public int k;
    public int l;
    public int m;
    public int n;
    public boolean o;
    public boolean p;
    public int q;
    public int z;

    public MaterialButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.moonshot.kimiclaw.R.attr.materialButtonStyle);
    }

    public static /* synthetic */ void a(MaterialButton materialButton) {
        materialButton.F = materialButton.getOpticalCenterShift();
        materialButton.j();
        materialButton.invalidate();
    }

    private Layout.Alignment getActualTextAlignment() {
        int textAlignment = getTextAlignment();
        return textAlignment != 1 ? (textAlignment == 6 || textAlignment == 3) ? Layout.Alignment.ALIGN_OPPOSITE : textAlignment != 4 ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_CENTER : getGravityTextAlignment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getDisplayedWidthIncrease() {
        return this.K;
    }

    private Layout.Alignment getGravityTextAlignment() {
        int gravity = getGravity() & 8388615;
        return gravity != 1 ? (gravity == 5 || gravity == 8388613) ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_CENTER;
    }

    private int getOpticalCenterShift() {
        C3168ma0 c3168ma0A;
        if (this.E && this.G && (c3168ma0A = this.d.a(false)) != null) {
            return (int) (c3168ma0A.h() * 0.11f);
        }
        return 0;
    }

    private int getTextHeight() {
        if (getLineCount() > 1) {
            return getLayout().getHeight();
        }
        TextPaint paint = getPaint();
        String string = getText().toString();
        if (getTransformationMethod() != null) {
            string = getTransformationMethod().getTransformation(string, this).toString();
        }
        Rect rect = new Rect();
        paint.getTextBounds(string, 0, string.length(), rect);
        return Math.min(rect.height(), getLayout().getHeight());
    }

    private int getTextLayoutWidth() {
        int lineCount = getLineCount();
        float fMax = 0.0f;
        for (int i = 0; i < lineCount; i++) {
            fMax = Math.max(fMax, getLayout().getLineWidth(i));
        }
        return (int) Math.ceil(fMax);
    }

    private void setCheckedInternal(boolean z) {
        Y90 y90 = this.d;
        if (y90 == null || !y90.t || this.o == z) {
            return;
        }
        this.o = z;
        refreshDrawableState();
        if (getParent() instanceof MaterialButtonToggleGroup) {
            MaterialButtonToggleGroup materialButtonToggleGroup = (MaterialButtonToggleGroup) getParent();
            boolean z2 = this.o;
            if (!materialButtonToggleGroup.l) {
                materialButtonToggleGroup.f(getId(), z2);
            }
        }
        if (this.p) {
            return;
        }
        this.p = true;
        Iterator it = this.e.iterator();
        if (it.hasNext()) {
            throw YI0.f(it);
        }
        this.p = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayedWidthIncrease(float f) {
        MaterialButton materialButton;
        MaterialButton materialButton2;
        if (this.K != f) {
            this.K = f;
            j();
            invalidate();
            if (getParent() instanceof X90) {
                X90 x90 = (X90) getParent();
                int i = (int) this.K;
                int iIndexOfChild = x90.indexOfChild(this);
                if (iIndexOfChild < 0) {
                    return;
                }
                int i2 = iIndexOfChild - 1;
                while (true) {
                    materialButton = null;
                    if (i2 < 0) {
                        materialButton2 = null;
                        break;
                    } else {
                        if (x90.c(i2)) {
                            materialButton2 = (MaterialButton) x90.getChildAt(i2);
                            break;
                        }
                        i2--;
                    }
                }
                int childCount = x90.getChildCount();
                while (true) {
                    iIndexOfChild++;
                    if (iIndexOfChild >= childCount) {
                        break;
                    } else if (x90.c(iIndexOfChild)) {
                        materialButton = (MaterialButton) x90.getChildAt(iIndexOfChild);
                        break;
                    }
                }
                if (materialButton2 == null && materialButton == null) {
                    return;
                }
                if (materialButton2 == null) {
                    materialButton.setDisplayedWidthDecrease(i);
                }
                if (materialButton == null) {
                    materialButton2.setDisplayedWidthDecrease(i);
                }
                if (materialButton2 == null || materialButton == null) {
                    return;
                }
                materialButton2.setDisplayedWidthDecrease(i / 2);
                materialButton.setDisplayedWidthDecrease((i + 1) / 2);
            }
        }
    }

    public final C2296gJ0 d() {
        Context context = getContext();
        TypedValue typedValueW = AbstractC2917km0.W(context, com.moonshot.kimiclaw.R.attr.motionSpringFastSpatial);
        int[] iArr = AbstractC1264Xu0.s;
        TypedArray typedArrayObtainStyledAttributes = typedValueW == null ? context.obtainStyledAttributes(null, iArr, 0, com.moonshot.kimiclaw.R.style.Motion_Material3_Spring_Standard_Fast_Spatial) : context.obtainStyledAttributes(typedValueW.resourceId, iArr);
        C2296gJ0 c2296gJ0 = new C2296gJ0();
        try {
            float f = typedArrayObtainStyledAttributes.getFloat(1, Float.MIN_VALUE);
            if (f == Float.MIN_VALUE) {
                throw new IllegalArgumentException("A MaterialSpring style must have stiffness value.");
            }
            float f2 = typedArrayObtainStyledAttributes.getFloat(0, Float.MIN_VALUE);
            if (f2 == Float.MIN_VALUE) {
                throw new IllegalArgumentException("A MaterialSpring style must have a damping value.");
            }
            if (f <= 0.0f) {
                throw new IllegalArgumentException("Spring stiffness constant must be positive.");
            }
            c2296gJ0.a = Math.sqrt(f);
            c2296gJ0.c = false;
            if (f2 < 0.0f) {
                throw new IllegalArgumentException("Damping ratio must be non-negative");
            }
            c2296gJ0.b = f2;
            c2296gJ0.c = false;
            typedArrayObtainStyledAttributes.recycle();
            return c2296gJ0;
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes.recycle();
            throw th;
        }
    }

    public final boolean e() {
        Y90 y90 = this.d;
        return (y90 == null || y90.r) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void f(boolean z) {
        int i;
        if (this.I == null) {
            return;
        }
        if (this.M == null) {
            C2156fJ0 c2156fJ0 = new C2156fJ0(this, P);
            this.M = c2156fJ0;
            c2156fJ0.j = d();
        }
        if (!this.G) {
            return;
        }
        int i2 = this.J;
        C2018eK0 c2018eK0 = this.I;
        int[] drawableState = getDrawableState();
        int[][] iArr = c2018eK0.c;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            i = -1;
            if (i4 >= c2018eK0.a) {
                i4 = -1;
                break;
            } else if (StateSet.stateSetMatches(iArr[i4], drawableState)) {
                break;
            } else {
                i4++;
            }
        }
        if (i4 < 0) {
            int[] iArr2 = StateSet.WILD_CARD;
            int[][] iArr3 = c2018eK0.c;
            int i5 = 0;
            while (true) {
                if (i5 >= c2018eK0.a) {
                    break;
                }
                if (StateSet.stateSetMatches(iArr3[i5], iArr2)) {
                    i = i5;
                    break;
                }
                i5++;
            }
            i4 = i;
        }
        C1879dK0 c1879dK0 = (C1879dK0) (i4 < 0 ? c2018eK0.b : c2018eK0.d[i4]).a;
        int width = getWidth();
        float f = c1879dK0.b;
        int i6 = c1879dK0.a;
        if (i6 != 1) {
            if (i6 == 2) {
            }
            this.M.a(Math.min(i2, i3));
            if (z) {
                return;
            }
            this.M.d();
            return;
        }
        f *= width;
        i3 = (int) f;
        this.M.a(Math.min(i2, i3));
        if (z) {
        }
    }

    public final void g() {
        int i = this.q;
        boolean z = true;
        if (i != 1 && i != 2) {
            z = false;
        }
        if (z) {
            setCompoundDrawablesRelative(this.i, null, null, null);
            return;
        }
        if (i == 3 || i == 4) {
            setCompoundDrawablesRelative(null, null, this.i, null);
        } else if (i == 16 || i == 32) {
            setCompoundDrawablesRelative(null, this.i, null, null);
        }
    }

    @SuppressLint({"KotlinPropertyAccess"})
    public String getA11yClassName() {
        if (!TextUtils.isEmpty(this.j)) {
            return this.j;
        }
        Y90 y90 = this.d;
        return ((y90 == null || !y90.t) ? Button.class : CompoundButton.class).getName();
    }

    public int getAllowedWidthDecrease() {
        return this.H;
    }

    @Override // android.view.View
    public ColorStateList getBackgroundTintList() {
        return getSupportBackgroundTintList();
    }

    @Override // android.view.View
    public PorterDuff.Mode getBackgroundTintMode() {
        return getSupportBackgroundTintMode();
    }

    public int getCornerRadius() {
        if (e()) {
            return this.d.j;
        }
        return 0;
    }

    public C2296gJ0 getCornerSpringForce() {
        return this.d.d;
    }

    public Drawable getIcon() {
        return this.i;
    }

    public int getIconGravity() {
        return this.q;
    }

    public int getIconPadding() {
        return this.n;
    }

    public int getIconSize() {
        return this.k;
    }

    public ColorStateList getIconTint() {
        return this.h;
    }

    public PorterDuff.Mode getIconTintMode() {
        return this.g;
    }

    public int getInsetBottom() {
        return this.d.i;
    }

    public int getInsetTop() {
        return this.d.h;
    }

    public ColorStateList getRippleColor() {
        if (e()) {
            return this.d.o;
        }
        return null;
    }

    public C3126mF0 getShapeAppearanceModel() {
        if (e()) {
            return this.d.b;
        }
        throw new IllegalStateException("Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten background.");
    }

    public C1738cK0 getStateListShapeAppearanceModel() {
        if (e()) {
            return this.d.c;
        }
        throw new IllegalStateException("Attempted to get StateListShapeAppearanceModel from a MaterialButton which has an overwritten background.");
    }

    public ColorStateList getStrokeColor() {
        if (e()) {
            return this.d.n;
        }
        return null;
    }

    public int getStrokeWidth() {
        if (e()) {
            return this.d.k;
        }
        return 0;
    }

    @Override // defpackage.I9
    public ColorStateList getSupportBackgroundTintList() {
        return e() ? this.d.m : super.getSupportBackgroundTintList();
    }

    @Override // defpackage.I9
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return e() ? this.d.l : super.getSupportBackgroundTintMode();
    }

    public final void h(boolean z) {
        Drawable drawable = this.i;
        if (drawable != null) {
            Drawable drawableMutate = drawable.mutate();
            this.i = drawableMutate;
            drawableMutate.setTintList(this.h);
            PorterDuff.Mode mode = this.g;
            if (mode != null) {
                this.i.setTintMode(mode);
            }
            int intrinsicWidth = this.k;
            if (intrinsicWidth == 0) {
                intrinsicWidth = this.i.getIntrinsicWidth();
            }
            int intrinsicHeight = this.k;
            if (intrinsicHeight == 0) {
                intrinsicHeight = this.i.getIntrinsicHeight();
            }
            Drawable drawable2 = this.i;
            int i = this.l;
            int i2 = this.m;
            drawable2.setBounds(i, i2, intrinsicWidth + i, intrinsicHeight + i2);
            this.i.setVisible(true, z);
        }
        if (z) {
            g();
            return;
        }
        Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
        Drawable drawable3 = compoundDrawablesRelative[0];
        Drawable drawable4 = compoundDrawablesRelative[1];
        Drawable drawable5 = compoundDrawablesRelative[2];
        int i3 = this.q;
        if (((i3 == 1 || i3 == 2) && drawable3 != this.i) || (((i3 == 3 || i3 == 4) && drawable5 != this.i) || ((i3 == 16 || i3 == 32) && drawable4 != this.i))) {
            g();
        }
    }

    public final void i(int i, int i2) {
        if (this.i == null || getLayout() == null) {
            return;
        }
        int i3 = this.q;
        if (!(i3 == 1 || i3 == 2) && i3 != 3 && i3 != 4) {
            if (i3 == 16 || i3 == 32) {
                this.l = 0;
                if (i3 == 16) {
                    this.m = 0;
                    h(false);
                    return;
                }
                int intrinsicHeight = this.k;
                if (intrinsicHeight == 0) {
                    intrinsicHeight = this.i.getIntrinsicHeight();
                }
                int iMax = Math.max(0, (((((i2 - getTextHeight()) - getPaddingTop()) - intrinsicHeight) - this.n) - getPaddingBottom()) / 2);
                if (this.m != iMax) {
                    this.m = iMax;
                    h(false);
                    return;
                }
                return;
            }
            return;
        }
        this.m = 0;
        Layout.Alignment actualTextAlignment = getActualTextAlignment();
        int i4 = this.q;
        if (i4 == 1 || i4 == 3 || ((i4 == 2 && actualTextAlignment == Layout.Alignment.ALIGN_NORMAL) || (i4 == 4 && actualTextAlignment == Layout.Alignment.ALIGN_OPPOSITE))) {
            this.l = 0;
            h(false);
            return;
        }
        int intrinsicWidth = this.k;
        if (intrinsicWidth == 0) {
            intrinsicWidth = this.i.getIntrinsicWidth();
        }
        int textLayoutWidth = ((((i - getTextLayoutWidth()) - getPaddingEnd()) - intrinsicWidth) - this.n) - getPaddingStart();
        if (actualTextAlignment == Layout.Alignment.ALIGN_CENTER) {
            textLayoutWidth /= 2;
        }
        if ((getLayoutDirection() == 1) != (this.q == 4)) {
            textLayoutWidth = -textLayoutWidth;
        }
        if (this.l != textLayoutWidth) {
            this.l = textLayoutWidth;
            h(false);
        }
    }

    @Override // android.widget.Checkable
    public final boolean isChecked() {
        return this.o;
    }

    public final void j() {
        int i = (int) (this.K - this.L);
        int i2 = (i / 2) + this.F;
        getLayoutParams().width = (int) (this.A + i);
        setPaddingRelative(this.B + i2, getPaddingTop(), (this.C + i) - i2, getPaddingBottom());
    }

    @Override // android.widget.TextView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (e()) {
            AbstractC1975e21.K(this, this.d.a(false));
        }
    }

    @Override // android.widget.TextView, android.view.View
    public final int[] onCreateDrawableState(int i) {
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i + 2);
        Y90 y90 = this.d;
        if (y90 != null && y90.t) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, N);
        }
        if (this.o) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, O);
        }
        return iArrOnCreateDrawableState;
    }

    @Override // defpackage.I9, android.view.View
    public final void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(getA11yClassName());
        accessibilityEvent.setChecked(this.o);
    }

    @Override // defpackage.I9, android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(getA11yClassName());
        Y90 y90 = this.d;
        accessibilityNodeInfo.setCheckable(y90 != null && y90.t);
        accessibilityNodeInfo.setChecked(this.o);
        accessibilityNodeInfo.setClickable(isClickable());
    }

    @Override // defpackage.I9, android.widget.TextView, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        super.onLayout(z, i, i2, i3, i4);
        i(getMeasuredWidth(), getMeasuredHeight());
        int i6 = getResources().getConfiguration().orientation;
        if (this.z != i6) {
            this.z = i6;
            this.A = -1.0f;
        }
        if (this.A == -1.0f) {
            this.A = getMeasuredWidth();
            if (this.D == null && (getParent() instanceof X90) && ((X90) getParent()).getButtonSizeChange() != null) {
                this.D = (LinearLayout.LayoutParams) getLayoutParams();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.D);
                layoutParams.width = (int) this.A;
                setLayoutParams(layoutParams);
            }
        }
        boolean z2 = false;
        if (this.H == -1) {
            if (this.i == null) {
                i5 = 0;
            } else {
                int iconPadding = getIconPadding();
                int intrinsicWidth = this.k;
                if (intrinsicWidth == 0) {
                    intrinsicWidth = this.i.getIntrinsicWidth();
                }
                i5 = iconPadding + intrinsicWidth;
            }
            this.H = (getMeasuredWidth() - getTextLayoutWidth()) - i5;
        }
        if (this.B == -1) {
            this.B = getPaddingStart();
        }
        if (this.C == -1) {
            this.C = getPaddingEnd();
        }
        if ((getParent() instanceof X90) && ((X90) getParent()).getOrientation() == 0) {
            z2 = true;
        }
        this.G = z2;
    }

    @Override // android.widget.TextView, android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof W90)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        W90 w90 = (W90) parcelable;
        super.onRestoreInstanceState(w90.a);
        setChecked(w90.c);
    }

    @Override // android.widget.TextView, android.view.View
    public final Parcelable onSaveInstanceState() {
        W90 w90 = new W90(super.onSaveInstanceState());
        w90.c = this.o;
        return w90;
    }

    @Override // defpackage.I9, android.widget.TextView
    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        super.onTextChanged(charSequence, i, i2, i3);
        i(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override // android.view.View
    public final boolean performClick() {
        if (isEnabled() && this.d.u) {
            toggle();
        }
        return super.performClick();
    }

    @Override // android.view.View
    public final void refreshDrawableState() {
        super.refreshDrawableState();
        if (this.i != null) {
            if (this.i.setState(getDrawableState())) {
                invalidate();
            }
        }
    }

    public void setA11yClassName(String str) {
        this.j = str;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        if (!e()) {
            super.setBackgroundColor(i);
            return;
        }
        Y90 y90 = this.d;
        if (y90.a(false) != null) {
            y90.a(false).setTint(i);
        }
    }

    @Override // defpackage.I9, android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (!e()) {
            super.setBackgroundDrawable(drawable);
            return;
        }
        if (drawable == getBackground()) {
            getBackground().setState(drawable.getState());
            return;
        }
        Log.w("MaterialButton", "MaterialButton manages its own background to control elevation, shape, color and states. Consider using backgroundTint, shapeAppearance and other attributes where available. A custom background will ignore these attributes and you should consider handling interaction states such as pressed, focused and disabled");
        Y90 y90 = this.d;
        y90.r = true;
        ColorStateList colorStateList = y90.m;
        MaterialButton materialButton = y90.a;
        materialButton.setSupportBackgroundTintList(colorStateList);
        materialButton.setSupportBackgroundTintMode(y90.l);
        super.setBackgroundDrawable(drawable);
    }

    @Override // defpackage.I9, android.view.View
    public void setBackgroundResource(int i) {
        setBackgroundDrawable(i != 0 ? T51.f0(getContext(), i) : null);
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        setSupportBackgroundTintList(colorStateList);
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        setSupportBackgroundTintMode(mode);
    }

    public void setCheckable(boolean z) {
        if (e()) {
            this.d.t = z;
        }
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        setCheckedInternal(z);
    }

    public void setCornerRadius(int i) {
        if (e()) {
            Y90 y90 = this.d;
            if (y90.s && y90.j == i) {
                return;
            }
            y90.j = i;
            y90.s = true;
            float f = i;
            C2986lF0 c2986lF0F = y90.b.f();
            c2986lF0F.e = new C2664j(f);
            c2986lF0F.f = new C2664j(f);
            c2986lF0F.g = new C2664j(f);
            c2986lF0F.h = new C2664j(f);
            y90.b = c2986lF0F.a();
            y90.c = null;
            y90.d();
        }
    }

    public void setCornerRadiusResource(int i) {
        if (e()) {
            setCornerRadius(getResources().getDimensionPixelSize(i));
        }
    }

    public void setCornerSpringForce(C2296gJ0 c2296gJ0) {
        Y90 y90 = this.d;
        y90.d = c2296gJ0;
        if (y90.c != null) {
            y90.d();
        }
    }

    public void setDisplayedWidthDecrease(int i) {
        this.L = Math.min(i, this.H);
        j();
        invalidate();
    }

    @Override // android.view.View
    public void setElevation(float f) {
        super.setElevation(f);
        if (e()) {
            this.d.a(false).m(f);
        }
    }

    public void setIcon(Drawable drawable) {
        if (this.i != drawable) {
            this.i = drawable;
            h(true);
            i(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public void setIconGravity(int i) {
        if (this.q != i) {
            this.q = i;
            i(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public void setIconPadding(int i) {
        if (this.n != i) {
            this.n = i;
            setCompoundDrawablePadding(i);
        }
    }

    public void setIconResource(int i) {
        setIcon(i != 0 ? T51.f0(getContext(), i) : null);
    }

    public void setIconSize(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("iconSize cannot be less than 0");
        }
        if (this.k != i) {
            this.k = i;
            h(true);
        }
    }

    public void setIconTint(ColorStateList colorStateList) {
        if (this.h != colorStateList) {
            this.h = colorStateList;
            h(false);
        }
    }

    public void setIconTintMode(PorterDuff.Mode mode) {
        if (this.g != mode) {
            this.g = mode;
            h(false);
        }
    }

    public void setIconTintResource(int i) {
        setIconTint(AbstractC4635x31.L(getContext(), i));
    }

    public void setInsetBottom(int i) {
        Y90 y90 = this.d;
        y90.b(y90.h, i);
    }

    public void setInsetTop(int i) {
        Y90 y90 = this.d;
        y90.b(i, y90.i);
    }

    public void setInternalBackground(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    public void setOnPressedChangeListenerInternal(V90 v90) {
        this.f = v90;
    }

    public void setOpticalCenterEnabled(boolean z) {
        if (this.E != z) {
            this.E = z;
            Y90 y90 = this.d;
            if (z) {
                C2113f2 c2113f2 = new C2113f2(this, 19);
                y90.e = c2113f2;
                C3168ma0 c3168ma0A = y90.a(false);
                if (c3168ma0A != null) {
                    c3168ma0A.L = c2113f2;
                }
            } else {
                y90.e = null;
                C3168ma0 c3168ma0A2 = y90.a(false);
                if (c3168ma0A2 != null) {
                    c3168ma0A2.L = null;
                }
            }
            post(new NN(this, 2));
        }
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        V90 v90 = this.f;
        if (v90 != null) {
            ((MaterialButtonToggleGroup) ((C3099m41) v90).b).invalidate();
        }
        super.setPressed(z);
        f(false);
    }

    public void setRippleColor(ColorStateList colorStateList) {
        if (e()) {
            Y90 y90 = this.d;
            if (y90.o != colorStateList) {
                y90.o = colorStateList;
                MaterialButton materialButton = y90.a;
                if (materialButton.getBackground() instanceof RippleDrawable) {
                    ((RippleDrawable) materialButton.getBackground()).setColor(AbstractC0545Jy0.a(colorStateList));
                }
            }
        }
    }

    public void setRippleColorResource(int i) {
        if (e()) {
            setRippleColor(AbstractC4635x31.L(getContext(), i));
        }
    }

    @Override // defpackage.InterfaceC4945zF0
    public void setShapeAppearanceModel(C3126mF0 c3126mF0) {
        if (!e()) {
            throw new IllegalStateException("Attempted to set ShapeAppearanceModel on a MaterialButton which has an overwritten background.");
        }
        Y90 y90 = this.d;
        y90.b = c3126mF0;
        y90.c = null;
        y90.d();
    }

    public void setShouldDrawSurfaceColorStroke(boolean z) {
        if (e()) {
            Y90 y90 = this.d;
            y90.q = z;
            y90.e();
        }
    }

    public void setSizeChange(C2018eK0 c2018eK0) {
        if (this.I != c2018eK0) {
            this.I = c2018eK0;
            f(true);
        }
    }

    public void setStateListShapeAppearanceModel(C1738cK0 c1738cK0) {
        if (!e()) {
            throw new IllegalStateException("Attempted to set StateListShapeAppearanceModel on a MaterialButton which has an overwritten background.");
        }
        Y90 y90 = this.d;
        if (y90.d == null && c1738cK0.d()) {
            y90.d = d();
            if (y90.c != null) {
                y90.d();
            }
        }
        y90.c = c1738cK0;
        y90.d();
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        if (e()) {
            Y90 y90 = this.d;
            if (y90.n != colorStateList) {
                y90.n = colorStateList;
                y90.e();
            }
        }
    }

    public void setStrokeColorResource(int i) {
        if (e()) {
            setStrokeColor(AbstractC4635x31.L(getContext(), i));
        }
    }

    public void setStrokeWidth(int i) {
        if (e()) {
            Y90 y90 = this.d;
            if (y90.k != i) {
                y90.k = i;
                y90.e();
            }
        }
    }

    public void setStrokeWidthResource(int i) {
        if (e()) {
            setStrokeWidth(getResources().getDimensionPixelSize(i));
        }
    }

    @Override // defpackage.I9
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        if (!e()) {
            super.setSupportBackgroundTintList(colorStateList);
            return;
        }
        Y90 y90 = this.d;
        if (y90.m != colorStateList) {
            y90.m = colorStateList;
            if (y90.a(false) != null) {
                y90.a(false).setTintList(y90.m);
            }
        }
    }

    @Override // defpackage.I9
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        if (!e()) {
            super.setSupportBackgroundTintMode(mode);
            return;
        }
        Y90 y90 = this.d;
        if (y90.l != mode) {
            y90.l = mode;
            if (y90.a(false) == null || y90.l == null) {
                return;
            }
            y90.a(false).setTintMode(y90.l);
        }
    }

    @Override // android.view.View
    public void setTextAlignment(int i) {
        super.setTextAlignment(i);
        i(getMeasuredWidth(), getMeasuredHeight());
    }

    public void setToggleCheckedStateOnClick(boolean z) {
        this.d.u = z;
    }

    @Override // android.widget.TextView
    public void setWidth(int i) {
        this.A = -1.0f;
        super.setWidth(i);
    }

    public void setWidthChangeMax(int i) {
        if (this.J != i) {
            this.J = i;
            f(true);
        }
    }

    @Override // android.widget.Checkable
    public final void toggle() {
        setChecked(!this.o);
    }

    public MaterialButton(Context context, AttributeSet attributeSet, int i) {
        super(AbstractC4123tO0.g0(context, attributeSet, i, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Button, new int[]{com.moonshot.kimiclaw.R.attr.materialSizeOverlay}), attributeSet, i);
        this.e = new LinkedHashSet();
        this.o = false;
        this.p = false;
        this.z = -1;
        this.A = -1.0f;
        this.B = -1;
        this.C = -1;
        this.H = -1;
        Context context2 = getContext();
        TypedArray typedArrayB0 = AbstractC4635x31.b0(context2, attributeSet, AbstractC1264Xu0.j, i, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Button, new int[0]);
        this.n = typedArrayB0.getDimensionPixelSize(13, 0);
        int i2 = typedArrayB0.getInt(16, -1);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
        this.g = AbstractC0690Mt0.L(i2, mode);
        this.h = AbstractC4123tO0.J(getContext(), typedArrayB0, 15);
        this.i = AbstractC4123tO0.K(getContext(), typedArrayB0, 11);
        this.q = typedArrayB0.getInteger(12, 1);
        this.k = typedArrayB0.getDimensionPixelSize(14, 0);
        C1738cK0 c1738cK0B = C1738cK0.b(context2, typedArrayB0, 19);
        C3126mF0 c3126mF0C = c1738cK0B != null ? c1738cK0B.c() : C3126mF0.b(context2, attributeSet, i, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Button).a();
        boolean z = typedArrayB0.getBoolean(17, false);
        Y90 y90 = new Y90(this, c3126mF0C);
        this.d = y90;
        y90.f = typedArrayB0.getDimensionPixelOffset(2, 0);
        y90.g = typedArrayB0.getDimensionPixelOffset(3, 0);
        y90.h = typedArrayB0.getDimensionPixelOffset(4, 0);
        y90.i = typedArrayB0.getDimensionPixelOffset(5, 0);
        if (typedArrayB0.hasValue(9)) {
            int dimensionPixelSize = typedArrayB0.getDimensionPixelSize(9, -1);
            y90.j = dimensionPixelSize;
            float f = dimensionPixelSize;
            C2986lF0 c2986lF0F = y90.b.f();
            c2986lF0F.e = new C2664j(f);
            c2986lF0F.f = new C2664j(f);
            c2986lF0F.g = new C2664j(f);
            c2986lF0F.h = new C2664j(f);
            y90.b = c2986lF0F.a();
            y90.c = null;
            y90.d();
            y90.s = true;
        }
        y90.k = typedArrayB0.getDimensionPixelSize(22, 0);
        y90.l = AbstractC0690Mt0.L(typedArrayB0.getInt(8, -1), mode);
        y90.m = AbstractC4123tO0.J(getContext(), typedArrayB0, 7);
        y90.n = AbstractC4123tO0.J(getContext(), typedArrayB0, 21);
        y90.o = AbstractC4123tO0.J(getContext(), typedArrayB0, 18);
        y90.t = typedArrayB0.getBoolean(6, false);
        y90.w = typedArrayB0.getDimensionPixelSize(10, 0);
        y90.u = typedArrayB0.getBoolean(23, true);
        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
        int paddingEnd = getPaddingEnd();
        int paddingBottom = getPaddingBottom();
        if (typedArrayB0.hasValue(0)) {
            y90.r = true;
            setSupportBackgroundTintList(y90.m);
            setSupportBackgroundTintMode(y90.l);
        } else {
            y90.c();
        }
        setPaddingRelative(paddingStart + y90.f, paddingTop + y90.h, paddingEnd + y90.g, paddingBottom + y90.i);
        setCheckedInternal(typedArrayB0.getBoolean(1, false));
        if (c1738cK0B != null) {
            y90.d = d();
            if (y90.c != null) {
                y90.d();
            }
            y90.c = c1738cK0B;
            y90.d();
        }
        setOpticalCenterEnabled(z);
        typedArrayB0.recycle();
        setCompoundDrawablePadding(this.n);
        h(this.i != null);
    }
}
