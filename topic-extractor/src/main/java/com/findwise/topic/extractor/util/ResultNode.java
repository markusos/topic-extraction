/*
 * ResultNode
 * 
 * Represents a node in the candidate graph
 */

package com.findwise.topic.extractor.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.findwise.topic.api.BasicTokenizer;

public class ResultNode implements Comparable<Object> {

	private boolean use;
	private float score;
	private float searchScore;
	private String title;
	private Set<ResultNode> links;
	private Set<ResultNode> backlinks;

	public ResultNode(String title) {
		links = new HashSet<ResultNode>();
		backlinks = new HashSet<ResultNode>();
		this.title = title;
		this.score = 0;
		this.searchScore = 0;
		this.use = false;
	}

	public String toString() {
		return title;
	}

	public String getTitle() {
		return title;
	}

	public boolean isUsed() {
		return use;
	}

	public void setUsed(boolean use) {
		this.use = use;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public Set<ResultNode> getEdges() {
		Set<ResultNode> edges = new HashSet<ResultNode>();
		edges.addAll(links);
		edges.addAll(backlinks);
		return edges;
	}

	public Set<ResultNode> getLinks() {
		return links;
	}

	public Set<ResultNode> getBacklinks() {
		return backlinks;
	}

	public void addLink(ResultNode node) {
		links.add(node);
	}

	public void addBacklink(ResultNode node) {
		backlinks.add(node);
	}

	public void setSearchScore(float score) {
		searchScore = score;
	}

	public void updateSearchScore(float score) {
		searchScore += score; // TODO normalize ?
	}

	public float getSearchScore() {
		return searchScore;
	}

	public int compareTo(Object otherResultNode) throws ClassCastException {
		if (!(otherResultNode instanceof ResultNode))
			throw new ClassCastException("A ResultNode object expected.");
		float otherResultNodeScore = ((ResultNode) otherResultNode).score;

		if (this.score - otherResultNodeScore == 0)
			return 0;
		else if (this.score - otherResultNodeScore > 0)
			return -1;
		else
			return 1;
	}

	public List<ResultNode> getUsedLinks() {
		ArrayList<ResultNode> usedLinks = new ArrayList<ResultNode>();
		for (ResultNode link : links)
			if (link.use)
				usedLinks.add(link);

		return usedLinks;
	}

	public List<ResultNode> getUsedBackLinks() {
		ArrayList<ResultNode> usedBackLinks = new ArrayList<ResultNode>();
		for (ResultNode link : backlinks)
			if (link.use)
				usedBackLinks.add(link);

		return usedBackLinks;
	}

	public Set<ResultNode> getUsedEdges() {
		Set<ResultNode> edges = new HashSet<ResultNode>();
		edges.addAll(getUsedLinks());
		edges.addAll(getUsedBackLinks());
		return edges;
	}
	
	public Set<String> getTokens(){
		Set<String> tokens = new HashSet<String>();
		BasicTokenizer tokenizer = new BasicTokenizer();
		for(ResultNode n : getLinks()){
			tokens.addAll(tokenizer.getTokens(n.getTitle()));
		}
		
		//System.out.println(tokens);
		return tokens;
	}

	public int countUsedLinks() {

		int count = 0;

		for (ResultNode link : links)
			if (link.use)
				count++;

		return count;
	}

	public int countUsedBackLinks() {

		int count = 0;

		for (ResultNode link : backlinks)
			if (link.use)
				count++;

		return count;
	}
}
