package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bucket {
    private int bucketID;
    private String bucketName;
    private String imagePath;
    private int gameID;
    private List<Word> wordsInBucket;

    public Bucket(int bucketId, String bucketName, int gameId, String imagePath){
        this.bucketID=bucketId;
        this.bucketName=bucketName;
        this.imagePath = imagePath;
        this.gameID = gameId;
        wordsInBucket = new ArrayList<>();
    }

    public int getBucketID(){
        return this.bucketID;
    }

    public int getGameID(){
        return this.gameID;
    }

    public String getBucketName(){
        return this.bucketName;
    }

    public String imagePath(){
        return this.imagePath;
    }

    /**
     * Gets the words that the user has put into the bucket
     * @return an unmodifiable list of Word objects that have been assigned to the bucket
     */
    public List<Word> getWordsInBucket(){
        return Collections.unmodifiableList(this.wordsInBucket);
    }


    @Override
    public String toString(){
        return ("(bucketID: "+this.bucketID+", bucketName: "+ this.bucketName+", gameID: "+this.gameID+", ImagePath: "+this.imagePath+")");
    }

    /**
     * gets the bucketID of the word and checks whether it matches this bucket
     * @param word Word object being checked
     * @return TRUE: the word belongs in this bucket FALSE: the does not belong to this bucket
     */
    public boolean isWordInCorrectBucket(Word word){
        //get the bucketID for the word
        int wordBucketID = word.getBucketID();
        //check whether the bucket ID of the word matched this ID
        if(wordBucketID==this.bucketID){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Goes through each word in the bucket and counts the words that are correct
     * @return number of words that have been correctly allocated to this bucket
     */
    public int getNoCorrectWords(){
        //create a variable to count the number of correct words
        int noCorrectWords = 0;
        //go through list of words in the bucket
        for(Word word:wordsInBucket){
            //check whether the word belongs to the bucket
            if(isWordInCorrectBucket(word)){
                //word is in the correct bucket so add to the number of correct words
                noCorrectWords +=1;
            }
        }
        return noCorrectWords;
    }

    /**
     * goes through every word in the bucket and determines how many have been incorrectly allocated
     * @return no of incorrect words in the bucket
     */
    public int getNoIncorrectWords(){
        //create a variable to count the number of incorrect words
        int noIncorrectWords = 0;
        //go through list of words in the bucket
        for(Word word:wordsInBucket){
            //check whether the word belongs to the bucket
            if(!isWordInCorrectBucket(word)){
                //word is not in correct bucket so add to the number of correct words
                noIncorrectWords +=1;
            }
        }
        return noIncorrectWords;
    }

    /**
     * resets the list of words in the bucket to an empty list
     */
    public void resetBucket(){
        this.wordsInBucket = new ArrayList<>();
    }

    /**
     * adds the word to the list of words in the bucket
     * @param word the word being added to the bucket
     */
    public void addWordToBucket(Word word) {
        wordsInBucket.add(word);
    }
}
