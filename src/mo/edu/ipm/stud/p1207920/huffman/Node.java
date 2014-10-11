package mo.edu.ipm.stud.p1207920.huffman;


public class Node implements Comparable<Node> {
    private int character;
    private int priority;
    private Node leftChild;
    private Node rightChild;

    public Node(int character, int priority) {
        this.character = character;
        this.priority = priority;
    }

    public Node(Node leftChild, Node rightChild, int priority) {
        this.priority = priority;
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
