package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.model.Model.PREDICATE_SHOW_ALL_TRIPS;

import java.time.LocalDate;
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
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String PREFIX_SORT = "sort/";

    public static final String MESSAGE_SUCCESS = "Listed all trips sorted by %s.\n%s";
    public static final String MESSAGE_INVALID_SORT_KEY = "Invalid sort key! "
            + "Supported keys: name, start, end, len";

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

        Comparator<Trip> comparator;
        String sortDescription;

        switch (sortKey) {
        case "name":
            comparator = Comparator.comparing(trip -> trip.getName().fullName.toLowerCase());
            sortDescription = "name (alphabetical)";
            break;
        case "end":
            comparator = Comparator.comparing(
                    trip -> trip.getEndDate() == null ? null : trip.getEndDate().value,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
            sortDescription = "end date";
            break;
        case "len":
            comparator = (t1, t2) -> Long.compare(calculateDuration(t2), calculateDuration(t1));
            sortDescription = "duration (longest first)";
            break;
        case "start":
            comparator = Comparator.comparing(
                    trip -> trip.getStartDate() == null ? null : trip.getStartDate().value,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
            sortDescription = "start date";
            break;
        default:
            throw new CommandException(MESSAGE_INVALID_SORT_KEY);
        }

        model.updateFilteredTripList(PREDICATE_SHOW_ALL_TRIPS);
        model.updateSortedTripList(comparator);

        ObservableList<Trip> lastShownList = model.getFilteredTripList();
        String summary = calculateSummary(lastShownList);

        return new CommandResult(String.format(MESSAGE_SUCCESS, sortDescription, summary));
    }

    private long calculateDuration(Trip trip) {
        if (trip.getStartDate() == null || trip.getEndDate() == null) {
            return -1;
        }
        return ChronoUnit.DAYS.between(trip.getStartDate().value, trip.getEndDate().value);
    }

    /**
     * Calculates the summary dashboard text for a given list of trips.
     */
    public static String calculateSummary(ObservableList<Trip> trips) {
        int upcoming = 0;
        int ongoing = 0;
        int completed = 0;
        int planning = 0;
        LocalDate today = LocalDate.now();

        for (Trip trip : trips) {
            if (trip.getStartDate() == null) {
                planning++;
                continue;
            }

            LocalDate start = trip.getStartDate().value;
            LocalDate end = (trip.getEndDate() == null) ? null : trip.getEndDate().value;

            if (today.isBefore(start)) {
                upcoming++;
            } else if (end != null && today.isAfter(end)) {
                completed++;
            } else {
                ongoing++;
            }
        }
        return String.format("Summary: %d Upcoming, %d Ongoing, %d Completed, %d Planning",
                upcoming, ongoing, completed, planning);
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
