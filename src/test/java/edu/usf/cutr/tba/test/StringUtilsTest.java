package edu.usf.cutr.tba.test;

import edu.usf.cutr.tba.utils.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests utilities for Strings functionalities
 */
public class StringUtilsTest {


    /**
     * Given a list of  QueryDocumentSnapshot and a endActivityTime,
     * verify the behavior of GetClosestDeviceInfo
     */
    @Test
    public void testValidateStringDateJava8() {
        String strDate1 = "02-29-2021";
        String strDate2 = "02-29-2020";
        String strDate3 = "02152021";
        String strDate4 = "02-18-2021";
        String strDate5 = "21-05-2020";
        String strDate6 = "May-05-2020";
        String strDate7 = "Hello World";
        String strDate8 = "123456788";

        // Verify for a invalid leap year
        assertFalse(StringUtils.validateStringDateJava8(strDate1));

        // Verify valid leap year
        assertTrue(StringUtils.validateStringDateJava8(strDate2));

        //Verify invalid format
        assertFalse(StringUtils.validateStringDateJava8(strDate3));

        //Verify valid date
        assertTrue(StringUtils.validateStringDateJava8(strDate4));

        //Verify valid date, invalid format
        assertFalse(StringUtils.validateStringDateJava8(strDate5));
        assertFalse(StringUtils.validateStringDateJava8(strDate6));

        //Verify invalid strings
        assertFalse(StringUtils.validateStringDateJava8(strDate7));
        assertFalse(StringUtils.validateStringDateJava8(strDate8));

    }
}
