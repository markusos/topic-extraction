/*
 * Test that the link parsing is done correctly
 */

package com.findwise.topic.indexer.link;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LinkParserTest {

	LinkParser linkParser;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		linkParser = new LinkParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void parseLineToLinkWithSpaceTest() {

		Link testLink;
		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/Autism> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/Centers_for_Disease_Control_and_Prevention> .");
			assertTrue(testLink.getFrom().equals("Autism"));
			assertTrue(testLink.getTo().equals(
					"Centers for Disease Control and Prevention"));
		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}
	}

	@Test
	public void decodeWikiResourceTest() {
		Link testLink;
		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/%41%42_%43%20> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/Centers_for_Disease_Control_and_Prevention> .");
			assertTrue(testLink.getFrom().equals("AB C "));
			assertTrue(testLink.getTo().equals(
					"Centers for Disease Control and Prevention"));
		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}
	}

	@Test
	public void skipPrefixesTest() {
		Link testLink;
		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/Category: Test> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/Centers_for_Disease_Control_and_Prevention> .");
			assertTrue(testLink == null);

			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/Test> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/List of tests>");
			assertTrue(testLink == null);

		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}
	}

	@Test
	public void selfLinkTest() {

		Link testLink;
		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/Autism> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/Autism> .");
			assertTrue(testLink == null);
		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}
	}

	@Test
	public void yearLinkTest() {

		Link testLink;
		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/2000> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/Autism> .");
			assertTrue(testLink == null);
		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}

		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/Test> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/2000> .");
			assertTrue(testLink == null);
		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}

		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/2000 in music> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/Autism> .");
			assertTrue(testLink == null);
		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}

		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/Test> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/2000 in american film> .");
			assertTrue(testLink == null);
		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}

		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/Windows 2000> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/Test> .");
			assertTrue(testLink.getFrom().equals("Windows 2000"));
			assertTrue(testLink.getTo().equals("Test"));
		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}

	}

	@Test
	public void monthLinkTest() {

		Link testLink;
		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/January_1> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/Autism> .");
			assertTrue(testLink == null);
		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}

		try {
			testLink = linkParser
					.parseLineToLink("<http://dbpedia.org/resource/March_(music)> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://dbpedia.org/resource/Test> .");
			assertTrue(testLink.getFrom().equals("March (music)"));
			assertTrue(testLink.getTo().equals("Test"));
		} catch (UnsupportedEncodingException e) {
			fail("Parse error");
		}

	}
}
