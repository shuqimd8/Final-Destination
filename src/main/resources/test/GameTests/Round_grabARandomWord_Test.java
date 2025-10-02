package GameTests;

import Model.Game;

import java.io.File;

public class Round_grabARandomWord_Test {
    //define variable to create game parent class
    public static final File BUCKET_FILE = new File("BucketDB.txt");
    public static final File WORD_FILE = new File("WordDB.txt");
    public static final int GAME_ID = 1;
    public static final String GAME_NAME = "NATURE";
    //establish parent class
    public static final Game game = new Game(GAME_ID,GAME_NAME,BUCKET_FILE, WORD_FILE);

    //+ grabARandomWord(): Word


    //+ extractWordID(String TextFileLine):int

    //+ extractWord(String TextFileLine): String
    //+ extractBucketIDForWord(String TextFileLine): int
    //+ extractGameID
    //
    //+ isWordForGame(String): boolean
    //+ isBucketInGame(bucketID): boolean
}
