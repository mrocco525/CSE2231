import components.sequence.Sequence;
import components.sequence.Sequence1L;

/**
 * Implements method to smooth a {@code Sequence<Integer>}.
 *
 * @author Put your name here
 *
 */
public final class SequenceSmooth {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private SequenceSmooth() {
    }

    /**
     * Smooths a given {@code Sequence<Integer>}.
     *
     * @param s1
     *            the sequence to smooth
     * @return s2, the resulting sequence
     * @requires |s1| >= 1
     * @ensures <pre>
     * |s2| = |s1| - 1  and
     *  for all i, j: integer, a, b: string of integer
     *      where (s1 = a * <i> * <j> * b)
     *    (there exists c, d: string of integer
     *       (|c| = |a|  and
     *        s2 = c * <(i+j)/2> * d))
     * </pre>
     */
    public static Sequence<Integer> smooth(Sequence<Integer> s1) {
        assert s1 != null : "Violation of: s1 is not null";
        assert s1.length() >= 1 : "Violation of: |s1| >= 1";
        Sequence<Integer> s2 = new Sequence1L<>();

        /*
         * iterative method
         *
         * for (int i = 0; i < s1.length() - 1; i++) {
         *
         * int remainder = 0; int num1 = s1.entry(i); int num2 = s1.entry(i +
         * 1); if (num1 % 2 != 0 && num2 % 2 != 0) { remainder = remainder + 1;
         * } num1 = num1 / 2; num2 = num2 / 2; int mean = (num1 + num2) +
         * remainder; s2.add(i, mean); }
         *
         *
         */
        if (s1.length() >= 1) {
            int remainder = 0;
            int num1 = s1.remove(0);
            int num2 = s1.remove(1);
            if (num1 % 2 != 0 && num2 % 2 != 0) {
                remainder = remainder + 1;
            }
            num1 = num1 / 2;
            num2 = num2 / 2;
            int mean = num1 + num2;
            s2.add(mean, 1);
            s2 = smooth(s2);
        }
        return s2;
    }
}
