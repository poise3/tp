package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.logic.parser.DeleteCommandParser;
import seedu.triplog.logic.parser.exceptions.ParseException;
import seedu.triplog.model.Model;
import seedu.triplog.model.trip.Trip;

/**
 * Internal command used to preview which trip(s) would be deleted.
 */
public class PreviewDeleteCommand extends Command {

    public static final String COMMAND_WORD = "deletepreview";

    private final String deleteArguments;

    /**
     * Creates a PreviewDeleteCommand using the raw delete arguments.
     */
    public PreviewDeleteCommand(String deleteArguments) {
        requireNonNull(deleteArguments);
        this.deleteArguments = deleteArguments;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            DeleteCommand deleteCommand = new DeleteCommandParser().parse(deleteArguments);
            List<Trip> tripsToDelete = deleteCommand.getTripsToDelete(model);
            return new CommandResult(deleteCommand.buildPreviewMessage(tripsToDelete));
        } catch (ParseException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
