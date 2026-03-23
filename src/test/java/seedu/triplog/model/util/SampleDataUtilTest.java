package seedu.triplog.model.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.triplog.model.ReadOnlyTripLog;

public class SampleDataUtilTest {
    @Test
    public void getSampleTripLog_returnsPopulatedTripLog() {
        ReadOnlyTripLog sampleTripLog = SampleDataUtil.getSampleTripLog();
        assertNotNull(sampleTripLog);
        assertTrue(sampleTripLog.getTripList().size() > 0);
    }
}
