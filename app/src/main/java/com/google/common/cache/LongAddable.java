package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
interface LongAddable {
    void add(long j);

    void increment();

    long sum();
}
