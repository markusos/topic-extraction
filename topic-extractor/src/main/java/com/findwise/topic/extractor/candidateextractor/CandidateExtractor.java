/*
 * Interface for candidate extractor
 */

package com.findwise.topic.extractor.candidateextractor;

import com.findwise.topic.extractor.util.Result;

public interface CandidateExtractor {

    public Result getCandidates(String section);

}
