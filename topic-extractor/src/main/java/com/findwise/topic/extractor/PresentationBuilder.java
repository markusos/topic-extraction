/*
 * Presentation builder class
 * 
 * Generates presentation html file and 
 * dot files with candidate graph structure
 */

package com.findwise.topic.extractor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileOutputStream;

import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.ResultNode;

public class PresentationBuilder {

    int count = 0;
    BufferedWriter out;
    int nrOfResults = 10;

    public PresentationBuilder(String filename, int nrOfResults) {
        try {
            this.nrOfResults = nrOfResults;

            File file = new File(filename + ".html");
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF8"));
            init();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {

        String init = "<!DOCTYPE html>"
                + "<html class='no-js'>"
                + "<head>"
                + "<meta charset='utf-8'>"
                + "<meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1'>"
                + "<link rel='stylesheet' type='text/css' href='results.css' media='screen' />"
                + "<title>Results</title>" + "</head>" + "<body>"
                + "<div class=results>";

        out.write(init);
    }

    public void close() {
        try {
            String close = "</div></div></body></html>";
            out.write(close);
            out.close();
            generateGraphs();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addResult(Result r) {
        try {
            out.write("<div class=result><div class=text>");

            out.write(r.getText());
            out.write("</div><div class=topics><ol>");

            for (ResultNode n : r.getResultList(nrOfResults)) {
                out.write("<li>" + n.getTitle() + " [" + n.getScore()
                        + "] </li>");
            }
            out.write("</ol></div><div class=clear></div><img src='graphs/resultGraph"
                    + count + ".dot.svg' class=graph /></div>");

            r.buildDotFile("dot/resultGraph" + count);

        } catch (IOException e) {
            e.printStackTrace();
        }
        count++;
    }

    private void generateGraphs() {
        try {
            // TODO update to detect running env
            // Windows
            //Runtime.getRuntime().exec("cmd /c start CreateGraph.cmd");
            // *NIX
            Runtime.getRuntime().exec("/bin/bash -c ./CreateGraph.sh");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
