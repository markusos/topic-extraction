/*
 * PageRankResultScorer
 * 
 * Uses PageRank to score candidate graphs
 */

package com.findwise.topic.extractor.resultscorer;

import java.util.Map;
import java.util.Map.Entry;

import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.ResultNode;

public class PageRankResultScorer implements ResultScorer {

	public void calculateScore(Result result) {
		calculatePageRank(result);
	}
	
	public String toString(){
		return "PageRankResultScorer";
	}

	private void calculatePageRank(Result result) {
		Map<String, ResultNode> graph = result.getGraph();

		for (Entry<String, ResultNode> g : graph.entrySet()) {
			ResultNode currentNode = g.getValue();
			currentNode.setScore(1);
		}

		float d = 0.85f;

		float maxChange = 1;
		while (maxChange > 0.1) {
			maxChange = 0;
			for (Entry<String, ResultNode> g : graph.entrySet()) {

				ResultNode currentNode = g.getValue();
				if (currentNode.isUsed()) {
					float currentScore = currentNode.getScore();
					float linkNodeScoreSum = 0;

					for (ResultNode n : currentNode.getUsedBackLinks()) {
						linkNodeScoreSum += n.getScore() / n.getUsedLinks().size();
					}

					float score = (1 - d) + d * linkNodeScoreSum;
					currentNode.setScore(score);

					float change = Math.abs(score - currentScore);
					// System.out.println("PageRank: " + g.getValue().title +
					// " : " + score + " : " + change);
					if (change > maxChange)
						maxChange = change;
				}
			}
		}
	}
}
