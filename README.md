# onebusaway-firebase-export [![Java CI with Maven](https://github.com/CUTR-at-USF/travel-behavior-analysis/actions/workflows/maven.yml/badge.svg)](https://github.com/CUTR-at-USF/travel-behavior-analysis/actions/workflows/maven.yml)
Java application to export travel behavior data collected by OneBusAway and stored in Firebase Firestore into comma-separate value (CSV) and KML/KMZ files.

A single CSV file contains trips in an origin/destination format where each line is a trip taken by a user. Multiple KML/KMZ files are output, one for each user-day of travel behavior.

For more details about the output formats see [this final report](https://digitalcommons.usf.edu/cutr_nctr/13/).

## Build 
To build the application use `mvn clean package` command. This command will create a jar file 
(i.e., `travel-behavior-analysis-1.0-SNAPSHOT.jar`) under the `target` folder.
 
## Setup Firebase Account
1. Generate a admin private-key json file (e.g., `admin-key.json`) for your service account.
To generate the key file follow the instructions in Firebase [setup page](https://firebase.google.com/docs/admin/setup#initialize_the_sdk).

## Run
To run the application use `java -jar` command and pass the `admin-key.json` file as an argument:
`java -jar travel-behavior-analysis-1.0-SNAPSHOT.jar -keyFile /path/to/file/fileName.json`

## Additional Optional Command Line Arguments 
* `-userId <userId>` Takes a user id as an argument and performs the analysis for that specific user. Example usage:
`-userId abcdef`.
* `-dayStart <hour>` The analysis day start time. For example `-dayStart 3` makes the analysis time 
window between 3AM to 3AM. By default, the analysis time is 3AM to 3AM.
* `-stillMergeThreshold <minutes>` Still event merge threshold in minutes. By default it is 2 minutes. Example usage:
`-stillMergeThreshold 1` makes the merge threshold 1 minute.
* `-walkingRunningMergeThreshold <minutes>` Walking and running events merge threshold in minutes. 
By default it is 2 minutes. Example usage: `-walkingRunningMergeThreshold 1` makes the merge threshold 1 minute.
* `-noMergeWalkingRunning` Does not merge the walking and running events. This option does not take a parameter.
* `-noMergeStill` Does not merge the still events.  This option does not take a parameter.
* `-startDate <mm-dd-yyyy> -endDate <mm-dd-yyyy>` Takes two dates as argument and performs the analysis for activities in
  the specific date range. The filter will assume the dates in the America/New_York timezone. Example usage:
  -startDate 05-16-2020 -endDate 03-30-2021 will perform the analysis from May 16, 2020 00:00:00, to March 30, 2021 00:00:00.
* `-outputDir <outputFolderPathAndName>` Takes a string with the desired output folder path and use it to save the
analysis output files. If the folder does not exist, the application will try to create it. Example usage:
-outputDir myOutputData will look for the myOutputData folder in the current working directory. If myOutputData folder 
  does not exist, it will be created.
* `-skipKMZ` Does not export the output data in KMZ format. This option does not take a parameter.
* `-multiUserId <pathToCSVFile>` Takes a string as argument as the path to a CSV file containing a list of userIds, 
  one per row. Performs the analysis for each user in list of users. Example usage: `-multiUserId "listOfUsers.csv"`.

## License

```
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
 ```
