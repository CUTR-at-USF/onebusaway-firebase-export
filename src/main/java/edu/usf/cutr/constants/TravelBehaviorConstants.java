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
package edu.usf.cutr.constants;

import java.util.concurrent.TimeUnit;

public class TravelBehaviorConstants {

    public static final String ACTIVITY_TRANSITION_ENTER = "ENTER";

    public static final String ACTIVITY_TRANSITION_EXIT = "EXIT";

    public static final String ACTIVITY_STILL = "STILL";

    public static final long  STILL_ACTIVITY_THRESHOLD = TimeUnit.MINUTES.toMillis(5);
}
