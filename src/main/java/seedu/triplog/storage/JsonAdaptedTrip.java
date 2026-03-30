package seedu.triplog.storage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.triplog.commons.exceptions.IllegalValueException;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Address;
import seedu.triplog.model.trip.Email;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.Phone;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripDate;


/**
 * Jackson-friendly version of {@link Trip}.
 */
class JsonAdaptedTrip {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Trip's %s field is missing!";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String startDate;
    private final String endDate;

    /**
     * Constructs a {@code JsonAdaptedTrip} with the given trip details.
     */
    @JsonCreator
    public JsonAdaptedTrip(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                           @JsonProperty("email") String email, @JsonProperty("address") String address,
                           @JsonProperty("tags") List<JsonAdaptedTag> tags, @JsonProperty("startDate") String startDate,
                           @JsonProperty("endDate") String endDate) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Converts a given {@code Trip} into this class for Jackson use.
     */
    public JsonAdaptedTrip(Trip source) {
        name = source.getName().fullName;
        phone = !Objects.isNull(source.getPhone()) ? source.getPhone().value : null;
        email = !Objects.isNull(source.getEmail()) ? source.getEmail().value : null;
        address = !Objects.isNull(source.getAddress()) ? source.getAddress().value : null;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        startDate = !Objects.isNull(source.getStartDate()) ? source.getStartDate().value.toString() : null;
        endDate = !Objects.isNull(source.getEndDate()) ? source.getEndDate().value.toString() : null;
    }

    /**
     * Converts this Jackson-friendly adapted trip object into the model's {@code Trip} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted trip.
     */
    public Trip toModelType() throws IllegalValueException {
        final List<Tag> tripTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            tripTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        final Phone modelPhone;
        if (phone == null) {
            modelPhone = null;
        } else if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        } else {
            modelPhone = new Phone(phone);
        }

        final Email modelEmail;
        if (email == null) {
            modelEmail = null;
        } else if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        } else {
            modelEmail = new Email(email);
        }

        final Address modelAddress;
        if (address == null) {
            modelAddress = null;
        } else if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        } else {
            modelAddress = new Address(address);
        }

        final TripDate modelStartDate;
        if (startDate == null) {
            modelStartDate = null;
        } else if (!TripDate.isValidDate(startDate)) {
            throw new IllegalValueException(TripDate.MESSAGE_CONSTRAINTS);
        } else {
            modelStartDate = new TripDate(startDate);
        }

        final TripDate modelEndDate;
        if (endDate == null) {
            modelEndDate = null;
        } else if (!TripDate.isValidDate(endDate)) {
            throw new IllegalValueException(TripDate.MESSAGE_CONSTRAINTS);
        } else {
            modelEndDate = new TripDate(endDate);
        }

        if (modelStartDate != null && modelEndDate != null && modelStartDate.value.isAfter(modelEndDate.value)) {
            throw new IllegalValueException(Trip.MESSAGE_INVALID_DATE_ORDER);
        }

        final Set<Tag> modelTags = new HashSet<>(tripTags);
        return new Trip(modelName, modelPhone, modelEmail, modelAddress, modelTags,
                modelStartDate, modelEndDate);
    }

}
