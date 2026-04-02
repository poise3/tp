package seedu.triplog.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import seedu.triplog.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path tripLogFilePath = Paths.get("data", "triplog.json");
    private String lastSortDescription = "start date";

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setTripLogFilePath(newUserPrefs.getTripLogFilePath());
        setLastSortDescription(newUserPrefs.getLastSortDescription());
    }

    @Override
    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    @Override
    public Path getTripLogFilePath() {
        return tripLogFilePath;
    }

    public void setTripLogFilePath(Path tripLogFilePath) {
        requireNonNull(tripLogFilePath);
        this.tripLogFilePath = tripLogFilePath;
    }

    @Override
    public String getLastSortDescription() {
        return lastSortDescription;
    }

    public void setLastSortDescription(String lastSortDescription) {
        requireNonNull(lastSortDescription);
        this.lastSortDescription = lastSortDescription;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UserPrefs)) {
            return false;
        }

        UserPrefs otherUserPrefs = (UserPrefs) other;
        return guiSettings.equals(otherUserPrefs.guiSettings)
                && tripLogFilePath.equals(otherUserPrefs.tripLogFilePath)
                && lastSortDescription.equals(otherUserPrefs.lastSortDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, tripLogFilePath, lastSortDescription);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + tripLogFilePath);
        sb.append("\nLast sort description : " + lastSortDescription);
        return sb.toString();
    }

}
