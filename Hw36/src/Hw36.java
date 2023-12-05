import components.map.Map;

/**
 *
 * @author Mason Rocco
 *
 */
public final class Hw36 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Hw36() {
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
     * @requires [the salaries in map are positive] and raisePercent > 0
     * @ensures <pre>
     * DOMAIN(map) = DOMAIN(#map)  and
     * [the salaries of the employees in map whose names start with the given
     *  initial have been increased by raisePercent percent (and truncated to
     *  the nearest integer); all other employees have the same salary]
     * </pre>
     */
    private static void giveRaise(components.map.Map<String, Integer> map,
            char initial, int raisePercent) {
        for (int i = 0; i < map.size(); i++) {
            Map.Pair<String, Integer> pair = map.removeAny();
            String x = pair.key();
            Integer wage = pair.value();
            if (x.charAt(0) == initial) {
                map.replaceValue(x, wage * (1 + (raisePercent / 100)));
            }
        }
    }
}
