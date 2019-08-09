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
import edu.usf.cutr.tba.constants.TravelBehaviorConstants;
import edu.usf.cutr.tba.model.TravelBehaviorRecord;
import edu.usf.cutr.tba.utils.TravelBehaviorUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static edu.usf.cutr.tba.constants.FirebaseConstants.*;

public class KmlFileWriter {

    BufferedWriter mKmlWriter;

    private static final String STYLE_TEXT =
            "<StyleMap id=\"msn_ylw-pushpin\">\n" +
                    "<Pair>\n" +
                    "<key>normal</key>\n" +
                    "<styleUrl>#sn_ylw-pushpin</styleUrl>\n" +
                    "</Pair>\n" +
                    "<Pair>\n" +
                    "<key>highlight</key>\n" +
                    "<styleUrl>#sh_ylw-pushpin</styleUrl>\n" +
                    "</Pair>\n" +
                    "</StyleMap>\n" +
                    "<Style id=\"sh_target\">\n" +
                    "<IconStyle>\n" +
                    "<color>ff0000ff</color>\n" +
                    "<scale>1.18182</scale>\n" +
                    "<Icon>\n" +
                    "<href>http://maps.google.com/mapfiles/kml/shapes/target.png</href>\n" +
                    "</Icon>\n" +
                    "</IconStyle>\n" +
                    "<ListStyle>\n" +
                    "</ListStyle>\n" +
                    "<LabelStyle>\n" +
                    "<scale>0</scale>\n" +
                    "</LabelStyle>\n" +
                    "</Style>\n" +
                    "<Style id=\"sh_triangle\">\n" +
                    "<IconStyle>\n" +
                    "<color>ff00aa00</color>\n" +
                    "<scale>1.18182</scale>\n" +
                    "<Icon>\n" +
                    "<href>http://maps.google.com/mapfiles/kml/shapes/triangle.png</href>\n" +
                    "</Icon>\n" +
                    "</IconStyle>\n" +
                    "<ListStyle>\n" +
                    "</ListStyle>\n" +
                    "<LabelStyle>\n" +
                    "<scale>0</scale>\n" +
                    "</LabelStyle>\n" +
                    "</Style>\n" +
                    "<Style id=\"sh_ylw-pushpin\">\n" +
                    "<IconStyle>\n" +
                    "<scale>1.2</scale>\n" +
                    "</IconStyle>\n" +
                    "<LabelStyle>\n" +
                    "<scale>0</scale>\n" +
                    "</LabelStyle>\n" +
                    "</Style>\n" +
                    "<StyleMap id=\"msn_triangle\">\n" +
                    "<Pair>\n" +
                    "<key>normal</key>\n" +
                    "<styleUrl>#sn_triangle</styleUrl>\n" +
                    "</Pair>\n" +
                    "<Pair>\n" +
                    "<key>highlight</key>\n" +
                    "<styleUrl>#sh_triangle</styleUrl>\n" +
                    "</Pair>\n" +
                    "</StyleMap>\n" +
                    "<Style id=\"sn_triangle\">\n" +
                    "<IconStyle>\n" +
                    "<color>ff00aa00</color>\n" +
                    "<Icon>\n" +
                    "<href>http://maps.google.com/mapfiles/kml/shapes/triangle.png</href>\n" +
                    "</Icon>\n" +
                    "</IconStyle>\n" +
                    "<ListStyle>\n" +
                    "</ListStyle>\n" +
                    "<LabelStyle>\n" +
                    "<scale>0</scale>\n" +
                    "</LabelStyle>\n" +
                    "</Style>\n" +
                    "<Style id=\"sn_target\">\n" +
                    "<IconStyle>\n" +
                    "<color>ff0000ff</color>\n" +
                    "<Icon>\n" +
                    "<href>http://maps.google.com/mapfiles/kml/shapes/target.png</href>\n" +
                    "</Icon>\n" +
                    "</IconStyle>\n" +
                    "<ListStyle>\n" +
                    "</ListStyle>\n" +
                    "<LabelStyle>\n" +
                    "<scale>0</scale>\n" +
                    "</LabelStyle>\n" +
                    "</Style>\n" +
                    "<StyleMap id=\"msn_target\">\n" +
                    "<Pair>\n" +
                    "<key>normal</key>\n" +
                    "<styleUrl>#sn_target</styleUrl>\n" +
                    "</Pair>\n" +
                    "<Pair>\n" +
                    "<key>highlight</key>\n" +
                    "<styleUrl>#sh_target</styleUrl>\n" +
                    "</Pair>\n" +
                    "</StyleMap>\n" +
                    "<Style id=\"sn_ylw-pushpin\">\n" +
                    "<LabelStyle>\n" +
                    "<scale>0</scale>\n" +
                    "</LabelStyle>\n" +
                    "</Style>\n" +
                    "<Style id=\"predictionPoly\">\n" +
                    "<LineStyle>\n" +
                    "<color>8000ffff</color>\n" +
                    "<width>1</width>\n" +
                    "</LineStyle>\n" +
                    "<PolyStyle>\n" +
                    "<color>4000ffff</color>\n" +
                    "</PolyStyle>\n" +
                    "</Style>\n" +
                    "<Style id=\"purpleLine\">\n" +
                    "<LineStyle>\n" +
                    "<color>7fcf0064</color>\n" +
                    "<width>8</width>\n" +
                    "</LineStyle>\n" +
                    "<PolyStyle>\n" +
                    "<color>7f00ff00</color>\n" +
                    "</PolyStyle>\n" +
                    "</Style>"; // end of styleString

    public KmlFileWriter() {
    }

    public void appendAllToKml(List<TravelBehaviorRecord> travelBehaviorRecords) {
        if (travelBehaviorRecords.isEmpty()) {
            return;
        }
        try {
            TravelBehaviorRecord firstTbr = travelBehaviorRecords.get(0);

            String fileName = firstTbr.getUserId() + "_" + TravelBehaviorUtils.getDateFromMillis(firstTbr.getActivityStartTimeMillis());
            File kmlFile = new File(fileName + TRAVEL_BEHAVIOR_KML_FILE_EXTENSION);
            mKmlWriter = new BufferedWriter(new FileWriter(kmlFile));

            mKmlWriter.write("<?xml version=\"1.0\" encoding =\"UTF-8\"?>\n");
            mKmlWriter.write("<kml xmlns=\"http://earth.google.com/kml/2.1\">\n <Document>\n");
            mKmlWriter.write("<name>" +fileName + "</name>");
            mKmlWriter.write("<description><![CDATA[" + fileName + "]]></description>");
            mKmlWriter.write(STYLE_TEXT);
            for (TravelBehaviorRecord tbr : travelBehaviorRecords) {
                appendToKml(tbr);
            }
            mKmlWriter.write("</Document>\n</kml>");
            mKmlWriter.close();
            convertKMLtoKMZ(fileName, kmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the provided KML file to a KMZ file (zipped version of KML), and deletes the KML file
     * @param fileName
     * @param kmlFile
     * @throws IOException
     */
    private void convertKMLtoKMZ(String fileName, File kmlFile) throws IOException {
        final int BUFFER = 2048;
        byte data[] = new byte[BUFFER];

        FileInputStream kmlIn = new FileInputStream(kmlFile);
        FileOutputStream kmzDestination = new FileOutputStream(fileName + TRAVEL_BEHAVIOR_KMZ_FILE_EXTENSION);
        ZipOutputStream kmzOut = new ZipOutputStream(new BufferedOutputStream(kmzDestination));

        kmzOut.putNextEntry(new ZipEntry("doc.kml")); // name of kml in zip archive

        int count;
        while ((count = kmlIn.read(data, 0, BUFFER)) > 0) {
            kmzOut.write(data, 0, count);
        }
        kmzOut.closeEntry();
        kmlIn.close();
        kmzOut.close();

        kmlFile.delete();
    }

    private void appendToKml(TravelBehaviorRecord tbr) throws IOException {
        // Write Placemark for trip origin
        mKmlWriter.write(getLocationPlacemark(tbr, true));
        mKmlWriter.write(getLocationPlacemark(tbr, false));
        mKmlWriter.write(getLinePlacemark(tbr));
    }

    /**
     * Returns a location Placemark from the provided TravelBehaviorRecord - an origin placemark if origin is true, and a destination placemark if origin is false
     * @param tbr user travel behavior for a trip
     * @param origin if true will return an origin placemark, and if false will return a destination placemark
     * @return a location Placemark from the provided TravelBehaviorRecord - an origin placemark if origin is true, and a destination placemark if origin is false
     */
    private String getLocationPlacemark(TravelBehaviorRecord tbr, boolean origin) {
        StringBuilder sb = new StringBuilder();
        String name = origin ? "Start" : "End";
        sb.append("<Placemark><name>").append(name).append(" - Trip ID ").append(tbr.getTripId()).append("</name>\n");
        sb.append("<description><![CDATA[" + "<strong>Horizontal Accuracy:</strong> ")
                .append(new DecimalFormat("#,##0.00").format(origin ? tbr.getOriginHorAccuracy() : tbr.getDestinationHorAccuracy())).append("m\n<br />")
                .append("<strong>Location Provider:</strong> ").append(origin ? tbr.getOriginProvider() : tbr.getDestinationProvider()).append("\n<br />")
                .append("<strong>").append(name).append(" Time: </strong> ").append(origin ? tbr.getActivityStartDateAndTime() : tbr.getActivityEndDateAndTime()).append("\n<br />")
                .append("]]>\n</description>")
                .append("<styleUrl>" + (origin ? "#msn_triangle" : "#msn_target") + "</styleUrl>")
                .append("<Point><coordinates><![CDATA[").append(origin ? tbr.getStartLon() : tbr.getEndLon())
                .append(",").append(origin ? tbr.getStartLat() : tbr.getEndLat()).append("]]></coordinates>\n")
                .append("</Point>")
                .append("<TimeStamp>").append("<when>").append(origin ? tbr.getActivityStartDateAndTime() : tbr.getActivityEndDateAndTime())
                .append("</when>\n</TimeStamp></Placemark>");
        return sb.toString();
    }

    /**
     * Returns a line Placemark between the origin and destination location of the provided user travel behavior trip. This is a straight line that connects the two
     * locations, not the actual travel path.
     * @param tbr user travel behavior for a trip
     * @return a line Placemark between the origin and destination location of the provided user travel behavior trip. This is a straight line that connects the two
     * locations, not the actual travel path.
     */
    private String getLinePlacemark(TravelBehaviorRecord tbr) {
        StringBuilder sb = new StringBuilder();
        // Draw a line between the two places
        sb.append("<Placemark>\n" + "<name>Trip ID " + tbr.getTripId() + " </name>\n" + "<styleUrl>#purpleLine</styleUrl>\n" + "<LineString>\n" + "<extrude>0</extrude>\n" + "<tessellate>1</tessellate>\n" + "<altitudeMode>relativeToGround</altitudeMode>\n" + "<coordinates><![CDATA[\n");
        sb.append(tbr.getStartLon()).append(",").append(tbr.getStartLat()).append(" ");
        sb.append(tbr.getEndLon()).append(",").append(tbr.getEndLat());
        sb.append("]]></coordinates></LineString></Placemark>");
        return sb.toString();
    }
}
