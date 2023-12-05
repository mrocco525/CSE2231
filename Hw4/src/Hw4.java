/**
 * A program to take the average of two integers.
 *
 * @author Mason Rocco
 *
 */
public final class Hw4 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Hw4() {
    }

    /**
     * Returns the integer average of two given {@code int}s.
     *
     * @param j
     *            the first of two integers to average
     * @param k
     *            the second of two integers to average
     * @return the integer average of j and k
     * @ensures average = (j+k)/2
     */
    public static int average(int j, int k) {
        int num1 = j / 2;
        int num2 = k / 2;
        int remainder = ((j % 2) + (k % 2)) / 2;
        int avg = (num1 + num2) + remainder;

        return avg;
    }
}
