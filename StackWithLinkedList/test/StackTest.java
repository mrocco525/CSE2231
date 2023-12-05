import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.stack.Stack;

/**
 * JUnit test fixture for {@code Stack<String>}'s constructor and kernel
 * methods.
 *
 * @author Put your name here
 *
 */
public abstract class StackTest {

    /**
     * Invokes the appropriate {@code Stack} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new stack
     * @ensures constructorTest = <>
     */
    protected abstract Stack<String> constructorTest();

    /**
     * Invokes the appropriate {@code Stack} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new stack
     * @ensures constructorRef = <>
     */
    protected abstract Stack<String> constructorRef();

    /**
     *
     * Creates and returns a {@code Stack<String>} of the implementation under
     * test type with the given entries.
     *
     * @param args
     *            the entries for the stack
     * @return the constructed stack
     * @ensures createFromArgsTest = [entries in args]
     */
    private Stack<String> createFromArgsTest(String... args) {
        Stack<String> stack = this.constructorTest();
        for (String s : args) {
            stack.push(s);
        }
        stack.flip();
        return stack;
    }

    /**
     *
     * Creates and returns a {@code Stack<String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the entries for the stack
     * @return the constructed stack
     * @ensures createFromArgsRef = [entries in args]
     */
    private Stack<String> createFromArgsRef(String... args) {
        Stack<String> stack = this.constructorRef();
        for (String s : args) {
            stack.push(s);
        }
        stack.flip();
        return stack;
    }

    @Test
    public void testPopWithOne() {
        Stack<String> s = this.createFromArgsRef("red");
        Stack<String> sExpected = this.createFromArgsRef();
        String popped = s.pop();
        String poppedExpected = "red";
        assertEquals(popped, poppedExpected);
        assertEquals(s, sExpected);
    }

    @Test
    public void testPopWithSome() {
        Stack<String> s = this.createFromArgsRef("red", "blue", "green");
        Stack<String> sExpected = this.createFromArgsRef("blue", "green");
        String popped = s.pop();
        String poppedExpected = "red";
        assertEquals(popped, poppedExpected);
        assertEquals(s, sExpected);
    }

    @Test
    public void testPushWithSome() {
        Stack<String> s = this.createFromArgsRef("red", "blue");
        Stack<String> sExpected = this.createFromArgsRef("green", "red",
                "blue");
        s.push("green");
        assertEquals(s, sExpected);
    }

    @Test
    public void testPushWithNone() {
        Stack<String> s = this.createFromArgsRef();
        Stack<String> sExpected = this.createFromArgsRef("green");
        s.push("green");
        assertEquals(s, sExpected);
    }

    public void testLengthEmpty() {
        Stack<String> s = this.createFromArgsRef();
        assertEquals(s.length(), 0);
    }

    public void testLengthWithSome() {
        Stack<String> s = this.createFromArgsRef("red", "blue");
        assertEquals(s.length(), 2);
    }

    public void testConstructor() {
        Stack<String> s = this.createFromArgsRef("red", "blue");
        Stack<String> sExpected = this.createFromArgsTest("red", "blue");
        assertEquals(s, sExpected);
    }

}
