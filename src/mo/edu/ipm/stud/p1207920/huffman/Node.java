package mo.edu.ipm.stud.p1207920.huffman;


import java.io.Serializable;

public class Node implements Comparable<Node>, Serializable {
    final static public int NO_CHARACTER = 0;

    private final int character;
    private final int priority;
    private Node leftChild;
    private Node rightChild;


    public Node(int character, int priority) {
        this.character = character;
        this.priority = priority;
    }

    public Node(Node leftChild, Node rightChild, int priority) {
        this(NO_CHARACTER, priority);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public int getCharacter() {
        return character;
    }

    public int getPriority() {
        return priority;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    @Override
    public int compareTo(Node o) {
        return getPriority() - o.getPriority();
    }
}
