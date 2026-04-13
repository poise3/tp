package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalTrips.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.triplog.commons.core.GuiSettings;
import seedu.triplog.logic.Messages;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.ReadOnlyUserPrefs;
import seedu.triplog.model.TripLog;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.testutil.TripBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Trip validTrip = new TripBuilder().build();

        CommandResult commandResult = new AddCommand(validTrip).execute(modelStub);

        String expectedSummary = TripSummaryUtil.calculateSummary(modelStub.getFilteredTripList());
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validTrip), expectedSummary),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validTrip), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Trip validTrip = new TripBuilder().build();
        AddCommand addCommand = new AddCommand(validTrip);
        ModelStub modelStub = new ModelStubWithPerson(validTrip);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_TRIP, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Trip alice = new TripBuilder().withName("Alice").build();
        Trip bob = new TripBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different trip -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getTripLogFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTripLogFilePath(Path tripLogFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTrip(Trip trip) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTripLog(ReadOnlyTripLog newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyTripLog getTripLog() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTrip(Trip trip) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTripExcluding(Trip trip, Trip otherTrip) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTrip(Trip target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTrip(Trip target, Trip editedTrip) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Trip> getFilteredTripList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTripList(Predicate<Trip> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Trip> getSortedTripList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateSortedTripList(Comparator<Trip> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getLastSortDescription() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setLastSortDescription(String description) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single trip.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Trip trip;

        ModelStubWithPerson(Trip trip) {
            requireNonNull(trip);
            this.trip = trip;
        }

        @Override
        public boolean hasTrip(Trip trip) {
            requireNonNull(trip);
            return this.trip.isSameTrip(trip);
        }
    }

    /**
     * A Model stub that always accept the trip being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Trip> personsAdded = new ArrayList<>();

        @Override
        public boolean hasTrip(Trip trip) {
            requireNonNull(trip);
            return personsAdded.stream().anyMatch(trip::isSameTrip);
        }

        @Override
        public void addTrip(Trip trip) {
            requireNonNull(trip);
            personsAdded.add(trip);
        }

        @Override
        public void updateFilteredTripList(Predicate<Trip> predicate) {
            // No action needed for stub verification
        }

        @Override
        public ObservableList<Trip> getFilteredTripList() {
            return FXCollections.observableList(personsAdded);
        }

        @Override
        public ReadOnlyTripLog getTripLog() {
            return new TripLog();
        }
    }

}
