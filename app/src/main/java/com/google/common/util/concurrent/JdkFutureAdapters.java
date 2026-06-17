package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public final class JdkFutureAdapters {

    public static final class ListenableFutureAdapter<V> extends ForwardingFuture<V> implements ListenableFuture<V> {
        private static final Executor defaultAdapterExecutor;
        private static final ThreadFactory threadFactory;
        private final Executor adapterExecutor;
        private final Future<V> delegate;
        private final ExecutionList executionList;
        private final AtomicBoolean hasListeners;

        static {
            ThreadFactory threadFactoryBuild = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ListenableFutureAdapter-thread-%d").build();
            threadFactory = threadFactoryBuild;
            defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactoryBuild);
        }

        public ListenableFutureAdapter(Future<V> future) {
            this(future, defaultAdapterExecutor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addListener$0() {
            try {
                Uninterruptibles.getUninterruptibly(this.delegate);
            } catch (Throwable unused) {
            }
            this.executionList.execute();
        }

        @Override // com.google.common.util.concurrent.ListenableFuture
        public void addListener(Runnable runnable, Executor executor) {
            this.executionList.add(runnable, executor);
            if (this.hasListeners.compareAndSet(false, true)) {
                if (this.delegate.isDone()) {
                    this.executionList.execute();
                } else {
                    this.adapterExecutor.execute(new b(this, 1));
                }
            }
        }

        public ListenableFutureAdapter(Future<V> future, Executor executor) {
            this.executionList = new ExecutionList();
            this.hasListeners = new AtomicBoolean(false);
            this.delegate = (Future) Preconditions.checkNotNull(future);
            this.adapterExecutor = (Executor) Preconditions.checkNotNull(executor);
        }

        @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        public Future<V> delegate() {
            return this.delegate;
        }
    }

    private JdkFutureAdapters() {
    }

    public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future) {
        return future instanceof ListenableFuture ? (ListenableFuture) future : new ListenableFutureAdapter(future);
    }

    public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future, Executor executor) {
        Preconditions.checkNotNull(executor);
        if (future instanceof ListenableFuture) {
            return (ListenableFuture) future;
        }
        return new ListenableFutureAdapter(future, executor);
    }
}
