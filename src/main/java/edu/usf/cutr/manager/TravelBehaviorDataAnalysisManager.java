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
import edu.usf.cutr.constants.TravelBehaviorConstants;
import edu.usf.cutr.io.CSVFileWriter;
import edu.usf.cutr.io.FirebaseReader;
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

    private List<TravelBehaviorRecord> mOneDayTravelBehaviorRecordList;

    private int mTripId = 0;

    private int mTourId = 0;

    public TravelBehaviorDataAnalysisManager() {
        mFirebaseReader = new FirebaseReader();
        mCSVFileWriter = new CSVFileWriter();
        mOneDayTravelBehaviorRecordList = new ArrayList<>();
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

        for (QueryDocumentSnapshot doc : userInfoById) {
            processUserActivityTransitionData(doc, userId);
        }

        if (mOneDayTravelBehaviorRecordList.size() > 0) {
            applyTourAlgorithmToOneDayRecordList();
        }
    }

    private void processUserActivityTransitionData(QueryDocumentSnapshot doc, String userId) {
        TravelBehaviorInfo tbi = doc.toObject(TravelBehaviorInfo.class);
        if (mLastTravelBehaviorRecord == null) {
            TravelBehaviorInfo.TravelBehaviorActivity enterActivity = TravelBehaviorUtils.getEnterActivity(tbi.activities);
            if (enterActivity != null) {
                mLastTravelBehaviorRecord = createTravelBehaviorRecord(userId, tbi, enterActivity);
            }
        } else {
            TravelBehaviorInfo.TravelBehaviorActivity exitActivity = TravelBehaviorUtils.getExitActivity(tbi.activities);
            if (exitActivity != null &&
                    exitActivity.detectedActivity.equals(mLastTravelBehaviorRecord.getGoogleActivity())) {
                completeTravelBehaviorRecord(tbi);
                mLastTravelBehaviorRecord.setTripId(String.valueOf(mTripId++));

                addTravelBehaviorRecord(mLastTravelBehaviorRecord);

                eliminateStillEvent();
            }

            mLastTravelBehaviorRecord = null;

            TravelBehaviorInfo.TravelBehaviorActivity enterActivity = TravelBehaviorUtils.getEnterActivity(tbi.activities);
            if (enterActivity != null) {
                mLastTravelBehaviorRecord = createTravelBehaviorRecord(userId, tbi, enterActivity);
            }
        }
    }

    private TravelBehaviorRecord createTravelBehaviorRecord(String userId, TravelBehaviorInfo tbi,
                                                            TravelBehaviorInfo.TravelBehaviorActivity enterActivity) {
        TravelBehaviorRecord tbr = new TravelBehaviorRecord(userId);
        tbr.setGoogleActivity(enterActivity.detectedActivity).
                setGoogleConfidence(enterActivity.confidenceLevel == null ? null :
                        ((float) enterActivity.confidenceLevel / 100f));

        Long activityStartTime = TravelBehaviorUtils.getActivityStartTime(tbi);

        if (activityStartTime != null) {
            tbr.setActivityStartDateAndTime(TravelBehaviorUtils.getDateAndTimeFromMillis(activityStartTime)).
                    setActivityStartTimeMillis(activityStartTime);
        }

        List<TravelBehaviorInfo.LocationInfo> locationInfoList = tbi.locationInfoList;
        TravelBehaviorInfo.LocationInfo bestLocation = LocationUtils.getBestLocation(locationInfoList, activityStartTime);
        if (bestLocation != null) {
            tbr.setStartLat(bestLocation.getLat()).setStartLon(bestLocation.getLon()).
                    setOriginLocationDateAndTime(TravelBehaviorUtils.getDateAndTimeFromMillis(bestLocation.time)).
                    setOriginHorAccuracy(bestLocation.accuracy).setOriginProvider(bestLocation.provider).
                    setLocationStartTimeMillis(bestLocation.time);
        }

        if (tbr.getActivityStartTimeMillis() != null && tbr.getLocationStartTimeMillis() != null) {
            long diff = Math.abs(tbr.getActivityStartTimeMillis() - tbr.getLocationStartTimeMillis());
            tbr.setActivityStartOriginTimeDiff(TimeUnit.MILLISECONDS.toMinutes(diff));
        }

        return tbr;
    }

    private void completeTravelBehaviorRecord(TravelBehaviorInfo tbi) {
        Long activityEndTime = TravelBehaviorUtils.getActivityStartTime(tbi);

        if (activityEndTime != null) {
            mLastTravelBehaviorRecord.setActivityEndDateAndTime(TravelBehaviorUtils.getDateAndTimeFromMillis(activityEndTime)).
                    setActivityEndTimeMillis(activityEndTime);
        }

        List<TravelBehaviorInfo.LocationInfo> locationInfoList = tbi.locationInfoList;
        TravelBehaviorInfo.LocationInfo bestLocation = LocationUtils.getBestLocation(locationInfoList, activityEndTime);
        if (bestLocation != null) {
            mLastTravelBehaviorRecord.setEndLat(bestLocation.getLat()).setEndLon(bestLocation.getLon()).
                    setDestinationLocationDateAndTime(TravelBehaviorUtils.getDateAndTimeFromMillis(bestLocation.time)).
                    setDestinationHorAccuracy(bestLocation.accuracy).setDestinationProvider(bestLocation.provider).
                    setLocationEndTimeMillis(bestLocation.time);
        }

        if (mLastTravelBehaviorRecord.getActivityEndTimeMillis() != null &&
                mLastTravelBehaviorRecord.getLocationEndTimeMillis() != null) {
            long diff = Math.abs(mLastTravelBehaviorRecord.getActivityEndTimeMillis() -
                    mLastTravelBehaviorRecord.getLocationEndTimeMillis());
            mLastTravelBehaviorRecord.setActivityEndDestinationTimeDiff(TimeUnit.MILLISECONDS.toMinutes(diff));
        }

        if (mLastTravelBehaviorRecord.getActivityStartTimeMillis() != null &&
                mLastTravelBehaviorRecord.getActivityEndTimeMillis() != null) {
            long diff = TimeUnit.MILLISECONDS.toMinutes(mLastTravelBehaviorRecord.getActivityEndTimeMillis() -
                    mLastTravelBehaviorRecord.getActivityStartTimeMillis());
            mLastTravelBehaviorRecord.setActivityDuration(diff);
        }

        if (mLastTravelBehaviorRecord.getStartLat() != null && mLastTravelBehaviorRecord.getStartLon() != null &&
                mLastTravelBehaviorRecord.getEndLat() != null && mLastTravelBehaviorRecord.getEndLon() != null) {
            float distance = LocationUtils.computeDistanceAndBearing(mLastTravelBehaviorRecord.getStartLat(),
                    mLastTravelBehaviorRecord.getStartLon(), mLastTravelBehaviorRecord.getEndLat(),
                    mLastTravelBehaviorRecord.getEndLon());
            mLastTravelBehaviorRecord.setOriginDestinationDistance(distance);
        }
    }

    private void addTravelBehaviorRecord(TravelBehaviorRecord tbr) {
        if (mOneDayTravelBehaviorRecordList.size() == 0) {
            // add new data to list with new tour id
            mOneDayTravelBehaviorRecordList.add(tbr);
        } else if (TravelBehaviorUtils.isInSameDay(mOneDayTravelBehaviorRecordList, tbr)) {
            mOneDayTravelBehaviorRecordList.add(tbr);
        } else {
            applyTourAlgorithmToOneDayRecordList();
            // add new data to list with new tour id
            mOneDayTravelBehaviorRecordList.add(tbr);
        }
    }

    private void eliminateStillEvent() {
        int size = mOneDayTravelBehaviorRecordList.size();
        if (size < 3 || TravelBehaviorConstants.ACTIVITY_STILL.equals(mOneDayTravelBehaviorRecordList.get(size - 1).
                getGoogleActivity()) || !TravelBehaviorConstants.ACTIVITY_STILL.equals(mOneDayTravelBehaviorRecordList.
                get(size - 2).getGoogleActivity())) {
            return;
        }

        TravelBehaviorRecord tbrFirst = mOneDayTravelBehaviorRecordList.get(size - 3);
        TravelBehaviorRecord tbrLast = mOneDayTravelBehaviorRecordList.get(size - 1);

        if (tbrFirst.getGoogleActivity().equals(tbrLast.getGoogleActivity()) &&
                tbrFirst.getActivityEndTimeMillis() != null && tbrLast.getActivityStartTimeMillis() != null &&
                tbrLast.getActivityStartTimeMillis() - tbrFirst.getActivityEndTimeMillis() <
                        TravelBehaviorConstants.STILL_ACTIVITY_THRESHOLD) {
            tbrFirst.setActivityEndDateAndTime(tbrLast.getActivityEndDateAndTime()).setActivityEndTimeMillis(
                    tbrLast.getActivityEndTimeMillis()).setEndLat(tbrLast.getEndLat()).setEndLon(tbrLast.getEndLon()).
                    setDestinationLocationDateAndTime(tbrLast.getDestinationLocationDateAndTime()).
                    setDestinationHorAccuracy(tbrLast.getDestinationHorAccuracy()).setDestinationProvider
                    (tbrLast.getDestinationProvider()).setLocationEndTimeMillis(tbrLast.getLocationEndTimeMillis()).
                    setActivityEndDestinationTimeDiff(tbrLast.getActivityEndDestinationTimeDiff());

            if (tbrFirst.getActivityStartTimeMillis() != null &&
                    tbrFirst.getActivityEndTimeMillis() != null) {
                long diff = TimeUnit.MILLISECONDS.toMinutes(tbrFirst.getActivityEndTimeMillis() -
                        tbrFirst.getActivityStartTimeMillis());
                tbrFirst.setActivityDuration(diff);
            }

            if (tbrFirst.getStartLat() != null && tbrFirst.getStartLon() != null &&
                    tbrFirst.getEndLat() != null && tbrFirst.getEndLon() != null) {
                float distance = LocationUtils.computeDistanceAndBearing(tbrFirst.getStartLat(), tbrFirst.getStartLon(),
                        tbrFirst.getEndLat(), tbrFirst.getEndLon());
                tbrFirst.setOriginDestinationDistance(distance);
            }

            mOneDayTravelBehaviorRecordList.remove(mOneDayTravelBehaviorRecordList.size() - 1);
            mOneDayTravelBehaviorRecordList.remove(mOneDayTravelBehaviorRecordList.size() - 1);
            mOneDayTravelBehaviorRecordList.remove(mOneDayTravelBehaviorRecordList.size() - 1);
            mOneDayTravelBehaviorRecordList.add(tbrFirst);

        }
    }

    private void applyTourAlgorithmToOneDayRecordList() {
        if (mOneDayTravelBehaviorRecordList.size() < 2) {
            flushOneDayTravelBehaviorRecordList();
            return;
        }

        // Assume the first record of the day is at home
        TravelBehaviorRecord homeTbr = mOneDayTravelBehaviorRecordList.get(0);
        int startIndex = 0;
        int tailIndex = 1;

        while (startIndex < mOneDayTravelBehaviorRecordList.size() && tailIndex < mOneDayTravelBehaviorRecordList.size()) {
            TravelBehaviorRecord currTbr = mOneDayTravelBehaviorRecordList.get(tailIndex);
            if (LocationUtils.isTravelBehaviorRecordsClose(homeTbr, currTbr)) {
                setTourIdBetweenIndices(startIndex, tailIndex);
                startIndex = ++tailIndex;
            } else {
                tailIndex++;
            }
        }

        flushOneDayTravelBehaviorRecordList();
    }

    private void setTourIdBetweenIndices(int startIndex, int tailIndex) {
        int tourId = mTourId++;
        int tourIndex = 1;
        for (int i = startIndex; i <= tailIndex; i++) {
            mOneDayTravelBehaviorRecordList.get(i).setTourId(tourId).setTourIndex(tourIndex++);
        }
    }

    private void flushOneDayTravelBehaviorRecordList() {
        // flush all data to csv
        mCSVFileWriter.appendAllToCsV(mOneDayTravelBehaviorRecordList);
        // clean the one day list
        mOneDayTravelBehaviorRecordList.clear();
    }
}
