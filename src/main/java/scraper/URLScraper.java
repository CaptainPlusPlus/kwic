/**
 * Course:      Data Structures and Algorithms for Computational Linguistics II SS2021
 * Assignment:  (Final KWIC Project)
 * Authors:      (Hoyeon Lee, Nkonye Gbadegoye, Dewi Surya, Jacqueline Becker, Felix Redfox)
 * Description: (Project to extract text from url and find/save needle typed results on it with their POS information)
 *
 * Honor Code:  We pledge that this program represents our own work.
 *  we did not receive help from anyone in designing and debugging this program.
 */


package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Scraper obtaining body from given url
 */
public class URLScraper {
    /**
     *
     * @param url address body to be retrieved from http protocoled site
     * @return the body html of given URL
     * @throws IOException
     */
    static public String scrape(String url) throws IOException {
        //Initiate path for scraping, connect to site and retrieve HTML
        String pageHTML = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla").get().html();

        //Extract text from page, process HTML into string text and isolate body
        Document document = Jsoup.parse(pageHTML);
        Element body = document.body();

        //convert body to string format
        return body.text();
    }
}
