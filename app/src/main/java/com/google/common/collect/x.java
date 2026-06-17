package com.google.common.collect;

import com.google.common.collect.TableCollectors;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class x implements BiConsumer {
    public final /* synthetic */ int a;
    public final /* synthetic */ Function b;
    public final /* synthetic */ Function c;
    public final /* synthetic */ Function d;
    public final /* synthetic */ BinaryOperator e;

    public /* synthetic */ x(Function function, Function function2, Function function3, BinaryOperator binaryOperator, int i) {
        this.a = i;
        this.b = function;
        this.c = function2;
        this.d = function3;
        this.e = binaryOperator;
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        switch (this.a) {
            case 0:
                Function function = this.c;
                Function function2 = this.d;
                TableCollectors.lambda$toTable$1(this.b, function, function2, this.e, (Table) obj, obj2);
                break;
            default:
                Function function3 = this.c;
                Function function4 = this.d;
                TableCollectors.lambda$toImmutableTable$2(this.b, function3, function4, this.e, (TableCollectors.ImmutableTableCollectorState) obj, obj2);
                break;
        }
    }
}
