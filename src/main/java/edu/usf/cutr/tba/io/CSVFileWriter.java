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

import com.opencsv.CSVWriter;
import edu.usf.cutr.tba.constants.FirebaseConstants;
import edu.usf.cutr.tba.model.TravelBehaviorRecord;
import edu.usf.cutr.tba.options.ProgramOptions;
import edu.usf.cutr.tba.utils.TravelBehaviorUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVFileWriter {

    private CSVWriter mCSVWriter;
    private ProgramOptions mProgramOptions;

    public CSVFileWriter() {
        mProgramOptions = ProgramOptions.getInstance();
        try {
            Path localPath;
            localPath = Paths.get(mProgramOptions.getOutputDir(), FirebaseConstants.TRAVEL_BEHAVIOR_CSV_FILE);
            File file = new File(localPath.toString());
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file);

            // create CSVFileWriter object filewriter object as parameter
            mCSVWriter = new CSVWriter(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createHeader(String[] header) {
        try {
            mCSVWriter.writeNext(header);
        } catch(Exception e) {
            System.out.println("Create header error: " + e);
        }
    }

    public void appendAllToCsV(List<TravelBehaviorRecord> travelBehaviorRecords) {
        for (TravelBehaviorRecord tbr : travelBehaviorRecords) {
            if (TravelBehaviorUtils.isAllowedToExport(tbr)) {
                appendToCsV(tbr);
            }
        }
    }

    private void appendToCsV(TravelBehaviorRecord travelBehaviorRecord) {
        try  {
            mCSVWriter.writeNext(travelBehaviorRecord.toStringArray());
        } catch (Exception e) {
            System.out.println("Append to CSV Exception: " + e);
        }
    }

    public void closeWriter() {
        try {
            mCSVWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
