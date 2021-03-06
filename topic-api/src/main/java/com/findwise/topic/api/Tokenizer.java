/*
 * Tokenizer class, uses ElasticSearch Analyzer to split text into tokens
 */

package com.findwise.topic.api;

import java.util.HashSet;
import java.util.Set;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

public class Tokenizer {

    Client client;

    public Tokenizer(Client client) {
        this.client = client;
    }

    public Set<String> getTokens(String input) {

        Set<String> tokens = new HashSet<String>();

        AnalyzeResponse analyzeResponse = client.admin().indices()
                .prepareAnalyze(input).execute().actionGet();

        for (AnalyzeToken t : analyzeResponse.getTokens()) {
            tokens.add(t.getTerm());
        }
        return tokens;
    }
}
