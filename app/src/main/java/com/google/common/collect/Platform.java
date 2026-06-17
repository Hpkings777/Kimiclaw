package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.J2ktIncompatible;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class Platform {
    private Platform() {
    }

    public static <T> T[] copy(Object[] objArr, int i, int i2, T[] tArr) {
        return (T[]) Arrays.copyOfRange(objArr, i, i2, tArr.getClass());
    }

    public static <E extends Enum<E>> Class<E> getDeclaringClassOrObjectForJ2cl(E e) {
        return e.getDeclaringClass();
    }

    public static <T> T[] newArray(T[] tArr, int i) {
        if (tArr.length != 0) {
            tArr = (T[]) Arrays.copyOf(tArr, 0);
        }
        return (T[]) Arrays.copyOf(tArr, i);
    }

    public static <K, V> Map<K, V> newHashMapWithExpectedSize(int i) {
        return CompactHashMap.createWithExpectedSize(i);
    }

    public static <E> Set<E> newHashSetWithExpectedSize(int i) {
        return CompactHashSet.createWithExpectedSize(i);
    }

    public static <K, V> Map<K, V> newLinkedHashMapWithExpectedSize(int i) {
        return CompactLinkedHashMap.createWithExpectedSize(i);
    }

    public static <E> Set<E> newLinkedHashSetWithExpectedSize(int i) {
        return CompactLinkedHashSet.createWithExpectedSize(i);
    }

    public static <E> Set<E> preservesInsertionOrderOnAddsSet() {
        return CompactHashSet.create();
    }

    public static <K, V> Map<K, V> preservesInsertionOrderOnPutsMap() {
        return CompactHashMap.create();
    }

    public static <K, V> Map<K, V> preservesInsertionOrderOnPutsMapWithExpectedSize(int i) {
        return Maps.newLinkedHashMapWithExpectedSize(i);
    }

    public static int reduceExponentIfGwt(int i) {
        return i;
    }

    public static int reduceIterationsIfGwt(int i) {
        return i;
    }

    @J2ktIncompatible
    public static MapMaker tryWeakKeys(MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }
}
