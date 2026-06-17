package com.google.common.math;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Preconditions;
import defpackage.C1093Un;
import defpackage.C3416oK0;
import defpackage.C3556pK0;
import defpackage.C3696qK0;
import defpackage.C4443vg;
import java.util.Iterator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public final class StatsAccumulator {
    private long count = 0;
    private double mean = 0.0d;
    private double sumOfSquaresOfDeltas = 0.0d;
    private double min = Double.NaN;
    private double max = Double.NaN;

    public static double calculateNewMeanNonFinite(double d, double d2) {
        if (Double.isFinite(d)) {
            return d2;
        }
        if (Double.isFinite(d2) || d == d2) {
            return d;
        }
        return Double.NaN;
    }

    private void merge(long j, double d, double d2, double d3, double d4) {
        long j2 = this.count;
        if (j2 == 0) {
            this.count = j;
            this.mean = d;
            this.sumOfSquaresOfDeltas = d2;
            this.min = d3;
            this.max = d4;
            return;
        }
        this.count = j2 + j;
        if (Double.isFinite(this.mean) && Double.isFinite(d)) {
            double d5 = this.mean;
            double d6 = d - d5;
            double d7 = j;
            double d8 = ((d6 * d7) / this.count) + d5;
            this.mean = d8;
            this.sumOfSquaresOfDeltas = ((d - d8) * d6 * d7) + d2 + this.sumOfSquaresOfDeltas;
        } else {
            this.mean = calculateNewMeanNonFinite(this.mean, d);
            this.sumOfSquaresOfDeltas = Double.NaN;
        }
        this.min = Math.min(this.min, d3);
        this.max = Math.max(this.max, d4);
    }

    public void add(double d) {
        long j = this.count;
        if (j == 0) {
            this.count = 1L;
            this.mean = d;
            this.min = d;
            this.max = d;
            if (Double.isFinite(d)) {
                return;
            }
            this.sumOfSquaresOfDeltas = Double.NaN;
            return;
        }
        this.count = j + 1;
        if (Double.isFinite(d) && Double.isFinite(this.mean)) {
            double d2 = this.mean;
            double d3 = d - d2;
            double d4 = (d3 / this.count) + d2;
            this.mean = d4;
            this.sumOfSquaresOfDeltas = ((d - d4) * d3) + this.sumOfSquaresOfDeltas;
        } else {
            this.mean = calculateNewMeanNonFinite(this.mean, d);
            this.sumOfSquaresOfDeltas = Double.NaN;
        }
        this.min = Math.min(this.min, d);
        this.max = Math.max(this.max, d);
    }

    public void addAll(Iterable<? extends Number> iterable) {
        Iterator<? extends Number> it = iterable.iterator();
        while (it.hasNext()) {
            add(it.next().doubleValue());
        }
    }

    public long count() {
        return this.count;
    }

    public double max() {
        Preconditions.checkState(this.count != 0);
        return this.max;
    }

    public double mean() {
        Preconditions.checkState(this.count != 0);
        return this.mean;
    }

    public double min() {
        Preconditions.checkState(this.count != 0);
        return this.min;
    }

    public final double populationStandardDeviation() {
        return Math.sqrt(populationVariance());
    }

    public final double populationVariance() {
        Preconditions.checkState(this.count != 0);
        if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
            return Double.NaN;
        }
        if (this.count == 1) {
            return 0.0d;
        }
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / this.count;
    }

    public final double sampleStandardDeviation() {
        return Math.sqrt(sampleVariance());
    }

    public final double sampleVariance() {
        Preconditions.checkState(this.count > 1);
        if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
            return Double.NaN;
        }
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1);
    }

    public Stats snapshot() {
        return new Stats(this.count, this.mean, this.sumOfSquaresOfDeltas, this.min, this.max);
    }

    public final double sum() {
        return this.mean * this.count;
    }

    public double sumOfSquaresOfDeltas() {
        return this.sumOfSquaresOfDeltas;
    }

    public void addAll(Iterator<? extends Number> it) {
        while (it.hasNext()) {
            add(it.next().doubleValue());
        }
    }

    public void addAll(double... dArr) {
        for (double d : dArr) {
            add(d);
        }
    }

    public void addAll(int... iArr) {
        for (int i : iArr) {
            add(i);
        }
    }

    public void addAll(long... jArr) {
        for (long j : jArr) {
            add(j);
        }
    }

    @IgnoreJRERequirement
    public void addAll(DoubleStream doubleStream) {
        addAll((StatsAccumulator) doubleStream.collect(new C1093Un(10), new C3696qK0(), new C4443vg(5)));
    }

    @IgnoreJRERequirement
    public void addAll(IntStream intStream) {
        addAll((StatsAccumulator) intStream.collect(new C1093Un(10), new C3556pK0(), new C4443vg(5)));
    }

    @IgnoreJRERequirement
    public void addAll(LongStream longStream) {
        addAll((StatsAccumulator) longStream.collect(new C1093Un(10), new C3416oK0(), new C4443vg(5)));
    }

    public void addAll(Stats stats) {
        if (stats.count() == 0) {
            return;
        }
        merge(stats.count(), stats.mean(), stats.sumOfSquaresOfDeltas(), stats.min(), stats.max());
    }

    public void addAll(StatsAccumulator statsAccumulator) {
        if (statsAccumulator.count() == 0) {
            return;
        }
        merge(statsAccumulator.count(), statsAccumulator.mean(), statsAccumulator.sumOfSquaresOfDeltas(), statsAccumulator.min(), statsAccumulator.max());
    }
}
