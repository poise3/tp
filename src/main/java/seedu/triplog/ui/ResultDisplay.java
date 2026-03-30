package seedu.triplog.ui;

import static java.util.Objects.requireNonNull;

import java.util.stream.Stream;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A ui component that displays command feedback and formatted trip summaries.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";

    private static final String SUCCESS_ICON = "[OK]  ";
    private static final String ERROR_ICON = "[!!]  ";
    private static final String DIVIDER = "--------------------------------------------------";
    private static final String SUMMARY_KEYWORD = "Summary:";

    @FXML
    private TextArea resultDisplay;

    /**
     * Constructs a {@code ResultDisplay}.
     */
    public ResultDisplay() {
        super(FXML);
        resultDisplay.setWrapText(true);
    }

    /**
     * Sets the feedback to the user with enhanced formatting for the summary.
     *
     * @param feedbackToUser The raw feedback string from the command execution.
     */
    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);

        String formattedMessage = formatFeedback(feedbackToUser);
        resultDisplay.setText(formattedMessage);
        resultDisplay.setScrollTop(0.0);
    }

    /**
     * Formats the raw feedback string into a structured dashboard.
     * Enforces SLAP by delegating sub-tasks to helper methods.
     *
     * @param feedback The raw feedback string.
     * @return A formatted string with icons and dividers.
     */
    private String formatFeedback(String feedback) {
        String icon = getIcon(feedback);
        String body = hasSummary(feedback) ? formatSummaryBlock(feedback) : feedback;
        return icon + body;
    }

    /**
     * Determines the appropriate status icon based on the feedback content.
     */
    private String getIcon(String feedback) {
        return isError(feedback) ? ERROR_ICON : SUCCESS_ICON;
    }

    /**
     * Returns true if the feedback contains a trip summary.
     */
    private boolean hasSummary(String feedback) {
        return feedback.contains(SUMMARY_KEYWORD);
    }

    /**
     * Formats a feedback string that contains a summary into a structured block.
     */
    private String formatSummaryBlock(String feedback) {
        StringBuilder sb = new StringBuilder();
        String[] parts = feedback.split("\n", 2);

        String message = parts[0];
        String rawSummary = parts.length > 1 ? parts[1] : "";

        sb.append(message).append("\n");
        sb.append(DIVIDER).append("\n");

        if (!rawSummary.isEmpty()) {
            sb.append("TRIP STATS | ").append(rawSummary.replace(SUMMARY_KEYWORD + " ", ""));
        }

        return sb.toString();
    }

    /**
     * Returns true if the message indicates a command failure.
     *
     * @param message The feedback message to check.
     * @return True if the message contains error-related keywords.
     */
    private boolean isError(String message) {
        String lower = message.toLowerCase();
        return Stream.of("invalid", "unknown", "error", "cannot", "failed", "exception",
                        "must", "no such", "not allowed", "insufficient", "duplicate")
                .anyMatch(lower::contains);
    }
}
