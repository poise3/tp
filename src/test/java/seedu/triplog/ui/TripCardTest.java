package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_END_DATE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_START_DATE_BOB;
import static seedu.triplog.testutil.TypicalTrips.BOB;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import javafx.scene.control.Label;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripDate;
import seedu.triplog.testutil.TripBuilder;

@ExtendWith(ApplicationExtension.class)
public class TripCardTest {
    @Test
    public void display_tripWithAllFields_showsAllFields() {
        Trip validTrip = new TripBuilder(BOB)
                .withAddress(VALID_ADDRESS_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withStart(VALID_START_DATE_BOB)
                .withEnd(VALID_END_DATE_BOB)
                .build();

        TripCard tripCard = new TripCard(validTrip, 1);

        // check that the text for optional fields are set
        assertEquals("Phone: " + VALID_PHONE_BOB, tripCard.getPhoneLabel().getText());
        assertEquals("Address: " + VALID_ADDRESS_BOB, tripCard.getAddressLabel().getText());
        assertEquals("Email: " + VALID_EMAIL_BOB, tripCard.getEmailLabel().getText());
        assertEquals("Start: " + VALID_START_DATE_BOB, tripCard.getStartDateLabel().getText());
        assertEquals("End: " + VALID_END_DATE_BOB, tripCard.getEndDateLabel().getText());

        // check that the nodes are managed
        assertTrue(tripCard.getPhoneLabel().isManaged());
        assertTrue(tripCard.getAddressLabel().isManaged());
        assertTrue(tripCard.getEmailLabel().isManaged());
        assertTrue(tripCard.getStartDateLabel().isManaged());
        assertTrue(tripCard.getEndDateLabel().isManaged());

        // check that the nodes are visible
        assertTrue(tripCard.getPhoneLabel().isVisible());
        assertTrue(tripCard.getAddressLabel().isVisible());
        assertTrue(tripCard.getEmailLabel().isVisible());
        assertTrue(tripCard.getStartDateLabel().isVisible());
        assertTrue(tripCard.getEndDateLabel().isVisible());
    }

    @Test
    public void tripCard_optionalFieldsNull_noExceptionsAndCorrectManaged() {
        Trip tripWithNulls = new Trip(
                new Name("Bali Trip"),
                null,
                null,
                null,
                Collections.emptySet(),
                null,
                null
        );

        TripCard tripCard = new TripCard(tripWithNulls, 1);

        // check that the text for optional fields is not set
        assertEquals("", tripCard.getPhoneLabel().getText());
        assertEquals("", tripCard.getAddressLabel().getText());
        assertEquals("", tripCard.getEmailLabel().getText());
        assertEquals("", tripCard.getStartDateLabel().getText());
        assertEquals("", tripCard.getEndDateLabel().getText());

        // check that the nodes are unmanaged
        assertFalse(tripCard.getPhoneLabel().isManaged());
        assertFalse(tripCard.getAddressLabel().isManaged());
        assertFalse(tripCard.getEmailLabel().isManaged());
        assertFalse(tripCard.getStartDateLabel().isManaged());
        assertFalse(tripCard.getEndDateLabel().isManaged());

        // check that the nodes are not visible
        assertFalse(tripCard.getPhoneLabel().isVisible());
        assertFalse(tripCard.getAddressLabel().isVisible());
        assertFalse(tripCard.getEmailLabel().isVisible());
        assertFalse(tripCard.getStartDateLabel().isVisible());
        assertFalse(tripCard.getEndDateLabel().isVisible());
    }

    @Test
    public void tripCard_tagsDisplayedCorrectly() {
        Set<Tag> tags = Set.of(new Tag("beach"), new Tag("mountain"));

        // test trip with tags
        Trip tripWithTags = new Trip(
                new Name("Holiday"),
                null, // phone
                null, // email
                null, // address
                tags,
                new TripDate("2026-03-16"),
                new TripDate("2026-03-20")
        );

        TripCard tripCard = new TripCard(tripWithTags, 1);
        Set<String> displayedTagNames = tripCard.getTags().getChildren()
                .stream()
                .map(node -> ((Label) node).getText())
                .collect(Collectors.toSet());

        Set<String> expectedTagNames = tags.stream().map(tag -> tag.tagName).collect(Collectors.toSet());
        assertEquals(expectedTagNames, displayedTagNames);
        assertTrue(tripCard.getTags().isVisible());
        assertTrue(tripCard.getTags().isManaged());

        // test trip with no tags
        Trip tripNoTags = new Trip(
                new Name("Solo Trip"),
                null, null, null,
                Set.of(), // empty tags
                null,
                null
        );

        TripCard tripCardNoTags = new TripCard(tripNoTags, 2);
        assertTrue(tripCardNoTags.getTags().getChildren().isEmpty());
        assertFalse(tripCardNoTags.getTags().isVisible());
        assertFalse(tripCardNoTags.getTags().isManaged());
    }
}
