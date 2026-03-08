package seedu.triplog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.triplog.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import org.junit.jupiter.api.Test;

import seedu.triplog.logic.commands.EditCommand.EditTripDescriptor;
import seedu.triplog.testutil.EditTripDescriptorBuilder;

/**
 * Contains unit tests for EditTripDescriptor.
 */
public class EditTripDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditTripDescriptor descriptorWithSameValues = new EditTripDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditTripDescriptor editedAmy = new EditTripDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditTripDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditTripDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditTripDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditTripDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different startDate -> returns false
        editedAmy = new EditTripDescriptorBuilder(DESC_AMY).withStart("2024-11-11").build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different endDate -> returns false
        editedAmy = new EditTripDescriptorBuilder(DESC_AMY).withEnd("2024-11-12").build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditTripDescriptor editTripDescriptor = new EditTripDescriptor();
        String expected = EditTripDescriptor.class.getCanonicalName() + "{name="
                + editTripDescriptor.getName().orElse(null) + ", phone="
                + editTripDescriptor.getPhone().orElse(null) + ", email="
                + editTripDescriptor.getEmail().orElse(null) + ", address="
                + editTripDescriptor.getAddress().orElse(null) + ", tags="
                + editTripDescriptor.getTags().orElse(null) + ", startDate="
                + editTripDescriptor.getStartDate().orElse(null) + ", endDate="
                + editTripDescriptor.getEndDate().orElse(null) + "}";
        assertEquals(expected, editTripDescriptor.toString());
    }
}
