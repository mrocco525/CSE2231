import components.simplewriter.SimpleWriter;
import components.statement.Statement;
import components.statement.Statement1;

/**
 * Layered implementation of secondary method {@code prettyPrint} for
 * {@code Statement}.
 */
public final class Hw24 extends Statement1 {
    /**
     * Pretty prints {@code this} to the given stream {@code out} {@code offset}
     * spaces from the left margin using
     * {@link components.program.Program#INDENT_SIZE Program.INDENT_SIZE} spaces
     * for each indentation level.
     *
     * @param out
     *            the output stream
     * @param offset
     *            the number of spaces to be placed before every nonempty line
     *            of output; nonempty lines of output that are indented further
     *            will, of course, continue with even more spaces
     * @updates out.content
     * @requires out.is_open and 0 <= offset
     * @ensures <pre>
     * out.content =
     *   #out.content * [this pretty printed offset spaces from the left margin
     *                   using Program.INDENT_SIZE spaces for indentation]
     * </pre>
     */
    @Override
    public void prettyPrint(SimpleWriter out, int offset) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";
        assert offset >= 0 : "Violation of: 0 <= offset";
        int indent = 4;
        switch (this.kind()) {
            case BLOCK: {
                for (int i = 0; i < this.lengthOfBlock(); i++) {
                    Statement block = this.removeFromBlock(i);
                    block.prettyPrint(out, offset);
                    this.addToBlock(i, block);
                }
                break;
            }
            case IF: {
                Statement ifBlock = this.newInstance();
                Condition condition = this.disassembleIf(ifBlock);
                printSpaces(out, offset);
                out.println("IF " + toStringCondition(condition) + " THEN");
                ifBlock.prettyPrint(out, offset + indent);
                printSpaces(out, offset);
                out.println("END IF");
                this.assembleIf(condition, ifBlock);
                break;
            }
            case IF_ELSE: {
                Statement ifBlock = this.newInstance();
                Statement elseBlock = this.newInstance();
                Condition condition = this.disassembleIfElse(ifBlock,
                        elseBlock);
                printSpaces(out, offset);
                out.println("IF " + toStringCondition(condition) + " THEN");
                ifBlock.prettyPrint(out, indent + offset);
                printSpaces(out, offset);
                out.println("ELSE");
                elseBlock.prettyPrint(out, indent + offset);
                printSpaces(out, offset);
                out.println("END IF");
                this.assembleIfElse(condition, ifBlock, elseBlock);
                break;
            }
            case WHILE: {
                Statement whileBlock = this.newInstance();
                Condition condition = this.disassembleWhile(whileBlock);
                printSpaces(out, offset);
                out.println("WHILE " + toStringCondition(condition) + " DO");
                whileBlock.prettyPrint(out, offset + indent);
                printSpaces(out, offset);
                out.println("END WHILE");
                this.assembleWhile(condition, whileBlock);
                break;
            }
            case CALL: {
                String call = this.disassembleCall();
                printSpaces(out, offset);
                out.println(call);
                this.assembleCall(call);
                break;
            }
            default: {
                // this will never happen...
                break;
            }
        }
    }
}
