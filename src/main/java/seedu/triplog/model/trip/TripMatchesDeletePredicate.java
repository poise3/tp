package seedu.triplog.model.trip;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import seedu.triplog.model.tag.Tag;

/**
 * Tests whether a {@code Trip} matches the given delete criteria.
 *
 * Different field types are matched using AND logic.
 * Multiple tags are matched using OR logic.
 */
public class TripMatchesDeletePredicate implements Predicate<Trip> {

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final TripDate startDate;
    private final TripDate endDate;
    private final Set<Tag> tags;

    /**
     * Creates a predicate with the given delete criteria.
     *
     * @param name The name criterion.
     * @param phone The phone criterion.
     * @param email The email criterion.
     * @param address The address criterion.
     * @param startDate The start date criterion.
     * @param endDate The end date criterion.
     * @param tags The tag criteria.
     */
    public TripMatchesDeletePredicate(Name name, Phone phone, Email email, Address address,
                                      TripDate startDate, TripDate endDate, Set<Tag> tags) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags == null ? new HashSet<>() : new HashSet<>(tags);
    }

    /**
     * Evaluates whether the given {@code Trip} matches the criteria.
     *
     * @param trip The trip to test.
     * @return true if the trip matches all criteria, false otherwise.
     */
    @Override
    public boolean test(Trip trip) {
        requireNonNull(trip);
        return matchesName(trip)
                && matchesPhone(trip)
                && matchesEmail(trip)
                && matchesAddress(trip)
                && matchesDateCriteria(trip)
                && matchesTags(trip);
    }

    /**
     * Returns true if the trip matches the name criterion.
     *
     * @param trip The trip to check.
     * @return true if the name matches.
     */
    private boolean matchesName(Trip trip) {
        return name == null || name.fullName.equalsIgnoreCase(trip.getName().fullName);
    }

    /**
     * Returns true if the trip matches the phone criterion.
     *
     * @param trip The trip to check.
     * @return true if the phone matches.
     */
    private boolean matchesPhone(Trip trip) {
        return phone == null || Objects.equals(phone, trip.getPhone());
    }

    /**
     * Returns true if the trip matches the email criterion.
     *
     * @param trip The trip to check.
     * @return true if the email matches.
     */
    private boolean matchesEmail(Trip trip) {
        return email == null
                || (trip.getEmail() != null
                && email.value.equalsIgnoreCase(trip.getEmail().value));
    }

    /**
     * Returns true if the trip matches the address criterion.
     *
     * @param trip The trip to check.
     * @return true if the address matches.
     */
    private boolean matchesAddress(Trip trip) {
        return address == null
                || (trip.getAddress() != null
                && address.value.equalsIgnoreCase(trip.getAddress().value));
    }

    /**
     * Returns true if the trip matches the date criterion.
     * If both startDate and endDate are provided, the trip must fall within the range.
     * Otherwise, individual dates are matched exactly.
     *
     * @param trip The trip to check.
     * @return true if the date criteria match.
     */
    private boolean matchesDateCriteria(Trip trip) {
        if (startDate != null && endDate != null) {
            return matchesDateRange(trip);
        }
        return matchesExactStartDate(trip) && matchesExactEndDate(trip);
    }

    /**
     * Returns true if the trip falls within the specified date range.
     *
     * @param trip The trip to check.
     * @return true if within the date range.
     */
    private boolean matchesDateRange(Trip trip) {
        if (trip.getStartDate() == null || trip.getEndDate() == null) {
            return false;
        }
        if (startDate.value.equals(endDate.value)) {
            return !trip.getEndDate().value.isBefore(startDate.value)
                    && !trip.getStartDate().value.isAfter(endDate.value);
        }
        return !trip.getStartDate().value.isBefore(startDate.value)
                && !trip.getEndDate().value.isAfter(endDate.value);
    }

    /**
     * Returns true if the trip matches the exact start date criterion.
     *
     * @param trip The trip to check.
     * @return true if the start date matches.
     */
    private boolean matchesExactStartDate(Trip trip) {
        return startDate == null || Objects.equals(startDate, trip.getStartDate());
    }

    /**
     * Returns true if the trip matches the exact end date criterion.
     *
     * @param trip The trip to check.
     * @return true if the end date matches.
     */
    private boolean matchesExactEndDate(Trip trip) {
        return endDate == null || Objects.equals(endDate, trip.getEndDate());
    }

    /**
     * Returns true if the trip matches the tag criterion.
     *
     * @param trip The trip to check.
     * @return true if any tag matches.
     */
    private boolean matchesTags(Trip trip) {
        return tags.isEmpty() || trip.getTags().stream().anyMatch(tags::contains);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TripMatchesDeletePredicate)) {
            return false;
        }

        TripMatchesDeletePredicate otherPredicate = (TripMatchesDeletePredicate) other;
        return Objects.equals(name, otherPredicate.name)
                && Objects.equals(phone, otherPredicate.phone)
                && Objects.equals(email, otherPredicate.email)
                && Objects.equals(address, otherPredicate.address)
                && Objects.equals(startDate, otherPredicate.startDate)
                && Objects.equals(endDate, otherPredicate.endDate)
                && Objects.equals(tags, otherPredicate.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, startDate, endDate, tags);
    }

    @Override
    public String toString() {
        return "TripMatchesDeletePredicate{"
                + "name=" + name
                + ", phone=" + phone
                + ", email=" + email
                + ", address=" + address
                + ", startDate=" + startDate
                + ", endDate=" + endDate
                + ", tags=" + tags
                + '}';
    }
}

