package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
@GwtCompatible
final class LazyLogger {
    private final Object lock = new Object();
    private volatile Logger logger;
    private final String loggerName;

    public LazyLogger(Class<?> cls) {
        this.loggerName = cls.getName();
    }

    public Logger get() {
        Logger logger = this.logger;
        if (logger != null) {
            return logger;
        }
        synchronized (this.lock) {
            try {
                Logger logger2 = this.logger;
                if (logger2 != null) {
                    return logger2;
                }
                Logger logger3 = Logger.getLogger(this.loggerName);
                this.logger = logger3;
                return logger3;
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
