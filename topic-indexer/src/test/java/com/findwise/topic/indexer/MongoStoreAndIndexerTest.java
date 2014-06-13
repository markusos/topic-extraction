/*
 * Test MongoStore and DataIndexer classes
 */

package com.findwise.topic.indexer;

import com.findwise.topic.api.Document;
import com.findwise.topic.api.Store;
import com.findwise.topic.api.TestStore;
import org.junit.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class MongoStoreAndIndexerTest {

    static Store store;
    static DataIndexer indexer;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        //store = new MongoStore("test");
        store = new TestStore();
        indexer = new DataIndexer(store);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        store.close();
    }

    @Before
    public void setUp() throws Exception {
        store.clear();
    }

    @After
    public void tearDown() throws Exception {

    }

    //@Test
    public void mergeTest() {

        Set<String> l1 = new HashSet<String>();
        l1.add("A");
        l1.add("B");
        l1.add("C");

        Set<String> res = new HashSet<String>();
        res.add("A2");

        Document d1 = new Document();
        d1.setTitle("A");
        d1.addLinks(l1);

        Document d2 = new Document();
        d2.setTitle("B");
        d2.addLinks(l1);

        Document d3 = new Document();
        d3.setTitle("A2");
        d3.addLink("A");

        store.save(d1);
        store.save(d2);
        store.save(d3);
        System.out.println("assert");
        assertTrue(store.getDocument("A").getLinks().equals(l1));
        assertTrue(store.getDocument("B").getLinks().equals(l1));
        assertTrue(store.getDocument("A2").getLinks().contains("A"));

        try {
            indexer.mergeRedirects();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(store.getDocument("A").getLinks().equals(l1));
        assertTrue(store.getDocument("B").getLinks().equals(l1));
        assertTrue(store.getDocument("A").getRedirects().equals(res));

        assertTrue(store.getDocument("A2") == null);

        assertTrue(store.getDocument("A").getRedirectCount() == 1);
        assertTrue(store.getDocument("A").getLinkCount() == 3);

        assertTrue(store.getDocument("B").getRedirectCount() == 0);
        assertTrue(store.getDocument("B").getLinkCount() == 3);
        System.out.println("done");
    }
}
