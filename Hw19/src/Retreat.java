/**
 * Retreats the position in {@code this} by one.
 *
 * @updates this
 * @requires this.left /= <>
 * @ensures <pre>
 * this.left * this.right = #this.left * #this.right  and
 * |this.left| = |#this.left| - 1
 * </pre>
 */
public void retreat() {
    this.moveToStart();
    for(int i = 0; i < this.leftLength() - 1; i++) {
        this.advance();
    }
}