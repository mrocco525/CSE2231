import components.binarytree.BinaryTree;

/**
 * A recursive program to report the size of a given BinaryTree.
 *
 * @author Mason Rocco
 *
 */
public final class Hw12 {
    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Hw12() {
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
        BinaryTree<T> a = null;
        BinaryTree<T> b = null;
        T root = t.disassemble(a, b);
        BinaryTree<T> left = a;
        BinaryTree<T> right = b;
        int c = 1 + size(left);
        int d = 1 + size(right);
        t.assemble(root, left, right);
        return c + d;
    }
}
