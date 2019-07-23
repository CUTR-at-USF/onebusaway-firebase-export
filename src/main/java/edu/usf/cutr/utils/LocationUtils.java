/*
 * Copyright (C) 2019 University of South Florida
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usf.cutr.utils;

import edu.usf.cutr.model.TravelBehaviorInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocationUtils {
    private static final long TIME_THRESHOLD = TimeUnit.MINUTES.toMillis(10);  // 10 minutes

    private static final float ACC_THRESHOLD = 50f;  // 50 meters

    public static TravelBehaviorInfo.LocationInfo getBestLocation(List<TravelBehaviorInfo.LocationInfo> locationInfoList,
                                                                  final Long activityTime) {
        if (activityTime == null) return getBestLocation(locationInfoList);
        if (locationInfoList == null || locationInfoList.size() == 0) return null;
        if (locationInfoList.size() == 1) return locationInfoList.get(0);

        // Sort the locations based on the time difference of the activity time and the location time
        Collections.sort(locationInfoList, (Comparator.comparingLong(a -> (Math.abs(activityTime - a.time)))));

        // return the earliest location if the location is in accuracy threshold
        for (TravelBehaviorInfo.LocationInfo info: locationInfoList) {
            if (info.accuracy < ACC_THRESHOLD) return info;
        }

        // if non-of the location's accuracy is less then threshold then return the best one
        TravelBehaviorInfo.LocationInfo retInfo = locationInfoList.get(0);
        Float acc = retInfo.accuracy;
        for (int i = 1; i < locationInfoList.size(); i++) {
            TravelBehaviorInfo.LocationInfo info = locationInfoList.get(i);
            if (info.accuracy < acc) {
                acc = info.accuracy;
                retInfo = info;
            }
        }

        return retInfo;
    }
    public static TravelBehaviorInfo.LocationInfo getBestLocation(List<TravelBehaviorInfo.LocationInfo> locationInfoList) {
        if (locationInfoList == null || locationInfoList.size() == 0) return null;
        if (locationInfoList.size() == 1) return locationInfoList.get(0);

        TravelBehaviorInfo.LocationInfo bestLocation = locationInfoList.get(0);
        for(int i = 1; i < locationInfoList.size(); i++) {
            if (!compareLocations(bestLocation, locationInfoList.get(i))) {
                bestLocation = locationInfoList.get(i);
            }
        }

        return bestLocation;
    }

    /**
     * Compares Location A to Location B, considering timestamps and accuracy of locations.
     * Typically
     * this is used to compare a new location delivered by a LocationListener (Location A) to
     * a previously saved location (Location B).
     *
     * @param a location to compare
     * @param b location to compare against
     * @return true if Location a is "better" than b, or false if b is "better" than a
     */
    private static boolean compareLocations(TravelBehaviorInfo.LocationInfo a, TravelBehaviorInfo.LocationInfo b) {
        if (a == null) {
            // New location isn't valid, return false
            return false;
        }
        // If the new location is the first location, save it
        if (b == null) {
            return true;
        }

        // If the last location is older than TIME_THRESHOLD minutes, and the new location is more recent,
        // save the new location, even if the accuracy for new location is worse
        if (System.currentTimeMillis() - b.getTime() > TIME_THRESHOLD
                && compareLocationsByTime(a, b)) {
            return true;
        }

        // If the new location has an accuracy better than ACC_THRESHOLD and is newer than the last location, save it
        if (a.getAccuracy() < ACC_THRESHOLD && compareLocationsByTime(a, b)) {
            return true;
        }

        // If we get this far, A isn't better than B
        return false;
    }

    /**
     * Compares Location A to Location B - prefers a non-null location that is more recent.  Does
     * NOT take estimated accuracy into account.
     * @param a first location to compare
     * @param b second location to compare
     * @return true if Location a is "better" than b, or false if b is "better" than a
     */
    private static boolean compareLocationsByTime(TravelBehaviorInfo.LocationInfo a, TravelBehaviorInfo.LocationInfo b) {
        return (a != null && (b == null || a.getTime() > b.getTime()));
    }

}
