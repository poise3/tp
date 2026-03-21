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
    public void isCloseKey_nullCode_returnsFalse() {
        assertFalse(HelpWindow.isCloseKey(null));
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
    public void isCloseKey_aKey_returnsFalse() {
        assertFalse(HelpWindow.isCloseKey(KeyCode.A));
    }

    @Test
    public void isCloseKey_bKey_returnsFalse() {
        assertFalse(HelpWindow.isCloseKey(KeyCode.B));
    }

    @Test
    public void addUsage_containsCommandName() {
        assertTrue(HelpWindow.ADD_USAGE.startsWith("add "));
    }

    @Test
    public void editUsage_containsCommandName() {
        assertTrue(HelpWindow.EDIT_USAGE.startsWith("edit "));
    }

    @Test
    public void addUsage_containsDateOptions() {
        assertTrue(HelpWindow.ADD_USAGE.contains("sd/"));
        assertTrue(HelpWindow.ADD_USAGE.contains("ed/"));
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
    public void findUsage_containsCommandName() {
        assertTrue(HelpWindow.FIND_USAGE.startsWith("find "));
    }

    @Test
    public void filterUsage_containsCommandName() {
        assertTrue(HelpWindow.FILTER_USAGE.startsWith("filter "));
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

}
