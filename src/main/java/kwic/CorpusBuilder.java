package kwic;

import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * A class to extract and retain corpus properties from text using OpenNLP library
 */
public class CorpusBuilder {
    //Private variables to hold corpus properties
    private final String text;
    private String[] sentences;
    private List<List<String>> tokens;
    private List<List<String>> posTags;
    private List<List<String>> lemmas;

    /**
     * Create a CorpusBuilder which generates POS tags and Lemmas for text.
     * @param text The text which should be annotated.
     */
    CorpusBuilder(String text) {
        //Init text, then initialize properties based on text (and each other) one by one
        this.text = text;
        initSentences();
        initTokens();
        initPOSTags();
        initLemmas();
    }

    /**
     * Extracts POS tags from corpus text and initialize private variable POS tags
     */
    private void initPOSTags() {
        //Extract POS tagging model from file
        try (InputStream modelIn = getClass().getResourceAsStream(("/models/en-pos-maxent.bin"))) {
            POSModel model = new POSModel(modelIn);
            POSTaggerME tagger = new POSTaggerME(model);
            posTags = new ArrayList<>();

            //For every detected Token, Analyze its POS based on other tokens in the list.
            for (List<String> tokenList : tokens) {
                String[] posTagsArray = new String[tokenList.size()];
                tokenList.toArray(posTagsArray);
                posTags.add(Arrays.asList(tagger.tag(posTagsArray)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts tokens from corpus text and initialize private variable tokens
     */
    private void initTokens() {
        //Extract tokenizer model from file
        try (InputStream modelIn = getClass().getResourceAsStream("/models/en-token.bin")) {
            TokenizerModel model = new TokenizerModel(modelIn);
            Tokenizer tokenizer = new TokenizerME(model);
            tokens = new ArrayList<>();

            //For every detected sentence, split it into tokens and input each into the List of sentence lists
            for (String sentence : sentences) {
               tokens.add(Arrays.asList(tokenizer.tokenize(sentence)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts sentences from corpus text and initialize private variable sentences
     */
    private void initSentences() {
        //Extract sentence separator model from file
        try (InputStream modelIn = getClass().getResourceAsStream("/models/en-sent.bin")) {
            SentenceModel model = new SentenceModel(modelIn);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
            //Input detected sentences into array
            this.sentences = sentenceDetector.sentDetect(this.text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts Lemmas from corpus text and initialize private variable lemmas
     */
    private void initLemmas() {
        //Extract Lemmatizer model from file
        try (InputStream modelIn = getClass().getResourceAsStream(("/models/en-lemmatizer.bin"))) {
            LemmatizerModel model = new LemmatizerModel(modelIn);
            LemmatizerME lemmatizer = new LemmatizerME(model);
            lemmas = new ArrayList<>();
            for (int i = 0; i < tokens.size(); i++) {
                List<String> sentenceTokens = tokens.get(i);
                List<String> tmpPos = posTags.get(i);

                //Create sentence tokens and lemmatize each
                String[] tmpLemmas = lemmatizer.lemmatize(
                        sentenceTokens.toArray(new String[0]),
                        tmpPos.toArray((new String[0])));
                //Add lemmas to array at corresponding sentence
                lemmas.add(Arrays.asList(tmpLemmas));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the text of this CorpusBuilder
     * @return The text of this CorpusBuilder
     */
    public String getText() {
        return text;
    }

    /**
     * Return an array with the sentences of the CorpusBuilder
     * @return An array with the sentences of the CorpusBuildr
     */
    public String[] getSentences() {
        return sentences;
    }

    /**
     * Return a List of List with the tokens/words of the text of CorpusBuilder. The first list holds the words of the
     * first sentence, the second list holds the words of the second sentence and so on.
     * @return A List of List the tokens/words of the text of the CorpusBuilder.
     */
    public List<List<String>> getTokens() {
        return tokens;
    }

    /**
     * Return a List of List with the POS tags of the text of CorpusBuilder. The first list holds the POS tags of the
     * first sentence, the second list holds the POS tags of the second sentence and so on.
     * @return A List of List with the POS tags of the text of CorpusBuilder.
     */
    public List<List<String>> getPosTags() {
        return posTags;
    }

    /**
     * Return a List of List with the Lemmas of the text of CorpusBuilder. The first list holds the lemmas of the
     * first sentence, the second list holds the Lemmas of the second sentence and so on.
     * @return A List of List with the Lemmas of the text of CorpusBuilder.
     * @return
     */
    public List<List<String>> getLemmas() {
        return lemmas;
    }
}
