import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Program to copy a text file into another file.
 *
 * @author Mason Rocco
 *
 */
public final class Hw37 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Hw37() {
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments: input-file-name output-file-name
     */
    public static void main(String[] args) {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.print("Please input the name of the input file: ");
        String nameIn = "";
        BufferedReader input = null;
        try {
            nameIn = in.readLine();
            input = new BufferedReader(new FileReader(nameIn));
        } catch (IOException e) {
            System.err.println("Violation of: File can be found.");
            return;
        }
        System.out.print("Please input the name of the output file: ");
        String nameOut = "";
        PrintWriter output = null;
        try {
            nameOut = in.readLine();
            final int four = 4;
            //check suffix
            if (nameOut != null && nameOut
                    .substring(nameOut.length() - four, nameOut.length())
                    .equals(".txt")) {
                output = new PrintWriter(
                        new BufferedWriter(new FileWriter(nameOut)));
            } else {
                System.err.println("Violation: Output file format correct.");
                try {
                    input.close();
                } catch (IOException e1) {
                    System.err.println(
                            "Violation of: Input stream is unable to close.");
                }
                return;
            }
        } catch (IOException e) {
            System.err.println("Violation of: File can be written.");
            try {
                input.close();
            } catch (IOException e1) {
                System.err.println(
                        "Violation of: Input stream is unable to close.");
            }
            return;
        }
        String temp = "";
        try {
            temp = input.readLine();
            while (temp != null) {
                output.println(temp);
                temp = input.readLine();
            }
        } catch (IOException e) {
            System.err.println("Trouble read from the file.");
        }
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("Violation of: Input stream can close.");
            output.close();
            return;
        }
        output.close();
        try {
            in.close();
        } catch (IOException e) {
            System.err.println("Violation of: In stream can close.");
            return;
        }
    }
}
