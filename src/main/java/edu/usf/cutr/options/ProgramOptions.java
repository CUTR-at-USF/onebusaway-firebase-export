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
package edu.usf.cutr.options;

public class ProgramOptions {

    public static final String KEY_FILE = "keyFile";

    public static final String NO_MERGE_STILL = "noMergeStill";

    public static final String NO_MERGE_WALKING_RUNNING = "noMergeWalkingRunning";

    public static final String USER_ID = "userId";

    private boolean mIsMergeStillEventsEnabled = true;

    private boolean mIsMergeAllWalkingAndRunningEventsEnabled = true;

    private String mUserId;

    private String mKeyFilePath;

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
}
