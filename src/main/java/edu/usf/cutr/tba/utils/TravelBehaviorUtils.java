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
package edu.usf.cutr.tba.utils;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.database.annotations.NotNull;
import edu.usf.cutr.tba.constants.TravelBehaviorConstants;
import edu.usf.cutr.tba.model.DeviceInformation;
import edu.usf.cutr.tba.model.TravelBehaviorInfo;
import edu.usf.cutr.tba.model.TravelBehaviorRecord;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TravelBehaviorUtils {

    public static TravelBehaviorInfo.TravelBehaviorActivity getEnterActivity(List<TravelBehaviorInfo.
            TravelBehaviorActivity> activities) {
        return getActivityByType(activities, TravelBehaviorConstants.ACTIVITY_TRANSITION_ENTER);
    }

    public static TravelBehaviorInfo.TravelBehaviorActivity getExitActivity(List<TravelBehaviorInfo.
            TravelBehaviorActivity> activities) {
        return getActivityByType(activities, TravelBehaviorConstants.ACTIVITY_TRANSITION_EXIT);
    }

    /**
     * Gets the time in the string ISO 8601 UTC format from the provided epoch time
     * @param millis
     * @return the time in the string ISO 8601 UTC format from the provided epoch time
     */
    public static String getDateAndTimeFromMillis(Long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return sdf.format(date);
    }

    /**
     * Gets the date in string format from the provided epoch time
     * @param millis
     * @return the date in string format from the provided epoch time
     */
    public static String getDateAndTimeFileNameFromMillis(Long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH mm ss");
        return sdf.format(date);
    }

    /**
     * Gets the local time based on the provided epoch time and Zone Id, or an empty string if the time is null
     *
     * @param millis
     * @return the local time based on the provided epoch time and Zone Id, or an empty string if the time is null
     */
    public static String getLocalTimeFromMillis(Long millis, ZoneId zoneId) {
        if (millis == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        Instant instant = Instant.ofEpochMilli(millis);
        ZonedDateTime localTime = instant.atZone(zoneId);
        return localTime.format(formatter);
    }

    private static TravelBehaviorInfo.TravelBehaviorActivity getActivityByType(List<TravelBehaviorInfo.
            TravelBehaviorActivity> activities, String eventType) {
        for (TravelBehaviorInfo.TravelBehaviorActivity travelBehaviorActivity : activities) {
            if (eventType.equals(travelBehaviorActivity.detectedActivityType)) {
                return travelBehaviorActivity;
            }
        }
        return null;
    }

    public static Long getActivityStartTime(TravelBehaviorInfo travelBehaviorInfo) {
        if (travelBehaviorInfo.activities != null && travelBehaviorInfo.activities.size() > 0 &&
                travelBehaviorInfo.activities.get(0).eventTimeMillis != null) {
            return travelBehaviorInfo.activities.get(0).eventTimeMillis;
        }

        return null;
    }

    /**
     * Returns true if the provided travel behavior record (tbr) is in the same day as the others in the provided list
     * (oneDayTravelBehaviorRecordList), using (midnight + sameDayDiffHours) to split days.  Local time of the travel
     * behavior data is used for midnight.
     *
     * @param oneDayTravelBehaviorRecordList
     * @param tbr
     * @param sameDayDiffHours                    the number of hours past midnight to use as a time to split days.  For example, if you
     *                                       use 3, then 3am will be used to split the day.
     * @return true if the provided travel behavior record (tbr) is in the same day as the others in the provided list
     * (oneDayTravelBehaviorRecordList), using (midnight + sameDayDiffHours) to split days.  Local time of the travel
     * behavior data is used for midnight.
     */
    public static boolean isInSameDay(List<TravelBehaviorRecord> oneDayTravelBehaviorRecordList,
                                      TravelBehaviorRecord tbr, long sameDayDiffHours) {
        // Get the times of the first record in the list, as well as the time of the new tbr
        TravelBehaviorRecord firstRecord = oneDayTravelBehaviorRecordList.get(0);
        Long firstRecordActivityEndTime = firstRecord.getActivityEndTimeMillis() != null ? firstRecord.getActivityEndTimeMillis() :
                firstRecord.getLocationEndTimeMillis();

        Long newRecordActivityEndTime = tbr.getActivityEndTimeMillis() != null ? tbr.getActivityEndTimeMillis() :
                tbr.getLocationEndTimeMillis();

        if (firstRecordActivityEndTime == null || newRecordActivityEndTime == null) {
            // An incomplete record - return false
            return false;
        }

        Instant firstRecordActivityEndTimeInstant = Instant.ofEpochMilli(firstRecordActivityEndTime);
        Instant newRecordActivityEndTimeInstant = Instant.ofEpochMilli(newRecordActivityEndTime);

        double lat, lon;
        if (tbr.getStartLat() != null && tbr.getStartLon() != null) {
            lat = tbr.getStartLat();
            lon = tbr.getStartLon();
        } else if (tbr.getEndLat() != null && tbr.getEndLon() != null) {
            lat = tbr.getEndLat();
            lon = tbr.getEndLon();
        } else {
            // An incomplete record - return false
            return false;
        }

        // Get time zone from travel behavior location
        ZoneId zoneId = TimeZoneHelper.query(lat, lon);

        ZonedDateTime localTimeList = firstRecordActivityEndTimeInstant.atZone(zoneId);
        ZonedDateTime localTimeTbr = newRecordActivityEndTimeInstant.atZone(zoneId);

        if (localTimeList.truncatedTo(ChronoUnit.DAYS).equals(localTimeTbr.truncatedTo(ChronoUnit.DAYS))) {
            // If the two events tie within the same day, then return true
            return true;
        }

        // We still need to determine if the tbr is after midnight on the day after the list day, but before (midnight plus the sameDayDiffHours offset) (e.g., between midnight and 3am on the day after the list)
        ZonedDateTime nextDayMidnight = localTimeList.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
        ZonedDateTime nextDayMidnightPlusOffset = nextDayMidnight.plus(sameDayDiffHours, ChronoUnit.HOURS);
        return localTimeTbr.isAfter(nextDayMidnight) && localTimeTbr.isBefore(nextDayMidnightPlusOffset);
    }

    public static float millisToMinutes(long millis) {
        return TimeUnit.MILLISECONDS.toSeconds(millis) / 60f;
    }

    /**
     * Returns true if the TravelBehaviorRecord is allowed to be exported based on a variety of conditions, and false if it is not allowed
     *
     * @param tbr TravelBehaviorRecord to examine
     * @return true if the TravelBehaviorRecord is allowed to be exported based on a variety of conditions, and false if it is not allowed
     */
    public static boolean isAllowedToExport(TravelBehaviorRecord tbr) {
        final String YORK_REGION_ID = "5"; // From http://regions.onebusaway.org/regions-v3.json
        // We currently aren't exporting data from York Region Transit in Canada at their request due to international privacy concerns
        if (tbr.getRegionId() != null && tbr.getRegionId().equals(YORK_REGION_ID)) {
            return false;
        }
        // No other restrictions
        return true;
    }

    /**
     * Performs a binary search to return the DeviceInformation object which nearest timestamp
     * that occurs prior to (and not after) the activityEndTime.
     * @param userDeviceInfoList Sorted (by timeStamp) list of QueryDocumentSnapshot including information over time of user device
     * @param activityEndTimeMillis End time of an activity in milliseconds.
     * @return DeviceInformation object with the timestamp closest to the activityEndTime. If the
     * activityEndTime or DeviceInfo list are not available, return null
     */
    public static DeviceInformation getClosestDeviceInfo(@Nonnull List<QueryDocumentSnapshot> userDeviceInfoList, @NotNull Long activityEndTimeMillis) {
        // If the device list is empty or there is no activity end time then return null
        if (userDeviceInfoList.size() == 0) return null;

        int low = 0;
        int high = userDeviceInfoList.size() - 1;
        // If timestamp is lower that the first element on the array list, return the first devInfo in List
        DeviceInformation devInfo = userDeviceInfoList.get(low).toObject(DeviceInformation.class);
        String timeStamp = devInfo.getTimestamp();
        if (timeStamp == null) {
            timeStamp = userDeviceInfoList.get(low).getId();
        }
        if(activityEndTimeMillis < Long.parseLong(timeStamp)){
            return devInfo;
        }

        // If timestamp is higher that the last element on the array list, return last devInfo
        devInfo = userDeviceInfoList.get(high).toObject(DeviceInformation.class);
        timeStamp = devInfo.getTimestamp();
        if (timeStamp == null) {
            timeStamp = userDeviceInfoList.get(high).getId();
        }
        if (activityEndTimeMillis >= Long.parseLong(timeStamp)){
            devInfo.setTimestamp(timeStamp);
            return devInfo;
        }

        // activityEndTimeMillis is not bigger or lower thant the extreme values on the arrayList.
        // Perform a binary search to find closest timestamp record previous to activityEndTimeMillis
        while (low <= high) {
            int mid = (low + high) / 2;
            assert (mid <= high);
            devInfo = userDeviceInfoList.get(mid).toObject(DeviceInformation.class);
            timeStamp = devInfo.getTimestamp();
            if (timeStamp == null) {
                timeStamp = userDeviceInfoList.get(high).getId();
            }

            if (activityEndTimeMillis < Long.parseLong(timeStamp)) {
                high = mid -1;
            } else if (activityEndTimeMillis > Long.parseLong(timeStamp)) {
                low = mid +1;
            } else {
                devInfo.setTimestamp(timeStamp);
                return devInfo;
            }
        }
        // low is equal to high+1, return userDeviceInfoList at index high
        devInfo = userDeviceInfoList.get(high).toObject(DeviceInformation.class);
        timeStamp = devInfo.getTimestamp();
        if (timeStamp == null) {
            timeStamp = userDeviceInfoList.get(high).getId();
        }
        devInfo.setTimestamp(timeStamp);
        return devInfo;
    }
}
