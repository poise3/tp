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
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.NameContainsKeywordsPredicate;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripDate;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new TripLog(), new TripLog(modelManager.getTripLog()));
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
        assertFalse(modelManager.hasTrip(ALICE));
    }

    @Test
    public void hasTrip_tripInTripLog_returnsTrue() {
        modelManager.addTrip(ALICE);
        assertTrue(modelManager.hasTrip(ALICE));
    }

    @Test
    public void getFilteredTripList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredTripList().remove(0));
    }

    @Test
    public void getFilteredTripList_isSortedChronologically() {
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

        // same values -> returns true
        modelManager = new ModelManager(tripLog, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(tripLog, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different tripLog -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentTripLog, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredTripList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(tripLog, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTripList(PREDICATE_SHOW_ALL_TRIPS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setTripLogFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(tripLog, differentUserPrefs)));
    }
}
