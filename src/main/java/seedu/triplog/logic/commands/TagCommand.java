package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.triplog.commons.core.index.Index;
import seedu.triplog.commons.util.ToStringBuilder;
import seedu.triplog.logic.Messages;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Trip;


/**
 * Adds a tag to a trip in the trip log.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tag to a trip in the trip log. "
            + "Parameters: INDEX (must be a positive integer) "
            + "KEYWORD\n"
            + "Example: " + COMMAND_WORD + " 3 leisure";

    public static final String MESSAGE_TAG_TRIP_SUCCESS = "New tag %1$s added to trip: %2$s";
    public static final String MESSAGE_DUPLICATE_TAG = "Tag %1$s already exists for trip: %2$s";

    private final Index index;
    private final Tag tag;

    /**
     * Creates a TagCommand to add the specified {@code Tag}
     */
    public TagCommand(Index index, Tag tag) {
        requireNonNull(index);
        requireNonNull(tag);
        this.index = index;
        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Trip> lastShownList = model.getFilteredTripList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TRIP_DISPLAYED_INDEX);
        }

        Trip tripToTag = lastShownList.get(index.getZeroBased());

        if (tripToTag.getTags().contains(this.tag)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_TAG, this.tag, Messages.format(tripToTag)));
        }

        Trip tripWithUpdatedTag = new Trip(tripToTag, this.tag);

        model.setTrip(tripToTag, tripWithUpdatedTag);
        return new CommandResult(
                String.format(
                        MESSAGE_TAG_TRIP_SUCCESS,
                        this.tag,
                        Messages.format(tripWithUpdatedTag)
                )
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTagCommand = (TagCommand) other;
        return this.index.equals(otherTagCommand.index)
                && this.tag.equals(otherTagCommand.tag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", this.index)
                .add("tag", this.tag)
                .toString();
    }
}
