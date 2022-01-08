import java.util.ArrayList;

public class RBTree {
    private Node root;
    public ArrayList<Integer> List = new ArrayList<Integer>();

    // First print pre-order traversal , then print middle order traversal
    public void print() {
        if (root == null) {
            System.out.println("Empty");
            return;
        }
        System.out.println("pre-order traversal:");
        this.prePrint(root);
        System.out.println();
        System.out.println("middle order Traversal(Value,color,Node count):");
        this.midPrint(root);
        System.out.println();
    }

    public ArrayList<Integer> getList() {
        if (root == null) {
            return List = null;
        }
        List = list(root);
        return List;
    }

    private ArrayList<Integer> list(Node node) {
        if (node.getLeftNode() != null) {
            this.list(node.getLeftNode());
        }
        List.add(node.getValue());
        if (node.getRightNode() != null) {
            this.list(node.getRightNode());
        }
        return List;
    }

    // Insert Node
    public void Insert(Integer value) {
        // If the root node is empty, set it as the root node
        if (root == null) {
            Node head = new Node(value);
            head.setColor(false);// root-black
            head.setNumber(1);
            this.root = head;
            return;
        }
        addNode(value);
    }

    public void delete(Integer value) {
        Node node = getNode(value);
        // Find the data to be deleted does not exist
        if (node == null) {
            System.out.print(value + " is not exist\n");
            return;
        }
        reducecNumber(value);
        this.fixAndRemove(node);
    }

    public void change(Integer value1, Integer value2) {
    if (!search(value1)) {
    System.out.print("can not change\n");
    return;
    }
    delete(value1);
    Insert(value2);
    System.out.print("change success\n");
    return;
    }

    public boolean search(Integer value) {
        Node node = getNode(value);
        // Find the data to be deleted does not exist
        if (node == null) {
            System.out.print(value + " is not exist\n");
            return false;
        }
        System.out.print(node.getValue() + " exists, color is " + (node.getColor() ? "red" : "black") + "\n");
        return true;
    }

    // Find the minimum value
    public void min() {
        if (root != null) {
            Node n = root;
            while (true) {
                if (n.left != null) {
                    n = n.left;
                } else {
                    System.out.println("min is " + n.getValue());
                    return;
                }
            }
        } else {
            System.out.println("empty");
            return;
        }
    }

    // Find the maximum value
    public void max() {
        if (root != null) {
            Node n = root;
            while (true) {
                if (n.right != null) {
                    n = n.right;
                } else {
                    System.out.println("max is " + n.getValue());
                    return;
                }
            }
        } else {
            System.out.println("empty");
            return;
        }
    }

    // Find the median
    public void median() {
        if (root.getNumber() == 1) {
            System.out.println("median is " + root.getValue());
        }
        int mid = 0;
        if (root.number % 2 == 0) {
            mid = root.number / 2;
        } else {
            mid = (root.number + 1) / 2;
        }
        Node n = root;
        int pass = 0;
        while (mid - pass > 0) {
            if (n.getLeftNumber() == mid - pass) {
                int tmp = n.getLeftNumber();
                n = getSuccessorNode(n);
                pass += tmp;
            } else if (n.getLeftNumber() > mid - pass) {
                n = n.getLeftNode();
            } else if (n.getLeftNumber() == mid - pass - 1) {
                break;
            } else if (n.getLeftNumber() < mid - pass) {
                pass += n.getLeftNumber() + 1;
                n = n.getRightNode();
            }
        }
        if (root.number % 2 == 0) {
            int tmp = n.getValue();
            if (n.getRightNode() != null) {
                n = getPredecessorNode(n);
            } else {
                do {
                    n = n.getParentNode();
                } while (tmp > n.getValue());
            }
            System.out.println("median is " + (n.getValue() + tmp) / 2.0);
            return;
        } else {
            System.out.println("median is " + n.getValue());
        }
    }

    private void fixAndRemove(Node node) {
        if (node.getColor()) {
            // If the node is red
            if (node.getLeftNode() == null && node.getRightNode() == null) {
                // Delete directly without child nodes
                Node tmp = node;
                while (true) {
                    // tmp.number -= 1;
                    if (tmp != root) {
                        tmp = tmp.getParentNode();
                    } else
                        break;
                }
                this.remove(node);
            } else {
                // Otherwise it must be two black child nodes,
                // assign the value of the successor node to the node,
                // treat the successor node as the node to be deleted and then execute the
                // method.
                Node successorNode = this.getSuccessorNode(node);
                node.setValue(successorNode.getValue());
                // node.setNumber(successorNode.getNumber());
                node.number--;
                this.fixAndRemove(successorNode);
            }
        } else {
            // The node is black, if there is only one child node then it must be red,
            // replace the child node with the current node and turn it black.
            if (node.getLeftNode() == null && node.getRightNode() != null) {
                Node parent = node.getParentNode();
                Node right = node.getRightNode();
                if (this.remove(node)) {
                    parent.setLeftNode(right);
                } else {
                    parent.setRightNode(right);
                }
                right.setParentNode(parent);
                right.setColor(false);
            } else if (node.getLeftNode() != null && node.getRightNode() == null) {
                Node parent = node.getParentNode();
                Node left = node.getLeftNode();
                if (this.remove(node)) {
                    parent.setLeftNode(left);
                } else {
                    parent.setRightNode(left);
                }
                left.setParentNode(parent);
            } else if (node.getLeftNode() != null && node.getRightNode() != null) {
                // If there are two child nodes,
                // assign the value of the successor node to the node,
                // treat the successor node as the node to be deleted and then execute the
                // method.
                Node successorNode = this.getSuccessorNode(node);
                node.setValue(successorNode.getValue());
                node.number--;
                this.fixAndRemove(successorNode);
            } else {
                // No child nodes
                this.fixNoChild(node, true);
            }
        }
    }

    // Fix no child nodes and delete
    private void fixNoChild(Node node, Boolean delete) {
        Node siblingNode = this.getSiblingNode(node);
        Node parent = node.getParentNode();
        if (siblingNode.getColor()) {
            // If the sibling node is red, change the parent node to red and the sibling
            // node to black,
            // rotate the parent node, and execute the method again.
            parent.setColor(true);
            siblingNode.setColor(false);
            if (this.isLeft(node)) {
                this.rotateLeft(parent);
            } else {
                this.rotateRight(parent);
            }
            this.fixNoChild(node, delete);
        } else {
            // Brother nodes are black
            if (siblingNode.getLeftNode() != null && siblingNode.getLeftNode().getColor()) {
                // If the left child node of a sibling node is red
                if (this.isLeft(node)) {
                    // This node is also the left child of its parent node, right rotate the sibling
                    // node,
                    // set the color of the new sibling node to the color of the parent node, set
                    // the parent node to black, left rotate the parent node
                    this.rotateRight(siblingNode);
                    Node newSiblingNode = this.getSiblingNode(node);
                    newSiblingNode.setColor(parent.getColor());
                    parent.setColor(false);
                    this.rotateLeft(parent);
                } else {
                    // The node is the right child of its parent node, set the color of the sibling
                    // node to the color of the parent node,
                    // and the parent node is set to black, right the parent node
                    siblingNode.setColor(parent.getColor());
                    parent.setColor(false);
                    siblingNode.getLeftNode().setColor(false);
                    this.rotateRight(parent);
                }
                if (delete) {
                    this.remove(node);
                }
            } else if (siblingNode.getRightNode() != null && siblingNode.getRightNode().getColor()) {
                // If the right child of a sibling node is red
                if (this.isLeft(node)) {
                    // The node is the left child of its parent node, set the color of the sibling
                    // node to the color of the parent node,
                    // the parent node is set to black, and the left parent node
                    siblingNode.setColor(parent.getColor());
                    parent.setColor(false);
                    siblingNode.getRightNode().setColor(false);
                    this.rotateLeft(parent);
                } else {
                    // This node is also the right child of its parent node, left rotate the sibling
                    // node,
                    // set the color of the new sibling node to the color of the parent node, set
                    // the parent node to black, right rotate the parent node
                    this.rotateLeft(siblingNode);
                    Node newSiblingNode = this.getSiblingNode(node);
                    newSiblingNode.setColor(parent.getColor());
                    parent.setColor(false);
                    this.rotateRight(parent);
                }
                if (delete) {
                    this.remove(node);
                }
            } else {
                // The child nodes of the sibling node are all black. If the node needs to be
                // deleted,
                // the node will be deleted first and the sibling node will be set to red, and
                // if the parent node is red, the parent node will be set to black.
                // Otherwise, if the parent node is not the root node, treat the parent node as
                // the node to be repaired and execute the method again.
                if (delete) {
                    this.remove(node);
                }
                siblingNode.setColor(true);
                if (parent.getColor()) {
                    parent.setColor(false);
                } else {
                    if (parent != root) {
                        // Fix RBTree
                        this.fixNoChild(parent, false);
                    }
                }
            }
        }
    }

    // Get sibling nodes
    private Node getSiblingNode(Node node) {
        if (this.isLeft(node)) {
            return node.getParentNode().getRightNode();
        } else {
            return node.getParentNode().getLeftNode();
        }
    }

    // Whether it is the left child of the parent node
    private Boolean isLeft(Node node) {
        return node == node.getParentNode().getLeftNode();
    }

    // Remove node
    private Boolean remove(Node node) {
        if (this.isLeft(node)) {
            node.getParentNode().setLeftNode(null);
            return true;
        } else {
            node.getParentNode().setRightNode(null);
            return false;
        }
    }

    // Reducec Node Number
    private void reducecNumber(int val) {
        Node n = root;
        while (n.getValue() != val) {
            n.number--;
            if (n.getValue() > val) {
                n = n.getLeftNode();
            } else if (n.getValue() < val) {
                n = n.getRightNode();
            }
        }
    }

    // Get Successor Node
    private Node getSuccessorNode(Node node) {
        if (node.getLeftNode() == null) {
            return null;
        }
        Node tmp = node.getLeftNode();
        while (tmp.getRightNode() != null) {
            tmp = tmp.getRightNode();
        }
        return tmp;
    }

    // Get Predecessor Node
    private Node getPredecessorNode(Node node) {
        if (node.getRightNode() == null) {
            return null;
        }
        Node tmp = node.getRightNode();
        while (tmp.getLeftNode() != null) {
            tmp = tmp.getLeftNode();
        }
        return tmp;
    }

    // Find nodes with values
    private Node getNode(Integer value) {
        Node tmp = root;
        while (tmp != null) {
            if (tmp.getValue().equals(value)) {
                return tmp;
            } else if (tmp.getValue() > value) {
                tmp = tmp.getLeftNode();
            } else if (tmp.getValue() < value) {
                tmp = tmp.getRightNode();
            }
        }
        return null;
    }

    // Add node
    private void addNode(Integer value) {
        Node tmp = root;
        tmp.number += 1;
        Node n;
        // Determine the size of the inserted data,
        // compare it with the data in the tree,
        // and insert it into the corresponding node
        while (true) {
            if (tmp.getValue() >= value) {
                if (tmp.getLeftNode() == null) {
                    // tmp.number+=1;
                    n = new Node(value);
                    n.setParentNode(tmp);
                    tmp.setLeftNode(n);
                    n.setNumber(1);
                    break;
                } else {
                    // tmp.number+=1;
                    tmp = tmp.getLeftNode();
                    tmp.number += 1;
                }
            }
            if (tmp.getValue() < value) {
                if (tmp.getRightNode() == null) {
                    // tmp.number+=1;
                    n = new Node(value);
                    n.setParentNode(tmp);
                    tmp.setRightNode(n);
                    n.setNumber(1);
                    break;
                } else {
                    // tmp.number+=1;
                    tmp = tmp.getRightNode();
                    tmp.number += 1;
                }
            }
        }
        // Fix BRTree
        fixNode(n);
    }

    // Fix BRTree
    private void fixNode(Node node) {
        // If it is the root node, set the root node to black
        if (node == root) {
            node.setColor(false);
            return;
        }
        // If the parent node is black, return
        if (!node.getParentNode().getColor()) {
            return;
        }
        Node uncle = this.getUncle(node);
        if (uncle != null && uncle.getColor()) {
            // If the uncle node is red, set both the parent node and the uncle node to
            // black, set the grandfather node to red,
            // and then use the grandfather node as the newly inserted node to repair the
            // red-black tree again.
            node.parent.setColor(false);
            uncle.setColor(false);
            this.getGrandParent(node).setColor(true);
            this.fixNode(this.getGrandParent(node));
        } else if (uncle == null || !uncle.getColor()) {
            // If the uncle parent node is empty or black
            if (this.isLeft(node) && node.getParentNode() == this.getGrandParent(node).getLeftNode()) {
                // 1, three points of a line (new nodes and parent nodes are the left node of
                // their parent node)，
                // Set the parent node of the new node to black and the grandfather node to red,
                // and then rotate the grandfather node right
                this.fixLeftNode(node);
            } else if (!this.isLeft(node) && node.getParentNode() == this.getGrandParent(node).getRightNode()) {
                // 2. three points and one line (new nodes and parent nodes are the right nodes
                // of their parent nodes).
                // Set the parent node of the new node to black and the grandfather node to red,
                // and then rotate the grandfather node left
                this.fixRightNode(node);
            } else if (!this.isLeft(node) && node.getParentNode() == this.getGrandParent(node).getLeftNode()) {
                // 3. the three points are not on a line (the new node is the right child of the
                // parent node, and the parent node is the left child of the grandfather node)
                // Rotate the parent node left first, then perform the 1 operation.
                rotateLeft(node.getParentNode());
                this.fixLeftNode(node.getLeftNode());
            } else if (this.isLeft(node) && node.getParentNode() == this.getGrandParent(node).getRightNode()) {
                // 4. the three points are not on a line (the new node is the left child of the
                // parent node, and the parent node is the right child of the grandfather node)
                // Rotate the parent node right first, then perform the operation 2
                rotateRight(node.getParentNode());
                this.fixRightNode(node.getRightNode());
            }
        }
    }

    // Fix Left three points and one line
    private void fixLeftNode(Node node) {
        node.getParentNode().setColor(false);
        this.getGrandParent(node).setColor(true);
        this.rotateRight(this.getGrandParent(node));
    }

    // Fix right three points and one line
    private void fixRightNode(Node node) {
        node.getParentNode().setColor(false);
        this.getGrandParent(node).setColor(true);
        this.rotateLeft(this.getGrandParent(node));
    }

    // Left rotate
    private void rotateLeft(Node node) {
        Node right = node.getRightNode();
        // Set the parent of the right child of this node to the parent of this node
        right.setParentNode(node.getParentNode());
        // If the node is not the root node, set the child of the parent of the node as
        // the right node of the node
        if (node != root) {
            if (this.isLeft(node)) {
                int tmp = node.getNumber() - right.getNumber();
                if (right.getLeftNode() != null) {
                    tmp += right.getLeftNumber();
                }
                right.setNumber(node.getNumber());
                node.setNumber(tmp);
                node.getParentNode().setLeftNode(right);
            } else if (!this.isLeft(node)) {
                int tmp = node.getNumber() - right.getNumber();
                if (right.getLeftNode() != null) {
                    tmp += right.getLeftNumber();
                }
                right.setNumber(node.getNumber());
                node.setNumber(tmp);
                node.getParentNode().setRightNode(right);
            }
        } else {
            // If the node is the root node, set the root node to be the right node of the
            // node
            int tmp = node.getNumber() - right.getNumber();
            if (right.getLeftNode() != null) {
                tmp += right.getLeftNumber();
            }
            right.setNumber(node.getNumber());
            node.setNumber(tmp);
            root = right;
        }
        // Set the right node of this node to the left node of the right node of this
        // node
        node.setRightNode(right.getLeftNode());
        if (right.getLeftNode() != null) {
            right.getLeftNode().setParentNode(node);
        }
        // Set the parent of this node as the right node of this node
        node.setParentNode(right);
        // Set the left node of the right node of this node to this node
        right.setLeftNode(node);
    }

    // Right rotate
    private void rotateRight(Node node) {
        Node left = node.getLeftNode();
        // Set the parent of the left child of this node as the parent of this node
        left.setParentNode(node.getParentNode());
        // If it is not the root node, set the child node of the parent node as the left
        // node
        if (node != root) {
            if (this.isLeft(node)) {
                int tmp = node.getNumber() - left.getNumber();
                if (left.getRightNode() != null) {
                    tmp += left.getRightNumber();
                }
                left.setNumber(node.getNumber());
                node.setNumber(tmp);
                node.getParentNode().setLeftNode(left);
            } else if (!this.isLeft(node)) {
                int tmp = node.getNumber() - left.getNumber();
                if (left.getLeftNode() != null) {
                    tmp += left.getRightNumber();
                }
                left.setNumber(node.getNumber());
                node.setNumber(tmp);
                node.getParentNode().setRightNode(left);
            }
        } else {
            // If it is the root node, set the root node to be the left node of the node
            int tmp = node.getNumber() - left.getNumber();
            if (left.getRightNode() != null) {
                tmp += left.getRightNumber();
            }
            left.setNumber(node.getNumber());
            node.setNumber(tmp);
            root = left;
        }
        // Set the left node of this node to the left node of this node
        node.setLeftNode(left.getRightNode());
        if (left.getRightNode() != null) {
            left.getRightNode().setParentNode(node);
        }
        // Set the parent of this node as the left node of this node
        node.setParentNode(left);
        // Set the left node of the left node of this node to this node
        left.setRightNode(node);
    }

    // Get uncle node
    private Node getUncle(Node node) {
        if (node.getParentNode() != root && node.getParentNode() == this.getGrandParent(node).getLeftNode()) {
            return this.getGrandParent(node).getRightNode();
        }
        if (node.getParentNode() != root && node.getParentNode() == this.getGrandParent(node).getRightNode()) {
            return this.getGrandParent(node).getLeftNode();
        }
        return null;
    }

    // Get grandfather node
    private Node getGrandParent(Node node) {
        return node.getParentNode().getParentNode();
    }

    private void prePrint(Node node) {
        System.out.print(node.getValue() + "," + (node.getColor() ? "red" : "black") + "  ");
        if (node.getLeftNode() != null) {
            this.prePrint(node.getLeftNode());
        }
        if (node.getRightNode() != null) {
            this.prePrint(node.getRightNode());
        }
    }

    private void midPrint(Node node) {
        if (node.getLeftNode() != null) {
            this.midPrint(node.getLeftNode());
        }
        System.out.print(node.getValue() + "," + (node.getColor() ? "red" : "black") + "," + node.getNumber() + "  ");
        // List.add(node.getValue());
        if (node.getRightNode() != null) {
            this.midPrint(node.getRightNode());
        }
    }

    static class Node {
        private Integer value;
        private Boolean color;// true-red，false-black
        private Integer number;
        private Node left;
        private Node right;
        private Node parent;

        public Node(Integer value) {
            this.value = value;
            this.color = true;// Default Set red
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Boolean getColor() {
            return color;
        }

        public void setColor(Boolean color) {
            this.color = color;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Integer getLeftNumber() {
            return left.number;
        }

        public void setLeftNumber(Integer number) {
            left.number = number;
        }

        public Integer getRightNumber() {
            return right.number;
        }

        public void setRightNumber(Integer number) {
            right.number = number;
        }

        public Node getLeftNode() {
            return left;
        }

        public void setLeftNode(Node left) {
            this.left = left;
        }

        public Node getRightNode() {
            return right;
        }

        public void setRightNode(Node right) {
            this.right = right;
        }

        public Node getParentNode() {
            return parent;
        }

        public void setParentNode(Node parent) {
            this.parent = parent;
        }
    }
}