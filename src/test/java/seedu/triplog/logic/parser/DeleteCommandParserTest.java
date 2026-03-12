package seedu.triplog.logic.parser;

import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.Messages.MESSAGE_INVALID_INDEX_FORMAT;
import static seedu.triplog.logic.Messages.MESSAGE_MISSING_INDEX;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.triplog.logic.commands.DeleteCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(INDEX_FIRST_PERSON));
        assertParseSuccess(parser, "   1   ", new DeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertParseFailure(parser, "", MESSAGE_MISSING_INDEX);
        assertParseFailure(parser, "   ", MESSAGE_MISSING_INDEX);
    }

    @Test
    public void parse_invalidIndexFormat_throwsParseException() {
        assertParseFailure(parser, "a", MESSAGE_INVALID_INDEX_FORMAT);
        assertParseFailure(parser, "-1", MESSAGE_INVALID_INDEX_FORMAT);
        assertParseFailure(parser, "0", MESSAGE_INVALID_INDEX_FORMAT);
    }

    @Test
    public void parse_extraArguments_throwsParseException() {
        assertParseFailure(parser, "1 abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "2 extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
