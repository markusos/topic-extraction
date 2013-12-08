/*
 * Interface for data Index
 */

package com.findwise.topic.indexer;

import com.findwise.topic.api.Document;

public interface Index {

	public void initBulkIndex();

	public void addToBulkIndex(Document document);

	public void bulkIndex();

	public void index(Document document);

	public void close();

}
