package com.google.common.reflect;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface TypeToInstanceMap<B> extends Map<TypeToken<? extends B>, B> {
    <T extends B> T getInstance(TypeToken<T> typeToken);

    <T extends B> T getInstance(Class<T> cls);

    <T extends B> T putInstance(TypeToken<T> typeToken, @ParametricNullness T t);

    <T extends B> T putInstance(Class<T> cls, @ParametricNullness T t);
}
