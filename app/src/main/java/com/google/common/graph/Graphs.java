package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.graph.ImmutableGraph;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@Beta
public final class Graphs extends GraphsBridgeMethods {

    public static final class NodeAndRemainingSuccessors<N> {
        final N node;
        Queue<N> remainingSuccessors;

        public NodeAndRemainingSuccessors(N n) {
            this.node = n;
        }
    }

    public enum NodeVisitState {
        PENDING,
        COMPLETE
    }

    public static final class TransposedGraph<N> extends ForwardingGraph<N> {
        private final Graph<N> graph;

        /* JADX INFO: renamed from: com.google.common.graph.Graphs$TransposedGraph$1, reason: invalid class name */
        public class AnonymousClass1 extends IncidentEdgeSet<N> {
            public AnonymousClass1(BaseGraph baseGraph, Object obj) {
                super(baseGraph, obj);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ EndpointPair lambda$iterator$0(EndpointPair endpointPair) {
                return EndpointPair.of((Graph<?>) TransposedGraph.this.delegate(), endpointPair.nodeV(), endpointPair.nodeU());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<EndpointPair<N>> iterator() {
                return Iterators.transform(TransposedGraph.this.delegate().incidentEdges(this.node).iterator(), new e(this, 1));
            }
        }

        public TransposedGraph(Graph<N> graph) {
            this.graph = graph;
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public boolean hasEdgeConnecting(N n, N n2) {
            return delegate().hasEdgeConnecting(n2, n);
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public int inDegree(N n) {
            return delegate().outDegree(n);
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public Set<EndpointPair<N>> incidentEdges(N n) {
            return new AnonymousClass1(this, n);
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public int outDegree(N n) {
            return delegate().inDegree(n);
        }

        @Override // com.google.common.graph.ForwardingGraph
        public Graph<N> delegate() {
            return this.graph;
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.AbstractGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public boolean hasEdgeConnecting(EndpointPair<N> endpointPair) {
            return delegate().hasEdgeConnecting(Graphs.transpose(endpointPair));
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.BaseGraph, com.google.common.graph.PredecessorsFunction, com.google.common.graph.Graph
        public Set<N> predecessors(N n) {
            return delegate().successors((Object) n);
        }

        @Override // com.google.common.graph.ForwardingGraph, com.google.common.graph.BaseGraph, com.google.common.graph.SuccessorsFunction
        public Set<N> successors(N n) {
            return delegate().predecessors((Object) n);
        }
    }

    public static final class TransposedNetwork<N, E> extends ForwardingNetwork<N, E> {
        private final Network<N, E> network;

        public TransposedNetwork(Network<N, E> network) {
            this.network = network;
        }

        @Override // com.google.common.graph.ForwardingNetwork
        public Network<N, E> delegate() {
            return this.network;
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public E edgeConnectingOrNull(N n, N n2) {
            return delegate().edgeConnectingOrNull(n2, n);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public Set<E> edgesConnecting(N n, N n2) {
            return delegate().edgesConnecting(n2, n);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public boolean hasEdgeConnecting(N n, N n2) {
            return delegate().hasEdgeConnecting(n2, n);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public int inDegree(N n) {
            return delegate().outDegree(n);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.Network
        public Set<E> inEdges(N n) {
            return delegate().outEdges(n);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.Network
        public EndpointPair<N> incidentNodes(E e) {
            EndpointPair<N> endpointPairIncidentNodes = delegate().incidentNodes(e);
            return EndpointPair.of((Network<?, ?>) this.network, (Object) endpointPairIncidentNodes.nodeV(), (Object) endpointPairIncidentNodes.nodeU());
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public int outDegree(N n) {
            return delegate().inDegree(n);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.Network
        public Set<E> outEdges(N n) {
            return delegate().inEdges(n);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public E edgeConnectingOrNull(EndpointPair<N> endpointPair) {
            return delegate().edgeConnectingOrNull(Graphs.transpose(endpointPair));
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public Set<E> edgesConnecting(EndpointPair<N> endpointPair) {
            return delegate().edgesConnecting(Graphs.transpose(endpointPair));
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.AbstractNetwork, com.google.common.graph.Network
        public boolean hasEdgeConnecting(EndpointPair<N> endpointPair) {
            return delegate().hasEdgeConnecting(Graphs.transpose(endpointPair));
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.Network, com.google.common.graph.PredecessorsFunction, com.google.common.graph.Graph
        public Set<N> predecessors(N n) {
            return delegate().successors((Object) n);
        }

        @Override // com.google.common.graph.ForwardingNetwork, com.google.common.graph.Network, com.google.common.graph.SuccessorsFunction
        public Set<N> successors(N n) {
            return delegate().predecessors((Object) n);
        }
    }

    public static final class TransposedValueGraph<N, V> extends ForwardingValueGraph<N, V> {
        private final ValueGraph<N, V> graph;

        public TransposedValueGraph(ValueGraph<N, V> valueGraph) {
            this.graph = valueGraph;
        }

        @Override // com.google.common.graph.ForwardingValueGraph
        public ValueGraph<N, V> delegate() {
            return this.graph;
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.ValueGraph
        public V edgeValueOrDefault(N n, N n2, V v) {
            return delegate().edgeValueOrDefault(n2, n, v);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public boolean hasEdgeConnecting(N n, N n2) {
            return delegate().hasEdgeConnecting(n2, n);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public int inDegree(N n) {
            return delegate().outDegree(n);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public int outDegree(N n) {
            return delegate().inDegree(n);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.ValueGraph
        public V edgeValueOrDefault(EndpointPair<N> endpointPair, V v) {
            return delegate().edgeValueOrDefault(Graphs.transpose(endpointPair), v);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.AbstractValueGraph, com.google.common.graph.AbstractBaseGraph, com.google.common.graph.BaseGraph
        public boolean hasEdgeConnecting(EndpointPair<N> endpointPair) {
            return delegate().hasEdgeConnecting(Graphs.transpose(endpointPair));
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.PredecessorsFunction, com.google.common.graph.Graph
        public Set<N> predecessors(N n) {
            return delegate().successors((Object) n);
        }

        @Override // com.google.common.graph.ForwardingValueGraph, com.google.common.graph.BaseGraph, com.google.common.graph.SuccessorsFunction
        public Set<N> successors(N n) {
            return delegate().predecessors((Object) n);
        }
    }

    private Graphs() {
    }

    private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object obj, Object obj2) {
        return graph.isDirected() || !Objects.equals(obj2, obj);
    }

    public static int checkNonNegative(int i) {
        Preconditions.checkArgument(i >= 0, "Not true that %s is non-negative.", i);
        return i;
    }

    public static int checkPositive(int i) {
        Preconditions.checkArgument(i > 0, "Not true that %s is positive.", i);
        return i;
    }

    public static <N> MutableGraph<N> copyOf(Graph<N> graph) {
        MutableGraph<N> mutableGraph = (MutableGraph<N>) GraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
        Iterator<N> it = graph.nodes().iterator();
        while (it.hasNext()) {
            mutableGraph.addNode(it.next());
        }
        for (EndpointPair<N> endpointPair : graph.edges()) {
            mutableGraph.putEdge(endpointPair.nodeU(), endpointPair.nodeV());
        }
        return mutableGraph;
    }

    public static <N> boolean hasCycle(Graph<N> graph) {
        int size = graph.edges().size();
        if (size == 0) {
            return false;
        }
        if (!graph.isDirected() && size >= graph.nodes().size()) {
            return true;
        }
        HashMap mapNewHashMapWithExpectedSize = Maps.newHashMapWithExpectedSize(graph.nodes().size());
        Iterator<N> it = graph.nodes().iterator();
        while (it.hasNext()) {
            if (subgraphHasCycle(graph, mapNewHashMapWithExpectedSize, it.next())) {
                return true;
            }
        }
        return false;
    }

    public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> iterable) {
        StandardMutableGraph standardMutableGraph = iterable instanceof Collection ? (MutableGraph<N>) GraphBuilder.from(graph).expectedNodeCount(((Collection) iterable).size()).build() : (MutableGraph<N>) GraphBuilder.from(graph).build();
        Iterator<? extends N> it = iterable.iterator();
        while (it.hasNext()) {
            standardMutableGraph.addNode(it.next());
        }
        for (N n : standardMutableGraph.nodes()) {
            for (N n2 : graph.successors((Object) n)) {
                if (standardMutableGraph.nodes().contains(n2)) {
                    standardMutableGraph.putEdge(n, n2);
                }
            }
        }
        return standardMutableGraph;
    }

    public static <N> ImmutableSet<N> reachableNodes(Graph<N> graph, N n) {
        Preconditions.checkArgument(graph.nodes().contains(n), "Node %s is not an element of this graph.", n);
        return ImmutableSet.copyOf(Traverser.forGraph(graph).breadthFirst(n));
    }

    private static <N> boolean subgraphHasCycle(Graph<N> graph, Map<Object, NodeVisitState> map, N n) {
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.addLast(new NodeAndRemainingSuccessors(n));
        while (!arrayDeque.isEmpty()) {
            NodeAndRemainingSuccessors nodeAndRemainingSuccessors = (NodeAndRemainingSuccessors) arrayDeque.removeLast();
            NodeAndRemainingSuccessors nodeAndRemainingSuccessors2 = (NodeAndRemainingSuccessors) arrayDeque.peekLast();
            arrayDeque.addLast(nodeAndRemainingSuccessors);
            N n2 = nodeAndRemainingSuccessors.node;
            N n3 = nodeAndRemainingSuccessors2 == null ? null : nodeAndRemainingSuccessors2.node;
            if (nodeAndRemainingSuccessors.remainingSuccessors == null) {
                NodeVisitState nodeVisitState = map.get(n2);
                if (nodeVisitState == NodeVisitState.COMPLETE) {
                    arrayDeque.removeLast();
                } else {
                    NodeVisitState nodeVisitState2 = NodeVisitState.PENDING;
                    if (nodeVisitState == nodeVisitState2) {
                        return true;
                    }
                    map.put(n2, nodeVisitState2);
                    nodeAndRemainingSuccessors.remainingSuccessors = new ArrayDeque(graph.successors((Object) n2));
                }
            }
            if (!nodeAndRemainingSuccessors.remainingSuccessors.isEmpty()) {
                N nRemove = nodeAndRemainingSuccessors.remainingSuccessors.remove();
                if (canTraverseWithoutReusingEdge(graph, nRemove, n3)) {
                    arrayDeque.addLast(new NodeAndRemainingSuccessors(nRemove));
                }
            }
            arrayDeque.removeLast();
            map.put(n2, NodeVisitState.COMPLETE);
        }
        return false;
    }

    public static <N> ImmutableGraph<N> transitiveClosure(Graph<N> graph) {
        ImmutableGraph.Builder<N1> builderImmutable = GraphBuilder.from(graph).allowsSelfLoops(true).immutable();
        if (graph.isDirected()) {
            for (N n : graph.nodes()) {
                UnmodifiableIterator it = reachableNodes((Graph) graph, (Object) n).iterator();
                while (it.hasNext()) {
                    builderImmutable.putEdge(n, it.next());
                }
            }
        } else {
            HashSet hashSet = new HashSet();
            for (N n2 : graph.nodes()) {
                if (!hashSet.contains(n2)) {
                    ImmutableSet immutableSetReachableNodes = reachableNodes((Graph) graph, (Object) n2);
                    hashSet.addAll(immutableSetReachableNodes);
                    int i = 1;
                    for (Object obj : immutableSetReachableNodes) {
                        int i2 = i + 1;
                        Iterator it2 = Iterables.limit(immutableSetReachableNodes, i).iterator();
                        while (it2.hasNext()) {
                            builderImmutable.putEdge(obj, it2.next());
                        }
                        i = i2;
                    }
                }
            }
        }
        return builderImmutable.build();
    }

    public static <N> Graph<N> transpose(Graph<N> graph) {
        return !graph.isDirected() ? graph : graph instanceof TransposedGraph ? ((TransposedGraph) graph).graph : new TransposedGraph(graph);
    }

    public static long checkNonNegative(long j) {
        Preconditions.checkArgument(j >= 0, "Not true that %s is non-negative.", j);
        return j;
    }

    public static long checkPositive(long j) {
        Preconditions.checkArgument(j > 0, "Not true that %s is positive.", j);
        return j;
    }

    public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> valueGraph) {
        if (!valueGraph.isDirected()) {
            return valueGraph;
        }
        if (valueGraph instanceof TransposedValueGraph) {
            return ((TransposedValueGraph) valueGraph).graph;
        }
        return new TransposedValueGraph(valueGraph);
    }

    public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> valueGraph) {
        MutableValueGraph<N, V> mutableValueGraph = (MutableValueGraph<N, V>) ValueGraphBuilder.from(valueGraph).expectedNodeCount(valueGraph.nodes().size()).build();
        Iterator<N> it = valueGraph.nodes().iterator();
        while (it.hasNext()) {
            mutableValueGraph.addNode(it.next());
        }
        for (EndpointPair<N> endpointPair : valueGraph.edges()) {
            N nNodeU = endpointPair.nodeU();
            N nNodeV = endpointPair.nodeV();
            V vEdgeValueOrDefault = valueGraph.edgeValueOrDefault(endpointPair.nodeU(), endpointPair.nodeV(), null);
            Objects.requireNonNull(vEdgeValueOrDefault);
            mutableValueGraph.putEdgeValue(nNodeU, nNodeV, vEdgeValueOrDefault);
        }
        return mutableValueGraph;
    }

    public static boolean hasCycle(Network<?, ?> network) {
        if (network.isDirected() || !network.allowsParallelEdges() || network.edges().size() <= network.asGraph().edges().size()) {
            return hasCycle(network.asGraph());
        }
        return true;
    }

    public static <N, E> Network<N, E> transpose(Network<N, E> network) {
        if (!network.isDirected()) {
            return network;
        }
        if (network instanceof TransposedNetwork) {
            return ((TransposedNetwork) network).network;
        }
        return new TransposedNetwork(network);
    }

    public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> valueGraph, Iterable<? extends N> iterable) {
        StandardMutableValueGraph standardMutableValueGraph;
        if (iterable instanceof Collection) {
            standardMutableValueGraph = (MutableValueGraph<N, V>) ValueGraphBuilder.from(valueGraph).expectedNodeCount(((Collection) iterable).size()).build();
        } else {
            standardMutableValueGraph = (MutableValueGraph<N, V>) ValueGraphBuilder.from(valueGraph).build();
        }
        Iterator<? extends N> it = iterable.iterator();
        while (it.hasNext()) {
            standardMutableValueGraph.addNode(it.next());
        }
        for (N n : standardMutableValueGraph.nodes()) {
            for (N n2 : valueGraph.successors((Object) n)) {
                if (standardMutableValueGraph.nodes().contains(n2)) {
                    V vEdgeValueOrDefault = valueGraph.edgeValueOrDefault(n, n2, null);
                    Objects.requireNonNull(vEdgeValueOrDefault);
                    standardMutableValueGraph.putEdgeValue(n, n2, vEdgeValueOrDefault);
                }
            }
        }
        return standardMutableValueGraph;
    }

    public static <N> EndpointPair<N> transpose(EndpointPair<N> endpointPair) {
        return endpointPair.isOrdered() ? EndpointPair.ordered(endpointPair.target(), endpointPair.source()) : endpointPair;
    }

    public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network) {
        MutableNetwork<N, E> mutableNetwork = (MutableNetwork<N, E>) NetworkBuilder.from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
        Iterator<N> it = network.nodes().iterator();
        while (it.hasNext()) {
            mutableNetwork.addNode(it.next());
        }
        for (E e : network.edges()) {
            EndpointPair<N> endpointPairIncidentNodes = network.incidentNodes(e);
            mutableNetwork.addEdge(endpointPairIncidentNodes.nodeU(), endpointPairIncidentNodes.nodeV(), e);
        }
        return mutableNetwork;
    }

    public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> iterable) {
        StandardMutableNetwork standardMutableNetwork;
        if (iterable instanceof Collection) {
            standardMutableNetwork = (MutableNetwork<N, E>) NetworkBuilder.from(network).expectedNodeCount(((Collection) iterable).size()).build();
        } else {
            standardMutableNetwork = (MutableNetwork<N, E>) NetworkBuilder.from(network).build();
        }
        Iterator<? extends N> it = iterable.iterator();
        while (it.hasNext()) {
            standardMutableNetwork.addNode(it.next());
        }
        for (E e : standardMutableNetwork.nodes()) {
            for (E e2 : network.outEdges(e)) {
                N nAdjacentNode = network.incidentNodes(e2).adjacentNode(e);
                if (standardMutableNetwork.nodes().contains(nAdjacentNode)) {
                    standardMutableNetwork.addEdge(e, nAdjacentNode, e2);
                }
            }
        }
        return standardMutableNetwork;
    }
}
