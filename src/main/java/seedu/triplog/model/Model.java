package seedu.triplog.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.triplog.commons.core.GuiSettings;
import seedu.triplog.model.trip.Trip;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Trip> PREDICATE_SHOW_ALL_TRIPS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' trip log file path.
     */
    Path getTripLogFilePath();

    /**
     * Sets the user prefs' trip log file path.
     */
    void setTripLogFilePath(Path tripLogFilePath);

    /**
     * Replaces trip log data with the data in {@code tripLog}.
     */
    void setTripLog(ReadOnlyTripLog tripLog);

    /** Returns the TripLog */
    ReadOnlyTripLog getTripLog();

    /**
     * Returns true if a trip with the same identity as {@code trip} exists in the trip log.
     */
    boolean hasTrip(Trip trip);

    /**
     * Returns true if a trip with the same identity as {@code trip} exists in the trip log,
     * excluding {@code excludedTrip}.
     */
    boolean hasTripExcluding(Trip trip, Trip excludedTrip);

    /**
     * Deletes the given trip.
     * The trip must exist in the trip log.
     */
    void deleteTrip(Trip target);

    /**
     * Adds the given trip.
     * {@code trip} must not already exist in the trip log.
     */
    void addTrip(Trip trip);

    /**
     * Replaces the given trip {@code target} with {@code editedTrip}.
     * {@code target} must exist in the trip log.
     * The trip identity of {@code editedTrip} must not be the same as another existing trip in the trip log.
     */
    void setTrip(Trip target, Trip editedTrip);

    /** Returns an unmodifiable view of the filtered trip list */
    ObservableList<Trip> getFilteredTripList();

    /**
     * Updates the filter of the filtered trip list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTripList(Predicate<Trip> predicate);

    /** Returns an unmodifiable view of the sorted trip list */
    ObservableList<Trip> getSortedTripList();

    /**
     * Updates the sort order of the sorted trip list using the given {@code comparator}.
     * @throws NullPointerException if {@code comparator} is null.
     */
    void updateSortedTripList(Comparator<Trip> comparator);

    /** Returns the description of the last applied sort key */
    String getLastSortDescription();

    /** Sets the description of the last applied sort key */
    void setLastSortDescription(String description);
}
