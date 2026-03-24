package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

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
        // 1. Trigger Failure
        Platform.runLater(() -> commandTextField.setText("fail"));
        WaitForAsyncUtils.waitForFxEvents();

        invokeHandleCommandEntered();
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(commandTextField.getStyle().contains("#ff4d4d"),
                "Style should contain error color: " + commandTextField.getStyle());

        // 2. Simulate typing to fix
        Platform.runLater(() -> commandTextField.setText("fai"));
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(commandTextField.getStyle().contains("white"));
        assertFalse(commandTextField.getStyle().contains("#ff4d4d"));
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
