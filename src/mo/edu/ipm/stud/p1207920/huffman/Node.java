package mo.edu.ipm.stud.p1207920.huffman;


public class Node implements Comparable<Node> {
    final static public int UNDEFINED_PRIORITY = 0;
    final static public int NO_CHARACTER = 0;

    private int character;
    private int priority;
    private Node leftChild;
    private Node rightChild;

    public Node() {
        this(NO_CHARACTER, UNDEFINED_PRIORITY);
    }

    public Node(int character) {
        this(character, UNDEFINED_PRIORITY);
    }

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

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    @Override
    public int compareTo(Node o) {
        return getPriority() - o.getPriority();
    }
}
