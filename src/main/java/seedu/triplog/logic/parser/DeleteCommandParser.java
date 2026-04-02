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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.triplog.commons.core.index.Index;
import seedu.triplog.logic.commands.DeleteCommand;
import seedu.triplog.logic.parser.exceptions.ParseException;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Address;
import seedu.triplog.model.trip.Email;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.Phone;
import seedu.triplog.model.trip.TripDate;
import seedu.triplog.model.trip.TripMatchesDeletePredicate;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {
    static final String MESSAGE_MULTIPLE_DELETE_FIELDS =
            "Delete by field accepts exactly one field only, except for sd/ and ed/ used together as a date range.";
    private static final Pattern RANGE_PATTERN = Pattern.compile("\\s*(\\d+)\\s*-\\s*(\\d+)\\s*");
    private static final String MESSAGE_INVALID_DATE_RANGE =
            "Start date cannot be after end date.";

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + trimmedArgs,
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_START_DATE, PREFIX_END_DATE, PREFIX_TAG);

        if (isCriteriaDelete(argMultimap)) {
            return parseCriteriaDelete(argMultimap);
        }

        Matcher rangeMatcher = RANGE_PATTERN.matcher(trimmedArgs);
        if (rangeMatcher.matches()) {
            return parseRangeDelete(rangeMatcher);
        }

        if (looksLikeInvalidRange(trimmedArgs)) {
            throw new ParseException(DeleteCommand.MESSAGE_INVALID_RANGE_FORMAT);
        }

        if (looksLikeMalformedFieldDelete(trimmedArgs)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return parseSingleIndexDelete(trimmedArgs);
    }

    private DeleteCommand parseSingleIndexDelete(String trimmedArgs) throws ParseException {
        String[] tokens = trimmedArgs.split("\\s+");
        if (tokens.length > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(tokens[0]);
            return new DeleteCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(DeleteCommand.MESSAGE_INVALID_INDEX);
        }
    }

    private DeleteCommand parseRangeDelete(Matcher rangeMatcher) throws ParseException {
        String startToken = rangeMatcher.group(1);
        String endToken = rangeMatcher.group(2);

        Index startIndex;
        Index endIndex;

        try {
            startIndex = ParserUtil.parseIndex(startToken);
            endIndex = ParserUtil.parseIndex(endToken);
        } catch (ParseException pe) {
            throw new ParseException(DeleteCommand.MESSAGE_INVALID_RANGE_INDEX);
        }

        if (startIndex.getOneBased() > endIndex.getOneBased()) {
            throw new ParseException(DeleteCommand.MESSAGE_INVALID_RANGE_ORDER);
        }

        return new DeleteCommand(startIndex, endIndex);
    }

    private DeleteCommand parseCriteriaDelete(ArgumentMultimap argMultimap) throws ParseException {
        ensureNoPreamble(argMultimap);
        validateDeleteFieldCombination(argMultimap);

        Name name = parseOptionalName(argMultimap);
        Phone phone = parseOptionalPhone(argMultimap);
        Email email = parseOptionalEmail(argMultimap);
        Address address = parseOptionalAddress(argMultimap);
        TripDate startDate = parseOptionalTripDate(argMultimap, PREFIX_START_DATE);
        TripDate endDate = parseOptionalTripDate(argMultimap, PREFIX_END_DATE);
        Set<Tag> tags = parseOptionalTags(argMultimap);

        validateDateRange(startDate, endDate);

        return new DeleteCommand(new TripMatchesDeletePredicate(
                name, phone, email, address, startDate, endDate, tags));
    }

    /**
     * Returns true if the input should be parsed as criteria-based delete.
     */
    private boolean isCriteriaDelete(ArgumentMultimap argMultimap) {
        return arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_START_DATE, PREFIX_END_DATE, PREFIX_TAG);
    }

    /**
     * Returns true if the input looks like the user attempted a range but formatted it wrongly.
     */
    private boolean looksLikeInvalidRange(String input) {
        return input.matches(".*\\d+\\s*-\\s*\\D+.*")
                || input.matches(".*\\D+\\s*-\\s*\\d+.*")
                || input.matches(".*\\d+\\s*-\\s*\\d+\\s+.*");
    }

    /**
     * Returns true if the input looks like a malformed field delete,
     * such as "/Tokyo", which does not start with a valid prefix.
     */
    private boolean looksLikeMalformedFieldDelete(String input) {
        return input.startsWith("/");
    }

    /**
     * Returns true if at least one of the prefixes contains a value.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        for (Prefix prefix : prefixes) {
            if (argumentMultimap.getValue(prefix).isPresent() || !argumentMultimap.getAllValues(prefix).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of distinct delete prefixes present in the input.
     */
    private int countPresentPrefixes(ArgumentMultimap argMultimap) {
        int count = 0;

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            count++;
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            count++;
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            count++;
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            count++;
        }
        if (argMultimap.getValue(PREFIX_START_DATE).isPresent()) {
            count++;
        }
        if (argMultimap.getValue(PREFIX_END_DATE).isPresent()) {
            count++;
        }
        if (!argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            count++;
        }

        return count;
    }

    private void ensureNoPreamble(ArgumentMultimap argMultimap) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();
        if (!preamble.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

    private void validateDeleteFieldCombination(ArgumentMultimap argMultimap) throws ParseException {
        int prefixCount = countPresentPrefixes(argMultimap);

        boolean hasStartDate = argMultimap.getValue(PREFIX_START_DATE).isPresent();
        boolean hasEndDate = argMultimap.getValue(PREFIX_END_DATE).isPresent();
        boolean isDateRangeDelete = hasStartDate && hasEndDate && prefixCount == 2;

        if (prefixCount != 1 && !isDateRangeDelete) {
            throw new ParseException(MESSAGE_MULTIPLE_DELETE_FIELDS);
        }
    }

    private Name parseOptionalName(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> value = getSingleValue(argMultimap, PREFIX_NAME);
        return value.isPresent() ? ParserUtil.parseName(value.get()) : null;
    }

    private Phone parseOptionalPhone(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> value = getSingleValue(argMultimap, PREFIX_PHONE);
        return value.isPresent() ? ParserUtil.parsePhone(value.get()) : null;
    }

    private Email parseOptionalEmail(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> value = getSingleValue(argMultimap, PREFIX_EMAIL);
        return value.isPresent() ? ParserUtil.parseEmail(value.get()) : null;
    }

    private Address parseOptionalAddress(ArgumentMultimap argMultimap) throws ParseException {
        Optional<String> value = getSingleValue(argMultimap, PREFIX_ADDRESS);
        return value.isPresent() ? ParserUtil.parseAddress(value.get()) : null;
    }

    private TripDate parseOptionalTripDate(ArgumentMultimap argMultimap, Prefix prefix) throws ParseException {
        Optional<String> value = getSingleValue(argMultimap, prefix);
        return value.isPresent() ? ParserUtil.parseTripDate(value.get()) : null;
    }

    private Set<Tag> parseOptionalTags(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            return Set.of();
        }

        if (argMultimap.getAllValues(PREFIX_TAG).size() > 1) {
            throw new ParseException(MESSAGE_MULTIPLE_DELETE_FIELDS);
        }

        return ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
    }

    private Optional<String> getSingleValue(ArgumentMultimap argMultimap, Prefix prefix) throws ParseException {
        if (argMultimap.getAllValues(prefix).size() > 1) {
            throw new ParseException(MESSAGE_MULTIPLE_DELETE_FIELDS);
        }

        return argMultimap.getValue(prefix);
    }

    private void validateDateRange(TripDate startDate, TripDate endDate) throws ParseException {
        if (startDate != null && endDate != null && startDate.value.isAfter(endDate.value)) {
            throw new ParseException(MESSAGE_INVALID_DATE_RANGE);
        }
    }
}
