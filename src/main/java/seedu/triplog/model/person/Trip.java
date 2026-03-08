package seedu.triplog.model.person;

import static seedu.triplog.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.triplog.logic.parser.AddCommandParser.DEFAULT_END_DATE;
import static seedu.triplog.logic.parser.AddCommandParser.DEFAULT_START_DATE;

import java.time.LocalDate;
import java.util.Collections;
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

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Trip(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.startDate = LocalDate.parse(DEFAULT_START_DATE);
        this.endDate = LocalDate.parse(DEFAULT_END_DATE);
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
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

        // instanceof handles nulls
        if (!(other instanceof Trip)) {
            return false;
        }

        Trip otherTrip = (Trip) other;
        return name.equals(otherTrip.name)
                && phone.equals(otherTrip.phone)
                && email.equals(otherTrip.email)
                && address.equals(otherTrip.address)
                && startDate.equals(otherTrip.startDate)
                && endDate.equals(otherTrip.endDate)
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
