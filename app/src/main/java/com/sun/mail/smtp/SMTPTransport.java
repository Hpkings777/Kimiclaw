package com.sun.mail.smtp;

import com.google.common.base.Ascii;
import com.google.common.net.HttpHeaders;
import com.google.common.primitives.UnsignedBytes;
import com.sun.mail.auth.Ntlm;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.MailConnectException;
import com.sun.mail.util.MailLogger;
import com.sun.mail.util.PropUtil;
import com.sun.mail.util.SocketConnectException;
import com.sun.mail.util.SocketFetcher;
import com.sun.mail.util.TraceInputStream;
import com.sun.mail.util.TraceOutputStream;
import defpackage.AbstractC2039eV;
import defpackage.AbstractC4671xI0;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Service;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.ParseException;
import javax.net.ssl.SSLSocket;
import kotlin.jvm.internal.IntCompanionObject;
import org.apache.commons.io.IOUtils;

/* JADX INFO: loaded from: classes.dex */
public class SMTPTransport extends Transport {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String UNKNOWN = "UNKNOWN";
    private Address[] addresses;
    private boolean allowutf8;
    private Map<String, Authenticator> authenticators;
    private String authorizationID;
    private int chunkSize;
    private SMTPOutputStream dataStream;
    private boolean debugpassword;
    private boolean debugusername;
    private String defaultAuthenticationMechanisms;
    private int defaultPort;
    private boolean enableSASL;
    private MessagingException exception;
    private Hashtable<String, String> extMap;
    private String host;
    private Address[] invalidAddr;
    private boolean isSSL;
    private int lastReturnCode;
    private String lastServerResponse;
    private LineInputStream lineInputStream;
    private String localHostName;
    private MailLogger logger;
    private MimeMessage message;
    private String name;
    private boolean noauthdebug;
    private boolean noopStrict;
    private boolean notificationDone;
    private String ntlmDomain;
    private boolean quitOnSessionReject;
    private boolean quitWait;
    private boolean reportSuccess;
    private boolean requireStartTLS;
    private SaslAuthenticator saslAuthenticator;
    private String[] saslMechanisms;
    private String saslRealm;
    private boolean sendPartiallyFailed;
    private BufferedInputStream serverInput;
    private OutputStream serverOutput;
    private Socket serverSocket;
    private TraceInputStream traceInput;
    private MailLogger traceLogger;
    private TraceOutputStream traceOutput;
    private boolean useCanonicalHostName;
    private boolean useRset;
    private boolean useStartTLS;
    private Address[] validSentAddr;
    private Address[] validUnsentAddr;
    private static final String[] ignoreList = {"Bcc", HttpHeaders.CONTENT_LENGTH};
    private static final byte[] CRLF = {Ascii.CR, 10};
    private static final String[] UNKNOWN_SA = new String[0];
    private static char[] hexchar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public abstract class Authenticator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final boolean enabled;
        private final String mech;
        protected int resp;

        public Authenticator(SMTPTransport sMTPTransport, String str) {
            this(str, true);
        }

        public boolean authenticate(String str, String str2, String str3, String str4) throws MessagingException {
            String str5;
            str5 = "failed";
            try {
                try {
                    String initialResponse = getInitialResponse(str, str2, str3, str4);
                    if (SMTPTransport.this.noauthdebug && SMTPTransport.this.isTracing()) {
                        SMTPTransport.this.logger.fine("AUTH " + this.mech + " command trace suppressed");
                        SMTPTransport.this.suspendTracing();
                    }
                    if (initialResponse != null) {
                        SMTPTransport sMTPTransport = SMTPTransport.this;
                        StringBuilder sb = new StringBuilder("AUTH ");
                        sb.append(this.mech);
                        sb.append(" ");
                        sb.append(initialResponse.length() == 0 ? "=" : initialResponse);
                        this.resp = sMTPTransport.simpleCommand(sb.toString());
                    } else {
                        this.resp = SMTPTransport.this.simpleCommand("AUTH " + this.mech);
                    }
                    if (this.resp == 530) {
                        SMTPTransport.this.startTLS();
                        if (initialResponse != null) {
                            this.resp = SMTPTransport.this.simpleCommand("AUTH " + this.mech + " " + initialResponse);
                        } else {
                            this.resp = SMTPTransport.this.simpleCommand("AUTH " + this.mech);
                        }
                    }
                    if (this.resp == 334) {
                        doAuth(str, str2, str3, str4);
                    }
                    if (SMTPTransport.this.noauthdebug && SMTPTransport.this.isTracing()) {
                        MailLogger mailLogger = SMTPTransport.this.logger;
                        StringBuilder sb2 = new StringBuilder("AUTH ");
                        sb2.append(this.mech);
                        sb2.append(" ");
                        sb2.append(this.resp == 235 ? "succeeded" : "failed");
                        mailLogger.fine(sb2.toString());
                    }
                    SMTPTransport.this.resumeTracing();
                    if (this.resp == 235) {
                        return true;
                    }
                    SMTPTransport.this.closeConnection();
                    throw new AuthenticationFailedException(SMTPTransport.this.getLastServerResponse());
                } catch (Throwable th) {
                    if (SMTPTransport.this.noauthdebug && SMTPTransport.this.isTracing()) {
                        MailLogger mailLogger2 = SMTPTransport.this.logger;
                        StringBuilder sb3 = new StringBuilder("AUTH ");
                        sb3.append(this.mech);
                        sb3.append(" ");
                        if (this.resp == 235) {
                            str5 = "succeeded";
                        }
                        sb3.append(str5);
                        mailLogger2.fine(sb3.toString());
                    }
                    SMTPTransport.this.resumeTracing();
                    if (this.resp == 235) {
                        throw th;
                    }
                    SMTPTransport.this.closeConnection();
                    throw new AuthenticationFailedException(SMTPTransport.this.getLastServerResponse());
                }
            } catch (IOException e) {
                SMTPTransport.this.logger.log(Level.FINE, "AUTH " + this.mech + " failed", (Throwable) e);
                if (SMTPTransport.this.noauthdebug && SMTPTransport.this.isTracing()) {
                    MailLogger mailLogger3 = SMTPTransport.this.logger;
                    StringBuilder sb4 = new StringBuilder("AUTH ");
                    sb4.append(this.mech);
                    sb4.append(" ");
                    str5 = this.resp == 235 ? "succeeded" : "failed";
                    sb4.append(str5);
                    mailLogger3.fine(sb4.toString());
                }
                SMTPTransport.this.resumeTracing();
                if (this.resp != 235) {
                    SMTPTransport.this.closeConnection();
                    throw new AuthenticationFailedException(SMTPTransport.this.getLastServerResponse());
                }
                return true;
            } catch (Throwable th2) {
                SMTPTransport.this.logger.log(Level.FINE, "AUTH " + this.mech + " failed", th2);
                if (SMTPTransport.this.noauthdebug && SMTPTransport.this.isTracing()) {
                    MailLogger mailLogger4 = SMTPTransport.this.logger;
                    StringBuilder sb5 = new StringBuilder("AUTH ");
                    sb5.append(this.mech);
                    sb5.append(" ");
                    str5 = this.resp == 235 ? "succeeded" : "failed";
                    sb5.append(str5);
                    mailLogger4.fine(sb5.toString());
                }
                SMTPTransport.this.resumeTracing();
                if (this.resp != 235) {
                    SMTPTransport.this.closeConnection();
                    if (th2 instanceof Error) {
                        throw ((Error) th2);
                    }
                    if (th2 instanceof Exception) {
                        throw new AuthenticationFailedException(SMTPTransport.this.getLastServerResponse(), (Exception) th2);
                    }
                    throw new AuthenticationFailedException(SMTPTransport.this.getLastServerResponse());
                }
                return true;
            }
        }

        public abstract void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException;

        public boolean enabled() {
            return this.enabled;
        }

        public String getInitialResponse(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            return null;
        }

        public String getMechanism() {
            return this.mech;
        }

        public Authenticator(String str, boolean z) {
            this.mech = str.toUpperCase(Locale.ENGLISH);
            this.enabled = z;
        }
    }

    public class BDATOutputStream extends SMTPOutputStream {
        public BDATOutputStream(OutputStream outputStream, int i) {
            super(SMTPTransport.this.new ChunkedOutputStream(outputStream, i));
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            ((FilterOutputStream) this).out.close();
        }
    }

    public class DigestMD5Authenticator extends Authenticator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private DigestMD5 md5support;

        public DigestMD5Authenticator() {
            super(SMTPTransport.this, "DIGEST-MD5");
        }

        private synchronized DigestMD5 getMD5() {
            try {
                if (this.md5support == null) {
                    this.md5support = new DigestMD5(SMTPTransport.this.logger);
                }
            } catch (Throwable th) {
                throw th;
            }
            return this.md5support;
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        public void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            DigestMD5 md5 = getMD5();
            int iSimpleCommand = SMTPTransport.this.simpleCommand(md5.authClient(str, str3, str4, SMTPTransport.this.getSASLRealm(), SMTPTransport.this.getLastServerResponse()));
            this.resp = iSimpleCommand;
            if (iSimpleCommand == 334) {
                if (md5.authServer(SMTPTransport.this.getLastServerResponse())) {
                    this.resp = SMTPTransport.this.simpleCommand(new byte[0]);
                } else {
                    this.resp = -1;
                }
            }
        }
    }

    public class LoginAuthenticator extends Authenticator {
        public LoginAuthenticator() {
            super(SMTPTransport.this, "LOGIN");
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        public void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            SMTPTransport sMTPTransport = SMTPTransport.this;
            Charset charset = StandardCharsets.UTF_8;
            int iSimpleCommand = sMTPTransport.simpleCommand(BASE64EncoderStream.encode(str3.getBytes(charset)));
            this.resp = iSimpleCommand;
            if (iSimpleCommand == 334) {
                this.resp = SMTPTransport.this.simpleCommand(BASE64EncoderStream.encode(str4.getBytes(charset)));
            }
        }
    }

    public class NtlmAuthenticator extends Authenticator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private Ntlm ntlm;

        public NtlmAuthenticator() {
            super(SMTPTransport.this, "NTLM");
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        public void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            this.resp = SMTPTransport.this.simpleCommand(this.ntlm.generateType3Msg(SMTPTransport.this.getLastServerResponse().substring(4).trim()));
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        public String getInitialResponse(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            this.ntlm = new Ntlm(SMTPTransport.this.getNTLMDomain(), SMTPTransport.this.getLocalHost(), str3, str4, SMTPTransport.this.logger);
            return this.ntlm.generateType1Msg(PropUtil.getIntProperty(((Service) SMTPTransport.this).session.getProperties(), "mail." + SMTPTransport.this.name + ".auth.ntlm.flags", 0), PropUtil.getBooleanProperty(((Service) SMTPTransport.this).session.getProperties(), "mail." + SMTPTransport.this.name + ".auth.ntlm.v2", true));
        }
    }

    public class OAuth2Authenticator extends Authenticator {
        public OAuth2Authenticator() {
            super("XOAUTH2", false);
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        public void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            throw new AuthenticationFailedException("OAUTH2 asked for more");
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        public String getInitialResponse(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            return ASCIIUtility.toString(BASE64EncoderStream.encode(AbstractC4671xI0.k("user=", str3, "\u0001auth=Bearer ", str4, "\u0001\u0001").getBytes(StandardCharsets.UTF_8)));
        }
    }

    public class PlainAuthenticator extends Authenticator {
        public PlainAuthenticator() {
            super(SMTPTransport.this, "PLAIN");
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        public void doAuth(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            throw new AuthenticationFailedException("PLAIN asked for more");
        }

        @Override // com.sun.mail.smtp.SMTPTransport.Authenticator
        public String getInitialResponse(String str, String str2, String str3, String str4) throws MessagingException, IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BASE64EncoderStream bASE64EncoderStream = new BASE64EncoderStream(byteArrayOutputStream, IntCompanionObject.MAX_VALUE);
            if (str2 != null) {
                bASE64EncoderStream.write(str2.getBytes(StandardCharsets.UTF_8));
            }
            bASE64EncoderStream.write(0);
            Charset charset = StandardCharsets.UTF_8;
            bASE64EncoderStream.write(str3.getBytes(charset));
            bASE64EncoderStream.write(0);
            bASE64EncoderStream.write(str4.getBytes(charset));
            bASE64EncoderStream.flush();
            return ASCIIUtility.toString(byteArrayOutputStream.toByteArray());
        }
    }

    public SMTPTransport(Session session, URLName uRLName) {
        this(session, uRLName, "smtp", false);
    }

    private void addressesFailed() {
        Address[] addressArr = this.validSentAddr;
        if (addressArr != null) {
            Address[] addressArr2 = this.validUnsentAddr;
            if (addressArr2 == null) {
                this.validUnsentAddr = addressArr;
                this.validSentAddr = null;
                return;
            }
            Address[] addressArr3 = new Address[addressArr.length + addressArr2.length];
            System.arraycopy(addressArr, 0, addressArr3, 0, addressArr.length);
            Address[] addressArr4 = this.validUnsentAddr;
            System.arraycopy(addressArr4, 0, addressArr3, this.validSentAddr.length, addressArr4.length);
            this.validSentAddr = null;
            this.validUnsentAddr = addressArr3;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean authenticate(String str, String str2) throws MessagingException {
        SMTPTransport sMTPTransport;
        String str3;
        String str4;
        StringTokenizer stringTokenizer;
        String property = this.session.getProperty("mail." + this.name + ".auth.mechanisms");
        if (property == null) {
            property = this.defaultAuthenticationMechanisms;
        }
        String str5 = property;
        String authorizationId = getAuthorizationId();
        String str6 = authorizationId == null ? str : authorizationId;
        if (this.enableSASL) {
            this.logger.fine("Authenticate with SASL");
            try {
                sMTPTransport = this;
                str3 = str;
                str4 = str2;
            } catch (UnsupportedOperationException e) {
                e = e;
                sMTPTransport = this;
                str3 = str;
                str4 = str2;
            }
            try {
                if (sMTPTransport.sasllogin(getSASLMechanisms(), getSASLRealm(), str6, str3, str4)) {
                    return true;
                }
                sMTPTransport.logger.fine("SASL authentication failed");
                return false;
            } catch (UnsupportedOperationException e2) {
                e = e2;
                sMTPTransport.logger.log(Level.FINE, "SASL support failed", (Throwable) e);
                if (sMTPTransport.logger.isLoggable(Level.FINE)) {
                }
                stringTokenizer = new StringTokenizer(str5);
                while (stringTokenizer.hasMoreTokens()) {
                }
                throw new AuthenticationFailedException("No authentication mechanisms supported by both server and client");
            }
        }
        sMTPTransport = this;
        str3 = str;
        str4 = str2;
        if (sMTPTransport.logger.isLoggable(Level.FINE)) {
            sMTPTransport.logger.fine("Attempt to authenticate using mechanisms: " + str5);
        }
        stringTokenizer = new StringTokenizer(str5);
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            Locale locale = Locale.ENGLISH;
            String upperCase = strNextToken.toUpperCase(locale);
            Authenticator authenticator = sMTPTransport.authenticators.get(upperCase);
            if (authenticator == null) {
                sMTPTransport.logger.log(Level.FINE, "no authenticator for mechanism {0}", upperCase);
            } else {
                if (supportsAuthentication(upperCase)) {
                    if (str5 == sMTPTransport.defaultAuthenticationMechanisms) {
                        String str7 = "mail." + sMTPTransport.name + ".auth." + upperCase.toLowerCase(locale) + ".disable";
                        if (PropUtil.getBooleanProperty(sMTPTransport.session.getProperties(), str7, !authenticator.enabled())) {
                            if (sMTPTransport.logger.isLoggable(Level.FINE)) {
                                sMTPTransport.logger.fine("mechanism " + upperCase + " disabled by property: " + str7);
                            }
                        }
                    }
                    sMTPTransport.logger.log(Level.FINE, "Using mechanism {0}", upperCase);
                    return authenticator.authenticate(sMTPTransport.host, str6, str3, str4);
                }
                sMTPTransport.logger.log(Level.FINE, "mechanism {0} not supported by server", upperCase);
            }
        }
        throw new AuthenticationFailedException("No authentication mechanisms supported by both server and client");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void closeConnection() throws MessagingException {
        try {
            try {
                Socket socket = this.serverSocket;
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new MessagingException("Server Close Failed", e);
            }
        } finally {
            this.serverSocket = null;
            this.serverOutput = null;
            this.serverInput = null;
            this.lineInputStream = null;
            if (super.isConnected()) {
                super.close();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean convertTo8Bit(MimePart mimePart) {
        InputStream inputStream;
        boolean z = false;
        try {
            if (!mimePart.isMimeType("text/*")) {
                if (!mimePart.isMimeType("multipart/*")) {
                    return false;
                }
                MimeMultipart mimeMultipart = (MimeMultipart) mimePart.getContent();
                int count = mimeMultipart.getCount();
                boolean z2 = false;
                for (int i = 0; i < count; i++) {
                    try {
                        if (convertTo8Bit((MimePart) mimeMultipart.getBodyPart(i))) {
                            z2 = true;
                        }
                    } catch (IOException | MessagingException unused) {
                    }
                }
                return z2;
            }
            String encoding = mimePart.getEncoding();
            if (encoding == null) {
                return false;
            }
            if (!encoding.equalsIgnoreCase("quoted-printable") && !encoding.equalsIgnoreCase("base64")) {
                return false;
            }
            try {
                inputStream = mimePart.getInputStream();
            } catch (Throwable th) {
                th = th;
                inputStream = null;
            }
            try {
                if (is8Bit(inputStream)) {
                    mimePart.setContent(mimePart.getContent(), mimePart.getContentType());
                    mimePart.setHeader("Content-Transfer-Encoding", "8bit");
                    z = true;
                }
                if (inputStream == null) {
                    return z;
                }
                inputStream.close();
                return z;
            } catch (Throwable th2) {
                th = th2;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException unused2) {
                    }
                }
                throw th;
            }
        } catch (IOException | MessagingException unused3) {
            return false;
        }
    }

    private void expandGroups() {
        ArrayList arrayList = null;
        int i = 0;
        while (true) {
            Address[] addressArr = this.addresses;
            if (i >= addressArr.length) {
                break;
            }
            InternetAddress internetAddress = (InternetAddress) addressArr[i];
            if (internetAddress.isGroup()) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    for (int i2 = 0; i2 < i; i2++) {
                        arrayList.add(this.addresses[i2]);
                    }
                }
                try {
                    InternetAddress[] group = internetAddress.getGroup(true);
                    if (group != null) {
                        for (InternetAddress internetAddress2 : group) {
                            arrayList.add(internetAddress2);
                        }
                    } else {
                        arrayList.add(internetAddress);
                    }
                } catch (ParseException unused) {
                    arrayList.add(internetAddress);
                }
            } else if (arrayList != null) {
                arrayList.add(internetAddress);
            }
            i++;
        }
        if (arrayList != null) {
            InternetAddress[] internetAddressArr = new InternetAddress[arrayList.size()];
            arrayList.toArray(internetAddressArr);
            this.addresses = internetAddressArr;
        }
    }

    private void initStreams() throws IOException {
        boolean booleanProperty = PropUtil.getBooleanProperty(this.session.getProperties(), "mail.debug.quote", false);
        TraceInputStream traceInputStream = new TraceInputStream(this.serverSocket.getInputStream(), this.traceLogger);
        this.traceInput = traceInputStream;
        traceInputStream.setQuote(booleanProperty);
        TraceOutputStream traceOutputStream = new TraceOutputStream(this.serverSocket.getOutputStream(), this.traceLogger);
        this.traceOutput = traceOutputStream;
        traceOutputStream.setQuote(booleanProperty);
        this.serverOutput = new BufferedOutputStream(this.traceOutput);
        this.serverInput = new BufferedInputStream(this.traceInput);
        this.lineInputStream = new LineInputStream(this.serverInput);
    }

    private boolean is8Bit(InputStream inputStream) {
        boolean z = false;
        int i = 0;
        while (true) {
            try {
                int i2 = inputStream.read();
                if (i2 < 0) {
                    if (z) {
                        this.logger.fine("found an 8bit part");
                    }
                    return z;
                }
                int i3 = i2 & 255;
                if (i3 == 13 || i3 == 10) {
                    i = 0;
                } else if (i3 == 0 || (i = i + 1) > 998) {
                    return false;
                }
                if (i3 > 127) {
                    z = true;
                }
            } catch (IOException unused) {
                return false;
            }
        }
    }

    private boolean isNotLastLine(String str) {
        return str != null && str.length() >= 4 && str.charAt(3) == '-';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTracing() {
        return this.traceLogger.isLoggable(Level.FINEST);
    }

    private void issueSendCommand(String str, int i) throws MessagingException {
        sendCommand(str);
        int serverResponse = readServerResponse();
        if (serverResponse != i) {
            Address[] addressArr = this.validSentAddr;
            int length = addressArr == null ? 0 : addressArr.length;
            Address[] addressArr2 = this.validUnsentAddr;
            int length2 = addressArr2 == null ? 0 : addressArr2.length;
            Address[] addressArr3 = new Address[length + length2];
            if (length > 0) {
                System.arraycopy(addressArr, 0, addressArr3, 0, length);
            }
            if (length2 > 0) {
                System.arraycopy(this.validUnsentAddr, 0, addressArr3, length, length2);
            }
            this.validSentAddr = null;
            this.validUnsentAddr = addressArr3;
            if (this.logger.isLoggable(Level.FINE)) {
                MailLogger mailLogger = this.logger;
                StringBuilder sbR = AbstractC2039eV.r(serverResponse, "got response code ", ", with response: ");
                sbR.append(this.lastServerResponse);
                mailLogger.fine(sbR.toString());
            }
            String str2 = this.lastServerResponse;
            int i2 = this.lastReturnCode;
            if (this.serverSocket != null) {
                issueCommand("RSET", -1);
            }
            this.lastServerResponse = str2;
            this.lastReturnCode = i2;
            throw new SMTPSendFailedException(str, serverResponse, this.lastServerResponse, this.exception, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
        }
    }

    private String normalizeAddress(String str) {
        return (str.startsWith("<") || str.endsWith(">")) ? str : AbstractC4671xI0.z("<", str, ">");
    }

    private void openServer(String str, int i) throws MessagingException {
        int serverResponse;
        MailLogger mailLogger = this.logger;
        Level level = Level.FINE;
        if (mailLogger.isLoggable(level)) {
            this.logger.fine("trying to connect to host \"" + str + "\", port " + i + ", isSSL " + this.isSSL);
        }
        try {
            Socket socket = SocketFetcher.getSocket(str, i, this.session.getProperties(), "mail." + this.name, this.isSSL);
            this.serverSocket = socket;
            int port = socket.getPort();
            this.host = str;
            initStreams();
            if (readServerResponse() == 220) {
                if (this.logger.isLoggable(level)) {
                    this.logger.fine("connected to host \"" + str + "\", port: " + port);
                    return;
                }
                return;
            }
            String str2 = this.lastServerResponse;
            try {
                try {
                    if (this.quitOnSessionReject) {
                        sendCommand("QUIT");
                        if (this.quitWait && (serverResponse = readServerResponse()) != 221 && serverResponse != -1 && this.logger.isLoggable(level)) {
                            this.logger.fine("QUIT failed with " + serverResponse);
                        }
                    }
                    this.serverSocket.close();
                    this.serverSocket = null;
                    this.serverOutput = null;
                    this.serverInput = null;
                } catch (Exception e) {
                    MailLogger mailLogger2 = this.logger;
                    Level level2 = Level.FINE;
                    if (mailLogger2.isLoggable(level2)) {
                        this.logger.log(level2, "QUIT failed", (Throwable) e);
                    }
                    this.serverSocket.close();
                    this.serverSocket = null;
                    this.serverOutput = null;
                    this.serverInput = null;
                }
                this.lineInputStream = null;
                if (this.logger.isLoggable(Level.FINE)) {
                    this.logger.fine("got bad greeting from host \"" + str + "\", port: " + port + ", response: " + str2);
                }
                throw new MessagingException("Got bad greeting from SMTP host: " + str + ", port: " + port + ", response: " + str2);
            } catch (Throwable th) {
                this.serverSocket.close();
                this.serverSocket = null;
                this.serverOutput = null;
                this.serverInput = null;
                this.lineInputStream = null;
                throw th;
            }
        } catch (SocketConnectException e2) {
            throw new MailConnectException(e2);
        } catch (UnknownHostException e3) {
            throw new MessagingException(AbstractC4671xI0.y("Unknown SMTP host: ", str), e3);
        } catch (IOException e4) {
            throw new MessagingException("Could not connect to SMTP host: " + str + ", port: " + i, e4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resumeTracing() {
        if (this.traceLogger.isLoggable(Level.FINEST)) {
            this.traceInput.setTrace(true);
            this.traceOutput.setTrace(true);
        }
    }

    private boolean sasllogin(String[] strArr, String str, String str2, String str3, String str4) throws MessagingException {
        ArrayList arrayList;
        String str5;
        String canonicalHostName = this.useCanonicalHostName ? this.serverSocket.getInetAddress().getCanonicalHostName() : this.host;
        if (this.saslAuthenticator == null) {
            try {
                this.saslAuthenticator = (SaslAuthenticator) Class.forName("com.sun.mail.smtp.SMTPSaslAuthenticator").getConstructor(SMTPTransport.class, String.class, Properties.class, MailLogger.class, String.class).newInstance(this, this.name, this.session.getProperties(), this.logger, canonicalHostName);
            } catch (Exception e) {
                this.logger.log(Level.FINE, "Can't load SASL authenticator", (Throwable) e);
                return false;
            }
        }
        if (strArr == null || strArr.length <= 0) {
            arrayList = new ArrayList();
            Hashtable<String, String> hashtable = this.extMap;
            if (hashtable != null && (str5 = hashtable.get("AUTH")) != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(str5);
                while (stringTokenizer.hasMoreTokens()) {
                    arrayList.add(stringTokenizer.nextToken());
                }
            }
        } else {
            arrayList = new ArrayList(strArr.length);
            for (int i = 0; i < strArr.length; i++) {
                if (supportsAuthentication(strArr[i])) {
                    arrayList.add(strArr[i]);
                }
            }
        }
        String[] strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
        try {
            if (this.noauthdebug && isTracing()) {
                this.logger.fine("SASL AUTH command trace suppressed");
                suspendTracing();
            }
            boolean zAuthenticate = this.saslAuthenticator.authenticate(strArr2, str, str2, str3, str4);
            resumeTracing();
            return zAuthenticate;
        } catch (Throwable th) {
            resumeTracing();
            throw th;
        }
    }

    private void sendMessageEnd() {
    }

    private void sendMessageStart(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void suspendTracing() {
        if (this.traceLogger.isLoggable(Level.FINEST)) {
            this.traceInput.setTrace(false);
            this.traceOutput.setTrace(false);
        }
    }

    private byte[] toBytes(String str) {
        return this.allowutf8 ? str.getBytes(StandardCharsets.UTF_8) : ASCIIUtility.getBytes(str);
    }

    private String tracePassword(String str) {
        return this.debugpassword ? str : str == null ? "<null>" : "<non-null>";
    }

    private String traceUser(String str) {
        return this.debugusername ? str : "<user name suppressed>";
    }

    public static String xtext(String str) {
        return xtext(str, false);
    }

    public OutputStream bdat() throws MessagingException {
        BDATOutputStream bDATOutputStream = new BDATOutputStream(this.serverOutput, this.chunkSize);
        this.dataStream = bDATOutputStream;
        return bDATOutputStream;
    }

    public void checkConnected() {
        if (!super.isConnected()) {
            throw new IllegalStateException("Not connected");
        }
    }

    @Override // javax.mail.Service, java.lang.AutoCloseable
    public synchronized void close() throws MessagingException {
        int serverResponse;
        if (super.isConnected()) {
            try {
                if (this.serverSocket != null) {
                    sendCommand("QUIT");
                    if (this.quitWait && (serverResponse = readServerResponse()) != 221 && serverResponse != -1 && this.logger.isLoggable(Level.FINE)) {
                        this.logger.fine("QUIT failed with " + serverResponse);
                    }
                }
            } finally {
                closeConnection();
            }
        }
    }

    public synchronized void connect(Socket socket) throws MessagingException {
        this.serverSocket = socket;
        super.connect();
    }

    public OutputStream data() throws MessagingException {
        issueSendCommand("DATA", 354);
        SMTPOutputStream sMTPOutputStream = new SMTPOutputStream(this.serverOutput);
        this.dataStream = sMTPOutputStream;
        return sMTPOutputStream;
    }

    public boolean ehlo(String str) throws MessagingException {
        sendCommand(str != null ? "EHLO ".concat(str) : "EHLO");
        int serverResponse = readServerResponse();
        if (serverResponse == 250) {
            BufferedReader bufferedReader = new BufferedReader(new StringReader(this.lastServerResponse));
            this.extMap = new Hashtable<>();
            boolean z = true;
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (z) {
                        z = false;
                    } else if (line.length() >= 5) {
                        String strSubstring = line.substring(4);
                        int iIndexOf = strSubstring.indexOf(32);
                        String strSubstring2 = "";
                        if (iIndexOf > 0) {
                            strSubstring2 = strSubstring.substring(iIndexOf + 1);
                            strSubstring = strSubstring.substring(0, iIndexOf);
                        }
                        if (this.logger.isLoggable(Level.FINE)) {
                            this.logger.fine("Found extension \"" + strSubstring + "\", arg \"" + strSubstring2 + "\"");
                        }
                        this.extMap.put(strSubstring.toUpperCase(Locale.ENGLISH), strSubstring2);
                    }
                } catch (IOException unused) {
                }
            }
        }
        return serverResponse == 250;
    }

    @Override // javax.mail.Service
    public void finalize() throws Throwable {
        try {
            closeConnection();
        } catch (MessagingException unused) {
        } finally {
            super.finalize();
        }
    }

    public void finishBdat() throws MessagingException, IOException {
        this.dataStream.ensureAtBOL();
        this.dataStream.close();
    }

    public void finishData() throws MessagingException, IOException {
        this.dataStream.ensureAtBOL();
        issueSendCommand(".", 250);
    }

    public synchronized String getAuthorizationId() {
        try {
            if (this.authorizationID == UNKNOWN) {
                this.authorizationID = this.session.getProperty("mail." + this.name + ".sasl.authorizationid");
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.authorizationID;
    }

    public String getExtensionParameter(String str) {
        Hashtable<String, String> hashtable = this.extMap;
        if (hashtable == null) {
            return null;
        }
        return hashtable.get(str.toUpperCase(Locale.ENGLISH));
    }

    public synchronized int getLastReturnCode() {
        return this.lastReturnCode;
    }

    public synchronized String getLastServerResponse() {
        return this.lastServerResponse;
    }

    public synchronized String getLocalHost() {
        Socket socket;
        try {
            String str = this.localHostName;
            if (str == null || str.length() <= 0) {
                this.localHostName = this.session.getProperty("mail." + this.name + ".localhost");
            }
            String str2 = this.localHostName;
            if (str2 == null || str2.length() <= 0) {
                this.localHostName = this.session.getProperty("mail." + this.name + ".localaddress");
            }
            try {
                String str3 = this.localHostName;
                if (str3 == null || str3.length() <= 0) {
                    InetAddress localHost = InetAddress.getLocalHost();
                    String canonicalHostName = localHost.getCanonicalHostName();
                    this.localHostName = canonicalHostName;
                    if (canonicalHostName == null) {
                        this.localHostName = "[" + localHost.getHostAddress() + "]";
                    }
                }
            } catch (UnknownHostException unused) {
            }
            String str4 = this.localHostName;
            if ((str4 == null || str4.length() <= 0) && (socket = this.serverSocket) != null && socket.isBound()) {
                InetAddress localAddress = this.serverSocket.getLocalAddress();
                String canonicalHostName2 = localAddress.getCanonicalHostName();
                this.localHostName = canonicalHostName2;
                if (canonicalHostName2 == null) {
                    this.localHostName = "[" + localAddress.getHostAddress() + "]";
                }
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.localHostName;
    }

    public synchronized String getNTLMDomain() {
        try {
            if (this.ntlmDomain == UNKNOWN) {
                this.ntlmDomain = this.session.getProperty("mail." + this.name + ".auth.ntlm.domain");
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.ntlmDomain;
    }

    public synchronized boolean getNoopStrict() {
        return this.noopStrict;
    }

    public synchronized boolean getReportSuccess() {
        return this.reportSuccess;
    }

    public synchronized boolean getRequireStartTLS() {
        return this.requireStartTLS;
    }

    public synchronized boolean getSASLEnabled() {
        return this.enableSASL;
    }

    public synchronized String[] getSASLMechanisms() {
        try {
            if (this.saslMechanisms == UNKNOWN_SA) {
                ArrayList arrayList = new ArrayList(5);
                String property = this.session.getProperty("mail." + this.name + ".sasl.mechanisms");
                if (property != null && property.length() > 0) {
                    if (this.logger.isLoggable(Level.FINE)) {
                        this.logger.fine("SASL mechanisms allowed: ".concat(property));
                    }
                    StringTokenizer stringTokenizer = new StringTokenizer(property, " ,");
                    while (stringTokenizer.hasMoreTokens()) {
                        String strNextToken = stringTokenizer.nextToken();
                        if (strNextToken.length() > 0) {
                            arrayList.add(strNextToken);
                        }
                    }
                }
                String[] strArr = new String[arrayList.size()];
                this.saslMechanisms = strArr;
                arrayList.toArray(strArr);
            }
            String[] strArr2 = this.saslMechanisms;
            if (strArr2 == null) {
                return null;
            }
            return (String[]) strArr2.clone();
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized String getSASLRealm() {
        try {
            if (this.saslRealm == UNKNOWN) {
                String property = this.session.getProperty("mail." + this.name + ".sasl.realm");
                this.saslRealm = property;
                if (property == null) {
                    this.saslRealm = this.session.getProperty("mail." + this.name + ".saslrealm");
                }
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.saslRealm;
    }

    public synchronized boolean getStartTLS() {
        return this.useStartTLS;
    }

    public synchronized boolean getUseCanonicalHostName() {
        return this.useCanonicalHostName;
    }

    public synchronized boolean getUseRset() {
        return this.useRset;
    }

    public void helo(String str) throws MessagingException {
        if (str != null) {
            issueCommand("HELO ".concat(str), 250);
        } else {
            issueCommand("HELO", 250);
        }
    }

    @Override // javax.mail.Service
    public synchronized boolean isConnected() {
        try {
            if (!super.isConnected()) {
                return false;
            }
            try {
                try {
                    if (this.useRset) {
                        sendCommand("RSET");
                    } else {
                        sendCommand("NOOP");
                    }
                    int serverResponse = readServerResponse();
                    if (serverResponse >= 0 && (!this.noopStrict ? serverResponse == 421 : serverResponse != 250)) {
                        return true;
                    }
                    try {
                        closeConnection();
                    } catch (MessagingException unused) {
                    }
                    return false;
                } catch (MessagingException unused2) {
                }
            } catch (Exception unused3) {
                closeConnection();
            }
            return false;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized boolean isSSL() {
        return this.serverSocket instanceof SSLSocket;
    }

    public synchronized void issueCommand(String str, int i) throws MessagingException {
        sendCommand(str);
        int serverResponse = readServerResponse();
        if (i != -1 && serverResponse != i) {
            throw new MessagingException(this.lastServerResponse);
        }
    }

    public void mailFrom() throws MessagingException {
        Address[] from;
        MimeMessage mimeMessage = this.message;
        String envelopeFrom = mimeMessage instanceof SMTPMessage ? ((SMTPMessage) mimeMessage).getEnvelopeFrom() : null;
        if (envelopeFrom == null || envelopeFrom.length() <= 0) {
            envelopeFrom = this.session.getProperty("mail." + this.name + ".from");
        }
        boolean z = false;
        if (envelopeFrom == null || envelopeFrom.length() <= 0) {
            MimeMessage mimeMessage2 = this.message;
            Address localAddress = (mimeMessage2 == null || (from = mimeMessage2.getFrom()) == null || from.length <= 0) ? InternetAddress.getLocalAddress(this.session) : from[0];
            if (localAddress == null) {
                throw new MessagingException("can't determine local email address");
            }
            envelopeFrom = ((InternetAddress) localAddress).getAddress();
        }
        String strJ = "MAIL FROM:" + normalizeAddress(envelopeFrom);
        if (this.allowutf8 && supportsExtension("SMTPUTF8")) {
            strJ = AbstractC4671xI0.i(strJ, " SMTPUTF8");
        }
        if (supportsExtension("DSN")) {
            MimeMessage mimeMessage3 = this.message;
            String dSNRet = mimeMessage3 instanceof SMTPMessage ? ((SMTPMessage) mimeMessage3).getDSNRet() : null;
            if (dSNRet == null) {
                dSNRet = this.session.getProperty("mail." + this.name + ".dsn.ret");
            }
            if (dSNRet != null) {
                strJ = AbstractC4671xI0.j(strJ, " RET=", dSNRet);
            }
        }
        if (supportsExtension("AUTH")) {
            MimeMessage mimeMessage4 = this.message;
            String submitter = mimeMessage4 instanceof SMTPMessage ? ((SMTPMessage) mimeMessage4).getSubmitter() : null;
            if (submitter == null) {
                submitter = this.session.getProperty("mail." + this.name + ".submitter");
            }
            if (submitter != null) {
                try {
                    if (this.allowutf8 && supportsExtension("SMTPUTF8")) {
                        z = true;
                    }
                    strJ = strJ + " AUTH=" + xtext(submitter, z);
                } catch (IllegalArgumentException e) {
                    MailLogger mailLogger = this.logger;
                    Level level = Level.FINE;
                    if (mailLogger.isLoggable(level)) {
                        this.logger.log(level, "ignoring invalid submitter: ".concat(submitter), (Throwable) e);
                    }
                }
            }
        }
        MimeMessage mimeMessage5 = this.message;
        String mailExtension = mimeMessage5 instanceof SMTPMessage ? ((SMTPMessage) mimeMessage5).getMailExtension() : null;
        if (mailExtension == null) {
            mailExtension = this.session.getProperty("mail." + this.name + ".mailextension");
        }
        if (mailExtension != null && mailExtension.length() > 0) {
            strJ = AbstractC4671xI0.j(strJ, " ", mailExtension);
        }
        try {
            issueSendCommand(strJ, 250);
        } catch (SMTPSendFailedException e2) {
            int returnCode = e2.getReturnCode();
            if (returnCode == 501 || returnCode == 503 || returnCode == 553 || returnCode == 550 || returnCode == 551) {
                try {
                    e2.setNextException(new SMTPSenderFailedException(new InternetAddress(envelopeFrom), strJ, returnCode, e2.getMessage()));
                } catch (AddressException unused) {
                }
            }
            throw e2;
        }
    }

    @Override // javax.mail.Transport
    public void notifyTransportListeners(int i, Address[] addressArr, Address[] addressArr2, Address[] addressArr3, Message message) {
        if (this.notificationDone) {
            return;
        }
        super.notifyTransportListeners(i, addressArr, addressArr2, addressArr3, message);
        this.notificationDone = true;
    }

    @Override // javax.mail.Service
    public synchronized boolean protocolConnect(String str, int i, String str2, String str3) throws MessagingException {
        Properties properties = this.session.getProperties();
        boolean booleanProperty = PropUtil.getBooleanProperty(properties, "mail." + this.name + ".auth", false);
        if (booleanProperty && (str2 == null || str3 == null)) {
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("need username and password for authentication");
                this.logger.fine("protocolConnect returning false, host=" + str + ", user=" + traceUser(str2) + ", password=" + tracePassword(str3));
            }
            return false;
        }
        boolean booleanProperty2 = PropUtil.getBooleanProperty(properties, "mail." + this.name + ".ehlo", true);
        MailLogger mailLogger = this.logger;
        Level level = Level.FINE;
        if (mailLogger.isLoggable(level)) {
            this.logger.fine("useEhlo " + booleanProperty2 + ", useAuth " + booleanProperty);
        }
        if (i == -1) {
            i = PropUtil.getIntProperty(properties, "mail." + this.name + ".port", -1);
        }
        if (i == -1) {
            i = this.defaultPort;
        }
        if (str == null || str.length() == 0) {
            str = "localhost";
        }
        try {
            if (this.serverSocket != null) {
                openServer();
            } else {
                openServer(str, i);
            }
            if (!(booleanProperty2 ? ehlo(getLocalHost()) : false)) {
                helo(getLocalHost());
            }
            if (this.useStartTLS || this.requireStartTLS) {
                if (this.serverSocket instanceof SSLSocket) {
                    this.logger.fine("STARTTLS requested but already using SSL");
                } else if (supportsExtension("STARTTLS")) {
                    startTLS();
                    ehlo(getLocalHost());
                } else if (this.requireStartTLS) {
                    this.logger.fine("STARTTLS required but not supported");
                    throw new MessagingException("STARTTLS is required but host does not support STARTTLS");
                }
            }
            if (this.allowutf8 && !supportsExtension("SMTPUTF8")) {
                this.logger.log(Level.INFO, "mail.mime.allowutf8 set but server doesn't advertise SMTPUTF8 support");
            }
            if ((!booleanProperty && (str2 == null || str3 == null)) || (!supportsExtension("AUTH") && !supportsExtension("AUTH=LOGIN"))) {
                return true;
            }
            if (this.logger.isLoggable(level)) {
                this.logger.fine("protocolConnect login, host=" + str + ", user=" + traceUser(str2) + ", password=" + tracePassword(str3));
            }
            boolean zAuthenticate = authenticate(str2, str3);
            if (!zAuthenticate) {
                try {
                    closeConnection();
                } catch (MessagingException unused) {
                }
            }
            return zAuthenticate;
        } catch (Throwable th) {
            try {
                closeConnection();
            } catch (MessagingException unused2) {
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:156:0x0176 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00f7 A[PHI: r9 r10
      0x00f7: PHI (r9v3 java.lang.Exception) = (r9v2 java.lang.Exception), (r9v6 java.lang.Exception), (r9v8 java.lang.Exception), (r9v12 java.lang.Exception) binds: [B:79:0x016b, B:72:0x0155, B:66:0x0141, B:52:0x00f5] A[DONT_GENERATE, DONT_INLINE]
      0x00f7: PHI (r10v6 boolean) = (r10v1 boolean), (r10v7 boolean), (r10v9 boolean), (r10v11 boolean) binds: [B:79:0x016b, B:72:0x0155, B:66:0x0141, B:52:0x00f5] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0148  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void rcptTo() throws MessagingException {
        String dSNNotify;
        int i;
        Address[] addressArr;
        InternetAddress internetAddress;
        String strJ;
        int serverResponse;
        Exception sMTPAddressSucceededException;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        this.invalidAddr = null;
        this.validUnsentAddr = null;
        this.validSentAddr = null;
        MimeMessage mimeMessage = this.message;
        boolean sendPartial = mimeMessage instanceof SMTPMessage ? ((SMTPMessage) mimeMessage).getSendPartial() : false;
        if (!sendPartial) {
            sendPartial = PropUtil.getBooleanProperty(this.session.getProperties(), "mail." + this.name + ".sendpartial", false);
        }
        if (sendPartial) {
            this.logger.fine("sendPartial set");
        }
        if (supportsExtension("DSN")) {
            MimeMessage mimeMessage2 = this.message;
            dSNNotify = mimeMessage2 instanceof SMTPMessage ? ((SMTPMessage) mimeMessage2).getDSNNotify() : null;
            if (dSNNotify == null) {
                dSNNotify = this.session.getProperty("mail." + this.name + ".dsn.notify");
            }
            boolean z = dSNNotify != null;
            MessagingException messagingException = null;
            i = 0;
            boolean z2 = false;
            while (true) {
                addressArr = this.addresses;
                if (i < addressArr.length) {
                    if (sendPartial && arrayList.size() == 0) {
                        z2 = true;
                    }
                    if (z2) {
                        Address[] addressArr2 = new Address[arrayList3.size()];
                        this.invalidAddr = addressArr2;
                        arrayList3.toArray(addressArr2);
                        this.validUnsentAddr = new Address[arrayList2.size() + arrayList.size()];
                        int i2 = 0;
                        int i3 = 0;
                        while (i2 < arrayList.size()) {
                            this.validUnsentAddr[i3] = (Address) arrayList.get(i2);
                            i2++;
                            i3++;
                        }
                        int i4 = 0;
                        while (i4 < arrayList2.size()) {
                            this.validUnsentAddr[i3] = (Address) arrayList2.get(i4);
                            i4++;
                            i3++;
                        }
                    } else if (this.reportSuccess || (sendPartial && (arrayList3.size() > 0 || arrayList2.size() > 0))) {
                        this.sendPartiallyFailed = true;
                        this.exception = messagingException;
                        Address[] addressArr3 = new Address[arrayList3.size()];
                        this.invalidAddr = addressArr3;
                        arrayList3.toArray(addressArr3);
                        Address[] addressArr4 = new Address[arrayList2.size()];
                        this.validUnsentAddr = addressArr4;
                        arrayList2.toArray(addressArr4);
                        Address[] addressArr5 = new Address[arrayList.size()];
                        this.validSentAddr = addressArr5;
                        arrayList.toArray(addressArr5);
                    } else {
                        this.validSentAddr = this.addresses;
                    }
                    if (this.logger.isLoggable(Level.FINE)) {
                        Address[] addressArr6 = this.validSentAddr;
                        if (addressArr6 != null && addressArr6.length > 0) {
                            this.logger.fine("Verified Addresses");
                            for (int i5 = 0; i5 < this.validSentAddr.length; i5++) {
                                this.logger.fine("  " + this.validSentAddr[i5]);
                            }
                        }
                        Address[] addressArr7 = this.validUnsentAddr;
                        if (addressArr7 != null && addressArr7.length > 0) {
                            this.logger.fine("Valid Unsent Addresses");
                            for (int i6 = 0; i6 < this.validUnsentAddr.length; i6++) {
                                this.logger.fine("  " + this.validUnsentAddr[i6]);
                            }
                        }
                        Address[] addressArr8 = this.invalidAddr;
                        if (addressArr8 != null && addressArr8.length > 0) {
                            this.logger.fine("Invalid Addresses");
                            for (int i7 = 0; i7 < this.invalidAddr.length; i7++) {
                                this.logger.fine("  " + this.invalidAddr[i7]);
                            }
                        }
                    }
                    if (z2) {
                        this.logger.fine("Sending failed because of invalid destination addresses");
                        notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                        String str = this.lastServerResponse;
                        int i8 = this.lastReturnCode;
                        try {
                            try {
                                try {
                                    if (this.serverSocket != null) {
                                        issueCommand("RSET", -1);
                                    }
                                } catch (MessagingException unused) {
                                    close();
                                }
                            } catch (MessagingException e) {
                                this.logger.log(Level.FINE, "close failed", (Throwable) e);
                            }
                            throw new SendFailedException("Invalid Addresses", messagingException, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
                        } finally {
                            this.lastServerResponse = str;
                            this.lastReturnCode = i8;
                        }
                    }
                    return;
                }
                internetAddress = (InternetAddress) addressArr[i];
                strJ = "RCPT TO:" + normalizeAddress(internetAddress.getAddress());
                if (z) {
                    strJ = AbstractC4671xI0.j(strJ, " NOTIFY=", dSNNotify);
                }
                sendCommand(strJ);
                serverResponse = readServerResponse();
                if (serverResponse == 250 || serverResponse == 251) {
                    arrayList.add(internetAddress);
                    if (this.reportSuccess) {
                        sMTPAddressSucceededException = new SMTPAddressSucceededException(internetAddress, strJ, serverResponse, this.lastServerResponse);
                        if (messagingException == null) {
                            messagingException = sMTPAddressSucceededException;
                        } else {
                            messagingException.setNextException(sMTPAddressSucceededException);
                        }
                    }
                } else if (serverResponse != 501 && serverResponse != 503) {
                    switch (serverResponse) {
                        default:
                            switch (serverResponse) {
                                case 550:
                                case 551:
                                case 553:
                                    break;
                                case 552:
                                    break;
                                default:
                                    if (serverResponse >= 400 && serverResponse <= 499) {
                                        arrayList2.add(internetAddress);
                                    } else if (serverResponse >= 500 && serverResponse <= 599) {
                                        arrayList3.add(internetAddress);
                                    }
                                    if (!sendPartial) {
                                        z2 = true;
                                    }
                                    sMTPAddressSucceededException = new SMTPAddressFailedException(internetAddress, strJ, serverResponse, this.lastServerResponse);
                                    if (messagingException != null) {
                                        messagingException.setNextException(sMTPAddressSucceededException);
                                        break;
                                    }
                                    break;
                            }
                        case 450:
                        case 451:
                        case 452:
                            if (!sendPartial) {
                                z2 = true;
                            }
                            arrayList2.add(internetAddress);
                            sMTPAddressSucceededException = new SMTPAddressFailedException(internetAddress, strJ, serverResponse, this.lastServerResponse);
                            if (messagingException != null) {
                                messagingException.setNextException(sMTPAddressSucceededException);
                                break;
                            }
                            break;
                    }
                } else {
                    if (!sendPartial) {
                        z2 = true;
                    }
                    arrayList3.add(internetAddress);
                    sMTPAddressSucceededException = new SMTPAddressFailedException(internetAddress, strJ, serverResponse, this.lastServerResponse);
                    if (messagingException != null) {
                        messagingException.setNextException(sMTPAddressSucceededException);
                    }
                }
                i++;
            }
            if (this.logger.isLoggable(Level.FINE)) {
                MailLogger mailLogger = this.logger;
                StringBuilder sbR = AbstractC2039eV.r(serverResponse, "got response code ", ", with response: ");
                sbR.append(this.lastServerResponse);
                mailLogger.fine(sbR.toString());
            }
            String str2 = this.lastServerResponse;
            int i9 = this.lastReturnCode;
            if (this.serverSocket != null) {
                issueCommand("RSET", -1);
            }
            this.lastServerResponse = str2;
            this.lastReturnCode = i9;
            throw new SMTPAddressFailedException(internetAddress, strJ, serverResponse, str2);
        }
        dSNNotify = null;
        MessagingException messagingException2 = null;
        i = 0;
        boolean z22 = false;
        while (true) {
            addressArr = this.addresses;
            if (i < addressArr.length) {
            }
            i++;
        }
        if (this.logger.isLoggable(Level.FINE)) {
        }
        String str22 = this.lastServerResponse;
        int i92 = this.lastReturnCode;
        if (this.serverSocket != null) {
        }
        this.lastServerResponse = str22;
        this.lastReturnCode = i92;
        throw new SMTPAddressFailedException(internetAddress, strJ, serverResponse, str22);
    }

    public int readServerResponse() throws MessagingException {
        String line;
        int i;
        StringBuilder sb = new StringBuilder(100);
        do {
            try {
                line = this.lineInputStream.readLine();
                if (line == null) {
                    String string = sb.toString();
                    if (string.length() == 0) {
                        string = "[EOF]";
                    }
                    this.lastServerResponse = string;
                    this.lastReturnCode = -1;
                    this.logger.log(Level.FINE, "EOF: {0}", string);
                    return -1;
                }
                sb.append(line);
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
            } catch (IOException e) {
                this.logger.log(Level.FINE, "exception reading response", (Throwable) e);
                this.lastServerResponse = "";
                this.lastReturnCode = 0;
                throw new MessagingException("Exception reading response", e);
            }
        } while (isNotLastLine(line));
        String string2 = sb.toString();
        if (string2.length() >= 3) {
            try {
                try {
                    try {
                        i = Integer.parseInt(string2.substring(0, 3));
                    } catch (MessagingException e2) {
                        this.logger.log(Level.FINE, "close failed", (Throwable) e2);
                    }
                } catch (MessagingException e3) {
                    this.logger.log(Level.FINE, "close failed", (Throwable) e3);
                }
            } catch (NumberFormatException unused) {
                close();
            } catch (StringIndexOutOfBoundsException unused2) {
                close();
            }
        } else {
            i = -1;
        }
        if (i == -1) {
            this.logger.log(Level.FINE, "bad server response: {0}", string2);
        }
        this.lastServerResponse = string2;
        this.lastReturnCode = i;
        return i;
    }

    public void sendCommand(String str) throws MessagingException {
        sendCommand(toBytes(str));
    }

    @Override // javax.mail.Transport
    public synchronized void sendMessage(Message message, Address[] addressArr) throws MessagingException {
        String subject;
        if (message != null) {
            try {
                subject = message.getSubject();
            } catch (Throwable th) {
                throw th;
            }
        } else {
            subject = "";
        }
        sendMessageStart(subject);
        checkConnected();
        if (!(message instanceof MimeMessage)) {
            this.logger.fine("Can only send RFC822 msgs");
            throw new MessagingException("SMTP can only send RFC822 messages");
        }
        if (addressArr == null || addressArr.length == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        for (int i = 0; i < addressArr.length; i++) {
            if (!(addressArr[i] instanceof InternetAddress)) {
                throw new MessagingException(addressArr[i] + " is not an InternetAddress");
            }
        }
        this.message = (MimeMessage) message;
        this.addresses = addressArr;
        this.validUnsentAddr = addressArr;
        expandGroups();
        boolean allow8bitMIME = message instanceof SMTPMessage ? ((SMTPMessage) message).getAllow8bitMIME() : false;
        if (!allow8bitMIME) {
            allow8bitMIME = PropUtil.getBooleanProperty(this.session.getProperties(), "mail." + this.name + ".allow8bitmime", false);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("use8bit " + allow8bitMIME);
        }
        if (allow8bitMIME && supportsExtension("8BITMIME") && convertTo8Bit(this.message)) {
            try {
                this.message.saveChanges();
            } catch (MessagingException unused) {
            }
        }
        try {
            try {
                mailFrom();
                rcptTo();
                if (this.chunkSize <= 0 || !supportsExtension("CHUNKING")) {
                    this.message.writeTo(data(), ignoreList);
                    finishData();
                } else {
                    this.message.writeTo(bdat(), ignoreList);
                    finishBdat();
                }
                if (this.sendPartiallyFailed) {
                    this.logger.fine("Sending partially failed because of invalid destination addresses");
                    notifyTransportListeners(3, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                    throw new SMTPSendFailedException(".", this.lastReturnCode, this.lastServerResponse, this.exception, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
                }
                this.logger.fine("message successfully delivered to mail server");
                notifyTransportListeners(1, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                this.invalidAddr = null;
                this.validUnsentAddr = null;
                this.validSentAddr = null;
                this.addresses = null;
                this.message = null;
                this.exception = null;
                this.sendPartiallyFailed = false;
                this.notificationDone = false;
                sendMessageEnd();
            } finally {
                this.invalidAddr = null;
                this.validUnsentAddr = null;
                this.validSentAddr = null;
                this.addresses = null;
                this.message = null;
                this.exception = null;
                this.sendPartiallyFailed = false;
                this.notificationDone = false;
            }
        } catch (IOException e) {
            this.logger.log(Level.FINE, "IOException while sending, closing", (Throwable) e);
            try {
                closeConnection();
            } catch (MessagingException unused2) {
            }
            addressesFailed();
            notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
            throw new MessagingException("IOException while sending message", e);
        } catch (MessagingException e2) {
            this.logger.log(Level.FINE, "MessagingException while sending", (Throwable) e2);
            if (e2.getNextException() instanceof IOException) {
                this.logger.fine("nested IOException, closing");
                try {
                    closeConnection();
                } catch (MessagingException unused3) {
                }
            }
            addressesFailed();
            notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
            throw e2;
        }
    }

    public synchronized void setAuthorizationID(String str) {
        this.authorizationID = str;
    }

    public synchronized void setLocalHost(String str) {
        this.localHostName = str;
    }

    public synchronized void setNTLMDomain(String str) {
        this.ntlmDomain = str;
    }

    public synchronized void setNoopStrict(boolean z) {
        this.noopStrict = z;
    }

    public synchronized void setReportSuccess(boolean z) {
        this.reportSuccess = z;
    }

    public synchronized void setRequireStartTLS(boolean z) {
        this.requireStartTLS = z;
    }

    public synchronized void setSASLEnabled(boolean z) {
        this.enableSASL = z;
    }

    public synchronized void setSASLMechanisms(String[] strArr) {
        if (strArr != null) {
            try {
                strArr = (String[]) strArr.clone();
            } catch (Throwable th) {
                throw th;
            }
        }
        this.saslMechanisms = strArr;
    }

    public synchronized void setSASLRealm(String str) {
        this.saslRealm = str;
    }

    public synchronized void setStartTLS(boolean z) {
        this.useStartTLS = z;
    }

    public synchronized void setUseCanonicalHostName(boolean z) {
        this.useCanonicalHostName = z;
    }

    public synchronized void setUseRset(boolean z) {
        this.useRset = z;
    }

    public synchronized int simpleCommand(String str) throws MessagingException {
        sendCommand(str);
        return readServerResponse();
    }

    public void startTLS() throws MessagingException {
        issueCommand("STARTTLS", 220);
        try {
            this.serverSocket = SocketFetcher.startTLS(this.serverSocket, this.host, this.session.getProperties(), "mail." + this.name);
            initStreams();
        } catch (IOException e) {
            closeConnection();
            throw new MessagingException("Could not convert socket to TLS", e);
        }
    }

    public boolean supportsAuthentication(String str) {
        String str2;
        Hashtable<String, String> hashtable = this.extMap;
        if (hashtable == null || (str2 = hashtable.get("AUTH")) == null) {
            return false;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str2);
        while (stringTokenizer.hasMoreTokens()) {
            if (stringTokenizer.nextToken().equalsIgnoreCase(str)) {
                return true;
            }
        }
        if (!str.equalsIgnoreCase("LOGIN") || !supportsExtension("AUTH=LOGIN")) {
            return false;
        }
        this.logger.fine("use AUTH=LOGIN hack");
        return true;
    }

    public boolean supportsExtension(String str) {
        Hashtable<String, String> hashtable = this.extMap;
        return (hashtable == null || hashtable.get(str.toUpperCase(Locale.ENGLISH)) == null) ? false : true;
    }

    public SMTPTransport(Session session, URLName uRLName, String str, boolean z) {
        super(session, uRLName);
        this.name = "smtp";
        this.defaultPort = 25;
        this.isSSL = false;
        this.sendPartiallyFailed = false;
        this.authenticators = new HashMap();
        this.quitWait = false;
        this.quitOnSessionReject = false;
        this.saslRealm = UNKNOWN;
        this.authorizationID = UNKNOWN;
        this.enableSASL = false;
        this.useCanonicalHostName = false;
        this.saslMechanisms = UNKNOWN_SA;
        this.ntlmDomain = UNKNOWN;
        this.noopStrict = true;
        this.noauthdebug = true;
        Properties properties = session.getProperties();
        MailLogger mailLogger = new MailLogger(getClass(), "DEBUG SMTP", session.getDebug(), session.getDebugOut());
        this.logger = mailLogger;
        this.traceLogger = mailLogger.getSubLogger("protocol", null);
        this.noauthdebug = !PropUtil.getBooleanProperty(properties, "mail.debug.auth", false);
        this.debugusername = PropUtil.getBooleanProperty(properties, "mail.debug.auth.username", true);
        this.debugpassword = PropUtil.getBooleanProperty(properties, "mail.debug.auth.password", false);
        str = uRLName != null ? uRLName.getProtocol() : str;
        this.name = str;
        z = z ? z : AbstractC2039eV.A("mail.", str, ".ssl.enable", properties, false);
        if (z) {
            this.defaultPort = 465;
        } else {
            this.defaultPort = 25;
        }
        this.isSSL = z;
        this.quitWait = AbstractC2039eV.A("mail.", str, ".quitwait", properties, true);
        this.quitOnSessionReject = AbstractC2039eV.A("mail.", str, ".quitonsessionreject", properties, false);
        this.reportSuccess = AbstractC2039eV.A("mail.", str, ".reportsuccess", properties, false);
        this.useStartTLS = AbstractC2039eV.A("mail.", str, ".starttls.enable", properties, false);
        this.requireStartTLS = AbstractC2039eV.A("mail.", str, ".starttls.required", properties, false);
        this.useRset = AbstractC2039eV.A("mail.", str, ".userset", properties, false);
        this.noopStrict = AbstractC2039eV.A("mail.", str, ".noop.strict", properties, true);
        boolean zA = AbstractC2039eV.A("mail.", str, ".sasl.enable", properties, false);
        this.enableSASL = zA;
        if (zA) {
            this.logger.config("enable SASL");
        }
        boolean zA2 = AbstractC2039eV.A("mail.", str, ".sasl.usecanonicalhostname", properties, false);
        this.useCanonicalHostName = zA2;
        if (zA2) {
            this.logger.config("use canonical host name");
        }
        boolean booleanProperty = PropUtil.getBooleanProperty(properties, "mail.mime.allowutf8", false);
        this.allowutf8 = booleanProperty;
        if (booleanProperty) {
            this.logger.config("allow UTF-8");
        }
        int intProperty = PropUtil.getIntProperty(properties, "mail." + str + ".chunksize", -1);
        this.chunkSize = intProperty;
        if (intProperty > 0 && this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config("chunk size " + this.chunkSize);
        }
        Authenticator[] authenticatorArr = {new LoginAuthenticator(), new PlainAuthenticator(), new DigestMD5Authenticator(), new NtlmAuthenticator(), new OAuth2Authenticator()};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            this.authenticators.put(authenticatorArr[i].getMechanism(), authenticatorArr[i]);
            sb.append(authenticatorArr[i].getMechanism());
            sb.append(' ');
        }
        this.defaultAuthenticationMechanisms = sb.toString();
    }

    private void sendCommand(byte[] bArr) throws MessagingException {
        try {
            this.serverOutput.write(bArr);
            this.serverOutput.write(CRLF);
            this.serverOutput.flush();
        } catch (IOException e) {
            throw new MessagingException("Can't send command to SMTP host", e);
        }
    }

    public static String xtext(String str, boolean z) {
        byte[] bytes = z ? str.getBytes(StandardCharsets.UTF_8) : ASCIIUtility.getBytes(str);
        StringBuilder sb = null;
        for (int i = 0; i < bytes.length; i++) {
            char c = (char) (bytes[i] & UnsignedBytes.MAX_VALUE);
            if (!z && c >= 128) {
                throw new IllegalArgumentException(AbstractC4671xI0.y("Non-ASCII character in SMTP submitter: ", str));
            }
            if (c < '!' || c > '~' || c == '+' || c == '=') {
                if (sb == null) {
                    sb = new StringBuilder(str.length() + 4);
                    sb.append(str.substring(0, i));
                }
                sb.append('+');
                sb.append(hexchar[(c & 240) >> 4]);
                sb.append(hexchar[c & 15]);
            } else if (sb != null) {
                sb.append(c);
            }
        }
        return sb != null ? sb.toString() : str;
    }

    public class ChunkedOutputStream extends OutputStream {
        private final byte[] buf;
        private int count = 0;
        private final OutputStream out;

        public ChunkedOutputStream(OutputStream outputStream, int i) {
            this.out = outputStream;
            this.buf = new byte[i];
        }

        private void bdat(byte[] bArr, int i, int i2, boolean z) throws IOException {
            if (i2 > 0 || z) {
                try {
                    if (z) {
                        SMTPTransport.this.sendCommand("BDAT " + i2 + " LAST");
                    } else {
                        SMTPTransport.this.sendCommand("BDAT " + i2);
                    }
                    this.out.write(bArr, i, i2);
                    this.out.flush();
                    if (SMTPTransport.this.readServerResponse() == 250) {
                    } else {
                        throw new IOException(SMTPTransport.this.lastServerResponse);
                    }
                } catch (MessagingException e) {
                    throw new IOException("BDAT write exception", e);
                }
            }
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            bdat(this.buf, 0, this.count, true);
            this.count = 0;
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            bdat(this.buf, 0, this.count, false);
            this.count = 0;
        }

        @Override // java.io.OutputStream
        public void write(int i) throws IOException {
            byte[] bArr = this.buf;
            int i2 = this.count;
            int i3 = i2 + 1;
            this.count = i3;
            bArr[i2] = (byte) i;
            if (i3 >= bArr.length) {
                flush();
            }
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i, int i2) throws IOException {
            while (i2 > 0) {
                int iMin = Math.min(this.buf.length - this.count, i2);
                byte[] bArr2 = this.buf;
                if (iMin == bArr2.length) {
                    bdat(bArr, i, iMin, false);
                } else {
                    System.arraycopy(bArr, i, bArr2, this.count, iMin);
                    this.count += iMin;
                }
                i += iMin;
                i2 -= iMin;
                if (this.count >= this.buf.length) {
                    flush();
                }
            }
        }
    }

    public int simpleCommand(byte[] bArr) throws MessagingException {
        sendCommand(bArr);
        return readServerResponse();
    }

    private void openServer() throws MessagingException {
        int serverResponse;
        this.host = UNKNOWN;
        int i = -1;
        try {
            int port = this.serverSocket.getPort();
            try {
                this.host = this.serverSocket.getInetAddress().getHostName();
                MailLogger mailLogger = this.logger;
                Level level = Level.FINE;
                if (mailLogger.isLoggable(level)) {
                    this.logger.fine("starting protocol to host \"" + this.host + "\", port " + port);
                }
                initStreams();
                int serverResponse2 = readServerResponse();
                if (serverResponse2 != 220) {
                    try {
                        try {
                            if (this.quitOnSessionReject) {
                                sendCommand("QUIT");
                                if (this.quitWait && (serverResponse = readServerResponse()) != 221 && serverResponse != -1 && this.logger.isLoggable(level)) {
                                    this.logger.fine("QUIT failed with " + serverResponse);
                                }
                            }
                            this.serverSocket.close();
                            this.serverSocket = null;
                            this.serverOutput = null;
                            this.serverInput = null;
                        } catch (Exception e) {
                            MailLogger mailLogger2 = this.logger;
                            Level level2 = Level.FINE;
                            if (mailLogger2.isLoggable(level2)) {
                                this.logger.log(level2, "QUIT failed", (Throwable) e);
                            }
                            this.serverSocket.close();
                            this.serverSocket = null;
                            this.serverOutput = null;
                            this.serverInput = null;
                        }
                        this.lineInputStream = null;
                        if (this.logger.isLoggable(Level.FINE)) {
                            this.logger.fine("got bad greeting from host \"" + this.host + "\", port: " + port + ", response: " + serverResponse2);
                        }
                        throw new MessagingException("Got bad greeting from SMTP host: " + this.host + ", port: " + port + ", response: " + serverResponse2);
                    } catch (Throwable th) {
                        this.serverSocket.close();
                        this.serverSocket = null;
                        this.serverOutput = null;
                        this.serverInput = null;
                        this.lineInputStream = null;
                        throw th;
                    }
                }
                if (this.logger.isLoggable(level)) {
                    this.logger.fine("protocol started to host \"" + this.host + "\", port: " + port);
                }
            } catch (IOException e2) {
                e = e2;
                i = port;
                throw new MessagingException("Could not start protocol to SMTP host: " + this.host + ", port: " + i, e);
            }
        } catch (IOException e3) {
            e = e3;
        }
    }
}
