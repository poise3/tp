package seedu.triplog.ui;

import static java.util.Objects.requireNonNull;

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
     * Splits the "Summary" line from the "Message" line for better hierarchy.
     *
     * @param feedback The raw feedback string.
     * @return A formatted string with icons and dividers.
     */
    private String formatFeedback(String feedback) {
        StringBuilder sb = new StringBuilder();

        boolean isError = isError(feedback);
        sb.append(isError ? ERROR_ICON : SUCCESS_ICON);

        if (feedback.contains("Summary:")) {
            String[] parts = feedback.split("\n", 2);
            String message = parts[0];
            String summary = parts.length > 1 ? parts[1] : "";

            sb.append(message).append("\n");
            sb.append(DIVIDER).append("\n");

            if (!summary.isEmpty()) {
                sb.append("TRIP STATS | ").append(summary.replace("Summary: ", ""));
            }
        } else {
            sb.append(feedback);
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
        return lower.contains("invalid") || lower.contains("unknown") || lower.contains("error");
    }
}
