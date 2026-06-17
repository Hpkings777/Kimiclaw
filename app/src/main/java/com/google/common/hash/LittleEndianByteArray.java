package com.google.common.hash;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedBytes;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.Objects;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: classes.dex */
final class LittleEndianByteArray {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final LittleEndianBytes byteArray = makeGetter();

    public enum JavaLittleEndianBytes implements LittleEndianBytes {
        INSTANCE { // from class: com.google.common.hash.LittleEndianByteArray.JavaLittleEndianBytes.1
            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public long getLongLittleEndian(byte[] bArr, int i) {
                return Longs.fromBytes(bArr[i + 7], bArr[i + 6], bArr[i + 5], bArr[i + 4], bArr[i + 3], bArr[i + 2], bArr[i + 1], bArr[i]);
            }

            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public void putLongLittleEndian(byte[] bArr, int i, long j) {
                long j2 = 255;
                for (int i2 = 0; i2 < 8; i2++) {
                    bArr[i + i2] = (byte) ((j & j2) >> (i2 * 8));
                    j2 <<= 8;
                }
            }

            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public boolean usesFastPath() {
                return false;
            }
        }
    }

    public interface LittleEndianBytes {
        long getLongLittleEndian(byte[] bArr, int i);

        void putLongLittleEndian(byte[] bArr, int i, long j);

        boolean usesFastPath();
    }

    @VisibleForTesting
    public enum UnsafeByteArray implements LittleEndianBytes {
        UNSAFE_LITTLE_ENDIAN { // from class: com.google.common.hash.LittleEndianByteArray.UnsafeByteArray.1
            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public long getLongLittleEndian(byte[] bArr, int i) {
                return UnsafeByteArray.theUnsafe.getLong(bArr, ((long) i) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET));
            }

            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public void putLongLittleEndian(byte[] bArr, int i, long j) {
                UnsafeByteArray.theUnsafe.putLong(bArr, ((long) i) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET), j);
            }
        },
        UNSAFE_BIG_ENDIAN { // from class: com.google.common.hash.LittleEndianByteArray.UnsafeByteArray.2
            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public long getLongLittleEndian(byte[] bArr, int i) {
                return Long.reverseBytes(UnsafeByteArray.theUnsafe.getLong(bArr, ((long) i) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET)));
            }

            @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
            public void putLongLittleEndian(byte[] bArr, int i, long j) {
                UnsafeByteArray.theUnsafe.putLong(bArr, ((long) i) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET), Long.reverseBytes(j));
            }
        };

        private static final int BYTE_ARRAY_BASE_OFFSET;
        private static final Unsafe theUnsafe;

        static {
            Unsafe unsafe = getUnsafe();
            theUnsafe = unsafe;
            BYTE_ARRAY_BASE_OFFSET = unsafe.arrayBaseOffset(byte[].class);
            if (unsafe.arrayIndexScale(byte[].class) != 1) {
                throw new AssertionError();
            }
        }

        private static Unsafe getUnsafe() {
            try {
                try {
                    return Unsafe.getUnsafe();
                } catch (PrivilegedActionException e) {
                    throw new RuntimeException("Could not initialize intrinsics", e.getCause());
                }
            } catch (SecurityException unused) {
                return (Unsafe) AccessController.doPrivileged(new a());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Unsafe lambda$getUnsafe$0() throws Exception {
            for (Field field : Unsafe.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object obj = field.get(null);
                if (Unsafe.class.isInstance(obj)) {
                    return (Unsafe) Unsafe.class.cast(obj);
                }
            }
            throw new NoSuchFieldError("the Unsafe");
        }

        @Override // com.google.common.hash.LittleEndianByteArray.LittleEndianBytes
        public boolean usesFastPath() {
            return true;
        }
    }

    private LittleEndianByteArray() {
    }

    public static int load32(byte[] bArr, int i) {
        return ((bArr[i + 3] & UnsignedBytes.MAX_VALUE) << 24) | (bArr[i] & UnsignedBytes.MAX_VALUE) | ((bArr[i + 1] & UnsignedBytes.MAX_VALUE) << 8) | ((bArr[i + 2] & UnsignedBytes.MAX_VALUE) << 16);
    }

    public static long load64(byte[] bArr, int i) {
        return byteArray.getLongLittleEndian(bArr, i);
    }

    public static long load64Safely(byte[] bArr, int i, int i2) {
        int iMin = Math.min(i2, 8);
        long j = 0;
        for (int i3 = 0; i3 < iMin; i3++) {
            j |= (((long) bArr[i + i3]) & 255) << (i3 * 8);
        }
        return j;
    }

    public static LittleEndianBytes makeGetter() {
        try {
            if (Objects.equals(System.getProperty("os.arch"), "amd64")) {
                return ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN) ? UnsafeByteArray.UNSAFE_LITTLE_ENDIAN : UnsafeByteArray.UNSAFE_BIG_ENDIAN;
            }
        } catch (Throwable unused) {
        }
        return JavaLittleEndianBytes.INSTANCE;
    }

    public static void store64(byte[] bArr, int i, long j) {
        byteArray.putLongLittleEndian(bArr, i, j);
    }

    public static boolean usingFastPath() {
        return byteArray.usesFastPath();
    }
}
