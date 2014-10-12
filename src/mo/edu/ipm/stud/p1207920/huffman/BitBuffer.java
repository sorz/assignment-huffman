package mo.edu.ipm.stud.p1207920.huffman;

import java.io.IOException;
import java.io.OutputStream;

public class BitBuffer {
    private byte[] byteBuffer;
    private int bytePosition = -1;
    private int bitBuffer;
    private int bitLength;

    public BitBuffer(int size) {
        byteBuffer = new byte[size];
    }

    public void writeBits(Code code) {
        bitBuffer <<= code.getLength();
        bitBuffer |= code.getCode();
        bitLength += code.getLength();

        if (bitLength >= 8)
            moveBitsToByteBuffer();
    }

    public boolean isFull() {
        return bytePosition >= byteBuffer.length;
    }

    private void moveBitsToByteBuffer() {
        while (bitLength >= 8 && !isFull()) {
            byteBuffer[++bytePosition] = (byte) ((bitBuffer >> (bitLength - 8)) & 0xff);
            bitLength -= 8;
        }
    }

    public Code getRemindingBits() {
        return new Code(bitBuffer & 0xff, bitLength);
    }

    public void writeOut(OutputStream outputStream) throws IOException {
        if (bitLength >= 8)
            moveBitsToByteBuffer();
        outputStream.write(byteBuffer, 0, bytePosition + 1);
        bytePosition = -1;
    }
}
