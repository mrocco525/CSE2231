package components.waitingLine;
import java.util.Comparator;

/**
 * {@code WaitingLineKernel} enhanced with secondary methods.
 *
 * @param <T>
 *            type of {@code WaitingLine} entries
 * @mathdefinitions <pre>
 * IS_TOTAL_PREORDER (
 *   r: binary relation on T
 *  ) : boolean is
 *  for all x, y, z: T
 *   ((r(x, y) or r(y, x))  and
 *    (if (r(x, y) and r(y, z)) then r(x, z)))
 *
 * IS_SORTED (
 *   s: string of T,
 *   r: binary relation on T
 *  ) : boolean is
 *  for all x, y: T where (<x, y> is substring of s) (r(x, y))
 * </pre>
 */
public interface WaitingLine<T> extends WaitingLineKernel<T> {

    /**
     * Reports the front of {@code this}.
     *
     * @return the front entry of {@code this}
     * @aliases reference returned by {@code front}
     * @requires this /= <>
     * @ensures <front> is prefix of this
     */
    T front();

    /**
     * Find the position of a given entry {@code entry}.
     *
     * @param entry
     *            a given entry
     * @aliases reference {@code entry}
     * @return the position of {@code entry} in {@code this}
     * @requires entry is in this
     * @ensures <pos> <= |this|
     */
    int pos(T entry);

    /**
     * Remove a given entry {@code x} known to be in {@code this}.
     *
     * @param x
     *            the entry to be removed
     * @aliases reference {@code x}
     * @updates this
     * @requires this /= <> and x is in this
     * @ensures #this = x * this
     */
    void removeEntry(T x);

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
    void sort(Comparator<T> order);

    /**
     * Concatenates ("appends") {@code q} to the end of {@code this}.
     *
     * @param q
     *            the {@code Queue} to be appended to the end of {@code this}
     * @updates this
     * @clears q
     * @ensures this = #this * #q
     */
    void append(WaitingLine<T> q);

}
