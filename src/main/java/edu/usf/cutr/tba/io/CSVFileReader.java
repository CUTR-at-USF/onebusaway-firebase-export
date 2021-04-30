package edu.usf.cutr.tba.io;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.*;
import java.util.List;

public class CSVFileReader {

    public List<String[]> readUserList(String csvFilePath) {
        // create parserSettings to be used in RowProcessor
        CsvParserSettings parserSettings = new CsvParserSettings();

        // automatically detect what line separator sequence is in the input
        parserSettings.setLineSeparatorDetectionEnabled(true);

        // rowProcessor will store each parsed row in a List.
        RowListProcessor rowProcessor = new RowListProcessor();

        // configure the parser to use the rowProcessor to process the values of each parsed row.
        parserSettings.setProcessor(rowProcessor);

        // Do not consider the first parsed row as the headers of each column in the file.
        parserSettings.setHeaderExtractionEnabled(false);

        // creates a parser instance with the given settings
        CsvParser parser = new CsvParser(parserSettings);

        try {
            // the 'parse' method will parse the file and delegate each parsed row to the RowProcessor you defined
            parser.parse(getReader(csvFilePath));

            // get the parsed records from the RowListProcessor.
            List<String[]> rows = rowProcessor.getRows();
            return  rows;
        } catch (FileNotFoundException e) {
            System.err.println("Csv file not found.");
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unable to read csv file.");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Exception while trying to parse csv file.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a reader for a resource in the relative path
     * @param filePath path of the file to be read using a streamReader
     * @return a reader of the resource
     */
    public static Reader getReader(String filePath)
            throws FileNotFoundException, UnsupportedEncodingException {
        return new InputStreamReader(new FileInputStream(filePath), "UTF-8");
    }
}
