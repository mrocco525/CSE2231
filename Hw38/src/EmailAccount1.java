import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;

/**
 * Implementation of {@code EmailAccount}.
 *
 * @author Mason Rocco
 *
 */
public final class EmailAccount1 implements EmailAccount {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * First name of client.
     */
    private String first;

    /**
     * Last name of client.
     */
    private String last;
    /**
     * List of names and their numbers.
     */
    private Map<String, Integer> numbers;

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructor.
     *
     * @param firstName
     *            the first name
     * @param lastName
     *            the last name
     */
    public EmailAccount1(String firstName, String lastName) {
        this.first = firstName;
        this.last = lastName;
        this.numbers = new Map1L<>();
    }

    /*
     * Methods ----------------------------------------------------------------
     */

    @Override
    public String name() {
        String title = this.first + " " + this.last;
        return title;
    }

    @Override
    public String emailAddress() {
        if (this.numbers.hasKey(this.last.toLowerCase())) {
            Pair<String, Integer> temp = this.numbers
                    .remove(this.last.toLowerCase());
            int increment = temp.value() + 1;
            this.numbers.add(this.last.toLowerCase(), increment);
        } else {
            this.numbers.add(this.last.toLowerCase(), 1);
        }
        int value = this.numbers.value(this.last.toLowerCase());
        String email = this.last.toLowerCase() + "." + value + "@osu.edu";
        return email;
    }

    @Override
    public String toString() {
        if (this.numbers.hasKey(this.last.toLowerCase())) {
            Pair<String, Integer> temp = this.numbers
                    .remove(this.last.toLowerCase());
            int increment = temp.value() + 1;
            this.numbers.add(this.last.toLowerCase(), increment);
        } else {
            this.numbers.add(this.last.toLowerCase(), 1);
        }
        int value = this.numbers.value(this.last.toLowerCase());
        String name = "Name: " + this.first + " " + this.last;
        String email = "Email: " + this.last.toLowerCase() + "." + value
                + "@osu.edu";
        return name + ",  " + email;
    }

}
