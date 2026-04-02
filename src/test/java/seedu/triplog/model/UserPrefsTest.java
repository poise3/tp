package seedu.triplog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.triplog.logic.commands.ListCommand;

/**
 * Contains unit tests for UserPrefs.
 */
public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setTripLogFilePath(null));
    }

    @Test
    public void equals() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setLastSortDescription(ListCommand.SORT_DESC_NAME);

        // same values -> returns true
        UserPrefs cb = new UserPrefs();
        cb.setLastSortDescription(ListCommand.SORT_DESC_NAME);
        assertTrue(userPrefs.equals(cb));

        // same object -> returns true
        assertTrue(userPrefs.equals(userPrefs));

        // null -> returns false
        assertFalse(userPrefs.equals(null));

        // different types -> returns false
        assertFalse(userPrefs.equals(5));

        // different lastSortDescription -> returns false
        UserPrefs differentSort = new UserPrefs();
        differentSort.setLastSortDescription(ListCommand.SORT_DESC_END);
        assertFalse(userPrefs.equals(differentSort));
    }

    @Test
    public void hashCode_test() {
        UserPrefs userPrefs = new UserPrefs();
        assertEquals(userPrefs.hashCode(), userPrefs.hashCode());
    }

    @Test
    public void toString_test() {
        UserPrefs userPrefs = new UserPrefs();
        assertTrue(userPrefs.toString().contains("Last sort description"));
    }
}
