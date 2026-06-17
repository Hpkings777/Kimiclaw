package com.google.android.material.datepicker;

import android.view.View;
import android.widget.AdapterView;
import defpackage.C1911da0;
import defpackage.C2897kd0;

/* JADX INFO: loaded from: classes.dex */
public final class a implements AdapterView.OnItemClickListener {
    public final /* synthetic */ MaterialCalendarGridView a;
    public final /* synthetic */ c b;

    public a(c cVar, MaterialCalendarGridView materialCalendarGridView) {
        this.b = cVar;
        this.a = materialCalendarGridView;
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
        MaterialCalendarGridView materialCalendarGridView = this.a;
        C2897kd0 c2897kd0A = materialCalendarGridView.a();
        if (i < c2897kd0A.a() || i > c2897kd0A.c()) {
            return;
        }
        if (materialCalendarGridView.a().getItem(i).longValue() >= ((C1911da0) this.b.b.b).e0.c.a) {
            throw null;
        }
    }
}
