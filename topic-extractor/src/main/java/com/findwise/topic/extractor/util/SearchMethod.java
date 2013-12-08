/*
 * Enum SearchMethod
 * 
 * Used to define SearchMethod to the ElasticSearchCandidateExtractor
 */

package com.findwise.topic.extractor.util;

public enum SearchMethod {
	SINGLE, 
	MULTI;
	
	@Override
	  public String toString() {
	    switch(this) {
	      case SINGLE: return "SINGLE";
	      case MULTI: return "MULTI";
	      default: throw new IllegalArgumentException();
	    }
	  }
}
