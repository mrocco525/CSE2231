import components.queue.Queue;

/**
 * Program to move a certain entry to the front of a queue if the entry is in
 * the queue.
 *
 * @author Mason Rocco
 */
public final class MoveToFront {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private MoveToFront() {
        // no code needed here
    }

    /**
     * Finds {@code x} in {@code q} and, if such exists, moves it to the front
     * of {@code q}.
     *
     * @param <T>
     *            type of {@code Queue} entries
     * @param q
     *            the {@code Queue} to be searched
     * @param x
     *            the entry to be searched for
     * @updates q
     * @ensures <pre>
     * perms(q, #q)  and
     * if <x> is substring of q
     *  then <x> is prefix of q
     * </pre>
     */
    private static <T> void moveToFront(Queue<T> q, T x) {
        for (int i = 0; i < q.length(); i++) {
            if (q.front() != x) {
                T y = q.dequeue();
                q.enqueue(y);
            }
        }
    }
}
