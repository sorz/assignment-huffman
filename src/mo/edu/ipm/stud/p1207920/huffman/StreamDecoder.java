package mo.edu.ipm.stud.p1207920.huffman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamDecoder {
    private Dictionary dictionary;

    public StreamDecoder(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        int position = 0;
        while (true) {
            inputStream.mark(128);
            Dictionary.CharacterWithCodeLength read = dictionary.readCharacter(inputStream, position);
            if (read == null) // EOF reached.
                return;
            outputStream.write(read.getCharacter());
            position = read.getCodeLength() % 8;

            if (position != 0) {
                // Back to before the last byte.
                inputStream.reset();
                inputStream.skip(read.getCodeLength() / 8);
            }
        }
    }

}
