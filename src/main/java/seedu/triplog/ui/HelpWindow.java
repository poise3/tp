package seedu.triplog.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import seedu.triplog.commons.core.CommandUsage;
import seedu.triplog.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String PREFIX_NOTE =
            "Options use the prefix/ format — a short prefix followed immediately by a slash.\n"
                    + "Values with spaces do not need quotes (e.g., n/New York is valid).\n"
                    + "e.g.  sd/2026-06-01   ed/2026-06-10";

    public static final String EXIT_NOTE =
            "To exit the help window, press Q or ESCAPE, or click the close button.";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Label prefixNote;

    @FXML
    private Label helpCommand;

    @FXML
    private Label helpUsage;

    @FXML
    private Label addCommand;

    @FXML
    private Label addUsage;

    @FXML
    private Label editCommand;

    @FXML
    private Label editUsage;

    @FXML
    private Label deleteCommand;

    @FXML
    private Label deleteUsage;

    @FXML
    private Label tagCommand;

    @FXML
    private Label tagUsage;

    @FXML
    private Label findCommand;

    @FXML
    private Label findUsage;

    @FXML
    private Label filterCommand;

    @FXML
    private Label filterUsage;

    @FXML
    private Label listCommand;

    @FXML
    private Label listUsage;

    @FXML
    private Label clearCommand;

    @FXML
    private Label clearUsage;

    @FXML
    private Label exitCommand;

    @FXML
    private Label exitUsage;

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
        setCommandUsage(helpCommand, helpUsage, CommandUsage.HELP_USAGE);
        setCommandUsage(addCommand, addUsage, CommandUsage.ADD_USAGE);
        setCommandUsage(editCommand, editUsage, CommandUsage.EDIT_USAGE);
        setCommandUsage(deleteCommand, deleteUsage, CommandUsage.DELETE_USAGE);
        setCommandUsage(tagCommand, tagUsage, CommandUsage.TAG_USAGE);
        setCommandUsage(findCommand, findUsage, CommandUsage.FIND_USAGE);
        setCommandUsage(filterCommand, filterUsage, CommandUsage.FILTER_USAGE);
        setCommandUsage(listCommand, listUsage, CommandUsage.LIST_USAGE);
        setCommandUsage(clearCommand, clearUsage, CommandUsage.CLEAR_USAGE);
        setCommandUsage(exitCommand, exitUsage, CommandUsage.EXIT_USAGE);
        exitNote.setText(EXIT_NOTE);
        root.setMinWidth(400);
        root.setMinHeight(300);
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
     * Splits {@code usage} at the first newline and sets the command line (bold) and
     * description into separate labels.
     *
     * @param command     the label to display the command syntax (first line of {@code usage})
     * @param description the label to display the remaining description (lines after the first)
     * @param usage       the full usage string, where the first line is the command syntax
     */
    static void setCommandUsage(Label command, Label description, String usage) {
        String[] parts = usage.split("\n", 2);
        command.setText(parts[0]);
        description.setText(parts.length > 1 ? parts[1] : "");
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
        Stage root = getRoot();
        if (root.isIconified()) {
            root.setIconified(false);
        }
        root.requestFocus();
    }
}
