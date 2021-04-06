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
package edu.usf.cutr.tba.manager;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import edu.usf.cutr.tba.constants.TravelBehaviorConstants;
import edu.usf.cutr.tba.exception.FirebaseFileNotInitializedException;
import edu.usf.cutr.tba.io.CSVFileWriter;
import edu.usf.cutr.tba.io.FirebaseReader;
import edu.usf.cutr.tba.io.KmlFileWriter;
import edu.usf.cutr.tba.model.DeviceInformation;
import edu.usf.cutr.tba.model.TravelBehaviorInfo;
import edu.usf.cutr.tba.model.TravelBehaviorRecord;
import edu.usf.cutr.tba.options.ProgramOptions;
import edu.usf.cutr.tba.utils.LocationUtils;
import edu.usf.cutr.tba.utils.QueryDocumentSnapshotComparator;
import edu.usf.cutr.tba.utils.QueryDocumentSnapshotDeviceComparator;
import edu.usf.cutr.tba.utils.TravelBehaviorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TravelBehaviorDataAnalysisManager {

    private ProgramOptions mProgramOptions;

    private FirebaseReader mFirebaseReader;

    private CSVFileWriter mCSVFileWriter;

    private KmlFileWriter mKmlFileWriter;

    private TravelBehaviorRecord mLastTravelBehaviorRecord;

    private List<TravelBehaviorRecord> mOneDayTravelBehaviorRecordList;

    private int mTripId = 0;

    private int mTourId = 0;

    public TravelBehaviorDataAnalysisManager() throws FirebaseFileNotInitializedException {
        mFirebaseReader = new FirebaseReader();
        mCSVFileWriter = new CSVFileWriter();
        mKmlFileWriter = new KmlFileWriter();
        mOneDayTravelBehaviorRecordList = new ArrayList<>();
        mProgramOptions = ProgramOptions.getInstance();
    }

    public void processData() {
        // create csv file and add the header
        mCSVFileWriter.createHeader(TravelBehaviorRecord.CSV_HEADER);

        if (mProgramOptions.getUserId() == null) {
            // analyze all data and append the data in the csv file
            analyzeAllTravelBehaviorData();
        } else {
            // analyze specific user data
            processUserById(mProgramOptions.getUserId());
        }

        //close the csv file
        mCSVFileWriter.closeWriter();
    }

    /**
     * Retrieves all user ids in firebase and analyses each user's data one by one
     */
    private void analyzeAllTravelBehaviorData() {
        List<QueryDocumentSnapshot> allUserIds = mFirebaseReader.getAllUserIds();
        for (QueryDocumentSnapshot doc : allUserIds) {
            processUserById(doc.getId());
        }
    }

    /**
     * analyzes a user data by the given user id
     * @param userId firebase user id
     */
    private void processUserById(String userId) {
        // Holds all user data by id
        List<QueryDocumentSnapshot> userInfoById;

        long startDateMillis = mProgramOptions.getStartDate();
        long endDateMillis = mProgramOptions.getEndDate();

        if (startDateMillis > 0 && endDateMillis > 0) {
            // Valid date range exists, make a filtered query by using date range
            userInfoById = new ArrayList<>(mFirebaseReader.getAllUserInfoByIdAndDateRange(userId, startDateMillis, endDateMillis));
        } else {
            // No valid date range defined, continue with a regular query by userId
            userInfoById = new ArrayList<>(mFirebaseReader.getAllUserInfoById(userId));
        }
        // sorts the data by activity time
        Collections.sort(userInfoById, new QueryDocumentSnapshotComparator());

        mLastTravelBehaviorRecord = null;

        // Get the device information of the current userId
        List<QueryDocumentSnapshot> userDeviceInfoList = new ArrayList<>(mFirebaseReader.getAllUserDeviceInfoById(userId));

        // Sort the data by timestamp, if timestamp is not available, then the comparator will
        // fall back to the QueryDocumentSnapshot id which is assumed has the timestamp as its name
        Collections.sort(userDeviceInfoList, new QueryDocumentSnapshotDeviceComparator());

        // analyze each transition activity of the user one by one
        for (QueryDocumentSnapshot doc : userInfoById) {
            processUserActivityTransitionData(doc, userId, userDeviceInfoList);
        }

        if (mOneDayTravelBehaviorRecordList.size() > 0) {
            applyTourAlgorithmToOneDayRecordList();
        }
    }

    /**
     *  -- if mLastTravelBehaviorRecord is null which means there is no previous enter activity then we look for an enter
     *     activity ub tge given Travel Behavior data.
     *  -- if mLastTravelBehaviorRecord is not null which means there is a previous enter activity then we look for an
     *     exit activity in the data.
     *
     * @param doc user's travel behavior data
     * @param userId firebase user id
     * @param userDeviceInfoList a list that contains user's device info
     */
    private void processUserActivityTransitionData(QueryDocumentSnapshot doc, String userId,
                                                   List<QueryDocumentSnapshot> userDeviceInfoList) {
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
                completeTravelBehaviorRecord(tbi, doc, userDeviceInfoList);
                mLastTravelBehaviorRecord.setTripId(String.valueOf(mTripId++));

                addTravelBehaviorRecord(mLastTravelBehaviorRecord);

                mergeStillEvents();

                mergeWalkingAndRunningEvents();
            }

            mLastTravelBehaviorRecord = null;

            TravelBehaviorInfo.TravelBehaviorActivity enterActivity = TravelBehaviorUtils.getEnterActivity(tbi.activities);
            if (enterActivity != null) {
                mLastTravelBehaviorRecord = createTravelBehaviorRecord(userId, tbi, enterActivity);
            }
        }
    }

    /**
     * The method creates a TravelBehaviorRecord for an enter activity which is a row representation in the output CSV
     * The created object is not complete and it only contains the data from the enter activity. In order for this
     * object to be valid, the next TravelBehavior data should be a matching exit activity.
     *
     * @param userId firebase user id
     * @param tbi Firebase TravelBehaviorInfo object
     * @param enterActivity enter activity
     * @return
     */
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
            tbr.setActivityStartOriginTimeDiff(TravelBehaviorUtils.millisToMinutes(diff));
        }


        return tbr;
    }

    /**
     * This method completes the last travel behavior record object with an exit activity.
     *
     * @param tbi TravelBehaviorInfo
     * @param doc Firebase representation of  TravelBehaviorInfo, we use it's time to determine the closest device info
     * @param userDeviceInfoList the device info list that contains the region id
     */
    private void completeTravelBehaviorRecord(TravelBehaviorInfo tbi, QueryDocumentSnapshot doc,
                                              List<QueryDocumentSnapshot> userDeviceInfoList) {
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
            mLastTravelBehaviorRecord.setActivityEndDestinationTimeDiff(TravelBehaviorUtils.millisToMinutes(diff));
        }

        if (mLastTravelBehaviorRecord.getActivityStartTimeMillis() != null &&
                mLastTravelBehaviorRecord.getActivityEndTimeMillis() != null) {
            long diff = mLastTravelBehaviorRecord.getActivityEndTimeMillis() - mLastTravelBehaviorRecord.
                    getActivityStartTimeMillis();
            mLastTravelBehaviorRecord.setActivityDuration(TravelBehaviorUtils.millisToMinutes(diff));
        }

        if (mLastTravelBehaviorRecord.getStartLat() != null && mLastTravelBehaviorRecord.getStartLon() != null &&
                mLastTravelBehaviorRecord.getEndLat() != null && mLastTravelBehaviorRecord.getEndLon() != null) {
            float distance = LocationUtils.computeDistanceAndBearing(mLastTravelBehaviorRecord.getStartLat(),
                    mLastTravelBehaviorRecord.getStartLon(), mLastTravelBehaviorRecord.getEndLat(),
                    mLastTravelBehaviorRecord.getEndLon());
            mLastTravelBehaviorRecord.setOriginDestinationDistance(distance);
        }

        // While completing TBR update isIgnoringBatteryOptimization, isTalkBackEnabled
        // and isPowerSaveModeOn
        if (mLastTravelBehaviorRecord.getActivityEndTimeMillis() != null) {
            // Get the previous DeviceInformation but closest in time to the activity end time
            DeviceInformation dvInfo = TravelBehaviorUtils.getClosestDeviceInfo(userDeviceInfoList,
                    mLastTravelBehaviorRecord.getActivityEndTimeMillis());
            if (dvInfo != null) {
                // Get the corresponding properties when available
                if (dvInfo.getIgnoringBatteryOptimizations() != null) {
                    mLastTravelBehaviorRecord.setIsIgnoringBatteryOptimization(dvInfo.getIgnoringBatteryOptimizations());
                }
                if (dvInfo.getTalkBackEnabled() != null) {
                    mLastTravelBehaviorRecord.setIsTalkBackEnabled(dvInfo.getTalkBackEnabled());
                }

                if (dvInfo.getPowerSaveModeEnabled() != null){
                    mLastTravelBehaviorRecord.setIsPowerSaveModeEnabled(dvInfo.getPowerSaveModeEnabled());
                }
                // setRegionID
                mLastTravelBehaviorRecord.setRegionId(String.valueOf(dvInfo.regionId));
            }
        }
    }


    /**
     * Finds a region id in the device info list. It picks the closest time device info that is entered with the record
     * It performs binary search to find the closest device info
     * @param doc firebase representation of travel behavior data
     * @param userDeviceInfoList device info list
     * @return region id
     */
    private String findRegionIdFromDeviceInfoList(QueryDocumentSnapshot doc,
                                                  List<QueryDocumentSnapshot> userDeviceInfoList) {
        if (userDeviceInfoList == null || userDeviceInfoList.size() == 0) return null;
        long updateTimeMillis = doc.getUpdateTime().toDate().getTime();

        // binary search to find closest timestamp record
        int low = 0;
        int high = userDeviceInfoList.size() - 1;

        while (low < high) {
            int mid = (low + high) / 2;
            assert (mid < high);
            long d1 = Math.abs(userDeviceInfoList.get(mid).getUpdateTime().toDate().getTime() - updateTimeMillis);
            long d2 = Math.abs(userDeviceInfoList.get(mid).getUpdateTime().toDate().getTime() - updateTimeMillis);
            if (d2 <= d1) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        DeviceInformation deviceInformation = userDeviceInfoList.get(high).toObject(DeviceInformation.class);
        return String.valueOf(deviceInformation.regionId);
    }

    /**
     * Adds a completed travel behavior record to the one day list
     * if the given data is belong to next day it applies the tour algorithm and flushes the all today's data
     * then adds the new data that belongs to the next day.
     *
     * @param tbr TravelBehaviorRecord
     */
    private void addTravelBehaviorRecord(TravelBehaviorRecord tbr) {
        // By default the day starts at 3 AM and ends at 3 AM next day
        long sameDayDiffHours = ProgramOptions.getInstance().getSameDayStartPoint() == null ? TravelBehaviorConstants.
                SAME_DAY_TIME_DIFF : ProgramOptions.getInstance().getSameDayStartPoint();

        if (mOneDayTravelBehaviorRecordList.size() == 0) {
            // add new data to list with new tour id
            mOneDayTravelBehaviorRecordList.add(tbr);
        } else if (TravelBehaviorUtils.isInSameDay(mOneDayTravelBehaviorRecordList, tbr, sameDayDiffHours)) {
            mOneDayTravelBehaviorRecordList.add(tbr);
        } else {
            applyTourAlgorithmToOneDayRecordList();
            // add new data to list with new tour id
            mOneDayTravelBehaviorRecordList.add(tbr);
        }
    }

    /**
     * TODO: Implement subtours to this tour algorithm
     *
     * Tour Algorithm:
     * 1. Put all activity transition records that happened in the same day
     *    in a list
     * 2.  Assume the first element in the list is the home of the user
     * 3.  Iterate over the list, if the user comes back to home then
     * mark all transition between two home travel as one tour
     * 4. If the user does not come to the starting place (home),
     * all activity transition are marked with new tour ids
     */
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

    /**
     * Writes all one day travel behavior data to the CSV file
     */
    private void flushOneDayTravelBehaviorRecordList() {
        // flush all data to csv
        mCSVFileWriter.appendAllToCsV(mOneDayTravelBehaviorRecordList);
        // Write a KML file for this user's behavior for this day
        mKmlFileWriter.appendAllToKml(mOneDayTravelBehaviorRecordList);
        // clean the one day list
        mOneDayTravelBehaviorRecordList.clear();
    }

    /**
     * Every time we add a transition record to the same day record list,
     * we look if this event is walking or running and we look the previous
     * event. If the previous event is walking, running, or walking/running
     * we merge them together.
     * And make the event walking and running.
     */
    private void mergeWalkingAndRunningEvents() {
        int size = mOneDayTravelBehaviorRecordList.size();
        if (!mProgramOptions.isMergeAllWalkingAndRunningEventsEnabled() || size < 2) return;

        if ((TravelBehaviorConstants.ACTIVITY_RUNNING.equals(mOneDayTravelBehaviorRecordList.get(size - 1).
                getGoogleActivity()) || TravelBehaviorConstants.ACTIVITY_WALKING.equals(mOneDayTravelBehaviorRecordList.get(size - 1).
                getGoogleActivity())) && (TravelBehaviorConstants.ACTIVITY_RUNNING.equals(mOneDayTravelBehaviorRecordList.get(size - 2).
                getGoogleActivity()) || TravelBehaviorConstants.ACTIVITY_WALKING.equals(mOneDayTravelBehaviorRecordList.get(size - 2).
                getGoogleActivity()) || TravelBehaviorConstants.ACTIVITY_WALKING_AND_RUNNING.equals(mOneDayTravelBehaviorRecordList.get(size - 2).
                getGoogleActivity()))){

            TravelBehaviorRecord tbrFirst = mOneDayTravelBehaviorRecordList.get(size - 2);
            TravelBehaviorRecord tbrSecond = mOneDayTravelBehaviorRecordList.get(size - 1);

            long walkingRunningActivityMergeThreshold = mProgramOptions.getWalkingRunningEventMergeThreshold() == null ?
                    TravelBehaviorConstants.WALKING_RUNNING_THRESHOLD : TimeUnit.MINUTES.toMillis(mProgramOptions.
                    getWalkingRunningEventMergeThreshold());

            if (tbrFirst.getActivityEndTimeMillis() != null && tbrSecond.getActivityStartTimeMillis() != null &&
                    tbrSecond.getActivityStartTimeMillis() - tbrFirst.getActivityEndTimeMillis() <
                            walkingRunningActivityMergeThreshold) {

                mergeTravelBehaviorRecord(tbrFirst, tbrSecond);
                tbrFirst.setGoogleActivity(TravelBehaviorConstants.ACTIVITY_WALKING_AND_RUNNING);

                mOneDayTravelBehaviorRecordList.remove(mOneDayTravelBehaviorRecordList.size() - 1);
                mOneDayTravelBehaviorRecordList.remove(mOneDayTravelBehaviorRecordList.size() - 1);
                mOneDayTravelBehaviorRecordList.add(tbrFirst);
            }
        }
    }

    /**
     * Every time we add a transition record to the same day record list,
     * we look if the previous record is a still event and the two previous
     * event is the same event with this event and if the still event's duration
     * is less then the threshold we remove the middle still event and merge
     * two same (e.g., in_vehicle, in_vehicle) or similar events (e.g., walking,
     *  walking/running) events into a single event.
     */
    private void mergeStillEvents() {
        if (!mProgramOptions.isMergeStillEventsEnabled()) return;

        int size = mOneDayTravelBehaviorRecordList.size();
        if (size < 3 || TravelBehaviorConstants.ACTIVITY_STILL.equals(mOneDayTravelBehaviorRecordList.get(size - 1).
                getGoogleActivity()) || !TravelBehaviorConstants.ACTIVITY_STILL.equals(mOneDayTravelBehaviorRecordList.
                get(size - 2).getGoogleActivity())) {
            return;
        }

        TravelBehaviorRecord tbrFirst = mOneDayTravelBehaviorRecordList.get(size - 3);
        TravelBehaviorRecord tbrLast = mOneDayTravelBehaviorRecordList.get(size - 1);

        long stillActivityMergeThreshold = mProgramOptions.getStillEventMergeThreshold() == null ?
                TravelBehaviorConstants.STILL_ACTIVITY_THRESHOLD : TimeUnit.MINUTES.toMillis(mProgramOptions.
                getStillEventMergeThreshold());

        if ((tbrFirst.getGoogleActivity().contains(tbrLast.getGoogleActivity()) ||
                tbrLast.getGoogleActivity().contains(tbrFirst.getGoogleActivity())) &&
                tbrFirst.getActivityEndTimeMillis() != null && tbrLast.getActivityStartTimeMillis() != null &&
                tbrLast.getActivityStartTimeMillis() - tbrFirst.getActivityEndTimeMillis() < stillActivityMergeThreshold) {

            mergeTravelBehaviorRecord(tbrFirst, tbrLast);

            mOneDayTravelBehaviorRecordList.remove(mOneDayTravelBehaviorRecordList.size() - 1);
            mOneDayTravelBehaviorRecordList.remove(mOneDayTravelBehaviorRecordList.size() - 1);
            mOneDayTravelBehaviorRecordList.remove(mOneDayTravelBehaviorRecordList.size() - 1);
            mOneDayTravelBehaviorRecordList.add(tbrFirst);
        }
    }

    /**
     * Merges two TravelBehaviorRecord's into the first TravelBehaviorRecord object
     * @param tbrFirst First TravelBehaviorRecord
     * @param tbrLast Second TravelBehaviorRecord
     */
    private void mergeTravelBehaviorRecord(TravelBehaviorRecord tbrFirst, TravelBehaviorRecord tbrLast) {
        tbrFirst.setActivityEndDateAndTime(tbrLast.getActivityEndDateAndTime()).setActivityEndTimeMillis(
                tbrLast.getActivityEndTimeMillis()).setEndLat(tbrLast.getEndLat()).setEndLon(tbrLast.getEndLon()).
                setDestinationLocationDateAndTime(tbrLast.getDestinationLocationDateAndTime()).
                setDestinationHorAccuracy(tbrLast.getDestinationHorAccuracy()).setDestinationProvider
                (tbrLast.getDestinationProvider()).setLocationEndTimeMillis(tbrLast.getLocationEndTimeMillis()).
                setActivityEndDestinationTimeDiff(tbrLast.getActivityEndDestinationTimeDiff()).setRegionId(
                tbrLast.getRegionId());

        if (tbrFirst.getActivityStartTimeMillis() != null &&
                tbrFirst.getActivityEndTimeMillis() != null) {
            long diff = tbrFirst.getActivityEndTimeMillis() - tbrFirst.getActivityStartTimeMillis();
            tbrFirst.setActivityDuration(TravelBehaviorUtils.millisToMinutes(diff));
        }

        if (tbrFirst.getStartLat() != null && tbrFirst.getStartLon() != null &&
                tbrFirst.getEndLat() != null && tbrFirst.getEndLon() != null) {
            float distance = LocationUtils.computeDistanceAndBearing(tbrFirst.getStartLat(), tbrFirst.getStartLon(),
                    tbrFirst.getEndLat(), tbrFirst.getEndLon());
            tbrFirst.setOriginDestinationDistance(distance);
        }

        if (tbrLast.getGoogleActivity().equals(TravelBehaviorConstants.ACTIVITY_WALKING_AND_RUNNING)) {
            tbrFirst.setGoogleActivity(TravelBehaviorConstants.ACTIVITY_WALKING_AND_RUNNING);
        }
    }
}
