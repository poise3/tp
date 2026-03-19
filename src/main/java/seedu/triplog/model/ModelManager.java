package seedu.triplog.model;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.triplog.commons.core.GuiSettings;
import seedu.triplog.commons.core.LogsCenter;
import seedu.triplog.model.trip.Trip;

/**
 * Represents the in-memory model of the trip log data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TripLog tripLog;
    private final UserPrefs userPrefs;
    private final FilteredList<Trip> filteredTrips;
    private final SortedList<Trip> sortedTrips;

    /**
     * Initializes a ModelManager with the given tripLog and userPrefs.
     */
    public ModelManager(ReadOnlyTripLog tripLog, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(tripLog, userPrefs);

        logger.fine("Initializing with trip log: " + tripLog + " and user prefs " + userPrefs);

        this.tripLog = new TripLog(tripLog);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredTrips = new FilteredList<>(this.tripLog.getTripList());
        sortedTrips = new SortedList<>(filteredTrips);

        sortedTrips.setComparator(Trip.CHRONOLOGICAL_COMPARATOR);
    }

    public ModelManager() {
        this(new TripLog(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getTripLogFilePath() {
        return userPrefs.getTripLogFilePath();
    }

    @Override
    public void setTripLogFilePath(Path tripLogFilePath) {
        requireNonNull(tripLogFilePath);
        userPrefs.setTripLogFilePath(tripLogFilePath);
    }

    //=========== TripLog ================================================================================

    @Override
    public void setTripLog(ReadOnlyTripLog tripLog) {
        this.tripLog.resetData(tripLog);
    }

    @Override
    public ReadOnlyTripLog getTripLog() {
        return tripLog;
    }

    @Override
    public boolean hasTrip(Trip trip) {
        requireNonNull(trip);
        return tripLog.hasTrip(trip);
    }

    @Override
    public void deleteTrip(Trip target) {
        tripLog.removeTrip(target);
    }

    @Override
    public void addTrip(Trip trip) {
        tripLog.addTrip(trip);
        updateFilteredTripList(PREDICATE_SHOW_ALL_TRIPS);
    }

    @Override
    public void setTrip(Trip target, Trip editedTrip) {
        requireAllNonNull(target, editedTrip);
        tripLog.setTrip(target, editedTrip);
    }

    //=========== Filtered Trip List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Trip} backed by the internal list of
     * {@code tripLog}
     */
    @Override
    public ObservableList<Trip> getFilteredTripList() {
        return sortedTrips;
    }

    @Override
    public void updateFilteredTripList(Predicate<Trip> predicate) {
        requireNonNull(predicate);
        filteredTrips.setPredicate(predicate);
    }

    @Override
    public ObservableList<Trip> getSortedTripList() {
        return sortedTrips;
    }

    @Override
    public void updateSortedTripList(Comparator<Trip> comparator) {
        requireNonNull(comparator);
        sortedTrips.setComparator(comparator);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return tripLog.equals(otherModelManager.tripLog)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredTrips.equals(otherModelManager.filteredTrips);
    }

}
