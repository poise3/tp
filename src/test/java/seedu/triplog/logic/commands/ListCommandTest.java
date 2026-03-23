package seedu.triplog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.triplog.logic.commands.CommandTestUtil.showTripAtIndex;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_FIRST_TRIP;
import static seedu.triplog.testutil.TypicalTrips.getTypicalTripLog;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.ModelManager;
import seedu.triplog.model.TripLog;
import seedu.triplog.model.UserPrefs;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.testutil.TripBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalTripLog(), new UserPrefs());
        expectedModel = new ModelManager(model.getTripLog(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameListWithSummary() {
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS,
                calculateExpectedSummary(expectedModel.getFilteredTripList()));

        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverythingWithSummary() {
        showTripAtIndex(model, INDEX_FIRST_TRIP);

        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS,
                calculateExpectedSummary(expectedModel.getFilteredTripList()));

        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_variedTripStatuses_summaryCorrect() {
        TripLog tripLog = new TripLog();
        LocalDate today = LocalDate.now();
        Trip upcoming = new TripBuilder().withName("Upcoming")
                .withStart(today.plusDays(7).toString())
                .withEnd(today.plusDays(10).toString()).build();

        Trip ongoing = new TripBuilder().withName("Ongoing")
                .withStart(today.minusDays(1).toString())
                .withEnd(today.plusDays(1).toString()).build();

        Trip completed = new TripBuilder().withName("Completed")
                .withStart(today.minusDays(10).toString())
                .withEnd(today.minusDays(5).toString()).build();

        Trip planning = new TripBuilder().withName("Planning").build();
        Trip planningNull = new Trip(planning.getName(), planning.getPhone(),
                planning.getEmail(), planning.getAddress(), planning.getTags(),
                null, null);

        tripLog.addTrip(upcoming);
        tripLog.addTrip(ongoing);
        tripLog.addTrip(completed);
        tripLog.addTrip(planningNull);

        Model model = new ModelManager(tripLog, new UserPrefs());
        Model expectedModel = new ModelManager(tripLog, new UserPrefs());

        String expectedSummary = "Summary: 1 Upcoming, 1 Ongoing, 1 Completed, 1 Planning";
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, expectedSummary);

        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listIsSorted_showsTripsInAscendingOrder() throws CommandException {
        new ListCommand().execute(model);

        List<Trip> sorted = model.getSortedTripList();
        for (int i = 0; i < sorted.size() - 1; i++) {
            if (sorted.get(i).getStartDate() != null && sorted.get(i + 1).getStartDate() != null) {
                assertTrue(
                        !sorted.get(i).getStartDate().value.isAfter(sorted.get(i + 1).getStartDate().value),
                        "Expected trips sorted by start date ascending"
                );
            } else if (sorted.get(i).getStartDate() == null) {
                assertTrue(sorted.get(i + 1).getStartDate() == null,
                        "Expected null start dates to be at the very end");
            }
        }
    }

    private String calculateExpectedSummary(ObservableList<Trip> trips) {
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
