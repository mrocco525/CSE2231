import components.statement.Statement;
import components.statement.StatementKernel;

/**
 * Put a short phrase describing the program here.
 *
 * @author Mason Rocco
 *
 */
public final class SimplifyIfElse {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private SimplifyIfElse() {
    }

    /**
     * Refactors the given {@code Statement} so that every IF_ELSE statement
     * with a negated condition (NEXT_IS_NOT_EMPTY, NEXT_IS_NOT_ENEMY,
     * NEXT_IS_NOT_FRIEND, NEXT_IS_NOT_WALL) is replaced by an equivalent
     * IF_ELSE with the opposite condition and the "then" and "else" BLOCKs
     * switched. Every other statement is left unmodified.
     *
     * @param s
     *            the {@code Statement}
     * @updates s
     * @ensures <pre>
     * s = [#s refactored so that IF_ELSE statements with "not"
     *   conditions are simplified so the "not" is removed]
     * </pre>
     */
    public static void simplifyIfElse(Statement s) {
        switch (s.kind()) {
            case BLOCK: {

                // TODO - fill in case

                break;
            }
            case IF: {

                break;
            }
            case IF_ELSE: {
                Statement s1 = s.newInstance();
                Statement s2 = s.newInstance();
                StatementKernel.Condition c = s.disassembleIfElse(s1, s2);
                s.assembleIfElse(c, s2, s1);
                break;
            }
            case WHILE: {

                break;
            }
            case CALL: {
                // nothing to do here since the call does not need to be changed.
                break;
            }
            default: {
                // this will never happen since all possible cases are met.
                break;
            }
        }
    }
}
