package mo.edu.ipm.stud.p1207920.huffman;

import java.io.Serializable;

public class Bits implements Serializable {
    private int bits;
    private int length;

    public Bits(int bits, int length) {
        this.bits = bits;
        this.length = length;
    }

    public int getBits() {
        return bits;
    }

    public int getLength() {
        return length;
    }
}
