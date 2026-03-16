package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalTrips.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.triplog.commons.core.GuiSettings;
import seedu.triplog.commons.core.index.Index;
import seedu.triplog.logic.Messages;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.ReadOnlyUserPrefs;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.testutil.TripBuilder;


public class TagCommandTest {

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TagCommand(null, new Tag("leisure")));
        assertThrows(NullPointerException.class, () -> new TagCommand(Index.fromOneBased(1), null));
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        Trip trip = new TripBuilder().build();
        Tag tag = new Tag("leisure");
        ModelStubAcceptingTripUpdate modelStub = new ModelStubAcceptingTripUpdate(trip);

        TagCommand command = new TagCommand(Index.fromOneBased(1), tag);
        CommandResult result = command.execute(modelStub);

        Trip updatedTrip = modelStub.trips.get(0);

        // check tag added
        assertTrue(updatedTrip.getTags().contains(tag));

        // check feedback message
        assertEquals(String.format(TagCommand.MESSAGE_TAG_TRIP_SUCCESS, tag, Messages.format(updatedTrip)),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Trip trip = new TripBuilder().build();
        ModelStubWithTrip modelStub = new ModelStubWithTrip(trip);

        TagCommand command = new TagCommand(Index.fromOneBased(2), new Tag("leisure"));

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_TRIP_DISPLAYED_INDEX, () -> command.execute(modelStub));
    }

    @Test
    public void execute_duplicateTag_throwsCommandException() {
        Tag tag = new Tag("leisure");
        Trip trip = new TripBuilder().withTags("leisure").build();

        ModelStubWithTrip modelStub = new ModelStubWithTrip(trip);

        TagCommand command = new TagCommand(Index.fromOneBased(1), tag);

        assertThrows(CommandException.class,
                TagCommand.MESSAGE_DUPLICATE_TAG, () -> command.execute(modelStub));
    }

    @Test
    public void execute_newTagCreatesDuplicateTrip_throwsCommandException() {
        // 1. Create two trips that are different
        Trip firstTrip = new TripBuilder().withName("Alice").withTags("leisure").build();
        Trip secondTrip = new TripBuilder().withName("Alice").withTags("leisure").build();

        // 2. Add BOTH to your model stub
        ModelStubWithTwoTrips modelStub = new ModelStubWithTwoTrips(firstTrip, secondTrip);

        // 3. Try to tag the second trip with "leisure"
        // Now it matches firstTrip exactly!
        TagCommand command = new TagCommand(Index.fromOneBased(2), new Tag("leisure"));

        assertThrows(CommandException.class,
                TagCommand.MESSAGE_DUPLICATE_TRIP, () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        Index index1 = Index.fromOneBased(1);
        Index index2 = Index.fromOneBased(2);

        Tag tag1 = new Tag("leisure");
        Tag tag2 = new Tag("work");

        TagCommand command1 = new TagCommand(index1, tag1);
        TagCommand command2 = new TagCommand(index1, tag1);
        TagCommand command3 = new TagCommand(index2, tag1);
        TagCommand command4 = new TagCommand(index1, tag2);

        // same object -> true
        assertTrue(command1.equals(command1));

        // same values -> true
        assertTrue(command1.equals(command2));

        // different type -> false
        assertFalse(command1.equals(1));

        // null -> false
        assertFalse(command1.equals(null));

        // different index -> false
        assertFalse(command1.equals(command3));

        // different tag -> false
        assertFalse(command1.equals(command4));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        Tag tag = new Tag("leisure");
        TagCommand tagCommand = new TagCommand(index, tag);

        // ToStringBuilder uses the labels you defined in the method: "targetIndex" and "tag"
        String expected = TagCommand.class.getCanonicalName() + "{targetIndex=" + index + ", tag=" + tag + "}";

        assertEquals(expected, tagCommand.toString());
    }


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
    }

    private class ModelStubWithTrip extends ModelStub {

        private final ArrayList<Trip> trips = new ArrayList<>();

        ModelStubWithTrip(Trip trip) {
            requireNonNull(trip);
            trips.add(trip);
        }

        @Override
        public ObservableList<Trip> getFilteredTripList() {
            return FXCollections.observableArrayList(trips);
        }
    }

    private class ModelStubWithTwoTrips extends ModelStub {
        private final ArrayList<Trip> trips = new ArrayList<>();

        ModelStubWithTwoTrips(Trip trip1, Trip trip2) {
            trips.add(trip1);
            trips.add(trip2);
        }

        @Override
        public boolean hasTrip(Trip trip) {
            requireNonNull(trip);
            return trips.stream().anyMatch(trip::isSameTrip);
        }

        @Override
        public ObservableList<Trip> getFilteredTripList() {
            return FXCollections.observableArrayList(trips);
        }
    }

    private class ModelStubAcceptingTripUpdate extends ModelStub {

        final ArrayList<Trip> trips = new ArrayList<>();

        ModelStubAcceptingTripUpdate(Trip trip) {
            trips.add(trip);
        }

        @Override
        public ObservableList<Trip> getFilteredTripList() {
            return FXCollections.observableArrayList(trips);
        }

        @Override
        public void setTrip(Trip target, Trip editedTrip) {
            trips.set(0, editedTrip);
        }

        @Override
        public boolean hasTrip(Trip trip) {
            return trips.stream().anyMatch(trip::isSameTrip);
        }

        @Override
        public void updateFilteredTripList(Predicate<Trip> predicate) {
            requireNonNull(predicate);
            ArrayList<Trip> filteredTrips = trips.stream()
                    .filter(predicate)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }
}
