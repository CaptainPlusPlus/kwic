package kwic;

import java.util.List;

public class POSResult {
    String word;
    List<String> sentence;
    final int wordIndex;
    final String lemma;
    final String pos;

    public POSResult(List<String> sentence, int wordIndex, String lemma, String pos, String word) {
        this.sentence = sentence;
        this.wordIndex = wordIndex;
        this.lemma = lemma;
        this.pos = pos;
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public List<String> getSentence() {
        return sentence;
    }

    public int getWordIndex() {
        return wordIndex;
    }

    public String getLemma() {
        return lemma;
    }

    public String getPos() {
        return pos;
    }

}
