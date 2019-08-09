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
package edu.usf.cutr.tba.constants;

import java.util.concurrent.TimeUnit;

public class TravelBehaviorConstants {

    public static final String ACTIVITY_TRANSITION_ENTER = "ENTER";

    public static final String ACTIVITY_TRANSITION_EXIT = "EXIT";

    public static final String ACTIVITY_STILL = "STILL";

    public static final String ACTIVITY_WALKING = "WALKING";

    public static final String ACTIVITY_RUNNING = "RUNNING";

    public static final String ACTIVITY_WALKING_AND_RUNNING = "WALKING/RUNNING";

    public static final long  STILL_ACTIVITY_THRESHOLD = TimeUnit.MINUTES.toMillis(2);

    public static final long  WALKING_RUNNING_THRESHOLD = TimeUnit.MINUTES.toMillis(2);

    /**
     * We split days at this number of hours past midnight (e.g., if 3 hours, then it will be split at 3am)
     */
    public static final long SAME_DAY_TIME_DIFF = 3; // 3 hours
}
