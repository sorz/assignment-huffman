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
        byte[] readBuffer = new byte[2048];
        BitBuffer bitBuffer = new BitBuffer(2048);
        int readLength;
        while ((readLength = inputStream.read(readBuffer)) != -1) {
            for (int i = 0; i < readLength; ++i) {
                Bits bits = dictionary.getCode(readBuffer[i]);
                bitBuffer.writeBits(bits);
                if (bitBuffer.isFull())
                    bitBuffer.writeOut(outputStream);
            }
            bitBuffer.writeOut(outputStream);
        }
        Bits bits = bitBuffer.getRemindingBits();
        if (bits.getLength() > 0)
            outputStream.write((bits.getBits() << (8 - bits.getLength())) & 0xff);
    }
}
