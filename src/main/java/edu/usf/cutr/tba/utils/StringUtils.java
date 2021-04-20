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
import edu.usf.cutr.tba.options.ProgramOptions;
import org.apache.commons.lang3.math.NumberUtils;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class StringUtils {
    public static String valueOf(Object o) {
        if (o == null) return "";
        return String.valueOf(o);
    }

    /**
     * Gets the date in string format to validate is a date in format mm-dd-yyyy
     * @param dateStr string with the date to be validated
     * @return Valid date in millis if the dateStr is a valid date in format mm-dd-yyyy, 0 otherwise
     */
    public static long validateStringDateAndParseToMillis(String dateStr) {
        // uuuu is required in order to use ResolverStyle.STRICT
        String dateFormat = "MM-dd-uuuu";
        long validDateMillis;
        // Define formatter for "mm-dd-yyyy"
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat)
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate validDate = LocalDate.parse(dateStr, dateFormatter);
            validDateMillis = validDate.atStartOfDay(ZoneId.of(ProgramOptions.TIME_ZONE)).toInstant().toEpochMilli();
        } catch (DateTimeParseException e) {
            validDateMillis = 0;
        }
        return validDateMillis;
    }

    /**
     * Validate if newPath is a valid path. If newPath exists then return it. Otherwise try
     * to create it and return it. Return "" if it is not possible to create the newPath.
     * @param newPath path of directory to be verified.
     * @return String of a valid path or "" if the path is invalid or if its not possible to
     * be created.
     */
    public static String validateAndParseOutputPath(String newPath) {
        try {
            Path localPath = Paths.get(newPath);
            if (Files.exists(localPath)) {
                return localPath.toString();
            } else {
                // Folder does not exists, try to create it
                System.out.println("The provided path does not exist. " +
                        "Trying to create the folder: '" + localPath.toString() + "' ...");
                try {
                    Files.createDirectories(localPath);
                    return localPath.toString();
                } catch (Exception e ) {
                    System.out.println("It was not possible to create the directory: " + localPath.toString());
                    System.err.println("Error:" + e);
                    return "";
                }
            }
        } catch (InvalidPathException e) {
            System.err.println("Invalid command line option. SAVE_ON_PATH is not a valid path." + e);
            return "";
        }
    }

    /**
     * If stringTimestamp is parsable to Long then return its long value, it it is not parsable, the function will find
     * the best timeStamp option between a prefix value of the provided string or the MIN_VALUE of the Long class.
     * @param timeStamp string containing a possible time stamp
     * @return Long time stamp
     */
    public static long timeStampToLong(String timeStamp) {
        if (!NumberUtils.isParsable(timeStamp)) {
            // The timestamp must be a random string with a numeric prefix, get the prefix
            int indexDash = timeStamp.indexOf("-");
            if (indexDash == -1) {
                // Dash not found, return MIN_VALUE
                return Long.MIN_VALUE;
            }
            timeStamp = timeStamp.substring(0 , indexDash);
            // if prefix is not Parsable, return MIN_VALUE
            if (!NumberUtils.isParsable(timeStamp)) {
                return Long.MIN_VALUE;
            }
        }
        // timeStamp is parsable
        return Long.parseLong(timeStamp);
    }

    /**
     * If the provided timeStamp is parsable return the same string. If it is not parsable, the function will find the
     * best timeStamp option between a prefix value of the provided string or the MIN_VALUE of the Long class.
     * @param timeStamp string containing a possible time stamp
     * @return string with a parsable time stamp
     */
    public static String parsableTimeStamp(String timeStamp) {
        if (!NumberUtils.isParsable(timeStamp)) {
            // The timestamp must be a random string with a numeric prefix, get the prefix
            int indexDash = timeStamp.indexOf("-");
            if (indexDash == -1) {
                // Dash not found, return MIN_VALUE
                return Long.toString(Long.MIN_VALUE);
            }
            timeStamp = timeStamp.substring(0 , indexDash);
            // if prefix is not Parsable, return MIN_VALUE
            if (!NumberUtils.isParsable(timeStamp)) {
                return Long.toString(Long.MIN_VALUE);
            }
        }
        return timeStamp;
    }
}
