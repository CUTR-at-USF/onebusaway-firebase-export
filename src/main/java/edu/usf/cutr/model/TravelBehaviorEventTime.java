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

public class TravelBehaviorEventTime {

    public enum TimeType {LOCATION ("Location"), TRANSITION_EVENT("Transition Event");
        private String mName;

        TimeType(String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }

    private Long mEventTime;

    private TimeType mTimeType;

    public TravelBehaviorEventTime(Long eventTime, TimeType timeType) {
        mEventTime = eventTime;
        mTimeType = timeType;
    }

    public Long getEventTime() {
        return mEventTime;
    }

    public TimeType getTimeType() {
        return mTimeType;
    }
}
