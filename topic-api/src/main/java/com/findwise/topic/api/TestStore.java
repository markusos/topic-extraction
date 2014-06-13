package com.findwise.topic.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TestStore implements Store {

    private Map<String, Document> store = new HashMap<String, Document>();
    private Set<String> cursorSet;
    private Iterator<String> cursor;

    @Override
    public void close() throws IOException {
        // Do nothing
    }

    @Override
    public void clear() throws IOException {
        store = new HashMap<String, Document>();
    }

    @Override
    public Document getDocument(String title) {
        return store.get(title);
    }

    @Override
    public void save(Document document) {
        store.put(document.getTitle(), document);
    }

    @Override
    public void update(Document document) throws Exception {
        if (!store.containsKey(document.getTitle())) {
            System.err.println("Doc with title: " + document.getTitle()
                    + " does not exist, Saving!");
            save(document);
        } else {
            Document existing = store.get(document.getTitle());
            existing.merge(document);
            store.put(document.getTitle(), existing);
        }
    }

    @Override
    public void delete(String title) {
        store.remove(title);
    }

    @Override
    public void setupCursor() {
        cursorSet = store.keySet();
        cursor = cursorSet.iterator();
    }

    @Override
    public void setupCursor(int linkCount) {
        cursorSet = store.keySet();
        cursor = cursorSet.iterator();
    }

    @Override
    public boolean cursorHasNext() {
        return cursor.hasNext();
    }

    @Override
    public int cursorSize() {
        return cursorSet.size();
    }

    @Override
    public void closeCursor() {
        // Do nothing
    }

    @Override
    public Document getNextDocument() {
        return store.get(cursor.next());
    }
}
