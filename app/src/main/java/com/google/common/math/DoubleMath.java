package com.google.common.math;

import com.apm.insight.log.LogLevel;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class DoubleMath {

    @VisibleForTesting
    static final int MAX_FACTORIAL = 170;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9d;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18d;
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9d;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18d;
    private static final double LN_2 = Math.log(2.0d);

    @VisibleForTesting
    static final double[] everySixteenthFactorial = {1.0d, 2.0922789888E13d, 2.631308369336935E35d, 1.2413915592536073E61d, 1.2688693218588417E89d, 7.156945704626381E118d, 9.916779348709496E149d, 1.974506857221074E182d, 3.856204823625804E215d, 5.5502938327393044E249d, 4.7147236359920616E284d};

    /* JADX INFO: renamed from: com.google.common.math.DoubleMath$1, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode;

        static {
            int[] iArr = new int[RoundingMode.values().length];
            $SwitchMap$java$math$RoundingMode = iArr;
            try {
                iArr[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    private DoubleMath() {
    }

    @GwtIncompatible
    private static double checkFinite(double d) {
        Preconditions.checkArgument(DoubleUtils.isFinite(d));
        return d;
    }

    public static double factorial(int i) {
        MathPreconditions.checkNonNegative("n", i);
        if (i > MAX_FACTORIAL) {
            return Double.POSITIVE_INFINITY;
        }
        double d = 1.0d;
        for (int i2 = (i & (-16)) + 1; i2 <= i; i2++) {
            d *= (double) i2;
        }
        return d * everySixteenthFactorial[i >> 4];
    }

    public static int fuzzyCompare(double d, double d2, double d3) {
        if (fuzzyEquals(d, d2, d3)) {
            return 0;
        }
        if (d < d2) {
            return -1;
        }
        if (d > d2) {
            return 1;
        }
        return Boolean.compare(Double.isNaN(d), Double.isNaN(d2));
    }

    public static boolean fuzzyEquals(double d, double d2, double d3) {
        MathPreconditions.checkNonNegative("tolerance", d3);
        if (Math.copySign(d - d2, 1.0d) <= d3 || d == d2) {
            return true;
        }
        return Double.isNaN(d) && Double.isNaN(d2);
    }

    @GwtIncompatible
    public static boolean isMathematicalInteger(double d) {
        if (DoubleUtils.isFinite(d)) {
            return d == 0.0d || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(d)) <= Math.getExponent(d);
        }
        return false;
    }

    @GwtIncompatible
    public static boolean isPowerOfTwo(double d) {
        if (d > 0.0d && DoubleUtils.isFinite(d)) {
            long significand = DoubleUtils.getSignificand(d);
            if ((significand & (significand - 1)) == 0) {
                return true;
            }
        }
        return false;
    }

    public static double log2(double d) {
        return Math.log(d) / LN_2;
    }

    @GwtIncompatible
    @Deprecated
    public static double mean(double... dArr) {
        Preconditions.checkArgument(dArr.length > 0, "Cannot take mean of 0 values");
        double dCheckFinite = checkFinite(dArr[0]);
        long j = 1;
        for (int i = 1; i < dArr.length; i++) {
            checkFinite(dArr[i]);
            j++;
            dCheckFinite += (dArr[i] - dCheckFinite) / j;
        }
        return dCheckFinite;
    }

    @GwtIncompatible
    public static double roundIntermediate(double d, RoundingMode roundingMode) {
        if (!DoubleUtils.isFinite(d)) {
            throw new ArithmeticException("input is infinite or NaN");
        }
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(d));
                return d;
            case 2:
                return (d >= 0.0d || isMathematicalInteger(d)) ? d : ((long) d) - 1;
            case 3:
                return (d <= 0.0d || isMathematicalInteger(d)) ? d : ((long) d) + 1;
            case 4:
                return d;
            case 5:
                if (isMathematicalInteger(d)) {
                    return d;
                }
                return ((long) d) + ((long) (d > 0.0d ? 1 : -1));
            case 6:
                return Math.rint(d);
            case LogLevel.NONE /* 7 */:
                double dRint = Math.rint(d);
                return Math.abs(d - dRint) == 0.5d ? Math.copySign(0.5d, d) + d : dRint;
            case 8:
                double dRint2 = Math.rint(d);
                return Math.abs(d - dRint2) == 0.5d ? d : dRint2;
            default:
                throw new AssertionError();
        }
    }

    @GwtIncompatible
    public static BigInteger roundToBigInteger(double d, RoundingMode roundingMode) {
        double dRoundIntermediate = roundIntermediate(d, roundingMode);
        if ((MIN_LONG_AS_DOUBLE - dRoundIntermediate < 1.0d) && (dRoundIntermediate < MAX_LONG_AS_DOUBLE_PLUS_ONE)) {
            return BigInteger.valueOf((long) dRoundIntermediate);
        }
        BigInteger bigIntegerShiftLeft = BigInteger.valueOf(DoubleUtils.getSignificand(dRoundIntermediate)).shiftLeft(Math.getExponent(dRoundIntermediate) - 52);
        return dRoundIntermediate < 0.0d ? bigIntegerShiftLeft.negate() : bigIntegerShiftLeft;
    }

    @GwtIncompatible
    public static int roundToInt(double d, RoundingMode roundingMode) {
        double dRoundIntermediate = roundIntermediate(d, roundingMode);
        MathPreconditions.checkInRangeForRoundingInputs((dRoundIntermediate > -2.147483649E9d) & (dRoundIntermediate < 2.147483648E9d), d, roundingMode);
        return (int) dRoundIntermediate;
    }

    @GwtIncompatible
    public static long roundToLong(double d, RoundingMode roundingMode) {
        double dRoundIntermediate = roundIntermediate(d, roundingMode);
        MathPreconditions.checkInRangeForRoundingInputs((MIN_LONG_AS_DOUBLE - dRoundIntermediate < 1.0d) & (dRoundIntermediate < MAX_LONG_AS_DOUBLE_PLUS_ONE), d, roundingMode);
        return (long) dRoundIntermediate;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:29:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    @GwtIncompatible
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int log2(double d, RoundingMode roundingMode) {
        boolean zIsPowerOfTwo;
        Preconditions.checkArgument(d > 0.0d && DoubleUtils.isFinite(d), "x must be positive and finite");
        int exponent = Math.getExponent(d);
        if (!DoubleUtils.isNormal(d)) {
            return log2(d * 4.503599627370496E15d, roundingMode) - 52;
        }
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(d));
                return !z ? exponent + 1 : exponent;
            case 2:
                if (!z) {
                }
                break;
            case 3:
                z = !isPowerOfTwo(d);
                if (!z) {
                }
                break;
            case 4:
                z = exponent < 0;
                zIsPowerOfTwo = isPowerOfTwo(d);
                z &= !zIsPowerOfTwo;
                if (!z) {
                }
                break;
            case 5:
                z = exponent >= 0;
                zIsPowerOfTwo = isPowerOfTwo(d);
                z &= !zIsPowerOfTwo;
                if (!z) {
                }
                break;
            case 6:
            case LogLevel.NONE /* 7 */:
            case 8:
                double dScaleNormalize = DoubleUtils.scaleNormalize(d);
                if (dScaleNormalize * dScaleNormalize > 2.0d) {
                    z = true;
                }
                if (!z) {
                }
                break;
            default:
                throw new AssertionError();
        }
    }

    @Deprecated
    public static double mean(int... iArr) {
        Preconditions.checkArgument(iArr.length > 0, "Cannot take mean of 0 values");
        long j = 0;
        for (int i : iArr) {
            j += (long) i;
        }
        return j / ((double) iArr.length);
    }

    @Deprecated
    public static double mean(long... jArr) {
        Preconditions.checkArgument(jArr.length > 0, "Cannot take mean of 0 values");
        double d = jArr[0];
        long j = 1;
        for (int i = 1; i < jArr.length; i++) {
            j++;
            d += (jArr[i] - d) / j;
        }
        return d;
    }

    @GwtIncompatible
    @Deprecated
    public static double mean(Iterable<? extends Number> iterable) {
        return mean(iterable.iterator());
    }

    @GwtIncompatible
    @Deprecated
    public static double mean(Iterator<? extends Number> it) {
        Preconditions.checkArgument(it.hasNext(), "Cannot take mean of 0 values");
        double dCheckFinite = checkFinite(it.next().doubleValue());
        long j = 1;
        while (it.hasNext()) {
            j++;
            dCheckFinite += (checkFinite(it.next().doubleValue()) - dCheckFinite) / j;
        }
        return dCheckFinite;
    }
}
