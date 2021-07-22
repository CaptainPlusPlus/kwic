package scraper;

import junit.framework.TestCase;

import static scraper.URLScraper.scrape;

public class URLScraperTest extends TestCase {
    String exampleComText = "Example Domain\n" +
            "This domain is for use in illustrative examples in documents. You may use this domain in literature without prior coordination or asking for permission.\n" +
            "\n" +
            "More information...";

    void testScrape() {
        assertEquals(scrape("example.com"), exampleComText);
    }
}