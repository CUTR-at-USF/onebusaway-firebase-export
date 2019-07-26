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
package edu.usf.cutr.io;

import com.opencsv.CSVWriter;
import edu.usf.cutr.constants.FirebaseConstants;
import edu.usf.cutr.model.TravelBehaviorRecord;
import edu.usf.cutr.utils.FirebaseIOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class CSVFileWriter {

    private  CSVWriter mCSVWriter;

    public CSVFileWriter() {
        try {
            String filePath = FirebaseIOUtils.createFilePath(getClass(), FirebaseConstants.TRAVEL_BEHAVIOR_CSV_FILE);
            File file = new File(filePath);
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVFileWriter object filewriter object as parameter
            mCSVWriter = new CSVWriter(outputfile);
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void createHeader(String[] header) {
        mCSVWriter.writeNext(header);
    }

    public void appendAllToCsV(List<TravelBehaviorRecord> travelBehaviorRecords) {
        for (TravelBehaviorRecord tbr: travelBehaviorRecords) {
            appendToCsV(tbr);
        }
    }

    private void appendToCsV(TravelBehaviorRecord travelBehaviorRecord) {
        mCSVWriter.writeNext(travelBehaviorRecord.toStringArray());
    }

    public void closeWriter() {
        try {
            mCSVWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
