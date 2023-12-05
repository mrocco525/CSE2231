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
     * Invokes the appropriate {@code Set} constructor and returns the result.
     *
     * @return the new set
     * @ensures constructorTest = {}
     */
    protected abstract Set<String> constructorTest();

    /**
     * Invokes the appropriate {@code Set} constructor and returns the result.
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

    @Test
    public final void testAdd() {
        Set<String> s = this.createFromArgsTest("red", "green");
        Set<String> sExpected = this.createFromArgsRef("red", "green", "blue");
        s.add("blue");
        assertEquals(s, sExpected);
    }

    @Test
    public final void testRemove() {
        Set<String> s = this.createFromArgsTest("red", "green", "blue");
        Set<String> sExpected = this.createFromArgsRef("red", "green");
        s.remove("blue");
        assertEquals(s, sExpected);
    }

    /**
     * Tests removeAny() with many Strings in the set.
     */
    @Test
    public void testRemoveAnyWithMany() {
        Set<String> set1 = this.createFromArgsTest("a", "b", "c");
        Set<String> anyExpected = this.createFromArgsRef("a", "b", "c");
        String any = set1.removeAny();
        String anyActual = "";
        if (anyExpected.contains(any)) {
            anyActual = anyExpected.remove(any);
        }
        assertEquals(set1, anyExpected);
        assertEquals(any, anyActual);
    }

    @Test
    public final void testContains() {
        Set<String> s = this.createFromArgsTest("red", "green", "blue");
        boolean sExpected = true;
        assertEquals(s.contains("red"), sExpected);
    }

    @Test
    public final void testSize() {
        Set<String> s = this.createFromArgsTest("red", "green", "blue");
        int sExpected = 3;
        assertEquals(s.size(), sExpected);
    }

}
