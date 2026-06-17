package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;

/* JADX INFO: loaded from: classes.dex */
public final class AddressBookAUResultParser extends ResultParser {
    private static String[] matchMultipleValuePrefix(String str, String str2) {
        ArrayList arrayList = null;
        for (int i = 1; i <= 3; i++) {
            String strMatchSinglePrefixedField = ResultParser.matchSinglePrefixedField(str + i + ':', str2, '\r', true);
            if (strMatchSinglePrefixedField == null) {
                break;
            }
            if (arrayList == null) {
                arrayList = new ArrayList(3);
            }
            arrayList.add(strMatchSinglePrefixedField);
        }
        if (arrayList == null) {
            return null;
        }
        return (String[]) arrayList.toArray(ResultParser.EMPTY_STR_ARRAY);
    }

    @Override // com.google.zxing.client.result.ResultParser
    public AddressBookParsedResult parse(Result result) {
        String massagedText = ResultParser.getMassagedText(result);
        if (!massagedText.contains("MEMORY") || !massagedText.contains(IOUtils.LINE_SEPARATOR_WINDOWS)) {
            return null;
        }
        String strMatchSinglePrefixedField = ResultParser.matchSinglePrefixedField("NAME1:", massagedText, '\r', true);
        String strMatchSinglePrefixedField2 = ResultParser.matchSinglePrefixedField("NAME2:", massagedText, '\r', true);
        String[] strArrMatchMultipleValuePrefix = matchMultipleValuePrefix("TEL", massagedText);
        String[] strArrMatchMultipleValuePrefix2 = matchMultipleValuePrefix("MAIL", massagedText);
        String strMatchSinglePrefixedField3 = ResultParser.matchSinglePrefixedField("MEMORY:", massagedText, '\r', false);
        String strMatchSinglePrefixedField4 = ResultParser.matchSinglePrefixedField("ADD:", massagedText, '\r', true);
        return new AddressBookParsedResult(ResultParser.maybeWrap(strMatchSinglePrefixedField), null, strMatchSinglePrefixedField2, strArrMatchMultipleValuePrefix, null, strArrMatchMultipleValuePrefix2, null, null, strMatchSinglePrefixedField3, strMatchSinglePrefixedField4 != null ? new String[]{strMatchSinglePrefixedField4} : null, null, null, null, null, null, null);
    }
}
