package com.google.zxing.pdf417.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.pdf417.PDF417Common;
import com.google.zxing.pdf417.decoder.ec.ErrorCorrection;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Formatter;

/* JADX INFO: loaded from: classes.dex */
public final class PDF417ScanningDecoder {
    private static final int CODEWORD_SKEW_SIZE = 2;
    private static final int MAX_EC_CODEWORDS = 512;
    private static final int MAX_ERRORS = 3;
    private static final ErrorCorrection errorCorrection = new ErrorCorrection();

    private PDF417ScanningDecoder() {
    }

    private static BoundingBox adjustBoundingBox(DetectionResultRowIndicatorColumn detectionResultRowIndicatorColumn) throws NotFoundException {
        int[] rowHeights;
        if (detectionResultRowIndicatorColumn == null || (rowHeights = detectionResultRowIndicatorColumn.getRowHeights()) == null) {
            return null;
        }
        int max = getMax(rowHeights);
        int i = 0;
        int i2 = 0;
        for (int i3 : rowHeights) {
            i2 += max - i3;
            if (i3 > 0) {
                break;
            }
        }
        Codeword[] codewords = detectionResultRowIndicatorColumn.getCodewords();
        for (int i4 = 0; i2 > 0 && codewords[i4] == null; i4++) {
            i2--;
        }
        for (int length = rowHeights.length - 1; length >= 0; length--) {
            int i5 = rowHeights[length];
            i += max - i5;
            if (i5 > 0) {
                break;
            }
        }
        for (int length2 = codewords.length - 1; i > 0 && codewords[length2] == null; length2--) {
            i--;
        }
        return detectionResultRowIndicatorColumn.getBoundingBox().addMissingRows(i2, i, detectionResultRowIndicatorColumn.isLeft());
    }

    private static void adjustCodewordCount(DetectionResult detectionResult, BarcodeValue[][] barcodeValueArr) throws NotFoundException {
        BarcodeValue barcodeValue = barcodeValueArr[0][1];
        int[] value = barcodeValue.getValue();
        int barcodeColumnCount = (detectionResult.getBarcodeColumnCount() * detectionResult.getBarcodeRowCount()) - getNumberOfECCodeWords(detectionResult.getBarcodeECLevel());
        if (value.length == 0) {
            if (barcodeColumnCount < 1 || barcodeColumnCount > 928) {
                throw NotFoundException.getNotFoundInstance();
            }
            barcodeValue.setValue(barcodeColumnCount);
            return;
        }
        if (value[0] == barcodeColumnCount || barcodeColumnCount < 1 || barcodeColumnCount > 928) {
            return;
        }
        barcodeValue.setValue(barcodeColumnCount);
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0022, code lost:
    
        r0 = -r0;
        r8 = !r8;
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0022, code lost:
    
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0022, code lost:
    
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0022, code lost:
    
        continue;
     */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0011  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int adjustCodewordStartColumn(BitMatrix bitMatrix, int i, int i2, boolean z, int i3, int i4) {
        int i5 = z ? -1 : 1;
        int i6 = 0;
        int i7 = i3;
        while (i6 < 2) {
            while (true) {
                if (z) {
                    if (i7 >= i) {
                        if (z == bitMatrix.get(i7, i4)) {
                            if (Math.abs(i3 - i7) > 2) {
                                return i3;
                            }
                            i7 += i5;
                        }
                    }
                } else if (i7 < i2) {
                }
            }
        }
        return i7;
    }

    private static boolean checkCodewordSkew(int i, int i2, int i3) {
        return i2 + (-2) <= i && i <= i3 + 2;
    }

    private static int correctErrors(int[] iArr, int[] iArr2, int i) throws ChecksumException {
        if ((iArr2 == null || iArr2.length <= (i / 2) + 3) && i >= 0 && i <= MAX_EC_CODEWORDS) {
            return errorCorrection.decode(iArr, i, iArr2);
        }
        throw ChecksumException.getChecksumInstance();
    }

    private static BarcodeValue[][] createBarcodeMatrix(DetectionResult detectionResult) {
        int rowNumber;
        BarcodeValue[][] barcodeValueArr = (BarcodeValue[][]) Array.newInstance((Class<?>) BarcodeValue.class, detectionResult.getBarcodeRowCount(), detectionResult.getBarcodeColumnCount() + 2);
        for (BarcodeValue[] barcodeValueArr2 : barcodeValueArr) {
            int i = 0;
            while (true) {
                if (i < barcodeValueArr2.length) {
                    barcodeValueArr2[i] = new BarcodeValue();
                    i++;
                }
            }
        }
        int i2 = 0;
        for (DetectionResultColumn detectionResultColumn : detectionResult.getDetectionResultColumns()) {
            if (detectionResultColumn != null) {
                for (Codeword codeword : detectionResultColumn.getCodewords()) {
                    if (codeword != null && (rowNumber = codeword.getRowNumber()) >= 0 && rowNumber < barcodeValueArr.length) {
                        barcodeValueArr[rowNumber][i2].setValue(codeword.getValue());
                    }
                }
            }
            i2++;
        }
        return barcodeValueArr;
    }

    private static DecoderResult createDecoderResult(DetectionResult detectionResult) throws NotFoundException, ChecksumException, FormatException {
        BarcodeValue[][] barcodeValueArrCreateBarcodeMatrix = createBarcodeMatrix(detectionResult);
        adjustCodewordCount(detectionResult, barcodeValueArrCreateBarcodeMatrix);
        ArrayList arrayList = new ArrayList();
        int[] iArr = new int[detectionResult.getBarcodeRowCount() * detectionResult.getBarcodeColumnCount()];
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (int i = 0; i < detectionResult.getBarcodeRowCount(); i++) {
            int i2 = 0;
            while (i2 < detectionResult.getBarcodeColumnCount()) {
                int i3 = i2 + 1;
                int[] value = barcodeValueArrCreateBarcodeMatrix[i][i3].getValue();
                int barcodeColumnCount = (detectionResult.getBarcodeColumnCount() * i) + i2;
                if (value.length == 0) {
                    arrayList.add(Integer.valueOf(barcodeColumnCount));
                } else if (value.length == 1) {
                    iArr[barcodeColumnCount] = value[0];
                } else {
                    arrayList3.add(Integer.valueOf(barcodeColumnCount));
                    arrayList2.add(value);
                }
                i2 = i3;
            }
        }
        int size = arrayList2.size();
        int[][] iArr2 = new int[size][];
        for (int i4 = 0; i4 < size; i4++) {
            iArr2[i4] = (int[]) arrayList2.get(i4);
        }
        return createDecoderResultFromAmbiguousValues(detectionResult.getBarcodeECLevel(), iArr, PDF417Common.toIntArray(arrayList), PDF417Common.toIntArray(arrayList3), iArr2);
    }

    private static DecoderResult createDecoderResultFromAmbiguousValues(int i, int[] iArr, int[] iArr2, int[] iArr3, int[][] iArr4) throws ChecksumException, FormatException {
        int length = iArr3.length;
        int[] iArr5 = new int[length];
        int i2 = 100;
        while (true) {
            int i3 = i2 - 1;
            if (i2 <= 0) {
                throw ChecksumException.getChecksumInstance();
            }
            for (int i4 = 0; i4 < length; i4++) {
                iArr[iArr3[i4]] = iArr4[i4][iArr5[i4]];
            }
            try {
                return decodeCodewords(iArr, i, iArr2);
            } catch (ChecksumException unused) {
                if (length == 0) {
                    throw ChecksumException.getChecksumInstance();
                }
                int i5 = 0;
                while (true) {
                    if (i5 >= length) {
                        break;
                    }
                    int i6 = iArr5[i5];
                    if (i6 < iArr4[i5].length - 1) {
                        iArr5[i5] = i6 + 1;
                        break;
                    }
                    iArr5[i5] = 0;
                    if (i5 == length - 1) {
                        throw ChecksumException.getChecksumInstance();
                    }
                    i5++;
                }
                i2 = i3;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00f1 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static DecoderResult decode(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4, int i, int i2) throws NotFoundException, ChecksumException, FormatException {
        DetectionResultRowIndicatorColumn rowIndicatorColumn;
        BoundingBox boundingBox;
        DetectionResult detectionResultMerge;
        DetectionResultColumn detectionResultRowIndicatorColumn;
        DetectionResultColumn detectionResultColumn;
        int i3;
        Codeword codewordDetectCodeword;
        DetectionResultRowIndicatorColumn detectionResultRowIndicatorColumn2 = null;
        boolean z = true;
        BoundingBox boundingBox2 = new BoundingBox(bitMatrix, resultPoint, resultPoint2, resultPoint3, resultPoint4);
        DetectionResultRowIndicatorColumn rowIndicatorColumn2 = null;
        boolean z2 = true;
        while (true) {
            rowIndicatorColumn = resultPoint != null ? getRowIndicatorColumn(bitMatrix, boundingBox2, resultPoint, true, i, i2) : detectionResultRowIndicatorColumn2;
            if (resultPoint3 != null) {
                rowIndicatorColumn2 = getRowIndicatorColumn(bitMatrix, boundingBox2, resultPoint3, false, i, i2);
            }
            boundingBox = boundingBox2;
            detectionResultMerge = merge(rowIndicatorColumn, rowIndicatorColumn2);
            if (detectionResultMerge == null) {
                throw NotFoundException.getNotFoundInstance();
            }
            boundingBox2 = detectionResultMerge.getBoundingBox();
            if (!z2 || boundingBox2 == null || (boundingBox2.getMinY() >= boundingBox.getMinY() && boundingBox2.getMaxY() <= boundingBox.getMaxY())) {
                break;
            }
            detectionResultRowIndicatorColumn2 = rowIndicatorColumn;
            z2 = false;
        }
        detectionResultMerge.setBoundingBox(boundingBox);
        int barcodeColumnCount = detectionResultMerge.getBarcodeColumnCount() + 1;
        detectionResultMerge.setDetectionResultColumn(0, rowIndicatorColumn);
        detectionResultMerge.setDetectionResultColumn(barcodeColumnCount, rowIndicatorColumn2);
        boolean z3 = rowIndicatorColumn != null;
        int i4 = i;
        int i5 = i2;
        int i6 = 1;
        while (i6 <= barcodeColumnCount) {
            int i7 = z3 ? i6 : barcodeColumnCount - i6;
            if (detectionResultMerge.getDetectionResultColumn(i7) == null) {
                if (i7 == 0 || i7 == barcodeColumnCount) {
                    detectionResultRowIndicatorColumn = new DetectionResultRowIndicatorColumn(boundingBox, i7 == 0 ? z : false);
                } else {
                    detectionResultRowIndicatorColumn = new DetectionResultColumn(boundingBox);
                }
                detectionResultMerge.setDetectionResultColumn(i7, detectionResultRowIndicatorColumn);
                int i8 = -1;
                int iMin = i4;
                int iMax = i5;
                int minY = boundingBox.getMinY();
                int i9 = -1;
                while (minY <= boundingBox.getMaxY()) {
                    int startColumn = getStartColumn(detectionResultMerge, i7, minY, z3);
                    if (startColumn >= 0 && startColumn <= boundingBox.getMaxX()) {
                        detectionResultColumn = detectionResultRowIndicatorColumn;
                        i3 = i8;
                        int i10 = startColumn;
                        codewordDetectCodeword = detectCodeword(bitMatrix, boundingBox.getMinX(), boundingBox.getMaxX(), z3, i10, minY, iMin, iMax);
                        if (codewordDetectCodeword == null) {
                        }
                    } else if (i9 == i8) {
                        detectionResultColumn = detectionResultRowIndicatorColumn;
                        i3 = i8;
                    } else {
                        startColumn = i9;
                        detectionResultColumn = detectionResultRowIndicatorColumn;
                        i3 = i8;
                        int i102 = startColumn;
                        codewordDetectCodeword = detectCodeword(bitMatrix, boundingBox.getMinX(), boundingBox.getMaxX(), z3, i102, minY, iMin, iMax);
                        if (codewordDetectCodeword == null) {
                            detectionResultColumn.setCodeword(minY, codewordDetectCodeword);
                            iMin = Math.min(iMin, codewordDetectCodeword.getWidth());
                            iMax = Math.max(iMax, codewordDetectCodeword.getWidth());
                            i9 = i102;
                        }
                    }
                    minY++;
                    detectionResultRowIndicatorColumn = detectionResultColumn;
                    i8 = i3;
                }
                i4 = iMin;
                i5 = iMax;
            }
            i6++;
            z = true;
        }
        return createDecoderResult(detectionResultMerge);
    }

    private static DecoderResult decodeCodewords(int[] iArr, int i, int[] iArr2) throws ChecksumException, FormatException {
        if (iArr.length == 0) {
            throw FormatException.getFormatInstance();
        }
        int i2 = 1 << (i + 1);
        int iCorrectErrors = correctErrors(iArr, iArr2, i2);
        verifyCodewordCount(iArr, i2);
        DecoderResult decoderResultDecode = DecodedBitStreamParser.decode(iArr, String.valueOf(i));
        decoderResultDecode.setErrorsCorrected(Integer.valueOf(iCorrectErrors));
        decoderResultDecode.setErasures(Integer.valueOf(iArr2.length));
        return decoderResultDecode;
    }

    private static Codeword detectCodeword(BitMatrix bitMatrix, int i, int i2, boolean z, int i3, int i4, int i5, int i6) {
        int i7;
        int decodedValue;
        int codeword;
        int iAdjustCodewordStartColumn = adjustCodewordStartColumn(bitMatrix, i, i2, z, i3, i4);
        int[] moduleBitCount = getModuleBitCount(bitMatrix, i, i2, z, iAdjustCodewordStartColumn, i4);
        if (moduleBitCount == null) {
            return null;
        }
        int iSum = MathUtils.sum(moduleBitCount);
        if (z) {
            i7 = iAdjustCodewordStartColumn + iSum;
        } else {
            for (int i8 = 0; i8 < moduleBitCount.length / 2; i8++) {
                int i9 = moduleBitCount[i8];
                moduleBitCount[i8] = moduleBitCount[(moduleBitCount.length - 1) - i8];
                moduleBitCount[(moduleBitCount.length - 1) - i8] = i9;
            }
            iAdjustCodewordStartColumn -= iSum;
            i7 = iAdjustCodewordStartColumn;
        }
        if (checkCodewordSkew(iSum, i5, i6) && (codeword = PDF417Common.getCodeword((decodedValue = PDF417CodewordDecoder.getDecodedValue(moduleBitCount)))) != -1) {
            return new Codeword(iAdjustCodewordStartColumn, i7, getCodewordBucketNumber(decodedValue), codeword);
        }
        return null;
    }

    private static BarcodeMetadata getBarcodeMetadata(DetectionResultRowIndicatorColumn detectionResultRowIndicatorColumn, DetectionResultRowIndicatorColumn detectionResultRowIndicatorColumn2) {
        BarcodeMetadata barcodeMetadata;
        BarcodeMetadata barcodeMetadata2;
        if (detectionResultRowIndicatorColumn == null || (barcodeMetadata = detectionResultRowIndicatorColumn.getBarcodeMetadata()) == null) {
            if (detectionResultRowIndicatorColumn2 == null) {
                return null;
            }
            return detectionResultRowIndicatorColumn2.getBarcodeMetadata();
        }
        if (detectionResultRowIndicatorColumn2 == null || (barcodeMetadata2 = detectionResultRowIndicatorColumn2.getBarcodeMetadata()) == null || barcodeMetadata.getColumnCount() == barcodeMetadata2.getColumnCount() || barcodeMetadata.getErrorCorrectionLevel() == barcodeMetadata2.getErrorCorrectionLevel() || barcodeMetadata.getRowCount() == barcodeMetadata2.getRowCount()) {
            return barcodeMetadata;
        }
        return null;
    }

    private static int[] getBitCountForCodeword(int i) {
        int[] iArr = new int[8];
        int i2 = 0;
        int i3 = 7;
        while (true) {
            int i4 = i & 1;
            if (i4 != i2) {
                i3--;
                if (i3 < 0) {
                    return iArr;
                }
                i2 = i4;
            }
            iArr[i3] = iArr[i3] + 1;
            i >>= 1;
        }
    }

    private static int getCodewordBucketNumber(int i) {
        return getCodewordBucketNumber(getBitCountForCodeword(i));
    }

    private static int getMax(int[] iArr) {
        int iMax = -1;
        for (int i : iArr) {
            iMax = Math.max(iMax, i);
        }
        return iMax;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int[] getModuleBitCount(BitMatrix bitMatrix, int i, int i2, boolean z, int i3, int i4) {
        int[] iArr = new int[8];
        int i5 = z ? 1 : -1;
        int i6 = 0;
        boolean z2 = z;
        while (true) {
            if (!z) {
                if (i3 < i) {
                    break;
                }
            } else {
                if (i3 >= i2) {
                    break;
                }
                if (i6 >= 8) {
                    break;
                }
                if (bitMatrix.get(i3, i4) == z2) {
                    iArr[i6] = iArr[i6] + 1;
                    i3 += i5;
                } else {
                    i6++;
                    z2 = !z2;
                }
            }
        }
        if (i6 != 8) {
            if (z) {
                i = i2;
            }
            if (i3 != i || i6 != 7) {
                return null;
            }
        }
        return iArr;
    }

    private static int getNumberOfECCodeWords(int i) {
        return 2 << i;
    }

    private static DetectionResultRowIndicatorColumn getRowIndicatorColumn(BitMatrix bitMatrix, BoundingBox boundingBox, ResultPoint resultPoint, boolean z, int i, int i2) {
        DetectionResultRowIndicatorColumn detectionResultRowIndicatorColumn = new DetectionResultRowIndicatorColumn(boundingBox, z);
        int i3 = 0;
        while (i3 < 2) {
            int i4 = i3 == 0 ? 1 : -1;
            int x = (int) resultPoint.getX();
            for (int y = (int) resultPoint.getY(); y <= boundingBox.getMaxY() && y >= boundingBox.getMinY(); y += i4) {
                Codeword codewordDetectCodeword = detectCodeword(bitMatrix, 0, bitMatrix.getWidth(), z, x, y, i, i2);
                if (codewordDetectCodeword != null) {
                    detectionResultRowIndicatorColumn.setCodeword(y, codewordDetectCodeword);
                    x = z ? codewordDetectCodeword.getStartX() : codewordDetectCodeword.getEndX();
                }
            }
            i3++;
        }
        return detectionResultRowIndicatorColumn;
    }

    private static int getStartColumn(DetectionResult detectionResult, int i, int i2, boolean z) {
        int i3 = z ? 1 : -1;
        int i4 = i - i3;
        Codeword codeword = isValidBarcodeColumn(detectionResult, i4) ? detectionResult.getDetectionResultColumn(i4).getCodeword(i2) : null;
        if (codeword != null) {
            return z ? codeword.getEndX() : codeword.getStartX();
        }
        Codeword codewordNearby = detectionResult.getDetectionResultColumn(i).getCodewordNearby(i2);
        if (codewordNearby != null) {
            return z ? codewordNearby.getStartX() : codewordNearby.getEndX();
        }
        if (isValidBarcodeColumn(detectionResult, i4)) {
            codewordNearby = detectionResult.getDetectionResultColumn(i4).getCodewordNearby(i2);
        }
        if (codewordNearby != null) {
            return z ? codewordNearby.getEndX() : codewordNearby.getStartX();
        }
        int i5 = 0;
        while (true) {
            i -= i3;
            if (!isValidBarcodeColumn(detectionResult, i)) {
                BoundingBox boundingBox = detectionResult.getBoundingBox();
                return z ? boundingBox.getMinX() : boundingBox.getMaxX();
            }
            for (Codeword codeword2 : detectionResult.getDetectionResultColumn(i).getCodewords()) {
                if (codeword2 != null) {
                    return ((codeword2.getEndX() - codeword2.getStartX()) * i3 * i5) + (z ? codeword2.getEndX() : codeword2.getStartX());
                }
            }
            i5++;
        }
    }

    private static boolean isValidBarcodeColumn(DetectionResult detectionResult, int i) {
        return i >= 0 && i <= detectionResult.getBarcodeColumnCount() + 1;
    }

    private static DetectionResult merge(DetectionResultRowIndicatorColumn detectionResultRowIndicatorColumn, DetectionResultRowIndicatorColumn detectionResultRowIndicatorColumn2) throws NotFoundException {
        BarcodeMetadata barcodeMetadata;
        if ((detectionResultRowIndicatorColumn == null && detectionResultRowIndicatorColumn2 == null) || (barcodeMetadata = getBarcodeMetadata(detectionResultRowIndicatorColumn, detectionResultRowIndicatorColumn2)) == null) {
            return null;
        }
        return new DetectionResult(barcodeMetadata, BoundingBox.merge(adjustBoundingBox(detectionResultRowIndicatorColumn), adjustBoundingBox(detectionResultRowIndicatorColumn2)));
    }

    public static String toString(BarcodeValue[][] barcodeValueArr) {
        Formatter formatter = new Formatter();
        for (int i = 0; i < barcodeValueArr.length; i++) {
            try {
                formatter.format("Row %2d: ", Integer.valueOf(i));
                int i2 = 0;
                while (true) {
                    BarcodeValue[] barcodeValueArr2 = barcodeValueArr[i];
                    if (i2 < barcodeValueArr2.length) {
                        BarcodeValue barcodeValue = barcodeValueArr2[i2];
                        if (barcodeValue.getValue().length == 0) {
                            formatter.format("        ", null);
                        } else {
                            formatter.format("%4d(%2d)", Integer.valueOf(barcodeValue.getValue()[0]), barcodeValue.getConfidence(barcodeValue.getValue()[0]));
                        }
                        i2++;
                    }
                }
                formatter.format("%n", new Object[0]);
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    try {
                        formatter.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                    throw th2;
                }
            }
        }
        String string = formatter.toString();
        formatter.close();
        return string;
    }

    private static void verifyCodewordCount(int[] iArr, int i) throws FormatException {
        if (iArr.length < 4) {
            throw FormatException.getFormatInstance();
        }
        int i2 = iArr[0];
        if (i2 > iArr.length) {
            throw FormatException.getFormatInstance();
        }
        if (i2 == 0) {
            if (i >= iArr.length) {
                throw FormatException.getFormatInstance();
            }
            iArr[0] = iArr.length - i;
        }
    }

    private static int getCodewordBucketNumber(int[] iArr) {
        return ((((iArr[0] - iArr[2]) + iArr[4]) - iArr[6]) + 9) % 9;
    }
}
