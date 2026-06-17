package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.BloomFilterStrategies;
import com.google.common.math.DoubleMath;
import com.google.common.primitives.SignedBytes;
import com.google.common.primitives.UnsignedBytes;
import defpackage.AbstractC2039eV;
import defpackage.C4443vg;
import defpackage.C4583wg;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collector;

/* JADX INFO: loaded from: classes.dex */
@Beta
public final class BloomFilter<T> implements Predicate<T>, Serializable {
    private static final double LOG_TWO;
    private static final double SQUARED_LOG_TWO;
    private static final long serialVersionUID = 912559;
    private final BloomFilterStrategies.LockFreeBitArray bits;
    private final Funnel<? super T> funnel;
    private final int numHashFunctions;
    private final Strategy strategy;

    public static final class SerialForm<T> implements Serializable {
        private static final long serialVersionUID = 1;
        final long[] data;
        final Funnel<? super T> funnel;
        final int numHashFunctions;
        final Strategy strategy;

        public SerialForm(BloomFilter<T> bloomFilter) {
            this.data = BloomFilterStrategies.LockFreeBitArray.toPlainArray(((BloomFilter) bloomFilter).bits.data);
            this.numHashFunctions = ((BloomFilter) bloomFilter).numHashFunctions;
            this.funnel = ((BloomFilter) bloomFilter).funnel;
            this.strategy = ((BloomFilter) bloomFilter).strategy;
        }

        public Object readResolve() {
            return new BloomFilter(new BloomFilterStrategies.LockFreeBitArray(this.data), this.numHashFunctions, this.funnel, this.strategy);
        }
    }

    public interface Strategy extends Serializable {
        <T> boolean mightContain(@ParametricNullness T t, Funnel<? super T> funnel, int i, BloomFilterStrategies.LockFreeBitArray lockFreeBitArray);

        int ordinal();

        <T> boolean put(@ParametricNullness T t, Funnel<? super T> funnel, int i, BloomFilterStrategies.LockFreeBitArray lockFreeBitArray);
    }

    static {
        double dLog = Math.log(2.0d);
        LOG_TWO = dLog;
        SQUARED_LOG_TWO = dLog * dLog;
    }

    public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int i, double d) {
        return create(funnel, i, d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ BloomFilter lambda$toBloomFilter$1(BloomFilter bloomFilter, BloomFilter bloomFilter2) {
        bloomFilter.putAll(bloomFilter2);
        return bloomFilter;
    }

    @VisibleForTesting
    public static long optimalNumOfBits(long j, double d) {
        if (d == 0.0d) {
            d = Double.MIN_VALUE;
        }
        return (long) ((Math.log(d) * (-j)) / SQUARED_LOG_TWO);
    }

    @VisibleForTesting
    public static int optimalNumOfHashFunctions(double d) {
        return Math.max(1, (int) Math.round((-Math.log(d)) / LOG_TWO));
    }

    public static <T> BloomFilter<T> readFrom(InputStream inputStream, Funnel<? super T> funnel) throws IOException {
        int i;
        int unsignedInt;
        Preconditions.checkNotNull(inputStream, "InputStream");
        Preconditions.checkNotNull(funnel, "Funnel");
        int i2 = -1;
        try {
            try {
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                byte b = dataInputStream.readByte();
                try {
                    unsignedInt = Byte.toUnsignedInt(dataInputStream.readByte());
                    try {
                        i2 = dataInputStream.readInt();
                        BloomFilterStrategies bloomFilterStrategies = BloomFilterStrategies.values()[b];
                        BloomFilterStrategies.LockFreeBitArray lockFreeBitArray = new BloomFilterStrategies.LockFreeBitArray(Math.multiplyExact(i2, 64L));
                        for (int i3 = 0; i3 < i2; i3++) {
                            lockFreeBitArray.putData(i3, dataInputStream.readLong());
                        }
                        return new BloomFilter<>(lockFreeBitArray, unsignedInt, funnel, bloomFilterStrategies);
                    } catch (Exception e) {
                        e = e;
                        int i4 = i2;
                        i2 = b;
                        i = i4;
                        StringBuilder sbQ = AbstractC2039eV.q(i2, "Unable to deserialize BloomFilter from InputStream. strategyOrdinal: ", unsignedInt, " numHashFunctions: ", " dataLength: ");
                        sbQ.append(i);
                        throw new IOException(sbQ.toString(), e);
                    }
                } catch (Exception e2) {
                    e = e2;
                    unsignedInt = -1;
                    i2 = b;
                    i = -1;
                }
            } catch (IOException e3) {
                throw e3;
            }
        } catch (Exception e4) {
            e = e4;
            i = -1;
            unsignedInt = -1;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @IgnoreJRERequirement
    public static <T> Collector<T, ?, BloomFilter<T>> toBloomFilter(Funnel<? super T> funnel, long j) {
        return toBloomFilter(funnel, j, 0.03d);
    }

    private Object writeReplace() {
        return new SerialForm(this);
    }

    @Override // com.google.common.base.Predicate
    @Deprecated
    public boolean apply(@ParametricNullness T t) {
        return mightContain(t);
    }

    public long approximateElementCount() {
        double dBitSize = this.bits.bitSize();
        return DoubleMath.roundToLong(((-Math.log1p(-(this.bits.bitCount() / dBitSize))) * dBitSize) / ((double) this.numHashFunctions), RoundingMode.HALF_UP);
    }

    @VisibleForTesting
    public long bitSize() {
        return this.bits.bitSize();
    }

    public BloomFilter<T> copy() {
        return new BloomFilter<>(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
    }

    @Override // com.google.common.base.Predicate
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof BloomFilter) {
            BloomFilter bloomFilter = (BloomFilter) obj;
            if (this.numHashFunctions == bloomFilter.numHashFunctions && this.funnel.equals(bloomFilter.funnel) && this.bits.equals(bloomFilter.bits) && this.strategy.equals(bloomFilter.strategy)) {
                return true;
            }
        }
        return false;
    }

    public double expectedFpp() {
        return Math.pow(this.bits.bitCount() / bitSize(), this.numHashFunctions);
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits);
    }

    public boolean isCompatible(BloomFilter<T> bloomFilter) {
        Preconditions.checkNotNull(bloomFilter);
        return this != bloomFilter && this.numHashFunctions == bloomFilter.numHashFunctions && bitSize() == bloomFilter.bitSize() && this.strategy.equals(bloomFilter.strategy) && this.funnel.equals(bloomFilter.funnel);
    }

    public boolean mightContain(@ParametricNullness T t) {
        return this.strategy.mightContain(t, this.funnel, this.numHashFunctions, this.bits);
    }

    public boolean put(@ParametricNullness T t) {
        return this.strategy.put(t, this.funnel, this.numHashFunctions, this.bits);
    }

    public void putAll(BloomFilter<T> bloomFilter) {
        Preconditions.checkNotNull(bloomFilter);
        Preconditions.checkArgument(this != bloomFilter, "Cannot combine a BloomFilter with itself.");
        int i = this.numHashFunctions;
        int i2 = bloomFilter.numHashFunctions;
        Preconditions.checkArgument(i == i2, "BloomFilters must have the same number of hash functions (%s != %s)", i, i2);
        Preconditions.checkArgument(bitSize() == bloomFilter.bitSize(), "BloomFilters must have the same size underlying bit arrays (%s != %s)", bitSize(), bloomFilter.bitSize());
        Preconditions.checkArgument(this.strategy.equals(bloomFilter.strategy), "BloomFilters must have equal strategies (%s != %s)", this.strategy, bloomFilter.strategy);
        Preconditions.checkArgument(this.funnel.equals(bloomFilter.funnel), "BloomFilters must have equal funnels (%s != %s)", this.funnel, bloomFilter.funnel);
        this.bits.putAll(bloomFilter.bits);
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeByte(SignedBytes.checkedCast(this.strategy.ordinal()));
        dataOutputStream.writeByte(UnsignedBytes.checkedCast(this.numHashFunctions));
        dataOutputStream.writeInt(this.bits.data.length());
        for (int i = 0; i < this.bits.data.length(); i++) {
            dataOutputStream.writeLong(this.bits.data.get(i));
        }
    }

    private BloomFilter(BloomFilterStrategies.LockFreeBitArray lockFreeBitArray, int i, Funnel<? super T> funnel, Strategy strategy) {
        Preconditions.checkArgument(i > 0, "numHashFunctions (%s) must be > 0", i);
        Preconditions.checkArgument(i <= 255, "numHashFunctions (%s) must be <= 255", i);
        this.bits = (BloomFilterStrategies.LockFreeBitArray) Preconditions.checkNotNull(lockFreeBitArray);
        this.numHashFunctions = i;
        this.funnel = (Funnel) Preconditions.checkNotNull(funnel);
        this.strategy = (Strategy) Preconditions.checkNotNull(strategy);
    }

    public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long j, double d) {
        return create(funnel, j, d, BloomFilterStrategies.MURMUR128_MITZ_64);
    }

    @IgnoreJRERequirement
    public static <T> Collector<T, ?, BloomFilter<T>> toBloomFilter(final Funnel<? super T> funnel, final long j, final double d) {
        Preconditions.checkNotNull(funnel);
        Preconditions.checkArgument(j >= 0, "Expected insertions (%s) must be >= 0", j);
        Preconditions.checkArgument(d > 0.0d, "False positive probability (%s) must be > 0.0", Double.valueOf(d));
        Preconditions.checkArgument(d < 1.0d, "False positive probability (%s) must be < 1.0", Double.valueOf(d));
        return Collector.of(new Supplier() { // from class: ug
            @Override // java.util.function.Supplier
            public final Object get() {
                return BloomFilter.create(funnel, j, d);
            }
        }, new C4443vg(0), new C4583wg(0), Collector.Characteristics.UNORDERED, Collector.Characteristics.CONCURRENT);
    }

    @VisibleForTesting
    public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long j, double d, Strategy strategy) {
        Preconditions.checkNotNull(funnel);
        Preconditions.checkArgument(j >= 0, "Expected insertions (%s) must be >= 0", j);
        Preconditions.checkArgument(d > 0.0d, "False positive probability (%s) must be > 0.0", Double.valueOf(d));
        Preconditions.checkArgument(d < 1.0d, "False positive probability (%s) must be < 1.0", Double.valueOf(d));
        Preconditions.checkNotNull(strategy);
        if (j == 0) {
            j = 1;
        }
        long jOptimalNumOfBits = optimalNumOfBits(j, d);
        try {
            return new BloomFilter<>(new BloomFilterStrategies.LockFreeBitArray(jOptimalNumOfBits), optimalNumOfHashFunctions(d), funnel, strategy);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(AbstractC2039eV.k(jOptimalNumOfBits, "Could not create BloomFilter of ", " bits"), e);
        }
    }

    public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int i) {
        return create(funnel, i);
    }

    public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long j) {
        return create(funnel, j, 0.03d);
    }
}
