/*
 * Store Interface
 * 
 * Defines methods to store and access documents
 */

package com.findwise.topic.api;

import java.io.IOException;

public interface Store {

    public void close() throws IOException;

    public void clear() throws IOException;

    public Document getDocument(String title);

    public void save(Document document);

    public void update(Document document) throws Exception;

    public void delete(String title);

    public void setupCursor();

    public void setupCursor(int linkCount);

    public boolean cursorHasNext();

    public int cursorSize();

    public void closeCursor();

    public Document getNextDocument();

}
