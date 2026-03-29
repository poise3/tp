package seedu.triplog.model.trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalTrips.ALICE;

import org.junit.jupiter.api.Test;

public class UniqueTripListTest {

    private final UniqueTripList uniqueTripList = new UniqueTripList();

    @Test
    public void contains_nullTrip_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTripList.contains(null));
    }

    @Test
    public void contains_tripInList_returnsTrue() {
        uniqueTripList.add(ALICE);
        assertTrue(uniqueTripList.contains(ALICE));
    }

    @Test
    public void remove_existingTrip_removesTrip() {
        uniqueTripList.add(ALICE);
        assertTrue(uniqueTripList.contains(ALICE));
        uniqueTripList.remove(ALICE);

        UniqueTripList expectedUniqueTripList = new UniqueTripList();
        assertEquals(expectedUniqueTripList, uniqueTripList);
        assertFalse(uniqueTripList.contains(ALICE));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueTripList.asUnmodifiableObservableList().remove(0));
    }
}
