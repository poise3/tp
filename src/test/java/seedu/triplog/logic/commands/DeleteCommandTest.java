package seedu.triplog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.triplog.logic.commands.CommandTestUtil.showTripAtIndex;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_FIRST_TRIP;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_SECOND_TRIP;
import static seedu.triplog.testutil.TypicalTrips.ALICE;
import static seedu.triplog.testutil.TypicalTrips.ELLE;
import static seedu.triplog.testutil.TypicalTrips.FIONA;
import static seedu.triplog.testutil.TypicalTrips.getTypicalTripLog;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.triplog.commons.core.index.Index;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.ModelManager;
import seedu.triplog.model.UserPrefs;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripDate;
import seedu.triplog.model.trip.TripMatchesDeletePredicate;

/**
 * Contains integration tests and unit tests for {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Trip tripToDelete = model.getFilteredTripList().get(INDEX_FIRST_TRIP.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TRIP);

        ModelManager expectedModel = new ModelManager(model.getTripLog(), new UserPrefs());
        expectedModel.deleteTrip(tripToDelete);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIP_SUCCESS, 1, expectedSummary);

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
        showTripAtIndex(model, INDEX_FIRST_TRIP);

        Trip tripToDelete = model.getFilteredTripList().get(INDEX_FIRST_TRIP.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TRIP);

        Model expectedModel = new ModelManager(model.getTripLog(), new UserPrefs());
        expectedModel.deleteTrip(tripToDelete);
        showNoTrip(expectedModel);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIP_SUCCESS, 1, expectedSummary);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showTripAtIndex(model, INDEX_FIRST_TRIP);

        Index outOfBoundIndex = INDEX_SECOND_TRIP;
        // ensures that outOfBoundIndex is still in bounds of trip log list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTripLog().getTripList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_INDEX_OUT_OF_RANGE);
    }

    @Test
    public void execute_validRangeUnfilteredList_success() {
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1),
                Index.fromOneBased(2));

        Model expectedModel = new ModelManager(model.getTripLog(), new UserPrefs());
        Trip firstTrip = expectedModel.getFilteredTripList().get(0);
        Trip secondTrip = expectedModel.getFilteredTripList().get(1);
        expectedModel.deleteTrip(firstTrip);
        expectedModel.deleteTrip(secondTrip);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIPS_SUCCESS, 2, expectedSummary);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
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

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIP_SUCCESS, 1, expectedSummary);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteByTagNoMatch_throwsException() {
        DeleteCommand deleteCommand = new DeleteCommand(
                new TripMatchesDeletePredicate(null, null, null, null, null,
                        null, Set.of(new Tag("nonexistenttag"))));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_NO_MATCHING_TRIPS);
    }

    @Test
    public void getTripsToDelete_singleIndex_returnsTrip() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TRIP);

        assertEquals(1, deleteCommand.getTripsToDelete(model).size());
        assertEquals(model.getFilteredTripList().get(0), deleteCommand.getTripsToDelete(model).get(0));
    }

    @Test
    public void getTripsToDelete_range_returnsTrips() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(2));

        assertEquals(2, deleteCommand.getTripsToDelete(model).size());
    }

    @Test
    public void getTripsToDelete_filter_returnsTrips() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(
                new TripMatchesDeletePredicate(new Name("Alice Pauline"),
                        null, null, null, null, null, Set.of()));

        assertEquals(1, deleteCommand.getTripsToDelete(model).size());
    }

    @Test
    public void buildPreviewMessage_singleTrip_success() {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TRIP);
        Trip trip = model.getFilteredTripList().get(0);

        String message = deleteCommand.buildPreviewMessage(java.util.List.of(trip));
        assertTrue(message.contains("Preview:"));
        assertTrue(message.contains(trip.getName().fullName));
    }

    @Test
    public void buildPreviewMessage_multipleTrips_success() {
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        java.util.List<Trip> trips = java.util.List.of(
                model.getFilteredTripList().get(0),
                model.getFilteredTripList().get(1));

        String message = deleteCommand.buildPreviewMessage(trips);

        assertTrue(message.contains("Preview:"));
        assertTrue(message.contains(trips.get(0).getName().fullName));
        assertTrue(message.contains(trips.get(1).getName().fullName));
    }

    @Test
    public void buildPreviewMessage_singleTrip_usesSingularHeader() {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TRIP);
        Trip trip = model.getFilteredTripList().get(0);

        String message = deleteCommand.buildPreviewMessage(java.util.List.of(trip));

        assertTrue(message.contains("Preview: 1 trip will be deleted."));
    }

    @Test
    public void buildPreviewMessage_multipleTrips_usesPluralHeader() {
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        java.util.List<Trip> trips = java.util.List.of(
                model.getFilteredTripList().get(0),
                model.getFilteredTripList().get(1));

        String message = deleteCommand.buildPreviewMessage(trips);

        assertTrue(message.contains("Preview: 2 trips will be deleted."));
    }

    @Test
    public void buildPreviewMessage_tripWithNoDates_usesNoDatePlaceholders() {
        Trip tripWithNoDates = new Trip(
                new Name("No Date Trip"),
                null,
                null,
                null,
                Set.of(),
                null,
                null);

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TRIP);
        String message = deleteCommand.buildPreviewMessage(java.util.List.of(tripWithNoDates));

        assertTrue(message.contains("No date"));
        assertTrue(message.contains("No Date Trip"));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_TRIP);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_TRIP);
        DeleteCommand deleteRangeCommand = new DeleteCommand(Index.fromOneBased(1),
                Index.fromOneBased(2));
        DeleteCommand deleteRangeCommandCopy = new DeleteCommand(Index.fromOneBased(1),
                Index.fromOneBased(2));

        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_TRIP);
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

    @Test
    public void execute_deleteByDateRange_success() {
        Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTripLog(), new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand(
                new TripMatchesDeletePredicate(
                        null, null, null, null,
                        new TripDate("2026-03-01"),
                        new TripDate("2026-05-10"),
                        Set.of()));

        expectedModel.deleteTrip(ALICE);
        expectedModel.deleteTrip(ELLE);
        expectedModel.deleteTrip(FIONA);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIPS_SUCCESS, 3, expectedSummary);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteByExactStartDate_success() {
        Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTripLog(), new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand(
                new TripMatchesDeletePredicate(
                        null, null, null, null,
                        new TripDate("2026-03-01"),
                        null,
                        Set.of()));

        expectedModel.deleteTrip(FIONA);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIP_SUCCESS, 1, expectedSummary);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteByExactEndDate_success() {
        Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTripLog(), new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand(
                new TripMatchesDeletePredicate(
                        null, null, null, null,
                        null,
                        new TripDate("2026-04-10"),
                        Set.of()));

        expectedModel.deleteTrip(ALICE);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIP_SUCCESS, 1, expectedSummary);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteByDateRangeNoMatch_throwsCommandException() {
        Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());
        DeleteCommand deleteCommand = new DeleteCommand(
                new TripMatchesDeletePredicate(
                        null, null, null, null,
                        new TripDate("2025-01-01"),
                        new TripDate("2025-01-10"),
                        Set.of()));

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_NO_MATCHING_TRIPS);
    }

    @Test
    public void execute_validSingleElementRange_returnsSingleDeleteMessage() {
        Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTripLog(), new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(2), Index.fromOneBased(2));

        Trip tripToDelete = expectedModel.getFilteredTripList().get(1);
        expectedModel.deleteTrip(tripToDelete);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIP_SUCCESS, 1, expectedSummary);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filterDeleteSingleMatch_returnsSingleDeleteMessage() {
        Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTripLog(), new UserPrefs());

        Trip tripToDelete = expectedModel.getFilteredTripList().get(0);
        DeleteCommand deleteCommand = new DeleteCommand(
                trip -> trip.equals(model.getFilteredTripList().get(0)));

        expectedModel.deleteTrip(tripToDelete);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRIP_SUCCESS, 1, expectedSummary);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void getTripsToDelete_invalidRange_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(model.getFilteredTripList().size() + 1));

        CommandException exception = assertThrows(
                CommandException.class, () -> deleteCommand.getTripsToDelete(model));
        assertEquals(DeleteCommand.MESSAGE_RANGE_OUT_OF_RANGE, exception.getMessage());
    }

    @Test
    public void getTripsToDelete_rangeExactBoundary_success() throws Exception {
        int size = model.getFilteredTripList().size();

        DeleteCommand deleteCommand = new DeleteCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(size));

        assertEquals(size, deleteCommand.getTripsToDelete(model).size());
    }

    @Test
    public void getTripsToDelete_rangeEmptyList_throwsCommandException() {
        showNoTrip(model);

        DeleteCommand deleteCommand = new DeleteCommand(
                Index.fromOneBased(1), Index.fromOneBased(1));

        assertThrows(
                CommandException.class, () -> deleteCommand.getTripsToDelete(model));
    }

    @Test
    public void getTripsToDelete_singleIndexOutOfRange_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(
                Index.fromOneBased(model.getFilteredTripList().size() + 1));

        CommandException exception = assertThrows(
                CommandException.class, () -> deleteCommand.getTripsToDelete(model));
        assertEquals(DeleteCommand.MESSAGE_INDEX_OUT_OF_RANGE, exception.getMessage());
    }

    @Test
    public void getTripsToDelete_filterNoMatch_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(
                new TripMatchesDeletePredicate(
                        new Name("Nonexistent"),
                        null, null, null, null, null, Set.of()));

        CommandException exception = assertThrows(
                CommandException.class, () -> deleteCommand.getTripsToDelete(model));
        assertEquals(DeleteCommand.MESSAGE_NO_MATCHING_TRIPS, exception.getMessage());
    }

    @Test
    public void getTripsToDelete_rangeFilteredList_returnsTrips() throws Exception {
        showTripAtIndex(model, INDEX_FIRST_TRIP);

        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(1));

        assertEquals(1, deleteCommand.getTripsToDelete(model).size());
        assertEquals(model.getFilteredTripList().get(0), deleteCommand.getTripsToDelete(model).get(0));
    }

    @Test
    public void equals_filterMode() {
        TripMatchesDeletePredicate predicate1 = new TripMatchesDeletePredicate(
                new Name("Alice Pauline"), null, null, null, null, null, Set.of());
        TripMatchesDeletePredicate predicate2 = new TripMatchesDeletePredicate(
                new Name("Alice Pauline"), null, null, null, null, null, Set.of());
        TripMatchesDeletePredicate predicate3 = new TripMatchesDeletePredicate(
                new Name("Elle Tan"), null, null, null, null, null, Set.of());

        DeleteCommand deleteFilterCommand1 = new DeleteCommand(predicate1);
        DeleteCommand deleteFilterCommand2 = new DeleteCommand(predicate2);
        DeleteCommand deleteFilterCommand3 = new DeleteCommand(predicate3);

        assertTrue(deleteFilterCommand1.equals(deleteFilterCommand2));
        assertFalse(deleteFilterCommand1.equals(deleteFilterCommand3));
    }

    @Test
    public void equals_rangeModeDifferentStartIndex_returnsFalse() {
        DeleteCommand firstRange = new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        DeleteCommand secondRange = new DeleteCommand(Index.fromOneBased(2), Index.fromOneBased(2));

        assertFalse(firstRange.equals(secondRange));
    }

    @Test
    public void equals_rangeModeDifferentEndIndex_returnsFalse() {
        DeleteCommand firstRange = new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        DeleteCommand secondRange = new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(3));

        assertFalse(firstRange.equals(secondRange));
    }

    @Test
    public void equals_filterModeSameObject_returnsTrue() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                new Name("Alice Pauline"), null, null, null, null, null, Set.of());
        DeleteCommand deleteCommand = new DeleteCommand(predicate);

        assertTrue(deleteCommand.equals(deleteCommand));
    }

    @Test
    public void toStringMethod_rangeMode() {
        Index startIndex = Index.fromOneBased(1);
        Index endIndex = Index.fromOneBased(2);
        DeleteCommand deleteCommand = new DeleteCommand(startIndex, endIndex);

        String expected = DeleteCommand.class.getCanonicalName()
                + "{mode=RANGE, startIndex=" + startIndex + ", endIndex=" + endIndex + "}";

        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void toStringMethod_filterMode() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                new Name("Alice Pauline"), null, null, null, null, null, Set.of());
        DeleteCommand deleteCommand = new DeleteCommand(predicate);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{mode=FILTER, predicate=" + predicate + "}";
        assertEquals(expected, deleteCommand.toString());
    }
}
