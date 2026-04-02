package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        // EP: standard successful command feedback
        Platform.runLater(() -> resultDisplay.setFeedbackToUser("Listed all persons"));
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(resultTextArea.getText().contains("[OK]"));
    }

    @Test
    public void setFeedbackToUser_errorMessage_showsWarningIcon() {
        // EP: standard error message containing error keywords
        Platform.runLater(() -> resultDisplay.setFeedbackToUser("Unknown command entered"));
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(resultTextArea.getText().contains("[!!]"));
    }

    @Test
    public void setFeedbackToUser_withSummary_hitSummaryBranch() {
        // EP: feedback containing "Summary:" keyword (multi-line)
        Platform.runLater(() -> {
            // This triggers the 'if' block and the 'DIVIDER' lines in ResultDisplay.java
            resultDisplay.setFeedbackToUser("Success\nSummary: 1 Trip");
            String text = resultTextArea.getText();
            assertTrue(text.contains("TRIP STATS"));
            assertTrue(text.contains("---"));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void setFeedbackToUser_summaryWithoutSecondLine_formatsWithoutTripStats() {
        // EP: feedback containing summary keyword on the first line
        Platform.runLater(() -> {
            resultDisplay.setFeedbackToUser("Summary: 1 Trip");
            String text = resultTextArea.getText();
            assertTrue(text.contains("[OK]"));
            assertTrue(text.contains("Summary: 1 Trip"));
            assertFalse(text.contains("TRIP STATS |"));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void setFeedbackToUser_summaryWithoutSecondLine_doesNotAppendTripStats() {
        // EP: summary keyword with trailing colon but no content
        Platform.runLater(() -> {
            resultDisplay.setFeedbackToUser("Summary:");
            String text = resultTextArea.getText();
            assertTrue(text.contains("[OK]"));
            assertTrue(text.contains("Summary:"));
            assertTrue(text.contains("--------------------------------------------------"));
            assertFalse(text.contains("TRIP STATS |"));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void setFeedbackToUser_errorMessageWithMust_showsWarningIcon() {
        // EP: specific error keyword "must"
        Platform.runLater(() -> resultDisplay.setFeedbackToUser("Index must be a positive integer"));
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(resultTextArea.getText().contains("[!!]"));
    }

    @Test
    public void setFeedbackToUser_errorMessageWithDuplicate_showsWarningIcon() {
        // EP: specific error keyword "duplicate"
        Platform.runLater(() -> resultDisplay.setFeedbackToUser("Duplicate trip found"));
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(resultTextArea.getText().contains("[!!]"));
    }

    @Test
    public void setFeedbackToUser_nonSummaryMessage_showsOriginalBody() {
        // EP: message with numbers but no special summary formatting
        Platform.runLater(() -> {
            resultDisplay.setFeedbackToUser("Deleted 2 trips.");
            String text = resultTextArea.getText();
            assertTrue(text.contains("[OK]"));
            assertTrue(text.contains("Deleted 2 trips."));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void setFeedbackToUser_helpCommandVariations_showsSuccessIcon() {
        // EP: help command feedback containing "Usage" or "Parameters" (should ignore error keywords)
        Platform.runLater(() -> {
            // Test 'Parameters:' branch
            resultDisplay.setFeedbackToUser("Parameters: n/NAME");
            assertTrue(resultTextArea.getText().contains("[OK]"));

            // Test 'Example:' branch
            resultDisplay.setFeedbackToUser("Example: add n/Tokyo");
            assertTrue(resultTextArea.getText().contains("[OK]"));

            // Test 'e.g.' branch
            resultDisplay.setFeedbackToUser("e.g. add n/Tokyo");
            assertTrue(resultTextArea.getText().contains("[OK]"));

            // Test 'Usage:' branch
            resultDisplay.setFeedbackToUser("Usage: list sort/KEY");
            assertTrue(resultTextArea.getText().contains("[OK]"));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void setFeedbackToUser_helpCommand_showsSuccessIcon() {
        // EP: complex multi-line help documentation
        Platform.runLater(() -> {
            String helpFeedback = "add: Adds a trip.\nParameters: n/NAME\nExample: add n/Tokyo";
            resultDisplay.setFeedbackToUser(helpFeedback);

            String text = resultTextArea.getText();
            assertTrue(text.contains("[OK]"), "Help should show success icon. Actual: " + text);
            assertFalse(text.contains("[!!]"), "Help should NOT show error icon.");
        });
        WaitForAsyncUtils.waitForFxEvents();
    }
}
