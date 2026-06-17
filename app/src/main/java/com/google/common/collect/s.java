package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.collect.Table;
import java.util.Collection;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class s implements Function {
    public final /* synthetic */ int a;

    public /* synthetic */ s(int i) {
        this.a = i;
    }

    @Override // com.google.common.base.Function
    public final Object apply(Object obj) {
        switch (this.a) {
            case 0:
                return Multimaps.access$000((Collection) obj);
            default:
                return Tables.access$000((Table.Cell) obj);
        }
    }
}
