package edu.usf.cutr.tba.test;

import edu.usf.cutr.tba.io.CSVFileReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CSVFileReaderTest {
    private CSVFileReader mCSVFileReader;
    /**
     * This folder will be deleted after tests are run,
     * even in the event of failures or exceptions.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testReadUserList() {
        mCSVFileReader = new CSVFileReader();

        try {
            // Create a subfolder inside the temporary folder
            File createdFolder= folder.newFolder("subfolder");

            // Create a temporary csv file.
            final File tempCsvFile = folder.newFile("tempCSV.csv");

            // Write empty row to the csv file
            Files.write(tempCsvFile.toPath(), "\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

            // Test existent csv file with empty content. Length of Arraylist must be 0
            assertEquals(0, mCSVFileReader.readUserList(tempCsvFile.getPath()).size());

            // Test one row of data without comma at the end
            Files.write(tempCsvFile.toPath(), "XYZYjgC7yRANDOMbwh9N81pY4Bstring2\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            // size must be 1 and test the expected value
            assertEquals(1, mCSVFileReader.readUserList(tempCsvFile.getPath()).size());
            assertEquals("XYZYjgC7yRANDOMbwh9N81pY4Bstring2", mCSVFileReader.readUserList(tempCsvFile.getPath()).get(0)[0]);

            // test file with more than one row of data and more than one column
            Files.write(tempCsvFile.toPath(), "XYZYjgC7yRANDOMbwh9N81pY4Bstring3, extra string\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            // size must be 2 and test the expected values for different columns
            assertEquals(2, mCSVFileReader.readUserList(tempCsvFile.getPath()).size());
            assertEquals("XYZYjgC7yRANDOMbwh9N81pY4Bstring2", mCSVFileReader.readUserList(tempCsvFile.getPath()).get(0)[0]);
            assertEquals("XYZYjgC7yRANDOMbwh9N81pY4Bstring3", mCSVFileReader.readUserList(tempCsvFile.getPath()).get(1)[0]);
            assertEquals("extra string", mCSVFileReader.readUserList(tempCsvFile.getPath()).get(1)[1]);

            // test file with empty row at the end
            Files.write(tempCsvFile.toPath(), "\n".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            assertEquals(2, mCSVFileReader.readUserList(tempCsvFile.getPath()).size());
            assertEquals("XYZYjgC7yRANDOMbwh9N81pY4Bstring2", mCSVFileReader.readUserList(tempCsvFile.getPath()).get(0)[0]);
            assertEquals("XYZYjgC7yRANDOMbwh9N81pY4Bstring3", mCSVFileReader.readUserList(tempCsvFile.getPath()).get(1)[0]);
            assertEquals("extra string", mCSVFileReader.readUserList(tempCsvFile.getPath()).get(1)[1]);

            // Test non existent file
            String invalidFile = Paths.get(createdFolder.getParent(), "nonExistentCsvFile.csv").toString();
            assertNull(mCSVFileReader.readUserList(invalidFile));

        } catch (Exception e) {
            System.err.println("Error while testing readUserList() method.\n" + e);
        }
    }

}