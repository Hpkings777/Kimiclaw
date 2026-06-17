package com.google.zxing.oned;

import com.google.common.base.Ascii;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Arrays;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class Code93Reader extends OneDReader {
    static final int ASTERISK_ENCODING;
    static final int[] CHARACTER_ENCODINGS;
    static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*";
    private static final char[] ALPHABET = ALPHABET_STRING.toCharArray();
    private final StringBuilder decodeRowResult = new StringBuilder(20);
    private final int[] counters = new int[6];

    static {
        int[] iArr = {276, 328, 324, 322, 296, 292, 290, 336, 274, 266, 424, 420, 418, 404, 402, 394, 360, 356, 354, 308, 282, 344, 332, 326, 300, 278, 436, 434, 428, 422, 406, 410, 364, 358, 310, 314, 302, 468, 466, 458, 366, 374, 430, 294, 474, 470, 306, 350};
        CHARACTER_ENCODINGS = iArr;
        ASTERISK_ENCODING = iArr[47];
    }

    private static void checkChecksums(CharSequence charSequence) throws ChecksumException {
        int length = charSequence.length();
        checkOneChecksum(charSequence, length - 2, 20);
        checkOneChecksum(charSequence, length - 1, 15);
    }

    private static void checkOneChecksum(CharSequence charSequence, int i, int i2) throws ChecksumException {
        int iIndexOf = 0;
        int i3 = 1;
        for (int i4 = i - 1; i4 >= 0; i4--) {
            iIndexOf += ALPHABET_STRING.indexOf(charSequence.charAt(i4)) * i3;
            i3++;
            if (i3 > i2) {
                i3 = 1;
            }
        }
        if (charSequence.charAt(i) != ALPHABET[iIndexOf % 47]) {
            throw ChecksumException.getChecksumInstance();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:12:0x002c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String decodeExtended(CharSequence charSequence) throws FormatException {
        int i;
        char c;
        int length = charSequence.length();
        StringBuilder sb = new StringBuilder(length);
        int i2 = 0;
        while (i2 < length) {
            char cCharAt = charSequence.charAt(i2);
            if (cCharAt >= 'a' && cCharAt <= 'd') {
                if (i2 >= length - 1) {
                    throw FormatException.getFormatInstance();
                }
                i2++;
                char cCharAt2 = charSequence.charAt(i2);
                switch (cCharAt) {
                    case 'a':
                        if (cCharAt2 < 'A' || cCharAt2 > 'Z') {
                            throw FormatException.getFormatInstance();
                        }
                        i = cCharAt2 - '@';
                        c = (char) i;
                        sb.append(c);
                        break;
                    case 'b':
                        if (cCharAt2 >= 'A' && cCharAt2 <= 'E') {
                            i = cCharAt2 - '&';
                        } else if (cCharAt2 >= 'F' && cCharAt2 <= 'J') {
                            i = cCharAt2 - 11;
                        } else if (cCharAt2 >= 'K' && cCharAt2 <= 'O') {
                            i = cCharAt2 + 16;
                        } else if (cCharAt2 < 'P' || cCharAt2 > 'T') {
                            if (cCharAt2 == 'U') {
                                c = 0;
                            } else if (cCharAt2 == 'V') {
                                c = '@';
                            } else if (cCharAt2 == 'W') {
                                c = '`';
                            } else {
                                if (cCharAt2 < 'X' || cCharAt2 > 'Z') {
                                    throw FormatException.getFormatInstance();
                                }
                                c = Ascii.MAX;
                            }
                            sb.append(c);
                        } else {
                            i = cCharAt2 + '+';
                        }
                        c = (char) i;
                        sb.append(c);
                        break;
                    case 'c':
                        if (cCharAt2 >= 'A' && cCharAt2 <= 'O') {
                            i = cCharAt2 - ' ';
                            c = (char) i;
                            sb.append(c);
                        } else {
                            if (cCharAt2 != 'Z') {
                                throw FormatException.getFormatInstance();
                            }
                            c = ':';
                            sb.append(c);
                        }
                        break;
                    case 'd':
                        if (cCharAt2 < 'A' || cCharAt2 > 'Z') {
                            throw FormatException.getFormatInstance();
                        }
                        i = cCharAt2 + ' ';
                        c = (char) i;
                        sb.append(c);
                        break;
                }
            } else {
                sb.append(cCharAt);
            }
            i2++;
        }
        return sb.toString();
    }

    private int[] findAsteriskPattern(BitArray bitArray) throws NotFoundException {
        int size = bitArray.getSize();
        int nextSet = bitArray.getNextSet(0);
        Arrays.fill(this.counters, 0);
        int[] iArr = this.counters;
        int length = iArr.length;
        boolean z = false;
        int i = 0;
        int i2 = nextSet;
        while (nextSet < size) {
            if (bitArray.get(nextSet) != z) {
                iArr[i] = iArr[i] + 1;
            } else {
                if (i != length - 1) {
                    i++;
                } else {
                    if (toPattern(iArr) == ASTERISK_ENCODING) {
                        return new int[]{i2, nextSet};
                    }
                    i2 += iArr[0] + iArr[1];
                    int i3 = i - 1;
                    System.arraycopy(iArr, 2, iArr, 0, i3);
                    iArr[i3] = 0;
                    iArr[i] = 0;
                    i--;
                }
                iArr[i] = 1;
                z = !z;
            }
            nextSet++;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static char patternToChar(int i) throws NotFoundException {
        int i2 = 0;
        while (true) {
            int[] iArr = CHARACTER_ENCODINGS;
            if (i2 >= iArr.length) {
                throw NotFoundException.getNotFoundInstance();
            }
            if (iArr[i2] == i) {
                return ALPHABET[i2];
            }
            i2++;
        }
    }

    private static int toPattern(int[] iArr) {
        int i = 0;
        for (int i2 : iArr) {
            i += i2;
        }
        int length = iArr.length;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            int iRound = Math.round((iArr[i4] * 9.0f) / i);
            if (iRound < 1 || iRound > 4) {
                return -1;
            }
            if ((i4 & 1) == 0) {
                for (int i5 = 0; i5 < iRound; i5++) {
                    i3 = (i3 << 1) | 1;
                }
            } else {
                i3 <<= iRound;
            }
        }
        return i3;
    }

    @Override // com.google.zxing.oned.OneDReader
    public Result decodeRow(int i, BitArray bitArray, Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException {
        int nextSet = bitArray.getNextSet(findAsteriskPattern(bitArray)[1]);
        int size = bitArray.getSize();
        int[] iArr = this.counters;
        Arrays.fill(iArr, 0);
        StringBuilder sb = this.decodeRowResult;
        sb.setLength(0);
        while (true) {
            OneDReader.recordPattern(bitArray, nextSet, iArr);
            int pattern = toPattern(iArr);
            if (pattern < 0) {
                throw NotFoundException.getNotFoundInstance();
            }
            char cPatternToChar = patternToChar(pattern);
            sb.append(cPatternToChar);
            int i2 = nextSet;
            for (int i3 : iArr) {
                i2 += i3;
            }
            int nextSet2 = bitArray.getNextSet(i2);
            if (cPatternToChar == '*') {
                sb.deleteCharAt(sb.length() - 1);
                int i4 = 0;
                for (int i5 : iArr) {
                    i4 += i5;
                }
                if (nextSet2 == size || !bitArray.get(nextSet2)) {
                    throw NotFoundException.getNotFoundInstance();
                }
                if (sb.length() < 2) {
                    throw NotFoundException.getNotFoundInstance();
                }
                checkChecksums(sb);
                sb.setLength(sb.length() - 2);
                float f = i;
                Result result = new Result(decodeExtended(sb), null, new ResultPoint[]{new ResultPoint((r14[1] + r14[0]) / 2.0f, f), new ResultPoint((i4 / 2.0f) + nextSet, f)}, BarcodeFormat.CODE_93);
                result.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]G0");
                return result;
            }
            nextSet = nextSet2;
        }
    }
}
