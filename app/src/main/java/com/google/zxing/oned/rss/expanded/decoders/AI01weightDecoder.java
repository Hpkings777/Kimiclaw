package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.common.BitArray;

/* JADX INFO: loaded from: classes.dex */
abstract class AI01weightDecoder extends AI01decoder {
    public AI01weightDecoder(BitArray bitArray) {
        super(bitArray);
    }

    public abstract void addWeightCode(StringBuilder sb, int i);

    public abstract int checkWeight(int i);

    public final void encodeCompressedWeight(StringBuilder sb, int i, int i2) {
        int iExtractNumericValueFromBitArray = getGeneralDecoder().extractNumericValueFromBitArray(i, i2);
        addWeightCode(sb, iExtractNumericValueFromBitArray);
        int iCheckWeight = checkWeight(iExtractNumericValueFromBitArray);
        int i3 = 100000;
        for (int i4 = 0; i4 < 5; i4++) {
            if (iCheckWeight / i3 == 0) {
                sb.append('0');
            }
            i3 /= 10;
        }
        sb.append(iCheckWeight);
    }
}
