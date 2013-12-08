/*
 * Result data structure
 * 
 * Used to store the candidate graph and the ranked results
 * 
 */

package com.findwise.topic.extractor.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.findwise.topic.api.Document;
import com.findwise.topic.extractor.resultscorer.ResultScorer;

public class Result {

	private HashMap<String, ResultNode> graph = new HashMap<String, ResultNode>();
	private String text;

	public Result(String text) {
		this.text = text;
	}

	public void add(Document document, float score) {
		createNodesFromDocument(document, score);
	}

	public String toString() {
		StringBuilder resultString = new StringBuilder();
		resultString.append("Section: " + text + ",\n Results: [");
		for (Entry<String, ResultNode> g : graph.entrySet()) {
			resultString.append(g.getValue().getTitle() + " : ");
		}
		resultString.append("]");

		return resultString.toString();
	}
	
	public String getText(){
		return text;
	}

	public void filterUsedNodes(int minLinks, int minBacklinks) {
		for (Entry<String, ResultNode> g : graph.entrySet()) {
			if (g.getValue().getLinks().size() >= minLinks
					|| g.getValue().getBacklinks().size() >= minBacklinks)
				g.getValue().setUsed(true);
			else
				g.getValue().setUsed(false);
		}
	}

	public void calculateScore(ResultScorer scorer) {
		scorer.calculateScore(this);
	}
	
	public void resetScore() {
		for (Entry<String, ResultNode> g : graph.entrySet()) {
			g.getValue().setScore(0);
		}
	}

	private void createNodesFromDocument(Document document, float score) {
		ResultNode titleNode = getNode(document.getTitle());
		titleNode.updateSearchScore(score);
		addLinksToNode(titleNode, document.getLinks());
	}

	private ResultNode getNode(String title) {
		if (!graph.containsKey(title)) {
			ResultNode node = new ResultNode(title);
			graph.put(title, node);
			return node;
		} else
			return graph.get(title);
	}

	private void addLinksToNode(ResultNode node, Set<String> links) {
		ResultNode linkNode;
		for (String link : links) {
			linkNode = getNode(link);
			node.addLink(linkNode);
			linkNode.addBacklink(node);
		}
	}

	public Map<String, ResultNode> getGraph() {
		return graph;
	}

	public Map<String, ResultNode> getUsedGraph() {
		Map<String, ResultNode> usedGraph = new HashMap<String, ResultNode>();

		for (Entry<String, ResultNode> entry : graph.entrySet()) {
			if (entry.getValue().isUsed())
				usedGraph.put(entry.getKey(), entry.getValue());
		}

		return usedGraph;
	}

	public List<ResultNode> getResultList(int precision) {
		ArrayList<ResultNode> result = new ArrayList<ResultNode>();
		for (String key : graph.keySet()) {
			ResultNode g = graph.get(key);
			if (g.isUsed() && g.getScore() > 0) {
				result.add(g);
			}

		}
		Collections.sort(result);

		precision = Math.min(precision, result.size());
		result = new ArrayList<ResultNode>(result.subList(0, precision));
		return result;
	}

	public void buildDotFile(String fileName) {

		try {
			File file = new File(fileName + ".dot");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF8"));

			String init = "digraph G {\n" + "overlap=false;\n"
					+ "bgcolor=transparent;\n" + "splines=true;\n"
					+ "rankdir=TB;\n";

			String close = "}";

			out.write(init);

			for (String key : getUsedGraph().keySet()) {
				ResultNode g = getUsedGraph().get(key);
				for (ResultNode link : g.getLinks()) {
					if (link.isUsed())
						out.write("\"" + g.getTitle() + "\" -> \""
								+ link.getTitle() + "\"\n");
				}
			}
			out.write(close);
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
