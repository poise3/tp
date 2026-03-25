package seedu.triplog.model.trip;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
}
