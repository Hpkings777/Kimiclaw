package com.google.android.material.internal;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import defpackage.InterfaceC4293uc0;
import defpackage.MenuC1776cc0;

/* JADX INFO: loaded from: classes.dex */
public class NavigationMenuView extends RecyclerView implements InterfaceC4293uc0 {
    public NavigationMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        setLayoutManager(new LinearLayoutManager(1));
    }

    @Override // defpackage.InterfaceC4293uc0
    public final void b(MenuC1776cc0 menuC1776cc0) {
    }

    public int getWindowAnimations() {
        return 0;
    }
}
