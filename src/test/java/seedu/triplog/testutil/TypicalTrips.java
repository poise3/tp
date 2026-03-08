package seedu.triplog.testutil;

import static seedu.triplog.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_END_DATE_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_END_DATE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_START_DATE_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_START_DATE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.triplog.model.TripLog;
import seedu.triplog.model.person.Trip;

/**
 * A utility class containing a list of {@code Trip} objects to be used in tests.
 */
public class TypicalTrips {

    public static final Trip ALICE = new TripBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253")
            .withTags("friends")
            .withStart("2026-01-01").withEnd("2026-01-10").build();
    public static final Trip BENSON = new TripBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withTags("owesMoney", "friends")
            .withStart("2026-01-01").withEnd("2026-01-10").build();
    public static final Trip CARL = new TripBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street")
            .withStart("2026-01-01").withEnd("2026-01-10").build();
    public static final Trip DANIEL = new TripBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").withTags("friends")
            .withStart("2026-01-01").withEnd("2026-01-10").build();
    public static final Trip ELLE = new TripBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave")
            .withStart("2026-01-01").withEnd("2026-01-10").build();
    public static final Trip FIONA = new TripBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo")
            .withStart("2026-01-01").withEnd("2026-01-10").build();
    public static final Trip GEORGE = new TripBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street")
            .withStart("2026-01-01").withEnd("2026-01-10").build();

    // Manually added
    public static final Trip HOON = new TripBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").build();
    public static final Trip IDA = new TripBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - Trip's details found in {@code CommandTestUtil}
    public static final Trip AMY = new TripBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND)
            .withStart(VALID_START_DATE_AMY)
            .withEnd(VALID_END_DATE_AMY)
            .build();
    public static final Trip BOB = new TripBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .withStart(VALID_START_DATE_BOB)
            .withEnd(VALID_END_DATE_BOB)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier";

    private TypicalTrips() {}

    /**
     * Returns a {@code TripLog} with all the typical trips.
     */
    public static TripLog getTypicalTripLog() {
        TripLog tl = new TripLog();
        for (Trip trip : getTypicalTrips()) {
            tl.addTrip(trip);
        }
        return tl;
    }

    public static List<Trip> getTypicalTrips() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
