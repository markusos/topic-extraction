/*
 * Test for DocumentBuilder class
 */

package com.findwise.topic.indexer;

import com.findwise.topic.api.Store;
import com.findwise.topic.api.TestStore;
import com.findwise.topic.indexer.link.Link;
import org.junit.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class DocumentBuilderTest {

    static DocumentBuilder builder;
    static Store store;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        store = new TestStore();
        builder = new DocumentBuilder(store);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    @Before
    public void setUp() throws Exception {
        store.clear();
    }

    @After
    public void tearDown() throws Exception {
        store.close();
    }

    @Test
    public void testSaveAndGetLinks() {
        Set<Link> links = new HashSet<Link>();
        links.add(new Link("A", "A"));
        links.add(new Link("A", "B"));
        links.add(new Link("A", "B"));
        links.add(new Link("A", "C"));

        links.add(new Link("B", "A"));
        links.add(new Link("B", "B"));
        links.add(new Link("B", "C"));

        links.add(new Link("B2", "B"));

        links.add(new Link("A", "B"));
        links.add(new Link("A", "C"));
        links.add(new Link("B", "D"));
        links.add(new Link("C", "A"));
        links.add(new Link("C", "B"));
        links.add(new Link("A", "D"));

        links.add(new Link("A2", "A"));

        links.add(new Link("C", "C"));
        links.add(new Link("C", "D"));

        links.add(new Link("D", "A"));
        links.add(new Link("D", "B"));
        links.add(new Link("D", "C"));
        links.add(new Link("D", "D"));

        Set<String> res = new HashSet<String>();
        res.add("A");
        res.add("B");
        res.add("C");
        res.add("D");
        try {
            for (Link l : links) {
                System.out.println("saving link: " + l.getFrom() + " : "
                        + l.getTo());
                builder.saveLink(l);
            }

            builder.commit();
            builder.commitToDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // assert data in db
        assertTrue(store.getDocument("A").getLinks().equals(res));
        assertTrue(store.getDocument("B").getLinks().equals(res));
        assertTrue(store.getDocument("C").getLinks().equals(res));
        assertTrue(store.getDocument("D").getLinks().equals(res));

        assertTrue(store.getDocument("A").getRedirectCount() == 0);
        // assertTrue(store.getDocument("A").getRedirects().contains("A2"));
        assertTrue(store.getDocument("A").getLinkCount() == 4);

        assertTrue(store.getDocument("B").getRedirectCount() == 0);
        assertTrue(store.getDocument("B").getLinkCount() == 4);

        assertTrue(store.getDocument("C").getRedirectCount() == 0);
        assertTrue(store.getDocument("C").getLinkCount() == 4);
    }

}
