package Model;

import java.io.File;
import java.util.List;
import java.util.Random;

import static Model.GameSystem.turnFileToListOfLines;

public class Round{
    private Game game;
    private File wordFile;
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
        this.game = game;
        this.wordFile = game.getWordFile();
        //score, noCorrectWords, and noWordsDisplayed starts at 0
        this.setScore(0);
        this.setNoCorrectWords(0);
        this.setNoWordsDisplayed(0);

        //set starting word to random word
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
        this.score= score;
    }

    /**
     * Increments the students current score by a set value
     * @param addToScore integer value to increase the score by
     */
    public void addToScore(int addToScore) {
        this.score += addToScore;
    }

    /**
     * Setter for noCorrectWords. Sets the value for the number of words the student has correctly sorted.
     * @param noCorrectWords new value for the correct number of words the student has sorted
     */
    public void setNoCorrectWords(int noCorrectWords) {
        this.noCorrectWords = noCorrectWords;
    }
    /**
     * Setter for noIncorrectWords. Sets the value for the number of words the student has incorrectly sorted.
     * @param noIncorrectWords new value for the incorrect number of words the student has sorted
     */
    public void setNoIncorrectWords(int noIncorrectWords) {
        this.noIncorrectWords = noIncorrectWords;

    }

    /**
     * Setter for noWordsDisplayed. Sets the value for the total number of words that has been displayed to the student throughout the round.
     * @param noWordsDisplayed total number of words that hgs been shown to the student throughout the round.
     */
    public void setNoWordsDisplayed(int noWordsDisplayed) {
        this.noWordsDisplayed = noWordsDisplayed;
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
     * given a line from the wordFile as a string it will create an instance of the word
     * @param textFileLine line from WordFile as a string, will follow the format 'WordID-Word-BucketID'
     * @return Word object created from the line
     */
    public Word createWord(String textFileLine){
        String word = extractWord(textFileLine);
        int wordId = extractWordID(textFileLine);
        int bucketId = extractBucketIDForWord(textFileLine);
        return new Word(wordId, word,bucketId);
    }



    /**
     * Method grab a random word from the WordFile and check whether the word is for this game. If it is, it will create an instance of the Word using the word class.If not it will repeat the process until it find a word.
     * This method runs data validation using areThereWordsAvailableForTheGame and if not will throw an error.
     * @return
     */
    public Word generateRandomWord() throws Exception {
        Word randomWord = null;
        //get list of lines from word file
        List<String> wordFileLines = turnFileToListOfLines(this.wordFile);
        //check if there are words available for the game
        if(!areThereWordsAvailbleForTheGame()){
            //no words for this game are available throw error
            throw new Exception("There are no words availble for this game in the word file");
        }
        //continue
        //get length of list
        int lenWordList = wordFileLines.size();
        //create an instance of the random class
        Random random = new Random();

        boolean wordFound = false;
        //create a while loop to run until a word is found.
        //while no word is found generate a random word from the list and check whether to word is for the game and if so then use that word and mark wordFound as true
        while (!wordFound){
            int randomInt = random.nextInt(lenWordList+1);
            //get the text file line
            String text_file_line = wordFileLines.get(randomInt);
            //check whether the word in this line belongs to the game
            if(isWordForGame(text_file_line)){
                //word is in game so create the word and set it as the random word
                randomWord = createWord(text_file_line);
                //mark word as found
                wordFound = true;
            }
        }
        //word has been generated. return word.
        return randomWord;
    }

    /**
     * This is called by the generateRandomWord class as a form of data validation. It checks every line of the WordFile to ensure there is a valid word availble for the game.
     * @return TRUE: there is a word in the file for this game FALSE: there is not a word in the file for this game
     */
    public boolean areThereWordsAvailbleForTheGame(){
        //get list of lines from the word file
        List<String> wordFileLines = turnFileToListOfLines(this.wordFile);
        //set word available to false
        boolean wordAvailable = false;
        //go through each line and grab the bucket id, check if the bucket id is in the game
        for(String line:wordFileLines){
            //get the bucket id for the word in the line
            int bucketId = extractBucketIDForWord(line);
            //check whether the bucket is in the game
            boolean bucketIsInGame = game.isBucketInGame(bucketId);
            if(bucketIsInGame){
                //set word available to true
                wordAvailable = true;
            }
            //continue to next word
        }
        return wordAvailable;
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
