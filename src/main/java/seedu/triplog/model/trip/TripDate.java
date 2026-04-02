package seedu.triplog.model.trip;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a trip date in the TripLog.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}.
 */
public class TripDate {
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2100;

    public static final String MESSAGE_CONSTRAINTS =
            "Dates should be in YYYY-MM-DD format and between " + MIN_YEAR
                    + "-01-01 and " + MAX_YEAR + "-12-31";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public final LocalDate value;

    /**
     * Constructs a {@code TripDate}.
     *
     * @param date A valid date.
     */
    public TripDate(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        this.value = LocalDate.parse(date, FORMATTER);
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        try {
            LocalDate parsed = LocalDate.parse(test, FORMATTER);
            int year = parsed.getYear();
            return year >= MIN_YEAR && year <= MAX_YEAR;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TripDate)) {
            return false;
        }

        TripDate otherDate = (TripDate) other;
        return value.equals(otherDate.value);
    }

    @Override
    public String toString() {
        return value.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
