package com.google.zxing.oned.rss.expanded;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class ExpandedRow {
    private final List<ExpandedPair> pairs;
    private final int rowNumber;

    public ExpandedRow(List<ExpandedPair> list, int i) {
        this.pairs = new ArrayList(list);
        this.rowNumber = i;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ExpandedRow) {
            return this.pairs.equals(((ExpandedRow) obj).pairs);
        }
        return false;
    }

    public List<ExpandedPair> getPairs() {
        return this.pairs;
    }

    public int getRowNumber() {
        return this.rowNumber;
    }

    public int hashCode() {
        return this.pairs.hashCode();
    }

    public boolean isEquivalent(List<ExpandedPair> list) {
        return this.pairs.equals(list);
    }

    public String toString() {
        return "{ " + this.pairs + " }";
    }
}
