package com.google.zxing.pdf417.detector;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class Detector {
    private static final int BARCODE_MIN_HEIGHT = 10;
    private static final float MAX_AVG_VARIANCE = 0.42f;
    private static final float MAX_INDIVIDUAL_VARIANCE = 0.8f;
    private static final int MAX_PATTERN_DRIFT = 5;
    private static final int MAX_PIXEL_DRIFT = 3;
    private static final float MAX_STOP_PATTERN_HEIGHT_VARIANCE = 0.5f;
    private static final int ROW_STEP = 5;
    private static final int SKIPPED_ROW_COUNT_MAX = 25;
    private static final int[] INDEXES_START_PATTERN = {0, 4, 1, 5};
    private static final int[] INDEXES_STOP_PATTERN = {6, 2, 7, 3};
    private static final int[] START_PATTERN = {8, 1, 1, 1, 1, 1, 1, 3};
    private static final int[] STOP_PATTERN = {7, 1, 1, 3, 1, 1, 1, 2, 1};
    private static final int[] ROTATIONS = {0, 180, 270, 90};

    private Detector() {
    }

    private static BitMatrix applyRotation(BitMatrix bitMatrix, int i) {
        if (i % 360 == 0) {
            return bitMatrix;
        }
        BitMatrix bitMatrixM228clone = bitMatrix.m228clone();
        bitMatrixM228clone.rotate(i);
        return bitMatrixM228clone;
    }

    private static void copyToResult(ResultPoint[] resultPointArr, ResultPoint[] resultPointArr2, int[] iArr) {
        for (int i = 0; i < iArr.length; i++) {
            resultPointArr[iArr[i]] = resultPointArr2[i];
        }
    }

    public static PDF417DetectorResult detect(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map, boolean z) throws NotFoundException {
        BitMatrix blackMatrix = binaryBitmap.getBlackMatrix();
        for (int i : ROTATIONS) {
            BitMatrix bitMatrixApplyRotation = applyRotation(blackMatrix, i);
            List<ResultPoint[]> listDetect = detect(z, bitMatrixApplyRotation);
            if (!listDetect.isEmpty()) {
                return new PDF417DetectorResult(bitMatrixApplyRotation, listDetect, i);
            }
        }
        return new PDF417DetectorResult(blackMatrix, new ArrayList(), 0);
    }

    private static int[] findGuardPattern(BitMatrix bitMatrix, int i, int i2, int i3, int[] iArr, int[] iArr2) {
        Arrays.fill(iArr2, 0, iArr2.length, 0);
        int i4 = 0;
        while (bitMatrix.get(i, i2) && i > 0) {
            int i5 = i4 + 1;
            if (i4 >= 3) {
                break;
            }
            i--;
            i4 = i5;
        }
        int length = iArr.length;
        int i6 = i;
        int i7 = 0;
        boolean z = false;
        while (i < i3) {
            if (bitMatrix.get(i, i2) != z) {
                iArr2[i7] = iArr2[i7] + 1;
            } else {
                if (i7 != length - 1) {
                    i7++;
                } else {
                    if (patternMatchVariance(iArr2, iArr) < MAX_AVG_VARIANCE) {
                        return new int[]{i6, i};
                    }
                    i6 += iArr2[0] + iArr2[1];
                    int i8 = i7 - 1;
                    System.arraycopy(iArr2, 2, iArr2, 0, i8);
                    iArr2[i8] = 0;
                    iArr2[i7] = 0;
                    i7--;
                }
                iArr2[i7] = 1;
                z = !z;
            }
            i++;
        }
        if (i7 != length - 1 || patternMatchVariance(iArr2, iArr) >= MAX_AVG_VARIANCE) {
            return null;
        }
        return new int[]{i6, i - 1};
    }

    private static ResultPoint[] findRowsWithPattern(BitMatrix bitMatrix, int i, int i2, int i3, int i4, int i5, int[] iArr) {
        int i6;
        boolean z;
        int[] iArrFindGuardPattern;
        ResultPoint[] resultPointArr = new ResultPoint[4];
        int[] iArr2 = iArr;
        int[] iArr3 = new int[iArr2.length];
        int i7 = i3;
        while (true) {
            if (i7 >= i) {
                i6 = i7;
                z = false;
                break;
            }
            int[] iArrFindGuardPattern2 = findGuardPattern(bitMatrix, i4, i7, i2, iArr2, iArr3);
            if (iArrFindGuardPattern2 != null) {
                int[] iArr4 = iArrFindGuardPattern2;
                while (true) {
                    i6 = i7;
                    if (i6 <= 0 || (iArrFindGuardPattern = findGuardPattern(bitMatrix, i4, i6 - 1, i2, iArr, iArr3)) == null) {
                        break;
                    }
                    iArr4 = iArrFindGuardPattern;
                }
                float f = i6;
                resultPointArr[0] = new ResultPoint(iArr4[0], f);
                resultPointArr[1] = new ResultPoint(iArr4[1], f);
                z = true;
            } else {
                i7 += 5;
                iArr2 = iArr;
            }
        }
        int i8 = i6 + 1;
        if (z) {
            int[] iArr5 = {(int) resultPointArr[0].getX(), (int) resultPointArr[1].getX()};
            int i9 = i8;
            int i10 = 0;
            while (i9 < i) {
                int[] iArrFindGuardPattern3 = findGuardPattern(bitMatrix, iArr5[0], i9, i2, iArr, iArr3);
                if (iArrFindGuardPattern3 != null && Math.abs(iArr5[0] - iArrFindGuardPattern3[0]) < 5 && Math.abs(iArr5[1] - iArrFindGuardPattern3[1]) < 5) {
                    iArr5 = iArrFindGuardPattern3;
                    i10 = 0;
                } else {
                    if (i10 > SKIPPED_ROW_COUNT_MAX) {
                        break;
                    }
                    i10++;
                }
                i9++;
            }
            i8 = i9 - (i10 + 1);
            float f2 = i8;
            resultPointArr[2] = new ResultPoint(iArr5[0], f2);
            resultPointArr[3] = new ResultPoint(iArr5[1], f2);
        }
        if (i8 - i6 < i5) {
            Arrays.fill(resultPointArr, (Object) null);
        }
        return resultPointArr;
    }

    private static ResultPoint[] findVertices(BitMatrix bitMatrix, int i, int i2) {
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();
        ResultPoint[] resultPointArr = new ResultPoint[8];
        int i3 = i;
        int i4 = i2;
        copyToResult(resultPointArr, findRowsWithPattern(bitMatrix, height, width, i3, i4, 10, START_PATTERN), INDEXES_START_PATTERN);
        ResultPoint resultPoint = resultPointArr[4];
        if (resultPoint != null) {
            int x = (int) resultPoint.getX();
            int y = (int) resultPointArr[4].getY();
            iMax = resultPointArr[5] != null ? (int) Math.max((((int) r8.getY()) - y) * MAX_STOP_PATTERN_HEIGHT_VARIANCE, 10.0f) : 10;
            i3 = y;
            i4 = x;
        }
        copyToResult(resultPointArr, findRowsWithPattern(bitMatrix, height, width, i3, i4, iMax, STOP_PATTERN), INDEXES_STOP_PATTERN);
        return resultPointArr;
    }

    private static float patternMatchVariance(int[] iArr, int[] iArr2) {
        int length = iArr.length;
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            i += iArr[i3];
            i2 += iArr2[i3];
        }
        if (i < i2) {
            return Float.POSITIVE_INFINITY;
        }
        float f = i;
        float f2 = f / i2;
        float f3 = MAX_INDIVIDUAL_VARIANCE * f2;
        float f4 = 0.0f;
        for (int i4 = 0; i4 < length; i4++) {
            float f5 = iArr2[i4] * f2;
            float f6 = iArr[i4];
            float f7 = f6 > f5 ? f6 - f5 : f5 - f6;
            if (f7 > f3) {
                return Float.POSITIVE_INFINITY;
            }
            f4 += f7;
        }
        return f4 / f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001d, code lost:
    
        if (r4 != 0) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0020, code lost:
    
        r3 = r0.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0028, code lost:
    
        if (r3.hasNext() == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002a, code lost:
    
        r4 = (com.google.zxing.ResultPoint[]) r3.next();
        r7 = r4[1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0032, code lost:
    
        if (r7 == null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0034, code lost:
    
        r2 = (int) java.lang.Math.max(r2, r7.getY());
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x003e, code lost:
    
        r4 = r4[3];
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0040, code lost:
    
        if (r4 == null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0042, code lost:
    
        r2 = java.lang.Math.max(r2, (int) r4.getY());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static List<ResultPoint[]> detect(boolean z, BitMatrix bitMatrix) {
        int x;
        float y;
        ArrayList arrayList = new ArrayList();
        int iMax = 0;
        int i = 0;
        loop0: while (true) {
            int i2 = i;
            while (true) {
                if (iMax >= bitMatrix.getHeight()) {
                    break loop0;
                }
                ResultPoint[] resultPointArrFindVertices = findVertices(bitMatrix, iMax, i);
                if (resultPointArrFindVertices[0] == null && resultPointArrFindVertices[3] == null) {
                    break;
                }
                arrayList.add(resultPointArrFindVertices);
                if (!z) {
                    break loop0;
                }
                ResultPoint resultPoint = resultPointArrFindVertices[2];
                if (resultPoint != null) {
                    x = (int) resultPoint.getX();
                    y = resultPointArrFindVertices[2].getY();
                } else {
                    x = (int) resultPointArrFindVertices[4].getX();
                    y = resultPointArrFindVertices[4].getY();
                }
                iMax = (int) y;
                i = x;
                i2 = 1;
            }
            iMax += 5;
            i = 0;
        }
        return arrayList;
    }
}
