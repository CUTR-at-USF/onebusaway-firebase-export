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
package edu.usf.cutr.tba.io;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import edu.usf.cutr.tba.constants.FirebaseConstants;
import edu.usf.cutr.tba.exception.FirebaseFileNotInitializedException;
import edu.usf.cutr.tba.model.TravelBehaviorInfo;
import edu.usf.cutr.tba.options.ProgramOptions;
import edu.usf.cutr.tba.utils.FirebaseIOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirebaseReader {

    private Firestore mFirestoreDB;

    public FirebaseReader() throws FirebaseFileNotInitializedException{
        initFirebase();
    }

    private void initFirebase() throws FirebaseFileNotInitializedException {
        FileInputStream serviceAccount = null;
        try {
            String filePath = ProgramOptions.getInstance().getKeyFilePath();
            if (filePath == null) {
                throw new FirebaseFileNotInitializedException();
            }
            serviceAccount = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirestoreOptions options = null;
        try {
            options = FirestoreOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setTimestampsInSnapshotsEnabled(true)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mFirestoreDB = options.getService();
    }

    public List<QueryDocumentSnapshot> getAllUserIds() {
        return FirebaseIOUtils.getAllUserIds(mFirestoreDB);
    }

    public List<QueryDocumentSnapshot> getAllUserInfoById(String userId) {
        return FirebaseIOUtils.getAllRecordIdsByUserIdAndFolder(mFirestoreDB, userId,
                FirebaseConstants.FIREBASE_ACTIVITY_TRANSITION_FOLDER);
    }

    public List<QueryDocumentSnapshot> getAllUserDeviceInfoById(String userId) {
        return FirebaseIOUtils.getAllRecordIdsByUserIdAndFolder(mFirestoreDB, userId,
                FirebaseConstants.FIREBASE_DEVICE_INFO_FOLDER);
    }

    private void processTransitionData(String recordId, String userId) {
        DocumentReference docRef = FirebaseIOUtils.getFirebaseDocReferenceByUserIdAndRecordId(mFirestoreDB, userId, recordId,
                FirebaseConstants.FIREBASE_ACTIVITY_TRANSITION_FOLDER);
        try {
            DocumentSnapshot documentSnapshot = docRef.get().get();
            TravelBehaviorInfo tbi = documentSnapshot.toObject(TravelBehaviorInfo.class);
            assert tbi != null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
