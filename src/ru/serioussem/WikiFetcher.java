package ru.serioussem;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WikiFetcher {
    private long lastRequestTime = -1;
    private long minInterval = 1000;

    public Elements fetchWikipedia(String url) throws IOException {
        sleepIfNeeded();

        Connection conn = Jsoup.connect(url);
        Document doc = conn.get();

        Element content = doc.getElementById("mw-content-text");
        Elements paras = content.select("p");
        return paras;
    }

    private void sleepIfNeeded() {
        if (lastRequestTime != -1) {
            long currentTime = System.currentTimeMillis();
            long nextRequestTime = lastRequestTime + minInterval;
            if (currentTime < nextRequestTime) {
                try {
                    Thread.sleep(nextRequestTime - currentTime);
                } catch (InterruptedException e) {
                    System.err.println("Warning: sleep interrupted in fetchWikipedia.");
                }
            }
        }
        lastRequestTime = System.currentTimeMillis();
    }

    public Elements readWikipedia(String url) throws IOException {
        URL realUrl = new URL(url);

        String slash = File.separator;
        String filename = "resources" + slash + realUrl.getHost() + realUrl.getPath();

        InputStream stream = WikiFetcher.class.getClassLoader().getResourceAsStream(filename);
        Document doc = Jsoup.parse(stream, "UTF-8", filename);

        Element content = doc.getElementById("mw-content-text");
        Elements paras = content.select("p");
        return paras;
    }
}
