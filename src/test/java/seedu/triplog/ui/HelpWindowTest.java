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

    // EP: valid Stage
    @Test
    public void constructor_createsHelpWindow() {
        assertNotNull(helpWindow);
    }

    // EP: null Stage is an invalid input
    @Test
    public void constructor_nullRoot_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new HelpWindow(null));
    }

    // EP: null key code is an invalid input
    @Test
    public void isCloseKey_nullCode_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> HelpWindow.isCloseKey(null));
    }

    // EP: Q is in the close-key partition
    @Test
    public void isCloseKey_qKey_returnsTrue() {
        assertTrue(HelpWindow.isCloseKey(KeyCode.Q));
    }

    // EP: ESCAPE is in the close-key partition
    @Test
    public void isCloseKey_escapeKey_returnsTrue() {
        assertTrue(HelpWindow.isCloseKey(KeyCode.ESCAPE));
    }

    // EP: alphabetic non-Q keys and non-alphabetic keys are not close keys
    @Test
    public void isCloseKey_otherKey_returnsFalse() {
        assertFalse(HelpWindow.isCloseKey(KeyCode.A));
        assertFalse(HelpWindow.isCloseKey(KeyCode.ENTER));
    }

    // EP: keys alphabetically adjacent to Q are still in the non-close-key partition
    @Test
    public void isCloseKey_alphabeticallyAdjacentToQ_returnsFalse() {
        assertFalse(HelpWindow.isCloseKey(KeyCode.P));
        assertFalse(HelpWindow.isCloseKey(KeyCode.R));
    }

    // EP: modifier keys form a distinct non-close-key partition
    @Test
    public void isCloseKey_modifierKeys_returnsFalse() {
        assertFalse(HelpWindow.isCloseKey(KeyCode.SHIFT));
        assertFalse(HelpWindow.isCloseKey(KeyCode.CONTROL));
        assertFalse(HelpWindow.isCloseKey(KeyCode.ALT));
    }

    // EP: function keys form a distinct non-close-key partition (F1 and F12 as representatives)
    @Test
    public void isCloseKey_functionKeys_returnsFalse() {
        assertFalse(HelpWindow.isCloseKey(KeyCode.F1));
        assertFalse(HelpWindow.isCloseKey(KeyCode.F12));
    }

    // EP: digit keys form a distinct non-close-key partition (DIGIT0 and DIGIT9 as representatives)
    @Test
    public void isCloseKey_digitKeys_returnsFalse() {
        assertFalse(HelpWindow.isCloseKey(KeyCode.DIGIT0));
        assertFalse(HelpWindow.isCloseKey(KeyCode.DIGIT9));
    }

    // EP: navigation and whitespace keys form a distinct non-close-key partition
    @Test
    public void isCloseKey_navigationAndSpaceKeys_returnsFalse() {
        assertFalse(HelpWindow.isCloseKey(KeyCode.SPACE));
        assertFalse(HelpWindow.isCloseKey(KeyCode.UP));
        assertFalse(HelpWindow.isCloseKey(KeyCode.DOWN));
    }

    // EP: hidden state
    @Test
    public void isShowing_initiallyFalse() {
        assertFalse(helpWindow.isShowing());
    }

    // EP: PREFIX_NOTE contains the expected prefix/ format
    @Test
    public void prefixNote_containsPrefixFormat() {
        assertTrue(HelpWindow.PREFIX_NOTE.contains("prefix/"));
    }

    // EP: EXIT_NOTE mentions Q as a close key
    @Test
    public void exitNote_mentionsQKey() {
        assertTrue(HelpWindow.EXIT_NOTE.contains("Q"));
    }

    // EP: EXIT_NOTE mentions ESCAPE as a close key
    @Test
    public void exitNote_mentionsEscapeKey() {
        assertTrue(HelpWindow.EXIT_NOTE.contains("ESCAPE"));
    }

    // EP: every CommandUsage string contains its respective command keyword
    @Test
    public void commandUsage_allStrings_containCommandKeyword() {
        assertTrue(CommandUsage.HELP_USAGE.contains("help"));
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
