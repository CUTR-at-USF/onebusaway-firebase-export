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
package edu.usf.cutr.utils;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirebaseIOUtils {

    private static String buildDocumentPathByUid(String uid, String folder) {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append("users/").append(uid).append("/").
                append(folder);
        return pathBuilder.toString();
    }

    public static String getExistingFilePath(Class cls, String fileName) throws URISyntaxException {
        URL res = cls.getClassLoader().getResource(fileName);
        File file = Paths.get(res.toURI()).toFile();
        return file.getAbsolutePath();
    }

    public static String createFilePath(Class cls, String fileName) throws URISyntaxException {
        URL res = cls.getClassLoader().getResource("");
        return res.getPath() + fileName;
    }

    public static DocumentReference getFirebaseDocReferenceByUserIdAndRecordId(Firestore db, String userId,
                                                                               String recordId, String folder) {
        String path = buildDocumentPathByUid(userId, folder);
        return db.collection(path).document(recordId);
    }

    public static List<QueryDocumentSnapshot> getAllUserIds(Firestore db) {
        CollectionReference collectionReference = db.collection("users");
        return getQueryDocumentSnapshots(collectionReference);
    }

    public static List<QueryDocumentSnapshot> getAllRecordIdsByUserIdAndFolder(Firestore db, String userId,
                                                                               String folder) {
        CollectionReference cr = db.collection("users/" + userId + "/" + folder);
        return getQueryDocumentSnapshots(cr);
    }

    private static List<QueryDocumentSnapshot> getQueryDocumentSnapshots(CollectionReference cr) {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = cr.get();
        try {
            return querySnapshotApiFuture.get().getDocuments();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
