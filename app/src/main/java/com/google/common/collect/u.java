package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.collect.Multimaps;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class u implements Function {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ u(Object obj, int i) {
        this.a = i;
        this.b = obj;
    }

    @Override // com.google.common.base.Function
    public final Object apply(Object obj) {
        switch (this.a) {
            case 0:
                return ((StandardTable) this.b).column(obj);
            case 1:
                return ((StandardTable) this.b).row(obj);
            default:
                return ((Multimaps.TransformedEntriesMultimap) this.b).lambda$createValues$0((Map.Entry) obj);
        }
    }
}
