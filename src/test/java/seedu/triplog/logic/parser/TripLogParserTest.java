package seedu.triplog.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.triplog.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_FIRST_TRIP;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.triplog.logic.commands.AddCommand;
import seedu.triplog.logic.commands.ClearCommand;
import seedu.triplog.logic.commands.Command;
import seedu.triplog.logic.commands.DeleteCommand;
import seedu.triplog.logic.commands.EditCommand;
import seedu.triplog.logic.commands.EditCommand.EditTripDescriptor;
import seedu.triplog.logic.commands.ExitCommand;
import seedu.triplog.logic.commands.FindCommand;
import seedu.triplog.logic.commands.HelpCommand;
import seedu.triplog.logic.commands.ListCommand;
import seedu.triplog.logic.commands.PreviewDeleteCommand;
import seedu.triplog.logic.parser.exceptions.ParseException;
import seedu.triplog.model.trip.NameContainsKeywordsPredicate;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.testutil.EditTripDescriptorBuilder;
import seedu.triplog.testutil.TripBuilder;
import seedu.triplog.testutil.TripUtil;

public class TripLogParserTest {

    private final TripLogParser parser = new TripLogParser();

    @Test
    public void parseCommand_add() throws Exception {
        Trip trip = new TripBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(TripUtil.getAddCommand(trip));
        assertEquals(new AddCommand(trip), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_TRIP.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_TRIP), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Trip trip = new TripBuilder().build();
        EditTripDescriptor descriptor = new EditTripDescriptorBuilder(trip).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_TRIP.getOneBased() + " " + TripUtil.getEditTripDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_TRIP, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertEquals(new ListCommand(), parser.parseCommand(ListCommand.COMMAND_WORD));
        assertEquals(new ListCommand("name"), parser.parseCommand(ListCommand.COMMAND_WORD
                + " sort/name"));
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, ()
                -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_deletePreview_returnsPreviewDeleteCommand() throws Exception {
        TripLogParser parser = new TripLogParser();
        Command command = parser.parseCommand("deletepreview 1");

        assertTrue(command instanceof PreviewDeleteCommand);
    }
}
