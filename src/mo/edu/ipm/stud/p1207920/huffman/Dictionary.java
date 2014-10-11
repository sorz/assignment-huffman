package mo.edu.ipm.stud.p1207920.huffman;


import java.io.*;
import java.util.PriorityQueue;

public class Dictionary {
    private Code[] codes;
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

        // Generate code for each character.
        Code[] codes = new Code[128];
        calculateCodes(root, 0, 0, codes);
        return new Dictionary(codes);
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

    public Dictionary(Code[] codes) {
        this.codes = codes;
    }

    public Code getCode(int character) throws IllegalCharacterException {
        if (character < 1 || character > 127)
            throw new IllegalCharacterException(character);
        return codes[character];
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
        if (node.getCharacter() != 0)
            codes[node.getCharacter()] = new Code(code, codeLength);
    }
}
