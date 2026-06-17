package com.google.common.hash;

import com.apm.insight.log.LogLevel;
import com.sun.mail.iap.Response;
import defpackage.AbstractC4671xI0;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes.dex */
final class Murmur3_128HashFunction extends AbstractHashFunction implements Serializable {
    private static final long serialVersionUID = 0;
    private final int seed;
    static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
    static final HashFunction GOOD_FAST_HASH_128 = new Murmur3_128HashFunction(Hashing.GOOD_FAST_HASH_SEED);

    public static final class Murmur3_128Hasher extends AbstractStreamingHasher {
        private static final long C1 = -8663945395140668459L;
        private static final long C2 = 5545529020109919103L;
        private static final int CHUNK_SIZE = 16;
        private long h1;
        private long h2;
        private int length;

        public Murmur3_128Hasher(int i) {
            super(16);
            long j = i;
            this.h1 = j;
            this.h2 = j;
            this.length = 0;
        }

        private void bmix64(long j, long j2) {
            long jMixK1 = mixK1(j) ^ this.h1;
            this.h1 = jMixK1;
            long jRotateLeft = Long.rotateLeft(jMixK1, 27);
            long j3 = this.h2;
            this.h1 = ((jRotateLeft + j3) * 5) + 1390208809;
            long jMixK2 = mixK2(j2) ^ j3;
            this.h2 = jMixK2;
            this.h2 = ((Long.rotateLeft(jMixK2, 31) + this.h1) * 5) + 944331445;
        }

        private static long fmix64(long j) {
            long j2 = (j ^ (j >>> 33)) * (-49064778989728563L);
            long j3 = (j2 ^ (j2 >>> 33)) * (-4265267296055464877L);
            return j3 ^ (j3 >>> 33);
        }

        private static long mixK1(long j) {
            return Long.rotateLeft(j * C1, 31) * C2;
        }

        private static long mixK2(long j) {
            return Long.rotateLeft(j * C2, 33) * C1;
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        public HashCode makeHash() {
            long j = this.h1;
            int i = this.length;
            long j2 = j ^ ((long) i);
            long j3 = this.h2 ^ ((long) i);
            long j4 = j2 + j3;
            this.h1 = j4;
            this.h2 = j3 + j4;
            this.h1 = fmix64(j4);
            long jFmix64 = fmix64(this.h2);
            long j5 = this.h1 + jFmix64;
            this.h1 = j5;
            this.h2 = jFmix64 + j5;
            return HashCode.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(this.h1).putLong(this.h2).array());
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        public void process(ByteBuffer byteBuffer) {
            bmix64(byteBuffer.getLong(), byteBuffer.getLong());
            this.length += 16;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // com.google.common.hash.AbstractStreamingHasher
        public void processRemaining(ByteBuffer byteBuffer) {
            long unsignedInt;
            long unsignedInt2;
            long unsignedInt3;
            long unsignedInt4;
            long unsignedInt5;
            long unsignedInt6;
            long unsignedInt7;
            this.length = byteBuffer.remaining() + this.length;
            long unsignedInt8 = 0;
            switch (byteBuffer.remaining()) {
                case 1:
                    unsignedInt = 0;
                    unsignedInt7 = unsignedInt ^ ((long) Byte.toUnsignedInt(byteBuffer.get(0)));
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 2:
                    unsignedInt2 = 0;
                    unsignedInt = unsignedInt2 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(1))) << 8);
                    unsignedInt7 = unsignedInt ^ ((long) Byte.toUnsignedInt(byteBuffer.get(0)));
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 3:
                    unsignedInt3 = 0;
                    unsignedInt2 = (((long) Byte.toUnsignedInt(byteBuffer.get(2))) << 16) ^ unsignedInt3;
                    unsignedInt = unsignedInt2 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(1))) << 8);
                    unsignedInt7 = unsignedInt ^ ((long) Byte.toUnsignedInt(byteBuffer.get(0)));
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 4:
                    unsignedInt4 = 0;
                    unsignedInt3 = unsignedInt4 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(3))) << 24);
                    unsignedInt2 = (((long) Byte.toUnsignedInt(byteBuffer.get(2))) << 16) ^ unsignedInt3;
                    unsignedInt = unsignedInt2 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(1))) << 8);
                    unsignedInt7 = unsignedInt ^ ((long) Byte.toUnsignedInt(byteBuffer.get(0)));
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 5:
                    unsignedInt5 = 0;
                    unsignedInt4 = unsignedInt5 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(4))) << 32);
                    unsignedInt3 = unsignedInt4 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(3))) << 24);
                    unsignedInt2 = (((long) Byte.toUnsignedInt(byteBuffer.get(2))) << 16) ^ unsignedInt3;
                    unsignedInt = unsignedInt2 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(1))) << 8);
                    unsignedInt7 = unsignedInt ^ ((long) Byte.toUnsignedInt(byteBuffer.get(0)));
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 6:
                    unsignedInt6 = 0;
                    unsignedInt5 = (((long) Byte.toUnsignedInt(byteBuffer.get(5))) << 40) ^ unsignedInt6;
                    unsignedInt4 = unsignedInt5 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(4))) << 32);
                    unsignedInt3 = unsignedInt4 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(3))) << 24);
                    unsignedInt2 = (((long) Byte.toUnsignedInt(byteBuffer.get(2))) << 16) ^ unsignedInt3;
                    unsignedInt = unsignedInt2 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(1))) << 8);
                    unsignedInt7 = unsignedInt ^ ((long) Byte.toUnsignedInt(byteBuffer.get(0)));
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case LogLevel.NONE /* 7 */:
                    unsignedInt6 = ((long) Byte.toUnsignedInt(byteBuffer.get(6))) << 48;
                    unsignedInt5 = (((long) Byte.toUnsignedInt(byteBuffer.get(5))) << 40) ^ unsignedInt6;
                    unsignedInt4 = unsignedInt5 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(4))) << 32);
                    unsignedInt3 = unsignedInt4 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(3))) << 24);
                    unsignedInt2 = (((long) Byte.toUnsignedInt(byteBuffer.get(2))) << 16) ^ unsignedInt3;
                    unsignedInt = unsignedInt2 ^ (((long) Byte.toUnsignedInt(byteBuffer.get(1))) << 8);
                    unsignedInt7 = unsignedInt ^ ((long) Byte.toUnsignedInt(byteBuffer.get(0)));
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 8:
                    unsignedInt7 = byteBuffer.getLong();
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 9:
                    unsignedInt8 ^= (long) Byte.toUnsignedInt(byteBuffer.get(8));
                    unsignedInt7 = byteBuffer.getLong();
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 10:
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(9))) << 8;
                    unsignedInt8 ^= (long) Byte.toUnsignedInt(byteBuffer.get(8));
                    unsignedInt7 = byteBuffer.getLong();
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 11:
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(10))) << 16;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(9))) << 8;
                    unsignedInt8 ^= (long) Byte.toUnsignedInt(byteBuffer.get(8));
                    unsignedInt7 = byteBuffer.getLong();
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case Response.BAD /* 12 */:
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(11))) << 24;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(10))) << 16;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(9))) << 8;
                    unsignedInt8 ^= (long) Byte.toUnsignedInt(byteBuffer.get(8));
                    unsignedInt7 = byteBuffer.getLong();
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 13:
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(12))) << 32;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(11))) << 24;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(10))) << 16;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(9))) << 8;
                    unsignedInt8 ^= (long) Byte.toUnsignedInt(byteBuffer.get(8));
                    unsignedInt7 = byteBuffer.getLong();
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 14:
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(13))) << 40;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(12))) << 32;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(11))) << 24;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(10))) << 16;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(9))) << 8;
                    unsignedInt8 ^= (long) Byte.toUnsignedInt(byteBuffer.get(8));
                    unsignedInt7 = byteBuffer.getLong();
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                case 15:
                    unsignedInt8 = ((long) Byte.toUnsignedInt(byteBuffer.get(14))) << 48;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(13))) << 40;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(12))) << 32;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(11))) << 24;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(10))) << 16;
                    unsignedInt8 ^= ((long) Byte.toUnsignedInt(byteBuffer.get(9))) << 8;
                    unsignedInt8 ^= (long) Byte.toUnsignedInt(byteBuffer.get(8));
                    unsignedInt7 = byteBuffer.getLong();
                    this.h1 = mixK1(unsignedInt7) ^ this.h1;
                    this.h2 ^= mixK2(unsignedInt8);
                    return;
                default:
                    throw new AssertionError("Should never get here.");
            }
        }
    }

    public Murmur3_128HashFunction(int i) {
        this.seed = i;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 128;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Murmur3_128HashFunction) && this.seed == ((Murmur3_128HashFunction) obj).seed;
    }

    public int hashCode() {
        return Murmur3_128HashFunction.class.hashCode() ^ this.seed;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new Murmur3_128Hasher(this.seed);
    }

    public String toString() {
        return AbstractC4671xI0.n(new StringBuilder("Hashing.murmur3_128("), this.seed, ")");
    }
}
