package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.model.Model.PREDICATE_SHOW_ALL_TRIPS;

import java.time.LocalDate;
import java.util.Comparator;

import javafx.collections.ObservableList;
import seedu.triplog.model.Model;
import seedu.triplog.model.trip.Trip;

/**
 * Lists all trips in the trip log to the user, sorted by start date ascending.
 * Trips with no start date are shown last.
 * Includes a summary of trip statuses (Upcoming, Ongoing, Completed).
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all trips sorted by start date.\n%s";

    public static final Comparator<Trip> SORT_BY_START_DATE = Comparator.comparing(
            trip -> trip.getStartDate() == null ? null : trip.getStartDate().value,
            Comparator.nullsLast(Comparator.naturalOrder())
    );

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTripList(PREDICATE_SHOW_ALL_TRIPS);
        model.updateSortedTripList(SORT_BY_START_DATE);

        ObservableList<Trip> lastShownList = model.getFilteredTripList();
        String summary = calculateSummary(lastShownList);

        return new CommandResult(String.format(MESSAGE_SUCCESS, summary));
    }

    /**
     * Calculates the count of trips based on their chronological status relative to today.
     */
    private String calculateSummary(ObservableList<Trip> trips) {
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
}
