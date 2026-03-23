package seedu.triplog.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.triplog.model.trip.Trip;

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
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;

    /**
     * Creates a {@code TripCard} with the given {@code Trip} and index to display.
     */
    public TripCard(Trip trip, int displayedIndex) {
        super(FXML);
        this.trip = trip;
        id.setText(displayedIndex + ". ");
        name.setText(trip.getName().fullName);

        setOptionalLabel(phone, "Phone: ", trip.getPhoneDisplay());
        setOptionalLabel(address, "Address: ", trip.getAddressDisplay());
        setOptionalLabel(email, "Email: ", trip.getEmailDisplay());
        setOptionalLabel(startDate, "Start: ", trip.getStartDateDisplay());
        setOptionalLabel(endDate, "End: ", trip.getEndDateDisplay());
        setTags(trip);
    }

    /**
     * Sets optional field label on the UI or hides it if value is null
     */
    private void setOptionalLabel(Label label, String prefix, String value) {
        if (value != null) {
            label.setText(prefix + value);
        } else {
            label.setText("");
            label.setManaged(false);
            label.setVisible(false);
        }
    }

    /**
     * Sets tags on the UI given a trip
     */
    private void setTags(Trip trip) {
        if (trip.getTags().isEmpty()) {
            tags.setManaged(false);
            tags.setVisible(false);
        } else {
            trip.getTags().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        }
    }

    // getters mainly used for testing
    public Label getPhoneLabel() {
        return phone;
    }

    public Label getAddressLabel() {
        return address;
    }

    public Label getEmailLabel() {
        return email;
    }

    public Label getStartDateLabel() {
        return startDate;
    }

    public Label getEndDateLabel() {
        return endDate;
    }

    public FlowPane getTags() {
        return tags;
    }
}
