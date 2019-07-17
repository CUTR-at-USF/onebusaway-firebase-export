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

    private String mUserId;
    private String mTripId;
    private String mGoogleActivity;
    private Float mGoogleConfidence;
    private String mVehicleType;
    private String mStartDate;
    private String mStartTime;
    private Double mStartLat;
    private Double mStartLon;
    private String mEndDate;
    private String mEndTime;
    private Double mEndLat;
    private Double mEndLon;
    private Long mActivityDuration;
    private Integer mChainId;
    private Integer mChainIndex;
    private Integer mTourId;
    private Integer mTourIndex;

    // Internal user
    private Long startTimeMillis;
    private Long endTimeMillis;

    public TravelBehaviorRecord(String userId) {
        mUserId = userId;
    }

    public String getUserId() {
        return mUserId;
    }

    public TravelBehaviorRecord setUserId(String userId) {
        mUserId = userId;
        return this;
    }

    public String getTripId() {
        return mTripId;
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

    public Float getGoogleConfidence() {
        return mGoogleConfidence;
    }

    public TravelBehaviorRecord setGoogleConfidence(Float googleConfidence) {
        mGoogleConfidence = googleConfidence;
        return this;
    }

    public String getVehicleType() {
        return mVehicleType;
    }

    public TravelBehaviorRecord setVehicleType(String vehicleType) {
        mVehicleType = vehicleType;
        return this;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public TravelBehaviorRecord setStartDate(String startDate) {
        mStartDate = startDate;
        return this;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public TravelBehaviorRecord setStartTime(String startTime) {
        mStartTime = startTime;
        return this;
    }

    public Double getStartLat() {
        return mStartLat;
    }

    public TravelBehaviorRecord setStartLat(Double startLat) {
        mStartLat = startLat;
        return this;
    }

    public Double getStartLon() {
        return mStartLon;
    }

    public TravelBehaviorRecord setStartLon(Double startLon) {
        mStartLon = startLon;
        return this;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public TravelBehaviorRecord setEndDate(String endDate) {
        mEndDate = endDate;
        return this;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public TravelBehaviorRecord setEndTime(String endTime) {
        mEndTime = endTime;
        return this;
    }

    public Double getEndLat() {
        return mEndLat;
    }

    public TravelBehaviorRecord setEndLat(Double endLat) {
        mEndLat = endLat;
        return this;
    }

    public Double getEndLon() {
        return mEndLon;
    }

    public TravelBehaviorRecord setEndLon(Double endLon) {
        mEndLon = endLon;
        return this;
    }

    public Long getActivityDuration() {
        return mActivityDuration;
    }

    public TravelBehaviorRecord setActivityDuration(Long activityDuration) {
        mActivityDuration = activityDuration;
        return this;
    }

    public Integer getChainId() {
        return mChainId;
    }

    public TravelBehaviorRecord setChainId(Integer chainId) {
        mChainId = chainId;
        return this;
    }

    public Integer getChainIndex() {
        return mChainIndex;
    }

    public TravelBehaviorRecord setChainIndex(Integer chainIndex) {
        mChainIndex = chainIndex;
        return this;
    }

    public Integer getTourId() {
        return mTourId;
    }

    public TravelBehaviorRecord setTourId(Integer tourId) {
        mTourId = tourId;
        return this;
    }

    public Integer getTourIndex() {
        return mTourIndex;
    }

    public TravelBehaviorRecord setTourIndex(Integer tourIndex) {
        mTourIndex = tourIndex;
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

    public String[] toStringArray() {
        return new String[]{mUserId, mTripId, mGoogleActivity, String.valueOf(mGoogleConfidence), mVehicleType,
                mStartDate, mStartTime, String.valueOf(mStartLat), String.valueOf(mStartLon), mEndDate, mEndTime,
                String.valueOf(mEndLat), String.valueOf(mEndLon), String.valueOf(mActivityDuration),
                String.valueOf(mChainId), String.valueOf(mChainIndex), String.valueOf(mTourId), String.valueOf(mTourId)};
    }
}
