package Model;

import java.io.File;

public class Game {
    private int gameID;
    private File bucketFile;

    public Game(int gameId, File bucketFile) {
        this.gameID = gameId;
        this.bucketFile = bucketFile;
    }

    public File getBucketFile() {
        return null; //dummy value
    }

    public int getGameID() {
        return 0; //dummy value
    }

    public Bucket[] getBucketList() {
        return null; //dummy value
    }

    public int extractBucketID(String textFileLine) {
        return 0;//dummy value
    }

    public String extractBucketName(String textFileLine) {
        return "";//dummy value
    }

    public String extractBucketImagePath(String textFileLine) {
        return "";//dummy value
    }
}
