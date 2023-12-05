import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.stack.Stack;
import components.stack.Stack1L;

/**
 * Homework #7 assignment to move entries between two stacks.
 *
 * @author Mason Rocco
 */
public final class StackChanger {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private StackChanger() {
        // no code needed here
    }

    /**
     * Shifts entries between {@code leftStack} and {@code rightStack}, keeping
     * reverse of the former concatenated with the latter fixed, and resulting
     * in length of the former equal to {@code newLeftLength}.
     *
     * @param <T>
     *            type of {@code Stack} entries
     * @param leftStack
     *            the left {@code Stack}
     * @param rightStack
     *            the right {@code Stack}
     * @param newLeftLength
     *            desired new length of {@code leftStack}
     * @updates leftStack, rightStack
     * @requires <pre>
     * 0 <= newLeftLength  and
     * newLeftLength <= |leftStack| + |rightStack|
     * </pre>
     * @ensures <pre>
     * rev(leftStack) * rightStack = rev(#leftStack) * #rightStack  and
     * |leftStack| = newLeftLength}
     * </pre>
     */
    public static <T> void setLengthOfLeftStack(Stack<T> leftStack,
            Stack<T> rightStack, int newLeftLength) {
        while (leftStack.length() != newLeftLength) {
            if (leftStack.length() > newLeftLength) {
                T temp = leftStack.pop();
                rightStack.push(temp);
            } else {
                T temp = rightStack.pop();
                leftStack.push(temp);
            }
        }
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
        out.println("Enter the length of the stack: ");
        int stackLength = in.nextInteger();
        Stack<Integer> leftStack = new Stack1L<>();
        Stack<Integer> rightStack = new Stack1L<>();
        for (int i = 1; i <= 4; i++) {
            leftStack.push(i);
        }
        for (int i = 5; i <= 8; i++) {
            rightStack.push(i);
        }
        leftStack.flip();
        out.print(leftStack);
        out.println(rightStack);
        leftStack.flip();
        setLengthOfLeftStack(leftStack, rightStack, stackLength);
        leftStack.flip();
        out.print(leftStack);
        out.print(rightStack);

        in.close();
        out.close();
    }

}
