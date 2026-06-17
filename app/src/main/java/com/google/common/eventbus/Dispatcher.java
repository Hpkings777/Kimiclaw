package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/* JADX INFO: loaded from: classes.dex */
abstract class Dispatcher {

    public static final class ImmediateDispatcher extends Dispatcher {
        private static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();

        private ImmediateDispatcher() {
        }

        @Override // com.google.common.eventbus.Dispatcher
        public void dispatch(Object obj, Iterator<Subscriber> it) {
            Preconditions.checkNotNull(obj);
            while (it.hasNext()) {
                it.next().dispatchEvent(obj);
            }
        }
    }

    public static final class LegacyAsyncDispatcher extends Dispatcher {
        private final ConcurrentLinkedQueue<EventWithSubscriber> queue;

        public static final class EventWithSubscriber {
            private final Object event;
            private final Subscriber subscriber;

            private EventWithSubscriber(Object obj, Subscriber subscriber) {
                this.event = obj;
                this.subscriber = subscriber;
            }
        }

        private LegacyAsyncDispatcher() {
            this.queue = new ConcurrentLinkedQueue<>();
        }

        @Override // com.google.common.eventbus.Dispatcher
        public void dispatch(Object obj, Iterator<Subscriber> it) {
            Preconditions.checkNotNull(obj);
            while (it.hasNext()) {
                this.queue.add(new EventWithSubscriber(obj, it.next()));
            }
            while (true) {
                EventWithSubscriber eventWithSubscriberPoll = this.queue.poll();
                if (eventWithSubscriberPoll == null) {
                    return;
                } else {
                    eventWithSubscriberPoll.subscriber.dispatchEvent(eventWithSubscriberPoll.event);
                }
            }
        }
    }

    public static final class PerThreadQueuedDispatcher extends Dispatcher {
        private final ThreadLocal<Boolean> dispatching;
        private final ThreadLocal<Queue<Event>> queue;

        public static final class Event {
            private final Object event;
            private final Iterator<Subscriber> subscribers;

            private Event(Object obj, Iterator<Subscriber> it) {
                this.event = obj;
                this.subscribers = it;
            }
        }

        private PerThreadQueuedDispatcher() {
            this.queue = new ThreadLocal<Queue<Event>>() { // from class: com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.1
                @Override // java.lang.ThreadLocal
                public Queue<Event> initialValue() {
                    return new ArrayDeque();
                }
            };
            this.dispatching = new ThreadLocal<Boolean>() { // from class: com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.lang.ThreadLocal
                public Boolean initialValue() {
                    return Boolean.FALSE;
                }
            };
        }

        @Override // com.google.common.eventbus.Dispatcher
        public void dispatch(Object obj, Iterator<Subscriber> it) {
            Preconditions.checkNotNull(obj);
            Preconditions.checkNotNull(it);
            Queue<Event> queue = this.queue.get();
            Objects.requireNonNull(queue);
            Queue<Event> queue2 = queue;
            queue2.offer(new Event(obj, it));
            if (this.dispatching.get().booleanValue()) {
                return;
            }
            this.dispatching.set(Boolean.TRUE);
            while (true) {
                try {
                    Event eventPoll = queue2.poll();
                    if (eventPoll == null) {
                        return;
                    }
                    while (eventPoll.subscribers.hasNext()) {
                        ((Subscriber) eventPoll.subscribers.next()).dispatchEvent(eventPoll.event);
                    }
                } finally {
                    this.dispatching.remove();
                    this.queue.remove();
                }
            }
        }
    }

    public static Dispatcher immediate() {
        return ImmediateDispatcher.INSTANCE;
    }

    public static Dispatcher legacyAsync() {
        return new LegacyAsyncDispatcher();
    }

    public static Dispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher();
    }

    public abstract void dispatch(Object obj, Iterator<Subscriber> it);
}
