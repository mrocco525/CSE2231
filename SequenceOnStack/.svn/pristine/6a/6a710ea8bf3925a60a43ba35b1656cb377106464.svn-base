import static org.junit.Assert.assertEquals;

import components.sequence.Sequence;

/**
 * JUnit test fixture for {@code Sequence<String>}'s constructor and kernel
 * methods.
 *
 * @author Put your name here
 *
 */
public abstract class SequenceTest {

    /**
     * Invokes the appropriate {@code Sequence} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new sequence
     * @ensures constructorTest = <>
     */
    protected abstract Sequence<String> constructorTest();

    /**
     * Invokes the appropriate {@code Sequence} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new sequence
     * @ensures constructorRef = <>
     */
    protected abstract Sequence<String> constructorRef();

    /**
     *
     * Creates and returns a {@code Sequence<String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the entries for the sequence
     * @return the constructed sequence
     * @ensures createFromArgsTest = [entries in args]
     */
    private Sequence<String> createFromArgsTest(String... args) {
        Sequence<String> sequence = this.constructorTest();
        for (String s : args) {
            sequence.add(sequence.length(), s);
        }
        return sequence;
    }

    /**
     *
     * Creates and returns a {@code Sequence<String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the entries for the sequence
     * @return the constructed sequence
     * @ensures createFromArgsRef = [entries in args]
     */
    private Sequence<String> createFromArgsRef(String... args) {
        Sequence<String> sequence = this.constructorRef();
        for (String s : args) {
            sequence.add(sequence.length(), s);
        }
        return sequence;
    }

    // TODO - add test cases for constructor, add, remove, and length

    /**
     * Testing adding to front.
     */
    public final void testAddfront() {
        Sequence<String> seq1 = this.createFromArgsTest("b", "c");
        Sequence<String> seq1Expected = this.createFromArgsRef("a", "b", "c");
        seq1.add(0, "a");

        assertEquals(seq1, seq1Expected);
    }

    /**
     * Testing adding to the back.
     */
    public final void testAddBack() {
        Sequence<String> seq1 = this.createFromArgsTest("a", "b");
        Sequence<String> seq1Expected = this.createFromArgsRef("a", "b", "c");
        seq1.add(seq1.length() - 1, "c");
        assertEquals(seq1, seq1Expected);
    }

    /**
     * Testing adding to the middle.
     */
    public final void testAddMid() {
        Sequence<String> seq1 = this.createFromArgsTest("a", "c");
        Sequence<String> seq1Expected = this.createFromArgsRef("a", "b", "c");
        seq1.add(1, "b");
        assertEquals(seq1, seq1Expected);
    }

    /**
     * Testing adding to an empty sequence.
     */
    public final void testAddEmpty() {
        Sequence<String> seq1 = this.createFromArgsTest();
        Sequence<String> seq1Expected = this.createFromArgsRef("a");
        seq1.add(0, "a");
        assertEquals(seq1, seq1Expected);
    }

    /**
     * Testing removing the front string.
     */
    public final void testRemovefront() {
        Sequence<String> seq1 = this.createFromArgsTest("a", "b", "c");
        Sequence<String> seq1Expected = this.createFromArgsRef("b", "c");
        seq1.remove(0);

        assertEquals(seq1, seq1Expected);
    }

    /**
     * Testing removing the back string.
     */
    public final void testRemoveBack() {
        Sequence<String> seq1 = this.createFromArgsTest("a", "b", "c");
        Sequence<String> seq1Expected = this.createFromArgsRef("a", "b");
        seq1.remove(seq1.length() - 1);
        assertEquals(seq1, seq1Expected);
    }

    /**
     * Testing removing the middle string.
     */
    public final void testRemoveMid() {
        Sequence<String> seq1 = this.createFromArgsTest("a", "b", "c");
        Sequence<String> seq1Expected = this.createFromArgsRef("a", "c");
        seq1.remove(1);
        assertEquals(seq1, seq1Expected);
    }

    /**
     * Testing removing the last string in a sequence.
     */
    public final void testRemoveEmpty() {
        Sequence<String> seq1 = this.createFromArgsTest("a");
        Sequence<String> seq1Expected = this.createFromArgsRef();
        seq1.remove(0);
        assertEquals(seq1, seq1Expected);
    }

    /**
     * Testing the length of an empty sequence.
     */
    public final void testLengthEmpty() {
        Sequence<String> seq1 = this.createFromArgsTest();
        Sequence<String> seq1Expected = this.createFromArgsRef();
        int length = seq1.length();
        int lengthExpected = 0;
        assertEquals(seq1, seq1Expected);
        assertEquals(length, lengthExpected);
    }

    /**
     * Testing the length of a sequence with one string in it.
     */
    public final void testLengthOne() {
        Sequence<String> seq1 = this.createFromArgsTest("a");
        Sequence<String> seq1Expected = this.createFromArgsRef("a");
        int length = seq1.length();
        int lengthExpected = 1;
        assertEquals(seq1, seq1Expected);
        assertEquals(length, lengthExpected);
    }

    /**
     * Testing the length of a sequence with some strings in it.
     */
    public final void testLengthSome() {
        Sequence<String> seq1 = this.createFromArgsTest("a", "b", "c");
        Sequence<String> seq1Expected = this.createFromArgsRef("a", "b", "c");
        int length = seq1.length();
        int lengthExpected = 3;
        assertEquals(seq1, seq1Expected);
        assertEquals(length, lengthExpected);
    }

}
