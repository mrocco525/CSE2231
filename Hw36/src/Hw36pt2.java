import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Mason Rocco
 *
 */
public final class Hw36pt2 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Hw36pt2() {
    }

    /**
     * Raises the salary of all the employees in {@code map} whose name starts
     * with the given {@code initial} by the given {@code raisePercent}.
     *
     * @param map
     *            the name to salary map
     * @param initial
     *            the initial of names of employees to be given a raise
     * @param raisePercent
     *            the raise to be given as a percentage of the current salary
     * @updates map
     * @requires <pre>
     * [the salaries in map are positive]  and  raisePercent > 0  and
     * [the dynamic types of map and of all objects reachable from map
     *  (including any objects returned by operations (such as entrySet() and,
     *  from there, iterator()), and so on, recursively) support all
     *  optional operations]
     * </pre>
     * @ensures <pre>
     * DOMAIN(map) = DOMAIN(#map)  and
     * [the salaries of the employees in map whose names start with the given
     *  initial have been increased by raisePercent percent (and truncated to
     *  the nearest integer); all other employees have the same salary]
     * </pre>
     */
    private static void giveRaise(java.util.Map<String, Integer> map,
            char initial, int raisePercent) {
        final int percent = 100;
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Integer> name = iterator.next();
            if (name.toString().charAt(0) == initial) {
                int wage = name.getValue();
                name.setValue(wage * (1 + (raisePercent / percent)));
            }
        }
    }
}
