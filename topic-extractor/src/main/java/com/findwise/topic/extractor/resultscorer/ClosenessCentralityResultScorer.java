/*
 * ClosenessCentralityResultScorer
 * 
 * Uses Closeness Centrality to score candidate graphs
 */

package com.findwise.topic.extractor.resultscorer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.ResultNode;

public class ClosenessCentralityResultScorer implements ResultScorer {


	public void calculateScore(Result result) {
		calculateClosenessCentrality(result);
	}


	public String toString(){
		return "ClosenessCentralityResultScorer";
	}
	
	private void calculateClosenessCentrality(Result result) {

		Map<String, ResultNode> graph = result.getUsedGraph();

		for (Entry<String, ResultNode> g : graph.entrySet()) {

			float closeness = 0;
			ResultNode node = g.getValue();
			int distance = 0;

			Map<String, Integer> distances = distances(node);

			for (Entry<String, ResultNode> h : graph.entrySet()) {
				ResultNode to = h.getValue();
				if (distances.containsKey(to.getTitle())) {
					distance = distances.get(to.getTitle());
					if (distance > 0) {
						closeness += 1.0 / distance;
					}
				}
			}
			node.setScore(1 + closeness);
		}
	}

	private Map<String, Integer> distances(ResultNode start) {
		Map<String, Integer> visited = new HashMap<String, Integer>();
		int distance = 0;
		Queue<ResultNode> queue = new LinkedList<ResultNode>();
		queue.add(start);
		visited.put(start.getTitle(), distance);

		ResultNode current;
		while (!queue.isEmpty()) {
			current = queue.poll();
			distance = visited.get(current.getTitle()) + 1;
			for (ResultNode r : current.getUsedEdges()) {
				if (!visited.containsKey(r.getTitle())) {
					visited.put(r.getTitle(), distance);
					queue.add(r);
				}
			}
		}
		return visited;
	}

}
