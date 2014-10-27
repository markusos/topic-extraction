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
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.findwise.topic.api.Document;

public class ElasticIndex implements Index {
    Client client;
    BulkRequestBuilder bulkRequest;

    public ElasticIndex() {
        client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
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
        IndexResponse response = client.prepareIndex("wiki", "article").setSource(document.toMap())
                .execute().actionGet();
        if (!response.isCreated())
            System.err.println("Error when indexing document " + document);
    }

    public void close() {
        client.close();
    }

}
