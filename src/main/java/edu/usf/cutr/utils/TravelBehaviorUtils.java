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

import edu.usf.cutr.constants.TravelBehaviorConstants;
import edu.usf.cutr.model.TravelBehaviorInfo;
import edu.usf.cutr.model.TravelBehaviorRecord;

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

    public static String getDateAndTimeFromMillis(Long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    // TODO: Test The method
    public static boolean isInSameDay(List<TravelBehaviorRecord> oneDayTravelBehaviorRecordList,
                                      TravelBehaviorRecord tbr) {
        TravelBehaviorRecord lastRecord = oneDayTravelBehaviorRecordList.get(oneDayTravelBehaviorRecordList.size() - 1);
        Long lastRecordActivityEndTime = lastRecord.getActivityEndTimeMillis() != null ? lastRecord.getActivityEndTimeMillis() :
                lastRecord.getLocationEndTimeMillis();

        Long newRecordActivityEndTime = tbr.getActivityEndTimeMillis() != null ? tbr.getActivityEndTimeMillis() :
                tbr.getLocationEndTimeMillis();

        if (lastRecordActivityEndTime == null || newRecordActivityEndTime == null) return false;
        return TimeUnit.MILLISECONDS.toDays(lastRecordActivityEndTime) ==
                TimeUnit.MILLISECONDS.toDays(newRecordActivityEndTime);
    }
}
