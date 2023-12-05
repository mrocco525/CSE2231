import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

import components.sortingmachine.SortingMachine;

/**
 * JUnit test fixture for {@code SortingMachine<String>}'s constructor and
 * kernel methods.
 *
 * @author Caden Sweeney, Mason Rocco
 *
 */
public abstract class SortingMachineTest {

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * implementation under test and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorTest = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorTest(
            Comparator<String> order);

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * reference implementation and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorRef = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorRef(
            Comparator<String> order);

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the
     * implementation under test type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsTest = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsTest(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorTest(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the reference
     * implementation type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsRef = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsRef(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorRef(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     * Comparator<String> implementation to be used in all test cases. Compare
     * {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }

    }

    /**
     * Comparator instance to be used in all test cases.
     */
    private static final StringLT ORDER = new StringLT();

    /*
     * Sample test cases.
     */

    /**
     * Tests the default constructor of the sortingMachine.
     */
    @Test
    public final void testConstructor() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.constructorRef(ORDER);
        assertEquals(mExpected, m);
    }

    /**
     * Tests changing the sortingMachine to extractionMode while empty.
     */
    @Test
    public final void testChangeToExtractionModeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    /**
     * Tests changing the sortingMachine to extractionMode while not empty.
     */
    @Test
    public final void testChangeToExtractionModeWithSome() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, false,
                "a", "b", "c");
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    /**
     * Tests adding an element to an empty sortingMachine.
     */
    @Test
    public final void testAddEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green");
        m.add("green");
        assertEquals(mExpected, m);
    }

    /**
     * Tests adding an element to a non-empty sortingMachine.
     */
    @Test
    public final void testAddToSome() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "b",
                "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true,
                "a", "b", "c");
        m.add("a");
        assertEquals(mExpected, m);
    }

    /**
     * Tests removeFirst with only one entry.
     */
    @Test
    public final void testRemoveFirstWithOne() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        String first = m.removeFirst();
        String firstExpected = "green";
        assertEquals(mExpected, m);
        assertEquals(firstExpected, first);
    }

    /**
     * Tests removeFirst with more than one entry.
     */
    @Test
    public final void testRemoveFirstWithSome() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, false,
                "b", "c");
        String takenOutExpected = "a";
        String takenOut = m.removeFirst();
        assertEquals(mExpected, m);
        assertEquals(takenOutExpected, takenOut);
    }

    /**
     * Tests whether the sortingMachine is in insertionMode (expecting true).
     */
    @Test
    public final void testIsInInsertionModeTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true,
                "a", "b", "c");
        boolean insertionModeExpected = true;
        boolean insertionMode = m.isInInsertionMode();
        assertEquals(mExpected, m);
        assertEquals(insertionModeExpected, insertionMode);
    }

    /**
     * Tests whether the sortingMachine is in insertionMode (expecting false).
     */
    @Test
    public final void testIsInInsertionModeFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, false,
                "a", "b", "c");
        boolean insertionModeExpected = false;
        boolean insertionMode = m.isInInsertionMode();
        assertEquals(mExpected, m);
        assertEquals(insertionModeExpected, insertionMode);
    }

    /**
     * Tests the order of the sortingMachine on true.
     */
    @Test
    public final void testSortingMachineOrderWithTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true,
                "a", "b", "c");
        Comparator<String> sortingOrderExpected = ORDER;
        Comparator<String> sortingOrder = m.order();
        assertEquals(mExpected, m);
        assertEquals(sortingOrderExpected, sortingOrder);
    }

    /**
     * Tests the order of the sortingMachine on false.
     */
    @Test
    public final void testSortingMachineOrderWithFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, false,
                "a", "b", "c");
        Comparator<String> sortingOrderExpected = ORDER;
        Comparator<String> sortingOrder = m.order();
        assertEquals(mExpected, m);
        assertEquals(sortingOrderExpected, sortingOrder);
    }

    /**
     * Tests the order of the sortingMachine on true expecting true.
     */
    @Test
    public final void testSortOrderWithTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true,
                "a", "b", "c");
        String a = "a";
        String b = "b";
        boolean expectedOrder = true;
        if (ORDER.compare(a, b) >= 0) {
            expectedOrder = false;
        }
        assertEquals(mExpected, m);
        assertEquals(expectedOrder, true);
    }

    /**
     * Tests the order of the sortingMachine on false expecting true.
     */
    @Test
    public final void testSortOrderWithFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, false,
                "a", "b", "c");
        String a = "a";
        String b = "b";
        boolean expectedOrder = true;
        if (ORDER.compare(a, b) >= 0) {
            expectedOrder = false;
        }
        assertEquals(mExpected, m);
        assertEquals(expectedOrder, true);
    }

    /**
     * Tests the order of the sortingMachine on true expecting true.
     */
    @Test
    public final void testEqualSortOrderWithTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true,
                "a", "b", "c");
        String a = "a";
        String b = "a";
        boolean expectedOrder = false;
        if (ORDER.compare(a, b) == 0) {
            expectedOrder = true;
        }
        assertEquals(mExpected, m);
        assertEquals(expectedOrder, true);
    }

    /**
     * Tests the order of the sortingMachine on false expecting true.
     */
    @Test
    public final void testEqualSortOrderWithFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, false,
                "a", "b", "c");
        String a = "a";
        String b = "a";
        boolean expectedOrder = false;
        if (ORDER.compare(a, b) == 0) {
            expectedOrder = true;
        }
        assertEquals(mExpected, m);
        assertEquals(expectedOrder, true);
    }

    /**
     * Tests the size of the sortingMachine on true when empty.
     */
    @Test
    public final void testSizeEmptyOnTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true);
        int sizeExpected = 0;
        int size = m.size();
        assertEquals(mExpected, m);
        assertEquals(sizeExpected, size);
    }

    /**
     * Tests the size of the sortingMachine on true with one entry.
     */
    @Test
    public final void testSizeWithOneOnTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "a");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true,
                "a");
        int sizeExpected = 1;
        int size = m.size();
        assertEquals(mExpected, m);
        assertEquals(sizeExpected, size);
    }

    /**
     * Tests the size of the sortingMachine on true with multiple entries.
     */
    @Test
    public final void testSizeWithSomeOnTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true,
                "a", "b", "c");
        int sizeExpected = 3;
        int size = m.size();
        assertEquals(mExpected, m);
        assertEquals(sizeExpected, size);
    }

    /**
     * Tests the size of the sortingMachine on false when empty.
     */
    @Test
    public final void testSizeEmptyOnFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER,
                false);
        int sizeExpected = 0;
        int size = m.size();
        assertEquals(mExpected, m);
        assertEquals(sizeExpected, size);
    }

    /**
     * Tests the size of the sortingMachine on false with one entry.
     */
    @Test
    public final void testSizeWithOneOnFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "a");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, false,
                "a");
        int sizeExpected = 1;
        int size = m.size();
        assertEquals(mExpected, m);
        assertEquals(sizeExpected, size);
    }

    /**
     * Tests the size of the sortingMachine on false with multiple entries.
     */
    @Test
    public final void testSizeWithSomeOnFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "a",
                "b", "c");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, false,
                "a", "b", "c");
        int sizeExpected = 3;
        int size = m.size();
        assertEquals(mExpected, m);
        assertEquals(sizeExpected, size);
    }
}
