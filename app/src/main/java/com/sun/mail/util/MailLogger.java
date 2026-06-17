package com.sun.mail.util;

import defpackage.AbstractC4671xI0;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;

/* JADX INFO: loaded from: classes.dex */
public final class MailLogger {
    private final boolean debug;
    private final Logger logger;
    private final PrintStream out;
    private final String prefix;

    public MailLogger(String str, String str2, boolean z, PrintStream printStream) {
        this.logger = Logger.getLogger(str);
        this.prefix = str2;
        this.debug = z;
        this.out = printStream == null ? System.out : printStream;
    }

    private void debugOut(String str) {
        if (this.prefix == null) {
            this.out.println(str);
            return;
        }
        this.out.println(this.prefix + ": " + str);
    }

    private void ifDebugOut(String str) {
        if (this.debug) {
            debugOut(str);
        }
    }

    private StackTraceElement inferCaller() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        int i = 0;
        while (i < stackTrace.length && !isLoggerImplFrame(stackTrace[i].getClassName())) {
            i++;
        }
        while (i < stackTrace.length) {
            StackTraceElement stackTraceElement = stackTrace[i];
            if (!isLoggerImplFrame(stackTraceElement.getClassName())) {
                return stackTraceElement;
            }
            i++;
        }
        return new StackTraceElement(MailLogger.class.getName(), "log", MailLogger.class.getName(), -1);
    }

    private boolean isLoggerImplFrame(String str) {
        return MailLogger.class.getName().equals(str);
    }

    private String packageOf(Class<?> cls) {
        Package r0 = cls.getPackage();
        if (r0 != null) {
            return r0.getName();
        }
        String name = cls.getName();
        int iLastIndexOf = name.lastIndexOf(46);
        return iLastIndexOf > 0 ? name.substring(0, iLastIndexOf) : "";
    }

    public void config(String str) {
        log(Level.CONFIG, str);
    }

    public void fine(String str) {
        log(Level.FINE, str);
    }

    public void finer(String str) {
        log(Level.FINER, str);
    }

    public void finest(String str) {
        log(Level.FINEST, str);
    }

    public MailLogger getLogger(String str, String str2) {
        return new MailLogger(str, str2, this.debug, this.out);
    }

    public MailLogger getSubLogger(String str, String str2) {
        return new MailLogger(this.logger.getName() + "." + str, str2, this.debug, this.out);
    }

    public boolean isLoggable(Level level) {
        return this.debug || this.logger.isLoggable(level);
    }

    public void log(Level level, String str) {
        ifDebugOut(str);
        if (this.logger.isLoggable(level)) {
            StackTraceElement stackTraceElementInferCaller = inferCaller();
            this.logger.logp(level, stackTraceElementInferCaller.getClassName(), stackTraceElementInferCaller.getMethodName(), str);
        }
    }

    public void logf(Level level, String str, Object... objArr) {
        String str2 = String.format(str, objArr);
        ifDebugOut(str2);
        this.logger.log(level, str2);
    }

    public MailLogger getLogger(Class<?> cls, String str) {
        return new MailLogger(cls, str, this.debug, this.out);
    }

    public MailLogger getSubLogger(String str, String str2, boolean z) {
        return new MailLogger(this.logger.getName() + "." + str, str2, z, this.out);
    }

    public void log(Level level, String str, Object obj) {
        if (this.debug) {
            str = MessageFormat.format(str, obj);
            debugOut(str);
        }
        String str2 = str;
        if (this.logger.isLoggable(level)) {
            StackTraceElement stackTraceElementInferCaller = inferCaller();
            this.logger.logp(level, stackTraceElementInferCaller.getClassName(), stackTraceElementInferCaller.getMethodName(), str2, obj);
        }
    }

    public MailLogger(Class<?> cls, String str, boolean z, PrintStream printStream) {
        this.logger = Logger.getLogger(packageOf(cls));
        this.prefix = str;
        this.debug = z;
        this.out = printStream == null ? System.out : printStream;
    }

    public void log(Level level, String str, Object... objArr) {
        if (this.debug) {
            str = MessageFormat.format(str, objArr);
            debugOut(str);
        }
        String str2 = str;
        if (this.logger.isLoggable(level)) {
            StackTraceElement stackTraceElementInferCaller = inferCaller();
            this.logger.logp(level, stackTraceElementInferCaller.getClassName(), stackTraceElementInferCaller.getMethodName(), str2, objArr);
        }
    }

    public MailLogger(Class<?> cls, String str, String str2, boolean z, PrintStream printStream) {
        this.logger = Logger.getLogger(packageOf(cls) + "." + str);
        this.prefix = str2;
        this.debug = z;
        this.out = printStream == null ? System.out : printStream;
    }

    public void log(Level level, String str, Throwable th) {
        if (this.debug) {
            if (th != null) {
                debugOut(AbstractC4671xI0.i(str, ", THROW: "));
                th.printStackTrace(this.out);
            } else {
                debugOut(str);
            }
        }
        if (this.logger.isLoggable(level)) {
            StackTraceElement stackTraceElementInferCaller = inferCaller();
            this.logger.logp(level, stackTraceElementInferCaller.getClassName(), stackTraceElementInferCaller.getMethodName(), str, th);
        }
    }

    @Deprecated
    public MailLogger(String str, String str2, Session session) {
        this(str, str2, session.getDebug(), session.getDebugOut());
    }

    @Deprecated
    public MailLogger(Class<?> cls, String str, Session session) {
        this(cls, str, session.getDebug(), session.getDebugOut());
    }
}
