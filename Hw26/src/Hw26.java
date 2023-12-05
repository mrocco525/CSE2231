import components.queue.Queue;
import components.queue.Queue1L;
import components.simplereader.SimpleReader;
import components.statement.Statement1;

/**
 * Layered implementation of secondary method {@code prettyPrint} for
 * {@code Statement}.
 */
public final class Hw26 extends Statement1 {
    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code SEPARATORS}) or "separator string" (maximal length string of
     * characters in {@code SEPARATORS}) in the given {@code text} starting at
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
     * if entries(text[position, position + 1)) intersection entries(SEPARATORS) = {}
     * then
     *   entries(nextWordOrSeparator) intersection entries(SEPARATORS) = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection entries(SEPARATORS) /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of entries(SEPARATORS)  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of entries(SEPARATORS))
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position) {
        String nextWord;
        int last = position;
        boolean isSeparator = false;
        while (last < text.length() && !isSeparator) {
            if (!SEPARATORS.contains(text.charAt(last) + "")) {
                last++;
            } else {
                isSeparator = true;
            }
        }
        nextWord = text.substring(position, last);
        if (SEPARATORS.contains(text.substring(position, position + 1))) {
            nextWord = text.substring(position, position + 1);
        }
        return nextWord;
    }

    /**
     * Tokenizes the entire input getting rid of all whitespace separators and
     * returning the non-separator tokens in a {@code Queue<String>}.
     *
     * @param in
     *            the input stream
     * @return the queue of tokens
     * @requires in.is_open
     * @ensures <pre>
     * tokens =
     *   [the non-whitespace tokens in #in.content] * <END_OF_INPUT>  and
     * in.content = <>
     * </pre>
     */
    public static Queue<String> tokens(SimpleReader in) {
        Queue<String> tokens = new Queue1L<String>();
        while (!in.atEOS()) {
            int position = 0;
            String line = in.nextLine();
            while (position < line.length()) {
                String word = nextWordOrSeparator(line, position);
                if (!SEPARATORS.contains(line.charAt(position) + "")) {
                    tokens.enqueue(word);
                }
                position += word.length();
            }
        }
        tokens.enqueue(END_OF_INPUT);
        return tokens;
    }

}
