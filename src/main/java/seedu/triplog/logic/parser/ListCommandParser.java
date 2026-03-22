package seedu.triplog.logic.parser;

import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.commands.ListCommand.PREFIX_SORT;

import seedu.triplog.logic.commands.ListCommand;
import seedu.triplog.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            return new ListCommand();
        }

        if (!trimmedArgs.startsWith(PREFIX_SORT)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_INVALID_SORT_KEY));
        }

        String sortKey = trimmedArgs.replace(PREFIX_SORT, "").trim();

        if (sortKey.isEmpty()) {
            throw new ParseException(ListCommand.MESSAGE_INVALID_SORT_KEY);
        }

        return new ListCommand(sortKey);
    }
}
