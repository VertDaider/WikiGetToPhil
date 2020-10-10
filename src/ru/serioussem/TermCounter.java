package ru.serioussem;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TermCounter {

    private Map<String, Integer> map;
    private String label;

    public TermCounter(String label) {
        this.label = label;
        this.map = new HashMap<String, Integer>();
    }

    public String getLabel() {
        return label;
    }

    public int size() {
        int total = 0;
        for (Integer value: map.values()) {
            total += value;
        }
        return total;
    }

    public void processElements(Elements paragraphs) {
        for (Node node : paragraphs) {
            processTree(node);
        }
    }

    private void processTree(Node root) {
        for (Node node : new WikiNodeIterable(root)) {
            if (node instanceof TextNode) {
                processText(((TextNode) node).text());
            }
        }
    }

    private void processText(String text) {
        String[] array = text.replaceAll("\\pP", " ").
                toLowerCase().
                split("\\s+");

        for (String term : array) {
            incrementTermCount(term);
        }
    }

    private void incrementTermCount(String term) {
        put(term, get(term) + 1);
    }

    public void put(String term, int count) {
        map.put(term, count);
    }

    public Integer get(String term) {
        Integer count = map.get(term);
        return count == null ? 0 : count;
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public void printCounts() {
        for (String key : keySet()) {
            Integer count = get(key);
            System.out.println(key + ", " + count);
        }
        System.out.println("Total of all counts = " + size());
    }

    public static void main(String[] args) throws IOException {
        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";

        WikiFetcher wf = new WikiFetcher();
        Elements paragraphs = wf.fetchWikipedia(url);

        TermCounter counter = new TermCounter(url);
        counter.processElements(paragraphs);
        counter.printCounts();

    }
}
