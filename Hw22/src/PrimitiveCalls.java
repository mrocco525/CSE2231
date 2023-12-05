import components.statement.Statement;

/**
 * Put a short phrase describing the program here.
 *
 * @author Mason Rocco
 *
 */
public final class PrimitiveCalls {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private PrimitiveCalls() {
    }

    /**
     * Reports the number of calls to primitive instructions (move, turnleft,
     * turnright, infect, skip) in a given {@code Statement}.
     *
     * @param s
     *            the {@code Statement}
     * @return the number of calls to primitive instructions in {@code s}
     * @ensures <pre>
     * countOfPrimitiveCalls =
     *  [number of calls to primitive instructions in s]
     * </pre>
     */
    public static int countOfPrimitiveCalls(Statement s) {
        int count = 0;
        switch (s.kind()) {
            case BLOCK: {
                /*
                 * Add up the number of calls to primitive instructions in each
                 * nested statement in the BLOCK.
                 */
                count = count + s.lengthOfBlock();
                break;
            }
            case IF: {
                /*
                 * Find the number of calls to primitive instructions in the
                 * body of the IF.
                 */
                count = count + s.lengthOfBlock();
                break;
            }
            case IF_ELSE: {
                /*
                 * Add up the number of calls to primitive instructions in the
                 * "then" and "else" bodies of the IF_ELSE.
                 */
                Statement s1 = s.newInstance();
                Statement s2 = s.newInstance();
                s.disassembleIfElse(s1, s2);
                count = count + countOfPrimitiveCalls(s1)
                        + countOfPrimitiveCalls(s2);

                break;
            }
            case WHILE: {
                /*
                 * Find the number of calls to primitive instructions in the
                 * body of the WHILE.
                 */
                count = count + s.lengthOfBlock();
                break;
            }
            case CALL: {
                /*
                 * This is a leaf: the count can only be 1 or 0. Determine
                 * whether this is a call to a primitive instruction or not.
                 */
                String sCall = s.disassembleCall();
                if (sCall.length() > 0) {
                    count = count + 1;
                }
                break;
            }
            default: {
                // this will never happen since all possible cases are met.
                break;
            }
        }
        return count;
    }
}
