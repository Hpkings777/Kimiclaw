package com.apm.insight.log.a;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
final class b implements FilenameFilter {
    private /* synthetic */ Pattern a;
    private /* synthetic */ a b;

    public b(a aVar, Pattern pattern) {
        this.b = aVar;
        this.a = pattern;
    }

    @Override // java.io.FilenameFilter
    public final boolean accept(File file, String str) {
        return this.a.matcher(str).find();
    }
}
