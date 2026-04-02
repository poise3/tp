package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

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
            + "n/NAME | p/PHONE | e/EMAIL | a/ADDRESS | "
            + "sd/START_DATE | ed/END_DATE | sd/START_DATE ed/END_DATE | t/TAG";

    public static final String MESSAGE_DELETE_TRIP_SUCCESS = "Deleted %1$d trip.\n%2$s";
    public static final String MESSAGE_DELETE_TRIPS_SUCCESS = "Deleted %1$d trips.\n%2$s";
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
    public static final String MESSAGE_PREVIEW_DELETE_TRIP = "Preview: 1 trip will be deleted.\n";
    public static final String MESSAGE_PREVIEW_DELETE_TRIPS = "Preview: %1$d trips will be deleted.\n";
    public static final String MESSAGE_PREVIEW_FOOTER =
            "\nPress Enter again to confirm deletion, or edit the command to cancel.";

    private enum DeleteMode {
        SINGLE,
        RANGE,
        FILTER
    }
    private static final Logger logger = Logger.getLogger(DeleteCommand.class.getName());
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
        assert startIndex.getZeroBased() <= endIndex.getZeroBased()
                : "startIndex should not be after endIndex";
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
        assertExactlyOneDeleteModeIsActive();
        logger.info("Executing delete command in mode: " + mode);

        List<Trip> tripsToDelete = getTripsToDelete(model);
        deleteTrips(model, tripsToDelete);

        String summary = TripSummaryUtil.calculateSummary(model.getFilteredTripList());
        return new CommandResult(formatDeleteSuccessMessage(tripsToDelete.size(), summary));
    }

    /**
     * Returns the list of trips that would be deleted by this command.
     *
     * @param model The model containing the currently displayed trip list.
     * @return The list of trips to be deleted.
     * @throws CommandException If the delete target is invalid or no trips match the criteria.
     */
    public List<Trip> getTripsToDelete(Model model) throws CommandException {
        requireNonNull(model);
        List<Trip> lastShownList = model.getFilteredTripList();

        switch (mode) {
        case SINGLE:
            return getSingleTripToDelete(lastShownList);
        case RANGE:
            return getRangeTripsToDelete(lastShownList);
        case FILTER:
            return getFilteredTripsToDelete(lastShownList);
        default:
            throw new AssertionError("Unknown delete mode");
        }
    }

    /**
     * Builds a preview message showing the trips that will be deleted upon confirmation.
     *
     * @param tripsToDelete The list of trips to be deleted.
     * @return A formatted preview message for display to the user.
     */
    public String buildPreviewMessage(List<Trip> tripsToDelete) {
        StringBuilder sb = new StringBuilder();

        if (tripsToDelete.size() == 1) {
            sb.append(MESSAGE_PREVIEW_DELETE_TRIP);
        } else {
            sb.append(String.format(MESSAGE_PREVIEW_DELETE_TRIPS, tripsToDelete.size()));
        }

        for (int i = 0; i < tripsToDelete.size(); i++) {
            Trip trip = tripsToDelete.get(i);

            String start = trip.getStartDate() == null
                    ? "No date"
                    : trip.getStartDate().toString();

            String end = trip.getEndDate() == null
                    ? "No date"
                    : trip.getEndDate().toString();

            sb.append(i + 1).append(". ")
                    .append(trip.getName())
                    .append(" (")
                    .append(start)
                    .append(" to ")
                    .append(end)
                    .append(")\n");
        }

        sb.append(MESSAGE_PREVIEW_FOOTER);
        return sb.toString().trim();
    }

    private void assertExactlyOneDeleteModeIsActive() {
        int activeModes = 0;
        activeModes += targetIndex != null ? 1 : 0;
        activeModes += startIndex != null && endIndex != null ? 1 : 0;
        activeModes += predicate != null ? 1 : 0;

        assert activeModes == 1 : "Exactly one delete mode should be active";
    }

    private List<Trip> getSingleTripToDelete(List<Trip> lastShownList) throws CommandException {
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INDEX_OUT_OF_RANGE);
        }

        return List.of(lastShownList.get(targetIndex.getZeroBased()));
    }

    private List<Trip> getRangeTripsToDelete(List<Trip> lastShownList) throws CommandException {
        if (startIndex.getZeroBased() >= lastShownList.size()
                || endIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_RANGE_OUT_OF_RANGE);
        }

        return new ArrayList<>(
                lastShownList.subList(startIndex.getZeroBased(), endIndex.getZeroBased() + 1));
    }

    private List<Trip> getFilteredTripsToDelete(List<Trip> lastShownList) throws CommandException {
        List<Trip> tripsToDelete = lastShownList.stream()
                .filter(predicate)
                .toList();

        if (tripsToDelete.isEmpty()) {
            throw new CommandException(MESSAGE_NO_MATCHING_TRIPS);
        }

        return tripsToDelete;
    }

    private void deleteTrips(Model model, List<Trip> tripsToDelete) {
        for (Trip trip : tripsToDelete) {
            model.deleteTrip(trip);
        }
    }

    private String formatDeleteSuccessMessage(int numberOfTripsDeleted, String summary) {
        if (numberOfTripsDeleted == 1) {
            return String.format(MESSAGE_DELETE_TRIP_SUCCESS, 1, summary);
        }

        return String.format(MESSAGE_DELETE_TRIPS_SUCCESS, numberOfTripsDeleted, summary);
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
