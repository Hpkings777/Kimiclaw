package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public interface LineProcessor<T> {
    @ParametricNullness
    T getResult();

    boolean processLine(String str) throws IOException;
}
