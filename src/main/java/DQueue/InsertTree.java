package main.java.DQueue;

import java.util.HashSet;

import main.java.DQueue.Block.Block;
import main.java.DQueue.Block.BlockCollection;
import main.java.DQueue.Block.BlockContainer;
import main.java.DQueue.Block.BlockNode;

class InsertTree implements BlockCollection{

    private static final InsertNode nilNode = new InsertNode(null, null, null, null, ColorEnum.BLACK);
    private final int blockSize;

    private InsertNode root;
    private int size;

    public InsertTree(int blockSize, int upperBound){
        this.blockSize = blockSize;
        size = 0;

        insertBlock(new Block(this.blockSize,upperBound));
    }

    public void insertElement(NodeDistStored element) {
        InsertNode node = root;
        while(node != null){
            if(element.dist > node.value.upperBound){
                node = node.right;
                continue;
            }
            if(node.left != null && element.dist <= node.left.value.upperBound){
                node = node.left;
            }else{
                node.value.addFirst(element);
                element.blockContainer = node;

                if(node.value.isFull()){
                    insertBlock(node.value.split());
                }
                return;
            }
        }
        return;
    }

    @Override
    public void delete(BlockContainer blockContainer){
        if(size != 1){
            delete((InsertNode)blockContainer);
        }
    }

    public HashSet<NodeDistStored> pull(){
        HashSet<NodeDistStored> ret = new HashSet<NodeDistStored>();
        getBlockSizeSmallest(root, ret);
        return ret;
    }

    private void getBlockSizeSmallest(InsertNode entry, HashSet<NodeDistStored> ret) {
        if (entry != null && ret.size() < blockSize) {
            getBlockSizeSmallest(entry.left, ret);
            if (entry.value != null) {
                for(NodeDistStored element : entry.value){
                    ret.add(element);
                }
            }
            getBlockSizeSmallest(entry.right, ret);
        }
    }

    private InsertNode insertBlock(Block element) {
        if (root == null) {
            root = createNode(element, null, null, null);
            size++;
            return root;
        }

        InsertNode insertParentNode = null;
        InsertNode searchTempNode = root;
        while (searchTempNode != null && searchTempNode.value != null) {
            insertParentNode = searchTempNode;
            if (element.compareTo(searchTempNode.value) < 0) {
                searchTempNode = searchTempNode.left;
            } else {
                searchTempNode = searchTempNode.right;
            }
        }

        InsertNode newNode = createNode(element, insertParentNode, null, null);
        if (insertParentNode.value.compareTo(newNode.value) > 0) {
            insertParentNode.left = newNode;
        } else {
            insertParentNode.right = newNode;
        }

        newNode.left = nilNode;
        newNode.right = nilNode;
        root.parent = nilNode;
        insertRBFixup((InsertNode) newNode);
        size++;
        return newNode;
    }
    
    private InsertNode delete(InsertNode deleteNode) {
        InsertNode replaceNode = null; // track node that replaces removedOrMovedNode
        if (deleteNode != null && deleteNode != nilNode) {
            InsertNode removedOrMovedNode = deleteNode; // same as deleteNode if it has only one child, and otherwise it replaces deleteNode
            ColorEnum removedOrMovedNodeColor = ((InsertNode)removedOrMovedNode).color;
        
            if (deleteNode.left == nilNode) {
                replaceNode = deleteNode.right;
                rbTreeTransplant(deleteNode, deleteNode.right);
            } else if (deleteNode.right == nilNode) {
                replaceNode = deleteNode.left;
                rbTreeTransplant(deleteNode, deleteNode.left);
            } else {
                removedOrMovedNode = getMinimum(deleteNode.right);
                removedOrMovedNodeColor = ((InsertNode)removedOrMovedNode).color;
                replaceNode = removedOrMovedNode.right;
                if (removedOrMovedNode.parent == deleteNode) {
                    replaceNode.parent = removedOrMovedNode;
                } else {
                    rbTreeTransplant(removedOrMovedNode, removedOrMovedNode.right);
                    removedOrMovedNode.right = deleteNode.right;
                    removedOrMovedNode.right.parent = removedOrMovedNode;
                }
                rbTreeTransplant(deleteNode, removedOrMovedNode);
                removedOrMovedNode.left = deleteNode.left;
                removedOrMovedNode.left.parent = removedOrMovedNode;
                ((InsertNode)removedOrMovedNode).color = ((InsertNode)deleteNode).color;
            }
            
            if (removedOrMovedNodeColor == ColorEnum.BLACK) {
                deleteRBFixup((InsertNode)replaceNode);
            }
        }
        size--;
        return replaceNode;
    }
    
    private InsertNode createNode(Block value, InsertNode parent, InsertNode left, InsertNode right) {
        InsertNode newNode = new InsertNode(value, parent, left, right, ColorEnum.RED);
        for(NodeDistStored element : value){
            element.blockContainer = newNode;
        }
        return new InsertNode(value, parent, left, right, ColorEnum.RED);
    }
    
    private InsertNode getMinimum(InsertNode node) {
        while (node.left != nilNode) {
            node = node.left;
        }
        return node;
    }

    private InsertNode rotateLeft(InsertNode node) {
        InsertNode temp = node.right;
        temp.parent = node.parent;
        
        node.right = temp.left;
        if (node.right != nilNode) {
            node.right.parent = node;
        }

        temp.left = node;
        node.parent = temp;

        // temp took over node's place so now its parent should point to temp
        if (temp.parent != nilNode) {
            if (node == temp.parent.left) {
                temp.parent.left = temp;
            } else {
                temp.parent.right = temp;
            }
        } else {
            root = temp;
        }
        
        return temp;
    }

    private InsertNode rotateRight(InsertNode node) {
        InsertNode temp = node.left;
        temp.parent = node.parent;

        node.left = temp.right;
        if (node.left != nilNode) {
            node.left.parent = node;
        }

        temp.right = node;
        node.parent = temp;

        // temp took over node's place so now its parent should point to temp
        if (temp.parent != nilNode) {
            if (node == temp.parent.left) {
                temp.parent.left = temp;
            } else {
                temp.parent.right = temp;
            }
        } else {
            root = temp;
        }
        
        return temp;
    }

    
    /**
     * Similar to original transplant() method in BST but uses nilNode instead of null.
     */
    private InsertNode rbTreeTransplant(InsertNode nodeToReplace, InsertNode newNode) {
        if (nodeToReplace.parent == nilNode) {
            this.root = newNode;
        } else if (nodeToReplace == nodeToReplace.parent.left) {
            nodeToReplace.parent.left = newNode;
        } else {
            nodeToReplace.parent.right = newNode;
        }
        newNode.parent = nodeToReplace.parent;
        return newNode;
    }
    
    /**
     * Restores Red-Black tree properties after delete if needed.
     */
    private void deleteRBFixup(InsertNode x) {
        while (x != root && isBlack(x)) {
            
            if (x == x.parent.left) {
                InsertNode w = (InsertNode)x.parent.right;
                if (isRed(w)) { // case 1 - sibling is red
                    w.color = ColorEnum.BLACK;
                    ((InsertNode)x.parent).color = ColorEnum.RED;
                    rotateLeft(x.parent);
                    w = (InsertNode)x.parent.right; // converted to case 2, 3 or 4
                }
                // case 2 sibling is black and both of its children are black
                if (isBlack(w.left) && isBlack(w.right)) {
                    w.color = ColorEnum.RED;
                    x = (InsertNode)x.parent;
                } else if (w != nilNode) {
                    if (isBlack(w.right)) { // case 3 sibling is black and its left child is red and right child is black
                        ((InsertNode)w.left).color = ColorEnum.BLACK;
                        w.color = ColorEnum.RED;
                        rotateRight(w);
                        w = (InsertNode)x.parent.right;
                    }
                    w.color = ((InsertNode)x.parent).color; // case 4 sibling is black and right child is red
                    ((InsertNode)x.parent).color = ColorEnum.BLACK;
                    ((InsertNode)w.right).color = ColorEnum.BLACK;
                    rotateLeft(x.parent);
                    x = (InsertNode)root;
                } else {
                    x.color = ColorEnum.BLACK;
                    x = (InsertNode)x.parent;
                }
            } else {
                InsertNode w = (InsertNode)x.parent.left;
                if (isRed(w)) { // case 1 - sibling is red
                    w.color = ColorEnum.BLACK;
                    ((InsertNode)x.parent).color = ColorEnum.RED;
                    rotateRight(x.parent);
                    w = (InsertNode)x.parent.left; // converted to case 2, 3 or 4
                }
                // case 2 sibling is black and both of its children are black
                if (isBlack(w.left) && isBlack(w.right)) {
                    w.color = ColorEnum.RED;
                    x = (InsertNode)x.parent;
                } else if (w != nilNode) {
                    if (isBlack(w.left)) { // case 3 sibling is black and its right child is red and left child is black
                        ((InsertNode)w.right).color = ColorEnum.BLACK;
                        w.color = ColorEnum.RED;
                        rotateLeft(w);
                        w = (InsertNode)x.parent.left;
                    }
                    w.color = ((InsertNode)x.parent).color; // case 4 sibling is black and left child is red
                    ((InsertNode)x.parent).color = ColorEnum.BLACK;
                    ((InsertNode)w.left).color = ColorEnum.BLACK;
                    rotateRight(x.parent);
                    x = (InsertNode)root;
                } else {
                    x.color = ColorEnum.BLACK;
                    x = (InsertNode)x.parent;
                }
            }
            
        }
    }
    
    private boolean isBlack(InsertNode node) {
        return node != null ? ((InsertNode)node).color == ColorEnum.BLACK : false;
    }
    
    private boolean isRed(InsertNode node) {
        return node != null ? ((InsertNode)node).color == ColorEnum.RED : false;
    }

    /**
     * Restores Red-Black tree properties after insert if needed. Insert can
     * break only 2 properties: root is red or if node is red then children must
     * be black.
     */
    private void insertRBFixup(InsertNode currentNode) {
        // current node is always RED, so if its parent is red it breaks
        // Red-Black property, otherwise no fixup needed and loop can terminate
        while (currentNode.parent != root && ((InsertNode) currentNode.parent).color == ColorEnum.RED) {
            InsertNode parent = (InsertNode) currentNode.parent;
            InsertNode grandParent = (InsertNode) parent.parent;
            if (parent == grandParent.left) {
                InsertNode uncle = (InsertNode) grandParent.right;
                // case1 - uncle and parent are both red
                // re color both of them to black
                if (((InsertNode) uncle).color == ColorEnum.RED) {
                    parent.color = ColorEnum.BLACK;
                    uncle.color = ColorEnum.BLACK;
                    grandParent.color = ColorEnum.RED;
                    // grandparent was recolored to red, so in next iteration we
                    // check if it does not break Red-Black property
                    currentNode = grandParent;
                } 
                // case 2/3 uncle is black - then we perform rotations
                else {
                    if (currentNode == parent.right) { // case 2, first rotate left
                        currentNode = parent;
                        rotateLeft(currentNode);
                        parent = (InsertNode) currentNode.parent;
                    }
                    // do not use parent
                    parent.color = ColorEnum.BLACK; // case 3
                    grandParent.color = ColorEnum.RED;
                    rotateRight(grandParent);
                }
            } else if (parent == grandParent.right) {
                InsertNode uncle = (InsertNode) grandParent.left;
                // case1 - uncle and parent are both red
                // re color both of them to black
                if (((InsertNode) uncle).color == ColorEnum.RED) {
                    parent.color = ColorEnum.BLACK;
                    uncle.color = ColorEnum.BLACK;
                    grandParent.color = ColorEnum.RED;
                    // grandparent was recolored to red, so in next iteration we
                    // check if it does not break Red-Black property
                    currentNode = grandParent;
                }
                // case 2/3 uncle is black - then we perform rotations
                else {
                    if (currentNode == parent.left) { // case 2, first rotate right
                        currentNode = parent;
                        rotateRight(currentNode);
                        parent = (InsertNode) currentNode.parent;
                    }
                    // do not use parent
                    parent.color = ColorEnum.BLACK; // case 3
                    grandParent.color = ColorEnum.RED;
                    rotateLeft(grandParent);
                }
            }

        }
        // ensure root is black in case it was colored red in fixup
        ((InsertNode) root).color = ColorEnum.BLACK;
    }

    
}

enum ColorEnum {
    RED,
    BLACK
};

class InsertNode implements BlockContainer {
    public final Block value;
    public InsertNode parent;
    public InsertNode left;
    public InsertNode right;
    public ColorEnum color;

    public InsertNode(Block value, InsertNode parent, InsertNode left, InsertNode right, ColorEnum color) {
        this.value = value;
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.color = color;
    }

    @Override
    public void delete(BlockNode element) {
        value.remove(element);
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    

}