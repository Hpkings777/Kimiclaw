package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.collect.Synchronized;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class w implements Function {
    public final /* synthetic */ int a;
    public final /* synthetic */ Synchronized.SynchronizedTable b;

    public /* synthetic */ w(Synchronized.SynchronizedTable synchronizedTable, int i) {
        this.a = i;
        this.b = synchronizedTable;
    }

    @Override // com.google.common.base.Function
    public final Object apply(Object obj) {
        switch (this.a) {
            case 0:
                return this.b.lambda$rowMap$0((Map) obj);
            default:
                return this.b.lambda$columnMap$0((Map) obj);
        }
    }
}
