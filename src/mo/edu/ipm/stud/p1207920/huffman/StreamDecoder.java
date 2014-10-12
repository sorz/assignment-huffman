package mo.edu.ipm.stud.p1207920.huffman;

import mo.edu.ipm.stud.p1207920.huffman.exceptions.UnexpectedEndOfStreamException;
import mo.edu.ipm.stud.p1207920.huffman.exceptions.UnknownCodeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamDecoder {
    private Dictionary dictionary;

    public StreamDecoder(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        Node treeRoot = dictionary.getTreeRoot();
        Node node = treeRoot;
        int code = inputStream.read();
        int mask = 0x80;  // The highest bit of a byte.
        long characterCounter = 0;
        while (node != null) {
            if (node.getCharacter() != Node.NO_CHARACTER) {
                // Reach leaf, character found.
                outputStream.write(node.getCharacter());
                node = treeRoot;
                ++characterCounter;
                if (characterCounter >= dictionary.getFileSize())
                    return;
                continue;
            }
            if (code == -1)  // EOF reached.
                    throw new UnexpectedEndOfStreamException();

            if ((code & mask) == 0)  // 0  is left, 1 is right.
                node = node.getLeftChild();
            else
                node = node.getRightChild();

            mask >>= 1;
            if (mask == 0) {
                // Read next byte, reset mask to the highest bit of byte.
                code = inputStream.read();
                mask = 0x80;
            }
        }
        // node == null.
        throw new UnknownCodeException();
    }
}
