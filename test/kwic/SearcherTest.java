package kwic;

import junit.framework.TestCase;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class SearcherTest extends TestCase {
    private String text;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Scanner scanner = new Scanner(new File("src/main/resources/50.txt"));
        text = scanner.useDelimiter("\\A").next();
        scanner.close();
    }
    public void testSearch() {
        List<POSResult> result = Searcher.search(text, SearchType.Lemma, "door", 6, 6);
        System.out.println("HOyo");
    }
}