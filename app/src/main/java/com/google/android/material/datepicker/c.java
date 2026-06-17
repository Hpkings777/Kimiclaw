package com.google.android.material.datepicker;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.g;
import com.moonshot.kimiclaw.R;
import defpackage.C0566Kj;
import defpackage.C2610ia0;
import defpackage.C2757jd0;
import defpackage.C2897kd0;
import defpackage.C3777qw0;
import defpackage.C4918z41;
import defpackage.IV0;
import java.util.Calendar;

/* JADX INFO: loaded from: classes.dex */
public final class c extends androidx.recyclerview.widget.c {
    public final C0566Kj a;
    public final C4918z41 b;
    public final int c;

    public c(ContextThemeWrapper contextThemeWrapper, C0566Kj c0566Kj, C4918z41 c4918z41) {
        C2757jd0 c2757jd0 = c0566Kj.a;
        C2757jd0 c2757jd02 = c0566Kj.d;
        if (c2757jd0.a.compareTo(c2757jd02.a) > 0) {
            throw new IllegalArgumentException("firstPage cannot be after currentPage");
        }
        if (c2757jd02.a.compareTo(c0566Kj.b.a) > 0) {
            throw new IllegalArgumentException("currentPage cannot be after lastPage");
        }
        this.c = (contextThemeWrapper.getResources().getDimensionPixelSize(R.dimen.mtrl_calendar_day_height) * C2897kd0.d) + (C2610ia0.K(contextThemeWrapper, android.R.attr.windowFullscreen) ? contextThemeWrapper.getResources().getDimensionPixelSize(R.dimen.mtrl_calendar_day_height) : 0);
        this.a = c0566Kj;
        this.b = c4918z41;
        setHasStableIds(true);
    }

    @Override // androidx.recyclerview.widget.c
    public final int getItemCount() {
        return this.a.g;
    }

    @Override // androidx.recyclerview.widget.c
    public final long getItemId(int i) {
        Calendar calendarA = IV0.a(this.a.a.a);
        calendarA.add(2, i);
        calendarA.set(5, 1);
        Calendar calendarA2 = IV0.a(calendarA);
        calendarA2.get(2);
        calendarA2.get(1);
        calendarA2.getMaximum(7);
        calendarA2.getActualMaximum(5);
        calendarA2.getTimeInMillis();
        return calendarA2.getTimeInMillis();
    }

    @Override // androidx.recyclerview.widget.c
    public final void onBindViewHolder(g gVar, int i) {
        b bVar = (b) gVar;
        C0566Kj c0566Kj = this.a;
        Calendar calendarA = IV0.a(c0566Kj.a.a);
        calendarA.add(2, i);
        C2757jd0 c2757jd0 = new C2757jd0(calendarA);
        bVar.a.setText(c2757jd0.c());
        MaterialCalendarGridView materialCalendarGridView = (MaterialCalendarGridView) bVar.b.findViewById(R.id.month_grid);
        if (materialCalendarGridView.a() == null || !c2757jd0.equals(materialCalendarGridView.a().a)) {
            new C2897kd0(c2757jd0, c0566Kj);
            throw null;
        }
        materialCalendarGridView.invalidate();
        materialCalendarGridView.a().getClass();
        throw null;
    }

    @Override // androidx.recyclerview.widget.c
    public final g onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mtrl_calendar_month_labeled, viewGroup, false);
        if (!C2610ia0.K(viewGroup.getContext(), android.R.attr.windowFullscreen)) {
            return new b(linearLayout, false);
        }
        linearLayout.setLayoutParams(new C3777qw0(-1, this.c));
        return new b(linearLayout, true);
    }
}
