import components.binarytree.BinaryTree;

/**
 * Put a short phrase describing the program here.
 *
 * @author Mason Rocco
 *
 */
public final class Heapsort {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Heapsort() {
    }

    /**
     * Checks if the given {@code BinaryTree<Integer>} satisfies the heap
     * ordering property according to the <= relation.
     *
     * @param t
     *            the binary tree
     * @return true if the given tree satisfies the heap ordering property;
     *         false otherwise
     * @ensures <pre>
     * satisfiesHeapOrdering = [t satisfies the heap ordering property]
     * </pre>
     */
    private static boolean satisfiesHeapOrdering(BinaryTree<Integer> t) {
        boolean isOrderedLeft = false;
        boolean isOrderedRight = false;
        BinaryTree<Integer> left = t.newInstance();
        BinaryTree<Integer> right = t.newInstance();
        int root = t.disassemble(left, right);
        if (root <= left.root() && root <= right.root()) {
            isOrderedLeft = satisfiesHeapOrdering(left);
            isOrderedRight = satisfiesHeapOrdering(right);
        }
        return (isOrderedLeft && isOrderedRight);
    }

}