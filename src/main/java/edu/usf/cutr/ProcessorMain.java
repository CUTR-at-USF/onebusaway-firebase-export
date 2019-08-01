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
package edu.usf.cutr;


import edu.usf.cutr.exception.FirebaseFileNotInitializedException;
import edu.usf.cutr.manager.TravelBehaviorDataAnalysisManager;
import edu.usf.cutr.options.ProgramOptions;
import org.apache.commons.cli.*;

public class ProcessorMain {
    public static void main(String[] args) {
        Options options = createCommandLineOptions();
        ProgramOptions programOptions = ProgramOptions.getInstance();

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption(ProgramOptions.USER_ID)) {
                programOptions.setUserId(cmd.getOptionValue(ProgramOptions.USER_ID));
            }

            if (cmd.hasOption(ProgramOptions.KEY_FILE)) {
                programOptions.setKeyFilePath(cmd.getOptionValue(ProgramOptions.KEY_FILE));
            }

            if (cmd.hasOption(ProgramOptions.NO_MERGE_STILL)) {
                programOptions.setMergeStillEventsEnabled(false);
            }

            if (cmd.hasOption(ProgramOptions.NO_MERGE_WALKING_RUNNING)) {
                programOptions.setMergeAllWalkingAndRunningEventsEnabled(false);
            }
        } catch (ParseException e) {
            System.err.println("Invalid command line options");
        }

        try {
            new TravelBehaviorDataAnalysisManager().processData();
        } catch (FirebaseFileNotInitializedException e) {
            System.err.println("Firebase file is not initialized properly.");
        }
    }

    private static Options createCommandLineOptions() {
        Options options = new Options();
        options.addOption(ProgramOptions.USER_ID, true, "Only run the analysis for specific user");
        options.addOption(ProgramOptions.KEY_FILE, true, "Admin key file of the Firebase account");
        options.addOption(ProgramOptions.NO_MERGE_STILL, false, "Do not merge still events");
        options.addOption(ProgramOptions.NO_MERGE_WALKING_RUNNING, false, "Do not merge waling and running events");
        return options;
    }
}
