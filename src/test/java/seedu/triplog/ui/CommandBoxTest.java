package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import seedu.triplog.logic.commands.CommandResult;
import seedu.triplog.logic.parser.exceptions.ParseException;

/**
 * Tests the visual state transitions of the CommandBox.
 */
@ExtendWith(ApplicationExtension.class)
public class CommandBoxTest {

    private TextField commandTextField;
    private CommandBox commandBox;

    @Start
    public void start(Stage stage) {
        commandBox = new CommandBox(commandText -> {
            if (commandText.equals("success")) {
                return new CommandResult("Success Message");
            }
            // Use ParseException so the CommandBox catch block actually triggers
            throw new ParseException("Failure Message");
        });

        stage.setScene(new Scene(commandBox.getRoot()));
        stage.show();

        commandTextField = (TextField) commandBox.getRoot().lookup("#commandTextField");
    }

    /**
     * Tests that the success style is applied correctly.
     */
    @Test
    public void handleCommandEntered_success_styleApplied() {
        commandTextField.setText("success");
        fireEnterKey(commandTextField);

        assertTrue(commandTextField.getStyle().contains("#00ffcc"));
        assertEquals("", commandTextField.getText());
    }

    /**
     * Tests that the error style is applied and then reset upon typing.
     */
    @Test
    public void handleCommandEntered_failure_styleAppliedAndResets() {
        // 1. Trigger Failure (this will now be caught by the CommandBox)
        commandTextField.setText("fail");
        fireEnterKey(commandTextField);

        assertTrue(commandTextField.getStyle().contains("#ff4d4d"));

        // 2. Simulate typing to fix - style should reset to white
        commandTextField.setText("fai");

        assertTrue(commandTextField.getStyle().contains("white"));
        assertFalse(commandTextField.getStyle().contains("#ff4d4d"));
    }

    /**
     * Helper to fire an ENTER key event to the text field.
     */
    private void fireEnterKey(TextField textField) {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "", "", KeyCode.ENTER,
                false, false, false, false
        );
        textField.fireEvent(event);
    }
}
