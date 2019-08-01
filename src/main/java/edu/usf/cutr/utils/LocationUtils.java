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
import edu.usf.cutr.model.TravelBehaviorRecord;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocationUtils {
    private static final long TIME_THRESHOLD = TimeUnit.MINUTES.toMillis(10);  // 10 minutes

    private static final float ACC_THRESHOLD = 50f;  // 50 meters

    private static final float DISTANCE_THRESHOLD = 50f;  // 50 meters

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

        // if non-of the location's accuracy is less then threshold then return the best one with the best accuracy
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

    public static boolean isTravelBehaviorRecordsClose(TravelBehaviorRecord lastRecord, TravelBehaviorRecord tbr) {
        if (lastRecord.getEndLon() == null || lastRecord.getEndLon() == null || tbr.getEndLat() == null ||
                tbr.getEndLon() == null) {
            return false;
        }
        float distance = computeDistanceAndBearing(lastRecord.getEndLat(), lastRecord.getEndLon(), tbr.getEndLat(),
                tbr.getEndLon());
        return distance < DISTANCE_THRESHOLD;
    }

    /**
     * Copied form https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/location/java/android/location/Location.java
     * Licence:
     * /*
     *  * Copyright (C) 2007 The Android Open Source Project
     *  *
     *  * Licensed under the Apache License, Version 2.0 (the "License");
     *  * you may not use this file except in compliance with the License.
     *  * You may obtain a copy of the License at
     *  *
     *  *      http://www.apache.org/licenses/LICENSE-2.0
     *  *
     *  * Unless required by applicable law or agreed to in writing, software
     *  * distributed under the License is distributed on an "AS IS" BASIS,
     *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     *  * See the License for the specific language governing permissions and
     *  * limitations under the License.
     */
    public static float computeDistanceAndBearing(double lat1, double lon1, double lat2, double lon2) {
        // Based on http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf
        // using the "Inverse Formula" (section 4)
        int MAXITERS = 20;
        // Convert lat/long to radians
        lat1 *= Math.PI / 180.0;
        lat2 *= Math.PI / 180.0;
        lon1 *= Math.PI / 180.0;
        lon2 *= Math.PI / 180.0;
        double a = 6378137.0; // WGS84 major axis
        double b = 6356752.3142; // WGS84 semi-major axis
        double f = (a - b) / a;
        double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);
        double L = lon2 - lon1;
        double A = 0.0;
        double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
        double U2 = Math.atan((1.0 - f) * Math.tan(lat2));
        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;
        double sigma = 0.0;
        double deltaSigma = 0.0;
        double cosSqAlpha = 0.0;
        double cos2SM = 0.0;
        double cosSigma = 0.0;
        double sinSigma = 0.0;
        double cosLambda = 0.0;
        double sinLambda = 0.0;
        double lambda = L; // initial guess
        for (int iter = 0; iter < MAXITERS; iter++) {
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
            double sinSqSigma = t1 * t1 + t2 * t2; // (14)
            sinSigma = Math.sqrt(sinSqSigma);
            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
            sigma = Math.atan2(sinSigma, cosSigma); // (16)
            double sinAlpha = (sinSigma == 0) ? 0.0 :
                    cosU1cosU2 * sinLambda / sinSigma; // (17)
            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
            cos2SM = (cosSqAlpha == 0) ? 0.0 :
                    cosSigma - 2.0 * sinU1sinU2 / cosSqAlpha; // (18)
            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
            A = 1 + (uSquared / 16384.0) * // (3)
                    (4096.0 + uSquared *
                            (-768 + uSquared * (320.0 - 175.0 * uSquared)));
            double B = (uSquared / 1024.0) * // (4)
                    (256.0 + uSquared *
                            (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
            double C = (f / 16.0) *
                    cosSqAlpha *
                    (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = B * sinSigma * // (6)
                    (cos2SM + (B / 4.0) *
                            (cosSigma * (-1.0 + 2.0 * cos2SMSq) -
                                    (B / 6.0) * cos2SM *
                                            (-3.0 + 4.0 * sinSigma * sinSigma) *
                                            (-3.0 + 4.0 * cos2SMSq)));
            lambda = L +
                    (1.0 - C) * f * sinAlpha *
                            (sigma + C * sinSigma *
                                    (cos2SM + C * cosSigma *
                                            (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)
            double delta = (lambda - lambdaOrig) / lambda;
            if (Math.abs(delta) < 1.0e-12) {
                break;
            }
        }
        float distance = (float) (b * A * (sigma - deltaSigma));
        return distance;
    }
}
