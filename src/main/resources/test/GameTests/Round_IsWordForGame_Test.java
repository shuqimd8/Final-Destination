package GameTests;

import Model.Game;

import java.io.File;

public class Round_IsWordForGame_Test {
    //define variable to create game parent class
    public static final File BUCKET_FILE = new File("BucketDB.txt");
    public static final File WORD_FILE = new File("WordDB.txt");
    public static final int GAME_ID = 1;
    public static final String GAME_NAME = "NATURE";
    //establish parent class
    public static final Game game = new Game(GAME_ID,GAME_NAME,BUCKET_FILE, WORD_FILE);

    //+ isWordForGame(String): boolean
    //Word belongs to game
    //word does not belong to game

    //+ isBucketInGame(bucketID): boolean
}
