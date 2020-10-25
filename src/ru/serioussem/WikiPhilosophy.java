package ru.serioussem;

import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WikiPhilosophy {
    private static final int NUMBER_OF_TRY = 50;
    static List<String> visited = new ArrayList<>();
    static WikiFetcher wf = new WikiFetcher();

    public static void main(String[] args) throws IOException {
        // TODO: 20.10.2020 сделать проверку 1000 рандомных сайтов
        // TODO: записать в файл результат (in steps) посчитать среднее
        // TODO: 20.10.2020 поймать NPE
        String destination = "https://en.wikipedia.org/wiki/Philosophy";
        String source = "https://en.wikipedia.org/wiki/Special:Random";
//        String source = "https://en.wikipedia.org/wiki/Country";

        for (int i = 0; i < NUMBER_OF_TRY; i++) {
            try {
                testConjecture(destination, source, 50);
                writeResult(visited.size());
            } catch (NotValidLinkException | HttpStatusException e) {
                System.out.println(e);
                writeResult(-1);
            } catch (LoopException e) {
                System.out.println(e);
                writeResult(0);
            } finally {
                visited = new ArrayList<>();
            }
            System.out.printf("Осталось %d фрагов\n", NUMBER_OF_TRY - i);
        }
    }

    public static class LoopException extends RuntimeException {
        public LoopException(String message) {
            super(message);
        }
    }

    public static class NotValidLinkException extends RuntimeException {
        public NotValidLinkException(String message) {
            super(message);
        }
    }

    private static void writeResult(int attempts) {
        try (FileWriter fileWriter = new FileWriter(new File("src/result/testOut.txt"), true);
             BufferedWriter bw = new BufferedWriter(fileWriter);
        ) {
            bw.write(String.format("%d", attempts) + System.lineSeparator());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void testConjecture(String destination, String source, int limit) throws IOException {
        String url = source;
        for (int i = 0; i < limit; i++) {
            if (visited.contains(url)) {
                throw new LoopException("We're in a loop, exiting.");
            } else {
                visited.add(url);
            }
            Element elt = getFirstValidLink(url);
            if (elt == null) {
                throw new NotValidLinkException("Got to a page with no valid links.");
            }

//            System.out.println("**" + elt.text() + "**");
            url = elt.attr("abs:href");

            if (url.equals(destination)) {
                System.out.println("Eureka!");
                System.out.printf("We reached the goal in %d steps\n", visited.size());
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
//        writeResult(visited.size());
    }
}