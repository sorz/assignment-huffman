package mo.edu.ipm.stud.p1207920.huffman.exceptions;

public class IllegalCharacterException extends Exception {
    private int character;

    public IllegalCharacterException(int character) {
        super();
        this.character = character;
    }

    public int getCharacter() {
        return character;
    }
}
