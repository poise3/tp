package seedu.triplog.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.triplog.model.person.Trip;

/**
 * An UI component that displays information of a {@code Trip}.
 */
public class TripCard extends UiPart<Region> {

    private static final String FXML = "TripListCard.fxml";

    public final Trip trip;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code TripCard} with the given {@code Trip} and index to display.
     */
    public TripCard(Trip trip, int displayedIndex) {
        super(FXML);
        this.trip = trip;
        id.setText(displayedIndex + ". ");
        name.setText(trip.getName().fullName);
        startDate.setText(trip.getStartDate().toString());
        endDate.setText(trip.getEndDate().toString());
        phone.setText(trip.getPhone().value);
        address.setText(trip.getAddress().value);
        email.setText(trip.getEmail().value);
        trip.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
