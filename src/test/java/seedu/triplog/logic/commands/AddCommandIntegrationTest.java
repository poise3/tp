package seedu.triplog.logic.commands;

import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.triplog.testutil.TypicalTrips.getTypicalTripLog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.triplog.logic.Messages;
import seedu.triplog.model.Model;
import seedu.triplog.model.ModelManager;
import seedu.triplog.model.UserPrefs;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.testutil.TripBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalTripLog(), new UserPrefs());
    }

    @Test
    public void execute_newTrip_success() {
        Trip validTrip = new TripBuilder().build();

        Model expectedModel = new ModelManager(model.getTripLog(), new UserPrefs());
        expectedModel.addTrip(validTrip);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS,
                Messages.format(validTrip), expectedSummary);

        assertCommandSuccess(new AddCommand(validTrip), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateTrip_throwsCommandException() {
        Trip tripInList = model.getTripLog().getTripList().get(0);
        assertCommandFailure(new AddCommand(tripInList), model,
                AddCommand.MESSAGE_DUPLICATE_TRIP);
    }

}
