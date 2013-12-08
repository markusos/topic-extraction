/*
 * ResultScorer Interface
 * 
 * Defines methods for ResultScorers
 */

package com.findwise.topic.extractor.resultscorer;

import com.findwise.topic.extractor.util.Result;

public interface ResultScorer {

	public void calculateScore(Result result);

	public String toString();
}
