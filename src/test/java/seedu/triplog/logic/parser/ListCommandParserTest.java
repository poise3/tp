package seedu.triplog.logic.parser;

import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.triplog.logic.commands.ListCommand;

public class ListCommandParserTest {

    private ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_emptyArg_returnsListCommand() {
        // No arguments should return the default ListCommand (sort by start date)
        assertParseSuccess(parser, "     ", new ListCommand());
    }

    @Test
    public void parse_validArgs_returnsListCommand() {
        // Test all supported sort keys
        assertParseSuccess(parser, " sort/name", new ListCommand("name"));
        assertParseSuccess(parser, " sort/start", new ListCommand("start"));
        assertParseSuccess(parser, " sort/end", new ListCommand("end"));
        assertParseSuccess(parser, " sort/len", new ListCommand("len"));

        // Test case insensitivity and extra whitespace
        assertParseSuccess(parser, "   sort/NAME  ", new ListCommand("name"));
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        // Input that doesn't use the sort/ prefix (results in a non-empty preamble)
        assertParseFailure(parser, " name",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));

        // Random arguments without prefix
        assertParseFailure(parser, " random",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptySortKey_throwsParseException() {
        // Prefix exists but no value provided
        assertParseFailure(parser, " sort/", ListCommand.MESSAGE_INVALID_SORT_KEY);
        assertParseFailure(parser, " sort/    ", ListCommand.MESSAGE_INVALID_SORT_KEY);
    }

    @Test
    public void parse_unsupportedSortKey_throwsParseException() {
        // Valid prefix but unsupported key
        assertParseFailure(parser, " sort/invalid", ListCommand.MESSAGE_INVALID_SORT_KEY);
        assertParseFailure(parser, " sort/123", ListCommand.MESSAGE_INVALID_SORT_KEY);
    }
}
