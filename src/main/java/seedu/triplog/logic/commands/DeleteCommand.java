package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import seedu.triplog.commons.core.index.Index;
import seedu.triplog.commons.util.ToStringBuilder;
import seedu.triplog.logic.Messages;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.trip.Trip;

/**
 * Deletes trip(s) identified using a displayed index, displayed range,
 * or matching criteria from the currently displayed trip list.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes trip(s) from the displayed list.\n"
            + "Usage: delete INDEX | START-END | "
            + "n/NAME | p/PHONE | e/EMAIL | a/ADDRESS | sd/START_DATE | ed/END_DATE | t/TAG";

    public static final String MESSAGE_DELETE_TRIP_SUCCESS = "Deleted %1$d trip.";
    public static final String MESSAGE_DELETE_TRIPS_SUCCESS = "Deleted %1$d trips.";
    public static final String MESSAGE_INVALID_INDEX =
            "Index must be a positive integer.";
    public static final String MESSAGE_INDEX_OUT_OF_RANGE =
            Messages.MESSAGE_INVALID_TRIP_DISPLAYED_INDEX;
    public static final String MESSAGE_INVALID_RANGE_FORMAT =
            "Range must be in the format START-END, where START and END are integers.";
    public static final String MESSAGE_INVALID_RANGE_INDEX =
            "Range indices must be positive integers.";
    public static final String MESSAGE_INVALID_RANGE_ORDER =
            "Start index should be less than or equal to end index.";
    public static final String MESSAGE_RANGE_OUT_OF_RANGE =
            "Range is out of range of the currently displayed trip list.";
    public static final String MESSAGE_NO_MATCHING_TRIPS =
            "No trips in the currently displayed list matched the given delete criteria.";

    private enum DeleteMode {
        SINGLE,
        RANGE,
        FILTER
    }

    private final DeleteMode mode;
    private final Index targetIndex;
    private final Index startIndex;
    private final Index endIndex;
    private final Predicate<Trip> predicate;

    /**
     * Creates a DeleteCommand for single-index deletion.
     */
    public DeleteCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.mode = DeleteMode.SINGLE;
        this.targetIndex = targetIndex;
        this.startIndex = null;
        this.endIndex = null;
        this.predicate = null;
    }

    /**
     * Creates a DeleteCommand for range deletion.
     */
    public DeleteCommand(Index startIndex, Index endIndex) {
        requireNonNull(startIndex);
        requireNonNull(endIndex);
        this.mode = DeleteMode.RANGE;
        this.targetIndex = null;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.predicate = null;
    }

    /**
     * Creates a DeleteCommand for criteria-based deletion.
     */
    public DeleteCommand(Predicate<Trip> predicate) {
        requireNonNull(predicate);
        this.mode = DeleteMode.FILTER;
        this.targetIndex = null;
        this.startIndex = null;
        this.endIndex = null;
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Trip> lastShownList = model.getFilteredTripList();

        switch (mode) {
        case SINGLE:
            return executeSingleDelete(model, lastShownList);
        case RANGE:
            return executeRangeDelete(model, lastShownList);
        case FILTER:
            return executeFilterDelete(model, lastShownList);
        default:
            throw new AssertionError("Unknown delete mode");
        }
    }

    private CommandResult executeSingleDelete(Model model, List<Trip> lastShownList) throws CommandException {
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INDEX_OUT_OF_RANGE);
        }

        Trip tripToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteTrip(tripToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_TRIP_SUCCESS, 1));
    }

    private CommandResult executeRangeDelete(Model model, List<Trip> lastShownList) throws CommandException {
        if (startIndex.getZeroBased() >= lastShownList.size() || endIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_RANGE_OUT_OF_RANGE);
        }

        List<Trip> tripsToDelete = new ArrayList<>(
                lastShownList.subList(startIndex.getZeroBased(), endIndex.getZeroBased() + 1));

        for (Trip trip : tripsToDelete) {
            model.deleteTrip(trip);
        }

        if (tripsToDelete.size() == 1) {
            return new CommandResult(String.format(MESSAGE_DELETE_TRIP_SUCCESS, 1));
        }

        return new CommandResult(String.format(MESSAGE_DELETE_TRIPS_SUCCESS, tripsToDelete.size()));
    }

    private CommandResult executeFilterDelete(Model model, List<Trip> lastShownList) throws CommandException {
        List<Trip> tripsToDelete = lastShownList.stream()
                .filter(predicate)
                .toList();

        if (tripsToDelete.isEmpty()) {
            throw new CommandException(MESSAGE_NO_MATCHING_TRIPS);
        }

        for (Trip trip : tripsToDelete) {
            model.deleteTrip(trip);
        }

        if (tripsToDelete.size() == 1) {
            return new CommandResult(String.format(MESSAGE_DELETE_TRIP_SUCCESS, 1));
        }

        return new CommandResult(String.format(MESSAGE_DELETE_TRIPS_SUCCESS, tripsToDelete.size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        if (mode != otherDeleteCommand.mode) {
            return false;
        }

        switch (mode) {
        case SINGLE:
            return targetIndex.equals(otherDeleteCommand.targetIndex);
        case RANGE:
            return startIndex.equals(otherDeleteCommand.startIndex)
                    && endIndex.equals(otherDeleteCommand.endIndex);
        case FILTER:
            return predicate.equals(otherDeleteCommand.predicate);
        default:
            return false;
        }
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this).add("mode", mode);

        switch (mode) {
        case SINGLE:
            builder.add("targetIndex", targetIndex);
            break;
        case RANGE:
            builder.add("startIndex", startIndex)
                    .add("endIndex", endIndex);
            break;
        case FILTER:
            builder.add("predicate", predicate);
            break;
        default:
            break;
        }

        return builder.toString();
    }
}
