package edu.usf.cutr.tba.utils;

import net.iakovlev.timeshape.TimeZoneEngine;

import java.time.ZoneId;
import java.util.Optional;

/**
 * Determines a time zone from a location.
 * <p>
 * Initializing the TimeZoneEngine library is expensive and should only happen once, so this is a singleton.
 */
public class TimeZoneHelper {
    private static TimeZoneEngine mTimeZoneEngine;

    /**
     * Returns a time ZoneId for a given location, or null if one couldn't be determined
     *
     * @param lat
     * @param lon
     * @return a time ZoneId for a given location, or null if one couldn't be determined
     */
    synchronized static public ZoneId query(double lat, double lon) {
        if (mTimeZoneEngine == null) {
            // Use North America bounding box to reduce initialization time
            mTimeZoneEngine = TimeZoneEngine.initialize(10.833305983642491, -136.0546875, 54.97761367069628, -46.40625);
        }
        Optional<ZoneId> maybeZoneId = mTimeZoneEngine.query(lat, lon);
        return maybeZoneId.orElse(null);
    }
}
