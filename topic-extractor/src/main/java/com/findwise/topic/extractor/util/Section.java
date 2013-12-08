/*
 * Section
 * 
 * Data structure for Input section of text and list of expected topics
 * 
 */

package com.findwise.topic.extractor.util;

import java.util.ArrayList;
import java.util.List;


public class Section {

	private List<String> topics;
	private String text;
	private Result result;

	public Section(String text, ArrayList<String> topics) {
		this.topics = topics;
		this.text = text;
	}

	public Section(String text) {
		this.topics = new ArrayList<String>();
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public List<String> getTopics() {
		return topics;
	}
	
	public void setResult(Result result){
		this.result = result;
	}
	
	public Result getResult(){
		return result;
	}

	public String toString() {
		String ret = "Topics: ";
		for (String t : topics) {
			ret += t + "; ";
		}
		ret += " Text: " + text;
		return ret;

	}

}
