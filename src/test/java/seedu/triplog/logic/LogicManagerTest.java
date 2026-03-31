package seedu.triplog.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.Messages.MESSAGE_INVALID_TRIP_DISPLAYED_INDEX;
import static seedu.triplog.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.triplog.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.END_DATE_DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.START_DATE_DESC_AMY;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalTrips.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.triplog.logic.commands.AddCommand;
import seedu.triplog.logic.commands.CommandResult;
import seedu.triplog.logic.commands.ListCommand;
import seedu.triplog.logic.commands.TripSummaryUtil;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.logic.parser.exceptions.ParseException;
import seedu.triplog.model.Model;
import seedu.triplog.model.ModelManager;
import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.UserPrefs;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.storage.JsonTripLogStorage;
import seedu.triplog.storage.JsonUserPrefsStorage;
import seedu.triplog.storage.StorageManager;
import seedu.triplog.testutil.TripBuilder;

/**
 * Contains unit tests for LogicManager.
 */
public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;
    private StorageManager storage;

    @BeforeEach
    public void setUp() {
        JsonTripLogStorage tripLogStorage =
                new JsonTripLogStorage(temporaryFolder.resolve("tripLog.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder
                .resolve("userPrefs.json"));
        storage = new StorageManager(tripLogStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_TRIP_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        String expectedSummary = TripSummaryUtil.calculateSummary(model.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, "start date", expectedSummary);
        assertCommandSuccess(listCommand, expectedMessage, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredTripList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredTripList().remove(0));
    }

    @Test
    public void getSortedTripList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getSortedTripList().remove(0));
    }

    @Test
    public void getInitialDataLoadError_noError_returnsNull() {
        assertNull(logic.getInitialDataLoadError());
    }

    @Test
    public void getInitialDataLoadError_withError_returnsErrorMessage() {
        String errorMessage = "Test Error Message";
        Logic logicWithError = new LogicManager(model, storage, errorMessage);
        assertEquals(errorMessage, logicWithError.getInitialDataLoadError());
    }

    @Test
    public void getSummary_initialState_returnsCorrectSummary() {
        String expectedSummary = TripSummaryUtil.calculateSummary(model.getFilteredTripList());
        String expectedMessage = String.format("Listed all trips sorted by %s.\n%s", "start date", expectedSummary);
        assertEquals(expectedMessage, logic.getSummary());
    }

    @Test
    public void getSummary_nullSortDescription_defaultsToStartDate() {
        // Use a Model stub to force a null return value for the sort description
        Model modelStub = new ModelManager() {
            @Override
            public String getLastSortDescription() {
                return null;
            }
        };

        Logic logicWithNullSort = new LogicManager(modelStub, storage);
        String summary = logicWithNullSort.getSummary();

        // This triggers the 'if (sortDescription == null)' branch in LogicManager
        assertTrue(summary.contains("sorted by start date"));
    }

    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
        Model expectedModel = new ModelManager(model.getTripLog(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        JsonTripLogStorage tripLogStorage = new JsonTripLogStorage(prefPath) {
            @Override
            public void saveTripLog(ReadOnlyTripLog tripLog, Path filePath) throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(tripLogStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + START_DATE_DESC_AMY + END_DATE_DESC_AMY;

        Trip expectedTrip = new TripBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addTrip(expectedTrip);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedFeedback = String.format(AddCommand.MESSAGE_SUCCESS,
                seedu.triplog.logic.Messages.format(expectedTrip), expectedSummary);

        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
