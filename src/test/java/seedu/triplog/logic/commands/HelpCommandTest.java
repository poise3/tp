package seedu.triplog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.triplog.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.jupiter.api.Test;

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
    public void execute_addArgument_showsInlineUsage() {
        CommandResult result = new HelpCommand("add").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("add "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_editArgument_showsInlineUsage() {
        CommandResult result = new HelpCommand("edit").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("edit "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_deleteArgument_showsInlineUsage() {
        CommandResult result = new HelpCommand("delete").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("delete "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_tagArgument_showsInlineUsage() {
        CommandResult result = new HelpCommand("tag").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("tag "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_findArgument_showsInlineUsage() {
        CommandResult result = new HelpCommand("find").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("find "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_filterArgument_showsInlineUsage() {
        CommandResult result = new HelpCommand("filter").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("filter "));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_listArgument_showsInlineUsage() {
        CommandResult result = new HelpCommand("list").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("list"));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_unknownArgument_showsErrorMessage() {
        CommandResult result = new HelpCommand("foobar").execute(model);
        assertTrue(result.getFeedbackToUser().contains("foobar"));
        assertFalse(result.isShowHelp());
    }

    @Test
    public void execute_argumentWithWhitespace_trims() {
        CommandResult result = new HelpCommand("  add  ").execute(model);
        assertTrue(result.getFeedbackToUser().startsWith("add "));
    }
}
