package com.google.zxing.common;

/* JADX INFO: loaded from: classes.dex */
public interface ECIInput {
    char charAt(int i);

    int getECIValue(int i);

    boolean haveNCharacters(int i, int i2);

    boolean isECI(int i);

    int length();

    CharSequence subSequence(int i, int i2);
}
