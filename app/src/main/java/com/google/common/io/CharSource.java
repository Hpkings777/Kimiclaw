package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import defpackage.J1;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import kotlin.jvm.internal.LongCompanionObject;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public abstract class CharSource {

    public final class AsByteSource extends ByteSource {
        final Charset charset;

        public AsByteSource(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        @Override // com.google.common.io.ByteSource
        public CharSource asCharSource(Charset charset) {
            return charset.equals(this.charset) ? CharSource.this : super.asCharSource(charset);
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() throws IOException {
            return new ReaderInputStream(CharSource.this.openStream(), this.charset, 8192);
        }

        public String toString() {
            return CharSource.this.toString() + ".asByteSource(" + this.charset + ")";
        }
    }

    public static class CharSequenceCharSource extends CharSource {
        private static final Splitter LINE_SPLITTER = Splitter.onPattern("\r\n|\n|\r");
        final CharSequence seq;

        public CharSequenceCharSource(CharSequence charSequence) {
            this.seq = (CharSequence) Preconditions.checkNotNull(charSequence);
        }

        private Iterator<String> linesIterator() {
            return new AbstractIterator<String>() { // from class: com.google.common.io.CharSource.CharSequenceCharSource.1
                final Iterator<String> lines;

                {
                    this.lines = CharSequenceCharSource.LINE_SPLITTER.split(CharSequenceCharSource.this.seq).iterator();
                }

                @Override // com.google.common.collect.AbstractIterator
                public String computeNext() {
                    if (this.lines.hasNext()) {
                        String next = this.lines.next();
                        if (this.lines.hasNext() || !next.isEmpty()) {
                            return next;
                        }
                    }
                    return endOfData();
                }
            };
        }

        @Override // com.google.common.io.CharSource
        public boolean isEmpty() {
            return this.seq.length() == 0;
        }

        @Override // com.google.common.io.CharSource
        public long length() {
            return this.seq.length();
        }

        @Override // com.google.common.io.CharSource
        public Optional<Long> lengthIfKnown() {
            return Optional.of(Long.valueOf(this.seq.length()));
        }

        @Override // com.google.common.io.CharSource
        @IgnoreJRERequirement
        public Stream<String> lines() {
            return Streams.stream(linesIterator());
        }

        @Override // com.google.common.io.CharSource
        public Reader openStream() {
            return new CharSequenceReader(this.seq);
        }

        @Override // com.google.common.io.CharSource
        public String read() {
            return this.seq.toString();
        }

        @Override // com.google.common.io.CharSource
        public String readFirstLine() {
            Iterator<String> itLinesIterator = linesIterator();
            if (itLinesIterator.hasNext()) {
                return itLinesIterator.next();
            }
            return null;
        }

        @Override // com.google.common.io.CharSource
        public ImmutableList<String> readLines() {
            return ImmutableList.copyOf(linesIterator());
        }

        public String toString() {
            return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
        }

        @Override // com.google.common.io.CharSource
        @ParametricNullness
        public <T> T readLines(LineProcessor<T> lineProcessor) throws IOException {
            Iterator<String> itLinesIterator = linesIterator();
            while (itLinesIterator.hasNext() && lineProcessor.processLine(itLinesIterator.next())) {
            }
            return lineProcessor.getResult();
        }
    }

    public static final class ConcatenatedCharSource extends CharSource {
        private final Iterable<? extends CharSource> sources;

        public ConcatenatedCharSource(Iterable<? extends CharSource> iterable) {
            this.sources = (Iterable) Preconditions.checkNotNull(iterable);
        }

        @Override // com.google.common.io.CharSource
        public boolean isEmpty() throws IOException {
            Iterator<? extends CharSource> it = this.sources.iterator();
            while (it.hasNext()) {
                if (!it.next().isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.io.CharSource
        public long length() throws IOException {
            Iterator<? extends CharSource> it = this.sources.iterator();
            long length = 0;
            while (it.hasNext()) {
                length += it.next().length();
            }
            return length;
        }

        @Override // com.google.common.io.CharSource
        public Optional<Long> lengthIfKnown() {
            Iterator<? extends CharSource> it = this.sources.iterator();
            long jLongValue = 0;
            while (it.hasNext()) {
                Optional<Long> optionalLengthIfKnown = it.next().lengthIfKnown();
                if (!optionalLengthIfKnown.isPresent()) {
                    return Optional.absent();
                }
                jLongValue += optionalLengthIfKnown.get().longValue();
            }
            return Optional.of(Long.valueOf(jLongValue));
        }

        @Override // com.google.common.io.CharSource
        public Reader openStream() throws IOException {
            return new MultiReader(this.sources.iterator());
        }

        public String toString() {
            return "CharSource.concat(" + this.sources + ")";
        }
    }

    public static final class EmptyCharSource extends StringCharSource {
        private static final EmptyCharSource INSTANCE = new EmptyCharSource();

        private EmptyCharSource() {
            super("");
        }

        @Override // com.google.common.io.CharSource.CharSequenceCharSource
        public String toString() {
            return "CharSource.empty()";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @IgnoreJRERequirement
    public static void closeUnchecked(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static CharSource concat(Iterable<? extends CharSource> iterable) {
        return new ConcatenatedCharSource(iterable);
    }

    private static long countBySkipping(Reader reader) throws IOException {
        long j = 0;
        while (true) {
            long jSkip = reader.skip(LongCompanionObject.MAX_VALUE);
            if (jSkip == 0) {
                return j;
            }
            j += jSkip;
        }
    }

    public static CharSource empty() {
        return EmptyCharSource.INSTANCE;
    }

    public static CharSource wrap(CharSequence charSequence) {
        return charSequence instanceof String ? new StringCharSource((String) charSequence) : new CharSequenceCharSource(charSequence);
    }

    public ByteSource asByteSource(Charset charset) {
        return new AsByteSource(charset);
    }

    public long copyTo(Appendable appendable) throws IOException {
        Preconditions.checkNotNull(appendable);
        try {
            return CharStreams.copy((Reader) Closer.create().register(openStream()), appendable);
        } finally {
        }
    }

    @IgnoreJRERequirement
    public void forEachLine(Consumer<? super String> consumer) throws IOException {
        try {
            Stream<String> streamLines = lines();
            try {
                streamLines.forEachOrdered(consumer);
                streamLines.close();
            } finally {
            }
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    public boolean isEmpty() throws IOException {
        Optional<Long> optionalLengthIfKnown = lengthIfKnown();
        if (optionalLengthIfKnown.isPresent()) {
            return optionalLengthIfKnown.get().longValue() == 0;
        }
        try {
            return ((Reader) Closer.create().register(openStream())).read() == -1;
        } finally {
        }
    }

    public long length() throws IOException {
        Optional<Long> optionalLengthIfKnown = lengthIfKnown();
        if (optionalLengthIfKnown.isPresent()) {
            return optionalLengthIfKnown.get().longValue();
        }
        try {
            return countBySkipping((Reader) Closer.create().register(openStream()));
        } finally {
        }
    }

    public Optional<Long> lengthIfKnown() {
        return Optional.absent();
    }

    @IgnoreJRERequirement
    public Stream<String> lines() throws IOException {
        BufferedReader bufferedReaderOpenBufferedStream = openBufferedStream();
        return (Stream) bufferedReaderOpenBufferedStream.lines().onClose(new J1(bufferedReaderOpenBufferedStream, 13));
    }

    public BufferedReader openBufferedStream() throws IOException {
        Reader readerOpenStream = openStream();
        return readerOpenStream instanceof BufferedReader ? (BufferedReader) readerOpenStream : new BufferedReader(readerOpenStream);
    }

    public abstract Reader openStream() throws IOException;

    public String read() throws IOException {
        try {
            return CharStreams.toString((Reader) Closer.create().register(openStream()));
        } finally {
        }
    }

    public String readFirstLine() throws IOException {
        try {
            return ((BufferedReader) Closer.create().register(openBufferedStream())).readLine();
        } finally {
        }
    }

    public ImmutableList<String> readLines() throws IOException {
        try {
            BufferedReader bufferedReader = (BufferedReader) Closer.create().register(openBufferedStream());
            ArrayList arrayList = new ArrayList();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    return ImmutableList.copyOf((Collection) arrayList);
                }
                arrayList.add(line);
            }
        } finally {
        }
    }

    public static class StringCharSource extends CharSequenceCharSource {
        public StringCharSource(String str) {
            super(str);
        }

        @Override // com.google.common.io.CharSource
        public long copyTo(Appendable appendable) throws IOException {
            appendable.append(this.seq);
            return this.seq.length();
        }

        @Override // com.google.common.io.CharSource.CharSequenceCharSource, com.google.common.io.CharSource
        public Reader openStream() {
            return new StringReader((String) this.seq);
        }

        @Override // com.google.common.io.CharSource
        public long copyTo(CharSink charSink) throws IOException {
            Preconditions.checkNotNull(charSink);
            try {
                ((Writer) Closer.create().register(charSink.openStream())).write((String) this.seq);
                return this.seq.length();
            } finally {
            }
        }
    }

    public static CharSource concat(Iterator<? extends CharSource> it) {
        return concat(ImmutableList.copyOf(it));
    }

    public static CharSource concat(CharSource... charSourceArr) {
        return concat(ImmutableList.copyOf(charSourceArr));
    }

    public long copyTo(CharSink charSink) throws IOException {
        Preconditions.checkNotNull(charSink);
        Closer closerCreate = Closer.create();
        try {
            return CharStreams.copy((Reader) closerCreate.register(openStream()), (Writer) closerCreate.register(charSink.openStream()));
        } finally {
        }
    }

    @ParametricNullness
    public <T> T readLines(LineProcessor<T> lineProcessor) throws IOException {
        Preconditions.checkNotNull(lineProcessor);
        try {
            return (T) CharStreams.readLines((Reader) Closer.create().register(openStream()), lineProcessor);
        } finally {
        }
    }
}
