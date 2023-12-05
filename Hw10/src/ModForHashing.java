/**
 * Homework 10 assignment to create a mod method.
 *
 * @author Mason Rocco
 *
 */
public final class ModForHashing {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private ModForHashing() {
    }

    /**
     * Computes {@code a} mod {@code b} as % should have been defined to work.
     *
     * @param a
     *            the number being reduced
     * @param b
     *            the modulus
     * @return the result of a mod b, which satisfies 0 <= {@code mod} < b
     * @requires b > 0
     * @ensures <pre>
     * 0 <= mod  and  mod < b  and
     * there exists k: integer (a = k * b + mod)
     * </pre>
     */
    public static int mod(int a, int b) {
        int modulo;
        if (a < 0) {
            modulo = (((a % b) + b) % b);
        } else {
            modulo = a % b;
        }
        return modulo;
    }

}
