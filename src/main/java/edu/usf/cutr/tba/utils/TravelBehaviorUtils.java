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
import edu.usf.cutr.tba.options.ProgramOptions;

import java.text.SimpleDateFormat;
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
    public static String getDateFromMillis(Long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
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

    public static boolean isInSameDay(List<TravelBehaviorRecord> oneDayTravelBehaviorRecordList,
                                      TravelBehaviorRecord tbr) {
        TravelBehaviorRecord lastRecord = oneDayTravelBehaviorRecordList.get(oneDayTravelBehaviorRecordList.size() - 1);
        Long lastRecordActivityEndTime = lastRecord.getActivityEndTimeMillis() != null ? lastRecord.getActivityEndTimeMillis() :
                lastRecord.getLocationEndTimeMillis();

        Long newRecordActivityEndTime = tbr.getActivityEndTimeMillis() != null ? tbr.getActivityEndTimeMillis() :
                tbr.getLocationEndTimeMillis();

        if (lastRecordActivityEndTime == null || newRecordActivityEndTime == null) return false;

        // By default the day starts at 3 AM and ends at 3 AM next day
        long sameDayDiff = ProgramOptions.getInstance().getSameDayStartPoint() == null ? TravelBehaviorConstants.
                SAME_DAY_TIME_DIFF : TimeUnit.HOURS.toMillis(ProgramOptions.getInstance().getSameDayStartPoint());

        return TimeUnit.MILLISECONDS.toDays(lastRecordActivityEndTime - sameDayDiff) ==
                TimeUnit.MILLISECONDS.toDays(newRecordActivityEndTime - sameDayDiff);
    }

    public static float millisToMinutes(long millis) {
        return TimeUnit.MILLISECONDS.toSeconds(millis) / 60f;
    }
}
