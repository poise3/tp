package seedu.triplog.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import seedu.triplog.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://se-education.org/addressbook-level3/UserGuide.html";

    public static final String PREFIX_NOTE =
            "Options use the /key:value format — the / must be followed immediately by the key and a colon.\n"
                    + "Fields with spaces must be wrapped in double quotes (e.g., \"New York\").\n"
                    + "e.g.  /start:2026-03-01   /end:2026-03-10";

    public static final String ADD_USAGE =
            "add /name:<destination> [/phone:<phone>] [/email:<email>] [/addr:<address>] "
                    + "[/start:<start-date>] [/end:<end-date>] [/tag:<tag>]...\n"
                    + "  Records a new trip. Items in [square brackets] are optional. Dates must be YYYY-MM-DD.\n"
                    + "  e.g. add /name:\"Tokyo, Japan\" /start:2026-03-01 /tag:food";

    public static final String DELETE_USAGE =
            "delete <INDEX>\n"
                    + "  Removes the trip at the given list position.\n"
                    + "  INDEX must be a positive integer (1, 2, 3, …)\n"
                    + "  e.g.  delete 2";

    public static final String TAG_USAGE =
            "tag <index> /tag:<tag-name>\n"
                    + "  Adds a keyword tag to an existing trip.\n"
                    + "  tag-name must be alphanumeric. Use quotes for tags with spaces.\n"
                    + "  e.g.  tag 1 /tag:adventure    or    tag 1 /tag:\"night market\"";

    public static final String LIST_USAGE =
            "list\n"
                    + "  Displays all trip entries.\n"
                    + "  e.g.  list";

    public static final String EXIT_NOTE =
            "To exit the help window, press Q or ESCAPE, or click the close button.";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Label prefixNote;

    @FXML
    private Label addUsage;

    @FXML
    private Label deleteUsage;

    @FXML
    private Label tagUsage;

    @FXML
    private Label listUsage;

    @FXML
    private Label helpMessage;

    @FXML
    private Label exitNote;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        prefixNote.setText(PREFIX_NOTE);
        addUsage.setText(ADD_USAGE);
        deleteUsage.setText(DELETE_USAGE);
        tagUsage.setText(TAG_USAGE);
        listUsage.setText(LIST_USAGE);
        helpMessage.setText("");
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
    }

    /**
     * Returns true if the given key code should close the help window.
     */
    static boolean isCloseKey(KeyCode code) {
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
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

}
