import components.binarytree.BinaryTree;

/**
 * A program using Binary Search Trees to report whether a value is in the tree
 * or not.
 *
 * @author Mason Rocco
 *
 */
public final class IsInTree {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private IsInTree() {
    }

    /**
     * Returns whether {@code x} is in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be searched for
     * @return true if t contains x, false otherwise
     * @requires IS_BST(t)
     * @ensures isInTree = (x is in labels(t))
     */
    public static <T extends Comparable<T>> boolean isInTree(BinaryTree<T> t,
            T x) {
        boolean inTree = false;
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();

        if (x == t.root()) {
            inTree = true;
        } else if (x.compareTo(t.root()) < 0) {
            T root = t.disassemble(left, right);
            inTree = isInTree(left, x);
            t.assemble(root, left, right);
        } else {
            T root = t.disassemble(left, right);
            inTree = isInTree(right, x);
            t.assemble(root, left, right);
        }
        return inTree;
    }

}
