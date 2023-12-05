import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.tree.Tree;

/**
 * Put a short phrase describing the program here.
 *
 * @author Put your name here
 *
 */
public final class max {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private max() {
    }

    /**
     * Returns the largest integer in the given {@code Tree<Integer>}.
     *
     * @param t
     *            the {@code Tree<Integer>} whose largest integer to return
     * @return the largest integer in the given {@code Tree<Integer>}
     * @requires |t| > 0
     * @ensures <pre>
     * max is in labels(t)  and
     * for all i: integer where (i is in labels(t)) (i <= max)
     * </pre>
     */
    public static int max(Tree<Integer> t) {
        int childMax = 0;
        Sequence<Tree<Integer>> children = new Sequence1L<Tree<Integer>>();
        if (t.numberOfSubtrees() > 0) {
            t.disassemble(children);
            for (int i = 0; i < t.numberOfSubtrees(); i++) {
                if (children.entry(i).height() > childMax) {
                    childMax = children.entry(i).height();
                }
            }
        }
        return childMax;
    }

}
