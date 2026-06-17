package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenerCallQueue;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Service;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@J2ktIncompatible
@GwtIncompatible
public abstract class AbstractService implements Service {
    private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_RUNNING_EVENT;
    private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_STARTING_EVENT;
    private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_NEW_EVENT;
    private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_RUNNING_EVENT;
    private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STARTING_EVENT;
    private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STOPPING_EVENT;
    private static final ListenerCallQueue.Event<Service.Listener> STARTING_EVENT = new ListenerCallQueue.Event<Service.Listener>() { // from class: com.google.common.util.concurrent.AbstractService.1
        public String toString() {
            return "starting()";
        }

        @Override // com.google.common.util.concurrent.ListenerCallQueue.Event
        public void call(Service.Listener listener) {
            listener.starting();
        }
    };
    private static final ListenerCallQueue.Event<Service.Listener> RUNNING_EVENT = new ListenerCallQueue.Event<Service.Listener>() { // from class: com.google.common.util.concurrent.AbstractService.2
        public String toString() {
            return "running()";
        }

        @Override // com.google.common.util.concurrent.ListenerCallQueue.Event
        public void call(Service.Listener listener) {
            listener.running();
        }
    };
    private final Monitor monitor = new Monitor();
    private final Monitor.Guard isStartable = new IsStartableGuard();
    private final Monitor.Guard isStoppable = new IsStoppableGuard();
    private final Monitor.Guard hasReachedRunning = new HasReachedRunningGuard();
    private final Monitor.Guard isStopped = new IsStoppedGuard();
    private final ListenerCallQueue<Service.Listener> listeners = new ListenerCallQueue<>();
    private volatile StateSnapshot snapshot = new StateSnapshot(Service.State.NEW);

    /* JADX INFO: renamed from: com.google.common.util.concurrent.AbstractService$6, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$util$concurrent$Service$State;

        static {
            int[] iArr = new int[Service.State.values().length];
            $SwitchMap$com$google$common$util$concurrent$Service$State = iArr;
            try {
                iArr[Service.State.NEW.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.STARTING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.RUNNING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.STOPPING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.TERMINATED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.FAILED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public final class HasReachedRunningGuard extends Monitor.Guard {
        public HasReachedRunningGuard() {
            super(AbstractService.this.monitor);
        }

        @Override // com.google.common.util.concurrent.Monitor.Guard
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0;
        }
    }

    public final class IsStartableGuard extends Monitor.Guard {
        public IsStartableGuard() {
            super(AbstractService.this.monitor);
        }

        @Override // com.google.common.util.concurrent.Monitor.Guard
        public boolean isSatisfied() {
            return AbstractService.this.state() == Service.State.NEW;
        }
    }

    public final class IsStoppableGuard extends Monitor.Guard {
        public IsStoppableGuard() {
            super(AbstractService.this.monitor);
        }

        @Override // com.google.common.util.concurrent.Monitor.Guard
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0;
        }
    }

    public final class IsStoppedGuard extends Monitor.Guard {
        public IsStoppedGuard() {
            super(AbstractService.this.monitor);
        }

        @Override // com.google.common.util.concurrent.Monitor.Guard
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(Service.State.TERMINATED) >= 0;
        }
    }

    public static final class StateSnapshot {
        final Throwable failure;
        final boolean shutdownWhenStartupFinishes;
        final Service.State state;

        public StateSnapshot(Service.State state) {
            this(state, false, null);
        }

        public Service.State externalState() {
            return (this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING) ? Service.State.STOPPING : this.state;
        }

        public Throwable failureCause() {
            Service.State state = this.state;
            Preconditions.checkState(state == Service.State.FAILED, "failureCause() is only valid if the service has failed, service is %s", state);
            Throwable th = this.failure;
            Objects.requireNonNull(th);
            return th;
        }

        public StateSnapshot(Service.State state, boolean z, Throwable th) {
            Preconditions.checkArgument(!z || state == Service.State.STARTING, "shutdownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", state);
            Preconditions.checkArgument((th != null) == (state == Service.State.FAILED), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", state, th);
            this.state = state;
            this.shutdownWhenStartupFinishes = z;
            this.failure = th;
        }
    }

    static {
        Service.State state = Service.State.STARTING;
        STOPPING_FROM_STARTING_EVENT = stoppingEvent(state);
        Service.State state2 = Service.State.RUNNING;
        STOPPING_FROM_RUNNING_EVENT = stoppingEvent(state2);
        TERMINATED_FROM_NEW_EVENT = terminatedEvent(Service.State.NEW);
        TERMINATED_FROM_STARTING_EVENT = terminatedEvent(state);
        TERMINATED_FROM_RUNNING_EVENT = terminatedEvent(state2);
        TERMINATED_FROM_STOPPING_EVENT = terminatedEvent(Service.State.STOPPING);
    }

    private void checkCurrentState(Service.State state) {
        Service.State state2 = state();
        if (state2 != state) {
            if (state2 == Service.State.FAILED) {
                throw new IllegalStateException("Expected the service " + this + " to be " + state + ", but the service has FAILED", failureCause());
            }
            throw new IllegalStateException("Expected the service " + this + " to be " + state + ", but was " + state2);
        }
    }

    private void dispatchListenerEvents() throws Exception {
        if (this.monitor.isOccupiedByCurrentThread()) {
            return;
        }
        this.listeners.dispatch();
    }

    private void enqueueFailedEvent(final Service.State state, final Throwable th) {
        this.listeners.enqueue(new ListenerCallQueue.Event<Service.Listener>(this) { // from class: com.google.common.util.concurrent.AbstractService.5
            final /* synthetic */ AbstractService this$0;

            {
                this.this$0 = this;
            }

            public String toString() {
                return "failed({from = " + state + ", cause = " + th + "})";
            }

            @Override // com.google.common.util.concurrent.ListenerCallQueue.Event
            public void call(Service.Listener listener) {
                listener.failed(state, th);
            }
        });
    }

    private void enqueueRunningEvent() {
        this.listeners.enqueue(RUNNING_EVENT);
    }

    private void enqueueStartingEvent() {
        this.listeners.enqueue(STARTING_EVENT);
    }

    private void enqueueStoppingEvent(Service.State state) {
        if (state == Service.State.STARTING) {
            this.listeners.enqueue(STOPPING_FROM_STARTING_EVENT);
        } else {
            if (state != Service.State.RUNNING) {
                throw new AssertionError();
            }
            this.listeners.enqueue(STOPPING_FROM_RUNNING_EVENT);
        }
    }

    private void enqueueTerminatedEvent(Service.State state) {
        switch (AnonymousClass6.$SwitchMap$com$google$common$util$concurrent$Service$State[state.ordinal()]) {
            case 1:
                this.listeners.enqueue(TERMINATED_FROM_NEW_EVENT);
                return;
            case 2:
                this.listeners.enqueue(TERMINATED_FROM_STARTING_EVENT);
                return;
            case 3:
                this.listeners.enqueue(TERMINATED_FROM_RUNNING_EVENT);
                return;
            case 4:
                this.listeners.enqueue(TERMINATED_FROM_STOPPING_EVENT);
                return;
            case 5:
            case 6:
                throw new AssertionError();
            default:
                return;
        }
    }

    private static ListenerCallQueue.Event<Service.Listener> stoppingEvent(final Service.State state) {
        return new ListenerCallQueue.Event<Service.Listener>() { // from class: com.google.common.util.concurrent.AbstractService.4
            public String toString() {
                return "stopping({from = " + state + "})";
            }

            @Override // com.google.common.util.concurrent.ListenerCallQueue.Event
            public void call(Service.Listener listener) {
                listener.stopping(state);
            }
        };
    }

    private static ListenerCallQueue.Event<Service.Listener> terminatedEvent(final Service.State state) {
        return new ListenerCallQueue.Event<Service.Listener>() { // from class: com.google.common.util.concurrent.AbstractService.3
            public String toString() {
                return "terminated({from = " + state + "})";
            }

            @Override // com.google.common.util.concurrent.ListenerCallQueue.Event
            public void call(Service.Listener listener) {
                listener.terminated(state);
            }
        };
    }

    @Override // com.google.common.util.concurrent.Service
    public final void addListener(Service.Listener listener, Executor executor) {
        this.listeners.addListener(listener, executor);
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning() {
        this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
        try {
            checkCurrentState(Service.State.RUNNING);
        } finally {
            this.monitor.leave();
        }
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated() {
        this.monitor.enterWhenUninterruptibly(this.isStopped);
        try {
            checkCurrentState(Service.State.TERMINATED);
        } finally {
            this.monitor.leave();
        }
    }

    public void doCancelStart() {
    }

    public abstract void doStart();

    public abstract void doStop();

    @Override // com.google.common.util.concurrent.Service
    public final Throwable failureCause() {
        return this.snapshot.failureCause();
    }

    @Override // com.google.common.util.concurrent.Service
    public final boolean isRunning() {
        return state() == Service.State.RUNNING;
    }

    public final void notifyFailed(Throwable th) {
        Preconditions.checkNotNull(th);
        this.monitor.enter();
        try {
            Service.State state = state();
            int i = AnonymousClass6.$SwitchMap$com$google$common$util$concurrent$Service$State[state.ordinal()];
            if (i != 1) {
                if (i == 2 || i == 3 || i == 4) {
                    this.snapshot = new StateSnapshot(Service.State.FAILED, false, th);
                    enqueueFailedEvent(state, th);
                } else if (i != 5) {
                }
                return;
            }
            throw new IllegalStateException("Failed while in state:" + state, th);
        } finally {
            this.monitor.leave();
            dispatchListenerEvents();
        }
    }

    public final void notifyStarted() throws Exception {
        this.monitor.enter();
        try {
            if (this.snapshot.state != Service.State.STARTING) {
                IllegalStateException illegalStateException = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
                notifyFailed(illegalStateException);
                throw illegalStateException;
            }
            if (this.snapshot.shutdownWhenStartupFinishes) {
                this.snapshot = new StateSnapshot(Service.State.STOPPING);
                doStop();
            } else {
                this.snapshot = new StateSnapshot(Service.State.RUNNING);
                enqueueRunningEvent();
            }
            this.monitor.leave();
            dispatchListenerEvents();
        } catch (Throwable th) {
            this.monitor.leave();
            dispatchListenerEvents();
            throw th;
        }
    }

    public final void notifyStopped() throws Exception {
        this.monitor.enter();
        try {
            Service.State state = state();
            switch (AnonymousClass6.$SwitchMap$com$google$common$util$concurrent$Service$State[state.ordinal()]) {
                case 1:
                case 5:
                case 6:
                    throw new IllegalStateException("Cannot notifyStopped() when the service is " + state);
                case 2:
                case 3:
                case 4:
                    this.snapshot = new StateSnapshot(Service.State.TERMINATED);
                    enqueueTerminatedEvent(state);
                    break;
            }
        } finally {
            this.monitor.leave();
            dispatchListenerEvents();
        }
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service startAsync() {
        if (!this.monitor.enterIf(this.isStartable)) {
            throw new IllegalStateException("Service " + this + " has already been started");
        }
        try {
            this.snapshot = new StateSnapshot(Service.State.STARTING);
            enqueueStartingEvent();
            doStart();
            return this;
        } catch (Throwable th) {
            try {
                Platform.restoreInterruptIfIsInterruptedException(th);
                notifyFailed(th);
                return this;
            } finally {
                this.monitor.leave();
                dispatchListenerEvents();
            }
        }
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service.State state() {
        return this.snapshot.externalState();
    }

    @Override // com.google.common.util.concurrent.Service
    public final Service stopAsync() {
        if (this.monitor.enterIf(this.isStoppable)) {
            try {
                Service.State state = state();
                switch (AnonymousClass6.$SwitchMap$com$google$common$util$concurrent$Service$State[state.ordinal()]) {
                    case 1:
                        this.snapshot = new StateSnapshot(Service.State.TERMINATED);
                        enqueueTerminatedEvent(Service.State.NEW);
                        break;
                    case 2:
                        Service.State state2 = Service.State.STARTING;
                        this.snapshot = new StateSnapshot(state2, true, null);
                        enqueueStoppingEvent(state2);
                        doCancelStart();
                        break;
                    case 3:
                        this.snapshot = new StateSnapshot(Service.State.STOPPING);
                        enqueueStoppingEvent(Service.State.RUNNING);
                        doStop();
                        break;
                    case 4:
                    case 5:
                    case 6:
                        throw new AssertionError("isStoppable is incorrectly implemented, saw: " + state);
                }
                return this;
            } catch (Throwable th) {
                try {
                    Platform.restoreInterruptIfIsInterruptedException(th);
                    notifyFailed(th);
                } finally {
                    this.monitor.leave();
                    dispatchListenerEvents();
                }
            }
        }
        return this;
    }

    public String toString() {
        return getClass().getSimpleName() + " [" + state() + "]";
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitRunning(long j, TimeUnit timeUnit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, j, timeUnit)) {
            try {
                checkCurrentState(Service.State.RUNNING);
            } finally {
                this.monitor.leave();
            }
        } else {
            throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state.");
        }
    }

    @Override // com.google.common.util.concurrent.Service
    public final void awaitTerminated(long j, TimeUnit timeUnit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.isStopped, j, timeUnit)) {
            try {
                checkCurrentState(Service.State.TERMINATED);
            } finally {
                this.monitor.leave();
            }
        } else {
            throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. Current state: " + state());
        }
    }
}
