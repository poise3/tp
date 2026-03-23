package seedu.triplog.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.TripLog;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Address;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripDate;


/**
 * Contains utility methods for populating {@code TripLog} with sample data.
 */
public class SampleDataUtil {
    public static Trip[] getSamplePersons() {
        return new Trip[] {
            new Trip(new Name("Hotel California"), null, null,
                        new Address("1670 Ocean Ave, Santa Monica"),
                        getTagSet("Hotel"), new TripDate("2026-01-10"),
                        new TripDate("2026-01-15")),
            new Trip(new Name("ZhangJiaJie Forest"), null, null,
                        new Address("Wulingyuan District, Zhangjiajie, Hunan, China, 427403"),
                        getTagSet("scenery", "China"), new TripDate("2026-03-20"),
                        new TripDate("2026-03-25")),
            new Trip(new Name("Singapore Zoo"), null, null,
                        new Address("80 Mandai Lake Rd, 729826"),
                        getTagSet("zoo"), new TripDate("2026-06-01"),
                        new TripDate("2026-06-05")),
            new Trip(new Name("Serangoon Gardens"), null, null,
                        new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                        getTagSet("garden"),
                        new TripDate("2026-03-22"),
                        new TripDate("2026-03-23")),
            new Trip(new Name("Gardens By The Bay"), null, null,
                        new Address("Marina Gardens Drive Supertree Grove, Singapore 018953"),
                        getTagSet("garden"), null, null),
            new Trip(new Name("National Museum of Singapore"), null, null,
                        new Address("93 Stamford Rd, Singapore 178897"),
                        getTagSet("colleagues"), new TripDate("2025-12-25"),
                        new TripDate("2025-12-30"))
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
