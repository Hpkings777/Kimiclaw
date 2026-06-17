package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.annotations.VisibleForTesting;
import java.lang.Thread;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public final class UncaughtExceptionHandlers {

    @VisibleForTesting
    public static final class Exiter implements Thread.UncaughtExceptionHandler {
        private static final LazyLogger logger = new LazyLogger(Exiter.class);
        private final RuntimeWrapper runtime;

        public Exiter(RuntimeWrapper runtimeWrapper) {
            this.runtime = runtimeWrapper;
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            try {
                logger.get().log(Level.SEVERE, String.format(Locale.ROOT, "Caught an exception in %s.  Shutting down.", thread), th);
            } catch (Throwable th2) {
                try {
                    System.err.println(th.getMessage());
                    System.err.println(th2.getMessage());
                } finally {
                    this.runtime.exit(1);
                }
            }
        }
    }

    @VisibleForTesting
    public interface RuntimeWrapper {
        void exit(int i);
    }

    private UncaughtExceptionHandlers() {
    }

    public static Thread.UncaughtExceptionHandler systemExit() {
        final Runtime runtime = Runtime.getRuntime();
        Objects.requireNonNull(runtime);
        return new Exiter(new RuntimeWrapper() { // from class: com.google.common.util.concurrent.l
            @Override // com.google.common.util.concurrent.UncaughtExceptionHandlers.RuntimeWrapper
            public final void exit(int i) {
                runtime.exit(i);
            }
        });
    }
}
