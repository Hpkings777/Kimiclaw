package com.google.zxing.client.result;

import com.google.zxing.Result;

/* JADX INFO: loaded from: classes.dex */
public final class AddressBookDoCoMoResultParser extends AbstractDoCoMoResultParser {
    private static String parseName(String str) {
        int iIndexOf = str.indexOf(44);
        if (iIndexOf < 0) {
            return str;
        }
        return str.substring(iIndexOf + 1) + ' ' + str.substring(0, iIndexOf);
    }

    @Override // com.google.zxing.client.result.ResultParser
    public AddressBookParsedResult parse(Result result) {
        String[] strArrMatchDoCoMoPrefixedField;
        String massagedText = ResultParser.getMassagedText(result);
        if (!massagedText.startsWith("MECARD:") || (strArrMatchDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("N:", massagedText)) == null) {
            return null;
        }
        String name = parseName(strArrMatchDoCoMoPrefixedField[0]);
        String strMatchSingleDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("SOUND:", massagedText, true);
        String[] strArrMatchDoCoMoPrefixedField2 = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("TEL:", massagedText);
        String[] strArrMatchDoCoMoPrefixedField3 = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("EMAIL:", massagedText);
        String strMatchSingleDoCoMoPrefixedField2 = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("NOTE:", massagedText, false);
        String[] strArrMatchDoCoMoPrefixedField4 = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("ADR:", massagedText);
        String strMatchSingleDoCoMoPrefixedField3 = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("BDAY:", massagedText, true);
        return new AddressBookParsedResult(ResultParser.maybeWrap(name), null, strMatchSingleDoCoMoPrefixedField, strArrMatchDoCoMoPrefixedField2, null, strArrMatchDoCoMoPrefixedField3, null, null, strMatchSingleDoCoMoPrefixedField2, strArrMatchDoCoMoPrefixedField4, null, AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("ORG:", massagedText, true), !ResultParser.isStringOfDigits(strMatchSingleDoCoMoPrefixedField3, 8) ? null : strMatchSingleDoCoMoPrefixedField3, null, AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("URL:", massagedText), null);
    }
}
