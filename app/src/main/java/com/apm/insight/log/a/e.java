package com.apm.insight.log.a;

import com.apm.insight.log.a.c;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
final class e implements Comparator<c.a> {
    @Override // java.util.Comparator
    public final /* bridge */ /* synthetic */ int compare(c.a aVar, c.a aVar2) {
        long j = aVar.b;
        long j2 = aVar2.b;
        if (j < j2) {
            return 1;
        }
        return j == j2 ? 0 : -1;
    }
}
