import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.tree.Tree;

/**
 * Put a short phrase describing the program here.
 *
 * @author Put your name here
 *
 */
public final class size {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private size() {
    }

    /**
     * Returns the size of the given {@code Tree<T>}.
     *
     * @param <T>
     *            the type of the {@code Tree} node labels
     * @param t
     *            the {@code Tree} whose size to return
     * @return the size of the given {@code Tree}
     * @ensures size = |t|
     */
    public static <T> int size(Tree<T> t) {
        int treeSize = 1;
        Sequence<Tree<T>> children = new Sequence1L<Tree<T>>();
        if (t.numberOfSubtrees() > 0) {
            t.disassemble(children);
            for (int i = 0; i < children.length(); i++) {
                treeSize = treeSize + size(children.entry(i));
            }
        }
        return treeSize;
    }

}
