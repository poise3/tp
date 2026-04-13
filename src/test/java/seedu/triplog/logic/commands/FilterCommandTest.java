package seedu.triplog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.triplog.testutil.TypicalTrips.getTypicalTripLog;

import org.junit.jupiter.api.Test;

import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.ModelManager;
import seedu.triplog.model.TripLog;
import seedu.triplog.model.UserPrefs;
import seedu.triplog.model.trip.TripDate;

public class FilterCommandTest {

    @Test
    public void equals() {
        TripDate startDate = new TripDate("2026-01-01");
        TripDate endDate = new TripDate("2026-03-01");

        FilterCommand filterFirstCommand = new FilterCommand(startDate, endDate);
        FilterCommand filterSecondCommand = new FilterCommand(startDate, endDate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        assertTrue(filterFirstCommand.equals(filterSecondCommand));

        // different type -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different start date -> returns false
        FilterCommand differentStartCommand =
                new FilterCommand(new TripDate("2025-01-01"), endDate);
        assertFalse(filterFirstCommand.equals(differentStartCommand));

        // different end date -> returns false
        FilterCommand differentEndCommand =
                new FilterCommand(startDate, new TripDate("2026-04-01"));
        assertFalse(filterFirstCommand.equals(differentEndCommand));
    }

    @Test
    public void execute_validRange_success() throws CommandException {
        Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());
        Model expectedModel = new ModelManager(new TripLog(model.getTripLog()), new UserPrefs());

        TripDate start = new TripDate("2020-01-01");
        TripDate end = new TripDate("2030-01-01");

        FilterCommand command = new FilterCommand(start, end);

        expectedModel.updateFilteredTripList(trip -> trip.getStartDate() != null
                && trip.getEndDate() != null
                && trip.getStartDate().value.isAfter(start.value.minusDays(1))
                && trip.getEndDate().value.isBefore(end.value.plusDays(1)),
                true);

        String expectedMessage = FilterCommand.MESSAGE_SUCCESS + start + " to " + end + ":";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noTripsFound() throws CommandException {
        Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());

        TripDate start = new TripDate("2100-01-01");
        TripDate end = new TripDate("2100-02-01");

        FilterCommand command = new FilterCommand(start, end);
        CommandResult result = command.execute(model);

        assertEquals(FilterCommand.MESSAGE_NO_TRIPS_FOUND, result.getFeedbackToUser());
        assertTrue(model.getFilteredTripList().isEmpty());
    }

    @Test
    public void execute_startDateAfterEndDate_throwsCommandException() {
        Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());

        TripDate start = new TripDate("2026-03-01");
        TripDate end = new TripDate("2026-01-01");

        FilterCommand command = new FilterCommand(start, end);

        CommandException thrown =
                assertThrows(CommandException.class, () -> command.execute(model));

        assertEquals(FilterCommand.MESSAGE_START_AFTER_END, thrown.getMessage());
    }

    @Test
    public void execute_tripWithNullDates_ignored() throws CommandException {
        Model model = new ModelManager(new TripLog(), new UserPrefs());

        TripDate start = new TripDate("2026-01-01");
        TripDate end = new TripDate("2026-03-01");

        FilterCommand command = new FilterCommand(start, end);

        CommandResult result = command.execute(model);

        assertEquals(FilterCommand.MESSAGE_NO_TRIPS_FOUND, result.getFeedbackToUser());
    }

    @Test
    public void toStringMethod() {
        TripDate start = new TripDate("2026-01-01");
        TripDate end = new TripDate("2026-03-01");

        FilterCommand command = new FilterCommand(start, end);

        String expected = FilterCommand.class.getCanonicalName()
                + "{startDate=" + start + ", endDate=" + end + "}";

        assertEquals(expected, command.toString());
    }
}
