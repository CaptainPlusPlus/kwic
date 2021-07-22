package kwic;

public class POSResult {
    String word;
    String[] sentence;
    final int wordIndex;
    final String lemma;
    final String pos;

    POSResult(String[] sentence, int wordIndex, String lemma, String pos, String word) {
        this.sentence = sentence;
        this.wordIndex = wordIndex;
        this.lemma = lemma;
        this.pos = pos;
        this.word = word;
    }
}
