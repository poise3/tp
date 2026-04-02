package seedu.triplog.model;

import java.nio.file.Path;

import seedu.triplog.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getTripLogFilePath();

    /**
     * Returns the last sort description used by the user.
     */
    String getLastSortDescription();

}
