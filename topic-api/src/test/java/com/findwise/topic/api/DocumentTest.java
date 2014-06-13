/*
 * Test Document structure
 */

package com.findwise.topic.api;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DocumentTest {

    Map<String, Object> map;
    Set<String> links;
    Set<String> redirects;
    Set<String> tokens;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        map = new HashMap<String, Object>();
        links = new HashSet<String>();
        links.add("A");
        links.add("B");
        links.add("C");
        links.add("D");

        redirects = new HashSet<String>();
        redirects.add("T");
        redirects.add("TEST");
        redirects.add("TeSt");

        tokens = new HashSet<String>();
        tokens.add("t");
        tokens.add("test");

        map.put("title", "test");
        map.put("linkCount", 4);
        map.put("links", new ArrayList<String>(links));
        map.put("redirects", new ArrayList<String>(redirects));
        map.put("redirectCount", 3);
        map.put("backLinkCount", 5);
        map.put("tokenCount", 2);
        map.put("tokens", new ArrayList<String>(tokens));

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void NewEmptyTest() {
        Document d = new Document();
        assertTrue(d.getTitle().equals(""));
        assertTrue(d.getLinkCount() == 0);
        assertTrue(d.getBackLinkCount() == 0);
        assertTrue(d.getRedirectCount() == 0);
        assertTrue(d.getLinks().isEmpty());
        assertTrue(d.getRedirects().isEmpty());
    }

    @Test
    public void NewFromMapTest() {
        Document d = new Document(map);
        assertTrue(d.getTitle().equals("test"));
        assertTrue(d.getLinkCount() == 4);
        assertTrue(d.getBackLinkCount() == 5);
        assertTrue(d.getRedirectCount() == 3);
        assertTrue(d.getLinks().equals(links));
        assertTrue(d.getRedirects().equals(redirects));
    }

    @Test
    public void toMapTest() {
        Document d = new Document(map);
        System.out.println(map.toString());
        System.out.println(d.toMap().toString());
        assertTrue(d.toMap().toString().equals(map.toString()));
    }

    @Test
    public void setTest() {
        Document d = new Document();
        d.setBackLinkCount(10);
        d.setTitle("Test");
        d.addLink("A");
        d.addLink("B");
        d.addRedirect("T");

        assertTrue(d.getTitle().equals("Test"));
        assertTrue(d.getLinkCount() == 2);
        assertTrue(d.getBackLinkCount() == 10);
        assertTrue(d.getRedirectCount() == 1);
        assertTrue(d.getLinks().contains("A"));
        assertTrue(d.getRedirects().contains("T"));
    }

    @Test
    public void mergeTest() {

        Map<String, Object> map2 = new HashMap<String, Object>();
        Set<String> links2 = new HashSet<String>();
        links2.add("E");
        links2.add("F");
        links2.add("G");
        links2.add("H");

        Set<String> redirects2 = new HashSet<String>();
        redirects2.add("Testa");
        redirects2.add("testad");
        redirects2.add("testat");

        Set<String> tokens2 = new HashSet<String>();
        tokens2.add("testa");
        tokens2.add("testad");
        tokens2.add("testat");
        tokens2.add("e");
        tokens2.add("f");
        tokens2.add("g");
        tokens2.add("h");

        map2.put("title", "test");
        map2.put("linkCount", 4);
        map2.put("links", new ArrayList<String>(links2));
        map2.put("redirectCount", 3);
        map2.put("redirects", new ArrayList<String>(redirects2));
        map2.put("backLinkCount", 5);
        map2.put("tokens", new ArrayList<String>(tokens2));
        map2.put("tokenCount", 7);

        Document d = new Document(map);
        Document d2 = new Document(map2);
        try {
            d.merge(d2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(d.getTitle().equals("test"));
        assertTrue(d.getLinkCount() == 8);
        assertTrue(d.getBackLinkCount() == 5);
        assertTrue(d.getRedirectCount() == 6);

        links.addAll(links2);
        redirects.addAll(redirects2);

        System.out.println(d.getLinks());
        System.out.println(links);

        System.out.println(d.getRedirects());
        System.out.println(redirects);

        assertTrue(d.getLinks().equals(links));
        assertTrue(d.getRedirects().equals(redirects));
    }

}
