package mo.edu.ipm.stud.p1207920.huffman.exceptions;


import java.io.IOException;

public class UnexpectedFileFormatException extends IOException {
    public UnexpectedFileFormatException() {
    }

    public UnexpectedFileFormatException(Throwable e) {
        super(e);
    }
}