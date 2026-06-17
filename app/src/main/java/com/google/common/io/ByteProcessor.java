package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public interface ByteProcessor<T> {
    @ParametricNullness
    T getResult();

    boolean processBytes(byte[] bArr, int i, int i2) throws IOException;
}
