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

/**
 * Enum to represent search type in a dynamic format
 */
public enum SearchType {
    ExactForm {
        @Override
        public String toString() {
            return "word";
        }
    }, Lemma {
        @Override
        public String toString() {
            return "lemma";
        }
    }, EnteringTag {
        @Override
        public String toString() {
            return "pos";
        }
    }
}
