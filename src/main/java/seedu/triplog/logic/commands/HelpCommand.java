package seedu.triplog.logic.commands;

import seedu.triplog.commons.core.CommandUsage;
import seedu.triplog.logic.commands.exceptions.CommandException;
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

    private final String argument;

    public HelpCommand() {
        this("");
    }

    public HelpCommand(String argument) {
        this.argument = argument.trim();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (argument.isEmpty()) {
            return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        }
        return new CommandResult(getUsageForCommand(argument), false, false);
    }

    private static String getUsageForCommand(String commandWord) throws CommandException {
        switch (commandWord) {
        case COMMAND_WORD:
            return MESSAGE_USAGE;
        case AddCommand.COMMAND_WORD:
            return CommandUsage.ADD_USAGE;
        case EditCommand.COMMAND_WORD:
            return CommandUsage.EDIT_USAGE;
        case DeleteCommand.COMMAND_WORD:
            return CommandUsage.DELETE_USAGE;
        case TagCommand.COMMAND_WORD:
            return CommandUsage.TAG_USAGE;
        case FindCommand.COMMAND_WORD:
            return CommandUsage.FIND_USAGE;
        case FilterCommand.COMMAND_WORD:
            return CommandUsage.FILTER_USAGE;
        case ListCommand.COMMAND_WORD:
            return CommandUsage.LIST_USAGE;
        default:
            throw new CommandException(String.format(MESSAGE_UNKNOWN_HELP_COMMAND, commandWord));
        }
    }
}
