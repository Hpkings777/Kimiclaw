package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import defpackage.AbstractC4671xI0;
import java.math.BigInteger;
import java.math.RoundingMode;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class MathPreconditions {
    private MathPreconditions() {
    }

    public static void checkInRangeForRoundingInputs(boolean z, double d, RoundingMode roundingMode) {
        if (z) {
            return;
        }
        throw new ArithmeticException("rounded value is out of range for input " + d + " and rounding mode " + roundingMode);
    }

    public static void checkNoOverflow(boolean z, String str, int i, int i2) {
        if (z) {
            return;
        }
        StringBuilder sb = new StringBuilder("overflow: ");
        sb.append(str);
        sb.append("(");
        sb.append(i);
        sb.append(", ");
        throw new ArithmeticException(AbstractC4671xI0.n(sb, i2, ")"));
    }

    public static int checkNonNegative(String str, int i) {
        if (i >= 0) {
            return i;
        }
        throw new IllegalArgumentException(str + " (" + i + ") must be >= 0");
    }

    public static int checkPositive(String str, int i) {
        if (i > 0) {
            return i;
        }
        throw new IllegalArgumentException(str + " (" + i + ") must be > 0");
    }

    public static void checkRoundingUnnecessary(boolean z) {
        if (!z) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }

    public static long checkNonNegative(String str, long j) {
        if (j >= 0) {
            return j;
        }
        throw new IllegalArgumentException(str + " (" + j + ") must be >= 0");
    }

    public static long checkPositive(String str, long j) {
        if (j > 0) {
            return j;
        }
        throw new IllegalArgumentException(str + " (" + j + ") must be > 0");
    }

    public static BigInteger checkNonNegative(String str, BigInteger bigInteger) {
        if (bigInteger.signum() >= 0) {
            return bigInteger;
        }
        throw new IllegalArgumentException(str + " (" + bigInteger + ") must be >= 0");
    }

    public static BigInteger checkPositive(String str, BigInteger bigInteger) {
        if (bigInteger.signum() > 0) {
            return bigInteger;
        }
        throw new IllegalArgumentException(str + " (" + bigInteger + ") must be > 0");
    }

    public static double checkNonNegative(String str, double d) {
        if (d >= 0.0d) {
            return d;
        }
        throw new IllegalArgumentException(str + " (" + d + ") must be >= 0");
    }

    public static void checkNoOverflow(boolean z, String str, long j, long j2) {
        if (z) {
            return;
        }
        throw new ArithmeticException("overflow: " + str + "(" + j + ", " + j2 + ")");
    }
}
