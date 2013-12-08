/*
 * BasicTokenizer class, used to split text into tokens
 */

package com.findwise.topic.api;

import java.util.HashSet;
import java.util.Set;

public class BasicTokenizer {

	public BasicTokenizer() {

	}

	public Set<String> getTokens(String input) {
		Set<String> tokens = new HashSet<String>();
		for (String t : tokenize(input))
			tokens.add(t);
		return tokens;
	}
	
	private String[] tokenize(String s) {
		return s.toLowerCase().split("[\\-\\+\\.\\^:,\\(\\)\\# ]");
	}
}
