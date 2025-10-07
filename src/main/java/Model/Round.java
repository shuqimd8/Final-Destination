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

    public static final int scoreModifier = 5;

    /**
     * Constructor for Round class. Starts the round by setting all the game stats to 0, and generating the first word.
     * @param game game class parent of Round
     */
    public Round(Game game){
        this.game = game;
        this.wordFile = game.getWordFile();
        //score, noCorrectWords, and noWordsDisplayed starts at 0
        this.setScore(0);
        this.setNoCorrectWords(0);
        this.setNoWordsDisplayed(0);

        //reset all the buckets
        resetBuckets();

        //set starting word to random word
        DisplayRandomWord();

    }

    /**
     * Displays a random word
     */
    public void DisplayRandomWord(){
        setCurrentWordDisplayed(generateRandomWord());
    }

    /**
     * Getter for currentWordDisplayed
     * @return current word being displayed on the screen
     */
    public Word getCurrentWordDisplayed(){
        return this.currentWordDisplayed;
    }

    /**
     * setter for current word displayed.
     * @param word the word being displayed
     */
    public void setCurrentWordDisplayed(Word word){
        //add to no words displayed
        noWordsDisplayed += 1;
        this.currentWordDisplayed = word;
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
        int newScore = this.score + addToScore;
        setScore(newScore);
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
        //get the position of the first break point '-'
        int endWordID = textFileLine.indexOf('-');
        //create a substring with just the wordID
        String wordIDSegment = textFileLine.substring(0,endWordID);
        //covert the string to Int
        int wordId_int;
        try {
            wordId_int = Integer.parseInt(wordIDSegment);
            //System.out.println("Converted integer using parseInt(): " + gameId_int);
        } catch (NumberFormatException e) {
            wordId_int = -1;
            System.out.println("ROUND_extractWordID Invalid number format: " + e.getMessage());
        }

        return wordId_int;//dummy value
    }
    /**
     * Given a line from the WordFile text file, the method will return the Word.
     * @param textFileLine line from wordFile as a string, which will follow the format 'WordID-Word-BucketID'
     * @return 'Word' in the line as a string
     */
    public String extractWord(String textFileLine) {
        //get the start position of the first break point '-'
        int startWord = textFileLine.indexOf('-')+1;
        //get the second break
        int endWord = textFileLine.indexOf('-',startWord);

        //create substring with just the word
        String word = textFileLine.substring(startWord,endWord);

        return word;
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
    public Word generateRandomWord(){
        Word randomWord = null;
        //get list of lines from word file
        List<String> wordFileLines = turnFileToListOfLines(this.wordFile);
        //check if there are words available for the game
        if(!areThereWordsAvailableForTheGame()){
            //no words for this game are available throw error
            System.out.println("GENERATE RANDOM WORD ERROR: There are no words available for this game in the word file");
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
            int randomInt = random.nextInt(lenWordList);
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
    public boolean areThereWordsAvailableForTheGame(){
        //get list of lines from the words file
        List<String> wordFileLines = turnFileToListOfLines(this.wordFile);
        //set word available to false
        boolean wordAvailable = false;
        //go through each line and grab the bucket id, check if the word is for the game
        for(String line:wordFileLines){
            if(isWordForGame(line)){
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
        //get the start position of the first break point '-'
        int firstBreak = textFileLine.indexOf('-')+1;
        //get the second break
        int secondBreak = textFileLine.indexOf('-',firstBreak)+1;

        //create substring for bucketId
        String bucketIDSegment = textFileLine.substring(secondBreak);

        //turn into int
        int bucketId_int;
        try {
            bucketId_int = Integer.parseInt(bucketIDSegment);
            //System.out.println("Converted integer using parseInt(): " + gameId_int);
        } catch (NumberFormatException e) {
            bucketId_int = -1;
            System.out.println("ROUND_extractBucketID Invalid number format: " + e.getMessage());
        }

        return bucketId_int;
    }

    /**
     * Given a line from the WordFile text file, the method will grab relevant information and determine whether the word in the line belongs to the game
     * @param textFileLine line from wordFile as a string, which will follow the format 'WordID-Word-BucketID'
     * @return TRUE: word is for the game, FALSE: word is not for the game
     */
    public boolean isWordForGame(String textFileLine) {
        boolean wordIsForGame;
        //get bucket
        int bucketId = extractBucketIDForWord(textFileLine);
        //check whether the bucket is in the game
        boolean bucketIsInGame = game.isBucketInGame(bucketId);
        //if the bucket allocated to the word is in this game then the word is for this game.
        wordIsForGame = bucketIsInGame;
        return wordIsForGame;
    }

    /**
     * Updates all the round variables to the up-to-date variables
     */
    public void updateRound(){
        setScore(calculateScore());
        updateNoCorrectWords();
        updateNoIncorrectWords();
    }

    /**
     * Grabs the updated number of correctly allocated words from all the buckets and updates the noCorrectWords variable accordingly
     */
    public void updateNoCorrectWords(){
        //get the current number of correct words in all the buckets
        int noCorrectWords = findNoCorrectWords();
        //set noCorrectWord
        setNoCorrectWords(noCorrectWords);
    }

    /**
     * Grabs the updated number of correctly allocated words from all the buckets and updates the noCorrectWords variable accordingly
     */
    public void updateNoIncorrectWords(){
        //get the current number of incorrect words in all the buckets
        int noIncorrectWords = findNoIncorrectWords();
        //set noIncorrectWord
        setNoCorrectWords(noIncorrectWords);
    }

    /**
     * Will go through each bucket in the game and check how many words have been correctly allocated
     * @return int number of words correctly allocated
     */
    public int findNoCorrectWords(){
        //create a variable to count the number of correct words
        int noCorrectWords = 0;
        //get all the buckets
        List<Bucket> buckets = this.game.getBuckets();
        //for each bucket get the number of correct words in the bucket
        for(Bucket bucket:buckets){
            //add the number of correct words in the bucket to the total no of correct words
            noCorrectWords += bucket.getNoCorrectWords();
        }
        return noCorrectWords;
    }
    /**
     * Will go through each bucket in the game and check how many words have been incorrectly allocated
     * @return int number of words incorrectly allocated
     */
    public int findNoIncorrectWords(){
        //create a variable to count the number of Incorrect words
        int noIncorrectWords = 0;
        //get all the buckets
        List<Bucket> buckets = this.game.getBuckets();
        //for each bucket get the number of Incorrect words in the bucket
        for(Bucket bucket:buckets){
            //add the number of correct words in the bucket to the total no of correct words
            noIncorrectWords += bucket.getNoIncorrectWords();
        }
        return noIncorrectWords;
    }


    /**
     * Grabs the number of correct words and applies the score modifier to get the latest score
     * @return users current score for the round
     */
    public int calculateScore(){
        return findNoCorrectWords()*scoreModifier;
    }

    /**
     * THIS FUNCTION WILL LIKELY CHANGE DEPENDING ON THE FXML IMPLEMENTATION:
     * This is the function that will be called when a used drags a word to a bucket. The function assigns the word to a bucket and then changes the word being displayed.
     * @param bucket the bucket object that the user has dragged the word to
     * @param word the word the user is putting in the bucket
     */
    public void putWordInBucket(Bucket bucket, Word word){
        //add the word to the bucket
        bucket.addWordToBucket(word);
        //update the round information
        updateRound();
        //change the word
        DisplayRandomWord();
    }

    /**
     * Goes through every bucket in the game and resets it so that it no longer has words in it
     */
    public void resetBuckets(){
        //get a list of all the buckets
        List<Bucket> buckets = this.game.getBuckets();
        //go through each bucket in the games list of bucket
        for(Bucket bucket:buckets){
            //reset the bucket
            bucket.resetBucket();
        }
    }


}
