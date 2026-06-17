package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class A implements Function {
    public final /* synthetic */ int a;
    public final /* synthetic */ Tables.TransformedTable b;

    public /* synthetic */ A(Tables.TransformedTable transformedTable, int i) {
        this.a = i;
        this.b = transformedTable;
    }

    @Override // com.google.common.base.Function
    public final Object apply(Object obj) {
        switch (this.a) {
            case 0:
                return this.b.applyToValue((Table.Cell) obj);
            case 1:
                return this.b.lambda$rowMap$0((Map) obj);
            default:
                return this.b.lambda$columnMap$0((Map) obj);
        }
    }
}
