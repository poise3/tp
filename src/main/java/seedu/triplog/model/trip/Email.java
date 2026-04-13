package seedu.triplog.model.trip;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.commons.util.AppUtil.checkArgument;

/**
 * Represents a Trip's email in the TripLog.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    private static final String SPECIAL_CHARACTERS = "+_.-";
    public static final String MESSAGE_CONSTRAINTS = "Emails should be of the format local-part@domain "
            + "and adhere to the following constraints:\n"
            + "1. The local-part should only contain alphanumeric characters and these special characters, excluding "
            + "the parentheses, (" + SPECIAL_CHARACTERS + "). The local-part may not start or end with any special "
            + "characters.\n"
            + "2. This is followed by a '@' and then a domain name. The domain name is made up of domain labels "
            + "separated by periods.\n"
            + "The domain name must:\n"
            + "    - end with a domain label at least 1 character long\n"
            + "    - have each domain label start and end with alphanumeric characters\n"
            + "    - have each domain label consist of alphanumeric characters, separated only by hyphens, if any\n"
            + "    - contain at least 1 period separating the domain labels (e.g. example.com)";

    // Alphanumeric characters except underscore
    private static final String ALPHANUMERIC_NO_UNDERSCORE = "[^\\W_]+";

    // Local part: starts/ends with alphanumeric, allows special characters in between
    private static final String LOCAL_PART_REGEX = "^" + ALPHANUMERIC_NO_UNDERSCORE + "([" + SPECIAL_CHARACTERS + "]"
            + ALPHANUMERIC_NO_UNDERSCORE + ")*";

    // Domain label: starts/ends with alphanumeric, hyphens allowed in between
    private static final String DOMAIN_LABEL_REGEX = ALPHANUMERIC_NO_UNDERSCORE + "(-"
            + ALPHANUMERIC_NO_UNDERSCORE + ")*";

    // Domain: sequence of labels separated by periods. Suffix length checked in the overall regex.
    private static final String DOMAIN_REGEX = DOMAIN_LABEL_REGEX + "(\\." + DOMAIN_LABEL_REGEX + ")+";

    public static final String VALIDATION_REGEX = LOCAL_PART_REGEX + "@" + DOMAIN_REGEX;

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        checkArgument(isValidEmail(email), MESSAGE_CONSTRAINTS);
        value = email;
    }

    /**
     * Returns if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
