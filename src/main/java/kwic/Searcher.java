package kwic;
import java.util.*;
public class Searcher {
    /**
     * @param txt
     * @param type
     * @param needle
     * @param preWords
     * @param postWords
     * @return indexes of words to highlight, empty list if nothing found
     */

        static public List<POSResult> search(String txt, SearchType type, String needle, Integer preWords, Integer postWords) {
            List<POSResult> results = new ArrayList<>();
            CorpusBuilder corpusBuilder = new CorpusBuilder(txt);
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

        private static List<POSResult> extractPOSresults(String txt, String needle, Integer preWords, Integer postWords, CorpusBuilder corpus, List<List<String>> corpusTypeSentences) {
            List<POSResult> results = new ArrayList<>();

            int sentenceIndex = 0;
            for (List<String> sentence : corpusTypeSentences) {
                if(sentence.contains(needle)) {
                    int wordIndex = sentence.indexOf(needle);
                    int firstWordIndex = Math.max(wordIndex - preWords, 0);
                    int lastWordIndex = Math.min(wordIndex + postWords + 1, sentence.size() - 1);
                    String pos = corpus.getPosTags().get(sentenceIndex).get(wordIndex);
                    String word = corpus.getTokens().get(sentenceIndex).get(wordIndex);
                    String lemma = corpus.getLemmas().get(sentenceIndex).get(wordIndex);
                    List<String> sentenceTokens = corpus.getTokens().get(sentenceIndex);
                    List<String> subSentence = sentenceTokens.subList(firstWordIndex, lastWordIndex);
                    results.add(new POSResult(subSentence, subSentence.indexOf(word), lemma, pos, word));
                }
                ++sentenceIndex;
            }
            return results;
        }
    }