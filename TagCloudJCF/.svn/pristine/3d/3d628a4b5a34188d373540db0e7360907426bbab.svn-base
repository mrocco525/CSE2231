import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine1L;

/**
 * Pulls the top n words by occurences from the desired input file and places
 * them into a cloud.
 *
 * @author Caden Sweeney, Mason Rocco
 *
 */
public final class TagCloud {

    /**
     * Smallest number in the cloud.
     */
    private static int smallest = 0;

    /**
     * Largest number in the cloud.
     */
    private static int largest = 0;

    /**
     * Definition of whitespace characters.
     */
    private static final String SEPARATORS = " ,-.!?[]';:/()\t\n\r<>\"{}_~`!@#$%^&*|";

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private TagCloud() {
    }

    /**
     * Compare {@code String}s in lexicographic order.
     */
    private static class StringLT
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            String k1 = o1.key();
            String k2 = o2.key();
            return k1.compareToIgnoreCase(k2);
        }
    }

    /**
     * Compare {@code Integer}s least to greatest.
     */
    private static class IntegerLT
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            Integer v1 = o1.value();
            Integer v2 = o2.value();
            return v2.compareTo(v1);
        }
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position) {
        assert text != null : "Violation of: text is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        String toReturn = "";
        int num = position;
        String letter = text.charAt(num) + "";
        while (!SEPARATORS.contains(letter) && num < text.length()) {
//Checks that the letter is not a separator and the the position number is less
//than the length.
            toReturn = toReturn + letter; //Adds the letter to the string
            num++; //Advances the position number
            if (num <= text.length() - 1) { //Checks the position number
                letter = text.charAt(num) + "";
            }
        }
        if (toReturn.length() == 0
                && SEPARATORS.contains(text.charAt(num) + "")) {
            toReturn = text.charAt(num) + "";
//Turns the character into a string
        }
        toReturn = toReturn.toLowerCase();
        return toReturn; //Returns the next word or separator
    }

    /**
     * Prints everything into the destination {@code outFile} using {@code out}
     * up until the table values are needed.
     *
     * @param out
     *            SimpleWriter that writes to the destination file
     * @param outputFile
     *            {@code String} of the destination file
     * @param inputFile
     *            {@code String} of the input file
     * @param n
     *            Number of words desired in the cloud
     */
    public static void outputHeader(SimpleWriter out, String outputFile,
            String inputFile, int n) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Top " + n + " words in " + inputFile + "</title>");
        out.println(
                "<link href=\"http://web.cse.ohio-state.edu/software/2231/web-sw2/assignments/projects/tag-cloud-generator/data/tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println(
                "<link href=\"tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Top " + n + " words in " + inputFile + "</h2>");
        out.println("<hr>");
        out.println("<div class=\"cdiv\">");
        out.println("<p class=\"cbox\">");
    }

    /**
     * Takes lines from {@code in} and pulls out words, then prints to the
     * destination file using {@code out}.
     *
     * @param in
     *            SimpleReader that reads lines from the file
     * @param out
     *            SimpleWriter that writes to the destination file
     * @param n
     *            Number of words to be added into the cloud
     */
    public static void pullOutWords(SimpleReader in, SimpleWriter out, int n) {
        Map<String, Integer> mapOfWords = new Map1L<String, Integer>();
        Queue<String> queueOfWords = new Queue1L<String>();
        while (!in.atEOS()) { //Loops while the reader is not at the end
            String word = in.nextLine(); //Pulls a line from the file
            int position = 0;
            while (position < word.length()) {
                String wordToBeAdded = nextWordOrSeparator(word, position);
                if (!SEPARATORS.contains(wordToBeAdded.charAt(0) + "")) {
                    addOrIncrement(mapOfWords, queueOfWords, wordToBeAdded);
//Sends a word to be added into the queue and map
                }
                position += wordToBeAdded.length();
            }
        }
        SortingMachine<Map.Pair<String, Integer>> si = sortBySize(mapOfWords);
        Map<String, Integer> firstN = new Map1L<String, Integer>();
        for (int i = 0; i < n; i++) {
            Map.Pair<String, Integer> temp = si.removeFirst();
            firstN.add(temp.key(), temp.value());
        }
        SortingMachine<Map.Pair<String, Integer>> si2 = sortByLetter(firstN);
        printToFile(out, si2);
//Sends the map, queue and printwriter to print the text into the html file
    }

    /**
     * Either adds {@code word} or increments the value associated with
     * {@code word} within the {@code map} depending on if it is already in.
     * Also adds {@code word} to the {@code q} if the {@code word} is already in
     * the {@code map}.
     *
     * @param map
     *            Map containing every individual word and how many occurrences
     *            of that word
     * @param q
     *            Queue of type {@code String} containing every individual word
     * @param word
     *            Word to either be added to the queue and map or increment the
     *            value in the map
     */
    public static void addOrIncrement(Map<String, Integer> map, Queue<String> q,
            String word) {
        if (map.hasKey(word)) {
            int num = map.value(word); //Finds the value with the word in the map
            num++; //Increases the value from the map
            map.replaceValue(word, num);
//Replaces the value from the map with the increased version
            if (num > largest) {
                largest = num;
            }
        } else {
            map.add(word, 1); //Adds the word into the map
            q.enqueue(word); //Adds the word into the queue
            smallest = 1;
        }
    }

    /**
     * Sorts the Map into a sorting machine by size.
     *
     * @param m
     *            the Map with the words and appearances.
     * @return sorted sorting machine by number of occurrences.
     */
    public static SortingMachine<Map.Pair<String, Integer>> sortBySize(
            Map<String, Integer> m) {
        Comparator<Map.Pair<String, Integer>> ci = new IntegerLT();
        SortingMachine<Map.Pair<String, Integer>> si = new SortingMachine1L<Map.Pair<String, Integer>>(
                ci);
        while (m.size() > 0) {
            si.add(m.removeAny());
        }
        si.changeToExtractionMode();
        return si;
    }

    /**
     * Sorts the Map into a sorting machine by letter.
     *
     * @param m
     *            the Map with the words and appearances.
     * @return sorted sorting machine in alphabetical order.
     */
    public static SortingMachine<Map.Pair<String, Integer>> sortByLetter(
            Map<String, Integer> m) {
        Comparator<Map.Pair<String, Integer>> ci = new StringLT();
        SortingMachine<Map.Pair<String, Integer>> si = new SortingMachine1L<Map.Pair<String, Integer>>(
                ci);
        while (m.size() > 0) {
            si.add(m.removeAny());
        }
        si.changeToExtractionMode();
        return si;
    }

    /**
     * Sets the font size based on how many occurrences a word has.
     *
     * @param n
     *            The number of occurrences of the word.
     * @return scaled font size for the value.
     */
    public static int getFontSize(int n) {
        int fontSize = 48 - 11;
        fontSize = fontSize * (n - smallest);
        fontSize = fontSize / (largest - smallest);
        return fontSize + 11;
    }

    /**
     * Prints the table in alphabetical order from {@code wordQueue} to access
     * the {@code map} to the destination file using {@code out}.
     *
     * @param si
     *            {@code SortingMachine} of words and integers in alphabetical
     *            order
     * @param out
     *            SimpleWriter that writes to the destination file
     */
    public static void printToFile(SimpleWriter out,
            SortingMachine<Map.Pair<String, Integer>> si) {
        while (si.size() > 0) {
            Map.Pair<String, Integer> pair = si.removeFirst();
            String key = pair.key();
            int value = pair.value();
            out.println("<span style=\"cursor:default\" class=\"f"
                    + getFontSize(value) + "\" title=\"count: " + value + "\">"
                    + key + "</span>");
        }
    }

    /**
     * Prints the footer of the destination file.
     *
     * @param out
     *            SimpleWriter that writes to the destination file
     */
    public static void outputFooter(SimpleWriter out) {
        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.print("Enter an input file: ");
        String inputFile = in.nextLine(); //Saves the input file
        out.print("Enter an output file: ");
        String outputFile = in.nextLine(); //Saves the output file
        out.print("Enter the desired number of words: ");
        int number = in.nextInteger();
        SimpleWriter output = new SimpleWriter1L(outputFile);
        SimpleReader input = new SimpleReader1L(inputFile);
        outputHeader(output, outputFile, inputFile, number);
//Prints everything up to the table into the output html file
        pullOutWords(input, output, number);
        outputFooter(output);
//Prints everything after the table into the output html file
        /*
         * Close input and output streams
         */
        in.close();
        out.close();
        output.close();
    }

}
