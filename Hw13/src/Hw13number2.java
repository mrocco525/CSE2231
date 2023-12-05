import components.binarytree.BinaryTree;

/**
 * An iterative program to report the size of a given BinaryTree.
 *
 * @author Mason Rocco
 *
 */
public final class Hw13number2 {
    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Hw13number2() {
    }

    /**
     * Returns a copy of the the given {@code BinaryTree}.
     *
     * @param t
     *            the {@code BinaryTree} to copy
     * @return a copy of the given {@code BinaryTree}
     * @ensures copy = t
     */
    public static BinaryTree<Integer> copy(BinaryTree<Integer> t) {
        BinaryTree<Integer> tCopy = t.newInstance();
        BinaryTree<Integer> left = t.newInstance();
        BinaryTree<Integer> right = t.newInstance();
        int root = t.disassemble(left, right);
        tCopy.assemble(root, copy(left), copy(right));
        t.assemble(root, left, right);
        return tCopy;
    }
}
