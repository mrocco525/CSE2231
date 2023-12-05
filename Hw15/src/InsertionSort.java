import java.util.Comparator;

import components.queue.Queue;
import components.queue.Queue1L;

/**
 * Put a short phrase describing the program here.
 *
 * @author Mason Rocco
 *
 */
public final class InsertionSort {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private InsertionSort() {
    }

    /**
     * Inserts the given {@code T} in the {@code Queue<T>} sorted according to
     * the given {@code Comparator<T>} and maintains the {@code Queue<T>}
     * sorted.
     *
     * @param <T>
     *            type of {@code Queue} entries
     * @param q
     *            the {@code Queue} to insert into
     * @param x
     *            the {@code T} to insert
     * @param order
     *            the {@code Comparator} defining the order for {@code T}
     * @updates q
     * @requires <pre>
     * IS_TOTAL_PREORDER([relation computed by order.compare method])  and
     * IS_SORTED(q, [relation computed by order.compare method])
     * </pre>
     * @ensures <pre>
     * perms(q, #q * <x>)  and
     * IS_SORTED(q, [relation computed by order.compare method])
     * </pre>
     */
    private static <T> void insertInOrder(Queue<T> q, T x,
            Comparator<T> order) {
        Queue<T> q2 = q.newInstance();
        int count = 0;
        int length = q.length();
        boolean isAdded = false;
        while (count < length && !isAdded) {
            T y = q.dequeue();
            if (order.compare(x, y) <= 0) {
                q2.enqueue(x);
                q2.enqueue(y);
                isAdded = true;
            } else {
                q2.enqueue(y);
            }
            count++;
        }
        q2.append(q);
        if (q2.length() == length) {
            q2.enqueue(x);
        }
        q.transferFrom(q2);
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
        Queue<T> temporaryQueue = new Queue1L<>();
        while (this.length() > 0) {
            T temp = this.dequeue();
            insertInOrder(temporaryQueue, temp, order);
        }
        this.transferFrom(temporaryQueue);
    }
}
