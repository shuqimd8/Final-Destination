package Model;

import java.io.File;

public class Game {
    private int gameID;
    private String gameName;
    private File bucketFile;
    private File wordFile;
    private Bucket[] buckets = {};

    /**
     * Constructor for the Game class
     * @param gameId int identifying number for the game
     * @param gameName string name of the game
     * @param bucketFile txt file with all the bucket information for the whole system, each line will follow the format 'BucketID-BucketName-GameID<<bucketImagePath>>'
     * @param wordFile txt file with all the words used by the whole system, each line will follow the format 'WordID-Word-BucketID'
     */
    public Game(int gameId, String gameName, File bucketFile, File wordFile) {
        this.gameID = gameId;
        this.bucketFile = bucketFile;
        this.gameName = gameName;
        createBuckets();
    }

    public File getBucketFile() {
        return this.bucketFile;
    }

    public int getGameID() {
        return this.gameID;
    }

    public Bucket[] getBucketList() {
        return null; //dummy value
    }


    /**
     * Called by the constructor:
     * Will go through each line in the bucketFile and create all the buckets that belong to this game (have matching gameIDs).
     */
    private void createBuckets(){
        //logic
        //for each line in text file get line:


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

        Bucket bucket = new Bucket(bucketID, bucketName, bucketImagepath);
        return bucket;
    }

    // returns the position of the next '-' in the line from the starting index given
    public int positionOfNextBreak(int startPosition, String textFileLine){
        final String breakCharacter = "-";
        int nextBreak = textFileLine.indexOf(breakCharacter,startPosition);
        return nextBreak;
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
            System.out.println("Converted integer using parseInt(): " + bucketId_int);
        } catch (NumberFormatException e) {
            bucketId_int = -1;
            System.out.println("Invalid number format: " + e.getMessage());
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
        return 0; //dummy value
    }



    public String getGameName() {
        return "";//dummy value
    }

    public File getWordFile() {
        return null; //dummy value
    }

    public boolean isBucketInGame(int bucketId) {
        return true;//dummy value
    }
}
