package seedu.triplog.commons.core;

/**
 * Contains usage strings for all supported commands.
 */
public class CommandUsage {

    public static final String ADD_USAGE =
            "add n/<destination> [p/<phone>] [e/<email>] [a/<address>] "
                    + "[sd/<start-date>] [ed/<end-date>] [t/<tag>]...\n"
                    + "  Records a new trip. Items in [square brackets] are optional. Dates must be YYYY-MM-DD.\n"
                    + "  e.g. add n/Tokyo sd/2026-03-01 t/food";

    public static final String EDIT_USAGE =
            "edit <INDEX> [n/<destination>] [p/<phone>] [e/<email>] [a/<address>] "
                    + "[sd/<start-date>] [ed/<end-date>] [t/<tag>]...\n"
                    + "  Edits the trip at the specified index. At least one field must be provided.\n"
                    + "  e.g. edit 1 n/Paris sd/2026-05-01";

    public static final String DELETE_USAGE =
            "delete <INDEX> | <START-END> | <PREFIX/VALUE> | sd/<start-date> ed/<end-date>\n"
                    + "  Removes trip(s) from the currently displayed list.\n"
                    + "  All delete operations first show a preview before confirmation.\n"
                    + "  Press Enter again to confirm deletion.\n"
                    + "  INDEX must be a positive integer (1, 2, 3, …).\n"
                    + "  START must be <= END for range deletion.\n"
                    + "  Only one PREFIX can be used for field deletion.\n"
                    + "  Dates must be YYYY-MM-DD.\n"
                    + "  e.g.  delete 2\n"
                    + "        delete 1-3\n"
                    + "        delete n/Tokyo\n"
                    + "        delete t/family\n"
                    + "        delete sd/2026-03-01 ed/2026-05-10";

    public static final String TAG_USAGE =
            "tag <INDEX> <tag-name>\n"
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
            "list [sort/<key>]\n"
                    + "  Displays all trips with a summary (Upcoming, Ongoing, Completed, Planning).\n"
                    + "  Optional sort keys: name, start, end, len.\n"
                    + "  e.g. list, list sort/name, list sort/len";

    public static final String CLEAR_USAGE =
            "clear\n"
                    + "  Deletes all trips from TripLog.\n"
                    + "  e.g. clear";

    public static final String EXIT_USAGE =
            "exit\n"
                    + "  Exits the application.\n"
                    + "  e.g. exit";

}
