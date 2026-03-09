package seedu.triplog.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalTrips.ALICE;
import static seedu.triplog.testutil.TypicalTrips.BOB;

import org.junit.jupiter.api.Test;

import seedu.triplog.testutil.TripBuilder;

public class TripTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Trip trip = new TripBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> trip.getTags().remove(0));
    }

    @Test
    public void isSameTrip() {
        // same object -> returns true
        assertTrue(ALICE.isSameTrip(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameTrip(null));

        // same name, all other attributes different -> returns true
        Trip editedAlice = new TripBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameTrip(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new TripBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameTrip(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Trip editedBob = new TripBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSameTrip(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new TripBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameTrip(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Trip aliceCopy = new TripBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different trip -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Trip editedAlice = new TripBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new TripBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new TripBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new TripBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new TripBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void hashCode_test() {
        Trip aliceCopy = new TripBuilder(ALICE).build();
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());
        assertNotEquals(ALICE.hashCode(), BOB.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = Trip.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress()
                + ", startDate=" + ALICE.getStartDate() + ", endDate=" + ALICE.getEndDate()
                + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
