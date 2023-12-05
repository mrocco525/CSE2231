import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.tree.Tree;

/**
 * Put a short phrase describing the program here.
 *
 * @author Put your name here
 *
 */
public final class height {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private height() {
    }

    /**
     * Returns the height of the given {@code Tree<T>}.
     *
     * @param <T>
     *            the type of the {@code Tree} node labels
     * @param t
     *            the {@code Tree} whose height to return
     * @return the height of the given {@code Tree}
     * @ensures height = ht(t)
     */
    public static <T> int height(Tree<T> t) {
        int treeHeight = 1;
        int childMax = 0;
        Sequence<Tree<T>> children = new Sequence1L<Tree<T>>();
        if (t.numberOfSubtrees() > 0) {
            t.disassemble(children);
            for (int i = 0; i < t.numberOfSubtrees(); i++) {
                if (height(children.entry(i)) > childMax) {
                    childMax = height(children.entry(i));
                }
            }
        }
        return treeHeight + childMax;
    }

}
