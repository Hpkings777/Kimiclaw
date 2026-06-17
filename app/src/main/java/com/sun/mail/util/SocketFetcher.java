package com.sun.mail.util;

import defpackage.AbstractC4671xI0;
import defpackage.YI0;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.PrivilegedAction;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.SocketFactory;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Marker;

/* JADX INFO: loaded from: classes.dex */
public class SocketFetcher {
    private static MailLogger logger = new MailLogger(SocketFetcher.class, "socket", "DEBUG SocketFetcher", PropUtil.getBooleanSystemProperty("mail.socket.debug", false), System.out);

    private SocketFetcher() {
    }

    private static void checkServerIdentity(String str, SSLSocket sSLSocket) throws IOException {
        try {
            Certificate[] peerCertificates = sSLSocket.getSession().getPeerCertificates();
            if (peerCertificates != null && peerCertificates.length > 0) {
                Certificate certificate = peerCertificates[0];
                if (certificate instanceof X509Certificate) {
                    if (matchCert(str, (X509Certificate) certificate)) {
                        return;
                    }
                }
            }
            sSLSocket.close();
            throw new IOException(AbstractC4671xI0.y("Can't verify identity of server: ", str));
        } catch (SSLPeerUnverifiedException e) {
            sSLSocket.close();
            IOException iOException = new IOException(AbstractC4671xI0.y("Can't verify identity of server: ", str));
            iOException.initCause(e);
            throw iOException;
        }
    }

    private static IOException cleanupAndThrow(Socket socket, IOException iOException) {
        try {
            socket.close();
            return iOException;
        } catch (Throwable th) {
            if (isRecoverable(th)) {
                iOException.addSuppressed(th);
                return iOException;
            }
            th.addSuppressed(iOException);
            if (th instanceof Error) {
                throw ((Error) th);
            }
            if (th instanceof RuntimeException) {
                throw ((RuntimeException) th);
            }
            throw new RuntimeException("unexpected exception", th);
        }
    }

    private static void configureSSLSocket(Socket socket, String str, Properties properties, String str2, SocketFactory socketFactory) throws IOException {
        if (socket instanceof SSLSocket) {
            SSLSocket sSLSocket = (SSLSocket) socket;
            String property = properties.getProperty(str2 + ".ssl.protocols", null);
            if (property != null) {
                sSLSocket.setEnabledProtocols(stringArray(property));
            } else {
                String[] enabledProtocols = sSLSocket.getEnabledProtocols();
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer("SSL enabled protocols before " + Arrays.asList(enabledProtocols));
                }
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < enabledProtocols.length; i++) {
                    String str3 = enabledProtocols[i];
                    if (str3 != null && !str3.startsWith("SSL")) {
                        arrayList.add(enabledProtocols[i]);
                    }
                }
                sSLSocket.setEnabledProtocols((String[]) arrayList.toArray(new String[arrayList.size()]));
            }
            String property2 = properties.getProperty(str2 + ".ssl.ciphersuites", null);
            if (property2 != null) {
                sSLSocket.setEnabledCipherSuites(stringArray(property2));
            }
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("SSL enabled protocols after " + Arrays.asList(sSLSocket.getEnabledProtocols()));
                logger.finer("SSL enabled ciphers after " + Arrays.asList(sSLSocket.getEnabledCipherSuites()));
            }
            sSLSocket.startHandshake();
            if (PropUtil.getBooleanProperty(properties, str2 + ".ssl.checkserveridentity", false)) {
                checkServerIdentity(str, sSLSocket);
            }
            if ((socketFactory instanceof MailSSLSocketFactory) && !((MailSSLSocketFactory) socketFactory).isServerTrusted(str, sSLSocket)) {
                throw cleanupAndThrow(sSLSocket, new IOException(AbstractC4671xI0.y("Server is not trusted: ", str)));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v2, types: [javax.net.SocketFactory] */
    /* JADX WARN: Type inference failed for: r10v3 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v14, types: [javax.net.ssl.SSLSocketFactory] */
    /* JADX WARN: Type inference failed for: r1v22 */
    /* JADX WARN: Type inference failed for: r1v23 */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v11, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v14, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v17 */
    /* JADX WARN: Type inference failed for: r3v36 */
    /* JADX WARN: Type inference failed for: r3v37 */
    /* JADX WARN: Type inference failed for: r3v38 */
    /* JADX WARN: Type inference failed for: r3v39 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r6v11 */
    /* JADX WARN: Type inference failed for: r6v12 */
    /* JADX WARN: Type inference failed for: r6v13 */
    /* JADX WARN: Type inference failed for: r6v14 */
    /* JADX WARN: Type inference failed for: r6v15 */
    /* JADX WARN: Type inference failed for: r6v16 */
    /* JADX WARN: Type inference failed for: r6v17 */
    /* JADX WARN: Type inference failed for: r6v19 */
    /* JADX WARN: Type inference failed for: r6v2 */
    /* JADX WARN: Type inference failed for: r6v21 */
    /* JADX WARN: Type inference failed for: r6v23, types: [boolean] */
    /* JADX WARN: Type inference failed for: r6v33 */
    /* JADX WARN: Type inference failed for: r6v41 */
    /* JADX WARN: Type inference failed for: r6v42 */
    /* JADX WARN: Type inference failed for: r6v43 */
    /* JADX WARN: Type inference failed for: r6v44 */
    /* JADX WARN: Type inference failed for: r6v45 */
    /* JADX WARN: Type inference failed for: r6v46 */
    /* JADX WARN: Type inference failed for: r6v47 */
    /* JADX WARN: Type inference failed for: r6v48 */
    /* JADX WARN: Type inference failed for: r6v49 */
    /* JADX WARN: Type inference failed for: r6v50 */
    /* JADX WARN: Type inference failed for: r6v51 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private static Socket createSocket(InetAddress inetAddress, int i, String str, int i2, int i3, int i4, Properties properties, String str2, SocketFactory socketFactory, boolean z) throws IOException {
        ?? r6;
        String property;
        int i5;
        String str3;
        String str4;
        String str5;
        Socket socketCreateSocket;
        ?? r62;
        String str6;
        Socket socket;
        ?? r63;
        ?? r3;
        ?? r64;
        Socket socket2;
        ?? r1;
        ?? r10;
        Socket socketCreateSocket2;
        String str7;
        String str8;
        SocketFactory socketFactory2 = socketFactory;
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("create socket: prefix " + str2 + ", localaddr " + inetAddress + ", localport " + i + ", host " + str + ", port " + i2 + ", connection timeout " + i3 + ", timeout " + i4 + ", socket factory " + socketFactory2 + ", useSSL " + z);
        }
        String property2 = properties.getProperty(str2 + ".proxy.host", null);
        String property3 = properties.getProperty(str2 + ".proxy.user", null);
        String property4 = properties.getProperty(str2 + ".proxy.password", null);
        int i6 = 80;
        int i7 = 1080;
        if (property2 != null) {
            int iIndexOf = property2.indexOf(58);
            if (iIndexOf >= 0) {
                try {
                    i6 = Integer.parseInt(property2.substring(iIndexOf + 1));
                } catch (NumberFormatException unused) {
                }
                property2 = property2.substring(0, iIndexOf);
            }
            int intProperty = PropUtil.getIntProperty(properties, str2 + ".proxy.port", i6);
            String str9 = "Using web proxy host, port: " + property2 + ", " + intProperty;
            if (logger.isLoggable(Level.FINER)) {
                String str10 = str9;
                logger.finer("web proxy host " + property2 + ", port " + intProperty);
                str8 = "web proxy host ";
                str7 = str10;
                if (property3 != null) {
                    MailLogger mailLogger = logger;
                    StringBuilder sbQ = AbstractC4671xI0.q("web proxy user ", property3, ", password ");
                    sbQ.append(property4 == null ? "<null>" : "<non-null>");
                    String string = sbQ.toString();
                    mailLogger.finer(string);
                    str8 = string;
                    str7 = str10;
                }
            } else {
                str7 = str9;
                str8 = str9;
            }
            i6 = intProperty;
            i5 = 1080;
            property = null;
            r6 = str8;
            str3 = str7;
        } else {
            r6 = 0;
            property = properties.getProperty(str2 + ".socks.host", null);
            if (property != null) {
                int iIndexOf2 = property.indexOf(58);
                if (iIndexOf2 >= 0) {
                    try {
                        i7 = Integer.parseInt(property.substring(iIndexOf2 + 1));
                    } catch (NumberFormatException unused2) {
                    }
                    property = property.substring(0, iIndexOf2);
                }
                int intProperty2 = PropUtil.getIntProperty(properties, str2 + ".socks.port", i7);
                String str11 = "Using SOCKS host, port: " + property + ", " + intProperty2;
                if (logger.isLoggable(Level.FINER)) {
                    str4 = str11;
                    logger.finer("socks host " + property + ", port " + intProperty2);
                    str5 = "socks host ";
                } else {
                    str4 = str11;
                    str5 = str11;
                }
                i5 = intProperty2;
                property2 = property2;
                str3 = str4;
                r6 = str5;
            } else {
                i5 = 1080;
                str3 = null;
            }
        }
        if (socketFactory2 == null || (r6 = socketFactory2 instanceof SSLSocketFactory) != 0) {
            socketCreateSocket = null;
            r62 = r6;
        } else {
            r62 = r6;
            socketCreateSocket = socketFactory2.createSocket();
        }
        if (socketCreateSocket != null) {
            str6 = property2;
            r63 = r62;
            socket = socketCreateSocket;
        } else if (property != null) {
            str6 = property2;
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(property, i5));
            Socket socket3 = new Socket(proxy);
            r63 = proxy;
            socket = socket3;
        } else {
            str6 = property2;
            r63 = 0;
            r63 = 0;
            if (PropUtil.getBooleanProperty(properties, str2 + ".usesocketchannels", false)) {
                logger.finer("using SocketChannels");
                socket = SocketChannel.open().socket();
            } else {
                socket = new Socket();
            }
        }
        if (i4 >= 0) {
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("set socket read timeout " + i4);
            }
            socket.setSoTimeout(i4);
        }
        ?? r32 = -1;
        int intProperty3 = PropUtil.getIntProperty(properties, str2 + ".writetimeout", -1);
        Socket socket4 = socket;
        if (intProperty3 != -1) {
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("set socket write timeout " + intProperty3);
            }
            WriteTimeoutSocket writeTimeoutSocket = new WriteTimeoutSocket(socket, intProperty3);
            socket4 = writeTimeoutSocket;
            r32 = writeTimeoutSocket;
        }
        if (inetAddress != null) {
            socket4.bind(new InetSocketAddress(inetAddress, i));
        }
        try {
            logger.finest("connecting...");
            try {
                if (str6 != null) {
                    int i8 = i2;
                    Socket socket5 = socket4;
                    try {
                        proxyConnect(socket5, str6, i6, property3, property4, str, i8 == true ? 1 : 0, i3);
                        r32 = str;
                        socket2 = socket5;
                        r63 = i8;
                    } catch (IOException e) {
                        e = e;
                        r3 = str;
                        r64 = i8;
                        IOException iOException = e;
                        logger.log(Level.FINEST, "connection failed", (Throwable) iOException);
                        throw new SocketConnectException(str3, iOException, r3, r64 == true ? 1 : 0, i3);
                    }
                } else {
                    String str12 = str;
                    int i9 = i2;
                    Socket socket6 = socket4;
                    if (i3 >= 0) {
                        socket6.connect(new InetSocketAddress(str12, i9 == true ? 1 : 0), i3);
                        socket2 = socket6;
                        r32 = str12;
                        r63 = i9;
                    } else {
                        socket6.connect(new InetSocketAddress(str12, i9 == true ? 1 : 0));
                        socket2 = socket6;
                        r32 = str12;
                        r63 = i9;
                    }
                }
                logger.finest("success!");
                if ((z || (socketFactory2 instanceof SSLSocketFactory)) && !(socket2 instanceof SSLSocket)) {
                    String property5 = properties.getProperty(str2 + ".ssl.trust");
                    if (property5 != null) {
                        try {
                            MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
                            if (property5.equals(Marker.ANY_MARKER)) {
                                mailSSLSocketFactory.setTrustAllHosts(true);
                            } else {
                                mailSSLSocketFactory.setTrustedHosts(property5.split("\\s+"));
                            }
                            r1 = mailSSLSocketFactory;
                        } catch (GeneralSecurityException e2) {
                            IOException iOException2 = new IOException("Can't create MailSSLSocketFactory");
                            iOException2.initCause(e2);
                            throw iOException2;
                        }
                    } else {
                        r1 = socketFactory2 instanceof SSLSocketFactory ? (SSLSocketFactory) socketFactory2 : (SSLSocketFactory) SSLSocketFactory.getDefault();
                    }
                    r10 = r1;
                    socketCreateSocket2 = r1.createSocket(socket2, r32, r63 == true ? 1 : 0, true);
                } else {
                    socketCreateSocket2 = socket2;
                    r10 = socketFactory2;
                }
                configureSSLSocket(socketCreateSocket2, r32, properties, str2, r10);
                return socketCreateSocket2;
            } catch (IOException e3) {
                e = e3;
                r3 = r32;
                r64 = r63;
            }
        } catch (IOException e4) {
            e = e4;
            r3 = str;
            r64 = i2;
        }
    }

    private static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: com.sun.mail.util.SocketFetcher.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                try {
                    return Thread.currentThread().getContextClassLoader();
                } catch (SecurityException unused) {
                    return null;
                }
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x0182  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01bd  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Socket getSocket(String str, int i, Properties properties, String str2, boolean z) throws IOException {
        String str3;
        int i2;
        SocketFactory socketFactory;
        String str4;
        Socket socketCreateSocket;
        String str5;
        Properties properties2;
        int i3;
        int i4;
        int i5;
        InetAddress inetAddress;
        int i6;
        Exception e;
        int i7;
        String str6 = str2;
        if (logger.isLoggable(Level.FINER)) {
            MailLogger mailLogger = logger;
            StringBuilder sb = new StringBuilder("getSocket, host ");
            str3 = str;
            sb.append(str3);
            sb.append(", port ");
            i2 = i;
            sb.append(i2);
            sb.append(", prefix ");
            sb.append(str6);
            sb.append(", useSSL ");
            sb.append(z);
            mailLogger.finer(sb.toString());
        } else {
            str3 = str;
            i2 = i;
        }
        if (str6 == null) {
            str6 = "socket";
        }
        String str7 = str6;
        Properties properties3 = properties == null ? new Properties() : properties;
        int intProperty = PropUtil.getIntProperty(properties3, str7.concat(".connectiontimeout"), -1);
        String property = properties3.getProperty(str7.concat(".localaddress"), null);
        InetAddress byName = property != null ? InetAddress.getByName(property) : null;
        int intProperty2 = PropUtil.getIntProperty(properties3, str7.concat(".localport"), 0);
        boolean booleanProperty = PropUtil.getBooleanProperty(properties3, str7.concat(".socketFactory.fallback"), true);
        int intProperty3 = PropUtil.getIntProperty(properties3, str7.concat(".timeout"), -1);
        String str8 = "unknown socket factory";
        if (z) {
            try {
                try {
                    Object obj = properties3.get(str7.concat(".ssl.socketFactory"));
                    if (obj instanceof SocketFactory) {
                        socketFactory = (SocketFactory) obj;
                        str8 = "SSL socket factory instance " + socketFactory;
                    } else {
                        socketFactory = null;
                    }
                    if (socketFactory == null) {
                        String property2 = properties3.getProperty(str7.concat(".ssl.socketFactory.class"));
                        str8 = "SSL socket factory class " + property2;
                        socketFactory = getSocketFactory(property2);
                    }
                    str4 = ".ssl.socketFactory.port";
                } catch (SocketTimeoutException e2) {
                    throw e2;
                }
            } catch (Exception e3) {
                e = e3;
                str5 = str7;
                properties2 = properties3;
                i5 = intProperty2;
                inetAddress = byName;
                i3 = intProperty3;
                i4 = intProperty;
                i6 = -1;
                if (!booleanProperty) {
                }
                socketCreateSocket = null;
                if (socketCreateSocket != null) {
                }
            }
        } else {
            socketFactory = null;
            str4 = null;
        }
        if (socketFactory == null) {
            Object obj2 = properties3.get(str7.concat(".socketFactory"));
            if (obj2 instanceof SocketFactory) {
                socketFactory = (SocketFactory) obj2;
                str8 = "socket factory instance " + socketFactory;
            }
            if (socketFactory == null) {
                String property3 = properties3.getProperty(str7.concat(".socketFactory.class"));
                str8 = "socket factory class " + property3;
                socketFactory = getSocketFactory(property3);
            }
            str4 = ".socketFactory.port";
        }
        String str9 = str8;
        if (socketFactory != null) {
            try {
                i7 = -1;
                try {
                    int intProperty4 = PropUtil.getIntProperty(properties3, str7 + str4, -1);
                    int i8 = intProperty4 == -1 ? i2 : intProperty4;
                    str5 = str7;
                    properties2 = properties3;
                    i5 = intProperty2;
                    inetAddress = byName;
                    i3 = intProperty3;
                    i4 = intProperty;
                    try {
                        socketCreateSocket = createSocket(inetAddress, i5, str3, i8, i4, i3, properties2, str5, socketFactory, z);
                    } catch (Exception e4) {
                        e = e4;
                        i6 = i8;
                        str8 = str9;
                        if (!booleanProperty) {
                            if (e instanceof InvocationTargetException) {
                                Throwable targetException = ((InvocationTargetException) e).getTargetException();
                                if (targetException instanceof Exception) {
                                    e = (Exception) targetException;
                                }
                            }
                            Exception exc = e;
                            if (exc instanceof IOException) {
                                throw ((IOException) exc);
                            }
                            throw new SocketConnectException(AbstractC4671xI0.y("Using ", str8), exc, str, i6, i4);
                        }
                        socketCreateSocket = null;
                    }
                } catch (Exception e5) {
                    e = e5;
                    str5 = str7;
                    properties2 = properties3;
                    i5 = intProperty2;
                    inetAddress = byName;
                    i3 = intProperty3;
                    i4 = intProperty;
                    i6 = i7;
                    str8 = str9;
                    if (!booleanProperty) {
                    }
                    socketCreateSocket = null;
                    if (socketCreateSocket != null) {
                    }
                }
            } catch (Exception e6) {
                e = e6;
                str5 = str7;
                properties2 = properties3;
                i5 = intProperty2;
                inetAddress = byName;
                i3 = intProperty3;
                i4 = intProperty;
                i7 = -1;
            }
            if (socketCreateSocket != null) {
                return createSocket(inetAddress, i5, str, i2, i4, i3, properties2, str5, null, z);
            }
            if (i3 < 0) {
                return socketCreateSocket;
            }
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("set socket read timeout " + i3);
            }
            socketCreateSocket.setSoTimeout(i3);
            return socketCreateSocket;
        }
        str5 = str7;
        properties2 = properties3;
        i5 = intProperty2;
        inetAddress = byName;
        i3 = intProperty3;
        i4 = intProperty;
        socketCreateSocket = null;
        if (socketCreateSocket != null) {
        }
    }

    private static SocketFactory getSocketFactory(String str) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InvocationTargetException {
        Class<?> cls = null;
        if (str == null || str.length() == 0) {
            return null;
        }
        ClassLoader contextClassLoader = getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                cls = Class.forName(str, false, contextClassLoader);
            } catch (ClassNotFoundException unused) {
            }
        }
        if (cls == null) {
            cls = Class.forName(str);
        }
        return (SocketFactory) cls.getMethod("getDefault", new Class[0]).invoke(new Object(), new Object[0]);
    }

    private static boolean isRecoverable(Throwable th) {
        return (th instanceof Exception) || (th instanceof LinkageError);
    }

    private static boolean matchCert(String str, X509Certificate x509Certificate) {
        MailLogger mailLogger = logger;
        Level level = Level.FINER;
        if (mailLogger.isLoggable(level)) {
            logger.finer("matchCert server " + str + ", cert " + x509Certificate);
        }
        try {
            Class<?> cls = Class.forName("sun.security.util.HostnameChecker");
            Object objInvoke = cls.getMethod("getInstance", Byte.TYPE).invoke(new Object(), (byte) 2);
            if (logger.isLoggable(level)) {
                logger.finer("using sun.security.util.HostnameChecker");
            }
            try {
                cls.getMethod("match", String.class, X509Certificate.class).invoke(objInvoke, str, x509Certificate);
                return true;
            } catch (InvocationTargetException e) {
                logger.log(Level.FINER, "HostnameChecker FAIL", (Throwable) e);
                return false;
            }
        } catch (Exception e2) {
            logger.log(Level.FINER, "NO sun.security.util.HostnameChecker", (Throwable) e2);
            try {
                Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
                if (subjectAlternativeNames != null) {
                    boolean z = false;
                    for (List<?> list : subjectAlternativeNames) {
                        if (((Integer) list.get(0)).intValue() == 2) {
                            String str2 = (String) list.get(1);
                            if (logger.isLoggable(Level.FINER)) {
                                logger.finer("found name: " + str2);
                            }
                            if (matchServer(str, str2)) {
                                return true;
                            }
                            z = true;
                        }
                    }
                    if (z) {
                        return false;
                    }
                }
            } catch (CertificateParsingException unused) {
            }
            Matcher matcher = Pattern.compile("CN=([^,]*)").matcher(x509Certificate.getSubjectX500Principal().getName());
            return matcher.find() && matchServer(str, matcher.group(1).trim());
        }
    }

    private static boolean matchServer(String str, String str2) {
        int length;
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("match server " + str + " with " + str2);
        }
        if (!str2.startsWith("*.")) {
            return str.equalsIgnoreCase(str2);
        }
        String strSubstring = str2.substring(2);
        return strSubstring.length() != 0 && (length = str.length() - strSubstring.length()) >= 1 && str.charAt(length + (-1)) == '.' && str.regionMatches(true, length, strSubstring, 0, strSubstring.length());
    }

    private static void proxyConnect(Socket socket, String str, int i, String str2, String str3, String str4, int i2, int i3) throws IOException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("connecting through proxy " + str + ":" + i + " to " + str4 + ":" + i2);
        }
        if (i3 >= 0) {
            socket.connect(new InetSocketAddress(str, i), i3);
        } else {
            socket.connect(new InetSocketAddress(str, i));
        }
        OutputStream outputStream = socket.getOutputStream();
        Charset charset = StandardCharsets.UTF_8;
        PrintStream printStream = new PrintStream(outputStream, false, charset.name());
        StringBuilder sb = new StringBuilder("CONNECT ");
        sb.append(str4);
        sb.append(":");
        sb.append(i2);
        sb.append(" HTTP/1.1\r\nHost: ");
        sb.append(str4);
        sb.append(":");
        sb.append(i2);
        sb.append(IOUtils.LINE_SEPARATOR_WINDOWS);
        if (str2 != null && str3 != null) {
            String str5 = new String(BASE64EncoderStream.encode((str2 + ':' + str3).getBytes(charset)), StandardCharsets.US_ASCII);
            sb.append("Proxy-Authorization: Basic ");
            sb.append(str5);
            sb.append(IOUtils.LINE_SEPARATOR_WINDOWS);
        }
        sb.append("Proxy-Connection: keep-alive\r\n\r\n");
        printStream.print(sb.toString());
        printStream.flush();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
        boolean z = true;
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null || line.length() == 0) {
                return;
            }
            logger.finest(line);
            if (z) {
                StringTokenizer stringTokenizer = new StringTokenizer(line);
                stringTokenizer.nextToken();
                if (!stringTokenizer.nextToken().equals("200")) {
                    try {
                        socket.close();
                    } catch (IOException unused) {
                    }
                    ConnectException connectException = new ConnectException("connection through proxy " + str + ":" + i + " to " + str4 + ":" + i2 + " failed: " + line);
                    logger.log(Level.FINE, "connect failed", (Throwable) connectException);
                    throw connectException;
                }
                z = false;
            }
        }
    }

    @Deprecated
    public static Socket startTLS(Socket socket) throws IOException {
        return startTLS(socket, new Properties(), "socket");
    }

    private static String[] stringArray(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        ArrayList arrayList = new ArrayList();
        while (stringTokenizer.hasMoreTokens()) {
            arrayList.add(stringTokenizer.nextToken());
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    @Deprecated
    public static Socket startTLS(Socket socket, Properties properties, String str) throws IOException {
        return startTLS(socket, socket.getInetAddress().getHostName(), properties, str);
    }

    public static Socket startTLS(Socket socket, String str, Properties properties, String str2) throws IOException {
        SocketFactory socketFactory;
        String str3;
        SSLSocketFactory sSLSocketFactory;
        int port = socket.getPort();
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("startTLS host " + str + ", port " + port);
        }
        String str4 = "unknown socket factory";
        try {
            Object obj = properties.get(str2 + ".ssl.socketFactory");
            SSLSocketFactory sSLSocketFactory2 = null;
            sSLSocketFactory2 = null;
            if (obj instanceof SocketFactory) {
                socketFactory = (SocketFactory) obj;
                str4 = "SSL socket factory instance " + socketFactory;
            } else {
                socketFactory = null;
            }
            if (socketFactory == null) {
                String property = properties.getProperty(str2 + ".ssl.socketFactory.class");
                socketFactory = getSocketFactory(property);
                str4 = "SSL socket factory class " + property;
            }
            if (socketFactory != null && (socketFactory instanceof SSLSocketFactory)) {
                sSLSocketFactory2 = (SSLSocketFactory) socketFactory;
            }
            if (sSLSocketFactory2 == null) {
                Object obj2 = properties.get(str2 + ".socketFactory");
                if (obj2 instanceof SocketFactory) {
                    socketFactory = (SocketFactory) obj2;
                    str4 = "socket factory instance " + socketFactory;
                }
                if (socketFactory == null) {
                    String property2 = properties.getProperty(str2 + ".socketFactory.class");
                    socketFactory = getSocketFactory(property2);
                    str4 = "socket factory class " + property2;
                }
                if (socketFactory != null && (socketFactory instanceof SSLSocketFactory)) {
                    sSLSocketFactory2 = (SSLSocketFactory) socketFactory;
                }
            }
            SSLSocketFactory sSLSocketFactory3 = sSLSocketFactory2;
            if (sSLSocketFactory2 == null) {
                String property3 = properties.getProperty(str2 + ".ssl.trust");
                if (property3 != null) {
                    try {
                        MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
                        if (property3.equals(Marker.ANY_MARKER)) {
                            mailSSLSocketFactory.setTrustAllHosts(true);
                        } else {
                            mailSSLSocketFactory.setTrustedHosts(property3.split("\\s+"));
                        }
                        str3 = "mail SSL socket factory";
                        sSLSocketFactory = mailSSLSocketFactory;
                    } catch (GeneralSecurityException e) {
                        IOException iOException = new IOException("Can't create MailSSLSocketFactory");
                        iOException.initCause(e);
                        throw iOException;
                    }
                } else {
                    str3 = "default SSL socket factory";
                    sSLSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                }
                sSLSocketFactory3 = sSLSocketFactory;
            }
            Socket socketCreateSocket = sSLSocketFactory3.createSocket(socket, str, port, true);
            configureSSLSocket(socketCreateSocket, str, properties, str2, sSLSocketFactory3);
            return socketCreateSocket;
        } catch (Exception e2) {
            e = e2;
            if (e instanceof InvocationTargetException) {
                Throwable targetException = ((InvocationTargetException) e).getTargetException();
                if (targetException instanceof Exception) {
                    e = (Exception) targetException;
                }
            }
            if (e instanceof IOException) {
                throw ((IOException) e);
            }
            StringBuilder sbI = YI0.i("Exception in startTLS using ", str4, ": host, port: ", str, ", ");
            sbI.append(port);
            sbI.append("; Exception: ");
            sbI.append(e);
            IOException iOException2 = new IOException(sbI.toString());
            iOException2.initCause(e);
            throw iOException2;
        }
    }

    public static Socket getSocket(String str, int i, Properties properties, String str2) throws IOException {
        return getSocket(str, i, properties, str2, false);
    }
}
