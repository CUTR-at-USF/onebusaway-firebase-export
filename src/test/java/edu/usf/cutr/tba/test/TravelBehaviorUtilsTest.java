package edu.usf.cutr.tba.test;

import edu.usf.cutr.tba.model.TravelBehaviorRecord;
import edu.usf.cutr.tba.utils.TravelBehaviorUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests utilities for processing travel behavior data
 */
public class TravelBehaviorUtilsTest {

    final String userId = "test-user";

    @Test
    public void testIsInSameDay() {
        List<TravelBehaviorRecord> list = new ArrayList<>();
        // Use Eastern time location
        double lat = 28.0587, lon = -82.4139;

        TravelBehaviorRecord day1a = new TravelBehaviorRecord(userId);
        day1a.setStartLat(lat);
        day1a.setStartLon(lon);
        day1a.setEndLat(lat);
        day1a.setEndLon(lon);
        TravelBehaviorRecord day1b = new TravelBehaviorRecord(userId);
        day1b.setStartLat(lat);
        day1b.setStartLon(lon);
        day1b.setEndLat(lat);
        day1b.setEndLon(lon);
        TravelBehaviorRecord day1c = new TravelBehaviorRecord(userId);
        day1c.setStartLat(lat);
        day1c.setStartLon(lon);
        day1c.setEndLat(lat);
        day1c.setEndLon(lon);
        TravelBehaviorRecord day1d = new TravelBehaviorRecord(userId);
        day1d.setStartLat(lat);
        day1d.setStartLon(lon);
        day1d.setEndLat(lat);
        day1d.setEndLon(lon);
        TravelBehaviorRecord day1e = new TravelBehaviorRecord(userId);
        day1e.setStartLat(lat);
        day1e.setStartLon(lon);
        day1e.setEndLat(lat);
        day1e.setEndLon(lon);
        TravelBehaviorRecord day1f = new TravelBehaviorRecord(userId);
        day1f.setStartLat(lat);
        day1f.setStartLon(lon);
        day1f.setEndLat(lat);
        day1f.setEndLon(lon);
        TravelBehaviorRecord day1g = new TravelBehaviorRecord(userId);
        day1g.setStartLat(lat);
        day1g.setStartLon(lon);
        day1g.setEndLat(lat);
        day1g.setEndLon(lon);

        TravelBehaviorRecord day2a = new TravelBehaviorRecord(userId);
        day2a.setStartLat(lat);
        day2a.setStartLon(lon);
        day2a.setEndLat(lat);
        day2a.setEndLon(lon);
        TravelBehaviorRecord day2b = new TravelBehaviorRecord(userId);
        day2b.setStartLat(lat);
        day2b.setStartLon(lon);
        day2b.setEndLat(lat);
        day2b.setEndLon(lon);

        // Day 1

        // Local time: Friday, August 9, 2019 4:29:42.198 PM
        // UTC time: Friday, August 9, 2019 8:29:42.198 PM
        day1a.setActivityEndTimeMillis(1565382582198L);

        // Local time: Friday, August 9, 2019 7:49:42.198 PM
        // UTC time: Friday, August 9, 2019 11:49:42.198 PM
        day1b.setActivityEndTimeMillis(1565394582198L);

        // Local time: Friday, August 9, 2019 8:39:42.198 PM
        // UTC time: Saturday, August 10, 2019 12:39:42.198 AM
        day1c.setActivityEndTimeMillis(1565397582198L);

        // Local time: Friday, August 9, 2019 10:43:02.198 PM
        // UTC time: Saturday, August 10, 2019 2:43:02.198 AM
        day1d.setActivityEndTimeMillis(1565404982198L);

        // Local time: Friday, August 9, 2019 11:33:02.198 PM
        // UTC time: Saturday, August 10, 2019 3:33:02.198 AM
        day1e.setActivityEndTimeMillis(1565407982198L);

        // Local time: Saturday, August 10, 2019 12:06:22.198 AM
        // UTC time: Saturday, August 10, 2019 4:06:22.198 AM
        day1f.setActivityEndTimeMillis(1565409982198L);

        // Local time:  Saturday, August 10, 2019 2:53:02.198 AM
        // UTC time: Saturday, August 10, 2019 6:53:02.198 AM
        day1g.setActivityEndTimeMillis(1565419982198L);

        // Day 2

        // Local time:  Saturday, August 10, 2019 3:09:52.198 AM
        // UTC time: Saturday, August 10, 2019 7:09:52.198 AM
        day2a.setActivityEndTimeMillis(1565420992198L);

        // Local time: Saturday, August 10, 2019 11:36:22.198 PM
        // UTC time: Sunday, August 11, 2019 3:36:22.198 AM
        day2b.setActivityEndTimeMillis(1565494582198L);

        // Days are split at midnight + SAME_DAY_TIME_DIFF, using local time.  We use 3am local time to split days in the below tests.
        final long SAME_DAY_TIME_DIFF = 3;

        // Add the times in the same day one-by-one and confirm that they all belong in the same day (returns true)
        list.add(day1a);
        assertTrue(TravelBehaviorUtils.isInSameDay(list, day1b, SAME_DAY_TIME_DIFF));

        list.add(day1b);
        assertTrue(TravelBehaviorUtils.isInSameDay(list, day1c, SAME_DAY_TIME_DIFF));

        list.add(day1c);
        assertTrue(TravelBehaviorUtils.isInSameDay(list, day1d, SAME_DAY_TIME_DIFF));

        list.add(day1d);
        assertTrue(TravelBehaviorUtils.isInSameDay(list, day1e, SAME_DAY_TIME_DIFF));

        list.add(day1e);
        assertTrue(TravelBehaviorUtils.isInSameDay(list, day1f, SAME_DAY_TIME_DIFF));

        list.add(day1f);
        assertTrue(TravelBehaviorUtils.isInSameDay(list, day1g, SAME_DAY_TIME_DIFF));
        list.add(day1g);

        // Test the times in day two against the day 1 list, and confirm that they all return false (not in the same day)
        assertFalse(TravelBehaviorUtils.isInSameDay(list, day2a, SAME_DAY_TIME_DIFF));
        assertFalse(TravelBehaviorUtils.isInSameDay(list, day2b, SAME_DAY_TIME_DIFF));
    }
}
