package edu.usf.cutr.tba.test;

import edu.usf.cutr.tba.utils.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests utilities for Strings functionalities
 */
public class StringUtilsTest {

    /**
     * Given a series of strings, verify the behavior of validateStringDateAndParseToMillis
     * to identify if each of the strings represent valid dates.
     */
    @Test
    public void testValidateStringDateAndParseToMillis() {
        String strDate1 = "02-29-2021";
        String strDate2 = "02-29-2020"; // 1582952400000L America/New York
        String strDate3 = "02152021";
        String strDate4 = "02-18-2021"; // 1613624400000L America/New York
        String strDate5 = "21-05-2020";
        String strDate6 = "May-05-2020";
        String strDate7 = "Hello World";
        String strDate8 = "123456788";

        // Verify for a invalid leap year
        assertEquals(0, StringUtils.validateStringDateAndParseToMillis(strDate1));

        // Verify valid leap year
        assertEquals(1582952400000L, StringUtils.validateStringDateAndParseToMillis(strDate2));

        //Verify invalid format
        assertEquals(0, StringUtils.validateStringDateAndParseToMillis(strDate3));

        //Verify valid date
        assertEquals(1613624400000L, StringUtils.validateStringDateAndParseToMillis(strDate4));

        //Verify valid date, invalid format
        assertEquals(0, StringUtils.validateStringDateAndParseToMillis(strDate5));
        assertEquals(0, StringUtils.validateStringDateAndParseToMillis(strDate6));

        //Verify invalid strings
        assertEquals(0, StringUtils.validateStringDateAndParseToMillis(strDate7));
        assertEquals(0, StringUtils.validateStringDateAndParseToMillis(strDate8));

    }

    /**
     * This folder will be deleted after tests are run,
     * even in the event of failures or exceptions.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * Given different strings representing existent, non existent and not possible paths,
     * verifies the behavior of validateAndParseOutputPath
     */
    @Test
    public void testValidateAndParseOutputPath() {
        try{
            // Create a subfolder inside the temporary folder
            File createdFolder= folder.newFolder("subfolder");
            String existentFolder = createdFolder.getPath().toString();

            // Create a path for a non existent folder.
            Path newFolderPath = Paths.get(createdFolder.getParent(), "newFolder");

            // test for an existent folder
            assertEquals(existentFolder, StringUtils.validateAndParseOutputPath(existentFolder));

            // test for a non existent folder, the new folder must be created.
            assertEquals(newFolderPath.toString(), StringUtils.validateAndParseOutputPath(newFolderPath.toString()));
            assertTrue(Files.exists(newFolderPath));

            // Test for an invalid path
            String invalidPath = createdFolder.getParent() + "/\0/";
            assertEquals("", StringUtils.validateAndParseOutputPath(invalidPath));

        } catch (Exception e){
            System.out.println("Exception while testing testValidateAndParseOutputPath: " + e);
        }
    }

}
