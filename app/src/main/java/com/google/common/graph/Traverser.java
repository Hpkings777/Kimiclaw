package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import defpackage.C4693xT0;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
@Beta
public abstract class Traverser<N> {
    private final SuccessorsFunction<N> successorFunction;

    public enum InsertionOrder {
        FRONT { // from class: com.google.common.graph.Traverser.InsertionOrder.1
            @Override // com.google.common.graph.Traverser.InsertionOrder
            public <T> void insertInto(Deque<T> deque, T t) {
                deque.addFirst(t);
            }
        },
        BACK { // from class: com.google.common.graph.Traverser.InsertionOrder.2
            @Override // com.google.common.graph.Traverser.InsertionOrder
            public <T> void insertInto(Deque<T> deque, T t) {
                deque.addLast(t);
            }
        };

        public abstract <T> void insertInto(Deque<T> deque, T t);
    }

    public static abstract class Traversal<N> {
        final SuccessorsFunction<N> successorFunction;

        public Traversal(SuccessorsFunction<N> successorsFunction) {
            this.successorFunction = successorsFunction;
        }

        public static <N> Traversal<N> inGraph(SuccessorsFunction<N> successorsFunction) {
            final HashSet hashSet = new HashSet();
            return new Traversal<N>(successorsFunction) { // from class: com.google.common.graph.Traverser.Traversal.1
                @Override // com.google.common.graph.Traverser.Traversal
                public N visitNext(Deque<Iterator<? extends N>> deque) {
                    Iterator<? extends N> first = deque.getFirst();
                    while (first.hasNext()) {
                        N next = first.next();
                        Objects.requireNonNull(next);
                        if (hashSet.add(next)) {
                            return next;
                        }
                    }
                    deque.removeFirst();
                    return null;
                }
            };
        }

        public static <N> Traversal<N> inTree(SuccessorsFunction<N> successorsFunction) {
            return new Traversal<N>(successorsFunction) { // from class: com.google.common.graph.Traverser.Traversal.2
                @Override // com.google.common.graph.Traverser.Traversal
                public N visitNext(Deque<Iterator<? extends N>> deque) {
                    Iterator<? extends N> first = deque.getFirst();
                    if (first.hasNext()) {
                        return (N) Preconditions.checkNotNull(first.next());
                    }
                    deque.removeFirst();
                    return null;
                }
            };
        }

        private Iterator<N> topDown(Iterator<? extends N> it, final InsertionOrder insertionOrder) {
            final ArrayDeque arrayDeque = new ArrayDeque();
            arrayDeque.add(it);
            return new AbstractIterator<N>(this) { // from class: com.google.common.graph.Traverser.Traversal.3
                final /* synthetic */ Traversal this$0;

                {
                    this.this$0 = this;
                }

                /* JADX WARN: Type inference fix 'apply assigned field type' failed
                java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
                	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
                	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
                	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
                 */
                @Override // com.google.common.collect.AbstractIterator
                public N computeNext() {
                    do {
                        N n = (N) this.this$0.visitNext(arrayDeque);
                        if (n != null) {
                            Iterator<? extends N> it2 = this.this$0.successorFunction.successors(n).iterator();
                            if (it2.hasNext()) {
                                insertionOrder.insertInto(arrayDeque, it2);
                            }
                            return n;
                        }
                    } while (!arrayDeque.isEmpty());
                    return endOfData();
                }
            };
        }

        public final Iterator<N> breadthFirst(Iterator<? extends N> it) {
            return topDown(it, InsertionOrder.BACK);
        }

        public final Iterator<N> postOrder(Iterator<? extends N> it) {
            final ArrayDeque arrayDeque = new ArrayDeque();
            final ArrayDeque arrayDeque2 = new ArrayDeque();
            arrayDeque2.add(it);
            return new AbstractIterator<N>(this) { // from class: com.google.common.graph.Traverser.Traversal.4
                final /* synthetic */ Traversal this$0;

                {
                    this.this$0 = this;
                }

                /* JADX WARN: Type inference fix 'apply assigned field type' failed
                java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
                	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
                	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
                	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
                 */
                @Override // com.google.common.collect.AbstractIterator
                public N computeNext() {
                    while (true) {
                        N n = (N) this.this$0.visitNext(arrayDeque2);
                        if (n == null) {
                            return !arrayDeque.isEmpty() ? (N) arrayDeque.pop() : endOfData();
                        }
                        Iterator<? extends N> it2 = this.this$0.successorFunction.successors(n).iterator();
                        if (!it2.hasNext()) {
                            return n;
                        }
                        arrayDeque2.addFirst(it2);
                        arrayDeque.push(n);
                    }
                }
            };
        }

        public final Iterator<N> preOrder(Iterator<? extends N> it) {
            return topDown(it, InsertionOrder.FRONT);
        }

        public abstract N visitNext(Deque<Iterator<? extends N>> deque);
    }

    public static <N> Traverser<N> forGraph(final SuccessorsFunction<N> successorsFunction) {
        return new Traverser<N>(successorsFunction) { // from class: com.google.common.graph.Traverser.1
            @Override // com.google.common.graph.Traverser
            public Traversal<N> newTraversal() {
                return Traversal.inGraph(successorsFunction);
            }
        };
    }

    public static <N> Traverser<N> forTree(final SuccessorsFunction<N> successorsFunction) {
        if (successorsFunction instanceof BaseGraph) {
            Preconditions.checkArgument(((BaseGraph) successorsFunction).isDirected(), "Undirected graphs can never be trees.");
        }
        if (successorsFunction instanceof Network) {
            Preconditions.checkArgument(((Network) successorsFunction).isDirected(), "Undirected networks can never be trees.");
        }
        return new Traverser<N>(successorsFunction) { // from class: com.google.common.graph.Traverser.2
            @Override // com.google.common.graph.Traverser
            public Traversal<N> newTraversal() {
                return Traversal.inTree(successorsFunction);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Iterator lambda$breadthFirst$0(ImmutableSet immutableSet) {
        return newTraversal().breadthFirst(immutableSet.iterator());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Iterator lambda$depthFirstPostOrder$0(ImmutableSet immutableSet) {
        return newTraversal().postOrder(immutableSet.iterator());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Iterator lambda$depthFirstPreOrder$0(ImmutableSet immutableSet) {
        return newTraversal().preOrder(immutableSet.iterator());
    }

    private ImmutableSet<N> validate(Iterable<? extends N> iterable) {
        ImmutableSet<N> immutableSetCopyOf = ImmutableSet.copyOf(iterable);
        UnmodifiableIterator<N> it = immutableSetCopyOf.iterator();
        while (it.hasNext()) {
            this.successorFunction.successors(it.next());
        }
        return immutableSetCopyOf;
    }

    public final Iterable<N> breadthFirst(N n) {
        return breadthFirst((Iterable) ImmutableSet.of(n));
    }

    public final Iterable<N> depthFirstPostOrder(N n) {
        return depthFirstPostOrder((Iterable) ImmutableSet.of(n));
    }

    public final Iterable<N> depthFirstPreOrder(N n) {
        return depthFirstPreOrder((Iterable) ImmutableSet.of(n));
    }

    public abstract Traversal<N> newTraversal();

    private Traverser(SuccessorsFunction<N> successorsFunction) {
        this.successorFunction = (SuccessorsFunction) Preconditions.checkNotNull(successorsFunction);
    }

    public final Iterable<N> breadthFirst(Iterable<? extends N> iterable) {
        return new C4693xT0(validate(iterable), 1, this);
    }

    public final Iterable<N> depthFirstPostOrder(Iterable<? extends N> iterable) {
        return new C4693xT0(validate(iterable), 2, this);
    }

    public final Iterable<N> depthFirstPreOrder(Iterable<? extends N> iterable) {
        return new C4693xT0(validate(iterable), 0, this);
    }
}
