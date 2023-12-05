/**
 * Simple class representing a 7-digit phone number in the form "XXX-XXXX" for a
 * phone in the immediate OSU area.
 */
public class PhoneNumber {

    /**
     * The phone number representation.
     */
    private String rep;

    /**
     * Constructor. {@code pNum} must be in the form "XXX-XXXX" where each "X"
     * is a digit '0'-'9'.
     */
    public PhoneNumber(String pNum) {
        this.rep = pNum;
    }

    @Override
    public int hashCode() {
        char ch = this.rep.charAt(0);
        int radix = 10;
        return Character.digit(ch, radix);
    }

}
