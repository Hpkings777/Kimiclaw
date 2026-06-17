package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import defpackage.AbstractC2039eV;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import kotlin.jvm.internal.LongCompanionObject;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public abstract class ByteSource {

    public class AsCharSource extends CharSource {
        final Charset charset;

        public AsCharSource(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        @Override // com.google.common.io.CharSource
        public ByteSource asByteSource(Charset charset) {
            return charset.equals(this.charset) ? ByteSource.this : super.asByteSource(charset);
        }

        @Override // com.google.common.io.CharSource
        public Reader openStream() throws IOException {
            return new InputStreamReader(ByteSource.this.openStream(), this.charset);
        }

        @Override // com.google.common.io.CharSource
        public String read() throws IOException {
            return new String(ByteSource.this.read(), this.charset);
        }

        public String toString() {
            return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
        }
    }

    public static class ByteArrayByteSource extends ByteSource {
        final byte[] bytes;
        final int length;
        final int offset;

        public ByteArrayByteSource(byte[] bArr) {
            this(bArr, 0, bArr.length);
        }

        @Override // com.google.common.io.ByteSource
        public long copyTo(OutputStream outputStream) throws IOException {
            outputStream.write(this.bytes, this.offset, this.length);
            return this.length;
        }

        @Override // com.google.common.io.ByteSource
        public HashCode hash(HashFunction hashFunction) throws IOException {
            return hashFunction.hashBytes(this.bytes, this.offset, this.length);
        }

        @Override // com.google.common.io.ByteSource
        public boolean isEmpty() {
            return this.length == 0;
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openBufferedStream() {
            return openStream();
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() {
            return new ByteArrayInputStream(this.bytes, this.offset, this.length);
        }

        @Override // com.google.common.io.ByteSource
        public byte[] read() {
            byte[] bArr = this.bytes;
            int i = this.offset;
            return Arrays.copyOfRange(bArr, i, this.length + i);
        }

        @Override // com.google.common.io.ByteSource
        public long size() {
            return this.length;
        }

        @Override // com.google.common.io.ByteSource
        public Optional<Long> sizeIfKnown() {
            return Optional.of(Long.valueOf(this.length));
        }

        @Override // com.google.common.io.ByteSource
        public ByteSource slice(long j, long j2) {
            Preconditions.checkArgument(j >= 0, "offset (%s) may not be negative", j);
            Preconditions.checkArgument(j2 >= 0, "length (%s) may not be negative", j2);
            long jMin = Math.min(j, this.length);
            return new ByteArrayByteSource(this.bytes, this.offset + ((int) jMin), (int) Math.min(j2, ((long) this.length) - jMin));
        }

        public String toString() {
            return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes, this.offset, this.length), 30, "...") + ")";
        }

        public ByteArrayByteSource(byte[] bArr, int i, int i2) {
            this.bytes = bArr;
            this.offset = i;
            this.length = i2;
        }

        @Override // com.google.common.io.ByteSource
        @ParametricNullness
        public <T> T read(ByteProcessor<T> byteProcessor) throws IOException {
            byteProcessor.processBytes(this.bytes, this.offset, this.length);
            return byteProcessor.getResult();
        }
    }

    public static final class ConcatenatedByteSource extends ByteSource {
        final Iterable<? extends ByteSource> sources;

        public ConcatenatedByteSource(Iterable<? extends ByteSource> iterable) {
            this.sources = (Iterable) Preconditions.checkNotNull(iterable);
        }

        @Override // com.google.common.io.ByteSource
        public boolean isEmpty() throws IOException {
            Iterator<? extends ByteSource> it = this.sources.iterator();
            while (it.hasNext()) {
                if (!it.next().isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() throws IOException {
            return new MultiInputStream(this.sources.iterator());
        }

        @Override // com.google.common.io.ByteSource
        public long size() throws IOException {
            Iterator<? extends ByteSource> it = this.sources.iterator();
            long size = 0;
            while (it.hasNext()) {
                size += it.next().size();
                if (size < 0) {
                    return LongCompanionObject.MAX_VALUE;
                }
            }
            return size;
        }

        @Override // com.google.common.io.ByteSource
        public Optional<Long> sizeIfKnown() {
            Iterable<? extends ByteSource> iterable = this.sources;
            if (!(iterable instanceof Collection)) {
                return Optional.absent();
            }
            Iterator<? extends ByteSource> it = iterable.iterator();
            long jLongValue = 0;
            while (it.hasNext()) {
                Optional<Long> optionalSizeIfKnown = it.next().sizeIfKnown();
                if (!optionalSizeIfKnown.isPresent()) {
                    return Optional.absent();
                }
                jLongValue += optionalSizeIfKnown.get().longValue();
                if (jLongValue < 0) {
                    return Optional.of(Long.valueOf(LongCompanionObject.MAX_VALUE));
                }
            }
            return Optional.of(Long.valueOf(jLongValue));
        }

        public String toString() {
            return "ByteSource.concat(" + this.sources + ")";
        }
    }

    public static final class EmptyByteSource extends ByteArrayByteSource {
        static final EmptyByteSource INSTANCE = new EmptyByteSource();

        public EmptyByteSource() {
            super(new byte[0]);
        }

        @Override // com.google.common.io.ByteSource
        public CharSource asCharSource(Charset charset) {
            Preconditions.checkNotNull(charset);
            return CharSource.empty();
        }

        @Override // com.google.common.io.ByteSource.ByteArrayByteSource, com.google.common.io.ByteSource
        public byte[] read() {
            return this.bytes;
        }

        @Override // com.google.common.io.ByteSource.ByteArrayByteSource
        public String toString() {
            return "ByteSource.empty()";
        }
    }

    public final class SlicedByteSource extends ByteSource {
        final long length;
        final long offset;

        public SlicedByteSource(long j, long j2) {
            Preconditions.checkArgument(j >= 0, "offset (%s) may not be negative", j);
            Preconditions.checkArgument(j2 >= 0, "length (%s) may not be negative", j2);
            this.offset = j;
            this.length = j2;
        }

        private InputStream sliceStream(InputStream inputStream) throws IOException {
            long j = this.offset;
            if (j > 0) {
                try {
                    if (ByteStreams.skipUpTo(inputStream, j) < this.offset) {
                        inputStream.close();
                        return new ByteArrayInputStream(new byte[0]);
                    }
                } finally {
                }
            }
            return ByteStreams.limit(inputStream, this.length);
        }

        @Override // com.google.common.io.ByteSource
        public boolean isEmpty() throws IOException {
            return this.length == 0 || super.isEmpty();
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openBufferedStream() throws IOException {
            return sliceStream(ByteSource.this.openBufferedStream());
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() throws IOException {
            return sliceStream(ByteSource.this.openStream());
        }

        @Override // com.google.common.io.ByteSource
        public Optional<Long> sizeIfKnown() {
            Optional<Long> optionalSizeIfKnown = ByteSource.this.sizeIfKnown();
            if (!optionalSizeIfKnown.isPresent()) {
                return Optional.absent();
            }
            long jLongValue = optionalSizeIfKnown.get().longValue();
            return Optional.of(Long.valueOf(Math.min(this.length, jLongValue - Math.min(this.offset, jLongValue))));
        }

        @Override // com.google.common.io.ByteSource
        public ByteSource slice(long j, long j2) {
            Preconditions.checkArgument(j >= 0, "offset (%s) may not be negative", j);
            Preconditions.checkArgument(j2 >= 0, "length (%s) may not be negative", j2);
            long j3 = this.length - j;
            return j3 <= 0 ? ByteSource.empty() : ByteSource.this.slice(this.offset + j, Math.min(j2, j3));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(ByteSource.this.toString());
            sb.append(".slice(");
            sb.append(this.offset);
            sb.append(", ");
            return AbstractC2039eV.l(this.length, ")", sb);
        }
    }

    public static ByteSource concat(Iterable<? extends ByteSource> iterable) {
        return new ConcatenatedByteSource(iterable);
    }

    private static long countBySkipping(InputStream inputStream) throws IOException {
        long j = 0;
        while (true) {
            long jSkipUpTo = ByteStreams.skipUpTo(inputStream, 2147483647L);
            if (jSkipUpTo <= 0) {
                return j;
            }
            j += jSkipUpTo;
        }
    }

    public static ByteSource empty() {
        return EmptyByteSource.INSTANCE;
    }

    public static ByteSource wrap(byte[] bArr) {
        return new ByteArrayByteSource(bArr);
    }

    public CharSource asCharSource(Charset charset) {
        return new AsCharSource(charset);
    }

    public boolean contentEquals(ByteSource byteSource) throws IOException {
        Preconditions.checkNotNull(byteSource);
        Closer closerCreate = Closer.create();
        try {
            return ByteStreams.contentsEqual((InputStream) closerCreate.register(openStream()), (InputStream) closerCreate.register(byteSource.openStream()));
        } finally {
        }
    }

    public long copyTo(OutputStream outputStream) throws IOException {
        Preconditions.checkNotNull(outputStream);
        try {
            return ByteStreams.copy((InputStream) Closer.create().register(openStream()), outputStream);
        } finally {
        }
    }

    public HashCode hash(HashFunction hashFunction) throws IOException {
        Hasher hasherNewHasher = hashFunction.newHasher();
        copyTo(Funnels.asOutputStream(hasherNewHasher));
        return hasherNewHasher.hash();
    }

    public boolean isEmpty() throws IOException {
        Optional<Long> optionalSizeIfKnown = sizeIfKnown();
        if (optionalSizeIfKnown.isPresent()) {
            return optionalSizeIfKnown.get().longValue() == 0;
        }
        try {
            return ((InputStream) Closer.create().register(openStream())).read() == -1;
        } finally {
        }
    }

    public InputStream openBufferedStream() throws IOException {
        InputStream inputStreamOpenStream = openStream();
        return inputStreamOpenStream instanceof BufferedInputStream ? (BufferedInputStream) inputStreamOpenStream : new BufferedInputStream(inputStreamOpenStream);
    }

    public abstract InputStream openStream() throws IOException;

    public byte[] read() throws IOException {
        Closer closerCreate = Closer.create();
        try {
            InputStream inputStream = (InputStream) closerCreate.register(openStream());
            Optional<Long> optionalSizeIfKnown = sizeIfKnown();
            return optionalSizeIfKnown.isPresent() ? ByteStreams.toByteArray(inputStream, optionalSizeIfKnown.get().longValue()) : ByteStreams.toByteArray(inputStream);
        } catch (Throwable th) {
            try {
                throw closerCreate.rethrow(th);
            } finally {
                closerCreate.close();
            }
        }
    }

    public long size() throws IOException {
        Optional<Long> optionalSizeIfKnown = sizeIfKnown();
        if (optionalSizeIfKnown.isPresent()) {
            return optionalSizeIfKnown.get().longValue();
        }
        Closer closerCreate = Closer.create();
        try {
            return countBySkipping((InputStream) closerCreate.register(openStream()));
        } catch (IOException unused) {
            closerCreate.close();
            try {
                return ByteStreams.exhaust((InputStream) Closer.create().register(openStream()));
            } finally {
            }
        } finally {
        }
    }

    public Optional<Long> sizeIfKnown() {
        return Optional.absent();
    }

    public ByteSource slice(long j, long j2) {
        return new SlicedByteSource(j, j2);
    }

    public static ByteSource concat(Iterator<? extends ByteSource> it) {
        return concat(ImmutableList.copyOf(it));
    }

    public static ByteSource concat(ByteSource... byteSourceArr) {
        return concat(ImmutableList.copyOf(byteSourceArr));
    }

    public long copyTo(ByteSink byteSink) throws IOException {
        Preconditions.checkNotNull(byteSink);
        Closer closerCreate = Closer.create();
        try {
            return ByteStreams.copy((InputStream) closerCreate.register(openStream()), (OutputStream) closerCreate.register(byteSink.openStream()));
        } finally {
        }
    }

    @ParametricNullness
    public <T> T read(ByteProcessor<T> byteProcessor) throws IOException {
        Preconditions.checkNotNull(byteProcessor);
        try {
            return (T) ByteStreams.readBytes((InputStream) Closer.create().register(openStream()), byteProcessor);
        } finally {
        }
    }
}
