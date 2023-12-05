import components.tree.Tree;

/**
 * Put a short phrase describing the program here.
 *
 * @author Put your name here
 *
 */
public final class size2 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private size2() {
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
        for (int i = 0; i < t.numberOfSubtrees(); i++) {
            if (t.numberOfSubtrees() > 0) {
                treeSize = treeSize + t.numberOfSubtrees();
            }
        }
        return treeSize;
    }

}
