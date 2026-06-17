package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Preconditions;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public abstract class ByteSink {

    public final class AsCharSink extends CharSink {
        private final Charset charset;

        @Override // com.google.common.io.CharSink
        public Writer openStream() throws IOException {
            return new OutputStreamWriter(ByteSink.this.openStream(), this.charset);
        }

        public String toString() {
            return ByteSink.this.toString() + ".asCharSink(" + this.charset + ")";
        }

        private AsCharSink(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }
    }

    public CharSink asCharSink(Charset charset) {
        return new AsCharSink(charset);
    }

    public OutputStream openBufferedStream() throws IOException {
        OutputStream outputStreamOpenStream = openStream();
        return outputStreamOpenStream instanceof BufferedOutputStream ? (BufferedOutputStream) outputStreamOpenStream : new BufferedOutputStream(outputStreamOpenStream);
    }

    public abstract OutputStream openStream() throws IOException;

    public void write(byte[] bArr) throws IOException {
        Preconditions.checkNotNull(bArr);
        OutputStream outputStreamOpenStream = openStream();
        try {
            outputStreamOpenStream.write(bArr);
            outputStreamOpenStream.close();
        } catch (Throwable th) {
            if (outputStreamOpenStream != null) {
                try {
                    outputStreamOpenStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public long writeFrom(InputStream inputStream) throws IOException {
        Preconditions.checkNotNull(inputStream);
        OutputStream outputStreamOpenStream = openStream();
        try {
            long jCopy = ByteStreams.copy(inputStream, outputStreamOpenStream);
            if (outputStreamOpenStream != null) {
                outputStreamOpenStream.close();
            }
            return jCopy;
        } catch (Throwable th) {
            if (outputStreamOpenStream != null) {
                try {
                    outputStreamOpenStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}
