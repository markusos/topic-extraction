/*
 * Main class for Topic Extraction
 */

package com.findwise.topic.extractor;

import com.findwise.topic.extractor.candidateextractor.CandidateExtractor;
import com.findwise.topic.extractor.candidateextractor.ElasticSearchCandidateExtractor;
import com.findwise.topic.extractor.resultscorer.ClosenessCentralityResultScorer;
import com.findwise.topic.extractor.resultscorer.ResultScorer;
import com.findwise.topic.extractor.util.QueryType;
import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.SearchMethod;
import com.findwise.topic.extractor.util.Section;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main {
	
	static ResultScorer scorer =  new ClosenessCentralityResultScorer(); //
	static CandidateExtractor extractor = 
			new ElasticSearchCandidateExtractor(20, SearchMethod.SINGLE, QueryType.SECTION); 
			// new DBpediaSpotlightCandidateExtractor();
	
	// Precision
	static int nrOfResults = 5;
	
	// Filter graph
	static int minBacklinks = Integer.MAX_VALUE;
	static int minLinks = 1;
	
	// Input file
	static String input = "test_input.txt";
	static Map<String, Set<String>> topics;
	
	public static void run() {
		topics = new HashMap<String, Set<String>>();
		extraxtTopics(input);
	}
	
	private static void extraxtTopics(String file) {
		TextImporter textImporter = new TextImporter();

		ArrayList<Section> sections;
		ArrayList<Result> results;
		try {
			sections = textImporter.readFile(file);
			System.out.println("Start Search");
			results = extractTopicCandidates(sections);

			System.out.println("Ranking results...");
			buildResultPresentation(results);

			System.out.println("Done!");
			openResult();

		} catch (IOException e) {
			System.err.println("Input file not found?");
			e.printStackTrace();
		}
	}

	private static ArrayList<Result> extractTopicCandidates(
			List<Section> sections) {

		ArrayList<Result> results = new ArrayList<Result>();
		for (Section s : sections) {
			System.out.println("Extracting topic candidates from section: "
					+ s.toString());
			results.add(extractor.getCandidates(s.getText()));
		}
		return results;
	}

	private static void buildResultPresentation(List<Result> results) {
		PresentationBuilder presentation = new PresentationBuilder("result", nrOfResults);
		for (Result r : results) {
			r.filterUsedNodes(minLinks, minBacklinks);
			r.calculateScore(scorer);
			presentation.addResult(r);
		}
		presentation.close();
	}

	private static void openResult() throws IOException {
		// Open result.html
		String htmlFilePath = "result.html";
		File htmlFile = new File(htmlFilePath);
		Desktop.getDesktop().open(htmlFile);
	}
}
