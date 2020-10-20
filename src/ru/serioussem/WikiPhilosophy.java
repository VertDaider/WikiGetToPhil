package ru.serioussem;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WikiPhilosophy {
    final static List<String> visited = new ArrayList<>();
    final static WikiFetcher wf = new WikiFetcher();

    public static void main(String[] args) throws IOException {

        String destination = "https://en.wikipedia.org/wiki/Philosophy";
        String source = "https://en.wikipedia.org/wiki/Special:Random";

        testConjecture(destination, source, 50);
        // TODO: 20.10.2020 сделать проверку 1000 рандомных сайтов
        // TODO: записать в файл результат (in steps) посчитать среднее
    }

    public static void testConjecture(String destination, String source, int limit) throws IOException {
        String url = source;
        for (int i=0; i<limit; i++) {
            if (visited.contains(url)) {
                System.err.println("We're in a loop, exiting.");
                return;
            } else {
                visited.add(url);
            }
            Element elt = getFirstValidLink(url);
            if (elt == null) {
                System.err.println("Got to a page with no valid links.");
                return;
            }

            System.out.println("**" + elt.text() + "**");
            url = elt.attr("abs:href");

            if (url.equals(destination)) {
                System.out.println("Eureka!");
                System.out.printf("We reached the goal in %d steps", visited.size());
                break;
            }
        }
    }

    public static Element getFirstValidLink(String url) throws IOException {
        print(url);
        Elements paragraphs = wf.fetchWikipedia(url);
        WikiParser wp = new WikiParser(paragraphs);
        return wp.findFirstLink();
    }

    private static void print(Object... args) {
        System.out.printf("Fetching %s...%n", args);
    }
}