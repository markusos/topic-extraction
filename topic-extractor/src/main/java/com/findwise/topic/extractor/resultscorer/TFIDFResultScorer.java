/*
 * TFIDFResultScorer
 * 
 * Uses ElasticSearch TFIDF score to score candidate graphs
 */

package com.findwise.topic.extractor.resultscorer;

import java.util.Map;
import java.util.Map.Entry;

import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.ResultNode;

public class TFIDFResultScorer implements ResultScorer {

	public void calculateScore(Result result) {
		calculateTFIDFScore(result);
	}
	
	public String toString(){
		return "TFIDFResultScorer";
	}

	private void calculateTFIDFScore(Result result) {

		Map<String, ResultNode> graph = result.getGraph();

		for (Entry<String, ResultNode> g : graph.entrySet()) {
			ResultNode node = g.getValue();
			node.setScore(node.getSearchScore());
		}
	}
}
