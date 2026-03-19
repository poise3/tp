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

    public static final String USERGUIDE_URL = "https://ay2526s2-cs2103-f13-2.github.io/tp/UserGuide.html";

    public static final String PREFIX_NOTE =
            "Options use the prefix/ format — a short prefix followed immediately by a slash.\n"
                    + "Values with spaces do not need quotes (e.g., n/New York is valid).\n"
                    + "e.g.  sd/2026-03-01   ed/2026-03-10";

    public static final String ADD_USAGE =
            "add n/<destination> [p/<phone>] [e/<email>] [a/<address>] "
                    + "[sd/<start-date>] [ed/<end-date>] [t/<tag>]...\n"
                    + "  Records a new trip. Items in [square brackets] are optional. Dates must be YYYY-MM-DD.\n"
                    + "  e.g. add n/Tokyo sd/2026-03-01 t/food";

    public static final String EDIT_USAGE =
            "edit INDEX [n/<destination>] [p/<phone>] [e/<email>] [a/<address>] "
                    + "[sd/<start-date>] [ed/<end-date>] [t/<tag>]...\n"
                    + "  Edits the trip at the specified index. At least one field must be provided.\n"
                    + "  e.g. edit 1 n/Paris sd/2026-05-01";

    public static final String DELETE_USAGE =
            "delete <INDEX> | <START-END> | <PREFIX/VALUE>\n"
                    + "  Removes trip(s) from the currently displayed list.\n"
                    + "  INDEX must be a positive integer (1, 2, 3, …).\n"
                    + "  START must be <= END for range deletion.\n"
                    + "  Only one PREFIX can be used for field deletion.\n"
                    + "  e.g.  delete 2\n"
                    + "        delete 1-3\n"
                    + "        delete n/Tokyo\n"
                    + "        delete t/family";

    public static final String TAG_USAGE =
            "tag <index> <tag-name>\n"
                    + "  Adds a keyword tag to an existing trip.\n"
                    + "  tag-name must be alphanumeric and may contain spaces.\n"
                    + "  e.g.  tag 1 adventure    or    tag 1 night market";

    public static final String FIND_USAGE =
            "find <KEYWORD> [MORE_KEYWORDS]...\n"
                    + "  Finds trips whose names contain any of the given keywords.\n"
                    + "  e.g. find Tokyo Osaka";

    public static final String FILTER_USAGE =
            "filter sd/<start-date> ed/<end-date>\n"
                    + "  Filters trips occurring within the specified date range.\n"
                    + "  e.g. filter sd/2026-01-01 ed/2026-03-31";

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
        editUsage.setText(EDIT_USAGE);
        deleteUsage.setText(DELETE_USAGE);
        tagUsage.setText(TAG_USAGE);
        findUsage.setText(FIND_USAGE);
        filterUsage.setText(FILTER_USAGE);
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
