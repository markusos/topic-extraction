/*
 * Enum QueryType
 * 
 * Used to define QueryType to the ElasticSearchCandidateExtractor
 */

package com.findwise.topic.extractor.util;

public enum QueryType {
	TOKEN,
	SECTION;
	
	@Override
	  public String toString() {
	    switch(this) {
	      case TOKEN: return "TOKEN";
	      case SECTION: return "SECTION";
	      default: throw new IllegalArgumentException();
	    }
	  }
}
