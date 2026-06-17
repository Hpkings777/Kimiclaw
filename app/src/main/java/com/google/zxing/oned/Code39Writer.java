package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import defpackage.AbstractC2039eV;
import defpackage.YI0;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.io.IOUtils;

/* JADX INFO: loaded from: classes.dex */
public final class Code39Writer extends OneDimensionalCodeWriter {
    private static void toIntArray(int i, int[] iArr) {
        for (int i2 = 0; i2 < 9; i2++) {
            int i3 = 1;
            if (((1 << (8 - i2)) & i) != 0) {
                i3 = 2;
            }
            iArr[i2] = i3;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x00d4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String tryToConvertToExtendedMode(String str) {
        int length = str.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == 0) {
                sb.append("%U");
            } else if (cCharAt == ' ') {
                sb.append(cCharAt);
            } else if (cCharAt == '@') {
                sb.append("%V");
            } else if (cCharAt == '`') {
                sb.append("%W");
            } else if (cCharAt != '-' && cCharAt != '.') {
                if (cCharAt <= 26) {
                    sb.append('$');
                    sb.append((char) (cCharAt + '@'));
                } else if (cCharAt < ' ') {
                    sb.append('%');
                    sb.append((char) (cCharAt + '&'));
                } else if (cCharAt <= ',' || cCharAt == '/' || cCharAt == ':') {
                    sb.append(IOUtils.DIR_SEPARATOR_UNIX);
                    sb.append((char) (cCharAt + ' '));
                } else if (cCharAt <= '9') {
                    sb.append(cCharAt);
                } else if (cCharAt <= '?') {
                    sb.append('%');
                    sb.append((char) (cCharAt + 11));
                } else if (cCharAt <= 'Z') {
                    sb.append(cCharAt);
                } else if (cCharAt <= '_') {
                    sb.append('%');
                    sb.append((char) (cCharAt - 16));
                } else if (cCharAt <= 'z') {
                    sb.append('+');
                    sb.append((char) (cCharAt - ' '));
                } else {
                    if (cCharAt > 127) {
                        throw new IllegalArgumentException("Requested content contains a non-encodable character: '" + str.charAt(i) + "'");
                    }
                    sb.append('%');
                    sb.append((char) (cCharAt - '+'));
                }
            }
        }
        return sb.toString();
    }

    @Override // com.google.zxing.oned.OneDimensionalCodeWriter
    public boolean[] encode(String str) {
        int length = str.length();
        if (length > 80) {
            throw new IllegalArgumentException(YI0.h(length, "Requested contents should be less than 80 digits long, but got "));
        }
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            if ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%".indexOf(str.charAt(i)) < 0) {
                str = tryToConvertToExtendedMode(str);
                length = str.length();
                if (length > 80) {
                    throw new IllegalArgumentException(AbstractC2039eV.i(length, "Requested contents should be less than 80 digits long, but got ", " (extended full ASCII mode)"));
                }
            } else {
                i++;
            }
        }
        int[] iArr = new int[9];
        boolean[] zArr = new boolean[(length * 13) + 25];
        toIntArray(148, iArr);
        int iAppendPattern = OneDimensionalCodeWriter.appendPattern(zArr, 0, iArr, true);
        int[] iArr2 = {1};
        int iAppendPattern2 = OneDimensionalCodeWriter.appendPattern(zArr, iAppendPattern, iArr2, false) + iAppendPattern;
        for (int i2 = 0; i2 < length; i2++) {
            toIntArray(Code39Reader.CHARACTER_ENCODINGS["0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%".indexOf(str.charAt(i2))], iArr);
            int iAppendPattern3 = OneDimensionalCodeWriter.appendPattern(zArr, iAppendPattern2, iArr, true) + iAppendPattern2;
            iAppendPattern2 = OneDimensionalCodeWriter.appendPattern(zArr, iAppendPattern3, iArr2, false) + iAppendPattern3;
        }
        toIntArray(148, iArr);
        OneDimensionalCodeWriter.appendPattern(zArr, iAppendPattern2, iArr, true);
        return zArr;
    }

    @Override // com.google.zxing.oned.OneDimensionalCodeWriter
    public Collection<BarcodeFormat> getSupportedWriteFormats() {
        return Collections.singleton(BarcodeFormat.CODE_39);
    }
}
