/*
 * DataImporter
 * 
 * Import and process XML file to data Store.
 */

package com.findwise.topic.indexer;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.Timestamp;

import com.findwise.topic.indexer.link.Link;
import com.findwise.topic.indexer.link.LinkParser;

public class DataImporter {
    static final boolean DEBUG = false;
    static int lineCounter = 0;
    Link currentLink = null;

    public void importFile(String fileName) throws Exception {
        try {
            readAndSaveFile(fileName);

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("File read error");
            e.printStackTrace();
        }
    }

    public void readAndSaveFile(String fileName) throws Exception {
        int size = 158373972; // Nr of lines in page_links_en
        int printInterval = 100000;

        InputStream fileInputStream;
        BufferedReader readBuffer;
        LinkParser linkParser = new LinkParser();

        String InputLine;
        DocumentBuilder documentBuilder = new DocumentBuilder();
        fileInputStream = new FileInputStream(fileName);
        readBuffer = new BufferedReader(new InputStreamReader(fileInputStream,
                Charset.forName("UTF-8")));
        while ((InputLine = readBuffer.readLine()) != null) {
            lineCounter++;
            currentLink = linkParser.parseLineToLink(InputLine);
            if (currentLink != null) {
                documentBuilder.saveLink(currentLink);
            }
            printProcessingProgressWhithTimestamp(lineCounter, size,
                    "Import progress", printInterval);
        }
        documentBuilder.commit();
        documentBuilder.commitToDb();
        readBuffer.close();
    }

    public void printProcessingProgressWhithTimestamp(int current, int total,
                                                      String info, int printInterval) {
        if (current % printInterval == 0) {
            java.util.Date date = new java.util.Date();
            System.out.println(info + ": "
                    + ((double) current / (double) total) * 100 + "%" + " ("
                    + new Timestamp(date.getTime()) + ")");
        }
    }
}
