package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class EmailDoCoMoResultParser extends AbstractDoCoMoResultParser {
    private static final Pattern ATEXT_ALPHANUMERIC = Pattern.compile("[a-zA-Z0-9@.!#$%&'*+\\-/=?^_`{|}~]+");

    public static boolean isBasicallyValidEmailAddress(String str) {
        return str != null && ATEXT_ALPHANUMERIC.matcher(str).matches() && str.indexOf(64) >= 0;
    }

    @Override // com.google.zxing.client.result.ResultParser
    public EmailAddressParsedResult parse(Result result) {
        String[] strArrMatchDoCoMoPrefixedField;
        String massagedText = ResultParser.getMassagedText(result);
        if (!massagedText.startsWith("MATMSG:") || (strArrMatchDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("TO:", massagedText)) == null) {
            return null;
        }
        for (String str : strArrMatchDoCoMoPrefixedField) {
            if (!isBasicallyValidEmailAddress(str)) {
                return null;
            }
        }
        return new EmailAddressParsedResult(strArrMatchDoCoMoPrefixedField, null, null, AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("SUB:", massagedText, false), AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("BODY:", massagedText, false));
    }
}
