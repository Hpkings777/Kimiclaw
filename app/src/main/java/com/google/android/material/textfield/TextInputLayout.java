package com.google.android.material.textfield;

import android.R;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.text.Editable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.internal.CheckableImageButton;
import defpackage.AP0;
import defpackage.AbstractC0923Rg;
import defpackage.AbstractC1183Wg;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC1975e21;
import defpackage.AbstractC2917km0;
import defpackage.AbstractC3154mT0;
import defpackage.AbstractC3564pO0;
import defpackage.AbstractC4123tO0;
import defpackage.AbstractC4421vX0;
import defpackage.AbstractC4635x31;
import defpackage.AbstractC4671xI0;
import defpackage.AbstractC5018zo;
import defpackage.BE;
import defpackage.C0070Av;
import defpackage.C0663Mg;
import defpackage.C0715Ng;
import defpackage.C0755Oa;
import defpackage.C1041Tn;
import defpackage.C1064Ty0;
import defpackage.C1479ag;
import defpackage.C2113f2;
import defpackage.C2329ga;
import defpackage.C2494hk;
import defpackage.C2664j;
import defpackage.C2705jF;
import defpackage.C2856kK0;
import defpackage.C2888ka0;
import defpackage.C2986lF0;
import defpackage.C3126mF0;
import defpackage.C3168ma0;
import defpackage.C3198mn;
import defpackage.C3273nJ;
import defpackage.C3781qy0;
import defpackage.C4407vQ0;
import defpackage.C4547wQ0;
import defpackage.C4827yQ0;
import defpackage.C5032zv;
import defpackage.CJ0;
import defpackage.F2;
import defpackage.InterfaceC0484Iu;
import defpackage.InterfaceC4687xQ0;
import defpackage.NG;
import defpackage.NN;
import defpackage.NW;
import defpackage.OG;
import defpackage.OW;
import defpackage.P21;
import defpackage.T51;
import defpackage.T90;
import defpackage.UD;
import defpackage.VA;
import defpackage.W8;
import defpackage.XD;
import defpackage.YO0;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class TextInputLayout extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    public static final int[][] L0 = {new int[]{R.attr.state_pressed}, new int[0]};
    public CharSequence A;
    public int A0;
    public boolean B;
    public int B0;
    public C0755Oa C;
    public int C0;
    public ColorStateList D;
    public boolean D0;
    public int E;
    public final C1041Tn E0;
    public C3273nJ F;
    public boolean F0;
    public C3273nJ G;
    public boolean G0;
    public ColorStateList H;
    public ValueAnimator H0;
    public ColorStateList I;
    public boolean I0;
    public ColorStateList J;
    public boolean J0;
    public ColorStateList K;
    public boolean K0;
    public boolean L;
    public CharSequence M;
    public boolean N;
    public C3168ma0 O;
    public C3168ma0 P;
    public StateListDrawable Q;
    public boolean R;
    public C3168ma0 S;
    public C3168ma0 T;
    public C3126mF0 U;
    public boolean V;
    public final int W;
    public final FrameLayout a;
    public int a0;
    public final CJ0 b;
    public int b0;
    public final OG c;
    public int c0;
    public final int d;
    public int d0;
    public EditText e;
    public int e0;
    public CharSequence f;
    public int f0;
    public int g;
    public int g0;
    public int h;
    public final Rect h0;
    public int i;
    public final Rect i0;
    public int j;
    public final RectF j0;
    public final OW k;
    public Typeface k0;
    public boolean l;
    public ColorDrawable l0;
    public int m;
    public int m0;
    public boolean n;
    public final LinkedHashSet n0;
    public InterfaceC4687xQ0 o;
    public ColorDrawable o0;
    public C0755Oa p;
    public int p0;
    public int q;
    public Drawable q0;
    public ColorStateList r0;
    public ColorStateList s0;
    public int t0;
    public int u0;
    public int v0;
    public ColorStateList w0;
    public int x0;
    public int y0;
    public int z;
    public int z0;

    public TextInputLayout(Context context, AttributeSet attributeSet) {
        super(AbstractC4123tO0.f0(context, attributeSet, com.moonshot.kimiclaw.R.attr.textInputStyle, com.moonshot.kimiclaw.R.style.Widget_Design_TextInputLayout), attributeSet, com.moonshot.kimiclaw.R.attr.textInputStyle);
        this.g = -1;
        this.h = -1;
        this.i = -1;
        this.j = -1;
        this.k = new OW(this);
        this.o = new C3781qy0(4);
        this.h0 = new Rect();
        this.i0 = new Rect();
        this.j0 = new RectF();
        this.n0 = new LinkedHashSet();
        C1041Tn c1041Tn = new C1041Tn(this);
        this.E0 = c1041Tn;
        this.K0 = false;
        Context context2 = getContext();
        setOrientation(1);
        setWillNotDraw(false);
        setAddStatesFromChildren(true);
        FrameLayout frameLayout = new FrameLayout(context2);
        this.a = frameLayout;
        frameLayout.setAddStatesFromChildren(true);
        LinearInterpolator linearInterpolator = W8.a;
        c1041Tn.R = linearInterpolator;
        c1041Tn.j(false);
        c1041Tn.Q = linearInterpolator;
        c1041Tn.j(false);
        if (c1041Tn.g != 8388659) {
            c1041Tn.g = 8388659;
            c1041Tn.j(false);
        }
        int[] iArr = AbstractC1264Xu0.D;
        AbstractC4635x31.y(context2, attributeSet, com.moonshot.kimiclaw.R.attr.textInputStyle, com.moonshot.kimiclaw.R.style.Widget_Design_TextInputLayout);
        AbstractC4635x31.B(context2, attributeSet, iArr, com.moonshot.kimiclaw.R.attr.textInputStyle, com.moonshot.kimiclaw.R.style.Widget_Design_TextInputLayout, 22, 20, 40, 45, 50);
        TypedArray typedArrayObtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, iArr, com.moonshot.kimiclaw.R.attr.textInputStyle, com.moonshot.kimiclaw.R.style.Widget_Design_TextInputLayout);
        C3198mn c3198mn = new C3198mn(context2, typedArrayObtainStyledAttributes);
        CJ0 cj0 = new CJ0(this, c3198mn);
        this.b = cj0;
        this.L = typedArrayObtainStyledAttributes.getBoolean(48, true);
        setHint(typedArrayObtainStyledAttributes.getText(4));
        this.G0 = typedArrayObtainStyledAttributes.getBoolean(47, true);
        this.F0 = typedArrayObtainStyledAttributes.getBoolean(42, true);
        if (typedArrayObtainStyledAttributes.hasValue(6)) {
            setMinEms(typedArrayObtainStyledAttributes.getInt(6, -1));
        } else if (typedArrayObtainStyledAttributes.hasValue(3)) {
            setMinWidth(typedArrayObtainStyledAttributes.getDimensionPixelSize(3, -1));
        }
        if (typedArrayObtainStyledAttributes.hasValue(5)) {
            setMaxEms(typedArrayObtainStyledAttributes.getInt(5, -1));
        } else if (typedArrayObtainStyledAttributes.hasValue(2)) {
            setMaxWidth(typedArrayObtainStyledAttributes.getDimensionPixelSize(2, -1));
        }
        this.U = C3126mF0.b(context2, attributeSet, com.moonshot.kimiclaw.R.attr.textInputStyle, com.moonshot.kimiclaw.R.style.Widget_Design_TextInputLayout).a();
        this.W = context2.getResources().getDimensionPixelOffset(com.moonshot.kimiclaw.R.dimen.mtrl_textinput_box_label_cutout_padding);
        this.b0 = typedArrayObtainStyledAttributes.getDimensionPixelOffset(9, 0);
        this.d = getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.m3_multiline_hint_filled_text_extra_space);
        this.d0 = typedArrayObtainStyledAttributes.getDimensionPixelSize(16, context2.getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.mtrl_textinput_box_stroke_width_default));
        this.e0 = typedArrayObtainStyledAttributes.getDimensionPixelSize(17, context2.getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.mtrl_textinput_box_stroke_width_focused));
        this.c0 = this.d0;
        float dimension = typedArrayObtainStyledAttributes.getDimension(13, -1.0f);
        float dimension2 = typedArrayObtainStyledAttributes.getDimension(12, -1.0f);
        float dimension3 = typedArrayObtainStyledAttributes.getDimension(10, -1.0f);
        float dimension4 = typedArrayObtainStyledAttributes.getDimension(11, -1.0f);
        C2986lF0 c2986lF0F = this.U.f();
        if (dimension >= 0.0f) {
            c2986lF0F.e = new C2664j(dimension);
        }
        if (dimension2 >= 0.0f) {
            c2986lF0F.f = new C2664j(dimension2);
        }
        if (dimension3 >= 0.0f) {
            c2986lF0F.g = new C2664j(dimension3);
        }
        if (dimension4 >= 0.0f) {
            c2986lF0F.h = new C2664j(dimension4);
        }
        this.U = c2986lF0F.a();
        ColorStateList colorStateListI = AbstractC4123tO0.I(context2, c3198mn, 7);
        if (colorStateListI != null) {
            int defaultColor = colorStateListI.getDefaultColor();
            this.x0 = defaultColor;
            this.g0 = defaultColor;
            if (colorStateListI.isStateful()) {
                this.y0 = colorStateListI.getColorForState(new int[]{-16842910}, -1);
                this.z0 = colorStateListI.getColorForState(new int[]{R.attr.state_focused, R.attr.state_enabled}, -1);
                this.A0 = colorStateListI.getColorForState(new int[]{R.attr.state_hovered, R.attr.state_enabled}, -1);
            } else {
                this.z0 = this.x0;
                ColorStateList colorStateListL = AbstractC4635x31.L(context2, com.moonshot.kimiclaw.R.color.mtrl_filled_background_color);
                this.y0 = colorStateListL.getColorForState(new int[]{-16842910}, -1);
                this.A0 = colorStateListL.getColorForState(new int[]{R.attr.state_hovered}, -1);
            }
        } else {
            this.g0 = 0;
            this.x0 = 0;
            this.y0 = 0;
            this.z0 = 0;
            this.A0 = 0;
        }
        if (typedArrayObtainStyledAttributes.hasValue(1)) {
            ColorStateList colorStateListZ = c3198mn.z(1);
            this.s0 = colorStateListZ;
            this.r0 = colorStateListZ;
        }
        ColorStateList colorStateListI2 = AbstractC4123tO0.I(context2, c3198mn, 14);
        this.v0 = typedArrayObtainStyledAttributes.getColor(14, 0);
        this.t0 = context2.getColor(com.moonshot.kimiclaw.R.color.mtrl_textinput_default_box_stroke_color);
        this.B0 = context2.getColor(com.moonshot.kimiclaw.R.color.mtrl_textinput_disabled_color);
        this.u0 = context2.getColor(com.moonshot.kimiclaw.R.color.mtrl_textinput_hovered_box_stroke_color);
        if (colorStateListI2 != null) {
            setBoxStrokeColorStateList(colorStateListI2);
        }
        if (typedArrayObtainStyledAttributes.hasValue(15)) {
            setBoxStrokeErrorColor(AbstractC4123tO0.I(context2, c3198mn, 15));
        }
        if (typedArrayObtainStyledAttributes.getResourceId(50, -1) != -1) {
            setHintTextAppearance(typedArrayObtainStyledAttributes.getResourceId(50, 0));
        }
        this.J = c3198mn.z(24);
        this.K = c3198mn.z(25);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(40, 0);
        CharSequence text = typedArrayObtainStyledAttributes.getText(35);
        int i = typedArrayObtainStyledAttributes.getInt(34, 1);
        boolean z = typedArrayObtainStyledAttributes.getBoolean(36, false);
        int resourceId2 = typedArrayObtainStyledAttributes.getResourceId(45, 0);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(44, false);
        CharSequence text2 = typedArrayObtainStyledAttributes.getText(43);
        int resourceId3 = typedArrayObtainStyledAttributes.getResourceId(58, 0);
        CharSequence text3 = typedArrayObtainStyledAttributes.getText(57);
        boolean z3 = typedArrayObtainStyledAttributes.getBoolean(18, false);
        setCounterMaxLength(typedArrayObtainStyledAttributes.getInt(19, -1));
        this.z = typedArrayObtainStyledAttributes.getResourceId(22, 0);
        this.q = typedArrayObtainStyledAttributes.getResourceId(20, 0);
        setBoxBackgroundMode(typedArrayObtainStyledAttributes.getInt(8, 0));
        setErrorContentDescription(text);
        setErrorAccessibilityLiveRegion(i);
        setCounterOverflowTextAppearance(this.q);
        setHelperTextTextAppearance(resourceId2);
        setErrorTextAppearance(resourceId);
        setCounterTextAppearance(this.z);
        setPlaceholderText(text3);
        setPlaceholderTextAppearance(resourceId3);
        if (typedArrayObtainStyledAttributes.hasValue(41)) {
            setErrorTextColor(c3198mn.z(41));
        }
        if (typedArrayObtainStyledAttributes.hasValue(46)) {
            setHelperTextColor(c3198mn.z(46));
        }
        if (typedArrayObtainStyledAttributes.hasValue(51)) {
            setHintTextColor(c3198mn.z(51));
        }
        if (typedArrayObtainStyledAttributes.hasValue(23)) {
            setCounterTextColor(c3198mn.z(23));
        }
        if (typedArrayObtainStyledAttributes.hasValue(21)) {
            setCounterOverflowTextColor(c3198mn.z(21));
        }
        if (typedArrayObtainStyledAttributes.hasValue(59)) {
            setPlaceholderTextColor(c3198mn.z(59));
        }
        OG og = new OG(this, c3198mn);
        this.c = og;
        boolean z4 = typedArrayObtainStyledAttributes.getBoolean(0, true);
        setHintMaxLines(typedArrayObtainStyledAttributes.getInt(49, 1));
        c3198mn.N();
        setImportantForAccessibility(2);
        setImportantForAutofill(1);
        frameLayout.addView(cj0);
        frameLayout.addView(og);
        addView(frameLayout);
        setEnabled(z4);
        setHelperTextEnabled(z2);
        setErrorEnabled(z);
        setCounterEnabled(z3);
        setHelperText(text2);
    }

    private Drawable getEditTextBoxBackground() {
        EditText editText = this.e;
        if (!(editText instanceof AutoCompleteTextView) || AbstractC4635x31.U(editText)) {
            return this.O;
        }
        int iW = AbstractC3564pO0.w(com.moonshot.kimiclaw.R.attr.colorControlHighlight, this.e);
        int i = this.a0;
        int[][] iArr = L0;
        if (i != 2) {
            if (i != 1) {
                return null;
            }
            C3168ma0 c3168ma0 = this.O;
            int i2 = this.g0;
            return new RippleDrawable(new ColorStateList(iArr, new int[]{AbstractC3564pO0.D(iW, i2, 0.1f), i2}), c3168ma0, c3168ma0);
        }
        Context context = getContext();
        C3168ma0 c3168ma02 = this.O;
        TypedValue typedValueA0 = AbstractC2917km0.a0(com.moonshot.kimiclaw.R.attr.colorSurface, context, "TextInputLayout");
        int i3 = typedValueA0.resourceId;
        int color = i3 != 0 ? context.getColor(i3) : typedValueA0.data;
        C3168ma0 c3168ma03 = new C3168ma0(c3168ma02.b.a);
        int iD = AbstractC3564pO0.D(iW, color, 0.1f);
        c3168ma03.n(new ColorStateList(iArr, new int[]{iD, 0}));
        c3168ma03.setTint(color);
        ColorStateList colorStateList = new ColorStateList(iArr, new int[]{iD, color});
        C3168ma0 c3168ma04 = new C3168ma0(c3168ma02.b.a);
        c3168ma04.setTint(-1);
        return new LayerDrawable(new Drawable[]{new RippleDrawable(colorStateList, c3168ma03, c3168ma04), c3168ma02});
    }

    private Drawable getOrCreateFilledDropDownMenuBackground() {
        if (this.Q == null) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            this.Q = stateListDrawable;
            stateListDrawable.addState(new int[]{R.attr.state_above_anchor}, getOrCreateOutlinedDropDownMenuBackground());
            this.Q.addState(new int[0], h(false));
        }
        return this.Q;
    }

    private Drawable getOrCreateOutlinedDropDownMenuBackground() {
        if (this.P == null) {
            this.P = h(true);
        }
        return this.P;
    }

    public static void m(ViewGroup viewGroup, boolean z) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            childAt.setEnabled(z);
            if (childAt instanceof ViewGroup) {
                m((ViewGroup) childAt, z);
            }
        }
    }

    private void setEditText(EditText editText) {
        if (this.e != null) {
            throw new IllegalArgumentException("We already have an EditText, can only have one");
        }
        if (getEndIconMode() != 3 && !(editText instanceof TextInputEditText)) {
            Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead.");
        }
        this.e = editText;
        int i = this.g;
        if (i != -1) {
            setMinEms(i);
        } else {
            setMinWidth(this.i);
        }
        int i2 = this.h;
        if (i2 != -1) {
            setMaxEms(i2);
        } else {
            setMaxWidth(this.j);
        }
        this.R = false;
        k();
        setTextInputAccessibilityDelegate(new C4547wQ0(this));
        Typeface typeface = this.e.getTypeface();
        C1041Tn c1041Tn = this.E0;
        c1041Tn.n(typeface);
        float textSize = this.e.getTextSize();
        if (c1041Tn.h != textSize) {
            c1041Tn.h = textSize;
            c1041Tn.j(false);
        }
        float letterSpacing = this.e.getLetterSpacing();
        if (c1041Tn.X != letterSpacing) {
            c1041Tn.X = letterSpacing;
            c1041Tn.j(false);
        }
        int gravity = this.e.getGravity();
        int i3 = (gravity & (-113)) | 48;
        if (c1041Tn.g != i3) {
            c1041Tn.g = i3;
            c1041Tn.j(false);
        }
        if (c1041Tn.f != gravity) {
            c1041Tn.f = gravity;
            c1041Tn.j(false);
        }
        this.C0 = editText.getMinimumHeight();
        this.e.addTextChangedListener(new C4407vQ0(this, editText));
        if (this.r0 == null) {
            this.r0 = this.e.getHintTextColors();
        }
        if (this.L) {
            if (TextUtils.isEmpty(this.M)) {
                CharSequence hint = this.e.getHint();
                this.f = hint;
                setHint(hint);
                this.e.setHint((CharSequence) null);
            }
            this.N = true;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            r();
        }
        if (this.p != null) {
            p(this.e.getText());
        }
        t();
        this.k.b();
        this.b.bringToFront();
        OG og = this.c;
        og.bringToFront();
        Iterator it = this.n0.iterator();
        while (it.hasNext()) {
            ((NG) it.next()).a(this);
        }
        og.m();
        if (!isEnabled()) {
            editText.setEnabled(false);
        }
        w(false, true);
    }

    private void setHintInternal(CharSequence charSequence) {
        if (TextUtils.equals(charSequence, this.M)) {
            return;
        }
        this.M = charSequence;
        C1041Tn c1041Tn = this.E0;
        if (charSequence == null || !TextUtils.equals(c1041Tn.B, charSequence)) {
            c1041Tn.B = charSequence;
            c1041Tn.C = null;
            c1041Tn.j(false);
        }
        if (this.D0) {
            return;
        }
        l();
    }

    private void setPlaceholderTextEnabled(boolean z) {
        if (this.B == z) {
            return;
        }
        if (z) {
            C0755Oa c0755Oa = this.C;
            if (c0755Oa != null) {
                this.a.addView(c0755Oa);
                this.C.setVisibility(0);
            }
        } else {
            C0755Oa c0755Oa2 = this.C;
            if (c0755Oa2 != null) {
                c0755Oa2.setVisibility(8);
            }
            this.C = null;
        }
        this.B = z;
    }

    public final void a() {
        if (this.e != null) {
            if (this.a0 != 1) {
                return;
            }
            if (!(getHintMaxLines() == 1)) {
                EditText editText = this.e;
                editText.setPaddingRelative(editText.getPaddingStart(), (int) (this.E0.f() + this.d), this.e.getPaddingEnd(), getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.material_filled_edittext_font_1_3_padding_bottom));
            } else if (getContext().getResources().getConfiguration().fontScale >= 2.0f) {
                EditText editText2 = this.e;
                editText2.setPaddingRelative(editText2.getPaddingStart(), getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.material_filled_edittext_font_2_0_padding_top), this.e.getPaddingEnd(), getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.material_filled_edittext_font_2_0_padding_bottom));
            } else if (AbstractC4123tO0.O(getContext())) {
                EditText editText3 = this.e;
                editText3.setPaddingRelative(editText3.getPaddingStart(), getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.material_filled_edittext_font_1_3_padding_top), this.e.getPaddingEnd(), getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.material_filled_edittext_font_1_3_padding_bottom));
            }
        }
    }

    @Override // android.view.ViewGroup
    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (!(view instanceof EditText)) {
            super.addView(view, i, layoutParams);
            return;
        }
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(layoutParams);
        layoutParams2.gravity = (layoutParams2.gravity & (-113)) | 16;
        FrameLayout frameLayout = this.a;
        frameLayout.addView(view, layoutParams2);
        frameLayout.setLayoutParams(layoutParams);
        v();
        setEditText((EditText) view);
    }

    public final void b(float f) {
        int i = 2;
        C1041Tn c1041Tn = this.E0;
        if (c1041Tn.b == f) {
            return;
        }
        if (this.H0 == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.H0 = valueAnimator;
            valueAnimator.setInterpolator(AbstractC2917km0.Z(getContext(), com.moonshot.kimiclaw.R.attr.motionEasingEmphasizedInterpolator, W8.b));
            this.H0.setDuration(AbstractC2917km0.Y(getContext(), com.moonshot.kimiclaw.R.attr.motionDurationMedium4, 167));
            this.H0.addUpdateListener(new C0663Mg(this, i));
        }
        this.H0.setFloatValues(c1041Tn.b, f);
        this.H0.start();
    }

    public final void c() {
        int i;
        int i2;
        C3168ma0 c3168ma0 = this.O;
        if (c3168ma0 == null) {
            return;
        }
        C3126mF0 c3126mF0 = c3168ma0.b.a;
        C3126mF0 c3126mF02 = this.U;
        if (c3126mF0 != c3126mF02) {
            c3168ma0.setShapeAppearanceModel(c3126mF02);
        }
        if (this.a0 == 2 && (i = this.c0) > -1 && (i2 = this.f0) != 0) {
            C3168ma0 c3168ma02 = this.O;
            c3168ma02.b.k = i;
            c3168ma02.invalidateSelf();
            ColorStateList colorStateListValueOf = ColorStateList.valueOf(i2);
            C2888ka0 c2888ka0 = c3168ma02.b;
            if (c2888ka0.e != colorStateListValueOf) {
                c2888ka0.e = colorStateListValueOf;
                c3168ma02.onStateChange(c3168ma02.getState());
            }
        }
        int iB = this.g0;
        if (this.a0 == 1) {
            iB = AbstractC5018zo.b(this.g0, AbstractC3564pO0.x(getContext(), com.moonshot.kimiclaw.R.attr.colorSurface, 0));
        }
        this.g0 = iB;
        this.O.n(ColorStateList.valueOf(iB));
        C3168ma0 c3168ma03 = this.S;
        if (c3168ma03 != null && this.T != null) {
            if (this.c0 > -1 && this.f0 != 0) {
                c3168ma03.n(this.e.isFocused() ? ColorStateList.valueOf(this.t0) : ColorStateList.valueOf(this.f0));
                this.T.n(ColorStateList.valueOf(this.f0));
            }
            invalidate();
        }
        u();
    }

    public final Rect d(Rect rect) {
        if (this.e == null) {
            throw new IllegalStateException();
        }
        boolean z = getLayoutDirection() == 1;
        int i = rect.bottom;
        Rect rect2 = this.i0;
        rect2.bottom = i;
        int i2 = this.a0;
        if (i2 == 1) {
            rect2.left = i(rect.left, z);
            rect2.top = rect.top + this.b0;
            rect2.right = j(rect.right, z);
            return rect2;
        }
        if (i2 != 2) {
            rect2.left = i(rect.left, z);
            rect2.top = getPaddingTop();
            rect2.right = j(rect.right, z);
            return rect2;
        }
        rect2.left = this.e.getPaddingLeft() + rect.left;
        rect2.top = rect.top - e();
        rect2.right = rect.right - this.e.getPaddingRight();
        return rect2;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchProvideAutofillStructure(ViewStructure viewStructure, int i) {
        EditText editText = this.e;
        if (editText == null) {
            super.dispatchProvideAutofillStructure(viewStructure, i);
            return;
        }
        if (this.f != null) {
            boolean z = this.N;
            this.N = false;
            CharSequence hint = editText.getHint();
            this.e.setHint(this.f);
            try {
                super.dispatchProvideAutofillStructure(viewStructure, i);
                return;
            } finally {
                this.e.setHint(hint);
                this.N = z;
            }
        }
        viewStructure.setAutofillId(getAutofillId());
        onProvideAutofillStructure(viewStructure, i);
        onProvideAutofillVirtualStructure(viewStructure, i);
        FrameLayout frameLayout = this.a;
        viewStructure.setChildCount(frameLayout.getChildCount());
        for (int i2 = 0; i2 < frameLayout.getChildCount(); i2++) {
            View childAt = frameLayout.getChildAt(i2);
            ViewStructure viewStructureNewChild = viewStructure.newChild(i2);
            childAt.dispatchProvideAutofillStructure(viewStructureNewChild, i);
            if (childAt == this.e) {
                viewStructureNewChild.setHint(getHint());
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchRestoreInstanceState(SparseArray sparseArray) {
        this.J0 = true;
        super.dispatchRestoreInstanceState(sparseArray);
        this.J0 = false;
    }

    @Override // android.view.View
    public final void draw(Canvas canvas) {
        C3168ma0 c3168ma0;
        Canvas canvas2 = canvas;
        super.draw(canvas);
        boolean z = this.L;
        C1041Tn c1041Tn = this.E0;
        if (z) {
            c1041Tn.getClass();
            int iSave = canvas2.save();
            if (c1041Tn.C != null) {
                RectF rectF = c1041Tn.e;
                if (rectF.width() > 0.0f && rectF.height() > 0.0f) {
                    TextPaint textPaint = c1041Tn.O;
                    textPaint.setTextSize(c1041Tn.G);
                    float f = c1041Tn.q;
                    float f2 = c1041Tn.r;
                    float f3 = c1041Tn.F;
                    if (f3 != 1.0f) {
                        canvas2.scale(f3, f3, f, f2);
                    }
                    if ((c1041Tn.e0 > 1 || c1041Tn.f0 > 1) && !c1041Tn.D && c1041Tn.o()) {
                        float lineStart = c1041Tn.q - c1041Tn.Z.getLineStart(0);
                        int alpha = textPaint.getAlpha();
                        canvas2.translate(lineStart, f2);
                        float f4 = alpha;
                        textPaint.setAlpha((int) (c1041Tn.c0 * f4));
                        int i = Build.VERSION.SDK_INT;
                        if (i >= 31) {
                            float f5 = c1041Tn.H;
                            float f6 = c1041Tn.I;
                            float f7 = c1041Tn.J;
                            int i2 = c1041Tn.K;
                            textPaint.setShadowLayer(f5, f6, f7, AbstractC5018zo.d(i2, (textPaint.getAlpha() * Color.alpha(i2)) / 255));
                        }
                        c1041Tn.Z.draw(canvas2);
                        textPaint.setAlpha((int) (c1041Tn.b0 * f4));
                        if (i >= 31) {
                            float f8 = c1041Tn.H;
                            float f9 = c1041Tn.I;
                            float f10 = c1041Tn.J;
                            int i3 = c1041Tn.K;
                            textPaint.setShadowLayer(f8, f9, f10, AbstractC5018zo.d(i3, (Color.alpha(i3) * textPaint.getAlpha()) / 255));
                        }
                        int lineBaseline = c1041Tn.Z.getLineBaseline(0);
                        CharSequence charSequence = c1041Tn.d0;
                        float f11 = lineBaseline;
                        canvas2.drawText(charSequence, 0, charSequence.length(), 0.0f, f11, textPaint);
                        if (i >= 31) {
                            textPaint.setShadowLayer(c1041Tn.H, c1041Tn.I, c1041Tn.J, c1041Tn.K);
                        }
                        String strTrim = c1041Tn.d0.toString().trim();
                        if (strTrim.endsWith("…")) {
                            strTrim = strTrim.substring(0, strTrim.length() - 1);
                        }
                        String str = strTrim;
                        textPaint.setAlpha(alpha);
                        canvas2 = canvas;
                        canvas2.drawText(str, 0, Math.min(c1041Tn.Z.getLineEnd(0), str.length()), 0.0f, f11, (Paint) textPaint);
                    } else {
                        canvas2.translate(f, f2);
                        c1041Tn.Z.draw(canvas2);
                    }
                    canvas2.restoreToCount(iSave);
                }
            }
        }
        if (this.T == null || (c3168ma0 = this.S) == null) {
            return;
        }
        c3168ma0.draw(canvas2);
        if (this.e.isFocused()) {
            Rect bounds = this.T.getBounds();
            Rect bounds2 = this.S.getBounds();
            float f12 = c1041Tn.b;
            int iCenterX = bounds2.centerX();
            bounds.left = W8.c(iCenterX, bounds2.left, f12);
            bounds.right = W8.c(iCenterX, bounds2.right, f12);
            this.T.draw(canvas2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x002f  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void drawableStateChanged() {
        boolean z;
        ColorStateList colorStateList;
        if (this.I0) {
            return;
        }
        this.I0 = true;
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        C1041Tn c1041Tn = this.E0;
        if (c1041Tn != null) {
            c1041Tn.M = drawableState;
            ColorStateList colorStateList2 = c1041Tn.k;
            if ((colorStateList2 == null || !colorStateList2.isStateful()) && ((colorStateList = c1041Tn.j) == null || !colorStateList.isStateful())) {
                z = false;
            } else {
                c1041Tn.j(false);
                z = true;
            }
        }
        if (this.e != null) {
            w(isLaidOut() && isEnabled(), false);
        }
        t();
        z();
        if (z) {
            invalidate();
        }
        this.I0 = false;
    }

    public final int e() {
        if (this.L) {
            int i = this.a0;
            C1041Tn c1041Tn = this.E0;
            if (i == 0) {
                return (int) c1041Tn.f();
            }
            if (i == 2) {
                if (getHintMaxLines() == 1) {
                    return (int) (c1041Tn.f() / 2.0f);
                }
                float f = c1041Tn.f();
                TextPaint textPaint = c1041Tn.P;
                textPaint.setTextSize(c1041Tn.i);
                textPaint.setTypeface(c1041Tn.s);
                textPaint.setLetterSpacing(c1041Tn.W);
                return Math.max(0, (int) (f - ((-textPaint.ascent()) / 2.0f)));
            }
        }
        return 0;
    }

    public final C3273nJ f() {
        C3273nJ c3273nJ = new C3273nJ();
        c3273nJ.c = AbstractC2917km0.Y(getContext(), com.moonshot.kimiclaw.R.attr.motionDurationShort2, 87);
        c3273nJ.d = AbstractC2917km0.Z(getContext(), com.moonshot.kimiclaw.R.attr.motionEasingLinearInterpolator, W8.a);
        return c3273nJ;
    }

    public final boolean g() {
        return this.L && !TextUtils.isEmpty(this.M) && (this.O instanceof C0070Av);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public int getBaseline() {
        EditText editText = this.e;
        if (editText == null) {
            return super.getBaseline();
        }
        return e() + getPaddingTop() + editText.getBaseline();
    }

    public C3168ma0 getBoxBackground() {
        int i = this.a0;
        if (i == 1 || i == 2) {
            return this.O;
        }
        throw new IllegalStateException();
    }

    public int getBoxBackgroundColor() {
        return this.g0;
    }

    public int getBoxBackgroundMode() {
        return this.a0;
    }

    public int getBoxCollapsedPaddingTop() {
        return this.b0;
    }

    public float getBoxCornerRadiusBottomEnd() {
        int layoutDirection = getLayoutDirection();
        RectF rectF = this.j0;
        return layoutDirection == 1 ? this.U.h.a(rectF) : this.U.g.a(rectF);
    }

    public float getBoxCornerRadiusBottomStart() {
        int layoutDirection = getLayoutDirection();
        RectF rectF = this.j0;
        return layoutDirection == 1 ? this.U.g.a(rectF) : this.U.h.a(rectF);
    }

    public float getBoxCornerRadiusTopEnd() {
        int layoutDirection = getLayoutDirection();
        RectF rectF = this.j0;
        return layoutDirection == 1 ? this.U.e.a(rectF) : this.U.f.a(rectF);
    }

    public float getBoxCornerRadiusTopStart() {
        int layoutDirection = getLayoutDirection();
        RectF rectF = this.j0;
        return layoutDirection == 1 ? this.U.f.a(rectF) : this.U.e.a(rectF);
    }

    public int getBoxStrokeColor() {
        return this.v0;
    }

    public ColorStateList getBoxStrokeErrorColor() {
        return this.w0;
    }

    public int getBoxStrokeWidth() {
        return this.d0;
    }

    public int getBoxStrokeWidthFocused() {
        return this.e0;
    }

    public int getCounterMaxLength() {
        return this.m;
    }

    public CharSequence getCounterOverflowDescription() {
        C0755Oa c0755Oa;
        if (this.l && this.n && (c0755Oa = this.p) != null) {
            return c0755Oa.getContentDescription();
        }
        return null;
    }

    public ColorStateList getCounterOverflowTextColor() {
        return this.I;
    }

    public ColorStateList getCounterTextColor() {
        return this.H;
    }

    public ColorStateList getCursorColor() {
        return this.J;
    }

    public ColorStateList getCursorErrorColor() {
        return this.K;
    }

    public ColorStateList getDefaultHintTextColor() {
        return this.r0;
    }

    public EditText getEditText() {
        return this.e;
    }

    public CharSequence getEndIconContentDescription() {
        return this.c.g.getContentDescription();
    }

    public Drawable getEndIconDrawable() {
        return this.c.g.getDrawable();
    }

    public int getEndIconMinSize() {
        return this.c.m;
    }

    public int getEndIconMode() {
        return this.c.i;
    }

    public ImageView.ScaleType getEndIconScaleType() {
        return this.c.n;
    }

    public CheckableImageButton getEndIconView() {
        return this.c.g;
    }

    public CharSequence getError() {
        OW ow = this.k;
        if (ow.q) {
            return ow.p;
        }
        return null;
    }

    public int getErrorAccessibilityLiveRegion() {
        return this.k.t;
    }

    public CharSequence getErrorContentDescription() {
        return this.k.s;
    }

    public int getErrorCurrentTextColors() {
        C0755Oa c0755Oa = this.k.r;
        if (c0755Oa != null) {
            return c0755Oa.getCurrentTextColor();
        }
        return -1;
    }

    public Drawable getErrorIconDrawable() {
        return this.c.c.getDrawable();
    }

    public CharSequence getHelperText() {
        OW ow = this.k;
        if (ow.x) {
            return ow.w;
        }
        return null;
    }

    public int getHelperTextCurrentTextColor() {
        C0755Oa c0755Oa = this.k.y;
        if (c0755Oa != null) {
            return c0755Oa.getCurrentTextColor();
        }
        return -1;
    }

    public CharSequence getHint() {
        if (this.L) {
            return this.M;
        }
        return null;
    }

    public final float getHintCollapsedTextHeight() {
        return this.E0.f();
    }

    public final int getHintCurrentCollapsedTextColor() {
        C1041Tn c1041Tn = this.E0;
        return c1041Tn.g(c1041Tn.k);
    }

    public int getHintMaxLines() {
        return this.E0.e0;
    }

    public ColorStateList getHintTextColor() {
        return this.s0;
    }

    public InterfaceC4687xQ0 getLengthCounter() {
        return this.o;
    }

    public int getMaxEms() {
        return this.h;
    }

    public int getMaxWidth() {
        return this.j;
    }

    public int getMinEms() {
        return this.g;
    }

    public int getMinWidth() {
        return this.i;
    }

    @Deprecated
    public CharSequence getPasswordVisibilityToggleContentDescription() {
        return this.c.g.getContentDescription();
    }

    @Deprecated
    public Drawable getPasswordVisibilityToggleDrawable() {
        return this.c.g.getDrawable();
    }

    public CharSequence getPlaceholderText() {
        if (this.B) {
            return this.A;
        }
        return null;
    }

    public int getPlaceholderTextAppearance() {
        return this.E;
    }

    public ColorStateList getPlaceholderTextColor() {
        return this.D;
    }

    public CharSequence getPrefixText() {
        return this.b.c;
    }

    public ColorStateList getPrefixTextColor() {
        return this.b.b.getTextColors();
    }

    public TextView getPrefixTextView() {
        return this.b.b;
    }

    public C3126mF0 getShapeAppearanceModel() {
        return this.U;
    }

    public CharSequence getStartIconContentDescription() {
        return this.b.d.getContentDescription();
    }

    public Drawable getStartIconDrawable() {
        return this.b.d.getDrawable();
    }

    public int getStartIconMinSize() {
        return this.b.g;
    }

    public ImageView.ScaleType getStartIconScaleType() {
        return this.b.h;
    }

    public CharSequence getSuffixText() {
        return this.c.p;
    }

    public ColorStateList getSuffixTextColor() {
        return this.c.q.getTextColors();
    }

    public TextView getSuffixTextView() {
        return this.c.q;
    }

    public Typeface getTypeface() {
        return this.k0;
    }

    public final C3168ma0 h(boolean z) {
        int i = 0;
        float dimensionPixelOffset = getResources().getDimensionPixelOffset(com.moonshot.kimiclaw.R.dimen.mtrl_shape_corner_size_small_component);
        float f = z ? dimensionPixelOffset : 0.0f;
        EditText editText = this.e;
        float popupElevation = editText instanceof T90 ? ((T90) editText).getPopupElevation() : getResources().getDimensionPixelOffset(com.moonshot.kimiclaw.R.dimen.m3_comp_outlined_autocomplete_menu_container_elevation);
        int dimensionPixelOffset2 = getResources().getDimensionPixelOffset(com.moonshot.kimiclaw.R.dimen.mtrl_exposed_dropdown_menu_popup_vertical_padding);
        C1064Ty0 c1064Ty0 = new C1064Ty0();
        C1064Ty0 c1064Ty02 = new C1064Ty0();
        C1064Ty0 c1064Ty03 = new C1064Ty0();
        C1064Ty0 c1064Ty04 = new C1064Ty0();
        C2705jF c2705jF = new C2705jF(i);
        C2705jF c2705jF2 = new C2705jF(i);
        C2705jF c2705jF3 = new C2705jF(i);
        C2705jF c2705jF4 = new C2705jF(i);
        C2664j c2664j = new C2664j(f);
        C2664j c2664j2 = new C2664j(f);
        C2664j c2664j3 = new C2664j(dimensionPixelOffset);
        C2664j c2664j4 = new C2664j(dimensionPixelOffset);
        C3126mF0 c3126mF0 = new C3126mF0();
        c3126mF0.a = c1064Ty0;
        c3126mF0.b = c1064Ty02;
        c3126mF0.c = c1064Ty03;
        c3126mF0.d = c1064Ty04;
        c3126mF0.e = c2664j;
        c3126mF0.f = c2664j2;
        c3126mF0.g = c2664j4;
        c3126mF0.h = c2664j3;
        c3126mF0.i = c2705jF;
        c3126mF0.j = c2705jF2;
        c3126mF0.k = c2705jF3;
        c3126mF0.l = c2705jF4;
        EditText editText2 = this.e;
        ColorStateList dropDownBackgroundTintList = editText2 instanceof T90 ? ((T90) editText2).getDropDownBackgroundTintList() : null;
        Context context = getContext();
        if (dropDownBackgroundTintList == null) {
            Paint paint = C3168ma0.M;
            TypedValue typedValueA0 = AbstractC2917km0.a0(com.moonshot.kimiclaw.R.attr.colorSurface, context, C3168ma0.class.getSimpleName());
            int i2 = typedValueA0.resourceId;
            dropDownBackgroundTintList = ColorStateList.valueOf(i2 != 0 ? context.getColor(i2) : typedValueA0.data);
        }
        C3168ma0 c3168ma0 = new C3168ma0();
        c3168ma0.k(context);
        c3168ma0.n(dropDownBackgroundTintList);
        c3168ma0.m(popupElevation);
        c3168ma0.setShapeAppearanceModel(c3126mF0);
        C2888ka0 c2888ka0 = c3168ma0.b;
        if (c2888ka0.h == null) {
            c2888ka0.h = new Rect();
        }
        c3168ma0.b.h.set(0, dimensionPixelOffset2, 0, dimensionPixelOffset2);
        c3168ma0.invalidateSelf();
        return c3168ma0;
    }

    public final int i(int i, boolean z) {
        return ((z || getPrefixText() == null) ? (!z || getSuffixText() == null) ? this.e.getCompoundPaddingLeft() : this.c.c() : this.b.a()) + i;
    }

    public final int j(int i, boolean z) {
        return i - ((z || getSuffixText() == null) ? (!z || getPrefixText() == null) ? this.e.getCompoundPaddingRight() : this.b.a() : this.c.c());
    }

    public final void k() {
        int i = this.a0;
        if (i == 0) {
            this.O = null;
            this.S = null;
            this.T = null;
        } else if (i == 1) {
            this.O = new C3168ma0(this.U);
            this.S = new C3168ma0();
            this.T = new C3168ma0();
        } else {
            if (i != 2) {
                throw new IllegalArgumentException(AbstractC4671xI0.n(new StringBuilder(), this.a0, " is illegal; only @BoxBackgroundMode constants are supported."));
            }
            if (!this.L || (this.O instanceof C0070Av)) {
                this.O = new C3168ma0(this.U);
            } else {
                C3126mF0 c3126mF0 = this.U;
                int i2 = C0070Av.P;
                if (c3126mF0 == null) {
                    c3126mF0 = new C3126mF0();
                }
                C5032zv c5032zv = new C5032zv(c3126mF0, new RectF());
                C0070Av c0070Av = new C0070Av(c5032zv);
                c0070Av.O = c5032zv;
                this.O = c0070Av;
            }
            this.S = null;
            this.T = null;
        }
        u();
        z();
        if (this.a0 == 1) {
            if (getContext().getResources().getConfiguration().fontScale >= 2.0f) {
                this.b0 = getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.material_font_2_0_box_collapsed_padding_top);
            } else if (AbstractC4123tO0.O(getContext())) {
                this.b0 = getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.material_font_1_3_box_collapsed_padding_top);
            }
        }
        a();
        if (this.a0 != 0) {
            v();
        }
        EditText editText = this.e;
        if (editText instanceof AutoCompleteTextView) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) editText;
            if (autoCompleteTextView.getDropDownBackground() == null) {
                int i3 = this.a0;
                if (i3 == 2) {
                    autoCompleteTextView.setDropDownBackgroundDrawable(getOrCreateOutlinedDropDownMenuBackground());
                } else if (i3 == 1) {
                    autoCompleteTextView.setDropDownBackgroundDrawable(getOrCreateFilledDropDownMenuBackground());
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00cb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void l() {
        float f;
        float f2;
        float f3;
        RectF rectF;
        float f4;
        int i;
        float f5;
        int i2;
        if (g()) {
            int width = this.e.getWidth();
            int gravity = this.e.getGravity();
            C1041Tn c1041Tn = this.E0;
            boolean zC = c1041Tn.c(c1041Tn.B);
            c1041Tn.D = zC;
            Rect rect = c1041Tn.d;
            if (gravity != 17 && (gravity & 7) != 1) {
                if ((gravity & 8388613) == 8388613 || (gravity & 5) == 5) {
                    if (zC) {
                        i2 = rect.left;
                        f3 = i2;
                    } else {
                        f = rect.right;
                        f2 = c1041Tn.a0;
                    }
                } else if (zC) {
                    f = rect.right;
                    f2 = c1041Tn.a0;
                } else {
                    i2 = rect.left;
                    f3 = i2;
                }
                float fMax = Math.max(f3, rect.left);
                rectF = this.j0;
                rectF.left = fMax;
                rectF.top = rect.top;
                if (gravity != 17 || (gravity & 7) == 1) {
                    f4 = (width / 2.0f) + (c1041Tn.a0 / 2.0f);
                } else if ((gravity & 8388613) == 8388613 || (gravity & 5) == 5) {
                    if (c1041Tn.D) {
                        f5 = c1041Tn.a0;
                        f4 = f5 + fMax;
                    } else {
                        i = rect.right;
                        f4 = i;
                    }
                } else if (c1041Tn.D) {
                    i = rect.right;
                    f4 = i;
                } else {
                    f5 = c1041Tn.a0;
                    f4 = f5 + fMax;
                }
                rectF.right = Math.min(f4, rect.right);
                rectF.bottom = c1041Tn.f() + rect.top;
                if (c1041Tn.Z != null && !c1041Tn.o()) {
                    StaticLayout staticLayout = c1041Tn.Z;
                    float lineWidth = (c1041Tn.i / c1041Tn.h) * staticLayout.getLineWidth(staticLayout.getLineCount() - 1);
                    if (c1041Tn.D) {
                        rectF.right = rectF.left + lineWidth;
                    } else {
                        rectF.left = rectF.right - lineWidth;
                    }
                }
                if (rectF.width() > 0.0f || rectF.height() <= 0.0f) {
                }
                float f6 = rectF.left;
                float f7 = this.W;
                rectF.left = f6 - f7;
                rectF.right += f7;
                rectF.offset(-getPaddingLeft(), ((-getPaddingTop()) - (rectF.height() / 2.0f)) + this.c0);
                rectF.top = 0.0f;
                C0070Av c0070Av = (C0070Av) this.O;
                c0070Av.getClass();
                c0070Av.t(rectF.left, rectF.top, rectF.right, rectF.bottom);
                return;
            }
            f = width / 2.0f;
            f2 = c1041Tn.a0 / 2.0f;
            f3 = f - f2;
            float fMax2 = Math.max(f3, rect.left);
            rectF = this.j0;
            rectF.left = fMax2;
            rectF.top = rect.top;
            if (gravity != 17) {
                f4 = (width / 2.0f) + (c1041Tn.a0 / 2.0f);
            }
            rectF.right = Math.min(f4, rect.right);
            rectF.bottom = c1041Tn.f() + rect.top;
            if (c1041Tn.Z != null) {
                StaticLayout staticLayout2 = c1041Tn.Z;
                float lineWidth2 = (c1041Tn.i / c1041Tn.h) * staticLayout2.getLineWidth(staticLayout2.getLineCount() - 1);
                if (c1041Tn.D) {
                }
            }
            if (rectF.width() > 0.0f) {
            }
        }
    }

    public final void n(C0755Oa c0755Oa, int i) {
        try {
            c0755Oa.setTextAppearance(i);
            if (c0755Oa.getTextColors().getDefaultColor() != -65281) {
                return;
            }
        } catch (Exception unused) {
        }
        c0755Oa.setTextAppearance(com.moonshot.kimiclaw.R.style.TextAppearance_AppCompat_Caption);
        c0755Oa.setTextColor(getContext().getColor(com.moonshot.kimiclaw.R.color.design_error));
    }

    public final boolean o() {
        OW ow = this.k;
        return (ow.o != 1 || ow.r == null || TextUtils.isEmpty(ow.p)) ? false : true;
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.E0.i(configuration);
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public final void onGlobalLayout() {
        int iMax;
        OG og = this.c;
        og.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        boolean z = false;
        this.K0 = false;
        if (this.e != null && this.e.getMeasuredHeight() < (iMax = Math.max(og.getMeasuredHeight(), this.b.getMeasuredHeight()))) {
            this.e.setMinimumHeight(iMax);
            z = true;
        }
        boolean zS = s();
        if (z || zS) {
            this.e.post(new NN(this, 24));
        }
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float fDescent;
        int i5;
        int compoundPaddingTop;
        super.onLayout(z, i, i2, i3, i4);
        EditText editText = this.e;
        if (editText != null) {
            Rect rect = this.h0;
            VA.a(this, editText, rect);
            C3168ma0 c3168ma0 = this.S;
            if (c3168ma0 != null) {
                int i6 = rect.bottom;
                c3168ma0.setBounds(rect.left, i6 - this.d0, rect.right, i6);
            }
            C3168ma0 c3168ma02 = this.T;
            if (c3168ma02 != null) {
                int i7 = rect.bottom;
                c3168ma02.setBounds(rect.left, i7 - this.e0, rect.right, i7);
            }
            if (this.L) {
                float textSize = this.e.getTextSize();
                C1041Tn c1041Tn = this.E0;
                if (c1041Tn.h != textSize) {
                    c1041Tn.h = textSize;
                    c1041Tn.j(false);
                }
                int gravity = this.e.getGravity();
                int i8 = (gravity & (-113)) | 48;
                if (c1041Tn.g != i8) {
                    c1041Tn.g = i8;
                    c1041Tn.j(false);
                }
                if (c1041Tn.f != gravity) {
                    c1041Tn.f = gravity;
                    c1041Tn.j(false);
                }
                Rect rectD = d(rect);
                int i9 = rectD.left;
                int i10 = rectD.top;
                int i11 = rectD.right;
                int i12 = rectD.bottom;
                Rect rect2 = c1041Tn.d;
                if (rect2.left != i9 || rect2.top != i10 || rect2.right != i11 || rect2.bottom != i12) {
                    rect2.set(i9, i10, i11, i12);
                    c1041Tn.N = true;
                }
                if (this.e == null) {
                    throw new IllegalStateException();
                }
                int hintMaxLines = getHintMaxLines();
                TextPaint textPaint = c1041Tn.P;
                if (hintMaxLines == 1) {
                    textPaint.setTextSize(c1041Tn.h);
                    textPaint.setTypeface(c1041Tn.v);
                    textPaint.setLetterSpacing(c1041Tn.X);
                    fDescent = -textPaint.ascent();
                } else {
                    textPaint.setTextSize(c1041Tn.h);
                    textPaint.setTypeface(c1041Tn.v);
                    textPaint.setLetterSpacing(c1041Tn.X);
                    fDescent = c1041Tn.l * (textPaint.descent() + (-textPaint.ascent()));
                }
                int compoundPaddingLeft = this.e.getCompoundPaddingLeft() + rect.left;
                Rect rect3 = this.i0;
                rect3.left = compoundPaddingLeft;
                if (this.a0 == 1 && this.e.getMinLines() <= 1) {
                    compoundPaddingTop = (int) (rect.centerY() - (fDescent / 2.0f));
                } else {
                    if (this.a0 != 0 || getHintMaxLines() == 1) {
                        i5 = 0;
                    } else {
                        textPaint.setTextSize(c1041Tn.h);
                        textPaint.setTypeface(c1041Tn.v);
                        textPaint.setLetterSpacing(c1041Tn.X);
                        i5 = (int) ((-textPaint.ascent()) / 2.0f);
                    }
                    compoundPaddingTop = (this.e.getCompoundPaddingTop() + rect.top) - i5;
                }
                rect3.top = compoundPaddingTop;
                rect3.right = rect.right - this.e.getCompoundPaddingRight();
                int compoundPaddingBottom = (this.a0 != 1 || this.e.getMinLines() > 1) ? rect.bottom - this.e.getCompoundPaddingBottom() : (int) (rect3.top + fDescent);
                rect3.bottom = compoundPaddingBottom;
                int i13 = rect3.left;
                int i14 = rect3.top;
                int i15 = rect3.right;
                Rect rect4 = c1041Tn.c;
                if (!(rect4.left == i13 && rect4.top == i14 && rect4.right == i15 && rect4.bottom == compoundPaddingBottom) || true != c1041Tn.k0) {
                    rect4.set(i13, i14, i15, compoundPaddingBottom);
                    c1041Tn.N = true;
                    c1041Tn.k0 = true;
                }
                c1041Tn.j(false);
                if (!g() || this.D0) {
                    return;
                }
                l();
            }
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        float f;
        EditText editText;
        super.onMeasure(i, i2);
        boolean z = this.K0;
        OG og = this.c;
        if (!z) {
            og.getViewTreeObserver().addOnGlobalLayoutListener(this);
            this.K0 = true;
        }
        if (this.C != null && (editText = this.e) != null) {
            this.C.setGravity(editText.getGravity());
            this.C.setPadding(this.e.getCompoundPaddingLeft(), this.e.getCompoundPaddingTop(), this.e.getCompoundPaddingRight(), this.e.getCompoundPaddingBottom());
        }
        og.m();
        if (getHintMaxLines() == 1) {
            return;
        }
        int measuredWidth = (this.e.getMeasuredWidth() - this.e.getCompoundPaddingLeft()) - this.e.getCompoundPaddingRight();
        C1041Tn c1041Tn = this.E0;
        TextPaint textPaint = c1041Tn.P;
        textPaint.setTextSize(c1041Tn.i);
        textPaint.setTypeface(c1041Tn.s);
        textPaint.setLetterSpacing(c1041Tn.W);
        float f2 = measuredWidth;
        c1041Tn.i0 = c1041Tn.e(c1041Tn.f0, textPaint, c1041Tn.B, (c1041Tn.i / c1041Tn.h) * f2, c1041Tn.D).getHeight();
        textPaint.setTextSize(c1041Tn.h);
        textPaint.setTypeface(c1041Tn.v);
        textPaint.setLetterSpacing(c1041Tn.X);
        c1041Tn.j0 = c1041Tn.e(c1041Tn.e0, textPaint, c1041Tn.B, f2, c1041Tn.D).getHeight();
        EditText editText2 = this.e;
        Rect rect = this.h0;
        VA.a(this, editText2, rect);
        Rect rectD = d(rect);
        int i3 = rectD.left;
        int i4 = rectD.top;
        int i5 = rectD.right;
        int i6 = rectD.bottom;
        Rect rect2 = c1041Tn.d;
        if (rect2.left != i3 || rect2.top != i4 || rect2.right != i5 || rect2.bottom != i6) {
            rect2.set(i3, i4, i5, i6);
            c1041Tn.N = true;
        }
        v();
        a();
        if (this.e == null) {
            return;
        }
        int i7 = c1041Tn.j0;
        if (i7 != -1) {
            f = i7;
        } else {
            TextPaint textPaint2 = c1041Tn.P;
            textPaint2.setTextSize(c1041Tn.h);
            textPaint2.setTypeface(c1041Tn.v);
            textPaint2.setLetterSpacing(c1041Tn.X);
            f = -textPaint2.ascent();
        }
        if (this.A != null) {
            TextPaint textPaint3 = new TextPaint(129);
            textPaint3.set(this.C.getPaint());
            textPaint3.setTextSize(this.C.getTextSize());
            textPaint3.setTypeface(this.C.getTypeface());
            textPaint3.setLetterSpacing(this.C.getLetterSpacing());
            C2856kK0 c2856kK0 = new C2856kK0(this.A, textPaint3, measuredWidth);
            c2856kK0.k = getLayoutDirection() == 1;
            c2856kK0.j = true;
            float lineSpacingExtra = this.C.getLineSpacingExtra();
            float lineSpacingMultiplier = this.C.getLineSpacingMultiplier();
            c2856kK0.g = lineSpacingExtra;
            c2856kK0.h = lineSpacingMultiplier;
            c2856kK0.m = new C2113f2(this, 27);
            f = (this.a0 == 1 ? c1041Tn.f() + this.b0 + this.d : 0.0f) + c2856kK0.a().getHeight();
        }
        float fMax = Math.max(f, f);
        if (this.e.getMeasuredHeight() < fMax) {
            this.e.setMinimumHeight(Math.round(fMax));
        }
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof C4827yQ0)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        C4827yQ0 c4827yQ0 = (C4827yQ0) parcelable;
        super.onRestoreInstanceState(c4827yQ0.a);
        setError(c4827yQ0.c);
        if (c4827yQ0.d) {
            post(new F2(this, 16));
        }
        requestLayout();
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        boolean z = i == 1;
        if (z != this.V) {
            InterfaceC0484Iu interfaceC0484Iu = this.U.e;
            RectF rectF = this.j0;
            float fA = interfaceC0484Iu.a(rectF);
            float fA2 = this.U.f.a(rectF);
            float fA3 = this.U.h.a(rectF);
            float fA4 = this.U.g.a(rectF);
            C3126mF0 c3126mF0 = this.U;
            AbstractC1183Wg abstractC1183Wg = c3126mF0.a;
            AbstractC1183Wg abstractC1183Wg2 = c3126mF0.b;
            AbstractC1183Wg abstractC1183Wg3 = c3126mF0.d;
            AbstractC1183Wg abstractC1183Wg4 = c3126mF0.c;
            C2705jF c2705jF = new C2705jF(0);
            C2705jF c2705jF2 = new C2705jF(0);
            C2705jF c2705jF3 = new C2705jF(0);
            C2705jF c2705jF4 = new C2705jF(0);
            C2986lF0.b(abstractC1183Wg2);
            C2986lF0.b(abstractC1183Wg);
            C2986lF0.b(abstractC1183Wg4);
            C2986lF0.b(abstractC1183Wg3);
            C2664j c2664j = new C2664j(fA2);
            C2664j c2664j2 = new C2664j(fA);
            C2664j c2664j3 = new C2664j(fA4);
            C2664j c2664j4 = new C2664j(fA3);
            C3126mF0 c3126mF02 = new C3126mF0();
            c3126mF02.a = abstractC1183Wg2;
            c3126mF02.b = abstractC1183Wg;
            c3126mF02.c = abstractC1183Wg3;
            c3126mF02.d = abstractC1183Wg4;
            c3126mF02.e = c2664j;
            c3126mF02.f = c2664j2;
            c3126mF02.g = c2664j4;
            c3126mF02.h = c2664j3;
            c3126mF02.i = c2705jF;
            c3126mF02.j = c2705jF2;
            c3126mF02.k = c2705jF3;
            c3126mF02.l = c2705jF4;
            this.V = z;
            setShapeAppearanceModel(c3126mF02);
        }
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        C4827yQ0 c4827yQ0 = new C4827yQ0(super.onSaveInstanceState());
        if (o()) {
            c4827yQ0.c = getError();
        }
        OG og = this.c;
        c4827yQ0.d = og.i != 0 && og.g.d;
        return c4827yQ0;
    }

    public final void p(Editable editable) {
        ((C3781qy0) this.o).getClass();
        int length = editable != null ? editable.length() : 0;
        boolean z = this.n;
        int i = this.m;
        String string = null;
        if (i == -1) {
            this.p.setText(String.valueOf(length));
            this.p.setContentDescription(null);
            this.n = false;
        } else {
            this.n = length > i;
            Context context = getContext();
            this.p.setContentDescription(context.getString(this.n ? com.moonshot.kimiclaw.R.string.character_counter_overflowed_content_description : com.moonshot.kimiclaw.R.string.character_counter_content_description, Integer.valueOf(length), Integer.valueOf(this.m)));
            if (z != this.n) {
                q();
            }
            String str = C1479ag.b;
            C1479ag c1479ag = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1 ? C1479ag.e : C1479ag.d;
            C0755Oa c0755Oa = this.p;
            String string2 = getContext().getString(com.moonshot.kimiclaw.R.string.character_counter_pattern, Integer.valueOf(length), Integer.valueOf(this.m));
            if (string2 == null) {
                c1479ag.getClass();
            } else {
                c1479ag.getClass();
                C0715Ng c0715Ng = AP0.a;
                string = c1479ag.c(string2).toString();
            }
            c0755Oa.setText(string);
        }
        if (this.e == null || z == this.n) {
            return;
        }
        w(false, false);
        z();
        t();
    }

    public final void q() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        C0755Oa c0755Oa = this.p;
        if (c0755Oa != null) {
            n(c0755Oa, this.n ? this.q : this.z);
            if (!this.n && (colorStateList2 = this.H) != null) {
                this.p.setTextColor(colorStateList2);
            }
            if (!this.n || (colorStateList = this.I) == null) {
                return;
            }
            this.p.setTextColor(colorStateList);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void r() {
        ColorStateList colorStateList;
        ColorStateList colorStateListValueOf = this.J;
        if (colorStateListValueOf == null) {
            Context context = getContext();
            TypedValue typedValueW = AbstractC2917km0.W(context, com.moonshot.kimiclaw.R.attr.colorControlActivated);
            if (typedValueW != null) {
                int i = typedValueW.resourceId;
                if (i != 0) {
                    colorStateListValueOf = AbstractC4635x31.L(context, i);
                } else {
                    int i2 = typedValueW.data;
                    colorStateListValueOf = i2 != 0 ? ColorStateList.valueOf(i2) : null;
                }
            }
        }
        EditText editText = this.e;
        if (editText == null || editText.getTextCursorDrawable() == null) {
            return;
        }
        Drawable drawableMutate = this.e.getTextCursorDrawable().mutate();
        if ((o() || (this.p != null && this.n)) && (colorStateList = this.K) != null) {
            colorStateListValueOf = colorStateList;
        }
        drawableMutate.setTintList(colorStateListValueOf);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean s() {
        boolean z;
        if (this.e == null) {
            return false;
        }
        CheckableImageButton checkableImageButton = null;
        boolean z2 = true;
        if (getStartIconDrawable() != null || (getPrefixText() != null && getPrefixTextView().getVisibility() == 0)) {
            CJ0 cj0 = this.b;
            if (cj0.getMeasuredWidth() > 0) {
                int measuredWidth = cj0.getMeasuredWidth() - this.e.getPaddingLeft();
                if (this.l0 == null || this.m0 != measuredWidth) {
                    ColorDrawable colorDrawable = new ColorDrawable();
                    this.l0 = colorDrawable;
                    this.m0 = measuredWidth;
                    colorDrawable.setBounds(0, 0, measuredWidth, 1);
                }
                Drawable[] compoundDrawablesRelative = this.e.getCompoundDrawablesRelative();
                Drawable drawable = compoundDrawablesRelative[0];
                ColorDrawable colorDrawable2 = this.l0;
                if (drawable != colorDrawable2) {
                    this.e.setCompoundDrawablesRelative(colorDrawable2, compoundDrawablesRelative[1], compoundDrawablesRelative[2], compoundDrawablesRelative[3]);
                    z = true;
                }
                z = false;
            } else {
                if (this.l0 != null) {
                    Drawable[] compoundDrawablesRelative2 = this.e.getCompoundDrawablesRelative();
                    this.e.setCompoundDrawablesRelative(null, compoundDrawablesRelative2[1], compoundDrawablesRelative2[2], compoundDrawablesRelative2[3]);
                    this.l0 = null;
                    z = true;
                }
                z = false;
            }
        }
        OG og = this.c;
        if ((og.e() || ((og.i != 0 && og.d()) || og.p != null)) && og.getMeasuredWidth() > 0) {
            int measuredWidth2 = og.q.getMeasuredWidth() - this.e.getPaddingRight();
            if (og.e()) {
                checkableImageButton = og.c;
            } else if (og.i != 0 && og.d()) {
                checkableImageButton = og.g;
            }
            if (checkableImageButton != null) {
                measuredWidth2 = ((ViewGroup.MarginLayoutParams) checkableImageButton.getLayoutParams()).getMarginStart() + checkableImageButton.getMeasuredWidth() + measuredWidth2;
            }
            Drawable[] compoundDrawablesRelative3 = this.e.getCompoundDrawablesRelative();
            ColorDrawable colorDrawable3 = this.o0;
            if (colorDrawable3 != null && this.p0 != measuredWidth2) {
                this.p0 = measuredWidth2;
                colorDrawable3.setBounds(0, 0, measuredWidth2, 1);
                this.e.setCompoundDrawablesRelative(compoundDrawablesRelative3[0], compoundDrawablesRelative3[1], this.o0, compoundDrawablesRelative3[3]);
                return true;
            }
            if (colorDrawable3 == null) {
                ColorDrawable colorDrawable4 = new ColorDrawable();
                this.o0 = colorDrawable4;
                this.p0 = measuredWidth2;
                colorDrawable4.setBounds(0, 0, measuredWidth2, 1);
            }
            Drawable drawable2 = compoundDrawablesRelative3[2];
            ColorDrawable colorDrawable5 = this.o0;
            if (drawable2 != colorDrawable5) {
                this.q0 = drawable2;
                this.e.setCompoundDrawablesRelative(compoundDrawablesRelative3[0], compoundDrawablesRelative3[1], colorDrawable5, compoundDrawablesRelative3[3]);
                return true;
            }
        } else if (this.o0 != null) {
            Drawable[] compoundDrawablesRelative4 = this.e.getCompoundDrawablesRelative();
            if (compoundDrawablesRelative4[2] == this.o0) {
                this.e.setCompoundDrawablesRelative(compoundDrawablesRelative4[0], compoundDrawablesRelative4[1], this.q0, compoundDrawablesRelative4[3]);
            } else {
                z2 = z;
            }
            this.o0 = null;
            return z2;
        }
        return z;
    }

    public void setBoxBackgroundColor(int i) {
        if (this.g0 != i) {
            this.g0 = i;
            this.x0 = i;
            this.z0 = i;
            this.A0 = i;
            c();
        }
    }

    public void setBoxBackgroundColorResource(int i) {
        setBoxBackgroundColor(getContext().getColor(i));
    }

    public void setBoxBackgroundColorStateList(ColorStateList colorStateList) {
        int defaultColor = colorStateList.getDefaultColor();
        this.x0 = defaultColor;
        this.g0 = defaultColor;
        this.y0 = colorStateList.getColorForState(new int[]{-16842910}, -1);
        this.z0 = colorStateList.getColorForState(new int[]{R.attr.state_focused, R.attr.state_enabled}, -1);
        this.A0 = colorStateList.getColorForState(new int[]{R.attr.state_hovered, R.attr.state_enabled}, -1);
        c();
    }

    public void setBoxBackgroundMode(int i) {
        if (i == this.a0) {
            return;
        }
        this.a0 = i;
        if (this.e != null) {
            k();
        }
    }

    public void setBoxCollapsedPaddingTop(int i) {
        this.b0 = i;
    }

    public void setBoxCornerFamily(int i) {
        C2986lF0 c2986lF0F = this.U.f();
        InterfaceC0484Iu interfaceC0484Iu = this.U.e;
        AbstractC1183Wg abstractC1183WgO = AbstractC1975e21.o(i);
        c2986lF0F.a = abstractC1183WgO;
        C2986lF0.b(abstractC1183WgO);
        c2986lF0F.e = interfaceC0484Iu;
        InterfaceC0484Iu interfaceC0484Iu2 = this.U.f;
        AbstractC1183Wg abstractC1183WgO2 = AbstractC1975e21.o(i);
        c2986lF0F.b = abstractC1183WgO2;
        C2986lF0.b(abstractC1183WgO2);
        c2986lF0F.f = interfaceC0484Iu2;
        InterfaceC0484Iu interfaceC0484Iu3 = this.U.h;
        AbstractC1183Wg abstractC1183WgO3 = AbstractC1975e21.o(i);
        c2986lF0F.d = abstractC1183WgO3;
        C2986lF0.b(abstractC1183WgO3);
        c2986lF0F.h = interfaceC0484Iu3;
        InterfaceC0484Iu interfaceC0484Iu4 = this.U.g;
        AbstractC1183Wg abstractC1183WgO4 = AbstractC1975e21.o(i);
        c2986lF0F.c = abstractC1183WgO4;
        C2986lF0.b(abstractC1183WgO4);
        c2986lF0F.g = interfaceC0484Iu4;
        this.U = c2986lF0F.a();
        c();
    }

    public void setBoxStrokeColor(int i) {
        if (this.v0 != i) {
            this.v0 = i;
            z();
        }
    }

    public void setBoxStrokeColorStateList(ColorStateList colorStateList) {
        if (colorStateList.isStateful()) {
            this.t0 = colorStateList.getDefaultColor();
            this.B0 = colorStateList.getColorForState(new int[]{-16842910}, -1);
            this.u0 = colorStateList.getColorForState(new int[]{R.attr.state_hovered, R.attr.state_enabled}, -1);
            this.v0 = colorStateList.getColorForState(new int[]{R.attr.state_focused, R.attr.state_enabled}, -1);
        } else if (this.v0 != colorStateList.getDefaultColor()) {
            this.v0 = colorStateList.getDefaultColor();
        }
        z();
    }

    public void setBoxStrokeErrorColor(ColorStateList colorStateList) {
        if (this.w0 != colorStateList) {
            this.w0 = colorStateList;
            z();
        }
    }

    public void setBoxStrokeWidth(int i) {
        this.d0 = i;
        z();
    }

    public void setBoxStrokeWidthFocused(int i) {
        this.e0 = i;
        z();
    }

    public void setBoxStrokeWidthFocusedResource(int i) {
        setBoxStrokeWidthFocused(getResources().getDimensionPixelSize(i));
    }

    public void setBoxStrokeWidthResource(int i) {
        setBoxStrokeWidth(getResources().getDimensionPixelSize(i));
    }

    public void setCounterEnabled(boolean z) {
        if (this.l != z) {
            OW ow = this.k;
            if (z) {
                C0755Oa c0755Oa = new C0755Oa(getContext(), null);
                this.p = c0755Oa;
                c0755Oa.setId(com.moonshot.kimiclaw.R.id.textinput_counter);
                Typeface typeface = this.k0;
                if (typeface != null) {
                    this.p.setTypeface(typeface);
                }
                this.p.setMaxLines(1);
                ow.a(this.p, 2);
                ((ViewGroup.MarginLayoutParams) this.p.getLayoutParams()).setMarginStart(getResources().getDimensionPixelOffset(com.moonshot.kimiclaw.R.dimen.mtrl_textinput_counter_margin_start));
                q();
                if (this.p != null) {
                    EditText editText = this.e;
                    p(editText != null ? editText.getText() : null);
                }
            } else {
                ow.g(this.p, 2);
                this.p = null;
            }
            this.l = z;
        }
    }

    public void setCounterMaxLength(int i) {
        if (this.m != i) {
            if (i > 0) {
                this.m = i;
            } else {
                this.m = -1;
            }
            if (!this.l || this.p == null) {
                return;
            }
            EditText editText = this.e;
            p(editText == null ? null : editText.getText());
        }
    }

    public void setCounterOverflowTextAppearance(int i) {
        if (this.q != i) {
            this.q = i;
            q();
        }
    }

    public void setCounterOverflowTextColor(ColorStateList colorStateList) {
        if (this.I != colorStateList) {
            this.I = colorStateList;
            q();
        }
    }

    public void setCounterTextAppearance(int i) {
        if (this.z != i) {
            this.z = i;
            q();
        }
    }

    public void setCounterTextColor(ColorStateList colorStateList) {
        if (this.H != colorStateList) {
            this.H = colorStateList;
            q();
        }
    }

    public void setCursorColor(ColorStateList colorStateList) {
        if (this.J != colorStateList) {
            this.J = colorStateList;
            r();
        }
    }

    public void setCursorErrorColor(ColorStateList colorStateList) {
        if (this.K != colorStateList) {
            this.K = colorStateList;
            if (o() || (this.p != null && this.n)) {
                r();
            }
        }
    }

    public void setDefaultHintTextColor(ColorStateList colorStateList) {
        this.r0 = colorStateList;
        this.s0 = colorStateList;
        if (this.e != null) {
            w(false, false);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        m(this, z);
        super.setEnabled(z);
    }

    public void setEndIconActivated(boolean z) {
        this.c.g.setActivated(z);
    }

    public void setEndIconCheckable(boolean z) {
        this.c.g.setCheckable(z);
    }

    public void setEndIconContentDescription(int i) {
        OG og = this.c;
        CharSequence text = i != 0 ? og.getResources().getText(i) : null;
        CheckableImageButton checkableImageButton = og.g;
        if (checkableImageButton.getContentDescription() != text) {
            checkableImageButton.setContentDescription(text);
        }
    }

    public void setEndIconDrawable(int i) {
        OG og = this.c;
        Drawable drawableF0 = i != 0 ? T51.f0(og.getContext(), i) : null;
        CheckableImageButton checkableImageButton = og.g;
        checkableImageButton.setImageDrawable(drawableF0);
        if (drawableF0 != null) {
            ColorStateList colorStateList = og.k;
            PorterDuff.Mode mode = og.l;
            TextInputLayout textInputLayout = og.a;
            AbstractC0923Rg.e(textInputLayout, checkableImageButton, colorStateList, mode);
            AbstractC0923Rg.J(textInputLayout, checkableImageButton, og.k);
        }
    }

    public void setEndIconMinSize(int i) {
        OG og = this.c;
        if (i < 0) {
            og.getClass();
            throw new IllegalArgumentException("endIconSize cannot be less than 0");
        }
        if (i != og.m) {
            og.m = i;
            CheckableImageButton checkableImageButton = og.g;
            checkableImageButton.setMinimumWidth(i);
            checkableImageButton.setMinimumHeight(i);
            CheckableImageButton checkableImageButton2 = og.c;
            checkableImageButton2.setMinimumWidth(i);
            checkableImageButton2.setMinimumHeight(i);
        }
    }

    public void setEndIconMode(int i) {
        this.c.g(i);
    }

    public void setEndIconOnClickListener(View.OnClickListener onClickListener) {
        OG og = this.c;
        View.OnLongClickListener onLongClickListener = og.o;
        CheckableImageButton checkableImageButton = og.g;
        checkableImageButton.setOnClickListener(onClickListener);
        AbstractC0923Rg.M(checkableImageButton, onLongClickListener);
    }

    public void setEndIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        OG og = this.c;
        og.o = onLongClickListener;
        CheckableImageButton checkableImageButton = og.g;
        checkableImageButton.setOnLongClickListener(onLongClickListener);
        AbstractC0923Rg.M(checkableImageButton, onLongClickListener);
    }

    public void setEndIconScaleType(ImageView.ScaleType scaleType) {
        OG og = this.c;
        og.n = scaleType;
        og.g.setScaleType(scaleType);
        og.c.setScaleType(scaleType);
    }

    public void setEndIconTintList(ColorStateList colorStateList) {
        OG og = this.c;
        if (og.k != colorStateList) {
            og.k = colorStateList;
            AbstractC0923Rg.e(og.a, og.g, colorStateList, og.l);
        }
    }

    public void setEndIconTintMode(PorterDuff.Mode mode) {
        OG og = this.c;
        if (og.l != mode) {
            og.l = mode;
            AbstractC0923Rg.e(og.a, og.g, og.k, mode);
        }
    }

    public void setEndIconVisible(boolean z) {
        this.c.h(z);
    }

    public void setError(CharSequence charSequence) {
        OW ow = this.k;
        if (!ow.q) {
            if (TextUtils.isEmpty(charSequence)) {
                return;
            } else {
                setErrorEnabled(true);
            }
        }
        if (TextUtils.isEmpty(charSequence)) {
            ow.f();
            return;
        }
        ow.c();
        ow.p = charSequence;
        ow.r.setText(charSequence);
        int i = ow.n;
        if (i != 1) {
            ow.o = 1;
        }
        ow.i(i, ow.o, ow.h(ow.r, charSequence));
    }

    public void setErrorAccessibilityLiveRegion(int i) {
        OW ow = this.k;
        ow.t = i;
        C0755Oa c0755Oa = ow.r;
        if (c0755Oa != null) {
            c0755Oa.setAccessibilityLiveRegion(i);
        }
    }

    public void setErrorContentDescription(CharSequence charSequence) {
        OW ow = this.k;
        ow.s = charSequence;
        C0755Oa c0755Oa = ow.r;
        if (c0755Oa != null) {
            c0755Oa.setContentDescription(charSequence);
        }
    }

    public void setErrorEnabled(boolean z) {
        OW ow = this.k;
        if (ow.q == z) {
            return;
        }
        ow.c();
        TextInputLayout textInputLayout = ow.h;
        if (z) {
            C0755Oa c0755Oa = new C0755Oa(ow.g, null);
            ow.r = c0755Oa;
            c0755Oa.setId(com.moonshot.kimiclaw.R.id.textinput_error);
            ow.r.setTextAlignment(5);
            Typeface typeface = ow.B;
            if (typeface != null) {
                ow.r.setTypeface(typeface);
            }
            int i = ow.u;
            ow.u = i;
            C0755Oa c0755Oa2 = ow.r;
            if (c0755Oa2 != null) {
                textInputLayout.n(c0755Oa2, i);
            }
            ColorStateList colorStateList = ow.v;
            ow.v = colorStateList;
            C0755Oa c0755Oa3 = ow.r;
            if (c0755Oa3 != null && colorStateList != null) {
                c0755Oa3.setTextColor(colorStateList);
            }
            CharSequence charSequence = ow.s;
            ow.s = charSequence;
            C0755Oa c0755Oa4 = ow.r;
            if (c0755Oa4 != null) {
                c0755Oa4.setContentDescription(charSequence);
            }
            int i2 = ow.t;
            ow.t = i2;
            C0755Oa c0755Oa5 = ow.r;
            if (c0755Oa5 != null) {
                c0755Oa5.setAccessibilityLiveRegion(i2);
            }
            ow.r.setVisibility(4);
            ow.a(ow.r, 0);
        } else {
            ow.f();
            ow.g(ow.r, 0);
            ow.r = null;
            textInputLayout.t();
            textInputLayout.z();
        }
        ow.q = z;
    }

    public void setErrorIconDrawable(int i) {
        OG og = this.c;
        og.i(i != 0 ? T51.f0(og.getContext(), i) : null);
        AbstractC0923Rg.J(og.a, og.c, og.d);
    }

    public void setErrorIconOnClickListener(View.OnClickListener onClickListener) {
        OG og = this.c;
        CheckableImageButton checkableImageButton = og.c;
        View.OnLongClickListener onLongClickListener = og.f;
        checkableImageButton.setOnClickListener(onClickListener);
        AbstractC0923Rg.M(checkableImageButton, onLongClickListener);
    }

    public void setErrorIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        OG og = this.c;
        og.f = onLongClickListener;
        CheckableImageButton checkableImageButton = og.c;
        checkableImageButton.setOnLongClickListener(onLongClickListener);
        AbstractC0923Rg.M(checkableImageButton, onLongClickListener);
    }

    public void setErrorIconTintList(ColorStateList colorStateList) {
        OG og = this.c;
        if (og.d != colorStateList) {
            og.d = colorStateList;
            AbstractC0923Rg.e(og.a, og.c, colorStateList, og.e);
        }
    }

    public void setErrorIconTintMode(PorterDuff.Mode mode) {
        OG og = this.c;
        if (og.e != mode) {
            og.e = mode;
            AbstractC0923Rg.e(og.a, og.c, og.d, mode);
        }
    }

    public void setErrorTextAppearance(int i) {
        OW ow = this.k;
        ow.u = i;
        C0755Oa c0755Oa = ow.r;
        if (c0755Oa != null) {
            ow.h.n(c0755Oa, i);
        }
    }

    public void setErrorTextColor(ColorStateList colorStateList) {
        OW ow = this.k;
        ow.v = colorStateList;
        C0755Oa c0755Oa = ow.r;
        if (c0755Oa == null || colorStateList == null) {
            return;
        }
        c0755Oa.setTextColor(colorStateList);
    }

    public void setExpandedHintEnabled(boolean z) {
        if (this.F0 != z) {
            this.F0 = z;
            w(false, false);
        }
    }

    public void setHelperText(CharSequence charSequence) {
        boolean zIsEmpty = TextUtils.isEmpty(charSequence);
        OW ow = this.k;
        if (zIsEmpty) {
            if (ow.x) {
                setHelperTextEnabled(false);
                return;
            }
            return;
        }
        if (!ow.x) {
            setHelperTextEnabled(true);
        }
        ow.c();
        ow.w = charSequence;
        ow.y.setText(charSequence);
        int i = ow.n;
        if (i != 2) {
            ow.o = 2;
        }
        ow.i(i, ow.o, ow.h(ow.y, charSequence));
    }

    public void setHelperTextColor(ColorStateList colorStateList) {
        OW ow = this.k;
        ow.A = colorStateList;
        C0755Oa c0755Oa = ow.y;
        if (c0755Oa == null || colorStateList == null) {
            return;
        }
        c0755Oa.setTextColor(colorStateList);
    }

    public void setHelperTextEnabled(boolean z) {
        OW ow = this.k;
        if (ow.x == z) {
            return;
        }
        ow.c();
        if (z) {
            C0755Oa c0755Oa = new C0755Oa(ow.g, null);
            ow.y = c0755Oa;
            c0755Oa.setId(com.moonshot.kimiclaw.R.id.textinput_helper_text);
            ow.y.setTextAlignment(5);
            Typeface typeface = ow.B;
            if (typeface != null) {
                ow.y.setTypeface(typeface);
            }
            ow.y.setVisibility(4);
            ow.y.setAccessibilityLiveRegion(1);
            int i = ow.z;
            ow.z = i;
            C0755Oa c0755Oa2 = ow.y;
            if (c0755Oa2 != null) {
                c0755Oa2.setTextAppearance(i);
            }
            ColorStateList colorStateList = ow.A;
            ow.A = colorStateList;
            C0755Oa c0755Oa3 = ow.y;
            if (c0755Oa3 != null && colorStateList != null) {
                c0755Oa3.setTextColor(colorStateList);
            }
            ow.a(ow.y, 1);
            ow.y.setAccessibilityDelegate(new NW(ow));
        } else {
            ow.c();
            int i2 = ow.n;
            if (i2 == 2) {
                ow.o = 0;
            }
            ow.i(i2, ow.o, ow.h(ow.y, ""));
            ow.g(ow.y, 1);
            ow.y = null;
            TextInputLayout textInputLayout = ow.h;
            textInputLayout.t();
            textInputLayout.z();
        }
        ow.x = z;
    }

    public void setHelperTextTextAppearance(int i) {
        OW ow = this.k;
        ow.z = i;
        C0755Oa c0755Oa = ow.y;
        if (c0755Oa != null) {
            c0755Oa.setTextAppearance(i);
        }
    }

    public void setHint(CharSequence charSequence) {
        if (this.L) {
            setHintInternal(charSequence);
            sendAccessibilityEvent(2048);
        }
    }

    public void setHintAnimationEnabled(boolean z) {
        this.G0 = z;
    }

    public void setHintEnabled(boolean z) {
        if (z != this.L) {
            this.L = z;
            if (z) {
                CharSequence hint = this.e.getHint();
                if (!TextUtils.isEmpty(hint)) {
                    if (TextUtils.isEmpty(this.M)) {
                        setHint(hint);
                    }
                    this.e.setHint((CharSequence) null);
                }
                this.N = true;
            } else {
                this.N = false;
                if (!TextUtils.isEmpty(this.M) && TextUtils.isEmpty(this.e.getHint())) {
                    this.e.setHint(this.M);
                }
                setHintInternal(null);
            }
            if (this.e != null) {
                v();
            }
        }
    }

    public void setHintMaxLines(int i) {
        C1041Tn c1041Tn = this.E0;
        if (i != c1041Tn.f0) {
            c1041Tn.f0 = i;
            c1041Tn.j(false);
        }
        if (i != c1041Tn.e0) {
            c1041Tn.e0 = i;
            c1041Tn.j(false);
        }
        requestLayout();
    }

    public void setHintTextAppearance(int i) {
        C1041Tn c1041Tn = this.E0;
        TextInputLayout textInputLayout = c1041Tn.a;
        YO0 yo0 = new YO0(textInputLayout.getContext(), i);
        ColorStateList colorStateList = yo0.k;
        if (colorStateList != null) {
            c1041Tn.k = colorStateList;
        }
        float f = yo0.l;
        if (f != 0.0f) {
            c1041Tn.i = f;
        }
        ColorStateList colorStateList2 = yo0.a;
        if (colorStateList2 != null) {
            c1041Tn.V = colorStateList2;
        }
        c1041Tn.T = yo0.f;
        c1041Tn.U = yo0.g;
        c1041Tn.S = yo0.h;
        c1041Tn.W = yo0.j;
        C2494hk c2494hk = c1041Tn.z;
        if (c2494hk != null) {
            c2494hk.l = true;
        }
        P21 p21 = new P21(c1041Tn, 5);
        yo0.a();
        c1041Tn.z = new C2494hk(p21, yo0.p);
        yo0.b(textInputLayout.getContext(), c1041Tn.z);
        c1041Tn.j(false);
        this.s0 = c1041Tn.k;
        if (this.e != null) {
            w(false, false);
            v();
        }
    }

    public void setHintTextColor(ColorStateList colorStateList) {
        if (this.s0 != colorStateList) {
            if (this.r0 == null) {
                C1041Tn c1041Tn = this.E0;
                if (c1041Tn.k != colorStateList) {
                    c1041Tn.k = colorStateList;
                    c1041Tn.j(false);
                }
            }
            this.s0 = colorStateList;
            if (this.e != null) {
                w(false, false);
            }
        }
    }

    public void setLengthCounter(InterfaceC4687xQ0 interfaceC4687xQ0) {
        this.o = interfaceC4687xQ0;
    }

    public void setMaxEms(int i) {
        this.h = i;
        EditText editText = this.e;
        if (editText == null || i == -1) {
            return;
        }
        editText.setMaxEms(i);
    }

    public void setMaxWidth(int i) {
        this.j = i;
        EditText editText = this.e;
        if (editText == null || i == -1) {
            return;
        }
        editText.setMaxWidth(i);
    }

    public void setMaxWidthResource(int i) {
        setMaxWidth(getContext().getResources().getDimensionPixelSize(i));
    }

    public void setMinEms(int i) {
        this.g = i;
        EditText editText = this.e;
        if (editText == null || i == -1) {
            return;
        }
        editText.setMinEms(i);
    }

    public void setMinWidth(int i) {
        this.i = i;
        EditText editText = this.e;
        if (editText == null || i == -1) {
            return;
        }
        editText.setMinWidth(i);
    }

    public void setMinWidthResource(int i) {
        setMinWidth(getContext().getResources().getDimensionPixelSize(i));
    }

    @Deprecated
    public void setPasswordVisibilityToggleContentDescription(int i) {
        OG og = this.c;
        og.g.setContentDescription(i != 0 ? og.getResources().getText(i) : null);
    }

    @Deprecated
    public void setPasswordVisibilityToggleDrawable(int i) {
        OG og = this.c;
        og.g.setImageDrawable(i != 0 ? T51.f0(og.getContext(), i) : null);
    }

    @Deprecated
    public void setPasswordVisibilityToggleEnabled(boolean z) {
        OG og = this.c;
        if (z && og.i != 1) {
            og.g(1);
        } else if (z) {
            og.getClass();
        } else {
            og.g(0);
        }
    }

    @Deprecated
    public void setPasswordVisibilityToggleTintList(ColorStateList colorStateList) {
        OG og = this.c;
        og.k = colorStateList;
        AbstractC0923Rg.e(og.a, og.g, colorStateList, og.l);
    }

    @Deprecated
    public void setPasswordVisibilityToggleTintMode(PorterDuff.Mode mode) {
        OG og = this.c;
        og.l = mode;
        AbstractC0923Rg.e(og.a, og.g, og.k, mode);
    }

    public void setPlaceholderText(CharSequence charSequence) {
        if (this.C == null) {
            C0755Oa c0755Oa = new C0755Oa(getContext(), null);
            this.C = c0755Oa;
            c0755Oa.setId(com.moonshot.kimiclaw.R.id.textinput_placeholder);
            this.C.setImportantForAccessibility(1);
            this.C.setAccessibilityLiveRegion(1);
            C3273nJ c3273nJF = f();
            this.F = c3273nJF;
            c3273nJF.b = 67L;
            this.G = f();
            setPlaceholderTextAppearance(this.E);
            setPlaceholderTextColor(this.D);
            AbstractC4421vX0.q(this.C, new XD(5));
        }
        if (TextUtils.isEmpty(charSequence)) {
            setPlaceholderTextEnabled(false);
        } else {
            if (!this.B) {
                setPlaceholderTextEnabled(true);
            }
            this.A = charSequence;
        }
        EditText editText = this.e;
        x(editText != null ? editText.getText() : null);
    }

    public void setPlaceholderTextAppearance(int i) {
        this.E = i;
        C0755Oa c0755Oa = this.C;
        if (c0755Oa != null) {
            c0755Oa.setTextAppearance(i);
        }
    }

    public void setPlaceholderTextColor(ColorStateList colorStateList) {
        if (this.D != colorStateList) {
            this.D = colorStateList;
            C0755Oa c0755Oa = this.C;
            if (c0755Oa == null || colorStateList == null) {
                return;
            }
            c0755Oa.setTextColor(colorStateList);
        }
    }

    public void setPrefixText(CharSequence charSequence) {
        CJ0 cj0 = this.b;
        cj0.getClass();
        cj0.c = TextUtils.isEmpty(charSequence) ? null : charSequence;
        cj0.b.setText(charSequence);
        cj0.e();
    }

    public void setPrefixTextAppearance(int i) {
        this.b.b.setTextAppearance(i);
    }

    public void setPrefixTextColor(ColorStateList colorStateList) {
        this.b.b.setTextColor(colorStateList);
    }

    public void setShapeAppearanceModel(C3126mF0 c3126mF0) {
        C3168ma0 c3168ma0 = this.O;
        if (c3168ma0 == null || c3168ma0.b.a == c3126mF0) {
            return;
        }
        this.U = c3126mF0;
        c();
    }

    public void setStartIconCheckable(boolean z) {
        this.b.d.setCheckable(z);
    }

    public void setStartIconContentDescription(CharSequence charSequence) {
        CheckableImageButton checkableImageButton = this.b.d;
        if (checkableImageButton.getContentDescription() != charSequence) {
            checkableImageButton.setContentDescription(charSequence);
        }
    }

    public void setStartIconDrawable(int i) {
        setStartIconDrawable(i != 0 ? T51.f0(getContext(), i) : null);
    }

    public void setStartIconMinSize(int i) {
        CJ0 cj0 = this.b;
        if (i < 0) {
            cj0.getClass();
            throw new IllegalArgumentException("startIconSize cannot be less than 0");
        }
        if (i != cj0.g) {
            cj0.g = i;
            CheckableImageButton checkableImageButton = cj0.d;
            checkableImageButton.setMinimumWidth(i);
            checkableImageButton.setMinimumHeight(i);
        }
    }

    public void setStartIconOnClickListener(View.OnClickListener onClickListener) {
        CJ0 cj0 = this.b;
        View.OnLongClickListener onLongClickListener = cj0.i;
        CheckableImageButton checkableImageButton = cj0.d;
        checkableImageButton.setOnClickListener(onClickListener);
        AbstractC0923Rg.M(checkableImageButton, onLongClickListener);
    }

    public void setStartIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        CJ0 cj0 = this.b;
        cj0.i = onLongClickListener;
        CheckableImageButton checkableImageButton = cj0.d;
        checkableImageButton.setOnLongClickListener(onLongClickListener);
        AbstractC0923Rg.M(checkableImageButton, onLongClickListener);
    }

    public void setStartIconScaleType(ImageView.ScaleType scaleType) {
        CJ0 cj0 = this.b;
        cj0.h = scaleType;
        cj0.d.setScaleType(scaleType);
    }

    public void setStartIconTintList(ColorStateList colorStateList) {
        CJ0 cj0 = this.b;
        if (cj0.e != colorStateList) {
            cj0.e = colorStateList;
            AbstractC0923Rg.e(cj0.a, cj0.d, colorStateList, cj0.f);
        }
    }

    public void setStartIconTintMode(PorterDuff.Mode mode) {
        CJ0 cj0 = this.b;
        if (cj0.f != mode) {
            cj0.f = mode;
            AbstractC0923Rg.e(cj0.a, cj0.d, cj0.e, mode);
        }
    }

    public void setStartIconVisible(boolean z) {
        this.b.c(z);
    }

    public void setSuffixText(CharSequence charSequence) {
        OG og = this.c;
        og.getClass();
        og.p = TextUtils.isEmpty(charSequence) ? null : charSequence;
        og.q.setText(charSequence);
        og.n();
    }

    public void setSuffixTextAppearance(int i) {
        this.c.q.setTextAppearance(i);
    }

    public void setSuffixTextColor(ColorStateList colorStateList) {
        this.c.q.setTextColor(colorStateList);
    }

    public void setTextInputAccessibilityDelegate(C4547wQ0 c4547wQ0) {
        EditText editText = this.e;
        if (editText != null) {
            AbstractC4421vX0.q(editText, c4547wQ0);
        }
    }

    public void setTypeface(Typeface typeface) {
        if (typeface != this.k0) {
            this.k0 = typeface;
            this.E0.n(typeface);
            OW ow = this.k;
            if (typeface != ow.B) {
                ow.B = typeface;
                C0755Oa c0755Oa = ow.r;
                if (c0755Oa != null) {
                    c0755Oa.setTypeface(typeface);
                }
                C0755Oa c0755Oa2 = ow.y;
                if (c0755Oa2 != null) {
                    c0755Oa2.setTypeface(typeface);
                }
            }
            C0755Oa c0755Oa3 = this.p;
            if (c0755Oa3 != null) {
                c0755Oa3.setTypeface(typeface);
            }
        }
    }

    public final void t() {
        Drawable background;
        C0755Oa c0755Oa;
        EditText editText = this.e;
        if (editText == null || this.a0 != 0 || (background = editText.getBackground()) == null) {
            return;
        }
        int[] iArr = UD.a;
        Drawable drawableMutate = background.mutate();
        if (o()) {
            drawableMutate.setColorFilter(C2329ga.c(getErrorCurrentTextColors(), PorterDuff.Mode.SRC_IN));
        } else if (this.n && (c0755Oa = this.p) != null) {
            drawableMutate.setColorFilter(C2329ga.c(c0755Oa.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
        } else {
            drawableMutate.clearColorFilter();
            this.e.refreshDrawableState();
        }
    }

    public final void u() {
        EditText editText = this.e;
        if (editText == null || this.O == null) {
            return;
        }
        if ((this.R || editText.getBackground() == null) && this.a0 != 0) {
            this.e.setBackground(getEditTextBoxBackground());
            this.R = true;
        }
    }

    public final void v() {
        if (this.a0 != 1) {
            FrameLayout frameLayout = this.a;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
            int iE = e();
            if (iE != layoutParams.topMargin) {
                layoutParams.topMargin = iE;
                frameLayout.requestLayout();
            }
        }
    }

    public final void w(boolean z, boolean z2) {
        ColorStateList colorStateList;
        C0755Oa c0755Oa;
        boolean zIsEnabled = isEnabled();
        EditText editText = this.e;
        boolean z3 = (editText == null || TextUtils.isEmpty(editText.getText())) ? false : true;
        EditText editText2 = this.e;
        boolean z4 = editText2 != null && editText2.hasFocus();
        ColorStateList colorStateList2 = this.r0;
        C1041Tn c1041Tn = this.E0;
        if (colorStateList2 != null) {
            c1041Tn.k(colorStateList2);
        }
        if (!zIsEnabled) {
            ColorStateList colorStateList3 = this.r0;
            c1041Tn.k(ColorStateList.valueOf(colorStateList3 != null ? colorStateList3.getColorForState(new int[]{-16842910}, this.B0) : this.B0));
        } else if (o()) {
            C0755Oa c0755Oa2 = this.k.r;
            c1041Tn.k(c0755Oa2 != null ? c0755Oa2.getTextColors() : null);
        } else if (this.n && (c0755Oa = this.p) != null) {
            c1041Tn.k(c0755Oa.getTextColors());
        } else if (z4 && (colorStateList = this.s0) != null && c1041Tn.k != colorStateList) {
            c1041Tn.k = colorStateList;
            c1041Tn.j(false);
        }
        OG og = this.c;
        CJ0 cj0 = this.b;
        if (z3 || !this.F0 || (isEnabled() && z4)) {
            if (z2 || this.D0) {
                ValueAnimator valueAnimator = this.H0;
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    this.H0.cancel();
                }
                if (z && this.G0) {
                    b(1.0f);
                } else {
                    c1041Tn.m(1.0f);
                }
                this.D0 = false;
                if (g()) {
                    l();
                }
                EditText editText3 = this.e;
                x(editText3 != null ? editText3.getText() : null);
                cj0.j = false;
                cj0.e();
                og.z = false;
                og.n();
                return;
            }
            return;
        }
        if (z2 || !this.D0) {
            ValueAnimator valueAnimator2 = this.H0;
            if (valueAnimator2 != null && valueAnimator2.isRunning()) {
                this.H0.cancel();
            }
            if (z && this.G0) {
                b(0.0f);
            } else {
                c1041Tn.m(0.0f);
            }
            if (g() && !((C0070Av) this.O).O.r.isEmpty() && g()) {
                ((C0070Av) this.O).t(0.0f, 0.0f, 0.0f, 0.0f);
            }
            this.D0 = true;
            C0755Oa c0755Oa3 = this.C;
            if (c0755Oa3 != null && this.B) {
                c0755Oa3.setText((CharSequence) null);
                AbstractC3154mT0.a(this.a, this.G);
                this.C.setVisibility(4);
            }
            cj0.j = true;
            cj0.e();
            og.z = true;
            og.n();
        }
    }

    public final void x(Editable editable) {
        ((C3781qy0) this.o).getClass();
        int length = editable != null ? editable.length() : 0;
        FrameLayout frameLayout = this.a;
        if (length != 0 || this.D0) {
            C0755Oa c0755Oa = this.C;
            if (c0755Oa == null || !this.B) {
                return;
            }
            c0755Oa.setText((CharSequence) null);
            AbstractC3154mT0.a(frameLayout, this.G);
            this.C.setVisibility(4);
            return;
        }
        if (this.C == null || !this.B || TextUtils.isEmpty(this.A)) {
            return;
        }
        this.C.setText(this.A);
        AbstractC3154mT0.a(frameLayout, this.F);
        this.C.setVisibility(0);
        this.C.bringToFront();
    }

    public final void y(boolean z, boolean z2) {
        int defaultColor = this.w0.getDefaultColor();
        int colorForState = this.w0.getColorForState(new int[]{R.attr.state_hovered, R.attr.state_enabled}, defaultColor);
        int colorForState2 = this.w0.getColorForState(new int[]{R.attr.state_activated, R.attr.state_enabled}, defaultColor);
        if (z) {
            this.f0 = colorForState2;
        } else if (z2) {
            this.f0 = colorForState;
        } else {
            this.f0 = defaultColor;
        }
    }

    public final void z() {
        C0755Oa c0755Oa;
        EditText editText;
        EditText editText2;
        if (this.O == null || this.a0 == 0) {
            return;
        }
        boolean z = false;
        boolean z2 = isFocused() || ((editText2 = this.e) != null && editText2.hasFocus());
        if (isHovered() || ((editText = this.e) != null && editText.isHovered())) {
            z = true;
        }
        if (!isEnabled()) {
            this.f0 = this.B0;
        } else if (o()) {
            if (this.w0 != null) {
                y(z2, z);
            } else {
                this.f0 = getErrorCurrentTextColors();
            }
        } else if (!this.n || (c0755Oa = this.p) == null) {
            if (z2) {
                this.f0 = this.v0;
            } else if (z) {
                this.f0 = this.u0;
            } else {
                this.f0 = this.t0;
            }
        } else if (this.w0 != null) {
            y(z2, z);
        } else {
            this.f0 = c0755Oa.getCurrentTextColor();
        }
        if (Build.VERSION.SDK_INT >= 29) {
            r();
        }
        OG og = this.c;
        og.l();
        CheckableImageButton checkableImageButton = og.c;
        ColorStateList colorStateList = og.d;
        TextInputLayout textInputLayout = og.a;
        AbstractC0923Rg.J(textInputLayout, checkableImageButton, colorStateList);
        ColorStateList colorStateList2 = og.k;
        CheckableImageButton checkableImageButton2 = og.g;
        AbstractC0923Rg.J(textInputLayout, checkableImageButton2, colorStateList2);
        if (og.b() instanceof BE) {
            if (!textInputLayout.o() || checkableImageButton2.getDrawable() == null) {
                AbstractC0923Rg.e(textInputLayout, checkableImageButton2, og.k, og.l);
            } else {
                Drawable drawableMutate = checkableImageButton2.getDrawable().mutate();
                drawableMutate.setTint(textInputLayout.getErrorCurrentTextColors());
                checkableImageButton2.setImageDrawable(drawableMutate);
            }
        }
        CJ0 cj0 = this.b;
        AbstractC0923Rg.J(cj0.a, cj0.d, cj0.e);
        if (this.a0 == 2) {
            int i = this.c0;
            if (z2 && isEnabled()) {
                this.c0 = this.e0;
            } else {
                this.c0 = this.d0;
            }
            if (this.c0 != i && g() && !this.D0) {
                if (g()) {
                    ((C0070Av) this.O).t(0.0f, 0.0f, 0.0f, 0.0f);
                }
                l();
            }
        }
        if (this.a0 == 1) {
            if (!isEnabled()) {
                this.g0 = this.y0;
            } else if (z && !z2) {
                this.g0 = this.A0;
            } else if (z2) {
                this.g0 = this.z0;
            } else {
                this.g0 = this.x0;
            }
        }
        c();
    }

    public void setStartIconDrawable(Drawable drawable) {
        this.b.b(drawable);
    }

    public void setHint(int i) {
        setHint(i != 0 ? getResources().getText(i) : null);
    }

    public void setStartIconContentDescription(int i) {
        setStartIconContentDescription(i != 0 ? getResources().getText(i) : null);
    }

    @Deprecated
    public void setPasswordVisibilityToggleContentDescription(CharSequence charSequence) {
        this.c.g.setContentDescription(charSequence);
    }

    @Deprecated
    public void setPasswordVisibilityToggleDrawable(Drawable drawable) {
        this.c.g.setImageDrawable(drawable);
    }

    public void setErrorIconDrawable(Drawable drawable) {
        this.c.i(drawable);
    }

    public void setEndIconContentDescription(CharSequence charSequence) {
        CheckableImageButton checkableImageButton = this.c.g;
        if (checkableImageButton.getContentDescription() != charSequence) {
            checkableImageButton.setContentDescription(charSequence);
        }
    }

    public void setEndIconDrawable(Drawable drawable) {
        OG og = this.c;
        CheckableImageButton checkableImageButton = og.g;
        checkableImageButton.setImageDrawable(drawable);
        if (drawable != null) {
            ColorStateList colorStateList = og.k;
            PorterDuff.Mode mode = og.l;
            TextInputLayout textInputLayout = og.a;
            AbstractC0923Rg.e(textInputLayout, checkableImageButton, colorStateList, mode);
            AbstractC0923Rg.J(textInputLayout, checkableImageButton, og.k);
        }
    }
}
