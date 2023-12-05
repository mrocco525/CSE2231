import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine1L;

/**
 * Build a program to generate tag clouds.
 *
 * @author Xingyue Zhao & Runting Shao
 */
public final class TagCloudEx {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Definition of separators.
     */
    private static final String SEPARATORS = " \t, \n\r,.<>/?;:\"'{}[]_-+=~`!@#$%^&*()|";

    /**
     * Definition of fMax.
     */
    private static final int FMAX = 48;

    /**
     * Definition of fMin.
     */
    private static final int FMIN = 11;

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private TagCloudEx() {
    }

    /**
     * Counting occurrence of words in Map using {@input}.
     *
     * @param input
     *            source of terms and definitions, one per line
     * @return separateWords set of strings from {@code input}
     * @requires input.is_open
     * @ensures <pre>
     * input.is_open and input.content = <>
     * concatenation = terms * definitions
     * </pre>
     */
    private static Map<String, Integer> separateWords(SimpleReader input) {
        assert input != null : "Violation of: input is not null";
        Map<String, Integer> terms = new Map1L<>();
        Set<Character> separator = new Set1L<>();
        generateElements(SEPARATORS, separator);
        while (!input.atEOS()) {
            StringBuilder line = new StringBuilder(input.nextLine());
            int position = 0;
            while (position < line.length()) {
                String word = nextWord(line.toString(), position).toLowerCase();
                if (Character.isLetter(word.charAt(0))) {
                    int check = 1;
                    if (terms.hasKey(word)) {
                        check += terms.value(word);
                        terms.replaceValue(word, check);
                    } else {
                        terms.add(word, check);
                    }
                }
                position += word.length();
            }
        }
        return terms;
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param strSet
     *            the {@code Set} to be replaced
     * @replaces strSet
     * @ensures strSet = entries(str)
     */
    private static void generateElements(String str, Set<Character> strSet) {
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";
        for (int i = 0; i < str.length(); i++) {
            char a = str.charAt(i);
            if (!strSet.contains(a)) {
                strSet.add(a);
            }
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
    private static String nextWord(String text, int position) {
        assert text != null : "Violation of: text is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";
        Set<Character> separator = new Set1L<>();
        generateElements(SEPARATORS, separator);
        String part = text.substring(position);
        StringBuilder out = new StringBuilder();
        if (separator.contains(text.charAt(position))) {
            int i = 0;
            boolean check = true;
            while (i < part.length() && check) {
                if (separator.contains(part.charAt(i))) {
                    out.append(part.charAt(i));
                } else {
                    check = false;
                }
                i++;
            }
        } else {
            int j = 0;
            boolean check = true;
            while (j < part.length() && check) {
                if (!separator.contains(part.charAt(j))) {
                    out.append(part.charAt(j));
                } else {
                    check = false;
                }
                j++;
            }
        }
        return out.toString();
    }

    /**
     * Compare {@code String}s in lexicographic order.
     */
    private static class IntegerLT
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            return o2.value().compareTo(o1.value());
        }
    }

    /**
     * Compare {@code String}s in lexicographic order.
     */
    private static class StringLT
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            return o1.key().compareToIgnoreCase(o2.key());
        }
    }

    /**
     * Sort Map{@code occurrences} by decreasing order.
     *
     * @param occurences
     *            word counts
     * @return sortInt sorted SortingMachine by decreasing order
     */
    private static SortingMachine<Map.Pair<String, Integer>> sortInt(
            Map<String, Integer> occurences) {
        Comparator<Map.Pair<String, Integer>> ab = new IntegerLT();
        SortingMachine<Map.Pair<String, Integer>> sortInt = new SortingMachine1L<>(
                ab);
        while (occurences.size() > 0) {
            Map.Pair<String, Integer> temp = occurences.removeAny();
            sortInt.add(temp);
        }
        return sortInt;
    }

    /**
     * Sort SortingMachine{@code sortInt} by alphabetical order.
     *
     * @param sortInt
     *            sorted SortingMachine by decreasing order
     * @param number
     *            user input number
     * @return sortStr sorted SortingMachine by alphabetical order
     */
    private static SortingMachine<Map.Pair<String, Integer>> sortStr(
            SortingMachine<Map.Pair<String, Integer>> sortInt, int number) {
        Comparator<Map.Pair<String, Integer>> cs = new StringLT();
        SortingMachine<Map.Pair<String, Integer>> sortStr = new SortingMachine1L<>(
                cs);
        while (sortStr.size() < number) {
            Map.Pair<String, Integer> first = sortInt.removeFirst();
            sortStr.add(first);
        }
        return sortStr;
    }

    /**
     * Font size of the word.
     *
     * @param fMin
     *            minimal domain
     * @param min
     *            minimal range
     * @param k
     *            slope
     * @param pair
     *            one pair of word
     * @ensures 11 <= f <= 48
     * @return f
     */
    private static int fontSize(int fMin, int min, double k,
            Map.Pair<String, Integer> pair) {
        int pairValue = pair.value();
        int f = (int) ((pairValue - min) / k) + fMin;
        return f;
    }

    /**
     * Output heading of the file.
     *
     * @param out
     *            output stream
     * @param number
     *            user input number
     * @param nameIn
     *            user input name
     * @requires out.is_open
     * @ensures <pre>
     * out.is_open and output the file into {@code out}
     * </pre>
     */
    public static void heading(SimpleWriter out, String number, String nameIn) {
        out.println("<html>");
        out.println("<head>");
        out.println(
                "<title>Top " + number + " words in " + nameIn + "</title>");
        out.println(
                "<link href=\"http://cse.osu.edu/software/2231/web-sw2/assignments/proje"
                        + "cts/tag-cloud-generator/data/tagcloud.css\" rel=\"sty"
                        + "lesheet\" type=\"text/css\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Top " + number + " words in " + nameIn + "</h2>");
        out.println("<hr>");
        out.println("<div class =\"cdiv\">");
        out.println("<p class=\"cbox\">");
    }

    /**
     * Take out the max and min value in the sorted words occurences map. Sort
     * {@code sortedInt} alphabetically. Print into the {@code out}.
     *
     * @param number
     *            user input number
     * @param sortedInt
     *            sorted unique words
     * @param out
     *            output stream
     * @requires out.is_open
     * @ensures <pre>
     * out.is_open and output the file into {@code out}
     * </pre>
     */
    public static void contentOutput(int number,
            SortingMachine<Map.Pair<String, Integer>> sortedInt,
            SimpleWriter out) {
        SortingMachine<Map.Pair<String, Integer>> tempInt = sortedInt
                .newInstance();
        sortedInt.changeToExtractionMode();
        //the first pair of the sorted words (the biggest)
        Map.Pair<String, Integer> maxMap = sortedInt.removeFirst();
        int max = maxMap.value();
        tempInt.add(maxMap);
        //ignore pairs appear in the middle of the sorted words
        for (int n = 1; n < number - 1; n++) {
            tempInt.add(sortedInt.removeFirst());
        }
        //the last pair of the sorted words (the smallest)
        Map.Pair<String, Integer> minMap = sortedInt.removeFirst();
        int min = minMap.value();
        tempInt.add(minMap);
        tempInt.changeToExtractionMode();
        sortedInt.transferFrom(tempInt);
        SortingMachine<Map.Pair<String, Integer>> sortedStr = sortStr(sortedInt,
                number);
        sortedStr.changeToExtractionMode();
        double k = (double) (max - min) / (FMAX - FMIN);
        int count = 0;
        while (count < number) {
            Map.Pair<String, Integer> pair = sortedStr.removeFirst();
            out.println("<span style=\"cursor:default\" class=\"f"
                    + fontSize(FMIN, min, k, pair) + "\" title=\"count: "
                    + pair.value() + "\">" + pair.key() + "</span>");
            count++;
        }

    }

    /**
     * Output ending of the file.
     *
     * @param out
     *            output stream
     * @requires out.is_open
     * @ensures <pre>
     * out.is_open and output the file into {@code out}
     * </pre>
     */
    public static void ending(SimpleWriter out) {
        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();
        SimpleReader in = new SimpleReader1L();
        out.print("Please input the name of the input file: ");
        String nameIn = in.nextLine();
        SimpleReader input = new SimpleReader1L(nameIn);
        out.print("Please input the name of the output file: ");
        String nameOut = in.nextLine();
        SimpleWriter output = new SimpleWriter1L(nameOut);
        out.print("Please enter the number of words to be included: ");
        String integer = in.nextLine();
        int number = Integer.parseInt(integer);
        if (input.atEOS()) {
            out.println("The input file is empty!");
        } else {
            Map<String, Integer> word = separateWords(input);
            SortingMachine<Map.Pair<String, Integer>> sortedInt = sortInt(word);
            if (sortedInt.size() < number) {
                out.println("The input number out of bound! There are only "
                        + sortedInt.size() + " unique words in the file!");
            } else {
                heading(output, integer, nameIn);
                contentOutput(number, sortedInt, output);
                ending(output);
            }
        }
        input.close();
        output.close();
        out.close();
        in.close();
    }

}
