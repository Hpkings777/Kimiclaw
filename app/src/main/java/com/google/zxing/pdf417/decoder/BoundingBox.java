package com.google.zxing.pdf417.decoder;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

/* JADX INFO: loaded from: classes.dex */
final class BoundingBox {
    private final ResultPoint bottomLeft;
    private final ResultPoint bottomRight;
    private final BitMatrix image;
    private final int maxX;
    private final int maxY;
    private final int minX;
    private final int minY;
    private final ResultPoint topLeft;
    private final ResultPoint topRight;

    public BoundingBox(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4) throws NotFoundException {
        boolean z = resultPoint == null || resultPoint2 == null;
        boolean z2 = resultPoint3 == null || resultPoint4 == null;
        if (z && z2) {
            throw NotFoundException.getNotFoundInstance();
        }
        if (z) {
            resultPoint = new ResultPoint(0.0f, resultPoint3.getY());
            resultPoint2 = new ResultPoint(0.0f, resultPoint4.getY());
        } else if (z2) {
            resultPoint3 = new ResultPoint(bitMatrix.getWidth() - 1, resultPoint.getY());
            resultPoint4 = new ResultPoint(bitMatrix.getWidth() - 1, resultPoint2.getY());
        }
        this.image = bitMatrix;
        this.topLeft = resultPoint;
        this.bottomLeft = resultPoint2;
        this.topRight = resultPoint3;
        this.bottomRight = resultPoint4;
        this.minX = (int) Math.min(resultPoint.getX(), resultPoint2.getX());
        this.maxX = (int) Math.max(resultPoint3.getX(), resultPoint4.getX());
        this.minY = (int) Math.min(resultPoint.getY(), resultPoint3.getY());
        this.maxY = (int) Math.max(resultPoint2.getY(), resultPoint4.getY());
    }

    public static BoundingBox merge(BoundingBox boundingBox, BoundingBox boundingBox2) throws NotFoundException {
        return boundingBox == null ? boundingBox2 : boundingBox2 == null ? boundingBox : new BoundingBox(boundingBox.image, boundingBox.topLeft, boundingBox.bottomLeft, boundingBox2.topRight, boundingBox2.bottomRight);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x005d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public BoundingBox addMissingRows(int i, int i2, boolean z) throws NotFoundException {
        ResultPoint resultPoint;
        ResultPoint resultPoint2;
        ResultPoint resultPoint3;
        ResultPoint resultPoint4;
        ResultPoint resultPoint5 = this.topLeft;
        ResultPoint resultPoint6 = this.bottomLeft;
        ResultPoint resultPoint7 = this.topRight;
        ResultPoint resultPoint8 = this.bottomRight;
        if (i > 0) {
            ResultPoint resultPoint9 = z ? resultPoint5 : resultPoint7;
            int y = ((int) resultPoint9.getY()) - i;
            if (y < 0) {
                y = 0;
            }
            ResultPoint resultPoint10 = new ResultPoint(resultPoint9.getX(), y);
            if (!z) {
                resultPoint2 = resultPoint10;
                resultPoint = resultPoint5;
                if (i2 <= 0) {
                    ResultPoint resultPoint11 = z ? this.bottomLeft : this.bottomRight;
                    int y2 = ((int) resultPoint11.getY()) + i2;
                    if (y2 >= this.image.getHeight()) {
                        y2 = this.image.getHeight() - 1;
                    }
                    ResultPoint resultPoint12 = new ResultPoint(resultPoint11.getX(), y2);
                    if (!z) {
                        resultPoint4 = resultPoint12;
                        resultPoint3 = resultPoint6;
                        return new BoundingBox(this.image, resultPoint, resultPoint3, resultPoint2, resultPoint4);
                    }
                    resultPoint3 = resultPoint12;
                } else {
                    resultPoint3 = resultPoint6;
                }
                resultPoint4 = resultPoint8;
                return new BoundingBox(this.image, resultPoint, resultPoint3, resultPoint2, resultPoint4);
            }
            resultPoint = resultPoint10;
        } else {
            resultPoint = resultPoint5;
        }
        resultPoint2 = resultPoint7;
        if (i2 <= 0) {
        }
        resultPoint4 = resultPoint8;
        return new BoundingBox(this.image, resultPoint, resultPoint3, resultPoint2, resultPoint4);
    }

    public ResultPoint getBottomLeft() {
        return this.bottomLeft;
    }

    public ResultPoint getBottomRight() {
        return this.bottomRight;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMinY() {
        return this.minY;
    }

    public ResultPoint getTopLeft() {
        return this.topLeft;
    }

    public ResultPoint getTopRight() {
        return this.topRight;
    }

    public BoundingBox(BoundingBox boundingBox) {
        this.image = boundingBox.image;
        this.topLeft = boundingBox.topLeft;
        this.bottomLeft = boundingBox.bottomLeft;
        this.topRight = boundingBox.topRight;
        this.bottomRight = boundingBox.bottomRight;
        this.minX = boundingBox.minX;
        this.maxX = boundingBox.maxX;
        this.minY = boundingBox.minY;
        this.maxY = boundingBox.maxY;
    }
}
