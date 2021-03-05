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
import java.time.LocalDate;
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
     * @param dateStr
     * @return true if the dateStr is a valid date in format mm-dd-yyyy
     */
    public static boolean validateStringDateJava8(String dateStr)
    {
        String dateFormat = "MM-dd-uuuu";
        boolean valid;
        // Define formatter for "mm-dd-yyyy"
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat)
                .withResolverStyle(ResolverStyle.STRICT);

        try {
            LocalDate.parse(dateStr, dateFormatter);
            valid = true;
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }
}
