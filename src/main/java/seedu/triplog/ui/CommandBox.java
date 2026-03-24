package seedu.triplog.ui;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import seedu.triplog.logic.commands.CommandResult;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    private static final String FXML = "CommandBox.fxml";

    private static final String STYLE_SUCCESS_UNDERLINE = "-fx-border-color: transparent transparent "
            + "#00ffcc transparent; "
            + "-fx-border-width: 2; "
            + "-fx-text-fill: #00ffcc;";

    private static final String STYLE_ERROR_UNDERLINE = "-fx-border-color: transparent transparent "
            + "#ff4d4d transparent; "
            + "-fx-border-width: 2; "
            + "-fx-text-fill: #ff4d4d;";

    private final CommandExecutor commandExecutor;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        commandTextField.textProperty().addListener((unused1, unused2,
                                                     unused3) -> resetStyle());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.isEmpty()) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
            applySuccessStyle();
        } catch (CommandException | ParseException e) {
            applyErrorStyle();
            applyShakeAnimation();
        }
    }

    /**
     * Resets the command box to the default state.
     */
    private void resetStyle() {
        commandTextField.setStyle("-fx-text-fill: white;");
    }

    /**
     * Applies a Neon Cyan underline and text color for success.
     */
    private void applySuccessStyle() {
        commandTextField.setStyle(STYLE_SUCCESS_UNDERLINE);
    }

    /**
     * Applies a Bright Red underline, text color, and shakes for errors.
     */
    private void applyErrorStyle() {
        commandTextField.setStyle(STYLE_ERROR_UNDERLINE);
    }

    /**
     * Applies a horizontal shake animation to provide haptic-style feedback on error.
     */
    private void applyShakeAnimation() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), commandTextField);
        tt.setFromX(0);
        tt.setByX(8);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.setOnFinished(event -> commandTextField.setTranslateX(0));
        tt.play();
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }
}
