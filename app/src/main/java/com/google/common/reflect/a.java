package com.google.common.reflect;

import com.google.common.base.Function;
import com.google.common.reflect.MutableTypeToInstanceMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class a implements Function {
    @Override // com.google.common.base.Function
    public final Object apply(Object obj) {
        return MutableTypeToInstanceMap.UnmodifiableEntry.a((Map.Entry) obj);
    }
}
