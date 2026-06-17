package com.google.android.material.timepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.moonshot.kimiclaw.R;
import defpackage.ViewOnTouchListenerC3384o60;

/* JADX INFO: loaded from: classes.dex */
class TimePickerView extends ConstraintLayout {
    public static final /* synthetic */ int B = 0;
    public final Chip A;

    public TimePickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        f fVar = new f(this);
        LayoutInflater.from(context).inflate(R.layout.material_timepicker, this);
        MaterialButtonToggleGroup materialButtonToggleGroup = (MaterialButtonToggleGroup) findViewById(R.id.material_clock_period_toggle);
        materialButtonToggleGroup.k.add(new e());
        Chip chip = (Chip) findViewById(R.id.material_minute_tv);
        Chip chip2 = (Chip) findViewById(R.id.material_hour_tv);
        this.A = chip2;
        ViewOnTouchListenerC3384o60 viewOnTouchListenerC3384o60 = new ViewOnTouchListenerC3384o60(new GestureDetector(getContext(), new g(this)), 1);
        chip.setOnTouchListener(viewOnTouchListenerC3384o60);
        chip2.setOnTouchListener(viewOnTouchListenerC3384o60);
        chip.setTag(R.id.selection_type, 12);
        chip2.setTag(R.id.selection_type, 10);
        chip.setOnClickListener(fVar);
        chip2.setOnClickListener(fVar);
        chip.setAccessibilityClassName("android.view.View");
        chip2.setAccessibilityClassName("android.view.View");
    }

    @Override // android.view.View
    public final void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (view == this && i == 0) {
            this.A.sendAccessibilityEvent(8);
        }
    }
}
