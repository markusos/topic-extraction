/*
 * ElasticIndex
 * 
 * ElasticSearch implementation of the data Index
 * Used to index Store data from MongoDB to ElasticSearch
 */

package com.findwise.topic.indexer;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

import com.findwise.topic.api.Document;

import static org.elasticsearch.node.NodeBuilder.*;

public class ElasticIndex implements Index {
	Node node;
	Client client;
	BulkRequestBuilder bulkRequest;

	public ElasticIndex() {
		node = nodeBuilder().client(true).node();
		client = node.client();
	}

	public void initBulkIndex() {
		System.out.println("New Bulk index");
		bulkRequest = client.prepareBulk();
	}

	public void addToBulkIndex(Document document) {
		IndexRequestBuilder request = client.prepareIndex("wiki", "article")
				.setSource(document.toMap());
		bulkRequest.add(request);
	}

	public void bulkIndex() {
		System.out.println("Index");
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures())
			System.err.println("Error in bulk index");
	}

	public void index(Document document) {
		client.prepareIndex("wiki", "article").setSource(document.toMap())
				.execute().actionGet();
	}

	public void close() {
		client.close();
	}

}
