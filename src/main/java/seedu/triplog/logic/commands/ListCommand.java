package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.model.Model.PREDICATE_SHOW_ALL_TRIPS;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.trip.Trip;

/**
 * Lists all trips in the trip log to the user, sorted by a specified key.
 * Defaults to chronological (start date) sorting.
 * Includes a summary of trip statuses (Upcoming, Ongoing, Completed, Planning).
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String PREFIX_SORT = "sort/";

    public static final String MESSAGE_SUCCESS = "Listed all trips sorted by %s.\n%s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all trips. "
            + "Parameters: [" + PREFIX_SORT + "KEY]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_SORT + "name";

    public static final String MESSAGE_INVALID_SORT_KEY = "Invalid sort key! "
            + "Supported keys: name, start, end, len";

    private static final long UNKNOWN_DURATION = -1;

    private final String sortKey;

    /**
     * Default constructor: sets sortKey to "start".
     */
    public ListCommand() {
        this.sortKey = "start";
    }

    /**
     * Creates a ListCommand with a specific sort key.
     * @param sortKey The key to sort the list by (e.g., name, start, end, len).
     */
    public ListCommand(String sortKey) {
        requireNonNull(sortKey);
        this.sortKey = sortKey.toLowerCase();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Comparator<Trip> comparator = getComparator(sortKey);
        String sortDescription = getSortDescription(sortKey);

        model.updateFilteredTripList(PREDICATE_SHOW_ALL_TRIPS);
        model.updateSortedTripList(comparator);

        ObservableList<Trip> lastShownList = model.getFilteredTripList();
        String summary = TripSummaryUtil.calculateSummary(lastShownList);

        return new CommandResult(String.format(MESSAGE_SUCCESS, sortDescription, summary));
    }

    /**
     * Returns the appropriate comparator based on the sort key.
     */
    private Comparator<Trip> getComparator(String key) throws CommandException {
        switch (key) {
        case "name":
            return Comparator.comparing(trip -> trip.getName().fullName.toLowerCase());
        case "end":
            return Comparator.comparing(
                    trip -> trip.getEndDate() == null ? null : trip.getEndDate().value,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
        case "len":
            return (t1, t2) -> Long.compare(calculateDuration(t2), calculateDuration(t1));
        case "start":
            return Comparator.comparing(
                    trip -> trip.getStartDate() == null ? null : trip.getStartDate().value,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
        default:
            throw new CommandException(MESSAGE_INVALID_SORT_KEY);
        }
    }

    /**
     * Returns a human-readable description of the sort key.
     */
    private String getSortDescription(String key) {
        switch (key) {
        case "name":
            return "name (alphabetical)";
        case "end":
            return "end date";
        case "len":
            return "duration (longest first)";
        case "start":
        default:
            return "start date";
        }
    }

    private long calculateDuration(Trip trip) {
        if (trip.getStartDate() == null || trip.getEndDate() == null) {
            return UNKNOWN_DURATION;
        }
        return ChronoUnit.DAYS.between(trip.getStartDate().value, trip.getEndDate().value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ListCommand)) {
            return false;
        }

        ListCommand otherListCommand = (ListCommand) other;
        return Objects.equals(sortKey, otherListCommand.sortKey);
    }
}
