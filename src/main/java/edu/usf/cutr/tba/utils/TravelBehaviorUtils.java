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

import edu.usf.cutr.tba.constants.TravelBehaviorConstants;
import edu.usf.cutr.tba.model.TravelBehaviorInfo;
import edu.usf.cutr.tba.model.TravelBehaviorRecord;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
     * Gets the local time based on the provided epoch time and Zone Id
     *
     * @param millis
     * @return the local time based on the provided epoch time and Zone Id
     */
    public static String getLocalTimeFromMillis(Long millis, ZoneId zoneId) {
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
     * (oneDayTravelBehaviorRecordList), using (midnight + sameDayDiff) to split days.  Local time of the travel
     * behavior data is used for midnight.
     *
     * @param oneDayTravelBehaviorRecordList
     * @param tbr
     * @param sameDayDiff                    the number of milliseconds past midnight to use as a time to split days.  For example, if you
     *                                       use 1.08e+7, then 3am will be used to split the day.
     * @return true if the provided travel behavior record (tbr) is in the same day as the others in the provided list
     * (oneDayTravelBehaviorRecordList), using (midnight + sameDayDiff) to split days.  Local time of the travel
     * behavior data is used for midnight.
     */
    public static boolean isInSameDay(List<TravelBehaviorRecord> oneDayTravelBehaviorRecordList,
                                      TravelBehaviorRecord tbr, long sameDayDiff) {
        TravelBehaviorRecord lastRecord = oneDayTravelBehaviorRecordList.get(oneDayTravelBehaviorRecordList.size() - 1);
        Long lastRecordActivityEndTime = lastRecord.getActivityEndTimeMillis() != null ? lastRecord.getActivityEndTimeMillis() :
                lastRecord.getLocationEndTimeMillis();

        Long newRecordActivityEndTime = tbr.getActivityEndTimeMillis() != null ? tbr.getActivityEndTimeMillis() :
                tbr.getLocationEndTimeMillis();

        if (lastRecordActivityEndTime == null || newRecordActivityEndTime == null) return false;

        return TimeUnit.MILLISECONDS.toDays(lastRecordActivityEndTime - sameDayDiff) ==
                TimeUnit.MILLISECONDS.toDays(newRecordActivityEndTime - sameDayDiff);
    }

    public static float millisToMinutes(long millis) {
        return TimeUnit.MILLISECONDS.toSeconds(millis) / 60f;
    }
}
