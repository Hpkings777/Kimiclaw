package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class q implements Function {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;
    public final /* synthetic */ Multimaps.TransformedEntriesMultimap c;

    public /* synthetic */ q(Multimaps.TransformedEntriesMultimap transformedEntriesMultimap, Object obj, int i) {
        this.a = i;
        this.c = transformedEntriesMultimap;
        this.b = obj;
    }

    @Override // com.google.common.base.Function
    public final Object apply(Object obj) {
        switch (this.a) {
            case 0:
                return ((Multimaps.TransformedEntriesListMultimap) this.c).lambda$transform$0(this.b, obj);
            default:
                return this.c.lambda$transform$0(this.b, obj);
        }
    }
}
