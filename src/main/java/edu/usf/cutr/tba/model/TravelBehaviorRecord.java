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
package edu.usf.cutr.tba.model;

import edu.usf.cutr.tba.utils.StringUtils;
import edu.usf.cutr.tba.utils.TravelBehaviorUtils;

import java.util.List;

import static edu.usf.cutr.tba.utils.TravelBehaviorUtils.getLocationInfo;

public class TravelBehaviorRecord {

    public static final String[] CSV_HEADER = {"User ID",
            "Trip ID",
            "Region ID",
            "Google Activity",
            "Google Activity Confidence",
            "Vehicle type",
            "Activity Start Date and Time (UTC)",
            "Origin location Date and Time (UTC)",
            "Activity Start/Origin Time Diff (minutes)",
            "Origin latitude",
            "Origin longitude",
            "Origin Horizontal Accuracy",
            "Origin Location Provider",
            "Activity Destination Date and Time (UTC)",
            "Destination Location Date and Time (UTC)",
            "Activity End/Destination Time Diff (minutes)",
            "Destination latitude",
            "Destination longitude",
            "Destination Horizontal Accuracy",
            "Destination Location Provider",
            "Duration (minutes)",
            "Origin-Destination Bird-Eye Distance (meters)",
            "Chain ID",
            "Chain Index",
            "Tour ID",
            "Tour Index",
            "Ignoring Battery Optimizations",
            "Talk Back Enabled",
            "Power Save Mode Enabled",
            "Origin fused Date and Time (UTC)",
            "Origin latitude fused",
            "Origin longitude fused",
            "Origin gps Date and Time (UTC)",
            "Origin latitude gps",
            "Origin longitude gps",
            "Origin network Date and Time (UTC)",
            "Origin latitude network",
            "Origin longitude network",
            "Destination fused Date and Time (UTC)",
            "Destination latitude fused",
            "Destination longitude fused",
            "Destination gps Date and Time (UTC)",
            "Destination latitude gps",
            "Destination longitude gps",
            "Destination network Date and Time (UTC)",
            "Destination latitude network",
            "Destination longitude network"
    };

    public static final String FUSED = "fused";
    public static final String GPS = "gps";
    public static final String NETWORK = "network";

    private String mUserId;
    private String mTripId;
    private String mRegionId;
    private String mGoogleActivity;
    private Float mGoogleConfidence;
    private String mVehicleType;
    private String mActivityStartDateAndTime;
    private Double mStartLat;
    private Double mStartLon;
    private String mActivityEndDateAndTime;
    private Double mEndLat;
    private Double mEndLon;
    private Float mActivityDuration;
    private Integer mChainId;
    private Integer mChainIndex;
    private Integer mTourId;
    private Integer mTourIndex;
    private String mOriginLocationDateAndTime;
    private Float mActivityStartOriginTimeDiff;
    private Float mOriginHorAccuracy;
    private String mOriginProvider;
    private String mDestinationLocationDateAndTime;
    private Float mOriginDestinationDistance;
    private Float mActivityEndDestinationTimeDiff;
    private Float mDestinationHorAccuracy;
    private String mDestinationProvider;

    private Boolean mIsIgnoringBatteryOptimizations;
    private Boolean mIsTalkBackEnabled;
    private Boolean mIsPowerSaveModeEnabled;

    public List<TravelBehaviorInfo.LocationInfo> locationInfoListOrigin = null;
    public List<TravelBehaviorInfo.LocationInfo> locationInfoListDestination = null;

    // Internal usage
    private Long mActivityStartTimeMillis;
    private Long mActivityEndTimeMillis;
    private Long mLocationStartTimeMillis;
    private Long mLocationEndTimeMillis;

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

    /**
     * The activity recognized by the Android Activity Transition API (see https://developer.android.com/guide/topics/location/transitions)
     *
     * @return activity recognized by the Android Activity Transition API
     */
    public String getGoogleActivity() {
        return mGoogleActivity;
    }

    /**
     * The confidence returned by the Android Activity Recognition API, following an activity transition. Values are between 0 and 1.
     *
     * @return confidence returned by the Android Activity Recognition API, following an activity transition. Values are between 0 and 1.
     */
    public Float getGoogleConfidence() {
        return mGoogleConfidence;
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

    public TravelBehaviorRecord setActivityDuration(Float activityDuration) {
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

    public TravelBehaviorRecord setActivityStartDateAndTime(String activityStartDateAndTime) {
        mActivityStartDateAndTime = activityStartDateAndTime;
        return this;
    }

    public TravelBehaviorRecord setActivityEndDateAndTime(String activityEndDateAndTime) {
        mActivityEndDateAndTime = activityEndDateAndTime;
        return this;
    }

    public TravelBehaviorRecord setActivityStartTimeMillis(Long activityStartTimeMillis) {
        this.mActivityStartTimeMillis = activityStartTimeMillis;
        return this;
    }

    public TravelBehaviorRecord setLocationStartTimeMillis(Long locationStartTimeMillis) {
        mLocationStartTimeMillis = locationStartTimeMillis;
        return this;
    }

    public TravelBehaviorRecord setLocationEndTimeMillis(Long locationEndTimeMillis) {
        this.mLocationEndTimeMillis = locationEndTimeMillis;
        return this;
    }

    public TravelBehaviorRecord setActivityEndTimeMillis(Long activityEndTimeMillis) {
        this.mActivityEndTimeMillis = activityEndTimeMillis;
        return this;
    }

    public TravelBehaviorRecord setOriginLocationDateAndTime(String originLocationDateAndTime) {
        mOriginLocationDateAndTime = originLocationDateAndTime;
        return this;
    }

    public TravelBehaviorRecord setActivityStartOriginTimeDiff(Float activityStartOriginTimeDiff) {
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

    public TravelBehaviorRecord setActivityEndDestinationTimeDiff(Float activityEndDestinationTimeDiff) {
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

    public TravelBehaviorRecord setOriginDestinationDistance(Float originDestinationDistance) {
        mOriginDestinationDistance = originDestinationDistance;
        return this;
    }

    public  TravelBehaviorRecord setIsIgnoringBatteryOptimization(Boolean isIgnoringBatteryOptimization) {
        mIsIgnoringBatteryOptimizations = isIgnoringBatteryOptimization;
        return this;
    }

    public TravelBehaviorRecord setIsTalkBackEnabled(Boolean isTalkBackEnabled) {
        mIsTalkBackEnabled = isTalkBackEnabled;
        return this;
    }

    public TravelBehaviorRecord setIsPowerSaveModeEnabled(Boolean isPowerSaveModeEnabled) {
        mIsPowerSaveModeEnabled = isPowerSaveModeEnabled;
        return this;
    }

    public String getRegionId() {
        return mRegionId;
    }

    public Double getStartLat() {
        return mStartLat;
    }

    public Double getStartLon() {
        return mStartLon;
    }

    public Long getLocationStartTimeMillis() {
        return mLocationStartTimeMillis;
    }

    public Long getLocationEndTimeMillis() {
        return mLocationEndTimeMillis;
    }

    public Long getActivityStartTimeMillis() {
        return mActivityStartTimeMillis;
    }

    public Long getActivityEndTimeMillis() {
        return mActivityEndTimeMillis;
    }

    public Double getEndLat() {
        return mEndLat;
    }

    public Double getEndLon() {
        return mEndLon;
    }

    public String getActivityStartDateAndTime() {
        return mActivityStartDateAndTime;
    }

    public String getActivityEndDateAndTime() {
        return mActivityEndDateAndTime;
    }

    public String getDestinationLocationDateAndTime() {
        return mDestinationLocationDateAndTime;
    }

    /**
     * Returns the time difference between the activity end time and location time, in minutes
     * @return the time difference between the activity end time and location time, in minutes
     */
    public Float getActivityEndDestinationTimeDiff() {
        return mActivityEndDestinationTimeDiff;
    }

    /**
     * Returns the time difference between the activity start time and location time, in minutes
     *
     * @return the time difference between the activity start time and location time, in minutes
     */
    public Float getActivityStartOriginTimeDiff() {
        return mActivityStartOriginTimeDiff;
    }

    public Float getDestinationHorAccuracy() {
        return mDestinationHorAccuracy;
    }

    public Float getOriginHorAccuracy() {
        return mOriginHorAccuracy;
    }


    public String getDestinationProvider() {
        return mDestinationProvider;
    }

    public String getOriginProvider() {
        return mOriginProvider;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getTripId() {
        return mTripId;
    }

    /**
     * Returns duration of the activity, in minutes
     *
     * @return duration of the activity, in minutes
     */
    public Float getActivityDuration() {
        return mActivityDuration;
    }

    public Boolean getIsIgnoringBatteryOptimization() { return mIsIgnoringBatteryOptimizations; }

    public Boolean getIsTalkBackEnabled() { return mIsTalkBackEnabled; }

    public Boolean getIsPowerSaveModeEnabled() { return mIsPowerSaveModeEnabled; }


    public String[] toStringArray() {
        TravelBehaviorInfo.LocationInfo locationStartFused = getLocationInfo(locationInfoListOrigin, FUSED);
        TravelBehaviorInfo.LocationInfo locationStartGps = getLocationInfo(locationInfoListOrigin, GPS);
        TravelBehaviorInfo.LocationInfo locationStartNetwork = getLocationInfo(locationInfoListOrigin, NETWORK);
        TravelBehaviorInfo.LocationInfo locationEndFused = getLocationInfo(locationInfoListDestination, FUSED);
        TravelBehaviorInfo.LocationInfo locationEndGps = getLocationInfo(locationInfoListDestination, GPS);
        TravelBehaviorInfo.LocationInfo locationEndNetwork = getLocationInfo(locationInfoListDestination, NETWORK);
        return new String[]{mUserId, mTripId, mRegionId, mGoogleActivity, StringUtils.valueOf(mGoogleConfidence), mVehicleType,
                mActivityStartDateAndTime, mOriginLocationDateAndTime, StringUtils.valueOf(mActivityStartOriginTimeDiff),
                StringUtils.valueOf(mStartLat), StringUtils.valueOf(mStartLon), StringUtils.valueOf(mOriginHorAccuracy), mOriginProvider,
                mActivityEndDateAndTime, mDestinationLocationDateAndTime, StringUtils.valueOf(mActivityEndDestinationTimeDiff),
                StringUtils.valueOf(mEndLat), StringUtils.valueOf(mEndLon), StringUtils.valueOf(mDestinationHorAccuracy), mDestinationProvider,
                StringUtils.valueOf(mActivityDuration), StringUtils.valueOf(mOriginDestinationDistance), StringUtils.valueOf(mChainId),
                StringUtils.valueOf(mChainIndex), StringUtils.valueOf(mTourId), StringUtils.valueOf(mTourIndex),
                StringUtils.valueOf(mIsIgnoringBatteryOptimizations), StringUtils.valueOf(mIsTalkBackEnabled),
                StringUtils.valueOf(mIsPowerSaveModeEnabled),
                (locationStartFused == null) ? "" :  TravelBehaviorUtils.getDateAndTimeFromMillis(locationStartFused.getTime()),
                (locationStartFused == null) ? "" :  StringUtils.valueOf(locationStartFused.getLat()),
                (locationStartFused == null) ? "" :  StringUtils.valueOf(locationStartFused.getLon()),
                (locationStartGps == null) ? "" :  TravelBehaviorUtils.getDateAndTimeFromMillis(locationStartGps.getTime()),
                (locationStartGps == null) ? "" :  StringUtils.valueOf(locationStartGps.getLat()),
                (locationStartGps == null) ? "" :  StringUtils.valueOf(locationStartGps.getLon()),
                (locationStartNetwork == null) ? "" :  TravelBehaviorUtils.getDateAndTimeFromMillis(locationStartNetwork.getTime()),
                (locationStartNetwork == null) ? "" :  StringUtils.valueOf(locationStartNetwork.getLat()),
                (locationStartNetwork == null) ? "" :  StringUtils.valueOf(locationStartNetwork.getLon()),
                (locationEndFused == null) ? "" :  TravelBehaviorUtils.getDateAndTimeFromMillis(locationEndFused.getTime()),
                (locationEndFused == null) ? "" :  StringUtils.valueOf(locationEndFused.getLat()),
                (locationEndFused == null) ? "" :  StringUtils.valueOf(locationEndFused.getLon()),
                (locationEndGps == null) ? "" :  TravelBehaviorUtils.getDateAndTimeFromMillis(locationEndGps.getTime()),
                (locationEndGps == null) ? "" :  StringUtils.valueOf(locationEndGps.getLat()),
                (locationEndGps == null) ? "" :  StringUtils.valueOf(locationEndGps.getLon()),
                (locationEndNetwork == null) ? "" :  TravelBehaviorUtils.getDateAndTimeFromMillis(locationEndNetwork.getTime()),
                (locationEndNetwork == null) ? "" :  StringUtils.valueOf(locationEndNetwork.getLat()),
                (locationEndNetwork == null) ? "" :  StringUtils.valueOf(locationEndNetwork.getLon())};
    }
}
