package seedu.triplog.model.trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalTrips.ALICE;
import static seedu.triplog.testutil.TypicalTrips.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.triplog.model.trip.exceptions.DuplicateTripException;
import seedu.triplog.model.trip.exceptions.TripNotFoundException;
import seedu.triplog.testutil.TripBuilder;

public class UniqueTripListTest {

    private final UniqueTripList uniqueTripList = new UniqueTripList();

    @Test
    public void contains_nullTrip_throwsNullPointerException() {
        // EP: null trip
        assertThrows(NullPointerException.class, () -> uniqueTripList.contains(null));
    }

    @Test
    public void contains_tripNotInList_returnsFalse() {
        // EP: trip not in list
        assertFalse(uniqueTripList.contains(ALICE));
    }

    @Test
    public void contains_tripInList_returnsTrue() {
        // EP: trip exists in list
        uniqueTripList.add(ALICE);
        assertTrue(uniqueTripList.contains(ALICE));
    }

    @Test
    public void contains_tripWithSameIdentityFieldsInList_returnsTrue() {
        // EP: trip with same identity (name + overlapping dates) exists
        uniqueTripList.add(ALICE);
        Trip editedAlice = new TripBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueTripList.contains(editedAlice));
    }

    @Test
    public void add_nullTrip_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTripList.add(null));
    }

    @Test
    public void add_duplicateTrip_throwsDuplicateTripException() {
        // EP: adding a trip that already exists
        uniqueTripList.add(ALICE);
        assertThrows(DuplicateTripException.class, () -> uniqueTripList.add(ALICE));
    }

    @Test
    public void setTrip_nullTargetTrip_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTripList.setTrip(null, ALICE));
    }

    @Test
    public void setTrip_nullEditedTrip_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTripList.setTrip(ALICE, null));
    }

    @Test
    public void setTrip_targetTripNotInList_throwsTripNotFoundException() {
        // EP: target trip does not exist in the list
        assertThrows(TripNotFoundException.class, () -> uniqueTripList.setTrip(ALICE, ALICE));
    }

    @Test
    public void add_sameNameNonOverlappingDates_success() {
        // EP: same name but dates do not intersect
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-02-01").withEnd("2026-02-10").build();
        uniqueTripList.add(trip1);
        uniqueTripList.add(trip2);
        assertEquals(2, uniqueTripList.asUnmodifiableObservableList().size());
    }

    @Test
    public void add_sameNameOverlappingDates_throwsDuplicateTripException() {
        // EP: same name and dates overlap
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-01-05").withEnd("2026-01-15").build();
        uniqueTripList.add(trip1);
        assertThrows(DuplicateTripException.class, () -> uniqueTripList.add(trip2));
    }

    @Test
    public void setTrip_editedTripOverlapsWithOtherTrip_throwsDuplicateTripException() {
        // EP: editing results in an overlap with a third trip
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-02-01").withEnd("2026-02-10").build();
        uniqueTripList.add(trip1);
        uniqueTripList.add(trip2);

        // Edit trip2's dates to overlap with trip1
        Trip editedTrip2 = new TripBuilder().withName("Trip").withStart("2026-01-05").withEnd("2026-01-15")
                .build();
        assertThrows(DuplicateTripException.class, () -> uniqueTripList.setTrip(trip2, editedTrip2));
    }

    @Test
    public void setTrip_editedTripIsSameTrip_success() {
        uniqueTripList.add(ALICE);
        uniqueTripList.setTrip(ALICE, ALICE);
        UniqueTripList expectedUniqueTripList = new UniqueTripList();
        expectedUniqueTripList.add(ALICE);
        assertEquals(expectedUniqueTripList, uniqueTripList);
    }

    @Test
    public void setTrip_editedTripHasSameIdentity_success() {
        uniqueTripList.add(ALICE);
        Trip editedAlice = new TripBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueTripList.setTrip(ALICE, editedAlice);
        UniqueTripList expectedUniqueTripList = new UniqueTripList();
        expectedUniqueTripList.add(editedAlice);
        assertEquals(expectedUniqueTripList, uniqueTripList);
    }

    @Test
    public void setTrip_editedTripHasDifferentIdentity_success() {
        uniqueTripList.add(ALICE);
        uniqueTripList.setTrip(ALICE, BOB);
        UniqueTripList expectedUniqueTripList = new UniqueTripList();
        expectedUniqueTripList.add(BOB);
        assertEquals(expectedUniqueTripList, uniqueTripList);
    }

    @Test
    public void setTrip_editedTripHasNonUniqueIdentity_throwsDuplicateTripException() {
        uniqueTripList.add(ALICE);
        uniqueTripList.add(BOB);
        assertThrows(DuplicateTripException.class, () -> uniqueTripList.setTrip(ALICE, BOB));
    }

    @Test
    public void remove_nullTrip_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTripList.remove(null));
    }

    @Test
    public void remove_tripDoesNotExist_throwsTripNotFoundException() {
        // EP: removing a trip not present in list
        assertThrows(TripNotFoundException.class, () -> uniqueTripList.remove(ALICE));
    }

    @Test
    public void remove_existingTrip_removesTrip() {
        // EP: removing a trip present in list
        uniqueTripList.add(ALICE);
        uniqueTripList.remove(ALICE);
        UniqueTripList expectedUniqueTripList = new UniqueTripList();
        assertEquals(expectedUniqueTripList, uniqueTripList);
        assertFalse(uniqueTripList.contains(ALICE)); // exercises post-condition assertions
    }

    @Test
    public void setTrips_nullUniqueTripList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTripList.setTrips((UniqueTripList) null));
    }

    @Test
    public void setTrips_uniqueTripList_replacesOwnListWithProvidedUniqueTripList() {
        uniqueTripList.add(ALICE);
        UniqueTripList expectedUniqueTripList = new UniqueTripList();
        expectedUniqueTripList.add(BOB);
        uniqueTripList.setTrips(expectedUniqueTripList);
        assertEquals(expectedUniqueTripList, uniqueTripList);
    }

    @Test
    public void setTrips_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTripList.setTrips((List<Trip>) null));
    }

    @Test
    public void setTrips_list_replacesOwnListWithProvidedList() {
        uniqueTripList.add(ALICE);
        List<Trip> tripList = Collections.singletonList(BOB);
        uniqueTripList.setTrips(tripList);
        UniqueTripList expectedUniqueTripList = new UniqueTripList();
        expectedUniqueTripList.add(BOB);
        assertEquals(expectedUniqueTripList, uniqueTripList);
    }

    @Test
    public void setTrips_listWithDuplicateTrips_throwsDuplicateTripException() {
        // EP: replacement list contains duplicates
        List<Trip> listWithDuplicateTrips = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicateTripException.class, () -> uniqueTripList.setTrips(listWithDuplicateTrips));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueTripList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueTripList.asUnmodifiableObservableList().toString(), uniqueTripList.toString());
    }
}
