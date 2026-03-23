package seedu.triplog.logic.commands;

import java.time.LocalDate;

import javafx.collections.ObservableList;
import seedu.triplog.model.trip.Trip;

/**
 * Utility class for calculating trip statistics and summaries.
 */
public class TripSummaryUtil {

    /**
     * Calculates the summary dashboard text for a given list of trips based on chronological status.
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
}
