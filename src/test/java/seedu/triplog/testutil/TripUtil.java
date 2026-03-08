package seedu.triplog.testutil;

import static seedu.triplog.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_START_DATE;

import java.util.Set;

import seedu.triplog.logic.commands.AddCommand;
import seedu.triplog.logic.commands.EditCommand.EditTripDescriptor;
import seedu.triplog.model.person.Trip;
import seedu.triplog.model.tag.Tag;

/**
 * A utility class for Trip.
 */
public class TripUtil {

    /**
     * Returns an add command string for adding the {@code trip}.
     */
    public static String getAddCommand(Trip trip) {
        return AddCommand.COMMAND_WORD + " " + getTripDetails(trip);
    }

    /**
     * Returns the part of command string for the given {@code trip}'s details.
     */
    public static String getTripDetails(Trip trip) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + trip.getName().fullName + " ");
        sb.append(PREFIX_PHONE + trip.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + trip.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + trip.getAddress().value + " ");

        if (trip.getStartDate() != null) {
            sb.append(PREFIX_START_DATE + trip.getStartDate().toString() + " ");
        }
        if (trip.getEndDate() != null) {
            sb.append(PREFIX_END_DATE + trip.getEndDate().toString() + " ");
        }

        trip.getTags().stream().forEach(
                s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditTripDescriptor}'s details.
     */
    public static String getEditTripDescriptorDetails(EditTripDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        descriptor.getStartDate().ifPresent(date -> sb.append(PREFIX_START_DATE).append(date.value).append(" "));
        descriptor.getEndDate().ifPresent(date -> sb.append(PREFIX_END_DATE).append(date.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
