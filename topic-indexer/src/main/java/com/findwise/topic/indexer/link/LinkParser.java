/*
 * LinkParser
 * 
 * Parse links from the XML file
 * 
 */
package com.findwise.topic.indexer.link;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkParser {

	public Link parseLineToLink(String line)
			throws UnsupportedEncodingException {
		String from = "";
		String to = "";
		Pattern pattern = Pattern
				.compile("<http://dbpedia.org/resource/(.*?)>");
		Matcher matcher = pattern.matcher(line);

		from = decodeWikiResource(parseWikiResource(matcher));
		to = decodeWikiResource(parseWikiResource(matcher));

		Link foundLink = new Link(from, to);
		if (foundLink.isCorrect())
			return foundLink;
		else
			return null;
	}

	private String parseWikiResource(Matcher matcher) {
		String MatchedResource = "";
		if (matcher.find()) {
			MatchedResource = matcher.group(1);
		}
		return MatchedResource;
	}

	private String decodeWikiResource(String resource)
			throws UnsupportedEncodingException {
		String URLDecodedResource = java.net.URLDecoder.decode(resource,
				"UTF-8");
		URLDecodedResource = URLDecodedResource.replace('_', ' ');
		return URLDecodedResource;
	}
}
