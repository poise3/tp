package seedu.triplog.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.Messages.MESSAGE_UNKNOWN_PREFIXES;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.triplog.commons.core.index.Index;
import seedu.triplog.logic.commands.EditCommand;
import seedu.triplog.logic.commands.EditCommand.EditTripDescriptor;
import seedu.triplog.logic.parser.exceptions.ParseException;
import seedu.triplog.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        List<String> unknownPrefixes = ArgumentTokenizer.extractUnknownPrefixes(args,
                PREFIX_NAME,
                PREFIX_PHONE,
                PREFIX_EMAIL,
                PREFIX_ADDRESS,
                PREFIX_TAG,
                PREFIX_START_DATE,
                PREFIX_END_DATE);
        if (!unknownPrefixes.isEmpty()) {
            throw new ParseException(MESSAGE_UNKNOWN_PREFIXES);
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                                           PREFIX_ADDRESS, PREFIX_TAG, PREFIX_START_DATE, PREFIX_END_DATE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_START_DATE, PREFIX_END_DATE);
        EditTripDescriptor editTripDescriptor = new EditTripDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editTripDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editTripDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editTripDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editTripDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_START_DATE).isPresent()) {
            editTripDescriptor.setStartDate(
                    ParserUtil.parseTripDate(argMultimap.getValue(PREFIX_START_DATE).get())
            );
        }

        if (argMultimap.getValue(PREFIX_END_DATE).isPresent()) {
            editTripDescriptor.setEndDate(
                    ParserUtil.parseTripDate(argMultimap.getValue(PREFIX_END_DATE).get())
            );
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editTripDescriptor::setTags);

        if (!editTripDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editTripDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
