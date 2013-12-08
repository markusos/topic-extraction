package com.findwise.topic.extractor;

import com.findwise.topic.api.Document;
import com.findwise.topic.extractor.resultscorer.ClosenessCentralityResultScorer;
import com.findwise.topic.extractor.util.Result;
import org.junit.Test;

public class ResultScorerTest {

	private String randomLink(String title) {
		String[] links = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j" };
		String link = links[(int) (Math.random() * 9)];
		while (link.equals(title))
			link = links[(int) (Math.random() * 9)];
		return link;
	}

	private Document randomDocument(String title) {
		Document doc = new Document();
		doc.setTitle(title);
		doc.addLink(randomLink(title));
		doc.addLink(randomLink(title));

		return doc;
	}

	private Result buildResult() {
		Result result = new Result("A B C D E F G H I J");

		Document a = randomDocument("a");
		Document b = randomDocument("b");
		Document c = randomDocument("c");
		Document d = randomDocument("d");
		Document e = randomDocument("e");
		Document f = randomDocument("f");
		Document g = randomDocument("g");
		Document h = randomDocument("h");
		Document i = randomDocument("i");

		Document j = new Document();
		j.setTitle("j");
		j.addLink("k");

		Document k = new Document();
		k.setTitle("k");

		result.add(a, 1);
		result.add(b, 1);
		result.add(c, 1);
		result.add(d, 1);
		result.add(e, 1);
		result.add(f, 1);
		result.add(g, 1);
		result.add(h, 1);
		result.add(i, 1);

		result.add(j, 1);
		result.add(k, 1);

		result.filterUsedNodes(0, 0);

		return result;
	}

	@Test
	public void test() {
		PresentationBuilder presenter = new PresentationBuilder("testResult", 12);

		for (int i = 0; i < 10; i++) {
			Result result = buildResult();
			result.calculateScore(new ClosenessCentralityResultScorer());
			 presenter.addResult(result);
		}
		 presenter.close();
	}

}
