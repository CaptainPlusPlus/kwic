/**
 * Course:      Data Structures and Algorithms for Computational Linguistics II SS2021
 * Assignment:  (Final KWIC Project)
 * Authors:      (Hoyeon Lee, Nkonye Gbadegoye, Dewi Surya, Jacqueline Becker, Felix Redfox)
 * Description: (Project to extract text from url and find/save needle typed results on it with their POS information)
 *
 * Honor Code:  We pledge that this program represents our own work.
 *  we did not receive help from anyone in designing and debugging this program.
 */

package kwic;
import java.util.*;
public class Searcher {

/**
 * @param txt text to execute search query over
 * @param type type of corpus search to execute
 * @param needle the word on which typed search should take place in corpus
 * @param preWords Amount of words before needle to fetch
 * @param postWords Amount of words after needle to fetch
 * @return indexes of words to highlight, empty list if nothing found
 */
static public List<POSResult> search(String txt, SearchType type, String needle, Integer preWords, Integer postWords) {
    List<POSResult> results = new ArrayList<>();
    CorpusBuilder corpusBuilder = new CorpusBuilder(txt);

    //Call extraction with the correct typed operation
    switch (type) {
        case Lemma:
            results = extractPOSresults(txt, needle, preWords, postWords, corpusBuilder, corpusBuilder.getLemmas());
            break;
        case ExactForm:
            results = extractPOSresults(txt, needle, preWords, postWords, corpusBuilder, corpusBuilder.getTokens());
            break;
        case EnteringTag:
            results = extractPOSresults(txt, needle, preWords, postWords, corpusBuilder, corpusBuilder.getPosTags());
            break;
    }
    return results;
}

    /**
     * Extracts the results over required needle from processed text
     * @param txt text to execute search query over
     * @param needle the word on which typed search should take place in corpus
     * @param preWords Amount of words before needle to fetch
     * @param postWords Amount of words after needle to fetch
     * @return indexes of words to highlight, empty list if nothing found
     */
    private static List<POSResult> extractPOSresults(String txt, String needle, Integer preWords, Integer postWords, CorpusBuilder corpus, List<List<String>> corpusTypeSentences) {
    List<POSResult> results = new ArrayList<>();

    //For every sentence in corpus
    int sentenceIndex = 0;
    for (List<String> sentence : corpusTypeSentences) {
        //If needle is found in the sentence
        if(sentence.contains(needle)) {
            //Init indices of word in found sentence and subsentence location
            int wordIndex = sentence.indexOf(needle);
            int firstWordIndex = Math.max(wordIndex - preWords, 0);
            int lastWordIndex = Math.min(wordIndex + postWords + 1, sentence.size() - 1);

            //Init NLP extracted for found needle
            String pos = corpus.getPosTags().get(sentenceIndex).get(wordIndex);
            String word = corpus.getTokens().get(sentenceIndex).get(wordIndex);
            String lemma = corpus.getLemmas().get(sentenceIndex).get(wordIndex);

            // Modify sentence to encapsulate result
            List<String> sentenceTokens = corpus.getTokens().get(sentenceIndex);
            List<String> subSentence = sentenceTokens.subList(firstWordIndex, lastWordIndex);

            //Create a POS result of the found instance
            results.add(new POSResult(subSentence, subSentence.indexOf(word), lemma, pos, word));
        }
        ++sentenceIndex;
    }
    return results;
}
}