/*
 * DegreeCentralityResultScorer
 * 
 * Uses Degree Centrality to score candidate graphs
 */

package com.findwise.topic.extractor.resultscorer;

import java.util.Map;
import java.util.Map.Entry;

import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.ResultNode;

public class DegreeCentralityResultScorer implements ResultScorer {


	public void calculateScore(Result result) {
		calculateDegreeCentrality(result);
	}
	

	public String toString(){
		return "DegreeCentralityResultScorer";
	}

	private void calculateDegreeCentrality(Result result) {

		Map<String, ResultNode> graph = result.getGraph();

		for (Entry<String, ResultNode> g : graph.entrySet()) {
			ResultNode node = g.getValue();
			node.setScore(node.countUsedBackLinks() + node.countUsedLinks());
		}
	}

}
