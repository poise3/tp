package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.model.Model.PREDICATE_SHOW_ALL_TRIPS;

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

    private final String sortKey;

    /**
     * Default constructor: sets sortKey to null to maintain existing sort order.
     */
    public ListCommand() {
        this.sortKey = null;
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

        String sortDescription;

        if (sortKey != null) {
            Comparator<Trip> comparator = getComparator(sortKey);
            model.updateSortedTripList(comparator);
            sortDescription = getSortDescription(sortKey);
            model.setLastSortDescription(sortDescription);
        } else {
            sortDescription = model.getLastSortDescription();
        }

        model.updateFilteredTripList(PREDICATE_SHOW_ALL_TRIPS);

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
            return getNameComparator();
        case "end":
            return getEndDateComparator();
        case "len":
            return getDurationComparator();
        case "start":
            return getStartDateComparator();
        default:
            throw new CommandException(MESSAGE_INVALID_SORT_KEY);
        }
    }

    private Comparator<Trip> getNameComparator() {
        return Comparator.comparing(Trip::getNameLowerCase);
    }

    private Comparator<Trip> getStartDateComparator() {
        return Comparator.comparing(Trip::getStartDateDisplay,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(getNameComparator());
    }

    private Comparator<Trip> getEndDateComparator() {
        return Comparator.comparing(Trip::getEndDateDisplay,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(getNameComparator());
    }

    private Comparator<Trip> getDurationComparator() {
        Comparator<Trip> lenComparator = (t1, t2) -> Long.compare(t2.getDurationInDays(),
                t1.getDurationInDays());
        return lenComparator.thenComparing(getNameComparator());
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
