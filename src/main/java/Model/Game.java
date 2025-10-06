package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Model.GameSystem.turnFileToListOfLines;

public class Game {
    private int gameID;
    private String gameName;
    private File bucketFile;
    private File wordFile;
    private List<Bucket> buckets;

    /**
     * Constructor for the Game class. Will create bucket objects for the game on creation.
     * @param gameId int identifying number for the game
     * @param gameName string name of the game
     * @param bucketFile txt file with all the bucket information for the whole system, each line will follow the format 'BucketID-BucketName-GameID<<bucketImagePath>>'
     * @param wordFile txt file with all the words used by the whole system, each line will follow the format 'WordID-Word-BucketID'
     */
    public Game(int gameId, String gameName, File bucketFile, File wordFile) {
        this.gameID = gameId;
        this.bucketFile = bucketFile;
        this.gameName = gameName;
        this.wordFile = wordFile;
        this.buckets = new ArrayList<>();
        createBuckets();
        System.out.println(getGameName()+" created with the following buckets: "+getBuckets());
    }


    /**
     * Getter for GameName method
     * @return the name of the game
     */
    public String getGameName() {
        return gameName;//dummy value
    }

    /**
     * Getter for wordFile method
     * @return file with word information
     */
    public File getWordFile() {
        return wordFile; //dummy value
    }

    /**
     * Getter for bucketFile
     * @return will return the File with all the bucket information
     */
    public File getBucketFile() {
        return this.bucketFile;
    }

    /**
     * Getter for gameID
     * @return will return integer game identifier for this game
     */
    public int getGameID() {
        return this.gameID;
    }

    /**
     * Getter method for the BucketList
     * @return List of the buckets in the game
     */
    public List<Bucket> getBuckets() {
        return Collections.unmodifiableList(this.buckets);
    }


    /**
     * Called by the constructor:
     * Will go through each line in the bucketFile and create all the buckets that belong to this game (have matching gameIDs).
     */
    private void createBuckets(){
        List<String> bucketFileLines = turnFileToListOfLines(bucketFile);
        for(String textfileline:bucketFileLines){
            //get the game ID from the file line
            int bucketGameID = extractGameID(textfileline);
            //check that it matches this gameID
            if(bucketGameID == gameID){
                //does belong to game
                //create the bucket and add it to the game
                Bucket bucket = createBucket(textfileline);
                //add to the list of buckets for the game
                buckets.add(bucket);
            }
        }
    }

    /**
     * Method will create a Bucket object for a given line in the bucketFile txt
     * @param textFileLine text file line from the bucketFile file, as a string
     * @return instance of the Bucket object from the text file line
     */
    public Bucket createBucket(String textFileLine) {
        int bucketID = extractBucketID(textFileLine);
        String bucketName = extractBucketName(textFileLine);
        String bucketImagepath = extractBucketImagePath(textFileLine);
        int bucketGameID = extractGameID(textFileLine);

        Bucket bucket = new Bucket(bucketID, bucketName, bucketGameID, bucketImagepath);
        return bucket;
    }

    /**
     * returns the position of the next '-' in the line from the given starting index
     * @param startPosition
     * @param textFileLine
     * @return
     */
    public int positionOfNextBreak(int startPosition, String textFileLine){
        final String breakCharacter = "-";
        int nextBreak = textFileLine.indexOf(breakCharacter,startPosition);
        return nextBreak;
    }

    /**
     * Given a line from the bucketFile the method will return a list of indexes of each break:'-' in the segment
     * @param textFileLine a line from the bucketFile File as a string, which will have the format 'BucketID-BucketName-GameID<<bucketImagePath>>'
     * @return list of each '-' in the textfileLine
     */
    private int[] positionOfBreaks(String textFileLine){
        final String breakCharacter ="-";
        //there are two breaks the first break is after BucketID, so the start position will be 0
        int startPosition = 0;
        //find the first break
        int firstBreak = textFileLine.indexOf(breakCharacter, startPosition);
        //add this to the list of breaks
        int[] breakList = new int[4];
        breakList[0] = firstBreak;
        //the next starting position to find the next break will be the first break + 1
        startPosition += (firstBreak+1);
        //find the next break character
        int secondBreak = textFileLine.indexOf(breakCharacter, startPosition);
        breakList[1] = secondBreak;

        //define what the starting character is
        final String startCharacter = "<<";
        //define the ending character
        final String endCharacter = ">>";
        //get the index for the starting character of the imagepath in the textfile string
        int startOfImagePath = textFileLine.indexOf(startCharacter);
        //get the index for the ending character of the imagepath in the textfile string
        int endOfImagePath = textFileLine.indexOf(endCharacter);

        //add these to the list
        breakList[2] = startOfImagePath;
        breakList[3] = endOfImagePath;

        return breakList;
    }
    /**
     * Given a string from the bucketFile it will return the BucketID
     * @param textFileLine a line from the bucketFile File as a string, which will have the format 'BucketID-BucketName-GameID<<bucketImagePath>>'
     * @return int value of 'BucketID' from the text file line
     */
    public int extractBucketID(String textFileLine) {
        //get the position of the next break
        int nextBreakPosition = positionOfNextBreak(0,textFileLine);
        String bucketIdSegment = textFileLine.substring(0,nextBreakPosition);
        //attempt to turn the bucketID string to an int
        int bucketId_int;
        try {
            bucketId_int = Integer.parseInt(bucketIdSegment);
            //System.out.println("Converted integer using parseInt(): " + bucketId_int);
        } catch (NumberFormatException e) {
            bucketId_int = -1;
            System.out.println("GAME_extractBucketID - Invalid number format: " + e.getMessage());
        }
        return bucketId_int;//dummy value
    }

    /**
     * Given a string from the bucketFile it will return the BucketName
     * @param textFileLine a line from the bucketFile File as a string, which will have the format 'BucketID-BucketName-GameID<<bucketImagePath>>'
     * @return string value of 'BucketName' from the text file line
     */
    public String extractBucketName(String textFileLine) {
        //get character position in string for the start of the Bucket name: the first '-' in the file
        // + 1 to it so that it doesn't include the first break character later
        int startOfBucketName = positionOfNextBreak(0, textFileLine) + 1;
        //get character position in string for end of bucket name: next '-' after the first one
        //have to do startOfBucketName+1 so it doesn't include the first character
        int endOfBucketName = positionOfNextBreak((startOfBucketName), textFileLine);

        //get the bucket name
        String bucketNameSegment = textFileLine.substring(startOfBucketName,endOfBucketName);
        return bucketNameSegment;//dummy value
    }

    /**
     * Given a string from the bucketFile it will return the BucketImagePath
     * @param textFileLine a line from the bucketFile File as a string, which will have the format 'BucketID-BucketName-GameID<<bucketImagePath>>'
     * @return string value of 'BucketImagePath' from the text file line
     */
    public String extractBucketImagePath(String textFileLine) {
        //define what the starting character is
        final String startCharacter = "<<";
        //define the ending character
        final String endCharacter = ">>";
        //get the index for the starting character of the imagepath in the textfile string, then add the length of the starting characters so that it doesn't shoe the <<
        int startOfImagePath = textFileLine.indexOf(startCharacter) + startCharacter.length();
        //get the index for the ending character of the imagepath in the textfile string
        int endOfImagePath = textFileLine.indexOf(endCharacter);

        //grab the imagepath from the text file line
        String bucketPathSegment = textFileLine.substring(startOfImagePath, endOfImagePath);

        return bucketPathSegment;
    }

    /**
     * Given a string from the bucketFile it will return the GameID
     * @param textFileLine a line from the bucketFile File as a string, which will have the format 'BucketID-BucketName-GameID<<bucketImagePath>>'
     * @return int value of 'GameID' from the text file line
     */
    public int extractGameID(String textFileLine) {
        //get the list of break points in the textfile
        int[] breakpoints = positionOfBreaks(textFileLine);
        //GameID will be between the second breakpoint and the third, then plus one so it skips the break character
        int startOfGameID = breakpoints[1]+1;
        int endOfGameID = breakpoints[2];

        String gameIDSegment = textFileLine.substring(startOfGameID, endOfGameID);

        //attempt to turn the gameID string to an int
        int gameId_int;
        try {
            gameId_int = Integer.parseInt(gameIDSegment);
            //System.out.println("Converted integer using parseInt(): " + gameId_int);
        } catch (NumberFormatException e) {
            gameId_int = -1;
            System.out.println("GAME_extractGameID Invalid number format: " + e.getMessage());
        }
        //return int
        return gameId_int;
    }


    /**
     * Given a bucketID the method will return whether the bucket belongs to this game
     * @param bucketId int bucketId identifier for the bucket
     * @return True: the bucket does belong to this game False: bucket does not belong to this game
     */
    public boolean isBucketInGame(int bucketId) {
        boolean inGame = false;
        //go through each bucket in game
        for(Bucket bucket:this.getBuckets()){
            //get the bucketId
            int listBucketID = bucket.getBucketID();
            //if equals the given bucket id then set in game to true
            if(listBucketID==bucketId){
                inGame=true;
            }
        }
        return inGame;//dummy value
    }

    @Override
    public String toString(){
        return "(gameName: "+this.gameName+", gameID: "+this.gameID+")";
    }


}
