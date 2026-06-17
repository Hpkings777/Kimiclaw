package com.google.common.io;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.primitives.UnsignedBytes;
import defpackage.AbstractC4671xI0;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public abstract class BaseEncoding {
    private static final BaseEncoding BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", '=');
    private static final BaseEncoding BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", '=');
    private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", '=');
    private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", '=');
    private static final BaseEncoding BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");

    public static final class Alphabet {
        final int bitsPerChar;
        final int bytesPerChunk;
        private final char[] chars;
        final int charsPerChunk;
        private final byte[] decodabet;
        private final boolean ignoreCase;
        final int mask;
        private final String name;
        private final boolean[] validPadding;

        public Alphabet(String str, char[] cArr) {
            this(str, cArr, decodabetFor(cArr), false);
        }

        private static byte[] decodabetFor(char[] cArr) {
            byte[] bArr = new byte[128];
            Arrays.fill(bArr, (byte) -1);
            for (int i = 0; i < cArr.length; i++) {
                char c = cArr[i];
                boolean z = true;
                Preconditions.checkArgument(c < 128, "Non-ASCII character: %s", c);
                if (bArr[c] != -1) {
                    z = false;
                }
                Preconditions.checkArgument(z, "Duplicate character: %s", c);
                bArr[c] = (byte) i;
            }
            return bArr;
        }

        private boolean hasLowerCase() {
            for (char c : this.chars) {
                if (Ascii.isLowerCase(c)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasUpperCase() {
            for (char c : this.chars) {
                if (Ascii.isUpperCase(c)) {
                    return true;
                }
            }
            return false;
        }

        public boolean canDecode(char c) {
            return c <= 127 && this.decodabet[c] != -1;
        }

        public int decode(char c) throws DecodingException {
            if (c > 127) {
                throw new DecodingException("Unrecognized character: 0x" + Integer.toHexString(c));
            }
            byte b = this.decodabet[c];
            if (b != -1) {
                return b;
            }
            if (c <= ' ' || c == 127) {
                throw new DecodingException("Unrecognized character: 0x" + Integer.toHexString(c));
            }
            throw new DecodingException("Unrecognized character: " + c);
        }

        public char encode(int i) {
            return this.chars[i];
        }

        public boolean equals(Object obj) {
            if (obj instanceof Alphabet) {
                Alphabet alphabet = (Alphabet) obj;
                if (this.ignoreCase == alphabet.ignoreCase && Arrays.equals(this.chars, alphabet.chars)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Arrays.hashCode(this.chars) + (this.ignoreCase ? 1231 : 1237);
        }

        public Alphabet ignoreCase() {
            if (this.ignoreCase) {
                return this;
            }
            byte[] bArr = this.decodabet;
            byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length);
            int i = 65;
            while (true) {
                if (i > 90) {
                    return new Alphabet(AbstractC4671xI0.p(new StringBuilder(), this.name, ".ignoreCase()"), this.chars, bArrCopyOf, true);
                }
                int i2 = i | 32;
                byte[] bArr2 = this.decodabet;
                byte b = bArr2[i];
                byte b2 = bArr2[i2];
                if (b == -1) {
                    bArrCopyOf[i] = b2;
                } else {
                    Preconditions.checkState(b2 == -1, "Can't ignoreCase() since '%s' and '%s' encode different values", (char) i, (char) i2);
                    bArrCopyOf[i2] = b;
                }
                i++;
            }
        }

        public boolean isValidPaddingStartPosition(int i) {
            return this.validPadding[i % this.charsPerChunk];
        }

        public Alphabet lowerCase() {
            if (!hasUpperCase()) {
                return this;
            }
            Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
            char[] cArr = new char[this.chars.length];
            int i = 0;
            while (true) {
                char[] cArr2 = this.chars;
                if (i >= cArr2.length) {
                    break;
                }
                cArr[i] = Ascii.toLowerCase(cArr2[i]);
                i++;
            }
            Alphabet alphabet = new Alphabet(AbstractC4671xI0.p(new StringBuilder(), this.name, ".lowerCase()"), cArr);
            return this.ignoreCase ? alphabet.ignoreCase() : alphabet;
        }

        public boolean matches(char c) {
            byte[] bArr = this.decodabet;
            return c < bArr.length && bArr[c] != -1;
        }

        public String toString() {
            return this.name;
        }

        public Alphabet upperCase() {
            if (!hasLowerCase()) {
                return this;
            }
            Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
            char[] cArr = new char[this.chars.length];
            int i = 0;
            while (true) {
                char[] cArr2 = this.chars;
                if (i >= cArr2.length) {
                    break;
                }
                cArr[i] = Ascii.toUpperCase(cArr2[i]);
                i++;
            }
            Alphabet alphabet = new Alphabet(AbstractC4671xI0.p(new StringBuilder(), this.name, ".upperCase()"), cArr);
            return this.ignoreCase ? alphabet.ignoreCase() : alphabet;
        }

        private Alphabet(String str, char[] cArr, byte[] bArr, boolean z) {
            this.name = (String) Preconditions.checkNotNull(str);
            this.chars = (char[]) Preconditions.checkNotNull(cArr);
            try {
                int iLog2 = IntMath.log2(cArr.length, RoundingMode.UNNECESSARY);
                this.bitsPerChar = iLog2;
                int iNumberOfTrailingZeros = Integer.numberOfTrailingZeros(iLog2);
                int i = 1 << (3 - iNumberOfTrailingZeros);
                this.charsPerChunk = i;
                this.bytesPerChunk = iLog2 >> iNumberOfTrailingZeros;
                this.mask = cArr.length - 1;
                this.decodabet = bArr;
                boolean[] zArr = new boolean[i];
                for (int i2 = 0; i2 < this.bytesPerChunk; i2++) {
                    zArr[IntMath.divide(i2 * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
                }
                this.validPadding = zArr;
                this.ignoreCase = z;
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException("Illegal alphabet length " + cArr.length, e);
            }
        }
    }

    public static final class Base16Encoding extends StandardBaseEncoding {
        final char[] encoding;

        public Base16Encoding(String str, String str2) {
            this(new Alphabet(str, str2.toCharArray()));
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.io.BaseEncoding
        public int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException {
            Preconditions.checkNotNull(bArr);
            if (charSequence.length() % 2 == 1) {
                throw new DecodingException("Invalid input length " + charSequence.length());
            }
            int i = 0;
            int i2 = 0;
            while (i < charSequence.length()) {
                bArr[i2] = (byte) ((this.alphabet.decode(charSequence.charAt(i)) << 4) | this.alphabet.decode(charSequence.charAt(i + 1)));
                i += 2;
                i2++;
            }
            return i2;
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.io.BaseEncoding
        public void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            for (int i3 = 0; i3 < i2; i3++) {
                int i4 = bArr[i + i3] & UnsignedBytes.MAX_VALUE;
                appendable.append(this.encoding[i4]);
                appendable.append(this.encoding[i4 | 256]);
            }
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding
        public BaseEncoding newInstance(Alphabet alphabet, Character ch) {
            return new Base16Encoding(alphabet);
        }

        private Base16Encoding(Alphabet alphabet) {
            super(alphabet, null);
            this.encoding = new char[512];
            Preconditions.checkArgument(alphabet.chars.length == 16);
            for (int i = 0; i < 256; i++) {
                this.encoding[i] = alphabet.encode(i >>> 4);
                this.encoding[i | 256] = alphabet.encode(i & 15);
            }
        }
    }

    public static final class Base64Encoding extends StandardBaseEncoding {
        public Base64Encoding(String str, String str2, Character ch) {
            this(new Alphabet(str, str2.toCharArray()), ch);
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.io.BaseEncoding
        public int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException {
            Preconditions.checkNotNull(bArr);
            CharSequence charSequenceTrimTrailingPadding = trimTrailingPadding(charSequence);
            if (!this.alphabet.isValidPaddingStartPosition(charSequenceTrimTrailingPadding.length())) {
                throw new DecodingException("Invalid input length " + charSequenceTrimTrailingPadding.length());
            }
            int i = 0;
            int i2 = 0;
            while (i < charSequenceTrimTrailingPadding.length()) {
                int i3 = i + 2;
                int iDecode = (this.alphabet.decode(charSequenceTrimTrailingPadding.charAt(i)) << 18) | (this.alphabet.decode(charSequenceTrimTrailingPadding.charAt(i + 1)) << 12);
                int i4 = i2 + 1;
                bArr[i2] = (byte) (iDecode >>> 16);
                if (i3 < charSequenceTrimTrailingPadding.length()) {
                    int i5 = i + 3;
                    int iDecode2 = iDecode | (this.alphabet.decode(charSequenceTrimTrailingPadding.charAt(i3)) << 6);
                    int i6 = i2 + 2;
                    bArr[i4] = (byte) ((iDecode2 >>> 8) & 255);
                    if (i5 < charSequenceTrimTrailingPadding.length()) {
                        i += 4;
                        i2 += 3;
                        bArr[i6] = (byte) ((iDecode2 | this.alphabet.decode(charSequenceTrimTrailingPadding.charAt(i5))) & 255);
                    } else {
                        i2 = i6;
                        i = i5;
                    }
                } else {
                    i2 = i4;
                    i = i3;
                }
            }
            return i2;
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding, com.google.common.io.BaseEncoding
        public void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            int i3 = i + i2;
            Preconditions.checkPositionIndexes(i, i3, bArr.length);
            while (i2 >= 3) {
                int i4 = i + 2;
                int i5 = ((bArr[i + 1] & UnsignedBytes.MAX_VALUE) << 8) | ((bArr[i] & UnsignedBytes.MAX_VALUE) << 16);
                i += 3;
                int i6 = i5 | (bArr[i4] & UnsignedBytes.MAX_VALUE);
                appendable.append(this.alphabet.encode(i6 >>> 18));
                appendable.append(this.alphabet.encode((i6 >>> 12) & 63));
                appendable.append(this.alphabet.encode((i6 >>> 6) & 63));
                appendable.append(this.alphabet.encode(i6 & 63));
                i2 -= 3;
            }
            if (i < i3) {
                encodeChunkTo(appendable, bArr, i, i3 - i);
            }
        }

        @Override // com.google.common.io.BaseEncoding.StandardBaseEncoding
        public BaseEncoding newInstance(Alphabet alphabet, Character ch) {
            return new Base64Encoding(alphabet, ch);
        }

        private Base64Encoding(Alphabet alphabet, Character ch) {
            super(alphabet, ch);
            Preconditions.checkArgument(alphabet.chars.length == 64);
        }
    }

    public static final class DecodingException extends IOException {
        public DecodingException(String str) {
            super(str);
        }
    }

    public static final class SeparatedBaseEncoding extends BaseEncoding {
        private final int afterEveryChars;
        private final BaseEncoding delegate;
        private final String separator;

        public SeparatedBaseEncoding(BaseEncoding baseEncoding, String str, int i) {
            this.delegate = (BaseEncoding) Preconditions.checkNotNull(baseEncoding);
            this.separator = (String) Preconditions.checkNotNull(str);
            this.afterEveryChars = i;
            Preconditions.checkArgument(i > 0, "Cannot add a separator after every %s chars", i);
        }

        @Override // com.google.common.io.BaseEncoding
        public boolean canDecode(CharSequence charSequence) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < charSequence.length(); i++) {
                char cCharAt = charSequence.charAt(i);
                if (this.separator.indexOf(cCharAt) < 0) {
                    sb.append(cCharAt);
                }
            }
            return this.delegate.canDecode(sb);
        }

        @Override // com.google.common.io.BaseEncoding
        public int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException {
            StringBuilder sb = new StringBuilder(charSequence.length());
            for (int i = 0; i < charSequence.length(); i++) {
                char cCharAt = charSequence.charAt(i);
                if (this.separator.indexOf(cCharAt) < 0) {
                    sb.append(cCharAt);
                }
            }
            return this.delegate.decodeTo(bArr, sb);
        }

        @Override // com.google.common.io.BaseEncoding
        @J2ktIncompatible
        @GwtIncompatible
        public InputStream decodingStream(Reader reader) {
            return this.delegate.decodingStream(BaseEncoding.ignoringReader(reader, this.separator));
        }

        @Override // com.google.common.io.BaseEncoding
        public void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            this.delegate.encodeTo(BaseEncoding.separatingAppendable(appendable, this.separator, this.afterEveryChars), bArr, i, i2);
        }

        @Override // com.google.common.io.BaseEncoding
        @J2ktIncompatible
        @GwtIncompatible
        public OutputStream encodingStream(Writer writer) {
            return this.delegate.encodingStream(BaseEncoding.separatingWriter(writer, this.separator, this.afterEveryChars));
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding ignoreCase() {
            return this.delegate.ignoreCase().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding lowerCase() {
            return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public int maxDecodedSize(int i) {
            return this.delegate.maxDecodedSize(i);
        }

        @Override // com.google.common.io.BaseEncoding
        public int maxEncodedSize(int i) {
            int iMaxEncodedSize = this.delegate.maxEncodedSize(i);
            return (IntMath.divide(Math.max(0, iMaxEncodedSize - 1), this.afterEveryChars, RoundingMode.FLOOR) * this.separator.length()) + iMaxEncodedSize;
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding omitPadding() {
            return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.delegate);
            sb.append(".withSeparator(\"");
            sb.append(this.separator);
            sb.append("\", ");
            return AbstractC4671xI0.n(sb, this.afterEveryChars, ")");
        }

        @Override // com.google.common.io.BaseEncoding
        public CharSequence trimTrailingPadding(CharSequence charSequence) {
            return this.delegate.trimTrailingPadding(charSequence);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding upperCase() {
            return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withPadChar(char c) {
            return this.delegate.withPadChar(c).withSeparator(this.separator, this.afterEveryChars);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withSeparator(String str, int i) {
            throw new UnsupportedOperationException("Already have a separator");
        }
    }

    public static class StandardBaseEncoding extends BaseEncoding {
        final Alphabet alphabet;
        private volatile BaseEncoding ignoreCase;
        private volatile BaseEncoding lowerCase;
        final Character paddingChar;
        private volatile BaseEncoding upperCase;

        public StandardBaseEncoding(String str, String str2, Character ch) {
            this(new Alphabet(str, str2.toCharArray()), ch);
        }

        @Override // com.google.common.io.BaseEncoding
        public boolean canDecode(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            CharSequence charSequenceTrimTrailingPadding = trimTrailingPadding(charSequence);
            if (!this.alphabet.isValidPaddingStartPosition(charSequenceTrimTrailingPadding.length())) {
                return false;
            }
            for (int i = 0; i < charSequenceTrimTrailingPadding.length(); i++) {
                if (!this.alphabet.canDecode(charSequenceTrimTrailingPadding.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.io.BaseEncoding
        public int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException {
            Alphabet alphabet;
            Preconditions.checkNotNull(bArr);
            CharSequence charSequenceTrimTrailingPadding = trimTrailingPadding(charSequence);
            if (!this.alphabet.isValidPaddingStartPosition(charSequenceTrimTrailingPadding.length())) {
                throw new DecodingException("Invalid input length " + charSequenceTrimTrailingPadding.length());
            }
            int i = 0;
            int i2 = 0;
            while (i < charSequenceTrimTrailingPadding.length()) {
                long jDecode = 0;
                int i3 = 0;
                int i4 = 0;
                while (true) {
                    alphabet = this.alphabet;
                    if (i3 >= alphabet.charsPerChunk) {
                        break;
                    }
                    jDecode <<= alphabet.bitsPerChar;
                    if (i + i3 < charSequenceTrimTrailingPadding.length()) {
                        jDecode |= (long) this.alphabet.decode(charSequenceTrimTrailingPadding.charAt(i4 + i));
                        i4++;
                    }
                    i3++;
                }
                int i5 = alphabet.bytesPerChunk;
                int i6 = (i5 * 8) - (i4 * alphabet.bitsPerChar);
                int i7 = (i5 - 1) * 8;
                while (i7 >= i6) {
                    bArr[i2] = (byte) ((jDecode >>> i7) & 255);
                    i7 -= 8;
                    i2++;
                }
                i += this.alphabet.charsPerChunk;
            }
            return i2;
        }

        @Override // com.google.common.io.BaseEncoding
        @J2ktIncompatible
        @GwtIncompatible
        public InputStream decodingStream(final Reader reader) {
            Preconditions.checkNotNull(reader);
            return new InputStream(this) { // from class: com.google.common.io.BaseEncoding.StandardBaseEncoding.2
                final /* synthetic */ StandardBaseEncoding this$0;
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int readChars = 0;
                boolean hitPadding = false;

                {
                    this.this$0 = this;
                }

                @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    reader.close();
                }

                /* JADX WARN: Code restructure failed: missing block: B:24:0x006e, code lost:
                
                    throw new com.google.common.io.BaseEncoding.DecodingException("Padding cannot start at index " + r4.readChars);
                 */
                @Override // java.io.InputStream
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public int read() throws IOException {
                    int i;
                    while (true) {
                        int i2 = reader.read();
                        if (i2 != -1) {
                            this.readChars++;
                            char c = (char) i2;
                            Character ch = this.this$0.paddingChar;
                            if (ch != null && ch.charValue() == c) {
                                if (!this.hitPadding && ((i = this.readChars) == 1 || !this.this$0.alphabet.isValidPaddingStartPosition(i - 1))) {
                                    break;
                                }
                                this.hitPadding = true;
                            } else {
                                if (this.hitPadding) {
                                    throw new DecodingException("Expected padding character but found '" + c + "' at index " + this.readChars);
                                }
                                int i3 = this.bitBuffer;
                                Alphabet alphabet = this.this$0.alphabet;
                                int i4 = i3 << alphabet.bitsPerChar;
                                this.bitBuffer = i4;
                                int iDecode = alphabet.decode(c) | i4;
                                this.bitBuffer = iDecode;
                                int i5 = this.bitBufferLength + this.this$0.alphabet.bitsPerChar;
                                this.bitBufferLength = i5;
                                if (i5 >= 8) {
                                    int i6 = i5 - 8;
                                    this.bitBufferLength = i6;
                                    return (iDecode >> i6) & 255;
                                }
                            }
                        } else {
                            if (this.hitPadding || this.this$0.alphabet.isValidPaddingStartPosition(this.readChars)) {
                                return -1;
                            }
                            throw new DecodingException("Invalid input length " + this.readChars);
                        }
                    }
                }

                @Override // java.io.InputStream
                public int read(byte[] bArr, int i, int i2) throws IOException {
                    int i3 = i2 + i;
                    Preconditions.checkPositionIndexes(i, i3, bArr.length);
                    int i4 = i;
                    while (i4 < i3) {
                        int i5 = read();
                        if (i5 == -1) {
                            int i6 = i4 - i;
                            if (i6 == 0) {
                                return -1;
                            }
                            return i6;
                        }
                        bArr[i4] = (byte) i5;
                        i4++;
                    }
                    return i4 - i;
                }
            };
        }

        public void encodeChunkTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            int i3 = 0;
            Preconditions.checkArgument(i2 <= this.alphabet.bytesPerChunk);
            long j = 0;
            for (int i4 = 0; i4 < i2; i4++) {
                j = (j | ((long) (bArr[i + i4] & UnsignedBytes.MAX_VALUE))) << 8;
            }
            int i5 = ((i2 + 1) * 8) - this.alphabet.bitsPerChar;
            while (i3 < i2 * 8) {
                Alphabet alphabet = this.alphabet;
                appendable.append(alphabet.encode(((int) (j >>> (i5 - i3))) & alphabet.mask));
                i3 += this.alphabet.bitsPerChar;
            }
            if (this.paddingChar != null) {
                while (i3 < this.alphabet.bytesPerChunk * 8) {
                    appendable.append(this.paddingChar.charValue());
                    i3 += this.alphabet.bitsPerChar;
                }
            }
        }

        @Override // com.google.common.io.BaseEncoding
        public void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException {
            Preconditions.checkNotNull(appendable);
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            int i3 = 0;
            while (i3 < i2) {
                encodeChunkTo(appendable, bArr, i + i3, Math.min(this.alphabet.bytesPerChunk, i2 - i3));
                i3 += this.alphabet.bytesPerChunk;
            }
        }

        @Override // com.google.common.io.BaseEncoding
        @J2ktIncompatible
        @GwtIncompatible
        public OutputStream encodingStream(final Writer writer) {
            Preconditions.checkNotNull(writer);
            return new OutputStream(this) { // from class: com.google.common.io.BaseEncoding.StandardBaseEncoding.1
                final /* synthetic */ StandardBaseEncoding this$0;
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int writtenChars = 0;

                {
                    this.this$0 = this;
                }

                @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    int i = this.bitBufferLength;
                    if (i > 0) {
                        int i2 = this.bitBuffer;
                        Alphabet alphabet = this.this$0.alphabet;
                        writer.write(alphabet.encode((i2 << (alphabet.bitsPerChar - i)) & alphabet.mask));
                        this.writtenChars++;
                        if (this.this$0.paddingChar != null) {
                            while (true) {
                                int i3 = this.writtenChars;
                                StandardBaseEncoding standardBaseEncoding = this.this$0;
                                if (i3 % standardBaseEncoding.alphabet.charsPerChunk == 0) {
                                    break;
                                }
                                writer.write(standardBaseEncoding.paddingChar.charValue());
                                this.writtenChars++;
                            }
                        }
                    }
                    writer.close();
                }

                @Override // java.io.OutputStream, java.io.Flushable
                public void flush() throws IOException {
                    writer.flush();
                }

                @Override // java.io.OutputStream
                public void write(int i) throws IOException {
                    this.bitBuffer = (i & 255) | (this.bitBuffer << 8);
                    this.bitBufferLength += 8;
                    while (true) {
                        int i2 = this.bitBufferLength;
                        Alphabet alphabet = this.this$0.alphabet;
                        int i3 = alphabet.bitsPerChar;
                        if (i2 < i3) {
                            return;
                        }
                        writer.write(alphabet.encode((this.bitBuffer >> (i2 - i3)) & alphabet.mask));
                        this.writtenChars++;
                        this.bitBufferLength -= this.this$0.alphabet.bitsPerChar;
                    }
                }
            };
        }

        public boolean equals(Object obj) {
            if (obj instanceof StandardBaseEncoding) {
                StandardBaseEncoding standardBaseEncoding = (StandardBaseEncoding) obj;
                if (this.alphabet.equals(standardBaseEncoding.alphabet) && Objects.equals(this.paddingChar, standardBaseEncoding.paddingChar)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return this.alphabet.hashCode() ^ Objects.hashCode(this.paddingChar);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding ignoreCase() {
            BaseEncoding baseEncodingNewInstance = this.ignoreCase;
            if (baseEncodingNewInstance == null) {
                Alphabet alphabetIgnoreCase = this.alphabet.ignoreCase();
                baseEncodingNewInstance = alphabetIgnoreCase == this.alphabet ? this : newInstance(alphabetIgnoreCase, this.paddingChar);
                this.ignoreCase = baseEncodingNewInstance;
            }
            return baseEncodingNewInstance;
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding lowerCase() {
            BaseEncoding baseEncodingNewInstance = this.lowerCase;
            if (baseEncodingNewInstance == null) {
                Alphabet alphabetLowerCase = this.alphabet.lowerCase();
                baseEncodingNewInstance = alphabetLowerCase == this.alphabet ? this : newInstance(alphabetLowerCase, this.paddingChar);
                this.lowerCase = baseEncodingNewInstance;
            }
            return baseEncodingNewInstance;
        }

        @Override // com.google.common.io.BaseEncoding
        public int maxDecodedSize(int i) {
            return (int) (((((long) this.alphabet.bitsPerChar) * ((long) i)) + 7) / 8);
        }

        @Override // com.google.common.io.BaseEncoding
        public int maxEncodedSize(int i) {
            Alphabet alphabet = this.alphabet;
            return IntMath.divide(i, alphabet.bytesPerChunk, RoundingMode.CEILING) * alphabet.charsPerChunk;
        }

        public BaseEncoding newInstance(Alphabet alphabet, Character ch) {
            return new StandardBaseEncoding(alphabet, ch);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding omitPadding() {
            return this.paddingChar == null ? this : newInstance(this.alphabet, null);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("BaseEncoding.");
            sb.append(this.alphabet);
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null) {
                    sb.append(".omitPadding()");
                } else {
                    sb.append(".withPadChar('");
                    sb.append(this.paddingChar);
                    sb.append("')");
                }
            }
            return sb.toString();
        }

        @Override // com.google.common.io.BaseEncoding
        public CharSequence trimTrailingPadding(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            Character ch = this.paddingChar;
            if (ch == null) {
                return charSequence;
            }
            char cCharValue = ch.charValue();
            int length = charSequence.length() - 1;
            while (length >= 0 && charSequence.charAt(length) == cCharValue) {
                length--;
            }
            return charSequence.subSequence(0, length + 1);
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding upperCase() {
            BaseEncoding baseEncodingNewInstance = this.upperCase;
            if (baseEncodingNewInstance == null) {
                Alphabet alphabetUpperCase = this.alphabet.upperCase();
                baseEncodingNewInstance = alphabetUpperCase == this.alphabet ? this : newInstance(alphabetUpperCase, this.paddingChar);
                this.upperCase = baseEncodingNewInstance;
            }
            return baseEncodingNewInstance;
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withPadChar(char c) {
            Character ch;
            return (8 % this.alphabet.bitsPerChar == 0 || ((ch = this.paddingChar) != null && ch.charValue() == c)) ? this : newInstance(this.alphabet, Character.valueOf(c));
        }

        @Override // com.google.common.io.BaseEncoding
        public BaseEncoding withSeparator(String str, int i) {
            for (int i2 = 0; i2 < str.length(); i2++) {
                Preconditions.checkArgument(!this.alphabet.matches(str.charAt(i2)), "Separator (%s) cannot contain alphabet characters", str);
            }
            Character ch = this.paddingChar;
            if (ch != null) {
                Preconditions.checkArgument(str.indexOf(ch.charValue()) < 0, "Separator (%s) cannot contain padding character", str);
            }
            return new SeparatedBaseEncoding(this, str, i);
        }

        public StandardBaseEncoding(Alphabet alphabet, Character ch) {
            this.alphabet = (Alphabet) Preconditions.checkNotNull(alphabet);
            Preconditions.checkArgument(ch == null || !alphabet.matches(ch.charValue()), "Padding character %s was already in alphabet", ch);
            this.paddingChar = ch;
        }
    }

    public static BaseEncoding base16() {
        return BASE16;
    }

    public static BaseEncoding base32() {
        return BASE32;
    }

    public static BaseEncoding base32Hex() {
        return BASE32_HEX;
    }

    public static BaseEncoding base64() {
        return BASE64;
    }

    public static BaseEncoding base64Url() {
        return BASE64_URL;
    }

    private static byte[] extract(byte[] bArr, int i) {
        if (i == bArr.length) {
            return bArr;
        }
        byte[] bArr2 = new byte[i];
        System.arraycopy(bArr, 0, bArr2, 0, i);
        return bArr2;
    }

    @J2ktIncompatible
    @GwtIncompatible
    public static Reader ignoringReader(final Reader reader, final String str) {
        Preconditions.checkNotNull(reader);
        Preconditions.checkNotNull(str);
        return new Reader() { // from class: com.google.common.io.BaseEncoding.3
            @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                reader.close();
            }

            @Override // java.io.Reader
            public int read() throws IOException {
                int i;
                do {
                    i = reader.read();
                    if (i == -1) {
                        break;
                    }
                } while (str.indexOf((char) i) >= 0);
                return i;
            }

            @Override // java.io.Reader
            public int read(char[] cArr, int i, int i2) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static Appendable separatingAppendable(Appendable appendable, String str, int i) {
        Preconditions.checkNotNull(appendable);
        Preconditions.checkNotNull(str);
        Preconditions.checkArgument(i > 0);
        return new Appendable(i, appendable, str) { // from class: com.google.common.io.BaseEncoding.4
            int charsUntilSeparator;
            final /* synthetic */ int val$afterEveryChars;
            final /* synthetic */ Appendable val$delegate;
            final /* synthetic */ String val$separator;

            {
                this.val$afterEveryChars = i;
                this.val$delegate = appendable;
                this.val$separator = str;
                this.charsUntilSeparator = i;
            }

            @Override // java.lang.Appendable
            public Appendable append(char c) throws IOException {
                if (this.charsUntilSeparator == 0) {
                    this.val$delegate.append(this.val$separator);
                    this.charsUntilSeparator = this.val$afterEveryChars;
                }
                this.val$delegate.append(c);
                this.charsUntilSeparator--;
                return this;
            }

            @Override // java.lang.Appendable
            public Appendable append(CharSequence charSequence, int i2, int i3) {
                throw new UnsupportedOperationException();
            }

            @Override // java.lang.Appendable
            public Appendable append(CharSequence charSequence) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @J2ktIncompatible
    @GwtIncompatible
    public static Writer separatingWriter(final Writer writer, String str, int i) {
        final Appendable appendableSeparatingAppendable = separatingAppendable(writer, str, i);
        return new Writer() { // from class: com.google.common.io.BaseEncoding.5
            @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                writer.close();
            }

            @Override // java.io.Writer, java.io.Flushable
            public void flush() throws IOException {
                writer.flush();
            }

            @Override // java.io.Writer
            public void write(int i2) throws IOException {
                appendableSeparatingAppendable.append((char) i2);
            }

            @Override // java.io.Writer
            public void write(char[] cArr, int i2, int i3) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
    }

    public abstract boolean canDecode(CharSequence charSequence);

    public final byte[] decode(CharSequence charSequence) {
        try {
            return decodeChecked(charSequence);
        } catch (DecodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public final byte[] decodeChecked(CharSequence charSequence) throws DecodingException {
        CharSequence charSequenceTrimTrailingPadding = trimTrailingPadding(charSequence);
        byte[] bArr = new byte[maxDecodedSize(charSequenceTrimTrailingPadding.length())];
        return extract(bArr, decodeTo(bArr, charSequenceTrimTrailingPadding));
    }

    public abstract int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException;

    @J2ktIncompatible
    @GwtIncompatible
    public final ByteSource decodingSource(final CharSource charSource) {
        Preconditions.checkNotNull(charSource);
        return new ByteSource(this) { // from class: com.google.common.io.BaseEncoding.2
            final /* synthetic */ BaseEncoding this$0;

            {
                this.this$0 = this;
            }

            @Override // com.google.common.io.ByteSource
            public InputStream openStream() throws IOException {
                return this.this$0.decodingStream(charSource.openStream());
            }
        };
    }

    @J2ktIncompatible
    @GwtIncompatible
    public abstract InputStream decodingStream(Reader reader);

    public String encode(byte[] bArr) {
        return encode(bArr, 0, bArr.length);
    }

    public abstract void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException;

    @J2ktIncompatible
    @GwtIncompatible
    public final ByteSink encodingSink(final CharSink charSink) {
        Preconditions.checkNotNull(charSink);
        return new ByteSink(this) { // from class: com.google.common.io.BaseEncoding.1
            final /* synthetic */ BaseEncoding this$0;

            {
                this.this$0 = this;
            }

            @Override // com.google.common.io.ByteSink
            public OutputStream openStream() throws IOException {
                return this.this$0.encodingStream(charSink.openStream());
            }
        };
    }

    @J2ktIncompatible
    @GwtIncompatible
    public abstract OutputStream encodingStream(Writer writer);

    public abstract BaseEncoding ignoreCase();

    public abstract BaseEncoding lowerCase();

    public abstract int maxDecodedSize(int i);

    public abstract int maxEncodedSize(int i);

    public abstract BaseEncoding omitPadding();

    public CharSequence trimTrailingPadding(CharSequence charSequence) {
        return (CharSequence) Preconditions.checkNotNull(charSequence);
    }

    public abstract BaseEncoding upperCase();

    public abstract BaseEncoding withPadChar(char c);

    public abstract BaseEncoding withSeparator(String str, int i);

    public final String encode(byte[] bArr, int i, int i2) {
        Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
        StringBuilder sb = new StringBuilder(maxEncodedSize(i2));
        try {
            encodeTo(sb, bArr, i, i2);
            return sb.toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
