package com.google.common.eventbus;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Primitives;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/* JADX INFO: loaded from: classes.dex */
final class SubscriberRegistry {
    private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache;
    private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache;
    private final EventBus bus;
    private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = Maps.newConcurrentMap();

    public static final class MethodIdentifier {
        private final String name;
        private final List<Class<?>> parameterTypes;

        public MethodIdentifier(Method method) {
            this.name = method.getName();
            this.parameterTypes = Arrays.asList(method.getParameterTypes());
        }

        public boolean equals(Object obj) {
            if (obj instanceof MethodIdentifier) {
                MethodIdentifier methodIdentifier = (MethodIdentifier) obj;
                if (this.name.equals(methodIdentifier.name) && this.parameterTypes.equals(methodIdentifier.parameterTypes)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.name, this.parameterTypes);
        }
    }

    static {
        final int i = 0;
        subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build(CacheLoader.from(new Function() { // from class: com.google.common.eventbus.b
            @Override // com.google.common.base.Function
            public final Object apply(Object obj) {
                Class cls = (Class) obj;
                switch (i) {
                    case 0:
                        return SubscriberRegistry.getAnnotatedMethodsNotCached(cls);
                    default:
                        return SubscriberRegistry.lambda$static$0(cls);
                }
            }
        }));
        final int i2 = 1;
        flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(CacheLoader.from(new Function() { // from class: com.google.common.eventbus.b
            @Override // com.google.common.base.Function
            public final Object apply(Object obj) {
                Class cls = (Class) obj;
                switch (i2) {
                    case 0:
                        return SubscriberRegistry.getAnnotatedMethodsNotCached(cls);
                    default:
                        return SubscriberRegistry.lambda$static$0(cls);
                }
            }
        }));
    }

    public SubscriberRegistry(EventBus eventBus) {
        this.bus = (EventBus) Preconditions.checkNotNull(eventBus);
    }

    private Multimap<Class<?>, Subscriber> findAllSubscribers(Object obj) {
        HashMultimap hashMultimapCreate = HashMultimap.create();
        UnmodifiableIterator<Method> it = getAnnotatedMethods(obj.getClass()).iterator();
        while (it.hasNext()) {
            Method next = it.next();
            hashMultimapCreate.put(next.getParameterTypes()[0], Subscriber.create(this.bus, obj, next));
        }
        return hashMultimapCreate;
    }

    @VisibleForTesting
    public static ImmutableSet<Class<?>> flattenHierarchy(Class<?> cls) {
        return flattenHierarchyCache.getUnchecked(cls);
    }

    private static ImmutableList<Method> getAnnotatedMethods(Class<?> cls) {
        try {
            return subscriberMethodsCache.getUnchecked(cls);
        } catch (UncheckedExecutionException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw new IllegalArgumentException(e.getCause().getMessage(), e.getCause());
            }
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> cls) {
        Set setRawTypes = TypeToken.of((Class) cls).getTypes().rawTypes();
        HashMap map = new HashMap();
        Iterator it = setRawTypes.iterator();
        while (it.hasNext()) {
            for (Method method : ((Class) it.next()).getDeclaredMethods()) {
                if (method.isAnnotationPresent(Subscribe.class) && !method.isSynthetic()) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Preconditions.checkArgument(parameterTypes.length == 1, "Method %s has @Subscribe annotation but has %s parameters. Subscriber methods must have exactly 1 parameter.", (Object) method, parameterTypes.length);
                    Preconditions.checkArgument(!parameterTypes[0].isPrimitive(), "@Subscribe method %s's parameter is %s. Subscriber methods cannot accept primitives. Consider changing the parameter to %s.", method, parameterTypes[0].getName(), Primitives.wrap(parameterTypes[0]).getSimpleName());
                    MethodIdentifier methodIdentifier = new MethodIdentifier(method);
                    if (!map.containsKey(methodIdentifier)) {
                        map.put(methodIdentifier, method);
                    }
                }
            }
        }
        return ImmutableList.copyOf(map.values());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ImmutableSet lambda$static$0(Class cls) {
        return ImmutableSet.copyOf((Collection) TypeToken.of(cls).getTypes().rawTypes());
    }

    public Iterator<Subscriber> getSubscribers(Object obj) {
        ImmutableSet<Class<?>> immutableSetFlattenHierarchy = flattenHierarchy(obj.getClass());
        ArrayList arrayListNewArrayListWithCapacity = Lists.newArrayListWithCapacity(immutableSetFlattenHierarchy.size());
        UnmodifiableIterator<Class<?>> it = immutableSetFlattenHierarchy.iterator();
        while (it.hasNext()) {
            CopyOnWriteArraySet<Subscriber> copyOnWriteArraySet = this.subscribers.get(it.next());
            if (copyOnWriteArraySet != null) {
                arrayListNewArrayListWithCapacity.add(copyOnWriteArraySet.iterator());
            }
        }
        return Iterators.concat(arrayListNewArrayListWithCapacity.iterator());
    }

    @VisibleForTesting
    public Set<Subscriber> getSubscribersForTesting(Class<?> cls) {
        return (Set) MoreObjects.firstNonNull(this.subscribers.get(cls), ImmutableSet.of());
    }

    public void register(Object obj) {
        for (Map.Entry<Class<?>, Collection<Subscriber>> entry : findAllSubscribers(obj).asMap().entrySet()) {
            Class<?> key = entry.getKey();
            Collection<Subscriber> value = entry.getValue();
            CopyOnWriteArraySet<Subscriber> copyOnWriteArraySet = this.subscribers.get(key);
            if (copyOnWriteArraySet == null) {
                CopyOnWriteArraySet<Subscriber> copyOnWriteArraySet2 = new CopyOnWriteArraySet<>();
                copyOnWriteArraySet = (CopyOnWriteArraySet) MoreObjects.firstNonNull(this.subscribers.putIfAbsent(key, copyOnWriteArraySet2), copyOnWriteArraySet2);
            }
            copyOnWriteArraySet.addAll(value);
        }
    }

    public void unregister(Object obj) {
        for (Map.Entry<Class<?>, Collection<Subscriber>> entry : findAllSubscribers(obj).asMap().entrySet()) {
            Class<?> key = entry.getKey();
            Collection<Subscriber> value = entry.getValue();
            CopyOnWriteArraySet<Subscriber> copyOnWriteArraySet = this.subscribers.get(key);
            if (copyOnWriteArraySet == null || !copyOnWriteArraySet.removeAll(value)) {
                throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + obj + " registered?");
            }
        }
    }
}
