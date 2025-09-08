/**
 * MIT License
 *
 * Copyright (c) 2024 Michael Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package main.java.commom.dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implements a Fibonacci Heap, a data structure for priority queues that supports
 * efficient operations such as insert, extract-min, and decrease-key. Fibonacci
 * heaps provide better amortized time complexity for several operations compared
 * to other heap structures like binary heaps, especially in scenarios that involve
 * many decrease-key and extract-min operations.
 *
 * @param <T> The type of the values stored in the heap. This can be any object type.
 */
public class FibonacciHeap<T> {

    /**
     * The minimum node in the Fibonacci heap. This node has the smallest key value
     * among all the nodes in the heap. The `min` node is used to efficiently perform
     * the `extract-min` operation, which removes the node with the smallest key from the heap. <br>
     *
     * The `min` node is always maintained at the root level and is updated after
     * each operation (e.g., insert, extract-min, etc.).
     */
    private Node<T> min;

    /**
     * The total number of nodes in the Fibonacci heap.
     */
    private int size;

    private HashMap<T,Node<T>> valueToNode;

    /**
     * A node in a Fibonacci Heap which has a value and a key.
     * A node has a reference to its left and right neighbors
     * as well as its parent and child if any.
     * @param <T> the type of the value.
     */
    public static final class Node<T> {

        /**
         * The value stored in the node. This is the data associated with the node,
         * which may be of any generic type.
         */
        private final T value;

        /**
         * The key associated with the node. Used for ordering the node in the heap.
         */
        private double key;

        /**
         * The parent node of this node. This node's parent is the node in the heap's
         * tree structure that directly points to one of its siblings in the child list.
         * If the node is in the root list, this pointer is null.
         */
        private Node<T> parent;

        /**
         * The first child node of this node. If the node has no children, this pointer
         * is null. The child pointer is used to represent the node's subtree.
         */
        private Node<T> child;

        /**
         * A pointer to the left sibling of this node in the doubly linked circular list
         * of siblings. This is used to traverse the sibling list of a node.
         */
        private Node<T> left;

        /**
         * A pointer to the right sibling of this node in the doubly linked circular list
         * of siblings. This is used to traverse the sibling list of a node.
         */
        private Node<T> right;

        /**
         * The degree of the node. Represents the number of direct children the node has.
         * The degree is incremented when a new child is added to the node, and decremented
         * when a child is removed.
         */
        private int degree;

        /**
         * A flag indicating whether the node has lost a child since it was last marked.
         * This is used in the cascading cut mechanism during decrease-key operations.
         * The node is marked after losing a child, and the mark is reset when the node
         * is moved to the root list.
         */
        private boolean marked;

        /**
         * Initializes a Node with a value and key.
         * Since the root list and child lists are doubly circular linked lists,
         * left and right are automatically initialized to itself.
         * @param value the data value to store.
         * @param key the priority of this node.
         */
        public Node(T value, double key) {
            this.key = key;
            this.value = value;
            left = this;
            right = this;
        }

        public T getValue() {
            return value;
        }

        public double getKey() {
            return key;
        }

        /**
         * Returns a string representation of the node.
         * The representation includes the node's value, key, degree,
         * and whether it is marked.
         *
         * @return a string representing the node's attributes.
         */
        @Override
        public String toString() {
            return "Node{value=" + value + ", key=" + key
                    + ", degree=" + degree + ", marked=" + marked + "}";
        }
    }

    /**
     * Initializes a new Fibonacci Heap with a size of 0 and min points to null.
     */
    public FibonacciHeap() {
        this.min = null;
        this.size = 0;
        this.valueToNode = new HashMap<T,Node<T>>();
    }

    /**
     * Inserts a new node in the root list to the right of the current minimum node.
     * No consolidation is performed when a new node is inserted.
     * Operation is performed in O(1) time. <br>
     *
     * Before: insert(..., 2) <br>
     * min------------| <br>
     * |-> (...) <-> (3) <-> (4) <-> (5) <-> (...) <-| <br>
     * |---------------------------------------------| <br>
     *
     * After: <br>
     * min--------------------| <br>
     * |-> (...) <-> (3) <-> (2) <-> (4) <-> (5) <-> (...) <-| <br>
     * |-----------------------------------------------------|
     *
     * @param value the value of the new node.
     * @param key the key of the new node.
     * @return the new node.
     */
    public Node<T> insert(T value, double key) {
        Node<T> newNode = new Node<>(value, key);
        valueToNode.put(value,newNode);
        addToRootList(newNode);
        size++;
        return newNode;
    }

    public T extractMinValue() {
        T minValue = extractMin().value;
        valueToNode.remove(minValue);
        return minValue;
    }

    /**
     * Removes and returns the node with the smallest key in the Fibonacci heap. <br>
     *
     * This method performs the following steps: <br>
     * - Identifies the current minimum node in the heap. <br>
     * - Moves all children of the minimum node to the root list of the heap,
     *   resetting their parent pointers to {@code null}.
     * - Removes the minimum node from the root list. <br>
     * - If the heap is not empty after removal, selects a new minimum node
     *   and calls {@link #consolidate()} to merge trees and restore heap structure. <br>
     * - Updates the size of the heap. <br>
     *
     * If the heap is empty, this method returns {@code null}.
     *
     * @return The node with the smallest key, or {@code null} if the heap is empty.
     */
    private Node<T> extractMin() {
        Node<T> extractedMin = min;
        if (extractedMin != null) {

            if (extractedMin.child != null) {
                // Move all children of extractedMin to root list.
                Node<T> current = extractedMin.child;
                do {
                    Node<T> next = current.right; // addToRootList updates right so save right before addToRootList is called.
                    current.parent = null; // Set all the children of the new min to null since they are added to root list.
                    addToRootList(current);
                    current = next;
                } while (current != extractedMin.child);
            }
            size--;

            if (extractedMin.right == extractedMin) {
                min = null; // no other nodes in heap so min is now null.
            } else {
                min.left.right = min.right;
                min.right.left = min.left;
                min = min.right; // arbitrary min, min will be updated in consolidate.

                consolidate();
            }
        }
        return extractedMin;
    }

    /**
     * Returns the number of nodes in the Fibonacci heap.
     * @return the total number of nodes in the Fibonacci heap.
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the Fibonacci heap is empty.
     * @return {@code true} if the Fibonacci heap is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return min == null;
    }

    public boolean containsValue(T value){
        return valueToNode.containsKey(value);
    }

    public void decreaseKeyByValue(T value, double newKey){
        decreaseKey(valueToNode.get(value), newKey);
    }

    /**
     * Decreases the key value of a given node in the Fibonacci Heap.
     * If the new key value violates the heap property (i.e., the node's key
     * becomes smaller than its parent's key), the node is cut from its parent
     * and added to the root list. This may trigger cascading cuts on its ancestors
     * if necessary.
     *
     * @param node The node whose key is to be decreased.
     * @param newKey The new key value, which must be smaller than the current key.
     * @throws IllegalArgumentException If the new key is greater than or equal to the current key.
     */
    private void decreaseKey(Node<T> node, double newKey) {
        if (newKey >= node.key) {
            throw new IllegalArgumentException("The new key must be less than the current key");
        }

        node.key = newKey;

        if (node.parent != null && node.key < node.parent.key) {
            cut(node, node.parent);
        }

        if (node.key <= min.key) {
            min = node;
        }
    }

    /**
     * Links two trees of the same degree by making node y a child of node x.
     * This method is used in the consolidate step of the Fibonacci heap to
     * reduce the number of trees in the root list by combining trees with the
     * same degree.
     * <p>
     * The method assumes that the key of x is less than or equal to the key of y,
     * making x the new parent of y. Node y is removed from the root list and added
     * as a child of x, and the degree of x is incremented to reflect the addition
     * of a new child.
     *
     * @param y The node to be made a child. This node will be removed from the root list.
     * @param x The node that will become the parent of y. This node's degree will be increased.
     */
    private void link(Node<T> y, Node<T> x) {
        y.right.left = y.left;
        y.left.right = y.right;

        y.parent = x;
        x.degree++;
        y.marked = false;

        if (x.child == null) {
            x.child = y;
            y.left = y;
            y.right = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right.left = y;
            x.child.right = y;
        }
    }

    /**
     * Cuts the specified node from its parent and moves it to the root list.
     * This operation is used during a decrease-key operation when a node
     * violates the heap property. The cut method also handles cascading cuts
     * if necessary, ensuring that any marked ancestors of the node are also
     * cut and added to the root list.
     *
     * @param node The node to be cut from its parent and added to the root list.
     * @param parent The parent of the node being cut.
     */
    private void cut(Node<T> node, Node<T> parent) {
        if (node.right == node) {
            parent.child = null;
        } else {
            node.right.left = node.left;
            node.left.right = node.right;
            if (parent.child == node) {
                parent.child = node.right;
            }
        }

        parent.degree--;
        node.parent = null;
        node.marked = false;

        addToRootList(node);

        // Ensure we aren't going to cascade cut a node in the root list
        if (parent.parent != null) {
            if (parent.marked) { // Perform a cascading cut if the parent is marked already
                cut(parent, parent.parent);
            } else {
                parent.marked = true;
            }
        }
    }

    /**
     * Consolidates the trees in the Fibonacci heap. This operation merges trees of the same degree
     * in the root list into a single tree, ensuring that there is only one tree of each degree in the
     * root list. The method iterates over all the root nodes, links trees of the same degree, and updates
     * the minimum node. After consolidation, the heap will have a set of trees with unique degrees,
     * and the root list will contain the consolidated trees.
     * <p>
     * This operation maintains the Fibonacci heap property and reduces the number of trees in the root list.
     * The time complexity of this operation is O(log n), where n is the number of nodes in the heap.
     *
     * @see #link(Node, Node)
     */
    private void consolidate() {
        final double phi = (1 + Math.sqrt(5)) / 2; // golden ratio
        final int maxDegree = (int) Math.floor(Math.log(size) / Math.log(phi)) + 1;
        final Node<T>[] degreeTable = new Node[maxDegree];
        final List<Node<T>> rootList = new ArrayList<>();

        Node<T> current = min;
        // Traverse all root nodes and add them to rootList.
        do {
            rootList.add(current);
            current = current.right;
        } while (current != min);

        // Consolidate the trees in the root list
        for (Node<T> node : rootList) {
            while (degreeTable[node.degree] != null) {
                Node<T> collisionNode = degreeTable[node.degree];
                degreeTable[node.degree] = null;

                if (collisionNode.key < node.key) {
                    link(node, collisionNode);
                    // node isn't in root list anymore, so we need to update it to node's parent.
                    node = node.parent;
                } else {
                    link(collisionNode, node);
                }
            }

            degreeTable[node.degree] = node;

            if (node.key < min.key) {
                min = node;
            }
        }
    }

    /**
     * Adds a single node to the root list and updates min.
     * This node will be added to the right of the current node.
     * @param node a node with no neighbors.
     */
    private void addToRootList(Node<T> node) {
        if (min != null) {
            node.left = min;
            node.right = min.right;
            min.right.left = node;
            min.right = node;
            if (node.key < min.key) {
                min = node;
            }
        } else {
            min = node;
        }
    }

}