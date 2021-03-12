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
     * @param dateStr string with the date to ve validated
     * @return Valid date in millis if the dateStr is a valid date in format mm-dd-yyyy, 0 otherwise
     */
    public static long validateStringDateAndParseToMillis(String dateStr)
    {
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
}
