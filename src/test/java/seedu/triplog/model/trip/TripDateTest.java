package seedu.triplog.model.trip;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.triplog.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TripDateTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TripDate(null));
    }

    @Test
    public void constructor_invalidFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TripDate("2026/01/01"));
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        // invalid calendar date (Feb 30 does not exist)
        assertThrows(IllegalArgumentException.class, () -> new TripDate("2026-02-30"));
    }

    @Test
    public void constructor_validDate_success() {
        TripDate date = new TripDate("2026-01-01");
        assertEquals("2026-01-01", date.toString());
    }

    @Test
    public void isValidDate() {
        // null input
        assertThrows(NullPointerException.class, () -> TripDate.isValidDate(null));

        // invalid formats
        assertFalse(TripDate.isValidDate("2026/01/01"));
        assertFalse(TripDate.isValidDate("01-01-2026"));
        assertFalse(TripDate.isValidDate("20260101"));

        // invalid calendar date
        assertFalse(TripDate.isValidDate("2026-02-30"));

        // valid dates
        assertTrue(TripDate.isValidDate("2026-01-01"));
        assertTrue(TripDate.isValidDate("2024-02-29")); // leap year
    }

    @Test
    public void equals() {
        TripDate date1 = new TripDate("2026-01-01");
        TripDate date2 = new TripDate("2026-01-01");
        TripDate date3 = new TripDate("2026-12-31");

        // same object
        assertEquals(date1, date1);

        // same values
        assertEquals(date1, date2);

        // different values
        assertNotEquals(date1, date3);

        // null
        assertNotEquals(null, date1);

        // different type
        assertNotEquals(5, date1);
    }

    @Test
    public void hashCode_sameValue_sameHash() {
        TripDate date1 = new TripDate("2026-01-01");
        TripDate date2 = new TripDate("2026-01-01");

        assertEquals(date1.hashCode(), date2.hashCode());
    }

    @Test
    public void toString_validDate_correctFormat() {
        TripDate date = new TripDate("2026-01-01");
        assertEquals("2026-01-01", date.toString());
    }
}
