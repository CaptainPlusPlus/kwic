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

import java.util.List;

/**
 * A wrapper class, single POS result analyzed from text field
 */
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
