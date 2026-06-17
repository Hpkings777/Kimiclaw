package com.sun.mail.util.logging;

import java.lang.reflect.UndeclaredThrowableException;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/* JADX INFO: loaded from: classes.dex */
public class CollectorFormatter extends Formatter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long INIT_TIME = System.currentTimeMillis();
    private final Comparator<? super LogRecord> comparator;
    private long count;
    private final String fmt;
    private final Formatter formatter;
    private LogRecord last;
    private long thrown;
    private long generation = 1;
    private long minMillis = INIT_TIME;
    private long maxMillis = Long.MIN_VALUE;

    public CollectorFormatter() {
        String name = getClass().getName();
        this.fmt = initFormat(name);
        this.formatter = initFormatter(name);
        this.comparator = initComparator(name);
    }

    private synchronized boolean accept(LogRecord logRecord, LogRecord logRecord2) {
        try {
            long millis = logRecord2.getMillis();
            Throwable thrown = logRecord2.getThrown();
            if (this.last != logRecord) {
                return false;
            }
            long j = this.count + 1;
            this.count = j;
            if (j != 1) {
                this.minMillis = Math.min(this.minMillis, millis);
            } else {
                this.minMillis = millis;
            }
            this.maxMillis = Math.max(this.maxMillis, millis);
            if (thrown != null) {
                this.thrown++;
            }
            return true;
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized boolean acceptAndUpdate(LogRecord logRecord, LogRecord logRecord2) {
        if (!accept(logRecord, logRecord2)) {
            return false;
        }
        this.last = logRecord2;
        return true;
    }

    private String formatRecord(Handler handler, boolean z) {
        LogRecord logRecord;
        long j;
        long j2;
        long j3;
        long j4;
        long j5;
        long jCurrentTimeMillis;
        String head;
        String message;
        String tail;
        long j6;
        MessageFormat messageFormat;
        ResourceBundle resourceBundle;
        synchronized (this) {
            try {
                logRecord = this.last;
                j = this.count;
                j2 = this.generation;
                j3 = this.thrown;
                j4 = this.minMillis;
                j5 = this.maxMillis;
                jCurrentTimeMillis = System.currentTimeMillis();
                if (j == 0) {
                    j5 = jCurrentTimeMillis;
                }
                if (z) {
                    reset(j5);
                }
            } finally {
            }
        }
        Formatter formatter = this.formatter;
        if (formatter != null) {
            synchronized (formatter) {
                try {
                    head = formatter.getHead(handler);
                    message = logRecord != null ? formatter.format(logRecord) : "";
                    tail = formatter.getTail(handler);
                } finally {
                }
            }
        } else {
            head = "";
            message = logRecord != null ? formatMessage(logRecord) : "";
            tail = "";
        }
        String str = head;
        String str2 = message;
        Locale locale = null;
        if (logRecord != null && (resourceBundle = logRecord.getResourceBundle()) != null) {
            locale = resourceBundle.getLocale();
        }
        Locale locale2 = locale;
        if (locale2 == null) {
            messageFormat = new MessageFormat(this.fmt);
            j6 = j2;
        } else {
            j6 = j2;
            messageFormat = new MessageFormat(this.fmt, locale2);
        }
        String strFinish = finish(str);
        String strFinish2 = finish(str2);
        String strFinish3 = finish(tail);
        Long lValueOf = Long.valueOf(j);
        Long lValueOf2 = Long.valueOf(j - 1);
        Long lValueOf3 = Long.valueOf(j3);
        Long lValueOf4 = Long.valueOf(j - j3);
        Long lValueOf5 = Long.valueOf(j4);
        Long lValueOf6 = Long.valueOf(j5);
        Long lValueOf7 = Long.valueOf(j5 - j4);
        long j7 = INIT_TIME;
        return messageFormat.format(new Object[]{strFinish, strFinish2, strFinish3, lValueOf, lValueOf2, lValueOf3, lValueOf4, lValueOf5, lValueOf6, lValueOf7, Long.valueOf(j7), Long.valueOf(jCurrentTimeMillis), Long.valueOf(jCurrentTimeMillis - j7), Long.valueOf(j6)});
    }

    private Comparator<? super LogRecord> initComparator(String str) {
        String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".comparator"));
        String strFromLogManager2 = LogManagerProperties.fromLogManager(str.concat(".comparator.reverse"));
        if (strFromLogManager != null) {
            try {
                if (strFromLogManager.length() != 0) {
                    if (!"null".equalsIgnoreCase(strFromLogManager)) {
                        Comparator<? super LogRecord> comparatorNewComparator = LogManagerProperties.newComparator(strFromLogManager);
                        return Boolean.parseBoolean(strFromLogManager2) ? LogManagerProperties.reverseOrder(comparatorNewComparator) : comparatorNewComparator;
                    }
                    if (strFromLogManager2 == null) {
                        return null;
                    }
                    throw new IllegalArgumentException("No comparator to reverse.");
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new UndeclaredThrowableException(e2);
            }
        }
        if (strFromLogManager2 == null) {
            return (Comparator) Comparator.class.cast(SeverityComparator.getInstance());
        }
        throw new IllegalArgumentException("No comparator to reverse.");
    }

    private String initFormat(String str) {
        String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".format"));
        return (strFromLogManager == null || strFromLogManager.length() == 0) ? "{0}{1}{2}{4,choice,-1#|0#|0<... {4,number,integer} more}\n" : strFromLogManager;
    }

    private Formatter initFormatter(String str) {
        String strFromLogManager = LogManagerProperties.fromLogManager(str.concat(".formatter"));
        if (strFromLogManager == null || strFromLogManager.length() == 0) {
            return (Formatter) Formatter.class.cast(new CompactFormatter());
        }
        if ("null".equalsIgnoreCase(strFromLogManager)) {
            return null;
        }
        try {
            return LogManagerProperties.newFormatter(strFromLogManager);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new UndeclaredThrowableException(e2);
        }
    }

    private synchronized LogRecord peek() {
        return this.last;
    }

    private synchronized void reset(long j) {
        try {
            if (this.last != null) {
                this.last = null;
                this.generation++;
            }
            this.count = 0L;
            this.thrown = 0L;
            this.minMillis = j;
            this.maxMillis = Long.MIN_VALUE;
        } catch (Throwable th) {
            throw th;
        }
    }

    public LogRecord apply(LogRecord logRecord, LogRecord logRecord2) {
        if (logRecord == null || logRecord2 == null) {
            throw null;
        }
        Comparator<? super LogRecord> comparator = this.comparator;
        return (comparator == null || comparator.compare(logRecord, logRecord2) < 0) ? logRecord2 : logRecord;
    }

    public String finish(String str) {
        return str.trim();
    }

    @Override // java.util.logging.Formatter
    public String format(LogRecord logRecord) {
        boolean zAccept;
        logRecord.getClass();
        do {
            LogRecord logRecordPeek = peek();
            LogRecord logRecordApply = apply(logRecordPeek != null ? logRecordPeek : logRecord, logRecord);
            if (logRecordPeek != logRecordApply) {
                logRecordApply.getSourceMethodName();
                zAccept = acceptAndUpdate(logRecordPeek, logRecordApply);
            } else {
                zAccept = accept(logRecordPeek, logRecord);
            }
        } while (!zAccept);
        return "";
    }

    @Override // java.util.logging.Formatter
    public String getTail(Handler handler) {
        super.getTail(handler);
        return formatRecord(handler, true);
    }

    public String toString() {
        try {
            return formatRecord(null, false);
        } catch (RuntimeException unused) {
            return super.toString();
        }
    }

    public CollectorFormatter(String str) {
        String name = getClass().getName();
        this.fmt = str == null ? initFormat(name) : str;
        this.formatter = initFormatter(name);
        this.comparator = initComparator(name);
    }

    public CollectorFormatter(String str, Formatter formatter, Comparator<? super LogRecord> comparator) {
        this.fmt = str == null ? initFormat(getClass().getName()) : str;
        this.formatter = formatter;
        this.comparator = comparator;
    }
}
