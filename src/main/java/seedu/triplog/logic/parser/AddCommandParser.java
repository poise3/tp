package seedu.triplog.logic.parser;

import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.triplog.logic.commands.AddCommand;
import seedu.triplog.logic.parser.exceptions.ParseException;
import seedu.triplog.model.person.Address;
import seedu.triplog.model.person.Email;
import seedu.triplog.model.person.Name;
import seedu.triplog.model.person.Phone;
import seedu.triplog.model.person.Trip;
import seedu.triplog.model.person.TripDate;
import seedu.triplog.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                    args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                    PREFIX_TAG, PREFIX_START_DATE, PREFIX_END_DATE);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
            PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
            PREFIX_START_DATE, PREFIX_END_DATE);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());

        Optional<String> maybePhone = argMultimap.getValue(PREFIX_PHONE);
        Phone phone = maybePhone.isEmpty() ? null : ParserUtil.parsePhone(maybePhone.get());

        Optional<String> maybeEmail = argMultimap.getValue(PREFIX_EMAIL);
        Email email = maybeEmail.isEmpty() ? null : ParserUtil.parseEmail(maybeEmail.get());

        Optional<String> maybeAddress = argMultimap.getValue(PREFIX_ADDRESS);
        Address address = maybeAddress.isEmpty() ? null : ParserUtil.parseAddress(maybeAddress.get());

        Optional<String> maybeStartDate = argMultimap.getValue(PREFIX_START_DATE);
        TripDate startDate = maybeStartDate.isEmpty() ? null : ParserUtil.parseTripDate(maybeStartDate.get());

        Optional<String> maybeEndDate = argMultimap.getValue(PREFIX_END_DATE);
        TripDate endDate = maybeEndDate.isEmpty() ? null : ParserUtil.parseTripDate(maybeEndDate.get());

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Trip trip = new Trip(name, phone, email, address, tagList, startDate, endDate);

        return new AddCommand(trip);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
