package com.google.common.reflect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import defpackage.AbstractC4671xI0;
import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.io.FilenameUtils;

/* JADX INFO: loaded from: classes.dex */
final class Types {
    private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");

    public enum ClassOwnership {
        OWNED_BY_ENCLOSING_CLASS { // from class: com.google.common.reflect.Types.ClassOwnership.1
            @Override // com.google.common.reflect.Types.ClassOwnership
            public Class<?> getOwnerType(Class<?> cls) {
                return cls.getEnclosingClass();
            }
        },
        LOCAL_CLASS_HAS_NO_OWNER { // from class: com.google.common.reflect.Types.ClassOwnership.2
            @Override // com.google.common.reflect.Types.ClassOwnership
            public Class<?> getOwnerType(Class<?> cls) {
                if (cls.isLocalClass()) {
                    return null;
                }
                return cls.getEnclosingClass();
            }
        };

        static final ClassOwnership JVM_BEHAVIOR = detectJvmBehavior();

        private static ClassOwnership detectJvmBehavior() {
            new C1LocalClass<String>() { // from class: com.google.common.reflect.Types.ClassOwnership.3
            };
            ParameterizedType parameterizedType = (ParameterizedType) AnonymousClass3.class.getGenericSuperclass();
            Objects.requireNonNull(parameterizedType);
            ParameterizedType parameterizedType2 = parameterizedType;
            for (ClassOwnership classOwnership : values()) {
                if (classOwnership.getOwnerType(C1LocalClass.class) == parameterizedType2.getOwnerType()) {
                    return classOwnership;
                }
            }
            throw new AssertionError();
        }

        public abstract Class<?> getOwnerType(Class<?> cls);
    }

    public static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type componentType;

        public GenericArrayTypeImpl(Type type) {
            this.componentType = JavaVersion.CURRENT.usedInGenericType(type);
        }

        public boolean equals(Object obj) {
            if (obj instanceof GenericArrayType) {
                return Objects.equals(getGenericComponentType(), ((GenericArrayType) obj).getGenericComponentType());
            }
            return false;
        }

        @Override // java.lang.reflect.GenericArrayType
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return AbstractC4671xI0.p(new StringBuilder(), Types.toString(this.componentType), "[]");
        }
    }

    /* JADX WARN: Enum visitor error
    jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v0 com.google.common.reflect.Types$JavaVersion, still in use, count: 1, list:
      (r0v0 com.google.common.reflect.Types$JavaVersion) from 0x0063: SPUT (r0v0 com.google.common.reflect.Types$JavaVersion) com.google.common.reflect.Types.JavaVersion.CURRENT com.google.common.reflect.Types$JavaVersion
    	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
    	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
    	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:99)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:98)
    	at jadx.core.utils.InsnRemover.removeAllAndUnbind(InsnRemover.java:252)
    	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:180)
    	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
     */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    public static abstract class JavaVersion {
        JAVA6 { // from class: com.google.common.reflect.Types.JavaVersion.1
            @Override // com.google.common.reflect.Types.JavaVersion
            public Type usedInGenericType(Type type) {
                Preconditions.checkNotNull(type);
                if (!(type instanceof Class)) {
                    return type;
                }
                Class cls = (Class) type;
                return cls.isArray() ? new GenericArrayTypeImpl(cls.getComponentType()) : type;
            }

            @Override // com.google.common.reflect.Types.JavaVersion
            public GenericArrayType newArrayType(Type type) {
                return new GenericArrayTypeImpl(type);
            }
        },
        JAVA7 { // from class: com.google.common.reflect.Types.JavaVersion.2
            @Override // com.google.common.reflect.Types.JavaVersion
            public Type newArrayType(Type type) {
                return type instanceof Class ? Types.getArrayClass((Class) type) : new GenericArrayTypeImpl(type);
            }

            @Override // com.google.common.reflect.Types.JavaVersion
            public Type usedInGenericType(Type type) {
                return (Type) Preconditions.checkNotNull(type);
            }
        },
        JAVA8 { // from class: com.google.common.reflect.Types.JavaVersion.3
            @Override // com.google.common.reflect.Types.JavaVersion
            public Type newArrayType(Type type) {
                return JavaVersion.JAVA7.newArrayType(type);
            }

            @Override // com.google.common.reflect.Types.JavaVersion
            @IgnoreJRERequirement
            public String typeName(Type type) {
                return type.getTypeName();
            }

            @Override // com.google.common.reflect.Types.JavaVersion
            public Type usedInGenericType(Type type) {
                return JavaVersion.JAVA7.usedInGenericType(type);
            }
        },
        JAVA9 { // from class: com.google.common.reflect.Types.JavaVersion.4
            @Override // com.google.common.reflect.Types.JavaVersion
            public boolean jdkTypeDuplicatesOwnerName() {
                return false;
            }

            @Override // com.google.common.reflect.Types.JavaVersion
            public Type newArrayType(Type type) {
                return JavaVersion.JAVA8.newArrayType(type);
            }

            @Override // com.google.common.reflect.Types.JavaVersion
            @IgnoreJRERequirement
            public String typeName(Type type) {
                return type.getTypeName();
            }

            @Override // com.google.common.reflect.Types.JavaVersion
            public Type usedInGenericType(Type type) {
                return JavaVersion.JAVA8.usedInGenericType(type);
            }
        };

        static final JavaVersion CURRENT;

        static {
            if (AnnotatedElement.class.isAssignableFrom(TypeVariable.class)) {
                if (new TypeCapture<Map.Entry<String, int[][]>>() { // from class: com.google.common.reflect.Types.JavaVersion.5
                }.capture().toString().contains("java.util.Map.java.util.Map")) {
                    CURRENT = javaVersion;
                    return;
                } else {
                    CURRENT = javaVersion;
                    return;
                }
            }
            if (new TypeCapture<int[]>() { // from class: com.google.common.reflect.Types.JavaVersion.6
            }.capture() instanceof Class) {
                CURRENT = javaVersion;
            } else {
                CURRENT = javaVersion;
            }
        }

        private JavaVersion() {
        }

        public static JavaVersion valueOf(String str) {
            return (JavaVersion) Enum.valueOf(JavaVersion.class, str);
        }

        public static JavaVersion[] values() {
            return (JavaVersion[]) $VALUES.clone();
        }

        public boolean jdkTypeDuplicatesOwnerName() {
            return true;
        }

        public abstract Type newArrayType(Type type);

        public String typeName(Type type) {
            return Types.toString(type);
        }

        public final ImmutableList<Type> usedInGenericType(Type[] typeArr) {
            ImmutableList.Builder builder = ImmutableList.builder();
            for (Type type : typeArr) {
                builder.add(usedInGenericType(type));
            }
            return builder.build();
        }

        public abstract Type usedInGenericType(Type type);
    }

    public static final class NativeTypeVariableEquals<X> {
        static final boolean NATIVE_TYPE_VARIABLE_ONLY = !NativeTypeVariableEquals.class.getTypeParameters()[0].equals(Types.newArtificialTypeVariable(NativeTypeVariableEquals.class, "X", new Type[0]));

        private NativeTypeVariableEquals() {
        }
    }

    public static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private static final long serialVersionUID = 0;
        private final ImmutableList<Type> argumentsList;
        private final Type ownerType;
        private final Class<?> rawType;

        public ParameterizedTypeImpl(Type type, Class<?> cls, Type[] typeArr) {
            Preconditions.checkNotNull(cls);
            Preconditions.checkArgument(typeArr.length == cls.getTypeParameters().length);
            Types.disallowPrimitiveType(typeArr, "type parameter");
            this.ownerType = type;
            this.rawType = cls;
            this.argumentsList = JavaVersion.CURRENT.usedInGenericType(typeArr);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType parameterizedType = (ParameterizedType) obj;
            return getRawType().equals(parameterizedType.getRawType()) && Objects.equals(getOwnerType(), parameterizedType.getOwnerType()) && Arrays.equals(getActualTypeArguments(), parameterizedType.getActualTypeArguments());
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            return Types.toArray(this.argumentsList);
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getOwnerType() {
            return this.ownerType;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.rawType;
        }

        public int hashCode() {
            Type type = this.ownerType;
            return ((type == null ? 0 : type.hashCode()) ^ this.argumentsList.hashCode()) ^ this.rawType.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.ownerType != null) {
                JavaVersion javaVersion = JavaVersion.CURRENT;
                if (javaVersion.jdkTypeDuplicatesOwnerName()) {
                    sb.append(javaVersion.typeName(this.ownerType));
                    sb.append(FilenameUtils.EXTENSION_SEPARATOR);
                }
            }
            sb.append(this.rawType.getName());
            sb.append('<');
            Joiner joiner = Types.COMMA_JOINER;
            ImmutableList<Type> immutableList = this.argumentsList;
            final JavaVersion javaVersion2 = JavaVersion.CURRENT;
            Objects.requireNonNull(javaVersion2);
            sb.append(joiner.join(Iterables.transform(immutableList, new Function() { // from class: com.google.common.reflect.b
                @Override // com.google.common.base.Function
                public final Object apply(Object obj) {
                    return javaVersion2.typeName((Type) obj);
                }
            })));
            sb.append('>');
            return sb.toString();
        }
    }

    public static final class TypeVariableImpl<D extends GenericDeclaration> {
        private final ImmutableList<Type> bounds;
        private final D genericDeclaration;
        private final String name;

        public TypeVariableImpl(D d, String str, Type[] typeArr) {
            Types.disallowPrimitiveType(typeArr, "bound for type variable");
            this.genericDeclaration = (D) Preconditions.checkNotNull(d);
            this.name = (String) Preconditions.checkNotNull(str);
            this.bounds = ImmutableList.copyOf(typeArr);
        }

        public boolean equals(Object obj) {
            if (!NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
                if (obj instanceof TypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) obj;
                    if (this.name.equals(typeVariable.getName()) && this.genericDeclaration.equals(typeVariable.getGenericDeclaration())) {
                        return true;
                    }
                }
                return false;
            }
            if (obj != null && Proxy.isProxyClass(obj.getClass()) && (Proxy.getInvocationHandler(obj) instanceof TypeVariableInvocationHandler)) {
                TypeVariableImpl typeVariableImpl = ((TypeVariableInvocationHandler) Proxy.getInvocationHandler(obj)).typeVariableImpl;
                if (this.name.equals(typeVariableImpl.getName()) && this.genericDeclaration.equals(typeVariableImpl.getGenericDeclaration()) && this.bounds.equals(typeVariableImpl.bounds)) {
                    return true;
                }
            }
            return false;
        }

        public Type[] getBounds() {
            return Types.toArray(this.bounds);
        }

        public D getGenericDeclaration() {
            return this.genericDeclaration;
        }

        public String getName() {
            return this.name;
        }

        public String getTypeName() {
            return this.name;
        }

        public int hashCode() {
            return this.genericDeclaration.hashCode() ^ this.name.hashCode();
        }

        public String toString() {
            return this.name;
        }
    }

    public static final class TypeVariableInvocationHandler implements InvocationHandler {
        private static final ImmutableMap<String, Method> typeVariableMethods;
        private final TypeVariableImpl<?> typeVariableImpl;

        static {
            ImmutableMap.Builder builder = ImmutableMap.builder();
            for (Method method : TypeVariableImpl.class.getMethods()) {
                if (method.getDeclaringClass().equals(TypeVariableImpl.class)) {
                    try {
                        method.setAccessible(true);
                    } catch (AccessControlException unused) {
                    }
                    builder.put(method.getName(), method);
                }
            }
            typeVariableMethods = builder.buildKeepingLast();
        }

        public TypeVariableInvocationHandler(TypeVariableImpl<?> typeVariableImpl) {
            this.typeVariableImpl = typeVariableImpl;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            String name = method.getName();
            Method method2 = typeVariableMethods.get(name);
            if (method2 == null) {
                throw new UnsupportedOperationException(name);
            }
            try {
                return method2.invoke(this.typeVariableImpl, objArr);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }

    public static final class WildcardTypeImpl implements WildcardType, Serializable {
        private static final long serialVersionUID = 0;
        private final ImmutableList<Type> lowerBounds;
        private final ImmutableList<Type> upperBounds;

        public WildcardTypeImpl(Type[] typeArr, Type[] typeArr2) {
            Types.disallowPrimitiveType(typeArr, "lower bound for wildcard");
            Types.disallowPrimitiveType(typeArr2, "upper bound for wildcard");
            JavaVersion javaVersion = JavaVersion.CURRENT;
            this.lowerBounds = javaVersion.usedInGenericType(typeArr);
            this.upperBounds = javaVersion.usedInGenericType(typeArr2);
        }

        public boolean equals(Object obj) {
            if (obj instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) obj;
                if (this.lowerBounds.equals(Arrays.asList(wildcardType.getLowerBounds())) && this.upperBounds.equals(Arrays.asList(wildcardType.getUpperBounds()))) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getLowerBounds() {
            return Types.toArray(this.lowerBounds);
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getUpperBounds() {
            return Types.toArray(this.upperBounds);
        }

        public int hashCode() {
            return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("?");
            UnmodifiableIterator<Type> it = this.lowerBounds.iterator();
            while (it.hasNext()) {
                Type next = it.next();
                sb.append(" super ");
                sb.append(JavaVersion.CURRENT.typeName(next));
            }
            for (Type type : Types.filterUpperBounds(this.upperBounds)) {
                sb.append(" extends ");
                sb.append(JavaVersion.CURRENT.typeName(type));
            }
            return sb.toString();
        }
    }

    private Types() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void disallowPrimitiveType(Type[] typeArr, String str) {
        for (Type type : typeArr) {
            if (type instanceof Class) {
                Preconditions.checkArgument(!r2.isPrimitive(), "Primitive type '%s' used as %s", (Class) type, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Iterable<Type> filterUpperBounds(Iterable<Type> iterable) {
        return Iterables.filter(iterable, Predicates.not(Predicates.equalTo(Object.class)));
    }

    public static Class<?> getArrayClass(Class<?> cls) {
        return Array.newInstance(cls, 0).getClass();
    }

    public static Type getComponentType(Type type) {
        Preconditions.checkNotNull(type);
        final AtomicReference atomicReference = new AtomicReference();
        new TypeVisitor() { // from class: com.google.common.reflect.Types.1
            @Override // com.google.common.reflect.TypeVisitor
            public void visitClass(Class<?> cls) {
                atomicReference.set(cls.getComponentType());
            }

            @Override // com.google.common.reflect.TypeVisitor
            public void visitGenericArrayType(GenericArrayType genericArrayType) {
                atomicReference.set(genericArrayType.getGenericComponentType());
            }

            @Override // com.google.common.reflect.TypeVisitor
            public void visitTypeVariable(TypeVariable<?> typeVariable) {
                atomicReference.set(Types.subtypeOfComponentType(typeVariable.getBounds()));
            }

            @Override // com.google.common.reflect.TypeVisitor
            public void visitWildcardType(WildcardType wildcardType) {
                atomicReference.set(Types.subtypeOfComponentType(wildcardType.getUpperBounds()));
            }
        }.visit(type);
        return (Type) atomicReference.get();
    }

    public static Type newArrayType(Type type) {
        if (!(type instanceof WildcardType)) {
            return JavaVersion.CURRENT.newArrayType(type);
        }
        WildcardType wildcardType = (WildcardType) type;
        Type[] lowerBounds = wildcardType.getLowerBounds();
        Preconditions.checkArgument(lowerBounds.length <= 1, "Wildcard cannot have more than one lower bounds.");
        if (lowerBounds.length == 1) {
            return supertypeOf(newArrayType(lowerBounds[0]));
        }
        Type[] upperBounds = wildcardType.getUpperBounds();
        Preconditions.checkArgument(upperBounds.length == 1, "Wildcard should have only one upper bound.");
        return subtypeOf(newArrayType(upperBounds[0]));
    }

    public static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(D d, String str, Type... typeArr) {
        if (typeArr.length == 0) {
            typeArr = new Type[]{Object.class};
        }
        return newTypeVariableImpl(d, str, typeArr);
    }

    public static ParameterizedType newParameterizedType(Class<?> cls, Type... typeArr) {
        return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR.getOwnerType(cls), cls, typeArr);
    }

    public static ParameterizedType newParameterizedTypeWithOwner(Type type, Class<?> cls, Type... typeArr) {
        if (type == null) {
            return newParameterizedType(cls, typeArr);
        }
        Preconditions.checkNotNull(typeArr);
        Preconditions.checkArgument(cls.getEnclosingClass() != null, "Owner type for unenclosed %s", cls);
        return new ParameterizedTypeImpl(type, cls, typeArr);
    }

    private static <D extends GenericDeclaration> TypeVariable<D> newTypeVariableImpl(D d, String str, Type[] typeArr) {
        return (TypeVariable) Reflection.newProxy(TypeVariable.class, new TypeVariableInvocationHandler(new TypeVariableImpl(d, str, typeArr)));
    }

    @VisibleForTesting
    public static WildcardType subtypeOf(Type type) {
        return new WildcardTypeImpl(new Type[0], new Type[]{type});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Type subtypeOfComponentType(Type[] typeArr) {
        for (Type type : typeArr) {
            Type componentType = getComponentType(type);
            if (componentType != null) {
                if (componentType instanceof Class) {
                    Class cls = (Class) componentType;
                    if (cls.isPrimitive()) {
                        return cls;
                    }
                }
                return subtypeOf(componentType);
            }
        }
        return null;
    }

    @VisibleForTesting
    public static WildcardType supertypeOf(Type type) {
        return new WildcardTypeImpl(new Type[]{type}, new Type[]{Object.class});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Type[] toArray(Collection<Type> collection) {
        return (Type[]) collection.toArray(new Type[0]);
    }

    public static String toString(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }
}
