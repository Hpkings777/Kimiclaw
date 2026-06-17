package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.util.SortedSet;

/* JADX INFO: loaded from: classes.dex */
@GwtIncompatible
interface SortedMultisetBridge<E> extends Multiset<E> {
    @Override // com.google.common.collect.Multiset
    SortedSet<E> elementSet();
}
