package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class URLScraper {
    static public String scrape(String url) throws IOException {
        String pageHTML = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla").get().html();

        //Extract text from page
        Document document = Jsoup.parse(pageHTML);
        Element body = document.body();

        return body.text();
    }
}
