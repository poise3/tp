package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class HelpWindowTest {

    @Test
    public void addUsage_containsCommandName() {
        assertTrue(HelpWindow.ADD_USAGE.startsWith("add "));
    }

    @Test
    public void addUsage_containsDateOptions() {
        assertTrue(HelpWindow.ADD_USAGE.contains("/start:"));
        assertTrue(HelpWindow.ADD_USAGE.contains("/end:"));
    }

    @Test
    public void deleteUsage_containsCommandName() {
        assertTrue(HelpWindow.DELETE_USAGE.startsWith("delete "));
    }

    @Test
    public void deleteUsage_containsIndexPlaceholder() {
        assertTrue(HelpWindow.DELETE_USAGE.contains("<INDEX>"));
    }

    @Test
    public void tagUsage_containsCommandName() {
        assertTrue(HelpWindow.TAG_USAGE.startsWith("tag "));
    }

    @Test
    public void tagUsage_containsIndexAndTagNamePlaceholders() {
        assertTrue(HelpWindow.TAG_USAGE.contains("<index>"));
        assertTrue(HelpWindow.TAG_USAGE.contains("<tag-name>"));
    }

    @Test
    public void listUsage_containsCommandName() {
        assertTrue(HelpWindow.LIST_USAGE.startsWith("list"));
    }

    @Test
    public void prefixNote_containsPrefixFormat() {
        assertTrue(HelpWindow.PREFIX_NOTE.contains("/key:value"));
    }

    @Test
    public void exitNote_mentionsQKey() {
        assertTrue(HelpWindow.EXIT_NOTE.contains("Q"));
    }

    @Test
    public void exitNote_mentionsEscapeKey() {
        assertTrue(HelpWindow.EXIT_NOTE.contains("ESCAPE"));
    }
}