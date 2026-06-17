package com.google.common.graph;

import com.google.common.annotations.Beta;

/* JADX INFO: loaded from: classes.dex */
@Beta
public interface PredecessorsFunction<N> {
    Iterable<? extends N> predecessors(N n);
}
