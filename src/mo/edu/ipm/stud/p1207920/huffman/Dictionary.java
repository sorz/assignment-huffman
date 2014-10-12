package mo.edu.ipm.stud.p1207920.huffman;


import mo.edu.ipm.stud.p1207920.huffman.exceptions.IllegalCharacterException;
import mo.edu.ipm.stud.p1207920.huffman.exceptions.UnexpectedFileFormatException;

import java.io.*;
import java.util.PriorityQueue;

public class Dictionary {
    private Code[] codes;
    private Node treeRoot;
    private final static String FILE_HEADER = "HF-Dict";
    private final static int EOF_FLAG = -233;

    public static Dictionary generate(InputStream in) throws IOException, IllegalCharacterException {
        // Count the number of each character.
        int[] counter = new int[128];
        int character;
        while ((character = in.read()) != -1) {
            if (character < 1 || character > 127)
                throw new IllegalCharacterException(character);
            ++counter[character];
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

        return new Dictionary(root);
    }

    public static Dictionary load(File file) throws IOException {
        InputStream inputStream = null;
        Code[] codes;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            DataInput input = new DataInputStream(inputStream);
            if (!input.readUTF().equals(FILE_HEADER))
                throw new UnexpectedFileFormatException();

            int codesNum = input.readInt();
            codes = new Code[codesNum];
            int i;
            while ((i = input.readInt()) != EOF_FLAG)
                codes[i] = new Code(input.readInt(), input.readInt());
        } catch (EOFException e) {
            throw new UnexpectedFileFormatException(e);
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
        return new Dictionary(codes);
    }


    public Dictionary(Node treeRoot) {
        // Generate codes from huffman tree.
        Code[] codes = new Code[128];
        calculateCodes(treeRoot, 0, 0, codes);

        this.treeRoot = treeRoot;
        this.codes = codes;
    }

    public Dictionary(Code[] codes) {
        // Generate huffman tree from codes.
        Node root = new Node();
        for (int i = 0; i < codes.length; ++i) {
            if (codes[i] == null)
                continue;
            int mask = (int) Math.pow(2, codes[i].getLength() - 1);
            createPathToCharacter(i, codes[i].getCode(), mask, root);
        }

        this.codes = codes;
        this.treeRoot = root;
    }

    public Code getCode(int character) throws IllegalCharacterException {
        if (character < 1 || character > 127)
            throw new IllegalCharacterException(character);
        return codes[character];
    }

    public Node getTreeRoot() {
        return treeRoot;
    }


    public void save(OutputStream outputStream) throws IOException {
        DataOutput output = new DataOutputStream(outputStream);
        output.writeUTF(FILE_HEADER);
        output.writeInt(codes.length);
        for (int i = 0; i < codes.length; ++i) {
            if (codes[i] == null)
                continue;
            output.writeInt(i);
            output.writeInt(codes[i].getCode());
            output.writeInt(codes[i].getLength());
        }
        output.writeInt(EOF_FLAG);
    }

    private static void calculateCodes(Node node, int code, int codeLength, Code[] codes) {
        if (node.getLeftChild() != null)
            calculateCodes(node.getLeftChild(), code << 1, codeLength + 1, codes);
        if (node.getRightChild() != null)
            calculateCodes(node.getRightChild(), (code << 1) + 1, codeLength + 1, codes);
        if (node.getCharacter() != Node.NO_CHARACTER)
            codes[node.getCharacter()] = new Code(code, codeLength);
    }

    private static Node createPathToCharacter(int character, int code, int mask, Node parent) {
        if (mask == 0)
            return new Node(character);
        if (parent == null)
            parent = new Node();
        if ((code & mask) == 0)  // 0 is left, 1 is right.
            parent.setLeftChild(createPathToCharacter(character, code, mask >> 1, parent.getLeftChild()));
        else
            parent.setRightChild(createPathToCharacter(character, code, mask >> 1, parent.getRightChild()));
        return parent;
    }
}
