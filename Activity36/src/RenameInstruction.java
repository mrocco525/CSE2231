import components.statement.Statement;
import components.statement.StatementKernel.Condition;

/**
 * Put a short phrase describing the program here.
 *
 * @author Mason Rocco
 *
 */
public final class RenameInstruction {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private RenameInstruction() {
    }

    /**
     * @param s
     *            {@code Statement}
     * @param s1
     *            the instruction being renamed
     * @param s2
     *            the new name of the instructionx
     */
    public static void renameInstruction(Statement s, String s1, String s2) {
        switch (s.kind()) {
            case BLOCK: {
                int length = s.lengthOfBlock();
                for (int i = 0; i < length; i++) {
                    Statement subtree = s.removeFromBlock(i);
                    renameInstruction(subtree, s1, s2);
                    s.addToBlock(i, subtree);
                }
                break;
            }
            case IF: {
                Statement subtree = s.newInstance();
                Condition condition = s.disassembleIf(subtree);
                renameInstruction(subtree, s1, s2);
                s.assembleIf(condition, subtree);
            }
            case IF_ELSE: {

                Statement subtree1 = s.newInstance();
                Statement subtree2 = s.newInstance();
                Condition condition = s.disassembleIfElse(subtree1, subtree2);
                renameInstruction(subtree1, s1, s2);
                renameInstruction(subtree2, s1, s2);
                s.assembleIfElse(condition, subtree1, subtree2);

            }
            case WHILE: {

                Statement subtree = s.newInstance();
                Condition condition = s.disassembleWhile(subtree);
                renameInstruction(subtree, s1, s2);
                s.assembleWhile(condition, subtree);

            }
            case CALL: {
                String call = s.disassembleCall();
                if (call.equals(s1)) {
                    s.assembleCall(s2);
                } else {
                    s.assembleCall(call);
                }
            }
            default: //this will never happen
                break;
        }
    }
}
