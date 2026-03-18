package seedu.triplog.logic.parser;

import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.triplog.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_FIRST_TRIP;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.triplog.commons.core.index.Index;
import seedu.triplog.logic.commands.DeleteCommand;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.TripMatchesDeletePredicate;

/**
 * Contains unit tests for DeleteCommandParser.
 */
public class DeleteCommandParserTest {

    private final DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validSingleIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(INDEX_FIRST_TRIP));
        assertParseSuccess(parser, "   1   ", new DeleteCommand(INDEX_FIRST_TRIP));
    }

    @Test
    public void parse_validRange_returnsDeleteCommand() {
        assertParseSuccess(parser, "1-3",
                new DeleteCommand(Index.fromOneBased(1), Index.fromOneBased(3)));
        assertParseSuccess(parser, "  2 - 5  ",
                new DeleteCommand(Index.fromOneBased(2), Index.fromOneBased(5)));
    }

    @Test
    public void parse_validCriteria_returnsDeleteCommand() {
        assertParseSuccess(parser, "n/Tokyo",
                new DeleteCommand(new TripMatchesDeletePredicate(
                        new Name("Tokyo"), null, null, null, null, null, Set.of())));

        assertParseSuccess(parser, "t/family",
                new DeleteCommand(new TripMatchesDeletePredicate(
                        null, null, null, null, null, null, Set.of(new Tag("family")))));

    }

    @Test
    public void parse_multipleFields_throwsParseException() {
        assertParseFailure(parser, "n/Tokyo t/family",
                "Delete by field accepts exactly one field only.");
        assertParseFailure(parser, "n/Tokyo n/India",
                "Delete by field accepts exactly one field only.");
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndexFormat_throwsParseException() {
        assertParseFailure(parser, "a", DeleteCommand.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "-1", DeleteCommand.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "0", DeleteCommand.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidRange_throwsParseException() {
        assertParseFailure(parser, "5-2", DeleteCommand.MESSAGE_INVALID_RANGE_ORDER);
        assertParseFailure(parser, "0-3", DeleteCommand.MESSAGE_INVALID_RANGE_INDEX);
        assertParseFailure(parser, "1-a", DeleteCommand.MESSAGE_INVALID_RANGE_FORMAT);
    }

    @Test
    public void parse_extraArgumentsForIndex_throwsParseException() {
        assertParseFailure(parser, "1 abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidCriteriaPreamble_throwsParseException() {
        assertParseFailure(parser, "abc n/Tokyo",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
