/*
 * ResultEvaluator class
 * 
 * Builds candidate graph form input file
 * and can use different result scorer algorithms
 * to calculate score and recall
 * 
 * Only used for evaluation
 */

package com.findwise.topic.extractor;

import java.util.ArrayList;
import java.util.List;

import com.findwise.topic.extractor.candidateextractor.CandidateExtractor;
import com.findwise.topic.extractor.resultscorer.ResultScorer;
import com.findwise.topic.extractor.util.EvaluationScore;
import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.ResultNode;
import com.findwise.topic.extractor.util.Section;

public class ResultEvaluator {

    ArrayList<Section> sections;

    private int nrOfResults;
    private CandidateExtractor extractor;

    // Filter graph defaults
    private int minBacklinks = Integer.MAX_VALUE;
    private int minLinks = 1;

    public ResultEvaluator(CandidateExtractor extractor, int nrOfResults) {
        this.extractor = extractor;
        this.nrOfResults = nrOfResults;
    }

    public void setGraphFilter(int minLinks, int minBacklinks) {
        this.minLinks = minLinks;
        this.minBacklinks = minBacklinks;
    }

    public void buildResults(String file, boolean xmlData) {

        TextImporter textImporter = new TextImporter();
        try {
            if (xmlData)
                sections = textImporter.readXMLEvaluationFile(file);
            else
                sections = textImporter.readEvaluationFile(file);

            for (Section s : sections) {
                Result result = extractor.getCandidates(s.getText());
                s.setResult(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public EvaluationScore evaluatePerformances(ResultScorer scorer) {

        float recall = 0;
        float totalRecall = 0;
        float totalScore = 0;

        for (Section s : sections) {
            Result result = s.getResult();
            result.resetScore();
            result.filterUsedNodes(minLinks, minBacklinks);
            result.calculateScore(scorer);
            int score = calculatePositionScore(result, s);
            totalScore += score;
            totalRecall += calculateRecall(result, s);
        }
        recall = totalRecall / sections.size();
        totalScore = totalScore / sections.size();

        return new EvaluationScore(recall, totalScore);
    }

    private float calculateRecall(Result result, Section section) {
        boolean found = false;
        for (ResultNode r : result.getResultList(nrOfResults)) {
            for (String t : section.getTopics()) {
                if (r.getTitle().toLowerCase().equals(t.toLowerCase())) {
                    found = true;
                }
            }
        }

        if (found)
            return 1;
        else
            return 0;
    }

    private int calculatePositionScore(Result result, Section section) {
        int score = 10;
        for (ResultNode r : result.getResultList(nrOfResults)) {
            for (String t : section.getTopics()) {
                if (r.getTitle().toLowerCase().equals(t.toLowerCase())) {
                    return score;
                }
                if (score > 0)
                    score--;
            }
        }
        return 0;
    }


    public void buildResultPresentation(String filename) {
        PresentationBuilder presentation = new PresentationBuilder(filename, nrOfResults);
        for (Section s : sections) {
            presentation.addResult(s.getResult());
        }
        presentation.close();
    }
}
