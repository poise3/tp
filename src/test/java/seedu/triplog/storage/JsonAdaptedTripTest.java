package seedu.triplog.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static seedu.triplog.storage.JsonAdaptedTrip.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalTrips.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.triplog.commons.exceptions.IllegalValueException;
import seedu.triplog.model.trip.Address;
import seedu.triplog.model.trip.Email;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.Phone;
import seedu.triplog.model.trip.Trip;

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
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final String VALID_START_DATE = BENSON.getStartDate().toString();
    private static final String VALID_END_DATE = BENSON.getEndDate().toString();

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        // EP: valid inputs
        JsonAdaptedTrip person = new JsonAdaptedTrip(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        // EP: invalid name format
        JsonAdaptedTrip person =
                new JsonAdaptedTrip(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_START_DATE, VALID_END_DATE);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        // EP: missing compulsory field (null)
        JsonAdaptedTrip person = new JsonAdaptedTrip(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_START_DATE, VALID_END_DATE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT,
                Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        // EP: invalid phone format
        JsonAdaptedTrip person =
                new JsonAdaptedTrip(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_START_DATE, VALID_END_DATE);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage,
                person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        // EP: invalid email format
        JsonAdaptedTrip person =
                new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_START_DATE, VALID_END_DATE);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        // EP: invalid address format
        JsonAdaptedTrip person =
                new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                        VALID_TAGS, VALID_START_DATE, VALID_END_DATE);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        // EP: list containing invalid tag format
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedTrip person =
                new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        invalidTags, VALID_START_DATE, VALID_END_DATE);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidDateOrder_throwsIllegalValueException() {
        // EP: logical error in persisted data (Start > End)
        String startDate = "2026-12-31";
        String endDate = "2026-01-01";
        JsonAdaptedTrip trip = new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, startDate, endDate);
        assertThrows(IllegalValueException.class, Trip.MESSAGE_INVALID_DATE_ORDER, trip::toModelType);
    }

    @Test
    public void toModelType_invalidStartDate_throwsIllegalValueException() {
        // EP: invalid date string format
        JsonAdaptedTrip trip = new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, "not-a-date", VALID_END_DATE);
        assertThrows(IllegalValueException.class, trip::toModelType);
    }

    @Test
    public void toModelType_invalidEndDate_throwsIllegalValueException() {
        // BVA: date value at logical maximum but formatted incorrectly for the parser
        JsonAdaptedTrip trip = new JsonAdaptedTrip(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_START_DATE, "2026-99-99");
        assertThrows(IllegalValueException.class, trip::toModelType);
    }

    @Test
    public void toModelType_nullOptionalFields_modelSetsNull() throws Exception {
        // EP: all optional fields are missing (null)
        JsonAdaptedTrip tripWithNulls =
                new JsonAdaptedTrip(VALID_NAME, null, null, null, VALID_TAGS,
                        null, null);
        Trip modelTrip = tripWithNulls.toModelType();

        // optional fields should be null
        assertNull(modelTrip.getPhone());
        assertNull(modelTrip.getEmail());
        assertNull(modelTrip.getAddress());
        assertNull(modelTrip.getStartDate());
        assertNull(modelTrip.getEndDate());
    }
}
