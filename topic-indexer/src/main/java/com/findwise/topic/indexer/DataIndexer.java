/*
 * DataIndexer
 * 
 * Handles the merging and indexing of Documents from data store to index.
 */

package com.findwise.topic.indexer;

import com.findwise.topic.api.BasicTokenizer;
import com.findwise.topic.api.Document;
import com.findwise.topic.api.MongoStore;
import com.findwise.topic.api.Store;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataIndexer {

    Store store;
    Index index;

    public DataIndexer() throws IOException {
        this.store = new MongoStore();
        this.index = new ElasticIndex();
    }

    public DataIndexer(String storeName) throws IOException {
        this.store = new MongoStore(storeName);
        this.index = new ElasticIndex();
    }

    public DataIndexer(Store store) throws IOException {
        this.store = store;
        this.index = new ElasticIndex();
    }

    public void close() {
        try {
            index.close();
            store.close();
        } catch (IOException e) {
            System.err.println("Could not close DataIndexer");
            e.printStackTrace();
        }
    }

    public void indexData() throws IOException {
        int batchIndexSize = 1000;
        int count = 0;
        store.setupCursor();
        int size = store.cursorSize();
        System.out.println("Indexing " + size + " documents...");
        index.initBulkIndex();
        try {
            while (store.cursorHasNext()) {
                Document document = store.getNextDocument();
                index.addToBulkIndex(document);
                count++;

                if (count % batchIndexSize == 0) {
                    index.bulkIndex();
                    index.initBulkIndex();
                }
            }
        } finally {
            index.bulkIndex();
            store.closeCursor();
            System.out.println(count);
        }
    }

    public void mergeRedirects() throws Exception {
        int count = 0;
        System.out.println("Start Merge...");
        store.setupCursor(1);
        int size = store.cursorSize();
        System.out.println("Merging " + size + " documents...");
        try {
            while (store.cursorHasNext()) {

                Document document = store.getNextDocument();
                Set<String> links = document.getLinks();
                String title = document.getTitle();
                if (links.size() == 1) {
                    Document mainDocument = store.getDocument(links.iterator()
                            .next());
                    if (mainDocument != null) {
                        mainDocument.addRedirect(title);
                        store.update(mainDocument);
                    }
                    store.delete(title);
                }
                count++;
                printProcessingProgressWhitTimestamp(count, size,
                        "Merge progress", 1000);
            }
        } finally {
            store.closeCursor();
            System.out.println(count);
        }
    }

    public void updateDocumentsWithBacklinkCount() throws Exception {
        HashMap<String, Integer> backlinks = countBacklinks();
        saveBacklinksCount(backlinks);
    }

    private HashMap<String, Integer> countBacklinks() throws IOException {
        int count = 0;
        store.setupCursor();
        int size = store.cursorSize();
        int linkCounter;
        HashMap<String, Integer> backlinks = new HashMap<String, Integer>();

        System.out.println("Start Backlink Count...");
        try {
            while (store.cursorHasNext()) {
                Document document = store.getNextDocument();
                Set<String> links = document.getLinks();
                for (String link : links) {
                    if (backlinks.containsKey(link)) {
                        linkCounter = backlinks.get(link);
                        linkCounter++;
                        backlinks.put(link, linkCounter);
                    } else {
                        backlinks.put(link, 1);
                    }
                }
                count++;
                printProcessingProgressWhitTimestamp(count, size,
                        "Backlink count progress", 1000);
            }
        } finally {
            store.closeCursor();
            System.out.println(count);
        }
        return backlinks;
    }

    private void saveBacklinksCount(HashMap<String, Integer> backlinks)
            throws Exception {
        int count = 0;
        store.setupCursor();
        int size = store.cursorSize();
        int linkCounter = 0;

        System.out.println("Start Backlink Count save...");
        try {
            while (store.cursorHasNext()) {
                Document document = store.getNextDocument();
                if (backlinks.containsKey(document.getTitle())) {
                    linkCounter = backlinks.get(document.getTitle());
                    document.setBackLinkCount(linkCounter);
                    store.update(document);
                }
                count++;
                printProcessingProgressWhitTimestamp(count, size,
                        "Backlink count save progress", 1000);
            }
        } finally {
            store.closeCursor();
            System.out.println(count);
        }
    }

    public void tokenize() throws Exception {
        BasicTokenizer tokenizer = new BasicTokenizer();
        int count = 0;
        store.setupCursor();
        int size = store.cursorSize();
        System.out.println("Start Tokenize...");
        try {
            while (store.cursorHasNext()) {

                HashSet<String> tokenSet = new HashSet<String>();

                Document document = store.getNextDocument();

                for (String s : tokenizer.getTokens(document.getTitle()))
                    tokenSet.add(s);

                Set<String> links = document.getLinks();
                for (String link : links) {
                    for (String s : tokenizer.getTokens(link))
                        tokenSet.add(s);
                }

                Set<String> redirects = document.getRedirects();
                for (String redirect : redirects) {
                    for (String s : tokenizer.getTokens(redirect))
                        tokenSet.add(s);
                }

                document.addTokens(tokenSet);
                store.update(document);
                count++;
                printProcessingProgressWhitTimestamp(count, size,
                        "Tokenize progress", 1000);
            }
        } finally {
            store.closeCursor();
            System.out.println(count);
        }

    }

    public void printProcessingProgressWhitTimestamp(int current, int total,
                                                     String info, int printInterval) {
        if (current % (total / printInterval + 1) == 0) {
            java.util.Date date = new java.util.Date();
            System.out.println(info + ": "
                    + ((double) current / (double) total) * 100 + "%" + " ("
                    + new Timestamp(date.getTime()) + ")");
        }
    }

}
