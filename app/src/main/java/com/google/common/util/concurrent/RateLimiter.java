package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.SmoothRateLimiter;
import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
@Beta
@J2ktIncompatible
@GwtIncompatible
public abstract class RateLimiter {
    private volatile Object mutexDoNotUseDirectly;
    private final SleepingStopwatch stopwatch;

    public static abstract class SleepingStopwatch {
        public static SleepingStopwatch createFromSystemTimer() {
            return new SleepingStopwatch() { // from class: com.google.common.util.concurrent.RateLimiter.SleepingStopwatch.1
                final Stopwatch stopwatch = Stopwatch.createStarted();

                @Override // com.google.common.util.concurrent.RateLimiter.SleepingStopwatch
                public long readMicros() {
                    return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
                }

                @Override // com.google.common.util.concurrent.RateLimiter.SleepingStopwatch
                public void sleepMicrosUninterruptibly(long j) {
                    if (j > 0) {
                        Uninterruptibles.sleepUninterruptibly(j, TimeUnit.MICROSECONDS);
                    }
                }
            };
        }

        public abstract long readMicros();

        public abstract void sleepMicrosUninterruptibly(long j);
    }

    public RateLimiter(SleepingStopwatch sleepingStopwatch) {
        this.stopwatch = (SleepingStopwatch) Preconditions.checkNotNull(sleepingStopwatch);
    }

    private boolean canAcquire(long j, long j2) {
        return queryEarliestAvailable(j) - j2 <= j;
    }

    private static void checkPermits(int i) {
        Preconditions.checkArgument(i > 0, "Requested permits (%s) must be positive", i);
    }

    public static RateLimiter create(double d) {
        return create(d, SleepingStopwatch.createFromSystemTimer());
    }

    private Object mutex() {
        Object obj;
        Object obj2 = this.mutexDoNotUseDirectly;
        if (obj2 != null) {
            return obj2;
        }
        synchronized (this) {
            try {
                obj = this.mutexDoNotUseDirectly;
                if (obj == null) {
                    obj = new Object();
                    this.mutexDoNotUseDirectly = obj;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return obj;
    }

    public double acquire() {
        return acquire(1);
    }

    public abstract double doGetRate();

    public abstract void doSetRate(double d, long j);

    public final double getRate() {
        double dDoGetRate;
        synchronized (mutex()) {
            dDoGetRate = doGetRate();
        }
        return dDoGetRate;
    }

    public abstract long queryEarliestAvailable(long j);

    public final long reserve(int i) {
        long jReserveAndGetWaitLength;
        checkPermits(i);
        synchronized (mutex()) {
            jReserveAndGetWaitLength = reserveAndGetWaitLength(i, this.stopwatch.readMicros());
        }
        return jReserveAndGetWaitLength;
    }

    public final long reserveAndGetWaitLength(int i, long j) {
        return Math.max(reserveEarliestAvailable(i, j) - j, 0L);
    }

    public abstract long reserveEarliestAvailable(int i, long j);

    public final void setRate(double d) {
        Preconditions.checkArgument(d > 0.0d, "rate must be positive");
        synchronized (mutex()) {
            doSetRate(d, this.stopwatch.readMicros());
        }
    }

    public String toString() {
        return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", Double.valueOf(getRate()));
    }

    @IgnoreJRERequirement
    public boolean tryAcquire(Duration duration) {
        return tryAcquire(1, Internal.toNanosSaturated(duration), TimeUnit.NANOSECONDS);
    }

    @VisibleForTesting
    public static RateLimiter create(double d, SleepingStopwatch sleepingStopwatch) {
        SmoothRateLimiter.SmoothBursty smoothBursty = new SmoothRateLimiter.SmoothBursty(sleepingStopwatch, 1.0d);
        smoothBursty.setRate(d);
        return smoothBursty;
    }

    public double acquire(int i) {
        long jReserve = reserve(i);
        this.stopwatch.sleepMicrosUninterruptibly(jReserve);
        return (jReserve * 1.0d) / TimeUnit.SECONDS.toMicros(1L);
    }

    public boolean tryAcquire(long j, TimeUnit timeUnit) {
        return tryAcquire(1, j, timeUnit);
    }

    public boolean tryAcquire(int i) {
        return tryAcquire(i, 0L, TimeUnit.MICROSECONDS);
    }

    @IgnoreJRERequirement
    public static RateLimiter create(double d, Duration duration) {
        return create(d, Internal.toNanosSaturated(duration), TimeUnit.NANOSECONDS);
    }

    public boolean tryAcquire() {
        return tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
    }

    public static RateLimiter create(double d, long j, TimeUnit timeUnit) {
        Preconditions.checkArgument(j >= 0, "warmupPeriod must not be negative: %s", j);
        return create(d, j, timeUnit, 3.0d, SleepingStopwatch.createFromSystemTimer());
    }

    @IgnoreJRERequirement
    public boolean tryAcquire(int i, Duration duration) {
        return tryAcquire(i, Internal.toNanosSaturated(duration), TimeUnit.NANOSECONDS);
    }

    public boolean tryAcquire(int i, long j, TimeUnit timeUnit) {
        long jMax = Math.max(timeUnit.toMicros(j), 0L);
        checkPermits(i);
        synchronized (mutex()) {
            try {
                long micros = this.stopwatch.readMicros();
                if (!canAcquire(micros, jMax)) {
                    return false;
                }
                this.stopwatch.sleepMicrosUninterruptibly(reserveAndGetWaitLength(i, micros));
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @VisibleForTesting
    public static RateLimiter create(double d, long j, TimeUnit timeUnit, double d2, SleepingStopwatch sleepingStopwatch) {
        SmoothRateLimiter.SmoothWarmingUp smoothWarmingUp = new SmoothRateLimiter.SmoothWarmingUp(sleepingStopwatch, j, timeUnit, d2);
        smoothWarmingUp.setRate(d);
        return smoothWarmingUp;
    }
}
