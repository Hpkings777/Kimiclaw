package com.sun.mail.util;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

/* JADX INFO: loaded from: classes.dex */
public class BASE64EncoderStream extends FilterOutputStream {
    private static byte[] newline = {Ascii.CR, 10};
    private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', IOUtils.DIR_SEPARATOR_UNIX};
    private byte[] buffer;
    private int bufsize;
    private int bytesPerLine;
    private int count;
    private int lineLimit;
    private boolean noCRLF;
    private byte[] outbuf;

    public BASE64EncoderStream(OutputStream outputStream, int i) {
        super(outputStream);
        this.bufsize = 0;
        this.count = 0;
        this.noCRLF = false;
        this.buffer = new byte[3];
        if (i == Integer.MAX_VALUE || i < 4) {
            this.noCRLF = true;
            i = 76;
        }
        int i2 = (i / 4) * 4;
        this.bytesPerLine = i2;
        this.lineLimit = (i2 / 4) * 3;
        if (this.noCRLF) {
            this.outbuf = new byte[i2];
            return;
        }
        byte[] bArr = new byte[i2 + 2];
        this.outbuf = bArr;
        bArr[i2] = Ascii.CR;
        bArr[i2 + 1] = 10;
    }

    private void encode() throws IOException {
        int iEncodedSize = encodedSize(this.bufsize);
        ((FilterOutputStream) this).out.write(encode(this.buffer, 0, this.bufsize, this.outbuf), 0, iEncodedSize);
        int i = this.count + iEncodedSize;
        this.count = i;
        if (i >= this.bytesPerLine) {
            if (!this.noCRLF) {
                ((FilterOutputStream) this).out.write(newline);
            }
            this.count = 0;
        }
    }

    private static int encodedSize(int i) {
        return ((i + 2) / 3) * 4;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        try {
            flush();
            if (this.count > 0 && !this.noCRLF) {
                ((FilterOutputStream) this).out.write(newline);
                ((FilterOutputStream) this).out.flush();
            }
            ((FilterOutputStream) this).out.close();
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public synchronized void flush() throws IOException {
        try {
            if (this.bufsize > 0) {
                encode();
                this.bufsize = 0;
            }
            ((FilterOutputStream) this).out.flush();
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
        int i3 = i2 + i;
        while (this.bufsize != 0 && i < i3) {
            try {
                write(bArr[i]);
                i++;
            } catch (Throwable th) {
                throw th;
            }
        }
        int i4 = ((this.bytesPerLine - this.count) / 4) * 3;
        int i5 = i + i4;
        if (i5 <= i3) {
            int iEncodedSize = encodedSize(i4);
            if (!this.noCRLF) {
                byte[] bArr2 = this.outbuf;
                int i6 = iEncodedSize + 1;
                bArr2[iEncodedSize] = Ascii.CR;
                iEncodedSize += 2;
                bArr2[i6] = 10;
            }
            ((FilterOutputStream) this).out.write(encode(bArr, i, i4, this.outbuf), 0, iEncodedSize);
            this.count = 0;
            i = i5;
        }
        while (true) {
            int i7 = this.lineLimit;
            if (i + i7 > i3) {
                break;
            }
            ((FilterOutputStream) this).out.write(encode(bArr, i, i7, this.outbuf));
            i += this.lineLimit;
        }
        if (i + 3 <= i3) {
            int i8 = ((i3 - i) / 3) * 3;
            int iEncodedSize2 = encodedSize(i8);
            ((FilterOutputStream) this).out.write(encode(bArr, i, i8, this.outbuf), 0, iEncodedSize2);
            i += i8;
            this.count += iEncodedSize2;
        }
        while (i < i3) {
            write(bArr[i]);
            i++;
        }
    }

    public static byte[] encode(byte[] bArr) {
        return bArr.length == 0 ? bArr : encode(bArr, 0, bArr.length, null);
    }

    private static byte[] encode(byte[] bArr, int i, int i2, byte[] bArr2) {
        if (bArr2 == null) {
            bArr2 = new byte[encodedSize(i2)];
        }
        int i3 = 0;
        while (i2 >= 3) {
            int i4 = i + 2;
            int i5 = ((bArr[i + 1] & UnsignedBytes.MAX_VALUE) | ((bArr[i] & UnsignedBytes.MAX_VALUE) << 8)) << 8;
            i += 3;
            int i6 = i5 | (bArr[i4] & UnsignedBytes.MAX_VALUE);
            char[] cArr = pem_array;
            bArr2[i3 + 3] = (byte) cArr[i6 & 63];
            bArr2[i3 + 2] = (byte) cArr[(i6 >> 6) & 63];
            bArr2[i3 + 1] = (byte) cArr[(i6 >> 12) & 63];
            bArr2[i3] = (byte) cArr[(i6 >> 18) & 63];
            i2 -= 3;
            i3 += 4;
        }
        if (i2 == 1) {
            int i7 = (bArr[i] & UnsignedBytes.MAX_VALUE) << 4;
            bArr2[i3 + 3] = 61;
            bArr2[i3 + 2] = 61;
            char[] cArr2 = pem_array;
            bArr2[i3 + 1] = (byte) cArr2[i7 & 63];
            bArr2[i3] = (byte) cArr2[(i7 >> 6) & 63];
            return bArr2;
        }
        if (i2 == 2) {
            int i8 = ((bArr[i + 1] & UnsignedBytes.MAX_VALUE) | ((bArr[i] & UnsignedBytes.MAX_VALUE) << 8)) << 2;
            bArr2[i3 + 3] = 61;
            char[] cArr3 = pem_array;
            bArr2[i3 + 2] = (byte) cArr3[i8 & 63];
            bArr2[i3 + 1] = (byte) cArr3[(i8 >> 6) & 63];
            bArr2[i3] = (byte) cArr3[(i8 >> 12) & 63];
        }
        return bArr2;
    }

    public BASE64EncoderStream(OutputStream outputStream) {
        this(outputStream, 76);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(int i) throws IOException {
        byte[] bArr = this.buffer;
        int i2 = this.bufsize;
        int i3 = i2 + 1;
        this.bufsize = i3;
        bArr[i2] = (byte) i;
        if (i3 == 3) {
            encode();
            this.bufsize = 0;
        }
    }
}
