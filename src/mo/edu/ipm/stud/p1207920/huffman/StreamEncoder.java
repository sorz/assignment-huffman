package mo.edu.ipm.stud.p1207920.huffman;


import mo.edu.ipm.stud.p1207920.huffman.exceptions.IllegalCharacterException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamEncoder {
    private Dictionary dictionary;

    public StreamEncoder(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void encode(InputStream inputStream, OutputStream outputStream)
            throws IOException, IllegalCharacterException {
        int character;
        int buffer = 0;
        int length = 0;
        while ((character = inputStream.read()) != -1) {
            Code code = dictionary.getCode(character);
            buffer <<= code.getLength();
            buffer |= code.getCode();
            length += code.getLength();
            while (length >= 8) {
                outputStream.write(buffer >> (length - 8));
                length -= 8;
            }
        }
        if (length > 0)
            outputStream.write(buffer);
    }
}
