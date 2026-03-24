package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Verifies that the ResultDisplay correctly formats messages and icons.
 */
@ExtendWith(ApplicationExtension.class)
public class ResultDisplayTest {

    private ResultDisplay resultDisplay;
    private TextArea resultTextArea;

    @BeforeAll
    public static void initJavaFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Platform already started
        }
    }

    @BeforeEach
    public void setUp() {
        resultDisplay = new ResultDisplay();
        resultTextArea = (TextArea) resultDisplay.getRoot().lookup("#resultDisplay");
    }

    @Test
    public void setFeedbackToUser_successMessage_showsSuccessIcon() {
        Platform.runLater(() -> resultDisplay.setFeedbackToUser("Listed all persons"));
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(resultTextArea.getText().contains("[OK]"));
    }

    @Test
    public void setFeedbackToUser_errorMessage_showsWarningIcon() {
        Platform.runLater(() -> resultDisplay.setFeedbackToUser("Unknown command entered"));
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(resultTextArea.getText().contains("[!!]"));
    }

    @Test
    public void setFeedbackToUser_withSummary_hitSummaryBranch() {
        Platform.runLater(() -> {
            // This triggers the 'if' block and the 'DIVIDER' lines in ResultDisplay.java
            resultDisplay.setFeedbackToUser("Success\nSummary: 1 Trip");
            String text = resultTextArea.getText();
            assertTrue(text.contains("TRIP STATS"));
            assertTrue(text.contains("---"));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }
}
