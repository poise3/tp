package seedu.triplog.model.trip;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.triplog.model.trip.exceptions.DuplicateTripException;
import seedu.triplog.model.trip.exceptions.TripNotFoundException;

/**
 * A list of trips that enforces uniqueness between its elements and does not allow nulls.
 * A trip is considered unique by comparing using {@code Trip#isSameTrip(Trip)}. As such, adding and updating of
 * trips uses Trip#isSameTrip(Trip) for equality so as to ensure that the trip being added or updated is
 * unique in terms of identity in the UniqueTripList. However, the removal of a trip uses Trip#equals(Object) so
 * as to ensure that the trip with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Trip#isSameTrip(Trip)
 */
public class UniqueTripList implements Iterable<Trip> {

    private final ObservableList<Trip> internalList = FXCollections.observableArrayList();
    private final ObservableList<Trip> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent trip as the given argument.
     */
    public boolean contains(Trip toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameTrip);
    }

    /**
     * Adds a trip to the list.
     * The trip must not already exist in the list.
     */
    public void add(Trip toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTripException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the trip {@code target} in the list with {@code editedTrip}.
     * {@code target} must exist in the list.
     * The trip identity of {@code editedTrip} must not be the same as another existing trip in the list.
     */
    public void setTrip(Trip target, Trip editedTrip) {
        requireAllNonNull(target, editedTrip);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TripNotFoundException();
        }

        if (!target.isSameTrip(editedTrip) && contains(editedTrip)) {
            throw new DuplicateTripException();
        }

        internalList.set(index, editedTrip);
    }

    /**
     * Removes the equivalent trip from the list.
     * The trip must exist in the list.
     */
    public void remove(Trip toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new TripNotFoundException();
        }
        assert !contains(toRemove);
    }

    public void setTrips(UniqueTripList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code trips}.
     * {@code trips} must not contain duplicate trips.
     */
    public void setTrips(List<Trip> trips) {
        requireAllNonNull(trips);
        if (!tripsAreUnique(trips)) {
            throw new DuplicateTripException();
        }

        internalList.setAll(trips);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Trip> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Trip> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueTripList)) {
            return false;
        }

        UniqueTripList otherUniqueTripList = (UniqueTripList) other;
        return internalList.equals(otherUniqueTripList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code trips} contains only unique trips.
     */
    private boolean tripsAreUnique(List<Trip> trips) {
        for (int i = 0; i < trips.size() - 1; i++) {
            for (int j = i + 1; j < trips.size(); j++) {
                if (trips.get(i).isSameTrip(trips.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
