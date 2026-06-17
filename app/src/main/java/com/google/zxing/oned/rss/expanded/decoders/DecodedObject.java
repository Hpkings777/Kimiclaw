package com.google.zxing.oned.rss.expanded.decoders;

/* JADX INFO: loaded from: classes.dex */
abstract class DecodedObject {
    private final int newPosition;

    public DecodedObject(int i) {
        this.newPosition = i;
    }

    public final int getNewPosition() {
        return this.newPosition;
    }
}
