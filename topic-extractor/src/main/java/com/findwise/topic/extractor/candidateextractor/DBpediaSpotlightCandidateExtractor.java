/*
 * DBpedia Spotlight candidate extractor
 * 
 * Uses the DBpedia Spotlight service
 *  at: http://spotlight.dbpedia.org/rest/annotate/
 *  to annotate candidates from the text.
 */

package com.findwise.topic.extractor.candidateextractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.findwise.topic.api.Document;
import com.findwise.topic.api.MongoStore;
import com.findwise.topic.api.Store;
import com.findwise.topic.extractor.util.Result;

public class DBpediaSpotlightCandidateExtractor implements CandidateExtractor {

    private Store store;

    public DBpediaSpotlightCandidateExtractor() {
        try {
            store = new MongoStore();
        } catch (UnknownHostException e) {
            System.err.println("Error connecting to MongoDB");
            e.printStackTrace();
        }
    }

    public Result getCandidates(String text) {
        Result result = new Result(text);
        try {
            Set<String> candidates = getAnnotation(text);
            Document doc;
            for (String c : candidates) {
                doc = store.getDocument(c);
                if (doc != null)
                    result.add(doc, 0);
            }

        } catch (Exception e) {
            System.err.println(text);
            e.printStackTrace();
        }

        return result;
    }

    public Set<String> getAnnotation(String text) throws Exception {
        Set<String> result = new HashSet<String>();
        if (text.equals("")) {
            return result;
        }

        String data = "disambiguator=Document&confidence=-1&support=-1&text="
                + URLEncoder.encode(text, "UTF-8");

        URL url = new URL("http://spotlight.dbpedia.org/rest/annotate/");
        String charset = "UTF-8";
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=" + charset);
        OutputStream output = null;
        try {
            output = connection.getOutputStream();
            output.write(data.getBytes(charset));
        } finally {
            if (output != null)
                try {
                    output.close();
                } catch (IOException e) {
                }
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String inputLine;
        Pattern pattern = Pattern
                .compile("\"@URI\": \"http://dbpedia.org/resource/(.*?)\"");
        while ((inputLine = in.readLine()) != null) {
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                String resource = matcher.group(1);
                resource = URLDecoder.decode(resource, "UTF-8");
                resource = resource.replace('_', ' ');
                result.add(resource);
            }
        }
        in.close();

        return result;
    }
}
