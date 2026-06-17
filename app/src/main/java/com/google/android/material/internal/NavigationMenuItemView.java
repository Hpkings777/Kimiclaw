package com.google.android.material.internal;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import defpackage.AbstractC1062Tx0;
import defpackage.AbstractC2585iO;
import defpackage.AbstractC2732jS0;
import defpackage.AbstractC4421vX0;
import defpackage.C2196fc0;
import defpackage.C2637im;
import defpackage.InterfaceC4153tc0;
import defpackage.J50;

/* JADX INFO: loaded from: classes.dex */
public class NavigationMenuItemView extends AbstractC2585iO implements InterfaceC4153tc0 {
    public static final int[] O = {R.attr.state_checked};
    public int D;
    public boolean E;
    public boolean F;
    public final boolean G;
    public final CheckedTextView H;
    public FrameLayout I;
    public C2196fc0 J;
    public ColorStateList K;
    public boolean L;
    public Drawable M;
    public final C2637im N;

    public NavigationMenuItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.G = true;
        C2637im c2637im = new C2637im(this, 3);
        this.N = c2637im;
        setOrientation(0);
        LayoutInflater.from(context).inflate(com.moonshot.kimiclaw.R.layout.design_navigation_menu_item, (ViewGroup) this, true);
        setIconSize(context.getResources().getDimensionPixelSize(com.moonshot.kimiclaw.R.dimen.design_navigation_icon_size));
        CheckedTextView checkedTextView = (CheckedTextView) findViewById(com.moonshot.kimiclaw.R.id.design_menu_item_text);
        this.H = checkedTextView;
        AbstractC4421vX0.q(checkedTextView, c2637im);
    }

    private void setActionView(View view) {
        if (view != null) {
            if (this.I == null) {
                this.I = (FrameLayout) ((ViewStub) findViewById(com.moonshot.kimiclaw.R.id.design_menu_item_action_area_stub)).inflate();
            }
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            this.I.removeAllViews();
            this.I.addView(view);
        }
    }

    @Override // defpackage.InterfaceC4153tc0
    public final void c(C2196fc0 c2196fc0) {
        StateListDrawable stateListDrawable;
        this.J = c2196fc0;
        int i = c2196fc0.a;
        if (i > 0) {
            setId(i);
        }
        setVisibility(c2196fc0.isVisible() ? 0 : 8);
        if (getBackground() == null) {
            TypedValue typedValue = new TypedValue();
            if (getContext().getTheme().resolveAttribute(com.moonshot.kimiclaw.R.attr.colorControlHighlight, typedValue, true)) {
                stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(O, new ColorDrawable(typedValue.data));
                stateListDrawable.addState(ViewGroup.EMPTY_STATE_SET, new ColorDrawable(0));
            } else {
                stateListDrawable = null;
            }
            setBackground(stateListDrawable);
        }
        setCheckable(c2196fc0.isCheckable());
        setChecked(c2196fc0.isChecked());
        setEnabled(c2196fc0.isEnabled());
        setTitle(c2196fc0.e);
        setIcon(c2196fc0.getIcon());
        setActionView(c2196fc0.getActionView());
        setContentDescription(c2196fc0.q);
        AbstractC2732jS0.a(this, c2196fc0.z);
        C2196fc0 c2196fc02 = this.J;
        CharSequence charSequence = c2196fc02.e;
        CheckedTextView checkedTextView = this.H;
        if (charSequence == null && c2196fc02.getIcon() == null && this.J.getActionView() != null) {
            checkedTextView.setVisibility(8);
            FrameLayout frameLayout = this.I;
            if (frameLayout != null) {
                J50 j50 = (J50) frameLayout.getLayoutParams();
                ((LinearLayout.LayoutParams) j50).width = -1;
                this.I.setLayoutParams(j50);
                return;
            }
            return;
        }
        checkedTextView.setVisibility(0);
        FrameLayout frameLayout2 = this.I;
        if (frameLayout2 != null) {
            J50 j502 = (J50) frameLayout2.getLayoutParams();
            ((LinearLayout.LayoutParams) j502).width = -2;
            this.I.setLayoutParams(j502);
        }
    }

    @Override // defpackage.InterfaceC4153tc0
    public C2196fc0 getItemData() {
        return this.J;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final int[] onCreateDrawableState(int i) {
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i + 1);
        C2196fc0 c2196fc0 = this.J;
        if (c2196fc0 != null && c2196fc0.isCheckable() && this.J.isChecked()) {
            View.mergeDrawableStates(iArrOnCreateDrawableState, O);
        }
        return iArrOnCreateDrawableState;
    }

    public void setCheckable(boolean z) {
        refreshDrawableState();
        if (this.F != z) {
            this.F = z;
            this.N.h(this.H, 2048);
        }
    }

    public void setChecked(boolean z) {
        refreshDrawableState();
        CheckedTextView checkedTextView = this.H;
        checkedTextView.setChecked(z);
        checkedTextView.setTypeface(checkedTextView.getTypeface(), (z && this.G) ? 1 : 0);
    }

    public void setHorizontalPadding(int i) {
        setPadding(i, getPaddingTop(), i, getPaddingBottom());
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            if (this.L) {
                Drawable.ConstantState constantState = drawable.getConstantState();
                if (constantState != null) {
                    drawable = constantState.newDrawable();
                }
                drawable = drawable.mutate();
                drawable.setTintList(this.K);
            }
            int i = this.D;
            drawable.setBounds(0, 0, i, i);
        } else if (this.E) {
            if (this.M == null) {
                Resources resources = getResources();
                Resources.Theme theme = getContext().getTheme();
                ThreadLocal threadLocal = AbstractC1062Tx0.a;
                Drawable drawable2 = resources.getDrawable(com.moonshot.kimiclaw.R.drawable.navigation_empty_icon, theme);
                this.M = drawable2;
                if (drawable2 != null) {
                    int i2 = this.D;
                    drawable2.setBounds(0, 0, i2, i2);
                }
            }
            drawable = this.M;
        }
        this.H.setCompoundDrawablesRelative(drawable, null, null, null);
    }

    public void setIconPadding(int i) {
        this.H.setCompoundDrawablePadding(i);
    }

    public void setIconSize(int i) {
        this.D = i;
    }

    public void setIconTintList(ColorStateList colorStateList) {
        this.K = colorStateList;
        this.L = colorStateList != null;
        C2196fc0 c2196fc0 = this.J;
        if (c2196fc0 != null) {
            setIcon(c2196fc0.getIcon());
        }
    }

    public void setMaxLines(int i) {
        this.H.setMaxLines(i);
    }

    public void setNeedsEmptyIcon(boolean z) {
        this.E = z;
    }

    public void setTextAppearance(int i) {
        this.H.setTextAppearance(i);
    }

    public void setTextColor(ColorStateList colorStateList) {
        this.H.setTextColor(colorStateList);
    }

    public void setTitle(CharSequence charSequence) {
        this.H.setText(charSequence);
    }
}
