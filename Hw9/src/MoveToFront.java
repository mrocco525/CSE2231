import components.map.Map;
import components.map.Map.Pair;
import components.queue.Queue;

/**
 * Program containing moveToFront method.
 *
 * @author Mason Rocco
 *
 */
public final class MoveToFront {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private MoveToFront() {
    }

    /**
     * Finds pair with first component {@code key} and, if such exists, moves it
     * to the front of {@code q}.
     *
     * @param <K>
     *            type of {@code Pair} key
     * @param <V>
     *            type of {@code Pair} value
     * @param q
     *            the {@code Queue} to be searched
     * @param key
     *            the key to be searched for
     * @updates q
     * @ensures <pre>
     * perms(q, #q)  and
     * if there exists value: V (<(key, value)> is substring of q)
     *  then there exists value: V (<(key, value)> is prefix of q)
     * </pre>
     */
    private static <K, V> void moveToFront(Queue<Pair<K, V>> q, K key) {
        for (int i = 0; i < q.length(); i++) {
            if (q.front() != key) {
                Map.Pair<K, V> y = q.dequeue();
                q.enqueue(y);
            }
        }

    }
}
