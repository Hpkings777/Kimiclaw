package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class MonochromeRectangleDetector {
    private static final int MAX_MODULES = 32;
    private final BitMatrix image;

    public MonochromeRectangleDetector(BitMatrix bitMatrix) {
        this.image = bitMatrix;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0020  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0031 A[EDGE_INSN: B:70:0x0031->B:22:0x0031 BREAK  A[LOOP:1: B:13:0x001c->B:73:0x001c], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0067 A[EDGE_INSN: B:86:0x0067->B:47:0x0067 BREAK  A[LOOP:3: B:38:0x0053->B:91:0x0053], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int[] blackWhiteRange(int i, int i2, int i3, int i4, boolean z) {
        int i5;
        int i6;
        int i7 = (i3 + i4) / 2;
        int i8 = i7;
        while (i8 >= i3) {
            BitMatrix bitMatrix = this.image;
            if (!z) {
                if (!bitMatrix.get(i, i8)) {
                    i6 = i8;
                    while (true) {
                        i6--;
                        if (i6 >= i3) {
                        }
                    }
                    int i9 = i8 - i6;
                    if (i6 < i3) {
                        break;
                    }
                    break;
                    break;
                }
                i8--;
            } else if (bitMatrix.get(i8, i)) {
                i8--;
            } else {
                i6 = i8;
                while (true) {
                    i6--;
                    if (i6 >= i3) {
                        break;
                    }
                    BitMatrix bitMatrix2 = this.image;
                    if (z) {
                        if (bitMatrix2.get(i6, i)) {
                            break;
                        }
                    } else if (bitMatrix2.get(i, i6)) {
                        break;
                    }
                }
                int i92 = i8 - i6;
                if (i6 < i3 || i92 > i2) {
                    break;
                }
                i8 = i6;
            }
        }
        int i10 = i8 + 1;
        while (i7 < i4) {
            BitMatrix bitMatrix3 = this.image;
            if (!z) {
                if (!bitMatrix3.get(i, i7)) {
                    i5 = i7;
                    while (true) {
                        i5++;
                        if (i5 < i4) {
                        }
                    }
                    int i11 = i5 - i7;
                    if (i5 >= i4) {
                        break;
                    }
                    break;
                    break;
                }
                i7++;
            } else if (bitMatrix3.get(i7, i)) {
                i7++;
            } else {
                i5 = i7;
                while (true) {
                    i5++;
                    if (i5 < i4) {
                        break;
                    }
                    BitMatrix bitMatrix4 = this.image;
                    if (z) {
                        if (bitMatrix4.get(i5, i)) {
                            break;
                        }
                    } else if (bitMatrix4.get(i, i5)) {
                        break;
                    }
                }
                int i112 = i5 - i7;
                if (i5 >= i4 || i112 > i2) {
                    break;
                }
                i7 = i5;
            }
        }
        int i12 = i7 - 1;
        if (i12 > i10) {
            return new int[]{i10, i12};
        }
        return null;
    }

    private ResultPoint findCornerFromCenter(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) throws NotFoundException {
        int[] iArr = null;
        int i10 = i;
        int i11 = i5;
        while (i11 < i8 && i11 >= i7 && i10 < i4 && i10 >= i3) {
            int[] iArrBlackWhiteRange = i2 == 0 ? blackWhiteRange(i11, i9, i3, i4, true) : blackWhiteRange(i10, i9, i7, i8, false);
            if (iArrBlackWhiteRange == null) {
                if (iArr == null) {
                    throw NotFoundException.getNotFoundInstance();
                }
                if (i2 == 0) {
                    int i12 = i11 - i6;
                    int i13 = iArr[0];
                    if (i13 >= i) {
                        return new ResultPoint(iArr[1], i12);
                    }
                    if (iArr[1] > i) {
                        return new ResultPoint(iArr[i6 <= 0 ? (char) 1 : (char) 0], i12);
                    }
                    return new ResultPoint(i13, i12);
                }
                int i14 = i10 - i2;
                int i15 = iArr[0];
                if (i15 >= i5) {
                    return new ResultPoint(i14, iArr[1]);
                }
                if (iArr[1] > i5) {
                    return new ResultPoint(i14, iArr[i2 >= 0 ? (char) 1 : (char) 0]);
                }
                return new ResultPoint(i14, i15);
            }
            i11 += i6;
            i10 += i2;
            iArr = iArrBlackWhiteRange;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    public ResultPoint[] detect() throws NotFoundException {
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int i = height / 2;
        int i2 = width / 2;
        int iMax = Math.max(1, height / 256);
        int iMax2 = Math.max(1, width / 256);
        int i3 = -iMax;
        int i4 = i2 / 2;
        int y = ((int) findCornerFromCenter(i2, 0, 0, width, i, i3, 0, height, i4).getY()) - 1;
        int i5 = i / 2;
        ResultPoint resultPointFindCornerFromCenter = findCornerFromCenter(i2, -iMax2, 0, width, i, 0, y, height, i5);
        int x = ((int) resultPointFindCornerFromCenter.getX()) - 1;
        ResultPoint resultPointFindCornerFromCenter2 = findCornerFromCenter(i2, iMax2, x, width, i, 0, y, height, i5);
        int x2 = ((int) resultPointFindCornerFromCenter2.getX()) + 1;
        ResultPoint resultPointFindCornerFromCenter3 = findCornerFromCenter(i2, 0, x, x2, i, iMax, y, height, i4);
        return new ResultPoint[]{findCornerFromCenter(i2, 0, x, x2, i, i3, y, ((int) resultPointFindCornerFromCenter3.getY()) + 1, i2 / 4), resultPointFindCornerFromCenter, resultPointFindCornerFromCenter2, resultPointFindCornerFromCenter3};
    }
}
