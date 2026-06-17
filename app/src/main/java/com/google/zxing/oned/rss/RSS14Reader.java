package com.google.zxing.oned.rss;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.oned.OneDReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class RSS14Reader extends AbstractRSSReader {
    private final List<Pair> possibleLeftPairs = new ArrayList();
    private final List<Pair> possibleRightPairs = new ArrayList();
    private static final int[] OUTSIDE_EVEN_TOTAL_SUBSET = {1, 10, 34, 70, 126};
    private static final int[] INSIDE_ODD_TOTAL_SUBSET = {4, 20, 48, 81};
    private static final int[] OUTSIDE_GSUM = {0, 161, 961, 2015, 2715};
    private static final int[] INSIDE_GSUM = {0, 336, 1036, 1516};
    private static final int[] OUTSIDE_ODD_WIDEST = {8, 6, 4, 3, 1};
    private static final int[] INSIDE_ODD_WIDEST = {2, 4, 6, 8};
    private static final int[][] FINDER_PATTERNS = {new int[]{3, 8, 2, 1}, new int[]{3, 5, 5, 1}, new int[]{3, 3, 7, 1}, new int[]{3, 1, 9, 1}, new int[]{2, 7, 4, 1}, new int[]{2, 5, 6, 1}, new int[]{2, 3, 8, 1}, new int[]{1, 5, 7, 1}, new int[]{1, 3, 9, 1}};

    private static void addOrTally(Collection<Pair> collection, Pair pair) {
        if (pair == null) {
            return;
        }
        for (Pair pair2 : collection) {
            if (pair2.getValue() == pair.getValue()) {
                pair2.incrementCount();
                return;
            }
        }
        collection.add(pair);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002a A[PHI: r6 r7
      0x002a: PHI (r6v5 boolean) = (r6v2 boolean), (r6v10 boolean) binds: [B:25:0x0044, B:12:0x0028] A[DONT_GENERATE, DONT_INLINE]
      0x002a: PHI (r7v5 boolean) = (r7v2 boolean), (r7v14 boolean) binds: [B:25:0x0044, B:12:0x0028] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002d A[PHI: r6 r7
      0x002d: PHI (r6v4 boolean) = (r6v2 boolean), (r6v10 boolean) binds: [B:25:0x0044, B:12:0x0028] A[DONT_GENERATE, DONT_INLINE]
      0x002d: PHI (r7v4 boolean) = (r7v2 boolean), (r7v14 boolean) binds: [B:25:0x0044, B:12:0x0028] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void adjustOddEvenCounts(boolean z, int i) throws NotFoundException {
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        int iSum = MathUtils.sum(getOddCounts());
        int iSum2 = MathUtils.sum(getEvenCounts());
        boolean z6 = true;
        if (z) {
            if (iSum > 12) {
                z3 = true;
                z2 = false;
            } else if (iSum < 4) {
                z2 = true;
                z3 = false;
            } else {
                z2 = false;
                z3 = false;
            }
            if (iSum2 <= 12) {
                if (iSum2 < 4) {
                    z4 = true;
                    z5 = false;
                } else {
                    z4 = false;
                    z5 = false;
                }
            }
            z5 = true;
            z4 = false;
        } else {
            if (iSum > 11) {
                z3 = true;
                z2 = false;
            } else if (iSum < 5) {
                z2 = true;
                z3 = false;
            } else {
                z2 = false;
                z3 = false;
            }
            if (iSum2 <= 10) {
                if (iSum2 < 4) {
                }
            }
            z5 = true;
            z4 = false;
        }
        int i2 = (iSum + iSum2) - i;
        boolean z7 = (iSum & 1) == z;
        boolean z8 = (iSum2 & 1) == 1;
        if (i2 != -1) {
            if (i2 != 0) {
                if (i2 != 1) {
                    throw NotFoundException.getNotFoundInstance();
                }
                if (z7) {
                    if (z8) {
                        throw NotFoundException.getNotFoundInstance();
                    }
                    z3 = true;
                } else {
                    if (!z8) {
                        throw NotFoundException.getNotFoundInstance();
                    }
                    z5 = true;
                }
            } else if (z7) {
                if (!z8) {
                    throw NotFoundException.getNotFoundInstance();
                }
                if (iSum < iSum2) {
                    z5 = true;
                } else {
                    z4 = true;
                    z3 = true;
                }
            } else if (z8) {
                throw NotFoundException.getNotFoundInstance();
            }
            z6 = z2;
        } else if (z7) {
            if (z8) {
                throw NotFoundException.getNotFoundInstance();
            }
        } else {
            if (!z8) {
                throw NotFoundException.getNotFoundInstance();
            }
            z4 = true;
            z6 = z2;
        }
        if (z6) {
            if (z3) {
                throw NotFoundException.getNotFoundInstance();
            }
            AbstractRSSReader.increment(getOddCounts(), getOddRoundingErrors());
        }
        if (z3) {
            AbstractRSSReader.decrement(getOddCounts(), getOddRoundingErrors());
        }
        if (z4) {
            if (z5) {
                throw NotFoundException.getNotFoundInstance();
            }
            AbstractRSSReader.increment(getEvenCounts(), getOddRoundingErrors());
        }
        if (z5) {
            AbstractRSSReader.decrement(getEvenCounts(), getEvenRoundingErrors());
        }
    }

    private static boolean checkChecksum(Pair pair, Pair pair2) {
        int checksumPortion = ((pair2.getChecksumPortion() * 16) + pair.getChecksumPortion()) % 79;
        int value = pair2.getFinderPattern().getValue() + (pair.getFinderPattern().getValue() * 9);
        if (value > 72) {
            value--;
        }
        if (value > 8) {
            value--;
        }
        return checksumPortion == value;
    }

    private static Result constructResult(Pair pair, Pair pair2) {
        String strValueOf = String.valueOf((((long) pair.getValue()) * 4537077) + ((long) pair2.getValue()));
        StringBuilder sb = new StringBuilder(14);
        for (int length = 13 - strValueOf.length(); length > 0; length--) {
            sb.append('0');
        }
        sb.append(strValueOf);
        int i = 0;
        for (int i2 = 0; i2 < 13; i2++) {
            int iCharAt = sb.charAt(i2) - '0';
            if ((i2 & 1) == 0) {
                iCharAt *= 3;
            }
            i += iCharAt;
        }
        int i3 = 10 - (i % 10);
        if (i3 == 10) {
            i3 = 0;
        }
        sb.append(i3);
        ResultPoint[] resultPoints = pair.getFinderPattern().getResultPoints();
        ResultPoint[] resultPoints2 = pair2.getFinderPattern().getResultPoints();
        Result result = new Result(sb.toString(), null, new ResultPoint[]{resultPoints[0], resultPoints[1], resultPoints2[0], resultPoints2[1]}, BarcodeFormat.RSS_14);
        result.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]e0");
        return result;
    }

    private DataCharacter decodeDataCharacter(BitArray bitArray, FinderPattern finderPattern, boolean z) throws NotFoundException {
        int[] dataCharacterCounters = getDataCharacterCounters();
        Arrays.fill(dataCharacterCounters, 0);
        if (z) {
            OneDReader.recordPatternInReverse(bitArray, finderPattern.getStartEnd()[0], dataCharacterCounters);
        } else {
            OneDReader.recordPattern(bitArray, finderPattern.getStartEnd()[1], dataCharacterCounters);
            int i = 0;
            for (int length = dataCharacterCounters.length - 1; i < length; length--) {
                int i2 = dataCharacterCounters[i];
                dataCharacterCounters[i] = dataCharacterCounters[length];
                dataCharacterCounters[length] = i2;
                i++;
            }
        }
        int i3 = z ? 16 : 15;
        float fSum = MathUtils.sum(dataCharacterCounters) / i3;
        int[] oddCounts = getOddCounts();
        int[] evenCounts = getEvenCounts();
        float[] oddRoundingErrors = getOddRoundingErrors();
        float[] evenRoundingErrors = getEvenRoundingErrors();
        for (int i4 = 0; i4 < dataCharacterCounters.length; i4++) {
            float f = dataCharacterCounters[i4] / fSum;
            int i5 = (int) (0.5f + f);
            if (i5 < 1) {
                i5 = 1;
            } else if (i5 > 8) {
                i5 = 8;
            }
            int i6 = i4 / 2;
            if ((i4 & 1) == 0) {
                oddCounts[i6] = i5;
                oddRoundingErrors[i6] = f - i5;
            } else {
                evenCounts[i6] = i5;
                evenRoundingErrors[i6] = f - i5;
            }
        }
        adjustOddEvenCounts(z, i3);
        int i7 = 0;
        int i8 = 0;
        for (int length2 = oddCounts.length - 1; length2 >= 0; length2--) {
            int i9 = oddCounts[length2];
            i7 = (i7 * 9) + i9;
            i8 += i9;
        }
        int i10 = 0;
        int i11 = 0;
        for (int length3 = evenCounts.length - 1; length3 >= 0; length3--) {
            int i12 = evenCounts[length3];
            i10 = (i10 * 9) + i12;
            i11 += i12;
        }
        int i13 = (i10 * 3) + i7;
        if (!z) {
            if ((i11 & 1) != 0 || i11 > 10 || i11 < 4) {
                throw NotFoundException.getNotFoundInstance();
            }
            int i14 = (10 - i11) / 2;
            int i15 = INSIDE_ODD_WIDEST[i14];
            return new DataCharacter((RSSUtils.getRSSvalue(evenCounts, 9 - i15, false) * INSIDE_ODD_TOTAL_SUBSET[i14]) + RSSUtils.getRSSvalue(oddCounts, i15, true) + INSIDE_GSUM[i14], i13);
        }
        if ((i8 & 1) != 0 || i8 > 12 || i8 < 4) {
            throw NotFoundException.getNotFoundInstance();
        }
        int i16 = (12 - i8) / 2;
        int i17 = OUTSIDE_ODD_WIDEST[i16];
        return new DataCharacter((RSSUtils.getRSSvalue(oddCounts, i17, false) * OUTSIDE_EVEN_TOTAL_SUBSET[i16]) + RSSUtils.getRSSvalue(evenCounts, 9 - i17, true) + OUTSIDE_GSUM[i16], i13);
    }

    private Pair decodePair(BitArray bitArray, boolean z, int i, Map<DecodeHintType, ?> map) {
        try {
            FinderPattern foundFinderPattern = parseFoundFinderPattern(bitArray, i, z, findFinderPattern(bitArray, z));
            ResultPointCallback resultPointCallback = map == null ? null : (ResultPointCallback) map.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
            if (resultPointCallback != null) {
                int[] startEnd = foundFinderPattern.getStartEnd();
                float size = ((startEnd[0] + startEnd[1]) - 1) / 2.0f;
                if (z) {
                    size = (bitArray.getSize() - 1) - size;
                }
                resultPointCallback.foundPossibleResultPoint(new ResultPoint(size, i));
            }
            DataCharacter dataCharacterDecodeDataCharacter = decodeDataCharacter(bitArray, foundFinderPattern, true);
            DataCharacter dataCharacterDecodeDataCharacter2 = decodeDataCharacter(bitArray, foundFinderPattern, false);
            return new Pair((dataCharacterDecodeDataCharacter.getValue() * 1597) + dataCharacterDecodeDataCharacter2.getValue(), (dataCharacterDecodeDataCharacter2.getChecksumPortion() * 4) + dataCharacterDecodeDataCharacter.getChecksumPortion(), foundFinderPattern);
        } catch (NotFoundException unused) {
            return null;
        }
    }

    private int[] findFinderPattern(BitArray bitArray, boolean z) throws NotFoundException {
        int[] decodeFinderCounters = getDecodeFinderCounters();
        decodeFinderCounters[0] = 0;
        decodeFinderCounters[1] = 0;
        decodeFinderCounters[2] = 0;
        decodeFinderCounters[3] = 0;
        int size = bitArray.getSize();
        int i = 0;
        boolean z2 = false;
        while (i < size) {
            z2 = !bitArray.get(i);
            if (z == z2) {
                break;
            }
            i++;
        }
        int i2 = 0;
        int i3 = i;
        while (i < size) {
            if (bitArray.get(i) != z2) {
                decodeFinderCounters[i2] = decodeFinderCounters[i2] + 1;
            } else {
                if (i2 != 3) {
                    i2++;
                } else {
                    if (AbstractRSSReader.isFinderPattern(decodeFinderCounters)) {
                        return new int[]{i3, i};
                    }
                    i3 += decodeFinderCounters[0] + decodeFinderCounters[1];
                    decodeFinderCounters[0] = decodeFinderCounters[2];
                    decodeFinderCounters[1] = decodeFinderCounters[3];
                    decodeFinderCounters[2] = 0;
                    decodeFinderCounters[3] = 0;
                    i2--;
                }
                decodeFinderCounters[i2] = 1;
                z2 = !z2;
            }
            i++;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private FinderPattern parseFoundFinderPattern(BitArray bitArray, int i, boolean z, int[] iArr) throws NotFoundException {
        int i2;
        boolean z2 = bitArray.get(iArr[0]);
        int i3 = iArr[0] - 1;
        while (i3 >= 0 && z2 != bitArray.get(i3)) {
            i3--;
        }
        int i4 = i3 + 1;
        int i5 = iArr[0] - i4;
        int[] decodeFinderCounters = getDecodeFinderCounters();
        System.arraycopy(decodeFinderCounters, 0, decodeFinderCounters, 1, decodeFinderCounters.length - 1);
        decodeFinderCounters[0] = i5;
        int finderValue = AbstractRSSReader.parseFinderValue(decodeFinderCounters, FINDER_PATTERNS);
        int size = iArr[1];
        if (z) {
            int size2 = (bitArray.getSize() - 1) - i4;
            size = (bitArray.getSize() - 1) - size;
            i2 = size2;
        } else {
            i2 = i4;
        }
        return new FinderPattern(finderValue, new int[]{i4, iArr[1]}, i2, size, i);
    }

    @Override // com.google.zxing.oned.OneDReader
    public Result decodeRow(int i, BitArray bitArray, Map<DecodeHintType, ?> map) throws NotFoundException {
        addOrTally(this.possibleLeftPairs, decodePair(bitArray, false, i, map));
        bitArray.reverse();
        addOrTally(this.possibleRightPairs, decodePair(bitArray, true, i, map));
        bitArray.reverse();
        for (Pair pair : this.possibleLeftPairs) {
            if (pair.getCount() > 1) {
                for (Pair pair2 : this.possibleRightPairs) {
                    if (pair2.getCount() > 1 && checkChecksum(pair, pair2)) {
                        return constructResult(pair, pair2);
                    }
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    @Override // com.google.zxing.oned.OneDReader, com.google.zxing.Reader
    public void reset() {
        this.possibleLeftPairs.clear();
        this.possibleRightPairs.clear();
    }
}
