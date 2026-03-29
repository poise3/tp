package seedu.triplog.model.trip;

import static seedu.triplog.commons.util.AppUtil.checkArgument;
import static seedu.triplog.commons.util.CollectionUtil.requireAllNonNull;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.triplog.commons.util.ToStringBuilder;
import seedu.triplog.model.tag.Tag;

/**
 * Represents a Trip in the TripLog.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Trip {

    public static final String MESSAGE_INVALID_DATE_ORDER = "Start date cannot be after end date.";

    /**
     * Comparison logic for trips:
     * 1. Sort by start date (earliest first).
     * 2. Trips with no start date are pushed to the end.
     * 3. If dates are equal (or both missing), sort alphabetically by name (case-insensitive).
     */
    public static final Comparator<Trip> CHRONOLOGICAL_COMPARATOR = (trip1, trip2) -> {
        if (trip1.getStartDate() == null && trip2.getStartDate() == null) {
            return trip1.getNameLowerCase().compareTo(trip2.getNameLowerCase());
        }
        if (trip1.getStartDate() == null) {
            return 1;
        }
        if (trip2.getStartDate() == null) {
            return -1;
        }
        int dateComparison = trip1.getStartDate().toString().compareTo(trip2.getStartDate().toString());

        if (dateComparison == 0) {
            return trip1.getNameLowerCase().compareTo(trip2.getNameLowerCase());
        }

        return dateComparison;
    };

    private static final long UNKNOWN_DURATION = -1;

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final TripDate startDate;
    private final TripDate endDate;

    /**
     * Every field must be present and not null.
     */
    public Trip(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                TripDate startDate, TripDate endDate) {
        requireAllNonNull(name, tags);

        if (startDate != null && endDate != null) {
            checkArgument(!startDate.value.isAfter(endDate.value), MESSAGE_INVALID_DATE_ORDER);
        }

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * New constructor for adding of single tag
     * **/
    public Trip(Trip trip, Tag tag) {
        requireAllNonNull(trip, tag);
        this.name = trip.name;
        this.phone = trip.phone;
        this.email = trip.email;
        this.address = trip.address;
        this.startDate = trip.startDate;
        this.endDate = trip.endDate;
        this.tags.addAll(trip.tags);
        this.tags.add(tag);
    }

    public Name getName() {
        return name;
    }

    /**
     * Returns the full name in lower case.
     * Used for case-insensitive sorting and satisfying Law of Demeter.
     */
    public String getNameLowerCase() {
        return name.fullName.toLowerCase();
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public TripDate getStartDate() {
        return startDate;
    }

    public TripDate getEndDate() {
        return endDate;
    }

    /**
     * Calculates the duration of the trip in days.
     * Returns -1 if dates are missing.
     */
    public long getDurationInDays() {
        if (startDate == null || endDate == null) {
            return UNKNOWN_DURATION;
        }
        return ChronoUnit.DAYS.between(startDate.value, endDate.value);
    }

    public String getPhoneDisplay() {
        return phone == null ? null : phone.value;
    }

    public String getEmailDisplay() {
        return email == null ? null : email.value;
    }

    public String getAddressDisplay() {
        return address == null ? null : address.value;
    }

    public String getStartDateDisplay() {
        return startDate == null ? null : startDate.toString();
    }

    public String getEndDateDisplay() {
        return endDate == null ? null : endDate.toString();
    }

    /**
     * Returns true if both trips have the same name.
     * This defines a weaker notion of equality between two trips.
     */
    public boolean isSameTrip(Trip otherTrip) {
        if (otherTrip == this) {
            return true;
        }

        return otherTrip != null
                && otherTrip.getName().equals(getName());
    }

    /**
     * Returns true if both trips have the same identity and data fields.
     * This defines a stronger notion of equality between two trips.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Trip)) {
            return false;
        }
        Trip otherTrip = (Trip) other;

        return name.equals(otherTrip.name)
                && Objects.equals(phone, otherTrip.phone)
                && Objects.equals(email, otherTrip.email)
                && Objects.equals(address, otherTrip.address)
                && Objects.equals(startDate, otherTrip.startDate)
                && Objects.equals(endDate, otherTrip.endDate)
                && tags.equals(otherTrip.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, startDate, endDate, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("tags", tags)
                .toString();
    }
}
