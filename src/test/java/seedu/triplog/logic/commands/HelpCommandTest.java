package seedu.triplog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.triplog.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.triplog.commons.core.CommandUsage;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_noArgument_opensHelpWindow() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_emptyStringArgument_opensHelpWindow() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(""), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_addArgument_showsInlineUsage() throws CommandException {
        CommandResult result = new HelpCommand("add").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("add "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_editArgument_showsInlineUsage() throws CommandException {
        CommandResult result = new HelpCommand("edit").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("edit "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_deleteArgument_showsInlineUsage() throws CommandException {
        CommandResult result = new HelpCommand("delete").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("delete "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_tagArgument_showsInlineUsage() throws CommandException {
        CommandResult result = new HelpCommand("tag").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("tag "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_findArgument_showsInlineUsage() throws CommandException {
        CommandResult result = new HelpCommand("find").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("find "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_filterArgument_showsInlineUsage() throws CommandException {
        CommandResult result = new HelpCommand("filter").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("filter "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_listArgument_showsInlineUsage() throws CommandException {
        CommandResult result = new HelpCommand("list").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("list"));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_helpArgument_showsInlineUsage() throws CommandException {
        CommandResult result = new HelpCommand("help").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("help"));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_clearArgument_showsInlineUsage() throws CommandException {
        CommandResult result = new HelpCommand("clear").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("clear"));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_exitArgument_showsInlineUsage() throws CommandException {
        CommandResult result = new HelpCommand("exit").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("exit"));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_unknownArgument_showsErrorMessage() {
        assertThrows(CommandException.class, () -> new HelpCommand("foobar").execute(model));
    }

    @Test
    public void execute_argumentWithWhitespace_trims() throws CommandException {
        CommandResult result = new HelpCommand("  add  ").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("add "));
    }

    @Test
    public void addUsage_containsDateOptions() {
        assertTrue(CommandUsage.ADD_USAGE.contains("sd/"));
        assertTrue(CommandUsage.ADD_USAGE.contains("ed/"));
    }

    @Test
    public void deleteUsage_containsIndexPlaceholder() {
        assertTrue(CommandUsage.DELETE_USAGE.contains("<INDEX>"));
    }

    @Test
    public void tagUsage_containsIndexAndTagNamePlaceholders() {
        assertTrue(CommandUsage.TAG_USAGE.contains("<INDEX>"));
        assertTrue(CommandUsage.TAG_USAGE.contains("<tag-name>"));
    }
}
