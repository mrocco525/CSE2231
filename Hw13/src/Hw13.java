import components.binarytree.BinaryTree;

/**
 * A recursive program to represent a given BinaryTree as a String.
 *
 * @author Mason Rocco
 *
 */
public final class Hw13 {
    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Hw13() {
    }

    /**
     * Returns the {@code String} prefix representation of the given
     * {@code BinaryTree<T>}.
     *
     * @param <T>
     *            the type of the {@code BinaryTree} node labels
     * @param t
     *            the {@code BinaryTree} to convert to a {@code String}
     * @return the prefix representation of {@code t}
     * @ensures treeToString = [the String prefix representation of t]
     */
    public static <T> String treeToString(BinaryTree<T> t) {
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();
        T root = t.disassemble(left, right);
        String x = "";
        x = root + "(" + treeToString(left) + treeToString(right) + ")";
        t.assemble(root, left, right);
        return x;
    }
}
