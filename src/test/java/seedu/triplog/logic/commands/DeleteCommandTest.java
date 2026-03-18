package seedu.triplog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.triplog.logic.commands.CommandTestUtil.showTripAtIndex;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.triplog.testutil.TypicalTrips.getTypicalTripLog;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.triplog.commons.core.index.Index;
import seedu.triplog.model.Model;
import seedu.triplog.model.ModelManager;
import seedu.triplog.model.UserPrefs;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripMatchesDeletePredicate;

/**
 * Contains integration tests and unit tests for {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Trip tripToDelete = model.getFilteredTripList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIP_SUCCESS, 1);

        ModelManager expectedModel = new ModelManager(model.getTripLog(), new UserPrefs());
        expectedModel.deleteTrip(tripToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTripList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_INDEX_OUT_OF_RANGE);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showTripAtIndex(model, INDEX_FIRST_PERSON);

        Trip tripToDelete = model.getFilteredTripList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIP_SUCCESS, 1);

        Model expectedModel = new ModelManager(model.getTripLog(), new UserPrefs());
        expectedModel.deleteTrip(tripToDelete);
        showNoTrip(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showTripAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTripLog().getTripList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_INDEX_OUT_OF_RANGE);
    }

    @Test
    public void execute_validRangeUnfilteredList_success() {
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(2));

        Model expectedModel = new ModelManager(model.getTripLog(), new UserPrefs());
        Trip firstTrip = expectedModel.getFilteredTripList().get(0);
        Trip secondTrip = expectedModel.getFilteredTripList().get(1);
        expectedModel.deleteTrip(firstTrip);
        expectedModel.deleteTrip(secondTrip);

        assertCommandSuccess(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_DELETE_TRIPS_SUCCESS, 2), expectedModel);
    }

    @Test
    public void execute_invalidRangeUnfilteredList_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1),
                Index.fromOneBased(model.getFilteredTripList().size() + 1));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_RANGE_OUT_OF_RANGE);
    }

    @Test
    public void execute_deleteByName_success() {
        Trip firstTrip = model.getFilteredTripList().get(0);
        DeleteCommand deleteCommand = new DeleteCommand(
                new TripMatchesDeletePredicate(new Name(firstTrip.getName().fullName),
                        null, null, null, null, null, Set.of()));

        Model expectedModel = new ModelManager(model.getTripLog(), new UserPrefs());
        expectedModel.deleteTrip(firstTrip);

        assertCommandSuccess(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_DELETE_TRIP_SUCCESS, 1), expectedModel);
    }

    @Test
    public void execute_deleteByTagNoMatch_throwsException() {
        DeleteCommand deleteCommand = new DeleteCommand(
                new TripMatchesDeletePredicate(null, null, null, null, null, null,
                        Set.of(new Tag("nonexistenttag"))));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_NO_MATCHING_TRIPS);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);
        DeleteCommand deleteRangeCommand = new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        DeleteCommand deleteRangeCommandCopy = new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(2));

        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        assertTrue(deleteRangeCommand.equals(deleteRangeCommandCopy));

        assertFalse(deleteFirstCommand.equals(1));
        assertFalse(deleteFirstCommand.equals(null));
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
        assertFalse(deleteFirstCommand.equals(deleteRangeCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{mode=SINGLE, targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no trips.
     */
    private void showNoTrip(Model model) {
        model.updateFilteredTripList(p -> false);
        assertTrue(model.getFilteredTripList().isEmpty());
    }
}
