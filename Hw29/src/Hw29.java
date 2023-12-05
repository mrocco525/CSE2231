import components.queue.Queue;

public final class Hw29 {
    /**
     * Evaluates a Boolean expression and returns its value.
     *
     * @param tokens
     *            the {@code Queue<String>} that starts with a bool-expr string
     * @return value of the expression
     * @updates tokens
     * @requires [a bool-expr string is a prefix of tokens]
     * @ensures <pre>
     * valueOfBoolExpr =
     *   [value of longest bool-expr string at start of #tokens]  and
     * #tokens = [longest bool-expr string at start of #tokens] * tokens
     * </pre>
     */
    public static boolean valueOfBoolExpr(Queue<String> tokens) {
        while (tokens.length() > 0) {
            switch (tokens.dequeue()) {
                case "T": {
                    return true;
                }
                case "F": {
                    return false;
                }
                case "NOT": {
                    return !valueOfBoolExpr(tokens);
                }
                case "(": {
                    boolean first = valueOfBoolExpr(tokens);
                    boolean and = valueOfBoolExpr(tokens);
                    boolean second = valueOfBoolExpr(tokens);
                    tokens.dequeue(); // )
                    if (and) {
                        return first && second;
                    } else {
                        return first || second;
                    }
                }
                case "AND": {
                    return true;
                }
                case "OR": {
                    return false;
                }
                default:
                    return false;
            }
        }
        return false;
    }
}
