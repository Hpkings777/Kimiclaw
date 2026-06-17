package com.sun.mail.imap;

import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
class LengthCounter extends OutputStream {
    private int maxsize;
    private int size = 0;
    private byte[] buf = new byte[8192];

    public LengthCounter(int i) {
        this.maxsize = i;
    }

    public byte[] getBytes() {
        return this.buf;
    }

    public int getSize() {
        return this.size;
    }

    @Override // java.io.OutputStream
    public void write(int i) {
        int i2 = this.size;
        int i3 = i2 + 1;
        byte[] bArr = this.buf;
        if (bArr != null) {
            int i4 = this.maxsize;
            if (i3 > i4 && i4 >= 0) {
                this.buf = null;
            } else if (i3 > bArr.length) {
                byte[] bArr2 = new byte[Math.max(bArr.length << 1, i3)];
                System.arraycopy(this.buf, 0, bArr2, 0, this.size);
                this.buf = bArr2;
                bArr2[this.size] = (byte) i;
            } else {
                bArr[i2] = (byte) i;
            }
        }
        this.size = i3;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) {
        int i3;
        if (i < 0 || i > bArr.length || i2 < 0 || (i3 = i + i2) > bArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i2 == 0) {
            return;
        }
        int i4 = this.size;
        int i5 = i4 + i2;
        byte[] bArr2 = this.buf;
        if (bArr2 != null) {
            int i6 = this.maxsize;
            if (i5 > i6 && i6 >= 0) {
                this.buf = null;
            } else if (i5 > bArr2.length) {
                byte[] bArr3 = new byte[Math.max(bArr2.length << 1, i5)];
                System.arraycopy(this.buf, 0, bArr3, 0, this.size);
                this.buf = bArr3;
                System.arraycopy(bArr, i, bArr3, this.size, i2);
            } else {
                System.arraycopy(bArr, i, bArr2, i4, i2);
            }
        }
        this.size = i5;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }
}
