package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;

import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import seedu.triplog.commons.core.CommandUsage;

@ExtendWith(ApplicationExtension.class)
public class HelpWindowTest {

    private HelpWindow helpWindow;

    @Start
    public void start(Stage stage) {
        helpWindow = new HelpWindow(new Stage());
    }

    @Stop
    public void stop() {
        helpWindow.hide();
    }

    @Test
    public void constructor_createsHelpWindow() {
        assertNotNull(helpWindow);
    }

    @Test
    public void constructor_nullRoot_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new HelpWindow(null));
    }

    @Test
    public void isCloseKey_nullCode_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> HelpWindow.isCloseKey(null));
    }

    @Test
    public void isCloseKey_qKey_returnsTrue() {
        assertTrue(HelpWindow.isCloseKey(KeyCode.Q));
    }

    @Test
    public void isCloseKey_escapeKey_returnsTrue() {
        assertTrue(HelpWindow.isCloseKey(KeyCode.ESCAPE));
    }

    @Test
    public void isCloseKey_otherKey_returnsFalse() {
        assertFalse(HelpWindow.isCloseKey(KeyCode.A));
        assertFalse(HelpWindow.isCloseKey(KeyCode.ENTER));
    }

    @Test
    public void prefixNote_containsPrefixFormat() {
        assertTrue(HelpWindow.PREFIX_NOTE.contains("prefix/"));
    }

    @Test
    public void exitNote_mentionsQKey() {
        assertTrue(HelpWindow.EXIT_NOTE.contains("Q"));
    }

    @Test
    public void exitNote_mentionsEscapeKey() {
        assertTrue(HelpWindow.EXIT_NOTE.contains("ESCAPE"));
    }

    @Test
    public void isShowing_initiallyFalse() {
        assertFalse(helpWindow.isShowing());
    }

    @Test
    public void commandUsage_allStrings_containCommandKeyword() {
        assertTrue(CommandUsage.ADD_USAGE.contains("add"));
        assertTrue(CommandUsage.EDIT_USAGE.contains("edit"));
        assertTrue(CommandUsage.DELETE_USAGE.contains("delete"));
        assertTrue(CommandUsage.TAG_USAGE.contains("tag"));
        assertTrue(CommandUsage.FIND_USAGE.contains("find"));
        assertTrue(CommandUsage.FILTER_USAGE.contains("filter"));
        assertTrue(CommandUsage.LIST_USAGE.contains("list"));
        assertTrue(CommandUsage.CLEAR_USAGE.contains("clear"));
        assertTrue(CommandUsage.EXIT_USAGE.contains("exit"));
    }
}
