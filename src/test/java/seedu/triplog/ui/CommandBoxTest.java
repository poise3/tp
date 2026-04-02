package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import seedu.triplog.logic.commands.CommandResult;
import seedu.triplog.logic.parser.exceptions.ParseException;

/**
 * Tests the visual state transitions of the CommandBox.
 */
@ExtendWith(ApplicationExtension.class)
public class CommandBoxTest {

    private TextField commandTextField;
    private CommandBox commandBox;

    @BeforeAll
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @BeforeEach
    public void setUp() {
        commandBox = new CommandBox(commandText -> {
            if (commandText.equals("success")) {
                return new CommandResult("Success Message");
            }
            throw new ParseException("Failure Message");
        });
        commandTextField = (TextField) commandBox.getRoot().lookup("#commandTextField");
    }

    @Test
    public void handleCommandEntered_success_styleApplied() throws Exception {
        // EP: successfully executed command
        Platform.runLater(() -> commandTextField.setText("success"));
        WaitForAsyncUtils.waitForFxEvents();

        // Directly invoke the FXML handler to ensure the logic runs
        invokeHandleCommandEntered();
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(commandTextField.getStyle().contains("#00ffcc"),
                "Style should contain success color: " + commandTextField.getStyle());
        assertEquals("", commandTextField.getText());
    }

    @Test
    public void handleCommandEntered_failure_styleAppliedAndResets() throws Exception {
        // EP: command that fails parsing or execution
        // 1. Trigger Failure
        Platform.runLater(() -> commandTextField.setText("fail"));
        WaitForAsyncUtils.waitForFxEvents();

        invokeHandleCommandEntered();
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(commandTextField.getStyle().contains("#ff4d4d"),
                "Style should contain error color: " + commandTextField.getStyle());

        // EP: user modifies text after an error (resets style)
        // 2. Simulate typing to fix
        Platform.runLater(() -> commandTextField.setText("fai"));
        WaitForAsyncUtils.waitForFxEvents();

        // Updated to check for the dark body text color instead of white
        assertTrue(commandTextField.getStyle().contains("#1A1A1A"),
                "Style should contain reset color #1A1A1A: " + commandTextField.getStyle());
        assertFalse(commandTextField.getStyle().contains("#ff4d4d"));
    }

    @Test
    public void handleCommandEntered_deleteFirstEnter_previewShownTextNotCleared() throws Exception {
        // EP: delete command (first enter - preview phase)
        AtomicInteger previewCount = new AtomicInteger(0);

        CommandBox deleteBox = new CommandBox(commandText -> {
            if (commandText.equals("deletepreview 1")) {
                previewCount.incrementAndGet();
                return new CommandResult("Preview: 1 trip will be deleted.");
            }
            if (commandText.equals("delete 1")) {
                return new CommandResult("Deleted 1 trip.");
            }
            throw new ParseException("Failure Message");
        });

        TextField field = (TextField) deleteBox.getRoot().lookup("#commandTextField");

        Platform.runLater(() -> field.setText("delete 1"));
        WaitForAsyncUtils.waitForFxEvents();

        invokeHandle(deleteBox);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("delete 1", field.getText(),
                "First Enter should show preview only and keep the command text.");
        assertEquals(1, previewCount.get(),
                "Preview command should be triggered exactly once.");
        assertTrue(field.getStyle().contains("#00ffcc"),
                "Style should contain success color: " + field.getStyle());
    }

    @Test
    public void handleCommandEntered_deleteSecondEnter_confirmsAndClearsText() throws Exception {
        // EP: delete command (second enter - confirmation phase)
        AtomicInteger previewCount = new AtomicInteger(0);
        AtomicInteger deleteCount = new AtomicInteger(0);

        CommandBox deleteBox = new CommandBox(commandText -> {
            if (commandText.equals("deletepreview 1")) {
                previewCount.incrementAndGet();
                return new CommandResult("Preview: 1 trip will be deleted.");
            }
            if (commandText.equals("delete 1")) {
                deleteCount.incrementAndGet();
                return new CommandResult("Deleted 1 trip.");
            }
            throw new ParseException("Failure Message");
        });

        TextField field = (TextField) deleteBox.getRoot().lookup("#commandTextField");

        Platform.runLater(() -> field.setText("delete 1"));
        WaitForAsyncUtils.waitForFxEvents();

        // First Enter -> preview
        invokeHandle(deleteBox);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("delete 1", field.getText());

        // Second Enter -> actual delete
        invokeHandle(deleteBox);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("", field.getText(),
                "Second Enter should confirm deletion and clear the command text.");
        assertEquals(1, previewCount.get(),
                "Preview should happen exactly once.");
        assertEquals(1, deleteCount.get(),
                "Actual delete should happen exactly once.");
        assertTrue(field.getStyle().contains("#00ffcc"),
                "Style should contain success color: " + field.getStyle());
    }

    @Test
    public void handleCommandEntered_deleteEditAfterPreview_cancelsConfirmation() throws Exception {
        // EP: delete command edited after preview (cancels pending confirmation)
        AtomicInteger previewOneCount = new AtomicInteger(0);
        AtomicInteger previewTwoCount = new AtomicInteger(0);
        AtomicInteger deleteCount = new AtomicInteger(0);

        CommandBox deleteBox = new CommandBox(commandText -> {
            if (commandText.equals("deletepreview 1")) {
                previewOneCount.incrementAndGet();
                return new CommandResult("Preview: 1 trip will be deleted.");
            }
            if (commandText.equals("deletepreview 2")) {
                previewTwoCount.incrementAndGet();
                return new CommandResult("Preview: 1 trip will be deleted.");
            }
            if (commandText.equals("delete 1") || commandText.equals("delete 2")) {
                deleteCount.incrementAndGet();
                return new CommandResult("Deleted 1 trip.");
            }
            throw new ParseException("Failure Message");
        });

        TextField field = (TextField) deleteBox.getRoot().lookup("#commandTextField");

        Platform.runLater(() -> field.setText("delete 1"));
        WaitForAsyncUtils.waitForFxEvents();

        // First Enter -> preview delete 1
        invokeHandle(deleteBox);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("delete 1", field.getText());

        // User edits command, which should cancel pending confirmation
        Platform.runLater(() -> field.setText("delete 2"));
        WaitForAsyncUtils.waitForFxEvents();

        // Next Enter should preview delete 2, not confirm it
        invokeHandle(deleteBox);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("delete 2", field.getText(),
                "Editing the delete command should cancel previous confirmation.");
        assertEquals(1, previewOneCount.get(),
                "First delete command should have been previewed once.");
        assertEquals(1, previewTwoCount.get(),
                "Edited delete command should be previewed once.");
        assertEquals(0, deleteCount.get(),
                "No actual deletion should happen after editing the command.");
    }

    @Test
    public void handleCommandEntered_emptyCommand_noExecution() throws Exception {
        // EP: empty string input
        AtomicInteger executeCount = new AtomicInteger(0);

        CommandBox box = new CommandBox(commandText -> {
            executeCount.incrementAndGet();
            return new CommandResult("Should not run");
        });

        TextField field = (TextField) box.getRoot().lookup("#commandTextField");

        Platform.runLater(() -> field.setText(""));
        WaitForAsyncUtils.waitForFxEvents();

        invokeHandle(box);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(0, executeCount.get());
        assertEquals("", field.getText());
    }

    @Test
    public void handleCommandEntered_deleteOnly_previewShownTextNotCleared() throws Exception {
        // EP: delete command without arguments (preview only)
        AtomicInteger previewCount = new AtomicInteger(0);

        CommandBox deleteBox = new CommandBox(commandText -> {
            if (commandText.equals("deletepreview")) {
                previewCount.incrementAndGet();
                return new CommandResult("Preview: nothing");
            }
            throw new ParseException("Failure Message");
        });

        TextField field = (TextField) deleteBox.getRoot().lookup("#commandTextField");

        Platform.runLater(() -> field.setText("delete"));
        WaitForAsyncUtils.waitForFxEvents();

        invokeHandle(deleteBox);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("delete", field.getText());
        assertEquals(1, previewCount.get());
        assertTrue(field.getStyle().contains("#00ffcc"));
    }

    private void invokeHandle(CommandBox box) throws Exception {
        Method method = CommandBox.class.getDeclaredMethod("handleCommandEntered");
        method.setAccessible(true);
        Platform.runLater(() -> {
            try {
                method.invoke(box);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Triggers the private handleCommandEntered method via reflection.
     */
    private void invokeHandleCommandEntered() throws Exception {
        Method method = CommandBox.class.getDeclaredMethod("handleCommandEntered");
        method.setAccessible(true);
        Platform.runLater(() -> {
            try {
                method.invoke(commandBox);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
