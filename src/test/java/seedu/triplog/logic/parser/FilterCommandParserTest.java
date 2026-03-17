package seedu.triplog.logic.parser;

import static seedu.triplog.logic.Messages.MESSAGE_DUPLICATE_FIELDS;
import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.triplog.logic.commands.FilterCommand;
import seedu.triplog.model.trip.TripDate;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        String userInput = " " + PREFIX_START_DATE + "2026-01-01 "
                + PREFIX_END_DATE + "2026-03-01";

        FilterCommand expectedCommand =
                new FilterCommand(new TripDate("2026-01-01"), new TripDate("2026-03-01"));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingStartDate_failure() {
        String userInput = " " + PREFIX_END_DATE + "2026-03-01";

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingEndDate_failure() {
        String userInput = " " + PREFIX_START_DATE + "2026-01-01";

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDateFormat_failure() {
        String userInput = " " + PREFIX_START_DATE + "invalid-date "
                + PREFIX_END_DATE + "2026-03-01";

        assertParseFailure(parser, userInput, TripDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        String userInput = " "
                + PREFIX_START_DATE + "2026-01-01 "
                + PREFIX_START_DATE + "2026-02-01 "
                + PREFIX_END_DATE + "2026-03-01";

        assertParseFailure(parser, userInput,
                MESSAGE_DUPLICATE_FIELDS + PREFIX_START_DATE);
    }

    @Test
    public void parse_preamblePresent_failure() {
        String userInput = "randomText "
                + PREFIX_START_DATE + "2026-01-01 "
                + PREFIX_END_DATE + "2026-03-01";

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }
}
