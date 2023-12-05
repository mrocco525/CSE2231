import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;

/**
 * JUnit test fixture for {@code Set<String>}'s constructor and kernel methods.
 *
 * @author Mason Rocco
 * @param <T>
 *
 */
public abstract class SetTest<T> {

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

    @Test
    public final void testRemoveAny() {
        Set<String> s = this.createFromArgsTest("red", "green", "blue");
        Set<String> sExpected = this.createFromArgsRef("red", "green", "blue");
        String x = s.removeAny();
        assertEquals(true, s.contains(x));
        sExpected.remove(x);
        assertEquals(s, sExpected);
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
