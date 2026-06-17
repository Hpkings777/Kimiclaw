package com.google.zxing.client.result;

import com.apm.insight.log.LogLevel;
import com.google.common.base.Ascii;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.aztec.encoder.Encoder;
import com.google.zxing.pdf417.PDF417Common;
import com.sun.mail.iap.Response;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class ExpandedProductResultParser extends ResultParser {
    private static String findAIvalue(int i, String str) {
        char cCharAt;
        if (str.charAt(i) != '(') {
            return null;
        }
        String strSubstring = str.substring(i + 1);
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < strSubstring.length() && (cCharAt = strSubstring.charAt(i2)) != ')'; i2++) {
            if (cCharAt < '0' || cCharAt > '9') {
                return null;
            }
            sb.append(cCharAt);
        }
        return sb.toString();
    }

    private static String findValue(int i, String str) {
        StringBuilder sb = new StringBuilder();
        String strSubstring = str.substring(i);
        for (int i2 = 0; i2 < strSubstring.length(); i2++) {
            char cCharAt = strSubstring.charAt(i2);
            if (cCharAt != '(') {
                sb.append(cCharAt);
            } else {
                if (findAIvalue(i2, strSubstring) != null) {
                    break;
                }
                sb.append('(');
            }
        }
        return sb.toString();
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.google.zxing.client.result.ResultParser
    public ExpandedProductParsedResult parse(Result result) {
        ExpandedProductParsedResult expandedProductParsedResult;
        ExpandedProductParsedResult expandedProductParsedResult2 = null;
        if (result.getBarcodeFormat() != BarcodeFormat.RSS_EXPANDED) {
            return null;
        }
        String massagedText = ResultParser.getMassagedText(result);
        HashMap map = new HashMap();
        int length = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        String str9 = null;
        String strSubstring = null;
        String strSubstring2 = null;
        String strSubstring3 = null;
        String strSubstring4 = null;
        while (length < massagedText.length()) {
            String strFindAIvalue = findAIvalue(length, massagedText);
            if (strFindAIvalue == null) {
                return expandedProductParsedResult2;
            }
            int length2 = strFindAIvalue.length() + 2 + length;
            String strFindValue = findValue(length2, massagedText);
            length = strFindValue.length() + length2;
            byte b = -1;
            switch (strFindAIvalue.hashCode()) {
                case 1536:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("00")) {
                        b = 0;
                    }
                    break;
                case 1537:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("01")) {
                        b = 1;
                    }
                    break;
                case 1567:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("10")) {
                        b = 2;
                    }
                    break;
                case 1568:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("11")) {
                        b = 3;
                    }
                    break;
                case 1570:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("13")) {
                        b = 4;
                    }
                    break;
                case 1572:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("15")) {
                        b = 5;
                    }
                    break;
                case 1574:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("17")) {
                        b = 6;
                    }
                    break;
                case 1567966:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3100")) {
                        b = 7;
                    }
                    break;
                case 1567967:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3101")) {
                        b = 8;
                    }
                    break;
                case 1567968:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3102")) {
                        b = 9;
                    }
                    break;
                case 1567969:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3103")) {
                        b = 10;
                    }
                    break;
                case 1567970:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3104")) {
                        b = Ascii.VT;
                    }
                    break;
                case 1567971:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3105")) {
                        b = Ascii.FF;
                    }
                    break;
                case 1567972:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3106")) {
                        b = Ascii.CR;
                    }
                    break;
                case 1567973:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3107")) {
                        b = Ascii.SO;
                    }
                    break;
                case 1567974:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3108")) {
                        b = Ascii.SI;
                    }
                    break;
                case 1567975:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3109")) {
                        b = Ascii.DLE;
                    }
                    break;
                case 1568927:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3200")) {
                        b = 17;
                    }
                    break;
                case 1568928:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3201")) {
                        b = Ascii.DC2;
                    }
                    break;
                case 1568929:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3202")) {
                        b = 19;
                    }
                    break;
                case 1568930:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3203")) {
                        b = Ascii.DC4;
                    }
                    break;
                case 1568931:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3204")) {
                        b = Ascii.NAK;
                    }
                    break;
                case 1568932:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3205")) {
                        b = Ascii.SYN;
                    }
                    break;
                case 1568933:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3206")) {
                        b = Ascii.ETB;
                    }
                    break;
                case 1568934:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3207")) {
                        b = Ascii.CAN;
                    }
                    break;
                case 1568935:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3208")) {
                        b = Ascii.EM;
                    }
                    break;
                case 1568936:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3209")) {
                        b = Ascii.SUB;
                    }
                    break;
                case 1575716:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3920")) {
                        b = Ascii.ESC;
                    }
                    break;
                case 1575717:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3921")) {
                        b = Ascii.FS;
                    }
                    break;
                case 1575718:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3922")) {
                        b = Ascii.GS;
                    }
                    break;
                case 1575719:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3923")) {
                        b = Ascii.RS;
                    }
                    break;
                case 1575747:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3930")) {
                        b = Ascii.US;
                    }
                    break;
                case 1575748:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3931")) {
                        b = 32;
                    }
                    break;
                case 1575749:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3932")) {
                        b = 33;
                    }
                    break;
                case 1575750:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    if (strFindAIvalue.equals("3933")) {
                        b = 34;
                    }
                    break;
                default:
                    expandedProductParsedResult = expandedProductParsedResult2;
                    break;
            }
            switch (b) {
                case 0:
                    str2 = strFindValue;
                    continue;
                    expandedProductParsedResult2 = expandedProductParsedResult;
                    break;
                case 1:
                    str = strFindValue;
                    continue;
                    expandedProductParsedResult2 = expandedProductParsedResult;
                    break;
                case 2:
                    str3 = strFindValue;
                    continue;
                    expandedProductParsedResult2 = expandedProductParsedResult;
                    break;
                case 3:
                    str4 = strFindValue;
                    continue;
                    expandedProductParsedResult2 = expandedProductParsedResult;
                    break;
                case 4:
                    str5 = strFindValue;
                    continue;
                    expandedProductParsedResult2 = expandedProductParsedResult;
                    break;
                case 5:
                    str6 = strFindValue;
                    continue;
                    expandedProductParsedResult2 = expandedProductParsedResult;
                    break;
                case 6:
                    str7 = strFindValue;
                    continue;
                    expandedProductParsedResult2 = expandedProductParsedResult;
                    break;
                case LogLevel.NONE /* 7 */:
                case 8:
                case 9:
                case 10:
                case 11:
                case Response.BAD /* 12 */:
                case 13:
                case 14:
                case 15:
                case 16:
                    strSubstring = strFindAIvalue.substring(3);
                    str9 = ExpandedProductParsedResult.KILOGRAM;
                    break;
                case PDF417Common.MODULES_IN_CODEWORD /* 17 */:
                case PDF417Common.MODULES_IN_STOP_PATTERN /* 18 */:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                    strSubstring = strFindAIvalue.substring(3);
                    str9 = ExpandedProductParsedResult.POUND;
                    break;
                case 27:
                case Response.TYPE_MASK /* 28 */:
                case 29:
                case 30:
                    strSubstring3 = strFindAIvalue.substring(3);
                    strSubstring2 = strFindValue;
                    continue;
                    expandedProductParsedResult2 = expandedProductParsedResult;
                    break;
                case 31:
                case 32:
                case Encoder.DEFAULT_EC_PERCENT /* 33 */:
                case 34:
                    if (strFindValue.length() < 4) {
                        return expandedProductParsedResult;
                    }
                    strSubstring2 = strFindValue.substring(3);
                    strSubstring4 = strFindValue.substring(0, 3);
                    strSubstring3 = strFindAIvalue.substring(3);
                    continue;
                    expandedProductParsedResult2 = expandedProductParsedResult;
                    break;
                default:
                    map.put(strFindAIvalue, strFindValue);
                    continue;
                    expandedProductParsedResult2 = expandedProductParsedResult;
                    break;
            }
            str8 = strFindValue;
            expandedProductParsedResult2 = expandedProductParsedResult;
        }
        return new ExpandedProductParsedResult(massagedText, str, str2, str3, str4, str5, str6, str7, str8, str9, strSubstring, strSubstring2, strSubstring3, strSubstring4, map);
    }
}
