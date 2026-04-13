package seedu.triplog.logic.parser;

import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.Messages.MESSAGE_UNKNOWN_PREFIXES;
import static seedu.triplog.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.END_DATE_DESC_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.triplog.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.triplog.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.triplog.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.triplog.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.triplog.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.triplog.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.triplog.logic.commands.CommandTestUtil.START_DATE_DESC_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.triplog.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_END_DATE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_START_DATE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.triplog.model.trip.Trip.MAX_NAME_LENGTH;
import static seedu.triplog.model.trip.Trip.MESSAGE_INVALID_DATE_ORDER;
import static seedu.triplog.model.trip.Trip.MESSAGE_INVALID_NAME_LENGTH;
import static seedu.triplog.testutil.TypicalTrips.AMY;
import static seedu.triplog.testutil.TypicalTrips.BOB;

import org.junit.jupiter.api.Test;

import seedu.triplog.logic.Messages;
import seedu.triplog.logic.commands.AddCommand;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Address;
import seedu.triplog.model.trip.Email;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.Phone;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripDate;
import seedu.triplog.testutil.TripBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Trip expectedTrip = new TripBuilder(BOB)
                .withTags(VALID_TAG_FRIEND)
                .withStart(VALID_START_DATE_BOB)
                .withEnd(VALID_END_DATE_BOB)
                .build();

        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + START_DATE_DESC_BOB + END_DATE_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedTrip));

        Trip expectedTripMultipleTags = new TripBuilder(BOB)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .withStart(VALID_START_DATE_BOB)
                .withEnd(VALID_END_DATE_BOB)
                .build();

        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + START_DATE_DESC_BOB + END_DATE_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedTripMultipleTags));
    }

    @Test
    public void parse_unknownPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_UNKNOWN_PREFIXES);

        // unknown prefix alongside valid prefix
        assertParseFailure(parser, NAME_DESC_BOB + " s/unknown", expectedMessage);

        // unknown prefix only
        assertParseFailure(parser, " s/unknown", expectedMessage);
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Trip expectedTrip = new TripBuilder(AMY).withPhone(null).withEmail(null).withAddress(null)
                                                .withStart(null).withEnd(null).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY,
                new AddCommand(expectedTrip));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDateOrder_failure() {
        String startDateLater = " " + PREFIX_START_DATE + "2025-12-31";
        String endDateLater = " " + PREFIX_END_DATE + "2025-01-01";

        // start date is after end date
        assertParseFailure(parser,
                NAME_DESC_BOB + startDateLater + endDateLater,
                MESSAGE_INVALID_DATE_ORDER);
    }

    @Test
    public void parse_addCommandNameTooLong_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_NAME + "a".repeat(MAX_NAME_LENGTH + 1),
                MESSAGE_INVALID_NAME_LENGTH);
    }

    @Test
    public void parse_addCommandInvalidDate_throwsParseException() {
        String invalidStartDate = " " + PREFIX_START_DATE + "0000-12-31";
        assertParseFailure(parser, NAME_DESC_BOB + invalidStartDate, TripDate.MESSAGE_CONSTRAINTS);
    }
}
