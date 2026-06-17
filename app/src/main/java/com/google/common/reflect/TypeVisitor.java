package com.google.common.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
abstract class TypeVisitor {
    private final Set<Type> visited = new HashSet();

    public final void visit(Type... typeArr) {
        for (Type type : typeArr) {
            if (type != null && this.visited.add(type)) {
                try {
                    if (type instanceof TypeVariable) {
                        visitTypeVariable((TypeVariable) type);
                    } else if (type instanceof WildcardType) {
                        visitWildcardType((WildcardType) type);
                    } else if (type instanceof ParameterizedType) {
                        visitParameterizedType((ParameterizedType) type);
                    } else if (type instanceof Class) {
                        visitClass((Class) type);
                    } else {
                        if (!(type instanceof GenericArrayType)) {
                            throw new AssertionError("Unknown type: " + type);
                        }
                        visitGenericArrayType((GenericArrayType) type);
                    }
                } catch (Throwable th) {
                    this.visited.remove(type);
                    throw th;
                }
            }
        }
    }

    public void visitClass(Class<?> cls) {
    }

    public void visitGenericArrayType(GenericArrayType genericArrayType) {
    }

    public void visitParameterizedType(ParameterizedType parameterizedType) {
    }

    public void visitTypeVariable(TypeVariable<?> typeVariable) {
    }

    public void visitWildcardType(WildcardType wildcardType) {
    }
}
