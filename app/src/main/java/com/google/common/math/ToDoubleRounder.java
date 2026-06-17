package com.google.common.math;

import com.apm.insight.log.LogLevel;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.lang.Comparable;
import java.lang.Number;
import java.math.RoundingMode;

/* JADX INFO: loaded from: classes.dex */
@GwtIncompatible
abstract class ToDoubleRounder<X extends Number & Comparable<X>> {

    /* JADX INFO: renamed from: com.google.common.math.ToDoubleRounder$1, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode;

        static {
            int[] iArr = new int[RoundingMode.values().length];
            $SwitchMap$java$math$RoundingMode = iArr;
            try {
                iArr[RoundingMode.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    public abstract X minus(X x, X x2);

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0058  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final double roundToDouble(X x, RoundingMode roundingMode) {
        Number x2;
        double dNextUp;
        int iCompareTo;
        Preconditions.checkNotNull(x, "x");
        Preconditions.checkNotNull(roundingMode, "mode");
        double dRoundToDoubleArbitrarily = roundToDoubleArbitrarily(x);
        if (Double.isInfinite(dRoundToDoubleArbitrarily)) {
            switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                    return ((double) sign(x)) * Double.MAX_VALUE;
                case 5:
                    return dRoundToDoubleArbitrarily == Double.POSITIVE_INFINITY ? Double.MAX_VALUE : Double.NEGATIVE_INFINITY;
                case 6:
                    return dRoundToDoubleArbitrarily == Double.POSITIVE_INFINITY ? Double.POSITIVE_INFINITY : -1.7976931348623157E308d;
                case LogLevel.NONE /* 7 */:
                    break;
                case 8:
                    throw new ArithmeticException(x + " cannot be represented precisely as a double");
                default:
                    Number x3 = toX(dRoundToDoubleArbitrarily, RoundingMode.UNNECESSARY);
                    int iCompareTo2 = ((Comparable) x).compareTo(x3);
                    int[] iArr = AnonymousClass1.$SwitchMap$java$math$RoundingMode;
                    switch (iArr[roundingMode.ordinal()]) {
                        case 1:
                            if (sign(x) >= 0) {
                                if (iCompareTo2 < 0) {
                                    return DoubleUtils.nextDown(dRoundToDoubleArbitrarily);
                                }
                            } else if (iCompareTo2 > 0) {
                                return Math.nextUp(dRoundToDoubleArbitrarily);
                            }
                        case 2:
                        case 3:
                        case 4:
                            if (iCompareTo2 >= 0) {
                                dNextUp = Math.nextUp(dRoundToDoubleArbitrarily);
                                if (dNextUp != Double.POSITIVE_INFINITY) {
                                    x2 = toX(dNextUp, RoundingMode.CEILING);
                                    iCompareTo = ((Comparable) minus(x, x3)).compareTo(minus(x2, x));
                                    if (iCompareTo >= 0) {
                                        if (iCompareTo <= 0) {
                                            int i = iArr[roundingMode.ordinal()];
                                            if (i != 2) {
                                                if (i != 3) {
                                                    if (i != 4) {
                                                        throw new AssertionError("impossible");
                                                    }
                                                    if (sign(x) >= 0) {
                                                    }
                                                } else if (sign(x) >= 0) {
                                                }
                                            } else if ((Double.doubleToRawLongBits(dRoundToDoubleArbitrarily) & 1) == 0) {
                                            }
                                        }
                                        return dNextUp;
                                    }
                                    return dRoundToDoubleArbitrarily;
                                }
                            } else {
                                double dNextDown = DoubleUtils.nextDown(dRoundToDoubleArbitrarily);
                                if (dNextDown != Double.NEGATIVE_INFINITY) {
                                    Number x4 = toX(dNextDown, RoundingMode.FLOOR);
                                    x2 = x3;
                                    x3 = x4;
                                    dNextUp = dRoundToDoubleArbitrarily;
                                    dRoundToDoubleArbitrarily = dNextDown;
                                    iCompareTo = ((Comparable) minus(x, x3)).compareTo(minus(x2, x));
                                    if (iCompareTo >= 0) {
                                    }
                                    return dRoundToDoubleArbitrarily;
                                }
                            }
                        case 5:
                            if (iCompareTo2 < 0) {
                                return DoubleUtils.nextDown(dRoundToDoubleArbitrarily);
                            }
                            break;
                        case 6:
                            if (iCompareTo2 > 0) {
                                return Math.nextUp(dRoundToDoubleArbitrarily);
                            }
                            break;
                        case LogLevel.NONE /* 7 */:
                            if (sign(x) >= 0) {
                                if (iCompareTo2 > 0) {
                                    return Math.nextUp(dRoundToDoubleArbitrarily);
                                }
                            } else if (iCompareTo2 < 0) {
                                return DoubleUtils.nextDown(dRoundToDoubleArbitrarily);
                            }
                        case 8:
                            MathPreconditions.checkRoundingUnnecessary(iCompareTo2 == 0);
                            return dRoundToDoubleArbitrarily;
                        default:
                            throw new AssertionError("impossible");
                    }
                    break;
            }
        }
        return dRoundToDoubleArbitrarily;
    }

    public abstract double roundToDoubleArbitrarily(X x);

    public abstract int sign(X x);

    public abstract X toX(double d, RoundingMode roundingMode);
}
