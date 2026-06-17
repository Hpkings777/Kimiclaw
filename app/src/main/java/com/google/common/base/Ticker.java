package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
public abstract class Ticker {
    private static final Ticker SYSTEM_TICKER = new Ticker() { // from class: com.google.common.base.Ticker.1
        @Override // com.google.common.base.Ticker
        public long read() {
            return System.nanoTime();
        }
    };

    public static Ticker systemTicker() {
        return SYSTEM_TICKER;
    }

    public abstract long read();
}
