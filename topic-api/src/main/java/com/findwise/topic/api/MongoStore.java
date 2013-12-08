/*
 * Store implementation for MongoDB
 */

package com.findwise.topic.api;

import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

public class MongoStore implements Store {
	static final boolean DEBUG = false;
	DB db;
	DBCollection collection;
	MongoClient mongoClient;

	DBCursor currentCursor;

	public MongoStore() throws UnknownHostException {
		mongoClient = new MongoClient("localhost");
		db = mongoClient.getDB("wiki");
		collection = db.getCollection("wiki");

		// Set up index
		BasicDBObject query = new BasicDBObject("title", 1);
		collection.ensureIndex(query, "title", true);
	}

	public MongoStore(String store) throws UnknownHostException {
		mongoClient = new MongoClient("localhost");
		db = mongoClient.getDB(store);
		collection = db.getCollection(store);
		collection.ensureIndex("title");
	}

	public void close() throws IOException {
		mongoClient.close();
	}

	public void clear() throws IOException {
		collection.drop();
	}

	@SuppressWarnings("unchecked")
	public Document getDocument(String title) {
		Document document;
		BasicDBObject doc = new BasicDBObject("title", title);
		DBObject article = collection.findOne(doc);
		if (article != null) {
			document = new Document(article.toMap());
			return document;
		} else {
			return null;
		}
	}

	public void setupCursor() {
		currentCursor = collection.find();
	}


	public void setupCursor(int linkCount) {
		BasicDBObject filter = new BasicDBObject("linkCount", linkCount);
		currentCursor = collection.find(filter);
	}

	public boolean cursorHasNext() {
		return currentCursor.hasNext();
	}


	public int cursorSize() {
		return currentCursor.size();
	}


	public void closeCursor() {
		currentCursor.close();
	}

	@SuppressWarnings("unchecked")

	public Document getNextDocument() {
		return new Document(currentCursor.next().toMap());
	}


	public void save(Document document) {
		BasicDBObject query = new BasicDBObject("title", document.getTitle());
		BasicDBObject doc = new BasicDBObject(document.toMap());
		collection.update(query, doc, true, false);
	}


	public void update(Document document) throws Exception {
		BasicDBObject title = new BasicDBObject("title", document.getTitle());
		DBObject article = collection.findOne(title);
		if (article == null) {
			System.err.println("Doc with title: " + title
					+ " does not exist, Saving!");
			save(document);
		} else {
			@SuppressWarnings("unchecked")
			Document existing = new Document(article.toMap());
			existing.merge(document);
			BasicDBObject doc = new BasicDBObject(existing.toMap());
			collection.update(title, doc);
		}
	}

	public void delete(String title) {
		DBObject query = new BasicDBObject("title", title);
		collection.remove(query);
	}
}
