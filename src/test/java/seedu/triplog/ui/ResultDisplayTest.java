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

    @Test
    public void setFeedbackToUser_summaryWithoutSecondLine_formatsWithoutTripStats() {
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
        Platform.runLater(() -> resultDisplay.setFeedbackToUser("Index must be a positive integer"));
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(resultTextArea.getText().contains("[!!]"));
    }

    @Test
    public void setFeedbackToUser_errorMessageWithDuplicate_showsWarningIcon() {
        Platform.runLater(() -> resultDisplay.setFeedbackToUser("Duplicate trip found"));
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(resultTextArea.getText().contains("[!!]"));
    }

    @Test
    public void setFeedbackToUser_nonSummaryMessage_showsOriginalBody() {
        Platform.runLater(() -> {
            resultDisplay.setFeedbackToUser("Deleted 2 trips.");
            String text = resultTextArea.getText();
            assertTrue(text.contains("[OK]"));
            assertTrue(text.contains("Deleted 2 trips."));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }
}
