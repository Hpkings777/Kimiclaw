package com.sun.mail.util.logging;

import com.google.common.net.HttpHeaders;
import com.sun.mail.imap.IMAPStore;
import defpackage.AbstractC2039eV;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import javax.activation.DataHandler;
import javax.activation.FileTypeMap;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessageContext;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Service;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.io.FilenameUtils;

/* JADX INFO: loaded from: classes.dex */
public class MailHandler extends Handler {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int MIN_HEADER_SIZE = 1024;
    private volatile Filter[] attachmentFilters;
    private Formatter[] attachmentFormatters;
    private Formatter[] attachmentNames;
    private Authenticator auth;
    private int capacity;
    private Comparator<? super LogRecord> comparator;
    private FileTypeMap contentTypes;
    private LogRecord[] data;
    private String encoding;
    private volatile Filter filter;
    private Formatter formatter;
    private boolean isWriting;
    private Properties mailProps;
    private int[] matched;
    private Filter pushFilter;
    private Level pushLevel;
    private volatile boolean sealed;
    private Session session;
    private int size;
    private Formatter subjectFormatter;
    private static final Filter[] EMPTY_FILTERS = new Filter[0];
    private static final Formatter[] EMPTY_FORMATTERS = new Formatter[0];
    private static final int offValue = Level.OFF.intValue();
    private static final PrivilegedAction<Object> MAILHANDLER_LOADER = new GetAndSetContext(MailHandler.class);
    private static final ThreadLocal<Integer> MUTEX = new ThreadLocal<>();
    private static final Integer MUTEX_PUBLISH = -2;
    private static final Integer MUTEX_REPORT = -4;
    private static final Integer MUTEX_LINKAGE = -8;
    private volatile Level logLevel = Level.ALL;
    private volatile ErrorManager errorManager = defaultErrorManager();

    public static final class DefaultAuthenticator extends Authenticator {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final String pass;

        private DefaultAuthenticator(String str) {
            this.pass = str;
        }

        public static Authenticator of(String str) {
            return new DefaultAuthenticator(str);
        }

        @Override // javax.mail.Authenticator
        public final PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(getDefaultUserName(), this.pass);
        }
    }

    public static final class GetAndSetContext implements PrivilegedAction<Object> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        public static final Object NOT_MODIFIED = GetAndSetContext.class;
        private final Object source;

        public GetAndSetContext(Object obj) {
            this.source = obj;
        }

        @Override // java.security.PrivilegedAction
        public final Object run() {
            Thread threadCurrentThread = Thread.currentThread();
            ClassLoader contextClassLoader = threadCurrentThread.getContextClassLoader();
            Object obj = this.source;
            ClassLoader classLoader = obj == null ? null : obj instanceof ClassLoader ? (ClassLoader) obj : obj instanceof Class ? ((Class) obj).getClassLoader() : obj instanceof Thread ? ((Thread) obj).getContextClassLoader() : obj.getClass().getClassLoader();
            if (contextClassLoader == classLoader) {
                return NOT_MODIFIED;
            }
            threadCurrentThread.setContextClassLoader(classLoader);
            return contextClassLoader;
        }
    }

    public static final class TailNameFormatter extends Formatter {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final String name;

        private TailNameFormatter(String str) {
            this.name = str;
        }

        public static Formatter of(String str) {
            return new TailNameFormatter(str);
        }

        public final boolean equals(Object obj) {
            if (obj instanceof TailNameFormatter) {
                return this.name.equals(((TailNameFormatter) obj).name);
            }
            return false;
        }

        @Override // java.util.logging.Formatter
        public final String format(LogRecord logRecord) {
            return "";
        }

        @Override // java.util.logging.Formatter
        public final String getTail(Handler handler) {
            return this.name;
        }

        public final int hashCode() {
            return this.name.hashCode() + TailNameFormatter.class.hashCode();
        }

        public final String toString() {
            return this.name;
        }
    }

    public MailHandler() {
        init(null);
        this.sealed = true;
        checkAccess();
    }

    private boolean alignAttachmentFilters() {
        int length = this.attachmentFormatters.length;
        int length2 = this.attachmentFilters.length;
        if (length2 != length) {
            this.attachmentFilters = (Filter[]) Arrays.copyOf(this.attachmentFilters, length, Filter[].class);
            clearMatches(length2);
            z = length2 != 0;
            Filter filter = this.filter;
            if (filter != null) {
                while (length2 < length) {
                    this.attachmentFilters[length2] = filter;
                    length2++;
                }
            }
        }
        if (length == 0) {
            this.attachmentFilters = emptyFilterArray();
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0017  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean alignAttachmentNames() {
        boolean z;
        int length = this.attachmentFormatters.length;
        Formatter[] formatterArr = this.attachmentNames;
        int length2 = formatterArr.length;
        if (length2 != length) {
            this.attachmentNames = (Formatter[]) Arrays.copyOf(formatterArr, length, Formatter[].class);
            z = length2 != 0;
        }
        if (length == 0) {
            this.attachmentNames = emptyFormatterArray();
            return z;
        }
        for (int i = 0; i < length; i++) {
            Formatter[] formatterArr2 = this.attachmentNames;
            if (formatterArr2[i] == null) {
                formatterArr2[i] = TailNameFormatter.of(toString(this.attachmentFormatters[i]));
            }
        }
        return z;
    }

    private boolean allowRestrictedHeaders() {
        return LogManagerProperties.hasLogManager();
    }

    private void appendContentLang(MimePart mimePart, Locale locale) {
        try {
            String languageTag = LogManagerProperties.toLanguageTag(locale);
            if (languageTag.length() != 0) {
                String header = mimePart.getHeader(HttpHeaders.CONTENT_LANGUAGE, null);
                if (isEmpty(header)) {
                    mimePart.setHeader(HttpHeaders.CONTENT_LANGUAGE, languageTag);
                    return;
                }
                if (header.equalsIgnoreCase(languageTag)) {
                    return;
                }
                String strConcat = ",".concat(languageTag);
                int iIndexOf = 0;
                do {
                    iIndexOf = header.indexOf(strConcat, iIndexOf);
                    if (iIndexOf <= -1 || (iIndexOf = iIndexOf + strConcat.length()) == header.length()) {
                        break;
                    }
                } while (header.charAt(iIndexOf) != ',');
                if (iIndexOf < 0) {
                    int iLastIndexOf = header.lastIndexOf("\r\n\t");
                    mimePart.setHeader(HttpHeaders.CONTENT_LANGUAGE, (iLastIndexOf < 0 ? header.length() + 20 : (header.length() - iLastIndexOf) + 8) + strConcat.length() > 76 ? header.concat("\r\n\t".concat(strConcat)) : header.concat(strConcat));
                }
            }
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void appendFileName(Part part, String str) {
        if (str == null) {
            reportNullError(5);
        } else if (str.length() > 0) {
            appendFileName0(part, str);
        }
    }

    private void appendFileName0(Part part, String str) {
        try {
            String strReplaceAll = str.replaceAll("[\\x00-\\x1F\\x7F]+", "");
            String fileName = part.getFileName();
            if (fileName != null) {
                strReplaceAll = fileName.concat(strReplaceAll);
            }
            part.setFileName(strReplaceAll);
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void appendSubject(Message message, String str) {
        if (str == null) {
            reportNullError(5);
        } else if (str.length() > 0) {
            appendSubject0(message, str);
        }
    }

    private void appendSubject0(Message message, String str) {
        try {
            String strReplaceAll = str.replaceAll("[\\x00-\\x1F\\x7F]+", "");
            String encodingName = getEncodingName();
            String subject = message.getSubject();
            MimeMessage mimeMessage = (MimeMessage) message;
            if (subject != null) {
                strReplaceAll = subject.concat(strReplaceAll);
            }
            mimeMessage.setSubject(strReplaceAll, MimeUtility.mimeCharset(encodingName));
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private static String atIndexMsg(int i) {
        return "At index: " + i + FilenameUtils.EXTENSION_SEPARATOR;
    }

    private static MessagingException attach(MessagingException messagingException, Exception exc) {
        if (exc != null && !messagingException.setNextException(exc)) {
            if (exc instanceof MessagingException) {
                MessagingException messagingException2 = (MessagingException) exc;
                if (messagingException2.setNextException(messagingException)) {
                    return messagingException2;
                }
            }
            if (exc != messagingException) {
                messagingException.addSuppressed(exc);
            }
        }
        return messagingException;
    }

    private static RuntimeException attachmentMismatch(String str) {
        return new IndexOutOfBoundsException(str);
    }

    private void checkAccess() {
        if (this.sealed) {
            LogManagerProperties.checkLogManagerAccess();
        }
    }

    private void clearMatches(int i) {
        for (int i2 = 0; i2 < this.size; i2++) {
            int[] iArr = this.matched;
            if (iArr[i2] >= i) {
                iArr[i2] = MUTEX_PUBLISH.intValue();
            }
        }
    }

    private String contentWithEncoding(String str, String str2) {
        String string;
        try {
            ContentType contentType = new ContentType(str);
            contentType.setParameter("charset", MimeUtility.mimeCharset(str2));
            string = contentType.toString();
        } catch (MessagingException e) {
            reportError(str, e, 5);
        }
        return !isEmpty(string) ? string : str;
    }

    private MimeBodyPart createBodyPart() throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setDisposition(Part.INLINE);
        mimeBodyPart.setDescription(descriptionFrom(getFormatter(), getFilter(), this.subjectFormatter));
        setAcceptLang(mimeBodyPart);
        return mimeBodyPart;
    }

    private static Formatter createSimpleFormatter() {
        return (Formatter) Formatter.class.cast(new SimpleFormatter());
    }

    private ErrorManager defaultErrorManager() {
        ErrorManager errorManager;
        try {
            errorManager = super.getErrorManager();
        } catch (LinkageError | RuntimeException unused) {
            errorManager = null;
        }
        return errorManager == null ? new ErrorManager() : errorManager;
    }

    private String descriptionFrom(Comparator<?> comparator, Level level, Filter filter) {
        StringBuilder sb = new StringBuilder("Sorted using ");
        sb.append(comparator == null ? "no comparator" : comparator.getClass().getName());
        sb.append(", pushed when ");
        sb.append(level.getName());
        sb.append(", and ");
        return AbstractC2039eV.o(sb, filter == null ? "no push filter" : filter.getClass().getName(), FilenameUtils.EXTENSION_SEPARATOR);
    }

    private static Filter[] emptyFilterArray() {
        return EMPTY_FILTERS;
    }

    private static Formatter[] emptyFormatterArray() {
        return EMPTY_FORMATTERS;
    }

    private void envelopeFor(Message message, boolean z) {
        setAcceptLang(message);
        setFrom(message);
        Message.RecipientType recipientType = Message.RecipientType.TO;
        if (!setRecipient(message, "mail.to", recipientType)) {
            setDefaultRecipient(message, recipientType);
        }
        setRecipient(message, "mail.cc", Message.RecipientType.CC);
        setRecipient(message, "mail.bcc", Message.RecipientType.BCC);
        setReplyTo(message);
        setSender(message);
        setMailer(message);
        setAutoSubmitted(message);
        if (z) {
            setPriority(message);
        }
        try {
            message.setSentDate(new Date());
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private String format(Formatter formatter, LogRecord logRecord) {
        try {
            return formatter.format(logRecord);
        } catch (RuntimeException e) {
            reportError(e.getMessage(), e, 5);
            return "";
        }
    }

    private Object getAndSetContextClassLoader(Object obj) {
        if (obj != GetAndSetContext.NOT_MODIFIED) {
            try {
                return AccessController.doPrivileged(obj instanceof PrivilegedAction ? (PrivilegedAction) obj : new GetAndSetContext(obj));
            } catch (SecurityException unused) {
            }
        }
        return GetAndSetContext.NOT_MODIFIED;
    }

    private String getClassId(Formatter formatter) {
        return formatter instanceof TailNameFormatter ? String.class.getName() : formatter.getClass().getName();
    }

    private String getContentType(String str) {
        String contentType = this.contentTypes.getContentType(str);
        if ("application/octet-stream".equalsIgnoreCase(contentType)) {
            return null;
        }
        return contentType;
    }

    private String getEncodingName() {
        String encoding = getEncoding();
        return encoding == null ? MimeUtility.getDefaultJavaCharset() : encoding;
    }

    private String getLocalHost(Service service) {
        try {
            return LogManagerProperties.getLocalHost(service);
        } catch (Exception e) {
            reportError(service.toString(), e, 4);
            return null;
        } catch (LinkageError | NoSuchMethodException | SecurityException unused) {
            return null;
        }
    }

    private int getMatchedPart() {
        Integer num = MUTEX.get();
        if (num == null || num.intValue() >= readOnlyAttachmentFilters().length) {
            num = MUTEX_PUBLISH;
        }
        return num.intValue();
    }

    private Session getSession(Message message) {
        message.getClass();
        return new MessageContext(message).getSession();
    }

    private void grow() {
        LogRecord[] logRecordArr = this.data;
        int length = logRecordArr.length;
        int i = (length >> 1) + length + 1;
        int i2 = this.capacity;
        if (i > i2 || i < length) {
            i = i2;
        }
        this.data = (LogRecord[]) Arrays.copyOf(logRecordArr, i, LogRecord[].class);
        this.matched = Arrays.copyOf(this.matched, i);
    }

    private static boolean hasValue(String str) {
        return (isEmpty(str) || "null".equalsIgnoreCase(str)) ? false : true;
    }

    private String head(Formatter formatter) {
        try {
            return formatter.getHead(this);
        } catch (RuntimeException e) {
            reportError(e.getMessage(), e, 5);
            return "";
        }
    }

    private synchronized void init(Properties properties) {
        try {
            String name = getClass().getName();
            this.mailProps = new Properties();
            Object andSetContextClassLoader = getAndSetContextClassLoader(MAILHANDLER_LOADER);
            try {
                this.contentTypes = FileTypeMap.getDefaultFileTypeMap();
                getAndSetContextClassLoader(andSetContextClassLoader);
                initErrorManager(name);
                initLevel(name);
                initFilter(name);
                initCapacity(name);
                initAuthenticator(name);
                initEncoding(name);
                initFormatter(name);
                initComparator(name);
                initPushLevel(name);
                initPushFilter(name);
                initSubject(name);
                initAttachmentFormaters(name);
                initAttachmentFilters(name);
                initAttachmentNames(name);
                if (properties == null && LogManagerProperties.fromLogManager(name.concat(".verify")) != null) {
                    verifySettings(initSession());
                }
                intern();
            } catch (Throwable th) {
                getAndSetContextClassLoader(andSetContextClassLoader);
                throw th;
            }
        } catch (Throwable th2) {
            throw th2;
        }
    }

    private void initAttachmentFilters(String str) {
        String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".attachment.filters"));
        if (isEmpty(strFromLogManager)) {
            this.attachmentFilters = emptyFilterArray();
            alignAttachmentFilters();
            return;
        }
        String[] strArrSplit = strFromLogManager.split(",");
        int length = strArrSplit.length;
        Filter[] filterArr = new Filter[length];
        for (int i = 0; i < length; i++) {
            String strTrim = strArrSplit[i].trim();
            strArrSplit[i] = strTrim;
            if (!"null".equalsIgnoreCase(strTrim)) {
                try {
                    filterArr[i] = LogManagerProperties.newFilter(strArrSplit[i]);
                } catch (SecurityException e) {
                    throw e;
                } catch (Exception e2) {
                    reportError(e2.getMessage(), e2, 4);
                }
            }
        }
        this.attachmentFilters = filterArr;
        if (alignAttachmentFilters()) {
            reportError("Attachment filters.", attachmentMismatch("Length mismatch."), 4);
        }
    }

    private void initAttachmentFormaters(String str) {
        String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".attachment.formatters"));
        if (isEmpty(strFromLogManager)) {
            this.attachmentFormatters = emptyFormatterArray();
            return;
        }
        String[] strArrSplit = strFromLogManager.split(",");
        Formatter[] formatterArrEmptyFormatterArray = strArrSplit.length == 0 ? emptyFormatterArray() : new Formatter[strArrSplit.length];
        for (int i = 0; i < formatterArrEmptyFormatterArray.length; i++) {
            String strTrim = strArrSplit[i].trim();
            strArrSplit[i] = strTrim;
            if ("null".equalsIgnoreCase(strTrim)) {
                reportError("Attachment formatter.", new NullPointerException(atIndexMsg(i)), 4);
                formatterArrEmptyFormatterArray[i] = createSimpleFormatter();
            } else {
                try {
                    Formatter formatterNewFormatter = LogManagerProperties.newFormatter(strArrSplit[i]);
                    formatterArrEmptyFormatterArray[i] = formatterNewFormatter;
                    if (formatterNewFormatter instanceof TailNameFormatter) {
                        reportError("Attachment formatter.", new ClassNotFoundException(formatterArrEmptyFormatterArray[i].toString()), 4);
                        formatterArrEmptyFormatterArray[i] = createSimpleFormatter();
                    }
                } catch (SecurityException e) {
                    throw e;
                } catch (Exception e2) {
                    reportError(e2.getMessage(), e2, 4);
                    formatterArrEmptyFormatterArray[i] = createSimpleFormatter();
                }
            }
        }
        this.attachmentFormatters = formatterArrEmptyFormatterArray;
    }

    private void initAttachmentNames(String str) {
        String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".attachment.names"));
        if (isEmpty(strFromLogManager)) {
            this.attachmentNames = emptyFormatterArray();
            alignAttachmentNames();
            return;
        }
        String[] strArrSplit = strFromLogManager.split(",");
        int length = strArrSplit.length;
        Formatter[] formatterArr = new Formatter[length];
        for (int i = 0; i < length; i++) {
            String strTrim = strArrSplit[i].trim();
            strArrSplit[i] = strTrim;
            if ("null".equalsIgnoreCase(strTrim)) {
                reportError("Attachment names.", new NullPointerException(atIndexMsg(i)), 4);
            } else {
                try {
                    try {
                        formatterArr[i] = LogManagerProperties.newFormatter(strArrSplit[i]);
                    } catch (ClassCastException | ClassNotFoundException unused) {
                        formatterArr[i] = TailNameFormatter.of(strArrSplit[i]);
                    }
                } catch (SecurityException e) {
                    throw e;
                } catch (Exception e2) {
                    reportError(e2.getMessage(), e2, 4);
                }
            }
        }
        this.attachmentNames = formatterArr;
        if (alignAttachmentNames()) {
            reportError("Attachment names.", attachmentMismatch("Length mismatch."), 4);
        }
    }

    private void initAuthenticator(String str) {
        String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".authenticator"));
        if (strFromLogManager == null || "null".equalsIgnoreCase(strFromLogManager)) {
            return;
        }
        if (strFromLogManager.length() == 0) {
            this.auth = DefaultAuthenticator.of(strFromLogManager);
            return;
        }
        try {
            this.auth = (Authenticator) LogManagerProperties.newObjectFrom(strFromLogManager, Authenticator.class);
        } catch (ClassCastException | ClassNotFoundException unused) {
            this.auth = DefaultAuthenticator.of(strFromLogManager);
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void initCapacity(String str) {
        try {
            String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".capacity"));
            if (strFromLogManager != null) {
                setCapacity0(Integer.parseInt(strFromLogManager));
            } else {
                setCapacity0(IMAPStore.RESPONSE);
            }
        } catch (SecurityException e) {
            throw e;
        } catch (RuntimeException e2) {
            reportError(e2.getMessage(), e2, 4);
        }
        if (this.capacity <= 0) {
            this.capacity = IMAPStore.RESPONSE;
        }
        LogRecord[] logRecordArr = new LogRecord[1];
        this.data = logRecordArr;
        this.matched = new int[logRecordArr.length];
    }

    private void initComparator(String str) {
        try {
            String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".comparator"));
            String strFromLogManager2 = LogManagerProperties.fromLogManager(str.concat(".comparator.reverse"));
            if (!hasValue(strFromLogManager)) {
                if (!isEmpty(strFromLogManager2)) {
                    throw new IllegalArgumentException("No comparator to reverse.");
                }
            } else {
                this.comparator = LogManagerProperties.newComparator(strFromLogManager);
                if (Boolean.parseBoolean(strFromLogManager2)) {
                    this.comparator = LogManagerProperties.reverseOrder(this.comparator);
                }
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void initEncoding(String str) {
        try {
            String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".encoding"));
            if (strFromLogManager != null) {
                setEncoding0(strFromLogManager);
            }
        } catch (UnsupportedEncodingException e) {
            e = e;
            reportError(e.getMessage(), e, 4);
        } catch (SecurityException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            e = e3;
            reportError(e.getMessage(), e, 4);
        }
    }

    private void initErrorManager(String str) {
        try {
            String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".errorManager"));
            if (strFromLogManager != null) {
                setErrorManager0(LogManagerProperties.newErrorManager(strFromLogManager));
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void initFilter(String str) {
        try {
            String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".filter"));
            if (hasValue(strFromLogManager)) {
                this.filter = LogManagerProperties.newFilter(strFromLogManager);
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void initFormatter(String str) {
        try {
            String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".formatter"));
            if (!hasValue(strFromLogManager)) {
                this.formatter = createSimpleFormatter();
                return;
            }
            Formatter formatterNewFormatter = LogManagerProperties.newFormatter(strFromLogManager);
            if (formatterNewFormatter instanceof TailNameFormatter) {
                this.formatter = createSimpleFormatter();
            } else {
                this.formatter = formatterNewFormatter;
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
            this.formatter = createSimpleFormatter();
        }
    }

    private void initLevel(String str) {
        try {
            String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".level"));
            if (strFromLogManager != null) {
                this.logLevel = Level.parse(strFromLogManager);
            } else {
                this.logLevel = Level.WARNING;
            }
        } catch (SecurityException e) {
            throw e;
        } catch (RuntimeException e2) {
            reportError(e2.getMessage(), e2, 4);
            this.logLevel = Level.WARNING;
        }
    }

    private void initPushFilter(String str) {
        try {
            String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".pushFilter"));
            if (hasValue(strFromLogManager)) {
                this.pushFilter = LogManagerProperties.newFilter(strFromLogManager);
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void initPushLevel(String str) {
        try {
            String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".pushLevel"));
            if (strFromLogManager != null) {
                this.pushLevel = Level.parse(strFromLogManager);
            }
        } catch (RuntimeException e) {
            reportError(e.getMessage(), e, 4);
        }
        if (this.pushLevel == null) {
            this.pushLevel = Level.OFF;
        }
    }

    private Session initSession() {
        Session session = Session.getInstance(new LogManagerProperties(this.mailProps, getClass().getName()), this.auth);
        this.session = session;
        return session;
    }

    private void initSubject(String str) {
        String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".subject"));
        if (strFromLogManager == null) {
            strFromLogManager = "com.sun.mail.util.logging.CollectorFormatter";
        }
        if (!hasValue(strFromLogManager)) {
            this.subjectFormatter = TailNameFormatter.of(strFromLogManager);
            return;
        }
        try {
            this.subjectFormatter = LogManagerProperties.newFormatter(strFromLogManager);
        } catch (ClassCastException | ClassNotFoundException unused) {
            this.subjectFormatter = TailNameFormatter.of(strFromLogManager);
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e2) {
            this.subjectFormatter = TailNameFormatter.of(strFromLogManager);
            reportError(e2.getMessage(), e2, 4);
        }
    }

    private void intern() {
        try {
            HashMap map = new HashMap();
            try {
                intern(map, this.errorManager);
            } catch (SecurityException e) {
                reportError(e.getMessage(), e, 4);
            }
            try {
                Filter filter = this.filter;
                Object objIntern = intern(map, filter);
                if (objIntern != filter && (objIntern instanceof Filter)) {
                    this.filter = (Filter) objIntern;
                }
                Formatter formatter = this.formatter;
                Object objIntern2 = intern(map, formatter);
                if (objIntern2 != formatter && (objIntern2 instanceof Formatter)) {
                    this.formatter = (Formatter) objIntern2;
                }
            } catch (SecurityException e2) {
                reportError(e2.getMessage(), e2, 4);
            }
            Formatter formatter2 = this.subjectFormatter;
            Object objIntern3 = intern(map, formatter2);
            if (objIntern3 != formatter2 && (objIntern3 instanceof Formatter)) {
                this.subjectFormatter = (Formatter) objIntern3;
            }
            Filter filter2 = this.pushFilter;
            Object objIntern4 = intern(map, filter2);
            if (objIntern4 != filter2 && (objIntern4 instanceof Filter)) {
                this.pushFilter = (Filter) objIntern4;
            }
            int i = 0;
            while (true) {
                Formatter[] formatterArr = this.attachmentFormatters;
                if (i >= formatterArr.length) {
                    return;
                }
                Formatter formatter3 = formatterArr[i];
                Object objIntern5 = intern(map, formatter3);
                if (objIntern5 != formatter3 && (objIntern5 instanceof Formatter)) {
                    this.attachmentFormatters[i] = (Formatter) objIntern5;
                }
                Filter filter3 = this.attachmentFilters[i];
                Object objIntern6 = intern(map, filter3);
                if (objIntern6 != filter3 && (objIntern6 instanceof Filter)) {
                    this.attachmentFilters[i] = (Filter) objIntern6;
                }
                Formatter formatter4 = this.attachmentNames[i];
                Object objIntern7 = intern(map, formatter4);
                if (objIntern7 != formatter4 && (objIntern7 instanceof Formatter)) {
                    this.attachmentNames[i] = (Formatter) objIntern7;
                }
                i++;
            }
        } catch (Exception e3) {
            reportError(e3.getMessage(), e3, 4);
        } catch (LinkageError e4) {
            reportError(e4.getMessage(), new InvocationTargetException(e4), 4);
        }
    }

    private boolean isAttachmentLoggable(LogRecord logRecord) {
        Filter[] onlyAttachmentFilters = readOnlyAttachmentFilters();
        for (int i = 0; i < onlyAttachmentFilters.length; i++) {
            Filter filter = onlyAttachmentFilters[i];
            if (filter == null || filter.isLoggable(logRecord)) {
                setMatchedPart(i);
                return true;
            }
        }
        return false;
    }

    private static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    private boolean isPushable(LogRecord logRecord) {
        int iIntValue = getPushLevel().intValue();
        if (iIntValue == offValue || logRecord.getLevel().intValue() < iIntValue) {
            return false;
        }
        Filter pushFilter = getPushFilter();
        if (pushFilter == null) {
            return true;
        }
        int matchedPart = getMatchedPart();
        if (!(matchedPart == -1 && getFilter() == pushFilter) && (matchedPart < 0 || this.attachmentFilters[matchedPart] != pushFilter)) {
            return pushFilter.isLoggable(logRecord);
        }
        return true;
    }

    private Locale localeFor(LogRecord logRecord) {
        ResourceBundle resourceBundle = logRecord.getResourceBundle();
        if (resourceBundle == null) {
            return null;
        }
        Locale locale = resourceBundle.getLocale();
        return (locale == null || isEmpty(locale.getLanguage())) ? Locale.getDefault() : locale;
    }

    private void publish0(LogRecord logRecord) {
        Message messageWriteLogRecords;
        boolean zIsPushable;
        synchronized (this) {
            try {
                int i = this.size;
                if (i == this.data.length && i < this.capacity) {
                    grow();
                }
                int i2 = this.size;
                messageWriteLogRecords = null;
                if (i2 < this.data.length) {
                    this.matched[i2] = getMatchedPart();
                    LogRecord[] logRecordArr = this.data;
                    int i3 = this.size;
                    logRecordArr[i3] = logRecord;
                    this.size = i3 + 1;
                    zIsPushable = isPushable(logRecord);
                    if (zIsPushable || this.size >= this.capacity) {
                        messageWriteLogRecords = writeLogRecords(1);
                    }
                } else {
                    zIsPushable = false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (messageWriteLogRecords != null) {
            send(messageWriteLogRecords, zIsPushable, 1);
        }
    }

    private Filter[] readOnlyAttachmentFilters() {
        return this.attachmentFilters;
    }

    private void releaseMutex() {
        MUTEX.remove();
    }

    private void reportFilterError(LogRecord logRecord) {
        Formatter formatterCreateSimpleFormatter = createSimpleFormatter();
        reportError("Log record " + logRecord.getSequenceNumber() + " was filtered from all message parts.  " + head(formatterCreateSimpleFormatter) + format(formatterCreateSimpleFormatter, logRecord) + tail(formatterCreateSimpleFormatter, ""), new IllegalArgumentException(getFilter() + ", " + Arrays.asList(readOnlyAttachmentFilters())), 5);
    }

    private void reportLinkageError(Throwable th, int i) {
        if (th == null) {
            throw new NullPointerException(String.valueOf(i));
        }
        ThreadLocal<Integer> threadLocal = MUTEX;
        Integer num = threadLocal.get();
        if (num == null || num.intValue() > MUTEX_LINKAGE.intValue()) {
            threadLocal.set(MUTEX_LINKAGE);
            try {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
                if (num != null) {
                    threadLocal.set(num);
                } else {
                    threadLocal.remove();
                }
            } catch (LinkageError | RuntimeException unused) {
                if (num != null) {
                    MUTEX.set(num);
                } else {
                    MUTEX.remove();
                }
            } catch (Throwable th2) {
                if (num != null) {
                    MUTEX.set(num);
                } else {
                    MUTEX.remove();
                }
                throw th2;
            }
        }
    }

    private void reportNonDiscriminating(Object obj, Object obj2) {
        reportError("Non discriminating equals implementation.", new IllegalArgumentException(obj.getClass().getName() + " should not be equal to " + obj2.getClass().getName()), 4);
    }

    private void reportNonSymmetric(Object obj, Object obj2) {
        reportError("Non symmetric equals implementation.", new IllegalArgumentException(obj.getClass().getName() + " is not equal to " + obj2.getClass().getName()), 4);
    }

    private void reportNullError(int i) {
        reportError("null", new NullPointerException(), i);
    }

    private void reportUnPublishedError(LogRecord logRecord) {
        String str;
        ThreadLocal<Integer> threadLocal = MUTEX;
        Integer num = threadLocal.get();
        if (num == null || num.intValue() > MUTEX_REPORT.intValue()) {
            threadLocal.set(MUTEX_REPORT);
            if (logRecord != null) {
                try {
                    Formatter formatterCreateSimpleFormatter = createSimpleFormatter();
                    str = "Log record " + logRecord.getSequenceNumber() + " was not published. " + head(formatterCreateSimpleFormatter) + format(formatterCreateSimpleFormatter, logRecord) + tail(formatterCreateSimpleFormatter, "");
                } catch (Throwable th) {
                    if (num != null) {
                        MUTEX.set(num);
                    } else {
                        MUTEX.remove();
                    }
                    throw th;
                }
            } else {
                str = null;
            }
            reportError(str, new IllegalStateException("Recursive publish detected by thread " + Thread.currentThread()), 1);
            if (num != null) {
                threadLocal.set(num);
            } else {
                threadLocal.remove();
            }
        }
    }

    private void reportUnexpectedSend(MimeMessage mimeMessage, String str, Exception exc) {
        Exception messagingException = new MessagingException("An empty message was sent.", exc);
        setErrorContent(mimeMessage, str, messagingException);
        reportError(mimeMessage, messagingException, 4);
    }

    private void reset() {
        int i = this.size;
        LogRecord[] logRecordArr = this.data;
        if (i < logRecordArr.length) {
            Arrays.fill(logRecordArr, 0, i, (Object) null);
        } else {
            Arrays.fill(logRecordArr, (Object) null);
        }
        this.size = 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0023 A[Catch: MessagingException -> 0x0006, RuntimeException | MessagingException -> 0x0008, TRY_ENTER, TryCatch #5 {RuntimeException | MessagingException -> 0x0008, blocks: (B:3:0x0002, B:19:0x0023, B:20:0x0026), top: B:23:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void saveChangesNoContent(Message message, String str) {
        if (message != null) {
            try {
                try {
                    message.saveChanges();
                } catch (NullPointerException e) {
                    try {
                        if (message.getHeader("Content-Transfer-Encoding") != null) {
                            throw e;
                        }
                        message.setHeader("Content-Transfer-Encoding", "base64");
                        message.saveChanges();
                    } catch (RuntimeException e2) {
                        e = e2;
                        if (e != e) {
                            e.addSuppressed(e);
                        }
                        throw e;
                    } catch (MessagingException e3) {
                        e = e3;
                        if (e != e) {
                        }
                        throw e;
                    }
                }
            } catch (RuntimeException | MessagingException e4) {
                reportError(str, e4, 5);
            }
        }
    }

    private void send(Message message, boolean z, int i) {
        try {
            envelopeFor(message, z);
            Object andSetContextClassLoader = getAndSetContextClassLoader(MAILHANDLER_LOADER);
            try {
                Transport.send(message);
            } finally {
                getAndSetContextClassLoader(andSetContextClassLoader);
            }
        } catch (RuntimeException e) {
            reportError(message, e, i);
        } catch (Exception e2) {
            reportError(message, e2, i);
        }
    }

    private void setAcceptLang(Part part) {
        try {
            String languageTag = LogManagerProperties.toLanguageTag(Locale.getDefault());
            if (languageTag.length() != 0) {
                part.setHeader(HttpHeaders.ACCEPT_LANGUAGE, languageTag);
            }
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void setAuthenticator0(Authenticator authenticator) throws Throwable {
        Session sessionUpdateSession;
        checkAccess();
        synchronized (this) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.auth = authenticator;
            sessionUpdateSession = updateSession();
        }
        verifySettings(sessionUpdateSession);
    }

    private void setAutoSubmitted(Message message) {
        if (allowRestrictedHeaders()) {
            try {
                message.setHeader("auto-submitted", "auto-generated");
            } catch (MessagingException e) {
                reportError(e.getMessage(), e, 5);
            }
        }
    }

    private synchronized void setCapacity0(int i) {
        try {
            checkAccess();
            if (i <= 0) {
                throw new IllegalArgumentException("Capacity must be greater than zero.");
            }
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            if (this.capacity < 0) {
                this.capacity = -i;
            } else {
                this.capacity = i;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void setContent(MimePart mimePart, CharSequence charSequence, String str) throws MessagingException {
        String encodingName = getEncodingName();
        if (str == null || "text/plain".equalsIgnoreCase(str)) {
            mimePart.setText(charSequence.toString(), MimeUtility.mimeCharset(encodingName));
            return;
        }
        try {
            mimePart.setDataHandler(new DataHandler(new ByteArrayDataSource(charSequence.toString(), contentWithEncoding(str, encodingName))));
        } catch (IOException e) {
            reportError(e.getMessage(), e, 5);
            mimePart.setText(charSequence.toString(), encodingName);
        }
    }

    private void setDefaultFrom(Message message) {
        try {
            message.setFrom();
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void setDefaultRecipient(Message message, Message.RecipientType recipientType) {
        try {
            InternetAddress localAddress = InternetAddress.getLocalAddress(getSession(message));
            if (localAddress != null) {
                message.setRecipient(recipientType, localAddress);
                return;
            }
            MimeMessage mimeMessage = new MimeMessage(getSession(message));
            mimeMessage.setFrom();
            Address[] from = mimeMessage.getFrom();
            if (from.length <= 0) {
                throw new MessagingException("No local address.");
            }
            message.setRecipients(recipientType, from);
        } catch (RuntimeException e) {
            e = e;
            reportError("Unable to compute a default recipient.", e, 5);
        } catch (MessagingException e2) {
            e = e2;
            reportError("Unable to compute a default recipient.", e, 5);
        }
    }

    private void setEncoding0(String str) throws UnsupportedEncodingException {
        if (str != null) {
            try {
                if (!Charset.isSupported(str)) {
                    throw new UnsupportedEncodingException(str);
                }
            } catch (IllegalCharsetNameException unused) {
                throw new UnsupportedEncodingException(str);
            }
        }
        synchronized (this) {
            this.encoding = str;
        }
    }

    private void setErrorContent(MimeMessage mimeMessage, String str, Throwable th) {
        MimeBodyPart mimeBodyPartCreateBodyPart;
        String strDescriptionFrom;
        String classId;
        try {
            synchronized (this) {
                mimeBodyPartCreateBodyPart = createBodyPart();
                strDescriptionFrom = descriptionFrom(this.comparator, this.pushLevel, this.pushFilter);
                classId = getClassId(this.subjectFormatter);
            }
            StringBuilder sb = new StringBuilder("Formatted using ");
            sb.append(th == null ? Throwable.class.getName() : th.getClass().getName());
            sb.append(", filtered with ");
            sb.append(str);
            sb.append(", and named by ");
            sb.append(classId);
            sb.append(FilenameUtils.EXTENSION_SEPARATOR);
            mimeBodyPartCreateBodyPart.setDescription(sb.toString());
            setContent(mimeBodyPartCreateBodyPart, toMsgString(th), "text/plain");
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(mimeBodyPartCreateBodyPart);
            mimeMessage.setContent(mimeMultipart);
            mimeMessage.setDescription(strDescriptionFrom);
            setAcceptLang(mimeMessage);
            mimeMessage.saveChanges();
        } catch (RuntimeException e) {
            e = e;
            reportError("Unable to create body.", e, 4);
        } catch (MessagingException e2) {
            e = e2;
            reportError("Unable to create body.", e, 4);
        }
    }

    private void setErrorManager0(ErrorManager errorManager) {
        errorManager.getClass();
        try {
            synchronized (this) {
                this.errorManager = errorManager;
                super.setErrorManager(errorManager);
            }
        } catch (LinkageError | RuntimeException unused) {
        }
    }

    private void setFrom(Message message) {
        String property = getSession(message).getProperty("mail.from");
        if (property == null) {
            setDefaultFrom(message);
            return;
        }
        try {
            InternetAddress[] internetAddressArr = InternetAddress.parse(property, false);
            if (internetAddressArr.length > 0) {
                if (internetAddressArr.length == 1) {
                    message.setFrom(internetAddressArr[0]);
                } else {
                    message.addFrom(internetAddressArr);
                }
            }
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
            setDefaultFrom(message);
        }
    }

    private void setIncompleteCopy(Message message) {
        try {
            message.setHeader("Incomplete-Copy", "");
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void setMailProperties0(Properties properties) throws Throwable {
        Session sessionUpdateSession;
        checkAccess();
        Properties properties2 = (Properties) properties.clone();
        synchronized (this) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.mailProps = properties2;
            sessionUpdateSession = updateSession();
        }
        verifySettings(sessionUpdateSession);
    }

    private void setMailer(Message message) {
        String strReplaceAll;
        String strFold;
        try {
            Class<?> cls = getClass();
            if (cls == MailHandler.class) {
                strFold = MailHandler.class.getName();
            } else {
                try {
                    strReplaceAll = MimeUtility.encodeText(cls.getName());
                } catch (UnsupportedEncodingException e) {
                    reportError(e.getMessage(), e, 5);
                    strReplaceAll = cls.getName().replaceAll("[^\\x00-\\x7F]", "\u001a");
                }
                strFold = MimeUtility.fold(10, MailHandler.class.getName() + " using the " + strReplaceAll + " extension.");
            }
            message.setHeader("X-Mailer", strFold);
        } catch (MessagingException e2) {
            reportError(e2.getMessage(), e2, 5);
        }
    }

    private void setMatchedPart(int i) {
        Integer num = MUTEX_PUBLISH;
        ThreadLocal<Integer> threadLocal = MUTEX;
        if (num.equals(threadLocal.get())) {
            threadLocal.set(Integer.valueOf(i));
        }
    }

    private void setPriority(Message message) {
        try {
            message.setHeader("Importance", "High");
            message.setHeader("Priority", "urgent");
            message.setHeader("X-Priority", "2");
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private boolean setRecipient(Message message, String str, Message.RecipientType recipientType) {
        String property = getSession(message).getProperty(str);
        boolean z = property != null;
        if (!isEmpty(property)) {
            try {
                InternetAddress[] internetAddressArr = InternetAddress.parse(property, false);
                if (internetAddressArr.length > 0) {
                    message.setRecipients(recipientType, internetAddressArr);
                    return z;
                }
            } catch (MessagingException e) {
                reportError(e.getMessage(), e, 5);
            }
        }
        return z;
    }

    private void setReplyTo(Message message) {
        String property = getSession(message).getProperty("mail.reply.to");
        if (isEmpty(property)) {
            return;
        }
        try {
            InternetAddress[] internetAddressArr = InternetAddress.parse(property, false);
            if (internetAddressArr.length > 0) {
                message.setReplyTo(internetAddressArr);
            }
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void setSender(Message message) {
        String property = getSession(message).getProperty("mail.sender");
        if (isEmpty(property)) {
            return;
        }
        try {
            InternetAddress[] internetAddressArr = InternetAddress.parse(property, false);
            if (internetAddressArr.length > 0) {
                ((MimeMessage) message).setSender(internetAddressArr[0]);
                if (internetAddressArr.length > 1) {
                    reportError("Ignoring other senders.", tooManyAddresses(internetAddressArr, 1), 5);
                }
            }
        } catch (MessagingException e) {
            reportError(e.getMessage(), e, 5);
        }
    }

    private void sort() {
        Comparator<? super LogRecord> comparator = this.comparator;
        if (comparator != null) {
            try {
                int i = this.size;
                if (i != 1) {
                    Arrays.sort(this.data, 0, i, comparator);
                    return;
                }
                LogRecord logRecord = this.data[0];
                if (comparator.compare(logRecord, logRecord) != 0) {
                    throw new IllegalArgumentException(this.comparator.getClass().getName());
                }
            } catch (RuntimeException e) {
                reportError(e.getMessage(), e, 5);
            }
        }
    }

    private String tail(Formatter formatter, String str) {
        try {
            return formatter.getTail(this);
        } catch (RuntimeException e) {
            reportError(e.getMessage(), e, 5);
            return str;
        }
    }

    private String toMsgString(Throwable th) {
        if (th == null) {
            return "null";
        }
        String encodingName = getEncodingName();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(MIN_HEADER_SIZE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, encodingName);
            try {
                PrintWriter printWriter = new PrintWriter(outputStreamWriter);
                try {
                    printWriter.println(th.getMessage());
                    th.printStackTrace(printWriter);
                    printWriter.flush();
                    printWriter.close();
                    outputStreamWriter.close();
                    return byteArrayOutputStream.toString(encodingName);
                } finally {
                }
            } catch (Throwable th2) {
                try {
                    outputStreamWriter.close();
                } catch (Throwable th3) {
                    th2.addSuppressed(th3);
                }
                throw th2;
            }
        } catch (RuntimeException e) {
            return th.toString() + ' ' + e.toString();
        } catch (Exception e2) {
            return th.toString() + ' ' + e2.toString();
        }
    }

    private String toRawString(Message message) throws MessagingException, IOException {
        if (message == null) {
            return null;
        }
        Object andSetContextClassLoader = getAndSetContextClassLoader(MAILHANDLER_LOADER);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Math.max(message.getSize() + MIN_HEADER_SIZE, MIN_HEADER_SIZE));
            message.writeTo(byteArrayOutputStream);
            return byteArrayOutputStream.toString("UTF-8");
        } finally {
            getAndSetContextClassLoader(andSetContextClassLoader);
        }
    }

    private String toString(Formatter formatter) {
        String string = formatter.toString();
        return !isEmpty(string) ? string : getClassId(formatter);
    }

    private AddressException tooManyAddresses(Address[] addressArr, int i) {
        return new AddressException(Arrays.asList(addressArr).subList(i, addressArr.length).toString());
    }

    private boolean tryMutex() {
        ThreadLocal<Integer> threadLocal = MUTEX;
        if (threadLocal.get() != null) {
            return false;
        }
        threadLocal.set(MUTEX_PUBLISH);
        return true;
    }

    private Session updateSession() {
        if (this.mailProps.getProperty("verify") != null) {
            return initSession();
        }
        this.session = null;
        return null;
    }

    private static void verifyAddresses(Address[] addressArr) throws AddressException {
        if (addressArr != null) {
            for (Address address : addressArr) {
                if (address instanceof InternetAddress) {
                    ((InternetAddress) address).validate();
                }
            }
        }
    }

    private static InetAddress verifyHost(String str) throws IOException {
        InetAddress localHost = isEmpty(str) ? InetAddress.getLocalHost() : InetAddress.getByName(str);
        if (localHost.getCanonicalHostName().length() != 0) {
            return localHost;
        }
        throw new UnknownHostException();
    }

    private static void verifyProperties(Session session, String str) {
        session.getProperty("mail.from");
        session.getProperty("mail." + str + ".from");
        session.getProperty("mail.dsn.ret");
        session.getProperty("mail." + str + ".dsn.ret");
        session.getProperty("mail.dsn.notify");
        session.getProperty("mail." + str + ".dsn.notify");
        session.getProperty("mail." + str + ".port");
        session.getProperty("mail.user");
        session.getProperty("mail." + str + ".user");
        session.getProperty("mail." + str + ".localport");
    }

    private void verifySettings(Session session) throws Throwable {
        if (session != null) {
            try {
                Object objPut = session.getProperties().put("verify", "");
                if (!(objPut instanceof String)) {
                    if (objPut != null) {
                        verifySettings0(session, objPut.getClass().toString());
                    }
                } else {
                    String str = (String) objPut;
                    if (hasValue(str)) {
                        verifySettings0(session, str);
                    }
                }
            } catch (LinkageError e) {
                reportLinkageError(e, 4);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x0243 A[Catch: Exception -> 0x00e2, RuntimeException -> 0x00e5, TRY_LEAVE, TryCatch #10 {RuntimeException -> 0x00e5, blocks: (B:34:0x00d8, B:36:0x00de, B:42:0x00e9, B:47:0x00f4, B:49:0x00f7, B:60:0x0128, B:63:0x0132, B:65:0x015d, B:67:0x0169, B:69:0x0189, B:71:0x01bd, B:84:0x01ea, B:70:0x01a4, B:66:0x0164, B:95:0x020e, B:99:0x0215, B:101:0x021d, B:134:0x0269, B:106:0x0225, B:117:0x023d, B:119:0x0243, B:125:0x0252, B:127:0x0255, B:130:0x0261, B:132:0x0264, B:110:0x0234, B:114:0x023a, B:53:0x0107, B:54:0x0111, B:45:0x00ee), top: B:221:0x00d8 }] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0269 A[Catch: Exception -> 0x00e2, RuntimeException -> 0x00e5, TRY_LEAVE, TryCatch #10 {RuntimeException -> 0x00e5, blocks: (B:34:0x00d8, B:36:0x00de, B:42:0x00e9, B:47:0x00f4, B:49:0x00f7, B:60:0x0128, B:63:0x0132, B:65:0x015d, B:67:0x0169, B:69:0x0189, B:71:0x01bd, B:84:0x01ea, B:70:0x01a4, B:66:0x0164, B:95:0x020e, B:99:0x0215, B:101:0x021d, B:134:0x0269, B:106:0x0225, B:117:0x023d, B:119:0x0243, B:125:0x0252, B:127:0x0255, B:130:0x0261, B:132:0x0264, B:110:0x0234, B:114:0x023a, B:53:0x0107, B:54:0x0111, B:45:0x00ee), top: B:221:0x00d8 }] */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0327 A[Catch: Exception -> 0x00e2, RuntimeException -> 0x030d, TryCatch #1 {RuntimeException -> 0x030d, blocks: (B:58:0x0120, B:136:0x0271, B:150:0x02a1, B:165:0x0309, B:175:0x0319, B:173:0x0315, B:174:0x0318, B:176:0x0324, B:178:0x0327, B:180:0x0336, B:181:0x033c, B:183:0x0346, B:185:0x0349, B:186:0x034d, B:188:0x0350, B:190:0x0358, B:191:0x035b, B:192:0x037b, B:194:0x037e, B:195:0x0386, B:196:0x0392, B:197:0x0393, B:198:0x039a, B:149:0x0296, B:86:0x01f7, B:123:0x024c, B:128:0x025b, B:55:0x0112, B:57:0x011c, B:204:0x03a3, B:205:0x03a6), top: B:212:0x0112 }] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0393 A[Catch: Exception -> 0x00e2, RuntimeException -> 0x030d, TryCatch #1 {RuntimeException -> 0x030d, blocks: (B:58:0x0120, B:136:0x0271, B:150:0x02a1, B:165:0x0309, B:175:0x0319, B:173:0x0315, B:174:0x0318, B:176:0x0324, B:178:0x0327, B:180:0x0336, B:181:0x033c, B:183:0x0346, B:185:0x0349, B:186:0x034d, B:188:0x0350, B:190:0x0358, B:191:0x035b, B:192:0x037b, B:194:0x037e, B:195:0x0386, B:196:0x0392, B:197:0x0393, B:198:0x039a, B:149:0x0296, B:86:0x01f7, B:123:0x024c, B:128:0x025b, B:55:0x0112, B:57:0x011c, B:204:0x03a3, B:205:0x03a6), top: B:212:0x0112 }] */
    /* JADX WARN: Removed duplicated region for block: B:233:0x0279 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void verifySettings0(Session session, String str) throws Throwable {
        String str2;
        int length;
        String[] strArr;
        int i;
        Object andSetContextClassLoader;
        Transport transport;
        Throwable th;
        String localHost;
        Exception exc;
        Address[] invalidAddresses;
        Address[] validSentAddresses;
        String localHost2;
        String strContentTypeOf;
        MimeBodyPart mimeBodyPartCreateBodyPart;
        Address[] from;
        if (!"local".equals(str) && !"remote".equals(str) && !"limited".equals(str) && !"resolve".equals(str) && !"login".equals(str)) {
            reportError("Verify must be 'limited', local', 'resolve', 'login', or 'remote'.", new IllegalArgumentException(str), 4);
            return;
        }
        MimeMessage mimeMessage = new MimeMessage(session);
        if ("limited".equals(str)) {
            str2 = "Skipping local address check.";
        } else {
            str2 = "Local address is " + InternetAddress.getLocalAddress(session) + FilenameUtils.EXTENSION_SEPARATOR;
            try {
                Charset.forName(getEncodingName());
            } catch (RuntimeException e) {
                Exception unsupportedEncodingException = new UnsupportedEncodingException(e.toString());
                unsupportedEncodingException.initCause(e);
                reportError(str2, unsupportedEncodingException, 5);
            }
        }
        synchronized (this) {
            try {
                appendSubject(mimeMessage, head(this.subjectFormatter));
                appendSubject(mimeMessage, tail(this.subjectFormatter, ""));
                length = this.attachmentNames.length;
                strArr = new String[length];
                for (int i2 = 0; i2 < length; i2++) {
                    String strHead = head(this.attachmentNames[i2]);
                    strArr[i2] = strHead;
                    if (strHead.length() == 0) {
                        strArr[i2] = tail(this.attachmentNames[i2], "");
                    } else {
                        strArr[i2] = strArr[i2].concat(tail(this.attachmentNames[i2], ""));
                    }
                }
            } finally {
            }
        }
        setIncompleteCopy(mimeMessage);
        envelopeFor(mimeMessage, true);
        saveChangesNoContent(mimeMessage, str2);
        try {
            try {
                Address[] allRecipients = mimeMessage.getAllRecipients();
                if (allRecipients == null) {
                    allRecipients = new InternetAddress[0];
                }
                Address[] addressArr = allRecipients;
                try {
                    from = addressArr.length != 0 ? addressArr : mimeMessage.getFrom();
                } catch (MessagingException e2) {
                    try {
                        andSetContextClassLoader = getAndSetContextClassLoader(MAILHANDLER_LOADER);
                        try {
                            try {
                                transport = session.getTransport();
                                getAndSetContextClassLoader(andSetContextClassLoader);
                            } catch (MessagingException e3) {
                                throw attach(e2, e3);
                            }
                        } finally {
                            getAndSetContextClassLoader(andSetContextClassLoader);
                        }
                    } catch (RuntimeException e4) {
                        e = e4;
                        i = 4;
                        setErrorContent(mimeMessage, str, e);
                        reportError(mimeMessage, e, i);
                        return;
                    }
                }
                if (from == null || from.length == 0) {
                    Exception messagingException = new MessagingException("No recipient or from address.");
                    reportError(str2, messagingException, 4);
                    throw messagingException;
                }
                transport = session.getTransport(from[0]);
                session.getProperty("mail.transport.protocol");
                Transport transport2 = transport;
                if ("remote".equals(str) || "login".equals(str)) {
                    transport2.connect();
                    Exception exc2 = null;
                    try {
                        localHost = getLocalHost(transport2);
                        try {
                            if ("remote".equals(str)) {
                                transport2.sendMessage(mimeMessage, addressArr);
                            }
                            try {
                                transport2.close();
                                exc = null;
                            } catch (MessagingException e5) {
                                exc = e5;
                            }
                            try {
                                if ("remote".equals(str)) {
                                    reportUnexpectedSend(mimeMessage, str, null);
                                } else {
                                    verifyProperties(session, transport2.getURLName().getProtocol());
                                }
                            } catch (SendFailedException e6) {
                                e = e6;
                                invalidAddresses = e.getInvalidAddresses();
                                if (invalidAddresses != null && invalidAddresses.length != 0) {
                                    setErrorContent(mimeMessage, str, e);
                                    reportError(mimeMessage, e, 4);
                                }
                                validSentAddresses = e.getValidSentAddresses();
                                if (validSentAddresses != null && validSentAddresses.length != 0) {
                                    reportUnexpectedSend(mimeMessage, str, e);
                                }
                            } catch (MessagingException e7) {
                                e = e7;
                                if (!isMissingContent(mimeMessage, e)) {
                                    setErrorContent(mimeMessage, str, e);
                                    reportError(mimeMessage, e, 4);
                                }
                            }
                            if (exc != null) {
                                setErrorContent(mimeMessage, str, exc);
                                reportError(mimeMessage, exc, 3);
                            }
                            localHost2 = localHost;
                        } catch (Throwable th2) {
                            th = th2;
                            try {
                                transport2.close();
                            } catch (MessagingException e8) {
                                exc2 = e8;
                            }
                            try {
                                throw th;
                            } catch (SendFailedException e9) {
                                e = e9;
                                exc = exc2;
                                invalidAddresses = e.getInvalidAddresses();
                                if (invalidAddresses != null) {
                                    setErrorContent(mimeMessage, str, e);
                                    reportError(mimeMessage, e, 4);
                                }
                                validSentAddresses = e.getValidSentAddresses();
                                if (validSentAddresses != null) {
                                    reportUnexpectedSend(mimeMessage, str, e);
                                }
                                if (exc != null) {
                                }
                                localHost2 = localHost;
                                if (!"limited".equals(str)) {
                                }
                                if (addressArr.length == 0) {
                                }
                            } catch (MessagingException e10) {
                                e = e10;
                                exc = exc2;
                                if (!isMissingContent(mimeMessage, e)) {
                                }
                                if (exc != null) {
                                }
                                localHost2 = localHost;
                                if (!"limited".equals(str)) {
                                }
                                if (addressArr.length == 0) {
                                }
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        localHost = null;
                    }
                } else {
                    String protocol = transport2.getURLName().getProtocol();
                    verifyProperties(session, protocol);
                    String property = session.getProperty("mail." + protocol + ".host");
                    if (isEmpty(property)) {
                        property = session.getProperty("mail.host");
                    } else {
                        session.getProperty("mail.host");
                    }
                    localHost2 = session.getProperty("mail." + protocol + ".localhost");
                    if (isEmpty(localHost2)) {
                        localHost2 = session.getProperty("mail." + protocol + ".localaddress");
                    } else {
                        session.getProperty("mail." + protocol + ".localaddress");
                    }
                    if ("resolve".equals(str)) {
                        try {
                            String host = transport2.getURLName().getHost();
                            if (isEmpty(host)) {
                                verifyHost(property);
                            } else {
                                verifyHost(host);
                                if (!host.equalsIgnoreCase(property)) {
                                    verifyHost(property);
                                }
                            }
                        } catch (IOException e11) {
                            e = e11;
                            Exception messagingException2 = new MessagingException(str2, e);
                            setErrorContent(mimeMessage, str, messagingException2);
                            reportError(mimeMessage, messagingException2, 4);
                        } catch (RuntimeException e12) {
                            e = e12;
                            Exception messagingException22 = new MessagingException(str2, e);
                            setErrorContent(mimeMessage, str, messagingException22);
                            reportError(mimeMessage, messagingException22, 4);
                        }
                    }
                }
                if (!"limited".equals(str)) {
                    try {
                        if (!"remote".equals(str) && !"login".equals(str)) {
                            localHost2 = getLocalHost(transport2);
                        }
                        verifyHost(localHost2);
                    } catch (IOException e13) {
                        e = e13;
                        Exception messagingException3 = new MessagingException(str2, e);
                        setErrorContent(mimeMessage, str, messagingException3);
                        reportError(mimeMessage, messagingException3, 4);
                    } catch (RuntimeException e14) {
                        e = e14;
                        Exception messagingException32 = new MessagingException(str2, e);
                        setErrorContent(mimeMessage, str, messagingException32);
                        reportError(mimeMessage, messagingException32, 4);
                    }
                    try {
                        andSetContextClassLoader = getAndSetContextClassLoader(MAILHANDLER_LOADER);
                        try {
                            Multipart mimeMultipart = new MimeMultipart();
                            MimePart[] mimePartArr = new MimeBodyPart[length];
                            synchronized (this) {
                                try {
                                    strContentTypeOf = contentTypeOf(getFormatter());
                                    mimeBodyPartCreateBodyPart = createBodyPart();
                                    for (int i3 = 0; i3 < length; i3++) {
                                        MimePart mimePartCreateBodyPart = createBodyPart(i3);
                                        mimePartArr[i3] = mimePartCreateBodyPart;
                                        mimePartCreateBodyPart.setFileName(strArr[i3]);
                                        strArr[i3] = getContentType(strArr[i3]);
                                    }
                                } finally {
                                }
                            }
                            mimeBodyPartCreateBodyPart.setDescription(str);
                            setContent(mimeBodyPartCreateBodyPart, "", strContentTypeOf);
                            mimeMultipart.addBodyPart(mimeBodyPartCreateBodyPart);
                            for (int i4 = 0; i4 < length; i4++) {
                                mimePartArr[i4].setDescription(str);
                                setContent(mimePartArr[i4], "", strArr[i4]);
                            }
                            mimeMessage.setContent(mimeMultipart);
                            mimeMessage.saveChanges();
                            mimeMessage.writeTo(new ByteArrayOutputStream(MIN_HEADER_SIZE));
                        } finally {
                        }
                    } catch (IOException e15) {
                        Exception messagingException4 = new MessagingException(str2, e15);
                        setErrorContent(mimeMessage, str, messagingException4);
                        reportError(mimeMessage, messagingException4, 5);
                    }
                }
                if (addressArr.length == 0) {
                    throw new MessagingException("No recipient addresses.");
                }
                verifyAddresses(addressArr);
                Address[] from2 = mimeMessage.getFrom();
                Object sender = mimeMessage.getSender();
                if (sender instanceof InternetAddress) {
                    ((InternetAddress) sender).validate();
                }
                if (mimeMessage.getHeader(HttpHeaders.FROM, ",") != null && from2.length != 0) {
                    verifyAddresses(from2);
                    for (Address address : from2) {
                        if (address.equals(sender)) {
                            throw new MessagingException(str2, new MessagingException("Sender address '" + sender + "' equals from address."));
                        }
                    }
                } else if (sender == null) {
                    throw new MessagingException(str2, new MessagingException("No from or sender address."));
                }
                verifyAddresses(mimeMessage.getReplyTo());
            } catch (RuntimeException e16) {
                e = e16;
                i = 4;
            }
        } catch (Exception e17) {
            setErrorContent(mimeMessage, str, e17);
            reportError(mimeMessage, e17, 4);
        }
    }

    /* JADX WARN: Finally extract failed */
    private Message writeLogRecords(int i) {
        try {
            synchronized (this) {
                try {
                    if (this.size <= 0 || this.isWriting) {
                        return null;
                    }
                    this.isWriting = true;
                    try {
                        Message messageWriteLogRecords0 = writeLogRecords0();
                        this.isWriting = false;
                        if (this.size > 0) {
                            reset();
                        }
                        return messageWriteLogRecords0;
                    } catch (Throwable th) {
                        this.isWriting = false;
                        if (this.size > 0) {
                            reset();
                        }
                        throw th;
                    }
                } finally {
                }
            }
        } catch (RuntimeException e) {
            reportError(e.getMessage(), e, i);
            return null;
        } catch (Exception e2) {
            reportError(e2.getMessage(), e2, i);
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r14v0, types: [java.util.logging.LogRecord[]] */
    /* JADX WARN: Type inference failed for: r14v1, types: [java.util.Locale] */
    /* JADX WARN: Type inference failed for: r15v0, types: [java.util.logging.LogRecord] */
    /* JADX WARN: Type inference failed for: r17v0 */
    /* JADX WARN: Type inference failed for: r17v1 */
    /* JADX WARN: Type inference failed for: r17v2 */
    /* JADX WARN: Type inference failed for: r19v0 */
    /* JADX WARN: Type inference failed for: r21v0, types: [com.sun.mail.util.logging.MailHandler] */
    /* JADX WARN: Type inference failed for: r4v20, types: [java.util.logging.Filter[]] */
    /* JADX WARN: Type inference failed for: r4v21, types: [java.util.logging.Filter] */
    /* JADX WARN: Type inference failed for: r4v9, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r5v3, types: [javax.mail.internet.MimePart] */
    /* JADX WARN: Type inference failed for: r5v7 */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r7v1, types: [java.util.logging.Filter] */
    /* JADX WARN: Type inference failed for: r7v4, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r8v10 */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v12 */
    /* JADX WARN: Type inference failed for: r8v13 */
    /* JADX WARN: Type inference failed for: r8v14 */
    /* JADX WARN: Type inference failed for: r8v8, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r8v9 */
    private Message writeLogRecords0() throws Exception {
        ?? CreateBodyPart;
        int i;
        ?? r17;
        boolean z;
        sort();
        if (this.session == null) {
            initSession();
        }
        MimeMessage mimeMessage = new MimeMessage(this.session);
        int length = this.attachmentFormatters.length;
        MimeBodyPart[] mimeBodyPartArr = new MimeBodyPart[length];
        StringBuilder[] sbArr = new StringBuilder[length];
        if (length == 0) {
            mimeMessage.setDescription(descriptionFrom(getFormatter(), getFilter(), this.subjectFormatter));
            CreateBodyPart = mimeMessage;
        } else {
            mimeMessage.setDescription(descriptionFrom(this.comparator, this.pushLevel, this.pushFilter));
            CreateBodyPart = createBodyPart();
        }
        appendSubject(mimeMessage, head(this.subjectFormatter));
        Formatter formatter = getFormatter();
        ?? filter = getFilter();
        MimeBodyPart mimeBodyPart = null;
        StringBuilder sb = null;
        ?? r12 = 0;
        int i2 = 0;
        while (i2 < this.size) {
            int i3 = this.matched[i2];
            ?? r14 = this.data;
            ?? r15 = r14[i2];
            r14[i2] = mimeBodyPart;
            ?? LocaleFor = localeFor(r15);
            MimeBodyPart mimeBodyPart2 = mimeBodyPart;
            appendSubject(mimeMessage, format(this.subjectFormatter, r15));
            if (filter == 0 || i3 == -1 || length == 0 || (i3 < -1 && filter.isLoggable(r15))) {
                if (sb == null) {
                    sb = new StringBuilder();
                    sb.append(head(formatter));
                }
                sb.append(format(formatter, r15));
                if (LocaleFor != 0 && !LocaleFor.equals(r12)) {
                    appendContentLang(CreateBodyPart, LocaleFor);
                }
                r17 = filter;
                z = true;
            } else {
                r17 = mimeBodyPart2;
                z = false;
            }
            MimeBodyPart[] mimeBodyPartArr2 = mimeBodyPartArr;
            ?? r8 = r17;
            int i4 = 0;
            while (i4 < length) {
                StringBuilder[] sbArr2 = sbArr;
                ?? r4 = this.attachmentFilters[i4];
                if (r4 == 0 || r8 == r4 || i3 == i4 || (i3 < i4 && r4.isLoggable(r15))) {
                    if (r8 == 0 && r4 != 0) {
                        r8 = r4;
                    }
                    if (mimeBodyPartArr2[i4] == null) {
                        mimeBodyPartArr2[i4] = createBodyPart(i4);
                        StringBuilder sb2 = new StringBuilder();
                        sbArr2[i4] = sb2;
                        sb2.append(head(this.attachmentFormatters[i4]));
                        appendFileName(mimeBodyPartArr2[i4], head(this.attachmentNames[i4]));
                    }
                    appendFileName(mimeBodyPartArr2[i4], format(this.attachmentNames[i4], r15));
                    sbArr2[i4].append(format(this.attachmentFormatters[i4], r15));
                    if (LocaleFor != 0 && !LocaleFor.equals(r12)) {
                        appendContentLang(mimeBodyPartArr2[i4], LocaleFor);
                    }
                    z = true;
                }
                i4++;
                sbArr = sbArr2;
                r8 = r8;
            }
            StringBuilder[] sbArr3 = sbArr;
            if (!z) {
                reportFilterError(r15);
            } else if (CreateBodyPart != mimeMessage && LocaleFor != 0 && !LocaleFor.equals(r12)) {
                appendContentLang(mimeMessage, LocaleFor);
            }
            i2++;
            r12 = LocaleFor;
            mimeBodyPart = mimeBodyPart2;
            mimeBodyPartArr = mimeBodyPartArr2;
            sbArr = sbArr3;
        }
        MimeBodyPart[] mimeBodyPartArr3 = mimeBodyPartArr;
        ?? r19 = sbArr;
        MimeBodyPart mimeBodyPart3 = mimeBodyPart;
        this.size = 0;
        for (int i5 = length - 1; i5 >= 0; i5--) {
            MimeBodyPart mimeBodyPart4 = mimeBodyPartArr3[i5];
            if (mimeBodyPart4 != null) {
                appendFileName(mimeBodyPart4, tail(this.attachmentNames[i5], "err"));
                r19[i5].append(tail(this.attachmentFormatters[i5], ""));
                if (r19[i5].length() > 0) {
                    String fileName = mimeBodyPartArr3[i5].getFileName();
                    if (isEmpty(fileName)) {
                        fileName = toString(this.attachmentFormatters[i5]);
                        mimeBodyPartArr3[i5].setFileName(fileName);
                    }
                    setContent(mimeBodyPartArr3[i5], r19[i5], getContentType(fileName));
                } else {
                    setIncompleteCopy(mimeMessage);
                    mimeBodyPartArr3[i5] = mimeBodyPart3;
                }
                r19[i5] = mimeBodyPart3;
            }
        }
        if (sb != null) {
            sb.append(tail(formatter, ""));
            i = 0;
        } else {
            i = 0;
            sb = new StringBuilder(0);
        }
        appendSubject(mimeMessage, tail(this.subjectFormatter, ""));
        String strContentTypeOf = contentTypeOf(sb);
        String strContentTypeOf2 = contentTypeOf(formatter);
        if (strContentTypeOf2 != null) {
            strContentTypeOf = strContentTypeOf2;
        }
        setContent(CreateBodyPart, sb, strContentTypeOf);
        if (CreateBodyPart != mimeMessage) {
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart((BodyPart) CreateBodyPart);
            for (int i6 = i; i6 < length; i6++) {
                MimeBodyPart mimeBodyPart5 = mimeBodyPartArr3[i6];
                if (mimeBodyPart5 != null) {
                    mimeMultipart.addBodyPart(mimeBodyPart5);
                }
            }
            mimeMessage.setContent(mimeMultipart);
        }
        return mimeMessage;
    }

    @Override // java.util.logging.Handler
    public void close() {
        Message messageWriteLogRecords;
        try {
            checkAccess();
            synchronized (this) {
                try {
                    try {
                        messageWriteLogRecords = writeLogRecords(3);
                        this.logLevel = Level.OFF;
                        int i = this.capacity;
                        if (i > 0) {
                            this.capacity = -i;
                        }
                        if (this.size == 0 && this.data.length != 1) {
                            LogRecord[] logRecordArr = new LogRecord[1];
                            this.data = logRecordArr;
                            this.matched = new int[logRecordArr.length];
                        }
                    } catch (Throwable th) {
                        this.logLevel = Level.OFF;
                        if (this.capacity > 0) {
                            this.capacity = -this.capacity;
                        }
                        if (this.size == 0 && this.data.length != 1) {
                            LogRecord[] logRecordArr2 = new LogRecord[1];
                            this.data = logRecordArr2;
                            this.matched = new int[logRecordArr2.length];
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            }
            if (messageWriteLogRecords != null) {
                send(messageWriteLogRecords, false, 3);
            }
        } catch (LinkageError e) {
            reportLinkageError(e, 3);
        }
    }

    public final String contentTypeOf(CharSequence charSequence) {
        if (isEmpty(charSequence)) {
            return null;
        }
        if (charSequence.length() > 25) {
            charSequence = charSequence.subSequence(0, 25);
        }
        try {
            return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(charSequence.toString().getBytes(getEncodingName())));
        } catch (IOException e) {
            reportError(e.getMessage(), e, 5);
            return null;
        }
    }

    @Override // java.util.logging.Handler
    public void flush() {
        push(false, 2);
    }

    public final Filter[] getAttachmentFilters() {
        return (Filter[]) readOnlyAttachmentFilters().clone();
    }

    public final Formatter[] getAttachmentFormatters() {
        Formatter[] formatterArr;
        synchronized (this) {
            formatterArr = this.attachmentFormatters;
        }
        return (Formatter[]) formatterArr.clone();
    }

    public final Formatter[] getAttachmentNames() {
        Formatter[] formatterArr;
        synchronized (this) {
            formatterArr = this.attachmentNames;
        }
        return (Formatter[]) formatterArr.clone();
    }

    public final synchronized Authenticator getAuthenticator() {
        checkAccess();
        return this.auth;
    }

    public final synchronized int getCapacity() {
        return Math.abs(this.capacity);
    }

    public final synchronized Comparator<? super LogRecord> getComparator() {
        return this.comparator;
    }

    @Override // java.util.logging.Handler
    public synchronized String getEncoding() {
        return this.encoding;
    }

    @Override // java.util.logging.Handler
    public ErrorManager getErrorManager() {
        checkAccess();
        return this.errorManager;
    }

    @Override // java.util.logging.Handler
    public Filter getFilter() {
        return this.filter;
    }

    @Override // java.util.logging.Handler
    public synchronized Formatter getFormatter() {
        return this.formatter;
    }

    @Override // java.util.logging.Handler
    public Level getLevel() {
        return this.logLevel;
    }

    public final Properties getMailProperties() {
        Properties properties;
        checkAccess();
        synchronized (this) {
            properties = this.mailProps;
        }
        return (Properties) properties.clone();
    }

    public final synchronized Filter getPushFilter() {
        return this.pushFilter;
    }

    public final synchronized Level getPushLevel() {
        return this.pushLevel;
    }

    public final synchronized Formatter getSubject() {
        return this.subjectFormatter;
    }

    @Override // java.util.logging.Handler
    public boolean isLoggable(LogRecord logRecord) {
        int iIntValue;
        if (logRecord == null || logRecord.getLevel().intValue() < (iIntValue = getLevel().intValue()) || iIntValue == offValue) {
            return false;
        }
        Filter filter = getFilter();
        if (filter != null && !filter.isLoggable(logRecord)) {
            return isAttachmentLoggable(logRecord);
        }
        setMatchedPart(-1);
        return true;
    }

    public final boolean isMissingContent(Message message, Throwable th) {
        Object andSetContextClassLoader = getAndSetContextClassLoader(MAILHANDLER_LOADER);
        try {
            try {
                try {
                    message.writeTo(new ByteArrayOutputStream(MIN_HEADER_SIZE));
                } catch (Exception e) {
                    String message2 = e.getMessage();
                    if (!isEmpty(message2)) {
                        int i = 0;
                        while (th != null) {
                            if (e.getClass() == th.getClass() && message2.equals(th.getMessage())) {
                                getAndSetContextClassLoader(andSetContextClassLoader);
                                return true;
                            }
                            Throwable cause = th.getCause();
                            th = (cause == null && (th instanceof MessagingException)) ? ((MessagingException) th).getNextException() : cause;
                            i++;
                            if (i == 65536) {
                                break;
                            }
                        }
                    }
                }
                getAndSetContextClassLoader(andSetContextClassLoader);
                return false;
            } catch (RuntimeException e2) {
                throw e2;
            }
        } catch (Throwable th2) {
            getAndSetContextClassLoader(andSetContextClassLoader);
            throw th2;
        }
    }

    public void postConstruct() {
    }

    public void preDestroy() {
        push(false, 3);
    }

    @Override // java.util.logging.Handler
    public void publish(LogRecord logRecord) {
        if (!tryMutex()) {
            reportUnPublishedError(logRecord);
            return;
        }
        try {
            try {
                if (isLoggable(logRecord)) {
                    if (logRecord != null) {
                        logRecord.getSourceMethodName();
                        publish0(logRecord);
                    } else {
                        reportNullError(1);
                    }
                }
            } catch (LinkageError e) {
                reportLinkageError(e, 1);
            }
            releaseMutex();
        } catch (Throwable th) {
            releaseMutex();
            throw th;
        }
    }

    public void push() {
        push(true, 2);
    }

    @Override // java.util.logging.Handler
    public void reportError(String str, Exception exc, int i) {
        try {
            if (str != null) {
                this.errorManager.error(Level.SEVERE.getName().concat(": ").concat(str), exc, i);
            } else {
                this.errorManager.error(null, exc, i);
            }
        } catch (LinkageError e) {
            e = e;
            reportLinkageError(e, i);
        } catch (RuntimeException e2) {
            e = e2;
            reportLinkageError(e, i);
        }
    }

    public final void setAttachmentFilters(Filter... filterArr) {
        checkAccess();
        Filter[] filterArrEmptyFilterArray = filterArr.length == 0 ? emptyFilterArray() : (Filter[]) Arrays.copyOf(filterArr, filterArr.length, Filter[].class);
        synchronized (this) {
            try {
                Formatter[] formatterArr = this.attachmentFormatters;
                if (formatterArr.length != filterArrEmptyFilterArray.length) {
                    throw attachmentMismatch(formatterArr.length, filterArrEmptyFilterArray.length);
                }
                if (this.isWriting) {
                    throw new IllegalStateException();
                }
                if (this.size != 0) {
                    int i = 0;
                    while (true) {
                        if (i >= filterArrEmptyFilterArray.length) {
                            break;
                        }
                        if (filterArrEmptyFilterArray[i] != this.attachmentFilters[i]) {
                            clearMatches(i);
                            break;
                        }
                        i++;
                    }
                }
                this.attachmentFilters = filterArrEmptyFilterArray;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final void setAttachmentFormatters(Formatter... formatterArr) {
        Formatter[] formatterArrEmptyFormatterArray;
        checkAccess();
        if (formatterArr.length == 0) {
            formatterArrEmptyFormatterArray = emptyFormatterArray();
        } else {
            formatterArrEmptyFormatterArray = (Formatter[]) Arrays.copyOf(formatterArr, formatterArr.length, Formatter[].class);
            for (int i = 0; i < formatterArrEmptyFormatterArray.length; i++) {
                if (formatterArrEmptyFormatterArray[i] == null) {
                    throw new NullPointerException(atIndexMsg(i));
                }
            }
        }
        synchronized (this) {
            try {
                if (this.isWriting) {
                    throw new IllegalStateException();
                }
                this.attachmentFormatters = formatterArrEmptyFormatterArray;
                alignAttachmentFilters();
                alignAttachmentNames();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final void setAttachmentNames(String... strArr) {
        checkAccess();
        Formatter[] formatterArrEmptyFormatterArray = strArr.length == 0 ? emptyFormatterArray() : new Formatter[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            if (str == null) {
                throw new NullPointerException(atIndexMsg(i));
            }
            if (str.length() <= 0) {
                throw new IllegalArgumentException(atIndexMsg(i));
            }
            formatterArrEmptyFormatterArray[i] = TailNameFormatter.of(str);
        }
        synchronized (this) {
            try {
                Formatter[] formatterArr = this.attachmentFormatters;
                if (formatterArr.length != strArr.length) {
                    throw attachmentMismatch(formatterArr.length, strArr.length);
                }
                if (this.isWriting) {
                    throw new IllegalStateException();
                }
                this.attachmentNames = formatterArrEmptyFormatterArray;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final void setAuthenticator(Authenticator authenticator) throws Throwable {
        setAuthenticator0(authenticator);
    }

    public final synchronized void setComparator(Comparator<? super LogRecord> comparator) {
        checkAccess();
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.comparator = comparator;
    }

    @Override // java.util.logging.Handler
    public void setEncoding(String str) throws UnsupportedEncodingException {
        checkAccess();
        setEncoding0(str);
    }

    @Override // java.util.logging.Handler
    public void setErrorManager(ErrorManager errorManager) {
        checkAccess();
        setErrorManager0(errorManager);
    }

    @Override // java.util.logging.Handler
    public void setFilter(Filter filter) {
        checkAccess();
        synchronized (this) {
            try {
                if (filter != this.filter) {
                    clearMatches(-1);
                }
                this.filter = filter;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // java.util.logging.Handler
    public synchronized void setFormatter(Formatter formatter) throws SecurityException {
        checkAccess();
        if (formatter == null) {
            throw new NullPointerException();
        }
        this.formatter = formatter;
    }

    @Override // java.util.logging.Handler
    public void setLevel(Level level) {
        level.getClass();
        checkAccess();
        synchronized (this) {
            try {
                if (this.capacity > 0) {
                    this.logLevel = level;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final void setMailProperties(Properties properties) throws Throwable {
        setMailProperties0(properties);
    }

    public final synchronized void setPushFilter(Filter filter) {
        checkAccess();
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.pushFilter = filter;
    }

    public final synchronized void setPushLevel(Level level) {
        checkAccess();
        if (level == null) {
            throw new NullPointerException();
        }
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.pushLevel = level;
    }

    public final void setSubject(String str) {
        if (str != null) {
            setSubject(TailNameFormatter.of(str));
        } else {
            checkAccess();
            throw null;
        }
    }

    private static RuntimeException attachmentMismatch(int i, int i2) {
        return attachmentMismatch("Attachments mismatched, expected " + i + " but given " + i2 + FilenameUtils.EXTENSION_SEPARATOR);
    }

    private void push(boolean z, int i) {
        try {
            if (!tryMutex()) {
                reportUnPublishedError(null);
                return;
            }
            try {
                Message messageWriteLogRecords = writeLogRecords(i);
                if (messageWriteLogRecords != null) {
                    send(messageWriteLogRecords, z, i);
                }
            } catch (LinkageError e) {
                reportLinkageError(e, i);
            }
        } finally {
            releaseMutex();
        }
    }

    public final void setAuthenticator(char... cArr) throws Throwable {
        if (cArr == null) {
            setAuthenticator0(null);
        } else {
            setAuthenticator0(DefaultAuthenticator.of(new String(cArr)));
        }
    }

    public final void setSubject(Formatter formatter) {
        checkAccess();
        formatter.getClass();
        synchronized (this) {
            try {
                if (!this.isWriting) {
                    this.subjectFormatter = formatter;
                } else {
                    throw new IllegalStateException();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void reportError(Message message, Exception exc, int i) {
        try {
            try {
                this.errorManager.error(toRawString(message), exc, i);
            } catch (RuntimeException e) {
                reportError(toMsgString(e), exc, i);
            } catch (Exception e2) {
                reportError(toMsgString(e2), exc, i);
            }
        } catch (LinkageError e3) {
            reportLinkageError(e3, i);
        }
    }

    public MailHandler(int i) {
        init(null);
        this.sealed = true;
        setCapacity0(i);
    }

    private MimeBodyPart createBodyPart(int i) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setDisposition(Part.ATTACHMENT);
        mimeBodyPart.setDescription(descriptionFrom(this.attachmentFormatters[i], this.attachmentFilters[i], this.attachmentNames[i]));
        setAcceptLang(mimeBodyPart);
        return mimeBodyPart;
    }

    private String descriptionFrom(Formatter formatter, Filter filter, Formatter formatter2) {
        String name;
        StringBuilder sb = new StringBuilder("Formatted using ");
        sb.append(getClassId(formatter));
        sb.append(", filtered with ");
        if (filter == null) {
            name = "no filter";
        } else {
            name = filter.getClass().getName();
        }
        sb.append(name);
        sb.append(", and named by ");
        return AbstractC2039eV.o(sb, getClassId(formatter2), FilenameUtils.EXTENSION_SEPARATOR);
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0062, code lost:
    
        r7 = r7.getSuperclass();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final String contentTypeOf(Formatter formatter) {
        String name;
        if (formatter == null) {
            return null;
        }
        String contentType = getContentType(formatter.getClass().getName());
        if (contentType != null) {
            return contentType;
        }
        Class<?> superclass = formatter.getClass();
        while (superclass != Formatter.class) {
            try {
                name = superclass.getSimpleName();
            } catch (InternalError unused) {
                name = superclass.getName();
            }
            String lowerCase = name.toLowerCase(Locale.ENGLISH);
            int iIndexOf = lowerCase.indexOf(36) + 1;
            while (true) {
                int iIndexOf2 = lowerCase.indexOf("ml", iIndexOf);
                if (iIndexOf2 > -1) {
                    if (iIndexOf2 > 0) {
                        int i = iIndexOf2 - 1;
                        if (lowerCase.charAt(i) == 'x') {
                            return "application/xml";
                        }
                        if (iIndexOf2 > 1 && lowerCase.charAt(iIndexOf2 - 2) == 'h' && lowerCase.charAt(i) == 't') {
                            return "text/html";
                        }
                    }
                    iIndexOf = iIndexOf2 + 2;
                }
            }
        }
        return null;
    }

    public MailHandler(Properties properties) throws Throwable {
        properties.getClass();
        init(properties);
        this.sealed = true;
        setMailProperties0(properties);
    }

    public final void setAttachmentNames(Formatter... formatterArr) {
        Formatter[] formatterArrEmptyFormatterArray;
        checkAccess();
        if (formatterArr.length == 0) {
            formatterArrEmptyFormatterArray = emptyFormatterArray();
        } else {
            formatterArrEmptyFormatterArray = (Formatter[]) Arrays.copyOf(formatterArr, formatterArr.length, Formatter[].class);
        }
        for (int i = 0; i < formatterArrEmptyFormatterArray.length; i++) {
            if (formatterArrEmptyFormatterArray[i] == null) {
                throw new NullPointerException(atIndexMsg(i));
            }
        }
        synchronized (this) {
            try {
                Formatter[] formatterArr2 = this.attachmentFormatters;
                if (formatterArr2.length == formatterArrEmptyFormatterArray.length) {
                    if (!this.isWriting) {
                        this.attachmentNames = formatterArrEmptyFormatterArray;
                    } else {
                        throw new IllegalStateException();
                    }
                } else {
                    throw attachmentMismatch(formatterArr2.length, formatterArrEmptyFormatterArray.length);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private Object intern(Map<Object, Object> map, Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Object objNewInstance = obj.getClass().getName().equals(TailNameFormatter.class.getName()) ? obj : obj.getClass().getConstructor(new Class[0]).newInstance(new Object[0]);
        if (objNewInstance.getClass() == obj.getClass()) {
            Object obj2 = map.get(objNewInstance);
            if (obj2 == null) {
                boolean zEquals = objNewInstance.equals(obj);
                boolean zEquals2 = obj.equals(objNewInstance);
                if (zEquals && zEquals2) {
                    Object objPut = map.put(obj, obj);
                    if (objPut != null) {
                        reportNonDiscriminating(objNewInstance, objPut);
                        Object objRemove = map.remove(objNewInstance);
                        if (objRemove != obj) {
                            reportNonDiscriminating(objNewInstance, objRemove);
                            map.clear();
                            return obj;
                        }
                    }
                } else if (zEquals != zEquals2) {
                    reportNonSymmetric(obj, objNewInstance);
                    return obj;
                }
            } else {
                if (obj.getClass() == obj2.getClass()) {
                    return obj2;
                }
                reportNonDiscriminating(obj, obj2);
            }
        }
        return obj;
    }
}
