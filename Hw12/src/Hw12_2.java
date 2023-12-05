import components.binarytree.BinaryTree;

/**
 * An iterative program to report the size of a given BinaryTree.
 *
 * @author Mason Rocco
 *
 */
public final class Hw12_2 {
    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Hw12_2() {
    }

    /**
     * Returns the size of the given {@code BinaryTree<T>}.
     *
     * @param <T>
     *            the type of the {@code BinaryTree} node labels
     * @param t
     *            the {@code BinaryTree} whose size to return
     * @return the size of the given {@code BinaryTree}
     * @ensures size = |t|
     */
    public static <T> int size(BinaryTree<T> t) {
        int totalSize = 0;
        for (int i = 0; i < t.height(); i++) {
            BinaryTree<T> a = null;
            BinaryTree<T> b = null;
            T root = t.disassemble(a, b);
            BinaryTree<T> left = a;
            BinaryTree<T> right = b;
            t.assemble(root, left, right);
            totalSize++;
        }
        return totalSize;
    }
}
