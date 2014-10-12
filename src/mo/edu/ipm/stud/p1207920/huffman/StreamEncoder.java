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
        int writeBuffer = 0;
        int writeLength = 0;
        byte[] readBuffer = new byte[2048];
        int readLength;
        while ((readLength = inputStream.read(readBuffer)) != -1) {
            for (int i = 0; i < readLength; ++i) {
                Code code = dictionary.getCode(readBuffer[i]);
                writeBuffer <<= code.getLength();
                writeBuffer |= code.getCode();
                writeLength += code.getLength();
                while (writeLength >= 8) {
                    outputStream.write((writeBuffer >> (writeLength - 8)) & 0xff);
                    writeLength -= 8;
                }
            }
        }
        if (writeLength > 0)
            outputStream.write((writeBuffer << (8 - writeLength)) & 0xff);
    }
}
