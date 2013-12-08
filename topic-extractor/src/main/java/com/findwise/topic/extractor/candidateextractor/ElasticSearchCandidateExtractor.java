/*
 * ElasticSearch candidate extractor
 * 
 * Uses ElasticSearch to extract candidate topics from the text

 */

package com.findwise.topic.extractor.candidateextractor;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import com.findwise.topic.api.Document;
import com.findwise.topic.api.Tokenizer;
import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.SearchMethod;
import com.findwise.topic.extractor.util.QueryType;

import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

public class ElasticSearchCandidateExtractor implements CandidateExtractor {

	Node node;
	Client client;
	Tokenizer tokenizer;
	int nrofSearchResults;
	SearchMethod searchMethod;
	QueryType queryType;
	
	Result result;
	
	public ElasticSearchCandidateExtractor(int nrofSearchResults, SearchMethod searchMethod, QueryType queryType) {
		this.nrofSearchResults = nrofSearchResults;
		this.searchMethod = searchMethod;
		this.queryType = queryType;
		
		node = nodeBuilder().client(true).node();
		client = node.client();
		tokenizer = new Tokenizer(node, client);
	}
	
	public Result getCandidates(String section) {
		
		result = new Result(section);
		
		if(searchMethod == SearchMethod.MULTI)
			getCandidatesMultiSearch(section, result);
		else{
			getSentenceCandidates(section, result);
		}
		return result;
	}

	private Result getCandidatesMultiSearch(String section, Result result) {
		String[] sentences = section.split("\\.");
		for (String s : sentences)
			getSentenceCandidates(s, result);

		return result;
	}

	private Result getSentenceCandidates(String sentence, Result result) {
		BoolQueryBuilder query;
		if(queryType == QueryType.TOKEN)
			query = tokenQuery(sentence);
		else
			query = sectionQuery(sentence);

		QueryBuilder scoreQuery = QueryBuilders.customScoreQuery(query).script("_score"); 

		SearchResponse response = client.prepareSearch("wiki")
				.setTypes("article")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(scoreQuery).setFrom(0).setSize(nrofSearchResults)
				.execute().actionGet();

		Document hitDocument;
		for (SearchHit hit : response.getHits()) {
			hitDocument = new Document(hit.getSource());
			result.add(hitDocument, hit.score());
		}
		
		return result;
	}

	private BoolQueryBuilder tokenQuery(String section) {
		Set<String> tokens = tokenizer.getTokens(section);
		BoolQueryBuilder query = QueryBuilders.boolQuery();

		for (String s : tokens) {
			query = query
					.should(QueryBuilders.matchQuery("links", s).boost(
							(float) (1.0)))
					.should(QueryBuilders.matchQuery("title", s).boost(
							(float) 0.1))
					.should(QueryBuilders.matchQuery("redirects", s).boost(
							(float) 0.1));
		}

		return query;
	}

	private BoolQueryBuilder sectionQuery(String section) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();

		query = query
				.should(QueryBuilders.matchQuery("links", section).boost(
						(float) 1.0))
				.should(QueryBuilders.matchQuery("title", section).boost(
						(float) 0.1))
				.should(QueryBuilders.matchQuery("redirects", section).boost(
						(float) 0.1));

		return query;
	}
}
