package seedu.triplog.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.triplog.logic.commands.EditCommand.EditTripDescriptor;
import seedu.triplog.model.person.*;
import seedu.triplog.model.tag.Tag;

/**
 * A utility class to help with building EditTripDescriptor objects.
 */
public class EditTripDescriptorBuilder {

    private EditTripDescriptor descriptor;

    public EditTripDescriptorBuilder() {
        descriptor = new EditTripDescriptor();
    }

    public EditTripDescriptorBuilder(EditTripDescriptor descriptor) {
        this.descriptor = new EditTripDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditTripDescriptor} with fields containing {@code trip}'s details
     */
    public EditTripDescriptorBuilder(Trip trip) {
        descriptor = new EditTripDescriptor();
        descriptor.setName(trip.getName());
        descriptor.setPhone(trip.getPhone());
        descriptor.setEmail(trip.getEmail());
        descriptor.setAddress(trip.getAddress());
        descriptor.setTags(trip.getTags());
        descriptor.setStartDate(trip.getStartDate());
        descriptor.setEndDate(trip.getEndDate());
    }

    /**
     * Sets the {@code Name} of the {@code EditTripDescriptor} that we are building.
     */
    public EditTripDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditTripDescriptor} that we are building.
     */
    public EditTripDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditTripDescriptor} that we are building.
     */
    public EditTripDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditTripDescriptor} that we are building.
     */
    public EditTripDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditTripDescriptor}
     * that we are building.
     */
    public EditTripDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditTripDescriptor build() {
        return descriptor;
    }

    public EditTripDescriptorBuilder withStart(String date) {
        descriptor.setStartDate(new TripDate(date));
        return this;
    }

    public EditTripDescriptorBuilder withEnd(String date) {
        descriptor.setEndDate(new TripDate(date));
        return this;
    }
}
