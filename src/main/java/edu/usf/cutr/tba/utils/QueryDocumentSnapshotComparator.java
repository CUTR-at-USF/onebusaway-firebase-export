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
package edu.usf.cutr.tba.utils;

import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.Comparator;

public class QueryDocumentSnapshotComparator implements Comparator<QueryDocumentSnapshot> {

    @Override
    public int compare(QueryDocumentSnapshot o1, QueryDocumentSnapshot o2) {
        long t1 = TravelBehaviorUtils.getComparableTime(o1);
        long t2 = TravelBehaviorUtils.getComparableTime(o2);

        return Long.compare(t1, t2);
    }
}
