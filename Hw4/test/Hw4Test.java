import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Sample JUnit test fixture for Hw4.
 *
 * @author Mason Rocco
 *
 */
public final class Hw4Test {

    /**
     * Test average with j = 6 and k = 8.
     */
    @Test
    public void test1() {
        /*
         * Set up variables and call method under test
         */
        int j = 6;
        int k = 8;
        int average = Hw4.average(j, k);
        int expectedAverage = 7;
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(average, expectedAverage);
    }

    /**
     * Test average with j = 15 and k = 8.
     */
    @Test
    public void test2() {
        /*
         * Set up variables and call method under test
         */
        int j = 15;
        int k = 8;
        int average = Hw4.average(j, k);
        int expectedAverage = 11;
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(average, expectedAverage);
    }

    /**
     * Test average with j = 2147483647 and k = 2147483647.
     */
    @Test
    public void test3() {
        /*
         * Set up variables and call method under test
         */
        int j = 2147483647;
        int k = 2147483647;
        int average = Hw4.average(j, k);
        int expectedAverage = 2147483647;
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(average, expectedAverage);
    }

    /**
     * Test average with j = 2147483647 and k = -2147483647.
     */
    @Test
    public void test4() {
        /*
         * Set up variables and call method under test
         */
        int j = 2147483647;
        int k = -2147483647;
        int average = Hw4.average(j, k);
        int expectedAverage = 0;
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(average, expectedAverage);
    }

    /**
     * Test average with j = 2147483647 and k = 2147483646.
     */
    @Test
    public void test5() {
        /*
         * Set up variables and call method under test
         */
        int j = 2147483647;
        int k = 2147483646;
        int average = Hw4.average(j, k);
        int expectedAverage = 2147483646;
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(average, expectedAverage);
    }

}
