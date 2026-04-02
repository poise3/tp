package seedu.triplog.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.triplog.commons.core.GuiSettings;
import seedu.triplog.logic.commands.CommandResult;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.logic.parser.exceptions.ParseException;
import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.trip.Trip;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the TripLog.
     *
     * @see seedu.triplog.model.Model#getTripLog()
     */
    ReadOnlyTripLog getTripLog();

    /**
     * Returns an unmodifiable view of the filtered list of trips
     */
    ObservableList<Trip> getFilteredTripList();

    /**
     * Returns an unmodifiable view of the sorted trip list
     */
    ObservableList<Trip> getSortedTripList();

    /**
     * Returns the user prefs' trip log file path.
     */
    Path getTripLogFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the error message from the initial data load, if any.
     */
    String getInitialDataLoadError();

    /**
     * Returns the summary of trip statuses including sort information.
     */
    String getSummary();
}
