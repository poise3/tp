package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Verifies that the ResultDisplay correctly formats messages and icons.
 */
@ExtendWith(ApplicationExtension.class)
public class ResultDisplayTest {

    private ResultDisplay resultDisplay;
    private TextArea resultTextArea;

    @Start
    public void start(Stage stage) {
        resultDisplay = new ResultDisplay();
        stage.setScene(new Scene(resultDisplay.getRoot()));
        stage.show();

        resultTextArea = (TextArea) resultDisplay.getRoot().lookup("#resultDisplay");
    }

    @Test
    public void setFeedbackToUser_successMessage_showsSuccessIcon() {
        resultDisplay.setFeedbackToUser("Listed all persons");
        assertTrue(resultTextArea.getText().contains("[OK]"));
    }

    @Test
    public void setFeedbackToUser_errorMessage_showsWarningIcon() {
        resultDisplay.setFeedbackToUser("Unknown command entered");
        assertTrue(resultTextArea.getText().contains("[!!]"));
    }
}
