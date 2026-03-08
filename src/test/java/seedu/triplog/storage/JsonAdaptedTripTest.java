package seedu.triplog.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.triplog.storage.JsonAdaptedTrip.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalTrips.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.triplog.commons.exceptions.IllegalValueException;
import seedu.triplog.model.person.Address;
import seedu.triplog.model.person.Email;
import seedu.triplog.model.person.Name;
import seedu.triplog.model.person.Phone;

public class JsonAdaptedTripTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_START_DATE = BENSON.getStartDate().toString();
    private static final String VALID_END_DATE = BENSON.getEndDate().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedTrip person = new JsonAdaptedTrip(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedTrip person =
                new JsonAdaptedTrip(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_START_DATE, VALID_END_DATE, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedTrip person = new JsonAdaptedTrip(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_START_DATE, VALID_END_DATE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedTrip person =
                new JsonAdaptedTrip(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_START_DATE, VALID_END_DATE, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedTrip person = new JsonAdaptedTrip(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS,
                VALID_START_DATE, VALID_END_DATE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedTrip person =
                new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        VALID_START_DATE, VALID_END_DATE, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedTrip person = new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                VALID_START_DATE, VALID_END_DATE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedTrip person =
                new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                        VALID_START_DATE, VALID_END_DATE, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedTrip person = new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_START_DATE, VALID_END_DATE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullDates_throwsIllegalValueException() {
        JsonAdaptedTrip personNullStart = new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, null, VALID_END_DATE, VALID_TAGS);
        assertThrows(IllegalValueException.class, personNullStart::toModelType);

        JsonAdaptedTrip personNullEnd = new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_START_DATE, null, VALID_TAGS);
        assertThrows(IllegalValueException.class, personNullEnd::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedTrip person =
                new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_START_DATE, VALID_END_DATE, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
