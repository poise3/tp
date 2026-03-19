package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_START_DATE;

import seedu.triplog.commons.util.ToStringBuilder;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.trip.TripDate;

/**
 * Filter trips by date range.
 * Criteria: query start date <= trip start date (not null) <= trip end date (can be null) <= query end date
 * Ignores trips in the log with null date field
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filter trips by date range. "
            + "Parameters: "
            + PREFIX_START_DATE + "START DATE "
            + PREFIX_END_DATE + "END DATE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_START_DATE + "2026-01-01 "
            + PREFIX_END_DATE + "2026-03-01 ";

    public static final String MESSAGE_SUCCESS = "Found the following trips:";
    public static final String MESSAGE_NO_TRIPS_FOUND = "No trips found with the given date range.";
    public static final String MESSAGE_START_AFTER_END = "Start date should not be after end date.";

    private final TripDate startDate;
    private final TripDate endDate;

    /**
     * Creates an FilterCommand with date range params
     */
    public FilterCommand(TripDate startDate, TripDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (startDate.value.isAfter(endDate.value)) {
            throw new CommandException(MESSAGE_START_AFTER_END);
        }
        model.updateFilteredTripList(trip -> trip.getStartDate() != null
                && trip.getStartDate().value.isAfter(startDate.value.minusDays(1))
                && (trip.getEndDate() == null
                ? trip.getStartDate().value.isBefore(endDate.value.plusDays(1))
                : trip.getEndDate().value.isBefore(endDate.value.plusDays(1))));

        return new CommandResult(model.getFilteredTripList().isEmpty() ? MESSAGE_NO_TRIPS_FOUND : MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterCommand)) {
            return false;
        }

        return ((FilterCommand) other).startDate.equals(startDate)
                && ((FilterCommand) other).endDate.equals(endDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .toString();
    }
}
