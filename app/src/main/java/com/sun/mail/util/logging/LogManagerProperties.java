package com.sun.mail.util.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectStreamException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;
import org.apache.commons.io.FilenameUtils;

/* JADX INFO: loaded from: classes.dex */
final class LogManagerProperties extends Properties {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final Object LOG_MANAGER;
    private static final Method LR_GET_INSTANT;
    private static final Method LR_GET_LONG_TID;
    private static volatile String[] REFLECT_NAMES = null;
    private static final Method ZDT_OF_INSTANT;
    private static final Method ZI_SYSTEM_DEFAULT;
    private static final long serialVersionUID = -2239983349056806252L;
    private final String prefix;

    /* JADX WARN: Removed duplicated region for block: B:17:0x0062 A[PHI: r0 r1 r2
      0x0062: PHI (r0v13 java.lang.reflect.Method) = 
      (r0v7 java.lang.reflect.Method)
      (r0v8 java.lang.reflect.Method)
      (r0v9 java.lang.reflect.Method)
      (r0v26 java.lang.reflect.Method)
     binds: [B:48:0x00a6, B:52:0x00ad, B:44:0x009f, B:16:0x0060] A[DONT_GENERATE, DONT_INLINE]
      0x0062: PHI (r1v8 java.lang.reflect.Method) = 
      (r1v5 java.lang.reflect.Method)
      (r1v6 java.lang.reflect.Method)
      (r1v7 java.lang.reflect.Method)
      (r1v9 java.lang.reflect.Method)
     binds: [B:48:0x00a6, B:52:0x00ad, B:44:0x009f, B:16:0x0060] A[DONT_GENERATE, DONT_INLINE]
      0x0062: PHI (r2v11 java.lang.reflect.Method) = 
      (r2v6 java.lang.reflect.Method)
      (r2v7 java.lang.reflect.Method)
      (r2v8 java.lang.reflect.Method)
      (r2v17 java.lang.reflect.Method)
     binds: [B:48:0x00a6, B:52:0x00ad, B:44:0x009f, B:16:0x0060] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0065  */
    static {
        Method method;
        Object obj;
        Method method2;
        Method method3;
        Method method4;
        Method method5 = null;
        try {
            method = LogRecord.class.getMethod("getLongThreadID", new Class[0]);
        } catch (RuntimeException | Exception | LinkageError unused) {
            method = null;
        }
        LR_GET_LONG_TID = method;
        try {
            method3 = LogRecord.class.getMethod("getInstant", new Class[0]);
            try {
                try {
                    method4 = findClass("java.time.ZoneId").getMethod("systemDefault", new Class[0]);
                    try {
                        try {
                        } catch (LinkageError unused2) {
                            method2 = null;
                        }
                    } catch (RuntimeException unused3) {
                        method2 = null;
                    } catch (Exception unused4) {
                        method2 = null;
                    } catch (Throwable th) {
                        throw th;
                    }
                } catch (LinkageError unused5) {
                    method2 = null;
                    method4 = null;
                }
            } catch (RuntimeException unused6) {
                method2 = null;
                method4 = null;
            } catch (Exception unused7) {
                method2 = null;
                method4 = null;
            } catch (Throwable th2) {
                th = th2;
                obj = null;
                throw th;
            }
        } catch (RuntimeException unused8) {
            method2 = null;
            method3 = null;
            method4 = null;
        } catch (Exception unused9) {
            method2 = null;
            method3 = null;
            method4 = null;
        } catch (LinkageError unused10) {
            method2 = null;
            method3 = null;
            method4 = null;
        } catch (Throwable th3) {
            th = th3;
            obj = null;
        }
        if (!Modifier.isStatic(method4.getModifiers())) {
            throw null;
        }
        method2 = findClass("java.time.ZonedDateTime").getMethod("ofInstant", findClass("java.time.Instant"), findClass("java.time.ZoneId"));
        try {
        } catch (LinkageError unused11) {
            if (method3 != null) {
            }
            method2 = null;
            method4 = null;
            LR_GET_INSTANT = method5;
            ZI_SYSTEM_DEFAULT = method4;
            ZDT_OF_INSTANT = method2;
            LOG_MANAGER = loadLogManager();
        } catch (RuntimeException unused12) {
            if (method3 == null || method4 == null || method2 == null) {
            }
            LR_GET_INSTANT = method5;
            ZI_SYSTEM_DEFAULT = method4;
            ZDT_OF_INSTANT = method2;
            LOG_MANAGER = loadLogManager();
        } catch (Exception unused13) {
            if (method3 == null || method4 == null || method2 == null) {
            }
            LR_GET_INSTANT = method5;
            ZI_SYSTEM_DEFAULT = method4;
            ZDT_OF_INSTANT = method2;
            LOG_MANAGER = loadLogManager();
        } catch (Throwable th4) {
            throw th4;
        }
        if (Modifier.isStatic(method2.getModifiers())) {
            if (Comparable.class.isAssignableFrom(method2.getReturnType())) {
                if (method3 != null) {
                    method5 = method3;
                } else {
                    method2 = null;
                    method4 = null;
                }
                LR_GET_INSTANT = method5;
                ZI_SYSTEM_DEFAULT = method4;
                ZDT_OF_INSTANT = method2;
                LOG_MANAGER = loadLogManager();
            }
        }
        throw null;
        if (method3 != null || method4 == null || method2 == null) {
        }
        LR_GET_INSTANT = method5;
        ZI_SYSTEM_DEFAULT = method4;
        ZDT_OF_INSTANT = method2;
        LOG_MANAGER = loadLogManager();
    }

    public LogManagerProperties(Properties properties, String str) {
        super(properties);
        if (properties == null || str == null) {
            throw null;
        }
        this.prefix = str;
    }

    public static void checkLogManagerAccess() {
        Object obj = LOG_MANAGER;
        boolean z = false;
        if (obj != null) {
            try {
                if (obj instanceof LogManager) {
                    z = true;
                    ((LogManager) obj).checkAccess();
                }
            } catch (LinkageError | RuntimeException unused) {
            } catch (SecurityException e) {
                if (z) {
                    throw e;
                }
            }
        }
        if (z) {
            return;
        }
        checkLoggingAccess();
    }

    private static void checkLoggingAccess() {
        Logger logger = Logger.getLogger("global");
        try {
            if (Logger.class == logger.getClass()) {
                logger.removeHandler(null);
                return;
            }
        } catch (NullPointerException unused) {
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new LoggingPermission("control", null));
        }
    }

    private Properties exportCopy(Properties properties) {
        Thread.holdsLock(this);
        Properties properties2 = new Properties(properties);
        properties2.putAll(this);
        return properties2;
    }

    private static Class<?> findClass(String str) throws ClassNotFoundException {
        ClassLoader[] classLoaders = getClassLoaders();
        ClassLoader classLoader = classLoaders[0];
        if (classLoader == null) {
            return tryLoad(str, classLoaders[1]);
        }
        try {
            return Class.forName(str, false, classLoader);
        } catch (ClassNotFoundException unused) {
            return tryLoad(str, classLoaders[1]);
        }
    }

    public static String fromLogManager(String str) {
        str.getClass();
        Object obj = LOG_MANAGER;
        try {
            if (obj instanceof Properties) {
                return ((Properties) obj).getProperty(str);
            }
        } catch (RuntimeException unused) {
        }
        if (obj == null) {
            return null;
        }
        try {
            if (obj instanceof LogManager) {
                return ((LogManager) obj).getProperty(str);
            }
            return null;
        } catch (LinkageError | RuntimeException unused2) {
            return null;
        }
    }

    private static ClassLoader[] getClassLoaders() {
        return (ClassLoader[]) AccessController.doPrivileged(new PrivilegedAction<ClassLoader[]>() { // from class: com.sun.mail.util.logging.LogManagerProperties.1
            @Override // java.security.PrivilegedAction
            public ClassLoader[] run() {
                ClassLoader[] classLoaderArr = new ClassLoader[2];
                try {
                    classLoaderArr[0] = ClassLoader.getSystemClassLoader();
                } catch (SecurityException unused) {
                    classLoaderArr[0] = null;
                }
                try {
                    classLoaderArr[1] = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException unused2) {
                    classLoaderArr[1] = null;
                }
                return classLoaderArr;
            }
        });
    }

    public static String getLocalHost(Object obj) throws Exception {
        try {
            Method method = obj.getClass().getMethod("getLocalHost", new Class[0]);
            if (Modifier.isStatic(method.getModifiers()) || method.getReturnType() != String.class) {
                throw new NoSuchMethodException(method.toString());
            }
            return (String) method.invoke(obj, new Object[0]);
        } catch (ExceptionInInitializerError e) {
            throw wrapOrThrow(e);
        } catch (InvocationTargetException e2) {
            throw paramOrError(e2);
        }
    }

    public static Long getLongThreadID(LogRecord logRecord) {
        logRecord.getClass();
        Method method = LR_GET_LONG_TID;
        if (method == null) {
            return null;
        }
        try {
            return (Long) method.invoke(logRecord, new Object[0]);
        } catch (RuntimeException | Exception unused) {
            return null;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new UndeclaredThrowableException(e);
        }
    }

    public static Comparable<?> getZonedDateTime(LogRecord logRecord) {
        logRecord.getClass();
        Method method = ZDT_OF_INSTANT;
        if (method != null) {
            try {
                return (Comparable) method.invoke(null, LR_GET_INSTANT.invoke(logRecord, new Object[0]), ZI_SYSTEM_DEFAULT.invoke(null, new Object[0]));
            } catch (RuntimeException | Exception unused) {
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof Error) {
                    throw ((Error) cause);
                }
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new UndeclaredThrowableException(e);
            }
        }
        return null;
    }

    public static boolean hasLogManager() {
        Object obj = LOG_MANAGER;
        return (obj == null || (obj instanceof Properties)) ? false : true;
    }

    public static boolean isReflectionClass(String str) throws Exception {
        String[] strArrReflectionClassNames = REFLECT_NAMES;
        if (strArrReflectionClassNames == null) {
            strArrReflectionClassNames = reflectionClassNames();
            REFLECT_NAMES = strArrReflectionClassNames;
        }
        for (String str2 : strArrReflectionClassNames) {
            if (str.equals(str2)) {
                return true;
            }
        }
        findClass(str);
        return false;
    }

    public static boolean isStaticUtilityClass(String str) throws Exception {
        Class<?> clsFindClass = findClass(str);
        if (clsFindClass != Object.class) {
            Method[] methods = clsFindClass.getMethods();
            if (methods.length != 0) {
                for (Method method : methods) {
                    if (method.getDeclaringClass() != Object.class && !Modifier.isStatic(method.getModifiers())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static Object loadLogManager() {
        try {
            return LogManager.getLogManager();
        } catch (LinkageError unused) {
            return readConfiguration();
        } catch (RuntimeException unused2) {
            return readConfiguration();
        }
    }

    public static Comparator<? super LogRecord> newComparator(String str) throws Exception {
        return (Comparator) newObjectFrom(str, Comparator.class);
    }

    public static ErrorManager newErrorManager(String str) throws Exception {
        return (ErrorManager) newObjectFrom(str, ErrorManager.class);
    }

    public static Filter newFilter(String str) throws Exception {
        return (Filter) newObjectFrom(str, Filter.class);
    }

    public static Formatter newFormatter(String str) throws Exception {
        return (Formatter) newObjectFrom(str, Formatter.class);
    }

    public static <T> T newObjectFrom(String str, Class<T> cls) throws Exception {
        try {
            Class<?> clsFindClass = findClass(str);
            if (cls.isAssignableFrom(clsFindClass)) {
                try {
                    return cls.cast(clsFindClass.getConstructor(new Class[0]).newInstance(new Object[0]));
                } catch (InvocationTargetException e) {
                    throw paramOrError(e);
                }
            }
            throw new ClassCastException(clsFindClass.getName() + " cannot be cast to " + cls.getName());
        } catch (ExceptionInInitializerError e2) {
            throw wrapOrThrow(e2);
        } catch (NoClassDefFoundError e3) {
            throw new ClassNotFoundException(e3.toString(), e3);
        }
    }

    private static Exception paramOrError(InvocationTargetException invocationTargetException) {
        Throwable cause = invocationTargetException.getCause();
        if (cause == null || (!(cause instanceof VirtualMachineError) && !(cause instanceof ThreadDeath))) {
            return invocationTargetException;
        }
        throw ((Error) cause);
    }

    public static long parseDurationToMillis(CharSequence charSequence) throws Exception {
        charSequence.getClass();
        try {
            Class<?> clsFindClass = findClass("java.time.Duration");
            Method method = clsFindClass.getMethod("parse", CharSequence.class);
            if (!clsFindClass.isAssignableFrom(method.getReturnType()) || !Modifier.isStatic(method.getModifiers())) {
                throw new NoSuchMethodException(method.toString());
            }
            Method method2 = clsFindClass.getMethod("toMillis", new Class[0]);
            if (!Long.TYPE.isAssignableFrom(method2.getReturnType()) || Modifier.isStatic(method2.getModifiers())) {
                throw new NoSuchMethodException(method2.toString());
            }
            return ((Long) method2.invoke(method.invoke(null, charSequence), new Object[0])).longValue();
        } catch (ExceptionInInitializerError e) {
            throw wrapOrThrow(e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof ArithmeticException) {
                throw ((ArithmeticException) cause);
            }
            throw paramOrError(e2);
        }
    }

    private Object preWrite(Object obj) {
        return get(obj);
    }

    private static Properties readConfiguration() {
        Properties properties = new Properties();
        try {
            String property = System.getProperty("java.util.logging.config.file");
            if (property != null) {
                FileInputStream fileInputStream = new FileInputStream(new File(property).getCanonicalFile());
                try {
                    properties.load(fileInputStream);
                    return properties;
                } finally {
                    fileInputStream.close();
                }
            }
        } catch (RuntimeException | Exception | LinkageError unused) {
        }
        return properties;
    }

    private static String[] reflectionClassNames() throws Exception {
        try {
            HashSet hashSet = new HashSet();
            Throwable th = (Throwable) Throwable.class.getConstructor(new Class[0]).newInstance(new Object[0]);
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                if (LogManagerProperties.class.getName().equals(stackTraceElement.getClassName())) {
                    break;
                }
                hashSet.add(stackTraceElement.getClassName());
            }
            Throwable.class.getMethod("fillInStackTrace", new Class[0]).invoke(th, new Object[0]);
            for (StackTraceElement stackTraceElement2 : th.getStackTrace()) {
                if (LogManagerProperties.class.getName().equals(stackTraceElement2.getClassName())) {
                    break;
                }
                hashSet.add(stackTraceElement2.getClassName());
            }
            return (String[]) hashSet.toArray(new String[hashSet.size()]);
        } catch (InvocationTargetException e) {
            throw paramOrError(e);
        }
    }

    public static <T> Comparator<T> reverseOrder(Comparator<T> comparator) {
        comparator.getClass();
        Comparator<T> comparator2 = null;
        try {
            try {
                Method method = comparator.getClass().getMethod("reversed", new Class[0]);
                if (!Modifier.isStatic(method.getModifiers()) && Comparator.class.isAssignableFrom(method.getReturnType())) {
                    try {
                        comparator2 = (Comparator) method.invoke(comparator, new Object[0]);
                    } catch (ExceptionInInitializerError e) {
                        throw wrapOrThrow(e);
                    }
                }
            } catch (ReflectiveOperationException | RuntimeException unused) {
            }
        } catch (InvocationTargetException e2) {
            paramOrError(e2);
        }
        return comparator2 == null ? Collections.reverseOrder(comparator) : comparator2;
    }

    public static String toLanguageTag(Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        char[] cArr = new char[variant.length() + country.length() + language.length() + 2];
        int length = language.length();
        language.getChars(0, length, cArr, 0);
        if (country.length() != 0 || (language.length() != 0 && variant.length() != 0)) {
            cArr[length] = '-';
            int i = length + 1;
            country.getChars(0, country.length(), cArr, i);
            length = i + country.length();
        }
        if (variant.length() != 0 && (language.length() != 0 || country.length() != 0)) {
            cArr[length] = '-';
            int i2 = length + 1;
            variant.getChars(0, variant.length(), cArr, i2);
            length = i2 + variant.length();
        }
        return String.valueOf(cArr, 0, length);
    }

    private static Class<?> tryLoad(String str, ClassLoader classLoader) throws ClassNotFoundException {
        return classLoader != null ? Class.forName(str, false, classLoader) : Class.forName(str);
    }

    private static InvocationTargetException wrapOrThrow(ExceptionInInitializerError exceptionInInitializerError) {
        if (exceptionInInitializerError.getCause() instanceof Error) {
            throw exceptionInInitializerError;
        }
        return new InvocationTargetException(exceptionInInitializerError);
    }

    private synchronized Object writeReplace() throws ObjectStreamException {
        return exportCopy((Properties) ((Properties) this).defaults.clone());
    }

    @Override // java.util.Hashtable
    public synchronized Object clone() {
        return exportCopy(((Properties) this).defaults);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0025  */
    @Override // java.util.Hashtable, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized boolean containsKey(Object obj) {
        boolean z;
        try {
            z = (obj instanceof String) && getProperty((String) obj) != null;
            if (!z) {
                if (!((Properties) this).defaults.containsKey(obj)) {
                    boolean z2 = super.containsKey(obj);
                    z = z2;
                }
            }
        } finally {
        }
        return z;
    }

    @Override // java.util.Hashtable, java.util.Map
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof Properties) {
            return super.equals(obj);
        }
        return false;
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized Object get(Object obj) {
        Object property;
        try {
            property = obj instanceof String ? getProperty((String) obj) : null;
            if (property == null && (property = ((Properties) this).defaults.get(obj)) == null && !((Properties) this).defaults.containsKey(obj)) {
                property = super.get(obj);
            }
        } catch (Throwable th) {
            throw th;
        }
        return property;
    }

    @Override // java.util.Properties
    public synchronized String getProperty(String str) {
        String property;
        try {
            property = ((Properties) this).defaults.getProperty(str);
            if (property == null) {
                if (str.length() > 0) {
                    property = fromLogManager(this.prefix + FilenameUtils.EXTENSION_SEPARATOR + str);
                }
                if (property == null) {
                    property = fromLogManager(str);
                }
                if (property != null) {
                    super.put(str, property);
                } else {
                    Object obj = super.get(str);
                    property = obj instanceof String ? (String) obj : null;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
        return property;
    }

    @Override // java.util.Hashtable, java.util.Map
    public int hashCode() {
        return super.hashCode();
    }

    @Override // java.util.Properties
    public Enumeration<?> propertyNames() {
        return super.propertyNames();
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized Object put(Object obj, Object obj2) {
        if (!(obj instanceof String) || !(obj2 instanceof String)) {
            return super.put(obj, obj2);
        }
        Object objPreWrite = preWrite(obj);
        Object objPut = super.put(obj, obj2);
        if (objPut != null) {
            objPreWrite = objPut;
        }
        return objPreWrite;
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized Object remove(Object obj) {
        Object objPreWrite;
        objPreWrite = preWrite(obj);
        Object objRemove = super.remove(obj);
        if (objRemove != null) {
            objPreWrite = objRemove;
        }
        return objPreWrite;
    }

    @Override // java.util.Properties
    public Object setProperty(String str, String str2) {
        return put(str, str2);
    }

    @Override // java.util.Properties
    public String getProperty(String str, String str2) {
        String property = getProperty(str);
        return property == null ? str2 : property;
    }
}
