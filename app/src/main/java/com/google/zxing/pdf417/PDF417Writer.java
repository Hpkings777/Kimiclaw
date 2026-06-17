package com.google.zxing.pdf417;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.encoder.Compaction;
import com.google.zxing.pdf417.encoder.Dimensions;
import com.google.zxing.pdf417.encoder.PDF417;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class PDF417Writer implements Writer {
    private static final int DEFAULT_ERROR_CORRECTION_LEVEL = 2;
    private static final int WHITE_SPACE = 30;

    private static BitMatrix bitMatrixFromBitArray(byte[][] bArr, int i) {
        int i2 = i * 2;
        BitMatrix bitMatrix = new BitMatrix(bArr[0].length + i2, bArr.length + i2);
        bitMatrix.clear();
        int height = (bitMatrix.getHeight() - i) - 1;
        int i3 = 0;
        while (i3 < bArr.length) {
            byte[] bArr2 = bArr[i3];
            for (int i4 = 0; i4 < bArr[0].length; i4++) {
                if (bArr2[i4] == 1) {
                    bitMatrix.set(i4 + i, height);
                }
            }
            i3++;
            height--;
        }
        return bitMatrix;
    }

    private static BitMatrix bitMatrixFromEncoder(PDF417 pdf417, String str, int i, int i2, int i3, int i4, boolean z) throws WriterException {
        boolean z2;
        pdf417.generateBarcodeLogic(str, i, z);
        byte[][] scaledMatrix = pdf417.getBarcodeMatrix().getScaledMatrix(1, 4);
        if ((i3 > i2) != (scaledMatrix[0].length < scaledMatrix.length)) {
            scaledMatrix = rotateArray(scaledMatrix);
            z2 = true;
        } else {
            z2 = false;
        }
        int iMin = Math.min(i2 / scaledMatrix[0].length, i3 / scaledMatrix.length);
        if (iMin <= 1) {
            return bitMatrixFromBitArray(scaledMatrix, i4);
        }
        byte[][] scaledMatrix2 = pdf417.getBarcodeMatrix().getScaledMatrix(iMin, iMin * 4);
        if (z2) {
            scaledMatrix2 = rotateArray(scaledMatrix2);
        }
        return bitMatrixFromBitArray(scaledMatrix2, i4);
    }

    private static byte[][] rotateArray(byte[][] bArr) {
        byte[][] bArr2 = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, bArr[0].length, bArr.length);
        for (int i = 0; i < bArr.length; i++) {
            int length = (bArr.length - i) - 1;
            for (int i2 = 0; i2 < bArr[0].length; i2++) {
                bArr2[i2][length] = bArr[i][i2];
            }
        }
        return bArr2;
    }

    @Override // com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) throws WriterException {
        if (barcodeFormat != BarcodeFormat.PDF_417) {
            throw new IllegalArgumentException("Can only encode PDF_417, but got " + barcodeFormat);
        }
        PDF417 pdf417 = new PDF417();
        boolean z = false;
        if (map != null) {
            EncodeHintType encodeHintType = EncodeHintType.PDF417_COMPACT;
            if (map.containsKey(encodeHintType)) {
                pdf417.setCompact(Boolean.parseBoolean(map.get(encodeHintType).toString()));
            }
            EncodeHintType encodeHintType2 = EncodeHintType.PDF417_COMPACTION;
            if (map.containsKey(encodeHintType2)) {
                pdf417.setCompaction(Compaction.valueOf(map.get(encodeHintType2).toString()));
            }
            EncodeHintType encodeHintType3 = EncodeHintType.PDF417_DIMENSIONS;
            if (map.containsKey(encodeHintType3)) {
                Dimensions dimensions = (Dimensions) map.get(encodeHintType3);
                pdf417.setDimensions(dimensions.getMaxCols(), dimensions.getMinCols(), dimensions.getMaxRows(), dimensions.getMinRows());
            }
            EncodeHintType encodeHintType4 = EncodeHintType.MARGIN;
            i = map.containsKey(encodeHintType4) ? Integer.parseInt(map.get(encodeHintType4).toString()) : 30;
            EncodeHintType encodeHintType5 = EncodeHintType.ERROR_CORRECTION;
            i = map.containsKey(encodeHintType5) ? Integer.parseInt(map.get(encodeHintType5).toString()) : 2;
            EncodeHintType encodeHintType6 = EncodeHintType.CHARACTER_SET;
            if (map.containsKey(encodeHintType6)) {
                pdf417.setEncoding(Charset.forName(map.get(encodeHintType6).toString()));
            }
            EncodeHintType encodeHintType7 = EncodeHintType.PDF417_AUTO_ECI;
            if (map.containsKey(encodeHintType7) && Boolean.parseBoolean(map.get(encodeHintType7).toString())) {
                z = true;
            }
        }
        return bitMatrixFromEncoder(pdf417, str, i, i, i2, i, z);
    }

    @Override // com.google.zxing.Writer
    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2) throws WriterException {
        return encode(str, barcodeFormat, i, i2, null);
    }
}
