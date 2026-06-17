package com.apm.insight.log.a;

import android.text.TextUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
final class c {
    private static final int a = 9;
    private static long b;
    private static long c;
    private static ArrayList<String> d;
    private static String e;

    public static class a {
        public File a;
        public long b;

        public a(File file, long j) {
            this.a = file;
            this.b = j;
        }
    }

    public static File[] a(String str, String str2, String str3, long j, long j2, int i) {
        b = j;
        c = j2;
        e = null;
        d = null;
        if (j > j2) {
            e = "time interval is invalid";
            return new File[0];
        }
        File file = new File(str);
        if (!file.exists() || !file.isDirectory()) {
            e = "log dir not exists";
            return new File[0];
        }
        if (!TextUtils.isEmpty(str2)) {
            str2 = str2.replace(':', '-');
        }
        StringBuilder sb = new StringBuilder("^\\d{4}_\\d{2}_\\d{2}_(\\d+)__");
        sb.append(TextUtils.isEmpty(str2) ? "\\S+" : Pattern.quote(str2));
        sb.append("__");
        sb.append(TextUtils.isEmpty(str3) ? "\\S+" : Pattern.quote(str3));
        sb.append("\\.vlog$");
        Pattern patternCompile = Pattern.compile(sb.toString());
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList arrayList2 = i > 0 ? new ArrayList() : null;
        File[] fileArrListFiles = file.listFiles(new d(arrayList, patternCompile, j2, j, arrayList2));
        if (fileArrListFiles == null || fileArrListFiles.length == 0) {
            e = "log file not found";
            d = arrayList;
        }
        if (i <= 0) {
            return fileArrListFiles == null ? new File[0] : fileArrListFiles;
        }
        Collections.sort(arrayList2, new e());
        int iMin = Math.min(i, arrayList2.size());
        File[] fileArr = new File[iMin];
        for (int i2 = 0; i2 < iMin; i2++) {
            fileArr[i2] = ((a) arrayList2.get(i2)).a;
        }
        return fileArr;
    }

    public static HashMap<String, String> a() {
        HashMap<String, String> map = new HashMap<>();
        map.put("start", Long.toString(b));
        map.put("end", Long.toString(c));
        map.put("reason", e);
        if (d != null) {
            StringBuilder sb = new StringBuilder();
            for (String strSubstring : d) {
                if (strSubstring.endsWith(".alog.hot")) {
                    strSubstring = strSubstring.substring(0, strSubstring.length() - a);
                }
                sb.append(strSubstring);
                sb.append(";");
            }
            map.put("file", sb.toString());
        }
        e = null;
        d = null;
        return map;
    }
}
