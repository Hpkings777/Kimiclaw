package com.sun.mail.util;

import java.util.Properties;
import javax.mail.Session;

/* JADX INFO: loaded from: classes.dex */
public class PropUtil {
    private PropUtil() {
    }

    private static boolean getBoolean(Object obj, boolean z) {
        if (obj != null) {
            if (obj instanceof String) {
                return z ? !((String) obj).equalsIgnoreCase("false") : ((String) obj).equalsIgnoreCase("true");
            }
            if (obj instanceof Boolean) {
                return ((Boolean) obj).booleanValue();
            }
        }
        return z;
    }

    public static boolean getBooleanProperty(Properties properties, String str, boolean z) {
        return getBoolean(getProp(properties, str), z);
    }

    @Deprecated
    public static boolean getBooleanSessionProperty(Session session, String str, boolean z) {
        return getBoolean(getProp(session.getProperties(), str), z);
    }

    public static boolean getBooleanSystemProperty(String str, boolean z) {
        try {
            try {
                return getBoolean(getProp(System.getProperties(), str), z);
            } catch (SecurityException unused) {
            }
        } catch (SecurityException unused2) {
            String property = System.getProperty(str);
            return property == null ? z : z ? !property.equalsIgnoreCase("false") : property.equalsIgnoreCase("true");
        }
    }

    private static int getInt(Object obj, int i) {
        if (obj != null) {
            if (obj instanceof String) {
                try {
                    String str = (String) obj;
                    return str.startsWith("0x") ? Integer.parseInt(str.substring(2), 16) : Integer.parseInt(str);
                } catch (NumberFormatException unused) {
                }
            }
            if (obj instanceof Integer) {
                return ((Integer) obj).intValue();
            }
        }
        return i;
    }

    public static int getIntProperty(Properties properties, String str, int i) {
        return getInt(getProp(properties, str), i);
    }

    @Deprecated
    public static int getIntSessionProperty(Session session, String str, int i) {
        return getInt(getProp(session.getProperties(), str), i);
    }

    private static Object getProp(Properties properties, String str) {
        Object obj = properties.get(str);
        return obj != null ? obj : properties.getProperty(str);
    }
}
