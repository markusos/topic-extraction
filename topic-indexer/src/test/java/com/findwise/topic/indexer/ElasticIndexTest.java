package com.findwise.topic.indexer;

import com.findwise.topic.api.Document;
import org.junit.*;

public class ElasticIndexTest {
    // Use to debug ElasticIndex connection setup
    //@Test
    public void index() {
        ElasticIndex index = new ElasticIndex();
        Document d = new Document();
        d.addLink("a");
        index.index(d);
        index.close();
    }

}
