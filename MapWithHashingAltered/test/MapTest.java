import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author Put your name here
 *
 */
public abstract class MapTest {

    /**
     * Invokes the appropriate {@code Map} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new map
     * @ensures constructorTest = {}
     */
    protected abstract Map<String, String> constructorTest();

    /**
     * Invokes the appropriate {@code Map} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new map
     * @ensures constructorRef = {}
     */
    protected abstract Map<String, String> constructorRef();

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsTest = [pairs in args]
     */
    private Map<String, String> createFromArgsTest(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorTest();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsRef = [pairs in args]
     */
    private Map<String, String> createFromArgsRef(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorRef();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     */
    @Test
    public final void testMapDefaultConstructor() {
        Map<String, String> m = this.constructorTest();
        Map<String, String> mExpected = this.constructorRef();
        assertEquals(mExpected, m);
    }

    /**
     * Tests adding to an empty map.
     */
    @Test
    public final void addToEmptyMap() {
        Map<String, String> m = this.createFromArgsTest();
        Map<String, String> mExpected = this.createFromArgsRef("one", "1");
        m.add("one", "1");
        assertEquals(mExpected, m);
    }

    /**
     * Tests adding to a non empty map.
     */
    @Test
    public final void addToNonEmptyMap() {
        Map<String, String> m = this.createFromArgsTest("one", "1");
        Map<String, String> mExpected = this.createFromArgsRef("one", "1",
                "two", "2");
        m.add("two", "2");
        assertEquals(mExpected, m);
    }

    /**
     * Testing removing the last element in a map.
     */
    @Test
    public final void removeToEmptyMap() {
        Map<String, String> m = this.createFromArgsTest("one", "1");
        Map<String, String> mExpected = this.createFromArgsRef();
        m.remove("one");
        assertEquals(mExpected, m);
    }

    /**
     * Testing removing an element from a map.
     */
    @Test
    public final void removeToNonEmptyMap() {
        Map<String, String> m = this.createFromArgsTest("one", "1", "two", "2",
                "three", "3");
        Map<String, String> mExpected = this.createFromArgsRef("one", "1",
                "two", "2");
        m.remove("three");
        assertEquals(mExpected, m);
    }

    /**
     * Testing .removeAny() with only one option.
     */
    @Test
    public final void removeAnyToEmptyMap() {
        Map<String, String> m = this.createFromArgsTest("one", "1");
        Map<String, String> mExpected = this.createFromArgsRef();
        m.removeAny();
        assertEquals(mExpected, m);
    }

    /**
     * Testing .removeAny() with many options.
     */
    @Test
    public final void removeAnyToNonEmptyMap() {
        Map<String, String> m = this.createFromArgsTest("one", "1", "two", "2",
                "three", "3");
        Map<String, String> mExpected1 = this.createFromArgsRef("one", "1",
                "two", "2");
        Map<String, String> mExpected2 = this.createFromArgsRef("one", "1",
                "three", "3");
        Map<String, String> mExpected3 = this.createFromArgsRef("two", "2",
                "three", "3");
        m.removeAny();
        assertTrue(mExpected1.equals(m) || (mExpected2.equals(m))
                || (mExpected3.equals(m)));
    }

    /**
     * Testing .value() for only one element.
     */
    @Test
    public final void testValueOfOneElementMap() {
        Map<String, String> m = this.createFromArgsTest("one", "1");
        Map<String, String> mExpected = this.createFromArgsRef("one", "1");
        assertEquals(mExpected.value("one"), m.value("one"));
    }

    /**
     * Testing .value() with many elements in map.
     */
    @Test
    public final void testValueOfManyElementsMap() {
        Map<String, String> m = this.createFromArgsTest("one", "1", "two", "2",
                "three", "3");
        Map<String, String> mExpected = this.createFromArgsRef("one", "1",
                "two", "2", "three", "3");
        assertEquals(mExpected.value("two"), m.value("two"));
    }

    /**
     * Testing .value() with duplicate values in a map.
     */
    @Test
    public final void testValueOfDuplicateElementsMap() {
        Map<String, String> m = this.createFromArgsTest("one", "1", "two", "1",
                "three", "3");
        Map<String, String> mExpected = this.createFromArgsRef("one", "1",
                "two", "1", "three", "3");
        assertEquals(mExpected.value("two"), m.value("two"));
        assertEquals(mExpected.value("one"), m.value("one"));
        assertEquals(mExpected.value("two"), m.value("one"));
        assertEquals(mExpected.value("one"), m.value("two"));
    }

    /**
     * Tests .hasKey() expecting true.
     */
    @Test
    public final void testHasKeyTrue() {
        Map<String, String> m = this.createFromArgsTest("one", "1");
        Map<String, String> mExpected = this.createFromArgsRef("one", "1");
        boolean hasKeyExpected = true;
        boolean hasKey = m.hasKey("one");
        assertEquals(mExpected, m);
        assertEquals(hasKeyExpected, hasKey);
    }

    /**
     * Tests .hasKey() expecting false.
     */
    @Test
    public final void testHasKeyFalse() {
        Map<String, String> m = this.createFromArgsTest("one", "1");
        Map<String, String> mExpected = this.createFromArgsRef("one", "1");
        boolean hasKeyExpected = false;
        boolean hasKey = m.hasKey("two");
        assertEquals(mExpected, m);
        assertEquals(hasKeyExpected, hasKey);
    }

    /**
     * Tests .hasKey() expecting false.
     */
    @Test
    public final void testHasKeyEmptyFalse() {
        Map<String, String> m = this.createFromArgsTest();
        Map<String, String> mExpected = this.createFromArgsRef();
        boolean hasKeyExpected = false;
        boolean hasKey = m.hasKey("two");
        assertEquals(mExpected, m);
        assertEquals(hasKeyExpected, hasKey);
    }

    /**
     * Testing size with no entries.
     */
    @Test
    public final void testSizeEmpty() {
        Map<String, String> m = this.createFromArgsTest();
        Map<String, String> mExpected = this.createFromArgsRef();
        int sizeExpected = 0;
        int size = m.size();
        assertEquals(mExpected, m);
        assertEquals(sizeExpected, size);
    }

    /**
     * Testing size with one entry.
     */
    @Test
    public final void testSizeOne() {
        Map<String, String> m = this.createFromArgsTest("one", "1");
        Map<String, String> mExpected = this.createFromArgsRef("one", "1");
        int sizeExpected = 1;
        int size = m.size();
        assertEquals(mExpected, m);
        assertEquals(sizeExpected, size);
    }

    /**
     * Testing size with two entries.
     */
    @Test
    public final void testSizetwo() {
        Map<String, String> m = this.createFromArgsTest("one", "1", "two", "2");
        Map<String, String> mExpected = this.createFromArgsRef("one", "1",
                "two", "2");
        int sizeExpected = 2;
        int size = m.size();
        assertEquals(mExpected, m);
        assertEquals(sizeExpected, size);
    }

    /**
     * Testing size with three entries.
     */
    @Test
    public final void testSizeThree() {
        Map<String, String> m = this.createFromArgsTest("one", "1", "two", "1",
                "three", "3");
        Map<String, String> mExpected = this.createFromArgsRef("one", "1",
                "two", "1", "three", "3");
        int sizeExpected = 3;
        int size = m.size();
        assertEquals(mExpected, m);
        assertEquals(sizeExpected, size);
    }
}
