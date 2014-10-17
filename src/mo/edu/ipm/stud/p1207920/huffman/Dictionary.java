package mo.edu.ipm.stud.p1207920.huffman;


import mo.edu.ipm.stud.p1207920.huffman.exceptions.IllegalCharacterException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.PriorityQueue;

public class Dictionary implements Serializable {
    private final Bits[] codes;
    private final Node treeRoot;
    private final long fileSize;

    public static Dictionary generate(InputStream in) throws IOException, IllegalCharacterException {
        // Count the number of each character.
        int[] counter = new int[128];
        long size = 0;
        byte[] buffer = new byte[2048];
        int len;
        while ((len = in.read(buffer)) != -1) {
            for (int i = 0; i < len; ++i) {
                int character = buffer[i];
                if (character < 1)
                    throw new IllegalCharacterException(character);
                ++counter[character];
                ++size;
            }
        }

        // Create node and put into queue.
        PriorityQueue<Node> queue = new PriorityQueue<Node>(128);
        for (int i = 0; i < counter.length; ++i)
            if (counter[i] != 0)
                queue.add(new Node(i, counter[i]));

        // Build huffman tree.
        while (queue.size() > 1) {
            Node a = queue.poll();
            Node b = queue.poll();
            queue.add(new Node(a, b, a.getPriority() + b.getPriority()));
        }
        Node root = queue.poll();

        return new Dictionary(root, size);
    }

    private Dictionary(Node treeRoot, long fileSize) {
        // Generate codes from huffman tree.
        Bits[] codes = new Bits[128];
        calculateCodes(treeRoot, 0, 0, codes);

        this.treeRoot = treeRoot;
        this.codes = codes;
        this.fileSize = fileSize;
    }

    public Bits getCode(int character) throws IllegalCharacterException {
        if (character < 1 || character > 127)
            throw new IllegalCharacterException(character);
        return codes[character];
    }

    public Node getTreeRoot() {
        return treeRoot;
    }

    public long getFileSize() {
        return fileSize;
    }

    private static void calculateCodes(Node node, int code, int codeLength, Bits[] codes) {
        if (node.getLeftChild() != null)
            calculateCodes(node.getLeftChild(), code << 1, codeLength + 1, codes);
        if (node.getRightChild() != null)
            calculateCodes(node.getRightChild(), (code << 1) + 1, codeLength + 1, codes);
        if (node.getCharacter() != Node.NO_CHARACTER)
            codes[node.getCharacter()] = new Bits(code, codeLength);
    }

}
