package seedu.triplog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.model.Model.PREDICATE_SHOW_ALL_TRIPS;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalTrips.ALICE;
import static seedu.triplog.testutil.TypicalTrips.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.triplog.commons.core.GuiSettings;
import seedu.triplog.logic.commands.ListCommand;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.NameContainsKeywordsPredicate;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripDate;

/**
 * Contains unit tests for ModelManager.
 */
public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new TripLog(), new TripLog(modelManager.getTripLog()));
    }

    @Test
    public void constructor_initializesCorrectComparator() {
        UserPrefs namePrefs = new UserPrefs();
        ModelManager model = new ModelManager(new TripLog(), namePrefs, Trip.CHRONOLOGICAL_COMPARATOR);
        assertEquals(new UserPrefs(), model.getUserPrefs());
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setTripLogFilePath(Paths.get("triplog/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setTripLogFilePath(Paths.get("new/triplog/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setTripLogFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setTripLogFilePath(null));
    }

    @Test
    public void setTripLogFilePath_validPath_setsTripLogFilePath() {
        Path path = Paths.get("triplog/file/path");
        modelManager.setTripLogFilePath(path);
        assertEquals(path, modelManager.getTripLogFilePath());
    }

    @Test
    public void hasTrip_nullTrip_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasTrip(null));
    }

    @Test
    public void hasTrip_tripNotInTripLog_returnsFalse() {
        // EP: trip not present in the model
        assertFalse(modelManager.hasTrip(ALICE));
    }

    @Test
    public void hasTrip_tripInTripLog_returnsTrue() {
        // EP: trip present in the model
        modelManager.addTrip(ALICE);
        assertTrue(modelManager.hasTrip(ALICE));
    }

    @Test
    public void getFilteredTripList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredTripList().remove(0));
    }

    @Test
    public void getFilteredTripList_isSortedChronologically() {
        // EP: list containing trips with early and late dates
        Trip laterTrip = new Trip(new Name("Later"), null, null, null, Collections.emptySet(),
                new TripDate("2026-12-01"), null);
        Trip earlierTrip = new Trip(new Name("Earlier"), null, null, null, Collections.emptySet(),
                new TripDate("2026-01-01"), null);

        modelManager.addTrip(laterTrip);
        modelManager.addTrip(earlierTrip);
        assertEquals(earlierTrip, modelManager.getFilteredTripList().get(0));
        assertEquals(laterTrip, modelManager.getFilteredTripList().get(1));
    }

    @Test
    public void equals() {
        TripLog tripLog = new TripLog();
        tripLog.addTrip(ALICE);
        tripLog.addTrip(BENSON);
        TripLog differentTripLog = new TripLog();
        UserPrefs userPrefs = new UserPrefs();

        // EP: same values
        modelManager = new ModelManager(tripLog, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(tripLog, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // EP: same object
        assertTrue(modelManager.equals(modelManager));

        // EP: null
        assertFalse(modelManager.equals(null));

        // EP: different types
        assertFalse(modelManager.equals(5));

        // EP: different internal trip logs
        assertFalse(modelManager.equals(new ModelManager(differentTripLog, userPrefs)));

        // EP: different filtered list states
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredTripList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(tripLog, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTripList(PREDICATE_SHOW_ALL_TRIPS);

        // EP: different file paths in user prefs
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setTripLogFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(tripLog, differentUserPrefs)));

        // EP: different last sort descriptions
        UserPrefs diffSortPrefs = new UserPrefs();
        diffSortPrefs.setLastSortDescription(ListCommand.SORT_DESC_NAME);
        assertFalse(modelManager.equals(new ModelManager(tripLog, diffSortPrefs)));
    }
}
