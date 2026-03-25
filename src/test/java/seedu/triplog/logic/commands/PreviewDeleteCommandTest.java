package seedu.triplog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.testutil.TypicalTrips.ALICE;
import static seedu.triplog.testutil.TypicalTrips.ELLE;
import static seedu.triplog.testutil.TypicalTrips.FIONA;
import static seedu.triplog.testutil.TypicalTrips.getTypicalTripLog;

import org.junit.jupiter.api.Test;

import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.ModelManager;
import seedu.triplog.model.UserPrefs;

public class PreviewDeleteCommandTest {

    private final Model model = new ModelManager(getTypicalTripLog(), new UserPrefs());

    @Test
    public void execute_validIndexPreview_success() throws Exception {
        PreviewDeleteCommand command = new PreviewDeleteCommand("1");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Preview:"));
        assertTrue(feedback.contains(model.getFilteredTripList().get(0).getName().fullName));
    }

    @Test
    public void execute_validDateRangePreview_success() throws Exception {
        PreviewDeleteCommand command = new PreviewDeleteCommand("sd/2026-03-01 ed/2026-05-10");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Preview:"));
        assertTrue(feedback.contains("3"));
        assertTrue(feedback.contains(ALICE.getName().fullName));
        assertTrue(feedback.contains(ELLE.getName().fullName));
        assertTrue(feedback.contains(FIONA.getName().fullName));
    }

    @Test
    public void execute_invalidDeleteArgs_throwsCommandException() {
        PreviewDeleteCommand command = new PreviewDeleteCommand("abc");

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_noMatchingTrips_throwsCommandException() {
        PreviewDeleteCommand command = new PreviewDeleteCommand("n/Nonexistent");

        CommandException e = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(DeleteCommand.MESSAGE_NO_MATCHING_TRIPS, e.getMessage());
    }
}
