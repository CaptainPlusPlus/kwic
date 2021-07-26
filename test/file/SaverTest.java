package file;

import junit.framework.TestCase;
import kwic.POSResult;
import kwic.SearchType;
import java.util.Arrays;
import java.util.List;

public class SaverTest extends TestCase {
    private POSResult mockResult;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        List<String> sentence = Arrays.asList("succumb", "is", "buhhh");
        mockResult = new POSResult(sentence, 1, "be", "NN", "is");
    }

    public void testSingleXMLElement() {
        List<POSResult> posResults = Arrays.asList(mockResult);
        Saver saver = new Saver("www.internet.com", "is", SearchType.ExactForm, posResults);

        saver.save();
    }
    public void testMultipleXMLElements() {
        List<POSResult> posResults = Arrays.asList(mockResult, mockResult);
        Saver saver = new Saver("www.internet.com", "succumb", SearchType.ExactForm, posResults);

        saver.save();
    }
}