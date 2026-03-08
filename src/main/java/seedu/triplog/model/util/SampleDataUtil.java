package seedu.triplog.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.TripLog;
import seedu.triplog.model.person.Address;
import seedu.triplog.model.person.Email;
import seedu.triplog.model.person.Name;
import seedu.triplog.model.person.Phone;
import seedu.triplog.model.person.Trip;
import seedu.triplog.model.person.TripDate;
import seedu.triplog.model.tag.Tag;

/**
 * Contains utility methods for populating {@code TripLog} with sample data.
 */
public class SampleDataUtil {
    public static Trip[] getSamplePersons() {
        return new Trip[] {
            new Trip(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"),
                    getTagSet("friends"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05")),
            new Trip(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    getTagSet("colleagues", "friends"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05")),
            new Trip(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                    new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    getTagSet("neighbours"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05")),
            new Trip(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    getTagSet("family"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05")),
            new Trip(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"),
                    getTagSet("classmates"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05")),
            new Trip(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"),
                    getTagSet("colleagues"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05"))
        };
    }

    public static ReadOnlyTripLog getSampleTripLog() {
        TripLog sampleAb = new TripLog();
        for (Trip sampleTrip : getSamplePersons()) {
            sampleAb.addTrip(sampleTrip);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
