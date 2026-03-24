package seedu.triplog.logic.parser;

import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.commands.ListCommand.PREFIX_SORT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import seedu.triplog.logic.commands.ListCommand;
import seedu.triplog.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {

    private static final Set<String> VALID_KEYS = new HashSet<>(Arrays.asList("name", "start", "end", "len"));

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        Prefix prefixSort = new Prefix(PREFIX_SORT);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, prefixSort);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }

        // If sort/ prefix is missing, return a ListCommand with a null sortKey to persist current sorting
        if (argMultimap.getValue(prefixSort).isEmpty()) {
            return new ListCommand();
        }

        String sortKey = argMultimap.getValue(prefixSort).get().trim().toLowerCase();
        if (sortKey.isEmpty() || !VALID_KEYS.contains(sortKey)) {
            throw new ParseException(ListCommand.MESSAGE_INVALID_SORT_KEY);
        }

        return new ListCommand(sortKey);
    }
}
