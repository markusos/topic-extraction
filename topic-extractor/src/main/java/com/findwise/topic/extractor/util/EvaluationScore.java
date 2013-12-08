/*
 * Data structure to store EvaluationScore 
 * 
 * Used only for evaluation
 */

package com.findwise.topic.extractor.util;

public class EvaluationScore {

	private float recall;
	private float score;
	
	public EvaluationScore(float recall, float score){
		this.recall = recall;
		this.score = score;
	}
	
	public float getRecall(){
		return recall;
	}
	
	public float getScore(){
		return score;
	}
	
}
