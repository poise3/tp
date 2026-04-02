package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.triplog.model.Model.PREDICATE_SHOW_ALL_TRIPS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.triplog.commons.core.index.Index;
import seedu.triplog.commons.util.CollectionUtil;
import seedu.triplog.commons.util.ToStringBuilder;
import seedu.triplog.logic.Messages;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Address;
import seedu.triplog.model.trip.Email;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.Phone;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripDate;


/**
 * Edits the details of an existing trip in the trip log.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the trip identified "
            + "by the index number used in the displayed trip list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_TRIP_SUCCESS = "Edited Trip: %1$s\n%2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TRIP = "This trip already exists in the trip log.";
    public static final String MESSAGE_NO_CHANGES =
            "Edited fields are the same as the original.";
    private final Index index;
    private final EditTripDescriptor editTripDescriptor;

    /**
     * @param index of the trip in the filtered trip list to edit
     * @param editTripDescriptor details to edit the trip with
     */
    public EditCommand(Index index, EditTripDescriptor editTripDescriptor) {
        requireNonNull(index);
        requireNonNull(editTripDescriptor);

        this.index = index;
        this.editTripDescriptor = new EditTripDescriptor(editTripDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Trip> lastShownList = model.getFilteredTripList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TRIP_DISPLAYED_INDEX);
        }

        Trip tripToEdit = lastShownList.get(index.getZeroBased());
        Trip editedTrip = createEditedTrip(tripToEdit, editTripDescriptor);

        if (tripToEdit.equals(editedTrip)) {
            throw new CommandException(MESSAGE_NO_CHANGES);
        }

        if (model.hasTripExcluding(editedTrip, tripToEdit)) {
            throw new CommandException(MESSAGE_DUPLICATE_TRIP);
        }

        model.setTrip(tripToEdit, editedTrip);
        model.updateFilteredTripList(PREDICATE_SHOW_ALL_TRIPS);

        String summary = TripSummaryUtil.calculateSummary(model.getFilteredTripList());
        return new CommandResult(String.format(MESSAGE_EDIT_TRIP_SUCCESS, Messages.format(editedTrip), summary));
    }

    /**
     * Creates and returns a {@code Trip} with the details of {@code tripToEdit}
     * edited with {@code editTripDescriptor}.
     */
    private static Trip createEditedTrip(Trip tripToEdit, EditTripDescriptor editTripDescriptor)
            throws CommandException {
        assert tripToEdit != null;

        Name updatedName = editTripDescriptor.getName().orElse(tripToEdit.getName());
        Phone updatedPhone = editTripDescriptor.getPhone().orElse(tripToEdit.getPhone());
        Email updatedEmail = editTripDescriptor.getEmail().orElse(tripToEdit.getEmail());
        Address updatedAddress = editTripDescriptor.getAddress().orElse(tripToEdit.getAddress());
        Set<Tag> updatedTags = editTripDescriptor.getTags().orElse(tripToEdit.getTags());
        TripDate updatedStartDate = editTripDescriptor.getStartDate().orElse(tripToEdit.getStartDate());
        TripDate updatedEndDate = editTripDescriptor.getEndDate().orElse(tripToEdit.getEndDate());

        Trip editedTrip;
        try {
            editedTrip = new Trip(updatedName, updatedPhone, updatedEmail, updatedAddress,
                    updatedTags, updatedStartDate, updatedEndDate);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }

        return editedTrip;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editTripDescriptor.equals(otherEditCommand.editTripDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editTripDescriptor", editTripDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the trip with. Each non-empty field value will replace the
     * corresponding field value of the trip.
     */
    public static class EditTripDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;
        private TripDate startDate;
        private TripDate endDate;

        public EditTripDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditTripDescriptor(EditTripDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
            setStartDate(toCopy.startDate);
            setEndDate(toCopy.endDate);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags, startDate, endDate);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public void setStartDate(TripDate startDate) {
            this.startDate = startDate;
        }

        public Optional<TripDate> getStartDate() {
            return Optional.ofNullable(startDate);
        }

        public void setEndDate(TripDate endDate) {
            this.endDate = endDate;
        }

        public Optional<TripDate> getEndDate() {
            return Optional.ofNullable(endDate);
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditTripDescriptor)) {
                return false;
            }

            EditTripDescriptor otherEditTripDescriptor = (EditTripDescriptor) other;
            return Objects.equals(name, otherEditTripDescriptor.name)
                    && Objects.equals(phone, otherEditTripDescriptor.phone)
                    && Objects.equals(email, otherEditTripDescriptor.email)
                    && Objects.equals(address, otherEditTripDescriptor.address)
                    && Objects.equals(tags, otherEditTripDescriptor.tags)
                    && Objects.equals(startDate, otherEditTripDescriptor.startDate)
                    && Objects.equals(endDate, otherEditTripDescriptor.endDate);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("tags", tags)
                    .add("startDate", startDate)
                    .add("endDate", endDate)
                    .toString();
        }
    }
}
