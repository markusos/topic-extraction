/*
 * Text importer class
 * 
 * Used to read text files ad returns text split into Sections
 * 
 */

package com.findwise.topic.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.findwise.topic.api.MongoStore;
import com.findwise.topic.api.Store;
import com.findwise.topic.extractor.util.Section;

public class TextImporter {

    private Store store;

    public TextImporter() {
        try {
            store = new MongoStore();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    // Read text file, no formatting
    public ArrayList<Section> readFile(String filepath) throws IOException {

        ArrayList<Section> sections = new ArrayList<Section>();
        StringBuilder input = new StringBuilder();

        // Read File
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String line;
        while ((line = br.readLine()) != null) {
            input.append(line + "\n");
        }
        br.close();

        // Split and create Sections
        String[] paragraphs = sectionSplit(input.toString());
        for (String p : paragraphs) {
            sections.add(new Section(p));
        }

        return sections;
    }

    // Split after a period
    private String[] sectionSplit(String text) {
        return text.split("(\\n\\n)");
    }

    // Read Simple Wikipedia abstract database XML dump structure
    public ArrayList<Section> readXMLEvaluationFile(String filepath)
            throws Exception {
        ArrayList<Section> sections = new ArrayList<Section>();
        int minSectionLength = 400;
        int maxSectionLength = Integer.MAX_VALUE;

        File fXmlFile = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("doc");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String articleTitle = eElement.getElementsByTagName("title")
                        .item(0).getTextContent().replace("Wikipedia: ", "");
                String articleAbstract = eElement
                        .getElementsByTagName("abstract").item(0)
                        .getTextContent();

                if (articleAbstract.length() > minSectionLength
                        && articleAbstract.length() < maxSectionLength) {
                    ArrayList<String> t = new ArrayList<String>();
                    t.add(articleTitle);

                    Section s = new Section(articleAbstract, t);

                    if (topicsExists(t))
                        sections.add(s);
                }
            }
        }
        return sections;
    }

    /*
     * Reads evaluation format: <topic>Topic1</topic> <text>Text containing
     * Topic1</text> <topic>Topic2</topic> <text>Text containing Topic2</text>
     * ... Returns Sections for each topic/text pair
     */
    public ArrayList<Section> readEvaluationFile(String filePath)
            throws IOException {
        ArrayList<Section> sections = new ArrayList<Section>();

        ArrayList<String> topics = new ArrayList<String>();
        StringBuilder text = new StringBuilder();
        Pattern patternTopic = Pattern.compile("(?<=<topic>).+?(?=</topic>)");
        Pattern patternTextStart = Pattern.compile("(<text>)");
        Pattern patternTextEnd = Pattern.compile("(</text>)");

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            Matcher topicMatcher = patternTopic.matcher(line);
            Matcher textStartMatcher = patternTextStart.matcher(line);
            Matcher textEndMatcher = patternTextEnd.matcher(line);

            if (topicMatcher.find()) {
                topics.add(topicMatcher.group(0));
            } else if (textStartMatcher.find()) {
                text = new StringBuilder();
            } else if (textEndMatcher.find()) {
                Section s = new Section(text.toString(), topics);
                sections.add(s);
                topics = new ArrayList<String>();
            } else
                text.append(line);
        }
        br.close();

        return sections;
    }

    private boolean topicsExists(ArrayList<String> topics) {
        com.findwise.topic.api.Document doc;

        for (String s : topics) {
            doc = store.getDocument(s);
            if (doc == null)
                return false;
        }
        return true;
    }

}
