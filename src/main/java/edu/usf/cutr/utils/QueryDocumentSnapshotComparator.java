package edu.usf.cutr.utils;

import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.Comparator;

public class QueryDocumentSnapshotComparator implements Comparator<QueryDocumentSnapshot> {

    @Override
    public int compare(QueryDocumentSnapshot o1, QueryDocumentSnapshot o2) {
        try {
            int i1 = Integer.valueOf(o1.getReference().getId().split("-")[0]);
            int i2 = Integer.valueOf(o2.getReference().getId().split("-")[0]);
            return i1 - i2;
        } catch (Exception e) {
            return 0;
        }
    }
}
