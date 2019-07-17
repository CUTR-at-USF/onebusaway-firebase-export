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
