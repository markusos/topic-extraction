/*
 * Document
 * 
 * Defines the structure used to store documents 
 */

package com.findwise.topic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Document {

	private String title;
	private int backlinkCount;
	private Set<String> links;
	private int linkCount;
	private Set<String> redirects;
	private int redirectCount;
	private Set<String> tokens;
	private int tokenCount;

	public Document() {
		this.title = "";
		this.linkCount = 0;
		this.redirectCount = 0;
		this.tokenCount = 0;
		this.backlinkCount = 0;
		this.links = new HashSet<String>();
		this.redirects = new HashSet<String>();
		this.tokens = new HashSet<String>();
	}

	@SuppressWarnings({ "unchecked" })
	public Document(Map<String, Object> document) {

		try {
			this.title = (String) document.get("title");
			this.linkCount = (Integer) document.get("linkCount");
			this.redirectCount = (Integer) document.get("redirectCount");
			this.tokenCount = (Integer) document.get("tokenCount");
			this.backlinkCount = (Integer) document.get("backLinkCount");
			;
			this.links = new HashSet<String>(
					(ArrayList<String>) document.get("links"));
			this.redirects = new HashSet<String>(
					(ArrayList<String>) document.get("redirects"));
			this.tokens = new HashSet<String>(
					(ArrayList<String>) document.get("tokens"));
		} catch (Exception e) {
			System.err.println("Error in document structure: "
					+ document.toString());
			e.printStackTrace();
		}
	}

	public Map<String, Object> toMap() {

		Map<String, Object> document;
		document = new HashMap<String, Object>();
		document.put("title", title);
		document.put("backLinkCount", backlinkCount);
		document.put("links", links);
		document.put("linkCount", linkCount);
		document.put("redirects", redirects);
		document.put("redirectCount", redirectCount);
		document.put("tokens", tokens);
		document.put("tokenCount", tokenCount);

		return document;
	}

	public String toString() {
		StringBuilder documentString = new StringBuilder();

		documentString.append("title: " + getTitle() + ", ");
		documentString.append("backLinkCount: " + getBackLinkCount() + ", ");
		documentString.append("linkCount: " + getLinkCount() + ", ");
		documentString.append("links: " + getLinks().toString() + ", ");
		documentString.append("redirectCount: " + getRedirectCount() + ", ");
		documentString.append("redirects: " + getRedirects().toString() + ", ");
		documentString.append("tokenCount: " + getTokenCount() + ", ");
		documentString.append("tokens: " + getTokens().toString());

		return documentString.toString();
	}

	public void merge(Document doc) throws Exception {
		if (getTitle().equals(doc.getTitle())) {
			addLinks(doc.getLinks());
			addRedirects(doc.getRedirects());
			setBackLinkCount(doc.getBackLinkCount());
			addTokens(doc.getTokens());
		} else
			throw new Exception(
					"Trying to merge documents with different title");
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBackLinkCount(int count) {
		this.backlinkCount = count;
	}

	public int getBackLinkCount() {
		return backlinkCount;
	}

	public Set<String> getTokens() {
		return this.tokens;
	}

	public boolean containsToken(String token) {
		return getTokens().contains(token);
	}

	public int getTokenCount() {
		return this.tokenCount;
	}

	public void addToken(String token) {
		if (!this.tokens.contains(token)) {
			this.tokens.add(token);
			tokenCount++;
		}
	}

	public void addTokens(Set<String> tokens) {
		int count = 0;
		for (String token : tokens) {
			if (!this.tokens.contains(token)) {
				this.tokens.add(token);
				count++;
			}
		}
		tokenCount += count;
	}

	public Set<String> getLinks() {
		return this.links;
	}

	public boolean containsLink(String link) {
		return getLinks().contains(link);
	}

	public int getLinkCount() {
		return this.linkCount;
	}

	public void addLink(String link) {
		if (!this.links.contains(link)) {
			this.links.add(link);
			linkCount++;
		}
	}

	public void addLinks(Set<String> links) {
		int count = 0;
		for (String link : links) {
			if (!this.links.contains(link)) {
				this.links.add(link);
				count++;
			}
		}
		linkCount += count;
	}

	public Set<String> getRedirects() {
		return this.redirects;
	}

	public boolean containsRedirects(String redirect) {
		return getRedirects().contains(redirect);
	}

	public int getRedirectCount() {
		return this.redirectCount;
	}

	public void addRedirect(String redirect) {
		if (!this.redirects.contains(redirect)) {
			this.redirects.add(redirect);
			redirectCount++;
		}
	}

	public void addRedirects(Set<String> redirects) {
		int count = 0;
		for (String redirect : redirects) {
			if (!this.redirects.contains(redirect)) {
				this.redirects.add(redirect);
				count++;
			}
		}
		redirectCount += count;
	}

}
