package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import defpackage.AbstractC2039eV;

/* JADX INFO: loaded from: classes.dex */
final class AI01AndOtherAIs extends AI01decoder {
    private static final int HEADER_SIZE = 4;

    public AI01AndOtherAIs(BitArray bitArray) {
        super(bitArray);
    }

    @Override // com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder
    public String parseInformation() throws NotFoundException, FormatException {
        StringBuilder sbT = AbstractC2039eV.t("(01)");
        int length = sbT.length();
        sbT.append(getGeneralDecoder().extractNumericValueFromBitArray(4, 4));
        encodeCompressedGtinWithoutAI(sbT, 8, length);
        return getGeneralDecoder().decodeAllCodes(sbT, 48);
    }
}
