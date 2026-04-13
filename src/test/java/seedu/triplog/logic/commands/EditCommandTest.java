package seedu.triplog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.triplog.logic.commands.CommandTestUtil.showTripAtIndex;
import static seedu.triplog.logic.commands.EditCommand.MESSAGE_EDIT_TRIP_SUCCESS;
import static seedu.triplog.model.trip.Trip.MAX_NAME_LENGTH;
import static seedu.triplog.model.trip.Trip.MESSAGE_INVALID_DATE_ORDER;
import static seedu.triplog.model.trip.Trip.MESSAGE_INVALID_NAME_LENGTH;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_FIRST_TRIP;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_SECOND_TRIP;
import static seedu.triplog.testutil.TypicalTrips.getTypicalTripLog;

import org.junit.jupiter.api.Test;

import seedu.triplog.commons.core.index.Index;
import seedu.triplog.logic.Messages;
import seedu.triplog.logic.commands.EditCommand.EditTripDescriptor;
import seedu.triplog.model.Model;
import seedu.triplog.model.ModelManager;
import seedu.triplog.model.TripLog;
import seedu.triplog.model.UserPrefs;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripDate;
import seedu.triplog.testutil.EditTripDescriptorBuilder;
import seedu.triplog.testutil.TripBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Trip editedTrip = new TripBuilder().build();
        EditTripDescriptor descriptor = new EditTripDescriptorBuilder(editedTrip).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRIP, descriptor);

        Model expectedModel = new ModelManager(new TripLog(model.getTripLog()), new UserPrefs());
        expectedModel.setTrip(model.getFilteredTripList().get(0), editedTrip);
        expectedModel.updateFilteredTripList(Model.PREDICATE_SHOW_ALL_TRIPS);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(MESSAGE_EDIT_TRIP_SUCCESS, Messages.format(editedTrip), expectedSummary);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredTripList().size());
        Trip lastTrip = model.getFilteredTripList().get(indexLastPerson.getZeroBased());

        TripBuilder personInList = new TripBuilder(lastTrip);
        Trip editedTrip = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditTripDescriptor descriptor = new EditTripDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        Model expectedModel = new ModelManager(new TripLog(model.getTripLog()), new UserPrefs());
        expectedModel.setTrip(lastTrip, editedTrip);
        expectedModel.updateFilteredTripList(Model.PREDICATE_SHOW_ALL_TRIPS);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(MESSAGE_EDIT_TRIP_SUCCESS, Messages.format(editedTrip), expectedSummary);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sameFieldValueUnfilteredList_failure() {
        Trip tripToEdit = model.getFilteredTripList().get(INDEX_FIRST_TRIP.getZeroBased());

        EditTripDescriptor descriptor = new EditTripDescriptorBuilder()
                .withStart(tripToEdit.getStartDate().value.toString())
                .build();

        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRIP, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_NO_CHANGES);
    }

    @Test
    public void execute_filteredList_success() {
        Trip tripInFilteredList = model.getFilteredTripList().get(INDEX_FIRST_TRIP.getZeroBased());
        Trip editedTrip = new TripBuilder(tripInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRIP,
                new EditTripDescriptorBuilder().withName(VALID_NAME_BOB).build());

        Model expectedModel = new ModelManager(new TripLog(model.getTripLog()), new UserPrefs());
        expectedModel.setTrip(model.getFilteredTripList().get(0), editedTrip);
        expectedModel.updateFilteredTripList(Model.PREDICATE_SHOW_ALL_TRIPS);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(MESSAGE_EDIT_TRIP_SUCCESS, Messages.format(editedTrip), expectedSummary);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Trip firstTrip = model.getFilteredTripList().get(INDEX_FIRST_TRIP.getZeroBased());
        EditTripDescriptor descriptor = new EditTripDescriptorBuilder(firstTrip).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_TRIP, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_TRIP);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showTripAtIndex(model, INDEX_FIRST_TRIP);

        // edit trip in filtered list into a duplicate in trip log
        Trip tripInList = model.getTripLog().getTripList().get(INDEX_SECOND_TRIP.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRIP,
                new EditTripDescriptorBuilder(tripInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_TRIP);
    }

    @Test
    public void execute_invalidDateOrder_failure() {
        EditTripDescriptor descriptor = new EditTripDescriptorBuilder()
                .withStart("2026-03-20")
                .withEnd("2026-03-10")
                .build();

        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRIP, descriptor);

        assertCommandFailure(editCommand, model, MESSAGE_INVALID_DATE_ORDER);
    }

    @Test
    public void execute_validDateOrder_success() {
        EditTripDescriptor descriptor = new EditTripDescriptorBuilder()
                .withStart("2026-03-10")
                .withEnd("2026-03-20")
                .build();

        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRIP, descriptor);

        Trip editedTrip = new TripBuilder(model.getFilteredTripList().get(INDEX_FIRST_TRIP.getZeroBased()))
                .withStart("2026-03-10")
                .withEnd("2026-03-20")
                .build();

        Model expectedModel = new ModelManager(new TripLog(model.getTripLog()), new UserPrefs());
        expectedModel.setTrip(model.getFilteredTripList().get(INDEX_FIRST_TRIP.getZeroBased()), editedTrip);
        expectedModel.updateFilteredTripList(Model.PREDICATE_SHOW_ALL_TRIPS);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_TRIP_SUCCESS,
                Messages.format(editedTrip),
                expectedSummary
        );

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidStartDateYear_failure() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                new EditTripDescriptorBuilder().withStart("0000-01-01").build()
        );

        assertEquals(TripDate.MESSAGE_CONSTRAINTS, thrown.getMessage());
    }

    @Test
    public void execute_invalidEndDateYear_failure() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                new EditTripDescriptorBuilder().withEnd("9999-12-31").build()
        );

        assertEquals(TripDate.MESSAGE_CONSTRAINTS, thrown.getMessage());
    }

    @Test
    public void execute_nameTooLong_failure() {
        String longName = "A".repeat(MAX_NAME_LENGTH + 1);
        EditTripDescriptor descriptor = new EditTripDescriptorBuilder()
                .withName(longName)
                .build();

        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRIP, descriptor);

        assertCommandFailure(editCommand, model, MESSAGE_INVALID_NAME_LENGTH);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTripList().size() + 1);
        EditTripDescriptor descriptor = new EditTripDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_TRIP_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of trip log
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showTripAtIndex(model, INDEX_FIRST_TRIP);
        Index outOfBoundIndex = INDEX_SECOND_TRIP;
        // ensures that outOfBoundIndex is still in bounds of trip log list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTripLog().getTripList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditTripDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_TRIP_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_TRIP, DESC_AMY);

        // same values -> returns true
        EditTripDescriptor copyDescriptor = new EditTripDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_TRIP, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_TRIP, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_TRIP, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditTripDescriptor editTripDescriptor = new EditTripDescriptor();
        EditCommand editCommand = new EditCommand(index, editTripDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editTripDescriptor="
                + editTripDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
