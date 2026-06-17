package com.google.android.material.datepicker;

import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.g;
import com.moonshot.kimiclaw.R;
import defpackage.AbstractC4421vX0;
import defpackage.C2742jX0;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public final class b extends g {
    public final TextView a;
    public final MaterialCalendarGridView b;

    public b(LinearLayout linearLayout, boolean z) {
        super(linearLayout);
        TextView textView = (TextView) linearLayout.findViewById(R.id.month_title);
        this.a = textView;
        WeakHashMap weakHashMap = AbstractC4421vX0.a;
        new C2742jX0(R.id.tag_accessibility_heading, Boolean.class, 0, 28, 3).g(textView, Boolean.TRUE);
        this.b = (MaterialCalendarGridView) linearLayout.findViewById(R.id.month_grid);
        if (z) {
            return;
        }
        textView.setVisibility(8);
    }
}
