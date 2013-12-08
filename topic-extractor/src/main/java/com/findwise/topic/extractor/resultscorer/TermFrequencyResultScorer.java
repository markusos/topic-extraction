/*
 * TermFrequencyResultScorer
 * 
 * Uses only Term Frequency to score topic candidates
 */

package com.findwise.topic.extractor.resultscorer;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.findwise.topic.api.BasicTokenizer;
import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.ResultNode;

public class TermFrequencyResultScorer implements ResultScorer {

	public void calculateScore(Result result) {
		calculateTermFrequencyScore(result);
	}
	
	public String toString(){
		return "TermFrequencyResultScorer";
	}

	private void calculateTermFrequencyScore(Result result) {

		Map<String, ResultNode> graph = result.getGraph();
		Set<String> textSet = buildSet(result.getText());
		
		for (Entry<String, ResultNode> g : graph.entrySet()) {
			ResultNode currentNode = g.getValue();
			Set<String> nodeSet = currentNode.getTokens();
			
			float tokenCount = 0;

			for(String token : textSet){
				if(nodeSet.contains(token)){
					tokenCount++;
				}
			}
			float score = 0;
			score = tokenCount / (nodeSet.size()+1);
			
			currentNode.setScore(score);
		}
	}
	
	private Set<String> buildSet(String text){
		Set<String> set = new HashSet<String>();
		BasicTokenizer tokenizer = new BasicTokenizer();
		
		for(String s : tokenizer.getTokens(text))
			set.add(s);
		
		return set;
	}
	
}
