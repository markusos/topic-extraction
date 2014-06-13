package com.findwise.topic.extractor.resultscorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Stack;

import com.findwise.topic.extractor.util.Result;
import com.findwise.topic.extractor.util.ResultNode;

public class BetweennessCentralityResultScorer implements ResultScorer {

    public void calculateScore(Result result) {
        Map<String, Float> centrality = calculateBetweennessCentrality(result);

        Map<String, ResultNode> graph = result.getUsedGraph();

        for (Entry<String, ResultNode> g : graph.entrySet()) {
            ResultNode node = g.getValue();
            node.setScore(1 + centrality.get(node.getTitle()));
        }
    }

    public String toString() {
        return "BetweennessCentralityResultScorer";
    }

    private Map<String, Float> calculateBetweennessCentrality(Result result) {

        Map<String, ResultNode> graph = result.getUsedGraph();

        Map<String, Float> centrality = new HashMap<String, Float>();
        Map<String, List<String>> p = new HashMap<String, List<String>>();

        for (Entry<String, ResultNode> g : graph.entrySet()) {
            ResultNode v = g.getValue();
            centrality.put(v.getTitle(), (float) 0);
        }

        for (Entry<String, ResultNode> g : graph.entrySet()) {
            ResultNode s = g.getValue();

            Stack<ResultNode> stack = new Stack<ResultNode>();
            Queue<ResultNode> queue = new LinkedList<ResultNode>();

            // Init maps
            Map<String, Float> d = new HashMap<String, Float>();
            Map<String, Float> o = new HashMap<String, Float>();

            for (Entry<String, ResultNode> h : graph.entrySet()) {
                ResultNode init = h.getValue();
                p.put(init.getTitle(), new ArrayList<String>());

                if (init.getTitle().equals(s.getTitle())) {
                    d.put(init.getTitle(), 0f);
                    o.put(init.getTitle(), 1f);
                } else {
                    d.put(init.getTitle(), -1f);
                    o.put(init.getTitle(), 0f);
                }
            }
            queue.add(s);

            while (!queue.isEmpty()) {
                ResultNode v = queue.poll();
                stack.push(v);

                for (ResultNode w : v.getUsedLinks()) {

                    if (d.get(w.getTitle()) < 0) {
                        queue.add(w);
                        d.put(w.getTitle(), d.get(v.getTitle()) + 1);
                    }

                    if (d.get(w.getTitle()) == (d.get(v.getTitle()) + 1)) {
                        o.put(w.getTitle(),
                                o.get(w.getTitle()) + o.get(v.getTitle()));
                        List<String> pw = p.get(w.getTitle());
                        pw.add(v.toString());
                        p.put(w.getTitle(), pw);
                    }
                }
            }

            Map<String, Float> delta = new HashMap<String, Float>();

            for (Entry<String, ResultNode> h : graph.entrySet()) {
                ResultNode v = h.getValue();
                delta.put(v.getTitle(), 0f);
            }

            while (!stack.isEmpty()) {
                String w = stack.pop().getTitle();
                for (String v : p.get(w)) {
                    float delta_v = delta.get(v);
                    float delta_w = delta.get(w);
                    float o_v = o.get(v);
                    float o_w = o.get(w);
                    delta_v = delta_v + (o_v / o_w) * (1 + delta_w);
                    delta.put(v, delta_v);
                }
                if (!w.equals(s.getTitle())) {
                    float c_w = centrality.get(w);
                    float delta_w = delta.get(w);
                    c_w = c_w + delta_w;
                    centrality.put(w, c_w);
                }
            }
        }

        return centrality;
    }
}
