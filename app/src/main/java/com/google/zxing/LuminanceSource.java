package com.google.zxing;

import com.google.common.primitives.UnsignedBytes;
import org.apache.commons.io.FilenameUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class LuminanceSource {
    private final int height;
    private final int width;

    public LuminanceSource(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public LuminanceSource crop(int i, int i2, int i3, int i4) {
        throw new UnsupportedOperationException("This luminance source does not support cropping.");
    }

    public final int getHeight() {
        return this.height;
    }

    public abstract byte[] getMatrix();

    public abstract byte[] getRow(int i, byte[] bArr);

    public final int getWidth() {
        return this.width;
    }

    public LuminanceSource invert() {
        return new InvertedLuminanceSource(this);
    }

    public boolean isCropSupported() {
        return false;
    }

    public boolean isRotateSupported() {
        return false;
    }

    public LuminanceSource rotateCounterClockwise() {
        throw new UnsupportedOperationException("This luminance source does not support rotation by 90 degrees.");
    }

    public LuminanceSource rotateCounterClockwise45() {
        throw new UnsupportedOperationException("This luminance source does not support rotation by 45 degrees.");
    }

    public final String toString() {
        int i = this.width;
        byte[] row = new byte[i];
        StringBuilder sb = new StringBuilder((i + 1) * this.height);
        for (int i2 = 0; i2 < this.height; i2++) {
            row = getRow(i2, row);
            for (int i3 = 0; i3 < this.width; i3++) {
                int i4 = row[i3] & UnsignedBytes.MAX_VALUE;
                sb.append(i4 < 64 ? '#' : i4 < 128 ? '+' : i4 < 192 ? FilenameUtils.EXTENSION_SEPARATOR : ' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
