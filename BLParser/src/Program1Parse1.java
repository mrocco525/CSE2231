import components.map.Map;
import components.map.Map.Pair;
import components.program.Program;
import components.program.Program1;
import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary method {@code parse} for {@code Program}.
 *
 * @author Caden Sweeney, Mason Rocco
 *
 */
public final class Program1Parse1 extends Program1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Parses a single BL instruction from {@code tokens} returning the
     * instruction name as the value of the function and the body of the
     * instruction in {@code body}.
     *
     * @param tokens
     *            the input tokens
     * @param body
     *            the instruction body
     * @return the instruction name
     * @replaces body
     * @updates tokens
     * @requires <pre>
     * [<"INSTRUCTION"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an instruction string is a proper prefix of #tokens]  and
     *    [the beginning name of this instruction equals its ending name]  and
     *    [the name of this instruction does not equal the name of a primitive
     *     instruction in the BL language] then
     *  parseInstruction = [name of instruction at start of #tokens]  and
     *  body = [Statement corresponding to the block string that is the body of
     *          the instruction string at start of #tokens]  and
     *  #tokens = [instruction string at start of #tokens] * tokens
     * else
     *  [report an appropriate error message to the console and terminate client]
     * </pre>
     */
    private static String parseInstruction(Queue<String> tokens,
            Statement body) {
        assert tokens != null : "Violation of: tokens is not null";
        assert body != null : "Violation of: body is not null";
        assert tokens.length() > 0 && tokens.front().equals("INSTRUCTION") : ""
                + "Violation of: <\"INSTRUCTION\"> is proper prefix of tokens";

        String instruction = tokens.dequeue();
        Reporter.assertElseFatalError(instruction.equals("INSTRUCTION"),
                "Expected: INSTUCTION, Found: " + instruction);
        String name = tokens.dequeue();
        String[] primitives = { "move", "turnleft", "turnright", "infect",
                "skip" };
        for (String primitive : primitives) {
            if (name.equals(primitive)) {
                Reporter.assertElseFatalError(name.equals(primitive),
                        "Instuction name must not be name of a primitive instruction");
            }
        }
        String is = tokens.dequeue();
        Reporter.assertElseFatalError(instruction.equals("INSTRUCTION"),
                "Expected: IS, Found: " + is);
        body.parseBlock(tokens);
        String end = tokens.dequeue();
        Reporter.assertElseFatalError(end.equals("END"),
                "Expected: END, Found: " + end);
        String endName = tokens.dequeue();
        Reporter.assertElseFatalError(endName.equals(name),
                "Name of the instruction must be the same at the start and end");
        return name;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Program1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(SimpleReader in) {
        assert in != null : "Violation of: in is not null";
        assert in.isOpen() : "Violation of: in.is_open";
        Queue<String> tokens = Tokenizer.tokens(in);
        this.parse(tokens);
    }

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";
        Program p = new Program1Parse1();
        String program = tokens.dequeue();
        Reporter.assertElseFatalError(program.equals("PROGRAM"),
                "Exepcted: PROGRAM, Found: " + program);
        String name = tokens.dequeue();

        String is = tokens.dequeue();
        Reporter.assertElseFatalError(is.equals("IS"),
                "Exepcted: IS, Found: " + is);

        Map<String, Statement> context = p.newContext();

        while (tokens.front().equals("INSTRUCTION")) {
            Statement instructionBody = new Statement1();
            String instructionName = parseInstruction(tokens, instructionBody);
            for (Pair<String, Statement> pair : context) {
                Reporter.assertElseFatalError(
                        !instructionName.equals(pair.key()),
                        "There should not be duplicate instruction names.");
            }
            context.add(instructionName, instructionBody);
        }
        String begin = tokens.dequeue();
        Reporter.assertElseFatalError(begin.equals("BEGIN"),
                "Exepcted: BEGIN, Found: " + begin);
        Statement programBody = p.newBody();
        programBody.parseBlock(tokens);
        String end = tokens.dequeue();
        Reporter.assertElseFatalError(end.equals("END"),
                "Exepcted: END, Found: " + end);
        String endName = tokens.dequeue();
        Reporter.assertElseFatalError(endName.equals(name),
                "Name must stay the same in the beginning and end");
        String endOfInput = tokens.dequeue();
        Reporter.assertElseFatalError(endOfInput.equals("### END OF INPUT ###"),
                "Expected: ### END OF INPUT ###, Found: " + endOfInput);

        this.setName(name);
        this.swapContext(context);
        this.swapBody(programBody);
    }

    /*
     * Main test method -------------------------------------------------------
     */

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Enter valid BL program file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Program p = new Program1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        p.parse(tokens);
        /*
         * Pretty print the program
         */
        out.println("*** Pretty print of parsed program ***");
        p.prettyPrint(out);

        in.close();
        out.close();
    }

}
