package com.google.common.base;

import com.google.common.base.Splitter;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class b implements Splitter.Strategy {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ b(Object obj, int i) {
        this.a = i;
        this.b = obj;
    }

    @Override // com.google.common.base.Splitter.Strategy
    public final Iterator iterator(Splitter splitter, CharSequence charSequence) {
        switch (this.a) {
            case 0:
                return Splitter.lambda$on$1((String) this.b, splitter, charSequence);
            case 1:
                return Splitter.lambda$on$0((CharMatcher) this.b, splitter, charSequence);
            default:
                return Splitter.lambda$onPatternInternal$0((CommonPattern) this.b, splitter, charSequence);
        }
    }
}
