package Model;

public class Round {
    private Game game;
    private int score;
    private int noCorrectWords;
    private int noIncorrectWords;
    private int noWordsDisplayed;
    private Word currentWordDisplayed;

    /**
     * Constructor for Round class. Starts the round by setting all the game stats to 0, and generating the first word.
     * @param game game class parent of Round
     */
    public Round(Game game) {
        //score, noCorrectWords, and noWordsDisplayed starts at 0
        this.setScore(0);
        this.setNoCorrectWords(0);
        this.setNoWordsDisplayed(0);
    }

    /**
     * Getter for score variable.
     * @return the students current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for NoCorrectWords
     * @return number words the student has correctly put in buckets
     */
    public int getNoCorrectWords() {
        return noCorrectWords;
    }

    /**
     * Getter for NoIncorrectWords. Gets the number words that the student has put in the wrong bucket
     * @return int NoIncorrectWords
     */
    public int getNoIncorrectWords() {
        return noIncorrectWords;
    }

    /**
     * Getter for noWordsDisplayed. Gets the Number of words that have showed up on the screen during the round.
     * @return int NoWordsDisplayed
     */
    public int getNoWordsDisplayed() {
        return noWordsDisplayed;
    }

    /**
     * Setter for the score. Sets the current score for the round.
     * @param score new student score
     */
    public void setScore(int score) {
    }

    /**
     * Increments the students current score by a set value
     * @param addToScore integer value to increase the score by
     */
    public void addToScore(int addToScore) {
    }

    /**
     * Setter for noCorrectWords. Sets the value for the number of words the student has correctly sorted.
     * @param noCorrectWords new value for the correct number of words the student has sorted
     */
    public void setNoCorrectWords(int noCorrectWords) {
    }
    /**
     * Setter for noIncorrectWords. Sets the value for the number of words the student has incorrectly sorted.
     * @param noIncorrectWords new value for the incorrect number of words the student has sorted
     */
    public void setNoIncorrectWords(int noIncorrectWords) {

    }

    /**
     * Setter for noWordsDisplayed. Sets the value for the total number of words that has been displayed to the student throughout the round.
     * @param noWordsDisplayed total number of words that hgs been shown to the student throughout the round.
     */
    public void setNoWordsDisplayed(int noWordsDisplayed) {
    }

    /**
     * Given a line from the WordFile text file, the method will return the WordID.
     * @param textFileLine line from wordFile as a string, which will follow the format 'WordID-Word-BucketID'
     * @return WordID in the line as an int
     */
    public int extractWordID(String textFileLine) {
        return 0;//dummy value
    }
    /**
     * Given a line from the WordFile text file, the method will return the Word.
     * @param textFileLine line from wordFile as a string, which will follow the format 'WordID-Word-BucketID'
     * @return 'Word' in the line as a string
     */
    public String extractWord(String textFileLine) {
        return "";//dummy value
    }



    /**
     * Method grab a random word from the WordFile and check whether the word is for this game. If it is, it will create an instance of the Word using the word class.If not it will repeat the process until it find a word.
     * This method runs data validation using areThereWordsAvailableForTheGame and if not will throw an error.
     * @return
     */
    public Word generateRandomWord(){
        return null;
    }

    /**
     * This is called by the generateRandomWord class as a form of data validation. It checks every line of the WordFile to ensure there is a valid word availble for the game.
     * @return TRUE: there is a word in the file for this game FALSE: there is not a word in the file for this game
     */
    public boolean areThereWordsAvailbleForTheGame(){
        return false;
    }


    /**
     * Given a line from the WordFile text file, the method will return the BucketID.
     * @param textFileLine line from wordFile as a string, which will follow the format 'WordID-Word-BucketID'
     * @return 'bucketID' in line as int
     */
    public int extractBucketIDForWord(String textFileLine) {
        return 0; //dummy value
    }

    /**
     * Given a line from the WordFile text file, the method will grab relevant information and determine whether the word in the line belongs to the game
     * @param textFileLine line from wordFile as a string, which will follow the format 'WordID-Word-BucketID'
     * @return TRUE: word is for the game, FALSE: word is not for the game
     */
    public boolean isWordForGame(String textFileLine) {
        return true;//dummy value
    }

}
