package seedu.triplog.logic.parser;

import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.Messages.MESSAGE_INVALID_INDEX_FORMAT;
import static seedu.triplog.logic.Messages.MESSAGE_MISSING_INDEX;

import seedu.triplog.commons.core.index.Index;
import seedu.triplog.logic.commands.DeleteCommand;
import seedu.triplog.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_INDEX);
        }

        String[] tokens = trimmedArgs.split("\\s+");
        if (tokens.length > 1) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(tokens[0]);
            return new DeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(MESSAGE_INVALID_INDEX_FORMAT);
        }
    }

}
