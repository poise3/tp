package seedu.triplog.model.trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.triplog.model.tag.Tag;

public class TripMatchesDeletePredicateTest {

    @Test
    public void test_dateRangeTripWithinRange_returnsTrue() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, null,
                new TripDate("2026-03-01"),
                new TripDate("2026-03-10"),
                Set.of());

        Trip trip = new Trip(
                new Name("Tokyo"),
                null,
                null,
                new Address("Shibuya"),
                Set.of(new Tag("food")),
                new TripDate("2026-03-02"),
                new TripDate("2026-03-08"));

        assertTrue(predicate.test(trip));
    }

    @Test
    public void test_dateRangeTripWithoutDates_returnsFalse() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, null,
                new TripDate("2026-03-01"),
                new TripDate("2026-03-10"),
                Set.of());

        Trip trip = new Trip(
                new Name("Planning Trip"),
                null,
                null,
                new Address("No address"),
                Set.of(new Tag("plan")),
                null,
                null);

        assertFalse(predicate.test(trip));
    }

    @Test
    public void test_dateRangeTripStartBeforeRange_returnsFalse() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, null,
                new TripDate("2026-03-01"),
                new TripDate("2026-03-10"),
                Set.of());

        Trip trip = new Trip(
                new Name("Early Trip"),
                null,
                null,
                new Address("Somewhere"),
                Set.of(),
                new TripDate("2026-02-28"),
                new TripDate("2026-03-05"));

        assertFalse(predicate.test(trip));
    }

    @Test
    public void test_dateRangeTripEndAfterRange_returnsFalse() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, null,
                new TripDate("2026-03-01"),
                new TripDate("2026-03-10"),
                Set.of());

        Trip trip = new Trip(
                new Name("Late Trip"),
                null,
                null,
                new Address("Somewhere"),
                Set.of(),
                new TripDate("2026-03-02"),
                new TripDate("2026-03-12"));

        assertFalse(predicate.test(trip));
    }

    @Test
    public void test_tagMatchOverlap_returnsTrue() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, null,
                null, null,
                Set.of(new Tag("family")));

        Trip trip = new Trip(
                new Name("Family Trip"),
                null,
                null,
                new Address("Home"),
                Set.of(new Tag("family"), new Tag("fun")),
                null,
                null);

        assertTrue(predicate.test(trip));
    }

    @Test
    public void test_tagMatchNoOverlap_returnsFalse() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, null,
                null, null,
                Set.of(new Tag("family")));

        Trip trip = new Trip(
                new Name("Work Trip"),
                null,
                null,
                new Address("Office"),
                Set.of(new Tag("business")),
                null,
                null);

        assertFalse(predicate.test(trip));
    }

    @Test
    public void test_phoneMismatch_returnsFalse() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, new Phone("91234567"), null, null,
                null, null, Set.of());

        Trip trip = new Trip(
                new Name("Tokyo"),
                new Phone("98765432"),
                null,
                new Address("Shibuya"),
                Set.of(),
                null,
                null);

        assertFalse(predicate.test(trip));
    }

    @Test
    public void test_emailMismatch_returnsFalse() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, new Email("a@example.com"), null,
                null, null, Set.of());

        Trip trip = new Trip(
                new Name("Tokyo"),
                null,
                new Email("b@example.com"),
                new Address("Shibuya"),
                Set.of(),
                null,
                null);

        assertFalse(predicate.test(trip));
    }

    @Test
    public void test_addressMismatch_returnsFalse() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, new Address("Japan"),
                null, null, Set.of());

        Trip trip = new Trip(
                new Name("Tokyo"),
                null,
                null,
                new Address("Korea"),
                Set.of(),
                null,
                null);

        assertFalse(predicate.test(trip));
    }

    @Test
    public void test_exactStartDateMatch_returnsTrue() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, null,
                new TripDate("2026-03-01"),
                null,
                Set.of());

        Trip trip = new Trip(
                new Name("Start Match"),
                null,
                null,
                new Address("Somewhere"),
                Set.of(),
                new TripDate("2026-03-01"),
                new TripDate("2026-03-05"));

        assertTrue(predicate.test(trip));
    }

    @Test
    public void test_exactStartDateMismatch_returnsFalse() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, null,
                new TripDate("2026-03-01"),
                null,
                Set.of());

        Trip trip = new Trip(
                new Name("Start Mismatch"),
                null,
                null,
                new Address("Somewhere"),
                Set.of(),
                new TripDate("2026-03-02"),
                new TripDate("2026-03-05"));

        assertFalse(predicate.test(trip));
    }

    @Test
    public void test_exactEndDateMatch_returnsTrue() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, null,
                null,
                new TripDate("2026-03-10"),
                Set.of());

        Trip trip = new Trip(
                new Name("End Match"),
                null,
                null,
                new Address("Somewhere"),
                Set.of(),
                new TripDate("2026-03-01"),
                new TripDate("2026-03-10"));

        assertTrue(predicate.test(trip));
    }

    @Test
    public void test_exactEndDateMismatch_returnsFalse() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                null, null, null, null,
                null,
                new TripDate("2026-03-10"),
                Set.of());

        Trip trip = new Trip(
                new Name("End Mismatch"),
                null,
                null,
                new Address("Somewhere"),
                Set.of(),
                new TripDate("2026-03-01"),
                new TripDate("2026-03-09"));

        assertFalse(predicate.test(trip));
    }

    @Test
    public void test_equalsSameObject_returnsTrue() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                new Name("Tokyo"), null, null, null, null, null, Set.of());

        assertTrue(predicate.equals(predicate));
    }

    @Test
    public void test_equalsNonPredicate_returnsFalse() {
        TripMatchesDeletePredicate predicate = new TripMatchesDeletePredicate(
                new Name("Tokyo"), null, null, null, null, null, Set.of());

        assertFalse(predicate.equals(1));
    }

    @Test
    public void test_equalsSameValues_returnsTrue() {
        TripMatchesDeletePredicate first = new TripMatchesDeletePredicate(
                new Name("Tokyo"),
                new Phone("91234567"),
                new Email("tokyo@example.com"),
                new Address("Shibuya"),
                new TripDate("2026-03-01"),
                new TripDate("2026-03-05"),
                Set.of(new Tag("food")));

        TripMatchesDeletePredicate second = new TripMatchesDeletePredicate(
                new Name("Tokyo"),
                new Phone("91234567"),
                new Email("tokyo@example.com"),
                new Address("Shibuya"),
                new TripDate("2026-03-01"),
                new TripDate("2026-03-05"),
                Set.of(new Tag("food")));

        assertTrue(first.equals(second));
    }

    @Test
    public void test_equalsDifferentName_returnsFalse() {
        TripMatchesDeletePredicate first = new TripMatchesDeletePredicate(
                new Name("Tokyo"), null, null, null, null, null, Set.of());
        TripMatchesDeletePredicate second = new TripMatchesDeletePredicate(
                new Name("Seoul"), null, null, null, null, null, Set.of());

        assertFalse(first.equals(second));
    }

    @Test
    public void test_hashCodeSameValues_returnsSameHashCode() {
        TripMatchesDeletePredicate first = new TripMatchesDeletePredicate(
                new Name("Tokyo"),
                new Phone("91234567"),
                new Email("tokyo@example.com"),
                new Address("Shibuya"),
                new TripDate("2026-03-01"),
                new TripDate("2026-03-05"),
                Set.of(new Tag("food")));

        TripMatchesDeletePredicate second = new TripMatchesDeletePredicate(
                new Name("Tokyo"),
                new Phone("91234567"),
                new Email("tokyo@example.com"),
                new Address("Shibuya"),
                new TripDate("2026-03-01"),
                new TripDate("2026-03-05"),
                Set.of(new Tag("food")));

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void test_hashCodeDifferentValues_returnsDifferentHashCode() {
        TripMatchesDeletePredicate first = new TripMatchesDeletePredicate(
                new Name("Tokyo"), null, null, null, null, null, Set.of());
        TripMatchesDeletePredicate second = new TripMatchesDeletePredicate(
                new Name("Seoul"), null, null, null, null, null, Set.of());

        assertNotEquals(first.hashCode(), second.hashCode());
    }
}
