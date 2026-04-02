package seedu.triplog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.triplog.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.triplog.logic.commands.CommandTestUtil.showTripAtIndex;
import static seedu.triplog.testutil.TypicalIndexes.INDEX_FIRST_TRIP;
import static seedu.triplog.testutil.TypicalTrips.getTypicalTripLog;

import java.time.LocalDate;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.triplog.model.Model;
import seedu.triplog.model.ModelManager;
import seedu.triplog.model.TripLog;
import seedu.triplog.model.UserPrefs;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.testutil.TripBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalTripLog(), new UserPrefs(), Trip.CHRONOLOGICAL_COMPARATOR);
        expectedModel = new ModelManager(model.getTripLog(), new UserPrefs(), Trip.CHRONOLOGICAL_COMPARATOR);
    }

    @Test
    public void execute_listIsNotFiltered_showsSameListWithSummary() {
        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, ListCommand.SORT_DESC_START,
                expectedSummary);

        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverythingWithSummary() {
        showTripAtIndex(model, INDEX_FIRST_TRIP);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, ListCommand.SORT_DESC_START,
                expectedSummary);

        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_variedTripStatuses_summaryCorrect() {
        TripLog tripLog = new TripLog();
        LocalDate today = LocalDate.now();

        // EP: trip starts in the future
        Trip upcoming = new TripBuilder().withName("Upcoming")
                .withStart(today.plusDays(7).toString())
                .withEnd(today.plusDays(10).toString()).build();

        // EP: current date is within trip range
        Trip ongoing = new TripBuilder().withName("Ongoing")
                .withStart(today.minusDays(1).toString())
                .withEnd(today.plusDays(1).toString()).build();

        // EP: trip end date is in the past
        Trip completed = new TripBuilder().withName("Completed")
                .withStart(today.minusDays(10).toString())
                .withEnd(today.minusDays(5).toString()).build();

        // EP: trip has no start date
        Trip planning = new TripBuilder().withName("Planning").build();
        Trip planningNull = new Trip(planning.getName(), planning.getPhone(),
                planning.getEmail(), planning.getAddress(), planning.getTags(),
                null, null);

        tripLog.addTrip(upcoming);
        tripLog.addTrip(ongoing);
        tripLog.addTrip(completed);
        tripLog.addTrip(planningNull);

        Model model = new ModelManager(tripLog, new UserPrefs(), Trip.CHRONOLOGICAL_COMPARATOR);
        Model expectedModel = new ModelManager(tripLog, new UserPrefs(), Trip.CHRONOLOGICAL_COMPARATOR);

        String expectedSummary = "Summary: 1 Upcoming, 1 Ongoing, 1 Completed, 1 Planning";
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, ListCommand.SORT_DESC_START,
                expectedSummary);

        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sortBySortName_success() {
        String description = ListCommand.SORT_DESC_NAME;
        expectedModel.setLastSortDescription(description);
        expectedModel.updateSortedTripList(Comparator.comparing(Trip::getNameLowerCase));

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, description, expectedSummary);
        assertCommandSuccess(new ListCommand("name"), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sortBySortLen_success() {
        String description = ListCommand.SORT_DESC_LEN;
        expectedModel.setLastSortDescription(description);
        Comparator<Trip> expectedComparator = ((Comparator<Trip>) (t1, t2) -> Long.compare(t2.getDurationInDays(),
                t1.getDurationInDays())).thenComparing(Trip::getNameLowerCase);
        expectedModel.updateSortedTripList(expectedComparator);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, description, expectedSummary);
        assertCommandSuccess(new ListCommand("len"), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sortBySortEnd_success() {
        String description = ListCommand.SORT_DESC_END;
        expectedModel.setLastSortDescription(description);
        expectedModel.updateSortedTripList(Comparator.comparing(Trip::getEndDateDisplay,
                Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Trip::getNameLowerCase));

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, description, expectedSummary);
        assertCommandSuccess(new ListCommand("end"), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidSortKey_throwsCommandException() {
        // EP: invalid sort key
        assertCommandFailure(new ListCommand("price"), model, ListCommand.MESSAGE_INVALID_SORT_KEY);
    }

    @Test
    public void execute_persistenceCheck_sortOrderMaintained() {
        Comparator<Trip> nameComparator = Comparator.comparing(Trip::getNameLowerCase);
        String nameDescription = ListCommand.SORT_DESC_NAME;

        model.updateSortedTripList(nameComparator);
        model.setLastSortDescription(nameDescription);

        expectedModel.updateSortedTripList(nameComparator);
        expectedModel.setLastSortDescription(nameDescription);

        String expectedSummary = TripSummaryUtil.calculateSummary(model.getFilteredTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, nameDescription, expectedSummary);

        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sortTripsWithNullDates_success() {
        TripLog tripLog = new TripLog();
        Trip tripWithDates = new TripBuilder().withName("A")
                .withStart("2026-01-01").withEnd("2026-01-10").build();
        Trip tripWithoutDates = new TripBuilder().withName("B")
                .withStart(null).withEnd(null).build();

        tripLog.addTrip(tripWithDates);
        tripLog.addTrip(tripWithoutDates);

        Model model = new ModelManager(tripLog, new UserPrefs(), Trip.CHRONOLOGICAL_COMPARATOR);
        Model expectedModel = new ModelManager(tripLog, new UserPrefs(), Trip.CHRONOLOGICAL_COMPARATOR);

        String expectedSummary = TripSummaryUtil.calculateSummary(expectedModel.getFilteredTripList());

        // Test Start Sort
        String expectedMessageStart = String.format(ListCommand.MESSAGE_SUCCESS, ListCommand.SORT_DESC_START,
                expectedSummary);
        expectedModel.setLastSortDescription(ListCommand.SORT_DESC_START);
        assertCommandSuccess(new ListCommand("start"), model, expectedMessageStart, expectedModel);

        // Test End Sort
        String expectedMessageEnd = String.format(ListCommand.MESSAGE_SUCCESS, ListCommand.SORT_DESC_END,
                expectedSummary);
        expectedModel.setLastSortDescription(ListCommand.SORT_DESC_END);
        expectedModel.updateSortedTripList(Comparator.comparing(Trip::getEndDateDisplay,
                Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Trip::getNameLowerCase));
        assertCommandSuccess(new ListCommand("end"), model, expectedMessageEnd, expectedModel);

        // Test Duration Sort
        String expectedMessageLen = String.format(ListCommand.MESSAGE_SUCCESS, ListCommand.SORT_DESC_LEN,
                expectedSummary);
        expectedModel.setLastSortDescription(ListCommand.SORT_DESC_LEN);
        Comparator<Trip> lenComparator = ((Comparator<Trip>) (t1, t2) -> Long.compare(t2.getDurationInDays(),
                t1.getDurationInDays())).thenComparing(Trip::getNameLowerCase);
        expectedModel.updateSortedTripList(lenComparator);
        assertCommandSuccess(new ListCommand("len"), model, expectedMessageLen, expectedModel);
    }

    @Test
    public void execute_sortTieBreaker_alphabeticalFallBack() {
        TripLog tripLog = new TripLog();
        // EP: trips have identical start/end dates
        Trip zebraTrip = new TripBuilder().withName("Zebra").withStart("2026-06-01")
                .withEnd("2026-06-10").build();
        Trip appleTrip = new TripBuilder().withName("Apple").withStart("2026-06-01")
                .withEnd("2026-06-10").build();

        tripLog.addTrip(zebraTrip);
        tripLog.addTrip(appleTrip);

        Model model = new ModelManager(tripLog, new UserPrefs(), Trip.CHRONOLOGICAL_COMPARATOR);
        Model expectedModel = new ModelManager(tripLog, new UserPrefs(), Trip.CHRONOLOGICAL_COMPARATOR);

        Comparator<Trip> nameTieBreaker = Comparator.comparing(Trip::getNameLowerCase);
        Comparator<Trip> startComparator = Comparator.comparing(Trip::getStartDateDisplay,
                Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(nameTieBreaker);

        expectedModel.updateSortedTripList(startComparator);
        expectedModel.setLastSortDescription(ListCommand.SORT_DESC_START);

        String summary = TripSummaryUtil.calculateSummary(tripLog.getTripList());
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, ListCommand.SORT_DESC_START, summary);

        assertCommandSuccess(new ListCommand("start"), model, expectedMessage, expectedModel);

        assertTrue(model.getFilteredTripList().get(0).getName().fullName.equals(appleTrip.getName().fullName));
    }

    @Test
    public void getComparatorFromDescription_validDescriptions_returnsCorrectComparator() {
        // EP: valid description strings
        assertNotNull(ListCommand.getComparatorFromDescription(ListCommand.SORT_DESC_NAME));
        assertNotNull(ListCommand.getComparatorFromDescription(ListCommand.SORT_DESC_END));
        assertNotNull(ListCommand.getComparatorFromDescription(ListCommand.SORT_DESC_LEN));
        assertNotNull(ListCommand.getComparatorFromDescription(ListCommand.SORT_DESC_START));

        // EP: null description
        assertNotNull(ListCommand.getComparatorFromDescription(null));

        // EP: unknown description string
        assertNotNull(ListCommand.getComparatorFromDescription("unknown"));
    }

    @Test
    public void equals() {
        ListCommand listNameCommand = new ListCommand("name");
        ListCommand listStartCommand = new ListCommand("start");
        ListCommand listDefaultCommand = new ListCommand();

        // EP: same object
        assertTrue(listNameCommand.equals(listNameCommand));

        // EP: same values
        ListCommand listNameCommandCopy = new ListCommand("name");
        assertTrue(listNameCommand.equals(listNameCommandCopy));

        // EP: different types
        assertFalse(listNameCommand.equals(1));

        // EP: null
        assertFalse(listNameCommand.equals(null));

        // EP: different sortKey
        assertFalse(listNameCommand.equals(listStartCommand));

        // EP: null vs non-null sortKey
        assertFalse(listNameCommand.equals(listDefaultCommand));
    }
}
