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

import java.util.List;

public class TravelBehaviorInfo {

    public static class TravelBehaviorActivity {
        public String detectedActivity;
        public String detectedActivityType;
        public Integer confidenceLevel;
        public Long eventElapsedRealtimeNanos;
        public Long systemClockElapsedRealtimeNanos;
        public Long systemClockCurrentTimeMillis;
        public Long numberOfNanosInThePastWhenEventHappened;
        public Long eventTimeMillis;

        public TravelBehaviorActivity() {
        }

        public TravelBehaviorActivity(String detectedActivity, String detectedActivityType) {
            this.detectedActivity = detectedActivity;
            this.detectedActivityType = detectedActivityType;
        }
    }

    public static class LocationInfo {
        public Double lat = null;

        public Double lon = null;

        public Long time = null;

        public Long elapsedRealtimeNanos = null;

        public Double altitude = null;

        public String provider = null;

        public Float accuracy = null;

        public Float bearing = null;

        public Float verticalAccuracyMeters = null;

        public Float bearingAccuracyDegrees = null;

        public Float speed = null;

        public Float speedAccuracyMetersPerSecond = null;

        public Boolean isFromMockProvider = null;

        public LocationInfo() {
        }

        public Double getLat() {
            return lat;
        }

        public Double getLon() {
            return lon;
        }

        public Long getTime() {
            return time;
        }

        public Long getElapsedRealtimeNanos() {
            return elapsedRealtimeNanos;
        }

        public Double getAltitude() {
            return altitude;
        }

        public String getProvider() {
            return provider;
        }

        public Float getAccuracy() {
            return accuracy;
        }

        public Float getBearing() {
            return bearing;
        }

        public Float getVerticalAccuracyMeters() {
            return verticalAccuracyMeters;
        }

        public Float getBearingAccuracyDegrees() {
            return bearingAccuracyDegrees;
        }

        public Float getSpeed() {
            return speed;
        }

        public Float getSpeedAccuracyMetersPerSecond() {
            return speedAccuracyMetersPerSecond;
        }

        public Boolean getFromMockProvider() {
            return isFromMockProvider;
        }
    }

    public List<TravelBehaviorActivity> activities;

    public List<LocationInfo> locationInfoList;

    public Boolean isIgnoringBatteryOptimizations;

    public TravelBehaviorInfo() {
    }

    public TravelBehaviorInfo(List<TravelBehaviorActivity> activities,
                              Boolean isIgnoringBatteryOptimizations) {
        this.activities = activities;
        this.isIgnoringBatteryOptimizations = isIgnoringBatteryOptimizations;
    }
}
