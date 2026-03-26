package seedu.triplog.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import seedu.triplog.commons.core.LogsCenter;
import seedu.triplog.logic.commands.HelpCommand;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String PREFIX_NOTE =
            "Options use the prefix/ format — a short prefix followed immediately by a slash.\n"
                    + "Values with spaces do not need quotes (e.g., n/New York is valid).\n"
                    + "e.g.  sd/2026-03-01   ed/2026-03-10";

    public static final String EXIT_NOTE =
            "To exit the help window, press Q or ESCAPE, or click the close button.";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Label prefixNote;

    @FXML
    private Label addUsage;

    @FXML
    private Label editUsage;

    @FXML
    private Label deleteUsage;

    @FXML
    private Label tagUsage;

    @FXML
    private Label findUsage;

    @FXML
    private Label filterUsage;

    @FXML
    private Label listUsage;

    @FXML
    private Label exitNote;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, requireNonNullRoot(root));
        logger.fine("Creating a new HelpWindow with provided root stage.");
        prefixNote.setText(PREFIX_NOTE);
        addUsage.setText(HelpCommand.ADD_USAGE);
        editUsage.setText(HelpCommand.EDIT_USAGE);
        deleteUsage.setText(HelpCommand.DELETE_USAGE);
        tagUsage.setText(HelpCommand.TAG_USAGE);
        findUsage.setText(HelpCommand.FIND_USAGE);
        filterUsage.setText(HelpCommand.FILTER_USAGE);
        listUsage.setText(HelpCommand.LIST_USAGE);
        exitNote.setText(EXIT_NOTE);
        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (isCloseKey(event.getCode())) {
                hide();
                event.consume();
            }
        });
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
        logger.fine("Initializing help page about the application.");
    }

    /**
     * Ensures that the root stage is not null before passing it to the superclass constructor.
     * @param root The root stage to check.
     * @return The non-null root stage.
     * @throws IllegalArgumentException if the root stage is null.
     */
    private static Stage requireNonNullRoot(Stage root) {
        if (root == null) {
            throw new IllegalArgumentException("Root stage cannot be null");
        }
        return root;
    }

    /**
     * Returns true if the given key code should close the help window.
     */
    static boolean isCloseKey(KeyCode code) {
        assert code != null : "code should not be null";
        return code == KeyCode.Q || code == KeyCode.ESCAPE;
    }

    /**
     * Shows the help window.
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        logger.fine("Hiding help page about the application.");
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        logger.fine("Focusing on help page about the application.");
        getRoot().requestFocus();
    }
}
