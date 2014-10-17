import mo.edu.ipm.stud.p1207920.huffman.Dictionary;
import mo.edu.ipm.stud.p1207920.huffman.StreamDecoder;
import mo.edu.ipm.stud.p1207920.huffman.exceptions.UnexpectedEndOfStreamException;
import mo.edu.ipm.stud.p1207920.huffman.exceptions.UnknownCodeException;

import java.io.*;

public class Decoder {
    static private final int EXIT_CODE_SUCCESS = 0;
    static private final int EXIT_CODE_ARGS_ERROR = 1;
    static private final int EXIT_CODE_IO_ERROR = 2;

    public static void main(String[] args) {
        // Check arguments.
        if (args.length != 3) {
            System.err.println("Usage: java Decode abc.dat abc.dic abc-result.txt");
            System.exit(EXIT_CODE_ARGS_ERROR);
        }

        long beginTime = System.currentTimeMillis();
        int result = decodeFile(args[0], args[1], args[2]);
        if (result == 0) {
            long runningTime = System.currentTimeMillis() - beginTime;
            System.out.printf("(%.3fs in total.)", runningTime / 1000.0);
        } else {
            System.out.print("Decoding was interrupted, output is incomplete or totally wrong.");
        }
        System.exit(result);
    }

    private static int decodeFile(String inputPath, String dictionaryPath, String outputPath) {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        File dictionaryFile = new File(dictionaryPath);

        // Load dictionary.
        System.out.println("Loading dictionary...");
        Dictionary dictionary;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dictionaryFile));
            dictionary = (Dictionary) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.err.printf("Dictionary file not found on %s\n", dictionaryFile.getPath());
            return EXIT_CODE_IO_ERROR;
        } catch (ClassNotFoundException e) {
            System.err.println("Invalid or damaged dictionary file.");
            return EXIT_CODE_IO_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return EXIT_CODE_IO_ERROR;
        }

        // Decode
        System.out.println("Decoding...");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(inputFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            new StreamDecoder(dictionary).decode(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            System.err.printf("Input file not found on %s\n", inputFile.getPath());
            return EXIT_CODE_IO_ERROR;
        } catch (UnexpectedEndOfStreamException e) {
            System.err.println("Unexpected end of file, please whether the data file is complete.");
            return EXIT_CODE_IO_ERROR;
        } catch (UnknownCodeException e) {
            System.err.println("Unknown code found, please check whether the data file and dictionary are matched.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
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
