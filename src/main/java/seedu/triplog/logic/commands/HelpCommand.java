package seedu.triplog.logic.commands;

import seedu.triplog.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD
          + ": Opens the TripLog command syntax guide, or shows usage for a specific command.\n"
          + "Format: " + COMMAND_WORD + " [COMMAND]\n"
          + "Example: " + COMMAND_WORD + "\n"
          + "Example: " + COMMAND_WORD + " add";
    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";
    public static final String MESSAGE_UNKNOWN_HELP_COMMAND =
            "Unknown command: '%s'. Type 'help' to open the full command guide.";

    public static final String ADD_USAGE =
            "add n/<destination> [p/<phone>] [e/<email>] [a/<address>] "
                    + "[sd/<start-date>] [ed/<end-date>] [t/<tag>]...\n"
                    + "  Records a new trip. Items in [square brackets] are optional. Dates must be YYYY-MM-DD.\n"
                    + "  e.g. add n/Tokyo sd/2026-03-01 t/food";

    public static final String EDIT_USAGE =
            "edit INDEX [n/<destination>] [p/<phone>] [e/<email>] [a/<address>] "
                    + "[sd/<start-date>] [ed/<end-date>] [t/<tag>]...\n"
                    + "  Edits the trip at the specified index. At least one field must be provided.\n"
                    + "  e.g. edit 1 n/Paris sd/2026-05-01";

    public static final String DELETE_USAGE =
            "delete <INDEX> | <START-END> | <PREFIX/VALUE>\n"
                    + "  Removes trip(s) from the currently displayed list.\n"
                    + "  INDEX must be a positive integer (1, 2, 3, …).\n"
                    + "  START must be <= END for range deletion.\n"
                    + "  Only one PREFIX can be used for field deletion.\n"
                    + "  e.g.  delete 2\n"
                    + "        delete 1-3\n"
                    + "        delete n/Tokyo\n"
                    + "        delete t/family";

    public static final String TAG_USAGE =
            "tag <index> <tag-name>\n"
                    + "  Adds a keyword tag to an existing trip.\n"
                    + "  tag-name must be alphanumeric and may contain spaces.\n"
                    + "  e.g.  tag 1 adventure    or    tag 1 night market";

    public static final String FIND_USAGE =
            "find <KEYWORD> [MORE_KEYWORDS]...\n"
                    + "  Finds trips whose names contain any of the given keywords.\n"
                    + "  e.g. find Tokyo Osaka";

    public static final String FILTER_USAGE =
            "filter sd/<start-date> ed/<end-date>\n"
                    + "  Filters trips occurring within the specified date range.\n"
                    + "  e.g. filter sd/2026-01-01 ed/2026-03-31";

    public static final String LIST_USAGE =
            "list\n"
                    + "  Displays all trip entries.\n"
                    + "  e.g.  list";

    private final String argument;

    public HelpCommand() {
        this("");
    }

    public HelpCommand(String argument) {
        this.argument = argument.trim();
    }

    @Override
    public CommandResult execute(Model model) {
        if (argument.isEmpty()) {
            return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        }
        return new CommandResult(getUsageForCommand(argument), false, false);
    }

    private static String getUsageForCommand(String commandWord) {
        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
            return ADD_USAGE;
        case EditCommand.COMMAND_WORD:
            return EDIT_USAGE;
        case DeleteCommand.COMMAND_WORD:
            return DELETE_USAGE;
        case TagCommand.COMMAND_WORD:
            return TAG_USAGE;
        case FindCommand.COMMAND_WORD:
            return FIND_USAGE;
        case FilterCommand.COMMAND_WORD:
            return FILTER_USAGE;
        case ListCommand.COMMAND_WORD:
            return LIST_USAGE;
        default:
            return String.format(MESSAGE_UNKNOWN_HELP_COMMAND, commandWord);
        }
    }
}
