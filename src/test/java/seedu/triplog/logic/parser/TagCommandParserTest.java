package seedu.triplog.logic.parser;

import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_FIRST_TRIP;

import org.junit.jupiter.api.Test;

import seedu.triplog.logic.commands.TagCommand;
import seedu.triplog.model.tag.Tag;

public class TagCommandParserTest {

    private TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_validArgs_returnsTagCommand() {
        // Simple case: index 1 and tag "leisure"
        assertParseSuccess(parser, "1 leisure", new TagCommand(INDEX_FIRST_TRIP, new Tag("leisure")));

        // Case with multiple whitespaces between arguments
        assertParseSuccess(parser, "  1    work  ", new TagCommand(INDEX_FIRST_TRIP, new Tag("work")));
    }

    @Test
    public void parse_missingParts_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE);

        // Path 1: trimmedArgs is empty
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "   ", expectedMessage);

        // Path 2: parts.length < 2 (No space at all)
        assertParseFailure(parser, "1", expectedMessage);

        // Path 3: parts.length == 2 but parts[1] is just whitespace
        assertParseFailure(parser, "1 ", expectedMessage); // One space
        assertParseFailure(parser, "1  ", expectedMessage); // Multiple spaces
        assertParseFailure(parser, "1 \t", expectedMessage); // Tab
        assertParseFailure(parser, "1 \n", expectedMessage); // Newline
    }

    @Test
    public void parse_invalidIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE);

        // zero index
        assertParseFailure(parser, "0 leisure", expectedMessage);

        // negative index
        assertParseFailure(parser, "-1 leisure", expectedMessage);

        // non-numeric index
        assertParseFailure(parser, "a leisure", expectedMessage);
    }

    @Test
    public void parse_invalidTag_failure() {
        // Tag with invalid characters (assuming Tag.MESSAGE_CONSTRAINTS is the error)
        // Adjust the expected message based on your actual Tag class constraints
        assertParseFailure(parser, "1 *&^%$", Tag.MESSAGE_CONSTRAINTS);
    }
}
