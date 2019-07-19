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
package edu.usf.cutr.model;

public class TravelBehaviorRecord {

    public static final String[] CSV_HEADER = {"User ID",
            "Trip ID",
            "Region ID",
            "Google Activity",
            "Google Activity Confidence",
            "Vehicle type",
            "Origin Date and Time (UTC)",
            "Origin Date and Time Source",
            "Origin location Date and Time (UTC)",
            "Activity Start/Origin Time Diff (minutes)",
            "Origin latitude",
            "Origin longitude",
            "Origin Horizontal Accuracy",
            "Origin Location Provider",
            "Destination Date and Time (UTC)",
            "Destination Date and Time Source",
            "Destination Location Date and Time (UTC)",
            "Activity End/Destination Time Diff (minutes)",
            "Destination latitude",
            "Destination longitude",
            "Destination Horizontal Accuracy",
            "Destination Location Provider",
            "Activity duration (minutes)",
            "Chain ID",
            "Chain Index",
            "Tour ID",
            "Tour Index"
    };

    private String mUserId;
    private String mTripId;
    private String mRegionId;
    private String mGoogleActivity;
    private Float mGoogleConfidence;
    private String mVehicleType;
    private String mStartDateAndTime;
    private String mStartDateAndTimeSource;
    private Double mStartLat;
    private Double mStartLon;
    private String mEndDateAndTime;
    private String mEndDateAndTimeSource;
    private Double mEndLat;
    private Double mEndLon;
    private Long mActivityDuration;
    private Integer mChainId;
    private Integer mChainIndex;
    private Integer mTourId;
    private Integer mTourIndex;
    private String mOriginLocationDateAndTime;
    private Integer mActivityStartOriginTimeDiff;
    private Float mOriginHorAccuracy;
    private String mOriginProvider;
    private String mDestinationLocationDateAndTime;
    private Integer mActivityEndDestinationTimeDiff;
    private Float mDestinationHorAccuracy;
    private String mDestinationProvider;

    // Internal user
    private Long startTimeMillis;
    private Long endTimeMillis;

    public TravelBehaviorRecord(String userId) {
        mUserId = userId;
    }

    public TravelBehaviorRecord setRegionId(String regionId) {
        mRegionId = regionId;
        return this;
    }

    public TravelBehaviorRecord setTripId(String tripId) {
        mTripId = tripId;
        return this;
    }

    public String getGoogleActivity() {
        return mGoogleActivity;
    }

    public TravelBehaviorRecord setGoogleActivity(String googleActivity) {
        mGoogleActivity = googleActivity;
        return this;
    }

    public TravelBehaviorRecord setGoogleConfidence(Float googleConfidence) {
        mGoogleConfidence = googleConfidence;
        return this;
    }

    public TravelBehaviorRecord setVehicleType(String vehicleType) {
        mVehicleType = vehicleType;
        return this;
    }

    public TravelBehaviorRecord setStartLat(Double startLat) {
        mStartLat = startLat;
        return this;
    }

    public TravelBehaviorRecord setStartLon(Double startLon) {
        mStartLon = startLon;
        return this;
    }

    public TravelBehaviorRecord setEndLat(Double endLat) {
        mEndLat = endLat;
        return this;
    }

    public TravelBehaviorRecord setEndLon(Double endLon) {
        mEndLon = endLon;
        return this;
    }

    public TravelBehaviorRecord setActivityDuration(Long activityDuration) {
        mActivityDuration = activityDuration;
        return this;
    }

    public TravelBehaviorRecord setChainId(Integer chainId) {
        mChainId = chainId;
        return this;
    }

    public TravelBehaviorRecord setChainIndex(Integer chainIndex) {
        mChainIndex = chainIndex;
        return this;
    }

    public TravelBehaviorRecord setTourId(Integer tourId) {
        mTourId = tourId;
        return this;
    }

    public TravelBehaviorRecord setTourIndex(Integer tourIndex) {
        mTourIndex = tourIndex;
        return this;
    }

    public TravelBehaviorRecord setStartDateAndTime(String startDateAndTime) {
        mStartDateAndTime = startDateAndTime;
        return this;
    }

    public TravelBehaviorRecord setEndDateAndTime(String endDateAndTime) {
        mEndDateAndTime = endDateAndTime;
        return this;
    }

    public TravelBehaviorRecord setStartDateAndTimeSource(String startDateAndTimeSource) {
        mStartDateAndTimeSource = startDateAndTimeSource;
        return this;
    }

    public TravelBehaviorRecord setEndDateAndTimeSource(String endDateAndTimeSource) {
        mEndDateAndTimeSource = endDateAndTimeSource;
        return this;
    }

    public Long getStartTimeMillis() {
        return startTimeMillis;
    }

    public TravelBehaviorRecord setStartTimeMillis(Long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
        return this;
    }

    public Long getEndTimeMillis() {
        return endTimeMillis;
    }

    public TravelBehaviorRecord setEndTimeMillis(Long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
        return this;
    }

    public TravelBehaviorRecord setOriginLocationDateAndTime(String originLocationDateAndTime) {
        mOriginLocationDateAndTime = originLocationDateAndTime;
        return this;
    }

    // TODO: Implement time difference
    public TravelBehaviorRecord setActivityStartOriginTimeDiff(Integer activityStartOriginTimeDiff) {
        mActivityStartOriginTimeDiff = activityStartOriginTimeDiff;
        return this;
    }

    public TravelBehaviorRecord setOriginHorAccuracy(Float originHorAccuracy) {
        mOriginHorAccuracy = originHorAccuracy;
        return this;
    }

    public TravelBehaviorRecord setOriginProvider(String originProvider) {
        mOriginProvider = originProvider;
        return this;
    }

    public TravelBehaviorRecord setDestinationLocationDateAndTime(String destinationLocationDateAndTime) {
        mDestinationLocationDateAndTime = destinationLocationDateAndTime;
        return this;
    }

    public TravelBehaviorRecord setActivityEndDestinationTimeDiff(Integer activityEndDestinationTimeDiff) {
        mActivityEndDestinationTimeDiff = activityEndDestinationTimeDiff;
        return this;
    }

    public TravelBehaviorRecord setDestinationHorAccuracy(Float destinationHorAccuracy) {
        mDestinationHorAccuracy = destinationHorAccuracy;
        return this;
    }

    public TravelBehaviorRecord setDestinationProvider(String destinationProvider) {
        mDestinationProvider = destinationProvider;
        return this;
    }

    public String[] toStringArray() {
        return new String[]{mUserId, mTripId, mRegionId, mGoogleActivity, String.valueOf(mGoogleConfidence), mVehicleType,
                mStartDateAndTime, mStartDateAndTimeSource, mOriginLocationDateAndTime, String.valueOf(mActivityStartOriginTimeDiff),
                String.valueOf(mStartLat), String.valueOf(mStartLon), String.valueOf(mOriginHorAccuracy), mOriginProvider,
                mEndDateAndTime, mEndDateAndTimeSource, mDestinationLocationDateAndTime, String.valueOf(mActivityEndDestinationTimeDiff),
                String.valueOf(mEndLat), String.valueOf(mEndLon), String.valueOf(mDestinationHorAccuracy), mDestinationProvider,
                String.valueOf(mActivityDuration), String.valueOf(mChainId), String.valueOf(mChainIndex),
                String.valueOf(mTourId), String.valueOf(mTourId)};
    }
}
