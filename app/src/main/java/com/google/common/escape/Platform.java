package com.google.common.escape;

import com.google.common.annotations.GwtCompatible;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class Platform {
    private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>() { // from class: com.google.common.escape.Platform.1
        @Override // java.lang.ThreadLocal
        public char[] initialValue() {
            return new char[1024];
        }
    };

    private Platform() {
    }

    public static char[] charBufferFromThreadLocal() {
        char[] cArr = DEST_TL.get();
        Objects.requireNonNull(cArr);
        return cArr;
    }
}
