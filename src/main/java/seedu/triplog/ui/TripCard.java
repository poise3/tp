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

    @FXML
    private HBox startDateBox;
    @FXML
    private HBox endDateBox;
    @FXML
    private HBox phoneBox;
    @FXML
    private HBox addressBox;
    @FXML
    private HBox emailBox;

    /**
     * Creates a {@code TripCard} with the given {@code Trip} and index to display.
     */
    public TripCard(Trip trip, int displayedIndex) {
        super(FXML);
        this.trip = trip;
        id.setText(displayedIndex + ". ");
        name.setText(trip.getName().fullName);

        setOptionalLabel(phone, "Phone: ", trip.getPhoneDisplay(), phoneBox);
        setOptionalLabel(address, "Address: ", trip.getAddressDisplay(), addressBox);
        setOptionalLabel(email, "Email: ", trip.getEmailDisplay(), emailBox);
        setOptionalLabel(startDate, "Start: ", trip.getStartDateDisplay(), startDateBox);
        setOptionalLabel(endDate, "End: ", trip.getEndDateDisplay(), endDateBox);
        setTags(trip);
    }

    /**
     * Sets optional field label on the UI or hides it if value is null
     */
    private void setOptionalLabel(Label label, String prefix, String value, HBox box) {
        if (value != null) {
            label.setText(prefix + value);
        } else {
            box.setVisible(false);
            box.setManaged(false);
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

    public HBox getPhoneBox() {
        return phoneBox;
    }

    public HBox getAddressBox() {
        return addressBox;
    }

    public HBox getEmailBox() {
        return emailBox;
    }

    public HBox getStartDateBox() {
        return startDateBox;
    }

    public HBox getEndDateBox() {
        return endDateBox;
    }

    public FlowPane getTags() {
        return tags;
    }
}
