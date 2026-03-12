package seedu.triplog.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.triplog.logic.parser.Prefix;
import seedu.triplog.model.trip.Trip;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_TRIP_DISPLAYED_INDEX = "Error: Trip index out of range.";
    public static final String MESSAGE_TRIPS_LISTED_OVERVIEW = "%1$d trips listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_MISSING_INDEX =
        "Error: Please specify the index of the trip to delete. Usage: delete <index>";
    public static final String MESSAGE_INVALID_INDEX_FORMAT = "Error: Index must be a positive integer.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code trip} for display to the user.
     */
    public static String format(Trip trip) {
        final StringBuilder builder = new StringBuilder();
        builder.append(trip.getName());

        if (trip.getPhone() != null) {
            builder.append("; Phone: ").append(trip.getPhone());
        }
        if (trip.getEmail() != null) {
            builder.append("; Email: ").append(trip.getEmail());
        }
        if (trip.getAddress() != null) {
            builder.append("; Address: ").append(trip.getAddress());
        }
        if (trip.getStartDate() != null) {
            builder.append("; Start Date: ").append(trip.getStartDate());
        }
        if (trip.getEndDate() != null) {
            builder.append("; End Date: ").append(trip.getEndDate());
        }

        if (!trip.getTags().isEmpty()) {
            builder.append("; Tags: ");
            trip.getTags().forEach(builder::append);
        }
        return builder.toString();
    }

}
