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

    @Override
    public boolean test(Trip trip) {
        requireNonNull(trip);

        if (name != null && !name.equals(trip.getName())) {
            return false;
        }
        if (phone != null && !Objects.equals(phone, trip.getPhone())) {
            return false;
        }
        if (email != null && !Objects.equals(email, trip.getEmail())) {
            return false;
        }
        if (address != null && !Objects.equals(address, trip.getAddress())) {
            return false;
        }
        if (startDate != null && !Objects.equals(startDate, trip.getStartDate())) {
            return false;
        }
        if (endDate != null && !Objects.equals(endDate, trip.getEndDate())) {
            return false;
        }
        if (!tags.isEmpty() && trip.getTags().stream().noneMatch(tags::contains)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
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
