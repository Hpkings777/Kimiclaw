package com.sun.mail.util;

import java.io.OutputStream;
import kotlin.jvm.internal.IntCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public class BEncoderStream extends BASE64EncoderStream {
    public BEncoderStream(OutputStream outputStream) {
        super(outputStream, IntCompanionObject.MAX_VALUE);
    }

    public static int encodedLength(byte[] bArr) {
        return ((bArr.length + 2) / 3) * 4;
    }
}
