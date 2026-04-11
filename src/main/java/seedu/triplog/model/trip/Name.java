package seedu.triplog.model.trip;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.commons.util.AppUtil.checkArgument;

/**
 * Represents a Trip's name in the TripLog.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Trip names should start with a letter or number, and can only contain alphanumeric characters, "
                    + "spaces, and common punctuation (- , . ' () !). It cannot be blank.";

    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ,.'()!-]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
