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
package edu.usf.cutr.manager;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import edu.usf.cutr.io.CSVFileWriter;
import edu.usf.cutr.io.FirebaseReader;
import edu.usf.cutr.model.TravelBehaviorEventTime;
import edu.usf.cutr.model.TravelBehaviorInfo;
import edu.usf.cutr.model.TravelBehaviorRecord;
import edu.usf.cutr.utils.LocationUtils;
import edu.usf.cutr.utils.QueryDocumentSnapshotComparator;
import edu.usf.cutr.utils.TravelBehaviorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TravelBehaviorDataAnalysisManager {

    private FirebaseReader mFirebaseReader;

    private CSVFileWriter mCSVFileWriter;

    private TravelBehaviorRecord mLastTravelBehaviorRecord;

    public TravelBehaviorDataAnalysisManager() {
        mFirebaseReader = new FirebaseReader();
        mCSVFileWriter = new CSVFileWriter();


    }

    public void processData() {
        // create csv file and add the header
        mCSVFileWriter.createHeader(TravelBehaviorRecord.CSV_HEADER);

        // analyze all data and append the data in the csv file
        analyzeAllTravelBehaviorData();

        //close the csv file
        mCSVFileWriter.closeWriter();
    }

    private void analyzeAllTravelBehaviorData() {
        List<QueryDocumentSnapshot> allUserIds = mFirebaseReader.getAllUserIds();
        for (QueryDocumentSnapshot doc : allUserIds) {
            processUserById(doc.getId());
        }
    }

    private void processUserById(String userId) {
        List<QueryDocumentSnapshot> userInfoById = new ArrayList<>(mFirebaseReader.getAllUserInfoById(userId));
        Collections.sort(userInfoById, new QueryDocumentSnapshotComparator());

        mLastTravelBehaviorRecord = null;
        int tripId = 0;

        for (QueryDocumentSnapshot doc : userInfoById) {
            processUserActivityTransitionData(doc, userId, tripId++);
        }
    }

    private void processUserActivityTransitionData(QueryDocumentSnapshot doc, String userId, int tripId) {
        TravelBehaviorInfo tbi = doc.toObject(TravelBehaviorInfo.class);
        if (mLastTravelBehaviorRecord == null) {
            TravelBehaviorInfo.TravelBehaviorActivity enterActivity = TravelBehaviorUtils.getEnterActivity(tbi.activities);
            if (enterActivity != null) {
                mLastTravelBehaviorRecord = createTravelBehaviorRecord(userId, tripId, tbi, enterActivity);
            }
        } else {
            TravelBehaviorInfo.TravelBehaviorActivity exitActivity = TravelBehaviorUtils.getExitActivity(tbi.activities);
            if (exitActivity != null &&
                    exitActivity.detectedActivity.equals(mLastTravelBehaviorRecord.getGoogleActivity())) {
                completeTravelBehaviorRecord(tbi);
                mCSVFileWriter.appendToCsV(mLastTravelBehaviorRecord);
            }

            mLastTravelBehaviorRecord = null;

            TravelBehaviorInfo.TravelBehaviorActivity enterActivity = TravelBehaviorUtils.getEnterActivity(tbi.activities);
            if (enterActivity != null) {
                mLastTravelBehaviorRecord = createTravelBehaviorRecord(userId, tripId, tbi, enterActivity);
            }
        }
    }

    private TravelBehaviorRecord createTravelBehaviorRecord(String userId, int tripId, TravelBehaviorInfo tbi,
                                                            TravelBehaviorInfo.TravelBehaviorActivity enterActivity) {
        TravelBehaviorRecord tbr = new TravelBehaviorRecord(userId);
        tbr.setTripId(tripId + "").setGoogleActivity(enterActivity.detectedActivity).
                setGoogleConfidence(enterActivity.confidenceLevel == null ? null :
                        ((float) enterActivity.confidenceLevel / 100f));

        TravelBehaviorEventTime bestTime = TravelBehaviorUtils.getBestTime(tbi);

        if (bestTime != null) {
            tbr.setStartDateAndTime(TravelBehaviorUtils.getDateandTimeFromMillis(bestTime.getEventTime())).
                    setStartDateAndTimeSource(bestTime.getTimeType().toString()).
                    setStartTimeMillis(bestTime.getEventTime());
        }

        List<TravelBehaviorInfo.LocationInfo> locationInfoList = tbi.locationInfoList;
        TravelBehaviorInfo.LocationInfo bestLocation = LocationUtils.getBestLocation(locationInfoList);
        if (bestLocation != null) {
            tbr.setStartLat(bestLocation.getLat()).setStartLon(bestLocation.getLon()).
                    setOriginLocationDateAndTime(TravelBehaviorUtils.getDateandTimeFromMillis(bestLocation.time)).
                    setOriginHorAccuracy(bestLocation.accuracy).setOriginProvider(bestLocation.provider);
        }
        return tbr;
    }

    private void completeTravelBehaviorRecord(TravelBehaviorInfo tbi) {
        TravelBehaviorEventTime bestTime = TravelBehaviorUtils.getBestTime(tbi);
        if (bestTime != null) {
            mLastTravelBehaviorRecord.setEndDateAndTime(TravelBehaviorUtils.getDateandTimeFromMillis(bestTime.getEventTime())).
                    setEndDateAndTimeSource(bestTime.getTimeType().toString()).
                    setEndTimeMillis(bestTime.getEventTime());
        }

        List<TravelBehaviorInfo.LocationInfo> locationInfoList = tbi.locationInfoList;
        TravelBehaviorInfo.LocationInfo bestLocation = LocationUtils.getBestLocation(locationInfoList);
        if (bestLocation != null) {
            mLastTravelBehaviorRecord.setEndLat(bestLocation.getLat()).setEndLon(bestLocation.getLon()).
                    setDestinationLocationDateAndTime(TravelBehaviorUtils.getDateandTimeFromMillis(bestLocation.time)).
                    setDestinationHorAccuracy(bestLocation.accuracy).setDestinationProvider(bestLocation.provider );
        }

        if (mLastTravelBehaviorRecord.getStartTimeMillis() != null && mLastTravelBehaviorRecord.getEndTimeMillis() != null) {
            long diff = TimeUnit.MILLISECONDS.toMinutes(mLastTravelBehaviorRecord.getEndTimeMillis() -
                    mLastTravelBehaviorRecord.getStartTimeMillis());
            mLastTravelBehaviorRecord.setActivityDuration(diff);
        }

    }
}
