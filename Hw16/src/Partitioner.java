import java.util.Comparator;

import components.queue.Queue;

/**
 * Put a short phrase describing the program here.
 *
 * @author Mason Rocco
 *
 */
public final class Partitioner {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Partitioner() {
    }

    /**
     * Partitions {@code q} into two parts: entries no larger than
     * {@code partitioner} are put in {@code front}, and the rest are put in
     * {@code back}.
     *
     * @param <T>
     *            type of {@code Queue} entries
     * @param q
     *            the {@code Queue} to be partitioned
     * @param partitioner
     *            the partitioning value
     * @param front
     *            upon return, the entries no larger than {@code partitioner}
     * @param back
     *            upon return, the entries larger than {@code partitioner}
     * @param order
     *            ordering by which to separate entries
     * @clears q
     * @replaces front, back
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * perms(#q, front * back)  and
     * for all x: T where (<x> is substring of front)
     *  ([relation computed by order.compare method](x, partitioner))  and
     * for all x: T where (<x> is substring of back)
     *  (not [relation computed by order.compare method](x, partitioner))
     * </pre>
     */
    private static <T> void partition(Queue<T> q, T partitioner, Queue<T> front,
            Queue<T> back, Comparator<T> order) {
        for (int i = 0; i < q.length(); i++) {
            T first = q.dequeue();
            if (order.compare(first, partitioner) <= 0) {
                front.enqueue(first);
            } else {
                back.enqueue(first);
            }
        }
    }

    /**
     * Sorts {@code this} according to the ordering provided by the
     * {@code compare} method from {@code order}.
     *
     * @param order
     *            ordering by which to sort
     * @updates this
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * perms(this, #this)  and
     * IS_SORTED(this, [relation computed by order.compare method])
     * </pre>
     */
    public void sort(Comparator<T> order) {
        if (this.length() > 1) {
            /*
             * Dequeue the partitioning entry from this
             */
            T partitioner = this.dequeue();
            /*
             * Partition this into two queues as discussed above (you will need
             * to declare and initialize two new queues)
             */
            Queue<T> left = this.newInstance();
            Queue<T> right = this.newInstance();
            partition(this, partitioner, left, right, order);
            /*
             * Recursively sort the two queues
             */
            left.sort();
            right.sort();
            /*
             * Reconstruct this by combining the two sorted queues and the
             * partitioning entry in the proper order
             */
            this.enqueue(right);
            this.enqueue(partitioner);
            this.enqueue(left);
        }
    }
}
