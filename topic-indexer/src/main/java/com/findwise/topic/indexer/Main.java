/*
 * Main class for topic indexer
 * 
 * Builds ElasticSearch index and MongoDB structure from XML file of DBpedia/Wikipedia link structure
 */

package com.findwise.topic.indexer;

import java.io.*;

public class Main {

	static DataIndexer indexer;
	
	//File to read data from
	static String fileName = "page_links_en.nt";

	public static void run() {
		try {
            indexer = new DataIndexer();
			logToFile("logg.txt" ,"error.txt");

			System.out.println("Start Import");
			importData();
			System.out.println("Import Done");

			System.out.println("Start Merge");
			mergeData();
			System.out.println("Merge Done!");

			System.out.println("Start fix backlinks");
			backlinks();
			System.out.println("Fix backlinks Done!");

			System.out.println("Start tokenize");
			tokenize();
			System.out.println("Tokenize Done!");

			System.out.println("Start index");
			index();
			System.out.println("index Done!");

			System.out.println("All Done!");

			indexer.close();

		}
        catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void importData() throws Exception {
		
		DataImporter WikiLinkImporter = new DataImporter();
		WikiLinkImporter.importFile(fileName);
	}

	private static void mergeData() throws Exception {
		indexer.mergeRedirects();
	}

	private static void backlinks() throws Exception {
		indexer.updateDocumentsWithBacklinkCount();
	}

	private static void tokenize() throws Exception {
		indexer.tokenize();
	}

	private static void index() throws Exception {
		indexer.indexData();
	}

	private static void logToFile(String log, String error) {
		try {
			File errorFile = new File(error);
			File logFile = new File(log);
			FileOutputStream fosLog;
			FileOutputStream fosError;
			fosLog = new FileOutputStream(logFile);
			fosError = new FileOutputStream(errorFile);
			PrintStream psLog = new PrintStream(fosLog);
			PrintStream psError = new PrintStream(fosError);
			System.setErr(psError);
			System.setOut(psLog);
		} catch (FileNotFoundException e) {
			System.err.println("Could not open log file!");
		}
	}

}
