package Model;

import java.io.File;

public class Game {
    private int gameID;
    private File bucketFile;
    private Bucket[] buckets = {};

    public Game(int gameId, File bucketFile) {
        this.gameID = gameId;
        this.bucketFile = bucketFile;
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

    public void createBuckets(){
        //logic
        //for each line in text file get line:


    }

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

    // returns the position of the next '-' in the line from the starting index given
    public int positionOfNextBreak(int startPosition, String textFileLine){
        final String breakCharacter = "-";
        int nextBreak = textFileLine.indexOf(breakCharacter,startPosition);
        return nextBreak;
    }

    public String extractBucketName(String textFileLine) {
        return "";//dummy value
    }

    public String extractBucketImagePath(String textFileLine) {
        return "";//dummy value
    }
}
