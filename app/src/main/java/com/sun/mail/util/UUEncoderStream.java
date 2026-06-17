package com.sun.mail.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/* JADX INFO: loaded from: classes.dex */
public class UUEncoderStream extends FilterOutputStream {
    private byte[] buffer;
    private int bufsize;
    private int mode;
    private String name;
    private boolean wrotePrefix;
    private boolean wroteSuffix;

    public UUEncoderStream(OutputStream outputStream) {
        this(outputStream, "encoder.buf", 420);
    }

    private void encode() throws IOException {
        byte b;
        ((FilterOutputStream) this).out.write((this.bufsize & 63) + 32);
        int i = 0;
        while (true) {
            int i2 = this.bufsize;
            if (i >= i2) {
                ((FilterOutputStream) this).out.write(10);
                return;
            }
            byte[] bArr = this.buffer;
            int i3 = i + 1;
            byte b2 = bArr[i];
            byte b3 = 1;
            if (i3 < i2) {
                int i4 = i + 2;
                byte b4 = bArr[i3];
                if (i4 < i2) {
                    i += 3;
                    b = bArr[i4];
                } else {
                    b = 1;
                    i = i4;
                }
                b3 = b4;
            } else {
                i = i3;
                b = 1;
            }
            ((FilterOutputStream) this).out.write(((b2 >>> 2) & 63) + 32);
            ((FilterOutputStream) this).out.write((((b2 << 4) & 48) | ((b3 >>> 4) & 15)) + 32);
            ((FilterOutputStream) this).out.write((((b3 << 2) & 60) | ((b >>> 6) & 3)) + 32);
            ((FilterOutputStream) this).out.write((b & 63) + 32);
        }
    }

    private void writePrefix() throws IOException {
        if (this.wrotePrefix) {
            return;
        }
        PrintStream printStream = new PrintStream(((FilterOutputStream) this).out, false, "utf-8");
        printStream.format("begin %o %s%n", Integer.valueOf(this.mode), this.name);
        printStream.flush();
        this.wrotePrefix = true;
    }

    private void writeSuffix() throws IOException {
        if (this.wroteSuffix) {
            return;
        }
        PrintStream printStream = new PrintStream(((FilterOutputStream) this).out, false, "us-ascii");
        printStream.println(" \nend");
        printStream.flush();
        this.wroteSuffix = true;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
        ((FilterOutputStream) this).out.close();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.bufsize > 0) {
            writePrefix();
            encode();
            this.bufsize = 0;
        }
        writeSuffix();
        ((FilterOutputStream) this).out.flush();
    }

    public void setNameMode(String str, int i) {
        this.name = str;
        this.mode = i;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        for (int i3 = 0; i3 < i2; i3++) {
            write(bArr[i + i3]);
        }
    }

    public UUEncoderStream(OutputStream outputStream, String str) {
        this(outputStream, str, 420);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public UUEncoderStream(OutputStream outputStream, String str, int i) {
        super(outputStream);
        this.bufsize = 0;
        this.wrotePrefix = false;
        this.wroteSuffix = false;
        this.name = str;
        this.mode = i;
        this.buffer = new byte[45];
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i) throws IOException {
        byte[] bArr = this.buffer;
        int i2 = this.bufsize;
        int i3 = i2 + 1;
        this.bufsize = i3;
        bArr[i2] = (byte) i;
        if (i3 == 45) {
            writePrefix();
            encode();
            this.bufsize = 0;
        }
    }
}
