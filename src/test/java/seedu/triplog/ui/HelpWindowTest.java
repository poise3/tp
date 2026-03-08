package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;

public class HelpWindowTest {

    @BeforeAll
    static void initToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            latch.countDown();
        }
        assertTrue(latch.await(10, TimeUnit.SECONDS), "JavaFX toolkit did not start");
    }

    /**
     * Runs the given task on the JavaFX Application Thread and waits for it to complete.
     * @throws InterruptedException if the current thread is interrupted while waiting
     * @throws Exception if the task fails
     */
    public void runOnFxThreadAndWait(Runnable task) throws Exception {
        if (Platform.isFxApplicationThread()) {
            task.run();
            return;
        }
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                task.run();
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout waiting for task to complete");
        }
    }

    @Test
    public void constructor_createsHelpWindow() throws Exception {
        runOnFxThreadAndWait(() -> {
            HelpWindow hw = new HelpWindow();
            assertNotNull(hw);
            hw.hide();
        });
    }

    @Test
    public void showHideFocus_lifecycle() throws Exception {
        runOnFxThreadAndWait(() -> {
            HelpWindow hw = new HelpWindow();
            hw.show();
            assertTrue(hw.isShowing());
            hw.focus();
            hw.hide();
            assertFalse(hw.isShowing());
        });
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

