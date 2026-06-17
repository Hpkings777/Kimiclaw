package com.sun.mail.util;

import com.google.common.primitives.UnsignedBytes;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import kotlin.jvm.internal.IntCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public class ASCIIUtility {
    private ASCIIUtility() {
    }

    public static byte[] getBytes(String str) {
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = (byte) charArray[i];
        }
        return bArr;
    }

    public static int parseInt(byte[] bArr, int i, int i2, int i3) throws NumberFormatException {
        int i4;
        int i5;
        boolean z;
        if (bArr == null) {
            throw new NumberFormatException("null");
        }
        if (i2 <= i) {
            throw new NumberFormatException("illegal number");
        }
        int i6 = 0;
        if (bArr[i] == 45) {
            i5 = i + 1;
            i4 = IntCompanionObject.MIN_VALUE;
            z = true;
        } else {
            i4 = -2147483647;
            i5 = i;
            z = false;
        }
        int i7 = i4 / i3;
        if (i5 < i2) {
            int i8 = i5 + 1;
            int iDigit = Character.digit((char) bArr[i5], i3);
            if (iDigit < 0) {
                throw new NumberFormatException("illegal number: " + toString(bArr, i, i2));
            }
            i6 = -iDigit;
            i5 = i8;
        }
        while (i5 < i2) {
            int i9 = i5 + 1;
            int iDigit2 = Character.digit((char) bArr[i5], i3);
            if (iDigit2 < 0) {
                throw new NumberFormatException("illegal number");
            }
            if (i6 < i7) {
                throw new NumberFormatException("illegal number");
            }
            int i10 = i6 * i3;
            if (i10 < i4 + iDigit2) {
                throw new NumberFormatException("illegal number");
            }
            i6 = i10 - iDigit2;
            i5 = i9;
        }
        if (!z) {
            return -i6;
        }
        if (i5 > i + 1) {
            return i6;
        }
        throw new NumberFormatException("illegal number");
    }

    public static long parseLong(byte[] bArr, int i, int i2, int i3) throws NumberFormatException {
        long j;
        boolean z;
        int i4;
        long j2;
        if (bArr == null) {
            throw new NumberFormatException("null");
        }
        if (i2 <= i) {
            throw new NumberFormatException("illegal number");
        }
        if (bArr[i] == 45) {
            i4 = i + 1;
            j = Long.MIN_VALUE;
            z = true;
        } else {
            j = -9223372036854775807L;
            z = false;
            i4 = i;
        }
        long j3 = i3;
        long j4 = j / j3;
        if (i4 < i2) {
            int i5 = i4 + 1;
            int iDigit = Character.digit((char) bArr[i4], i3);
            if (iDigit < 0) {
                throw new NumberFormatException("illegal number: " + toString(bArr, i, i2));
            }
            j2 = -iDigit;
            i4 = i5;
        } else {
            j2 = 0;
        }
        while (i4 < i2) {
            int i6 = i4 + 1;
            int iDigit2 = Character.digit((char) bArr[i4], i3);
            if (iDigit2 < 0) {
                throw new NumberFormatException("illegal number");
            }
            if (j2 < j4) {
                throw new NumberFormatException("illegal number");
            }
            long j5 = j2 * j3;
            long j6 = j;
            long j7 = iDigit2;
            if (j5 < j6 + j7) {
                throw new NumberFormatException("illegal number");
            }
            j2 = j5 - j7;
            i4 = i6;
            j = j6;
        }
        if (!z) {
            return -j2;
        }
        if (i4 > i + 1) {
            return j2;
        }
        throw new NumberFormatException("illegal number");
    }

    public static String toString(byte[] bArr, int i, int i2) {
        int i3 = i2 - i;
        char[] cArr = new char[i3];
        int i4 = 0;
        while (i4 < i3) {
            cArr[i4] = (char) (bArr[i] & UnsignedBytes.MAX_VALUE);
            i4++;
            i++;
        }
        return new String(cArr);
    }

    public static String toString(byte[] bArr) {
        return toString(bArr, 0, bArr.length);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        if (inputStream instanceof ByteArrayInputStream) {
            int iAvailable = inputStream.available();
            byte[] bArr = new byte[iAvailable];
            inputStream.read(bArr, 0, iAvailable);
            return bArr;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr2 = new byte[1024];
        while (true) {
            int i = inputStream.read(bArr2, 0, 1024);
            if (i != -1) {
                byteArrayOutputStream.write(bArr2, 0, i);
            } else {
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    public static String toString(ByteArrayInputStream byteArrayInputStream) {
        int iAvailable = byteArrayInputStream.available();
        char[] cArr = new char[iAvailable];
        byte[] bArr = new byte[iAvailable];
        byteArrayInputStream.read(bArr, 0, iAvailable);
        for (int i = 0; i < iAvailable; i++) {
            cArr[i] = (char) (bArr[i] & UnsignedBytes.MAX_VALUE);
        }
        return new String(cArr);
    }

    public static int parseInt(byte[] bArr, int i, int i2) throws NumberFormatException {
        return parseInt(bArr, i, i2, 10);
    }

    public static long parseLong(byte[] bArr, int i, int i2) throws NumberFormatException {
        return parseLong(bArr, i, i2, 10);
    }
}
