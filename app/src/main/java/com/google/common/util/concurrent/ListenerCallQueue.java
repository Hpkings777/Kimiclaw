package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
final class ListenerCallQueue<L> {
    private static final LazyLogger logger = new LazyLogger(ListenerCallQueue.class);
    private final List<PerListenerQueue<L>> listeners = Collections.synchronizedList(new ArrayList());

    public interface Event<L> {
        void call(L l);
    }

    public static final class PerListenerQueue<L> implements Runnable {
        final Executor executor;
        boolean isThreadScheduled;
        final L listener;
        final Queue<Event<L>> waitQueue = new ArrayDeque();
        final Queue<Object> labelQueue = new ArrayDeque();

        public PerListenerQueue(L l, Executor executor) {
            this.listener = (L) Preconditions.checkNotNull(l);
            this.executor = (Executor) Preconditions.checkNotNull(executor);
        }

        public synchronized void add(Event<L> event, Object obj) {
            this.waitQueue.add(event);
            this.labelQueue.add(obj);
        }

        public void dispatch() throws Exception {
            boolean z;
            synchronized (this) {
                try {
                    if (this.isThreadScheduled) {
                        z = false;
                    } else {
                        z = true;
                        this.isThreadScheduled = true;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (z) {
                try {
                    this.executor.execute(this);
                } catch (Exception e) {
                    synchronized (this) {
                        this.isThreadScheduled = false;
                        ListenerCallQueue.logger.get().log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, (Throwable) e);
                        throw e;
                    }
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:14:0x0025, code lost:
        
            r2.call(r9.listener);
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x002d, code lost:
        
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x002e, code lost:
        
            com.google.common.util.concurrent.ListenerCallQueue.logger.get().log(java.util.logging.Level.SEVERE, "Exception while executing callback: " + r9.listener + " " + r3, (java.lang.Throwable) r2);
         */
        /* JADX WARN: Removed duplicated region for block: B:28:0x0062  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() throws Throwable {
            boolean z;
            Throwable th;
            while (true) {
                boolean z2 = true;
                try {
                    synchronized (this) {
                        try {
                            Preconditions.checkState(this.isThreadScheduled);
                            Event<L> eventPoll = this.waitQueue.poll();
                            Object objPoll = this.labelQueue.poll();
                            if (eventPoll == null) {
                                this.isThreadScheduled = false;
                                try {
                                    return;
                                } catch (Throwable th2) {
                                    th = th2;
                                    z = false;
                                }
                            }
                        } catch (Throwable th3) {
                            z = true;
                            th = th3;
                        }
                        while (true) {
                            try {
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        }
                    }
                    try {
                        throw th;
                    } catch (Throwable th5) {
                        boolean z3 = z;
                        th = th5;
                        z2 = z3;
                        if (z2) {
                            synchronized (this) {
                                this.isThreadScheduled = false;
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    if (z2) {
                    }
                    throw th;
                }
            }
        }
    }

    private void enqueueHelper(Event<L> event, Object obj) {
        Preconditions.checkNotNull(event, "event");
        Preconditions.checkNotNull(obj, "label");
        synchronized (this.listeners) {
            try {
                Iterator<PerListenerQueue<L>> it = this.listeners.iterator();
                while (it.hasNext()) {
                    it.next().add(event, obj);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void addListener(L l, Executor executor) {
        Preconditions.checkNotNull(l, "listener");
        Preconditions.checkNotNull(executor, "executor");
        this.listeners.add(new PerListenerQueue<>(l, executor));
    }

    public void dispatch() throws Exception {
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).dispatch();
        }
    }

    public void enqueue(Event<L> event) {
        enqueueHelper(event, event);
    }

    public void enqueue(Event<L> event, String str) {
        enqueueHelper(event, str);
    }
}
