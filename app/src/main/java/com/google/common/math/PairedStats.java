package com.google.common.math;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public final class PairedStats implements Serializable {
    private static final int BYTES = 88;
    private static final long serialVersionUID = 0;
    private final double sumOfProductsOfDeltas;
    private final Stats xStats;
    private final Stats yStats;

    public PairedStats(Stats stats, Stats stats2, double d) {
        this.xStats = stats;
        this.yStats = stats2;
        this.sumOfProductsOfDeltas = d;
    }

    private static double ensureInUnitRange(double d) {
        if (d >= 1.0d) {
            return 1.0d;
        }
        if (d <= -1.0d) {
            return -1.0d;
        }
        return d;
    }

    private static double ensurePositive(double d) {
        if (d > 0.0d) {
            return d;
        }
        return Double.MIN_VALUE;
    }

    public static PairedStats fromByteArray(byte[] bArr) {
        Preconditions.checkNotNull(bArr);
        Preconditions.checkArgument(bArr.length == BYTES, "Expected PairedStats.BYTES = %s, got %s", BYTES, bArr.length);
        ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
        return new PairedStats(Stats.readFrom(byteBufferOrder), Stats.readFrom(byteBufferOrder), byteBufferOrder.getDouble());
    }

    public long count() {
        return this.xStats.count();
    }

    public boolean equals(Object obj) {
        if (obj == null || PairedStats.class != obj.getClass()) {
            return false;
        }
        PairedStats pairedStats = (PairedStats) obj;
        return this.xStats.equals(pairedStats.xStats) && this.yStats.equals(pairedStats.yStats) && Double.doubleToLongBits(this.sumOfProductsOfDeltas) == Double.doubleToLongBits(pairedStats.sumOfProductsOfDeltas);
    }

    public int hashCode() {
        return Objects.hash(this.xStats, this.yStats, Double.valueOf(this.sumOfProductsOfDeltas));
    }

    public LinearTransformation leastSquaresFit() {
        Preconditions.checkState(count() > 1);
        if (Double.isNaN(this.sumOfProductsOfDeltas)) {
            return LinearTransformation.forNaN();
        }
        double dSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
        if (dSumOfSquaresOfDeltas > 0.0d) {
            return this.yStats.sumOfSquaresOfDeltas() > 0.0d ? LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean()).withSlope(this.sumOfProductsOfDeltas / dSumOfSquaresOfDeltas) : LinearTransformation.horizontal(this.yStats.mean());
        }
        Preconditions.checkState(this.yStats.sumOfSquaresOfDeltas() > 0.0d);
        return LinearTransformation.vertical(this.xStats.mean());
    }

    public double pearsonsCorrelationCoefficient() {
        Preconditions.checkState(count() > 1);
        if (Double.isNaN(this.sumOfProductsOfDeltas)) {
            return Double.NaN;
        }
        double dSumOfSquaresOfDeltas = xStats().sumOfSquaresOfDeltas();
        double dSumOfSquaresOfDeltas2 = yStats().sumOfSquaresOfDeltas();
        Preconditions.checkState(dSumOfSquaresOfDeltas > 0.0d);
        Preconditions.checkState(dSumOfSquaresOfDeltas2 > 0.0d);
        return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(ensurePositive(dSumOfSquaresOfDeltas * dSumOfSquaresOfDeltas2)));
    }

    public double populationCovariance() {
        Preconditions.checkState(count() != 0);
        return this.sumOfProductsOfDeltas / count();
    }

    public double sampleCovariance() {
        Preconditions.checkState(count() > 1);
        return this.sumOfProductsOfDeltas / (count() - 1);
    }

    public double sumOfProductsOfDeltas() {
        return this.sumOfProductsOfDeltas;
    }

    public byte[] toByteArray() {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(BYTES).order(ByteOrder.LITTLE_ENDIAN);
        this.xStats.writeTo(byteBufferOrder);
        this.yStats.writeTo(byteBufferOrder);
        byteBufferOrder.putDouble(this.sumOfProductsOfDeltas);
        return byteBufferOrder.array();
    }

    public String toString() {
        return count() > 0 ? MoreObjects.toStringHelper(this).add("xStats", this.xStats).add("yStats", this.yStats).add("populationCovariance", populationCovariance()).toString() : MoreObjects.toStringHelper(this).add("xStats", this.xStats).add("yStats", this.yStats).toString();
    }

    public Stats xStats() {
        return this.xStats;
    }

    public Stats yStats() {
        return this.yStats;
    }
}
