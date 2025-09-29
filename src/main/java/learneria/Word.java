package learneria;

public class Word {
    int WordID = 0;
    String WordString;
    int Category;
    String Bucket;
    int letterCount;

    public void addNewWord(String word, int category, String bucket) {
        WordID++;
        WordString = word;
        Category = category;
        Bucket = bucket;
        letterCount = word.length();
    }
}
