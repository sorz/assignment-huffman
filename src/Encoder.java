import mo.edu.ipm.stud.p1207920.huffman.Dictionary;
import mo.edu.ipm.stud.p1207920.huffman.StreamEncoder;
import mo.edu.ipm.stud.p1207920.huffman.exceptions.IllegalCharacterException;

import java.io.*;

public class Encoder {
    static private final int EXIT_CODE_SUCCESS = 0;
    static private final int EXIT_CODE_ARGS_ERROR = 1;
    static private final int EXIT_CODE_IO_ERROR = 2;


    public static void main(String[] args) {
        // Check arguments.
        if (args.length != 3) {
            System.err.println("Usage: java Encoder abc.txt abc.dat abc.dic");
            System.exit(EXIT_CODE_ARGS_ERROR);
        }
        long beginTime = System.currentTimeMillis();
        int result = encodeFile(args[0], args[1], args[2]);
        if (result == 0) {
            long runningTime = System.currentTimeMillis() - beginTime;
            System.out.printf("(%.3fs in total.)", runningTime / 1000.0);
        }
        System.exit(result);
    }

    static private int encodeFile(String inputPath, String outputPath, String dictionaryPath) {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        File dictionaryFile = new File(dictionaryPath);

        // Generate dictionary.
        InputStream inputStream;
        Dictionary dictionary;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(inputFile));
            // Only buffer less than 16 MiB to avoid using too much memory.
            inputStream.mark(16 * 1024 * 1024);
            System.out.println("Generating dictionary...");
            dictionary = Dictionary.generate(inputStream);
        } catch (FileNotFoundException e) {
            System.err.printf("Input file not found on %s\n", inputFile.getPath());
            return EXIT_CODE_IO_ERROR;
        } catch (IllegalCharacterException e) {
            System.err.printf("Illegal character (%d) found on input file.\n", e.getCharacter());
            System.err.println("According to the requirement of assignment, all character must be " +
                    "ASCII code from 1 to 127.");
            return EXIT_CODE_IO_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return EXIT_CODE_IO_ERROR;
        }

        // Write out dictionary.
        ObjectOutputStream dictionaryStream;
        try {
            dictionaryStream = new ObjectOutputStream(new FileOutputStream(dictionaryFile));
            System.out.println("Writing dictionary...");
            dictionaryStream.writeObject(dictionary);
            dictionaryStream.close();
        } catch (FileNotFoundException e) {
            System.err.printf("Cannot write dictionary to %s\n", dictionaryFile.getPath());
            System.exit(2);
        } catch (IOException e) {
            e.printStackTrace();
            return EXIT_CODE_IO_ERROR;
        }

        // Encoding.
        OutputStream outputStream = null;
        try {
            try {
                inputStream.reset();
                // May throw IOException (Resetting to invalid mark). Reopen it.
            } catch (IOException e) {
                inputStream.close();
                inputStream = new BufferedInputStream(new FileInputStream(inputFile));
            }
            outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            System.out.println("Encoding...");
            new StreamEncoder(dictionary).encode(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            System.err.printf("Cannot write encoded data to %s\n", outputFile.getPath());
            return EXIT_CODE_IO_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalCharacterException e) {
            // Cannot occur.
        } finally {
            try {
                inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
        System.out.print("Done.");
        return EXIT_CODE_SUCCESS;
    }

}
