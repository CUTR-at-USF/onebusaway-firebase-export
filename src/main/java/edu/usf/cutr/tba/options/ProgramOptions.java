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
package edu.usf.cutr.tba.options;

public class ProgramOptions {

    public static final String KEY_FILE = "keyFile";

    public static final String NO_MERGE_STILL = "noMergeStill";

    public static final String NO_MERGE_WALKING_RUNNING = "noMergeWalkingRunning";

    public static final String USER_ID = "userId";

    public static final String SAME_DAY_START_POINT = "dayStart";

    // In minutes
    public static final String WALKING_RUNNING_EVENT_MERGE_THRESHOLD = "walkingRunningMergeThreshold";

    // In minutes
    public static final String STILL_EVENT_MERGE_THRESHOLD = "stillMergeThreshold";

    // Start date option to perform a document search
    public static final String START_DATE = "startDate";
    // End date option to perform a document search
    public static final String END_DATE = "endDate";

    // Time zone used to parse the date parameters
    public  static final String TIME_ZONE = "America/New_York";

    // Custom directory to save output files on
    public static final String SAVE_ON_PATH = "outputDir";

    // Option to skip KMZ export
    public static final String SKIP_KMZ = "skipKMZ";

    // Path to file with multiple IDs
    public static final String MULTI_USERS_PATH = "multiUserId";

    // When to show the number of processed user records (interval)
    public static final int mShowProgressInterval = 1000;

    private boolean mIsMergeStillEventsEnabled = true;

    private boolean mIsMergeAllWalkingAndRunningEventsEnabled = true;

    private String mUserId;

    private String mMultiUserId;

    private String mKeyFilePath;

    private Integer mSameDayStartPoint;

    private Integer mStillEventMergeThreshold;

    private Integer mWalkingRunningEventMergeThreshold;

    private long mStartDate = 0;

    private long mEndDate = 0;

    private String mOutputDir = "";

    private boolean mSkipKmz = false;

    private static ProgramOptions sProgramOptions = null;

    private ProgramOptions() {

    }

    public static ProgramOptions getInstance() {
        if (sProgramOptions == null) {
            sProgramOptions = new ProgramOptions();
        }
        return sProgramOptions;
    }

    public boolean isMergeStillEventsEnabled() {
        return mIsMergeStillEventsEnabled;
    }

    public ProgramOptions setMergeStillEventsEnabled(boolean mergeStillEventsEnabled) {
        mIsMergeStillEventsEnabled = mergeStillEventsEnabled;
        return this;
    }

    public boolean isMergeAllWalkingAndRunningEventsEnabled() {
        return mIsMergeAllWalkingAndRunningEventsEnabled;
    }

    public ProgramOptions setMergeAllWalkingAndRunningEventsEnabled(boolean mergeAllWalkingAndRunningEventsEnabled) {
        mIsMergeAllWalkingAndRunningEventsEnabled = mergeAllWalkingAndRunningEventsEnabled;
        return this;
    }

    public String getUserId() {
        return mUserId;
    }

    public ProgramOptions setUserId(String userId) {
        mUserId = userId;
        return this;
    }

    public String getKeyFilePath() {
        return mKeyFilePath;
    }

    public ProgramOptions setKeyFilePath(String keyFilePath) {
        mKeyFilePath = keyFilePath;
        return this;
    }

    public Integer getSameDayStartPoint() {
        return mSameDayStartPoint;
    }

    public ProgramOptions setSameDayStartPoint(Integer sameDayStartPoint) {
        mSameDayStartPoint = sameDayStartPoint;
        return this;
    }

    public Integer getStillEventMergeThreshold() {
        return mStillEventMergeThreshold;
    }

    public ProgramOptions setStillEventMergeThreshold(Integer stillEventMergeThreshold) {
        mStillEventMergeThreshold = stillEventMergeThreshold;
        return this;
    }

    public Integer getWalkingRunningEventMergeThreshold() {
        return mWalkingRunningEventMergeThreshold;
    }

    public ProgramOptions setWalkingRunningEventMergeThreshold(Integer walkingRunningEventMergeThreshold) {
        mWalkingRunningEventMergeThreshold = walkingRunningEventMergeThreshold;
        return this;
    }

    public  long getStartDate() { return mStartDate; }

    public  ProgramOptions setStartDate(long startDate) {
        this.mStartDate = startDate;
        return this;
    }

    public  long getEndDate() { return mEndDate; }

    public  ProgramOptions setEndDate(long endDate) {
        this.mEndDate = endDate;
        return this;
    }

    public String getOutputDir() { return mOutputDir; }

    public ProgramOptions setOutputDir(String outputDir) {
        this.mOutputDir = outputDir;
        return this;
    }

    public boolean skipKmz() { return mSkipKmz; }

    public ProgramOptions setSkipKmz(boolean skipKmz) {
        this.mSkipKmz = skipKmz;
        return this;
    }

    public String getMultiUserId() { return mMultiUserId; }

    public ProgramOptions setMultiUserId( String multiUserId) {
        this.mMultiUserId = multiUserId;
        return this;
    }

}
