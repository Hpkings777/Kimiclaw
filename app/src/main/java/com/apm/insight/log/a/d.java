package com.apm.insight.log.a;

import com.apm.insight.log.a.c;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
final class d implements FilenameFilter {
    private /* synthetic */ ArrayList a;
    private /* synthetic */ Pattern b;
    private /* synthetic */ long c;
    private /* synthetic */ long d;
    private /* synthetic */ ArrayList e;

    public d(ArrayList arrayList, Pattern pattern, long j, long j2, ArrayList arrayList2) {
        this.a = arrayList;
        this.b = pattern;
        this.c = j;
        this.d = j2;
        this.e = arrayList2;
    }

    @Override // java.io.FilenameFilter
    public final boolean accept(File file, String str) {
        String strGroup;
        this.a.add(str);
        Matcher matcher = this.b.matcher(str);
        if (!matcher.find() || matcher.groupCount() != 1 || (strGroup = matcher.group(1)) == null) {
            return false;
        }
        long j = Long.parseLong(strGroup);
        if (j > 0 && j <= this.c) {
            File file2 = new File(file, str);
            long jLastModified = file2.lastModified();
            if (jLastModified > 0 && jLastModified >= this.d) {
                ArrayList arrayList = this.e;
                if (arrayList != null) {
                    arrayList.add(new c.a(file2, j));
                }
                return true;
            }
        }
        return false;
    }
}
