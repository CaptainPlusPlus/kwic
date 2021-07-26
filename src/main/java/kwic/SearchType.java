package kwic;

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
