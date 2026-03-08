package seedu.triplog.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.triplog.model.person.Address;
import seedu.triplog.model.person.Email;
import seedu.triplog.model.person.Name;
import seedu.triplog.model.person.Phone;
import seedu.triplog.model.person.Trip;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.util.SampleDataUtil;

/**
 * A utility class to help with building Trip objects.
 */
public class TripBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_START_DATE = "2026-01-01";
    public static final String DEFAULT_END_DATE = "2026-01-10";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private String startDate;
    private String endDate;
    private Set<Tag> tags;

    /**
     * Creates a {@code TripBuilder} with the default details.
     */
    public TripBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        startDate = DEFAULT_START_DATE;
        endDate = DEFAULT_END_DATE;
        tags = new HashSet<>();
    }

    /**
     * Initializes the TripBuilder with the data of {@code tripToCopy}.
     */
    public TripBuilder(Trip tripToCopy) {
        name = tripToCopy.getName();
        phone = tripToCopy.getPhone();
        email = tripToCopy.getEmail();
        address = tripToCopy.getAddress();
        startDate = tripToCopy.getStartDate().toString();
        endDate = tripToCopy.getEndDate().toString();
        tags = new HashSet<>(tripToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Trip} that we are building.
     */
    public TripBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Trip} that we are building.
     */
    public TripBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Trip} that we are building.
     */
    public TripBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Trip} that we are building.
     */
    public TripBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Trip} that we are building.
     */
    public TripBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code startDate} of the {@code Trip} being built.
     */
    public TripBuilder withStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * Sets the {@code endDate} of the {@code Trip} being built.
     */
    public TripBuilder withEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public Trip build() {
        return new Trip(name, phone, email, address, tags);
    }
}
