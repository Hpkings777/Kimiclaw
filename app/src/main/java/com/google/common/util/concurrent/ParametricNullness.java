package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* JADX INFO: loaded from: classes.dex */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@GwtCompatible
@Retention(RetentionPolicy.CLASS)
@interface ParametricNullness {
}
