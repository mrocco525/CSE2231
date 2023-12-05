import static org.junit.Assert.assertEquals;

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

    /*
    *
    */
    @Test
    public void testAddToNone() {
        Map<String, String> map = this.createFromArgsTest();
        Map<String, String> mapExpected = this.createFromArgsRef("one", "1");
        map.add("one", "1");
        assertEquals(map, mapExpected);
    }

    /*
    *
    */
    @Test
    public void testAddToSome() {
        Map<String, String> map = this.createFromArgsTest("one", "1", "two",
                "2");
        Map<String, String> mapExpected = this.createFromArgsRef("one", "1",
                "two", "2", "three", "3");
        map.add("three", "3");
        assertEquals(map, mapExpected);
    }

    /*
    *
    */
    @Test
    public void testRemoveLast() {
        Map<String, String> map = this.createFromArgsTest("one", "1");
        Map<String, String> mapExpected = this.createFromArgsRef();
        map.remove("one");
        assertEquals(map, mapExpected);
    }

    /*
    *
    */
    @Test
    public void testRemoveFromSome() {
        Map<String, String> map = this.createFromArgsTest("one", "1", "two",
                "2", "three", "3");
        Map<String, String> mapExpected = this.createFromArgsRef("one", "1",
                "two", "2");
        map.remove("three");
        assertEquals(map, mapExpected);
    }

    /*
    *
    */
    @Test
    public void testRemoveAnyLast() {
        Map<String, String> map = this.createFromArgsTest("one", "1");
        Map<String, String> mapExpected = this.createFromArgsRef();
        map.removeAny();
        assertEquals(map, mapExpected);
    }

    /*
    *
    */
    @Test
    public void testRemoveAnyFromSome() {
        Map<String, String> map = this.createFromArgsTest("one", "1", "two",
                "2");
        Map<String, String> mapExpected = this.createFromArgsRef("one", "1",
                "two", "2");
        Map.Pair<String, String> pair = map.removeAny();
        String keyActual = pair.key();
        mapExpected.remove(keyActual);
        assertEquals(map, mapExpected);
    }

    /*
    *
    */
    @Test
    public void testValue() {
        Map<String, String> map = this.createFromArgsTest("one", "1", "two",
                "2");
        Map<String, String> mapExpected = this.createFromArgsRef("two", "2");
        Map.Pair<String, String> pair = map.remove("one");
        String value = pair.value();
        String valueExpected = "1";
        assertEquals(value, valueExpected);
        assertEquals(map, mapExpected);
    }

    /*
     * Tests hasKey expecting true
     */
    public void testHasKeyTrue() {
        Map<String, String> map = this.createFromArgsTest("one", "1", "two",
                "2");
        Map<String, String> mapExpected = this.createFromArgsRef("one", "1",
                "two", "2");
        boolean hasKeyTrue = true;
        boolean kasKeyActual = map.hasKey("one");
        assertEquals(map, mapExpected);
        assertEquals(hasKeyTrue, kasKeyActual);
    }

    /*
     * Tests hasKey expecting false
     */
    public void testHasKeyFalse() {
        Map<String, String> map = this.createFromArgsTest("one", "1", "two",
                "2");
        Map<String, String> mapExpected = this.createFromArgsRef("one", "1",
                "two", "2");
        boolean hasKeyTrue = true;
        boolean kasKeyActual = map.hasKey("three");
        assertEquals(map, mapExpected);
        assertEquals(hasKeyTrue, kasKeyActual);
    }

    /*
    *
    */
    @Test
    public void testSizeZero() {
        Map<String, String> map = this.createFromArgsTest();
        Map<String, String> mapExpected = this.createFromArgsRef();
        int sizeExpected = 0;
        assertEquals(map, mapExpected);
        assertEquals(sizeExpected, map.size());
    }

    /*
    *
    */
    @Test
    public void testSizeNotZero() {
        Map<String, String> map = this.createFromArgsTest("one", "1", "two",
                "2");
        Map<String, String> mapExpected = this.createFromArgsRef("one", "1",
                "two", "2");
        int sizeExpected = 2;
        assertEquals(map, mapExpected);
        assertEquals(sizeExpected, map.size());
    }

}
