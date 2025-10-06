package Model;

public class Word {
    int WordID = 0;
    String WordString;
    int Category;
    int bucketId;
    int letterCount;

    public Word(int wordIdFromTextFileLine, String wordFromTextFileLine, int bucketIdFromTextFileLine) {
        this.WordID = wordIdFromTextFileLine;
        this.WordString = wordFromTextFileLine;
        this.bucketId = bucketIdFromTextFileLine;
    }

    public void addNewWord(String word, int category, String bucket) {
        WordID++;
        WordString = word;
        Category = category;
        letterCount = word.length();
    }

    public int getBucketID() {
        return this.bucketId;
    }
}
