package Model;

public class Round {
    public Round(Game game) {
    }

    public int getScore() {
        return 0;//dummy
    }

    public int getNoCorrectWords() {
        return 0;//dummy
    }

    public int getNoIncorrectWords() {
        return 0;//dummy
    }

    public int getNoWordsDisplayed() {
        return 0;//dummy
    }

    public void setScore(int score) {
    }

    public void addToScore(int addToScore) {
    }

    public void setNoCorrectWords(int noCorrectWords) {
    }

    public void setNoIncorrectWords(int noIncorrectWords) {

    }


    public void setNoWordsDisplayed(int noWordsDisplayed) {
    }


    public int extractWordID(String textFileLine) {
        return 0;//dummy value
    }

    public String extractWord(String textFileLine) {
        return "";//dummy value
    }

    public int extractBucketIDForWord(String textFileLine) {
        return 0; //dummy value
    }

    public boolean isWordForGame(String textFileLine) {
        return true;//dummy value
    }
}
