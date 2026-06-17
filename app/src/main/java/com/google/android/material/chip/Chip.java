package com.google.android.material.chip;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.TextView;
import defpackage.AP0;
import defpackage.AbstractC0545Jy0;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC1975e21;
import defpackage.AbstractC2917km0;
import defpackage.AbstractC4123tO0;
import defpackage.AbstractC4421vX0;
import defpackage.AbstractC4635x31;
import defpackage.C0715Ng;
import defpackage.C1479ag;
import defpackage.C3126mF0;
import defpackage.C4175tm;
import defpackage.C4315um;
import defpackage.C4455vm;
import defpackage.C4735xm;
import defpackage.C4856yd0;
import defpackage.DP0;
import defpackage.InterfaceC2470ha0;
import defpackage.InterfaceC4595wm;
import defpackage.InterfaceC4945zF0;
import defpackage.K9;
import defpackage.P11;
import defpackage.T51;
import defpackage.YO0;
import java.lang.ref.WeakReference;
import java.util.Locale;
import kotlin.jvm.internal.IntCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public class Chip extends K9 implements InterfaceC4595wm, InterfaceC4945zF0, Checkable {
    public static final Rect E = new Rect();
    public static final int[] F = {R.attr.state_selected};
    public static final int[] G = {R.attr.state_checkable};
    public boolean A;
    public final Rect B;
    public final RectF C;
    public final C4175tm D;
    public C4735xm e;
    public InsetDrawable f;
    public RippleDrawable g;
    public View.OnClickListener h;
    public CompoundButton.OnCheckedChangeListener i;
    public boolean j;
    public boolean k;
    public boolean l;
    public boolean m;
    public boolean n;
    public int o;
    public int p;
    public CharSequence q;
    public final C4455vm z;

    public Chip(Context context, AttributeSet attributeSet) {
        int resourceId;
        int resourceId2;
        int resourceId3;
        super(AbstractC4123tO0.f0(context, attributeSet, com.moonshot.kimiclaw.R.attr.chipStyle, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Chip_Action), attributeSet, com.moonshot.kimiclaw.R.attr.chipStyle);
        this.B = new Rect();
        this.C = new RectF();
        this.D = new C4175tm(this, 0);
        Context context2 = getContext();
        if (attributeSet != null) {
            if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "background") != null) {
                Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
            }
            if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableLeft") != null) {
                throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
            }
            if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableStart") != null) {
                throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
            }
            if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableEnd") != null) {
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            }
            if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableRight") != null) {
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            }
            if (!attributeSet.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "singleLine", true) || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "lines", 1) != 1 || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "minLines", 1) != 1 || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLines", 1) != 1) {
                throw new UnsupportedOperationException("Chip does not support multi-line text");
            }
            if (attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "gravity", 8388627) != 8388627) {
                Log.w("Chip", "Chip text must be vertically center and start aligned");
            }
        }
        C4735xm c4735xm = new C4735xm(context2, attributeSet);
        int[] iArr = AbstractC1264Xu0.c;
        TypedArray typedArrayB0 = AbstractC4635x31.b0(c4735xm.v0, attributeSet, iArr, com.moonshot.kimiclaw.R.attr.chipStyle, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Chip_Action, new int[0]);
        c4735xm.V0 = typedArrayB0.hasValue(37);
        Context context3 = c4735xm.v0;
        ColorStateList colorStateListJ = AbstractC4123tO0.J(context3, typedArrayB0, 24);
        if (c4735xm.O != colorStateListJ) {
            c4735xm.O = colorStateListJ;
            c4735xm.onStateChange(c4735xm.getState());
        }
        ColorStateList colorStateListJ2 = AbstractC4123tO0.J(context3, typedArrayB0, 11);
        if (c4735xm.P != colorStateListJ2) {
            c4735xm.P = colorStateListJ2;
            c4735xm.onStateChange(c4735xm.getState());
        }
        float dimension = typedArrayB0.getDimension(19, 0.0f);
        if (c4735xm.Q != dimension) {
            c4735xm.Q = dimension;
            c4735xm.invalidateSelf();
            c4735xm.A();
        }
        if (typedArrayB0.hasValue(12)) {
            c4735xm.G(typedArrayB0.getDimension(12, 0.0f));
        }
        c4735xm.L(AbstractC4123tO0.J(context3, typedArrayB0, 22));
        c4735xm.M(typedArrayB0.getDimension(23, 0.0f));
        c4735xm.W(AbstractC4123tO0.J(context3, typedArrayB0, 36));
        String text = typedArrayB0.getText(5);
        text = text == null ? "" : text;
        if (!TextUtils.equals(c4735xm.V, text)) {
            c4735xm.V = text;
            c4735xm.B0.d = true;
            c4735xm.invalidateSelf();
            c4735xm.A();
        }
        YO0 yo0 = (!typedArrayB0.hasValue(0) || (resourceId3 = typedArrayB0.getResourceId(0, 0)) == 0) ? null : new YO0(context3, resourceId3);
        yo0.l = typedArrayB0.getDimension(1, yo0.l);
        c4735xm.X(yo0);
        int i = typedArrayB0.getInt(3, 0);
        if (i == 1) {
            c4735xm.S0 = TextUtils.TruncateAt.START;
        } else if (i == 2) {
            c4735xm.S0 = TextUtils.TruncateAt.MIDDLE;
        } else if (i == 3) {
            c4735xm.S0 = TextUtils.TruncateAt.END;
        }
        c4735xm.K(typedArrayB0.getBoolean(18, false));
        if (attributeSet != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconEnabled") != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconVisible") == null) {
            c4735xm.K(typedArrayB0.getBoolean(15, false));
        }
        c4735xm.H(AbstractC4123tO0.K(context3, typedArrayB0, 14));
        if (typedArrayB0.hasValue(17)) {
            c4735xm.J(AbstractC4123tO0.J(context3, typedArrayB0, 17));
        }
        c4735xm.I(typedArrayB0.getDimension(16, -1.0f));
        c4735xm.T(typedArrayB0.getBoolean(31, false));
        if (attributeSet != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconEnabled") != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconVisible") == null) {
            c4735xm.T(typedArrayB0.getBoolean(26, false));
        }
        c4735xm.N(AbstractC4123tO0.K(context3, typedArrayB0, 25));
        c4735xm.S(AbstractC4123tO0.J(context3, typedArrayB0, 30));
        c4735xm.P(typedArrayB0.getDimension(28, 0.0f));
        c4735xm.C(typedArrayB0.getBoolean(6, false));
        c4735xm.F(typedArrayB0.getBoolean(10, false));
        if (attributeSet != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconEnabled") != null && attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconVisible") == null) {
            c4735xm.F(typedArrayB0.getBoolean(8, false));
        }
        c4735xm.D(AbstractC4123tO0.K(context3, typedArrayB0, 7));
        if (typedArrayB0.hasValue(9)) {
            c4735xm.E(AbstractC4123tO0.J(context3, typedArrayB0, 9));
        }
        c4735xm.l0 = (!typedArrayB0.hasValue(39) || (resourceId2 = typedArrayB0.getResourceId(39, 0)) == 0) ? null : C4856yd0.a(context3, resourceId2);
        c4735xm.m0 = (!typedArrayB0.hasValue(33) || (resourceId = typedArrayB0.getResourceId(33, 0)) == 0) ? null : C4856yd0.a(context3, resourceId);
        float dimension2 = typedArrayB0.getDimension(21, 0.0f);
        if (c4735xm.n0 != dimension2) {
            c4735xm.n0 = dimension2;
            c4735xm.invalidateSelf();
            c4735xm.A();
        }
        c4735xm.V(typedArrayB0.getDimension(35, 0.0f));
        c4735xm.U(typedArrayB0.getDimension(34, 0.0f));
        float dimension3 = typedArrayB0.getDimension(41, 0.0f);
        if (c4735xm.q0 != dimension3) {
            c4735xm.q0 = dimension3;
            c4735xm.invalidateSelf();
            c4735xm.A();
        }
        float dimension4 = typedArrayB0.getDimension(40, 0.0f);
        if (c4735xm.r0 != dimension4) {
            c4735xm.r0 = dimension4;
            c4735xm.invalidateSelf();
            c4735xm.A();
        }
        c4735xm.Q(typedArrayB0.getDimension(29, 0.0f));
        c4735xm.O(typedArrayB0.getDimension(27, 0.0f));
        float dimension5 = typedArrayB0.getDimension(13, 0.0f);
        if (c4735xm.u0 != dimension5) {
            c4735xm.u0 = dimension5;
            c4735xm.invalidateSelf();
            c4735xm.A();
        }
        c4735xm.U0 = typedArrayB0.getDimensionPixelSize(4, IntCompanionObject.MAX_VALUE);
        typedArrayB0.recycle();
        AbstractC4635x31.y(context2, attributeSet, com.moonshot.kimiclaw.R.attr.chipStyle, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Chip_Action);
        AbstractC4635x31.B(context2, attributeSet, iArr, com.moonshot.kimiclaw.R.attr.chipStyle, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Chip_Action, new int[0]);
        TypedArray typedArrayObtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, iArr, com.moonshot.kimiclaw.R.attr.chipStyle, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Chip_Action);
        this.n = typedArrayObtainStyledAttributes.getBoolean(32, false);
        TypedValue typedValueW = AbstractC2917km0.W(context2, com.moonshot.kimiclaw.R.attr.minTouchTargetSize);
        this.p = (int) Math.ceil(typedArrayObtainStyledAttributes.getDimension(20, (int) ((typedValueW == null || typedValueW.type != 5) ? context2.getResources().getDimension(com.moonshot.kimiclaw.R.dimen.mtrl_min_touch_target_size) : typedValueW.getDimension(context2.getResources().getDisplayMetrics()))));
        typedArrayObtainStyledAttributes.recycle();
        setChipDrawable(c4735xm);
        c4735xm.m(getElevation());
        AbstractC4635x31.y(context2, attributeSet, com.moonshot.kimiclaw.R.attr.chipStyle, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Chip_Action);
        AbstractC4635x31.B(context2, attributeSet, iArr, com.moonshot.kimiclaw.R.attr.chipStyle, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Chip_Action, new int[0]);
        TypedArray typedArrayObtainStyledAttributes2 = context2.obtainStyledAttributes(attributeSet, iArr, com.moonshot.kimiclaw.R.attr.chipStyle, com.moonshot.kimiclaw.R.style.Widget_MaterialComponents_Chip_Action);
        boolean zHasValue = typedArrayObtainStyledAttributes2.hasValue(37);
        typedArrayObtainStyledAttributes2.recycle();
        this.z = new C4455vm(this, this);
        d();
        if (!zHasValue) {
            setOutlineProvider(new C4315um(this));
        }
        setChecked(this.j);
        setText(c4735xm.V);
        setEllipsize(c4735xm.S0);
        g();
        if (!this.e.T0) {
            setLines(1);
            setHorizontallyScrolling(true);
        }
        setGravity(8388627);
        f();
        if (this.n) {
            setMinHeight(this.p);
        }
        this.o = getLayoutDirection();
        super.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: sm
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                CompoundButton.OnCheckedChangeListener onCheckedChangeListener = this.a.i;
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(compoundButton, z);
                }
            }
        });
    }

    private RectF getCloseIconTouchBounds() {
        RectF rectF = this.C;
        rectF.setEmpty();
        if (c() && this.h != null) {
            C4735xm c4735xm = this.e;
            Rect bounds = c4735xm.getBounds();
            rectF.setEmpty();
            if (c4735xm.a0()) {
                float f = c4735xm.u0 + c4735xm.t0 + c4735xm.f0 + c4735xm.s0 + c4735xm.r0;
                if (c4735xm.getLayoutDirection() == 0) {
                    float f2 = bounds.right;
                    rectF.right = f2;
                    rectF.left = f2 - f;
                } else {
                    float f3 = bounds.left;
                    rectF.left = f3;
                    rectF.right = f3 + f;
                }
                rectF.top = bounds.top;
                rectF.bottom = bounds.bottom;
            }
        }
        return rectF;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getCloseIconTouchBoundsInt() {
        RectF closeIconTouchBounds = getCloseIconTouchBounds();
        int i = (int) closeIconTouchBounds.left;
        int i2 = (int) closeIconTouchBounds.top;
        int i3 = (int) closeIconTouchBounds.right;
        int i4 = (int) closeIconTouchBounds.bottom;
        Rect rect = this.B;
        rect.set(i, i2, i3, i4);
        return rect;
    }

    private YO0 getTextAppearance() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.B0.f;
        }
        return null;
    }

    private void setCloseIconHovered(boolean z) {
        if (this.l != z) {
            this.l = z;
            refreshDrawableState();
        }
    }

    private void setCloseIconPressed(boolean z) {
        if (this.k != z) {
            this.k = z;
            refreshDrawableState();
        }
    }

    public final void b(int i) {
        this.p = i;
        if (!this.n) {
            InsetDrawable insetDrawable = this.f;
            if (insetDrawable == null) {
                e();
                return;
            } else {
                if (insetDrawable != null) {
                    this.f = null;
                    setMinWidth(0);
                    setMinHeight((int) getChipMinHeight());
                    e();
                    return;
                }
                return;
            }
        }
        int iMax = Math.max(0, i - ((int) this.e.Q));
        int iMax2 = Math.max(0, i - this.e.getIntrinsicWidth());
        if (iMax2 <= 0 && iMax <= 0) {
            InsetDrawable insetDrawable2 = this.f;
            if (insetDrawable2 == null) {
                e();
                return;
            } else {
                if (insetDrawable2 != null) {
                    this.f = null;
                    setMinWidth(0);
                    setMinHeight((int) getChipMinHeight());
                    e();
                    return;
                }
                return;
            }
        }
        int i2 = iMax2 > 0 ? iMax2 / 2 : 0;
        int i3 = iMax > 0 ? iMax / 2 : 0;
        if (this.f != null) {
            Rect rect = new Rect();
            this.f.getPadding(rect);
            if (rect.top == i3 && rect.bottom == i3 && rect.left == i2 && rect.right == i2) {
                e();
                return;
            }
        }
        if (getMinHeight() != i) {
            setMinHeight(i);
        }
        if (getMinWidth() != i) {
            setMinWidth(i);
        }
        this.f = new InsetDrawable((Drawable) this.e, i2, i3, i2, i3);
        e();
    }

    public final boolean c() {
        C4735xm c4735xm = this.e;
        if (c4735xm == null) {
            return false;
        }
        Object obj = c4735xm.c0;
        if (obj == null) {
            obj = null;
        } else if (obj instanceof P11) {
            obj = null;
        }
        return obj != null;
    }

    public final void d() {
        C4735xm c4735xm;
        if (!c() || (c4735xm = this.e) == null || !c4735xm.b0 || this.h == null) {
            AbstractC4421vX0.q(this, null);
            this.A = false;
        } else {
            AbstractC4421vX0.q(this, this.z);
            this.A = true;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x006c  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean dispatchHoverEvent(MotionEvent motionEvent) {
        int i;
        if (!this.A) {
            return super.dispatchHoverEvent(motionEvent);
        }
        C4455vm c4455vm = this.z;
        AccessibilityManager accessibilityManager = c4455vm.h;
        if (accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled()) {
            int action = motionEvent.getAction();
            if (action == 7 || action == 9) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                Chip chip = c4455vm.n;
                int i2 = (chip.c() && chip.getCloseIconTouchBounds().contains(x, y)) ? 1 : 0;
                int i3 = c4455vm.m;
                if (i3 != i2) {
                    c4455vm.m = i2;
                    c4455vm.r(i2, 128);
                    c4455vm.r(i3, 256);
                }
                if (i2 == Integer.MIN_VALUE) {
                }
            } else if (action == 10 && (i = c4455vm.m) != Integer.MIN_VALUE) {
                if (i != Integer.MIN_VALUE) {
                    c4455vm.m = IntCompanionObject.MIN_VALUE;
                    c4455vm.r(i, 256);
                    return true;
                }
            }
        } else if (!super.dispatchHoverEvent(motionEvent)) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0058  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!this.A) {
            return super.dispatchKeyEvent(keyEvent);
        }
        C4455vm c4455vm = this.z;
        c4455vm.getClass();
        boolean zM = false;
        int i = 0;
        zM = false;
        zM = false;
        zM = false;
        zM = false;
        zM = false;
        if (keyEvent.getAction() != 1) {
            int keyCode = keyEvent.getKeyCode();
            if (keyCode != 61) {
                int i2 = 66;
                if (keyCode != 66) {
                    switch (keyCode) {
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                            if (keyEvent.hasNoModifiers()) {
                                if (keyCode == 19) {
                                    i2 = 33;
                                } else if (keyCode == 21) {
                                    i2 = 17;
                                } else if (keyCode != 22) {
                                    i2 = 130;
                                }
                                int repeatCount = keyEvent.getRepeatCount() + 1;
                                boolean z = false;
                                while (i < repeatCount && c4455vm.m(i2, null)) {
                                    i++;
                                    z = true;
                                }
                                zM = z;
                            }
                            break;
                        case 23:
                            if (keyEvent.hasNoModifiers() && keyEvent.getRepeatCount() == 0) {
                                int i3 = c4455vm.l;
                                if (i3 != Integer.MIN_VALUE) {
                                    Chip chip = c4455vm.n;
                                    if (i3 == 0) {
                                        chip.performClick();
                                    } else if (i3 == 1) {
                                        chip.playSoundEffect(0);
                                        View.OnClickListener onClickListener = chip.h;
                                        if (onClickListener != null) {
                                            onClickListener.onClick(chip);
                                        }
                                        if (chip.A) {
                                            chip.z.r(1, 1);
                                        }
                                    }
                                }
                                zM = true;
                            }
                            break;
                    }
                }
            } else if (keyEvent.hasNoModifiers()) {
                zM = c4455vm.m(2, null);
            } else if (keyEvent.hasModifiers(1)) {
                zM = c4455vm.m(1, null);
            }
        }
        if (!zM || c4455vm.l == Integer.MIN_VALUE) {
            return super.dispatchKeyEvent(keyEvent);
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [boolean, int] */
    @Override // defpackage.K9, android.widget.CompoundButton, android.widget.TextView, android.view.View
    public final void drawableStateChanged() {
        super.drawableStateChanged();
        C4735xm c4735xm = this.e;
        boolean zR = false;
        int i = 0;
        zR = false;
        if (c4735xm != null && C4735xm.z(c4735xm.c0)) {
            C4735xm c4735xm2 = this.e;
            ?? IsEnabled = isEnabled();
            int i2 = IsEnabled;
            if (this.m) {
                i2 = IsEnabled + 1;
            }
            int i3 = i2;
            if (this.l) {
                i3 = i2 + 1;
            }
            int i4 = i3;
            if (this.k) {
                i4 = i3 + 1;
            }
            int i5 = i4;
            if (isChecked()) {
                i5 = i4 + 1;
            }
            int[] iArr = new int[i5];
            if (isEnabled()) {
                iArr[0] = 16842910;
                i = 1;
            }
            if (this.m) {
                iArr[i] = 16842908;
                i++;
            }
            if (this.l) {
                iArr[i] = 16843623;
                i++;
            }
            if (this.k) {
                iArr[i] = 16842919;
                i++;
            }
            if (isChecked()) {
                iArr[i] = 16842913;
            }
            zR = c4735xm2.R(iArr);
        }
        if (zR) {
            invalidate();
        }
    }

    public final void e() {
        this.g = new RippleDrawable(AbstractC0545Jy0.a(this.e.U), getBackgroundDrawable(), null);
        this.e.getClass();
        setBackground(this.g);
        f();
    }

    public final void f() {
        C4735xm c4735xm;
        if (TextUtils.isEmpty(getText()) || (c4735xm = this.e) == null) {
            return;
        }
        int iW = (int) (c4735xm.w() + c4735xm.u0 + c4735xm.r0);
        C4735xm c4735xm2 = this.e;
        int iV = (int) (c4735xm2.v() + c4735xm2.n0 + c4735xm2.q0);
        if (this.f != null) {
            Rect rect = new Rect();
            this.f.getPadding(rect);
            iV += rect.left;
            iW += rect.right;
        }
        setPaddingRelative(iV, getPaddingTop(), iW, getPaddingBottom());
    }

    public final void g() {
        TextPaint paint = getPaint();
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            paint.drawableState = c4735xm.getState();
        }
        YO0 textAppearance = getTextAppearance();
        if (textAppearance != null) {
            textAppearance.d(getContext(), paint, this.D);
        }
    }

    @Override // android.widget.CheckBox, android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        if (!TextUtils.isEmpty(this.q)) {
            return this.q;
        }
        C4735xm c4735xm = this.e;
        if (!(c4735xm != null && c4735xm.h0)) {
            return isClickable() ? "android.widget.Button" : "android.view.View";
        }
        getParent();
        return "android.widget.Button";
    }

    public Drawable getBackgroundDrawable() {
        InsetDrawable insetDrawable = this.f;
        return insetDrawable == null ? this.e : insetDrawable;
    }

    public Drawable getCheckedIcon() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.j0;
        }
        return null;
    }

    public ColorStateList getCheckedIconTint() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.k0;
        }
        return null;
    }

    public ColorStateList getChipBackgroundColor() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.P;
        }
        return null;
    }

    public float getChipCornerRadius() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return Math.max(0.0f, c4735xm.x());
        }
        return 0.0f;
    }

    public Drawable getChipDrawable() {
        return this.e;
    }

    public float getChipEndPadding() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.u0;
        }
        return 0.0f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Drawable getChipIcon() {
        Drawable drawable;
        C4735xm c4735xm = this.e;
        if (c4735xm == null || (drawable = c4735xm.X) == 0) {
            return null;
        }
        if (!(drawable instanceof P11)) {
            return drawable;
        }
        return null;
    }

    public float getChipIconSize() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.Z;
        }
        return 0.0f;
    }

    public ColorStateList getChipIconTint() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.Y;
        }
        return null;
    }

    public float getChipMinHeight() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.Q;
        }
        return 0.0f;
    }

    public float getChipStartPadding() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.n0;
        }
        return 0.0f;
    }

    public ColorStateList getChipStrokeColor() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.S;
        }
        return null;
    }

    public float getChipStrokeWidth() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.T;
        }
        return 0.0f;
    }

    @Deprecated
    public CharSequence getChipText() {
        return getText();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Drawable getCloseIcon() {
        Drawable drawable;
        C4735xm c4735xm = this.e;
        if (c4735xm == null || (drawable = c4735xm.c0) == 0) {
            return null;
        }
        if (!(drawable instanceof P11)) {
            return drawable;
        }
        return null;
    }

    public CharSequence getCloseIconContentDescription() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.g0;
        }
        return null;
    }

    public float getCloseIconEndPadding() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.t0;
        }
        return 0.0f;
    }

    public float getCloseIconSize() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.f0;
        }
        return 0.0f;
    }

    public float getCloseIconStartPadding() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.s0;
        }
        return 0.0f;
    }

    public ColorStateList getCloseIconTint() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.e0;
        }
        return null;
    }

    @Override // android.widget.TextView
    public TextUtils.TruncateAt getEllipsize() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.S0;
        }
        return null;
    }

    @Override // android.widget.TextView, android.view.View
    public final void getFocusedRect(Rect rect) {
        if (this.A) {
            C4455vm c4455vm = this.z;
            if (c4455vm.l == 1 || c4455vm.k == 1) {
                rect.set(getCloseIconTouchBoundsInt());
                return;
            }
        }
        super.getFocusedRect(rect);
    }

    public C4856yd0 getHideMotionSpec() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.m0;
        }
        return null;
    }

    public float getIconEndPadding() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.p0;
        }
        return 0.0f;
    }

    public float getIconStartPadding() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.o0;
        }
        return 0.0f;
    }

    public ColorStateList getRippleColor() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.U;
        }
        return null;
    }

    public C3126mF0 getShapeAppearanceModel() {
        return this.e.b.a;
    }

    public C4856yd0 getShowMotionSpec() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.l0;
        }
        return null;
    }

    public float getTextEndPadding() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.r0;
        }
        return 0.0f;
    }

    public float getTextStartPadding() {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            return c4735xm.q0;
        }
        return 0.0f;
    }

    @Override // android.widget.TextView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        AbstractC1975e21.K(this, this.e);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public final int[] onCreateDrawableState(int i) {
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i + 2);
        if (isChecked()) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, F);
        }
        C4735xm c4735xm = this.e;
        if (c4735xm != null && c4735xm.h0) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, G);
        }
        return iArrOnCreateDrawableState;
    }

    @Override // android.widget.TextView, android.view.View
    public final void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        if (this.A) {
            C4455vm c4455vm = this.z;
            int i2 = c4455vm.l;
            if (i2 != Integer.MIN_VALUE) {
                c4455vm.j(i2);
            }
            if (z) {
                c4455vm.m(i, rect);
            }
        }
    }

    @Override // android.view.View
    public final boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 7) {
            setCloseIconHovered(getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()));
        } else if (actionMasked == 10) {
            setCloseIconHovered(false);
        }
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(getAccessibilityClassName());
        C4735xm c4735xm = this.e;
        accessibilityNodeInfo.setCheckable(c4735xm != null && c4735xm.h0);
        accessibilityNodeInfo.setClickable(isClickable());
        getParent();
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    public final PointerIcon onResolvePointerIcon(MotionEvent motionEvent, int i) {
        return (getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()) && isEnabled()) ? PointerIcon.getSystemIcon(getContext(), 1002) : super.onResolvePointerIcon(motionEvent, i);
    }

    @Override // android.widget.TextView, android.view.View
    public final void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (this.o != i) {
            this.o = i;
            f();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001e, code lost:
    
        if (r0 != 3) goto L28;
     */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        int actionMasked = motionEvent.getActionMasked();
        boolean zContains = getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY());
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (this.k) {
                        if (!zContains) {
                            setCloseIconPressed(false);
                        }
                        z = true;
                    }
                }
                z = false;
            } else {
                if (this.k) {
                    playSoundEffect(0);
                    View.OnClickListener onClickListener = this.h;
                    if (onClickListener != null) {
                        onClickListener.onClick(this);
                    }
                    if (this.A) {
                        this.z.r(1, 1);
                    }
                    z = true;
                }
                setCloseIconPressed(false);
            }
            z = false;
            setCloseIconPressed(false);
        } else {
            if (zContains) {
                setCloseIconPressed(true);
                z = true;
            }
            z = false;
        }
        return z || super.onTouchEvent(motionEvent);
    }

    public void setAccessibilityClassName(CharSequence charSequence) {
        this.q = charSequence;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        if (drawable == getBackgroundDrawable() || drawable == this.g) {
            super.setBackground(drawable);
        } else {
            Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        Log.w("Chip", "Do not set the background color; Chip manages its own background drawable.");
    }

    @Override // defpackage.K9, android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (drawable == getBackgroundDrawable() || drawable == this.g) {
            super.setBackgroundDrawable(drawable);
        } else {
            Log.w("Chip", "Do not set the background drawable; Chip manages its own background drawable.");
        }
    }

    @Override // defpackage.K9, android.view.View
    public void setBackgroundResource(int i) {
        Log.w("Chip", "Do not set the background resource; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        Log.w("Chip", "Do not set the background tint list; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        Log.w("Chip", "Do not set the background tint mode; Chip manages its own background drawable.");
    }

    public void setCheckable(boolean z) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.C(z);
        }
    }

    public void setCheckableResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.C(c4735xm.v0.getResources().getBoolean(i));
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z) {
        C4735xm c4735xm = this.e;
        if (c4735xm == null) {
            this.j = z;
        } else if (c4735xm.h0) {
            super.setChecked(z);
        }
    }

    public void setCheckedIcon(Drawable drawable) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.D(drawable);
        }
    }

    @Deprecated
    public void setCheckedIconEnabled(boolean z) {
        setCheckedIconVisible(z);
    }

    @Deprecated
    public void setCheckedIconEnabledResource(int i) {
        setCheckedIconVisible(i);
    }

    public void setCheckedIconResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.D(T51.f0(c4735xm.v0, i));
        }
    }

    public void setCheckedIconTint(ColorStateList colorStateList) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.E(colorStateList);
        }
    }

    public void setCheckedIconTintResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.E(AbstractC4635x31.L(c4735xm.v0, i));
        }
    }

    public void setCheckedIconVisible(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.F(c4735xm.v0.getResources().getBoolean(i));
        }
    }

    public void setChipBackgroundColor(ColorStateList colorStateList) {
        C4735xm c4735xm = this.e;
        if (c4735xm == null || c4735xm.P == colorStateList) {
            return;
        }
        c4735xm.P = colorStateList;
        c4735xm.onStateChange(c4735xm.getState());
    }

    public void setChipBackgroundColorResource(int i) {
        ColorStateList colorStateListL;
        C4735xm c4735xm = this.e;
        if (c4735xm == null || c4735xm.P == (colorStateListL = AbstractC4635x31.L(c4735xm.v0, i))) {
            return;
        }
        c4735xm.P = colorStateListL;
        c4735xm.onStateChange(c4735xm.getState());
    }

    @Deprecated
    public void setChipCornerRadius(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.G(f);
        }
    }

    @Deprecated
    public void setChipCornerRadiusResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.G(c4735xm.v0.getResources().getDimension(i));
        }
    }

    public void setChipDrawable(C4735xm c4735xm) {
        C4735xm c4735xm2 = this.e;
        if (c4735xm2 != c4735xm) {
            if (c4735xm2 != null) {
                c4735xm2.R0 = new WeakReference(null);
            }
            this.e = c4735xm;
            c4735xm.T0 = false;
            c4735xm.R0 = new WeakReference(this);
            b(this.p);
        }
    }

    public void setChipEndPadding(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm == null || c4735xm.u0 == f) {
            return;
        }
        c4735xm.u0 = f;
        c4735xm.invalidateSelf();
        c4735xm.A();
    }

    public void setChipEndPaddingResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            float dimension = c4735xm.v0.getResources().getDimension(i);
            if (c4735xm.u0 != dimension) {
                c4735xm.u0 = dimension;
                c4735xm.invalidateSelf();
                c4735xm.A();
            }
        }
    }

    public void setChipIcon(Drawable drawable) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.H(drawable);
        }
    }

    @Deprecated
    public void setChipIconEnabled(boolean z) {
        setChipIconVisible(z);
    }

    @Deprecated
    public void setChipIconEnabledResource(int i) {
        setChipIconVisible(i);
    }

    public void setChipIconResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.H(T51.f0(c4735xm.v0, i));
        }
    }

    public void setChipIconSize(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.I(f);
        }
    }

    public void setChipIconSizeResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.I(c4735xm.v0.getResources().getDimension(i));
        }
    }

    public void setChipIconTint(ColorStateList colorStateList) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.J(colorStateList);
        }
    }

    public void setChipIconTintResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.J(AbstractC4635x31.L(c4735xm.v0, i));
        }
    }

    public void setChipIconVisible(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.K(c4735xm.v0.getResources().getBoolean(i));
        }
    }

    public void setChipMinHeight(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm == null || c4735xm.Q == f) {
            return;
        }
        c4735xm.Q = f;
        c4735xm.invalidateSelf();
        c4735xm.A();
    }

    public void setChipMinHeightResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            float dimension = c4735xm.v0.getResources().getDimension(i);
            if (c4735xm.Q != dimension) {
                c4735xm.Q = dimension;
                c4735xm.invalidateSelf();
                c4735xm.A();
            }
        }
    }

    public void setChipStartPadding(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm == null || c4735xm.n0 == f) {
            return;
        }
        c4735xm.n0 = f;
        c4735xm.invalidateSelf();
        c4735xm.A();
    }

    public void setChipStartPaddingResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            float dimension = c4735xm.v0.getResources().getDimension(i);
            if (c4735xm.n0 != dimension) {
                c4735xm.n0 = dimension;
                c4735xm.invalidateSelf();
                c4735xm.A();
            }
        }
    }

    public void setChipStrokeColor(ColorStateList colorStateList) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.L(colorStateList);
        }
    }

    public void setChipStrokeColorResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.L(AbstractC4635x31.L(c4735xm.v0, i));
        }
    }

    public void setChipStrokeWidth(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.M(f);
        }
    }

    public void setChipStrokeWidthResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.M(c4735xm.v0.getResources().getDimension(i));
        }
    }

    @Deprecated
    public void setChipText(CharSequence charSequence) {
        setText(charSequence);
    }

    @Deprecated
    public void setChipTextResource(int i) {
        setText(getResources().getString(i));
    }

    public void setCloseIcon(Drawable drawable) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.N(drawable);
        }
        d();
    }

    public void setCloseIconContentDescription(CharSequence charSequence) {
        C4735xm c4735xm = this.e;
        if (c4735xm == null || c4735xm.g0 == charSequence) {
            return;
        }
        String str = C1479ag.b;
        C1479ag c1479ag = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1 ? C1479ag.e : C1479ag.d;
        c1479ag.getClass();
        C0715Ng c0715Ng = AP0.a;
        c4735xm.g0 = c1479ag.c(charSequence);
        c4735xm.invalidateSelf();
    }

    @Deprecated
    public void setCloseIconEnabled(boolean z) {
        setCloseIconVisible(z);
    }

    @Deprecated
    public void setCloseIconEnabledResource(int i) {
        setCloseIconVisible(i);
    }

    public void setCloseIconEndPadding(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.O(f);
        }
    }

    public void setCloseIconEndPaddingResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.O(c4735xm.v0.getResources().getDimension(i));
        }
    }

    public void setCloseIconResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.N(T51.f0(c4735xm.v0, i));
        }
        d();
    }

    public void setCloseIconSize(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.P(f);
        }
    }

    public void setCloseIconSizeResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.P(c4735xm.v0.getResources().getDimension(i));
        }
    }

    public void setCloseIconStartPadding(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.Q(f);
        }
    }

    public void setCloseIconStartPaddingResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.Q(c4735xm.v0.getResources().getDimension(i));
        }
    }

    public void setCloseIconTint(ColorStateList colorStateList) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.S(colorStateList);
        }
    }

    public void setCloseIconTintResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.S(AbstractC4635x31.L(c4735xm.v0, i));
        }
    }

    public void setCloseIconVisible(int i) {
        setCloseIconVisible(getResources().getBoolean(i));
    }

    @Override // defpackage.K9, android.widget.TextView
    public final void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
    }

    @Override // defpackage.K9, android.widget.TextView
    public final void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawablesRelativeWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        if (i != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (i3 != 0) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(i, i2, i3, i4);
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawablesWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        if (i != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (i3 != 0) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesWithIntrinsicBounds(i, i2, i3, i4);
    }

    @Override // android.view.View
    public void setElevation(float f) {
        super.setElevation(f);
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.m(f);
        }
    }

    @Override // android.widget.TextView
    public void setEllipsize(TextUtils.TruncateAt truncateAt) {
        if (this.e == null) {
            return;
        }
        if (truncateAt == TextUtils.TruncateAt.MARQUEE) {
            throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
        }
        super.setEllipsize(truncateAt);
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.S0 = truncateAt;
        }
    }

    public void setEnsureMinTouchTargetSize(boolean z) {
        this.n = z;
        b(this.p);
    }

    @Override // android.widget.TextView
    public void setGravity(int i) {
        if (i != 8388627) {
            Log.w("Chip", "Chip text must be vertically center and start aligned");
        } else {
            super.setGravity(i);
        }
    }

    public void setHideMotionSpec(C4856yd0 c4856yd0) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.m0 = c4856yd0;
        }
    }

    public void setHideMotionSpecResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.m0 = C4856yd0.a(c4735xm.v0, i);
        }
    }

    public void setIconEndPadding(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.U(f);
        }
    }

    public void setIconEndPaddingResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.U(c4735xm.v0.getResources().getDimension(i));
        }
    }

    public void setIconStartPadding(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.V(f);
        }
    }

    public void setIconStartPaddingResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.V(c4735xm.v0.getResources().getDimension(i));
        }
    }

    public void setInternalOnCheckedChangeListener(InterfaceC2470ha0 interfaceC2470ha0) {
    }

    @Override // android.view.View
    public void setLayoutDirection(int i) {
        if (this.e == null) {
            return;
        }
        super.setLayoutDirection(i);
    }

    @Override // android.widget.TextView
    public void setLines(int i) {
        if (i > 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setLines(i);
    }

    @Override // android.widget.TextView
    public void setMaxLines(int i) {
        if (i > 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setMaxLines(i);
    }

    @Override // android.widget.TextView
    public void setMaxWidth(int i) {
        super.setMaxWidth(i);
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.U0 = i;
        }
    }

    @Override // android.widget.TextView
    public void setMinLines(int i) {
        if (i > 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setMinLines(i);
    }

    @Override // android.widget.CompoundButton
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.i = onCheckedChangeListener;
    }

    public void setOnCloseIconClickListener(View.OnClickListener onClickListener) {
        this.h = onClickListener;
        d();
    }

    public void setRippleColor(ColorStateList colorStateList) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.W(colorStateList);
        }
        this.e.getClass();
        e();
    }

    public void setRippleColorResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.W(AbstractC4635x31.L(c4735xm.v0, i));
            this.e.getClass();
            e();
        }
    }

    @Override // defpackage.InterfaceC4945zF0
    public void setShapeAppearanceModel(C3126mF0 c3126mF0) {
        this.e.setShapeAppearanceModel(c3126mF0);
    }

    public void setShowMotionSpec(C4856yd0 c4856yd0) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.l0 = c4856yd0;
        }
    }

    public void setShowMotionSpecResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.l0 = C4856yd0.a(c4735xm.v0, i);
        }
    }

    @Override // android.widget.TextView
    public void setSingleLine(boolean z) {
        if (!z) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setSingleLine(z);
    }

    @Override // android.widget.TextView
    public final void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        C4735xm c4735xm = this.e;
        if (c4735xm == null) {
            return;
        }
        if (charSequence == null) {
            charSequence = "";
        }
        super.setText(c4735xm.T0 ? null : charSequence, bufferType);
        C4735xm c4735xm2 = this.e;
        if (c4735xm2 == null || TextUtils.equals(c4735xm2.V, charSequence)) {
            return;
        }
        c4735xm2.V = charSequence;
        c4735xm2.B0.d = true;
        c4735xm2.invalidateSelf();
        c4735xm2.A();
    }

    public void setTextAppearance(YO0 yo0) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.X(yo0);
        }
        g();
    }

    public void setTextAppearanceResource(int i) {
        setTextAppearance(getContext(), i);
    }

    public void setTextEndPadding(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm == null || c4735xm.r0 == f) {
            return;
        }
        c4735xm.r0 = f;
        c4735xm.invalidateSelf();
        c4735xm.A();
    }

    public void setTextEndPaddingResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            float dimension = c4735xm.v0.getResources().getDimension(i);
            if (c4735xm.r0 != dimension) {
                c4735xm.r0 = dimension;
                c4735xm.invalidateSelf();
                c4735xm.A();
            }
        }
    }

    @Override // android.widget.TextView
    public final void setTextSize(int i, float f) {
        super.setTextSize(i, f);
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            float fApplyDimension = TypedValue.applyDimension(i, f, getResources().getDisplayMetrics());
            DP0 dp0 = c4735xm.B0;
            YO0 yo0 = dp0.f;
            if (yo0 != null) {
                yo0.l = fApplyDimension;
                dp0.a.setTextSize(fApplyDimension);
                c4735xm.A();
                c4735xm.invalidateSelf();
            }
        }
        g();
    }

    public void setTextStartPadding(float f) {
        C4735xm c4735xm = this.e;
        if (c4735xm == null || c4735xm.q0 == f) {
            return;
        }
        c4735xm.q0 = f;
        c4735xm.invalidateSelf();
        c4735xm.A();
    }

    public void setTextStartPaddingResource(int i) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            float dimension = c4735xm.v0.getResources().getDimension(i);
            if (c4735xm.q0 != dimension) {
                c4735xm.q0 = dimension;
                c4735xm.invalidateSelf();
                c4735xm.A();
            }
        }
    }

    public void setCloseIconVisible(boolean z) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.T(z);
        }
        d();
    }

    public void setCheckedIconVisible(boolean z) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.F(z);
        }
    }

    public void setChipIconVisible(boolean z) {
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.K(z);
        }
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 == null) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
            return;
        }
        throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        }
        if (drawable3 == null) {
            super.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
            return;
        }
        throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
    }

    @Override // android.widget.TextView
    public final void setTextAppearance(Context context, int i) {
        super.setTextAppearance(context, i);
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.X(new YO0(c4735xm.v0, i));
        }
        g();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(int i) {
        super.setTextAppearance(i);
        C4735xm c4735xm = this.e;
        if (c4735xm != null) {
            c4735xm.X(new YO0(c4735xm.v0, i));
        }
        g();
    }
}
