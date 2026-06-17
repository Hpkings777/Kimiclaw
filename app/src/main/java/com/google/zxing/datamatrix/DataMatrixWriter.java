package com.google.zxing.datamatrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Dimension;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.encoder.DefaultPlacement;
import com.google.zxing.datamatrix.encoder.ErrorCorrection;
import com.google.zxing.datamatrix.encoder.HighLevelEncoder;
import com.google.zxing.datamatrix.encoder.MinimalEncoder;
import com.google.zxing.datamatrix.encoder.SymbolInfo;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import java.nio.charset.Charset;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class DataMatrixWriter implements Writer {
    private static BitMatrix convertByteMatrixToBitMatrix(ByteMatrix byteMatrix, int i, int i2) {
        BitMatrix bitMatrix;
        int width = byteMatrix.getWidth();
        int height = byteMatrix.getHeight();
        int iMax = Math.max(i, width);
        int iMax2 = Math.max(i2, height);
        int iMin = Math.min(iMax / width, iMax2 / height);
        int i3 = (iMax - (width * iMin)) / 2;
        int i4 = (iMax2 - (height * iMin)) / 2;
        if (i2 < height || i < width) {
            bitMatrix = new BitMatrix(width, height);
            i3 = 0;
            i4 = 0;
        } else {
            bitMatrix = new BitMatrix(i, i2);
        }
        bitMatrix.clear();
        int i5 = 0;
        while (i5 < height) {
            int i6 = i3;
            int i7 = 0;
            while (i7 < width) {
                if (byteMatrix.get(i7, i5) == 1) {
                    bitMatrix.setRegion(i6, i4, iMin, iMin);
                }
                i7++;
                i6 += iMin;
            }
            i5++;
            i4 += iMin;
        }
        return bitMatrix;
    }

    private static BitMatrix encodeLowLevel(DefaultPlacement defaultPlacement, SymbolInfo symbolInfo, int i, int i2) {
        int symbolDataWidth = symbolInfo.getSymbolDataWidth();
        int symbolDataHeight = symbolInfo.getSymbolDataHeight();
        ByteMatrix byteMatrix = new ByteMatrix(symbolInfo.getSymbolWidth(), symbolInfo.getSymbolHeight());
        int i3 = 0;
        for (int i4 = 0; i4 < symbolDataHeight; i4++) {
            if (i4 % symbolInfo.matrixHeight == 0) {
                int i5 = 0;
                for (int i6 = 0; i6 < symbolInfo.getSymbolWidth(); i6++) {
                    byteMatrix.set(i5, i3, i6 % 2 == 0);
                    i5++;
                }
                i3++;
            }
            int i7 = 0;
            for (int i8 = 0; i8 < symbolDataWidth; i8++) {
                if (i8 % symbolInfo.matrixWidth == 0) {
                    byteMatrix.set(i7, i3, true);
                    i7++;
                }
                byteMatrix.set(i7, i3, defaultPlacement.getBit(i8, i4));
                int i9 = i7 + 1;
                int i10 = symbolInfo.matrixWidth;
                if (i8 % i10 == i10 - 1) {
                    byteMatrix.set(i9, i3, i4 % 2 == 0);
                    i7 += 2;
                } else {
                    i7 = i9;
                }
            }
            int i11 = i3 + 1;
            int i12 = symbolInfo.matrixHeight;
            if (i4 % i12 == i12 - 1) {
                int i13 = 0;
                for (int i14 = 0; i14 < symbolInfo.getSymbolWidth(); i14++) {
                    byteMatrix.set(i13, i11, true);
                    i13++;
                }
                i3 += 2;
            } else {
                i3 = i11;
            }
        }
        return convertByteMatrixToBitMatrix(byteMatrix, i, i2);
    }

    @Override // com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2) {
        return encode(str, barcodeFormat, i, i2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0089  */
    @Override // com.google.zxing.Writer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) {
        Dimension dimension;
        Dimension dimension2;
        String strEncodeHighLevel;
        if (str.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        }
        if (barcodeFormat != BarcodeFormat.DATA_MATRIX) {
            throw new IllegalArgumentException("Can only encode DATA_MATRIX, but got " + barcodeFormat);
        }
        if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("Requested dimensions can't be negative: " + i + 'x' + i2);
        }
        SymbolShapeHint symbolShapeHint = SymbolShapeHint.FORCE_NONE;
        if (map != null) {
            SymbolShapeHint symbolShapeHint2 = (SymbolShapeHint) map.get(EncodeHintType.DATA_MATRIX_SHAPE);
            if (symbolShapeHint2 != null) {
                symbolShapeHint = symbolShapeHint2;
            }
            dimension = (Dimension) map.get(EncodeHintType.MIN_SIZE);
            if (dimension == null) {
                dimension = null;
            }
            dimension2 = (Dimension) map.get(EncodeHintType.MAX_SIZE);
            if (dimension2 == null) {
                dimension2 = null;
            }
        } else {
            dimension = null;
            dimension2 = null;
        }
        boolean z = false;
        if (map != null) {
            EncodeHintType encodeHintType = EncodeHintType.DATA_MATRIX_COMPACT;
            if (map.containsKey(encodeHintType) && Boolean.parseBoolean(map.get(encodeHintType).toString())) {
                EncodeHintType encodeHintType2 = EncodeHintType.GS1_FORMAT;
                if (map.containsKey(encodeHintType2) && Boolean.parseBoolean(map.get(encodeHintType2).toString())) {
                    z = true;
                }
                EncodeHintType encodeHintType3 = EncodeHintType.CHARACTER_SET;
                strEncodeHighLevel = MinimalEncoder.encodeHighLevel(str, map.containsKey(encodeHintType3) ? Charset.forName(map.get(encodeHintType3).toString()) : null, z ? 29 : -1, symbolShapeHint);
            } else {
                if (map != null) {
                    EncodeHintType encodeHintType4 = EncodeHintType.FORCE_C40;
                    if (map.containsKey(encodeHintType4) && Boolean.parseBoolean(map.get(encodeHintType4).toString())) {
                        z = true;
                    }
                }
                strEncodeHighLevel = HighLevelEncoder.encodeHighLevel(str, symbolShapeHint, dimension, dimension2, z);
            }
        }
        SymbolInfo symbolInfoLookup = SymbolInfo.lookup(strEncodeHighLevel.length(), symbolShapeHint, dimension, dimension2, true);
        DefaultPlacement defaultPlacement = new DefaultPlacement(ErrorCorrection.encodeECC200(strEncodeHighLevel, symbolInfoLookup), symbolInfoLookup.getSymbolDataWidth(), symbolInfoLookup.getSymbolDataHeight());
        defaultPlacement.place();
        return encodeLowLevel(defaultPlacement, symbolInfoLookup, i, i2);
    }
}
