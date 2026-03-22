package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.triplog.commons.util.ToStringBuilder;
import seedu.triplog.logic.Messages;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.trip.Trip;

/**
 * Adds a trip to the trip log.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a trip to the trip log. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New trip added: %1$s\n%2$s";
    public static final String MESSAGE_DUPLICATE_TRIP = "This trip already exists in the trip log";

    private final Trip toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Trip}
     */
    public AddCommand(Trip trip) {
        requireNonNull(trip);
        toAdd = trip;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasTrip(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TRIP);
        }

        model.addTrip(toAdd);
        String summary = ListCommand.calculateSummary(model.getFilteredTripList());
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd), summary));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
