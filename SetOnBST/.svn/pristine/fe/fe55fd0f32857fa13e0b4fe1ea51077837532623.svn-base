import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;

/**
 * JUnit test fixture for {@code Set<String>}'s constructor and kernel methods.
 *
 * @author Put your name here
 *
 */
public abstract class SetTest {

    /**
     * Invokes the appropriate {@code Set} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new set
     * @ensures constructorTest = {}
     */
    protected abstract Set<String> constructorTest();

    /**
     * Invokes the appropriate {@code Set} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new set
     * @ensures constructorRef = {}
     */
    protected abstract Set<String> constructorRef();

    /**
     * Creates and returns a {@code Set<String>} of the implementation under
     * test type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsTest = [entries in args]
     */
    private Set<String> createFromArgsTest(String... args) {
        Set<String> set = this.constructorTest();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Creates and returns a {@code Set<String>} of the reference implementation
     * type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsRef = [entries in args]
     */
    private Set<String> createFromArgsRef(String... args) {
        Set<String> set = this.constructorRef();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Tests the default constructor.
     */
    @Test
    public final void testSetDefaultConstructor() {
        Set<String> s = this.constructorTest();
        Set<String> sExpected = this.constructorRef();
        assertEquals(sExpected, s);
    }

    /**
     * Tests add when no entries are present.
     */
    @Test
    public final void testAddToNone() {
        Set<String> s = this.createFromArgsTest();
        Set<String> sExpected = this.createFromArgsTest("red");
        s.add("red");
        assertEquals(s, sExpected);
    }

    /**
     * Tests add when one entry is present.
     */
    @Test
    public final void testAddToOne() {
        Set<String> s = this.createFromArgsTest("red");
        Set<String> sExpected = this.createFromArgsTest("red", "green");
        s.add("green");
        assertEquals(s, sExpected);
    }

    /**
     * Tests add when three entries are present.
     */
    @Test
    public final void testAddToSome() {
        Set<String> s = this.createFromArgsTest("red", "green", "yellow");
        Set<String> sExpected = this.createFromArgsTest("red", "green",
                "yellow", "blue");
        s.add("blue");
        assertEquals(s, sExpected);
    }

    /**
     * Tests remove when one entry is present.
     */
    @Test
    public final void testRemoveLastEntry() {
        Set<String> s = this.createFromArgsTest("red");
        Set<String> sExpected = this.createFromArgsTest();
        String removed = s.remove("red");
        String removeExpected = "red";
        assertEquals(removed, removeExpected);
        assertEquals(sExpected, s);
    }

    /**
     * Tests remove when three entries are present.
     */
    @Test
    public final void testRemoveSome() {
        Set<String> s = this.createFromArgsTest("red", "green", "yellow");
        Set<String> sExpected = this.createFromArgsTest("green", "yellow");
        String removed = s.remove("red");
        String removeExpected = "red";
        assertEquals(s, sExpected);
        assertEquals(removed, removeExpected);
    }

    /**
     * Tests removeAny with only one entry.
     */
    @Test
    public final void testRemoveAnyWithOne() {
        Set<String> s = this.createFromArgsTest("red");
        Set<String> sExpected = this.createFromArgsRef();
        String removeAnyExpected = "red";
        String removeAny = s.removeAny();

        assertEquals(sExpected, s);
        assertEquals(removeAnyExpected, removeAny);
    }

    /**
     * Tests removeAny with multiple entries.
     */
    @Test
    public final void testRemoveAnyWithSome() {
        Set<String> s = this.createFromArgsTest("green", "red", "yellow");
        Set<String> sExpected = this.createFromArgsRef("green", "red",
                "yellow");
        String removeAny = s.removeAny();
        assertEquals(true, sExpected.contains(removeAny));
        sExpected.remove(removeAny);
        assertEquals(sExpected, s);
    }

    /**
     * Tests contains with only one entry (true).
     */
    @Test
    public final void testContainsWithOneTrue() {
        Set<String> s = this.createFromArgsTest("green");
        assertEquals(s.contains("green"), true);
    }

    /**
     * Tests contains with only one entry (false).
     */
    @Test
    public final void testContainsWithOneFalse() {
        Set<String> s = this.createFromArgsTest("green");
        assertEquals(s.contains("red"), false);
    }

    /**
     * Tests contains with three entries (true).
     */
    @Test
    public final void testContainsWithSomeTrue() {
        Set<String> s = this.createFromArgsTest("green", "red", "yellow");
        assertEquals(s.contains("red"), true);
    }

    /**
     * Tests contains with three entries (false).
     */
    @Test
    public final void testContainsWithSomeFalse() {
        Set<String> s = this.createFromArgsTest("green", "red", "yellow");
        assertEquals(s.contains("pink"), false);
    }

    /**
     * Tests size with no entries.
     */
    @Test
    public final void testSizeWithZero() {
        Set<String> s = this.createFromArgsTest();
        assertEquals(s.size(), 0);
    }

    /**
     * Tests size with one entry.
     */
    @Test
    public final void testSizeWithOne() {
        Set<String> s = this.createFromArgsTest("green");
        assertEquals(s.size(), 1);
    }

    /**
     * Test size with three entries.
     */
    @Test
    public final void testSizeWithSome() {
        Set<String> s = this.createFromArgsTest("green", "red", "yellow");
        assertEquals(s.size(), 3);
    }
}
