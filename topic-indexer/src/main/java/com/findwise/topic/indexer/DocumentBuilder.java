/*
 * DocumentBuilder
 * 
 * Builds Documents from Links, and saves them to the Document Store
 */

package com.findwise.topic.indexer;

import com.findwise.topic.api.Document;
import com.findwise.topic.api.MongoStore;
import com.findwise.topic.api.Store;
import com.findwise.topic.indexer.link.Link;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class DocumentBuilder {

    Store store;
    Document currentDocument;
    HashMap<String, Document> memStore;
    static private int memStoreMaxSize = 100000;

    public DocumentBuilder() throws IOException {
        currentDocument = new Document();
        store = new MongoStore();
        memStore = new HashMap<String, Document>();
    }

    public DocumentBuilder(String store) throws IOException {
        currentDocument = new Document();
        this.store = new MongoStore(store);
        memStore = new HashMap<String, Document>();
    }

    public DocumentBuilder(Store store) throws IOException {
        currentDocument = new Document();
        this.store = store;
        memStore = new HashMap<String, Document>();
    }

    public void saveLink(Link link) throws Exception {
        if (!currentDocument.getTitle().equals(link.getFrom())) {
            // Save old document
            if (!currentDocument.getTitle().equals("")) {
                commit();
            }
            // Reset list of links
            currentDocument = new Document();
            currentDocument.setTitle(link.getFrom());
            currentDocument.addLink(link.getTo());
        } else {
            currentDocument.addLink(link.getTo());
        }
    }

    public void commit() throws Exception {
        if (memStore.containsKey(currentDocument.getTitle())) {
            Document savedDocument = memStore.get(currentDocument.getTitle());
            savedDocument.merge(currentDocument);
            memStore.put(savedDocument.getTitle(), savedDocument);
        } else
            memStore.put(currentDocument.getTitle(), currentDocument);

        if (memStore.size() >= memStoreMaxSize)
            commitToDb();
    }

    public void memstoreSizeprint() {
        System.out.println("Memstore size is: " + memStore.size());
    }

    public void commitToDb() throws Exception {

        System.out.println("Commiting to DB...");
        int i = 1;
        for (Entry<String, Document> entry : memStore.entrySet()) {

            Document d = entry.getValue();
            Document storedDocument = store.getDocument(d.getTitle());
            if (storedDocument != null) {
                storedDocument.merge(d);
                store.update(storedDocument);
            } else {
                store.save(d);
            }
            if (i % (memStore.size() / 20 + 1) == 0)
                System.out.println(Math.round(((double) i / (double) memStore
                        .size()) * 100) + "%");

            i++;
        }
        memStore.clear();
        System.out.println("Done committing to DB!");
    }
}
