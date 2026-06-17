package com.google.zxing.client.result;

import com.google.zxing.Result;

/* JADX INFO: loaded from: classes.dex */
public final class BookmarkDoCoMoResultParser extends AbstractDoCoMoResultParser {
    @Override // com.google.zxing.client.result.ResultParser
    public URIParsedResult parse(Result result) {
        String text = result.getText();
        if (!text.startsWith("MEBKM:")) {
            return null;
        }
        String strMatchSingleDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("TITLE:", text, true);
        String[] strArrMatchDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("URL:", text);
        if (strArrMatchDoCoMoPrefixedField == null) {
            return null;
        }
        String str = strArrMatchDoCoMoPrefixedField[0];
        if (URIResultParser.isBasicallyValidURI(str)) {
            return new URIParsedResult(str, strMatchSingleDoCoMoPrefixedField);
        }
        return null;
    }
}
