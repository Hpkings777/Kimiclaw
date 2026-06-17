package com.google.common.escape;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import defpackage.C4344v0;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public abstract class Escaper {
    private final Function<String, String> asFunction = new C4344v0(this, 1);

    public final Function<String, String> asFunction() {
        return this.asFunction;
    }

    public abstract String escape(String str);
}
