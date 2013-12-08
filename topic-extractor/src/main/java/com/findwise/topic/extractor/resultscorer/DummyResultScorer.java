/*
 * DummyResultScorer
 * 
 * Sets the score to 1, used for evaluation and testing purposes
 */

package com.findwise.topic.extractor.resultscorer;

import java.util.Map;
import java.util.Map.Entry;

import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.ResultNode;

public class DummyResultScorer implements ResultScorer{

	public void calculateScore(Result result) {
		Map<String, ResultNode> graph = result.getGraph();

		for (Entry<String, ResultNode> g : graph.entrySet()) {
			ResultNode node = g.getValue();
			node.setScore(1);
		}
		
	}
	
	public String toString(){
		return "DummyResultScorer";
	}
}
