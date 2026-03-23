package seedu.triplog.logic.commands;

import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.triplog.logic.commands.CommandTestUtil.showTripAtIndex;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_FIRST_TRIP;
import static seedu.triplog.testutil.TypicalTrips.getTypicalTripLog;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, "start date", expectedSummary);

        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverythingWithSummary() {
        showTripAtIndex(model, INDEX_FIRST_TRIP);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, "start date", expectedSummary);

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
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, "start date", expectedSummary);

        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sortBySortName_success() {
        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, "name (alphabetical)", expectedSummary);
        assertCommandSuccess(new ListCommand("name"), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sortBySortLen_success() {
        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS,
                "duration (longest first)", expectedSummary);
        assertCommandSuccess(new ListCommand("len"), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidSortKey_throwsCommandException() {
        assertCommandFailure(new ListCommand("price"), model, ListCommand.MESSAGE_INVALID_SORT_KEY);
    }
}
