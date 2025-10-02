package Model;

public class Word {
    int WordID = 0;
    String WordString;
    int Category;
    String Bucket;
    int letterCount;

    public Word(int wordIdFromTextFileLine, String wordFromTextFileLine, int bucketIdFromTextFileLine) {
    }

    public void addNewWord(String word, int category, String bucket) {
        WordID++;
        WordString = word;
        Category = category;
        Bucket = bucket;
        letterCount = word.length();
    }
}
