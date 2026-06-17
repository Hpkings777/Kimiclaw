package com.google.android.material.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RadioButton;
import android.widget.ToggleButton;
import com.google.android.material.timepicker.e;
import com.moonshot.kimiclaw.R;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC4123tO0;
import defpackage.AbstractC4421vX0;
import defpackage.AbstractC4635x31;
import defpackage.C1599bK0;
import defpackage.C2637im;
import defpackage.C2664j;
import defpackage.X90;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class MaterialButtonToggleGroup extends X90 {
    public static final /* synthetic */ int q = 0;
    public final LinkedHashSet k;
    public boolean l;
    public boolean m;
    public boolean n;
    public final int o;
    public HashSet p;

    public MaterialButtonToggleGroup(Context context, AttributeSet attributeSet) {
        super(AbstractC4123tO0.f0(context, attributeSet, R.attr.materialButtonToggleGroupStyle, R.style.Widget_MaterialComponents_MaterialButtonToggleGroup), attributeSet);
        this.k = new LinkedHashSet();
        this.l = false;
        this.p = new HashSet();
        TypedArray typedArrayB0 = AbstractC4635x31.b0(getContext(), attributeSet, AbstractC1264Xu0.l, R.attr.materialButtonToggleGroupStyle, R.style.Widget_MaterialComponents_MaterialButtonToggleGroup, new int[0]);
        setSingleSelection(typedArrayB0.getBoolean(7, false));
        this.o = typedArrayB0.getResourceId(2, -1);
        this.n = typedArrayB0.getBoolean(4, false);
        if (this.f == null) {
            this.f = C1599bK0.b(new C2664j(0.0f));
        }
        setEnabled(typedArrayB0.getBoolean(0, true));
        typedArrayB0.recycle();
        setImportantForAccessibility(1);
    }

    private String getChildrenA11yClassName() {
        return (this.m ? RadioButton.class : ToggleButton.class).getName();
    }

    private int getVisibleButtonCount() {
        int i = 0;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            if ((getChildAt(i2) instanceof MaterialButton) && getChildAt(i2).getVisibility() != 8) {
                i++;
            }
        }
        return i;
    }

    private void setupButtonChild(MaterialButton materialButton) {
        materialButton.setMaxLines(1);
        materialButton.setEllipsize(TextUtils.TruncateAt.END);
        materialButton.setCheckable(true);
        materialButton.setA11yClassName(getChildrenA11yClassName());
    }

    @Override // defpackage.X90, android.view.ViewGroup
    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (!(view instanceof MaterialButton)) {
            Log.e("MButtonToggleGroup", "Child views must be of type MaterialButton.");
            return;
        }
        super.addView(view, i, layoutParams);
        MaterialButton materialButton = (MaterialButton) view;
        setupButtonChild(materialButton);
        f(materialButton.getId(), materialButton.o);
        AbstractC4421vX0.q(materialButton, new C2637im(this, 1));
    }

    public final void f(int i, boolean z) {
        if (i == -1) {
            Log.e("MButtonToggleGroup", "Button ID is not valid: " + i);
            return;
        }
        HashSet hashSet = new HashSet(this.p);
        if (z && !hashSet.contains(Integer.valueOf(i))) {
            if (this.m && !hashSet.isEmpty()) {
                hashSet.clear();
            }
            hashSet.add(Integer.valueOf(i));
        } else {
            if (z || !hashSet.contains(Integer.valueOf(i))) {
                return;
            }
            if (!this.n || hashSet.size() > 1) {
                hashSet.remove(Integer.valueOf(i));
            }
        }
        g(hashSet);
    }

    public final void g(Set set) {
        HashSet hashSet = this.p;
        this.p = new HashSet(set);
        for (int i = 0; i < getChildCount(); i++) {
            int id = ((MaterialButton) getChildAt(i)).getId();
            boolean zContains = set.contains(Integer.valueOf(id));
            View viewFindViewById = findViewById(id);
            if (viewFindViewById instanceof MaterialButton) {
                this.l = true;
                ((MaterialButton) viewFindViewById).setChecked(zContains);
                this.l = false;
            }
            if (hashSet.contains(Integer.valueOf(id)) != set.contains(Integer.valueOf(id))) {
                set.contains(Integer.valueOf(id));
                Iterator it = this.k.iterator();
                while (it.hasNext()) {
                    ((e) it.next()).a();
                }
            }
        }
        invalidate();
    }

    public int getCheckedButtonId() {
        if (!this.m || this.p.isEmpty()) {
            return -1;
        }
        return ((Integer) this.p.iterator().next()).intValue();
    }

    public List<Integer> getCheckedButtonIds() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < getChildCount(); i++) {
            int id = ((MaterialButton) getChildAt(i)).getId();
            if (this.p.contains(Integer.valueOf(id))) {
                arrayList.add(Integer.valueOf(id));
            }
        }
        return arrayList;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        int i = this.o;
        if (i != -1) {
            g(Collections.singleton(Integer.valueOf(i)));
        }
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setCollectionInfo(AccessibilityNodeInfo.CollectionInfo.obtain(1, getVisibleButtonCount(), false, this.m ? 1 : 2));
    }

    public void setSelectionRequired(boolean z) {
        this.n = z;
    }

    public void setSingleSelection(boolean z) {
        if (this.m != z) {
            this.m = z;
            g(new HashSet());
        }
        String childrenA11yClassName = getChildrenA11yClassName();
        for (int i = 0; i < getChildCount(); i++) {
            ((MaterialButton) getChildAt(i)).setA11yClassName(childrenA11yClassName);
        }
    }

    public void setSingleSelection(int i) {
        setSingleSelection(getResources().getBoolean(i));
    }
}
