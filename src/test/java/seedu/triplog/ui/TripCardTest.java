package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

@ExtendWith(ApplicationExtension.class)
public class TripCardTest {
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
    }
}
