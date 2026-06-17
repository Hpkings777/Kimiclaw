package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import defpackage.AbstractC2039eV;
import defpackage.AbstractC4671xI0;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public final class Strings {
    private Strings() {
    }

    public static String commonPrefix(CharSequence charSequence, CharSequence charSequence2) {
        Preconditions.checkNotNull(charSequence);
        Preconditions.checkNotNull(charSequence2);
        int iMin = Math.min(charSequence.length(), charSequence2.length());
        int i = 0;
        while (i < iMin && charSequence.charAt(i) == charSequence2.charAt(i)) {
            i++;
        }
        int i2 = i - 1;
        if (validSurrogatePairAt(charSequence, i2) || validSurrogatePairAt(charSequence2, i2)) {
            i--;
        }
        return charSequence.subSequence(0, i).toString();
    }

    public static String commonSuffix(CharSequence charSequence, CharSequence charSequence2) {
        Preconditions.checkNotNull(charSequence);
        Preconditions.checkNotNull(charSequence2);
        int iMin = Math.min(charSequence.length(), charSequence2.length());
        int i = 0;
        while (i < iMin && charSequence.charAt((charSequence.length() - i) - 1) == charSequence2.charAt((charSequence2.length() - i) - 1)) {
            i++;
        }
        if (validSurrogatePairAt(charSequence, (charSequence.length() - i) - 1) || validSurrogatePairAt(charSequence2, (charSequence2.length() - i) - 1)) {
            i--;
        }
        return charSequence.subSequence(charSequence.length() - i, charSequence.length()).toString();
    }

    public static String emptyToNull(String str) {
        return Platform.emptyToNull(str);
    }

    public static boolean isNullOrEmpty(String str) {
        return Platform.stringIsNullOrEmpty(str);
    }

    public static String lenientFormat(String str, Object... objArr) {
        int iIndexOf;
        String strValueOf = String.valueOf(str);
        int i = 0;
        if (objArr == null) {
            objArr = new Object[]{"(Object[])null"};
        }
        StringBuilder sb = new StringBuilder((objArr.length * 16) + strValueOf.length());
        int i2 = 0;
        while (i < objArr.length && (iIndexOf = strValueOf.indexOf("%s", i2)) != -1) {
            sb.append((CharSequence) strValueOf, i2, iIndexOf);
            sb.append(lenientToString(objArr[i]));
            i2 = iIndexOf + 2;
            i++;
        }
        sb.append((CharSequence) strValueOf, i2, strValueOf.length());
        if (i < objArr.length) {
            String str2 = " [";
            while (i < objArr.length) {
                sb.append(str2);
                sb.append(lenientToString(objArr[i]));
                i++;
                str2 = ", ";
            }
            sb.append(']');
        }
        return sb.toString();
    }

    private static String lenientToString(Object obj) {
        if (obj == null) {
            return "null";
        }
        try {
            return obj.toString();
        } catch (Exception e) {
            String str = obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj));
            Logger.getLogger("com.google.common.base.Strings").log(Level.WARNING, "Exception during lenientFormat for " + str, (Throwable) e);
            StringBuilder sbQ = AbstractC4671xI0.q("<", str, " threw ");
            sbQ.append(e.getClass().getName());
            sbQ.append(">");
            return sbQ.toString();
        }
    }

    public static String nullToEmpty(String str) {
        return Platform.nullToEmpty(str);
    }

    public static String padEnd(String str, int i, char c) {
        Preconditions.checkNotNull(str);
        if (str.length() >= i) {
            return str;
        }
        StringBuilder sb = new StringBuilder(i);
        sb.append(str);
        for (int length = str.length(); length < i; length++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static String padStart(String str, int i, char c) {
        Preconditions.checkNotNull(str);
        if (str.length() >= i) {
            return str;
        }
        StringBuilder sb = new StringBuilder(i);
        for (int length = str.length(); length < i; length++) {
            sb.append(c);
        }
        sb.append(str);
        return sb.toString();
    }

    public static String repeat(String str, int i) {
        Preconditions.checkNotNull(str);
        if (i <= 1) {
            Preconditions.checkArgument(i >= 0, "invalid count: %s", i);
            return i == 0 ? "" : str;
        }
        int length = str.length();
        long j = ((long) length) * ((long) i);
        int i2 = (int) j;
        if (i2 != j) {
            throw new ArrayIndexOutOfBoundsException(AbstractC2039eV.j(j, "Required array size too large: "));
        }
        char[] cArr = new char[i2];
        str.getChars(0, length, cArr, 0);
        while (true) {
            int i3 = i2 - length;
            if (length >= i3) {
                System.arraycopy(cArr, 0, cArr, length, i3);
                return new String(cArr);
            }
            System.arraycopy(cArr, 0, cArr, length, length);
            length <<= 1;
        }
    }

    @VisibleForTesting
    public static boolean validSurrogatePairAt(CharSequence charSequence, int i) {
        return i >= 0 && i <= charSequence.length() + (-2) && Character.isHighSurrogate(charSequence.charAt(i)) && Character.isLowSurrogate(charSequence.charAt(i + 1));
    }
}
