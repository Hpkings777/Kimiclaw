package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.FormatException;
import defpackage.YI0;
import java.util.Collection;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public final class EAN8Writer extends UPCEANWriter {
    private static final int CODE_WIDTH = 67;

    @Override // com.google.zxing.oned.OneDimensionalCodeWriter
    public boolean[] encode(String str) {
        int length = str.length();
        if (length == 7) {
            try {
                str = str + UPCEANReader.getStandardUPCEANChecksum(str);
            } catch (FormatException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            if (length != 8) {
                throw new IllegalArgumentException(YI0.h(length, "Requested contents should be 7 or 8 digits long, but got "));
            }
            try {
                if (!UPCEANReader.checkStandardUPCEANChecksum(str)) {
                    throw new IllegalArgumentException("Contents do not pass checksum");
                }
            } catch (FormatException unused) {
                throw new IllegalArgumentException("Illegal contents");
            }
        }
        OneDimensionalCodeWriter.checkNumeric(str);
        boolean[] zArr = new boolean[CODE_WIDTH];
        int iAppendPattern = OneDimensionalCodeWriter.appendPattern(zArr, 0, UPCEANReader.START_END_PATTERN, true);
        for (int i = 0; i <= 3; i++) {
            iAppendPattern += OneDimensionalCodeWriter.appendPattern(zArr, iAppendPattern, UPCEANReader.L_PATTERNS[Character.digit(str.charAt(i), 10)], false);
        }
        int iAppendPattern2 = OneDimensionalCodeWriter.appendPattern(zArr, iAppendPattern, UPCEANReader.MIDDLE_PATTERN, false) + iAppendPattern;
        for (int i2 = 4; i2 <= 7; i2++) {
            iAppendPattern2 += OneDimensionalCodeWriter.appendPattern(zArr, iAppendPattern2, UPCEANReader.L_PATTERNS[Character.digit(str.charAt(i2), 10)], true);
        }
        OneDimensionalCodeWriter.appendPattern(zArr, iAppendPattern2, UPCEANReader.START_END_PATTERN, true);
        return zArr;
    }

    @Override // com.google.zxing.oned.OneDimensionalCodeWriter
    public Collection<BarcodeFormat> getSupportedWriteFormats() {
        return Collections.singleton(BarcodeFormat.EAN_8);
    }
}
