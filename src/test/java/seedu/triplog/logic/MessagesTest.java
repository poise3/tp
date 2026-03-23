package seedu.triplog.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.testutil.TypicalTrips.ALICE;

import org.junit.jupiter.api.Test;

import seedu.triplog.model.trip.Trip;
import seedu.triplog.testutil.TripBuilder;

public class MessagesTest {

    @Test
    public void format_tripWithAllFields_success() {
        Trip trip = ALICE;
        String expected = Messages.format(trip);

        // simply checks if formatting succeeds with all fields
        assertEquals(expected, Messages.format(trip));
    }

    @Test
    public void format_tripWithMissingOptionalFields_success() {
        Trip trip = new TripBuilder(ALICE)
                .withPhone(null)
                .withEmail(null)
                .withAddress(null)
                .withStart(null)
                .withEnd(null)
                .build();

        String formatted = Messages.format(trip);

        // should still contain name
        assertTrue(formatted.contains(trip.getName().toString()));

        // ensure optional fields are not present
        assertFalse(formatted.contains("Phone:"));
        assertFalse(formatted.contains("Email:"));
        assertFalse(formatted.contains("Address:"));
        assertFalse(formatted.contains("Start Date:"));
        assertFalse(formatted.contains("End Date:"));
    }
}
