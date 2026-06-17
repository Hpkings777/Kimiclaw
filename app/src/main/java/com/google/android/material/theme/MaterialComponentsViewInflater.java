package com.google.android.material.theme;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.moonshot.kimiclaw.R;
import defpackage.AbstractC1264Xu0;
import defpackage.AbstractC4123tO0;
import defpackage.AbstractC4635x31;
import defpackage.C0755Oa;
import defpackage.C1171Wa;
import defpackage.C2330ga0;
import defpackage.C2748ja0;
import defpackage.C3447oa;
import defpackage.H9;
import defpackage.I9;
import defpackage.K9;
import defpackage.T90;

/* JADX INFO: loaded from: classes.dex */
public class MaterialComponentsViewInflater extends C1171Wa {
    @Override // defpackage.C1171Wa
    public final H9 a(Context context, AttributeSet attributeSet) {
        return new T90(context, attributeSet);
    }

    @Override // defpackage.C1171Wa
    public final I9 b(Context context, AttributeSet attributeSet) {
        return new MaterialButton(context, attributeSet);
    }

    @Override // defpackage.C1171Wa
    public final K9 c(Context context, AttributeSet attributeSet) {
        return new C2330ga0(context, attributeSet);
    }

    @Override // defpackage.C1171Wa
    public final C3447oa d(Context context, AttributeSet attributeSet) {
        C2748ja0 c2748ja0 = new C2748ja0(AbstractC4123tO0.f0(context, attributeSet, R.attr.radioButtonStyle, R.style.Widget_MaterialComponents_CompoundButton_RadioButton), attributeSet);
        Context context2 = c2748ja0.getContext();
        TypedArray typedArrayB0 = AbstractC4635x31.b0(context2, attributeSet, AbstractC1264Xu0.q, R.attr.radioButtonStyle, R.style.Widget_MaterialComponents_CompoundButton_RadioButton, new int[0]);
        if (typedArrayB0.hasValue(0)) {
            c2748ja0.setButtonTintList(AbstractC4123tO0.J(context2, typedArrayB0, 0));
        }
        c2748ja0.f = typedArrayB0.getBoolean(1, false);
        typedArrayB0.recycle();
        return c2748ja0;
    }

    @Override // defpackage.C1171Wa
    public final C0755Oa e(Context context, AttributeSet attributeSet) {
        return new MaterialTextView(context, attributeSet);
    }
}
