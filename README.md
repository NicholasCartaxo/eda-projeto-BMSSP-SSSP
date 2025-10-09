# Breaking the Sorting Barrier for Directed Single-Source Shortest Path
Implementation, documentation, and experimentation of the **Bounded Multi-Source Shortest Path** (**BMSSP**) as an algorithm that solves the **Single Source Shortest Path** (**SSSP**) problem, in comparison with **Dijkstra**.

This repository uses the article [Breaking the Sorting Barrier for Directed Single-Source Shortest Paths](https://arxiv.org/abs/2504.17033) as a basis to implement and document **BMSSP**, solving the single-source shortest path problem. In addition, an experimental comparison is made with **Dijkstra** to analyze the promised time complexity improvement for **SSSP**.

## Introduction
Shortest path algorithms identify the shortest path between points in a graph. The shortest path refers to the sum of edge weights (cost, time, or distance). This problem is extremely important for many applications, and the efficiency with which the shortest path is found is crucial for the proper functioning of flow networks, which apply to logistics, digital network routing, and social network recommendation algorithms. The newly proposed **BMSSP** algorithm identifies single-source shortest paths and breaks **Dijkstra**’s time barrier. The lack of implementations and documentation motivated this study. For this analysis, the group implemented the program in Java, documented its functionality, and conducted efficiency comparison tests with **Dijkstra**’s shortest path algorithm.

## Objective
Finding shortest paths from a single source in directed graphs with non-negative weights is a central problem in computer science. In this context, the **Dijkstra** algorithm has become the standard method for solving the single-source shortest path problem due to its efficiency.  
The traditional **Dijkstra** algorithm has a time complexity of $O(m \log n)$, where **n** is the number of nodes and **m** is the number of edges, which was considered the theoretical limit for this problem. The paper **Breaking the Sorting Barrier for Directed Single-Source Shortest Paths** introduces a new theoretical algorithm with time complexity $O(m\log^{2/3}n)$, breaking that barrier. However, the lack of concrete, documented implementations of this algorithm hinders its understanding and practical analysis, limiting academic and technical accessibility.  
Therefore, this project aims to implement the algorithm proposed in the referenced paper, faithfully following its logic, data structures, and optimization strategies. The code is written in Java, using language and implementation optimizations when possible without altering its complexity, allowing functional validation. Comparative tests are performed using sparse graphs, since the difference in complexity is more prominent for small **m**, and with similar degrees between nodes, as assumed in the paper for the complexity to hold.

# Theoretical Background

## General Idea of the Algorithm
To understand the algorithm’s specifics, it is first necessary to understand the general idea, as it is unusual for a shortest-path algorithm to use divide and conquer.

The general idea is to divide the problem into small parts, based on distance limits, until the part is small enough for a simple **Dijkstra** computation, while ensuring divisions are efficient enough to reduce time complexity. Unlike traditional approaches, it doesn’t perform a complete ordering of distances in a priority queue, but only a partial ordering between parts, allowing the time barrier to be broken.

More specifically, a call to `bmssp` receives an upper bound, the current recursion level, and already resolved nodes. It finds, among those nodes, the ones that reach unresolved nodes within the specified boundary — the **pivots** — and also solves the distances of some nodes reachable from them. With these pivots, a new recursive call is made for a smaller upper bound, until the recursion level indicates it is simple enough for **Dijkstra** to handle. As nodes are completed for smaller limits, they can serve as pivots for larger ones, gradually solving the problem by selecting pivots with smaller distances using the specialized `DQueue` structure.

Thus, each function call returns the new completed nodes and the upper bound they reached. For this strategy to work, it must guarantee that, given distances $b < B$, all paths with distances between **b** and **B** pass through a node with distance ≤ **b**. Each recursive call solves the problem for a certain **b**, allowing the previous call to solve the **B** frontier using these results.

Therefore, the first `bmssp` call has an infinite upper bound, as it aims to solve all nodes regardless of distance, and the only completed node is the origin with distance zero.

The algorithm’s relationship with priority queues is also important. During execution, **Dijkstra** maintains a frontier — the set of discovered but not fully processed vertices. Their minimum provisional distances are known, but their neighbors may not be fully explored. These frontier vertices are stored in a priority queue, from which the nearest vertex is repeatedly extracted. The frontier can contain up to **n** elements, and each extraction costs $\log n$, leading to a total cost of $n\log n$.

**BMSSP** recursively reduces the frontier size, combining **Dijkstra** and **Bellman-Ford** concepts to divide the problem, using a data structure that allows grouped insertions and deletions for partial ordering. As a result, the total number of operations is reduced, improving runtime and making it faster.

## Implementation
The algorithm implementation is organized into three main methods: `bmssp()`, `baseCase()`, and `findPivots()`. Additionally, there is a `solve()` method that receives the source node and graph, initializes constants and data structures, sets all node distances to infinity (except the source node at 0), and calls `bmssp()` with initial parameters. Constants are defined as $k=\lfloor\log^{1/3}n\rfloor$, $t=\lfloor\log^{2/3}n\rfloor$, and $level=\lceil(\log n)/t\rceil$. The algorithm returns a global structure that stores the shortest distance from the source to each node.

### BMSSP
The `bmssp()` method controls recursion, dividing the problem into subproblems until the base case is reached (level = 0). It takes three parameters: the recursion level, an upper bound, and a set of already known nodes. Initially, this set contains only the source node. If the level equals zero, it calls `baseCase()`. Otherwise, it calls `findPivots()` to identify pivots, initializes the **DQueue**, and executes its main loop.  
The **DQueue** retrieves the **M** smallest elements efficiently, where $M=2^{(level-1)t}$. The main loop repeatedly:
1. Extracts the **M** smallest completed nodes (`prevNodes`) and their `prevUpperBound`.
2. Calls itself recursively with a smaller level, using `prevNodes` and `prevUpperBound`.
3. Updates new completed nodes and adds them to `newCompleteNodes`.
4. Adds all new nodes between bounds to the **DQueue** using **batchPrepend**.
5. The loop continues until the size limit or queue exhaustion.  
Finally, the method returns the smallest upper bound reached and the new completed nodes.

### Base Case
When recursion level reaches zero, `baseCase()` is executed. It takes an upper bound and a pivot. It defines a `completeNodes` set and a min-priority queue initialized with the pivot. While the queue isn’t empty and fewer than $k + 1$ nodes are completed:
1. It removes the smallest node-distance pair and skips outdated ones.
2. Updates the upper bound when larger distances are found.
3. Explores each edge, updating distances if a shorter path is found.  
If the number of completed nodes ≤ **k**, the method returns them with the given bound. Otherwise, only nodes with distances below the new upper bound are returned.

### Find Pivots
The `findPivots()` method identifies pivot nodes to partition the problem. It receives an upper bound and a **border** node set. It iterates **k** times, expanding nodes and updating distances. If the total completed nodes exceed $k·|border|$, it returns all border nodes as pivots. Otherwise, pivots are nodes that are roots of shortest-path trees of size ≥ **k**.

### DQueue
The specialized **DQueue** structure is a block-based priority queue with three key operations:
- **Insert** — inserts a (node, distance) pair if no smaller exists.  
- **BatchPrepend** — adds a batch of smaller pairs.  
- **Pull** — retrieves and deletes the **M** smallest pairs and the separating upper bound.  
Internally, it uses two structures: **BatchList** (a stack of blocks) and **InsertTree** (a Red-Black tree of blocks).

## Practical Example
An example is shown with origin **1**, recursion level **3**, and constants $k=1$, $t=1$.  
Through successive recursive calls, pivots and upper bounds evolve until all nodes are completed. The final distances returned are:

| Node | Shortest Distance |
|-|-|
|1|0|
|2|13|
|3|5|
|4|8|
|5|11|
|6|15|

# Methodology
The project has two parts: practical and experimental.

## Practical
Implementation of the algorithm according to the paper:
- `DQueue()`
- `findPivots()`
- `baseCase()`
- `bmssp()`

## Experimental
Experimental verification comparing **BMSSP** and **Dijkstra** for correctness and runtime.

Random sparse graphs with $n$ nodes and $2n$ edges were generated, edges weighted from $1$ to $10^9$. Graphs ranged from $10^3$ to $10^6$ nodes, each tested 20 times, averaging execution time with Java’s `System.nanoTime()`.

Hardware:
| | |
|-|-|
|RAM|32GB|
|CPU|i5-10500|

## Results
Raw data in [RESULTS](benchmarkResults/results.csv).  
No discrepancies were found between **BMSSP** and **Dijkstra**, confirming correctness. However, in practice, **BMSSP** did not outperform **Dijkstra** due to high constants and recursion overhead. Still, the performance ratio shows a decreasing trend, suggesting asymptotically lower growth for **BMSSP**, potentially confirming its theoretical advantage.

![Comparison Graph Dijkstra vs BMSSP](benchmarkResults/benchmarkResults.png)

# Final Considerations
The project successfully implemented and documented the algorithm, clarifying its functioning. While **BMSSP** theoretically breaks the sorting barrier, further optimizations are needed to achieve practical superiority over **Dijkstra**. The project will continue toward that goal, including further communication with the paper’s authors.
